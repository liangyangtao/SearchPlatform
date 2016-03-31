package com.unbank.entity;

import java.util.List;

import com.google.gson.Gson;

public class SearchCondition {
	
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
	 * 来源网址
	 */
	private List<Integer> websiteIDs;
	
	/**
	 * 按照哪个字段排序
	 */
	private OrderByMode orderByColumn;
	
	/**
	 * 开始时间
	 */
	private Long startTime;
	
	/**
	 * 结束时间
	 */
	private Long endTime;
	
	/**
	 * 开始标记
	 */
	private int from=0;
	
	/**
	 * 分页大小
	 */
	private int pageSize=20;
	
	public enum OrderByMode{
		TIME,SCORE
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

	public OrderByMode getOrderByColumn() {
		return orderByColumn;
	}

	public void setOrderByColumn(OrderByMode orderByColumn) {
		this.orderByColumn = orderByColumn;
	}

	
	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	
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

	public List<Integer> getWebsiteIDs() {
		return websiteIDs;
	}

	public void setWebsiteIDs(List<Integer> websiteIDs) {
		this.websiteIDs = websiteIDs;
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

	@Override
	public String toString() {
		return "SearchCondition [mustContentWords=" + mustContentWords + ", mustNotContentWords=" + mustNotContentWords
				+ ", shouldContentWords=" + shouldContentWords + ", mustTitleWords=" + mustTitleWords
				+ ", mustNotTitleWords=" + mustNotTitleWords + ", shouldTitleWords=" + shouldTitleWords
				+ ", mustTagNames=" + mustTagNames + ", mustNotTagNames=" + mustNotTagNames + ", shouldTagNames="
				+ shouldTagNames + ", websiteIDs=" + websiteIDs + ", orderByColumn=" + orderByColumn + ", startTime="
				+ startTime + ", endTime=" + endTime + ", from=" + from + ", pageSize=" + pageSize + "]";
	}
	
	public String toJsonString(){
		Gson gson=new Gson();
		return gson.toJson(this);
	}
	
}
