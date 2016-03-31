package com.unbank.rest.news;

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
import com.unbank.common.utils.GsonUtil;
import com.unbank.entity.NewsSearchCondition;
import com.unbank.entity.SearchData;
import com.unbank.es.search.SearchClient;
import com.unbank.es.search.SearchErrorInfo;

@Component
@Path("/multiConditionSearch")
public class MultiConditionSearchService {
	
	private static final Logger logger = LoggerFactory.getLogger(MultiConditionSearchService.class);
	
	@Path("/searchByJsonStr")
	@POST
	public String searchByJsonStr(@FormParam("jsonStr") String jsonStr,
			@FormParam("from") String from,
			@FormParam("pageSize") String pageSize,
			@FormParam("startTime") Long startTime,
			@FormParam("endTime") Long endTime,
			@FormParam("fullContent") boolean fullContent,
			@FormParam("orderByColumn") String orderByColumn,
			@FormParam("highlight") boolean highlight) {
		NewsSearchCondition newsSearchCondition=GsonUtil.getObjectFromJsonStr(jsonStr, NewsSearchCondition.class);
		logger.info("searchByJsonStr:" + jsonStr + "\t from:" + from
				+ "\t pageSize:" + pageSize + "\t startTime:" + startTime
				+ "\t endTime:" + endTime + "\t orderByColumn:" + orderByColumn + "\t highlight:" + highlight);
		SearchClient sc = new SearchClient();
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
			logger.info("searchByJsonStr," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}
		SearchData sd = sc.search(newsSearchCondition, f, size, startTime, endTime, orderByColumn, fullContent, highlight);
		return sd.toJsonString();
	}
	
	@Path("/searchByMultiJsonStr")
	@POST
	public String searchByMultiJsonStr(@FormParam("jsonStr") String jsonStr,
			@FormParam("filterJsonStr") String filterJsonStr,
			@FormParam("from") String from,
			@FormParam("pageSize") String pageSize,
			@FormParam("startTime") Long startTime,
			@FormParam("endTime") Long endTime,
			@FormParam("fullContent") boolean fullContent,
			@FormParam("orderByColumn") String orderByColumn,
			@FormParam("highlight") boolean highlight,
			@FormParam("searchFlag") boolean searchFlag) {
		
		logger.info("searchByMultiJsonStr:" + jsonStr + "\t filterJsonStr:"
				+ filterJsonStr + "\t from:" + from + "\t pageSize:" + pageSize
				+ "\t startTime:" + startTime + "\t endTime:" + endTime
				+ "\t orderByColumn:" + orderByColumn + "\t highlight:" + highlight + "\t searchFlag:" + searchFlag);
		Gson gson = new Gson();
		
		List<NewsSearchCondition> newsSearchConditions = gson.fromJson(jsonStr, new TypeToken<List<NewsSearchCondition>>() {}.getType());

		NewsSearchCondition newsSearchCondition = null;
		if (!CommonUtils.isEmpty(filterJsonStr)) {
			newsSearchCondition = gson.fromJson(filterJsonStr, NewsSearchCondition.class);
		}
		
		SearchClient sc = new SearchClient();
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
			logger.info("searchByMultiJsonStr," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}
		
		SearchData sd = sc.search(newsSearchConditions, newsSearchCondition, f, size, startTime, endTime, orderByColumn, fullContent, highlight, searchFlag);
		return sd.toJsonString();
	}

}
