package com.unbank.common.utils;

import java.util.Map;

import org.elasticsearch.search.highlight.HighlightField;
import org.jsoup.Jsoup;

import com.unbank.common.constants.SysParameters;
import com.unbank.entity.News;
import com.unbank.entity.SearchNews;
import com.unbank.entity.risk.SearchRisk;
import com.unbank.es.document.Document;
import com.unbank.es.journal.Journal;

/**
 * 封装ES读取数据
 * @author LiuLei
 *
 */
public class SourcesUtils {
	
	/**
	 * 封装文档数据
	 * @param source
	 * @return
	 */
	public static Document getDocumentInfo(Map<String, Object> source, boolean showContent) {
		// 封装文档信息
		Document info = new Document();
		// 文档ID
		info.setArticleId(getIntValue(source.get("articleId")));
		// 文档所属项目（文档来源的平台）
		info.setArticleProject(getStringValue(source.get("articleProject")));
		// 文档标题
		info.setArticleName(getStringValue(source.get("articleName")));
		// 文档类型
		info.setArticleType(getStringValue(source.get("articleType")));
		// 用户名
		info.setArticleUser(getStringValue(source.get("articleUser")));
		// 用户ID
		info.setUserId(getIntValue(source.get("userId")));
		// 文档摘要
		info.setArticleBrief(getStringValue(source.get("articleBrief")));
		
		// 文档内容 --- 内容过大，数据不取
		if(showContent){
			info.setArticleContent(getStringValue(source.get("articleContent")));
		}
		
		// 封面
		info.setArticleCover(getStringValue(source.get("articleCover")));
		// 标签
		info.setArticleLabel(getStringValue(source.get("articleLabel")));
		// 关键字
		info.setArticleKeyWord(getStringValue(source.get("articleKeyWord")));
		// 存储类别
		info.setPassType(getStringValue(source.get("passType")));
		// 提交时间
		info.setSubmitTime(getLongValue(source.get("submitTime")));
		// 审核时间
		info.setPassTime(getLongValue(source.get("passTime")));
		// 审核人
		info.setPassUserName(getStringValue(source.get("passUserName")));
		// 审核人ID
		info.setPassUser(getIntValue(source.get("passUser")));
		// 建立文档的时间
		info.setInsertTime(getLongValue(source.get("insertTime")));
		// 更新时间
		info.setUpdateTime(getLongValue(source.get("updateTime")));
		// 生成word的时间
		info.setDownTime(getLongValue(source.get("downTime")));
		// 生成的文档ID
		info.setDocId(getIntValue(source.get("docId")));
		// 文档保存路径
		info.setDocPath(getStringValue(source.get("docPath")));
		// 文档生成的HTML保存路径
		info.setHtmlPath(getStringValue(source.get("htmlPath")));
		// 文档价格
		info.setArticlePrice(getFloatValue(source.get("articlePrice")));
		// 扩展字段
		info.setArticleInfo(getStringValue(source.get("articleInfo")));
		// 文档简介
		info.setArticleSkip(getStringValue(source.get("articleSkip")));
		// 文档生成的HTML文件名称
		info.setHtmlName(getStringValue(source.get("htmlName")));
		// 文档生成的HTML页数
		info.setHtmlPage(getStringValue(source.get("htmlPage")));
		// 文档操作类型
		info.setArticleSave(getStringValue(source.get("articleSave")));
		// 文档格式
		Object articleFormat = source.get("articleFormat");
		info.setArticleFormat(articleFormat == null ? "doc" : articleFormat.toString());
		// 期刊ID
		info.setArticleJournalId(getIntValue(source.get("articleJournalId")));
		// 刊次名称
		info.setArticleJournalName(getStringValue(source.get("articleJournalName")));
		// 期刊点击数
		info.setArticleJournalTime(getLongValue(source.get("articleJournalTime")));
		
		return info;
	}
	
	/**
	 * 封装期刊数据
	 * @param source
	 * @return
	 */
	public static Journal getJournalInfo(Map<String, Object> source) {
		Journal info = new Journal();
		// ESID
		info.setEsId(getStringValue(source.get("esId")));
		// 期刊ID
		info.setJournalId(getIntValue(source.get("journalId")));
		// 期刊名称
		info.setName(getStringValue(source.get("name")));
		// 期刊简介
		info.setSkip(getStringValue(source.get("skip")));
		// 期刊标签
		info.setLabel(getStringValue(source.get("label")));
		// 期刊关键字
		info.setKeyWord(getStringValue(source.get("keyWord")));
		//期刊封面图片URL
		info.setCover(getStringValue(source.get("cover")));
		// 期刊类型
		info.setType(getStringValue(source.get("type")));
		// 期刊类型ID  对应数据库
		info.setTypeId(getIntValue(source.get("typeId")));
		// 保存状态
		info.setPassType(getStringValue(source.get("passType")));
		// 提交用户ID
		info.setSubmitUserId(getIntValue(source.get("submitUserId")));
		// 提交用户名称
		info.setSubmitUserName(getStringValue(source.get("submitUserName")));
		// 提交时间
		info.setSubmitTime(getLongValue(source.get("submitTime")));
		// 创建时间（第一次提交的时间，一直不变的字段）
		info.setCreateTime(getLongValue(source.get("createTime")));
		// 审核人ID
		info.setPassUserId(getIntValue(source.get("passUserId")));
		// 审核人名称
		info.setPassUserName(getStringValue(source.get("passUserName")));
		// 审核通过时间
		info.setPassTime(getLongValue(source.get("passTime")));
		// 价格
		info.setPrice(getDoubleValue(source.get("price")));
		// 期刊下是否有文档
		info.setHaveData(getIntValue(source.get("haveData")));
		// 期刊更新文档时间
		info.setUpdateTime(getLongValue(source.get("updateTime")));
		
		return info;
	}

