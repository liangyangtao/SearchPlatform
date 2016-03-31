package com.unbank.es.query;

import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;

import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.ESUtils;

/**
 * QueryBuilder 测试类
 * 
 * @author Administrator
 * 
 */
public class QueryTest {

	private static String index = SysParameters.DOCUMENTINDEX;

	private static String type = SysParameters.DOCUMENTTYPE;

	public static void main(String[] args) {
		Client client = ESUtils.getClient();

		QueryBuilder query = QueryBuilders.matchPhraseQuery("articleContent", "发布");
		
		SearchRequestBuilder b = client.prepareSearch(index).setTypes(type)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(query)
				.addHighlightedField("articleContent", 500);
		
		
		SearchResponse response = b.get();
		SearchHits hits = response.getHits();

		System.out.println("数据总数：" + hits.getTotalHits());
		for (SearchHit searchHit : hits) {
			Map<String, HighlightField> highlightMap = searchHit.getHighlightFields();
			HighlightField hs = highlightMap.get("articleContent");
			System.out.println(hs.getFragments().length);
			if(hs.getFragments().length > 0){
				System.out.println(hs);
			}
			
//			System.out.println(searchHit.getSource().get("articleContent"));
		}

	}
}
