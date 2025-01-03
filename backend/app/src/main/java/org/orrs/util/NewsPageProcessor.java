package org.orrs.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.orrs.model.News;
import org.qifu.util.LoadResources;
import org.qifu.util.SimpleUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class NewsPageProcessor implements PageProcessor {
	protected Logger logger = LogManager.getLogger(NewsPageProcessor.class);
	
	private static int retryTimes;
	private static int retryTimesSleepTime;
	private static String newsUrl = "";
	
	private String searchContent;
	
	private List<String> results = new ArrayList<String>();
	
	static {
		try {
			Map<String, Object> confMap = (Map<String, Object>) LoadResources.objectMapperReadValue("WebPageProcessor.json", Map.class, WikiPageProcessor.class);
			retryTimes = (int) confMap.getOrDefault("retryTimes", 3);
			retryTimesSleepTime = (int) confMap.getOrDefault("retryTimesSleepTime", 1000);
			newsUrl = (String) confMap.getOrDefault("newsUrl", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private NewsPageProcessor search(String searchContent) {
		this.searchContent = searchContent;
		return this;
	}
	
	public static NewsPageProcessor build(String searchContent) {
		NewsPageProcessor w = new NewsPageProcessor();
		w.search(searchContent);
		return w;
	}
	
	public List<String> getNews() {
		if (newsUrl.endsWith("API_KEY")) {
			logger.warn("getNews() no API_KEY ...");
			return results;
		}
		DateTime dateTime = new DateTime(new Date());
		dateTime = dateTime.plusDays(-30);		
		String fromDate = SimpleUtils.getStrYMD(dateTime.toDate(), "-");
		String currNewsUrl = newsUrl;
		currNewsUrl = StringUtils.replaceOnce(currNewsUrl, "${q}", this.searchContent);
		currNewsUrl = StringUtils.replaceOnce(currNewsUrl, "${from}", fromDate);
		Spider.create(this).addUrl(currNewsUrl).thread(1).run();
		return results;
	}
	
	public List<News> getNewsResults(List<String> results, int maxResult) {
		if (CollectionUtils.isEmpty(results)) {
			return new ArrayList<>();
		}
		List<News> newsList = new ArrayList<>();
		for (String result : results) {
			if (newsList.size() >= maxResult) {
                continue;
            }			
			Map<String, Object> m;
			try {
				m = new ObjectMapper().readValue(result, Map.class);
				if (m != null && "ok".equals(m.get("status"))) {
					List<Map> articles = (List<Map>) m.get("articles");
					for (int i = 0; articles != null && !articles.isEmpty() && i < articles.size(); i++) {
						if (newsList.size() >= maxResult) {
                            continue;
                        }
						Map article = articles.get(i);
						String title = (String) article.get("title");
						String description = (String) article.get("description");
						if (!StringUtils.isEmpty(title) && !StringUtils.isEmpty(description)) {
							News newsData = new News(title, description);
                            newsList.add(newsData);	
						}
					}
				}
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return newsList;
	}
	
    @Override
    public Site getSite() {
        return Site.me().setUserAgent("Mozilla/5.0 (X11; Linux i686; rv:10.0) Gecko/20100101 Firefox/10.0").setRetryTimes(retryTimes).setSleepTime(retryTimesSleepTime);
    }	
	
	@Override
	public void process(Page page) {
		this.results.add( page.getRawText() );
	}
	
	/*
	public static void main(String[] args) {
		WikiPageProcessor wpp = WikiPageProcessor.build("馬保國");
		List<News> news = wpp.getNewsResults(wpp.getNews(), 5);
		if (!CollectionUtils.isEmpty(news)) {
			for (News n : news) {
				System.out.println("News>>>" + n.getTitle());
				System.out.println("News content>>>" + n.getDescription());
			}
		}		
	}	
	*/
	
}
