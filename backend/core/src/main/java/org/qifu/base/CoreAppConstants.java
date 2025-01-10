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
package org.qifu.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.qifu.util.LoadResources;

public class CoreAppConstants {
	
	public static final String SYS_PAGE_LOGIN = "/loginPage";
	public static final String SYS_PAGE_TAB_LOGIN_AGAIN = "/loginAgainPage";
	public static final String SYS_PAGE_NO_AUTH = "/noAuthPage";
	public static final String SYS_BLANK_ROLE = "QIFU_BLANK_USERROLE";
	
	// ======================================================================================
	
	private static Map<String, Object> excludePathPatternsMap = null;
	
	private static String[] excludePathPatternsArray = null;
	
	protected CoreAppConstants() {
		throw new IllegalStateException("Constants class: CoreAppConstants");
	}
	
	static {
		try {
			excludePathPatternsMap = LoadResources.objectMapperReadValue("excludePathPatterns.json", HashMap.class, CoreAppConstants.class);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null==excludePathPatternsMap) {
				excludePathPatternsMap = new HashMap<>();
			}
		}
	}
	
	protected static final String [] WebConfigResource = {
			"/webjars/**",
			"swagger-ui.html",
            "/**" // frontend static html/js resource
	};
	
	protected static final String[] WebConfigResourceLocations = {
            "classpath:/META-INF/resources/webjars/",
            "classpath:/META-INF/resources/",
            "classpath:/static/" // frontend static html/js/image resource
	};
	
	protected static final String [] WebConfigInterceptorExcludePathPatterns = {
			"/ui5/**", 
			"/webjars/**", 
			"/bootbox/**", 
			"/bootstrap-vali/**", 
			"/css/**", 
			"/font-awesome/**", 
			"/icons/**", 
			"/images/**",
			"/jquery/**", 
			"/js/**", 
			"/codemirror/**",
			"/popper-js/**", 
			"/tether/**", 
			"/toastr/**",
			"/error**",
			"/swagger-resources/**",
			"/swagger-ui.html",
			"/swagger-ui.html/**",
			"/swagger**",
			"/swagger-ui**", // v3 swagger
			"/swagger-ui/**", // v3 swagger			
			"/**/v3/api-docs**", // api-docs
			"/**/v3/api-docs/**", // api-docs 
			"/v2**", //"/api-docs**",
			"/v2/**", //"/api-docs/**",
			"/springfox.js",
			"/springfox.js/**",
			"/csrf",
			"/csrf/**",
			"/configJs.js",
			
			/* for ControllerAuthorityCheckInterceptor no to check */
			SYS_PAGE_TAB_LOGIN_AGAIN,
			"/login",
			SYS_PAGE_LOGIN,
			"/logout",
			"/error",
			SYS_PAGE_NO_AUTH
			
	};	
	
	public static String[] getWebconfigresource() {
		return WebConfigResource;
	}

	public static String[] getWebconfigresourcelocations() {
		return WebConfigResourceLocations;
	}

	public static String[] getWebconfiginterceptorexcludepathpatternsVariable() {
		return WebConfigInterceptorExcludePathPatterns;
	}

	public static String[] getWebConfiginterceptorExcludePathPatterns() {
		if (excludePathPatternsArray != null) {
			return excludePathPatternsArray;
		}
		@SuppressWarnings("unchecked")
		List<String> excludePathPatterns = (List<String>) excludePathPatternsMap.get("excludePathPatterns");
		if ( null == excludePathPatterns ) {
			excludePathPatterns = new ArrayList<>();
		}
		Object[] arr = Stream.concat( Arrays.stream(WebConfigInterceptorExcludePathPatterns), Arrays.stream( excludePathPatterns.toArray()) ).toArray();
		excludePathPatternsArray = Arrays.copyOf(arr, arr.length, String[].class);
		return excludePathPatternsArray;
	}	
	
	// ======================================================================================
	
}
