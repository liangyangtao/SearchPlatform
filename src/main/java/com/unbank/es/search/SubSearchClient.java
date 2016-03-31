package com.unbank.es.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.count.CountRequestBuilder;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unbank.common.constants.SysParameters;
import com.unbank.entity.SearchNews;

public class SubSearchClient {

	private static final Logger logger = LoggerFactory.getLogger(SubSearchClient.class);
	private static final String INDEXNAME = SysParameters.INDEXNAME;
	
	/**
	 * 多个标签，标签之间按照或的关系取并集
	 * 
	 * @param tags
	 * @param sizeFrom
	 * @param size
	 * @return
	 */
	public List<SearchNews>searchByORTags(List<String> tags, Long startTime, Long endTime, int sizeFrom, int size) {
		List<SearchNews>list = new ArrayList<SearchNews>();
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder tagBoolQueryBuilder = QueryBuilders.boolQuery();
		
		for (String tag : tags) {
			tagBoolQueryBuilder.should(QueryBuilders.termQuery("tagName", tag));
		}
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(tagBoolQueryBuilder);
		if (startTime != null && endTime != null) {
			BoolQueryBuilder timeBoolQueryBuilder = QueryBuilders.boolQuery();
			timeBoolQueryBuilder.must(QueryBuilders.rangeQuery(SysParameters.ORDERBYTIMENAME).from(startTime)
					.to(endTime));
			boolQueryBuilder.must(timeBoolQueryBuilder);
		}

		list = search(searchRequestBuilder, boolQueryBuilder, sizeFrom, size,0f);
		return list;
	}

