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
package org.orrs.api;

import java.util.List;

import org.orrs.entity.TbOrrsTaskResult;
import org.orrs.logic.IOrrsLogicService;
import org.orrs.service.IOrrsTaskResultService;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchBody;
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

@Tag(name = "ORRS001D0003", description = "llm task result.")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ORRS001D0003")
public class ORRS001D0003Controller extends CoreApiSupport {
	private static final long serialVersionUID = -9041763871660010620L;
	
	@Autowired
	IOrrsTaskResultService<TbOrrsTaskResult, String> orrsTaskResultService;
	
	@Autowired
	IOrrsLogicService orrsLogicService;		
	
	@ControllerMethodAuthority(programId = "ORRS001D0003Q", check = true)
	@Operation(summary = "ORRS001D0003 - findPage", description = "查核TB_ORRS_TASK_RESULT資料")
	@ResponseBody
	@PostMapping(value = "/findPage", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<QueryResult<List<TbOrrsTaskResult>>> findPage(@RequestBody SearchBody searchBody) {
		QueryResult<List<TbOrrsTaskResult>> result = this.initResult();
		try {
			QueryResult<List<TbOrrsTaskResult>> queryResult = this.orrsTaskResultService.findPage(
					"countSimpleFindPage", 
					"findSimpleFindPage", 
					this.queryParameter(searchBody).fullEquals("taskId").fullEquals("lastCmd").fullEquals("date1").fullEquals("date2").fullLink("processIdLike").value(), 
					searchBody.getPageOf().orderBy("PROCESS_ID").sortTypeDesc());
			this.setQueryResponseJsonResult(queryResult, result, searchBody.getPageOf());
		} catch (ServiceException | ControllerException e) {
			this.noSuccessResult(result, e);
		} catch (Exception e) {
			this.noSuccessResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}		
	
	@ControllerMethodAuthority(programId = "ORRS001D0003E", check = true)
	@Operation(summary = "ORRS001D0003E - load", description = "讀取TB_ORRS_TASK_RESULT資料")
	@ResponseBody
	@PostMapping(value = "/load", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbOrrsTaskResult>> doLoad(@RequestBody TbOrrsTaskResult taskResult) {
		DefaultControllerJsonResultObj<TbOrrsTaskResult> result = this.initDefaultJsonResult();
		try {
			DefaultResult<TbOrrsTaskResult> lResult = this.orrsTaskResultService.selectByEntityPrimaryKey(taskResult);
			this.setDefaultResponseJsonResult(lResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}		
	
	@ControllerMethodAuthority(programId = "ORRS001D0003D", check = true)
	@Operation(summary = "ORRS001D0003D - delete", description = "刪除TB_ORRS_TASK資料")
	@ResponseBody
	@PostMapping(value = "/delete", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<Boolean>> doDelete(@RequestBody TbOrrsTaskResult taskResult) {
		DefaultControllerJsonResultObj<Boolean> result = this.initDefaultJsonResult();
		try {
			DefaultResult<Boolean> delResult = this.orrsLogicService.deleteTaskResult(taskResult);
			this.setDefaultResponseJsonResult(delResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}	
	
}
