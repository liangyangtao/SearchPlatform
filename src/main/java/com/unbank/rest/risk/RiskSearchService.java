package com.unbank.rest.risk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unbank.common.utils.CommonUtils;
import com.unbank.entity.risk.Risk;
import com.unbank.entity.risk.RiskSearchCondition;
import com.unbank.entity.risk.SearchRisk;
import com.unbank.entity.risk.SearchRiskData;
import com.unbank.es.risk.RiskSearchClient;
import com.unbank.es.search.SearchErrorInfo;

@Component
@Path("/risk/multiConditionSearch")
public class RiskSearchService {

	private static final Logger logger = LoggerFactory.getLogger(RiskSearchService.class);

	@Path("/searchByEsId")
	@POST
	public String searchByEsId(@FormParam("esId") String esId){
		RiskSearchClient searchClient = new RiskSearchClient();
		logger.info("风险库，按照ESID搜索,searchByNewsId,newsId:" + esId);
		
		if (esId == null) {
			// 参数错误
			logger.info("风险库，按照esId搜索,esId," + SearchErrorInfo.ILLEGALARGS);
			return SearchErrorInfo.ILLEGALARGS;
		}
		SearchRisk risk=searchClient.searchByESId(esId);
		return risk == null ? null : risk.toJsonString();
	}
	
	@Path("/searchByIds")
	@POST
	public String searchByCrawlId(@FormParam("crawl_id") String crawl_id, @FormParam("categoryId") String categoryId) {
		RiskSearchClient searchClient = new RiskSearchClient();
		logger.info("风险库，按照新闻ID搜索,searchByNewsId,newsId:" + crawl_id);
		if (crawl_id == null || categoryId == null) {
			// 参数错误
			logger.info("风险库，按照新闻ID搜索,searchByNewsId," + SearchErrorInfo.ILLEGALARGS);
			return SearchErrorInfo.ILLEGALARGS;
		}
		int id = Integer.valueOf(crawl_id);
		int _cid = Integer.valueOf(categoryId);

		Risk risk = searchClient.searchByCrawlId(id, _cid);
		if (risk == null) {
			return null;
		}
		Gson gson = new Gson();
		return gson.toJson(risk);
	}

