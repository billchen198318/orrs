package org.orrs.api;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.orrs.model.LlmModels;
import org.orrs.util.DocumentSearch;
import org.orrs.util.OrrsSupport;
import org.qifu.core.util.CoreApiSupport;
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
	OllamaChatModel ollamaChatModel;
	
	@Autowired
	OllamaApi ollamaApi;
	
	@Autowired
	DocumentSearch documentSearch;
	
	@Autowired
	OrrsSupport orrsSupport;
	
	@PostMapping("/chat")
    public Flux<ChatResponse> chat(@RequestParam String model, @RequestParam String system, @RequestParam String message) {
		List<Message> messageList = new LinkedList<Message>();
		if (!StringUtils.isEmpty(system)) {
			messageList.add(Message.builder(Message.Role.SYSTEM).content(system).build());
		}
		this.orrsSupport.fillPromptMessageFromDocuments(message, messageList, BigDecimal.ZERO);
		messageList.add(Message.builder(Message.Role.USER).content(message).build());
		var req = ChatRequest.builder(LlmModels.has(model) ? model : LlmModels.getFirst()).withStream(true).withMessages(messageList).build();
		return ollamaApi.streamingChat(req);
    }	
	
}
