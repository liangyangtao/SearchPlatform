package com.unbank.rest.document;

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
import com.unbank.common.constants.CommonConstants;
import com.unbank.common.entity.ResponseParam;
import com.unbank.common.utils.CommonUtils;
import com.unbank.common.utils.GsonUtil;
import com.unbank.es.document.Document;
import com.unbank.es.document.DocumentSearchClient;
import com.unbank.es.document.DocumentSearchCondition;
import com.unbank.es.document.GroupParam;
import com.unbank.es.document.SearchDocumentData;
import com.unbank.es.search.SearchErrorInfo;


@Component
@Path("/document")
public class DocumentSearchService {
	
	private static final Logger logger = LoggerFactory.getLogger(DocumentSearchService.class);
	
	private DocumentSearchClient client = new DocumentSearchClient();
	
	@Path("/getDocumentById")
	@POST
	public String getDocumentById(@FormParam("id") String id,
			@FormParam("withContent") boolean withContent) {
		
		if (CommonUtils.isEmpty(id)) {
			return SearchErrorInfo.ILLEGALARGS;
		}
		
		logger.info("文档库：getDocumentById：搜索id" + id);
		System.out.println("文档库：getDocumentById：搜索id" + id);
		
		Document doc = client.getDocumentById(id);
		
		return doc == null ? null : doc.toJsonString(withContent);
	}
	
	@Path("/getDocument")
	@POST
	public String getDocument(@FormParam("articleId") int articleId,
			@FormParam("articeProject") String articeProject,
			@FormParam("withContent") boolean withContent) {

		if (articleId == 0 || CommonUtils.isEmpty(articeProject)) {
			return SearchErrorInfo.ILLEGALARGS;
		}

		logger.info("文档库：getDocument：搜索articleId:" + articleId + "		articeProject:" + articeProject);

		Document doc = client.getDocumentByIds(articleId, articeProject);
		
		return doc == null ? null : doc.toJsonString(withContent);
	}
	
	@Path("/searchByJsonStr")
	@POST
	public String searchByJsonStr(@FormParam("jsonStr") String jsonStr,
			@FormParam("from") String from,
			@FormParam("pageSize") String pageSize,
			@FormParam("orderByField") String orderByField,
			@FormParam("order") String order,
			@FormParam("fullContent") boolean fullContent
			){
		
		logger.info("文档库：searchByJsonStr：搜索jsonStr"+jsonStr+"		from:"+from+"	pageSize"+pageSize+"	orderByField"+orderByField+"	order:"+order+"		fullContent:"+fullContent);
		
		Gson gson=new Gson();
		
		DocumentSearchCondition condition= gson.fromJson(jsonStr, DocumentSearchCondition.class);
		
		DocumentSearchClient client=new DocumentSearchClient();
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
		SearchDocumentData searchDocumentData = client.search(condition, f, size, orderByField, order);
		if(CommonUtils.isEmpty(searchDocumentData)){
			return null;
		}
		String resultStr=searchDocumentData.toJsonString(fullContent);
		return resultStr;
	}
	
	@Path("/searchByMultiJsonStr")
	@POST
	public String searchByMultiJsonStr(@FormParam("jsonStr") String jsonStr,
			@FormParam("filterJsonStr") String filterJsonStr,
			@FormParam("from") String from,
			@FormParam("pageSize") String pageSize,
			@FormParam("orderByField") String orderByField,
			@FormParam("order") String order,
			@FormParam("fullContent") boolean fullContent){
		
		logger.info("文档库：searchByMultiJsonStr：搜索jsonStr"+jsonStr+"		from:"+from+"	pageSize"+pageSize+"	orderByField"+orderByField+"	order:"+order+"		fullContent:"+fullContent);

		Gson gson=new Gson();
		
		ArrayList<DocumentSearchCondition> conditions=gson.fromJson(jsonStr, new TypeToken<List<DocumentSearchCondition>>() {  }.getType());
		
		DocumentSearchCondition filterCondition= null;
		if(filterJsonStr!=null){
			filterCondition=gson.fromJson(filterJsonStr, DocumentSearchCondition.class);
		}
		
		DocumentSearchClient client=new DocumentSearchClient();
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
			logger.info("文档库，searchByMultiJsonStr," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}
		
		SearchDocumentData searchDocumentData =  client.search(conditions, filterCondition, f, size, orderByField, order);
		if(CommonUtils.isEmpty(searchDocumentData)){
			return null;
		}
		String resultStr=searchDocumentData.toJsonString(fullContent);
		return resultStr;
	}
	
	@Path("/searchByQueryString")
	@POST
	public String searchByQueryString(
			@FormParam("queryString") String queryString,
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
		
		DocumentSearchCondition filterCondition= null;
		if(filterJsonStr!=null){
			filterCondition=gson.fromJson(filterJsonStr, DocumentSearchCondition.class);
		}
		
		DocumentSearchClient client=new DocumentSearchClient();
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
			logger.info("文档库，searchByQueryString," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}
		
		SearchDocumentData searchDocumentData = client.searchByString(queryString, boosts, filterCondition, f, size);
		if(CommonUtils.isEmpty(searchDocumentData)){
			return null;
		}
		String resultStr=searchDocumentData.toJsonString(fullContent);
		return resultStr;
	}
	
