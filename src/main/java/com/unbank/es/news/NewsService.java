package com.unbank.es.news;

import static com.unbank.common.constants.CommonConstants.ERROR;
import static com.unbank.common.constants.CommonConstants.SUCCESS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.unbank.common.constants.CommonConstants;
import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.CommonUtils;
import com.unbank.common.utils.ESUtils;
import com.unbank.common.utils.GsonUtil;
import com.unbank.common.utils.SourcesUtils;
import com.unbank.entity.News;
import com.unbank.entity.NewsSearchCondition;
import com.unbank.entity.SearchData;
import com.unbank.entity.SearchNews;

@Service
public class NewsService {
	private static final Logger logger = LoggerFactory
			.getLogger(NewsService.class);

	/**
	 *  将News对象建立索引
	 * @param news News对象
	 * @return
	 */
	public boolean oper(News news){
		/**
		 * 使用文档ID作为索引中的ID
		 */
		IndexRequestBuilder indexRequestBuilder = ESUtils.getClient().prepareIndex(SysParameters.INDEXNAME, "news", String.valueOf(news.getCrawl_id()));
		indexRequestBuilder.setSource(news.toJsonString());
		logger.info("开始建立索引,crawl_id:"+news.getCrawl_id());
		try {
			 indexRequestBuilder.execute().actionGet();
		} catch (Exception e) {
			logger.error(CommonUtils.getExceptionMessage(e));
			return false;
		}
		return true;
	}
	
	/**
	 * 判断新闻是否重复，并添加索引信息
	 * 
	 * @param news News对象
	 * @return
	 */
	public boolean index(News news){
		try {
			Client client = ESUtils.getClient();
			
			// 判断索引库是否存在
			boolean flag = ESUtils.queryIndexExists(client, SysParameters.INDEXNAME);
//			 索引库存在--查询是否有重复新闻(如果不做判断，在没有索引库的情况下，会报找到不索引库的异常信息)
			if(flag){
				BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
				queryBuilder.should(QueryBuilders.matchQuery("title", news.getTitle()).minimumShouldMatch("80%"))
							.should(QueryBuilders.matchQuery("content", news.getContent()).minimumShouldMatch("80%"));

				CountResponse countResponse = ESUtils.getClient().prepareCount(SysParameters.INDEXNAME)
						.setTypes("news").setQuery(queryBuilder).get();

				long count = countResponse.getCount();

				// 如果没有相似的新闻则保存
				if (count > 0) {
					news.setIsExists(1);
				}
			}
			
			logger.info("添加新闻索引信息,index,begin,crawl_id:" + news.getCrawl_id());
			
			// 执行保存索引
			IndexRequestBuilder indexRequestBuilder = client.prepareIndex(SysParameters.INDEXNAME, SysParameters.DATATYPE, String.valueOf(news.getCrawl_id()));
			indexRequestBuilder.setSource(news.toJsonString());
			indexRequestBuilder.execute().actionGet();
			
			// 刷新索引状态(否则新添加的索引不能查询)
			ESUtils.refreshIndex(client, SysParameters.INDEXNAME);
			
			logger.info("新闻库：添加新闻索引信息完成,index,end,crawl_id:" + news.getCrawl_id() + ",\t status:" + CommonConstants.SUCCESS 
					+ ",\t crawl_id:" + news.getCrawl_id());
			
		} catch (Exception e) {
			logger.error("新闻库：添加新闻索引信息失败,index,end,crawl_id:" + news.getCrawl_id() + ",\t status:" + CommonConstants.ERROR);
			logger.error(CommonUtils.getExceptionMessage(e));
			return false;
		}
		return true;
	}

	public String bulkIndex(List<News> newsList, Map<String, String> map) {
		try {
			// 参数判断
	        if (CommonUtils.isEmpty(newsList)) {
	            logger.error("添加新闻索引信息,------数据不能为空");
	            CommonUtils.putMap(map, ERROR, "数据不能为空", null);
	            return GsonUtil.getJsonStringFromObject(map);
	        }
			
			logger.info("批量添加新闻,bulkIndex,begin");
			Client client = ESUtils.getClient();
			BulkRequestBuilder bluk = client.prepareBulk();
			
			// 标记索引库是否存在
			boolean flag = false;
//			int i = 0;
			// 批量添加
			for (News news : newsList) {
				if(!flag){
					flag = ESUtils.queryIndexExists(client, SysParameters.INDEXNAME);
				}
				
				// 索引库存在--查询是否有重复新闻(如果不做判断，在没有索引库的情况下，会报找到不索引库的异常信息)
				if (!flag) {
					// 执行保存索引
					IndexRequestBuilder indexRequestBuilder = client.prepareIndex(SysParameters.INDEXNAME, SysParameters.DATATYPE, String.valueOf(news.getCrawl_id()));
					indexRequestBuilder.setSource(news.toJsonString());
					indexRequestBuilder.execute().actionGet();
				} else {
					BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
					queryBuilder.must(QueryBuilders.matchQuery("title", news.getTitle()).minimumShouldMatch("100%"))
								.must(QueryBuilders.matchQuery("content", news.getContent()).minimumShouldMatch("100%"));

					CountResponse countResponse = client.prepareCount(SysParameters.INDEXNAME)
							.setTypes("news").setQuery(queryBuilder).get();

					long count = countResponse.getCount();

					// 不同则保存
					if (count == 0) {
						bluk.add(client.prepareIndex(SysParameters.INDEXNAME, "news", String.valueOf(news.getCrawl_id()))
								.setSource(news.toJsonString()));
//						i++;
					}
				}
			}

			if(bluk.numberOfActions() > 0){
				// 执行保存
				bluk.get();
			}
			
			logger.info("新闻库：批量添加新闻完成,bulkIndex,end");
			
			CommonUtils.putMap(map, SUCCESS, "批量添加新闻成功", null);
			return GsonUtil.getJsonStringFromObject(map);
		} catch (Exception e) {
			logger.error("新闻库：批量添加新闻失败,bulkIndex,end");
			logger.error(CommonUtils.getExceptionMessage(e));
			CommonUtils.putMap(map, ERROR, "新闻库：批量添加新闻失败", null);
            return GsonUtil.getJsonStringFromObject(map);
		}
	}

