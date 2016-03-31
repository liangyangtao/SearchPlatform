package com.unbank.es.risk;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.unbank.common.constants.ServerConfig;
import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.ESUtils;

public class QueryRiskInsert {

	public static void main(String[] args) {
		
		// 盲目扩张致一丁集团破产31220769
		// 误涨还是勿涨?31224508
		// 连续三年亏损 雅芳疑似退市30818984
		 SearchResponse re = ESUtils.getClusterClient(ServerConfig.getClusterName(), true, 9300, "122.112.1.72")
				.prepareSearch(SysParameters.RISKINDEX)
				.setTypes(SysParameters.RISKTYPE)
				.setQuery(QueryBuilders.termQuery("repetitive", "0"))// QueryBuilders.termQuery("crawl_id", "31220769")
				// QueryBuilders.queryStringQuery("尿素").field("title")
				.setFrom(0)
				.setSize(100)
				.get();

		SearchHits hits = re.getHits();
		System.out.println(hits.getTotalHits());
//		TransportClient client = ESUtils.getClusterClient(ServerConfig.getClusterName(), true, 9300, "localhost");
		for (SearchHit searchHit : hits.getHits()) {
			System.out.println(searchHit.getSource().get("title"));
//			IndexRequestBuilder indexRequestBuilder = client.prepareIndex(SysParameters.RISKINDEX, SysParameters.RISKTYPE);
//			indexRequestBuilder.setSource(searchHit.getSource());
//			indexRequestBuilder.execute().actionGet();
		}
	}
	
	
//	public static void main(String[] args) {
//		int crawl_id = 31220769;
//		String categoryId = "20 27 34, 26 158 2, 149 150";
//		String categoryName = "BII风险 信用风险 商业贷款信用风险, 行业风险 批发与零售业, 业务条线风险 对公业务风险";
//		
//		Client client = ESUtils.getClient(CommonConstants.CLUSTERNAME_DEFAULT);
//		
//		String[] categoryIds = categoryId.split(",");
//		String[] categoryNames = categoryName.split(",");
//		if(categoryNames.length != categoryIds.length){
//			// TODO 数据不相等
//			System.out.println("参数错误,categoryId 和 categoryName 长度必须相同");
//			return;
//		}
//		// ID
//		String tempIdStr = "";
//		// NAME
//		String tempNameStr = "";
//		for (int i = 0; i < categoryIds.length; i++) {
//			// 
//			String tempStr = categoryIds[i].trim();
//			if(CommonUtils.isEmpty(tempStr)){
//				System.out.println("参数错误,categoryId不可为空!");
//			}
//			String[] tempArray = tempStr.split(" ");
//			if(CommonUtils.isNotEmpty(tempArray)){
//				// 封装搜索条件
//				BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//				boolQueryBuilder.must(QueryBuilders.termQuery("crawl_id", crawl_id));
//				for (String temp : tempArray) {
//					boolQueryBuilder.must(QueryBuilders.termQuery("categoryId", temp.trim()));
//				}
//				System.out.println(boolQueryBuilder.toString());
//				//获取结果
//				CountResponse response = client.prepareCount(SysParameters.RISKINDEX)
//						.setTypes(SysParameters.RISKTYPE)
//						.setQuery(boolQueryBuilder)
//						.get();
//				// 结果
//				long count = response.getCount();
//				
//				if(count == 0){
//					if(CommonUtils.isNotEmpty(tempIdStr)){
//						tempIdStr += " " + tempStr;
//						tempNameStr += " " + categoryNames[i].trim();
//					}else{
//						tempIdStr += tempStr;
//						tempNameStr += categoryNames[i].trim();
//					}
//				}
//			}
//		}
//		
//		if(CommonUtils.isEmpty(tempIdStr)){
//			System.out.println("栏目重复，提交失败");
//			return;
//		}
//		
//		System.out.println("ids:" + tempIdStr + "______" + "names:" + tempNameStr);
//		
//	}
	
}
