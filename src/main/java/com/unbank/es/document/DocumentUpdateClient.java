package com.unbank.es.document;

import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.CommonUtils;
import com.unbank.common.utils.ESUtils;
import com.unbank.es.search.SearchESClientFactory;

public class DocumentUpdateClient {
	
	private static final Logger logger = LoggerFactory.getLogger(DocumentUpdateClient.class);
	private static final String INDEXNAME = SysParameters.DOCUMENTINDEX;
	private static final String TYPE=SysParameters.DOCUMENTTYPE;
	
	public boolean update(Document document){
		UpdateRequestBuilder updateRequestBuilder= SearchESClientFactory.getClient().prepareUpdate(INDEXNAME, TYPE, ESUtils.getIdOfLucene(document));
		
		updateRequestBuilder.setDoc(document.toJsonString());
		
		try {
			updateRequestBuilder.execute().actionGet();
		} catch (Exception e) {
			logger.error(CommonUtils.getExceptionMessage(e));
			return false;
		}
		return true;
	}
	
}
