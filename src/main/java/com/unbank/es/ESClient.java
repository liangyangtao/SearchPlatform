package com.unbank.es;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.mlt.MoreLikeThisRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unbank.common.constants.SysParameters;
import com.unbank.entity.News;

public class ESClient {
	private static Client client;
	private static final String INDEXNAME =  SysParameters.INDEXNAME;

	public ESClient() {
		client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
	}

	
	public void createIndex(News news) {
		/**
		 * 使用文档ID作为索引中的ID
		 */
		client.prepareIndex(INDEXNAME, "news", String.valueOf(news.getCrawl_id())).setSource(news.toJsonString())
				.execute().actionGet();
	}
	
	/**
	 * 按照词语搜索
	 * 
	 * @param word
	 * @param sizeFrom
	 * @param size
	 * @return
	 */
	public List<News> SearchByWord(String word, int sizeFrom, int size) {
		List<News> list = new ArrayList<News>();

		// 依据查询索引库名称创建查询索引
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(INDEXNAME);
		/**
		 * 查询类型 DFS_QUERY_THEN_FETCH 精确查询 SCA 扫描查询,无序
		 */
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		// 设置查询条件
		searchRequestBuilder.setQuery(QueryBuilders.termQuery("content", word));
		
		/**
		 * 设置高亮
		 */
		searchRequestBuilder.addHighlightedField("content").setHighlighterPreTags("<span style=\"color:red\">")
				.setHighlighterPostTags("</span>");
		
		// Field field=new Field("");
		
		// 分页应用
		searchRequestBuilder.setFrom(sizeFrom).setSize(size);
		// 设置是否按查询匹配度排序
		searchRequestBuilder.setExplain(true);
		// 执行搜索,返回搜索响应信息
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		
		for (SearchHit hit : hits) {
			String json = hit.getSourceAsString();
			ObjectMapper mapper = new ObjectMapper();
			try {
				News news = mapper.readValue(json, News.class);
				list.add(news);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 按照多个ID搜索
	 * 
	 * @param IdList
	 * @param sizeFrom
	 * @param size
	 * @return
	 */
	public List<News> SearchByCrawlIds(List<Integer> IdList, int sizeFrom, int size) {
		List<News> list = new ArrayList<News>();

		// 依据查询索引库名称创建查询索引
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(INDEXNAME);
		/**
		 * 查询类型 DFS_QUERY_THEN_FETCH 精确查询 SCA 扫描查询,无序
		 */
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		// 设置查询条件
		// searchRequestBuilder.setQuery(QueryBuilders.inQuery("crawl_id",
		// IdList));
		searchRequestBuilder.setQuery(QueryBuilders.termsQuery("crawl_id", IdList));
		// 分页应用
		searchRequestBuilder.setFrom(sizeFrom).setSize(size);
		// 设置是否按查询匹配度排序
		searchRequestBuilder.setExplain(true);
		// 执行搜索,返回搜索响应信息
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();

		for (SearchHit hit : hits) {
			String json = hit.getSourceAsString();
			ObjectMapper mapper = new ObjectMapper();
			try {
				News news = mapper.readValue(json, News.class);
				list.add(news);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 按照多个ID搜索，结果按时间倒序排列
	 * 
	 * @param IdList
	 * @param sizeFrom
	 * @param size
	 * @return
	 */
	public List<News> SearchByCrawlIdsOrderByCrawlTime(List<Integer> IdList, int sizeFrom, int size) {
		List<News> list = new ArrayList<News>();

		// 依据查询索引库名称创建查询索引
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(INDEXNAME);
		/**
		 * 查询类型 DFS_QUERY_THEN_FETCH 精确查询 SCA 扫描查询,无序
		 */
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		// 设置查询条件
//		searchRequestBuilder.setQuery(QueryBuilders.inQuery("crawl_id", IdList));
		searchRequestBuilder.setQuery(QueryBuilders.termQuery("crawl_id", IdList));
		// 分页应用
		 searchRequestBuilder.setFrom(sizeFrom).setSize(size);
		// 设置是否按查询匹配度排序
		searchRequestBuilder.setExplain(true);
		
		// 按照时间降序
		searchRequestBuilder.addSort("crawlDate", SortOrder.DESC);
		
		// 执行搜索,返回搜索响应信息
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();

		for (SearchHit hit : hits) {
			String json = hit.getSourceAsString();
			ObjectMapper mapper = new ObjectMapper();
			try {
				News news = mapper.readValue(json, News.class);
				list.add(news);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public List<News> SearchMoreLikeByNews() {
		List<News> list = new ArrayList<News>();
		MoreLikeThisRequestBuilder mlt = new MoreLikeThisRequestBuilder(client, INDEXNAME, "news",
				"AUzFR9G2Dw538fjaW703");
		mlt.setField("content");// 匹配的字段
		SearchResponse response = client.moreLikeThis(mlt.request()).actionGet();

		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();

		for (SearchHit hit : hits) {
			String json = hit.getSourceAsString();
			ObjectMapper mapper = new ObjectMapper();
			try {
				News news = mapper.readValue(json, News.class);
				list.add(news);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 按照单个ID查询
	 * 
	 * @param crawl_id
	 * @return
	 */
	public News SearchByCrawlId(int crawl_id) {
		// 依据查询索引库名称创建查询索引
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(INDEXNAME);
		/**
		 * 查询类型 DFS_QUERY_THEN_FETCH 精确查询 SCA 扫描查询,无序
		 */
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		// 设置查询条件
		searchRequestBuilder.setQuery(QueryBuilders.termQuery("crawl_id", crawl_id));

		// 设置是否按查询匹配度排序
		searchRequestBuilder.setExplain(true);
		// 执行搜索,返回搜索响应信息
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		String json = hits[0].getSourceAsString();
		ObjectMapper mapper = new ObjectMapper();

		try {
			News news = mapper.readValue(json, News.class);
			return news;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public List<News> searchBySript(String str) {
		List<News> list = new ArrayList<News>();

		// 依据查询索引库名称创建查询索引
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(INDEXNAME);
		/**
		 * 查询类型 DFS_QUERY_THEN_FETCH 精确查询 SCA 扫描查询,无序
		 */
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		searchRequestBuilder.setExplain(true);

		Map<String,Object> map=new HashMap<String,Object>();
		map.put("param", "银行");
		
		searchRequestBuilder.setTemplateName("searchByTemp").setTemplateParams(map);

		// 按照时间降序
		searchRequestBuilder.addSort("crawlDate", SortOrder.DESC);

		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		
		for (SearchHit hit : hits) {
			String json = hit.getSourceAsString();
			ObjectMapper mapper = new ObjectMapper();
			try {
				News news = mapper.readValue(json, News.class);
				list.add(news);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		return list;
	}

}
