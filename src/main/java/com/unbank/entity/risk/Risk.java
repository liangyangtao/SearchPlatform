package com.unbank.entity.risk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Risk {

	/**
	 * 新闻ID
	 */
	private int crawl_id;
	/**
	 * 新闻URL
	 */
	private String url;
	/**
	 * 新闻标题
	 */
	private String title;
	/**
	 * 新闻内容
	 */
	private String content;
	/**
	 * 新闻时间
	 */
	private Long newsDate;
	/**
	 * 新闻抓取时间
	 */
	private Long crawlDate;
	/**
	 * 新闻来源网站名称
	 */
	private String webName;
	/**
	 * 新闻来源板块名称
	 */
	private String webSectionName;
	/**
	 * 新闻来源对应的Website_id
	 */
	private String website_id;

	/**
	 * 新闻来源（信息源）的URL
	 */
	private String p_url;
	/**
	 * 新闻所属标签名称
	 */
	private String tagName;
	/**
	 * 新闻关键词
	 */
	private String keyWords;

	/**
	 * 简介
	 */
	private String brief;
	
	/**
	 * 新词
	 */
	private String newWord;
	
	/**
	 * 栏目ID
	 */
	private String categoryId;
	
	/**
	 * 栏目
	 */
	private String category;
	
	/**
	 * 录入人（审查人）
	 */
	private String examiner;
	
	/**
	 * 入库时间
	 */
	private Long storeTime;
	
	/**
	 * 是否重复
	 */
	private String repetitive;

	public int getCrawl_id() {
		return crawl_id;
	}

	public void setCrawl_id(int crawl_id) {
		this.crawl_id = crawl_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getNewsDate() {
		return newsDate;
	}

	public void setNewsDate(Long newsDate) {
		this.newsDate = newsDate;
	}

	public Long getCrawlDate() {
		return crawlDate;
	}

	public void setCrawlDate(Long crawlDate) {
		this.crawlDate = crawlDate;
	}

	public String getWebName() {
		return webName;
	}

	public void setWebName(String webName) {
		this.webName = webName;
	}

	public String getWebSectionName() {
		return webSectionName;
	}

	public void setWebSectionName(String webSectionName) {
		this.webSectionName = webSectionName;
	}

	public String getWebsite_id() {
		return website_id;
	}

	public void setWebsite_id(String website_id) {
		this.website_id = website_id;
	}

	public String getP_url() {
		return p_url;
	}

	public void setP_url(String p_url) {
		this.p_url = p_url;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getNewWord() {
		return newWord;
	}

	public void setNewWord(String newWord) {
		this.newWord = newWord;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getExaminer() {
		return examiner;
	}

	public void setExaminer(String examiner) {
		this.examiner = examiner;
	}

	public Long getStoreTime() {
		return storeTime;
	}

	public void setStoreTime(Long storeTime) {
		this.storeTime = storeTime;
	}

	public String getRepetitive() {
		return repetitive;
	}

	public void setRepetitive(String repetitive) {
		this.repetitive = repetitive;
	}
	
	public boolean checkRisk(){
		if(this.title==null||this.content==null||this.categoryId==null||this.crawl_id==0){
			return false;
		}
		return true;
	}
	
	
	
	public Risk(){}
	public Risk(int crawl_id, String url, String title, String content, Long newsDate, Long crawlDate, String webName,
			String webSectionName, String website_id, String p_url, String tagName, String keyWords, String brief,
			String newWord, String categoryId, String category, String examiner, Long storeTime, String repetitive) {
		super();
		this.crawl_id = crawl_id;
		this.url = url;
		this.title = title;
		this.content = content;
		this.newsDate = newsDate;
		this.crawlDate = crawlDate;
		this.webName = webName;
		this.webSectionName = webSectionName;
		this.website_id = website_id;
		this.p_url = p_url;
		this.tagName = tagName;
		this.keyWords = keyWords;
		this.brief = brief;
		this.newWord = newWord;
		this.categoryId = categoryId;
		this.category = category;
		this.examiner = examiner;
		this.storeTime = storeTime;
		this.repetitive = repetitive;
	}

	@Override
	public String toString() {
		return "Risk [crawl_id=" + crawl_id + ", url=" + url + ", title=" + title + ", content=" + content
				+ ", newsDate=" + newsDate + ", crawlDate=" + crawlDate + ", webName=" + webName + ", webSectionName="
				+ webSectionName + ", website_id=" + website_id + ", p_url=" + p_url + ", tagName=" + tagName
				+ ", keyWords=" + keyWords + ", brief=" + brief + ", newWord=" + newWord + ", categoryId=" + categoryId
				+ ", category=" + category + ", examiner=" + examiner + ", storeTime=" + storeTime + ", repetitive="
				+ repetitive + "]";
	}
	
	public String toJsonString(){
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(this);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
