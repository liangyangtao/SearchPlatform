package com.unbank.es.risk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.unbank.common.utils.SourcesUtils;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.mlt.MoreLikeThisRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.CommonUtils;
import com.unbank.entity.risk.Risk;
import com.unbank.entity.risk.RiskSearchCondition;
import com.unbank.entity.risk.SearchRisk;
import com.unbank.entity.risk.SearchRiskData;
import com.unbank.es.search.SearchESClientFactory;

public class RiskSearchClient {

	private static final Logger logger = LoggerFactory.getLogger(RiskSearchClient.class);
	private static final String INDEXNAME = SysParameters.RISKINDEX;

	/**
	 * 使用crawl_id查询es_id
	 * @param crawl_id
	 * @return es_id
	 */
	public String searchByCrawlId(int crawl_id) {
		logger.info("风险库，按照单个crawl_id查询ES_ID，crawl_id:" + crawl_id);
        try {
            SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
            searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.termQuery("crawl_id", crawl_id));
            searchRequestBuilder.setQuery(boolQueryBuilder);
            SearchResponse response = searchRequestBuilder.execute().actionGet();
            SearchHits searchHits = response.getHits();
            SearchHit[] hits = searchHits.getHits();

            if (CommonUtils.isNotEmpty(hits)) {
                return hits[0].getId();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Risk searchByCrawlId(int crawl_id,int categoryId) {
		logger.info("风险库，按照单个crawl_id查询，crawl_id:" + crawl_id);
	
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		boolQueryBuilder.must(QueryBuilders.termQuery("crawl_id", crawl_id));
		boolQueryBuilder.must(QueryBuilders.termQuery("categoryId", categoryId));
		
		searchRequestBuilder.setQuery(boolQueryBuilder);
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		if(hits==null||hits.length==0){
			return null;
		}
		SearchHit hit =hits[0];
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Risk risk = mapper.readValue(hit.getSourceAsString(), Risk.class);
			return risk;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 依据ES自动生成的ID进行搜索对应的Risk
	 * @param id
	 * @return
	 */
	public SearchRisk searchByESId(String id){
		GetRequestBuilder getRequestBuilder= SearchESClientFactory.getClient().prepareGet(INDEXNAME, "risk",id);
		GetResponse response = getRequestBuilder.execute().actionGet();
		
		if(response.isSourceEmpty()){
			return null;
		}
		
		SearchRisk risk = SourcesUtils.getRiskInfo(response.getSource(), true);
		risk.setEsId(id);
		return risk;
	}
	
	public SearchRiskData search(RiskSearchCondition condition,int from ,int pageSize,Long startTime,Long endTime,String orderByColumn){
		SearchRiskData sd=new SearchRiskData();
		List<SearchRisk> list = new ArrayList<SearchRisk>();
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		boolQueryBuilder.must(condition.getQueryBuilder());
		searchRequestBuilder.setFrom(from).setSize(pageSize);
		
		if("SCORE".equals(orderByColumn)){
			
		} else if(orderByColumn==null||"TIME".equals(orderByColumn)){
			searchRequestBuilder.addSort("newsDate", SortOrder.DESC);
		}else {
			searchRequestBuilder.addSort(orderByColumn, SortOrder.DESC);
		}
		
		if(startTime!=null&&endTime!=null){
			BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
			boolQueryBuilder2.must(QueryBuilders.rangeQuery("newsDate").from(startTime).to(endTime));
			searchRequestBuilder.setPostFilter(FilterBuilders.queryFilter(boolQueryBuilder2));
		}
		
		searchRequestBuilder.setQuery(boolQueryBuilder);
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		
		for (SearchHit hit : hits) {
			SearchRisk risk = SourcesUtils.getRiskInfo(hit.getSource(), true);
			risk.setEsId(hit.getId());
			list.add(risk);
		}
		sd.setData(list);
		sd.setCount(searchHits.getTotalHits());
		return sd;
	}
	
	public SearchRiskData search(ArrayList<RiskSearchCondition> conditions,RiskSearchCondition filterCondition,int from ,int pageSize,Long startTime,Long endTime,String orderByColumn){
		SearchRiskData sd=new SearchRiskData();
		List<SearchRisk> list = new ArrayList<SearchRisk>();
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		for(RiskSearchCondition condition :conditions){
			boolQueryBuilder.should(condition.getQueryBuilder());
		}
		searchRequestBuilder.setFrom(from).setSize(pageSize);
		
		if("SCORE".equals(orderByColumn)){
			
		} else if(orderByColumn==null||"TIME".equals(orderByColumn)){
			searchRequestBuilder.addSort("newsDate", SortOrder.DESC);
		}else {
			searchRequestBuilder.addSort(orderByColumn, SortOrder.DESC);
		}
		
		
		BoolQueryBuilder filterBuilder= QueryBuilders.boolQuery();
		
		if(startTime!=null&&endTime!=null){
			BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
			boolQueryBuilder2.must(QueryBuilders.rangeQuery("newsDate").from(startTime).to(endTime));
			filterBuilder.must(boolQueryBuilder2);
		}
		
		if(filterCondition!=null){
			filterBuilder.must(filterCondition.getQueryBuilder());
		}
		
		searchRequestBuilder.setPostFilter(FilterBuilders.queryFilter(filterBuilder));
		
		searchRequestBuilder.setQuery(boolQueryBuilder);
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		
		for (SearchHit hit : hits) {
			SearchRisk risk = SourcesUtils.getRiskInfo(hit.getSource(), true);
			risk.setEsId(hit.getId());
			list.add(risk);
		}
		sd.setData(list);
		sd.setCount(searchHits.getTotalHits());
		return sd;
	}
	
	public SearchRiskData moreLikeSearch(String esId,int from ,int pageSize) {
		SearchRiskData sd=new SearchRiskData();
		List<SearchRisk> list = new ArrayList<SearchRisk>();
		
		MoreLikeThisRequestBuilder builder = new MoreLikeThisRequestBuilder( SearchESClientFactory.getClient(),INDEXNAME, "risk",
				esId);
		builder.setField("title","content");// 匹配的字段
		builder.setPercentTermsToMatch(0.35f);
		builder.setSearchFrom(from).setSearchSize(pageSize);
		
		long time1=System.currentTimeMillis();
		SearchResponse response =  SearchESClientFactory.getClient().moreLikeThis(builder.request()).actionGet();
		
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		long time2=System.currentTimeMillis();
		logger.info("risk more like search by es ID time:"+(time2-time1));
		
		for (SearchHit hit : hits) {
			SearchRisk risk = SourcesUtils.getRiskInfo(hit.getSource(), true);
			risk.setEsId(hit.getId());
			list.add(risk);
		}
		
		sd.setCount(searchHits.getTotalHits());
		sd.setData(list);
		return sd;
	}
	
	
	
	public SearchRiskData moreLikeSearch(int crawl_id,int categoryId,int from ,int pageSize) {
		SearchRiskData sd=new SearchRiskData();
		List<SearchRisk> list = new ArrayList<SearchRisk>();
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		boolQueryBuilder.must(QueryBuilders.termQuery("crawl_id", crawl_id));
		boolQueryBuilder.must(QueryBuilders.termQuery("categoryId", categoryId));
		
		searchRequestBuilder.setQuery(boolQueryBuilder);
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		if(hits==null||hits.length==0){
			return null;
		}
		SearchHit hit =hits[0];
		
		String id= hit.getId();
		
		MoreLikeThisRequestBuilder builder = new MoreLikeThisRequestBuilder( SearchESClientFactory.getClient(),INDEXNAME, "risk",
				id);
		
		builder.setField("title","content");// 匹配的字段
		builder.setPercentTermsToMatch(0.35f);
		builder.setSearchFrom(from).setSearchSize(pageSize);
		
		long time1=System.currentTimeMillis();
		SearchResponse response2 =  SearchESClientFactory.getClient().moreLikeThis(builder.request()).actionGet();
		
		SearchHits searchHits2 = response2.getHits();
		SearchHit[] hits2 = searchHits2.getHits();
		long time2=System.currentTimeMillis();
		logger.info("risk more like search time:"+(time2-time1));
		
		for (SearchHit hit2 : hits2) {
			SearchRisk info = SourcesUtils.getRiskInfo(hit2.getSource(), true);
			info.setEsId(hit2.getId());

			list.add(info);
		}
		
		sd.setCount(searchHits2.getTotalHits());
		sd.setData(list);
		return sd;
	}
	
	
	public SearchRiskData queryStringQuery(String queryString,HashMap<String ,Float> columnBoost,RiskSearchCondition filterCondition, int from, int pageSize){
		SearchRiskData sd=new SearchRiskData();
		List<SearchRisk> list = new ArrayList<SearchRisk>();
		
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		
		searchRequestBuilder.setFrom(from).setSize(pageSize);
		
		QueryStringQueryBuilder builder=new QueryStringQueryBuilder(QueryParser.escape(queryString));
		
		builder.allowLeadingWildcard(false);
		
		if(columnBoost!=null){
			Set<String> keySet=columnBoost.keySet();
			for(String key:keySet){
				builder.field(key, columnBoost.get(key));
			}
		}else{
			builder.field("title", 4.0f);
			builder.field("content", 1.0f);
			builder.field("keyWords", 2.0f);
			builder.field("category", 2.0f);
		}
		
		if(filterCondition!=null){
			searchRequestBuilder.setPostFilter(FilterBuilders.queryFilter(filterCondition.getQueryBuilder()));
		}
		searchRequestBuilder.setQuery(builder);
		
		searchRequestBuilder.setMinScore(SysParameters.STRINGQUERYMINSCORE);
		
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();

		for (SearchHit hit : hits) {
			String json = hit.getSourceAsString();
			ObjectMapper mapper = new ObjectMapper();
			try {
				SearchRisk risk = mapper.readValue(json, SearchRisk.class);
				risk.setEsId(hit.getId());
				list.add(risk);
			} catch (JsonParseException e) {
				logger.error(e.getMessage());
			} catch (JsonMappingException e) {
				logger.error(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		
		sd.setCount(searchHits.getTotalHits());
		sd.setData(list);
		return sd;
	}
	
}
