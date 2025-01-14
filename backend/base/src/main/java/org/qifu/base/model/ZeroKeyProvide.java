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

import java.math.BigDecimal;

public class ZeroKeyProvide {
	public static final String STR_KEY = " ";
	public static final int INTEGER_KEY = 0;
	public static final long LONG_KEY = 0L;
	public static final BigDecimal BIG_DECIMAL_KEY = BigDecimal.ZERO;
	public static final String OID_KEY="00000000-0000-0000-0000-000000000000";
	
	private ZeroKeyProvide() {
		throw new IllegalStateException("static model class: ZeroKeyProvide");
	}
}
