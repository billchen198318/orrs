/* 
 * Copyright 2019-2021 qifu of copyright Chen Xin Nien
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
package org.qifu.core.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.orrs.util.MarkdownCodeExtractor;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.ScriptTypeCode;
import org.qifu.base.model.YesNo;
import org.qifu.core.util.CoreApiSupport;
import org.qifu.core.vo.TestModel;
import org.qifu.util.ScriptExpressionUtils;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaApi.ChatRequest;
import org.springframework.ai.ollama.api.OllamaApi.ChatResponse;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "TEST.", description = "Test hello world.")
@RestController
@RequestMapping(value = "/api/hello")
public class HelloApiController extends CoreApiSupport {
	
	private static final long serialVersionUID = -2710621780849674671L;
	
//	@Autowired
//	RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	OllamaChatModel ollamaChatModel;
	
	@Autowired
	OllamaApi ollamaApi;
	
	@Autowired
	VectorStore vectorStore;
	
	@Operation(summary = "測試讀取文庫至embedded1", description = "測試讀取文庫至embedded - 1")
	@GetMapping(value = "/testEmbedded1/{msg}")	
	public QueryResult<String> testEmbedded1(@PathVariable String msg) {
		QueryResult<String> result = this.initResult();
		
		List<Document> documents = 
				List.of(
						new Document("DOC001", "印度司機喪彪，直線加油誰不會，彎道快才是真的快！ youtube影片位置: https://www.youtube.com/watch?v=9gN7WeN7u0g", new HashMap<>())
						,
						new Document("DOC002", "喪彪是一位大巴司機", new HashMap<>())					
				);
		this.vectorStore.add(documents);
		
        SearchRequest query = SearchRequest.query(msg).withTopK(SearchRequest.DEFAULT_TOP_K).withSimilarityThreshold(0.6d);;
        List<Document> similarDocuments = this.vectorStore.similaritySearch(query);
        String relevantData = similarDocuments.stream()
                            .map(Document::getContent)
                            .collect(Collectors.joining(System.lineSeparator()));
        result.setValue(relevantData);
        
		return result;
	}	
	
	@Operation(summary = "測試讀取文庫至embedded2", description = "測試讀取文庫至embedded - 2")
	@GetMapping(value = "/testEmbedded2/{msg}")	
	public QueryResult<String> testEmbedded2(@PathVariable String msg) {
		QueryResult<String> result = this.initResult();
		
		// Retrieve embeddings
        SearchRequest query = SearchRequest.query(msg).withTopK(SearchRequest.DEFAULT_TOP_K).withSimilarityThreshold(0.6d);
        List<Document> similarDocuments = this.vectorStore.similaritySearch(query);
        /*
        String information = similarDocuments.stream()
                            .map(Document::getContent)
                            .collect(Collectors.joining(System.lineSeparator()));
        result.setValue(information);
        System.out.println("information>>>" + information);
        */
        
        List<Message> messageList = new LinkedList<Message>();
        
        if (!CollectionUtils.isEmpty(similarDocuments)) {
        	for (Document document : similarDocuments) {
        		System.out.println("doc-id>>>" + document.getId());
        		System.out.println("doc-content>>>" + document.getContent());
        		
            	SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate("""
                        You are a helpful assistant.
                        
                        Use the following information to answer the question:
                        {information}
                        """);
            	org.springframework.ai.chat.messages.Message systemMessage = systemPromptTemplate.createMessage(Map.of("information", document.getContent()));
            	String sysPrompt = systemMessage.getContent();
            	System.out.println("sysPrompt>>>" + sysPrompt);
            	messageList.add(Message.builder(Message.Role.SYSTEM).withContent(sysPrompt).build());        		
        	}
        }        
        
        messageList.add(Message.builder(Message.Role.USER).withContent(msg).build());
        
		var req = ChatRequest.builder("qwen2.5-coder").withStream(false).withMessages(messageList).build();
		ChatResponse response = ollamaApi.chat(req);
		String content = StringUtils.defaultString(response.message().content());		
		
		System.out.println("content>>>" + content);        
        
		return result;
	}		
	
	@Operation(summary = "測試ollama", description = "測試用codegeex4 - 1")
	@GetMapping(value = "/generateResponse/{msg}")	
	public QueryResult<String> generateResponse(@PathVariable String msg) {
		QueryResult<String> result = this.initResult();
		String res = ollamaChatModel.call(msg);
		System.out.println("res>>>" + res);
		result.setValue(res);
		return result;
	}
	
	@Operation(summary = "測試ollama 與 prompt", description = "測試用codegeex4 - 2")
	@GetMapping(value = "/generateResponse2/{prompt}")	
	public QueryResult<String> generateResponse2(@PathVariable String prompt) {
		QueryResult<String> result = this.initResult();
		org.springframework.ai.chat.model.ChatResponse chatResponse = ollamaChatModel.call(new Prompt(prompt));
        String res = chatResponse.getResult().getOutput().getContent();
		System.out.println("res>>>" + res);
		result.setValue(res);   
		result.setValue( result.getValue() + "\n" + res);        
		return result;
	}	
	
	@Operation(summary = "測試ollama 第3個範例", description = "測試用codegeex4 - 3")
	@GetMapping(value = "/generateResponse3/{content}")		
	public QueryResult<String> generateResponse3(@PathVariable String content) {
		QueryResult<String> result = this.initResult();
		
		var req = ChatRequest.builder("codegeex4")
			.withStream(false)
			.withMessages(List.of(
					//Message.builder(Message.Role.SYSTEM).withContent("中文回應").build()
					//,
					Message.builder(Message.Role.SYSTEM).withContent("echarts version 5.3.2").build()
					,
					Message.builder(Message.Role.SYSTEM).withContent("echarts cdn base url https://cdnjs.cloudflare.com/ajax/libs/echarts/5.3.2/echarts.min.js").build()
					//,
					//Message.builder(Message.Role.SYSTEM).withContent("echarts width 640px, height 480px").build()
					,
					Message.builder(Message.Role.SYSTEM).withContent("give me full html code content").build()
					,
					//實際值 [100, 107, 84, 91] 標題[week1, week2, week3, week3] 用 echarts 5.3.2 產生bar圖, pie圖, line圖
					Message.builder(Message.Role.USER).withContent(content).build()
				)
			) // .withOptions(OllamaOptions.create().withTemperature(0.9f))
		.build();
		
		//OllamaApi ollamaApi = new OllamaApi("YOUR_HOST:YOUR_PORT");		
		ChatResponse resp = ollamaApi.chat(req);
		
		result.setValue(resp.message().content());
		
		System.out.println("------------------------------------------------------");
		String str = resp.message().content();
		str = MarkdownCodeExtractor.parseHtml(str);
		System.out.println(str);
		System.out.println("------------------------------------------------------");
		
		return result;
	}		
	
	@Operation(summary = "測試ollama 第4個範例", description = "測試用codegeex4 - 4")
	@GetMapping(value = "/generateResponse4/{content}")		
	public QueryResult<String> generateResponse4(@PathVariable String content) {
		QueryResult<String> result = this.initResult();
		
		var req = ChatRequest.builder("codegeex4")
			.withStream(false)
			.withMessages(List.of(
					Message.builder(Message.Role.SYSTEM).withContent(
							"database is MariaDB, json output use com.fasterxml.jackson , need import java.sql.* , jdbc driver class org.mariadb.jdbc.Drive , jdbc url start is jdbc:mariadb:// ").build()
					,
					// mariadb IP位置 127.0.0.1 , port 3306, 帳戶: root 密碼: password 資料庫 orrs , 資料表 tb_sys_event_log 欄位 [OID, USER, SYS_ID, EXECUTE_EVENT] , 所有欄位都是varchar格式, 先將資料放入List<Map> 中, 再將List<Map>轉成json輸出, 給我 groovy code
					Message.builder(Message.Role.USER).withContent(content).build()
				)
			)
		.build();
		
		ChatResponse resp = ollamaApi.chat(req);
		
		result.setValue(resp.message().content());
		
		System.out.println("------------------------------------------------------");
		String str = resp.message().content();
		str = MarkdownCodeExtractor.parseGroovy(str);
		System.out.println(str);
		try {
			ScriptExpressionUtils.execute(ScriptTypeCode.GROOVY, str, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("------------------------------------------------------");
		
		return result;
	}			
	
	@Operation(summary = "測試del", description = "測試用的接口del")
	//@ResponseBody
	@DeleteMapping("/delPlay")
	public String delPlay(String key) {
		String flag = YesNo.NO;
		if (StringUtils.isBlank(key)) {
			return YesNo.NO;
		}		
//		if (this.redisTemplate.delete(key)) {
//			flag = YesNo.YES;
//		}
		return flag;
	}
	
	@Operation(summary = "測試", description = "測試用的接口")
	//@ResponseBody
	@PostMapping(value = "/play", produces = {MediaType.APPLICATION_JSON_VALUE})
	public QueryResult<String> play(@RequestBody TestModel data) {
		QueryResult<String> result = this.initResult();
		if (null == data || StringUtils.isBlank(data.getKey())) {
			this.noSuccessResult(result, BaseSystemMessage.parameterBlank());
			return result;
		}
		try {
//			if (StringUtils.isBlank(data.getMsg())) {
//				this.successResult(result, this.redisTemplate.opsForValue().get(data.getKey()));
//				return result;
//			}
//			if ( StringUtils.defaultString(this.redisTemplate.opsForValue().get(data.getKey())).length() > 1000 ) {
//				this.successResult(result, this.redisTemplate.opsForValue().get(data.getKey()));
//				return result;
//			}
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("str", data.getMsg());
			ObjectMapper om = new ObjectMapper();
			String val = om.writeValueAsString(dataMap);
//			this.redisTemplate.opsForValue().append(data.getKey(), val);
			this.successResult(result, val);
		} catch (Exception e) {
			e.printStackTrace();
			this.noSuccessResult(result, e);
		}
		return result;
	}
	
	@Operation(summary = "測試2", description = "測試用的接口2")
	//@ResponseBody
	@GetMapping(value = "/testPV/{id}")
	public QueryResult<String> testPV(@PathVariable String id) {
		QueryResult<String> result = this.initResult();
		result.setValue(id);
		result.setMessage( "hello." );
		result.setSuccess( YES );
		return result;
	}
	
}
