package com.unbank.es.journal;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsFilterBuilder;

import com.unbank.common.utils.CommonUtils;

/**
 * 期刊搜索封装类 Created by LiuLei on 2015/10/27.
 */
public class JournalSearchData {

	/**
	 * 必须
	 */
	private List<String> journalIdMust;

	/**
	 * 可以
	 */
	private List<String> journalIdShould;

	/**
	 * 不能
	 */
	private List<String> journalIdNot;

	/**
	 * 必须包含期刊名称（标题）
	 */
	private List<String> nameMust;

	/**
	 * 可以包含期刊名称（标题）
	 */
	private List<String> nameShould;

	/**
	 * 不能包含期刊名称（标题）
	 */
	private List<String> nameNot;

	/**
	 * 必须包含简介
	 */
	private List<String> skipMust;

	/**
	 * 可以包含简介
	 */
	private List<String> skipShould;

	/**
	 * 不能包含简介
	 */
	private List<String> skipNot;

	/**
	 * 必须包含标签
	 */
	private List<String> labelMust;

	/**
	 * 可以包含的标签
	 */
	private List<String> labelShould;

	/**
	 * 不能包含的标签
	 */
	private List<String> labelNot;

	/**
	 * 必须包含的关键字
	 */
	private List<String> keyWordMust;

	/**
	 * 可以包含的关键字
	 */
	private List<String> keyWordShould;

	/**
	 * 不能包含的关键字
	 */
	private List<String> keyWordNot;

	/**
	 * 状态：默认all SAVED:私有保存, SUBMITTED:审核中, PASSED:审核成功, FAILED:审核失败 默认：SUBMITTED审核中
	 */
	private List<String> passType;

	/**
	 * 创建人
	 */
	private String submitUserName;

	/**
	 * 提交起始时间
	 */
	private long submitStartTime;

	/**
	 * 提交结果时间
	 */
	private long submitEndTime;
	
	/**
	 * 默认:all,　无数据：0，　有数据：1
	 */
	private String showData="1";
	
	/**
	 * true: 不过滤删除信息，并做删除标记
	 */
	private String deleteTag;
	
	/**
	 * 期刊类型ID
	 */
	private int typeId;
	
	public BoolQueryBuilder getQueryBuilder(List<String> topList) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		// 判断是否需要追加Must  (havaData)
		if(CommonUtils.isEmpty(journalIdMust) && CommonUtils.isEmpty(journalIdShould) && CommonUtils.isEmpty(journalIdNot)
				&& CommonUtils.isEmpty(nameMust) && CommonUtils.isEmpty(nameShould) && CommonUtils.isEmpty(nameNot)
				&& CommonUtils.isEmpty(skipMust) && CommonUtils.isEmpty(skipShould) && CommonUtils.isEmpty(skipNot)
				&& CommonUtils.isEmpty(labelMust) && CommonUtils.isEmpty(labelShould) && CommonUtils.isEmpty(labelNot)
				&& CommonUtils.isEmpty(keyWordMust) && CommonUtils.isEmpty(keyWordShould) && CommonUtils.isEmpty(keyWordNot)
				){
			
			if(!"all".equals(showData)){
				boolQueryBuilder.must(QueryBuilders.termQuery("haveData", showData));
			}
		}
		
		
		if(!"all".equals(passType)){
			if(CommonUtils.isNotEmpty(passType)){
				for (String tempType : passType) {
					
					BoolQueryBuilder tempQuery = QueryBuilders.boolQuery().should(QueryBuilders.queryStringQuery(tempType).field("passType"));
					
					boolQueryBuilder.must(tempQuery);
				}
			}
		}
		
		if (CommonUtils.isNotEmpty(journalIdMust) || CommonUtils.isNotEmpty(journalIdShould) || CommonUtils.isNotEmpty(journalIdNot)) {
			if (CommonUtils.isNotEmpty(deleteTag)) {
				journalIdTermQuery(journalIdMust, journalIdShould, null, "journalId", boolQueryBuilder, showData, topList, typeId);
			} else {
				journalIdTermQuery(journalIdMust, journalIdShould, journalIdNot, "journalId", boolQueryBuilder, showData, topList, typeId);
			}
		}

