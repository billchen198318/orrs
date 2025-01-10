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
package org.qifu.base.util;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.Constants;
import org.qifu.base.model.TokenBuilderVariable;
import org.qifu.base.support.TokenStore;
import org.qifu.base.support.TokenStoreValidate;
import org.qifu.util.SimpleUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class TokenBuilderUtils {
	
	protected TokenBuilderUtils() {
		throw new IllegalStateException("Utils class: TokenBuilderUtils");
	}
	
	public static boolean existsInfo(Map<String, Claim> claims) {
		if (null == claims) {
			return false;
		}
		return claims.get(Constants.TOKEN_USER_PARAM_NAME) != null;
	}
	
	public static boolean verifyRefresh(String refreshToken, TokenStoreValidate storeValidate ) {
		return storeValidate.refreshValidate(refreshToken);
	}
	
	public static Map<String, Claim> verifyToken(String token, TokenStoreValidate storeValidate ) {
		if (StringUtils.isBlank(token)) {
			return Collections.emptyMap();
		}
		DecodedJWT jwt = null;
		try {
			if (!storeValidate.accessValidate(token)) {
				return Collections.emptyMap();
			}
			JWTVerifier verifier = JWT.require(Algorithm.HMAC256(Constants.TOKEN_SECRET)).withIssuer(Constants.TOKEN_ISSUER).build();
			jwt = verifier.verify(token);
		} catch (SignatureVerificationException sve) {
			sve.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (jwt != null && jwt.getClaims() != null) ? jwt.getClaims() : null;
	}	
	
	public static TokenBuilderVariable createToken(String userId, String subject, String clientId, TokenStore store) {
		Date iatDate = new Date();
		
	    // expire time
	    Calendar setTime1 = Calendar.getInstance();
	    Calendar setTime2 = Calendar.getInstance();
	    setTime1.add(Calendar.MINUTE, Constants.TOKEN_ACCESS_EXPIRED_INTERVAL);
	    setTime2.add(Calendar.MINUTE, Constants.TOKEN_REFRESH_EXPIRED_INTERVAL);
	    Date expiresDate1 = setTime1.getTime();
	    Date expiresDate2 = setTime2.getTime();	    
	    
	    // Header Message
	    Map<String, Object> headerMap = new HashMap<>();
	    headerMap.put("alg", "HS256");
	    headerMap.put("typ", "JWT");
	    
	    TokenBuilderVariable tbv = new TokenBuilderVariable();
	    
	    String token = JWT.create()
	    		.withHeader(headerMap)
	    		.withIssuer(Constants.TOKEN_ISSUER)
	    		.withAudience(clientId)
	    		.withSubject(subject)
	    		.withIssuedAt(iatDate)
	    		.withExpiresAt(expiresDate1)
	    		.withClaim(Constants.TOKEN_USER_PARAM_NAME, StringUtils.defaultString(userId))
	    		.withClaim("currentTimeMillis", String.valueOf(System.currentTimeMillis()))
	    		.sign(Algorithm.HMAC256(Constants.TOKEN_SECRET));
	    tbv.setAccess(token);
	    tbv.setRefresh(SimpleUtils.getUUIDStr());
	    if (null != store) {
	    	store.save(tbv.getRefresh(), tbv.getAccess(), userId, expiresDate1, expiresDate2);
	    }
	    return tbv;
	}
	
}
