package org.orrs.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.orrs.model.DocumentSetting;
import org.orrs.model.DocumentSettingModel;
import org.orrs.model.LlmModels;
import org.orrs.util.DocumentSearch;
import org.orrs.util.OrrsSupport;
import org.orrs.vo.ORRS001D0005ChatBody;
import org.qifu.base.model.YesNoKeyProvide;
import org.qifu.core.util.CoreApiSupport;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaApi.ChatRequest;
import org.springframework.ai.ollama.api.OllamaApi.ChatResponse;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import ognl.Ognl;
import ognl.OgnlException;
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
	
	@Autowired
	@Resource(name="db1JdbcTemplate")
	NamedParameterJdbcTemplate db1JdbcTemplate;
	
	@PostMapping("/chat")
    public Flux<ChatResponse> chat(@RequestBody ORRS001D0005ChatBody chatBody) {
		List<Message> messageList = new LinkedList<Message>();
		if (!StringUtils.isEmpty(chatBody.getSystem())) {
			messageList.add(Message.builder(Message.Role.SYSTEM).content(chatBody.getSystem()).build());
		}
		if (YES.equals(chatBody.getDocmode())) {
			this.orrsSupport.fillPromptMessageFromDocuments(chatBody.getMessage(), messageList, chatBody.getSimThreshold());
		}
		if (YES.equals(chatBody.getWikimode())) {
			this.orrsSupport.fillPromptMessageFromWiki(chatBody.getMessage(), messageList);
			this.orrsSupport.fillPromptMessageFromWikiByQueryText(chatBody.getMessage(), messageList);
		}
		if (YES.equals(chatBody.getNewsmode())) {
			this.orrsSupport.fillPromptMessageFromNews(chatBody.getMessage(), messageList);
		}
		this.documentSetting(messageList, chatBody.getMessage());
		messageList.add(Message.builder(Message.Role.USER).content(chatBody.getMessage()).build());
		var req = ChatRequest.builder(LlmModels.has(chatBody.getModel()) ? chatBody.getModel() : LlmModels.getFirst()).stream(true)
				.options(this.orrsSupport.getOptions()).messages(messageList).build();
		return ollamaApi.streamingChat(req);
    }	
	
	private void documentSetting(List<Message> messageList, String userMessage) {
		DocumentSetting ds = DocumentSettingModel.getDocumentSetting();
		if (!YesNoKeyProvide.YES.equals(ds.getEnable())) {
			return;
		}
		for (DocumentSetting.Setting setting : ds.getSetting()) {
			Map<String, Object> param = new HashMap<>();
			param.put(setting.getVariable(), userMessage);
			try {
				Object res = Ognl.getValue(setting.getTest(), param);
				if ( res instanceof @SuppressWarnings("unused") Boolean bl && (Boolean) res ) {
					this.fillDocumentSetting(messageList, userMessage, setting);
				}
			} catch (OgnlException e) {
				e.printStackTrace();
			}			
		}
	}
	
	private void fillDocumentSetting(List<Message> messageList, String userMessage, DocumentSetting.Setting setting) {
		Map<String, Object> param = new HashMap<>();
		List<Map<String, Object>> docList = this.db1JdbcTemplate.queryForList("select * from tb_orrs_doc where 1=1 and " + setting.getSqlCondition(), param);
		if (CollectionUtils.isEmpty(docList)) {
			return;
		}
		for (Map<String, Object> docMap : docList) {
			String content = (String) docMap.get("CONTENT");
			String sysPromptTpl = (String) docMap.get("SYS_PROMPT_TPL");
			String tplVariable = (String) docMap.get("TPL_VARIABLE");
        	SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(sysPromptTpl);
        	String sysPrompt = systemPromptTemplate.createMessage(Map.of(tplVariable, content)).getText();			
        	messageList.add(Message.builder(Message.Role.ASSISTANT).content(sysPrompt).build());
        }
	}
	
}