	/**
	 * 搜索多个标签按照或关系进行搜索的数量
	 * 
	 * @param tags
	 * @return
	 */
	public Long countByORTags(List<String> tags, Long startTime, Long endTime) {
		CountRequestBuilder countRequestBuilder = SearchESClientFactory.getClient().prepareCount(
				SysParameters.INDEXNAME);
		BoolQueryBuilder tagBoolQueryBuilder = QueryBuilders.boolQuery();

		for (String tag : tags) {
			tagBoolQueryBuilder.should(QueryBuilders.termQuery("tagName", tag));
		}
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(tagBoolQueryBuilder);
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

	/**
	 * 多个标签，标签之间按照与的关系取交集
	 * 
	 * @param tags
	 * @param sizeFrom
	 * @param size
	 * @return
	 */
	public List<SearchNews>searchByANDTags(List<String> tags, Long startTime, Long endTime, int sizeFrom, int size) {
		List<SearchNews>list = new ArrayList<SearchNews>();
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder tagBoolQueryBuilder = QueryBuilders.boolQuery();

		for (String tag : tags) {
			tagBoolQueryBuilder.must(QueryBuilders.termQuery("tagName", tag));
		}
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(tagBoolQueryBuilder);
		if (startTime != null && endTime != null) {
			BoolQueryBuilder timeBoolQueryBuilder = QueryBuilders.boolQuery();
			timeBoolQueryBuilder.must(QueryBuilders.rangeQuery(SysParameters.ORDERBYTIMENAME).from(startTime)
					.to(endTime));
			boolQueryBuilder.must(timeBoolQueryBuilder);
		}
		list = search(searchRequestBuilder, boolQueryBuilder, sizeFrom, size,0f);
		return list;
	}

	/**
	 * 搜索多个标签按照或关系进行搜索的数量
	 * 
	 * @param tags
	 * @return
	 */
	public Long countByANDTags(List<String> tags, Long startTime, Long endTime) {
		CountRequestBuilder countRequestBuilder = SearchESClientFactory.getClient().prepareCount(
				SysParameters.INDEXNAME);
		BoolQueryBuilder tagBoolQueryBuilder = QueryBuilders.boolQuery();

		for (String tag : tags) {
			tagBoolQueryBuilder.must(QueryBuilders.termQuery("tagName", tag));
		}
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(tagBoolQueryBuilder);
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

	/**
	 * 获取多个标签的并集中的非集合
	 * 
	 * @param mustTags
	 * @param mustNotTags
	 * @param startTime
	 * @param endTime
	 * @param sizeFrom
	 * @param size
	 * @return
	 */
	public List<SearchNews>searchByNotTags(List<String> mustTags, List<String> mustNotTags, Long startTime, Long endTime,
			int sizeFrom, int size) {
		List<SearchNews>list = new ArrayList<SearchNews>();
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		BoolQueryBuilder tagBoolQueryBuilder = QueryBuilders.boolQuery();

		for (String mustTag : mustTags) {
			tagBoolQueryBuilder.should(QueryBuilders.termQuery("tagName", mustTag));
		}
		boolQueryBuilder.must(tagBoolQueryBuilder);

		for (String mustTag : mustNotTags) {
			tagBoolQueryBuilder.mustNot(QueryBuilders.termQuery("tagName", mustTag));
		}
		boolQueryBuilder.must(tagBoolQueryBuilder);
		if (startTime != null && endTime != null) {
			BoolQueryBuilder timeBoolQueryBuilder = QueryBuilders.boolQuery();
			timeBoolQueryBuilder.must(QueryBuilders.rangeQuery(SysParameters.ORDERBYTIMENAME).from(startTime)
					.to(endTime));
			boolQueryBuilder.must(timeBoolQueryBuilder);
		}
		list = search(searchRequestBuilder, boolQueryBuilder, sizeFrom, size,0f);
		return list;
	}

	public Long countByNotTags(List<String> mustTags, List<String> mustNotTags, Long startTime, Long endTime) {
		CountRequestBuilder countRequestBuilder = SearchESClientFactory.getClient().prepareCount(
				SysParameters.INDEXNAME);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		BoolQueryBuilder tagBoolQueryBuilder = QueryBuilders.boolQuery();

		for (String mustTag : mustTags) {
			tagBoolQueryBuilder.should(QueryBuilders.termQuery("tagName", mustTag));
		}
		boolQueryBuilder.must(tagBoolQueryBuilder);

		BoolQueryBuilder notTagBoolQueryBuilder = QueryBuilders.boolQuery();

		for (String mustTag : mustNotTags) {
			notTagBoolQueryBuilder.should(QueryBuilders.termQuery("tagName", mustTag));
		}
		boolQueryBuilder.mustNot(notTagBoolQueryBuilder);

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

	/**
	 * 搜索多组标签之间的“与”、“或”、“非”关系
	 * 
	 * @param mustTags
	 * @param shouldTags
	 * @param mustNotTags
	 * @param startTime
	 * @param endTime
	 * @param sizeFrom
	 * @param size
	 * @return
	 */
	public List<SearchNews>searchByMultigroupTags(List<String> mustTags, List<String> shouldTags, List<String> mustNotTags,
			Long startTime, Long endTime, int sizeFrom, int size) {
		List<SearchNews>list = new ArrayList<SearchNews>();
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		if (mustTags != null) {
			BoolQueryBuilder mustTagBoolQueryBuilder = QueryBuilders.boolQuery();
			for (String mustTag : mustTags) {
				mustTagBoolQueryBuilder.must(QueryBuilders.termQuery("tagName", mustTag));
			}
			boolQueryBuilder.should(mustTagBoolQueryBuilder);
		}

		if (shouldTags != null) {
			BoolQueryBuilder shouldTagBoolQueryBuilder = QueryBuilders.boolQuery();
			for (String shouldTag : shouldTags) {
				shouldTagBoolQueryBuilder.should(QueryBuilders.termQuery("tagName", shouldTag));
			}
			boolQueryBuilder.should(shouldTagBoolQueryBuilder);
		}

		if (mustNotTags != null) {
			BoolQueryBuilder mustNotTagBoolQueryBuilder = QueryBuilders.boolQuery();
			for (String mustNotTag : mustNotTags) {
				mustNotTagBoolQueryBuilder.should(QueryBuilders.termQuery("tagName", mustNotTag));
			}
			boolQueryBuilder.mustNot(mustNotTagBoolQueryBuilder);
		}

		if (startTime != null && endTime != null) {
			BoolQueryBuilder timeBoolQueryBuilder = QueryBuilders.boolQuery();
			timeBoolQueryBuilder.must(QueryBuilders.rangeQuery(SysParameters.ORDERBYTIMENAME).from(startTime)
					.to(endTime));
			boolQueryBuilder.must(timeBoolQueryBuilder);
		}
		list = search(searchRequestBuilder, boolQueryBuilder, sizeFrom, size,0f);
		return list;
	}

	/**
	 * 计算多组标签之间按照“与”、“或”、“非”关系搜索出来的数量
	 * 
	 * @param mustTags
	 * @param shouldTags
	 * @param mustNotTags
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Long countByMultigroupTags(List<String> mustTags, List<String> shouldTags, List<String> mustNotTags,
			Long startTime, Long endTime) {
		CountRequestBuilder countRequestBuilder = SearchESClientFactory.getClient().prepareCount(
				SysParameters.INDEXNAME);

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		if (mustTags != null) {
			BoolQueryBuilder mustTagBoolQueryBuilder = QueryBuilders.boolQuery();
			for (String mustTag : mustTags) {
				mustTagBoolQueryBuilder.must(QueryBuilders.termQuery("tagName", mustTag));
			}
			boolQueryBuilder.should(mustTagBoolQueryBuilder);
		}

		if (shouldTags != null) {
			BoolQueryBuilder shouldTagBoolQueryBuilder = QueryBuilders.boolQuery();
			for (String shouldTag : shouldTags) {
				shouldTagBoolQueryBuilder.should(QueryBuilders.termQuery("tagName", shouldTag));
			}
			boolQueryBuilder.should(shouldTagBoolQueryBuilder);
		}

		if (mustNotTags != null) {
			BoolQueryBuilder mustNotTagBoolQueryBuilder = QueryBuilders.boolQuery();
			for (String mustNotTag : mustNotTags) {
				mustNotTagBoolQueryBuilder.should(QueryBuilders.termQuery("tagName", mustNotTag));
			}
			boolQueryBuilder.mustNot(mustNotTagBoolQueryBuilder);
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

	/**
	 * 按照多个标签（标签之间按照或关系取并集）和指定信息源（多个信息源之间按照或关系取并集）进行与运算取交集
	 * 
	 * @param tags
	 * @param websiteIDs
	 * @param sizeFrom
	 * @param size
	 * @return
	 */
	public List<SearchNews>searchByORTagsAndORWebsiteIDs(List<String> tags, List<String> websiteIDs,String titleStr,String contentStr, Long startTime,
			Long endTime, int sizeFrom, int size) {
		List<SearchNews>list = new ArrayList<SearchNews>();
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		if (tags != null) {
			BoolQueryBuilder tagBoolQueryBuilder = QueryBuilders.boolQuery();
			for (String tag : tags) {
				tagBoolQueryBuilder.should(QueryBuilders.termQuery("tagName", tag));
			}
			boolQueryBuilder.must(tagBoolQueryBuilder);
		}
		if (websiteIDs != null) {
			BoolQueryBuilder webisteIDBoolQueryBuilder = QueryBuilders.boolQuery();
			for (String websiteID : websiteIDs) {
				webisteIDBoolQueryBuilder.should(QueryBuilders.termQuery("website_id", websiteID));
			}
			boolQueryBuilder.must(webisteIDBoolQueryBuilder);
		}
		
		if(titleStr!=null){
			BoolQueryBuilder titleBoolQueryBuilder = QueryBuilders.boolQuery();
			titleBoolQueryBuilder.must(QueryBuilders.matchPhraseQuery("title", titleStr));
			boolQueryBuilder.must(titleBoolQueryBuilder);
		}
		if(contentStr!=null){
			BoolQueryBuilder contentBoolQueryBuilder = QueryBuilders.boolQuery();
			contentBoolQueryBuilder.should(QueryBuilders.matchPhraseQuery("content", contentStr));
			boolQueryBuilder.must(contentBoolQueryBuilder);
		}
		
		if (startTime != null && endTime != null) {
			BoolQueryBuilder timeBoolQueryBuilder = QueryBuilders.boolQuery();
			timeBoolQueryBuilder.must(QueryBuilders.rangeQuery(SysParameters.ORDERBYTIMENAME).from(startTime)
					.to(endTime));
			boolQueryBuilder.must(timeBoolQueryBuilder);
		}
		
		list = search(searchRequestBuilder, boolQueryBuilder, sizeFrom, size,0f);
		return list;
	}
	
	/**
	 * 按照多个标签（标签之间"或、与、非"）和指定信息源（多个信息源之间按照或关系取并集）进行与运算取交集
	 * 
	 * @param tags
	 * @param websiteIDs
	 * @param sizeFrom
	 * @param size
	 * @return
	 */
	public List<SearchNews>searchByMultiTagsAndWebsiteIDs(List<String> mustTags,List<String> mustNotTags,List<String> shouldTags, 
			List<String> websiteIDs,String titleStr,String contentStr, Long startTime,
			Long endTime, int sizeFrom, int size) {
		List<SearchNews>list = new ArrayList<SearchNews>();
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		BoolQueryBuilder tagBoolQueryBuilder= composeQueryBuilder(mustTags,mustNotTags,shouldTags,"tagName");
		
		boolQueryBuilder.must(tagBoolQueryBuilder);
		
		int minScoreArg=1;
		
		if(mustTags!=null){
			minScoreArg+=mustTags.size();
		}
		if(mustNotTags!=null){
			minScoreArg+=mustNotTags.size();
		}
		if(shouldTags!=null){
			minScoreArg+=shouldTags.size();
		}
		
		if (websiteIDs != null) {
			minScoreArg+=websiteIDs.size();
			BoolQueryBuilder webisteIDBoolQueryBuilder = QueryBuilders.boolQuery();
			for (String websiteID : websiteIDs) {
				webisteIDBoolQueryBuilder.should(QueryBuilders.termQuery("website_id", websiteID));
			}
			boolQueryBuilder.must(webisteIDBoolQueryBuilder);
		}
		
		if(titleStr!=null){
			BoolQueryBuilder titleBoolQueryBuilder = QueryBuilders.boolQuery();
			//titleBoolQueryBuilder.must(QueryBuilders.termQuery("title", titleStr));
			titleBoolQueryBuilder.must(QueryBuilders.matchPhraseQuery("title", titleStr));
			boolQueryBuilder.must(titleBoolQueryBuilder);
		}
		
		if(contentStr!=null){
			BoolQueryBuilder contentBoolQueryBuilder = QueryBuilders.boolQuery();
			contentBoolQueryBuilder.should(QueryBuilders.matchPhraseQuery("content", contentStr));
			boolQueryBuilder.must(contentBoolQueryBuilder);
		}
		
		if (startTime != null && endTime != null) {
			BoolQueryBuilder timeBoolQueryBuilder = QueryBuilders.boolQuery();
			timeBoolQueryBuilder.must(QueryBuilders.rangeQuery(SysParameters.ORDERBYTIMENAME).from(startTime)
					.to(endTime));
			boolQueryBuilder.must(timeBoolQueryBuilder);
		}
		float minScore= computeMinScore(SysParameters.MINSCORE,minScoreArg);
		list = search(searchRequestBuilder, boolQueryBuilder, sizeFrom, size,minScore);
		return list;
	}
	

	/**
	 * 按照多个标签（标签之间"或、与、非"）和指定信息源（多个信息源之间按照或关系取并集）进行与运算取交集
	 * 
	 * @param tags
	 * @param websiteIDs
	 * @param sizeFrom
	 * @param size
	 * @return
	 */
	public Long countByMultiTagsAndWebsiteIDs(List<String> mustTags,List<String> mustNotTags,List<String> shouldTags, 
			List<String> websiteIDs,String titleStr,String contentStr, Long startTime,
			Long endTime) {
		/*CountRequestBuilder countRequestBuilder = SearchESClientFactory.getClient().prepareCount(
				SysParameters.INDEXNAME);*/
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(
				SysParameters.INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.COUNT);
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		BoolQueryBuilder tagBoolQueryBuilder= composeQueryBuilder(mustTags,mustNotTags,shouldTags,"tagName");
		
		boolQueryBuilder.must(tagBoolQueryBuilder);
		
		int minScoreArg=1;
		
		if(mustTags!=null){
			minScoreArg+=mustTags.size();
		}
		if(mustNotTags!=null){
			minScoreArg+=mustNotTags.size();
		}
		if(shouldTags!=null){
			minScoreArg+=shouldTags.size();
		}
		
		
		if (websiteIDs != null) {
			minScoreArg+=websiteIDs.size();
			BoolQueryBuilder webisteIDBoolQueryBuilder = QueryBuilders.boolQuery();
			for (String websiteID : websiteIDs) {
				webisteIDBoolQueryBuilder.should(QueryBuilders.termQuery("website_id", websiteID));
			}
			boolQueryBuilder.must(webisteIDBoolQueryBuilder);
		}
		
		if(titleStr!=null){
			BoolQueryBuilder titleBoolQueryBuilder = QueryBuilders.boolQuery();
			titleBoolQueryBuilder.must(QueryBuilders.matchPhraseQuery("title", titleStr));
			boolQueryBuilder.must(titleBoolQueryBuilder);
		}
		
		if(contentStr!=null){
			BoolQueryBuilder contentBoolQueryBuilder = QueryBuilders.boolQuery();
			contentBoolQueryBuilder.should(QueryBuilders.matchPhraseQuery("content", contentStr));
			boolQueryBuilder.must(contentBoolQueryBuilder);
		}
		
		if (startTime != null && endTime != null) {
			BoolQueryBuilder timeBoolQueryBuilder = QueryBuilders.boolQuery();
			timeBoolQueryBuilder.must(QueryBuilders.rangeQuery(SysParameters.ORDERBYTIMENAME).from(startTime)
					.to(endTime));
			boolQueryBuilder.must(timeBoolQueryBuilder);
		}
		
		float minScore= computeMinScore(SysParameters.MINSCORE,minScoreArg);
		
		searchRequestBuilder.setMinScore(minScore);
		searchRequestBuilder.setExplain(true);
		searchRequestBuilder.setQuery(boolQueryBuilder);
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		Long count= response.getHits().getTotalHits();
		return count;
	}
	

	
	/**
	 * 按照多个标签（标签之间按照或关系取并集）和指定信息源（多个信息源之间按照或关系取并集）进行与运算取交集的数量
	 * @param tags
	 * @param websiteIDs
	 * @return
	 */
	public Long countByORTagsAndORWebsiteIDs(List<String> tags, List<String> websiteIDs, String titleStr,String contentStr,Long startTime, Long endTime) {
		CountRequestBuilder countRequestBuilder = SearchESClientFactory.getClient().prepareCount(
				SysParameters.INDEXNAME);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		if (tags != null) {
			BoolQueryBuilder tagBoolQueryBuilder = QueryBuilders.boolQuery();
			for (String tag : tags) {
				tagBoolQueryBuilder.should(QueryBuilders.termQuery("tagName", tag));
			}
			boolQueryBuilder.must(tagBoolQueryBuilder);
		}
		if (websiteIDs != null) {
			BoolQueryBuilder webisteIDBoolQueryBuilder = QueryBuilders.boolQuery();
			for (String websiteID : websiteIDs) {
				webisteIDBoolQueryBuilder.should(QueryBuilders.termQuery("website_id", websiteID));
			}
			boolQueryBuilder.must(webisteIDBoolQueryBuilder);
		}

		if(titleStr!=null){
			BoolQueryBuilder titleBoolQueryBuilder = QueryBuilders.boolQuery();
			titleBoolQueryBuilder.must(QueryBuilders.termQuery("title", titleStr));
			boolQueryBuilder.must(titleBoolQueryBuilder);
		}
		if(contentStr!=null){
			BoolQueryBuilder contentBoolQueryBuilder = QueryBuilders.boolQuery();
			contentBoolQueryBuilder.must(QueryBuilders.queryStringQuery(contentStr).field("content"));
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

	/**
	 * 获取从多个信息源采集到的信息
	 * 
	 * @param websiteIDList
	 * @param startTime
	 * @param endTime
	 * @param sizeFrom
	 * @param size
	 * @return
	 */
	public List<SearchNews>searchByORWebsiteIDs(List<Integer> websiteIDList, Long startTime, Long endTime, int sizeFrom,
			int size) {
		List<SearchNews>list = new ArrayList<SearchNews>();
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		BoolQueryBuilder websiteIDBoolQueryBuilder = QueryBuilders.boolQuery();
		for (Integer websiteID : websiteIDList) {
			websiteIDBoolQueryBuilder.should(QueryBuilders.termQuery("website_id", websiteID));
		}
		boolQueryBuilder.must(websiteIDBoolQueryBuilder);

		if (startTime != null && endTime != null) {
			BoolQueryBuilder timeBoolQueryBuilder = QueryBuilders.boolQuery();
			timeBoolQueryBuilder.must(QueryBuilders.rangeQuery(SysParameters.ORDERBYTIMENAME).from(startTime)
					.to(endTime));
			boolQueryBuilder.must(timeBoolQueryBuilder);
		}
		list = search(searchRequestBuilder, boolQueryBuilder, sizeFrom, size,0f);
		return list;
	}

	/**
	 * 计算从多个信息源获取的新闻数量
	 * 
	 * @param websiteIDList
	 * @param startTime
	 * @param endTime
	 * @param sizeFrom
	 * @param size
	 * @return
	 */
	public Long countByORWebsiteIDs(List<Integer> websiteIDList, Long startTime, Long endTime, int sizeFrom, int size) {
		CountRequestBuilder countRequestBuilder = SearchESClientFactory.getClient().prepareCount(
				SysParameters.INDEXNAME);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		BoolQueryBuilder websiteIDBoolQueryBuilder = QueryBuilders.boolQuery();
		for (Integer websiteID : websiteIDList) {
			websiteIDBoolQueryBuilder.should(QueryBuilders.termQuery("website_id", websiteID));
		}
		boolQueryBuilder.must(websiteIDBoolQueryBuilder);

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

	/**
	 * 获取被多个信息源都发布过的新闻
	 * 
	 * @param websiteIDList
	 * @param startTime
	 * @param endTime
	 * @param sizeFrom
	 * @param size
	 * @return
	 */
	public List<SearchNews>searchByANDWebsiteIDs(List<Integer> websiteIDList, Long startTime, Long endTime, int sizeFrom,
			int size) {
		List<SearchNews>list = new ArrayList<SearchNews>();
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		BoolQueryBuilder websiteIDBoolQueryBuilder = QueryBuilders.boolQuery();
		for (Integer websiteID : websiteIDList) {
			websiteIDBoolQueryBuilder.must(QueryBuilders.termQuery("website_id", websiteID));
		}
		boolQueryBuilder.must(websiteIDBoolQueryBuilder);

		if (startTime != null && endTime != null) {
			BoolQueryBuilder timeBoolQueryBuilder = QueryBuilders.boolQuery();
			timeBoolQueryBuilder.must(QueryBuilders.rangeQuery(SysParameters.ORDERBYTIMENAME).from(startTime)
					.to(endTime));
			boolQueryBuilder.must(timeBoolQueryBuilder);
		}
		list = search(searchRequestBuilder, boolQueryBuilder, sizeFrom, size,0f);
		return list;
	}

	/**
	 * 计算被多个信息源都报道过的新闻数量
	 * 
	 * @param websiteIDList
	 * @param startTime
	 * @param endTime
	 * @param sizeFrom
	 * @param size
	 * @return
	 */
	public Long countByANDWebsiteIDs(List<Integer> websiteIDList, Long startTime, Long endTime, int sizeFrom, int size) {
		CountRequestBuilder countRequestBuilder = SearchESClientFactory.getClient().prepareCount(
				SysParameters.INDEXNAME);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		BoolQueryBuilder websiteIDBoolQueryBuilder = QueryBuilders.boolQuery();
		for (Integer websiteID : websiteIDList) {
			websiteIDBoolQueryBuilder.must(QueryBuilders.termQuery("website_id", websiteID));
		}
		boolQueryBuilder.must(websiteIDBoolQueryBuilder);

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

	/**
	 * 搜索属于某个标签或者来源于多个信息源的新闻
	 * @param tag
	 * @param websiteIDList
	 * @param startTime
	 * @param endTime
	 * @param sizeFrom
	 * @param size
	 * @return
	 */
	public List<SearchNews>searchByOneTagORWebsiteIDs(String tag, List<Integer> websiteIDList, Long startTime, Long endTime,
			int sizeFrom, int size) {
		List<SearchNews>list = new ArrayList<SearchNews>();
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
		
		BoolQueryBuilder tagBoolQueryBuilder = QueryBuilders.boolQuery();
		tagBoolQueryBuilder.should(QueryBuilders.termQuery("tagName", tag));
		boolQueryBuilder.should(tagBoolQueryBuilder);
		
		BoolQueryBuilder websiteIDBoolQueryBuilder = QueryBuilders.boolQuery();
		for (Integer websiteID : websiteIDList) {
			websiteIDBoolQueryBuilder.should(QueryBuilders.termQuery("website_id", websiteID));
		}
		boolQueryBuilder.should(websiteIDBoolQueryBuilder);
		boolQueryBuilder2.must(boolQueryBuilder);
		
		if (startTime != null && endTime != null) {
			BoolQueryBuilder timeBoolQueryBuilder = QueryBuilders.boolQuery();
			timeBoolQueryBuilder.must(QueryBuilders.rangeQuery(SysParameters.ORDERBYTIMENAME).from(startTime)
					.to(endTime));
			boolQueryBuilder2.must(timeBoolQueryBuilder);
		}
		list = search(searchRequestBuilder, boolQueryBuilder2, sizeFrom, size,0f);
		return list;
	}

	/**
	 * 计算来源某个标签或者多个信息源的新闻个数
	 * @param tag
	 * @param websiteIDList
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Long countByOneTagORWebsiteIDs(String tag, List<Integer> websiteIDList, Long startTime, Long endTime) {
		CountRequestBuilder countRequestBuilder = SearchESClientFactory.getClient().prepareCount(
				SysParameters.INDEXNAME);

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
		
		BoolQueryBuilder tagBoolQueryBuilder = QueryBuilders.boolQuery();
		tagBoolQueryBuilder.should(QueryBuilders.termQuery("tagName", tag));
		boolQueryBuilder.should(tagBoolQueryBuilder);
		
		BoolQueryBuilder websiteIDBoolQueryBuilder = QueryBuilders.boolQuery();
		for (Integer websiteID : websiteIDList) {
			websiteIDBoolQueryBuilder.should(QueryBuilders.termQuery("website_id", websiteID));
		}
		boolQueryBuilder.should(websiteIDBoolQueryBuilder);
		boolQueryBuilder2.must(boolQueryBuilder);
		
		if (startTime != null && endTime != null) {
			BoolQueryBuilder timeBoolQueryBuilder = QueryBuilders.boolQuery();
			timeBoolQueryBuilder.must(QueryBuilders.rangeQuery(SysParameters.ORDERBYTIMENAME).from(startTime)
					.to(endTime));
			boolQueryBuilder2.must(timeBoolQueryBuilder);
		}
		countRequestBuilder.setQuery(boolQueryBuilder2);
		CountResponse countResponse = countRequestBuilder.execute().actionGet();
		return countResponse.getCount();
	}

	/**
	 * 搜索属于某个标签而且来源于指定的多个信息源的新闻
	 * @param tag
	 * @param websiteIDList
	 * @param startTime
	 * @param endTime
	 * @param sizeFrom
	 * @param size
	 * @return
	 */
	public List<SearchNews>searchByOneTagANDWebsiteIDs(String tag, List<Integer> websiteIDList, Long startTime,
			Long endTime, int sizeFrom, int size) {
		List<SearchNews>list = new ArrayList<SearchNews>();
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		BoolQueryBuilder tagBoolQueryBuilder = QueryBuilders.boolQuery();
		tagBoolQueryBuilder.must(QueryBuilders.termQuery("tagName", tag));
		boolQueryBuilder.must(tagBoolQueryBuilder);
		
		BoolQueryBuilder websiteIDBoolQueryBuilder = QueryBuilders.boolQuery();

		for (Integer websiteID : websiteIDList) {
			websiteIDBoolQueryBuilder.should(QueryBuilders.termQuery("website_id", websiteID));
		}
		boolQueryBuilder.must(websiteIDBoolQueryBuilder);
		
		if (startTime != null && endTime != null) {
			BoolQueryBuilder timeBoolQueryBuilder = QueryBuilders.boolQuery();
			timeBoolQueryBuilder.must(QueryBuilders.rangeQuery(SysParameters.ORDERBYTIMENAME).from(startTime)
					.to(endTime));
			boolQueryBuilder.must(timeBoolQueryBuilder);
		}
		list = search(searchRequestBuilder, boolQueryBuilder, sizeFrom, size,0f);
		return list;
	}

	/**
	 * 计算属于某个标签而且来源于指定多个信息源的新闻数量
	 * @param tag
	 * @param websiteIDList
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Long countByOneTagANDWebsiteIDs(String tag, List<Integer> websiteIDList, Long startTime, Long endTime) {
		CountRequestBuilder countRequestBuilder = SearchESClientFactory.getClient().prepareCount(
				SysParameters.INDEXNAME);

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		BoolQueryBuilder tagBoolQueryBuilder = QueryBuilders.boolQuery();
		tagBoolQueryBuilder.must(QueryBuilders.termQuery("tagName", tag));
		boolQueryBuilder.must(tagBoolQueryBuilder);

		BoolQueryBuilder websiteIDBoolQueryBuilder = QueryBuilders.boolQuery();

		for (Integer websiteID : websiteIDList) {
			websiteIDBoolQueryBuilder.should(QueryBuilders.termQuery("website_id", websiteID));
		}
		boolQueryBuilder.must(websiteIDBoolQueryBuilder);

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
	
	/**
	 * 搜索属于指定标签，而且不是从指定的多个信息源抓取的新闻
	 * @param tag
	 * @param websiteIDList
	 * @param startTime
	 * @param endTime
	 * @param sizeFrom
	 * @param size
	 * @return
	 */
	public List<SearchNews>searchByOneTagNOTWebsiteIDs(String tag, List<Integer> websiteIDList, Long startTime,
			Long endTime, int sizeFrom, int size){
		
		List<SearchNews>list = new ArrayList<SearchNews>();
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		BoolQueryBuilder tagBoolQueryBuilder = QueryBuilders.boolQuery();
		tagBoolQueryBuilder.must(QueryBuilders.termQuery("tagName", tag));
		boolQueryBuilder.must(tagBoolQueryBuilder);
		
		BoolQueryBuilder websiteIDBoolQueryBuilder = QueryBuilders.boolQuery();

		for (Integer websiteID : websiteIDList) {
			websiteIDBoolQueryBuilder.should(QueryBuilders.termQuery("website_id", websiteID));
		}
		boolQueryBuilder.mustNot(websiteIDBoolQueryBuilder);
		
		if (startTime != null && endTime != null) {
			BoolQueryBuilder timeBoolQueryBuilder = QueryBuilders.boolQuery();
			timeBoolQueryBuilder.must(QueryBuilders.rangeQuery(SysParameters.ORDERBYTIMENAME).from(startTime)
					.to(endTime));
			boolQueryBuilder.must(timeBoolQueryBuilder);
		}
		list = search(searchRequestBuilder, boolQueryBuilder, sizeFrom, size,0f);
		return list;
	}
	
	/**
	 * 计算属于指定标签，而且不是从指定的多个信息源抓取的新闻数量
	 * @param tag
	 * @param websiteIDList
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Long countByOneTagNOTWebsiteIDs(String tag, List<Integer> websiteIDList, Long startTime, Long endTime) {
		CountRequestBuilder countRequestBuilder = SearchESClientFactory.getClient().prepareCount(
				SysParameters.INDEXNAME);

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		BoolQueryBuilder tagBoolQueryBuilder = QueryBuilders.boolQuery();
		tagBoolQueryBuilder.must(QueryBuilders.termQuery("tagName", tag));
		boolQueryBuilder.must(tagBoolQueryBuilder);
		
		BoolQueryBuilder websiteIDBoolQueryBuilder = QueryBuilders.boolQuery();

		for (Integer websiteID : websiteIDList) {
			websiteIDBoolQueryBuilder.should(QueryBuilders.termQuery("website_id", websiteID));
		}
		boolQueryBuilder.mustNot(websiteIDBoolQueryBuilder);

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
	
	

	private List<SearchNews>search(SearchRequestBuilder searchRequestBuilder, QueryBuilder queryBuilder, int sizeFrom,
			int size,float minScore) {
		List<SearchNews>list = new ArrayList<SearchNews>();

		searchRequestBuilder.setQuery(queryBuilder);
		searchRequestBuilder.setFrom(sizeFrom).setSize(size);
		searchRequestBuilder.addSort("crawlDate", SortOrder.DESC);
		
		if(minScore!=0f){
			searchRequestBuilder.setMinScore(minScore);
		}
		
		/*searchRequestBuilder.addHighlightedField("content").setHighlighterPreTags("<span style=\"color:red\">")
				.setHighlighterPostTags("</span>");
		
		searchRequestBuilder.addHighlightedField("title").setHighlighterPreTags("<span style=\"color:red\">")
				.setHighlighterPostTags("</span>");*/

		// 执行搜索,返回搜索响应信息
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();

		for (SearchHit hit : hits) {
			
			String json = hit.getSourceAsString();
			ObjectMapper mapper = new ObjectMapper();
			try {
				SearchNews news = mapper.readValue(json, SearchNews.class);
				list.add(news);
			} catch (JsonParseException e) {
				logger.error(e.getMessage());
			} catch (JsonMappingException e) {
				logger.error(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return list;
	}

	
	private BoolQueryBuilder composeQueryBuilder(List<String> mustCondition, List<String> mustNotCondition,
			List<String> shouldCondition,String fieldName) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (mustCondition != null) {
			BoolQueryBuilder mustTagsBuilder = QueryBuilders.boolQuery();
			for (String c : mustCondition) {
				mustTagsBuilder.must(QueryBuilders.termQuery(fieldName, c));
			}
			boolQueryBuilder.must(mustTagsBuilder);
		}
		
		if (shouldCondition != null) {
			BoolQueryBuilder shouldTagsBuilder = QueryBuilders.boolQuery();
			for (String c : shouldCondition) {
				shouldTagsBuilder.should(QueryBuilders.termQuery(fieldName, c));
			}
			boolQueryBuilder.must(shouldTagsBuilder);
		}
		
		if (mustNotCondition != null) {
			BoolQueryBuilder mustNotTagsBuilder = QueryBuilders.boolQuery();
			for (String c : mustNotCondition) {
				mustNotTagsBuilder.mustNot(QueryBuilders.termQuery(fieldName, c));
			}
			boolQueryBuilder.must(mustNotTagsBuilder);
		}
		return boolQueryBuilder;
	}
	
	private float computeMinScore(float initialMinScore,int arg){
		return initialMinScore/arg;
	}
	
}
