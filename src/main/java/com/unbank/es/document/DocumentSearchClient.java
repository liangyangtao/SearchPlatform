package com.unbank.es.document;

import static com.unbank.common.constants.CommonConstants.ERROR;
import static com.unbank.common.constants.CommonConstants.SUCCESS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.action.count.CountRequestBuilder;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unbank.common.constants.CommonConstants;
import com.unbank.common.constants.SysParameters;
import com.unbank.common.entity.ResponseParam;
import com.unbank.common.utils.CommonUtils;
import com.unbank.common.utils.ESUtils;
import com.unbank.common.utils.GsonUtil;
import com.unbank.common.utils.SourcesUtils;
import com.unbank.es.search.SearchESClientFactory;

public class DocumentSearchClient {

	private static final Logger logger = LoggerFactory.getLogger(DocumentSearchClient.class);
	private static final String INDEXNAME = SysParameters.DOCUMENTINDEX;
	private static final String TYPE=SysParameters.DOCUMENTTYPE;

	/**
	 * 通过文档id和文档所属项目唯一定位一篇文档
	 * 
	 * @param articleId
	 *            文档ID
	 * @param articeProject
	 *            文档所属的项目
	 * @return
	 */
	public Document getDocumentByIds(int articleId, String articeProject) {

		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		boolQueryBuilder.must(QueryBuilders.termQuery("articleId", articleId));
		boolQueryBuilder.must(QueryBuilders.termQuery("articleProject", articeProject));

		searchRequestBuilder.setQuery(boolQueryBuilder);
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		if (CommonUtils.isEmpty(hits)) {
			return null;
		}
		SearchHit hit = hits[0];

		Document document = SourcesUtils.getDocumentInfo(hit.getSource(), true);
		
		return document;
	}
	
	public Document getDocumentById(String id){
		GetResponse response = SearchESClientFactory.getClient()
				.prepareGet(INDEXNAME, TYPE, id)
				.get();
		
		if(response.isSourceEmpty()){
			return null;
		}
		
		Document doc = SourcesUtils.getDocumentInfo(response.getSource(), true);
		
		return doc;
	}
	
	public SearchDocumentData search(DocumentSearchCondition condition, int from, int pageSize, String orderByField,
			String order) {
		List<Document> documentList = new ArrayList<Document>();
		SearchDocumentData sdd = new SearchDocumentData();

		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		
		searchRequestBuilder.setFrom(from).setSize(pageSize);
		if (orderByField!=null&&!"SCORE".equals(orderByField)) {
			if("DESC".equals(order)){
				searchRequestBuilder.addSort(orderByField, SortOrder.DESC);
			}else if("ASC".equals(order)){
				searchRequestBuilder.addSort(orderByField, SortOrder.ASC);
			}
		}

		searchRequestBuilder.setQuery(condition.getQueryBuilder());
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		
		if(CommonUtils.isEmpty(hits)){
			return null;
		}
		
		for (SearchHit hit : hits) {
			// 封装文档信息
			Document tempDoc = SourcesUtils.getDocumentInfo(hit.getSource(), true);
			documentList.add(tempDoc);
		}
		sdd.setData(documentList);
		sdd.setCount(searchHits.getTotalHits());
		return sdd;
	}

	public SearchDocumentData search(List<DocumentSearchCondition> conditions, DocumentSearchCondition filterCondition, int from, int pageSize, String orderByField,
			String order) {
		List<Document> documentList = new ArrayList<Document>();
		SearchDocumentData sdd = new SearchDocumentData();

		SearchRequestBuilder searchRequestBuilder = ESUtils.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		
		searchRequestBuilder.setFrom(from).setSize(pageSize);

		if (orderByField!=null&&!"SCORE".equals(orderByField)) {
			if("DESC".equals(order)){
				searchRequestBuilder.addSort(orderByField, SortOrder.DESC);
			}else if("ASC".equals(order)){
				searchRequestBuilder.addSort(orderByField, SortOrder.ASC);
			}
		}

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		for(DocumentSearchCondition condition :conditions){
			boolQueryBuilder.should(condition.getQueryBuilder());
		}
		
		searchRequestBuilder.setQuery(boolQueryBuilder);
		
		if(filterCondition!=null){
			searchRequestBuilder.setPostFilter(FilterBuilders.queryFilter(filterCondition.getQueryBuilder()));
		}
		
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();

		if(CommonUtils.isEmpty(hits)){
			return null;
		}
		
		for (SearchHit hit : hits) {
			// 封装文档信息
			Document tempDoc = SourcesUtils.getDocumentInfo(hit.getSource(), true);
			documentList.add(tempDoc);
		}
		sdd.setData(documentList);
		sdd.setCount(searchHits.getTotalHits());
		return sdd;
	}
	
