package com.unbank.common.utils;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;

/**
 * 封装QueryBuilder
 * @author LiuLei
 *
 */
public class QueryBuilderUtils {
	
	
	public static BoolQueryBuilder minimumShouldMatchQueryBuilder(List<String> mustCondition, List<String> mustNotCondition,
			List<String> shouldCondition, String fieldName) {

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (mustCondition != null) {
			BoolQueryBuilder mustTagsBuilder = QueryBuilders.boolQuery();
			for (String c : mustCondition) {
				mustTagsBuilder.must(QueryBuilders.matchQuery(fieldName, c).minimumShouldMatch("50%"));
			}
			boolQueryBuilder.must(mustTagsBuilder);
		}

		if (shouldCondition != null) {
			BoolQueryBuilder shouldTagsBuilder = QueryBuilders.boolQuery();
			for (String c : shouldCondition) {
				shouldTagsBuilder.should(QueryBuilders.matchQuery(fieldName, c).minimumShouldMatch("50%"));
			}
			boolQueryBuilder.must(shouldTagsBuilder);
		}

		if (mustNotCondition != null) {
			BoolQueryBuilder mustNotTagsBuilder = QueryBuilders.boolQuery();
			for (String c : mustNotCondition) {
				mustNotTagsBuilder.mustNot(QueryBuilders.matchQuery(fieldName, c).minimumShouldMatch("50%"));
			}
			boolQueryBuilder.must(mustNotTagsBuilder);
		}
		return boolQueryBuilder;
	}
	
	
	public static BoolQueryBuilder matchPhraseQueryBuilder(List<String> mustCondition, List<String> mustNotCondition,
			List<String> shouldCondition, String fieldName) {

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (mustCondition != null) {
			BoolQueryBuilder mustTagsBuilder = QueryBuilders.boolQuery();
			for (String c : mustCondition) {

				mustTagsBuilder.must(QueryBuilders.matchPhraseQuery(fieldName, c));
			}
			boolQueryBuilder.must(mustTagsBuilder);
		}

		if (shouldCondition != null) {
			BoolQueryBuilder shouldTagsBuilder = QueryBuilders.boolQuery();
			for (String c : shouldCondition) {
				shouldTagsBuilder.should(QueryBuilders.matchPhraseQuery(fieldName, c));
			}
			boolQueryBuilder.must(shouldTagsBuilder);
		}

		if (mustNotCondition != null) {
			BoolQueryBuilder mustNotTagsBuilder = QueryBuilders.boolQuery();
			for (String c : mustNotCondition) {
				mustNotTagsBuilder.mustNot(QueryBuilders.matchPhraseQuery(fieldName, c));
			}
			boolQueryBuilder.must(mustNotTagsBuilder);
		}

		return boolQueryBuilder;
	}
	
	/**
	 * 常用词
	 * @param mustCondition
	 * @param mustNotCondition
	 * @param shouldCondition
	 * @param fieldName
	 * @return
	 */
	public static BoolQueryBuilder termStrQueryBuilder(List<String> mustCondition, List<String> mustNotCondition,
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
	
	public static BoolQueryBuilder termIntQueryBuilder(List<Integer> mustCondition, List<Integer> mustNotCondition,
			List<Integer> shouldCondition, String fieldName) {

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (CommonUtils.isNotEmpty(mustCondition)) {
			BoolQueryBuilder mustTagsBuilder = QueryBuilders.boolQuery();
			for (Integer c : mustCondition) {
				mustTagsBuilder.must(QueryBuilders.termQuery(fieldName, c));
			}
			boolQueryBuilder.must(mustTagsBuilder);
		}

		if (CommonUtils.isNotEmpty(shouldCondition)) {
			BoolQueryBuilder shouldTagsBuilder = QueryBuilders.boolQuery();
			for (Integer c : shouldCondition) {
				shouldTagsBuilder.should(QueryBuilders.termQuery(fieldName, c));
			}
			boolQueryBuilder.must(shouldTagsBuilder);
		}

		if (CommonUtils.isNotEmpty(mustNotCondition)) {
			BoolQueryBuilder mustNotTagsBuilder = QueryBuilders.boolQuery();
			for (Integer c : mustNotCondition) {
				mustNotTagsBuilder.mustNot(QueryBuilders.termQuery(fieldName, c));
			}
			boolQueryBuilder.must(mustNotTagsBuilder);
		}
		return boolQueryBuilder;
	}
	
	/**
	 * 模糊
	 * @param mustCondition
	 * @param mustNotCondition
	 * @param shouldCondition
	 * @param fieldName
	 * @return
	 */
	public static BoolQueryBuilder composeFuzzyQueryBuilder(List<String> mustCondition, List<String> mustNotCondition,
			List<String> shouldCondition, String fieldName) {

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (mustCondition != null) {
			BoolQueryBuilder mustTagsBuilder = QueryBuilders.boolQuery();
			for (String c : mustCondition) {

				mustTagsBuilder.must(QueryBuilders.fuzzyLikeThisQuery(fieldName).likeText(c));
			}
			boolQueryBuilder.must(mustTagsBuilder);
		}

		if (shouldCondition != null) {
			BoolQueryBuilder shouldTagsBuilder = QueryBuilders.boolQuery();
			for (String c : shouldCondition) {
				shouldTagsBuilder.should(QueryBuilders.fuzzyLikeThisQuery(fieldName).likeText(c));
			}
			boolQueryBuilder.must(shouldTagsBuilder);
		}

		if (mustNotCondition != null) {
			BoolQueryBuilder mustNotTagsBuilder = QueryBuilders.boolQuery();
			for (String c : mustNotCondition) {
				mustNotTagsBuilder.mustNot(QueryBuilders.fuzzyLikeThisQuery(fieldName).likeText(c));
			}
			boolQueryBuilder.must(mustNotTagsBuilder);
		}

		return boolQueryBuilder;
	}
	
	
	public static BoolQueryBuilder composeQueryString(List<String> must, List<String> should, List<String> not, String fieldName) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		BoolQueryBuilder tempQuery = null;
		
		if (CommonUtils.isNotEmpty(must)) {
			tempQuery = QueryBuilders.boolQuery();
			for (String tempStr : must) {
				QueryStringQueryBuilder queryString = QueryBuilders.queryStringQuery(tempStr)
						.field(fieldName);
				tempQuery.must(queryString);
			}
			boolQueryBuilder.must(tempQuery);
		}
		if (CommonUtils.isNotEmpty(should)) {
			tempQuery = QueryBuilders.boolQuery();
			for (String tempStr : should) {
				QueryStringQueryBuilder queryString = QueryBuilders.queryStringQuery(tempStr)
						.field(fieldName);
				tempQuery.should(queryString);
			}
			boolQueryBuilder.must(tempQuery);
		}
		if (CommonUtils.isNotEmpty(not)) {
			tempQuery = QueryBuilders.boolQuery();
			for (String tempStr : not) {
				QueryStringQueryBuilder queryString = QueryBuilders.queryStringQuery(tempStr)
						.field(fieldName);
				tempQuery.mustNot(queryString);
			}
			boolQueryBuilder.must(tempQuery);
		}
		return boolQueryBuilder;
	}
	
	
	public static BoolQueryBuilder composePrefixQuery(List<String> must, List<String> should, List<String> not, String fieldName) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		BoolQueryBuilder tempQuery = null;
		
