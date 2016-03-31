package com.unbank.es.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unbank.common.constants.SysParameters;
import com.unbank.entity.ExcessSearchCondition;
import com.unbank.entity.MultiSearchCondition;
import com.unbank.entity.SearchCondition;
import com.unbank.entity.SearchCondition.OrderByMode;
import com.unbank.entity.SearchNews;

public class MultiConditionSearchClient {

	private static final Logger logger = LoggerFactory.getLogger(MultiConditionSearchClient.class);
	private static final String INDEXNAME = SysParameters.INDEXNAME;

	public List<SearchNews> searchByMultiCondition(MultiSearchCondition multiSearchCondition) {
		List<SearchNews> list = new ArrayList<SearchNews>();

		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		SearchCondition condition = multiSearchCondition.getSearchCondition();

		ExcessSearchCondition excessSearchCondition = multiSearchCondition.getExcessSearchCondition();
		
		if (condition.getMustTagNames() != null || condition.getMustNotTagNames() != null
				|| condition.getShouldTagNames() != null) {
			BoolQueryBuilder tagQueryBuilder = composeQueryBuilder(condition.getMustTagNames(),
					condition.getMustNotTagNames(), condition.getShouldTagNames(), "tagName");
			boolQueryBuilder.must(tagQueryBuilder);
		}
		if (condition.getMustContentWords() != null || condition.getMustNotContentWords() != null
				|| condition.getShouldContentWords() != null) {
			BoolQueryBuilder contentQueryBuilder = composeQueryBuilder(condition.getMustContentWords(),
					condition.getMustNotContentWords(), condition.getShouldContentWords(), "content");
			boolQueryBuilder.must(contentQueryBuilder);
		}
		if (condition.getMustTitleWords() != null || condition.getMustNotTitleWords() != null
				|| condition.getShouldTitleWords() != null) {
			BoolQueryBuilder titleQueryBuilder = composeQueryBuilder(condition.getMustTitleWords(),
					condition.getMustNotTitleWords(), condition.getShouldTitleWords(), "title");
			boolQueryBuilder.must(titleQueryBuilder);
		}
		if (condition.getWebsiteIDs() != null) {
			BoolQueryBuilder websiteIDQueryBuilder = composeIntQueryBuilder(null, null, condition.getWebsiteIDs(),
					"website_id");
			boolQueryBuilder.must(websiteIDQueryBuilder);
		}
		if (condition.getStartTime() != null && condition.getEndTime() != null) {
			BoolQueryBuilder timeQueryBuilder = composeTimeQueryBuilder(condition.getStartTime(),
					condition.getEndTime(), "newsDate");
			boolQueryBuilder.must(timeQueryBuilder);
		}

		// System.out.println(excessSearchCondition==null);

		if (excessSearchCondition != null) {
			BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
			if (excessSearchCondition.getMustContentWords() != null
					|| excessSearchCondition.getMustNotContentWords() != null
					|| excessSearchCondition.getShouldContentWords() != null) {
				BoolQueryBuilder contentQueryBuilder = composeQueryBuilder(excessSearchCondition.getMustContentWords(),
						excessSearchCondition.getMustNotContentWords(), excessSearchCondition.getShouldContentWords(),
						"content");
				boolQueryBuilder2.must(contentQueryBuilder);
			}
			if (excessSearchCondition.getMustTitleWords() != null
					|| excessSearchCondition.getMustNotTitleWords() != null
					|| excessSearchCondition.getShouldTitleWords() != null) {
				BoolQueryBuilder titleQueryBuilder = composeQueryBuilder(excessSearchCondition.getMustTitleWords(),
						excessSearchCondition.getMustNotTitleWords(), excessSearchCondition.getShouldTitleWords(),
						"title");
				boolQueryBuilder2.must(titleQueryBuilder);
			}
			searchRequestBuilder.setPostFilter(FilterBuilders.queryFilter(boolQueryBuilder2));
		}

		searchRequestBuilder.setSize(condition.getPageSize());
		searchRequestBuilder.setFrom(condition.getFrom());
		
		if (OrderByMode.TIME.equals(condition.getOrderByColumn())) {
			searchRequestBuilder.addSort("newsDate", SortOrder.DESC);
		}

		list = search(searchRequestBuilder, boolQueryBuilder, condition.getFrom(), condition.getPageSize());
		return list;
	}

