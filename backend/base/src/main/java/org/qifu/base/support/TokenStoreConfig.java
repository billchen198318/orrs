package org.qifu.base.support;

import java.util.HashMap;
import java.util.Map;

import org.qifu.util.LoadResources;

public class TokenStoreConfig {
	
	private static Map<String, String> configMap;
	
	static {
		try {
			configMap = LoadResources.objectMapperReadValue("token-store-config.json", HashMap.class, TokenStoreConfig.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getTableName() {
		return configMap.get("table");
	}
	
}