		if (CommonUtils.isNotEmpty(nameMust)
				|| CommonUtils.isNotEmpty(nameShould)
				|| CommonUtils.isNotEmpty(nameNot)) {
			// 标题名称专用
			composeMatchStrQueryBuilder(nameMust, nameShould, nameNot, "name", boolQueryBuilder, showData, topList, typeId);
		}
		if (CommonUtils.isNotEmpty(skipMust)
				|| CommonUtils.isNotEmpty(skipShould)
				|| CommonUtils.isNotEmpty(skipNot)) {
			composeTermQuery(skipMust, skipShould, skipNot, "skip", boolQueryBuilder, showData, topList, typeId);
		}
		if (CommonUtils.isNotEmpty(labelMust)
				|| CommonUtils.isNotEmpty(labelShould)
				|| CommonUtils.isNotEmpty(labelNot)) {
			composeTermQuery(labelMust, labelShould, labelNot, "label", boolQueryBuilder, showData, topList, typeId);
		}
		if (CommonUtils.isNotEmpty(keyWordMust)
				|| CommonUtils.isNotEmpty(keyWordShould)
				|| CommonUtils.isNotEmpty(keyWordNot)) {
			composeTermQuery(keyWordMust, keyWordShould, keyWordNot, "keyWord", boolQueryBuilder, showData, topList, typeId);
		}

		// 创建人条件封装
		if (CommonUtils.isNotEmpty(submitUserName)) {
			List<String> nameList = new ArrayList<String>();
			nameList.add(submitUserName);
			composeTermQuery(nameList, null, null, "submitUserName", boolQueryBuilder, showData, topList, typeId);
//			boolQueryBuilder.must(QueryBuilders.termQuery("submitUserName", submitUserName));
		}
		
		if (submitStartTime > 0 && submitEndTime == 0) {
			// 如果结束时间为空，则结束时间为当前系统时间
			composeRangeQueryBuilder(submitStartTime, System.currentTimeMillis(), "submitTime", boolQueryBuilder, showData);
		} else if (submitStartTime == 0 && submitEndTime > 0) {
			composeRangeQueryBuilder(0, submitEndTime, "submitTime", boolQueryBuilder, showData);
		} else {
			// 提交时间段封装
			if(CommonUtils.isNotEmpty(submitStartTime) && submitStartTime > 0 && CommonUtils.isNotEmpty(submitEndTime) && submitEndTime > 0){
				composeRangeQueryBuilder(submitStartTime, submitEndTime, "submitTime", boolQueryBuilder, showData);
			}
		}
		
