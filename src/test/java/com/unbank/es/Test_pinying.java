package com.unbank.es;

import java.io.IOException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unbank.entity.News;
import com.unbank.es.search.SearchESClientFactory;

public class Test_pinying {

	//@Test
	public void test1() {
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch("unbanknews");
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		// boolQueryBuilder.must(QueryBuilders.queryStringQuery("银行").field("title"));
		boolQueryBuilder.must(QueryBuilders.queryStringQuery("中国").field("title").analyzer("ik"));

		searchRequestBuilder.setQuery(boolQueryBuilder);
		searchRequestBuilder.setExplain(true);
		searchRequestBuilder.setFrom(0).setSize(10);

		SearchResponse response = searchRequestBuilder.execute().actionGet();
		Long count = response.getHits().getTotalHits();
		System.out.println(count);
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();

		for (SearchHit hit : hits) {
			String json = hit.getSourceAsString();
			ObjectMapper mapper = new ObjectMapper();
			try {
				News news = mapper.readValue(json, News.class);
				System.out.println(news.getTitle());
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
