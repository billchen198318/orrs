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
package org.qifu.core.interceptor;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.Constants;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.RolePermissionAttr;
import org.qifu.base.support.TokenStoreValidateBuilder;
import org.qifu.base.util.TokenBuilderUtils;
import org.qifu.core.entity.TbRolePermission;
import org.qifu.core.entity.TbSysCode;
import org.qifu.core.entity.TbUserRole;
import org.qifu.core.service.IRolePermissionService;
import org.qifu.core.service.ISysCodeService;
import org.qifu.core.service.IUserRoleService;
import org.qifu.core.util.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

//import com.auth0.jwt.impl.PublicClaims;
import com.auth0.jwt.interfaces.Claim;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserBuilderInterceptor implements HandlerInterceptor {
	
	protected static Logger logger = LoggerFactory.getLogger(UserBuilderInterceptor.class);
	
	@Autowired
	ISysCodeService<TbSysCode, String> sysCodeService;	
	
	@Autowired
	IUserRoleService<TbUserRole, String> userRoleService; 
	
	@Autowired
	IRolePermissionService<TbRolePermission, String> rolePermissionService;
	
	@Autowired
	private DataSource dataSource;	
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (request.getRequestURL().indexOf(Constants.TOKEN_CHECK_URL_PATH) == -1) {
			return true;
		}
		String authorization = StringUtils.defaultString(request.getHeader( Constants.TOKEN_Authorization )).trim();
		if (!authorization.startsWith(Constants.TOKEN_PREFIX)) {
			logger.warn(">>> No authorization uri: " + request.getRequestURI() + " , remote-address: " + request.getRemoteAddr() + " , remote-port: " + request.getRemotePort());
			response.setCharacterEncoding( Constants.BASE_ENCODING );
			response.setStatus(401); // 2023-08-04 add , 讓前端的 axios interceptor 去接
			response.getWriter().print( "{ \"success\":\"N\",\"message\":\"No authorization head " + Constants.TOKEN_PREFIX + "\",\"login\":\"N\",\"isAuthorize\":\"N\" }" );
			response.getWriter().flush();
			response.getWriter().close();						
			return false;
		}
		
		TokenStoreValidateBuilder tsv = TokenStoreValidateBuilder.build(this.dataSource);
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		Map<String, Claim> claimToken = TokenBuilderUtils.verifyToken(authorization.replaceFirst(Constants.TOKEN_PREFIX, "").replaceAll(" ", ""), tsv);
		if (TokenBuilderUtils.existsInfo(claimToken)) {
			//String clientId = StringUtils.defaultString( claimToken.get(PublicClaims.AUDIENCE).asString() );
			String userId = StringUtils.defaultString( claimToken.get(Constants.TOKEN_USER_PARAM_NAME).asString() );
			List<String> roleIds = new ArrayList<String>();
			Map<String, List<RolePermissionAttr>> rolePermissionMap = new HashMap<String, List<RolePermissionAttr>>();
			try {
				param.clear();
				if (!StringUtils.isBlank(userId)) {
					param.put("account", userId);
					List<TbUserRole> urList = this.userRoleService.selectListByParams(param).getValue();
					for (int j = 0; urList != null && j < urList.size(); j++) {
						TbUserRole ur = urList.get(j);
						roleIds.add(ur.getRole());
						param.clear();
						param.put("role", ur.getRole());
						List<TbRolePermission> rpList = this.rolePermissionService.selectListByParams(param).getValue();
						rolePermissionMap.put(ur.getRole(), new ArrayList<RolePermissionAttr>());
						List<RolePermissionAttr> permList = rolePermissionMap.get(ur.getRole());
						for (int x = 0; rpList != null && x < rpList.size(); x++) {
							TbRolePermission rp = rpList.get(x);
							RolePermissionAttr rpa = new RolePermissionAttr();
							rpa.setPermission(rp.getPermission());
							rpa.setType(rp.getPermType());
							permList.add(rpa);
						}
					}
				}				
			} catch (ServiceException se) {
				se.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}			
			UserUtils.setUserInfoForUserLocalUtils( claimToken.get(Constants.TOKEN_USER_PARAM_NAME).asString(), roleIds, rolePermissionMap);
			logger.info("User builder from JWT Authorization header : " + claimToken.get(Constants.TOKEN_USER_PARAM_NAME).asString() + " , role: " + roleIds);
		}
		if ( UserUtils.getCurrentUser() == null ) {
			logger.warn(">>> No authorization uri: " + request.getRequestURI() + " , remote-address: " + request.getRemoteAddr() + " , remote-port: " + request.getRemotePort());
			response.setCharacterEncoding( Constants.BASE_ENCODING );
			response.setStatus(401); // 2023-08-04 add , 讓前端的 axios interceptor 去接
			response.getWriter().print( Constants.NO_LOGIN_JSON_DATA );
			response.getWriter().flush();
			response.getWriter().close();			
			return false;
		}
		return true;
	}
	
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    	
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    	UserUtils.removeForUserLocalUtils();
    }
	
}