		return boolQueryBuilder;
	}

	private BoolQueryBuilder journalIdTermQuery(List<String> must,
			List<String> should, List<String> not, String fieldName,
			BoolQueryBuilder boolQueryBuilder, String showData, List<String> topList, int typeId) {
		
		// 筛选期刊是否文档
		TermQueryBuilder havaDataQuery = null;
		if(!"all".equals(showData)){
			havaDataQuery = QueryBuilders.termQuery("haveData", showData);
		}
		
		// 判断期刊类型ID是否为空
		TermQueryBuilder typeIdQuery = null;
		if(typeId > 0){
			typeIdQuery = QueryBuilders.termQuery("typeId", typeId);
		}
		
		BoolQueryBuilder tempQuery = null;
		if(CommonUtils.isNotEmpty(topList)){
			if (CommonUtils.isNotEmpty(must)) {
				tempQuery = QueryBuilders.boolQuery();
				for (String tempStr : must) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					if(havaDataQuery != null){
						query.must(havaDataQuery);
					}
					if(typeIdQuery != null){
						query.must(typeIdQuery);
					}
					query.must(QueryBuilders.termQuery(fieldName, tempStr));
					query.must(QueryBuilders.termsQuery("journalId", topList));
					tempQuery.must(query);
				}
				boolQueryBuilder.should(tempQuery);
			}
			if (CommonUtils.isNotEmpty(should)) {
				tempQuery = QueryBuilders.boolQuery();
				for (String tempStr : should) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					if(havaDataQuery != null){
						query.must(havaDataQuery);
					}
					if(typeIdQuery != null){
						query.must(typeIdQuery);
					}
					query.must(QueryBuilders.termQuery(fieldName, tempStr));
					query.must(QueryBuilders.termsQuery("journalId", topList));
					tempQuery.should(query);
				}
				boolQueryBuilder.should(tempQuery);
			}
			if (CommonUtils.isNotEmpty(not)) {
				tempQuery = QueryBuilders.boolQuery();
				for (String tempStr : not) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					if(havaDataQuery != null){
						query.must(havaDataQuery);
					}
					if(typeIdQuery != null){
						query.must(typeIdQuery);
					}
					query.mustNot(QueryBuilders.termQuery(fieldName, tempStr));
					query.must(QueryBuilders.termsQuery("journalId", topList));
					tempQuery.must(query);
				}
				boolQueryBuilder.should(tempQuery);
			}
		}else{
			if (CommonUtils.isNotEmpty(must)) {
				tempQuery = QueryBuilders.boolQuery();
//				for (String tempStr : must) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					if(havaDataQuery != null){
						query.must(havaDataQuery);
					}
					if(typeIdQuery != null){
						query.must(typeIdQuery);
					}

					// 子条件使用过滤器，否则值过多， 超1024个term的话， 会出现TooManyClauses[maxClauseCount is set to 1024] 的异常
					TermsFilterBuilder termsFilterBuilder = new TermsFilterBuilder(fieldName, must);
					query.must(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), termsFilterBuilder));
//					query.must(QueryBuilders.termQuery(fieldName, tempStr));
					tempQuery.must(query);
//				}
				boolQueryBuilder.must(tempQuery);
			}
			if (CommonUtils.isNotEmpty(should)) {
				tempQuery = QueryBuilders.boolQuery();
//				for (String tempStr : should) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					if(havaDataQuery != null){
						query.must(havaDataQuery);
					}
					if(typeIdQuery != null){
						query.must(typeIdQuery);
					}

					TermsFilterBuilder termsFilterBuilder = new TermsFilterBuilder(fieldName, should);
					query.must(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), termsFilterBuilder));
//					query.must(QueryBuilders.termQuery(fieldName, tempStr));
					tempQuery.should(query);
//				}
				boolQueryBuilder.should(tempQuery);
			}
			if (CommonUtils.isNotEmpty(not)) {
				tempQuery = QueryBuilders.boolQuery();
//				for (String tempStr : not) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					if(havaDataQuery != null){
						query.must(havaDataQuery);
					}
					if(typeIdQuery != null){
						query.must(typeIdQuery);
					}
					TermsFilterBuilder termsFilterBuilder = new TermsFilterBuilder(fieldName, not);
					query.must(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), termsFilterBuilder));
//					query.mustNot(QueryBuilders.termQuery(fieldName, tempStr));
					tempQuery.must(query);
