package org.qifu.core.vo;

public class BoardDemo implements java.io.Serializable {
	private static final long serialVersionUID = 7212364997556842953L;
	
	private String userId = "";
	private Long countSize = 0L;
	private String date = "";
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public Long getCountSize() {
		return countSize;
	}
	
	public void setCountSize(Long countSize) {
		this.countSize = countSize;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