		if (CommonUtils.isNotEmpty(must)) {
			tempQuery = QueryBuilders.boolQuery();
			for (String tempStr : must) {
				tempQuery.must(QueryBuilders.prefixQuery(fieldName, tempStr));
			}
			boolQueryBuilder.must(tempQuery);
		}
		if (CommonUtils.isNotEmpty(should)) {
			tempQuery = QueryBuilders.boolQuery();
			for (String tempStr : should) {
				tempQuery.should(QueryBuilders.prefixQuery(fieldName, tempStr));
			}
			boolQueryBuilder.must(tempQuery);
		}
		if (CommonUtils.isNotEmpty(not)) {
			tempQuery = QueryBuilders.boolQuery();
			for (String tempStr : not) {
				tempQuery.mustNot(QueryBuilders.prefixQuery(fieldName, tempStr));
			}
			boolQueryBuilder.must(tempQuery);
		}
		return boolQueryBuilder;
	}
	
	public static BoolQueryBuilder composeRangeQueryBuilder(long start,long end, String fieldName){
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(QueryBuilders.rangeQuery(fieldName).from(start).to(end));
		return boolQueryBuilder;
	}


	public static BoolQueryBuilder matchPhraseQueryBuilder(
			List<String> mustCondition, List<String> mustNotCondition,
			List<String> shouldCondition, List<String> termsMustCondition,
			String fieldName) {
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (mustCondition != null) {
			BoolQueryBuilder mustTagsBuilder = QueryBuilders.boolQuery();
			for (String c : mustCondition) {

				mustTagsBuilder.must(QueryBuilders.matchPhraseQuery(fieldName, c));
			}
			boolQueryBuilder.must(mustTagsBuilder);
		}

		if (shouldCondition != null) {
			BoolQueryBuilder shouldTagsBuilder = QueryBuilders.boolQuery();
			for (String c : shouldCondition) {
				shouldTagsBuilder.should(QueryBuilders.matchPhraseQuery(fieldName, c));
			}
			boolQueryBuilder.must(shouldTagsBuilder);
		}

		if (mustNotCondition != null) {
			BoolQueryBuilder mustNotTagsBuilder = QueryBuilders.boolQuery();
			for (String c : mustNotCondition) {
				mustNotTagsBuilder.mustNot(QueryBuilders.matchPhraseQuery(fieldName, c));
			}
			boolQueryBuilder.must(mustNotTagsBuilder);
		}
		
		if(CommonUtils.isNotEmpty(termsMustCondition)){
			BoolQueryBuilder query = QueryBuilders.boolQuery();
			for (String c : termsMustCondition) {
				query.should(QueryBuilders.matchPhraseQuery(fieldName, c));
			}
			boolQueryBuilder.must(query);
		}

		return boolQueryBuilder;
	}
	
	
}
