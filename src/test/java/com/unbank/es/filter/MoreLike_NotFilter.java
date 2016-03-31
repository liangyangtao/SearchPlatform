package com.unbank.es.filter;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.mlt.MoreLikeThisRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.NotFilterBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.unbank.common.constants.CommonConstants;
import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.ESUtils;

public class MoreLike_NotFilter {
	
	public static void main(String[] args) {
		
		String index = SysParameters.DOCUMENTINDEX;
		String type = "document";
		
		Client client = ESUtils.getClient();
		
		GetResponse getResponse = client
				.prepareGet(index, type, "webword_19")
				.get();
		Object journalId = getResponse.getSource().get("articleJournalId");
		
//		System.out.println(journalId);
		
		MoreLikeThisRequestBuilder moreLikeThisRequestBuilder = new MoreLikeThisRequestBuilder(client, index, type, "webword_19");
		// 匹配的字段
		moreLikeThisRequestBuilder.setField("articleContent", "articleName", "articleKeyWord")
				// 匹配文档中的最低词频，低于此频率的词条将被忽略，默认值为：2
				.setMinTermFreq(1)
				// 包含词条的文档最小数目，低于此数目时，该词条将被忽视，默认值为：5，意味着一个词条至少应该出现在5个文档中，才不会被忽略
				.setMinDocFreq(1);
		
		
//		client.moreLikeThis(moreLikeThisRequestBuilder.)
		
		// 过滤器
		NotFilterBuilder notFilter = FilterBuilders.notFilter(FilterBuilders.queryFilter(QueryBuilders.termQuery("articleJournalId", journalId)));
		// moreLike
		MoreLikeThisQueryBuilder queryBuilder = QueryBuilders.moreLikeThisQuery("articleContent", "articleName", "articleKeyWord").likeText("哈哈");
		
		FilteredQueryBuilder filteredQueryBuilder = QueryBuilders.filteredQuery(queryBuilder, notFilter);
		
		System.out.println(filteredQueryBuilder.toString());
		
		SearchResponse response = client.prepareSearch(CommonConstants.INDEX_JOURNAL)
				.setTypes(CommonConstants.TYPE_JOURNAL)
				.setQuery(filteredQueryBuilder)
				.setPostFilter(notFilter)
				.get();
		
		
		System.out.println(response.getHits().getTotalHits());
		
//		System.out.println(response.getHits().getTotalHits());
		
		
		
		
	}
}