	/**
	 * 批量删除索引
	 * @param ids
	 * @return
	 */
	public String delete(List<String> ids) {
		Map<String, String> map = new HashMap<String, String>();
        Client client = ESUtils.getClient();
        try {
            client.prepareDeleteByQuery(SysParameters.INDEXNAME)
            		.setTypes(SysParameters.DATATYPE)
            		.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("crawl_id", ids)))
            		.get();
            
            CommonUtils.putMap(map, SUCCESS, "新闻库：批量删除索引成功", null);
            return GsonUtil.getJsonStringFromObject(map);

        } catch (Exception e) {
            logger.error(CommonUtils.getExceptionMessage(e));
            CommonUtils.putMap(map, ERROR, "新闻库：批量删除索引失败", null);
            return GsonUtil.getJsonStringFromObject(map);
        }
	}

	
	/**
	 * 根据新闻信息搜索相似新闻
	 * @param newsInfo
	 * @param from
	 * @param pageSize
	 * @param termFreq 
	 * @param docFreq
	 * @return
	 */
	public SearchData searchMoreLikeByNewsInfo(News newsInfo, int from,
			int pageSize, int termFreq, int docFreq) {
		
		long time1=System.currentTimeMillis();
		// 新闻内容
		String content = newsInfo.getContent();
		
		// moreLikeQuery
		MoreLikeThisQueryBuilder moreLikeThisQueryBuilder = QueryBuilders
				// 匹配的字段
				.moreLikeThisQuery("content")
				.likeText(content)
				// 匹配文档中的最低词频，低于此频率的词条将被忽略，默认值为：2
				.minTermFreq(termFreq)
				// 包含词条的文档最小数目，低于此数目时，该词条将被忽视，默认值为：5，意味着一个词条至少应该出现在5个文档中，才不会被忽略
				.minDocFreq(docFreq);
		
		SearchHits searchHits = null;
		try {
			SearchResponse response = ESUtils.getClient().prepareSearch(SysParameters.INDEXNAME)
					.setTypes(SysParameters.DATATYPE)
					.setQuery(moreLikeThisQueryBuilder)
					.setFrom(from)
					.setSize(pageSize)
					.get();
			
			searchHits = response.getHits();
			
		} catch (Exception e1) {
			logger.error(CommonUtils.getExceptionMessage(e1));
			return null;
		}
		
		SearchHit[] hits = searchHits.getHits();
		long time2=System.currentTimeMillis();
		logger.info("doucment more like search time:"+(time2-time1));
		
		List<SearchNews> documentList = new ArrayList<SearchNews>();
		
		for (SearchHit hit : hits) {
			// 封装文档信息
			SearchNews tempDoc = SourcesUtils.getNewsInfo(hit.getSource(), false);
			documentList.add(tempDoc);
		}
		
		SearchData sdd = new SearchData();
		
		sdd.setCount(searchHits.getTotalHits());
		sdd.setData(documentList);
		
		return sdd;
	}

	public SearchData searchNews(NewsSearchCondition searchInfo, Long startTime,
			Long endTime, int from, int pageSize, String orderByColumn, 
			boolean fullContent, boolean highlight) {
		
		Client client = ESUtils.getClient();
		
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		
		boolQuery.must(searchInfo.getQueryBuilder(false));
		
		if(startTime!=null&&endTime!=null){
			boolQuery.must(QueryBuilders.rangeQuery("newsDate").from(startTime).to(endTime));
		}
		
		// 排序方式 默认ES搜索排序
        List<FieldSortBuilder> sorts = ESUtils.getFieldSortBuilders(orderByColumn, "DESC");
        // 记录当前起始数据;
        int reFrom = 0;
        if(orderByColumn.equals("SCORE")){
        	// 记录当前起始数据
            reFrom = from % 100;
            if(from / 100 == 0){
            	from = 0;
            }else{
            	from = (from / 100) * 100;
            }
        }
        
        SearchRequestBuilder requestBuilder = ESUtils.getSearchRequestBuilder(client, SysParameters.INDEXNAME, SysParameters.DATATYPE, sorts, from, orderByColumn.equals("SCORE") ? 100 : pageSize);
        requestBuilder.setQuery(boolQuery);
        
        if(highlight){
        	requestBuilder.addHighlightedField("title");
        }
        
        SearchHits searchHits = requestBuilder.get().getHits();
        
		SearchData sd=new SearchData();
		String tempStr = "";
		List<SearchNews> list = new ArrayList<SearchNews>();
		List<SearchNews> result = new ArrayList<SearchNews>();
		for (SearchHit hit : searchHits) {
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
		
		if (orderByColumn.equals("SCORE")) {
			Collections.sort(list, new Comparator<SearchNews>() {
				public int compare(SearchNews arg0, SearchNews arg1) {
					return arg0.getNewsDate().compareTo(arg1.getNewsDate());
				}
			});
			int index = 0;
			for (int i = reFrom; i < list.size(); i++) {
				result.add(list.get(i));
				index ++;
				if(index == pageSize) break;
			}
		} else {
			result.addAll(list);
		}
		
		
		sd.setData(result);
		sd.setCount(searchHits.getTotalHits());
		sd.setCrawlIds(tempStr);
		return sd;
	}
	
}
