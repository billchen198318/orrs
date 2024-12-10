package org.orrs.api;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.orrs.entity.TbOrrsDoc;
import org.orrs.model.LlmModels;
import org.orrs.service.IOrrsDocService;
import org.orrs.util.DocumentSearch;
import org.qifu.core.util.CoreApiSupport;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaApi.ChatRequest;
import org.springframework.ai.ollama.api.OllamaApi.ChatResponse;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;

@Tag(name = "ORRS001D0005", description = "llm test.")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ORRS001D0005")
public class ORRS001D0005Controller extends CoreApiSupport {
	private static final long serialVersionUID = -5477526184299910978L;
	
	@Autowired
	IOrrsDocService<TbOrrsDoc, String> orrsDocService;
	
	@Autowired
	OllamaChatModel ollamaChatModel;
	
	@Autowired
	OllamaApi ollamaApi;
	
	@Autowired
	DocumentSearch documentSearch;
	
	private void fillPromptMessageFromDocuments(String userMessage, List<Message> messageList, BigDecimal simThreshold) {
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
	
	@PostMapping("/chat")
    public Flux<ChatResponse> chat(@RequestParam String model, @RequestParam String message) {
		List<Message> messageList = new LinkedList<Message>();
		this.fillPromptMessageFromDocuments(message, messageList, BigDecimal.ZERO);
		messageList.add(Message.builder(Message.Role.USER).content(message).build());
		var req = ChatRequest.builder(model).withStream(true).withMessages(messageList).build();
		return ollamaApi.streamingChat(req);
    }	
	
}
