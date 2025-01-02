package org.orrs.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.orrs.model.HanLpModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hankcs.hanlp.HanLP;

@Component
public class DocumentSearch {
	
	@Autowired
	VectorStore vectorStore;	
	
	public List<Map<String, Object>> queryByMetadataOrDefaultOrQuestionNL2ListMap(String question, double similarityThreshold) {
		List<Map<String, Object>> resultList = null;
		List<Document> similarDocuments = queryByMetadataOrDefaultOrQuestionNL(question, similarityThreshold);
        if (!CollectionUtils.isEmpty(similarDocuments)) {
        	resultList = similarDocuments.stream()
        			.map(document -> {
        				Map<String, Object> map = new HashMap<>();
                        map.put("docId", document.getId());
                        map.put("content", document.getText());
                        return map;
                    })
        			.collect(Collectors.toList());
        }
        return resultList;
	}
	
	public List<Document> queryByMetadataOrDefaultOrQuestionNL(String question, double similarityThreshold) {
		List<Document> resultList = queryByMetadataOrDefault(question, similarityThreshold);
		if (CollectionUtils.isEmpty(resultList)) {
			if (question.length() >= 3) {
				List<String> ss = HanLP.extractKeyword(question, HanLpModel.getExtractKeywordSize(question));
				ss.addAll( HanLP.extractPhrase(question, HanLpModel.getExtractPhraseSize(question)) );
				ss.addAll( HanLP.extractSummary(question, HanLpModel.getExtractSummary(question)) );
				for (int r = 0; CollectionUtils.isEmpty(resultList) && r < ss.size(); r++) {
					resultList = this.queryByMetadataOrDefault(question, similarityThreshold);
				}
			}
		}
		return resultList;
	}
	
	public List<Document> queryByMetadataOrDefault(String question, double similarityThreshold) {
		List<Document> resultList = this.queryByMetadata(question);
		if (CollectionUtils.isEmpty(resultList)) {
			resultList = this.query(question, similarityThreshold);
		}
		return resultList;
	}
	
	public List<Document> queryByMetadata(String question) {
        SearchRequest query = SearchRequest.builder().query(question).topK(SearchRequest.DEFAULT_TOP_K).similarityThreshold(SearchRequest.SIMILARITY_THRESHOLD_ACCEPT_ALL).build();
        List<Document> similarDocuments = this.vectorStore.similaritySearch(query);
        List<Document> resultList = null;
        if (!CollectionUtils.isEmpty(similarDocuments)) {
        	for (Document d : similarDocuments) {
        		List<String> keywordList = (List<String>) d.getMetadata().get("keyword");
        		List<String> phraseList = (List<String>) d.getMetadata().get("phrase");
        		List<String> summaryList = (List<String>) d.getMetadata().get("summary");
        		boolean s = false;
        		for (String k : keywordList) {
        			if (!s && (question.indexOf(k) > -1 || k.indexOf(question) > -1)) {
        				s = true;
        			}
        		}
        		for (String k : phraseList) {
        			if (!s && (question.indexOf(k) > -1 || k.indexOf(question) > -1)) {
        				s = true;
        			}
        		}
        		for (String k : summaryList) {
        			if (!s && (question.indexOf(k) > -1 || k.indexOf(question) > -1)) {
        				s = true;
        			}
        		} 
        		/*
        		if (!s && (d.getContent().indexOf(question) > -1 || question.indexOf(question) > -1)) {
        			s = true;
        		}
        		*/
        		if (s) {
        			if (null == resultList) {
        				resultList = new ArrayList<>();
        			}    
                    resultList.add(d);
        		}
        	}
        }
        return resultList;
	}
	
	public List<Document> query(String question) {
		return query(question, SearchRequest.SIMILARITY_THRESHOLD_ACCEPT_ALL);
	}
	
	public List<Document> query(String question, double similarityThreshold) {
        SearchRequest query = SearchRequest.builder().query(question).topK(SearchRequest.DEFAULT_TOP_K).similarityThreshold(similarityThreshold).build();
        List<Document> similarDocuments = this.vectorStore.similaritySearch(query);
        return similarDocuments;
	}
	
}
