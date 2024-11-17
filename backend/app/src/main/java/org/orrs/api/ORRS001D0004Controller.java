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

import org.orrs.entity.TbOrrsDoc;
import org.orrs.logic.IOrrsLogicService;
import org.orrs.service.IOrrsDocService;
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

@Tag(name = "ORRS001D0004", description = "llm documents result.")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ORRS001D0004")
public class ORRS001D0004Controller extends CoreApiSupport {
	private static final long serialVersionUID = -3457033358506339761L;
	
	@Autowired
	IOrrsDocService<TbOrrsDoc, String> orrsDocService;
	
	@Autowired
	IOrrsLogicService orrsLogicService;
	
	@ControllerMethodAuthority(programId = "ORRS001D0004Q", check = true)
	@Operation(summary = "ORRS001D0004 - findPage", description = "查核tb_orrs_doc資料")
	@ResponseBody
	@PostMapping(value = "/findPage", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<QueryResult<List<TbOrrsDoc>>> findPage(@RequestBody SearchBody searchBody) {
		QueryResult<List<TbOrrsDoc>> result = this.initResult();
		try {
			QueryResult<List<TbOrrsDoc>> queryResult = this.orrsDocService.findPage(
					this.queryParameter(searchBody).fullEquals("docId").fullLink("nameLike").value(), 
					searchBody.getPageOf().orderBy("DOC_ID").sortTypeAsc());
			this.setQueryResponseJsonResult(queryResult, result, searchBody.getPageOf());
		} catch (ServiceException | ControllerException e) {
			this.noSuccessResult(result, e);
		} catch (Exception e) {
			this.noSuccessResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}
	
	private void handlerCheck(DefaultControllerJsonResultObj<TbOrrsDoc> result, TbOrrsDoc doc) throws ControllerException, ServiceException, Exception {
		CheckControllerFieldHandler<TbOrrsDoc> chk = this.getCheckControllerFieldHandler(result);
		chk.testField("docId", doc, "@org.apache.commons.lang3.StringUtils@isBlank(docId)", "請輸入編號")
		.testField("name", doc, "@org.apache.commons.lang3.StringUtils@isBlank(name)", "請輸入名稱")
		.testField("content", doc, "@org.apache.commons.lang3.StringUtils@isBlank(content)", "請輸入文件內容")
		.testField("sysPromptTpl", doc, "@org.apache.commons.lang3.StringUtils@isBlank(sysPromptTpl)", "請輸入System prompt template內容")
		.testField("tplVariable", doc, "@org.apache.commons.lang3.StringUtils@isBlank(tplVariable)", "請輸入template variable name")
		.throwHtmlMessage();
		
		chk.testField("docId", doc, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09Id(docId)", "編號只允許輸入0-9,a-z,A-Z正常字元")
		.testField("docId", doc, "docId.length() > 10", "編號長度超過限制")
		.throwHtmlMessage();		
	}
	
	private void save(DefaultControllerJsonResultObj<TbOrrsDoc> result, TbOrrsDoc doc) throws ControllerException, ServiceException, Exception {
		this.handlerCheck(result, doc);
		DefaultResult<TbOrrsDoc> cResult = this.orrsLogicService.createDocument(doc);
		this.setDefaultResponseJsonResult(cResult, result);
	}
	
	private void update(DefaultControllerJsonResultObj<TbOrrsDoc> result, TbOrrsDoc doc) throws ControllerException, ServiceException, Exception {
		this.handlerCheck(result, doc);
		DefaultResult<TbOrrsDoc> uResult = this.orrsLogicService.updateDocument(doc);
		this.setDefaultResponseJsonResult(uResult, result);
	}	
	
	@ControllerMethodAuthority(programId = "ORRS001D0004C", check = true)
	@Operation(summary = "ORRS001D0004C - save", description = "新增tb_orrs_doc資料")
	@ResponseBody
	@PostMapping(value = "/save", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbOrrsDoc>> doSave(@RequestBody TbOrrsDoc doc) {
		DefaultControllerJsonResultObj<TbOrrsDoc> result = this.initDefaultJsonResult();
		try {
			this.save(result, doc);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}		
	
	@ControllerMethodAuthority(programId = "ORRS001D0004D", check = true)
	@Operation(summary = "ORRS001D0004D - delete", description = "刪除tb_orrs_doc資料")
	@ResponseBody
	@PostMapping(value = "/delete", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<Boolean>> doDelete(@RequestBody TbOrrsDoc doc) {
		DefaultControllerJsonResultObj<Boolean> result = this.initDefaultJsonResult();
		try {
			DefaultResult<Boolean> delResult = this.orrsLogicService.deleteDocument(doc);
			this.setDefaultResponseJsonResult(delResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}
	
	@ControllerMethodAuthority(programId = "ORRS001D0004E", check = true)
	@Operation(summary = "ORRS001D0004E - load", description = "讀取tb_orrs_doc資料")
	@ResponseBody
	@PostMapping(value = "/load", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbOrrsDoc>> doLoad(@RequestBody TbOrrsDoc doc) {
		DefaultControllerJsonResultObj<TbOrrsDoc> result = this.initDefaultJsonResult();
		try {
			DefaultResult<TbOrrsDoc> lResult = this.orrsDocService.selectByEntityPrimaryKey(doc);
			this.setDefaultResponseJsonResult(lResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}	
	
	@ControllerMethodAuthority(programId = "ORRS001D0004U", check = true)
	@Operation(summary = "ORRS001D0004U - update", description = "更新tb_orrs_doc資料")
	@ResponseBody
	@PostMapping(value = "/update", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<TbOrrsDoc>> doUpdate(@RequestBody TbOrrsDoc doc) {
		DefaultControllerJsonResultObj<TbOrrsDoc> result = this.initDefaultJsonResult();
		try {
			this.update(result, doc);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}	
	
}
