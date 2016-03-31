package com.unbank.es.journal;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.unbank.common.constants.CommonConstants;
import com.unbank.common.utils.ESUtils;

public class JournalTest {

	public static void main(String[] args) {
		Client client = ESUtils.getClient();
        
		SearchResponse response = client.prepareSearch(CommonConstants.INDEX_JOURNAL).setTypes(CommonConstants.TYPE_JOURNAL)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setFrom(0)
				.setSize(200)
				.setQuery(QueryBuilders.termQuery("journalId", 156))
				.get();
		SearchHits hits = response.getHits();
		
		SearchHit hit = hits.getHits()[0];
		hit.getSource().put("haveData", 1);
		hit.getSource().put("updateTime", System.currentTimeMillis());
		
		System.out.println(hit.getSource());
		
		
//		for (SearchHit searchHit : hits.getHits()) {
//			System.out.println("journalId:" + searchHit.getSource().get("journalId") + "\t" + "haveData:" + searchHit.getSource().get("haveData"));
//		}
		
	}

}
