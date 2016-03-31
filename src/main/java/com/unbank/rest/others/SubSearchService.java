package com.unbank.rest.others;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.unbank.entity.SearchData;
import com.unbank.entity.SearchNews;
import com.unbank.es.search.SearchErrorInfo;
import com.unbank.es.search.SubSearchClient;

@Component
@Path("/subSearch")
public class SubSearchService {

	private static final Logger logger = LoggerFactory.getLogger(SubSearchService.class);

	@Path("/searchByORTags")
	@POST
	public String searchByORTags(@FormParam("tagNames") String tagNames, @FormParam("startTime") Long startTime,
			@FormParam("endTime") Long endTime, @FormParam("pageSize") String pageSize,
			@FormParam("from") String from) {

		SubSearchClient searchClient = new SubSearchClient();
		Integer size = 20;
		Integer f = 0;

		try {

			if ((startTime != null && endTime == null) || (startTime == null && endTime != null)) {
				return SearchErrorInfo.TIMEARGS;
			}
			if ((startTime!=null&&endTime!=null)&&(startTime >= endTime)) {
				return SearchErrorInfo.TIMEORDERARGS;
			}
			if (tagNames == null) {
				return SearchErrorInfo.ILLEGALARGS;
			}
			if (pageSize != null) {
				size = Integer.valueOf(pageSize);
			}
			if (from != null) {
				f = Integer.valueOf(from);
			}
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			logger.info("按照分类搜索,searchByORTags," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}

		/**
		 * 多个标签按照“_”分隔
		 */
		String[] arrayTags = tagNames.split("_");
		List<String> tagList = convertArrayToList(arrayTags);
		List<SearchNews>newsList = searchClient.searchByORTags(tagList, startTime, endTime, f, size);
		Long count = searchClient.countByORTags(tagList, startTime, endTime);

		SearchData sd = new SearchData(count, newsList);

		return convertSearchDataToString(sd);
	}

	@Path("/searchByANDTags")
	@POST
	public String searchByANDTags(@FormParam("tagNames") String tagNames, @FormParam("startTime") Long startTime,
			@FormParam("endTime") Long endTime, @FormParam("pageSize") String pageSize,
			@FormParam("from") String from) {

		SubSearchClient searchClient = new SubSearchClient();
		Integer size = 20;
		Integer f = 0;

		try {
			if (tagNames == null) {
				return SearchErrorInfo.ILLEGALARGS;
			}
			if ((startTime != null && endTime == null) || (startTime == null && endTime != null)) {
				return SearchErrorInfo.TIMEARGS;
			}
			if ((startTime!=null&&endTime!=null)&&(startTime >= endTime)) {
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
			logger.info("按照分类搜索,searchByORTags," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}

		/**
		 * 多个标签按照“_”分隔
		 */
		String[] arrayTags = tagNames.split("_");
		List<String> tagList = convertArrayToList(arrayTags);
		List<SearchNews>newsList = searchClient.searchByANDTags(tagList, startTime, endTime, f, size);
		Long count = searchClient.countByANDTags(tagList, startTime, endTime);
		SearchData sd = new SearchData(count, newsList);
		return convertSearchDataToString(sd);
	}

	@Path("/searchByORTagsAndORWebsiteIDs")
	@POST
	public String searchByORTagsAndORWebsiteIDs(@FormParam("tagNames") String tagNames,
			@FormParam("titleStr") String titleStr, @FormParam("contentStr") String contentStr,
			@FormParam("startTime") Long startTime, @FormParam("endTime") Long endTime,
			@FormParam("websiteIDs") String websiteIDs, @FormParam("pageSize") String pageSize,
			@FormParam("from") String from) {
		
		SubSearchClient searchClient = new SubSearchClient();
		Integer size = 20;
		Integer f = 0;
		try {

			if ((startTime != null && endTime == null) || (startTime == null && endTime != null)) {
				return SearchErrorInfo.TIMEARGS;
			}
			if ((startTime!=null&&endTime!=null)&&(startTime >= endTime)) {
				return SearchErrorInfo.TIMEORDERARGS;
			}
			if (tagNames == null) {
				return SearchErrorInfo.ILLEGALARGS;
			}
			if (websiteIDs == null) {
				return SearchErrorInfo.ILLEGALARGS;
			}
			if (pageSize != null) {
				size = Integer.valueOf(pageSize);
			}
			if (from != null) {
				f = Integer.valueOf(from);
			}
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			logger.info("按照分类搜索,searchByORTags," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}

		/**
		 * 多个标签按照“_”分隔
		 */
		String[] arrayTags = tagNames.split("_");
		List<String> tagList = convertArrayToList(arrayTags);

		String[] arrayWebsiteIDs = websiteIDs.split("_");
		List<String> websiteIDList = convertArrayToList(arrayWebsiteIDs);
		List<SearchNews>newsList = searchClient.searchByORTagsAndORWebsiteIDs(tagList, websiteIDList,titleStr,contentStr, startTime, endTime, f,
				size);
		Long count = searchClient.countByORTagsAndORWebsiteIDs(tagList, websiteIDList, titleStr,contentStr,startTime, endTime);
		SearchData sd = new SearchData(count, newsList);
		return convertSearchDataToString(sd);
	}
	
	
	@Path("/searchByMultiTagsAndWebsiteIDs")
	@POST
	public String searchByMultiTagsAndWebsiteIDs(@FormParam("mustTagNames") String mustTagNames,
			@FormParam("mustNotTagNames") String mustNotTagNames,
			@FormParam("shouldTagNames") String shouldTagNames,
			@FormParam("titleStr") String titleStr, @FormParam("contentStr") String contentStr,
			@FormParam("startTime") Long startTime, @FormParam("endTime") Long endTime,
			@FormParam("websiteIDs") String websiteIDs, @FormParam("pageSize") String pageSize,
			@FormParam("from") String from) {
		logger.info("searchByMultiTagsAndWebsiteIDs-参数：mustTagNames"+mustTagNames+"	mustNotTagNames："+mustNotTagNames+"	" +
				"	shouldTagNames:"+shouldTagNames+"	titleStr:"+titleStr+"" +
						"	contentStr:"+contentStr+"	startTime:"+startTime+"		endTime:"+endTime+"" +
								"		websiteIDs:"+websiteIDs+"	pageSize:"+pageSize+"	from:"+from);
		SubSearchClient searchClient = new SubSearchClient();
		Integer size = 20;
		Integer f = 0;
		try {
			if ((startTime != null && endTime == null) || (startTime == null && endTime != null)) {
				return SearchErrorInfo.TIMEARGS;
			}
			if ((startTime!=null&&endTime!=null)&&(startTime >= endTime)) {
				return SearchErrorInfo.TIMEORDERARGS;
			}
			if (mustTagNames == null&&mustNotTagNames==null&&shouldTagNames==null&&websiteIDs==null) {
				return SearchErrorInfo.ILLEGALARGS;
			}
			
			if (pageSize != null) {
				size = Integer.valueOf(pageSize);
			}
			if (from != null) {
				f = Integer.valueOf(from);
			}
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			logger.info("按照分类搜索,searchByORTags," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}

		/**
		 * 多个标签按照“_”分隔
		 */
		List<String> mustTagsList=new ArrayList<String>();
		if(mustTagNames!=null){
			String[] mustTagsArr = mustTagNames.split("_");
			mustTagsList = convertArrayToList(mustTagsArr);
		}
		List<String> websiteIDList =new ArrayList<String>();
		
		if(websiteIDs!=null){
			String[] arrayWebsiteIDs = websiteIDs.split("_");
			websiteIDList = convertArrayToList(arrayWebsiteIDs);
		}
		
		List<String> mustNotTagsList=new ArrayList<String>();
		
		if(mustNotTagNames!=null){
			String[] mustNotTagsArr = mustNotTagNames.split("_");
			mustNotTagsList= convertArrayToList(mustNotTagsArr);
		}
		
		List<String> shouldTagsList=new ArrayList<String>();
		
		if(shouldTagNames!=null){
			String[] arr = shouldTagNames.split("_");
			shouldTagsList= convertArrayToList(arr);
		}
		
		List<SearchNews>newsList = searchClient.searchByMultiTagsAndWebsiteIDs(mustTagsList,mustNotTagsList,shouldTagsList, websiteIDList,titleStr,contentStr, startTime, endTime, f,
				size);
		Long count = searchClient.countByMultiTagsAndWebsiteIDs(mustTagsList,mustNotTagsList,shouldTagsList, websiteIDList, titleStr,contentStr,startTime, endTime);
		SearchData sd = new SearchData(count, newsList);
		return convertSearchDataToString(sd);
	}
	
	@Path("/searchByNotTags")
	@POST
	public String searchByNotTags(@FormParam("mustTagNames") String mustTagNames,
			@FormParam("mustNotTagNames") String mustNotTagNames, @FormParam("startTime") Long startTime,
			@FormParam("endTime") Long endTime, @FormParam("pageSize") String pageSize,
			@FormParam("from") String from) {

		SubSearchClient searchClient = new SubSearchClient();
		Integer size = 20;
		Integer f = 0;
		try {

			if ((startTime != null && endTime == null) || (startTime == null && endTime != null)) {
				return SearchErrorInfo.TIMEARGS;
			}
			if ((startTime!=null&&endTime!=null)&&(startTime >= endTime)) {
				return SearchErrorInfo.TIMEORDERARGS;
			}
			if (mustTagNames == null) {
				return SearchErrorInfo.ILLEGALARGS;
			}
			if (mustNotTagNames == null) {
				return SearchErrorInfo.ILLEGALARGS;
			}
			if (pageSize != null) {
				size = Integer.valueOf(pageSize);
			}
			if (from != null) {
				f = Integer.valueOf(from);
			}
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			logger.info("按照分类搜索,searchByORTags," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}
		
		/**
		 * 多个标签按照“_”分隔
		 */
		String[] mustArrayTags = mustTagNames.split("_");
		List<String> mustTagList = convertArrayToList(mustArrayTags);

		String[] mustNotArrayTags = mustNotTagNames.split("_");
		List<String> mustNotTagList = convertArrayToList(mustNotArrayTags);
		
		List<SearchNews>newsList = searchClient.searchByNotTags(mustTagList, mustNotTagList, startTime, endTime, f, size);
		Long count=searchClient.countByNotTags(mustTagList, mustNotTagList, startTime, endTime);
		
		SearchData sd = new SearchData(count, newsList);
		return convertSearchDataToString(sd);
	}
	
	@Path("/searchByMultigroupTags")
	@POST
	public String searchByMultigroupTags(@FormParam("mustTagNames") String mustTagNames,
			@FormParam("mustNotTagNames") String mustNotTagNames,
			@FormParam("shouldTagNames") String shouldTagNames,
			@FormParam("startTime") Long startTime,
			@FormParam("endTime") Long endTime, @FormParam("pageSize") String pageSize,
			@FormParam("from") String from){
		
		SubSearchClient searchClient = new SubSearchClient();
		Integer size = 20;
		Integer f = 0;
		try {
			if ((startTime != null && endTime == null) || (startTime == null && endTime != null)) {
				return SearchErrorInfo.TIMEARGS;
			}
			if ((startTime!=null&&endTime!=null)&&(startTime >= endTime)) {
				return SearchErrorInfo.TIMEORDERARGS;
			}
			if (mustTagNames == null) {
				return SearchErrorInfo.ILLEGALARGS;
			}
			if (pageSize != null) {
				size = Integer.valueOf(pageSize);
			}
			if (from != null) {
				f = Integer.valueOf(from);
			}
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			logger.info("按照分类搜索,searchByORTags," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}
		
		/**
		 * 多个标签按照“_”分隔
		 */
		String[] mustArrayTags = mustTagNames.split("_");
		List<String> mustTagList = convertArrayToList(mustArrayTags);
		
		List<String> mustNotTagList=null;
		if(mustNotTagNames!=null){
			String[] mustNotArrayTags = mustNotTagNames.split("_");
			mustNotTagList = convertArrayToList(mustNotArrayTags);
		}
		List<String> shouldTagList=null;
		if(shouldTagNames!=null){
			String[] shouldArrayTags = shouldTagNames.split("_");
			shouldTagList = convertArrayToList(shouldArrayTags);
		}
		
		List<SearchNews>newsList = searchClient.searchByMultigroupTags(mustTagList,shouldTagList ,mustNotTagList, startTime, endTime, f, size);
		Long count=searchClient.countByMultigroupTags(mustTagList,shouldTagList ,mustNotTagList, startTime, endTime);
		
		SearchData sd = new SearchData(count, newsList);
		return convertSearchDataToString(sd);
	}
	
	@Path("/searchByORWebsiteIDs")
	@POST
	public String searchByORWebsiteIDs(@FormParam("websiteIDs") String websiteIDs,
			@FormParam("startTime") Long startTime,
			@FormParam("endTime") Long endTime, @FormParam("pageSize") String pageSize,
			@FormParam("from") String from){
		
		SubSearchClient searchClient = new SubSearchClient();
		Integer size = 20;
		Integer f = 0;
		try {
			if ((startTime != null && endTime == null) || (startTime == null && endTime != null)) {
				return SearchErrorInfo.TIMEARGS;
			}
			if ((startTime!=null&&endTime!=null)&&(startTime >= endTime)) {
				return SearchErrorInfo.TIMEORDERARGS;
			}
			if (websiteIDs == null) {
				return SearchErrorInfo.ILLEGALARGS;
			}
			if (pageSize != null) {
				size = Integer.valueOf(pageSize);
			}
			if (from != null) {
				f = Integer.valueOf(from);
			}
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			logger.info("按照分类搜索,searchByORTags," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}
		
		String[] arraywebsiteIDs = websiteIDs.split("_");
		List<Integer> websiteIDList = convertArrayToIntList(arraywebsiteIDs);
		
		List<SearchNews>newsList =searchClient.searchByORWebsiteIDs(websiteIDList, startTime, endTime, f, size);
		Long count=searchClient.countByORWebsiteIDs(websiteIDList, startTime, endTime, f, size);
		SearchData sd = new SearchData(count, newsList);
		return convertSearchDataToString(sd);
	}
	
	
	@Path("/searchByANDWebsiteIDs")
	@POST
	public String searchByANDWebsiteIDs(@FormParam("websiteIDs") String websiteIDs,
			@FormParam("startTime") Long startTime,
			@FormParam("endTime") Long endTime, @FormParam("pageSize") String pageSize,
			@FormParam("from") String from){
		
		SubSearchClient searchClient = new SubSearchClient();
		Integer size = 20;
		Integer f = 0;
		try {
			if ((startTime != null && endTime == null) || (startTime == null && endTime != null)) {
				return SearchErrorInfo.TIMEARGS;
			}
			if ((startTime!=null&&endTime!=null)&&(startTime >= endTime)) {
				return SearchErrorInfo.TIMEORDERARGS;
			}
			if (websiteIDs == null) {
				return SearchErrorInfo.ILLEGALARGS;
			}
			if (pageSize != null) {
				size = Integer.valueOf(pageSize);
			}
			if (from != null) {
				f = Integer.valueOf(from);
			}
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			logger.info("按照分类搜索,searchByORTags," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}
		String[] arraywebsiteIDs = websiteIDs.split("_");
		List<Integer> websiteIDList = convertArrayToIntList(arraywebsiteIDs);
		
		List<SearchNews>newsList =searchClient.searchByANDWebsiteIDs(websiteIDList, startTime, endTime, f, size);
		Long count=searchClient.countByANDWebsiteIDs(websiteIDList, startTime, endTime, f, size);
		SearchData sd = new SearchData(count, newsList);
		return convertSearchDataToString(sd);
	}
	
	@Path("/searchByOneTagORWebsiteIDs")
	@POST
	public String searchByOneTagORWebsiteIDs(@FormParam("tagName") String tagName,
			@FormParam("websiteIDs") String websiteIDs,
			@FormParam("startTime") Long startTime,
			@FormParam("endTime") Long endTime, @FormParam("pageSize") String pageSize,
			@FormParam("from") String from){
		SubSearchClient searchClient = new SubSearchClient();
		Integer size = 20;
		Integer f = 0;
		try {
			if ((startTime != null && endTime == null) || (startTime == null && endTime != null)) {
				return SearchErrorInfo.TIMEARGS;
			}
			if ((startTime!=null&&endTime!=null)&&(startTime >= endTime)) {
				return SearchErrorInfo.TIMEORDERARGS;
			}
			if (websiteIDs == null) {
				return SearchErrorInfo.ILLEGALARGS;
			}
			if (tagName == null) {
				return SearchErrorInfo.ILLEGALARGS;
			}
			if (pageSize != null) {
				size = Integer.valueOf(pageSize);
			}
			if (from != null) {
				f = Integer.valueOf(from);
			}
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			logger.info("按照分类搜索,searchByORTags," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}
		String[] arraywebsiteIDs = websiteIDs.split("_");
		List<Integer> websiteIDList = convertArrayToIntList(arraywebsiteIDs);
		
		List<SearchNews>newsList =searchClient.searchByOneTagORWebsiteIDs(tagName, websiteIDList, startTime, endTime, f, size);
		Long count=searchClient.countByOneTagORWebsiteIDs(tagName, websiteIDList, startTime, endTime);
		SearchData sd = new SearchData(count, newsList);
		return convertSearchDataToString(sd);
	}
	
	@Path("/searchByOneTagANDWebsiteIDs")
	@POST
	public String searchByOneTagANDWebsiteIDs(@FormParam("tagName") String tagName,
			@FormParam("websiteIDs") String websiteIDs,
			@FormParam("startTime") Long startTime,
			@FormParam("endTime") Long endTime, @FormParam("pageSize") String pageSize,
			@FormParam("from") String from){
		SubSearchClient searchClient = new SubSearchClient();
		Integer size = 20;
		Integer f = 0;
		try {
			if ((startTime != null && endTime == null) || (startTime == null && endTime != null)) {
				return SearchErrorInfo.TIMEARGS;
			}
			if ((startTime!=null&&endTime!=null)&&(startTime >= endTime)) {
				return SearchErrorInfo.TIMEORDERARGS;
			}
			if (websiteIDs == null) {
				return SearchErrorInfo.ILLEGALARGS;
			}
			if (tagName == null) {
				return SearchErrorInfo.ILLEGALARGS;
			}
			if (pageSize != null) {
				size = Integer.valueOf(pageSize);
			}
			if (from != null) {
				f = Integer.valueOf(from);
			}
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			logger.info("按照分类搜索,searchByORTags," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}
		String[] arraywebsiteIDs = websiteIDs.split("_");
		List<Integer> websiteIDList = convertArrayToIntList(arraywebsiteIDs);
		
		List<SearchNews>newsList =searchClient.searchByOneTagANDWebsiteIDs(tagName, websiteIDList, startTime, endTime, f, size);
		Long count=searchClient.countByOneTagANDWebsiteIDs(tagName, websiteIDList, startTime, endTime);
		SearchData sd = new SearchData(count, newsList);
		return convertSearchDataToString(sd);
	}
	
	@Path("/searchByOneTagNOTWebsiteIDs")
	@POST
	public String searchByOneTagNOTWebsiteIDs(@FormParam("tagName") String tagName,
			@FormParam("websiteIDs") String websiteIDs,
			@FormParam("startTime") Long startTime,
			@FormParam("endTime") Long endTime, @FormParam("pageSize") String pageSize,
			@FormParam("from") String from){
		SubSearchClient searchClient = new SubSearchClient();
		Integer size = 20;
		Integer f = 0;
		try {
			if ((startTime != null && endTime == null) || (startTime == null && endTime != null)) {
				return SearchErrorInfo.TIMEARGS;
			}
			if ((startTime!=null&&endTime!=null)&&(startTime >= endTime)) {
				return SearchErrorInfo.TIMEORDERARGS;
			}
			if (websiteIDs == null) {
				return SearchErrorInfo.ILLEGALARGS;
			}
			if (tagName == null) {
				return SearchErrorInfo.ILLEGALARGS;
			}
			if (pageSize != null) {
				size = Integer.valueOf(pageSize);
			}
			if (from != null) {
				f = Integer.valueOf(from);
			}
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			logger.info("按照分类搜索,searchByORTags," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}
		String[] arraywebsiteIDs = websiteIDs.split("_");
		List<Integer> websiteIDList = convertArrayToIntList(arraywebsiteIDs);
		
		List<SearchNews>newsList =searchClient.searchByOneTagNOTWebsiteIDs(tagName, websiteIDList, startTime, endTime, f, size);
		Long count=searchClient.countByOneTagNOTWebsiteIDs(tagName, websiteIDList, startTime, endTime);
		SearchData sd = new SearchData(count, newsList);
		return convertSearchDataToString(sd);
	}
	
	private List<Integer> convertArrayToIntList(String[] arr) {
		List<Integer> list = new ArrayList<Integer>();
		for (String s : arr) {
			list.add(Integer.valueOf(s));
		}
		return list;
	}
	
	private List<String> convertArrayToList(String[] arr) {
		List<String> list = new ArrayList<String>();
		for (String s : arr) {
			list.add(s);
		}
		return list;
	}

	private String convertSearchDataToString(SearchData sd) {
		Gson gson = new Gson();
		return gson.toJson(sd);
	}

}
