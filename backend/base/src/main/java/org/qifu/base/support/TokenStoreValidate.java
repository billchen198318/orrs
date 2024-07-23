package org.qifu.base.support;

public interface TokenStoreValidate {
	
	public boolean refreshValidate(String refreshTokenOrOID);
	
	public boolean accessValidate(String accessToken);
	
	public boolean refreshValidate(String refreshTokenOrOID, String userId);
	
	public boolean accessValidate(String accessToken, String userId);
	
}
