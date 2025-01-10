package org.qifu.core.api;

import java.util.List;

import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchBody;
import org.qifu.core.entity.TbSysToken;
import org.qifu.core.service.ISysTokenService;
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

@Tag(name = "CORE_PROG004D0002", description = "Token log.")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@ResponseBody
@RequestMapping("/api/PROG004D0002")
public class PROG004D0002Controller extends CoreApiSupport {
	private static final long serialVersionUID = -1512324626977181535L;
	
	private final transient ISysTokenService<TbSysToken, String> sysTokenService;
	
	public PROG004D0002Controller(ISysTokenService<TbSysToken, String> sysTokenService) {
		super();
		this.sysTokenService = sysTokenService;
	}
	
	@ControllerMethodAuthority(programId = "CORE_PROG004D0002Q", check = true)
	@Operation(summary = "CORE_PROG004D0002 - findPage", description = "查詢TB_SYS_EVENT_LOG資料")
	@PostMapping(value = "/findPage", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<QueryResult<List<TbSysToken>>> findPage(@RequestBody SearchBody searchBody) {
		QueryResult<List<TbSysToken>> result = this.initResult();
		try {
			QueryResult<List<TbSysToken>> queryResult = this.sysTokenService.findPage(
					this.queryParameter(searchBody).fullEquals("userId").value(), 
					searchBody.getPageOf().orderBy("CDATE").sortTypeDesc());
			this.setQueryResponseJsonResult(queryResult, result, searchBody.getPageOf());
		} catch (ServiceException | ControllerException e) {
			this.noSuccessResult(result, e);
		} 
		return ResponseEntity.ok().body(result);
	}		
	
	@ControllerMethodAuthority(programId = "CORE_PROG004D0002D", check = true)
	@Operation(summary = "CORE_PROG004D0002 - delete", description = "刪除TB_SYS_EVENT_LOG資料")
	@PostMapping(value = "/delete", produces = {MediaType.APPLICATION_JSON_VALUE})	
	public ResponseEntity<DefaultControllerJsonResultObj<Boolean>> doDelete(@RequestBody TbSysToken log) {
		DefaultControllerJsonResultObj<Boolean> result = this.initDefaultJsonResult();
		try {
			DefaultResult<Boolean> delResult = this.sysTokenService.delete(log);
			this.setDefaultResponseJsonResult(delResult, result);
		} catch (ServiceException | ControllerException e) {
			this.exceptionResult(result, e);
		} 
		return ResponseEntity.ok().body(result);
	}	
	
}
