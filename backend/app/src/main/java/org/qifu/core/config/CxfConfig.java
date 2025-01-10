package org.qifu.core.config;

import java.util.Arrays;

import org.apache.cxf.Bus;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.qifu.core.webservice.interceptor.SoapForceDoclitBareInInterceptor;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CxfConfig {
	
	@SuppressWarnings("unused")
	private final Bus bus;
	
	private LoggingInInterceptor loggingInInterceptor;
	
	private LoggingOutInterceptor loggingOutInterceptor;
	
	private SoapForceDoclitBareInInterceptor soapForceDoclitBareInInterceptor;
	
	public CxfConfig(Bus bus) {
		super();
		this.bus = bus;
	}	
	
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
		ServletRegistrationBean<CXFServlet> srb = new ServletRegistrationBean<>();
		srb.setServlet(new CXFServlet());
		srb.setUrlMappings(Arrays.asList("/services/*"));
		return srb;
	}	
	
}
