package org.qifu.core.config;

import java.util.Arrays;

import org.apache.cxf.Bus;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.qifu.core.webservice.interceptor.SoapForceDoclitBareInInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CxfConfig {
	
	@Autowired
	Bus bus;
	
	private LoggingInInterceptor loggingInInterceptor;
	
	private LoggingOutInterceptor loggingOutInterceptor;
	
	private SoapForceDoclitBareInInterceptor soapForceDoclitBareInInterceptor;
	
	//@Autowired
	//ITestService testService;
	
	
	@Bean
	public LoggingInInterceptor loggingInInterceptor() {
		if (null == this.loggingInInterceptor) {
			this.loggingInInterceptor = new LoggingInInterceptor();
		}
		return this.loggingInInterceptor;
	}
	
	@Bean
	public LoggingOutInterceptor loggingOutInterceptor() {
		if (null == this.loggingOutInterceptor) {
			this.loggingOutInterceptor = new LoggingOutInterceptor();
		}
		return this.loggingOutInterceptor;
	}	
	
	@Bean
	public SoapForceDoclitBareInInterceptor soapForceDoclitBareInInterceptor() {
		if (null == this.soapForceDoclitBareInInterceptor) {
			this.soapForceDoclitBareInInterceptor = new SoapForceDoclitBareInInterceptor();
		}
		return this.soapForceDoclitBareInInterceptor;
	}		
	
	@Bean
	public ServletRegistrationBean<CXFServlet> cxfServlet() {
		ServletRegistrationBean<CXFServlet> srb = new ServletRegistrationBean<CXFServlet>();
		srb.setServlet(new CXFServlet());
		srb.setUrlMappings(Arrays.asList("/services/*"));
		return srb;
	}	
	
	// change to config endpoint-publish.json file in classpath
	/*
	@Bean
	public Endpoint testEndpoint() {
		EndpointImpl endpoint = new EndpointImpl(bus, this.testService);
		endpoint.publish("/testService");
		endpoint.getInInterceptors().add( this.loggingInInterceptor() );
		endpoint.getOutInterceptors().add( this.loggingOutInterceptor() );
		endpoint.getInInterceptors().add( this.soapForceDoclitBareInInterceptor() );
		return endpoint;
	}	
	*/
	
}
