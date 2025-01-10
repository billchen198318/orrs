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
import org.qifu.core.entity.TbRole;
import org.qifu.core.entity.TbRolePermission;
import org.qifu.core.logic.IRoleLogicService;
import org.qifu.core.service.IRolePermissionService;
import org.qifu.core.service.IRoleService;
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

@Tag(name = "CORE_PROG002D0001", description = "Role management.")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@ResponseBody
@RequestMapping("/api/PROG002D0001")
public class PROG002D0001Controller extends CoreApiSupport {
	private static final long serialVersionUID = -8319767341034129139L;
	
	private final transient IRoleService<TbRole, String> roleService;
	
	private final transient IRolePermissionService<TbRolePermission, String> rolePermissionService;
	
	private final transient IRoleLogicService roleLogicService;
	
	public PROG002D0001Controller(IRoleService<TbRole, String> roleService,
			IRolePermissionService<TbRolePermission, String> rolePermissionService,
			IRoleLogicService roleLogicService) {
		super();
		this.roleService = roleService;
		this.rolePermissionService = rolePermissionService;
		this.roleLogicService = roleLogicService;
	}
	
	@ControllerMethodAuthority(programId = "CORE_PROG002D0001Q", check = true)
	@Operation(summary = "CORE_PROG002D0001 - findPage", description = "查詢TB_ROLE資料")
	@PostMapping(value = "/findPage", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<QueryResult<List<TbRole>>> findPage(@RequestBody SearchBody searchBody) {
		QueryResult<List<TbRole>> result = this.initResult();
		try {
			QueryResult<List<TbRole>> queryResult = this.roleService.findPage(
					this.queryParameter(searchBody).fullEquals("role").value(),
					searchBody.getPageOf().orderBy("ROLE").sortTypeAsc());
			this.setQueryResponseJsonResult(queryResult, result, searchBody.getPageOf());
		} catch (ServiceException | ControllerException e) {
			this.noSuccessResult(result, e);
		} 
		return ResponseEntity.ok().body(result);
	}	
	
	@ControllerMethodAuthority(programId = "CORE_PROG002D0001D", check = true)
	@Operation(summary = "CORE_PROG002D0001 - delete", description = "刪除TB_ROLE資料")
	@PostMapping(value = "/delete", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<Boolean>> doDelete(@RequestBody TbRole role) {
		DefaultControllerJsonResultObj<Boolean> result = this.initDefaultJsonResult();
		try {
			DefaultResult<Boolean> delResult = this.roleLogicService.delete(role);
			this.setDefaultResponseJsonResult(delResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} 
		return ResponseEntity.ok().body(result);
	}	
	
	private void handlerCheck(DefaultControllerJsonResultObj<TbRole> result, TbRole role) throws ControllerException, ServiceException {
		CheckControllerFieldHandler<TbRole> chk = this.getCheckControllerFieldHandler(result);
		chk.testField("role", role, "@org.apache.commons.lang3.StringUtils@isBlank(role)", "請輸入Role編號").throwHtmlMessage();
		chk.testField("role", role, "!@org.qifu.util.SimpleUtils@checkBeTrueOfAZaz09Id(role)", "Role編號只允許輸入0-9,a-z,A-Z正常字元").throwHtmlMessage();
	}	
	
	private void save(DefaultControllerJsonResultObj<TbRole> result, TbRole role) throws ControllerException, ServiceException {
		this.handlerCheck(result, role);
		DefaultResult<TbRole> cResult = this.roleLogicService.create(role);
		this.setDefaultResponseJsonResult(result, cResult);
	}	
	
	private void update(DefaultControllerJsonResultObj<TbRole> result, TbRole role) throws ControllerException, ServiceException {
		this.handlerCheck(result, role);
		DefaultResult<TbRole> uResult = this.roleLogicService.update(role);
		this.setDefaultResponseJsonResult(result, uResult);
	}		
	
	@ControllerMethodAuthority(programId = "CORE_PROG002D0001C", check = true)
	@Operation(summary = "CORE_PROG002D0001 - save", description = "新增TB_ROLE資料")
	@PostMapping(value = "/save", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbRole>> doSave(@RequestBody TbRole role) {
		DefaultControllerJsonResultObj<TbRole> result = this.initDefaultJsonResult();
		try {
			this.save(result, role);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} 
		return ResponseEntity.ok().body(result);
	}
	
	@ControllerMethodAuthority(programId = "CORE_PROG002D0001E", check = true)
	@Operation(summary = "CORE_PROG002D0001 - load", description = "讀取TB_ROLE資料")
	@PostMapping(value = "/load", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbRole>> doLoad(@RequestBody TbRole role) {
		DefaultControllerJsonResultObj<TbRole> result = this.initDefaultJsonResult();
		try {
			DefaultResult<TbRole> lResult = this.roleService.selectByEntityPrimaryKey(role);
			this.setDefaultResponseJsonResult(lResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} 
		return ResponseEntity.ok().body(result);
	}		
	
	@ControllerMethodAuthority(programId = "CORE_PROG002D0001U", check = true)
	@Operation(summary = "CORE_PROG002D0001 - update", description = "更新TB_ROLE資料")
	@PostMapping(value = "/update", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbRole>> doUpdate(@RequestBody TbRole role) {
		DefaultControllerJsonResultObj<TbRole> result = this.initDefaultJsonResult();
		try {
			this.update(result, role);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} 
		return ResponseEntity.ok().body(result);
	}	
	
	@ControllerMethodAuthority(programId = "CORE_PROG002D0001Q", check = true)
	@Operation(summary = "CORE_PROG002D0001 - findSetParamPage", description = "查詢TB_ROLE_Permission資料")
	@PostMapping(value = "/findSetParamPage", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<QueryResult<List<TbRolePermission>>> findSetParamPage(@RequestBody SearchBody searchBody) {
		QueryResult<List<TbRolePermission>> result = this.initResult();
		try {
			QueryResult<List<TbRolePermission>> queryResult = this.rolePermissionService.findPage(
					this.queryParameter(searchBody).fullEquals("role").value(), 
					searchBody.getPageOf().orderBy("PERM_TYPE,PERMISSION").sortTypeAsc());					
			this.setQueryResponseJsonResult(queryResult, result, searchBody.getPageOf());
		} catch (ServiceException | ControllerException e) {
			this.noSuccessResult(result, e);
		} 
		return ResponseEntity.ok().body(result);
	}	
	
	private void handlerCheckParam(DefaultControllerJsonResultObj<TbRolePermission> result, TbRolePermission perm) throws ControllerException, ServiceException {
		CheckControllerFieldHandler<TbRolePermission> chk = this.getCheckControllerFieldHandler(result);
		chk
		.testField("permission", perm, "@org.apache.commons.lang3.StringUtils@isBlank(permission)", "請輸入permission字串")
		.testField("permType", perm, "@org.apache.commons.lang3.StringUtils@isBlank(permType)", "請輸入類別")
		.throwHtmlMessage();
	}	
	
	private void saveParam(DefaultControllerJsonResultObj<TbRolePermission> result, TbRolePermission perm) throws ControllerException, ServiceException {
		this.handlerCheckParam(result, perm);
		DefaultResult<TbRolePermission> cResult = this.rolePermissionService.insert(perm);
		this.setDefaultResponseJsonResult(cResult, result);
	}		
	
	@ControllerMethodAuthority(programId = "CORE_PROG002D0001C", check = true)
	@Operation(summary = "CORE_PROG002D0001 - saveSetParam", description = "新增TB_ROLE_Permission資料")
	@PostMapping(value = "/saveSetParam", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbRolePermission>> doSaveSetParam(@RequestBody TbRolePermission perm) {
		DefaultControllerJsonResultObj<TbRolePermission> result = this.initDefaultJsonResult();
		try {			
			this.saveParam(result, perm);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} 
		return ResponseEntity.ok().body(result);
	}		
	
	@ControllerMethodAuthority(programId = "CORE_PROG002D0001D", check = true)
	@Operation(summary = "CORE_PROG002D0001 - deleteSetParam", description = "刪除TB_ROLE_Permission資料")
	@PostMapping(value = "/deleteSetParam", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<Boolean>> doDeleteSetParam(@RequestBody TbRolePermission perm) {
		DefaultControllerJsonResultObj<Boolean> result = this.initDefaultJsonResult();
		try {
			DefaultResult<Boolean> delResult = this.roleLogicService.deletePermission(perm);
			this.setDefaultResponseJsonResult(delResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} 
		return ResponseEntity.ok().body(result);
	}	
	
	@ControllerMethodAuthority(programId = "CORE_PROG002D0001C", check = true)
	@Operation(summary = "CORE_PROG002D0001 - saveCopyAsNew", description = "複製TB_ROLE資料")
	@PostMapping(value = "/roleCopySaveJson", produces = {MediaType.APPLICATION_JSON_VALUE})		
	public ResponseEntity<DefaultControllerJsonResultObj<TbRole>> doSaveCopyAsNew(@RequestBody TbRole role) {
		DefaultControllerJsonResultObj<TbRole> result = this.initDefaultJsonResult();
		try {
			DefaultResult<TbRole> cResult = this.roleLogicService.copyAsNew(role.getFromRoleOid(), role);
			this.setDefaultResponseJsonResult(cResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} 
		return ResponseEntity.ok().body(result);
	}	
	
}
