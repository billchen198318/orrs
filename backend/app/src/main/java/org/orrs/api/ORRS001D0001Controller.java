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
import org.orrs.service.IOrrsCommandService;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.CheckControllerFieldHandler;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
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

@Tag(name = "ORRS001D0001", description = "llm command.")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ORRS001D0001")
public class ORRS001D0001Controller extends CoreApiSupport {
	private static final long serialVersionUID = 2667259412243494421L;
	
	@Autowired
	IOrrsCommandService<TbOrrsCommand, String> orrsCommandService;
	
	@ControllerMethodAuthority(programId = "ORRS001D0001Q", check = true)
	@Operation(summary = "ORRS001D0001 - findPage", description = "查核tb_orrs_command資料")
	@ResponseBody
	@PostMapping(value = "/findPage", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<QueryResult<List<TbOrrsCommand>>> findPage(@RequestBody SearchBody searchBody) {
		QueryResult<List<TbOrrsCommand>> result = this.initResult();
		try {
			QueryResult<List<TbOrrsCommand>> queryResult = this.orrsCommandService.findPage(
					this.queryParameter(searchBody).fullEquals("cmdId").fullLink("nameLike").value(), 
					searchBody.getPageOf().orderBy("CMD_ID").sortTypeAsc());
			this.setQueryResponseJsonResult(queryResult, result, searchBody.getPageOf());
		} catch (ServiceException | ControllerException e) {
			this.noSuccessResult(result, e);
		} catch (Exception e) {
			this.noSuccessResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}	
	
	private void handlerCheck(DefaultControllerJsonResultObj<TbOrrsCommand> result, TbOrrsCommand command) throws ControllerException, ServiceException, Exception {
		CheckControllerFieldHandler<TbOrrsCommand> chk = this.getCheckControllerFieldHandler(result);
		chk.testField("cmdId", command, "@org.apache.commons.lang3.StringUtils@isBlank(cmdId)", "請輸入編號")
		.testField("name", command, "@org.apache.commons.lang3.StringUtils@isBlank(name)", "請輸入名稱")
		.testField("userMessage", command, "@org.apache.commons.lang3.StringUtils@isBlank(userMessage)", "請輸入llm請求訊息")
		.testField("resultVariable", command, "@org.apache.commons.lang3.StringUtils@isBlank(resultVariable)", "腳本變數")
		.testField("resultType", command, "@org.apache.commons.lang3.StringUtils@isBlank(resultType)", "截取類別")
		.throwHtmlMessage();
		
		chk.testField("cmdId", command, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09Id(cmdId)", "編號只允許輸入0-9,a-z,A-Z正常字元")
		.testField("prompts", command, "!@org.apache.commons.collections.CollectionUtils@isEmpty(prompts) && prompts.size > org.orrs.OrrsConstants.MAX_COMMAND_SIZE", "最多" + org.orrs.OrrsConstants.MAX_COMMAND_SIZE + "筆prompt")
		.throwHtmlMessage();
		
	}
	
	private void save(DefaultControllerJsonResultObj<TbOrrsCommand> result, TbOrrsCommand command) throws ControllerException, ServiceException, Exception {
		this.handlerCheck(result, command);
		
	}
	
	@ControllerMethodAuthority(programId = "ORRS001D0001C", check = true)
	@Operation(summary = "ORRS001D0001C - save", description = "新增TB_ORRS_COMMAND資料")
	@ResponseBody
	@PostMapping(value = "/save", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbOrrsCommand>> doSave(@RequestBody TbOrrsCommand command) {
		DefaultControllerJsonResultObj<TbOrrsCommand> result = this.initDefaultJsonResult();
		try {
			this.save(result, command);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}	
	
}
