package org.qifu.base.support;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class TokenStoreValidateBuilder implements TokenStoreValidate {
	
	private DataSource dataSource;
	
	private static final String CURRENT_DATE_PARAM = "currDate";
	
	private static final String HEAD_COUNT_COMMAND = "select count(*) from ";
	
	private final String refreshValidateSql = HEAD_COUNT_COMMAND + TokenStoreConfig.getTableName() + " where OID = :refreshTokenOrOID and RF_EXPIRES_DATE > :currDate ";
	
	private final String accessValidateSql = HEAD_COUNT_COMMAND + TokenStoreConfig.getTableName() + " where TOKEN = :accessToken and EXPIRES_DATE > :currDate ";
	
	private final String refreshValidateWithUserIdSql = HEAD_COUNT_COMMAND + TokenStoreConfig.getTableName() + " where OID = :refreshTokenOrOID and USER_ID = :userId and RF_EXPIRES_DATE > :currDate ";
	
	private final String accessValidateWithUserIdSql = HEAD_COUNT_COMMAND + TokenStoreConfig.getTableName() + " where TOKEN = :accessToken and USER_ID = :userId and EXPIRES_DATE > :currDate ";	
	
	public static TokenStoreValidateBuilder build(DataSource dataSource) {
		return new TokenStoreValidateBuilder(dataSource);
	}
	
	public TokenStoreValidateBuilder(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public boolean refreshValidate(String refreshTokenOrOID) {
		Date currDate = new Date();
		Map<String, Object> param = new HashMap<>();
		param.put("refreshTokenOrOID", refreshTokenOrOID);
		param.put(CURRENT_DATE_PARAM, currDate);
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
		return (jdbcTemplate.queryForObject(refreshValidateSql, param, Integer.class) > 0);
	}

	@Override
	public boolean accessValidate(String accessToken) {
		Date currDate = new Date();
		Map<String, Object> param = new HashMap<>();
		param.put("accessToken", accessToken);
		param.put(CURRENT_DATE_PARAM, currDate);
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
		return (jdbcTemplate.queryForObject(accessValidateSql, param, Integer.class) > 0);
	}

	@Override
	public boolean refreshValidate(String refreshTokenOrOID, String userId) {
		Date currDate = new Date();
		Map<String, Object> param = new HashMap<>();
		param.put("refreshTokenOrOID", refreshTokenOrOID);
		param.put("userId", userId);
		param.put(CURRENT_DATE_PARAM, currDate);
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
		return (jdbcTemplate.queryForObject(refreshValidateWithUserIdSql, param, Integer.class) > 0);
	}

	@Override
	public boolean accessValidate(String accessToken, String userId) {
		Date currDate = new Date();
		Map<String, Object> param = new HashMap<>();
		param.put("accessToken", accessToken);
		param.put("userId", userId);
		param.put(CURRENT_DATE_PARAM, currDate);
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
		return (jdbcTemplate.queryForObject(accessValidateWithUserIdSql, param, Integer.class) > 0);
	}
	
}