	/**
	 * 封装新闻
	 * @param source 数据源
	 * @param showContent 是否显示新闻内容
	 * @return newsInfo
	 */
	public static SearchNews getNewsInfo(Map<String, Object> source, boolean showContent) {
		SearchNews info = new SearchNews();
		// 新闻ID
		info.setCrawl_id(getIntValue(source.get("crawl_id")));
		// 新闻URL
		info.setUrl(getStringValue(source.get("url")));
		// 新闻标题
		info.setTitle(getStringValue(source.get("title")));
		
		// 新闻内容
		String content = getStringValue(source.get("content"));
		if(showContent){
			info.setContent(content);
		}else {
			org.jsoup.nodes.Document doc = Jsoup.parse(content);
			String d_content=doc.body().text();
			
			if(SysParameters.CONTENTLENGHT>0){
				String _content=CommonUtils.substring(d_content, 0, SysParameters.CONTENTLENGHT)+"...";
				info.setContent(_content);
			}
		}
		
		// 新闻时间
		info.setNewsDate(getLongValue(source.get("newsDate")));
		if(CommonUtils.isNotEmpty(info.getNewsDate())){
			// 新闻发布时间
			Long newTime = info.getNewsDate();
			
			if(newTime > System.currentTimeMillis()){
				info.setNewsDate(System.currentTimeMillis());
			}
		}
		// 新闻抓取时间
		info.setCrawlDate(getLongValue(source.get("crawlDate")));
		// 新闻来源网站名称
		info.setWebName(getStringValue(source.get("webName")));
		// 新闻来源板块名称
		info.setWebSectionName(getStringValue(source.get("webSectionName")));
		// 新闻来源对应的Website_id
		info.setWebsite_id(getStringValue(source.get("website_id")));
		// 新闻来源（信息源）的URL
		info.setP_url(getStringValue(source.get("p_url")));
		// 新闻所属标签名称
		info.setTagName(getStringValue(source.get("tagName")));
		// 新闻关键词
		info.setKeyWords(getStringValue(source.get("keyWords")));
		// extraTag
		info.setExtraTag(getStringValue(source.get("extraTag")));
		// 图片URL
		info.setPicUrl(getStringValue(source.get("picUrl")));
		// 区域
		info.setRegion(getStringValue(source.get("region")));
		
		return info;
	}
	
	/**
	 * 封装新闻
	 * @param source 数据源
	 * @param showContent 是否显示新闻内容
	 * @return news
	 */
	public static News getNews(Map<String, Object> source, boolean showContent) {
		News info = new News();
		// 新闻ID
		info.setCrawl_id(getIntValue(source.get("crawl_id")));
		// 新闻URL
		info.setUrl(getStringValue(source.get("url")));
		// 新闻标题
		info.setTitle(getStringValue(source.get("title")));
		
		// 新闻内容
		String content = getStringValue(source.get("content"));
		if(showContent){
			info.setContent(content);
		}else {
			org.jsoup.nodes.Document doc = Jsoup.parse(content);
			String d_content=doc.body().text();
			
			if(SysParameters.CONTENTLENGHT>0){
				String _content=CommonUtils.substring(d_content, 0, SysParameters.CONTENTLENGHT)+"...";
				info.setContent(_content);
			}
		}
		// 新闻时间
		info.setNewsDate(getLongValue(source.get("newsDate")));
		if(CommonUtils.isNotEmpty(info.getNewsDate())){
			// 新闻发布时间
			Long newTime = info.getNewsDate();
			
			if(newTime > System.currentTimeMillis()){
				info.setNewsDate(System.currentTimeMillis());
			}
		}
		// 新闻抓取时间
		info.setCrawlDate(getLongValue(source.get("crawlDate")));
		// 新闻来源网站名称
		info.setWebName(getStringValue(source.get("webName")));
		// 新闻来源板块名称
		info.setWebSectionName(getStringValue(source.get("webSectionName")));
		// 新闻来源对应的Website_id
		info.setWebsite_id(getStringValue(source.get("website_id")));
		// 新闻来源（信息源）的URL
		info.setP_url(getStringValue(source.get("p_url")));
		// 新闻所属标签名称
		info.setTagName(getStringValue(source.get("tagName")));
		// 新闻关键词
		info.setKeyWords(getStringValue(source.get("keyWords")));
		// extraTag
		info.setExtraTag(getStringValue(source.get("extraTag")));
		// 图片URL
		info.setPicUrl(getStringValue(source.get("picUrl")));
		// 区域
		info.setRegion(getStringValue(source.get("region")));
		
		return info;
	}
	
