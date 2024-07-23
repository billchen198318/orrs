package org.qifu.base.support;

import java.util.Date;

public interface TokenStore {
	
	public void save(String refreshToken, String accessToken, String userId, Date expiresDate, Date refreshExpiresDate);
	
}
