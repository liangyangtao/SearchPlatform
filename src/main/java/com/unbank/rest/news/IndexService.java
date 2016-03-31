package com.unbank.rest.news;
import static com.unbank.common.constants.CommonConstants.ERROR;
import static com.unbank.common.constants.CommonConstants.PARAMERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unbank.common.utils.CommonUtils;
import com.unbank.common.utils.GsonUtil;
import com.unbank.entity.News;
import com.unbank.es.news.NewsService;

@Component
@Path("/index")
public class IndexService {
	private static final Logger logger = LoggerFactory.getLogger(IndexService.class); 

	@Autowired
	private NewsService newsService;

	/**
	 * 2015-4-24 由于暂时尚未实现@Beanparam的方法，后期需修改
	 * 
	 * @param crawl_id
	 * @param url
	 * @param title
	 * @param content
	 * @param newsDate
	 * @param crawlDate
	 * @param webName
	 * @param webSectionName
	 * @param website_id
	 * @param p_url
	 * @param tagName
	 * @param keyWords
	 * @return
	 */
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/oper")
	public String opter(@FormParam("crawl_id") int crawl_id,
			@FormParam("url") String url, @FormParam("title") String title,
			@FormParam("content") String content,
			@FormParam("webName") String webName,
			@FormParam("newsDate") Long newsDate,
			@FormParam("crawlDate") Long crawlDate,
			@FormParam("webSectionName") String webSectionName,
			@FormParam("website_id") String website_id,
			@FormParam("p_url") String p_url,
			@FormParam("tagName") String tagName,
			@FormParam("keyWords") String keyWords,
			@FormParam("picUrl") String picUrl,
			@FormParam("region") String region) {
		
		News news = new News();
		news.setCrawl_id(crawl_id);
		news.setUrl(url);
		news.setTitle(title);
		news.setContent(content);
		news.setNewsDate(newsDate);
		news.setCrawlDate(crawlDate);
		news.setWebName(webName);
		news.setWebSectionName(webSectionName);
		news.setWebsite_id(website_id);
		news.setTagName(tagName);
		news.setP_url(p_url);
		news.setTagName(tagName);
		news.setKeyWords(keyWords);
		news.setPicUrl(picUrl);
		news.setRegion(region);
		logger.info("建立索引，crawl_id:" + crawl_id);

		newsService.oper(news);
		return String.valueOf(crawl_id);
	}
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/index")
	public String index(@FormParam("jsonData") String jsonData) {
		
		Map<String, String> map = new HashMap<String, String>();
		// 参数判断
        if (CommonUtils.isEmpty(jsonData)) {
            logger.error("添加新闻索引信息,------" + PARAMERROR);
            CommonUtils.putMap(map, ERROR, PARAMERROR, null);
            return GsonUtil.getJsonStringFromObject(map);
        }
        
        // 转换json数据
        News newsInfo = GsonUtil.getObjectFromJsonStr(jsonData, News.class);
		newsService.index(newsInfo);
		return String.valueOf(newsInfo.getCrawl_id());
	}
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/bulkIndex")
	public String bulkIndex(@FormParam("jsonData") String jsonData) {
		
		Map<String, String> map = new HashMap<String, String>();
		// 参数判断
        if (CommonUtils.isEmpty(jsonData)) {
            logger.error("批量添加新闻,------" + PARAMERROR);
            CommonUtils.putMap(map, ERROR, PARAMERROR, null);
            return GsonUtil.getJsonStringFromObject(map);
        }
        // 转换json数据
        Gson gson = new Gson();
        List<News> newsList = gson.fromJson(jsonData, new TypeToken<List<News>>() {  }.getType());
        
		return newsService.bulkIndex(newsList, map);
	}
	
}
