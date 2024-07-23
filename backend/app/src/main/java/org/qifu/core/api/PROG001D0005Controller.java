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

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.CheckControllerFieldHandler;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchBody;
import org.qifu.core.entity.TbSysJreport;
import org.qifu.core.entity.TbSysJreportParam;
import org.qifu.core.logic.ISystemJreportLogicService;
import org.qifu.core.service.ISysJreportParamService;
import org.qifu.core.service.ISysJreportService;
import org.qifu.core.util.CoreApiSupport;
import org.qifu.core.util.JReportUtils;
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

@Tag(name = "CORE_PROG001D0005", description = "Jasper report resources management.")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/PROG001D0005")
public class PROG001D0005Controller extends CoreApiSupport {
	private static final long serialVersionUID = -6882334788025159559L;
	
	@Autowired
	ISysJreportService<TbSysJreport, String> sysJreportService;
	
	@Autowired
	ISysJreportParamService<TbSysJreportParam, String> sysJreportParamService;
	
	@Autowired
	ISystemJreportLogicService systemJreportLogicService;
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0005Q", check = true)
	@Operation(summary = "CORE_PROG001D0005 - findPage", description = "查詢TB_SYS_JREPORT資料")
	@ResponseBody
	@PostMapping(value = "/findPage", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<QueryResult<List<TbSysJreport>>> findPage(@RequestBody SearchBody searchBody) {
		QueryResult<List<TbSysJreport>> result = this.initResult();
		try {
			QueryResult<List<TbSysJreport>> queryResult = this.sysJreportService.findPage(
					"count", 
					"findPageSimple",
					this.queryParameter(searchBody).fullEquals("reportId").value(),
					searchBody.getPageOf().orderBy("REPORT_ID").sortTypeAsc());
			this.setQueryResponseJsonResult(queryResult, result, searchBody.getPageOf());
		} catch (ServiceException | ControllerException e) {
			this.noSuccessResult(result, e);
		} catch (Exception e) {
			this.noSuccessResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0005D", check = true)
	@Operation(summary = "CORE_PROG001D0005 - delete", description = "刪除TB_SYS_JREPORT資料")
	@ResponseBody
	@PostMapping(value = "/delete", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<Boolean>> doDelete(@RequestBody TbSysJreport sysJreport) {
		DefaultControllerJsonResultObj<Boolean> result = this.initDefaultJsonResult();
		try {
			DefaultResult<Boolean> delResult = this.systemJreportLogicService.delete(sysJreport);
			this.setDefaultResponseJsonResult(delResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}
	
	private void handlerCheck(DefaultControllerJsonResultObj<TbSysJreport> result, TbSysJreport sysJreport, boolean createMode) throws ControllerException, ServiceException, Exception {
		CheckControllerFieldHandler<TbSysJreport> chk = this.getCheckControllerFieldHandler(result);
		chk.testField("reportId", sysJreport, "@org.apache.commons.lang3.StringUtils@isBlank(reportId)", "請輸入報表編號").throwHtmlMessage();
		chk.testField("reportId", sysJreport, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09Id(reportId)", "報表編號只允許輸入0-9,a-z,A-Z正常字元").throwHtmlMessage();
		if (createMode) {
			chk.testField("file", sysJreport, "@org.apache.commons.lang3.StringUtils@isBlank(file)", "請輸入檔案").throwHtmlMessage();
			if (StringUtils.isBlank(sysJreport.getUploadBase64())) {
				chk.throwHtmlMessage("file", "請輸入檔案");
			}
		}
	}
	
	private void deployReport(DefaultResult<TbSysJreport> result) throws ControllerException, ServiceException, Exception {
		if (YES.equals(result.getSuccess())) {
			TbSysJreport sysJRpt = result.getValue();
			JReportUtils.deployReport(sysJRpt);			
		}		
	}
	
	private void save(DefaultControllerJsonResultObj<TbSysJreport> result, TbSysJreport sysJreport) throws ControllerException, ServiceException, Exception {
		this.handlerCheck(result, sysJreport, true);
		DefaultResult<TbSysJreport> cResult = this.systemJreportLogicService.create(sysJreport);
		this.deployReport(cResult);
		this.setDefaultResponseJsonResult(result, cResult);
	}
	
	private void update(DefaultControllerJsonResultObj<TbSysJreport> result, TbSysJreport sysJreport) throws ControllerException, ServiceException, Exception {
		this.handlerCheck(result, sysJreport, false);
		DefaultResult<TbSysJreport> uResult = this.systemJreportLogicService.update(sysJreport);
		this.deployReport(uResult);
		this.setDefaultResponseJsonResult(uResult, result);		
	}
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0005C", check = true)
	@Operation(summary = "CORE_PROG001D0005 - save", description = "新增TB_SYS_JREPORT資料")
	@ResponseBody
	@PostMapping(value = "/save", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbSysJreport>> doSave(@RequestBody TbSysJreport sysJreport) {
		DefaultControllerJsonResultObj<TbSysJreport> result = this.initDefaultJsonResult();
		try {
			this.save(result, sysJreport);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}		
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0005E", check = true)
	@Operation(summary = "CORE_PROG001D0005 - load", description = "讀取TB_SYS_JREPORT資料")
	@ResponseBody
	@PostMapping(value = "/load", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbSysJreport>> doLoad(@RequestBody TbSysJreport sysJreport) {
		DefaultControllerJsonResultObj<TbSysJreport> result = this.initDefaultJsonResult();
		try {
			DefaultResult<TbSysJreport> lResult = this.sysJreportService.selectByPrimaryKeySimple(sysJreport.getOid());
			this.setDefaultResponseJsonResult(lResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}	
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0005U", check = true)
	@Operation(summary = "CORE_PROG001D0005 - update", description = "更新TB_SYS_JREPORT資料")
	@ResponseBody
	@PostMapping(value = "/update", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbSysJreport>> doUpdate(@RequestBody TbSysJreport sysJreport) {
		DefaultControllerJsonResultObj<TbSysJreport> result = this.initDefaultJsonResult();
		try {
			this.update(result, sysJreport);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0005Q", check = true)
	@Operation(summary = "CORE_PROG001D0005 - findSetParamPage", description = "查詢TB_SYS_JREPORT_PARAM資料")
	@ResponseBody
	@PostMapping(value = "/findSetParamPage", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<QueryResult<List<TbSysJreportParam>>> findSetParamPage(@RequestBody SearchBody searchBody) {
		QueryResult<List<TbSysJreportParam>> result = this.initResult();
		try {
			QueryResult<List<TbSysJreportParam>> queryResult = this.sysJreportParamService.findPage(
					this.queryParameter(searchBody).fullEquals("reportId").value(), 
					searchBody.getPageOf().orderBy("RPT_PARAM").sortTypeAsc());					
			this.setQueryResponseJsonResult(queryResult, result, searchBody.getPageOf());
		} catch (ServiceException | ControllerException e) {
			this.noSuccessResult(result, e);
		} catch (Exception e) {
			this.noSuccessResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}
	
	private void handlerCheckParam(DefaultControllerJsonResultObj<TbSysJreportParam> result, TbSysJreportParam param) throws ControllerException, ServiceException, Exception {
		CheckControllerFieldHandler<TbSysJreportParam> chk = this.getCheckControllerFieldHandler(result);
		chk
		.testField("reportId", param, "@org.apache.commons.lang3.StringUtils@isBlank(reportId)", "請輸入Jasper報表編號")
		.testField("urlParam", param, "@org.apache.commons.lang3.StringUtils@isBlank(urlParam)", "請輸入Url參數")
		.testField("rptParam", param, "@org.apache.commons.lang3.StringUtils@isBlank(rptParam)", "請輸入報表參數")	
		.throwHtmlMessage();
		
		chk.testField("urlParam", param, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09Id(urlParam)", "Url參數只允許輸入0-9,a-z,A-Z正常字元");		
		chk.testField("rptParam", param, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09Id(rptParam)", "報表參數只允許輸入0-9,a-z,A-Z正常字元");	
	}	
	
	private void saveParam(DefaultControllerJsonResultObj<TbSysJreportParam> result, TbSysJreportParam param) throws ControllerException, ServiceException, Exception {
		this.handlerCheckParam(result, param);
		DefaultResult<TbSysJreportParam> cResult = this.sysJreportParamService.insert(param);
		this.setDefaultResponseJsonResult(cResult, result);
	}		
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0005C", check = true)
	@Operation(summary = "CORE_PROG001D0005 - saveSetParam", description = "新增TB_SYS_TEMPLATE_PARAM資料")
	@ResponseBody
	@PostMapping(value = "/saveSetParam", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbSysJreportParam>> doSaveSetParam(@RequestBody TbSysJreportParam param) {
		DefaultControllerJsonResultObj<TbSysJreportParam> result = this.initDefaultJsonResult();
		try {			
			this.saveParam(result, param);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}	
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0005D", check = true)
	@Operation(summary = "CORE_PROG001D0005 - deleteSetParam", description = "刪除TB_SYS_TEMPLATE_PARAM資料")
	@ResponseBody
	@PostMapping(value = "/deleteSetParam", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<Boolean>> doDeleteSetParam(@RequestBody TbSysJreportParam param) {
		DefaultControllerJsonResultObj<Boolean> result = this.initDefaultJsonResult();
		try {
			DefaultResult<Boolean> delResult = this.systemJreportLogicService.deleteParam(param);
			this.setDefaultResponseJsonResult(delResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}
	
}
