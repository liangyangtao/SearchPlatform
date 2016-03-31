package com.unbank.es.query;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.unbank.common.constants.ServerConfig;
import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.ESUtils;

public class QueryDocInsert {
	
	
	public static void main(String[] args) {
		
		Client client = ESUtils.getClusterClient(ServerConfig.getClusterName() , true, 9300, "10.0.2.152");
		
		SearchHits hits_152 = client
				.prepareSearch(SysParameters.DOCUMENTINDEX)
				.setTypes(SysParameters.DOCUMENTTYPE)
				.setQuery(QueryBuilders.matchAllQuery())
				.setFrom(0).setSize(200)
				.get().getHits();
		 
		System.out.println(hits_152.getHits().length);
		
		TransportClient tempClient = ESUtils.getClusterClient(ServerConfig.getClusterName() , true, 9300, "localhost");
		for (SearchHit hit : hits_152.getHits()) {
			tempClient.prepareIndex(SysParameters.DOCUMENTINDEX, SysParameters.DOCUMENTTYPE).setSource(hit.getSource()).get();
		}
		
	}
}
