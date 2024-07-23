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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * http://127.0.0.1:8088/swagger-ui/
 * http://127.0.0.1:8088/swagger-ui/index.html
 */
@Configuration
@OpenAPIDefinition
public class SwaggerConfig {
	
	@Bean
	public OpenAPI customOpenAPI() {
		Contact contact = new Contact().name("Bill Chen").email("chen.xin.nien@gmail.com");
		License license = new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0");
		Info info = new Info().title("QiFu4").description("backend interface.").version("0.4").contact(contact).license(license);
		
		return new OpenAPI().info(info);
	}
	
}
