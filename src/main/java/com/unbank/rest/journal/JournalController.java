package com.unbank.rest.journal;

import static com.unbank.common.constants.CommonConstants.ERROR;
import static com.unbank.common.constants.CommonConstants.PARAMERROR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unbank.common.utils.CommonUtils;
import com.unbank.common.utils.GsonUtil;
import com.unbank.db.mybatis.dao.wordresource.WordResource;
import com.unbank.db.mybatis.dao.wordresource.WordResourceDao;
import com.unbank.es.journal.Journal;
import com.unbank.es.journal.JournalParam;
import com.unbank.es.journal.JournalSearchData;
import com.unbank.es.journal.JournalService;

/**
 * 期刊对外接口
 * @author LiuLei
 *
 */
@Component
@Path("/journal")
public class JournalController {
	private static final Logger logger = LoggerFactory.getLogger(JournalController.class);
	
	@Autowired
	private WordResourceDao wordResourceDao;
	
	private JournalService journalService = new JournalService();
	
	/**
	 * 添加索引信息
	 * @param jsonData
	 * @return
	 */
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/index")
	public String index(@FormParam("jsonData") String jsonData){
		Map<String, String> map = new HashMap<String, String>();
		// 参数判断
        if (CommonUtils.isEmpty(jsonData)) {
            logger.error("添加期刊索引信息,------" + PARAMERROR);
            CommonUtils.putMap(map, ERROR, PARAMERROR, null);
            return GsonUtil.getJsonStringFromObject(map);
        }
        
        // 转换json数据
        Journal journal = GsonUtil.getObjectFromJsonStr(jsonData, Journal.class);
        logger.info("添加期刊索引信息,index,begin,journalId:" + journal.getJournalId());
		return journalService.index(journal);
	}
	
	/**
	 * 更新索引信息
	 * @param jsonData
	 * @return
	 */
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/update")
	public String  update(@FormParam("jsonData") String jsonData){
		Map<String, String> map = new HashMap<String, String>();

		// 参数判断
		if (CommonUtils.isEmpty(jsonData)) {
			logger.error("修改期刊索引信息,------" + PARAMERROR);
			CommonUtils.putMap(map, ERROR, PARAMERROR, null);
			return GsonUtil.getJsonStringFromObject(map);
		}

        // 转换json数据
        Journal journal = GsonUtil.getObjectFromJsonStr(jsonData, Journal.class);
        logger.info("修改期刊索引信息,update,begin,journalId:" + journal.getJournalId());
		return journalService.update(journal);
	}

	/**
	 * 删除单条（根据[journalID]）
	 * @param journalId
	 * @return
	 */
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/deleteByJournalId")
	public String deleteByJournalId(@FormParam("journalId") int journalId){
		Map<String, String> map = new HashMap<String, String>();
		// 参数判断
        if (journalId == 0) {
            logger.error("依据期刊ID删除期刊索引信息,------" + PARAMERROR);
            CommonUtils.putMap(map, ERROR, PARAMERROR, null);
            return GsonUtil.getJsonStringFromObject(map);
        }

        logger.info("期刊库：依据期刊ID删除索引,deleteByJournalId,begin,journalId:" + journalId);
		return journalService.deleteByJournalId(journalId);
	}
	
	/**
	 * 删除多条（根据[多个journalID]）
	 * @param ids
	 * @return
	 */
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/deleteByIds")
	public String deleteByIds(@FormParam("ids") String ids){
		Map<String, String> map = new HashMap<String, String>();

		// 参数判断
		if(CommonUtils.isEmpty(ids)){
			logger.error("批量删除期刊索引信息,------" + PARAMERROR);
            CommonUtils.putMap(map, ERROR, PARAMERROR, null);
            return GsonUtil.getJsonStringFromObject(map);
		}
		
		List<String> journalIds = GsonUtil.getListFromJson(ids);

        logger.info("期刊库：批量删除索引,ids:" + journalIds);
		
		return journalService.deleteByIds(journalIds);
	}
	
	/**
	 * 查询单条
	 * @param journalId
	 * @return
	 */
	@POST
	@Path("/searchByJournalId")
	public String searchByJournalId(@FormParam("journalId") int journalId){
		Map<String, String> map = new HashMap<String, String>();
		// 参数判断
		if(journalId == 0){
			logger.error("依据期刊ID搜索期刊索引信息,------" + PARAMERROR);
            CommonUtils.putMap(map, ERROR, PARAMERROR, null);
            return GsonUtil.getJsonStringFromObject(map);
		}

        logger.info("期刊库：依据期刊ID搜索期刊索引信息,journalId:" + journalId);
		return journalService.searchByJourlnalId(journalId);
	}


