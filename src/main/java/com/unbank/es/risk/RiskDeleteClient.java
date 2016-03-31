package com.unbank.es.risk;

import java.util.List;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.ESUtils;

public class RiskDeleteClient {
	
	
	private static final Logger logger = LoggerFactory.getLogger(RiskDeleteClient.class); 

	private String indexName=SysParameters.RISKINDEX;
	private String type=SysParameters.RISKTYPE;
	
	public boolean delete(String id){
		logger.info("风险库：依据ID删除（单篇）相应的索引，id:"+id);
		logger.info("风险库：依据ID删除（单篇）相应的索引，indexName:"+indexName);
		logger.info("风险库：依据ID删除（单篇）相应的索引，type:"+type);
		
		DeleteRequestBuilder deleteRequestBuilder = ESUtils.getClient().prepareDelete(indexName, type, id);
		DeleteResponse deleteResponse = deleteRequestBuilder.execute().actionGet();
		return deleteResponse.isFound();
		
	}

	public boolean delete(String id, List<String> extraTag){
		logger.info("风险库：依据ID删除（单篇）相应的索引，根据crawl_id 关联更新新闻库，id:"+id);
		logger.info("风险库：依据ID删除（单篇）相应的索引，根据crawl_id 关联更新新闻库，indexName:"+indexName);
		logger.info("风险库：依据ID删除（单篇）相应的索引，根据crawl_id 关联更新新闻库，type:"+type);
		
		DeleteRequestBuilder deleteRequestBuilder = ESUtils.getClient().prepareDelete(indexName, type, id);
		DeleteResponse deleteResponse = deleteRequestBuilder.execute().actionGet();
		return deleteResponse.isFound();
		
		//根据id得到wrab_id
	}
	
	public String delete(List<String> ids){
		logger.info("风险库：依据ID删除相应的索引，id:"+ids);
		logger.info("风险库：依据ID删除相应的索引，indexName:"+indexName);
		logger.info("风险库：依据ID删除相应的索引，type:"+type);
		Client client=ESUtils.getClient();
		BulkRequestBuilder bulkRequestBuilder=	client.prepareBulk();
		for(String id:ids){
			bulkRequestBuilder.add(client.prepareDelete(indexName, type, id));
		}
		BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
		
		StringBuffer accumulator=new StringBuffer();
		
		if(bulkResponse.hasFailures()){
			BulkItemResponse[] responses= bulkResponse.getItems();
			for(BulkItemResponse response:responses){
				if(response.isFailed()){
					accumulator.append(response.getFailure().getId()+" ");
				}
			}
		}
		
		return  bulkResponse.hasFailures()?String.valueOf(accumulator):"SUCCESS";
	}
	
	
}
