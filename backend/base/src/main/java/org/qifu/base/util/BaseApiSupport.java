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
package org.qifu.base.util;

import org.qifu.base.exception.ControllerException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.QueryParamBuilder;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchBody;
import org.qifu.base.model.YesNo;

public class BaseApiSupport extends YesNo {
	
	private static final long serialVersionUID = -3934810030443337825L;

	protected QueryParamBuilder queryParameter() {
		return QueryParamBuilder.build();
	}
	
	protected QueryParamBuilder queryParameter(SearchBody searchBody) {
		return QueryParamBuilder.build(searchBody);
	}	
	
	protected <T> DefaultControllerJsonResultObj<T> initDefaultJsonResult() {
		DefaultControllerJsonResultObj<T> r = new DefaultControllerJsonResultObj<>();
		if (UserLocalUtils.getUserInfo() != null) {
			r.setIsAuth(YES);
			r.setLogin(YES);
		}
		return r;
	}
	
	protected <T> QueryResult<T> initResult() {
		QueryResult<T> result = new QueryResult<>();
		this.initResult(result);
		return result;
	}
	
	protected <T> void initResult(QueryResult<T> result) {
		if (result == null) {
			return;
		}
		result.setIsAuth(NO);
		if (UserLocalUtils.getUserInfo() != null) {
			result.setIsAuth(YES);
		}
		result.setSuccess(NO);
	}
	
	protected <T> void successResult(DefaultControllerJsonResultObj<T> result) {
		if (result == null) {
			return;
		}
		result.setSuccess(YES);
	}	
	
	protected <T> void successResult(QueryResult<T> result) {
		if (result == null) {
			return;
		}
		result.setSuccess(YES);
	}
	
	protected <T> void successResult(QueryResult<T> result, T value) {
		if (result == null) {
			return;
		}
		result.setSuccess(YES);
		result.setValue(value);
	}
	
	protected <T> void noSuccessResult(QueryResult<T> result) {
		if (result == null) {
			return;
		}
		result.setSuccess(NO);
	}
	
	protected <T> void noSuccessResult(QueryResult<T> result, String message) {
		if (result == null) {
			return;
		}
		result.setSuccess(NO);
		result.setMessage(message);
	}	
	
	protected <T> void noSuccessResult(QueryResult<T> result, Exception e) {
		this.noSuccessResult(result, ( (e != null && e.getMessage() !=null) ? e.getMessage() : BaseSystemMessage.objectNull() ));		
	}	
	
	protected void exceptionResult(DefaultControllerJsonResultObj<?> result, Exception e) {
		e.printStackTrace();
		result.setMessage( e.getMessage() );
		result.setSuccess( ControllerException.PAGE_EXCEPTION_CODE );
	}
	
}

