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

import org.orrs.entity.TbOrrsCommand;
import org.orrs.entity.TbOrrsTask;
import org.orrs.logic.IOrrsLogicService;
import org.orrs.service.IOrrsCommandService;
import org.orrs.service.IOrrsTaskService;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.CheckControllerFieldHandler;
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

@Tag(name = "ORRS001D0002", description = "llm task.")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ORRS001D0002")
public class ORRS001D0002Controller extends CoreApiSupport {
	private static final long serialVersionUID = 1119907073197062918L;
	
	@Autowired
	IOrrsCommandService<TbOrrsCommand, String> orrsCommandService;
	
	@Autowired
	IOrrsTaskService<TbOrrsTask, String> orrsTaskService;
	
	@Autowired
	IOrrsLogicService orrsLogicService;	
	
	@ControllerMethodAuthority(programId = "ORRS001D0002Q", check = true)
	@Operation(summary = "ORRS001D0002 - findPage", description = "查核TB_ORRS_TASK資料")
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
	
	private void handlerCheck(DefaultControllerJsonResultObj<TbOrrsTask> result, TbOrrsTask task) throws ControllerException, ServiceException, Exception {
		CheckControllerFieldHandler<TbOrrsTask> chk = this.getCheckControllerFieldHandler(result);
		chk.testField("taskId", task, "@org.apache.commons.lang3.StringUtils@isBlank(taskId)", "請輸入編號")
		.testField("name", task, "@org.apache.commons.lang3.StringUtils@isBlank(name)", "請輸入名稱")
		.testField("cronExpr", task, "@org.apache.commons.lang3.StringUtils@isBlank(cronExpr)", "請輸入cron")
		.throwHtmlMessage();
		
		chk.testField("cronExpr", task, "!@org.springframework.scheduling.support.CronExpression@isValidExpression(cronExpr)", "cron不符合規範")
		.testField("cmds", task, "!@org.apache.commons.collections.CollectionUtils@isEmpty(cmds) && cmds.size > org.orrs.OrrsConstants.MAX_COMMAND_RECORD", "最多" + org.orrs.OrrsConstants.MAX_COMMAND_RECORD + "筆命令")
		.throwHtmlMessage();
	}
	
	private void save(DefaultControllerJsonResultObj<TbOrrsTask> result, TbOrrsTask task) throws ControllerException, ServiceException, Exception {
		this.handlerCheck(result, task);
		DefaultResult<TbOrrsTask> cResult = this.orrsLogicService.createTask(task);
		this.setDefaultResponseJsonResult(cResult, result);
	}
	
	private void update(DefaultControllerJsonResultObj<TbOrrsTask> result, TbOrrsTask task) throws ControllerException, ServiceException, Exception {
		this.handlerCheck(result, task);
		DefaultResult<TbOrrsTask> uResult = this.orrsLogicService.updateTask(task);
		this.setDefaultResponseJsonResult(uResult, result);
	}
	
	@ControllerMethodAuthority(programId = "ORRS001D0002C", check = true)
	@Operation(summary = "ORRS001D0002C - load command", description = "讀取TB_ORRS_TASK資料")
	@ResponseBody
	@PostMapping(value = "/loadCommandList", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<List<TbOrrsCommand>>> doLoadCommandList() {
		DefaultControllerJsonResultObj<List<TbOrrsCommand>> result = this.initDefaultJsonResult();
		try {
			DefaultResult<List<TbOrrsCommand>> lResult = this.orrsCommandService.selectList();
			this.setDefaultResponseJsonResult(lResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}		
	
	@ControllerMethodAuthority(programId = "ORRS001D0002C", check = true)
	@Operation(summary = "ORRS001D0002C - save", description = "新增TB_ORRS_TASK資料")
	@ResponseBody
	@PostMapping(value = "/save", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbOrrsTask>> doSave(@RequestBody TbOrrsTask task) {
		DefaultControllerJsonResultObj<TbOrrsTask> result = this.initDefaultJsonResult();
		try {
			this.save(result, task);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}		
	
	@ControllerMethodAuthority(programId = "ORRS001D0002D", check = true)
	@Operation(summary = "ORRS001D0002D - delete", description = "刪除TB_ORRS_TASK資料")
	@ResponseBody
	@PostMapping(value = "/delete", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<Boolean>> doDelete(@RequestBody TbOrrsTask task) {
		DefaultControllerJsonResultObj<Boolean> result = this.initDefaultJsonResult();
		try {
			DefaultResult<Boolean> delResult = this.orrsLogicService.deleteTask(task);
			this.setDefaultResponseJsonResult(delResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}
	
	@ControllerMethodAuthority(programId = "ORRS001D0002E", check = true)
	@Operation(summary = "ORRS001D0002E - load", description = "讀取TB_ORRS_TASK資料")
	@ResponseBody
	@PostMapping(value = "/load", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbOrrsTask>> doLoad(@RequestBody TbOrrsTask task) {
		DefaultControllerJsonResultObj<TbOrrsTask> result = this.initDefaultJsonResult();
		try {
			DefaultResult<TbOrrsTask> lResult = this.orrsLogicService.selectTask(task);
			this.setDefaultResponseJsonResult(lResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}		
	
	@ControllerMethodAuthority(programId = "ORRS001D0002U", check = true)
	@Operation(summary = "ORRS001D0002U - update", description = "更新TB_ORRS_TASK資料")
	@ResponseBody
	@PostMapping(value = "/update", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbOrrsTask>> doUpdate(@RequestBody TbOrrsTask task) {
		DefaultControllerJsonResultObj<TbOrrsTask> result = this.initDefaultJsonResult();
		try {
			this.update(result, task);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}	
	
}
