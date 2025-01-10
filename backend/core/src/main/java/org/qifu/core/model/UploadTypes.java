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

import java.util.Map;

import org.qifu.base.model.PleaseSelect;

public class UploadTypes {
	public static final String IS_IMAGE = "image"; // 圖片
	public static final String IS_TEMP = "tmp"; // 暫存
	public static final String IS_COMMON = "common"; // 共用
	protected static final String[] TYPES = new String[] { IS_IMAGE, IS_TEMP, IS_COMMON };
	
	protected UploadTypes() {
        throw new IllegalStateException("static model class: UploadTypes");
    }
	
	public static Map<String, String> getDataMap(boolean pleaseSelect) {
		Map<String, String> dataMap = PleaseSelect.pageSelectMap(pleaseSelect);
		dataMap.put(IS_TEMP, IS_TEMP);
		dataMap.put(IS_IMAGE, IS_IMAGE);
		dataMap.put(IS_COMMON, IS_COMMON);
		return dataMap;
	}
	
	public static boolean check(String type) {
		boolean f = false;
		for (int i=0; i<UploadTypes.TYPES.length; i++) {
			if (UploadTypes.TYPES[i].equals(type)) {
				f = true;
			}
		}
		return f;
	}	
	
}
