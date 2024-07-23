/* 
 * Copyright 2019-2023 qifu of copyright Chen Xin Nien
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

import javax.sql.DataSource;

import org.qifu.base.CoreAppConstants;
import org.qifu.base.properties.BaseInfoConfigProperties;
import org.qifu.base.service.impl.BaseUserDetailsService;
import org.qifu.core.support.JwtAuthEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
    @Autowired
    BaseUserDetailsService baseUserDetailsService;
    
    @Autowired
    JwtAuthEntryPoint unauthorizedHandler;    
    
    @Autowired
    DataSource dataSource;
    
    @Autowired
    BaseInfoConfigProperties baseInfoConfigProperties;
    
    @Autowired
    PasswordEncoder passwordEncoder;    
    
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    	return authenticationConfiguration.getAuthenticationManager();
    }    
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        
        authProvider.setUserDetailsService(this.baseUserDetailsService);
        authProvider.setPasswordEncoder(this.passwordEncoder);
        
        return authProvider;
    }    
    
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {   
    	http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable())
    		.sessionManagement( sessMgr -> sessMgr.sessionCreationPolicy(SessionCreationPolicy.STATELESS) )
    		.authorizeHttpRequests(auth -> {
    			auth.requestMatchers(antMatcher("/api/auth/**")).permitAll();
    			for (String par : CoreAppConstants.getWebConfiginterceptorExcludePathPatterns()) {
    				auth.requestMatchers(antMatcher(par)).permitAll();
    			}
    			auth.anyRequest().authenticated();
    		});
		http.exceptionHandling(exeConfig -> {
		    exeConfig.authenticationEntryPoint(this.unauthorizedHandler);
		});
    	return http.build();
    }
    
}
