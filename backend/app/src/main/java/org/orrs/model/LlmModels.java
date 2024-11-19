package org.orrs.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.qifu.util.LoadResources;

public class LlmModels {
	
	private static List<String> list = null;
	
	private static Double similarityThreshold = 1.0d;
	
	private static String embedding = "nomic-embed-text";
	
	static {
		try {
			Map<String, Object> llmModelMap = (Map<String, Object>) LoadResources.objectMapperReadValue("llmModel.json", Map.class, LlmModels.class);
			list = (List<String>) llmModelMap.get("models");
			similarityThreshold = (Double) llmModelMap.getOrDefault("similarityThreshold", 1.0d);
			embedding = (String) llmModelMap.getOrDefault("embedding", embedding);
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

	public static List<String> getList() {
		return list;
	}

	public static Double getSimilarityThreshold() {
		return similarityThreshold;
	}

	public static String getEmbedding() {
		return embedding;
	}
	
}