	/**
	 * 根据QueryString搜索
	 * @param searchParamJson
	 * @return
	 */
	@POST
	@Path("/searchJournalByQueryString")
	public String searchJournalBySearchData(@FormParam("searchParamJson") String searchParamJson) {
		Map<String, String> map = new HashMap<String, String>();
		if(CommonUtils.isEmpty(searchParamJson)){
            CommonUtils.putMap(map, ERROR, PARAMERROR, null);
            return GsonUtil.getJsonStringFromObject(map);
		}
		JournalParam journalParam;
		try {
			journalParam = GsonUtil.getObjectFromJsonStr(searchParamJson, JournalParam.class);
		} catch (Exception e) {
			CommonUtils.putMap(map, ERROR, PARAMERROR, null);
            return GsonUtil.getJsonStringFromObject(map);
		}
		
		List<JournalSearchData> searchData = journalParam.getJsonData();
        logger.info("期刊库：依据期刊queryString搜索期刊索引信息," + searchParamJson);
		return journalService.searchJournalBySearchData(searchData, journalParam.getFrom(), journalParam.getPageSize(), 
				journalParam.getOrderByField(), journalParam.getOrder());
	}

	/**
	 * 根据栏目搜索
	 * @param searchParamJson
	 * @return
	 */
	@POST
	@Path("/searchJournalByColumn")
	public String searchJournalByColumn(@FormParam("searchParamJson") String searchParamJson){
		Map<String, String> map = new HashMap<String, String>();
		if(CommonUtils.isEmpty(searchParamJson)){
            CommonUtils.putMap(map, ERROR, PARAMERROR, null);
            return GsonUtil.getJsonStringFromObject(map);
		}
		
		JournalParam journalParam;
		try {
			journalParam = GsonUtil.getObjectFromJsonStr(searchParamJson,
					JournalParam.class);
		} catch (Exception e) {
			CommonUtils.putMap(map, ERROR, PARAMERROR, null);
			return GsonUtil.getJsonStringFromObject(map);
		}
		
		List<JournalSearchData> searchData = journalParam.getJsonData();

        logger.info("期刊库：根据栏目搜索期刊索引信息");
		
		return journalService.searchByColumn(searchData, journalParam.getTopList(), journalParam.getFrom(), 
				journalParam.getPageSize(), journalParam.getOrderByField(), journalParam.getOrder());
	}
	
	/**
	 * 用户ID搜索栏目期刊
	 * @param searchParamJson
	 * @return
	 */
	@POST
	@Path("/searchJournalByUserId")
	public String searchJournalByUserId(@FormParam("searchParamJson") String searchParamJson, @FormParam("userId") int userId){
		Map<String, String> map = new HashMap<String, String>();
		if(CommonUtils.isEmpty(searchParamJson)){
            CommonUtils.putMap(map, ERROR, PARAMERROR, null);
            return GsonUtil.getJsonStringFromObject(map);
		}
		
		JournalParam journalParam;
		try {
			journalParam = GsonUtil.getObjectFromJsonStr(searchParamJson, JournalParam.class);
		} catch (Exception e) {
			CommonUtils.putMap(map, ERROR, PARAMERROR, null);
			return GsonUtil.getJsonStringFromObject(map);
		}
		
		List<JournalSearchData> searchData = journalParam.getJsonData();
		
		List<String> journalIds = null;
		if(userId > 0){
			journalIds = new ArrayList<String>();
			// 查询用户资源
			List<WordResource> result = wordResourceDao.queryByUserId(userId);
			if(CommonUtils.isNotEmpty(result)){
				for (WordResource wordResource : result) {
					journalIds.add(String.valueOf(wordResource.getJournalId()));
				}
				journalParam.setTopList(journalIds);
			}
		}

        logger.info("期刊库：根据栏目搜索期刊索引信息");
		
		return journalService.searchByColumns(searchData, journalParam.getTopList(), journalParam.getFrom(), 
				journalParam.getPageSize(), journalParam.getOrderByField(), journalParam.getOrder());
	}

}
