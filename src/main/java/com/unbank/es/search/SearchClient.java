package com.unbank.es.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.count.CountRequestBuilder;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.mlt.MoreLikeThisRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.CommonUtils;
import com.unbank.common.utils.SourcesUtils;
import com.unbank.entity.News;
import com.unbank.entity.NewsSearchCondition;
import com.unbank.entity.SearchData;
import com.unbank.entity.SearchNews;

public class SearchClient {
	
	private static final Logger logger = LoggerFactory.getLogger(SearchClient.class); 

	private static final String INDEXNAME = SysParameters.INDEXNAME;
	
	/**
	 * 按照单个ID查询
	 * @param crawl_id
	 * @return
	 */
	public News SearchByCrawlId(int crawl_id) {
		logger.info("按照单个crawl_id查询，crawl_id:"+crawl_id);
		
		GetRequestBuilder getRequestBuilder= SearchESClientFactory.getClient().prepareGet(SysParameters.INDEXNAME, "news",String.valueOf(crawl_id));
		GetResponse response = getRequestBuilder.execute().actionGet();
		
		News newsInfo = SourcesUtils.getNews(response.getSource(), true);
		
		if(CommonUtils.isEmpty(newsInfo)){
			return null;
		}
		String content= newsInfo.getContent();
		Document doc=Jsoup.parse(content);
		Elements imgElements= doc.getElementsByTag("img");
		if(imgElements!=null&&imgElements.size()>0){
			for(Element element:imgElements){
				String srcStr= element.attr("src");
				srcStr=srcStr.replace("http://10.0.2.35:8080/", "static/");
				element.attr("src",srcStr);
			}
			String contentStr= doc.body().html().toString();
			newsInfo.setContent(contentStr);
		}
		
		return newsInfo;
	}
	
	
	public List<SearchNews> searchByWords(List<String> titleWords,List<String> contentStrs,Long startTime, Long endTime, int sizeFrom, int size){
		List<SearchNews> list=new ArrayList<SearchNews>();
		
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		if(titleWords!=null){
			BoolQueryBuilder titleBoolQueryBuilder = QueryBuilders.boolQuery();
			for(String titleWord:titleWords){
				titleBoolQueryBuilder.must(QueryBuilders.termQuery("title", titleWord));
			}
			boolQueryBuilder.must(titleBoolQueryBuilder);
		}
		if(contentStrs!=null){
			BoolQueryBuilder contentBoolQueryBuilder = QueryBuilders.boolQuery();
			
			for(String contentStr:contentStrs){
				contentBoolQueryBuilder.must(QueryBuilders.queryStringQuery(contentStr).field("content"));
			}
			boolQueryBuilder.must(contentBoolQueryBuilder);
		}
		
		if (startTime != null && endTime != null) {
			BoolQueryBuilder timeBoolQueryBuilder = QueryBuilders.boolQuery();
			timeBoolQueryBuilder.must(QueryBuilders.rangeQuery(SysParameters.ORDERBYTIMENAME).from(startTime)
					.to(endTime));
			boolQueryBuilder.must(timeBoolQueryBuilder);
		}
		list = search(searchRequestBuilder, boolQueryBuilder, sizeFrom, size);
		return list;
	}
	
	public Long countByWords(List<String> titleWords,List<String> contentStrs,Long startTime, Long endTime){
		CountRequestBuilder countRequestBuilder = SearchESClientFactory.getClient().prepareCount(
				SysParameters.INDEXNAME);
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		if(titleWords!=null){
			BoolQueryBuilder titleBoolQueryBuilder = QueryBuilders.boolQuery();
			for(String titleWord:titleWords){
				titleBoolQueryBuilder.must(QueryBuilders.termQuery("title", titleWord));
			}
			boolQueryBuilder.must(titleBoolQueryBuilder);
		}
		if(contentStrs!=null){
			BoolQueryBuilder contentBoolQueryBuilder = QueryBuilders.boolQuery();
			
			for(String contentStr:contentStrs){
				contentBoolQueryBuilder.must(QueryBuilders.queryStringQuery(contentStr).field("content"));
			}
			boolQueryBuilder.must(contentBoolQueryBuilder);
		}
		
		if (startTime != null && endTime != null) {
			BoolQueryBuilder timeBoolQueryBuilder = QueryBuilders.boolQuery();
			timeBoolQueryBuilder.must(QueryBuilders.rangeQuery(SysParameters.ORDERBYTIMENAME).from(startTime)
					.to(endTime));
			boolQueryBuilder.must(timeBoolQueryBuilder);
		}
		
		countRequestBuilder.setQuery(boolQueryBuilder);
		CountResponse countResponse = countRequestBuilder.execute().actionGet();
		return countResponse.getCount();
	}
	
	
	private List<SearchNews> search(SearchRequestBuilder searchRequestBuilder, QueryBuilder queryBuilder, int sizeFrom,
			int size) {
		List<SearchNews> list = new ArrayList<SearchNews>();
		
		searchRequestBuilder.setQuery(queryBuilder);
		searchRequestBuilder.setFrom(sizeFrom).setSize(size);
		searchRequestBuilder.addSort("crawlDate", SortOrder.DESC);

		/**
		 * 设置高亮
		 */
		searchRequestBuilder.addHighlightedField("content").setHighlighterPreTags("<span style=\"color:red\">")
				.setHighlighterPostTags("</span>");

		/**
		 * 设置高亮
		 */
		searchRequestBuilder.addHighlightedField("title").setHighlighterPreTags("<span style=\"color:red\">")
				.setHighlighterPostTags("</span>");

		// 执行搜索,返回搜索响应信息
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();

		for (SearchHit hit : hits) {
			SearchNews news = SourcesUtils.getNewsInfo(hit.getSource(), true);
			list.add(news);
		}
		return list;
	}
	
