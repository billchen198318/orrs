/* 
 * Copyright 2019-2021 qifu of copyright Chen Xin Nien
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * -----------------------------------------------------------------------
 * 
 * author: 	Chen Xin Nien
 * contact: chen.xin.nien@gmail.com
 * 
 */
package org.qifu.core.config;

import org.qifu.base.CoreAppConstants;
import org.qifu.core.interceptor.ControllerAuthorityCheckInterceptor;
import org.qifu.core.interceptor.MDC4UserBuildInterceptor;
import org.qifu.core.interceptor.UserBuilderInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	private static final String[] AUTH_PATH = new String[] { 
			"/api/client", "/api/auth", "/api/auth/signin", "/api/auth/validLogined", "/api/auth/refreshNewToken" 
	};
	
	private static final String EVENT_LOG_PATH = "/api/PROG004D0001/**";
	
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
        .addResourceHandler( CoreAppConstants.getWebconfigresource() )
        .addResourceLocations( CoreAppConstants.getWebconfigresourcelocations() );
    }
    
    @Bean 
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }     
    
    @Bean
    MDC4UserBuildInterceptor mdcInterceptor() {
    	return new MDC4UserBuildInterceptor();
    }
    
    @Bean
    ControllerAuthorityCheckInterceptor controllerAuthorityCheckInterceptor() {
    	return new ControllerAuthorityCheckInterceptor();
    }
    
    @Bean
    UserBuilderInterceptor userBuilderInterceptor() {
    	return new UserBuilderInterceptor();
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userBuilderInterceptor())
        	.addPathPatterns("/api/*", "/api/**")
        	.excludePathPatterns( AUTH_PATH );
        
        registry.addInterceptor(mdcInterceptor())
    		.addPathPatterns("/*", "/**")
    		.excludePathPatterns( AUTH_PATH );        
        
        registry.addInterceptor(controllerAuthorityCheckInterceptor())
        	.addPathPatterns("/api/*", "/api/**")
        	.excludePathPatterns( AUTH_PATH ).excludePathPatterns( EVENT_LOG_PATH );
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	registry.addMapping("/**")
    		.allowedOrigins("*")
    		.allowedOriginPatterns("*")
    		.allowedMethods("*")
    		.allowCredentials(false) // header set Access-Control-Allow-Origin : '*' , need set allowCredentials to false
    		.maxAge(3600)
    		.allowedHeaders("*");
    }
    
}
