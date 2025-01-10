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
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchBody;
import org.qifu.core.entity.TbSysEventLog;
import org.qifu.core.service.ISysEventLogService;
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

@Tag(name = "CORE_PROG004D0001", description = "Event log.")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@ResponseBody
@RequestMapping("/api/PROG004D0001")
public class PROG004D0001Controller extends CoreApiSupport {
	private static final long serialVersionUID = 1711431436491423905L;
	
	private final transient ISysEventLogService<TbSysEventLog, String> sysEventLogService;
	
	public PROG004D0001Controller(ISysEventLogService<TbSysEventLog, String> sysEventLogService) {
		super();
		this.sysEventLogService = sysEventLogService;
	}
	
	@ControllerMethodAuthority(programId = "CORE_PROG004D0001Q", check = true)
	@Operation(summary = "CORE_PROG004D0001 - findPage", description = "查詢TB_SYS_EVENT_LOG資料")
	@PostMapping(value = "/findPage", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<QueryResult<List<TbSysEventLog>>> findPage(@RequestBody SearchBody searchBody) {
		QueryResult<List<TbSysEventLog>> result = this.initResult();
		try {
			QueryResult<List<TbSysEventLog>> queryResult = this.sysEventLogService.findPage(
					this.queryParameter(searchBody).fullEquals("user").value(), 
					searchBody.getPageOf().orderBy("CDATE").sortTypeDesc());
			this.setQueryResponseJsonResult(queryResult, result, searchBody.getPageOf());
		} catch (ServiceException | ControllerException e) {
			this.noSuccessResult(result, e);
		} 
		return ResponseEntity.ok().body(result);
	}		
	
	@ControllerMethodAuthority(programId = "CORE_PROG004D0001D", check = true)
	@Operation(summary = "CORE_PROG004D0001 - delete", description = "刪除TB_SYS_EVENT_LOG資料")
	@PostMapping(value = "/delete", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<Boolean>> doDelete(@RequestBody TbSysEventLog log) {
		DefaultControllerJsonResultObj<Boolean> result = this.initDefaultJsonResult();
		try {
			DefaultResult<Boolean> delResult = this.sysEventLogService.delete(log);
			this.setDefaultResponseJsonResult(delResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} 
		return ResponseEntity.ok().body(result);
	}
	
	@ControllerMethodAuthority(programId = "CORE_PROG004D0001D", check = true)
	@Operation(summary = "CORE_PROG004D0001 - deleteAll", description = "刪除TB_SYS_EVENT_LOG所有資料")
	@PostMapping(value = "/deleteAll", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<Boolean>> doDeleteAll() {
		DefaultControllerJsonResultObj<Boolean> result = this.initDefaultJsonResult();
		try {
			result.setValue( this.sysEventLogService.deleteAll() );
			result.setSuccess( YES );
			result.setMessage( BaseSystemMessage.deleteSuccess() );
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} 
		return ResponseEntity.ok().body(result);
	}	
	
}
