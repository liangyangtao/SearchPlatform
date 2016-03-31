package com.unbank.es.journal;

import java.util.List;

public class JournalParam {
	/**
	 * 起始数 默认0
	 */
	private int from;

	/**
	 * 查询数量 默认20
	 */
	private int pageSize;

	/**
	 * 排序字段 默认passTime
	 */
	private String orderByField;

	/**
	 * 排序方式 默认DESC
	 */
	private String order;

	/**
	 * 是否显示全文 默认false
	 */
	private boolean fullContent;

	/**
	 * 搜索条件封装
	 */
	private List<JournalSearchData> jsonData;

	/**
	 * 置顶信息
	 */
	private List<String> topList;
	

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrderByField() {
		return orderByField;
	}

	public void setOrderByField(String orderByField) {
		this.orderByField = orderByField;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public boolean isFullContent() {
		return fullContent;
	}

	public void setFullContent(boolean fullContent) {
		this.fullContent = fullContent;
	}

	public List<JournalSearchData> getJsonData() {
		return jsonData;
	}

	public void setJsonData(List<JournalSearchData> jsonData) {
		this.jsonData = jsonData;
	}

	public List<String> getTopList() {
		return topList;
	}

	public void setTopList(List<String> topList) {
		this.topList = topList;
	}

}
