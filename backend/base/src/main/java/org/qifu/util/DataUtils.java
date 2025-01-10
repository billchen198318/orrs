/* 
 * Copyright 2012-2017 qifu of copyright Chen Xin Nien
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
package org.qifu.util;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.ArrayUtils;
import org.qifu.base.AppContext;
import org.qifu.base.model.PleaseSelect;
import org.springframework.beans.BeansException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import ognl.OgnlException;

public class DataUtils {
	
	public static final String COLUMN_NAME = "COLUMN_NAME";
	public static final String TYPE_NAME = "TYPE_NAME";
	public static final String COLUMN_SIZE = "COLUMN_SIZE";
	
	protected DataUtils() {
		throw new IllegalStateException("Utils class: DataUtils");
	}
	
	public static Connection getConnection(DataSource dataSource) 
		throws CannotGetJdbcConnectionException {
		
		if (dataSource==null) {
			return null;
		}
		return DataSourceUtils.getConnection(dataSource);
	}
	
	public static Connection getConnection(String dataSourceId) {
		Connection conn = null;
		DataSource dataSource = (DataSource)AppContext.getContext().getBean(dataSourceId);
		try {
			conn = getConnection(dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return conn;
	}
	
	public static void doReleaseConnection(String dataSourceId, Connection connection) {
		if (null == connection) {
			return;
		}
		DataSource dataSource = (DataSource)AppContext.getContext().getBean(dataSourceId);
		try {
			DataSourceUtils.doReleaseConnection(connection, dataSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String[] getTables(Connection connection) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		String[] types = {"TABLE"};
		ResultSet rs = metaData.getTables(null, null, "%", types);
		List<String> tables = new LinkedList<>();
		while (rs.next()) {
			tables.add(rs.getString("TABLE_NAME"));
		}
		String[] names = new String[tables.size()];
		tables.toArray(names);		
		return names;
	}
	
	public static Map<String, Map<String, String>> getTableMetadata(Connection connection, String tableName) throws SQLException {
		Map<String, Map<String, String>> tableMetadatas = new LinkedHashMap<>();
		DatabaseMetaData metadata = connection.getMetaData();
		ResultSet resultSet = metadata.getColumns(null, null, tableName, null);
		while (resultSet.next()) {
			Map<String, String> metaDataMap = new LinkedHashMap<>();
			String name = resultSet.getString(COLUMN_NAME);
			String type = resultSet.getString(TYPE_NAME);
			int size = resultSet.getInt(COLUMN_SIZE);
			metaDataMap.put(COLUMN_NAME, name);
			metaDataMap.put(TYPE_NAME, type);
			metaDataMap.put(COLUMN_SIZE, Integer.toString(size));
			tableMetadatas.put(name, metaDataMap);
		}
		return tableMetadatas;
	}
	
	public static NamedParameterJdbcTemplate getManualJdbcTemplate(DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}
	
	public static NamedParameterJdbcTemplate getJdbcTemplate() throws BeansException {
		return (NamedParameterJdbcTemplate)AppContext.getBean("namedParameterJdbcTemplate");
	}	
	
	public static NamedParameterJdbcTemplate getManualJdbcTemplate(
			Class<?> dataSourceClass, String url, String user, String password) throws OgnlException, IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return getManualJdbcTemplate( ManualDataSourceFactory.getDataSource(dataSourceClass, url, user, password) );
	}
	
	/**
	 * 排除系統表
	 * @throws SQLException 
	 */
	public static String[] getTablesWithDoReleaseConnection(String dataSourceId) throws SQLException {
		Connection connection = getConnection(dataSourceId);
		if (null == connection) {
			return new String[0];
		}
		DatabaseMetaData metaData = connection.getMetaData();
		if (null == metaData) {
			return new String[0];
		}
		String[] types = {"TABLE"};
		ResultSet rs = metaData.getTables(null, null, "%", types);
		List<String> tables = new ArrayList<>();
		while (rs.next()) {
			String tableName = rs.getString("TABLE_NAME");
			if (tableName.startsWith("tb_") || tableName.startsWith("TB_") || tableName.startsWith("act_") || tableName.indexOf("qrtz_") > -1) {
				continue;
			}
			tables.add(tableName);
		}
		String[] names = new String[tables.size()];
		tables.toArray(names);		
		doReleaseConnection(dataSourceId, connection);
		return names;
	}
	
	public static Map<String, String> getTablesWithDoReleaseConnection(String dataSourceId, boolean pleaseSelect) throws SQLException {
		String[] tables = getTablesWithDoReleaseConnection(dataSourceId);
		Map<String, String> dataMap = PleaseSelect.pageSelectMap(pleaseSelect);
		if (ArrayUtils.isEmpty(tables)) {
			return dataMap;
		}		
		for (int i = 0; i < tables.length; i++) {
			dataMap.put(tables[i], tables[i]);
		}
		return dataMap;
	}
	
	public static Map<String, Map<String, String>> getTableMetadataWithDoReleaseConnection(String dataSourceId, String tableName) throws SQLException {
		Map<String, Map<String, String>> tableMetadatas = new LinkedHashMap<>();
		Connection connection = getConnection(dataSourceId);
		if (null == connection) {
			return tableMetadatas;
		}
		DatabaseMetaData metadata = connection.getMetaData();
		if (null == metadata) {
			return tableMetadatas;
		}
		ResultSet resultSet = metadata.getColumns(null, null, tableName, null);
		while (resultSet.next()) {
			Map<String, String> metaDataMap = new HashMap<>();
			String name = resultSet.getString(COLUMN_NAME);
			String nametToLowerCase = name.toLowerCase();
			if (nametToLowerCase.equals("oid") || nametToLowerCase.equals("cuserid") || nametToLowerCase.equals("cdate")
					|| nametToLowerCase.equals("uuserid") || nametToLowerCase.equals("udate") ) { // 不顯示這5個欄位
				continue;
			}
			String type = resultSet.getString(TYPE_NAME);
			int size = resultSet.getInt(COLUMN_SIZE);
			metaDataMap.put(COLUMN_NAME, name);
			metaDataMap.put(TYPE_NAME, type);
			metaDataMap.put(COLUMN_SIZE, Integer.toString(size));
			tableMetadatas.put(name, metaDataMap);
		}
		doReleaseConnection(dataSourceId, connection);
		return tableMetadatas;
	}	
	
}
