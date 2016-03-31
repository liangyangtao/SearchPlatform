package com.unbank.rest.news;

import static com.unbank.common.constants.CommonConstants.ERROR;
import static com.unbank.common.constants.CommonConstants.PARAMERROR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.unbank.common.utils.CommonUtils;
import com.unbank.common.utils.GsonUtil;
import com.unbank.entity.News;
import com.unbank.entity.NewsSearchCondition;
import com.unbank.entity.SearchData;
import com.unbank.entity.SearchNews;
import com.unbank.es.news.NewsService;
import com.unbank.es.search.SearchClient;
import com.unbank.es.search.SearchErrorInfo;

@Component
@Path("/search")
public class SearchService {
	
	private static final Logger logger = LoggerFactory.getLogger(SearchClient.class);
	
	@Autowired
	private NewsService newsService;
	
	@Path("/searchByNewsId")
	@POST
	public String searchByCrawlId(@FormParam("crawl_id") String crawl_id) {
		SearchClient searchClient = new SearchClient();
		logger.info("按照新闻ID搜索,searchByNewsId,newsId:"+crawl_id);
		if (crawl_id == null) {
			// 参数错误
			logger.info("按照新闻ID搜索,searchByNewsId," + PARAMERROR);
			return SearchErrorInfo.ILLEGALARGS;
		}
		int id = Integer.valueOf(crawl_id);
		News news = searchClient.SearchByCrawlId(id);
		Gson gson = new Gson();
		return gson.toJson(news);
	}
	
	@Path("/searchMoreLikeByNewsId")
	@POST
	public String searchMoreLikeByNewsId(@FormParam("crawl_id") String crawl_id,
			@FormParam("pageSize") String pageSize,
			@FormParam("from") String from){
		SearchClient searchClient = new SearchClient();
		logger.info("searchMoreLikeByNewsId,newsId:"+crawl_id);
		if (crawl_id == null) {
			// 参数错误
			logger.info("searchMoreLikeByNewsId," + PARAMERROR);
			return SearchErrorInfo.ILLEGALARGS;
		}
		Integer size = 10;
		Integer f = 0;
		if (pageSize != null) {
			size = Integer.valueOf(pageSize);
		}
		if (from != null) {
			f = Integer.valueOf(from);
		}
		
		List<SearchNews> list= searchClient.SearchMoreLikeByNewsID(crawl_id, f, size);
		Gson gson=new Gson();
		String r= gson.toJson(list);
		return r;
	}
	
	/**
	 * 根据新闻信息搜索相似新闻
	 * @param jsonData 
	 * @param from 起始数
	 * @param pageSize 查询条数
	 * @param termFreq 匹配文档中的最低词频，低于此频率的词条将被忽略，默认值为：2
	 * @param docFreq 包含词条的文档最小数目，低于此数目时，该词条将被忽视，默认值为：5，意味着一个词条至少应该出现在5个文档中，才不会被忽略
	 * @return
	 */
	@Path("/moreLikeByNewsInfo")
	@POST
	public String moreLikeByNewsInfo(@FormParam("jsonData") String jsonData,
			@FormParam("from") int from, @FormParam("pageSize") int pageSize,
			@FormParam("termFreq") int termFreq, @FormParam("docFreq") int docFreq) {
		
		logger.info("(/search/moreLikeByNewsInfo),jsonData:" + jsonData
				+ ",from:" + from + ",pageSize:" + pageSize + ",termFreq:"
				+ termFreq + ",docFreq:" + docFreq);
		
		Map<String, String> map = new HashMap<String, String>();
		// 参数判断
        if (CommonUtils.isEmpty(jsonData)) {
            logger.error("moreLikeByNewsInfo,------" + PARAMERROR);
            CommonUtils.putMap(map, ERROR, PARAMERROR, null);
            return GsonUtil.getJsonStringFromObject(map);
        }
        
		News newsInfo = GsonUtil.getObjectFromJsonStr(jsonData, News.class);

		SearchData data = newsService.searchMoreLikeByNewsInfo(newsInfo, from, pageSize, termFreq, docFreq);
		
		String r = GsonUtil.getJsonStringFromObject(data);
		return r;
	}
	
