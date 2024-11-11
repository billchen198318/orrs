package org.orrs.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.qifu.util.LoadResources;

public class LlmModels {
	
	public static List<String> list = null;
	
	static {
		try {
			Map<String, Object> llmModelMap = (Map<String, Object>) LoadResources.objectMapperReadValue("llmModel.json", Map.class, LlmModels.class);
			list = (List<String>) llmModelMap.get("models");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (CollectionUtils.isEmpty(list)) {
				list = List.of();
				list.add("gemma2");
			}
		}
	}
	
	public static boolean has(String modelName) {
		return list.contains(modelName);
	}
	
}