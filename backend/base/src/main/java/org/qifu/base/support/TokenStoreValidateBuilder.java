package org.qifu.base.support;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class TokenStoreValidateBuilder implements TokenStoreValidate {
	
	private DataSource dataSource;
	
	private static String _refreshValidateSql = "select count(*) from " + TokenStoreConfig.getTableName() + " where OID = :refreshTokenOrOID and RF_EXPIRES_DATE > :currDate ";
	
	private static String _accessValidateSql = "select count(*) from " + TokenStoreConfig.getTableName() + " where TOKEN = :accessToken and EXPIRES_DATE > :currDate ";
	
	private static String _refreshValidateWithUserIdSql = "select count(*) from " + TokenStoreConfig.getTableName() + " where OID = :refreshTokenOrOID and USER_ID = :userId and RF_EXPIRES_DATE > :currDate ";
	
	private static String _accessValidateWithUserIdSql = "select count(*) from " + TokenStoreConfig.getTableName() + " where TOKEN = :accessToken and USER_ID = :userId and EXPIRES_DATE > :currDate ";	
	
	public static TokenStoreValidateBuilder build(DataSource dataSource) {
		TokenStoreValidateBuilder b = new TokenStoreValidateBuilder(dataSource);
		return b;
	}
	
	public TokenStoreValidateBuilder(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public boolean refreshValidate(String refreshTokenOrOID) {
		Date currDate = new Date();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("refreshTokenOrOID", refreshTokenOrOID);
		param.put("currDate", currDate);
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
		if (jdbcTemplate.queryForObject(_refreshValidateSql, param, Integer.class) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean accessValidate(String accessToken) {
		Date currDate = new Date();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("accessToken", accessToken);
		param.put("currDate", currDate);
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
		if (jdbcTemplate.queryForObject(_accessValidateSql, param, Integer.class) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean refreshValidate(String refreshTokenOrOID, String userId) {
		Date currDate = new Date();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("refreshTokenOrOID", refreshTokenOrOID);
		param.put("userId", userId);
		param.put("currDate", currDate);
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
		if (jdbcTemplate.queryForObject(_refreshValidateWithUserIdSql, param, Integer.class) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean accessValidate(String accessToken, String userId) {
		Date currDate = new Date();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("accessToken", accessToken);
		param.put("userId", userId);
		param.put("currDate", currDate);
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
		if (jdbcTemplate.queryForObject(_accessValidateWithUserIdSql, param, Integer.class) > 0) {
			return true;
		}
		return false;
	}
	
}
