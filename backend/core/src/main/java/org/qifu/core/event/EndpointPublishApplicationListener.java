package org.qifu.core.event;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.qifu.util.LoadResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class EndpointPublishApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	Bus bus;	
	
	private Object getBean(ContextRefreshedEvent event, String beanId) {
		Object bean = null;
		try {
			bean = event.getApplicationContext().getBean(beanId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	private void publishCxfEndpoint(ContextRefreshedEvent event) throws Exception {
		Map<String, Object> configMap = LoadResources.objectMapperReadValue("endpoint-publish.json", Map.class, EndpointPublishApplicationListener.class);
		if (MapUtils.isEmpty(configMap)) {
			return;
		}
		List<Map<String, Object>> endpointsList = (List<Map<String, Object>>) configMap.get("endpoints");
		if (CollectionUtils.isEmpty(endpointsList)) {
			return;
		}
		for (Map<String, Object> endpointMap : endpointsList) {
			String name = StringUtils.defaultString((String)endpointMap.get("name"));
			String beanId = StringUtils.defaultString((String)endpointMap.get("beanId"));
			String publish = StringUtils.defaultString((String)endpointMap.get("publish"));
			boolean enable = (boolean) endpointMap.get("enable");
			if (!enable) {
				continue;
			}
			if (StringUtils.isBlank(beanId) || StringUtils.isBlank(name) || StringUtils.isBlank(publish)) {
				continue;
			}
			if (!publish.startsWith("/") || publish.length() < 2) {
				continue;
			}
			Object serviceBean = null;
			if ( (serviceBean = this.getBean(event, beanId)) == null ) {
				continue;
			}
			EndpointImpl endpoint = new EndpointImpl(bus, serviceBean);
			List<String> inInterceptors = (List<String>) endpointMap.get("inInterceptors");
			List<String> outInterceptors = (List<String>) endpointMap.get("outInterceptors");
			if (!CollectionUtils.isEmpty(inInterceptors)) {
				for (String interceptorBeanId : inInterceptors) {
					Object interceptorBean = this.getBean(event, interceptorBeanId);
					if (interceptorBean != null && interceptorBean instanceof AbstractPhaseInterceptor) {
						endpoint.getInInterceptors().add( (AbstractPhaseInterceptor)interceptorBean );
					}
				}
			}
			if (!CollectionUtils.isEmpty(outInterceptors)) {
				for (String interceptorBeanId : outInterceptors) {
					Object interceptorBean = this.getBean(event, interceptorBeanId);
					if (interceptorBean != null && interceptorBean instanceof AbstractPhaseInterceptor) {
						endpoint.getOutInterceptors().add( (AbstractPhaseInterceptor)interceptorBean );
					}
				}
			}
			endpoint.publish(publish);
		}		
	}
	
	@Override 
    public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			this.publishCxfEndpoint(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}