	public SearchData search(NewsSearchCondition condition, int from,
			int pageSize, Long startTime, Long endTime, String orderByColumn,
			boolean fullContent, boolean highlight) {
		SearchData sd=new SearchData();
		List<SearchNews> list = new ArrayList<SearchNews>();
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		boolQueryBuilder.must(condition.getQueryBuilder(false));
		
		searchRequestBuilder.setFrom(from).setSize(pageSize);
		if(!"SCORE".equals(orderByColumn)){
			searchRequestBuilder.addSort("newsDate", SortOrder.DESC);
		}
		
		if(startTime!=null&&endTime!=null){
			BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
			boolQueryBuilder2.must(QueryBuilders.rangeQuery("newsDate").from(startTime).to(endTime));
			searchRequestBuilder.setPostFilter(FilterBuilders.queryFilter(boolQueryBuilder2));
		}
		
		searchRequestBuilder.setQuery(boolQueryBuilder);
		
		// 设置高亮
		if(highlight){
			searchRequestBuilder.addHighlightedField("title");
		}
		
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		
		String tempStr = "";
		for (SearchHit hit : hits) {
			Map<String, Object> source = hit.getSource();
			SearchNews news = null;
			// 高亮
			if(highlight){
				Map<String, HighlightField> highlightMap = hit.getHighlightFields();
				news = SourcesUtils.getNewsInfo(source, fullContent, highlightMap);
			}else {
				news = SourcesUtils.getNewsInfo(source, fullContent);
			}
			
			tempStr += tempStr == "" ? news.getCrawl_id() : "_" + news.getCrawl_id();
			
			list.add(news);
		}
		sd.setData(list);
		sd.setCount(searchHits.getTotalHits());
		sd.setCrawlIds(tempStr);
		return sd;
	}
	
	public SearchData search(List<NewsSearchCondition> newsSearchConditions, NewsSearchCondition filterCondition, int from, int pageSize,
			Long startTime, Long endTime, String orderByColumn, boolean fullContent, boolean highlight, boolean searchFlag) {
		
		SearchData sd = new SearchData();
		List<SearchNews> list = new ArrayList<SearchNews>();
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		for (NewsSearchCondition condition : newsSearchConditions) {
			if (!condition.isEmpty()) {
				boolQueryBuilder.should(condition.getQueryBuilder(searchFlag));
			}
			
		}
		searchRequestBuilder.setFrom(from).setSize(pageSize);
		if(!"SCORE".equals(orderByColumn)){
			searchRequestBuilder.addSort("newsDate", SortOrder.DESC);
		}
		
		if ((startTime != null && endTime != null) || filterCondition != null) {
			BoolQueryBuilder filterBuilder= QueryBuilders.boolQuery();
			
			if (startTime != null && endTime != null) {
				BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
				boolQueryBuilder2.must(QueryBuilders.rangeQuery("newsDate").from(startTime).to(endTime));
				filterBuilder.must(boolQueryBuilder2);
			}
			if (filterCondition != null) {
				filterBuilder.must(filterCondition.getQueryBuilder(searchFlag));
			}
			
			searchRequestBuilder.setPostFilter(FilterBuilders.queryFilter(filterBuilder));
		}
		
		searchRequestBuilder.setQuery(boolQueryBuilder);
//		System.out.println(searchRequestBuilder.toString());
		// 设置高亮
		if (highlight) {
			searchRequestBuilder.addHighlightedField("title").addHighlightedField("content");
		}
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		String tempStr = "";
		for (SearchHit hit : hits) {
			Map<String, Object> source = hit.getSource();
			SearchNews news = null;
			// 高亮
			if(highlight){
				Map<String, HighlightField> highlightMap = hit.getHighlightFields();
				news = SourcesUtils.getNewsInfo(source, fullContent, highlightMap);
			}else {
				news = SourcesUtils.getNewsInfo(source, fullContent);
			}
			
			tempStr += tempStr == "" ? news.getCrawl_id() : "_" + news.getCrawl_id();
			
			list.add(news);
		}
		sd.setData(list);
		sd.setCount(searchHits.getTotalHits());
		sd.setCrawlIds(tempStr);
		return sd;
	}
	
	
	
	
	public List<SearchNews> SearchMoreLikeByNewsID(String crawl_id,int from,int pageSize){
		List<SearchNews> list = new ArrayList<SearchNews>();
		Client cilent=SearchESClientFactory.getClient();
		MoreLikeThisRequestBuilder builder = new MoreLikeThisRequestBuilder( cilent, INDEXNAME, "news",
				crawl_id);
		builder.setField("content");// 匹配的字段
		builder.setSearchFrom(from).setSearchSize(pageSize);
		long time1=System.currentTimeMillis();
		SearchResponse response =  cilent.moreLikeThis(builder.request()).actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		long time2=System.currentTimeMillis();
		logger.info("news more like search time:"+(time2-time1));
		for (SearchHit hit : hits) {
			// 提取新闻数据
			Map<String, Object> source = hit.getSource();
			SearchNews newsInfo = new SearchNews();
			newsInfo.setCrawl_id(Integer.parseInt(source.get("crawl_id").toString()));
			newsInfo.setTitle(source.get("title").toString());
			
			list.add(newsInfo);
		}
		long time3=System.currentTimeMillis();
		logger.info("提取新闻数据:"+(time3-time2));
		return list;
	}
	
	
}
