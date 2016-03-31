package com.unbank.es.risk;

import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.CommonUtils;
import com.unbank.entity.risk.Risk;
import com.unbank.es.search.SearchESClientFactory;

public class RiskUpdateClient {
	
	private static final Logger logger = LoggerFactory.getLogger(RiskUpdateClient.class);
	private static final String INDEXNAME = SysParameters.RISKINDEX;
	private static final String TYPE=SysParameters.RISKTYPE;
	
	public boolean update(String id, Risk risk){
		UpdateRequestBuilder updateRequestBuilder= SearchESClientFactory.getClient().prepareUpdate(INDEXNAME, TYPE, id);
		
		updateRequestBuilder.setDoc(risk.toJsonString());
		
		try {
			updateRequestBuilder.execute().actionGet();
		} catch (Exception e) {
			logger.error(CommonUtils.getExceptionMessage(e));
			return false;
		}
		return true;
	}
	
}
