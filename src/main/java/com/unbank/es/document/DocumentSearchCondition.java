package com.unbank.es.document;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsFilterBuilder;

import com.unbank.common.utils.CommonUtils;

public class DocumentSearchCondition {
	
	/**
	 * 文档来源的项目（注意：多个来源之间进行或运算取并集）
	 */
	private List<String> articleProjects;
	
	/**
	 * 文档操作类型（upload or write）
	 */
	private String articleSave;
	
	/**
	 * 文档名称中必须包含的词
	 */
	private List<String> mustWordsOfArticleName;
	
	/**
	 * 文档名称中可以包含的词
	 */
	private List<String> shouldWordsOfArticleName;
	
	/**
	 * 文档标题中不能包含的词
	 */
	private List<String> mustNotWordsOfArticleName;
	
	/**
	 * 文档类型
	 */
	private String articleType;
	
	/**
	 * 创建文档的用户名称（注意：多个名称之间进行或运算取并集）
	 * 
	 */
	private List<String> articleUsers;
	
	/**
	 * 创建文档的用户的ID（注意：多个ID之间进行或运算取并集）
	 */
	private List<Integer> userIds;
	
	/**
	 * 文档摘要中必须包含的词
	 */
	private List<String> mustWordsOfArticleBrief;
	
	/**
	 * 文档摘要中可以包含的词
	 */
	private List<String> shouldWordsOfArticleBrief;
	
	/**
	 * 文档摘要中不能包含的词
	 */
	private List<String> mustNotWordsOfArticleBrief;
	
	/**
	 * 文档内容中必须包含的词语
	 */
	private List<String> mustWordsOfArticleContent;
	
	/**
	 * 文档内容中可以包含的词
	 */
	private List<String> shouldWordsOfArticleContent;
	
	/**
	 * 文档内容中不能包含的词
	 */
	private List<String> mustNotWordsOfArticleContent;
	
	/**
	 * 文档标签中必须包含的词
	 */
	private List<String> mustArticleLabels;
	
	/**
	 * 文档标签中可以包含的词
	 */
	private List<String> shouldArticleLabels;
	
	/**
	 * 文档标签中不能包含的词
	 */
	private List<String> mustNotArticleLabels;
	
	
	/**
	 * 文档提交审核的开始时间范围
	 */
	private Long startSubmitTime;
	
	/**
	 * 文档提交审核的结束时间范围
	 */
	private Long endSubmitTime;
	
	
	/**
	 * 文档审核通过的开始时间范围
	 */
	private Long startPassTime;
	
	/**
	 * 文档审核通过的结束时间范围
	 */
	private Long endPassTime;
	
	/**
	 * 审核人（注意：多个审核人之间进行或运算取并集）
	 */
	private List<String> passUserNames;
	
	/**
	 * 审核人的ID（注意：多个审核人ID之间进行或运算取并集）
	 */
	private List<Integer> passUsers;
	
	/**
	 * 建立文档的开始时间范围
	 */
	private Long startInsertTime;
	
	/**
	 * 建立文档的结束时间范围
	 */
	private Long endInsertTime;
	
	/**
	 * 更新时间的开始时间范围
	 */
	private Long startUpdateTime;
	
	/**
	 * 更新时间的结束时间范围
	 */
	private Long endUpdateTime;
	
	/**
	 * 生成Word时间的开始时间范围
	 */
	private Long startDownTime;
	
	/**
	 * 生成Word时间的结束时间范围
	 */
	private Long endDownTime;
	
	/**
	 * 文档价格的下限范围
	 */
	private float articlePriceFrom;
	
	/**
	 * 文档价格的上线范围
	 */
	private float articlePriceTo;
	
	/**
	 * 文档简介中必须包含的词
	 */
	private List<String> mustWordsOfArticleSkip;
	
	/**
	 * 文档简介中不能包含的词
	 */
	private List<String> mustNotWordsOfArticleSkip;
	
