package com.unbank.es.risk;

import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.unbank.common.constants.ServerConfig;
import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.ESUtils;

public class Query_Update {
	
	
	public static void main(String[] args) {

		TransportClient client = ESUtils.getClusterClient(ServerConfig.getClusterName(), true, 9300, "localhost");

		SearchResponse re = client.prepareSearch(SysParameters.RISKINDEX)
				.setTypes(SysParameters.RISKTYPE)
				.setQuery(QueryBuilders.termQuery("repetitive", "0"))
				.setFrom(0).setSize(100).get();

		SearchHits hits = re.getHits();
		
		System.out.println(hits.getTotalHits());
		
		for (SearchHit searchHit : hits.getHits()) {
			Map<String, Object> source = searchHit.getSource();
			source.put("repetitive", "false");
			
			System.out.println(source.get("title"));
			
			client.prepareUpdate(SysParameters.RISKINDEX, SysParameters.RISKTYPE, searchHit.getId()).setDoc(source).get();
		}

	}
}
