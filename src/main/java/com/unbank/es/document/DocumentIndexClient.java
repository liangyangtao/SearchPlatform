package com.unbank.es.document;

import static com.unbank.common.constants.CommonConstants.ERROR;
import static com.unbank.common.constants.CommonConstants.SUCCESS;

import java.util.Map;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unbank.common.constants.CommonConstants;
import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.CommonUtils;
import com.unbank.common.utils.ESUtils;
import com.unbank.common.utils.GsonUtil;


public class DocumentIndexClient {
	
	private static final Logger logger = LoggerFactory.getLogger(DocumentIndexClient.class); 

	/**
	 * 索引名称
	 */
	private String INDEX = SysParameters.DOCUMENTINDEX;
	/**
	 * 索引类型
	 */
	private String INDEX_TYPE = SysParameters.DOCUMENTTYPE;
	
	public String index(Document document){
		String tempESID = document.getArticleProject() + "_"
				+ document.getArticleId();
		IndexRequestBuilder indexRequestBuilder = ESUtils.getClient().prepareIndex(INDEX, INDEX_TYPE, tempESID);
		indexRequestBuilder.setSource(document.toJsonString());
		logger.info("开始建立Document索引,id:" + tempESID);
		try {
			IndexResponse response= indexRequestBuilder.execute().actionGet();
			String esId = response.getId();
			logger.info("建立Document索引完成,index,end,id:" + tempESID + ",\t status:" + CommonConstants.SUCCESS + ",\t ESID:" + esId);
			return  esId;
		} catch (Exception e) {
			logger.error("建立Document索引失败,index,end,id:" + tempESID + ",\t status:" + CommonConstants.ERROR);
			logger.error(CommonUtils.getExceptionMessage(e));
			return null;
		}
	}

	public String indexByJournalId(Document doc, Map<String, String> map) {
		String tempId = ESUtils.getIdOfLucene(doc);
		logger.info("开始建立Document索引,id:" + tempId);
		
		Client client = ESUtils.getClient();
		try {
			// 添加索引信息
			IndexResponse response = client.prepareIndex(INDEX, INDEX_TYPE, ESUtils.getIdOfLucene(doc))
					.setSource(doc.toJsonString())
					.get();
			
			String esId = response.getId();
			
			logger.info("建立Document索引完成,index,end,id:" + tempId + ",\t status:" + CommonConstants.SUCCESS + ",\t ESID:" + esId);
			
			CommonUtils.putMap(map, SUCCESS, "添加索引成功", esId);
			return GsonUtil.getJsonStringFromObject(map);
		} catch (Exception e) {
			logger.error("建立Document索引失败,index,end,id:" + tempId + ",\t status:" + CommonConstants.ERROR);
			logger.error(CommonUtils.getExceptionMessage(e));
            CommonUtils.putMap(map, ERROR, "添加索引信息失败", null);
            return GsonUtil.getJsonStringFromObject(map);
		}
		
	}
	
}
