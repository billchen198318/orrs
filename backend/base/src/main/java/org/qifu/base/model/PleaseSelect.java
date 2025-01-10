/* 
 * Copyright 2012-2017 qifu of copyright Chen Xin Nien
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
package org.qifu.base.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.qifu.base.Constants;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PleaseSelect {
	private static String pleaseSelectDatas = " { } ";
	private static Map<String, Object> pleaseSelectMap;
	
	protected PleaseSelect() {
		throw new IllegalStateException("Utility class: PleaseSelect");
	}
	
	static {
		try (InputStream is = Constants.class.getClassLoader().getResource( "please-select-label-name.json" ).openStream()) {
			pleaseSelectDatas = IOUtils.toString(is, Constants.BASE_ENCODING);
			pleaseSelectMap = loadDatas();	
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null==pleaseSelectMap) {
				pleaseSelectMap = new HashMap<>();
			}	
		}
	}
	
	public static boolean isAllOption(String value) {
		return Constants.HTML_SELECT_NO_SELECT_ID.equals(value) ? Boolean.TRUE : Boolean.FALSE;
	}
	
	public static boolean noSelect(String value) {
		return (StringUtils.isBlank(value) || Constants.HTML_SELECT_NO_SELECT_ID.equals(value)) ? Boolean.TRUE : Boolean.FALSE;
	}	
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> loadDatas() {
		Map<String, Object> datas = null;
		try {
			datas = new ObjectMapper().readValue( pleaseSelectDatas, LinkedHashMap.class );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datas;
	}
	
	public static Map<String, Object> getDataMap() {
		return pleaseSelectMap;
	}		

	public static String getLabel(String localeLang) {
		String label = (String) pleaseSelectMap.get(localeLang);
		return (StringUtils.isBlank(label)) ? Constants.HTML_SELECT_NO_SELECT_NAME : label;
	}
	
	public static Map<String, String> pageSelectMap(boolean pleaseSelect) {
		Map<String, String> dataMap = new LinkedHashMap<>();
		if (pleaseSelect) {
			dataMap.put(Constants.HTML_SELECT_NO_SELECT_ID, Constants.HTML_SELECT_NO_SELECT_NAME);
		}
		return dataMap;
	}
	
}
