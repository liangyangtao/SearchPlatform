package com.unbank.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.count.CountResponse;
//import org.elasticsearch.action.exists.ExistsResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import com.unbank.common.constants.ServerConfig;
import com.unbank.es.document.Document;

/**
 * ES工具类
 * 
 * @author LiuLei
 *
 */
public class ESUtils {

	private static TransportClient transportClient = null;
	
	static {
		transportClient = getClusterClient(ServerConfig.getClusterName(), true, 9300, ServerConfig.getServerUrl());
	}
	
	public static Client getClient(){
		if(transportClient==null){
			transportClient = getClusterClient(ServerConfig.getClusterName(), true, 9300, ServerConfig.getServerUrl());
		}
		return transportClient;
		
	}


	/**
	 * 初始化并连接elasticsearch集群，返回连接后的client
	 * @ clusterName 中心节点名称
	 * @ clientTransportSniff 是否自动发现新加入的节点
	 * @ port 节点端口
	 * @ hostname 集群节点所在服务器IP，支持多个
	 * @return 返回连接的集群的client
	 */
	public static TransportClient getClusterClient(String clusterName, boolean clientTransportSniff, int port, String... hostname){
		Settings settings = ImmutableSettings.settingsBuilder()
				.put("cluster.name", clusterName)
				.put("client.transport.sniff", clientTransportSniff)
				.build();

		/*
			client.transport.ignore_cluster_name(Set to true to ignore cluster name validation of connected nodes. (since 0.19.4))
			client.transport.ping_timeout(The time to wait for a ping response from a node. Defaults to 5s.)
			client.transport.nodes_sampler_interval(How often to sample / ping the nodes listed and connected. Defaults to 5s.)
		 */

		TransportClient transportClient = null;

		if(CommonUtils.isNotEmpty(hostname)){
			transportClient = new TransportClient(settings);
			for (String host : hostname) {
				transportClient.addTransportAddress(new InetSocketTransportAddress(host, port));
			}
		}
		return transportClient;
	}

	/**
	 * Embedded node clients behave just like standalone nodes,
	 * which means that they will leave the HTTP port open!
	 * @return client
	 */
	public static Client getEmbeddedNodeClient() {
		Node node = NodeBuilder.nodeBuilder()
				.settings(ImmutableSettings.settingsBuilder().put("http.enabled", false))
				.client(true)
				.node();

		try {
			return node.client();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			node.close();
		}
		return null;
	}

	/**
	 * 实例化一个基于节点的Client。
	 * @return
	 */
	public static Client getNodeClient(String clusterName) {
		Node node = null;
		try {
			if(CommonUtils.isNotEmpty(clusterName)){
				node = NodeBuilder.nodeBuilder().clusterName(clusterName).node();
			}else{
				node = NodeBuilder.nodeBuilder().local(true).node();
			}
		
			return node.client();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			node.close();
		}
		return null;
	}
	
	/**
	 * 创建索引
	 * @ client
     * @ indexName 为索引库名，一个es集群中可以有多个索引库。 名称必须为小写
     * @ indexType Type为索引类型，是用来区分同索引库下不同类型的数据的，一个索引库下可以有多个索引类型。
     * @ jsonData  json格式的数据集合
     */
    public static void createIndexResponse(Client client, String indexname, String type, String... jsondata) {
        //创建索引库 需要注意的是.setRefresh(true)这里一定要设置,否则第一次建立索引查找不到数据
        IndexRequestBuilder requestBuilder = client.prepareIndex(indexname, type).setRefresh(true);
        for (int i = 0; i < jsondata.length; i++) {
            requestBuilder.setSource(jsondata[i]).execute().actionGet();
        }
    }
	
	/**
	 * 查询索引是否存在
	 * @param indexName
	 * @return
	 */
	public static boolean queryIndexExists(Client client, String indexName) {
		IndicesExistsRequest indicesExistsRequest = new IndicesExistsRequest(indexName.toLowerCase());
		indicesExistsRequest.indices();
		return client.admin().indices().exists(indicesExistsRequest).actionGet().isExists();
	}
	
	/**
	 * 根据搜索域搜索索引是否存在
	 * @param client
	 * @param indexName 索引名称
	 * @param field 搜索域
	 * @param fieldValue 搜索内容
	 * @return
	 */
	public static boolean searchIndexExists(Client client, String indexName, String field, String fieldValue) {

//		client.prepareSearch(indexName).setQuery(QueryBuilders.termQuery(field, fieldValue)).get();
		
		CountResponse countResponse = client.prepareCount(indexName).setQuery(QueryBuilders.termQuery(field, fieldValue)).get();

//		ExistsResponse reseponse = client.prepareExists(indexName).setQuery(QueryBuilders.boolQuery()
//				.must(QueryBuilders.termQuery(field, fieldValue)))
//				.get();
		// 如果存在返回true
//		return reseponse.exists();
		return countResponse.getCount() > 0;
	}

