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
import java.util.Map;

import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.core.entity.TbAccount;
import org.qifu.core.entity.TbRole;
import org.qifu.core.logic.IRoleLogicService;
import org.qifu.core.service.IAccountService;
import org.qifu.core.util.CoreApiSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "CORE_PROG002D0002", description = "Role of user.")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/PROG002D0002")
public class PROG002D0002Controller extends CoreApiSupport {
	private static final long serialVersionUID = 2856536977765326140L;
	
	@Autowired
	IAccountService<TbAccount, String> accountService;
	
	@Autowired
	IRoleLogicService roleLogicService;
	
	@ControllerMethodAuthority(programId = "CORE_PROG002D0002Q", check = true)
	@Operation(summary = "CORE_PROG002D0002 - loadUserList", description = "讀取TC_ACCOUNT資料")
	@ResponseBody
	@PostMapping(value = "/loadUserList", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<List<TbAccount>>> doLoadUserList() {
		DefaultControllerJsonResultObj<List<TbAccount>> result = this.initDefaultJsonResult();
		try {
			DefaultResult<List<TbAccount>> lResult = this.accountService.selectList();
			this.setDefaultResponseJsonResult(lResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}		
	
	@ControllerMethodAuthority(programId = "CORE_PROG002D0002Q", check = true)
	@Operation(summary = "CORE_PROG002D0002 - findUserRoleListByAccountOid", description = "讀取使用者TB_ROLE資料")
	@ResponseBody
	@PostMapping(value = "/findUserRoleListByAccountOid", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<Map<String, List<TbRole>>>> findUserRoleListByAccountOid(@RequestBody TbAccount account) {
		DefaultControllerJsonResultObj<Map<String, List<TbRole>>> result = this.initDefaultJsonResult();
		try {
			Map<String, List<TbRole>> searchDataMap = this.roleLogicService.findForAccountRoleEnableAndAll(account.getOid());
			result.setValue(searchDataMap);
			result.setSuccess( YES );
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}		
	
	@ControllerMethodAuthority(programId = "CORE_PROG002D0002U", check = true)
	@Operation(summary = "CORE_PROG002D0002 - update user role - 1.", description = "更新使用者ROLE資料 - 1")
	@ResponseBody
	@RequestMapping(value = "/updateUserRole/{accountOid}", produces = MediaType.APPLICATION_JSON_VALUE)		
	public ResponseEntity<DefaultControllerJsonResultObj<Boolean>> updateUserRole(@PathVariable String accountOid) {
		DefaultControllerJsonResultObj<Boolean> result = this.initDefaultJsonResult();
		try {
			DefaultResult<Boolean> uResult = this.roleLogicService.updateUserRole(accountOid, this.transformAppendKeyStringToList(""));
			this.setDefaultResponseJsonResult(result, uResult);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}	
	
	@ControllerMethodAuthority(programId = "CORE_PROG002D0002U", check = true)
	@Operation(summary = "CORE_PROG002D0002 - update user role - 2.", description = "更新使用者ROLE資料 - 2")
	@ResponseBody
	@RequestMapping(value = "/updateUserRole/{accountOid}/{roleAppendOid}", produces = MediaType.APPLICATION_JSON_VALUE)		
	public ResponseEntity<DefaultControllerJsonResultObj<Boolean>> updateUserRole(@PathVariable String accountOid, @PathVariable String roleAppendOid) {
		DefaultControllerJsonResultObj<Boolean> result = this.initDefaultJsonResult();
		try {
			DefaultResult<Boolean> uResult = this.roleLogicService.updateUserRole(accountOid, this.transformAppendKeyStringToList(roleAppendOid));
			this.setDefaultResponseJsonResult(result, uResult);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}
	
}