	/**
	 * 风险
	 * @param source
	 * @param showContent
	 * @return
	 */
	public static SearchRisk getRiskInfo(Map<String, Object> source, boolean showContent) {
		SearchRisk info = new SearchRisk();
		info.setEsId(getStringValue(source.get("esId")));
		info.setCrawl_id(getIntValue(source.get("crawl_id")));
		info.setUrl(getStringValue(source.get("url")));
		info.setTitle(getStringValue(source.get("title")));

		if (showContent) {
			info.setContent(getStringValue(source.get("content")));
		}

		info.setNewsDate(getLongValue(source.get("newsDate")));
		info.setCrawlDate(getLongValue(source.get("crawlDate")));
		info.setWebName(getStringValue(source.get("webName")));
		info.setWebSectionName(getStringValue(source.get("webSectionName")));
		info.setWebsite_id(getStringValue(source.get("website_id")));
		info.setP_url(getStringValue(source.get("p_url")));
		// 没有使用到
		info.setTagName(getStringValue(source.get("tagName")));

		info.setKeyWords(getStringValue(source.get("keyWords")));
		info.setBrief(getStringValue(source.get("brief")));
		info.setNewWord(getStringValue(source.get("newWord")));
		info.setCategoryId(getStringValue(source.get("categoryId")));
		info.setCategory(getStringValue(source.get("category")));
		info.setExaminer(getStringValue(source.get("examiner")));
		info.setStoreTime(getLongValue(source.get("storeTime")));
		info.setRepetitive(getStringValue(source.get("repetitive")));
		return info;
	}
	

	public static double getDoubleValue(Object object) {
		return CommonUtils.isEmpty(object) ? 0 : Double.parseDouble(object.toString());
	}


	public static Long getLongValue(Object object) {
		return CommonUtils.isEmpty(object) ? 0 : Long.parseLong(object.toString());
	}


	public static String getStringValue(Object object) {
		return CommonUtils.isEmpty(object) ? "" : object.toString();
	}


	public static int getIntValue(Object object) {
		return CommonUtils.isEmpty(object) ? 0 : Integer.parseInt(object.toString());
	}

	public static float getFloatValue(Object object) {
		return CommonUtils.isEmpty(object) ? 0 : Float.parseFloat(object.toString());
	}

	public static SearchNews getNewsInfo(Map<String, Object> source,
			boolean showContent, Map<String, HighlightField> highlightMap) {
		SearchNews info = new SearchNews();
		// 新闻ID
		info.setCrawl_id(getIntValue(source.get("crawl_id")));
		// 新闻URL
		info.setUrl(getStringValue(source.get("url")));
		// 新闻内容
		String content = "";
		if(CommonUtils.isEmpty(highlightMap)){
			// 新闻标题
			info.setTitle(getStringValue(source.get("title")));
		}else{
			// 新闻标题
			if(CommonUtils.isNotEmpty(highlightMap.get("title"))){
				info.setTitle(highlightMap.get("title").fragments()[0].string());
			}
		}
		
		if(CommonUtils.isEmpty(info.getTitle())){
			// 新闻标题
			info.setTitle(getStringValue(source.get("title")));
		}
		
		if(CommonUtils.isEmpty(content)){
			// 新闻内容
			content = getStringValue(source.get("content"));
		}
		
		if(showContent){
			info.setContent(content);
		}else {
			org.jsoup.nodes.Document doc = Jsoup.parse(content);
			String d_content=doc.body().text();
			
			if(SysParameters.CONTENTLENGHT>0){
				String _content=CommonUtils.substring(d_content, 0, SysParameters.CONTENTLENGHT)+"...";
				info.setContent(_content);
			}
		}
		
		// 新闻时间
		info.setNewsDate(getLongValue(source.get("newsDate")));
		if(CommonUtils.isNotEmpty(info.getNewsDate())){
			// 新闻发布时间
			Long newTime = info.getNewsDate();
			
			if(newTime > System.currentTimeMillis()){
				info.setNewsDate(System.currentTimeMillis());
			}
		}
		// 新闻抓取时间
		info.setCrawlDate(getLongValue(source.get("crawlDate")));
		// 新闻来源网站名称
		info.setWebName(getStringValue(source.get("webName")));
		// 新闻来源板块名称
		info.setWebSectionName(getStringValue(source.get("webSectionName")));
		// 新闻来源对应的Website_id
		info.setWebsite_id(getStringValue(source.get("website_id")));
		// 新闻来源（信息源）的URL
		info.setP_url(getStringValue(source.get("p_url")));
		// 新闻所属标签名称
		info.setTagName(getStringValue(source.get("tagName")));
		// 新闻关键词
		info.setKeyWords(getStringValue(source.get("keyWords")));
		// extraTag
		info.setExtraTag(getStringValue(source.get("extraTag")));
		// 图片URL
		info.setPicUrl(getStringValue(source.get("picUrl")));
		// 区域
		info.setRegion(getStringValue(source.get("region")));
		
		return info;
	}
}
