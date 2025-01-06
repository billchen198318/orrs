package org.orrs.util;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.orrs.entity.TbOrrsDoc;
import org.orrs.model.HanLpModel;
import org.orrs.model.LlmModels;
import org.orrs.model.News;
import org.orrs.model.QueryTextSnippetData;
import org.orrs.service.IOrrsDocService;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import com.hankcs.hanlp.HanLP;

@Component
public class OrrsSupport {
	protected Logger logger = LogManager.getLogger(OrrsSupport.class);
	
	@Autowired
	Environment env;
	
	@Autowired
	IOrrsDocService<TbOrrsDoc, String> orrsDocService;
	
	@Autowired
	DocumentSearch documentSearch;
	
	public static String getMessageFromCommonTemplate(String userMessage, String referenceContent) {
		String template = LlmModels.getCommonTemplate();
		template = StringUtils.replaceOnce(template, "$COMMON_TEMPLATE_PARAM{qustion}", userMessage);
		template = StringUtils.replaceOnce(template, "$COMMON_TEMPLATE_PARAM{reference}", referenceContent);
		return template;
	}
	
	public void fillPromptMessageFromWiki(String userMessage, List<Message> messageList) {
		try {
			List<String> kwList = HanLP.extractKeyword(userMessage, HanLpModel.getExtractKeywordSize(userMessage));
			List<String> phList = HanLP.extractPhrase(userMessage, HanLpModel.getExtractPhraseSize(userMessage));
			List<String> ssList = HanLP.extractSummary(userMessage, HanLpModel.getExtractSummary(userMessage));
			List<String> searchList = new LinkedList<String>();
			searchList.add(userMessage);
			searchList.addAll(kwList);
			searchList.addAll(phList);
			searchList.addAll(ssList);
			for (String s : searchList) {
				WikiPageProcessor wpp = WikiPageProcessor.build(UriUtils.encodeQuery(s, StandardCharsets.UTF_8));
				List<String> results = wpp.getResults();			
				if (!CollectionUtils.isEmpty(results)) {
					for (String result : results) {
						String wikiExtract = wpp.getExtract(result);
						if (!StringUtils.isEmpty(wikiExtract)) {
							messageList.add(Message.builder(Message.Role.ASSISTANT).content(getMessageFromCommonTemplate(userMessage, wikiExtract)).build());
							logger.info("Wiki Extract>>> {}", wikiExtract);
							searchList.clear();
							return;
						}
	                }
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void fillPromptMessageFromWikiByQueryText(String userMessage, List<Message> messageList) {
		WikiPageProcessor wpp = WikiPageProcessor.build(UriUtils.encodeQuery(userMessage, StandardCharsets.UTF_8));
		List<QueryTextSnippetData> snippets = wpp.getSnippetForQueryTextResults( wpp.getQueryTextResults() , 3 );
		if (!CollectionUtils.isEmpty(snippets)) {
			for (QueryTextSnippetData snippetData : snippets) {
				if (StringUtils.isBlank(snippetData.getSnippet())) {
					continue;
				}
				messageList.add(Message.builder(Message.Role.ASSISTANT).content(getMessageFromCommonTemplate(userMessage, snippetData.getTitle() + "\n" + snippetData.getSnippet())).build());
				logger.info("Wiki snippet title>>> {}\nWiki snippet >>> {}", snippetData.getTitle(), snippetData.getSnippet());
			}
		}		
	}
	
	public void fillPromptMessageFromNews(String userMessage, List<Message> messageList) {
		NewsPageProcessor npp = NewsPageProcessor.build(UriUtils.encodeQuery(userMessage, StandardCharsets.UTF_8));
		List<News> newsList = npp.getNewsResults( npp.getNews() , 5 );
		if (!CollectionUtils.isEmpty(newsList)) {
			for (News news : newsList) {
				messageList.add(Message.builder(Message.Role.ASSISTANT).content(getMessageFromCommonTemplate(userMessage, news.getTitle() + "\n" + news.getDescription())).build());
				logger.info("News title>>> {}\nNews content >>> {}", news.getTitle(), news.getDescription());
			}
		}
	}
	
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
	        	similarDocuments = this.documentSearch.query(userMessage, similarityThreshold);
	        	if (CollectionUtils.isEmpty(similarDocuments)) {
	        		return;
	        	}
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
	
	public OllamaOptions getOptions() {
		OllamaOptions options = new OllamaOptions();
		if (env.getProperty("spring.ai.ollama.chat.options.temperature") != null) {
			options.setTemperature( NumberUtils.toDouble(env.getProperty("spring.ai.ollama.chat.options.temperature"), 0.8d) );
		}
		if (env.getProperty("spring.ai.ollama.chat.options.top-k") != null) {
			options.setTopK( NumberUtils.toInt(env.getProperty("spring.ai.ollama.chat.options.top-k"), 40) );
		}
		if (env.getProperty("spring.ai.ollama.chat.options.top-p") != null) {
			options.setTopP( NumberUtils.toDouble(env.getProperty("spring.ai.ollama.chat.options.top-p"), 0.9d) );
		}
		if (env.getProperty("spring.ai.ollama.chat.options.num-gpu") != null) {
			options.setNumGPU( NumberUtils.toInt(env.getProperty("spring.ai.ollama.chat.options.num-gpu"), -1) );
		}
		if (env.getProperty("spring.ai.ollama.chat.options.numa") != null) {
			options.setUseNUMA( Boolean.valueOf(env.getProperty("spring.ai.ollama.chat.options.numa")) );
		}
		if (env.getProperty("spring.ai.ollama.chat.options.num-ctx") != null) {
			options.setNumCtx( NumberUtils.toInt(env.getProperty("spring.ai.ollama.chat.options.num-ctx"), 2048) );
		}
		return options;
	}	
	
}