	/**
	 * 查询索引类别是否存在
	 * @param indexName
	 * @param indexType
	 * @return
	 */
	public static boolean typesExists(Client client, String indexName, String indexType) {
		if(CommonUtils.isNotEmpty(indexName) && CommonUtils.isNotEmpty(indexType)){
			// 小写
			indexName = indexName.toLowerCase();
			indexType = indexType.toLowerCase();
			
			if (queryIndexExists(client, indexName)) {
				TypesExistsRequest typesExistsRequest = new TypesExistsRequest(new String[] { indexName },
						indexType);
				return client.admin().indices().typesExists(typesExistsRequest).actionGet().isExists();
			}
		}

		return false;
	}
	
	
	
	/**
	 * 根据索引数据ID删除索引
	 * @param client
	 * @param indexName 索引名称
	 * @param indexType 索引类型
	 * @param id 对应数据ID
	 */
	public static void deleteIndex(Client client, String indexName, String indexType, String id){
		client.prepareDelete(indexName.toLowerCase(), indexType.toLowerCase(), id).execute().actionGet();
	}
	
	/**
	 * 根据索引名称删除索引
	 * @param indexName
	 */
	public static void deleteIndex(Client client, String indexName){
		if(queryIndexExists(client, indexName)){
			client.admin().indices().prepareDelete(indexName.toLowerCase()).execute().actionGet();
		}
	}
	
	/**
	 * 去掉不存在的索引
	 * @param indexName
	 */
	public static void refreshIndex(Client client, String indexName) {
		IndicesAdminClient adminClient = client.admin().indices();
		// 查询索引是否存在
		if(queryIndexExists(client, indexName)){
			adminClient .open(new OpenIndexRequest(indexName)).actionGet();
		}else{
			throw new IndexMissingException(new Index(indexName));
		}
		
		try{
			// 刷新索引
			adminClient.refresh(new RefreshRequest(indexName)).actionGet();
		}catch(IndexMissingException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 搜索
	 * @param indexName
	 * @param indexType
	 * @param queryBuilder
	 * @param sortBuilders
	 * @param from
	 * @param size
	 * @return
	 * @throws IndexMissingException
	 */
	public static SearchHits search(Client client, String indexName, String indexType, QueryBuilder queryBuilder,
			List<FieldSortBuilder> sortBuilders, int from, int size) throws IndexMissingException {
		
		if(CommonUtils.isEmpty(client)){
			System.err.println("client be not null");
			return null;
		}
		
		// 转为小写
		indexName = indexName.toLowerCase();
		indexType = indexType.toLowerCase();
		
		// 去掉不存在的索引
//		refreshIndex(client, indexName);
		
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName)
				.setTypes(indexType)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setFrom(from)
				.setSize(size)
				.setQuery(queryBuilder);
		
		if(CommonUtils.isNotEmpty(sortBuilders)){
			for (FieldSortBuilder fieldSortBuilder : sortBuilders) {
				searchRequestBuilder.addSort(fieldSortBuilder);
			}
		}

//		System.out.println("curl:" + searchRequestBuilder.toString());
		
		return searchRequestBuilder.execute().actionGet().getHits();
	}

	/**
	 * 搜索
	 * @param indexName
	 * @param indexType
	 * @param sortBuilders
	 * @param from
	 * @param size
	 * @return
	 * @throws IndexMissingException
	 */
	public static SearchRequestBuilder getSearchRequestBuilder(Client client, String indexName, String indexType,
			List<FieldSortBuilder> sortBuilders, int from, int size) throws IndexMissingException {
		
		if(CommonUtils.isEmpty(client)){
			System.err.println("client be not null");
			return null;
		}
		
		// 转为小写
		indexName = indexName.toLowerCase();
		indexType = indexType.toLowerCase();
		
		// 去掉不存在的索引
		refreshIndex(client, indexName);
		
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName)
				.setTypes(indexType)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setFrom(from)
				.setSize(size)
				.setExplain(false);
		
		if(CommonUtils.isNotEmpty(sortBuilders)){
			for (FieldSortBuilder fieldSortBuilder : sortBuilders) {
				searchRequestBuilder.addSort(fieldSortBuilder);
			}
		}
		
		return searchRequestBuilder;
	}


