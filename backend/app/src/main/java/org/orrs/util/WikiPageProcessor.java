package org.orrs.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.qifu.util.LoadResources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class WikiPageProcessor implements PageProcessor {
	private static int retryTimes;
	private static int retryTimesSleepTime;
	private static String summaryUrl = "";
	private static String queryTextUrl = "";
	private static String queryTitleUrl = "";
	
	private String searchContent;
	
	private List<String> results = new ArrayList<String>();
	
	static {
		try {
			Map<String, Object> confMap = (Map<String, Object>) LoadResources.objectMapperReadValue("WikiPageProcessor.json", Map.class, WikiPageProcessor.class);
			retryTimes = (int) confMap.getOrDefault("retryTimes", 3);
			retryTimesSleepTime = (int) confMap.getOrDefault("retryTimesSleepTime", 1000);
			summaryUrl = (String) confMap.getOrDefault("summaryUrl", "https://zh.wikipedia.org/api/rest_v1/page/summary/");
			queryTextUrl = (String) confMap.getOrDefault("queryTextUrl", "https://zh.wikipedia.org/w/api.php?action=query&list=search&srwhat=text&format=json&srsearch=");
			queryTitleUrl = (String) confMap.getOrDefault("queryTitleUrl", "https://zh.wikipedia.org/w/api.php?action=query&list=search&srwhat=title&format=json&srsearch=");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private WikiPageProcessor search(String searchContent) {
		this.searchContent = searchContent;
		return this;
	}
	
	public static WikiPageProcessor build(String searchContent) {
		WikiPageProcessor w = new WikiPageProcessor();
		w.search(searchContent);
		return w;
	}
	
	public List<String> getResults() {
		Spider.create(this).addUrl(summaryUrl + this.searchContent).thread(1).run();
        return results;
    }
	
	public List<String> getQueryTextResults() {
		Spider.create(this).addUrl(queryTextUrl + this.searchContent).thread(1).run();
        return results;
    }	
	
	public List<String> getQueryTitleResults() {
		Spider.create(this).addUrl(queryTitleUrl + this.searchContent).thread(1).run();
        return results;
    }	
	
    @Override
    public Site getSite() {
        return Site.me().setRetryTimes(retryTimes).setSleepTime(retryTimesSleepTime);
    }	
	
	@Override
	public void process(Page page) {
		this.results.add( page.getRawText() );
	}
	
	public String getExtract(String wikiSummaryResultJson) {
		try {
			Map<String, Object> m = new ObjectMapper().readValue(wikiSummaryResultJson, Map.class);
			if (!MapUtils.isEmpty(m)) {
				return (String) m.getOrDefault("extract", "");
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/*
	public static void main(String[] args) {
		WikiPageProcessor wpp = WikiPageProcessor.build("桃園市");
		List<String> results = wpp.getQueryTextResults();
		for (String result : results) {
			System.out.println(result);
		}
		
		WikiPageProcessor wpp = WikiPageProcessor.build("桃園市");
        List<String> results = wpp.getResults();
        for (String result : results) {
            System.out.println(wpp.getExtract(result));
        } 
	} */
	
}
