package org.qifu.core.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.qifu.base.Constants;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.RolePermissionAttr;
import org.qifu.base.model.ScriptTypeCode;
import org.qifu.base.model.UserRoleAndPermission;
import org.qifu.base.model.YesNoKeyProvide;
import org.qifu.core.entity.TbRolePermission;
import org.qifu.core.entity.TbSysLoginLog;
import org.qifu.core.entity.TbUserRole;
import org.qifu.core.model.PermissionType;
import org.qifu.core.model.User;
import org.qifu.core.service.IRolePermissionService;
import org.qifu.core.service.ISysLoginLogService;
import org.qifu.core.service.IUserRoleService;
import org.qifu.util.ScriptExpressionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Component
public class JwtAuthLoginedUserRoleService {
	private static final String CREATE_USER_DATA_LDAP_MODE_SCRIPT = "resource/create-user-data-ldap-mode.groovy";
	
	private static String createUserDataLdapModeScript = "";
	
	private ISysLoginLogService<TbSysLoginLog, String> sysLoginLogService;
	
	private IUserRoleService<TbUserRole, String> userRoleService;
    
	private IRolePermissionService<TbRolePermission, String> rolePermissionService;		
	
	public ISysLoginLogService<TbSysLoginLog, String> getSysLoginLogService() {
		return sysLoginLogService;
	}
	
	@Resource
	public void setSysLoginLogService(ISysLoginLogService<TbSysLoginLog, String> sysLoginLogService) {
		this.sysLoginLogService = sysLoginLogService;
	}

	public IUserRoleService<TbUserRole, String> getUserRoleService() {
		return userRoleService;
	}

	@Resource
	public void setUserRoleService(IUserRoleService<TbUserRole, String> userRoleService) {
		this.userRoleService = userRoleService;
	}

	public IRolePermissionService<TbRolePermission, String> getRolePermissionService() {
		return rolePermissionService;
	}

	@Resource
	public void setRolePermissionService(IRolePermissionService<TbRolePermission, String> rolePermissionService) {
		this.rolePermissionService = rolePermissionService;
	}
    
	public void onLoginedSuccess(Authentication authentication) {
		UserDetails user = (UserDetails) authentication.getPrincipal();
		try {
			if (user instanceof @SuppressWarnings("unused") User ux) {
				User u = (User) user;
				if (YesNoKeyProvide.YES.equals(u.getByLdap())) {
					this.processLdapAccountData(u);
				}
				List<TbUserRole> userRoleList = this.findUserRoleList(user.getUsername());
				List<UserRoleAndPermission> urapList = new ArrayList<>();
				this.fillOfOnLoginedSuccess(userRoleList, urapList);
				u.setRoles(urapList);
			}
			TbSysLoginLog loginLog = new TbSysLoginLog();
			loginLog.setUser(user.getUsername());
			this.sysLoginLogService.insert(loginLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void fillOfOnLoginedSuccess(List<TbUserRole> userRoleList, List<UserRoleAndPermission> urapList) {
		for (int i = 0; userRoleList != null && i < userRoleList.size(); i++) {
			TbUserRole ur = userRoleList.get(i);
			UserRoleAndPermission urap = new UserRoleAndPermission();
			urap.setRole(ur.getRole());
			List<TbRolePermission> rPermList = ur.getRolePermission();
			if (urap.getRolePermission() == null) {
				urap.setRolePermission(new ArrayList<>());
			}
			for (int j = 0; rPermList != null && j < rPermList.size(); j++) {
				if (!PermissionType.VIEW.name().equals(rPermList.get(j).getPermType())) {
					continue;
				}
				RolePermissionAttr rpa = new RolePermissionAttr();
				rpa.setPermission(rPermList.get(j).getPermission());
				rpa.setType(rPermList.get(j).getPermType());
				urap.getRolePermission().add(rpa);
			}
			urapList.add(urap);
		}		
	}
	
    private List<TbUserRole> findUserRoleList(String username) throws ServiceException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("account", username);
        DefaultResult<List<TbUserRole>> result = userRoleService.selectListByParams(paramMap);
        List<TbUserRole> roleList = result.getValue();
        for (int i = 0; roleList != null && i < roleList.size(); i++) {
        	TbUserRole userRole = roleList.get(i);
        	paramMap.clear();
        	paramMap.put("role", userRole.getRole());
        	try {
				DefaultResult<List<TbRolePermission>> permResult = rolePermissionService.selectListByParams(paramMap);
				userRole.setRolePermission( permResult.getValue() );
			} catch (Exception e) {
				e.printStackTrace();
			}
        	if (userRole.getRolePermission() == null) {
        		userRole.setRolePermission( new ArrayList<>() );
        	}
        }
        paramMap.clear();
        return roleList;
    }
    
    private void processLdapAccountData(User user) {
    	Map<String, Object> paramMap = new HashMap<>();
    	paramMap.put("user", user);
    	try {
			ScriptExpressionUtils.execute(ScriptTypeCode.GROOVY, getCreateUserDataLdapModeScript(), null, paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	paramMap.clear();
    }
    
	public static String getCreateUserDataLdapModeScript() throws IOException {
		if ( !StringUtils.isBlank(createUserDataLdapModeScript) ) {
			return createUserDataLdapModeScript;
		}
		try (InputStream is = JwtAuthLoginedUserRoleService.class.getClassLoader().getResource( CREATE_USER_DATA_LDAP_MODE_SCRIPT ).openStream()) {
			createUserDataLdapModeScript = IOUtils.toString(is, Constants.BASE_ENCODING);
		}
		return createUserDataLdapModeScript;
	}        
    
}