	@Path("/moreLikeSearch")
	@POST
	public String moreLikeSearch(		
			@FormParam("from") String from,
			@FormParam("pageSize") String pageSize,
			@FormParam("articleId") int articleId,
			@FormParam("articeProject") String articeProject,
			@FormParam("fullContent") boolean fullContent){
		
		if(articleId==0||CommonUtils.isEmpty(articeProject)){
			return SearchErrorInfo.ILLEGALARGS;
		}
		DocumentSearchClient client=new DocumentSearchClient();
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
			logger.info("文档库，moreLikeSearch," + SearchErrorInfo.ILLEGALARGS);
			// 参数错误
			return SearchErrorInfo.ILLEGALARGS;
		}
		
		String id=articeProject+"_"+articleId;
		SearchDocumentData searchDocumentData=client.searchMoreLike(id, f, size);
		// 数据不存在
		if(CommonUtils.isEmpty(searchDocumentData)){
			return null;
		}
		String resultStr=searchDocumentData.toJsonString();
		return resultStr;
	}
	
	
	/**
	 * 根据期刊Id查询文档信息
	 * @param searchParamJson
	 * @return
	 */
	@Path("/searchByJournalIds")
	@POST
	public String searchByJournalIds(@FormParam("searchParamJson") String searchParamJson){
		ResponseParam responseParam = new ResponseParam();
		// 是否搜索到数据
		if (CommonUtils.isEmpty(searchParamJson)) {
            responseParam.setStatus(CommonConstants.ERROR);
            responseParam.setMsg("参数错误");
            return GsonUtil.getJsonStringFromObject(responseParam);
        }
		GroupParam paramInfo = GsonUtil.getObjectFromJsonStr(searchParamJson, GroupParam.class);
		
		List<DocumentSearchCondition> conditions = paramInfo.getJsonData();
		
		DocumentSearchClient client = new DocumentSearchClient();
		String resultStr = client.searchByJournalIds(conditions, paramInfo.getFrom(), paramInfo.getPageSize(),
				paramInfo.getOrderByField(), paramInfo.getOrder(), paramInfo.isFullContent());

		return resultStr;
	}
	
	/**
	 * 查询文档信息并根据期刊ID分组
	 * @param searchParamJson
	 * @return
	 */
	@Path("/searchByGroup")
	@POST
	public String searchByGroup(@FormParam("searchParamJson") String searchParamJson) {
		ResponseParam responseParam = new ResponseParam();
		// 是否搜索到数据
		if (CommonUtils.isEmpty(searchParamJson)) {
            responseParam.setStatus(CommonConstants.ERROR);
            responseParam.setMsg("参数错误");
            return GsonUtil.getJsonStringFromObject(responseParam);
        }
		GroupParam paramInfo = GsonUtil.getObjectFromJsonStr(searchParamJson, GroupParam.class);
		List<DocumentSearchCondition> conditions = paramInfo.getJsonData();
		
		DocumentSearchClient client = new DocumentSearchClient();
		String resultStr = client.searchByGroup(conditions, paramInfo.getFrom(), paramInfo.getPageSize(),
				paramInfo.getOrderByField(), paramInfo.getOrder(), paramInfo.isFullContent());

		return resultStr;
	}
	
	/**
	 * 根据期刊ID搜索符合条件的文档信息
	 * @param searchParamJson
	 * @return
	 */
	@Path("/searchMoreByJournal")
	@POST
	public String searchMoreByJournal(@FormParam("searchParamJson") String searchParamJson) {
		
		GroupParam paramInfo = GsonUtil.getObjectFromJsonStr(searchParamJson, GroupParam.class);
		List<DocumentSearchCondition> conditions = paramInfo.getJsonData();
		
		ResponseParam responseParam = new ResponseParam();
		if(CommonUtils.isEmpty(conditions)){
			responseParam.setStatus(CommonConstants.ERROR);
			responseParam.setMsg("参数错误");
			return GsonUtil.getJsonStringFromObject(responseParam);
		}
		
		DocumentSearchCondition dsc = conditions.get(0);
		if(CommonUtils.isEmpty(dsc.getJournalIds())){
			responseParam.setStatus(CommonConstants.ERROR);
			responseParam.setMsg("期刊ID不允许空");
			return GsonUtil.getJsonStringFromObject(responseParam);
		}
		
		// 高亮关键字
		List<String> highlight = paramInfo.getHighlight();
		
		DocumentSearchClient client = new DocumentSearchClient();
		String resultStr = client.searchMoreByJournal(dsc, paramInfo.getFrom(), paramInfo.getPageSize(),
				paramInfo.getOrderByField(), paramInfo.getOrder(), highlight);

		return resultStr;
	}
	
}
