/* 
 * Copyright 2021-2024 qifu of copyright Chen Xin Nien
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
package org.orrs;

public class OrrsConstants {
	
	public static final int MAX_PROMPT_RECORD = 10;
	
	public static final int MAX_USER_MESSAGE_SIZE = 128000; /** 20000; */
	
	public static final int MAX_COMMAND_RECORD = 10;
	
	public static final String VARIABLE_PREVIOUS_MESSAGE = "$P{previousMessage}";
	
	public static final String VARIABLE_PREVIOUS_INVOKE_RESULT = "$P{previousInvokeResult}";
	
	public static final int MAX_SYSTEM_PROMPT_TEMPLATE_SIZE = 1000;
	
}
