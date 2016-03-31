package com.unbank.es.document;

import java.util.List;

/**
 * 文档搜索参数类
 * @author LiuLei 2015-11-16
 *
 */
public class GroupParam {
	
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
	private List<DocumentSearchCondition> jsonData;
	
	/**
	 * 高亮关键字
	 */
	private List<String> highlight;

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

	public List<DocumentSearchCondition> getJsonData() {
		return jsonData;
	}

	public void setJsonData(List<DocumentSearchCondition> jsonData) {
		this.jsonData = jsonData;
	}

	public List<String> getHighlight() {
		return highlight;
	}

	public void setHighlight(List<String> highlight) {
		this.highlight = highlight;
	}

	
}
