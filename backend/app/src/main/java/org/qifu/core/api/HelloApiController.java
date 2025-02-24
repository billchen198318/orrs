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
package org.qifu.core.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.YesNoKeyProvide;
import org.qifu.core.util.CoreApiSupport;
import org.qifu.core.vo.TestModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "TEST.", description = "Test hello world.")
@RestController
@RequestMapping(value = "/api/hello")
public class HelloApiController extends CoreApiSupport {
	
	private static final long serialVersionUID = -2710621780849674671L;
	
	@Operation(summary = "測試del", description = "測試用的接口del")
	@DeleteMapping("/delPlay")
	public String delPlay(String key) {
		String flag = YesNoKeyProvide.NO;
		if (StringUtils.isBlank(key)) {
			return YesNoKeyProvide.NO;
		}		
		return flag;
	}
	
	@Operation(summary = "測試", description = "測試用的接口")
	@PostMapping(value = "/play", produces = {MediaType.APPLICATION_JSON_VALUE})
	public QueryResult<String> play(@RequestBody TestModel data) {
		QueryResult<String> result = this.initResult();
		if (null == data || StringUtils.isBlank(data.getKey())) {
			this.noSuccessResult(result, BaseSystemMessage.parameterBlank());
			return result;
		}
		try {
			Map<String, String> dataMap = new HashMap<>();
			dataMap.put("str", data.getMsg());
			ObjectMapper om = new ObjectMapper();
			String val = om.writeValueAsString(dataMap);
			this.successResult(result, val);
		} catch (Exception e) {
			e.printStackTrace();
			this.noSuccessResult(result, e);
		}
		return result;
	}
	
	@Operation(summary = "測試2", description = "測試用的接口2")
	@GetMapping(value = "/testPV/{id}")
	public QueryResult<String> testPV(@PathVariable String id) {
		QueryResult<String> result = this.initResult();
		result.setValue(id);
		result.setMessage( "hello." );
		result.setSuccess( YES );
		return result;
	}
	
}
