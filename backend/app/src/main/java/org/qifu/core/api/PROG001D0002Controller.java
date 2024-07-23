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
import org.qifu.core.entity.TbSysProg;
import org.qifu.core.logic.ISystemProgramLogicService;
import org.qifu.core.service.ISysProgService;
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

@Tag(name = "CORE_PROG001D0002", description = "Program page management.")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/PROG001D0002")
public class PROG001D0002Controller extends CoreApiSupport {
	private static final long serialVersionUID = -2060599663035482390L;
	
	@Autowired
	ISysProgService<TbSysProg, String> sysProgService;
	
	@Autowired
	ISystemProgramLogicService systemProgramLogicService;
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0002Q", check = true)
	@Operation(summary = "CORE_PROG001D0002 - findPage", description = "查詢TB_SYS_PROG資料")
	@ResponseBody
	@PostMapping(value = "/findPage", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<QueryResult<List<TbSysProg>>> findPage(@RequestBody SearchBody searchBody) {
		QueryResult<List<TbSysProg>> result = this.initResult();
		try {
			QueryResult<List<TbSysProg>> queryResult = this.sysProgService.findPage(
					this.queryParameter(searchBody).fullEquals("progId").fullLink("nameLike").value(), 
					searchBody.getPageOf().orderBy("PROG_ID").sortTypeAsc());
			this.setQueryResponseJsonResult(queryResult, result, searchBody.getPageOf());
		} catch (ServiceException | ControllerException e) {
			this.noSuccessResult(result, e);
		} catch (Exception e) {
			this.noSuccessResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}	
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0002D", check = true)
	@Operation(summary = "CORE_PROG001D0002 - delete", description = "刪除TB_SYS_PROG資料")
	@ResponseBody
	@PostMapping(value = "/delete", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<Boolean>> doDelete(@RequestBody TbSysProg sysProg) {
		DefaultControllerJsonResultObj<Boolean> result = this.initDefaultJsonResult();
		try {
			DefaultResult<Boolean> delResult = this.systemProgramLogicService.delete(sysProg);
			this.setDefaultResponseJsonResult(delResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}
	
	private void handlerCheck(DefaultControllerJsonResultObj<TbSysProg> result, TbSysProg sysProg) throws ControllerException, ServiceException, Exception {
		CheckControllerFieldHandler<TbSysProg> chk = this.getCheckControllerFieldHandler(result);
		chk
		.testField("progId", sysProg, "@org.apache.commons.lang3.StringUtils@isBlank(progId)", "請輸入程式編號")
		.testField("name", sysProg, "@org.apache.commons.lang3.StringUtils@isBlank(name)", "請輸入程式名稱")
		.testField("url", sysProg, "@org.apache.commons.lang3.StringUtils@isBlank(url)", "請輸入Url")		
		.testField("editMode", sysProg, "@org.apache.commons.lang3.StringUtils@isBlank(editMode)", "請輸入Edit mode")
		.testField("isDialog", sysProg, "@org.apache.commons.lang3.StringUtils@isBlank(isDialog)", "請輸入Dialog")		
		.testField("dialogW", sysProg, "null == dialogW", "請輸入Dialog寬")
		.testField("dialogH", sysProg, "null == dialogH", "請輸入Dialog高")
		.testField("progSystem", sysProg, "@org.apache.commons.lang3.StringUtils@isBlank(progSystem)", "請輸入系統")		
		.testField("itemType", sysProg, "@org.apache.commons.lang3.StringUtils@isBlank(itemType)", "請輸入Item類別")		
		.testField("icon", sysProg, "@org.apache.commons.lang3.StringUtils@isBlank(icon)", "請輸入ICON")
		.testField("fontIconClassId", sysProg, "@org.apache.commons.lang3.StringUtils@isBlank(fontIconClassId)", "請輸入ICON class")		
		.throwHtmlMessage();
		
		chk.testField("progId", sysProg, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09Id(sysId)", "程式編號只允許輸入0-9,a-z,A-Z正常字元");		
	}	
	
	private void save(DefaultControllerJsonResultObj<TbSysProg> result, TbSysProg sysProg) throws ControllerException, ServiceException, Exception {
		this.handlerCheck(result, sysProg);
		DefaultResult<TbSysProg> cResult = this.sysProgService.insert(sysProg);
		this.setDefaultResponseJsonResult(cResult, result);
	}	
	
	private void update(DefaultControllerJsonResultObj<TbSysProg> result, TbSysProg sysProg) throws ControllerException, ServiceException, Exception {
		this.handlerCheck(result, sysProg);
		DefaultResult<TbSysProg> cResult = this.sysProgService.update(sysProg);
		this.setDefaultResponseJsonResult(cResult, result);
	}	
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0002C", check = true)
	@Operation(summary = "CORE_PROG001D0002 - save", description = "新增TB_SYS_PROG資料")
	@ResponseBody
	@PostMapping(value = "/save", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbSysProg>> doSave(@RequestBody TbSysProg sysProg) {
		DefaultControllerJsonResultObj<TbSysProg> result = this.initDefaultJsonResult();
		try {
			this.save(result, sysProg);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}	
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0002E", check = true)
	@Operation(summary = "CORE_PROG001D0002 - load", description = "讀取TB_SYS_PROG資料")
	@ResponseBody
	@PostMapping(value = "/load", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbSysProg>> doLoad(@RequestBody TbSysProg sysProg) {
		DefaultControllerJsonResultObj<TbSysProg> result = this.initDefaultJsonResult();
		try {
			DefaultResult<TbSysProg> lResult = this.sysProgService.selectByEntityPrimaryKey(sysProg);
			this.setDefaultResponseJsonResult(lResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}
	
	@ControllerMethodAuthority(programId = "CORE_PROG001D0002U", check = true)
	@Operation(summary = "CORE_PROG001D0002 - update", description = "更新TB_SYS_PROG資料")
	@ResponseBody
	@PostMapping(value = "/update", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbSysProg>> doUpdate(@RequestBody TbSysProg sysProg) {
		DefaultControllerJsonResultObj<TbSysProg> result = this.initDefaultJsonResult();
		try {
			this.update(result, sysProg);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}		
	
}
