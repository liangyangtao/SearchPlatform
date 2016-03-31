package com.unbank.entity;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;

import com.unbank.common.utils.CommonUtils;
import com.unbank.common.utils.QueryBuilderUtils;

public class NewsSearchCondition {
	
	public boolean isEmpty() {
		if (mustCrawlId == null && mustNotCrawlId == null
				&& shouldCrawlId == null && mustContentWords == null
				&& mustNotContentWords == null && shouldContentWords == null
				&& mustTitleWords == null && mustNotTitleWords == null
				&& shouldTitleWords == null && mustTagNames == null
				&& mustNotTagNames == null && shouldTagNames == null
				&& mustKeywords == null && mustNotKeywords == null
				&& shouldKeywords == null && mustWebNames == null
				&& mustNotWebNames == null && shouldWebNames == null
				&& mustSectionNames == null && mustNotSectionNames == null
				&& shouldSectionNames == null && mustExtraTags == null
				&& mustNotExtraTags == null && shouldExtraTags == null
				&& websiteIDs == null && extraTagNull == null
				&& picUrlNull == null && mustRegion == null
				&& mustNotRegion == null && shouldRegion == null
				&& termsMustContent == null && termsMustTitle == null) {
			return true;
		}
		return false;
	}
	
	/**
	 * 必须
	 */
	private List<String> mustCrawlId;
	/**
	 * 不能
	 */
	private List<String> mustNotCrawlId;
	
	/**
	 * 可以
	 */
	private List<String> shouldCrawlId;
	
	/**
	 * 必须被包含在正文中的词语
	 */
	private List<String> mustContentWords;
	
	/**
	 * 不能被包含在正文中的词语
	 */
	private List<String> mustNotContentWords;
	
	/**
	 * 可以出现在正文中的词语
	 */
	private List<String> shouldContentWords;

	private List<String> termsMustContent;
	
	/**
	 * 标题中必须出现的词语
	 */
	private List<String> mustTitleWords;
	
	/**
	 * 标题中不能出现的词语
	 */
	private List<String> mustNotTitleWords;
	
	/**
	 * 标题中可以出现的词语
	 */
	private List<String> shouldTitleWords;
	
	private List<String> termsMustTitle;
	
	/**
	 * 必须属于的标签
	 */
	private List<String> mustTagNames;
	
	/**
	 * 不能被包含的标签
	 */
	private List<String> mustNotTagNames;
	
	/**
	 * 可以出现的标签
	 */
	private List<String> shouldTagNames;
	
	/**
	 * 必须的关键词
	 */
	private List<String> mustKeywords;
	
	/**
	 * 不能出现的关键词
	 */
	private List<String> mustNotKeywords;
	
	/**
	 * 可以出现的关键词
	 */
	private List<String> shouldKeywords;
	
	/**
	 * 搜索必须从这些网站来的新闻
	 */
	private List<String> mustWebNames;
	
	/**
	 * 搜索不能从这些网站来的新闻
	 */
	private List<String> mustNotWebNames;
	
	/**
	 * 搜索可以从这些网站来的新闻
	 */
	private List<String> shouldWebNames;
	
	/**
	 * 搜索必须从这些网站来的新闻
	 */
	private List<String> mustSectionNames;
	
	/**
	 * 搜索不能从这些板块来的新闻
	 */
	private List<String> mustNotSectionNames;
	
	/**
	 * 搜索可以从这些板块来的新闻
	 */
	private List<String> shouldSectionNames;
	
	/**
	 * 必须属于的扩展标签
	 */
	private List<String> mustExtraTags;
	
	/**
	 * 不能属于的扩展标签
	 */
	private List<String> mustNotExtraTags;
	
	/**
	 * 可以属于的扩展标签
	 */
	private List<String> shouldExtraTags;
	
	/**
	 * 来源网址
	 */
	private List<Integer> websiteIDs;

	/**
	 * 是否允许extraTag为空
	 */
	private String extraTagNull;
	
	/**
	 * 图片是否为空
	 */
	private String picUrlNull;
	
	/**
	 * 必须包含的区域
	 */
	private List<String> mustRegion;
	
	/**
	 * 不能包含的区域
	 */
	private List<String> mustNotRegion;
	
	/**
	 * 可以包含的区域
	 */
	private List<String> shouldRegion;
	
