package org.qifu.core.api;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.QueryResult;
import org.qifu.core.entity.TbSysMenu;
import org.qifu.core.entity.TbSysProg;
import org.qifu.core.service.ISysMenuService;
import org.qifu.core.service.ISysProgService;
import org.qifu.core.util.CoreApiSupport;
import org.qifu.core.util.UserUtils;
import org.qifu.core.vo.ProgramAndMenuResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "MENU", description = "選單資料.")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/menu")
public class BaseHelperApiController extends CoreApiSupport {
	private static final long serialVersionUID = -769226894308818831L;
	
	@Autowired
	ISysMenuService<TbSysMenu, String> sysMenuService;
	
	@Autowired
	ISysProgService<TbSysProg, String> sysProgService;
	
	@Operation(summary = "選單與程式清單", description = "左邊選單項目與程式清單")
	@PostMapping(value = "/getMemuItemAndProgList", produces = {MediaType.APPLICATION_JSON_VALUE})
	public QueryResult<ProgramAndMenuResult> getMemuItem() {
		QueryResult<ProgramAndMenuResult> result = this.initResult();
		ProgramAndMenuResult r = new ProgramAndMenuResult();
		result.setValue(r);
		try {
			if (UserUtils.isAdmin()) {
				r.setMenuList( this.sysMenuService.getMemuItemListForFrontend("") );
				//r.setProgramList( this.sysProgService.findForInThePermRoleOfUserId("") );				
			} else {
				r.setMenuList( this.sysMenuService.getMemuItemListForFrontend(UserUtils.getCurrentUser().getUserId()) );
				//r.setProgramList( this.sysProgService.findForInThePermRoleOfUserId(UserUtils.getCurrentUser().getUserId()) );
			}			
			r.setProgramList( this.sysProgService.findForInThePermRoleOfUserId("") );
			result.setSuccess( YES );
		} catch (ServiceException | AuthorityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (CollectionUtils.isEmpty(r.getMenuList())) {
				r.setMenuList( new ArrayList<Map<String, Object>>());
			}
			if (CollectionUtils.isEmpty(r.getProgramList())) {
				r.setProgramList(new ArrayList<TbSysProg>());
			}
		}
		return result;
	}	
	
}