//				}
				boolQueryBuilder.must(tempQuery);
			}
		}
		
		return boolQueryBuilder;
		
	}
	
	/**
	 * 标题名称专用
	 * @param mustCondition
	 * @param shouldCondition
	 * @param mustNotCondition
	 * @param fieldName
	 * @param boolQueryBuilder
	 * @param haveData
	 * @param topList 
	 * @param typeId 
	 * @return
	 */
	private BoolQueryBuilder composeMatchStrQueryBuilder(List<String> mustCondition, List<String> shouldCondition,
			List<String> mustNotCondition, String fieldName, BoolQueryBuilder boolQueryBuilder, String haveData,
			List<String> topList, int typeId) {
		// 筛选期刊是否文档
		TermQueryBuilder havaDataQuery = null;
		if(!"all".equals(showData)){
			havaDataQuery = QueryBuilders.termQuery("haveData", showData);
		}
		
		// 判断期刊类型ID是否为空
		TermQueryBuilder typeIdQuery = null;
		if(typeId > 0){
			typeIdQuery = QueryBuilders.termQuery("typeId", typeId);
		}
		
		BoolQueryBuilder tempQuery = null;
		if(CommonUtils.isNotEmpty(topList)){
			if (CommonUtils.isNotEmpty(mustCondition)) {
				tempQuery = QueryBuilders.boolQuery();
				for (String tempStr : mustCondition) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					if(havaDataQuery != null){
						query.must(havaDataQuery);
					}
					if(typeIdQuery != null){
						query.must(typeIdQuery);
					}
					query.must(QueryBuilders.matchQuery(fieldName, tempStr).minimumShouldMatch("50%"));
					query.must(QueryBuilders.termsQuery("journalId", topList));
					tempQuery.must(query);
				}
				boolQueryBuilder.must(tempQuery);
			}
			if (CommonUtils.isNotEmpty(shouldCondition)) {
				tempQuery = QueryBuilders.boolQuery();
				for (String tempStr : shouldCondition) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					if(havaDataQuery != null){
						query.must(havaDataQuery);
					}
					if(typeIdQuery != null){
						query.must(typeIdQuery);
					}
					query.must(QueryBuilders.matchQuery(fieldName, tempStr).minimumShouldMatch("50%"));
					query.must(QueryBuilders.termsQuery("journalId", topList));
					tempQuery.should(query);
				}
				boolQueryBuilder.should(tempQuery);
			}
			if (CommonUtils.isNotEmpty(mustNotCondition)) {
				tempQuery = QueryBuilders.boolQuery();
				for (String tempStr : mustNotCondition) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					if(havaDataQuery != null){
						query.must(havaDataQuery);
					}
					if(typeIdQuery != null){
						query.must(typeIdQuery);
					}
					query.mustNot(QueryBuilders.matchQuery(fieldName, tempStr).minimumShouldMatch("50%"));
					query.must(QueryBuilders.termsQuery("journalId", topList));
					tempQuery.mustNot(query);
				}
				boolQueryBuilder.mustNot(tempQuery);
			}
		}else{
			if (CommonUtils.isNotEmpty(mustCondition)) {
				tempQuery = QueryBuilders.boolQuery();
				for (String tempStr : mustCondition) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					if(havaDataQuery != null){
						query.must(havaDataQuery);
					}
					if(typeIdQuery != null){
						query.must(typeIdQuery);
					}
					query.must(QueryBuilders.matchQuery(fieldName, tempStr).minimumShouldMatch("50%"));
					tempQuery.must(query);
				}
				boolQueryBuilder.must(tempQuery);
			}
			if (CommonUtils.isNotEmpty(shouldCondition)) {
				tempQuery = QueryBuilders.boolQuery();
				for (String tempStr : shouldCondition) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					if(havaDataQuery != null){
						query.must(havaDataQuery);
					}
					if(typeIdQuery != null){
						query.must(typeIdQuery);
					}
					query.must(QueryBuilders.matchQuery(fieldName, tempStr).minimumShouldMatch("50%"));
					tempQuery.should(query);
				}
				boolQueryBuilder.should(tempQuery);
			}
			if (CommonUtils.isNotEmpty(mustNotCondition)) {
				tempQuery = QueryBuilders.boolQuery();
				for (String tempStr : mustNotCondition) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					if(havaDataQuery != null){
						query.must(havaDataQuery);
					}
					if(typeIdQuery != null){
						query.must(typeIdQuery);
					}
					query.mustNot(QueryBuilders.matchQuery(fieldName, tempStr).minimumShouldMatch("50%"));
					tempQuery.mustNot(query);
				}
				boolQueryBuilder.mustNot(tempQuery);
			}
		}
		
		return boolQueryBuilder;
	}
	
	private BoolQueryBuilder composeRangeQueryBuilder(long start, long end, String fieldName, BoolQueryBuilder boolQueryBuilder, String showData) {
		BoolQueryBuilder timeQueryBuilder = QueryBuilders.boolQuery();
		timeQueryBuilder.must(QueryBuilders.rangeQuery(fieldName).from(start).to(end));
		if(!"all".equals(showData)){
			timeQueryBuilder.must(QueryBuilders.termQuery("haveData", showData));
		}
		boolQueryBuilder.must(timeQueryBuilder);
		return boolQueryBuilder;
	}

	private BoolQueryBuilder composeTermQuery(List<String> must,
			List<String> should, List<String> not, String fieldName,
			BoolQueryBuilder boolQueryBuilder, String showData, List<String> topList, int typeId) {
		
		// 筛选期刊是否文档
		TermQueryBuilder havaDataQuery = null;
		if(!"all".equals(showData)){
			havaDataQuery = QueryBuilders.termQuery("haveData", showData);
		}
		
		// 判断期刊类型ID是否为空
		TermQueryBuilder typeIdQuery = null;
		if(typeId > 0){
			typeIdQuery = QueryBuilders.termQuery("typeId", typeId);
		}
		
		BoolQueryBuilder tempQuery = null;
		
		if(CommonUtils.isNotEmpty(topList)){
			if (CommonUtils.isNotEmpty(must)) {
				tempQuery = QueryBuilders.boolQuery();
				for (String tempStr : must) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					if(havaDataQuery != null){
						query.must(havaDataQuery);
					}
					if(typeIdQuery != null){
						query.must(typeIdQuery);
					}
					query.must(QueryBuilders.termQuery(fieldName, tempStr));
					query.must(QueryBuilders.termsQuery("journalId", topList));
					tempQuery.must(query);
				}
				boolQueryBuilder.should(tempQuery);
			}
			if (CommonUtils.isNotEmpty(should)) {
				tempQuery = QueryBuilders.boolQuery();
				for (String tempStr : should) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					if(havaDataQuery != null){
						query.must(havaDataQuery);
					}
					if(typeIdQuery != null){
						query.must(typeIdQuery);
					}
					query.must(QueryBuilders.termQuery(fieldName, tempStr));
					query.must(QueryBuilders.termsQuery("journalId", topList));
					tempQuery.should(query);
				}
				boolQueryBuilder.should(tempQuery);
			}
			if (CommonUtils.isNotEmpty(not)) {
				tempQuery = QueryBuilders.boolQuery();
				for (String tempStr : not) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					if(havaDataQuery != null){
						query.must(havaDataQuery);
					}
					if(typeIdQuery != null){
						query.must(typeIdQuery);
					}
					query.mustNot(QueryBuilders.termQuery(fieldName, tempStr));
					query.must(QueryBuilders.termsQuery("journalId", topList));
					tempQuery.must(query);
				}
				boolQueryBuilder.should(tempQuery);
			}
		}else{
			if (CommonUtils.isNotEmpty(must)) {
				tempQuery = QueryBuilders.boolQuery();
				for (String tempStr : must) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					if(havaDataQuery != null){
						query.must(havaDataQuery);
					}
					if(typeIdQuery != null){
						query.must(typeIdQuery);
					}
					query.must(QueryBuilders.termQuery(fieldName, tempStr));
					tempQuery.must(query);
				}
				boolQueryBuilder.must(tempQuery);
			}
			if (CommonUtils.isNotEmpty(should)) {
				tempQuery = QueryBuilders.boolQuery();
				for (String tempStr : should) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					if(havaDataQuery != null){
						query.must(havaDataQuery);
					}
					if(typeIdQuery != null){
						query.must(typeIdQuery);
					}
					query.must(QueryBuilders.termQuery(fieldName, tempStr));
					tempQuery.should(query);
				}
				boolQueryBuilder.should(tempQuery);
			}
			if (CommonUtils.isNotEmpty(not)) {
				tempQuery = QueryBuilders.boolQuery();
				for (String tempStr : not) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					if(havaDataQuery != null){
						query.must(havaDataQuery);
					}
					if(typeIdQuery != null){
						query.must(typeIdQuery);
					}
					query.mustNot(QueryBuilders.termQuery(fieldName, tempStr));
					tempQuery.must(query);
				}
				boolQueryBuilder.must(tempQuery);
			}
		}
		
		return boolQueryBuilder;
		
	}

	public List<String> getNameMust() {
		return nameMust;
	}

	public void setNameMust(List<String> nameMust) {
		this.nameMust = nameMust;
	}

	public List<String> getNameShould() {
		return nameShould;
	}

	public void setNameShould(List<String> nameShould) {
		this.nameShould = nameShould;
	}

	public List<String> getNameNot() {
		return nameNot;
	}

	public void setNameNot(List<String> nameNot) {
		this.nameNot = nameNot;
	}

	public List<String> getSkipMust() {
		return skipMust;
	}

	public void setSkipMust(List<String> skipMust) {
		this.skipMust = skipMust;
	}

	public List<String> getSkipShould() {
		return skipShould;
	}

	public void setSkipShould(List<String> skipShould) {
		this.skipShould = skipShould;
	}

	public List<String> getSkipNot() {
		return skipNot;
	}

	public void setSkipNot(List<String> skipNot) {
		this.skipNot = skipNot;
	}

	public List<String> getLabelMust() {
		return labelMust;
	}

	public void setLabelMust(List<String> labelMust) {
		this.labelMust = labelMust;
	}

	public List<String> getLabelShould() {
		return labelShould;
	}

	public void setLabelShould(List<String> labelShould) {
		this.labelShould = labelShould;
	}

	public List<String> getLabelNot() {
		return labelNot;
	}

	public void setLabelNot(List<String> labelNot) {
		this.labelNot = labelNot;
	}

	public List<String> getKeyWordMust() {
		return keyWordMust;
	}

	public void setKeyWordMust(List<String> keyWordMust) {
		this.keyWordMust = keyWordMust;
	}

	public List<String> getKeyWordShould() {
		return keyWordShould;
	}

	public void setKeyWordShould(List<String> keyWordShould) {
		this.keyWordShould = keyWordShould;
	}

	public List<String> getKeyWordNot() {
		return keyWordNot;
	}

	public void setKeyWordNot(List<String> keyWordNot) {
		this.keyWordNot = keyWordNot;
	}

	public List<String> getPassType() {
		return passType;
	}

	public void setPassType(List<String> passType) {
		this.passType = passType;
	}

	public List<String> getJournalIdMust() {
		return journalIdMust;
	}

	public void setJournalIdMust(List<String> journalIdMust) {
		this.journalIdMust = journalIdMust;
	}

	public List<String> getJournalIdShould() {
		return journalIdShould;
	}

	public void setJournalIdShould(List<String> journalIdShould) {
		this.journalIdShould = journalIdShould;
	}

	public List<String> getJournalIdNot() {
		return journalIdNot;
	}

	public void setJournalIdNot(List<String> journalIdNot) {
		this.journalIdNot = journalIdNot;
	}

	public String getSubmitUserName() {
		return submitUserName;
	}

	public void setSubmitUserName(String submitUserName) {
		this.submitUserName = submitUserName;
	}

	public long getSubmitStartTime() {
		return submitStartTime;
	}

	public void setSubmitStartTime(long submitStartTime) {
		this.submitStartTime = submitStartTime;
	}

	public long getSubmitEndTime() {
		return submitEndTime;
	}

	public void setSubmitEndTime(long submitEndTime) {
		this.submitEndTime = submitEndTime;
	}

	public String getShowData() {
		return showData;
	}

	public void setShowData(String showData) {
		this.showData = showData;
	}

	public String getDeleteTag() {
		return deleteTag;
	}

	public void setDeleteTag(String deleteTag) {
		this.deleteTag = deleteTag;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
}
