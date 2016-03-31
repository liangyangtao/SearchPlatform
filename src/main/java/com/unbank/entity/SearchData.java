package com.unbank.entity;

import java.util.List;

import com.google.gson.Gson;

public class SearchData {
	private Long count;
	private List<SearchNews> data;
	private String crawlIds;

	public String getCrawlIds() {
		return crawlIds;
	}

	public void setCrawlIds(String crawlIds) {
		this.crawlIds = crawlIds;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public List<SearchNews> getData() {
		return data;
	}

	public SearchData(Long count, List<SearchNews> data) {
		super();
		this.count = count;
		this.data = data;
	}

	public void setData(List<SearchNews> data) {
		this.data = data;
	}

	public String toJsonString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public SearchData() {
	}

}