	public Long countByMultiCondition(MultiSearchCondition multiSearchCondition) {
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.COUNT);
		SearchCondition condition = multiSearchCondition.getSearchCondition();
		ExcessSearchCondition excessSearchCondition = multiSearchCondition.getExcessSearchCondition();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (condition.getMustTagNames() != null || condition.getMustNotTagNames() != null
				|| condition.getShouldTagNames() != null) {
			BoolQueryBuilder tagQueryBuilder = composeQueryBuilder(condition.getMustTagNames(),
					condition.getMustNotTagNames(), condition.getShouldTagNames(), "tagName");
			boolQueryBuilder.must(tagQueryBuilder);
		}
		if (condition.getMustContentWords() != null || condition.getMustNotContentWords() != null
				|| condition.getShouldContentWords() != null) {
			BoolQueryBuilder contentQueryBuilder = composeQueryBuilder(condition.getMustContentWords(),
					condition.getMustNotContentWords(), condition.getShouldContentWords(), "content");
			boolQueryBuilder.must(contentQueryBuilder);
		}
		if (condition.getMustTitleWords() != null || condition.getMustNotTitleWords() != null
				|| condition.getShouldTitleWords() != null) {
			BoolQueryBuilder titleQueryBuilder = composeQueryBuilder(condition.getMustTitleWords(),
					condition.getMustNotTitleWords(), condition.getShouldTitleWords(), "title");
			boolQueryBuilder.must(titleQueryBuilder);
		}
		if (condition.getWebsiteIDs() != null) {
			BoolQueryBuilder websiteIDQueryBuilder = composeIntQueryBuilder(null, null, condition.getWebsiteIDs(),
					"website_id");
			boolQueryBuilder.must(websiteIDQueryBuilder);
		}
		if (condition.getStartTime() != null && condition.getEndTime() != null) {
			BoolQueryBuilder timeQueryBuilder = composeTimeQueryBuilder(condition.getStartTime(),
					condition.getEndTime(), "newsDate");
			boolQueryBuilder.must(timeQueryBuilder);
		}

		if (excessSearchCondition != null) {
			BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
			if (excessSearchCondition.getMustContentWords() != null
					|| excessSearchCondition.getMustNotContentWords() != null
					|| excessSearchCondition.getShouldContentWords() != null) {
				BoolQueryBuilder contentQueryBuilder = composeQueryBuilder(excessSearchCondition.getMustContentWords(),
						excessSearchCondition.getMustNotContentWords(), excessSearchCondition.getShouldContentWords(),
						"content");
				boolQueryBuilder2.must(contentQueryBuilder);
			}
			if (excessSearchCondition.getMustTitleWords() != null
					|| excessSearchCondition.getMustNotTitleWords() != null
					|| excessSearchCondition.getShouldTitleWords() != null) {
				BoolQueryBuilder titleQueryBuilder = composeQueryBuilder(excessSearchCondition.getMustTitleWords(),
						excessSearchCondition.getMustNotTitleWords(), excessSearchCondition.getShouldTitleWords(),
						"title");
				boolQueryBuilder2.must(titleQueryBuilder);
			}
			searchRequestBuilder.setPostFilter(FilterBuilders.queryFilter(boolQueryBuilder2));
		}

		searchRequestBuilder.setSize(condition.getPageSize());
		searchRequestBuilder.setFrom(condition.getFrom());

