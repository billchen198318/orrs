package org.orrs.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.orrs.entity.TbOrrsDoc;
import org.orrs.model.LlmModels;
import org.orrs.service.IOrrsDocService;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrrsSupport {
	
	@Autowired
	IOrrsDocService<TbOrrsDoc, String> orrsDocService;
	
	@Autowired
	DocumentSearch documentSearch;
	
	public void fillPromptMessageFromDocuments(String userMessage, List<Message> messageList, BigDecimal simThreshold) {
		try {
			double similarityThreshold = -1.0d;
			if (simThreshold != null) {
                similarityThreshold = simThreshold.doubleValue();
            }
			if (similarityThreshold < 0.0d || similarityThreshold > 1.0d) {
				similarityThreshold = LlmModels.getSimilarityThreshold();
			}
	        List<Document> similarDocuments = this.documentSearch.queryByMetadataOrDefaultOrQuestionNL(userMessage, similarityThreshold);
	        if (CollectionUtils.isEmpty(similarDocuments)) {
	        	return;
	        }
	        for (Document doc : similarDocuments) {
	        	TbOrrsDoc orrsDoc = new TbOrrsDoc();
	        	orrsDoc.setDocId(doc.getId());
	        	orrsDoc = this.orrsDocService.selectByUniqueKey(orrsDoc).getValue();
	        	if (null == orrsDoc || StringUtils.isBlank(orrsDoc.getSysPromptTpl()) || StringUtils.isBlank(orrsDoc.getTplVariable())) {
	        		continue;
	        	}
            	SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(orrsDoc.getSysPromptTpl());
            	String sysPrompt = systemPromptTemplate.createMessage(Map.of(orrsDoc.getTplVariable(), doc.getText())).getText();
            	messageList.add(Message.builder(Message.Role.ASSISTANT).content(sysPrompt).build()); 
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
}
