package com.unbank.es.document;

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

public class DocumentDeleteClient {
	
	
	private static final Logger logger = LoggerFactory.getLogger(DocumentDeleteClient.class); 

	private String indexName=SysParameters.DOCUMENTINDEX;
	private String type=SysParameters.DOCUMENTTYPE;
	
	public boolean delete(String id){
		logger.info("文档库：依据ID删除相应的索引，id:"+id);
		
		DeleteRequestBuilder deleteRequestBuilder = ESUtils.getClient().prepareDelete(indexName, type, id);
		DeleteResponse deleteResponse = deleteRequestBuilder.execute().actionGet();
		return deleteResponse.isFound();
	}

	public String delete(List<String> ids){
		logger.info("文档库：依据ID删除相应的索引，id:"+ids);
		Client client = ESUtils.getClient();
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
