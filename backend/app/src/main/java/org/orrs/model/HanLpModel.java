package org.orrs.model;

import java.util.Map;

import org.qifu.util.LoadResources;

public class HanLpModel {
	private static int distinguishLength = 20;
	private static int minExtractKeywordSize = 1;
	private static int minExtractPhraseSize = 1;
	private static int minExtractSummary = 1;
	private static int maxExtractKeywordSize = 1;
	private static int maxExtractPhraseSize = 1;
	private static int maxExtractSummary = 1;
	
	static {
		try {
			Map<String, Object> dataModelMap = (Map<String, Object>) LoadResources.objectMapperReadValue("hanLpModel.json", Map.class, HanLpModel.class);
			distinguishLength = (int) dataModelMap.get("distinguishLength");
			minExtractKeywordSize = (int) dataModelMap.get("minExtractKeywordSize");
			minExtractPhraseSize = (int) dataModelMap.get("minExtractPhraseSize");
			minExtractSummary = (int) dataModelMap.get("minExtractSummary");
			maxExtractKeywordSize = (int) dataModelMap.get("maxExtractKeywordSize");
			maxExtractPhraseSize = (int) dataModelMap.get("maxExtractPhraseSize");
			maxExtractSummary = (int) dataModelMap.get("maxExtractSummary");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static int getDistinguishLength() {
		return distinguishLength;
	}

	public static int getMinExtractKeywordSize() {
		return minExtractKeywordSize;
	}

	public static int getMinExtractPhraseSize() {
		return minExtractPhraseSize;
	}

	public static int getMinExtractSummary() {
		return minExtractSummary;
	}

	public static int getMaxExtractKeywordSize() {
		return maxExtractKeywordSize;
	}

	public static int getMaxExtractPhraseSize() {
		return maxExtractPhraseSize;
	}

	public static int getMaxExtractSummary() {
		return maxExtractSummary;
	}
	
	public static int getExtractKeywordSize(String content) {
		if (content != null && content.length() > distinguishLength) {
			return maxExtractKeywordSize;
		}
		return minExtractKeywordSize;
	}

	public static int getExtractPhraseSize(String content) {
		if (content != null && content.length() > distinguishLength) {
			return maxExtractPhraseSize;
		}
		return minExtractPhraseSize;
	}

	public static int getExtractSummary(String content) {
		if (content != null && content.length() > distinguishLength) {
			return maxExtractSummary;
		}		
		return minExtractSummary;
	}	
	
}
