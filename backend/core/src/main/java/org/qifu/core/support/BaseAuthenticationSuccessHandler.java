/* 
 * Copyright 2019-2021 qifu of copyright Chen Xin Nien
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class BaseAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	private static final String CREATE_USER_DATA_LDAP_MODE_SCRIPT = "resource/create-user-data-ldap-mode.groovy";
	
	private static String createUserDataLdapModeScript = "";
	
	private final ISysLoginLogService<TbSysLoginLog, String> sysLoginLogService;
	
	private final IUserRoleService<TbUserRole, String> userRoleService;
    
	private final IRolePermissionService<TbRolePermission, String> rolePermissionService;	
    
	public BaseAuthenticationSuccessHandler(ISysLoginLogService<TbSysLoginLog, String> sysLoginLogService,
			IUserRoleService<TbUserRole, String> userRoleService,
			IRolePermissionService<TbRolePermission, String> rolePermissionService) {
		super();
		this.sysLoginLogService = sysLoginLogService;
		this.userRoleService = userRoleService;
		this.rolePermissionService = rolePermissionService;
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		UserDetails user = (UserDetails) authentication.getPrincipal();
		try {
			if (user instanceof @SuppressWarnings("unused") User ux) {
				User u = (User) user;
				if (YesNoKeyProvide.YES.equals(u.getByLdap())) {
					this.processLdapAccountData(u);
				}
				List<TbUserRole> userRoleList = this.findUserRoleList(user.getUsername());
				List<UserRoleAndPermission> urapList = new ArrayList<>();
				this.fillOfOnAuthenticationSuccess(userRoleList, urapList);
				u.setRoles(urapList);
			}
			TbSysLoginLog loginLog = new TbSysLoginLog();
			loginLog.setUser(user.getUsername());
			this.sysLoginLogService.insert(loginLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect("/index");
	}
	
	private void fillOfOnAuthenticationSuccess(List<TbUserRole> userRoleList, List<UserRoleAndPermission> urapList) {
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
	
    private List<TbUserRole> findUserRoleList(String username) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("account", username);
        DefaultResult<List<TbUserRole>> result = null;
        try {
            result = userRoleService.selectListByParams(paramMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == result) {
        	return new ArrayList<>();
        }
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
		try (InputStream is = BaseAuthenticationSuccessHandler.class.getClassLoader().getResource( CREATE_USER_DATA_LDAP_MODE_SCRIPT ).openStream()) {
			createUserDataLdapModeScript = IOUtils.toString(is, Constants.BASE_ENCODING);
		}
		return createUserDataLdapModeScript;
	}    
    
}
