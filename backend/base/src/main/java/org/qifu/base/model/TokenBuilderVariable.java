package org.qifu.base.model;

public class TokenBuilderVariable implements java.io.Serializable {
	private static final long serialVersionUID = 7337476628786779101L;
	
	private String refresh;
	
	private String access;

	public String getRefresh() {
		return refresh;
	}

	public void setRefresh(String refresh) {
		this.refresh = refresh;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	@Override
	public String toString() {
		return "TokenBuilderVariable [refresh=" + refresh + ", access=" + access + "]";
	}
	
}
