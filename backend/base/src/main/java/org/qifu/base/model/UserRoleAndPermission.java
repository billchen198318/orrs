package org.qifu.base.model;

import java.util.List;

public class UserRoleAndPermission implements java.io.Serializable {
	private static final long serialVersionUID = -7811595650387118293L;
	
	private String role;
	
	private List<RolePermissionAttr> rolePermission;
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}

	public List<RolePermissionAttr> getRolePermission() {
		return rolePermission;
	}

	public void setRolePermission(List<RolePermissionAttr> rolePermission) {
		this.rolePermission = rolePermission;
	}
	
}
