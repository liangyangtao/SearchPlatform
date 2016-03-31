package com.unbank.es.update.news;

import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.CommonUtils;
import com.unbank.entity.News;
import com.unbank.es.search.SearchESClientFactory;

public class NewsUpdateClient {
	
	private static final Logger logger = LoggerFactory.getLogger(NewsUpdateClient.class);
	private static final String INDEXNAME = SysParameters.INDEXNAME;
	private static final String TYPE=SysParameters.DATATYPE;
	
	public boolean update(String id, News news){
		UpdateRequestBuilder updateRequestBuilder= SearchESClientFactory.getClient().prepareUpdate(INDEXNAME, TYPE, id);
		
		updateRequestBuilder.setDoc(news.toJsonString());
		
		try {
			updateRequestBuilder.execute().actionGet();
		} catch (Exception e) {
			logger.error(CommonUtils.getExceptionMessage(e));
			return false;
		}
		return true;
	}
	
}
