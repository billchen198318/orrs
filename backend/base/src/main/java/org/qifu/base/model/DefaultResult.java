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
package org.qifu.base.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.qifu.base.exception.ServiceException;

public class DefaultResult<T> implements java.io.Serializable {
	private static final long serialVersionUID = 738672416927503320L;
	
	private String isAuth = YesNoKeyProvide.NO;
	
	private String success = YesNoKeyProvide.NO;
	
	private String message = "";
	
	private transient T value = null;

	public String getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(String isAuth) {
		this.isAuth = isAuth;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
	public T getValueEmptyThrowMessage() throws ServiceException {
		if (null == this.value) {
			throw new ServiceException(this.message);
		}
		if (this.value instanceof List &&  ( CollectionUtils.isEmpty( ((List<?>)this.value) ) )) {
			throw new ServiceException(this.message);
		}
		if (this.value instanceof Map &&  ( MapUtils.isEmpty( ((Map<?,?>)this.value) ) )) {
			throw new ServiceException(this.message);
		}
		return this.value;
	}
	
}