		if (OrderByMode.TIME.equals(condition.getOrderByColumn())) {
			searchRequestBuilder.addSort("newsDate", SortOrder.DESC);
		}
		searchRequestBuilder.setQuery(boolQueryBuilder);
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		return response.getHits().totalHits();
	}

	public List<SearchNews> searchByMultiCondition(SearchCondition condition) {
		List<SearchNews> list = new ArrayList<SearchNews>();

		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		if (condition.getMustTagNames() != null || condition.getMustNotTagNames() != null
				|| condition.getShouldTagNames() != null) {
			BoolQueryBuilder tagQueryBuilder = composeQueryBuilder(condition.getMustTagNames(),
					condition.getMustNotTagNames(), condition.getShouldTagNames(), "tagName");
			boolQueryBuilder.must(tagQueryBuilder);
		}
		if (condition.getMustContentWords() != null || condition.getMustNotContentWords() != null
				|| condition.getShouldContentWords() != null) {
			BoolQueryBuilder contentQueryBuilder = composeQueryBuilder(condition.getMustContentWords(),
					condition.getMustNotContentWords(), condition.getShouldContentWords(), "content");
			boolQueryBuilder.must(contentQueryBuilder);
		}
		if (condition.getMustTitleWords() != null || condition.getMustNotTitleWords() != null
				|| condition.getShouldTitleWords() != null) {
			BoolQueryBuilder titleQueryBuilder = composeQueryBuilder(condition.getMustTitleWords(),
					condition.getMustNotTitleWords(), condition.getShouldTitleWords(), "title");
			boolQueryBuilder.must(titleQueryBuilder);
		}
		if (condition.getWebsiteIDs() != null) {
			BoolQueryBuilder websiteIDQueryBuilder = composeIntQueryBuilder(null, null, condition.getWebsiteIDs(),
					"website_id");
			boolQueryBuilder.must(websiteIDQueryBuilder);
		}
		if (condition.getStartTime() != null && condition.getEndTime() != null) {
			BoolQueryBuilder timeQueryBuilder = composeTimeQueryBuilder(condition.getStartTime(),
					condition.getEndTime(), "newsDate");
			boolQueryBuilder.must(timeQueryBuilder);
		}

		searchRequestBuilder.setSize(condition.getPageSize());
		searchRequestBuilder.setFrom(condition.getFrom());

		if (OrderByMode.TIME.equals(condition.getOrderByColumn())) {
			searchRequestBuilder.addSort("newsDate", SortOrder.DESC);
		}

		list = search(searchRequestBuilder, boolQueryBuilder, condition.getFrom(), condition.getPageSize());
		return list;
	}

	public Long countByMultiCondition(SearchCondition condition) {
		SearchRequestBuilder searchRequestBuilder = SearchESClientFactory.getClient().prepareSearch(INDEXNAME);
		searchRequestBuilder.setSearchType(SearchType.COUNT);

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		if (condition.getMustTagNames() != null || condition.getMustNotTagNames() != null
				|| condition.getShouldTagNames() != null) {
			BoolQueryBuilder tagQueryBuilder = composeQueryBuilder(condition.getMustTagNames(),
					condition.getMustNotTagNames(), condition.getShouldTagNames(), "tagName");
			boolQueryBuilder.must(tagQueryBuilder);
		}
		if (condition.getMustContentWords() != null || condition.getMustNotContentWords() != null
				|| condition.getShouldContentWords() != null) {
			BoolQueryBuilder contentQueryBuilder = composeQueryBuilder(condition.getMustContentWords(),
					condition.getMustNotContentWords(), condition.getShouldContentWords(), "content");
			boolQueryBuilder.must(contentQueryBuilder);
		}
		if (condition.getMustTitleWords() != null || condition.getMustNotTitleWords() != null
				|| condition.getShouldTitleWords() != null) {
			BoolQueryBuilder titleQueryBuilder = composeQueryBuilder(condition.getMustTitleWords(),
					condition.getMustNotTitleWords(), condition.getShouldTitleWords(), "title");
			boolQueryBuilder.must(titleQueryBuilder);
		}
		if (condition.getWebsiteIDs() != null) {
			BoolQueryBuilder websiteIDQueryBuilder = composeIntQueryBuilder(null, null, condition.getWebsiteIDs(),
					"website_id");
			boolQueryBuilder.must(websiteIDQueryBuilder);
		}
		if (condition.getStartTime() != null && condition.getEndTime() != null) {
			BoolQueryBuilder timeQueryBuilder = composeTimeQueryBuilder(condition.getStartTime(),
					condition.getEndTime(), "newsDate");
			boolQueryBuilder.must(timeQueryBuilder);
		}

		searchRequestBuilder.setQuery(boolQueryBuilder);
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		return response.getHits().totalHits();
	}

	private BoolQueryBuilder composeTimeQueryBuilder(Long startTime, Long endTime, String fieldName) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(QueryBuilders.rangeQuery(fieldName).from(startTime).to(endTime));
		return boolQueryBuilder;
	}

	private BoolQueryBuilder composeQueryBuilder(List<String> mustCondition, List<String> mustNotCondition,
			List<String> shouldCondition, String fieldName) {

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (mustCondition != null) {
			BoolQueryBuilder mustTagsBuilder = QueryBuilders.boolQuery();
			for (String c : mustCondition) {
				mustTagsBuilder.must(QueryBuilders.termQuery(fieldName, c));
			}
			boolQueryBuilder.must(mustTagsBuilder);
		}

		if (shouldCondition != null) {
			BoolQueryBuilder shouldTagsBuilder = QueryBuilders.boolQuery();
			for (String c : shouldCondition) {
				shouldTagsBuilder.should(QueryBuilders.termQuery(fieldName, c));
			}
			boolQueryBuilder.must(shouldTagsBuilder);
		}

		if (mustNotCondition != null) {
			BoolQueryBuilder mustNotTagsBuilder = QueryBuilders.boolQuery();
			for (String c : mustNotCondition) {
				mustNotTagsBuilder.mustNot(QueryBuilders.termQuery(fieldName, c));
			}
			boolQueryBuilder.must(mustNotTagsBuilder);
		}

		return boolQueryBuilder;
	}

	private BoolQueryBuilder composeIntQueryBuilder(List<Integer> mustCondition, List<Integer> mustNotCondition,
			List<Integer> shouldCondition, String fieldName) {

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (mustCondition != null) {
			BoolQueryBuilder mustTagsBuilder = QueryBuilders.boolQuery();
			for (Integer c : mustCondition) {
				mustTagsBuilder.must(QueryBuilders.termQuery(fieldName, c));
			}
			boolQueryBuilder.must(mustTagsBuilder);
		}

		if (shouldCondition != null) {
			BoolQueryBuilder shouldTagsBuilder = QueryBuilders.boolQuery();
			for (Integer c : shouldCondition) {
				shouldTagsBuilder.should(QueryBuilders.termQuery(fieldName, c));
			}
			boolQueryBuilder.must(shouldTagsBuilder);
		}

		if (mustNotCondition != null) {
			BoolQueryBuilder mustNotTagsBuilder = QueryBuilders.boolQuery();
			for (Integer c : mustNotCondition) {
				mustNotTagsBuilder.mustNot(QueryBuilders.termQuery(fieldName, c));
			}
			boolQueryBuilder.must(mustNotTagsBuilder);
		}
		return boolQueryBuilder;
	}

	private List<SearchNews> search(SearchRequestBuilder searchRequestBuilder, QueryBuilder queryBuilder, int sizeFrom,
			int size) {
		List<SearchNews> list = new ArrayList<SearchNews>();

		searchRequestBuilder.setQuery(queryBuilder);
		searchRequestBuilder.setFrom(sizeFrom).setSize(size);
		searchRequestBuilder.addSort("crawlDate", SortOrder.DESC);

		/**
		 * 设置高亮
		 */
		searchRequestBuilder.addHighlightedField("content").setHighlighterPreTags("<span style=\"color:red\">")
				.setHighlighterPostTags("</span>");

		/**
		 * 设置高亮
		 */
		searchRequestBuilder.addHighlightedField("title").setHighlighterPreTags("<span style=\"color:red\">")
				.setHighlighterPostTags("</span>");

		// 执行搜索,返回搜索响应信息
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();

		for (SearchHit hit : hits) {
			String json = hit.getSourceAsString();
			ObjectMapper mapper = new ObjectMapper();
			try {
				SearchNews news = mapper.readValue(json, SearchNews.class);
				list.add(news);
			} catch (JsonParseException e) {
				logger.error(e.getMessage());
			} catch (JsonMappingException e) {
				logger.error(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return list;
	}

}
