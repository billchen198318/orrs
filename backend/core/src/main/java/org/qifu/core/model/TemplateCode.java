/* 
 * Copyright 2012-2016 bambooCORE, greenstep of copyright Chen Xin Nien
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
package org.qifu.core.model;

import java.io.IOException;
import java.util.Properties;

import org.qifu.util.SimpleUtils;

/**
 * config properties file in META-INF/template-code-use.properties
 */
public class TemplateCode {
	private static Properties props = new Properties();
	private static String[] useCode = null;
	
	protected TemplateCode() {
		throw new IllegalStateException("static model class: TemplateCode");
	}
	
	static {
		try {
			props.load(TemplateCode.class.getClassLoader().getResource("template-code-use.properties").openStream());
			useCode = SimpleUtils.getStr(props.getProperty("CODE")).trim().split(",");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isUsed(String code) {
		boolean f = false;
		for (int i=0; !f && useCode!=null && i<useCode.length; i++) {
			if (useCode[i].equals(code)) {
				f = true;
			}
		}
		return f;
	}
	
}