	@Path("/searchByWords")
	@POST
	public String searchByWords(@FormParam("titleWords") String titleWords,
			@FormParam("contentStrs") String contentStrs,
			@FormParam("startTime") Long startTime,
			@FormParam("endTime") Long endTime, 
			@FormParam("pageSize") String pageSize,
			@FormParam("from") String from){
		logger.info("(/search/searchByWords),titleWords" + titleWords
				+ ",contentStrs:" + contentStrs + ",startTime:" + startTime
				+ ",endTime:" + endTime + ",pageSize:" + pageSize + ",from:"
				+ from);

		SearchClient searchClient = new SearchClient();
		Integer size = 20;
		Integer f = 0;
		try {
			if ((startTime != null && endTime == null) || (startTime == null && endTime != null)) {
				return SearchErrorInfo.TIMEARGS;
			}
			if ((startTime!=null&&endTime!=null)&&(startTime >= endTime)) {
				return SearchErrorInfo.TIMEORDERARGS;
			}
			if (titleWords == null&&contentStrs==null) {
				return SearchErrorInfo.ILLEGALARGS;
			}
			if (pageSize != null) {
				size = Integer.valueOf(pageSize);
			}
			if (from != null) {
				f = Integer.valueOf(from);
			}
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			logger.info("按照分类搜索,searchByORTags," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}
		
		List<String> titleWordsList=new ArrayList<String>();
		if(titleWords!=null){
			String[] titleWordsArr = titleWords.split("_");
			titleWordsList = convertArrayToList(titleWordsArr);
		}
		
		List<String> contentStrsList=new ArrayList<String>();
		if(contentStrs!=null){
			String[] contentStrsArr = contentStrs.split("_");
			contentStrsList = convertArrayToList(contentStrsArr);
		}
		
		List<SearchNews> list= searchClient.searchByWords(titleWordsList, contentStrsList, startTime, endTime, f, size);
		
		Long count=searchClient.countByWords(titleWordsList, contentStrsList, startTime, endTime);
		
		SearchData sd = new SearchData(count, list);
		return convertSearchDataToString(sd);
	}
	
	
	@Path("/searchNews")
	@POST
	public String searchNews(@FormParam("jsonData") String jsonData,
			@FormParam("startTime") Long startTime,
			@FormParam("endTime") Long endTime,
			@FormParam("from") int from,
			@FormParam("pageSize") int pageSize,
			@FormParam("orderByColumn") String orderByColumn,
			@FormParam("fullContent") boolean fullContent, 
			@FormParam("highlight") boolean highlight){
		
		logger.info("(/search/searchNews),jsonData:" + jsonData + ",startTime:"
				+ startTime + ",endTime:" + endTime + ",from:" + from
				+ ",pageSize:" + pageSize + ",orderByColumn:" + orderByColumn
				+ ",fullContent:" + fullContent + ",highlight:" + highlight);
		
		try {
			if ((startTime != null && endTime == null)
					|| (startTime == null && endTime != null)) {
				return SearchErrorInfo.TIMEARGS;
			}
			if ((startTime != null && endTime != null)
					&& (startTime >= endTime)) {
				return SearchErrorInfo.TIMEORDERARGS;
			}
			if (pageSize == 0) {
				pageSize = 20;
			}
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			logger.info("按照分类搜索,searchNews," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}
		
		if(CommonUtils.isEmpty(jsonData)){
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}
		NewsSearchCondition searchInfo = GsonUtil.getObjectFromJsonStr(jsonData, NewsSearchCondition.class);
		
		SearchData result = newsService.searchNews(searchInfo, startTime,
				endTime, from, pageSize, orderByColumn, fullContent, highlight);

		return GsonUtil.getJsonStringFromObject(result);
	}
	
	
	private List<String> convertArrayToList(String[] arr) {
		List<String> list = new ArrayList<String>();
		for (String s : arr) {
			list.add(s);
		}
		return list;
	}

	private String convertSearchDataToString(SearchData sd) {
		Gson gson = new Gson();
		return gson.toJson(sd);
	}
	
}
