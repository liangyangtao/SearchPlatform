package com.unbank.es.query;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.NotFilterBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.ESUtils;

public class MoreLikeTest {
	public static void main(String[] args) {
		Client client = ESUtils.getClient();
		
		GetResponse getResponse = client.prepareGet(SysParameters.DOCUMENTINDEX, SysParameters.DOCUMENTTYPE, "webword_14").get();
		Object journalId = getResponse.getSource().get("articleJournalId");
		
		MoreLikeThisQueryBuilder moreLike = QueryBuilders.moreLikeThisQuery("articleFormat").likeText("doc");
		moreLike.minTermFreq(1).minDocFreq(1);
		
		
		// 过滤器
		NotFilterBuilder notFilter = FilterBuilders.notFilter(
				FilterBuilders.queryFilter(
						QueryBuilders.termQuery("articleJournalId", journalId)));
		
		
		FilteredQueryBuilder filteredQueryBuilder = QueryBuilders.filteredQuery(moreLike, notFilter);
		
		
		SearchResponse response = client.prepareSearch(SysParameters.DOCUMENTINDEX)
				.setTypes(SysParameters.DOCUMENTTYPE)
				.setQuery(filteredQueryBuilder)
				.get();
		System.out.println(response.getHits().getTotalHits());
		
		
	}
}
