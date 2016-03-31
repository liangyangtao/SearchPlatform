package com.unbank.es.news;

import java.io.IOException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHits;

import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.ESUtils;

public class Test_ESClient {

//	@Test
	public void testSearch() throws IOException {
		
		Client client = new TransportClient() .addTransportAddress(new InetSocketTransportAddress(
				"localhost", 9300));
		
		QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery("工行这只是一个测试标题").field("title").minimumShouldMatch("100%");
		
		
		SearchHits hits = ESUtils.search(client, SysParameters.INDEXNAME, SysParameters.DATATYPE, queryBuilder, null, 0, 100);
		
		
		
		if(hits.totalHits() > 0){
			
			System.out.println(hits.getHits().length);
			
		}
		
		// TODO 测试结果，需要添加一个标记为重复的索引字段  !!
		
		
	}
	
	
}
