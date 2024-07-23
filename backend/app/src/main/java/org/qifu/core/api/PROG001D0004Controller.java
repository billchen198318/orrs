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
import org.qifu.core.entity.TbSysTemplate;
import org.qifu.core.entity.TbSysTemplateParam;
import org.qifu.core.logic.ISystemTemplateLogicService;
import org.qifu.core.service.ISysTemplateParamService;
import org.qifu.core.service.ISysTemplateService;
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

@Tag(name = "CORE_PROG001D0004", description = "Freemarker樣板 management.")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/PROG001D0004")
public class PROG001D0004Controller extends CoreApiSupport {
	private static final long serialVersionUID = 1558945973473225107L;
	
	@Autowired
	ISysTemplateService<TbSysTemplate, String> sysTemplateService;
	
	@Autowired
	ISysTemplateParamService<TbSysTemplateParam, String> sysTemplateParamService;
	
	@Autowired
	ISystemTemplateLogicService systemTemplateLogicService;
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0004Q", check = true)
	@Operation(summary = "CORE_PROG001D0004 - findPage", description = "查詢TB_SYS_TEMPLATE資料")
	@ResponseBody
	@PostMapping(value = "/findPage", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<QueryResult<List<TbSysTemplate>>> findPage(@RequestBody SearchBody searchBody) {
		QueryResult<List<TbSysTemplate>> result = this.initResult();
		try {
			QueryResult<List<TbSysTemplate>> queryResult = this.sysTemplateService.findPage(
					this.queryParameter(searchBody).fullEquals("templateId").fullLink("titleLike").value(), 
					searchBody.getPageOf().orderBy("TEMPLATE_ID").sortTypeAsc());
			this.setQueryResponseJsonResult(queryResult, result, searchBody.getPageOf());
		} catch (ServiceException | ControllerException e) {
			this.noSuccessResult(result, e);
		} catch (Exception e) {
			this.noSuccessResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}		
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0004D", check = true)
	@Operation(summary = "CORE_PROG001D0004 - delete", description = "刪除TB_SYS_TEMPLATE資料")
	@ResponseBody
	@PostMapping(value = "/delete", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<Boolean>> doDelete(@RequestBody TbSysTemplate sysTemplate) {
		DefaultControllerJsonResultObj<Boolean> result = this.initDefaultJsonResult();
		try {
			DefaultResult<Boolean> delResult = this.systemTemplateLogicService.delete(sysTemplate);
			this.setDefaultResponseJsonResult(delResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}	
	
	private void handlerCheck(DefaultControllerJsonResultObj<TbSysTemplate> result, TbSysTemplate sysTemplate) throws ControllerException, ServiceException, Exception {
		CheckControllerFieldHandler<TbSysTemplate> chk = this.getCheckControllerFieldHandler(result);
		chk
		.testField("templateId", sysTemplate, "@org.apache.commons.lang3.StringUtils@isBlank(templateId)", "請輸入樣板編號")
		.testField("title", sysTemplate, "@org.apache.commons.lang3.StringUtils@isBlank(title)", "請輸入樣板標題")		
		.testField("message", sysTemplate, "@org.apache.commons.lang3.StringUtils@isBlank(message)", "請輸入樣板內容")		
		.throwHtmlMessage();
		
		chk.testField("templateId", sysTemplate, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09Id(templateId)", "樣板編號只允許輸入0-9,a-z,A-Z正常字元");		
	}	
	
	private void save(DefaultControllerJsonResultObj<TbSysTemplate> result, TbSysTemplate sysTemplate) throws ControllerException, ServiceException, Exception {
		this.handlerCheck(result, sysTemplate);
		DefaultResult<TbSysTemplate> cResult = this.systemTemplateLogicService.create(sysTemplate);
		this.setDefaultResponseJsonResult(cResult, result);
	}	
	
	private void update(DefaultControllerJsonResultObj<TbSysTemplate> result, TbSysTemplate sysTemplate) throws ControllerException, ServiceException, Exception {
		this.handlerCheck(result, sysTemplate);
		DefaultResult<TbSysTemplate> uResult = this.systemTemplateLogicService.update(sysTemplate);
		this.setDefaultResponseJsonResult(uResult, result);		
	}	
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0004C", check = true)
	@Operation(summary = "CORE_PROG001D0004 - save", description = "新增TB_SYS_TEMPLATE資料")
	@ResponseBody
	@PostMapping(value = "/save", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbSysTemplate>> doSave(@RequestBody TbSysTemplate sysTemplate) {
		DefaultControllerJsonResultObj<TbSysTemplate> result = this.initDefaultJsonResult();
		try {
			this.save(result, sysTemplate);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}		
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0004E", check = true)
	@Operation(summary = "CORE_PROG001D0004 - load", description = "讀取TB_SYS_TEMPLATE資料")
	@ResponseBody
	@PostMapping(value = "/load", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbSysTemplate>> doLoad(@RequestBody TbSysTemplate sysTemplate) {
		DefaultControllerJsonResultObj<TbSysTemplate> result = this.initDefaultJsonResult();
		try {
			DefaultResult<TbSysTemplate> lResult = this.sysTemplateService.selectByEntityPrimaryKey(sysTemplate);
			this.setDefaultResponseJsonResult(lResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}	
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0004U", check = true)
	@Operation(summary = "CORE_PROG001D0004 - update", description = "更新TB_SYS_TEMPLATE資料")
	@ResponseBody
	@PostMapping(value = "/update", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbSysTemplate>> doUpdate(@RequestBody TbSysTemplate sysTemplate) {
		DefaultControllerJsonResultObj<TbSysTemplate> result = this.initDefaultJsonResult();
		try {
			this.update(result, sysTemplate);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}		
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0004Q", check = true)
	@Operation(summary = "CORE_PROG001D0004 - findSetParamPage", description = "查詢TB_SYS_TEMPLATE_PARAM資料")
	@ResponseBody
	@PostMapping(value = "/findSetParamPage", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<QueryResult<List<TbSysTemplateParam>>> findSetParamPage(@RequestBody SearchBody searchBody) {
		QueryResult<List<TbSysTemplateParam>> result = this.initResult();
		try {
			QueryResult<List<TbSysTemplateParam>> queryResult = this.sysTemplateParamService.findPage(
					this.queryParameter(searchBody).fullEquals("templateId").value(), 
					searchBody.getPageOf().orderBy("TEMPLATE_VAR").sortTypeAsc());
			this.setQueryResponseJsonResult(queryResult, result, searchBody.getPageOf());
		} catch (ServiceException | ControllerException e) {
			this.noSuccessResult(result, e);
		} catch (Exception e) {
			this.noSuccessResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}	
	
	private void handlerCheckParam(DefaultControllerJsonResultObj<TbSysTemplateParam> result, TbSysTemplateParam param) throws ControllerException, ServiceException, Exception {
		CheckControllerFieldHandler<TbSysTemplateParam> chk = this.getCheckControllerFieldHandler(result);
		chk
		.testField("templateId", param, "@org.apache.commons.lang3.StringUtils@isBlank(templateId)", "請輸入樣板編號")
		.testField("isTitle", param, "@org.apache.commons.lang3.StringUtils@isBlank(isTitle)", "請輸入樣板是否標題")
		.testField("templateVar", param, "@org.apache.commons.lang3.StringUtils@isBlank(templateVar)", "請輸入樣板變數")
		.testField("objectVar", param, "@org.apache.commons.lang3.StringUtils@isBlank(objectVar)", "請輸入物件變數")		
		.throwHtmlMessage();
		
		chk.testField("templateId", param, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09Id(templateId)", "樣板編號只允許輸入0-9,a-z,A-Z正常字元");		
	}	
	
	private void saveParam(DefaultControllerJsonResultObj<TbSysTemplateParam> result, TbSysTemplateParam param) throws ControllerException, ServiceException, Exception {
		this.handlerCheckParam(result, param);
		DefaultResult<TbSysTemplateParam> cResult = this.sysTemplateParamService.insert(param);
		this.setDefaultResponseJsonResult(cResult, result);
	}		
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0004U", check = true)
	@Operation(summary = "CORE_PROG001D0004 - saveSetParam", description = "新增TB_SYS_TEMPLATE_PARAM資料")
	@ResponseBody
	@PostMapping(value = "/saveSetParam", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbSysTemplateParam>> doSaveSetParam(@RequestBody TbSysTemplateParam param) {
		DefaultControllerJsonResultObj<TbSysTemplateParam> result = this.initDefaultJsonResult();
		try {			
			this.saveParam(result, param);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}	
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0004D", check = true)
	@Operation(summary = "CORE_PROG001D0004 - deleteSetParam", description = "刪除TB_SYS_TEMPLATE_PARAM資料")
	@ResponseBody
	@PostMapping(value = "/deleteSetParam", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<Boolean>> doDeleteSetParam(@RequestBody TbSysTemplateParam param) {
		DefaultControllerJsonResultObj<Boolean> result = this.initDefaultJsonResult();
		try {
			DefaultResult<Boolean> delResult = this.systemTemplateLogicService.deleteParam(param);
			this.setDefaultResponseJsonResult(delResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}	
	
}