	/**
	 * 组合查询
	 * @param must
	 * @param not
	 * @param should
	 * @param fieldName
	 * @return BoolQueryBuilder
	 */
	public static BoolQueryBuilder composeQueryString(List<String> must, List<String> should, List<String> not, String fieldName, BoolQueryBuilder boolQueryBuilder) {
		BoolQueryBuilder tempQuery = null;
		
		if (CommonUtils.isNotEmpty(must)) {
			tempQuery = QueryBuilders.boolQuery();
			for (String tempStr : must) {
				QueryStringQueryBuilder queryString = QueryBuilders.queryStringQuery(QueryParser.escape(tempStr)).defaultField(fieldName);
				tempQuery.must(queryString);
			}
			boolQueryBuilder.must(tempQuery);
		}
		if (CommonUtils.isNotEmpty(should)) {
			tempQuery = QueryBuilders.boolQuery();
			for (String tempStr : should) {
				QueryStringQueryBuilder queryString = QueryBuilders.queryStringQuery(QueryParser.escape(tempStr)).defaultField(fieldName);
				tempQuery.should(queryString);
			}
			boolQueryBuilder.must(tempQuery);
		}
		if (CommonUtils.isNotEmpty(not)) {
			tempQuery = QueryBuilders.boolQuery();
			for (String tempStr : not) {
				QueryStringQueryBuilder queryString = QueryBuilders.queryStringQuery(QueryParser.escape(tempStr)).defaultField(fieldName);
				tempQuery.mustNot(queryString);
			}
			boolQueryBuilder.must(tempQuery);
		}
		return boolQueryBuilder;
	}


	public static BoolQueryBuilder composeTermQuery(List<String> must, List<String> should, List<String> not, String fieldName, BoolQueryBuilder boolQueryBuilder) {
		if (CommonUtils.isNotEmpty(must)) {
			for (String tempStr : must) {
				boolQueryBuilder.must(QueryBuilders.termQuery(fieldName, tempStr));
			}
		}
		if (CommonUtils.isNotEmpty(should)) {
//			boolQueryBuilder.should(QueryBuilders.termsQuery(fieldName, should));
			for (String tempStr : should) {
				boolQueryBuilder.should(QueryBuilders.termQuery(fieldName, tempStr));
			}
		}
		if (CommonUtils.isNotEmpty(not)) {
//			boolQueryBuilder.must(QueryBuilders.boolQuery().mustNot(QueryBuilders.termsQuery(fieldName, not)));
			for (String tempStr : not) {
				boolQueryBuilder.mustNot(QueryBuilders.termQuery(fieldName, tempStr));
			}
		}
		return boolQueryBuilder;
	}

	/**
	 * 获取过滤参数
	 * @param orderField
	 * @param order
	 * @return
	 */
	public static List<FieldSortBuilder> getFieldSortBuilders(String orderField, String order) {
		FieldSortBuilder fieldSortBuilder = null;
		// 排序字段
		if (CommonUtils.isNotEmpty(orderField) && !"SCORE".equals(orderField)) {
			fieldSortBuilder = SortBuilders.fieldSort(orderField);
			// 升降序
			if(order == null){
				order = "desc";
			}
			order = order.toLowerCase();
			if(CommonUtils.isNotEmpty(order)){
				if (SortOrder.DESC.toString().equals(order)) {
					// 降序
					fieldSortBuilder.order(SortOrder.DESC);
				} else if (SortOrder.ASC.toString().equals(order)) {
					// 升序
					fieldSortBuilder.order(SortOrder.ASC);
				}
			} else {
				// 默认降序
				fieldSortBuilder.order(SortOrder.DESC);
			}
		}

		if(CommonUtils.isNotEmpty(fieldSortBuilder)){
			List<FieldSortBuilder> sorts = new ArrayList<FieldSortBuilder>();
			sorts.add(fieldSortBuilder);
			return sorts;
		}
		
		return null;
	}
	
	
	public static BoolQueryBuilder composeRangeQueryBuilder(long start,long end, String fieldName, BoolQueryBuilder boolQueryBuilder){
		boolQueryBuilder.must(QueryBuilders.rangeQuery(fieldName).from(start).to(end));
		return boolQueryBuilder;
	}
	
	public static BoolQueryBuilder composeRangeQueryBuilder(float start,float end, String fieldName, BoolQueryBuilder boolQueryBuilder){
		boolQueryBuilder.must(QueryBuilders.rangeQuery(fieldName).from(start).to(end));
		return boolQueryBuilder;
	}
	
	public static String getIdOfLucene(Document document){
		return document.getArticleProject()+"_"+document.getArticleId();
	}


	/**
	 * 查询总数
	 * @param indexName
	 * @param type
	 * @param filedName
	 * @param value
	 * @return 总数
	 */
	public static long queryCount(String indexName, String type,
			String filedName, String value) {
		Client client = getClient();
		
		CountResponse response = client.prepareCount(indexName)
				.setTypes(type)
				.setQuery(QueryBuilders.termQuery(filedName, value))
				.get();
		
		return response.getCount();
		
	}
	
}
