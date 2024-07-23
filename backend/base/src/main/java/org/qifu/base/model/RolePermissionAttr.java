package org.qifu.base.model;

public class RolePermissionAttr implements java.io.Serializable {
	private static final long serialVersionUID = -8176999846345129588L;
	
	private String permission;
	private String type;
	
	public String getPermission() {
		return permission;
	}
	
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
}