	public SearchDocumentData searchByString(String queryString,HashMap<String ,Float> columnBoost,DocumentSearchCondition filterCondition, int from, int pageSize){
		SearchDocumentData sdd = new SearchDocumentData();
		List<Document> documentList = new ArrayList<Document>();
		
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
			builder.field("articleName", 8.0f);
			builder.field("articleLabel", 2.0f);
			builder.field("articleKeyWord", 2.0f);
			builder.field("articleContent", 1.0f);
		}
		
		if(filterCondition!=null){
			searchRequestBuilder.setPostFilter(FilterBuilders.queryFilter(filterCondition.getQueryBuilder()));
		}
		searchRequestBuilder.setQuery(builder);
		
		searchRequestBuilder.setMinScore(SysParameters.STRINGQUERYMINSCORE);
		
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();

		if(CommonUtils.isEmpty(hits)){
			return null;
		}
		
		for (SearchHit hit : hits) {
			// 封装文档信息
			Document tempDoc = SourcesUtils.getDocumentInfo(hit.getSource(), true);
			documentList.add(tempDoc);
		}
		sdd.setData(documentList);
		sdd.setCount(searchHits.getTotalHits());
		return sdd;
	}
	
	public SearchDocumentData searchMoreLike(String id,int from, int pageSize){
		SearchDocumentData sdd = new SearchDocumentData();
		List<Document> documentList = new ArrayList<Document>();
		
		long time1=System.currentTimeMillis();
		
		// 获取期刊ID
		GetResponse getResponse = SearchESClientFactory.getClient().prepareGet(SysParameters.DOCUMENTINDEX, SysParameters.DOCUMENTTYPE, id).get();
		// 数据源
		Map<String, Object> source = getResponse.getSource();
		// 期刊ID
		int journalId = SourcesUtils.getIntValue(source.get("articleJournalId"));
		// 文档内容
		String articleContent = SourcesUtils.getStringValue(source.get("articleContent"));
		// 文档标题
		String articleName = SourcesUtils.getStringValue(source.get("articleName"));
		// 关键字
		String keyWord = SourcesUtils.getStringValue(source.get("articleKeyWord"));
		
		List<Integer> journalIds = new ArrayList<Integer>();
		journalIds.add(journalId);
		List<Integer> ids = new ArrayList<Integer>();
		String [] ags = new String []{"articleContent", "articleName", "articleKeyWord"};
		String [] val = new String []{articleContent, articleName, keyWord};
		for (int i = 0; i < pageSize; i++) {
			BoolQueryBuilder qurey = QueryBuilders.boolQuery();
			
			for (int j = 0; j < ags.length; j++) {
				// moreLikeQuery
				MoreLikeThisQueryBuilder moreLikeThisQueryBuilder = QueryBuilders
						// 匹配的字段
						.moreLikeThisQuery(ags[j])
						.likeText(val[j])
						// 匹配文档中的最低词频，低于此频率的词条将被忽略，默认值为：2
						.minTermFreq(1)
						// 包含词条的文档最小数目，低于此数目时，该词条将被忽视，默认值为：5，意味着一个词条至少应该出现在5个文档中，才不会被忽略
						.minDocFreq(1);
				qurey.must(moreLikeThisQueryBuilder);
			}
			
			qurey.mustNot(QueryBuilders.termsQuery("articleId", ids));
			qurey.mustNot(QueryBuilders.termsQuery("articleJournalId", journalIds));
			
//			// 过滤器--过滤本期刊下的所有文档
//			NotFilterBuilder notFilter = FilterBuilders.notFilter(FilterBuilders.queryFilter(QueryBuilders.termsQuery("articleJournalId", journalIds)));
//			// 加载moreLike 和 notFilter过滤器
//			FilteredQueryBuilder filteredQueryBuilder = QueryBuilders.filteredQuery(qurey, notFilter);
			
			SearchHits searchHits = null;
			try {
				SearchResponse response = SearchESClientFactory.getClient().prepareSearch(SysParameters.DOCUMENTINDEX)
						.setTypes(SysParameters.DOCUMENTTYPE)
						.setQuery(qurey)
						.setFrom(from)
						.setSize(1)
						.get();
				
				searchHits = response.getHits();
				
			} catch (Exception e1) {
				logger.error(CommonUtils.getExceptionMessage(e1));
				return null;
			}
			
			SearchHit[] hits = searchHits.getHits();
			for (SearchHit hit : hits) {
				// 封装文档信息
				Document tempDoc = SourcesUtils.getDocumentInfo(hit.getSource(), false);
				documentList.add(tempDoc);
				if(tempDoc.getArticleJournalId() != 0){
					journalIds.add(tempDoc.getArticleJournalId());
				} else {
					ids.add(tempDoc.getArticleId());
				}
			}
			
		}
		long time2=System.currentTimeMillis();
		logger.info("doucment more like search time:"+(time2-time1));
		
		sdd.setCount(Long.parseLong(String.valueOf(documentList.size())));
		sdd.setData(documentList);
		
		return sdd;
	}


	public String searchByJournalIds(List<DocumentSearchCondition> conditions,
			int from, int pageSize, String orderByField, String order, boolean isFullContent) {

		ResponseParam responseParam = new ResponseParam();

		Client client = ESUtils.getClient();
		try {
			// 获取排序方式
			List<FieldSortBuilder> sorts = ESUtils.getFieldSortBuilders(orderByField, order);
			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
			
			for (DocumentSearchCondition documentSearchCondition : conditions) {
				queryBuilder.should(documentSearchCondition.getQueryBuilder());
			}
			

			if(pageSize == 0){
				pageSize = CommonConstants.PAGE_SIZE;
			}
			
			SearchHits hits = ESUtils.search(client, INDEXNAME, TYPE, queryBuilder, sorts, from, pageSize);

			// 是否搜索到数据
			if (CommonUtils.isEmpty(hits) || hits.getHits().length == 0) {
                responseParam.setStatus(CommonConstants.SUCCESS);
                responseParam.setMsg("查无结果");
                return GsonUtil.getJsonStringFromObject(responseParam);
            }

			List<Document> docs = new ArrayList<Document>();

			for (SearchHit temp : hits.getHits()) {
				// 封装文档信息
				Document tempDoc = SourcesUtils.getDocumentInfo(temp.getSource(), false);
				
                docs.add(tempDoc);
            }

			// 封装返回值
			responseParam.setStatus(SUCCESS);
			responseParam.setMsg("成功");
			responseParam.setJsonData(GsonUtil.getJsonStringFromObject(docs));
			// 获取总数
			responseParam.setCount(hits.getTotalHits());

			return GsonUtil.getJsonStringFromObject(responseParam);
		} catch (IndexMissingException e) {
			logger.error(CommonUtils.getExceptionMessage(e));
			responseParam.setStatus(ERROR);
			responseParam.setMsg("搜索失败");
			return GsonUtil.getJsonStringFromObject(responseParam);
		}
	}


	@SuppressWarnings("unchecked")
	public String searchByGroup(List<DocumentSearchCondition> conditions, int from, int pageSize, 
			String orderByField, String order, boolean isFullContent) {
		ResponseParam responseParam = new ResponseParam();
		Client client = ESUtils.getClient();
		try {
			// 获取排序方式
			List<FieldSortBuilder> sorts = ESUtils.getFieldSortBuilders(orderByField, order);
			// 封装queryBuilder
			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
			
			for (DocumentSearchCondition searchData : conditions) {
				queryBuilder.should(searchData.getQueryBuilder());
			}
			
			if(pageSize == 0){
				pageSize = CommonConstants.PAGE_SIZE;
			}
			
			SearchRequestBuilder searchBuilder = client.prepareSearch(INDEXNAME)
					.setTypes(TYPE)
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
					.setFrom(from)
					.setSize(pageSize)
					.setQuery(queryBuilder);
			
			if(CommonUtils.isNotEmpty(sorts)){
				for (FieldSortBuilder fieldSortBuilder : sorts) {
					searchBuilder.addSort(fieldSortBuilder);
				}
			}
			
			// 设置高亮，高亮默认返回字符长度是100
			searchBuilder.addHighlightedField("articleName").addHighlightedField("articleSkip");
			
			SearchHits hits = searchBuilder.get().getHits();
			
			// 是否搜索到数据
			if (CommonUtils.isEmpty(hits) || hits.getHits().length == 0) {
				responseParam.setStatus(CommonConstants.SUCCESS);
				responseParam.setMsg("查无结果");
				return GsonUtil.getJsonStringFromObject(responseParam);
			}
			
			// 结果集
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			// 查询出的原始数据
			SearchHit[] searchHits = hits.getHits();
			for (SearchHit hitInfo : searchHits) {
				// 封装文档信息
				Document docInfo = SourcesUtils.getDocumentInfo(hitInfo.getSource(), false);
				
				// 高亮
				Map<String, HighlightField> highlightMap = hitInfo.getHighlightFields();
				if(CommonUtils.isNotEmpty(highlightMap)){
					if(CommonUtils.isNotEmpty(highlightMap.get("articleName"))){
						docInfo.setArticleName(highlightMap.get("articleName").fragments()[0].string());
					}
					if(CommonUtils.isNotEmpty(highlightMap.get("articleSkip"))){
						docInfo.setArticleSkip(highlightMap.get("articleSkip").fragments()[0].string());
					}
				}
				// 期刊ID
				String journalId = String.valueOf(docInfo.getArticleJournalId());
				// 期刊文档
				if (!"0".equals(journalId)) {
					// 记录是否包含的状态
					boolean flag = false;
					// 循环结果集，是否包含本期刊
					for (Map<String, Object> tempMap : resultList) {
						if (journalId.equals(tempMap.get("journalId").toString())) {
							List<Document> docs = (List<Document>)tempMap.get("docs");
							docs.add(docInfo);
							tempMap.put("docs", docs);
							// 修改记录状态
							flag = true;
						}
					}
					
					// 不包含，则添加
					if (!flag) {
						List<Document> tempList = new ArrayList<Document>();
						tempList.add(docInfo);
						Map<String, Object> tempMap = new HashMap<String, Object>();
						tempMap.put("docs", tempList);
						tempMap.put("journalId", journalId);
						
						resultList.add(tempMap);
					}
				}
				// 非期刊文档
				else {
					List<Document> tempList = new ArrayList<Document>();
					tempList.add(docInfo);
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("docs", tempList);
					tempMap.put("journalId", 0);
					
					resultList.add(tempMap);
				}
			}
			
			for (Map<String, Object> tempMap : resultList) {
				String journalId = tempMap.get("journalId").toString();
				if (!"0".equals(journalId)) {
					// 根据期刊ID查询期刊信息
					QueryBuilder queryJournalBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("journalId", journalId));
					SearchRequestBuilder searchRequestBuilder = client.prepareSearch(CommonConstants.INDEX_JOURNAL).setTypes(CommonConstants.TYPE_JOURNAL)
							 .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
							 .setQuery(queryJournalBuilder);
					
					SearchHits journalHits = null;
					try {
						journalHits = searchRequestBuilder.get().getHits();
					} catch (Exception e) {
						// 是否搜索到数据
						if (CommonUtils.isEmpty(journalHits)) {
							responseParam.setStatus(CommonConstants.SUCCESS);
							responseParam.setMsg("查无结果");
							return GsonUtil.getJsonStringFromObject(responseParam);
						}
					}
					
					if(journalHits.getTotalHits() == 0){
						tempMap.put("journalName", "");
					}else{
						// 获取查询结果
						SearchHit journalHit = journalHits.getHits()[0];
						Map<String, Object> journalSource = journalHit.getSource();
						// 期刊名称
						Object journalName = journalSource.get("name");
						tempMap.put("journalName", journalName == null ? "" : journalName.toString());
					}
					
					// 查询本期刊总共有多少期
					long journalTotal = ESUtils.queryCount(INDEXNAME, TYPE, "articleJournalId", journalId);
					tempMap.put("journalTotal", journalTotal);
					
					// 本期刊符合条件的总数
					Client tempClient = ESUtils.getClient();
					CountRequestBuilder countRequestBuilder = tempClient.prepareCount(INDEXNAME).setTypes(TYPE);
					//　封装查询总数的query
					BoolQueryBuilder countQueryQuery = QueryBuilders.boolQuery();
					countQueryQuery.must(queryBuilder);
					countQueryQuery.must(QueryBuilders.termQuery("articleJournalId", journalId));
					
					CountResponse countResponse = countRequestBuilder.setQuery(countQueryQuery).get();
					long searchTotal = countResponse.getCount();
					tempMap.put("searchCount", searchTotal);
				}
			}

			// 封装返回值
			responseParam.setStatus(SUCCESS);
			responseParam.setMsg("成功");
			// 封装分组期刊
			responseParam.setJsonData(GsonUtil.getJsonStringFromObject(resultList));
			// 获取总数
			responseParam.setCount(hits.getTotalHits());
			return GsonUtil.getJsonStringFromObject(responseParam);
		} catch (IndexMissingException e) {
			logger.error(CommonUtils.getExceptionMessage(e));
			responseParam.setStatus(ERROR);
			responseParam.setMsg("搜索失败");
			return GsonUtil.getJsonStringFromObject(responseParam);
		}
	}
	
	/**
	 * 查询出符合搜索条件，本期刊下的所有文档信息
	 * @param highlight 
	 * @param paramInfo
	 * @return
	 */
	public String searchMoreByJournal(DocumentSearchCondition searchData, int from, int pageSize, 
			String orderByField, String order, List<String> highlight) {
		ResponseParam responseParam = new ResponseParam();
		
		Client client = ESUtils.getClient();
		try {
			// 获取排序方式
			List<FieldSortBuilder> sorts = ESUtils.getFieldSortBuilders(orderByField, order);
			// 封装queryBuilder
			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
			queryBuilder.must(searchData.getQueryBuilder());
			
			if(pageSize == 0){
				pageSize = CommonConstants.PAGE_SIZE;
			}
			
			SearchRequestBuilder searchBuilder = client.prepareSearch(INDEXNAME)
					.setTypes(TYPE)
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
					.setFrom(from)
					.setSize(pageSize)
					.setQuery(queryBuilder);
			
			if(CommonUtils.isNotEmpty(sorts)){
				for (FieldSortBuilder fieldSortBuilder : sorts) {
					searchBuilder.addSort(fieldSortBuilder);
				}
			}
			
			// 设置高亮，高亮默认返回字符长度是100
			searchBuilder.addHighlightedField("articleName").addHighlightedField("articleSkip");
			
			SearchHits hits = searchBuilder.get().getHits();
					// ESUtils.search(client, INDEXNAME, TYPE, queryBuilder, sorts, from, pageSize);

			if (CommonUtils.isEmpty(hits) || hits.getHits().length == 0) {
				responseParam.setStatus(CommonConstants.SUCCESS);
				responseParam.setMsg("查无结果");
				return GsonUtil.getJsonStringFromObject(responseParam);
			}
			
			List<Document> result = new ArrayList<Document>();
			for (SearchHit hit : hits.getHits()) {
				// 封装文档信息
				Document doc = SourcesUtils.getDocumentInfo(hit.getSource(), false);
				
				// 高亮
				Map<String, HighlightField> highlightMap = hit.getHighlightFields();
				if(CommonUtils.isNotEmpty(highlightMap)){
					if(CommonUtils.isNotEmpty(highlightMap.get("articleName"))){
						doc.setArticleName(highlightMap.get("articleName").fragments()[0].string());
					}
					if(CommonUtils.isNotEmpty(highlightMap.get("articleSkip"))){
						doc.setArticleSkip(highlightMap.get("articleSkip").fragments()[0].string());
					}
				}
				result.add(doc);
			}
			
			// 根据期刊ID查询期刊信息
			String journalName = null;
			List<Integer> ids = searchData.getJournalIds();
			if(CommonUtils.isNotEmpty(ids)){
				QueryBuilder queryJournalBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("journalId", ids.get(0)));
				SearchRequestBuilder searchRequestBuilder = client.prepareSearch(CommonConstants.INDEX_JOURNAL).setTypes(CommonConstants.TYPE_JOURNAL)
						 .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
						 .setQuery(queryJournalBuilder);
				
				SearchHits journalHits = null;
				try {
					journalHits = searchRequestBuilder.get().getHits();
				} catch (Exception e) {
					// 是否搜索到数据
					if (CommonUtils.isEmpty(journalHits)) {
						responseParam.setStatus(CommonConstants.SUCCESS);
						responseParam.setMsg("查无结果");
						return GsonUtil.getJsonStringFromObject(responseParam);
					}
				}
				
				if(journalHits.getTotalHits() == 0){
					journalName = "";
				}else{
					// 获取查询结果
					SearchHit journalHit = journalHits.getHits()[0];
					Map<String, Object> journalSource = journalHit.getSource();
					// 期刊名称
					Object tempName = journalSource.get("name");
					journalName = tempName == null ? "" : tempName.toString();
				}
			}

			// 期刊名称
			responseParam.setOthers(journalName);
			responseParam.setJsonData(GsonUtil.getJsonStringFromObject(result));
			responseParam.setStatus(SUCCESS);
			responseParam.setMsg("成功");
			responseParam.setCount(hits.getTotalHits());
			return GsonUtil.getJsonStringFromObject(responseParam);
		} catch (IndexMissingException e) {
			logger.error(CommonUtils.getExceptionMessage(e));
			responseParam.setStatus(ERROR);
			responseParam.setMsg("搜索失败");
			return GsonUtil.getJsonStringFromObject(responseParam);
		}
	}
}
