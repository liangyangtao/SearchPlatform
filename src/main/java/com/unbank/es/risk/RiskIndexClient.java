package com.unbank.es.risk;

import java.util.Map;

import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unbank.common.constants.CommonConstants;
import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.CommonUtils;
import com.unbank.common.utils.ESUtils;
import com.unbank.entity.risk.Risk;

public class RiskIndexClient {
	private static final Logger logger = LoggerFactory.getLogger(RiskIndexClient.class); 

	private String indexName = SysParameters.RISKINDEX;
	private String type = SysParameters.RISKTYPE;
	
	/**
	 * 初审添加索引
	 * @param risk
	 * @return
	 */
	public Map<String, String> index(Map<String, String> map, Risk risk){
		IndexRequestBuilder indexRequestBuilder = ESUtils.getClient().prepareIndex(indexName, type);
		// 新闻ID
		int newsId = risk.getCrawl_id();
		// 栏目ID
		String categoryId = risk.getCategoryId();
		// 栏目名称
		String categoryName = risk.getCategory();
		
		// 过滤重复的栏目ID
		map = categoryFilter(map, newsId, categoryId, categoryName);
		
		if(CommonConstants.ERROR.equals(map.get("status"))){
			logger.info("建立风险索引失败,passAndIndex,end,crawl_id:" + newsId + ",\t status:" + CommonConstants.ERROR);
			return map;
		}
		
		// 过滤后的ID和名称
		String finallyId = map.get("categoryId");
		String finallyName = map.get("categoryName");
		
		risk.setCategoryId(finallyId);
		risk.setCategory(finallyName);
		
		risk.setRepetitive("false");

		try {
			logger.info("开始建立Risk索引,crawl_id:" + newsId + ",\t categoryId:" + categoryId + ",\t 过滤后：finallyCategoryId:" + finallyId);
			
			IndexResponse response = indexRequestBuilder.setSource(risk.toJsonString()).execute().actionGet();
	
			String esId = response.getId();
			
			logger.info("建立Risk索引完成,passAndIndex,end,crawl_id:" + newsId + ",\t status:" + CommonConstants.SUCCESS + ",\t ESID:" + esId);
			
			map.put("status", CommonConstants.SUCCESS);
			map.put("msg", esId);
		} catch (Exception e) {
			logger.error(CommonUtils.getExceptionMessage(e));
			logger.info("建立风险索引失败,passAndIndex,end,crawl_id:" + newsId + ",\t status:" + CommonConstants.ERROR);
			map.put("status", CommonConstants.ERROR);
			map.put("msg", "建立索引失败");
		}
		return map;
	}
	
	/**
	 * 过滤栏目ID和栏目名称
	 * @param map
	 * @param newsId
	 * @param categoryId
	 * @param categoryName
	 * @return
	 */
	private Map<String, String> categoryFilter(Map<String, String> map, int newsId, String categoryId, String categoryName){
		Client client = ESUtils.getClient();
		// 栏目ID
		String[] categoryIds = categoryId.split(",");
		// 栏目名称
		String[] categoryNames = categoryName.split(",");
		
		// TODO 长度不相等
		if(categoryNames.length != categoryIds.length){
			map.put("status", CommonConstants.ERROR);
			map.put("msg", "参数错误,categoryId 和 categoryName 长度必须相同");
			return map;
		}
		// ID
		String tempIdStr = "";
		// NAME
		String tempNameStr = "";
		for (int i = 0; i < categoryIds.length; i++) {
			// 获取栏目ID,去空格
			String tempStr = categoryIds[i].trim();
			if(CommonUtils.isEmpty(tempStr)){
				map.put("status", CommonConstants.ERROR);
				map.put("msg", "参数错误,categoryId不可为空!");
				return map;
			}
			String[] tempArray = tempStr.split(" ");
			if(CommonUtils.isNotEmpty(tempArray)){
				// 封装搜索条件
				BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
				boolQueryBuilder.must(QueryBuilders.termQuery("crawl_id", newsId));
				for (String temp : tempArray) {
					boolQueryBuilder.must(QueryBuilders.termQuery("categoryId", temp.trim()));
				}
				// 获取结果
				CountResponse response = client.prepareCount(SysParameters.RISKINDEX)
						.setTypes(SysParameters.RISKTYPE)
						.setQuery(boolQueryBuilder)
						.get();
				long count = response.getCount();
				
				// 不存在
				if(count == 0){
					if(CommonUtils.isNotEmpty(tempIdStr)){
						tempIdStr += " " + tempStr;
						tempNameStr += " " + categoryNames[i].trim();
					}else{
						tempIdStr += tempStr;
						tempNameStr += categoryNames[i].trim();
					}
				}
			}
		}
		
		// 重复的栏目ID
		if(CommonUtils.isEmpty(tempIdStr) && CommonUtils.isEmpty(tempNameStr)){
			map.put("status", CommonConstants.ERROR);
			map.put("msg", "栏目重复,提交失败");
			return map;
		}
		
		// 过滤后的数据
		map.put("status", CommonConstants.SUCCESS);
		map.put("msg", "SUCCESS");
		map.put("categoryName", tempNameStr);
		map.put("categoryId", tempIdStr);
		return map;
	}

}