	/**
	 * 文档简介中可以包含的词
	 */
	private List<String> shouldWordsOfArticleSkip;
	
	/**
	 * 默认 all
	 * doc，pdf,ppt,docx，pptx
	 */
	private List<String> articleFormat;
	
	/**
	 * 1：全部 2：期刊文档 3：非期刊文档
	 */
	private int fileType;

	/**
	 * 审核类型状态：SAVED:私有保存, SUBMITTED:审核中, PASSED:审核成功, FAILED:审核失败 默认：SUBMITTED审核中
	 */
	private String passType;
	
	/*
	 * 期刊ID
	 * 多个ID之间进行或运算取并集
	 */
	private List<Integer> journalIds;
	
	/**
	 * 栏目类型id
	 */
	private String typeId;
	
	private List<String> mustArticleId;

	private List<String> mustNotArticleId;

	private List<String> shouldArticleId;
	
	private List<String> mustJournalId;
	
	private List<String> mustNotJournalId;
	
	private List<String> shouldJournalid;
	
	public BoolQueryBuilder getQueryBuilder(){
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		if(mustArticleId != null || mustNotArticleId != null || shouldArticleId != null){
			BoolQueryBuilder queryBuilder = composeQueryBuilder(mustArticleId,mustNotArticleId,shouldArticleId,"articleId");
			boolQueryBuilder.must(queryBuilder);
		}
		
		if(mustJournalId != null || mustNotJournalId != null || shouldJournalid != null){
			BoolQueryBuilder queryBuilder = composeQueryBuilder(mustJournalId,mustNotJournalId,shouldJournalid,"articleJournalId");
			boolQueryBuilder.must(queryBuilder);
		}
		
		if (CommonUtils.isNotEmpty(journalIds)) {
			// 子条件使用过滤器，否则值过多， 超1024个term的话， 会出现TooManyClauses[maxClauseCount is set to 1024] 的异常
			TermsFilterBuilder termsFilterBuilder = new TermsFilterBuilder("articleJournalId", journalIds);
			boolQueryBuilder.must(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), termsFilterBuilder));
//			boolQueryBuilder.must(QueryBuilders.termsQuery("articleJournalId", journalIds));
		}
		
		// 文档格式 (doc;pdf;ppt)
		if(CommonUtils.isNotEmpty(articleFormat)) {
			BoolQueryBuilder articleFormatQuery = QueryBuilders.boolQuery();
			if (articleFormat.contains("doc")) {
				// missing 过滤器，过滤缺失的搜索字段
				articleFormatQuery.should(QueryBuilders.constantScoreQuery(FilterBuilders.missingFilter("articleFormat").nullValue(true)));
			} 
			
			articleFormatQuery.should(QueryBuilders.termsQuery("articleFormat", articleFormat));
			
			boolQueryBuilder.must(articleFormatQuery);
		}
		
		// 审核类型
		if(CommonUtils.isNotEmpty(passType)){
			boolQueryBuilder.must(QueryBuilders.termQuery("passType", passType));
		}
		
		// 搜索文档类型
		if (fileType == 2) {
			// 期刊  字段存在且不等于0
			boolQueryBuilder.mustNot(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), FilterBuilders.missingFilter("articleJournalId")));
			boolQueryBuilder.mustNot(QueryBuilders.termQuery("articleJournalId", 0));
		}else if (fileType == 3) {
			// 非期刊  字段不存在或等于0
			BoolQueryBuilder query = QueryBuilders.boolQuery();
			query.should(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), FilterBuilders.missingFilter("articleJournalId")));
			query.should(QueryBuilders.termQuery("articleJournalId", 0));

			boolQueryBuilder.must(query);
		}
		
		if(articleProjects!=null){
			BoolQueryBuilder queryBuilder = composeQueryBuilder(null,null,articleProjects,"articleProject");
			boolQueryBuilder.must(queryBuilder);
		}
		
		if(articleSave!=null){
			BoolQueryBuilder queryBuilder =QueryBuilders.boolQuery();
			queryBuilder.must(QueryBuilders.termQuery("articleSave", articleSave));
			boolQueryBuilder.must(queryBuilder);
		}
		
		if(mustWordsOfArticleName!=null||mustNotWordsOfArticleName!=null||shouldWordsOfArticleName!=null){
//			BoolQueryBuilder queryBuilder = composeStrQueryBuilder(mustWordsOfArticleName,mustNotWordsOfArticleName,shouldWordsOfArticleName,"articleName");
			BoolQueryBuilder queryBuilder = composeMatchStrQueryBuilder(mustWordsOfArticleName,mustNotWordsOfArticleName,shouldWordsOfArticleName,"articleName");
//			BoolQueryBuilder queryBuilder = QueryBuilderUtils.composeStrQueryBuilder(mustWordsOfArticleName,mustNotWordsOfArticleName,shouldWordsOfArticleName,"articleName");
			boolQueryBuilder.must(queryBuilder);
		}
		
		if(articleType!=null){
			BoolQueryBuilder queryBuilder =QueryBuilders.boolQuery();
			queryBuilder.must(QueryBuilders.termQuery("articleType", articleType));
			boolQueryBuilder.must(queryBuilder);
		}
		// 2015-11-17　刘磊修改 （需求：资源库如果有模版信息会有问题），如果没有传值，默认搜索document
		else{
			BoolQueryBuilder queryBuilder =QueryBuilders.boolQuery();
			queryBuilder.must(QueryBuilders.termQuery("articleType", "document"));
			boolQueryBuilder.must(queryBuilder);
		}
		
		if(articleUsers!=null){
			BoolQueryBuilder queryBuilder = composeQueryBuilder(null,null,articleUsers,"articleUser");
			boolQueryBuilder.must(queryBuilder);
		}
		
		if(userIds!=null){
			BoolQueryBuilder queryBuilder = composeIntQueryBuilder(null,null,userIds,"userId");
			boolQueryBuilder.must(queryBuilder);
		}
		
		if(mustWordsOfArticleBrief!=null||mustNotWordsOfArticleBrief!=null||shouldWordsOfArticleBrief!=null){
			BoolQueryBuilder queryBuilder = composeStrQueryBuilder(mustWordsOfArticleBrief,mustNotWordsOfArticleBrief,shouldWordsOfArticleBrief,"articleBrief");
			boolQueryBuilder.must(queryBuilder);
		}
		
		if(mustWordsOfArticleContent!=null||mustNotWordsOfArticleContent!=null||shouldWordsOfArticleContent!=null){
			BoolQueryBuilder queryBuilder = composeStrQueryBuilder(mustWordsOfArticleContent,mustNotWordsOfArticleContent,shouldWordsOfArticleContent,"articleContent");
			boolQueryBuilder.must(queryBuilder);
		}
		
		if(mustArticleLabels!=null||mustNotArticleLabels!=null||shouldArticleLabels!=null){
			BoolQueryBuilder queryBuilder = composeQueryBuilder(mustArticleLabels,mustNotArticleLabels,shouldArticleLabels,"articleLabel");
			boolQueryBuilder.must(queryBuilder);
		}
		
		if(startSubmitTime!=null&&endSubmitTime!=null){
			BoolQueryBuilder queryBuilder =composeLongQueryBuilder(startSubmitTime,endSubmitTime,"submitTime");
			boolQueryBuilder.must(queryBuilder);
		}
		
		if(startPassTime!=null&&endPassTime!=null){
			BoolQueryBuilder queryBuilder =composeLongQueryBuilder(startPassTime,endPassTime,"passTime");
			boolQueryBuilder.must(queryBuilder);
		}
		
		if(passUserNames!=null){
			BoolQueryBuilder queryBuilder = composeQueryBuilder(null,null,passUserNames,"passUserName");
			boolQueryBuilder.must(queryBuilder);
		}
		
		if(passUsers!=null){
			BoolQueryBuilder queryBuilder = composeIntQueryBuilder(null,null,passUsers,"passUser");
			boolQueryBuilder.must(queryBuilder);
		}
		
		if(startInsertTime!=null&&endInsertTime!=null){
			BoolQueryBuilder queryBuilder =composeLongQueryBuilder(startInsertTime,endInsertTime,"insertTime");
			boolQueryBuilder.must(queryBuilder);
		}
		
		if(startUpdateTime!=null&&endUpdateTime!=null){
			BoolQueryBuilder queryBuilder =composeLongQueryBuilder(startUpdateTime,endUpdateTime,"updateTime");
			boolQueryBuilder.must(queryBuilder);
		}
		
		if(startDownTime!=null&&endDownTime!=null){
			BoolQueryBuilder queryBuilder =composeLongQueryBuilder(startDownTime,endDownTime,"downTime");
			boolQueryBuilder.must(queryBuilder);
		}
		
		if(articlePriceFrom!=0&&articlePriceTo!=0&&articlePriceFrom<articlePriceTo){
			BoolQueryBuilder queryBuilder =composeFloatQueryBuilder(articlePriceFrom,articlePriceTo,"articlePrice");
			boolQueryBuilder.must(queryBuilder);
		}
		
		if(mustWordsOfArticleSkip!=null||mustNotWordsOfArticleSkip!=null||shouldWordsOfArticleSkip!=null){
			BoolQueryBuilder queryBuilder = composeStrQueryBuilder(mustWordsOfArticleSkip,mustNotWordsOfArticleSkip,shouldWordsOfArticleSkip,"articleSkip");
			boolQueryBuilder.must(queryBuilder);
		}
		
		if(typeId != null){
			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("typeId", typeId));
			boolQueryBuilder.must(queryBuilder);
		}
		
		return boolQueryBuilder;
	}
	
	private BoolQueryBuilder composeLongQueryBuilder(Long start,Long end, String fieldName){
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if(start!=null&&end!=null&&start<end&&start!=0&&end!=0){
			boolQueryBuilder.must(QueryBuilders.rangeQuery(fieldName).from(start).to(end));
		}
		return boolQueryBuilder;
	}
	
	private BoolQueryBuilder composeFloatQueryBuilder(float start,float end, String fieldName){
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if(start<end&&start!=0&&end!=0){
			boolQueryBuilder.must(QueryBuilders.rangeQuery(fieldName).from(start).to(end));
		}
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
	
	private BoolQueryBuilder composeMatchStrQueryBuilder(List<String> mustCondition, List<String> mustNotCondition,
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
	
	
	private BoolQueryBuilder composeStrQueryBuilder(List<String> mustCondition, List<String> mustNotCondition,
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
	

	public List<String> getArticleProjects() {
		return articleProjects;
	}

	public void setArticleProjects(List<String> articleProjects) {
		this.articleProjects = articleProjects;
	}

	public String getArticleSave() {
		return articleSave;
	}

	public void setArticleSave(String articleSave) {
		this.articleSave = articleSave;
	}

	public List<String> getMustWordsOfArticleName() {
		return mustWordsOfArticleName;
	}

	public void setMustWordsOfArticleName(List<String> mustWordsOfArticleName) {
		this.mustWordsOfArticleName = mustWordsOfArticleName;
	}

	public List<String> getShouldWordsOfArticleName() {
		return shouldWordsOfArticleName;
	}

	public void setShouldWordsOfArticleName(List<String> shouldWordsOfArticleName) {
		this.shouldWordsOfArticleName = shouldWordsOfArticleName;
	}

	public List<String> getMustNotWordsOfArticleName() {
		return mustNotWordsOfArticleName;
	}

	public void setMustNotWordsOfArticleName(List<String> mustNotWordsOfArticleName) {
		this.mustNotWordsOfArticleName = mustNotWordsOfArticleName;
	}

	public String getArticleType() {
		return articleType;
	}

	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}

	public List<String> getArticleUsers() {
		return articleUsers;
	}

	public void setArticleUsers(List<String> articleUsers) {
		this.articleUsers = articleUsers;
	}


	public List<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}

	public List<String> getMustWordsOfArticleBrief() {
		return mustWordsOfArticleBrief;
	}

	public void setMustWordsOfArticleBrief(List<String> mustWordsOfArticleBrief) {
		this.mustWordsOfArticleBrief = mustWordsOfArticleBrief;
	}

	public List<String> getShouldWordsOfArticleBrief() {
		return shouldWordsOfArticleBrief;
	}

	public void setShouldWordsOfArticleBrief(List<String> shouldWordsOfArticleBrief) {
		this.shouldWordsOfArticleBrief = shouldWordsOfArticleBrief;
	}

	public List<String> getMustNotWordsOfArticleBrief() {
		return mustNotWordsOfArticleBrief;
	}

	public void setMustNotWordsOfArticleBrief(List<String> mustNotWordsOfArticleBrief) {
		this.mustNotWordsOfArticleBrief = mustNotWordsOfArticleBrief;
	}

	public List<String> getMustWordsOfArticleContent() {
		return mustWordsOfArticleContent;
	}

	public void setMustWordsOfArticleContent(List<String> mustWordsOfArticleContent) {
		this.mustWordsOfArticleContent = mustWordsOfArticleContent;
	}

	public List<String> getShouldWordsOfArticleContent() {
		return shouldWordsOfArticleContent;
	}

	public void setShouldWordsOfArticleContent(List<String> shouldWordsOfArticleContent) {
		this.shouldWordsOfArticleContent = shouldWordsOfArticleContent;
	}

	public List<String> getMustNotWordsOfArticleContent() {
		return mustNotWordsOfArticleContent;
	}

	public void setMustNotWordsOfArticleContent(List<String> mustNotWordsOfArticleContent) {
		this.mustNotWordsOfArticleContent = mustNotWordsOfArticleContent;
	}

	public List<String> getMustArticleLabels() {
		return mustArticleLabels;
	}

	public void setMustArticleLabels(List<String> mustArticleLabels) {
		this.mustArticleLabels = mustArticleLabels;
	}

	public List<String> getShouldArticleLabels() {
		return shouldArticleLabels;
	}

	public void setShouldArticleLabels(List<String> shouldArticleLabels) {
		this.shouldArticleLabels = shouldArticleLabels;
	}

	public List<String> getMustNotArticleLabels() {
		return mustNotArticleLabels;
	}

	public void setMustNotArticleLabels(List<String> mustNotArticleLabels) {
		this.mustNotArticleLabels = mustNotArticleLabels;
	}

	public Long getStartPassTime() {
		return startPassTime;
	}

	public void setStartPassTime(Long startPassTime) {
		this.startPassTime = startPassTime;
	}

	public Long getEndPassTime() {
		return endPassTime;
	}

	public void setEndPassTime(Long endPassTime) {
		this.endPassTime = endPassTime;
	}

	public List<String> getPassUserNames() {
		return passUserNames;
	}

	public void setPassUserNames(List<String> passUserNames) {
		this.passUserNames = passUserNames;
	}

	public List<Integer> getPassUsers() {
		return passUsers;
	}

	public void setPassUsers(List<Integer> passUsers) {
		this.passUsers = passUsers;
	}

	public Long getStartInsertTime() {
		return startInsertTime;
	}

	public void setStartInsertTime(Long startInsertTime) {
		this.startInsertTime = startInsertTime;
	}

	public Long getEndInsertTime() {
		return endInsertTime;
	}

	public void setEndInsertTime(Long endInsertTime) {
		this.endInsertTime = endInsertTime;
	}

	public Long getStartUpdateTime() {
		return startUpdateTime;
	}

	public void setStartUpdateTime(Long startUpdateTime) {
		this.startUpdateTime = startUpdateTime;
	}

	public Long getEndUpdateTime() {
		return endUpdateTime;
	}

	public void setEndUpdateTime(Long endUpdateTime) {
		this.endUpdateTime = endUpdateTime;
	}

	public Long getStartDownTime() {
		return startDownTime;
	}

	public void setStartDownTime(Long startDownTime) {
		this.startDownTime = startDownTime;
	}

	public Long getEndDownTime() {
		return endDownTime;
	}

	public void setEndDownTime(Long endDownTime) {
		this.endDownTime = endDownTime;
	}

	public float getArticlePriceFrom() {
		return articlePriceFrom;
	}

	public void setArticlePriceFrom(float articlePriceFrom) {
		this.articlePriceFrom = articlePriceFrom;
	}

	public float getArticlePriceTo() {
		return articlePriceTo;
	}

	public void setArticlePriceTo(float articlePriceTo) {
		this.articlePriceTo = articlePriceTo;
	}

	public Long getStartSubmitTime() {
		return startSubmitTime;
	}

	public void setStartSubmitTime(Long startSubmitTime) {
		this.startSubmitTime = startSubmitTime;
	}

	public Long getEndSubmitTime() {
		return endSubmitTime;
	}

	public void setEndSubmitTime(Long endSubmitTime) {
		this.endSubmitTime = endSubmitTime;
	}

	public List<String> getMustWordsOfArticleSkip() {
		return mustWordsOfArticleSkip;
	}

	public void setMustWordsOfArticleSkip(List<String> mustWordsOfArticleSkip) {
		this.mustWordsOfArticleSkip = mustWordsOfArticleSkip;
	}

	public List<String> getMustNotWordsOfArticleSkip() {
		return mustNotWordsOfArticleSkip;
	}

	public void setMustNotWordsOfArticleSkip(List<String> mustNotWordsOfArticleSkip) {
		this.mustNotWordsOfArticleSkip = mustNotWordsOfArticleSkip;
	}

	public List<String> getShouldWordsOfArticleSkip() {
		return shouldWordsOfArticleSkip;
	}

	public void setShouldWordsOfArticleSkip(List<String> shouldWordsOfArticleSkip) {
		this.shouldWordsOfArticleSkip = shouldWordsOfArticleSkip;
	}

	public String getPassType() {
		return passType;
	}

	public void setPassType(String passType) {
		this.passType = passType;
	}

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public List<String> getArticleFormat() {
		return articleFormat;
	}

	public void setArticleFormat(List<String> articleFormat) {
		this.articleFormat = articleFormat;
	}

	public List<Integer> getJournalIds() {
		return journalIds;
	}

	public void setJournalIds(List<Integer> journalIds) {
		this.journalIds = journalIds;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public List<String> getMustArticleId() {
		return mustArticleId;
	}

	public void setMustArticleId(List<String> mustArticleId) {
		this.mustArticleId = mustArticleId;
	}

	public List<String> getMustNotArticleId() {
		return mustNotArticleId;
	}

	public void setMustNotArticleId(List<String> mustNotArticleId) {
		this.mustNotArticleId = mustNotArticleId;
	}

	public List<String> getShouldArticleId() {
		return shouldArticleId;
	}

	public void setShouldArticleId(List<String> shouldArticleId) {
		this.shouldArticleId = shouldArticleId;
	}

	public List<String> getMustJournalId() {
		return mustJournalId;
	}

	public void setMustJournalId(List<String> mustJournalId) {
		this.mustJournalId = mustJournalId;
	}

	public List<String> getMustNotJournalId() {
		return mustNotJournalId;
	}

	public void setMustNotJournalId(List<String> mustNotJournalId) {
		this.mustNotJournalId = mustNotJournalId;
	}

	public List<String> getShouldJournalid() {
		return shouldJournalid;
	}

	public void setShouldJournalid(List<String> shouldJournalid) {
		this.shouldJournalid = shouldJournalid;
	}

}