	public BoolQueryBuilder getQueryBuilder(boolean searchFlag){
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		if (searchFlag) {
			if (CommonUtils.isNotEmpty(mustCrawlId) || CommonUtils.isNotEmpty(mustNotCrawlId) || CommonUtils.isNotEmpty(shouldCrawlId)) {
				composeQueryBuilder(mustCrawlId, mustNotCrawlId, shouldCrawlId, "crawl_id", boolQueryBuilder, shouldWebNames);
			}
			
			if (CommonUtils.isNotEmpty(mustTagNames) || CommonUtils.isNotEmpty(mustNotTagNames) || CommonUtils.isNotEmpty(shouldTagNames)) {
				tagNameQueryBuilder(mustTagNames, mustNotTagNames, shouldTagNames, "tagName", boolQueryBuilder, shouldWebNames);
			}
			if(CommonUtils.isNotEmpty(mustContentWords) || CommonUtils.isNotEmpty(mustNotContentWords)
					|| CommonUtils.isNotEmpty(shouldContentWords) || CommonUtils.isNotEmpty(termsMustContent)){
				titleContentQueryBuilder(mustContentWords, mustNotContentWords, shouldContentWords, termsMustContent, "content", boolQueryBuilder, shouldWebNames);
			}
			if(CommonUtils.isNotEmpty(mustTitleWords) || CommonUtils.isNotEmpty(mustNotTitleWords)
					|| CommonUtils.isNotEmpty(shouldTitleWords) || CommonUtils.isNotEmpty(termsMustTitle)){
				titleContentQueryBuilder(mustTitleWords, mustNotTitleWords, shouldTitleWords, termsMustTitle, "title", boolQueryBuilder, shouldWebNames);
			}
			if(CommonUtils.isNotEmpty(mustKeywords) ||CommonUtils.isNotEmpty(mustNotKeywords) ||CommonUtils.isNotEmpty(shouldKeywords) ){
				composeQueryBuilder(mustKeywords, mustNotKeywords, shouldKeywords, "keyWords", boolQueryBuilder, shouldWebNames);
			}
			if(CommonUtils.isNotEmpty(mustSectionNames) || CommonUtils.isNotEmpty(mustNotSectionNames) || CommonUtils.isNotEmpty(shouldSectionNames)){
				composeQueryBuilder(mustSectionNames,mustNotSectionNames,shouldSectionNames,"webSectionName", boolQueryBuilder, shouldWebNames);
			}
			if(CommonUtils.isNotEmpty(mustExtraTags) || CommonUtils.isNotEmpty(mustNotExtraTags) || CommonUtils.isNotEmpty(shouldExtraTags) ){
				composeQueryBuilder(mustExtraTags, mustNotExtraTags, shouldExtraTags, "extraTag", boolQueryBuilder, shouldWebNames);
			}
			
			if (CommonUtils.isNotEmpty(mustRegion) || CommonUtils.isNotEmpty(mustNotRegion) || CommonUtils.isNotEmpty(shouldRegion)) {
				composeQueryBuilder(mustRegion, mustNotRegion, shouldRegion, "region", boolQueryBuilder, shouldWebNames);
			}
		} else {
			if(mustTagNames!=null||mustNotTagNames!=null||shouldTagNames!=null){
				BoolQueryBuilder tagNameQueryBuilder = QueryBuilderUtils.termStrQueryBuilder(mustTagNames, mustNotTagNames, shouldTagNames, "tagName");
				boolQueryBuilder.must(tagNameQueryBuilder);
			}
			if(mustContentWords!=null||mustNotContentWords!=null||shouldContentWords!=null || CommonUtils.isNotEmpty(termsMustContent)){
				BoolQueryBuilder contentQueryBuilder = QueryBuilderUtils.matchPhraseQueryBuilder(mustContentWords, mustNotContentWords, shouldContentWords, termsMustContent, "content");
				boolQueryBuilder.must(contentQueryBuilder);
			}
			if(mustTitleWords!=null||mustNotTitleWords!=null||shouldTitleWords!=null || CommonUtils.isNotEmpty(termsMustTitle)){
				BoolQueryBuilder titleQueryBuilder = QueryBuilderUtils.matchPhraseQueryBuilder(mustTitleWords, mustNotTitleWords, shouldTitleWords, termsMustTitle, "title");
				boolQueryBuilder.must(titleQueryBuilder);
			}
			if(mustKeywords!=null||mustNotKeywords!=null||shouldKeywords!=null){
				BoolQueryBuilder keywordQueryBuilder = QueryBuilderUtils.termStrQueryBuilder(mustKeywords, mustNotKeywords, shouldKeywords, "keyWords");
				boolQueryBuilder.must(keywordQueryBuilder);
			}
			if(mustWebNames!=null||mustNotWebNames!=null||shouldWebNames!=null){
				BoolQueryBuilder webnameQueryBuilder = QueryBuilderUtils.termStrQueryBuilder(mustWebNames,mustNotWebNames,shouldWebNames,"webName");
				boolQueryBuilder.must(webnameQueryBuilder);
			}
			if(mustSectionNames!=null||mustNotSectionNames!=null||shouldSectionNames!=null){
				BoolQueryBuilder sectionNameQueryBuilder = QueryBuilderUtils.termStrQueryBuilder(mustSectionNames,mustNotSectionNames,shouldSectionNames,"webSectionName");
				boolQueryBuilder.must(sectionNameQueryBuilder);
			}
			if(mustExtraTags!=null||mustNotExtraTags!=null||shouldExtraTags!=null){
				BoolQueryBuilder extraTagQueryBuilder = QueryBuilderUtils.termStrQueryBuilder(mustExtraTags, mustNotExtraTags, shouldExtraTags, "extraTag");
				boolQueryBuilder.must(extraTagQueryBuilder);
			}
		}
		
		if (CommonUtils.isNotEmpty(websiteIDs)) {
			BoolQueryBuilder websiteIDQueryBuilder = QueryBuilderUtils.termIntQueryBuilder(null, null, websiteIDs, "website_id");
			boolQueryBuilder.must(websiteIDQueryBuilder);
		}
		if ("true".equals(extraTagNull)) {
			boolQueryBuilder.must(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), FilterBuilders.missingFilter("extraTag")));
		} else if("false".equals(extraTagNull)) {
			boolQueryBuilder.mustNot(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), FilterBuilders.missingFilter("extraTag")));
		}
		
		if ("true".equals(picUrlNull)) {
			boolQueryBuilder.must(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), FilterBuilders.missingFilter("picUrl")));
		} else if ("false".equals(picUrlNull)) {
			boolQueryBuilder.mustNot(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), FilterBuilders.missingFilter("picUrl")));
		}

		return boolQueryBuilder;
	}
	
	// tag
	private BoolQueryBuilder tagNameQueryBuilder(List<String> mustCondition, List<String> mustNotCondition,
			List<String> shouldCondition, String fieldName, BoolQueryBuilder boolQueryBuilder, List<String> shouldWebNames2) {
		if(CommonUtils.isEmpty(shouldWebNames2)){
			if (CommonUtils.isNotEmpty(mustCondition) && CommonUtils.isNotEmpty(shouldCondition)) {
				// 获取转换数据
				List<List<String>> result = CommonUtils.convertList2(mustCondition, shouldCondition);
				if (CommonUtils.isNotEmpty(result)) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					for (List<String> ls : result) {
						BoolQueryBuilder t = QueryBuilders.boolQuery();
						for (String c : ls) {
							t.must(QueryBuilders.termQuery(fieldName, c));
						}
						query.should(t);
						
					}
					boolQueryBuilder.must(query);
				}
			}else if (CommonUtils.isNotEmpty(mustCondition)) {
				BoolQueryBuilder query = QueryBuilders.boolQuery();
				for (String c : mustCondition) {
					BoolQueryBuilder t = QueryBuilders.boolQuery();
					t.must(QueryBuilders.termQuery(fieldName, c));
					query.must(t);
				}
				boolQueryBuilder.must(query);
			}else  if (CommonUtils.isNotEmpty(shouldCondition)) {
				BoolQueryBuilder query = QueryBuilders.boolQuery();
				for (String c : shouldCondition) {
					BoolQueryBuilder t = QueryBuilders.boolQuery();
					t.must(QueryBuilders.termQuery(fieldName, c));
					query.should(t);
				}
				boolQueryBuilder.must(query);
			}

			if (CommonUtils.isNotEmpty(mustNotCondition)) {
				BoolQueryBuilder query = QueryBuilders.boolQuery();
				BoolQueryBuilder temp = QueryBuilders.boolQuery();
				for (String c : mustNotCondition) {
					temp.must(QueryBuilders.termQuery(fieldName, c));
				}
				query.mustNot(temp);
				boolQueryBuilder.must(query);
			}
		}else {
			if (CommonUtils.isNotEmpty(mustCondition) && CommonUtils.isNotEmpty(shouldCondition)) {
				// 获取转换数据
				List<List<String>> result = CommonUtils.convertList2(mustCondition, shouldCondition);
				if (CommonUtils.isNotEmpty(result)) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					for (String str : shouldWebNames2) {
						BoolQueryBuilder tt = QueryBuilders.boolQuery();
						for (List<String> ls : result) {
							BoolQueryBuilder t = QueryBuilders.boolQuery();
							t.must(QueryBuilders.matchPhraseQuery("webName", str));
							for (String c : ls) {
								t.must(QueryBuilders.termQuery(fieldName, c));
							}
							tt.should(t);
						}
						query.should(tt);
					}
					
					boolQueryBuilder.must(query);
				}
			}else if (CommonUtils.isNotEmpty(mustCondition)) {
				BoolQueryBuilder query = QueryBuilders.boolQuery();
				for (String str : shouldWebNames2) {
					BoolQueryBuilder t = QueryBuilders.boolQuery();
					t.must(QueryBuilders.matchPhraseQuery("webName", str));
					for (String c : mustCondition) {
						t.must(QueryBuilders.termQuery(fieldName, c));
					}
					query.should(t);
				}
				boolQueryBuilder.must(query);
			}else  if (CommonUtils.isNotEmpty(shouldCondition)) {
				BoolQueryBuilder query = QueryBuilders.boolQuery();
				for (String str : shouldWebNames2) {
					BoolQueryBuilder t = QueryBuilders.boolQuery();
					t.must(QueryBuilders.matchPhraseQuery("webName", str));
					for (String c : shouldCondition) {
						t.must(QueryBuilders.termQuery(fieldName, c));
					}
					query.should(t);
				}
				boolQueryBuilder.must(query);
			}

			if (CommonUtils.isNotEmpty(mustNotCondition)) {
				BoolQueryBuilder query = QueryBuilders.boolQuery();
				for (String str : shouldWebNames2) {
					BoolQueryBuilder t = QueryBuilders.boolQuery();
					t.must(QueryBuilders.matchPhraseQuery("webName", str));
					for (String c : mustNotCondition) {
						t.must(QueryBuilders.termQuery(fieldName, c));
					}
					query.mustNot(t);
				}
				boolQueryBuilder.must(query);
			}
		}
		
		return boolQueryBuilder;
	}

	/**
	 * term查询
	 * @param mustCondition
	 * @param mustNotCondition
	 * @param shouldCondition
	 * @param fieldName
	 * @param boolQueryBuilder
	 * @param shouldWebNames2
	 * @return
	 */
	private BoolQueryBuilder composeQueryBuilder(List<String> mustCondition, List<String> mustNotCondition,
			List<String> shouldCondition, String fieldName, BoolQueryBuilder boolQueryBuilder, List<String> shouldWebNames2) {
		if(CommonUtils.isNotEmpty(shouldWebNames2)){
			if (CommonUtils.isNotEmpty(mustCondition)) {
				BoolQueryBuilder mustTagsBuilder = QueryBuilders.boolQuery();
				for (String c : mustCondition) {
					BoolQueryBuilder temp = QueryBuilders.boolQuery();
					temp.must(QueryBuilders.termQuery(fieldName, c));
					for (String s : shouldWebNames2) {
						temp.must(QueryBuilders.matchPhraseQuery("webName", s));
					}
					mustTagsBuilder.must(temp);
				}
				boolQueryBuilder.must(mustTagsBuilder);
			}

			if (CommonUtils.isNotEmpty(shouldCondition)) {
				BoolQueryBuilder shouldTagsBuilder = QueryBuilders.boolQuery();
				for (String c : shouldCondition) {
					BoolQueryBuilder temp = QueryBuilders.boolQuery();
					temp.must(QueryBuilders.termQuery(fieldName, c));
					for (String s : shouldWebNames2) {
						temp.must(QueryBuilders.matchPhraseQuery("webName", s));
					}
					shouldTagsBuilder.should(temp);
				}
				boolQueryBuilder.must(shouldTagsBuilder);
			}

			if (CommonUtils.isNotEmpty(mustNotCondition)) {
				BoolQueryBuilder mustNotTagsBuilder = QueryBuilders.boolQuery();
				for (String c : mustNotCondition) {
					mustNotTagsBuilder.mustNot(QueryBuilders.termQuery(fieldName, c));
					for (String s : shouldWebNames2) {
						mustNotTagsBuilder.must(QueryBuilders.matchPhraseQuery("webName", s));
					}
				}
				boolQueryBuilder.must(mustNotTagsBuilder);
			}
		} else {
			if (CommonUtils.isNotEmpty(mustCondition)) {
				BoolQueryBuilder mustTagsBuilder = QueryBuilders.boolQuery();
				for (String c : mustCondition) {
					mustTagsBuilder.must(QueryBuilders.termQuery(fieldName, c));
				}
				boolQueryBuilder.must(mustTagsBuilder);
			}

			if (CommonUtils.isNotEmpty(shouldCondition)) {
				BoolQueryBuilder shouldTagsBuilder = QueryBuilders.boolQuery();
				for (String c : shouldCondition) {
					shouldTagsBuilder.should(QueryBuilders.termQuery(fieldName, c));
				}
				boolQueryBuilder.should(shouldTagsBuilder);
			}

			if (CommonUtils.isNotEmpty(mustNotCondition)) {
				BoolQueryBuilder mustNotTagsBuilder = QueryBuilders.boolQuery();
				for (String c : mustNotCondition) {
					mustNotTagsBuilder.mustNot(QueryBuilders.termQuery(fieldName, c));
				}
				boolQueryBuilder.must(mustNotTagsBuilder);
			}
		}
		

		return boolQueryBuilder;
	}
	
	// 字符串搜索
	private BoolQueryBuilder titleContentQueryBuilder(List<String> mustCondition, List<String> mustNotCondition,
			List<String> shouldCondition, List<String> termsMustContent, String fieldName, BoolQueryBuilder boolQueryBuilder, List<String> shouldWebNames2) {
		if(CommonUtils.isEmpty(shouldWebNames2)){
			if (CommonUtils.isNotEmpty(mustCondition) && CommonUtils.isNotEmpty(shouldCondition)) {
				// 获取转换数据
				List<List<String>> result = CommonUtils.convertList2(mustCondition, shouldCondition);
				if (CommonUtils.isNotEmpty(result)) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					for (List<String> ls : result) {
						BoolQueryBuilder tt = QueryBuilders.boolQuery();
						for (String s : ls) {
							tt.must(QueryBuilders.matchPhraseQuery(fieldName, s));
						}
						query.should(tt);
					}
					
					boolQueryBuilder.must(query);
				}
			}else if (CommonUtils.isNotEmpty(mustCondition)) {
				// 只有must时，要全部匹配
				BoolQueryBuilder query = QueryBuilders.boolQuery();
				for (String c : mustCondition) {
					query.must(QueryBuilders.matchPhraseQuery(fieldName, c));
				}
				boolQueryBuilder.must(query);
			}else  if (CommonUtils.isNotEmpty(shouldCondition)) {
				BoolQueryBuilder query = QueryBuilders.boolQuery();
				for (String c : shouldCondition) {
					BoolQueryBuilder t = QueryBuilders.boolQuery();
					t.must(QueryBuilders.matchPhraseQuery(fieldName, c));
					query.should(t);
				}
				boolQueryBuilder.must(query);
			}

			if (CommonUtils.isNotEmpty(mustNotCondition)) {
				BoolQueryBuilder mustNotTagsBuilder = QueryBuilders.boolQuery();
				for (String c : mustNotCondition) {
					mustNotTagsBuilder.mustNot(QueryBuilders.matchPhraseQuery(fieldName, c));
				}
				boolQueryBuilder.must(mustNotTagsBuilder);
			}
			if(CommonUtils.isNotEmpty(termsMustContent)){
				BoolQueryBuilder query = QueryBuilders.boolQuery();
				for (String c : termsMustContent) {
					query.should(QueryBuilders.matchPhraseQuery(fieldName, c));
				}
				boolQueryBuilder.must(query);
			}
		}else {
			if (CommonUtils.isNotEmpty(mustCondition) && CommonUtils.isNotEmpty(shouldCondition)) {
				// 获取转换数据
				List<List<String>> result = CommonUtils.convertList2(mustCondition, shouldCondition);
				if (CommonUtils.isNotEmpty(result)) {
					BoolQueryBuilder query = QueryBuilders.boolQuery();
					for (String str : shouldWebNames2) {
						BoolQueryBuilder tt = QueryBuilders.boolQuery();
						for (List<String> ls : result) {
							BoolQueryBuilder t = QueryBuilders.boolQuery();
							t.must(QueryBuilders.matchPhraseQuery("webName", str));
							for (String s : ls) {
								t.must(QueryBuilders.matchPhraseQuery(fieldName, s));
							}
							tt.should(t);
						}
						query.should(tt);
					}
					
					boolQueryBuilder.must(query);
				}
			}else if (CommonUtils.isNotEmpty(mustCondition)) {
				// 只有must时，要全部匹配
				BoolQueryBuilder query = QueryBuilders.boolQuery();
				for (String str : shouldWebNames2) {
					BoolQueryBuilder tt = QueryBuilders.boolQuery();
					tt.must(QueryBuilders.matchPhraseQuery("webName", str));
					for (String c : mustCondition) {
						tt.must(QueryBuilders.matchPhraseQuery(fieldName, c));
					}
					query.should(tt);
				}
				boolQueryBuilder.must(query);
			}else  if (CommonUtils.isNotEmpty(shouldCondition)) {
				BoolQueryBuilder query = QueryBuilders.boolQuery();
				for (String str : shouldWebNames2) {
					BoolQueryBuilder tt = QueryBuilders.boolQuery();
					for (String c : shouldCondition) {
						BoolQueryBuilder t = QueryBuilders.boolQuery();
						t.must(QueryBuilders.matchPhraseQuery("webName", str));
						t.must(QueryBuilders.matchPhraseQuery(fieldName, c));
						tt.should(t);
					}
					query.should(tt);
				}
				boolQueryBuilder.must(query);
			}

			if (CommonUtils.isNotEmpty(mustNotCondition)) {
				BoolQueryBuilder query = QueryBuilders.boolQuery();
				for (String str : shouldWebNames2) {
					BoolQueryBuilder tt = QueryBuilders.boolQuery();
					for (String c : mustNotCondition) {
						BoolQueryBuilder t = QueryBuilders.boolQuery();
						t.must(QueryBuilders.matchPhraseQuery("webName", str));
						t.must(QueryBuilders.matchPhraseQuery(fieldName, c));
						tt.mustNot(t);
					}
					query.must(tt);
				}
				boolQueryBuilder.must(query);
			}
		}
		
		return boolQueryBuilder;
	}
	
	public List<String> getMustContentWords() {
		return mustContentWords;
	}

	public void setMustContentWords(List<String> mustContentWords) {
		this.mustContentWords = mustContentWords;
	}

	public List<String> getMustNotContentWords() {
		return mustNotContentWords;
	}

	public void setMustNotContentWords(List<String> mustNotContentWords) {
		this.mustNotContentWords = mustNotContentWords;
	}

	public List<String> getShouldContentWords() {
		return shouldContentWords;
	}

	public void setShouldContentWords(List<String> shouldContentWords) {
		this.shouldContentWords = shouldContentWords;
	}

	public List<String> getMustTitleWords() {
		return mustTitleWords;
	}

	public void setMustTitleWords(List<String> mustTitleWords) {
		this.mustTitleWords = mustTitleWords;
	}

	public List<String> getMustNotTitleWords() {
		return mustNotTitleWords;
	}

	public void setMustNotTitleWords(List<String> mustNotTitleWords) {
		this.mustNotTitleWords = mustNotTitleWords;
	}

	public List<String> getShouldTitleWords() {
		return shouldTitleWords;
	}

	public void setShouldTitleWords(List<String> shouldTitleWords) {
		this.shouldTitleWords = shouldTitleWords;
	}

	public List<String> getMustTagNames() {
		return mustTagNames;
	}

	public void setMustTagNames(List<String> mustTagNames) {
		this.mustTagNames = mustTagNames;
	}

	public List<String> getMustNotTagNames() {
		return mustNotTagNames;
	}

	public void setMustNotTagNames(List<String> mustNotTagNames) {
		this.mustNotTagNames = mustNotTagNames;
	}

	public List<String> getShouldTagNames() {
		return shouldTagNames;
	}

	public void setShouldTagNames(List<String> shouldTagNames) {
		this.shouldTagNames = shouldTagNames;
	}

	public List<String> getMustKeywords() {
		return mustKeywords;
	}

	public void setMustKeywords(List<String> mustKeywords) {
		this.mustKeywords = mustKeywords;
	}

	public List<String> getMustNotKeywords() {
		return mustNotKeywords;
	}

	public void setMustNotKeywords(List<String> mustNotKeywords) {
		this.mustNotKeywords = mustNotKeywords;
	}

	public List<String> getShouldKeywords() {
		return shouldKeywords;
	}

	public void setShouldKeywords(List<String> shouldKeywords) {
		this.shouldKeywords = shouldKeywords;
	}

	public List<String> getMustWebNames() {
		return mustWebNames;
	}

	public void setMustWebNames(List<String> mustWebNames) {
		this.mustWebNames = mustWebNames;
	}

	public List<String> getMustNotWebNames() {
		return mustNotWebNames;
	}

	public void setMustNotWebNames(List<String> mustNotWebNames) {
		this.mustNotWebNames = mustNotWebNames;
	}

	public List<String> getShouldWebNames() {
		return shouldWebNames;
	}

	public void setShouldWebNames(List<String> shouldWebNames) {
		this.shouldWebNames = shouldWebNames;
	}

	public List<String> getMustSectionNames() {
		return mustSectionNames;
	}

	public void setMustSectionNames(List<String> mustSectionNames) {
		this.mustSectionNames = mustSectionNames;
	}

	public List<String> getMustNotSectionNames() {
		return mustNotSectionNames;
	}

	public void setMustNotSectionNames(List<String> mustNotSectionNames) {
		this.mustNotSectionNames = mustNotSectionNames;
	}

	public List<String> getShouldSectionNames() {
		return shouldSectionNames;
	}

	public void setShouldSectionNames(List<String> shouldSectionNames) {
		this.shouldSectionNames = shouldSectionNames;
	}

	public List<String> getMustExtraTags() {
		return mustExtraTags;
	}

	public void setMustExtraTags(List<String> mustExtraTags) {
		this.mustExtraTags = mustExtraTags;
	}

	public List<String> getMustNotExtraTags() {
		return mustNotExtraTags;
	}

	public void setMustNotExtraTags(List<String> mustNotExtraTags) {
		this.mustNotExtraTags = mustNotExtraTags;
	}

	public List<String> getShouldExtraTags() {
		return shouldExtraTags;
	}

	public void setShouldExtraTags(List<String> shouldExtraTags) {
		this.shouldExtraTags = shouldExtraTags;
	}

	public List<Integer> getWebsiteIDs() {
		return websiteIDs;
	}

	public void setWebsiteIDs(List<Integer> websiteIDs) {
		this.websiteIDs = websiteIDs;
	}

	public String isExtraTagNull() {
		return extraTagNull;
	}

	public void setExtraTagNull(String extraTagNull) {
		this.extraTagNull = extraTagNull;
	}
	
	public String getPicUrlNull() {
		return picUrlNull;
	}
	
	public void setPicUrlNull(String picUrlNull) {
		this.picUrlNull = picUrlNull;
	}

	public String getExtraTagNull() {
		return extraTagNull;
	}
	
	public List<String> getMustRegion() {
		return mustRegion;
	}

	public void setMustRegion(List<String> mustRegion) {
		this.mustRegion = mustRegion;
	}

	public List<String> getMustNotRegion() {
		return mustNotRegion;
	}

	public void setMustNotRegion(List<String> mustNotRegion) {
		this.mustNotRegion = mustNotRegion;
	}

	public List<String> getShouldRegion() {
		return shouldRegion;
	}

	public void setShouldRegion(List<String> shouldRegion) {
		this.shouldRegion = shouldRegion;
	}

	public List<String> getMustCrawlId() {
		return mustCrawlId;
	}

	public void setMustCrawlId(List<String> mustCrawlId) {
		this.mustCrawlId = mustCrawlId;
	}

	public List<String> getMustNotCrawlId() {
		return mustNotCrawlId;
	}

	public void setMustNotCrawlId(List<String> mustNotCrawlId) {
		this.mustNotCrawlId = mustNotCrawlId;
	}

	public List<String> getShouldCrawlId() {
		return shouldCrawlId;
	}

	public void setShouldCrawlId(List<String> shouldCrawlId) {
		this.shouldCrawlId = shouldCrawlId;
	}

	public List<String> getTermsMustContent() {
		return termsMustContent;
	}


	public void setTermsMustContent(List<String> termsMustContent) {
		this.termsMustContent = termsMustContent;
	}


	public List<String> getTermsMustTitle() {
		return termsMustTitle;
	}


	public void setTermsMustTitle(List<String> termsMustTitle) {
		this.termsMustTitle = termsMustTitle;
	}

}
