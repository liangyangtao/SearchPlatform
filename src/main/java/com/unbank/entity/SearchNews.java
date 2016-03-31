package com.unbank.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonSyntaxException;
import com.unbank.common.utils.GsonUtil;

public class SearchNews {

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

	private String extraTag;

	/**
	 * 图片URL
	 */
	private String picUrl;

	/**
	 * 区域
	 */
	private String region;

	public String getExtraTag() {
		return extraTag;
	}

	public void setExtraTag(String extraTag) {
		this.extraTag = extraTag;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		if (tagName.contains("_")) {
			this.tagName = tagName.replace("_", " ");
			return;
		}
		this.tagName = tagName;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		if (keyWords.contains("/")) {
			this.keyWords = keyWords.replace("/", " ");
			return;
		}
		this.keyWords = keyWords;
	}

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

	public String toJsonString() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(this);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void appendTag(String jsonTags) {
		if (jsonTags == null) {
			return;
		}

		List<String> tagList = null;
		try {
			tagList = GsonUtil.getListFromJson(jsonTags);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
		appendTag(tagList);
	}

	public void appendTag(List<String> tagList) {
		if (tagList == null) {
			return;
		}
		String[] tags = this.tagName.split(" ");
		Set<String> set = new HashSet<String>();
		set.addAll(tagList);
		for (String tag : tags) {
			set.add(tag);
		}
		this.tagName = "";
		for (String tag : set) {
			this.tagName = this.tagName + " " + tag;
		}
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

}
