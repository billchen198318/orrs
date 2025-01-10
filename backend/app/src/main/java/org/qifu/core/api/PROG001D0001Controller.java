/* 
 * Copyright 2021-2023 qifu of copyright Chen Xin Nien
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
import org.qifu.base.model.CheckControllerFieldHandler;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchBody;
import org.qifu.core.entity.TbSys;
import org.qifu.core.logic.IApplicationSystemLogicService;
import org.qifu.core.service.ISysService;
import org.qifu.core.util.CoreApiSupport;
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

@Tag(name = "CORE_PROG001D0001", description = "Application site.")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@ResponseBody
@RequestMapping("/api/PROG001D0001")
public class PROG001D0001Controller extends CoreApiSupport {
	private static final long serialVersionUID = -5896010855369568172L;
	
	private static final String SYS_ID_VAR = "sysId";
	
	private final transient ISysService<TbSys, String> sysService;
	
	private final transient IApplicationSystemLogicService applicationSystemLogicService;
	
	public PROG001D0001Controller(ISysService<TbSys, String> sysService,
			IApplicationSystemLogicService applicationSystemLogicService) {
		super();
		this.sysService = sysService;
		this.applicationSystemLogicService = applicationSystemLogicService;
	}
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0001Q", check = true)
	@Operation(summary = "CORE_PROG001D0001 - findPage", description = "查核TB_SYS資料")
	@PostMapping(value = "/findPage", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<QueryResult<List<TbSys>>> findPage(@RequestBody SearchBody searchBody) {
		QueryResult<List<TbSys>> result = this.initResult();
		try {
			QueryResult<List<TbSys>> queryResult = this.sysService.findPage(
					this.queryParameter(searchBody).fullEquals(SYS_ID_VAR).fullLink("nameLike").value(), 
					searchBody.getPageOf().orderBy("SYS_ID").sortTypeAsc());
			this.setQueryResponseJsonResult(queryResult, result, searchBody.getPageOf());
		} catch (ServiceException | ControllerException e) {
			this.noSuccessResult(result, e);
		} 
		return ResponseEntity.ok().body(result);
	}
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0001D", check = true)
	@Operation(summary = "CORE_PROG001D0001 - delete", description = "刪除TB_SYS資料")
	@PostMapping(value = "/delete", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<Boolean>> doDelete(@RequestBody TbSys sys) {
		DefaultControllerJsonResultObj<Boolean> result = this.initDefaultJsonResult();
		try {
			DefaultResult<Boolean> delResult = this.applicationSystemLogicService.delete(sys);
			this.setDefaultResponseJsonResult(delResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} 
		return ResponseEntity.ok().body(result);
	}
	
	private void handlerCheck(DefaultControllerJsonResultObj<TbSys> result, TbSys sys) throws ControllerException, ServiceException {
		CheckControllerFieldHandler<TbSys> chk = this.getCheckControllerFieldHandler(result);
		
		chk.testField(SYS_ID_VAR, sys, "@org.apache.commons.lang3.StringUtils@isBlank(sysId)", "請輸入編號")
		.testField("name", sys, "@org.apache.commons.lang3.StringUtils@isBlank(name)", "請輸入名稱")
		.testField("host", sys, "@org.apache.commons.lang3.StringUtils@isBlank(host)", "請輸入host")
		.testField("contextPath", sys, "@org.apache.commons.lang3.StringUtils@isBlank(contextPath)", "請輸入Context path")
		.throwHtmlMessage();
		
		chk.testField(SYS_ID_VAR, sys, "!@org.qifu.util.SimpleUtils@checkBeTrueOfAZaz09Id(sysId)", "編號只允許輸入0-9,a-z,A-Z正常字元")
		.throwHtmlMessage();		
	}
	
	private void save(DefaultControllerJsonResultObj<TbSys> result, TbSys sys) throws ControllerException, ServiceException {
		this.handlerCheck(result, sys);
		DefaultResult<TbSys> cResult = this.applicationSystemLogicService.create(sys);
		this.setDefaultResponseJsonResult(cResult, result);
	}
	
	private void update(DefaultControllerJsonResultObj<TbSys> result, TbSys sys) throws ControllerException, ServiceException {
		this.handlerCheck(result, sys);
		DefaultResult<TbSys> uResult = this.applicationSystemLogicService.update(sys);
		this.setDefaultResponseJsonResult(uResult, result);
	}
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0001C", check = true)
	@Operation(summary = "CORE_PROG001D0001 - save", description = "新增TB_SYS資料")
	@PostMapping(value = "/save", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbSys>> doSave(@RequestBody TbSys sys) {
		DefaultControllerJsonResultObj<TbSys> result = this.initDefaultJsonResult();
		try {
			this.save(result, sys);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} 
		return ResponseEntity.ok().body(result);
	}
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0001E", check = true)
	@Operation(summary = "CORE_PROG001D0001 - load", description = "讀取TB_SYS資料")
	@PostMapping(value = "/load", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbSys>> doLoad(@RequestBody TbSys sys) {
		DefaultControllerJsonResultObj<TbSys> result = this.initDefaultJsonResult();
		try {
			DefaultResult<TbSys> lResult = this.sysService.selectByEntityPrimaryKey(sys);
			this.setDefaultResponseJsonResult(lResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} 
		return ResponseEntity.ok().body(result);
	}	
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0001U", check = true)
	@Operation(summary = "CORE_PROG001D0001 - update", description = "更新TB_SYS資料")
	@PostMapping(value = "/update", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbSys>> doUpdate(@RequestBody TbSys sys) {
		DefaultControllerJsonResultObj<TbSys> result = this.initDefaultJsonResult();
		try {
			this.update(result, sys);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} 
		return ResponseEntity.ok().body(result);
	}	
	
}
