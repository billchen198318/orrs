package org.qifu.core.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.Constants;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.RolePermissionAttr;
import org.qifu.base.model.TokenBuilderVariable;
import org.qifu.base.model.YesNoKeyProvide;
import org.qifu.base.support.TokenStoreBuilder;
import org.qifu.base.support.TokenStoreValidate;
import org.qifu.base.support.TokenStoreValidateBuilder;
import org.qifu.base.util.TokenBuilderUtils;
import org.qifu.core.entity.TbAccount;
import org.qifu.core.entity.TbRolePermission;
import org.qifu.core.entity.TbSysCode;
import org.qifu.core.entity.TbUserRole;
import org.qifu.core.model.PermissionType;
import org.qifu.core.model.User;
import org.qifu.core.service.IAccountService;
import org.qifu.core.service.IRolePermissionService;
import org.qifu.core.service.ISysCodeService;
import org.qifu.core.service.IUserRoleService;
import org.qifu.core.support.JwtAuthLoginedUserRoleService;
import org.qifu.core.util.UserUtils;
import org.qifu.core.vo.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.Claim;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final AuthenticationManager authenticationManager;
	
	private final DataSource dataSource;
	
	private final ISysCodeService<TbSysCode, String> sysCodeService;
	
	private final JwtAuthLoginedUserRoleService jwtAuthLoginedUserRoleService;
	
	private final IUserRoleService<TbUserRole, String> userRoleService; 
	
	private final IRolePermissionService<TbRolePermission, String> rolePermissionService;	
	
	private final IAccountService<TbAccount, String> accountService;
	
	public AuthController(AuthenticationManager authenticationManager, DataSource dataSource,
			ISysCodeService<TbSysCode, String> sysCodeService,
			JwtAuthLoginedUserRoleService jwtAuthLoginedUserRoleService,
			IUserRoleService<TbUserRole, String> userRoleService,
			IRolePermissionService<TbRolePermission, String> rolePermissionService,
			IAccountService<TbAccount, String> accountService) {
		super();
		this.authenticationManager = authenticationManager;
		this.dataSource = dataSource;
		this.sysCodeService = sysCodeService;
		this.jwtAuthLoginedUserRoleService = jwtAuthLoginedUserRoleService;
		this.userRoleService = userRoleService;
		this.rolePermissionService = rolePermissionService;
		this.accountService = accountService;
	}
	
	@PostMapping("/validLogined")
	public ResponseEntity<User> validCheck(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
		User user = null;
	    try {
	    	Map<String, Object> param = new HashMap<>();
	    	TokenStoreValidate tsv = new TokenStoreValidateBuilder(this.dataSource);
	    	if (TokenBuilderUtils.verifyRefresh(loginRequest.getRefreshToken(), tsv)) {
	    		Map<String,Claim> userMapClaim = null;
	    		if ((userMapClaim = TokenBuilderUtils.verifyToken(loginRequest.getAccessToken(), tsv)) != null) {
	    			String userId = StringUtils.defaultString( userMapClaim.get(Constants.TOKEN_USER_PARAM_NAME).asString() );
	    			List<String> roleIds = new ArrayList<>();
	    			Map<String, List<RolePermissionAttr>> rolePermissionMap = new HashMap<>();
					param.clear();
					if (!StringUtils.isBlank(userId)) {
						param.put("account", userId);
						user = this.processOfValidCheck(param, roleIds, rolePermissionMap, userId, loginRequest);
					}	    			
	    		}
	    	}	    	
	    } catch (AuthenticationException | ServiceException e) {
	    	e.printStackTrace();
	    	throw e;
		}  catch (Exception e) {
			e.printStackTrace();
		}		
	    if (null == user) {
	    	user = new User("", "", YesNoKeyProvide.NO);
	    	user.setAccessToken("");
	    	user.setRefreshToken("");
	    }	    
	    return ResponseEntity.ok().body(user);
	}
	
	private User processOfValidCheck(Map<String, Object> param, List<String> roleIds, 
			Map<String, List<RolePermissionAttr>> rolePermissionMap, String userId,
			LoginRequest loginRequest) throws ServiceException {
		List<TbUserRole> urList = this.userRoleService.selectListByParams(param).getValue();
		for (int j = 0; urList != null && j < urList.size(); j++) {
			TbUserRole ur = urList.get(j);
			roleIds.add(ur.getRole());
			param.clear();
			param.put("role", ur.getRole());
			List<TbRolePermission> rpList = this.rolePermissionService.selectListByParams(param).getValue();
			rolePermissionMap.put(ur.getRole(), new ArrayList<>());
			List<RolePermissionAttr> permList = rolePermissionMap.get(ur.getRole());
			for (int x = 0; rpList != null && x < rpList.size(); x++) {
				TbRolePermission rp = rpList.get(x);
				if (!PermissionType.VIEW.name().equals(rp.getPermType())) {
					continue;
				}								
				RolePermissionAttr rpa = new RolePermissionAttr();
				rpa.setPermission(rp.getPermission());
				rpa.setType(rp.getPermType());
				permList.add(rpa);
			}
		}
		User user = UserUtils.setUserInfoForUserLocalUtils( userId, roleIds, rolePermissionMap );
		TbAccount acc = new TbAccount();
		acc.setAccount(userId);
		accountService.selectByUniqueKey(acc).getValueEmptyThrowMessage();											
		user.setAccessToken(loginRequest.getAccessToken());
		user.setRefreshToken(loginRequest.getRefreshToken());	
		return user;
	}
	
	@PostMapping("/signin")
	public ResponseEntity<User> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
		TokenBuilderVariable tbv = null;
		User user = null;
	    try {
			request.setAttribute(Constants.HTTP_REQUEST_PASSWORD_AUTH, loginRequest.getPassword());
		    Authentication authentication = authenticationManager.authenticate(
		    		new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		    SecurityContextHolder.getContext().setAuthentication(authentication);
		    user = (User) authentication.getPrincipal();
		    
		    TbSysCode sysCode = new TbSysCode();
		    sysCode.setCode(Constants.SYSCODE_TOKEN_CODE);		    
		    
			sysCode = sysCodeService.selectByUniqueKey(sysCode).getValue();
			if (null != sysCode && Constants.SYSCODE_TOKEN_TYPE.equals(sysCode.getType()) && !StringUtils.isBlank(sysCode.getParam1())) {								
			    tbv = TokenBuilderUtils.createToken(user.getUserId(), Constants.TOKEN_AUTH, sysCode.getParam1(), TokenStoreBuilder.build(this.dataSource));
				user.setAccessToken(tbv.getAccess());
				user.setRefreshToken(tbv.getRefresh());
				user.blankPassword();
				this.jwtAuthLoginedUserRoleService.onLoginedSuccess(authentication);
			}
	    } catch (AuthenticationException | ServiceException e) {
	    	e.printStackTrace();
	    	throw e;
		}  catch (Exception e) {
			e.printStackTrace();
		}		
	    if (null == user) {
	    	user = new User("", "", YesNoKeyProvide.NO);
	    	user.setAccessToken("");
	    	user.setRefreshToken("");	    	
	    }
	    if (null == tbv) {
	    	user.blankPassword();
	    	user.setUserId("");
	    }
	    return ResponseEntity.ok().body(user);
	}
	
	@PostMapping("/refreshNewToken")
	public ResponseEntity<LoginRequest> refreshNewToken(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
		TokenBuilderVariable tbv = null;
		TokenStoreValidateBuilder tsv = null;
		LoginRequest res = new LoginRequest();
		boolean refreshNew = false;
	    try {
	    	if (StringUtils.isBlank(loginRequest.getAccessToken()) || StringUtils.isBlank(loginRequest.getRefreshToken()) || StringUtils.isBlank(loginRequest.getUsername())) {
	    		throw new ControllerException( BaseSystemMessage.parameterBlank() );
	    	}
	    	tsv = TokenStoreValidateBuilder.build(this.dataSource);
	    	if (tsv.refreshValidate(loginRequest.getRefreshToken(), loginRequest.getUsername())) {
			    TbSysCode sysCode = new TbSysCode();
			    sysCode.setCode(Constants.SYSCODE_TOKEN_CODE);	
			    sysCode = sysCodeService.selectByUniqueKey(sysCode).getValue();
			    if (null != sysCode && Constants.SYSCODE_TOKEN_TYPE.equals(sysCode.getType()) && !StringUtils.isBlank(sysCode.getParam1())) {
			    	tbv = TokenBuilderUtils.createToken(loginRequest.getUsername(), Constants.TOKEN_AUTH, sysCode.getParam1(), TokenStoreBuilder.build(this.dataSource));
			    	res.setAccessToken(tbv.getAccess());
			    	res.setRefreshToken(tbv.getRefresh());
			    	res.setUsername(loginRequest.getUsername());
			    	refreshNew = true;
			    }
	    	}
	    } catch (AuthenticationException | ControllerException | ServiceException e) {
	    	e.printStackTrace();
	    	throw e;
		}  catch (Exception e) {
			e.printStackTrace();
		}
	    if (refreshNew) {
	    	return ResponseEntity.ok().body(res);
	    }
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
	}	
	
}
