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
package org.qifu.core.api;

import java.util.List;

import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchBody;
import org.qifu.core.entity.TbOrrsTask;
import org.qifu.core.service.IOrrsTaskService;
import org.qifu.core.util.CoreApiSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "CORE_PROG010D0001", description = "orrs task.")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/PROG010D0001")
public class PROG010D0001Controller extends CoreApiSupport {
	private static final long serialVersionUID = 6872655082424023435L;
	
	@Autowired
	IOrrsTaskService<TbOrrsTask, String> orrsTaskService;
	
	@ControllerMethodAuthority(programId = "CORE_PROG010D0001Q", check = true)
	@Operation(summary = "CORE_PROG010D0001 - findPage", description = "查核TB_ORRS_TASK資料")
	@ResponseBody
	@PostMapping(value = "/findPage", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<QueryResult<List<TbOrrsTask>>> findPage(@RequestBody SearchBody searchBody) {
		QueryResult<List<TbOrrsTask>> result = this.initResult();
		try {
			QueryResult<List<TbOrrsTask>> queryResult = this.orrsTaskService.findPage(
					this.queryParameter(searchBody).fullEquals("taskId").fullLink("nameLike").value(), 
					searchBody.getPageOf().orderBy("TASK_ID").sortTypeAsc());
			this.setQueryResponseJsonResult(queryResult, result, searchBody.getPageOf());
		} catch (ServiceException | ControllerException e) {
			this.noSuccessResult(result, e);
		} catch (Exception e) {
			this.noSuccessResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}			
	
}
