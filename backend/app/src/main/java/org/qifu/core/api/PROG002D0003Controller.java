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
import org.qifu.core.entity.TbRole;
import org.qifu.core.entity.TbSys;
import org.qifu.core.entity.TbSysProg;
import org.qifu.core.logic.IRoleLogicService;
import org.qifu.core.service.ISysProgService;
import org.qifu.core.service.ISysService;
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

@Tag(name = "CORE_PROG002D0003", description = "Role of menu.")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/PROG002D0003")
public class PROG002D0003Controller extends CoreApiSupport {
	private static final long serialVersionUID = -6375259391250545485L;
	
	@Autowired
	ISysService<TbSys, String> sysService;
	
	@Autowired
	ISysProgService<TbSysProg, String> sysProgService;
	
	@Autowired
	IRoleLogicService roleLogicService;
	
	@ControllerMethodAuthority(programId = "CORE_PROG002D0003Q", check = true)
	@Operation(summary = "CORE_PROG002D0003 - findProgramFolderMenuItem", description = "讀取程式清單TB_SYS_PROG List資料")
	@ResponseBody
	@PostMapping(value = "/findProgramFolderMenuItem/{sysId}", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<List<TbSysProg>>> findProgramFolderMenuItem(@PathVariable String sysId) {
		DefaultControllerJsonResultObj<List<TbSysProg>> result = this.initDefaultJsonResult();
		try {
			List<TbSysProg> menuProgList = this.sysProgService.findForInTheFolderMenuItems(sysId, null, null);
			result.setValue(menuProgList);
			result.setSuccess( YES );
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}	
	
	@ControllerMethodAuthority(programId = "CORE_PROG002D0003Q", check = true)
	@Operation(summary = "CORE_PROG002D0003 - findMenuProgramRoleList", description = "讀取程式Role List資料")
	@ResponseBody
	@PostMapping(value = "/findMenuProgramRoleList", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj< Map<String, List<TbRole>> >> findMenuProgramRoleList(@RequestBody TbSysProg prog) {
		DefaultControllerJsonResultObj< Map<String, List<TbRole>> > result = this.initDefaultJsonResult();
		try {
			Map<String, List<TbRole>> searchDataMap = this.roleLogicService.findForProgramRoleEnableAndAll(prog.getOid());
			result.setValue( searchDataMap );
			result.setSuccess( YES );
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}		
	
	@ControllerMethodAuthority(programId = "CORE_PROG002D0003U", check = true)
	@Operation(summary = "CORE_PROG002D0003 - update menu role - 1.", description = "更新選單ROLE資料 - 1")
	@ResponseBody
	@RequestMapping(value = "/updateMenuRole/{progOid}/{appendOid}", produces = MediaType.APPLICATION_JSON_VALUE)		
	public ResponseEntity<DefaultControllerJsonResultObj<Boolean>> updateUserRole(@PathVariable String progOid, @PathVariable String appendOid) {
		DefaultControllerJsonResultObj<Boolean> result = this.initDefaultJsonResult();
		try {
			DefaultResult<Boolean> uResult = this.roleLogicService.updateMenuRole(progOid, super.transformAppendKeyStringToList(appendOid));
			this.setDefaultResponseJsonResult(result, uResult);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}
	
	
}