	@Path("/searchByJsonStr")
	@POST
	public String searchByJsonStr(@FormParam("jsonStr") String jsonStr, @FormParam("from") String from,
			@FormParam("pageSize") String pageSize, @FormParam("startTime") Long startTime,
			@FormParam("endTime") Long endTime, @FormParam("orderByColumn") String orderByColumn,
			@FormParam("fullContent") boolean fullContent) {
		Gson gson = new Gson();
		RiskSearchCondition newsSearchCondition = gson.fromJson(jsonStr, RiskSearchCondition.class);
		logger.info("风险库，searchByJsonStr:" + jsonStr + "  from:" + from + "	pageSize:" + pageSize + "	startTime:"
				+ startTime + "		endTime:" + endTime + "		orderByColumn:" + orderByColumn + "   fullContent:"
				+ fullContent);
		RiskSearchClient sc = new RiskSearchClient();
		Integer size = 20;
		Integer f = 0;
		try {
			if ((startTime != null && endTime == null) || (startTime == null && endTime != null)) {
				return SearchErrorInfo.TIMEARGS;
			}
			if ((startTime != null && endTime != null) && (startTime >= endTime)) {
				return SearchErrorInfo.TIMEORDERARGS;
			}
			if (pageSize != null) {
				size = Integer.valueOf(pageSize);
			}
			if (from != null) {
				f = Integer.valueOf(from);
			}
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			logger.info("风险库，searchByJsonStr," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}
		SearchRiskData sd = sc.search(newsSearchCondition, f, size, startTime, endTime, orderByColumn);
		return sd.toJsonString(fullContent);
	}

	@Path("/searchByMultiJsonStr")
	@POST
	public String searchByMultiJsonStr(@FormParam("jsonStr") String jsonStr,
			@FormParam("filterJsonStr") String filterJsonStr, @FormParam("from") String from,
			@FormParam("pageSize") String pageSize, @FormParam("startTime") Long startTime,
			@FormParam("endTime") Long endTime, @FormParam("orderByColumn") String orderByColumn,
			@FormParam("fullContent") boolean fullContent) {
		logger.info("风险库，searchByMultiJsonStr:" + jsonStr + "  	filterJsonStr:" + filterJsonStr + "  from:" + from
				+ "	pageSize:" + pageSize + "	startTime:" + startTime + "		endTime:" + endTime + "		orderByColumn:"
				+ orderByColumn + "   fullContent:" + fullContent);
		Gson gson = new Gson();

		ArrayList<RiskSearchCondition> newsSearchConditions = gson.fromJson(jsonStr,
				new TypeToken<List<RiskSearchCondition>>() {
				}.getType());

		RiskSearchCondition newsSearchCondition = null;
		if (!CommonUtils.isEmpty(filterJsonStr)) {
			Gson gson2 = new Gson();
			newsSearchCondition = gson2.fromJson(filterJsonStr, RiskSearchCondition.class);
		}

		RiskSearchClient sc = new RiskSearchClient();
		Integer size = 20;
		Integer f = 0;
		try {
			if ((startTime != null && endTime == null) || (startTime == null && endTime != null)) {
				return SearchErrorInfo.TIMEARGS;
			}
			if ((startTime != null && endTime != null) && (startTime >= endTime)) {
				return SearchErrorInfo.TIMEORDERARGS;
			}
			if (pageSize != null) {
				size = Integer.valueOf(pageSize);
			}
			if (from != null) {
				f = Integer.valueOf(from);
			}

		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			logger.info("风险库，searchByMultiJsonStr," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}

		SearchRiskData sd = sc.search(newsSearchConditions, newsSearchCondition, f, size, startTime, endTime,
				orderByColumn);
		return sd.toJsonString(fullContent);
	}

	@Path("/moreLikeSearch")
	@POST
	public String moreLikeSearch(@FormParam("from") String from, @FormParam("pageSize") String pageSize,
			@FormParam("crawl_id") int crawl_id, @FormParam("categoryId") int categoryId,
			@FormParam("fullContent") boolean fullContent) {
		Integer size = 20;
		Integer f = 0;
		try {
			if (pageSize != null) {
				size = Integer.valueOf(pageSize);
			}
			if (from != null) {
				f = Integer.valueOf(from);
			}

		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			logger.info("风险库，moreLikeSearch," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}

		RiskSearchClient client = new RiskSearchClient();

		SearchRiskData srd = client.moreLikeSearch(crawl_id, categoryId, f, size);
		if(srd == null){
			return null;
		}
		return srd.toJsonString(fullContent);
	}
	
	@Path("/moreLikeSearchByEsId")
	@POST
	public String moreLikeSearch(@FormParam("from") String from, @FormParam("pageSize") String pageSize,
			 @FormParam("esId") String esId,
			@FormParam("fullContent") boolean fullContent) {
		Integer size = 20;
		Integer f = 0;
		try {
			if (pageSize != null) {
				size = Integer.valueOf(pageSize);
			}
			if (from != null) {
				f = Integer.valueOf(from);
			}
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			logger.info("风险库，moreLikeSearch," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}
		RiskSearchClient client = new RiskSearchClient();
		SearchRiskData srd = client.moreLikeSearch(esId, f, size);
		return srd.toJsonString(fullContent);
	}
	

	@Path("/queryStringQuery")
	@POST
	public String queryStringQuery(@FormParam("queryString") String queryString,
			@FormParam("columnBoost") String columnBoost,
			@FormParam("filterJsonStr") String filterJsonStr,
			@FormParam("from") String from,
			@FormParam("pageSize") String pageSize,
			@FormParam("fullContent") boolean fullContent){
		
		if(queryString==null){
			return SearchErrorInfo.ILLEGALARGS;
		}
		
		Gson gson=new Gson();
		HashMap<String,Float> boosts=null;
		if(columnBoost!=null){
			boosts=gson.fromJson(columnBoost, new TypeToken<HashMap<String,Float>>() {  }.getType());
		}
		
		RiskSearchCondition filterCondition= null;
		if(filterJsonStr!=null){
			filterCondition=gson.fromJson(filterJsonStr, RiskSearchCondition.class);
		}
		
		RiskSearchClient client=new RiskSearchClient();
		Integer size = 20;
		Integer f = 0;
		try {
			if (pageSize != null) {
				size = Integer.valueOf(pageSize);
			}
			if (from != null) {
				f = Integer.valueOf(from);
			}
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			logger.info("文档库，searchByJsonStr," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}
		SearchRiskData searchRiskData= client.queryStringQuery(queryString, boosts, filterCondition, f, size);
		return searchRiskData.toJsonString(fullContent);
	}

}
