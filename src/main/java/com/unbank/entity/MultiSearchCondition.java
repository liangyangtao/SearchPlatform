package com.unbank.entity;

import com.google.gson.Gson;

public class MultiSearchCondition {
	
	private SearchCondition searchCondition;
	private ExcessSearchCondition excessSearchCondition;
	
	public SearchCondition getSearchCondition() {
		return searchCondition;
	}
	public void setSearchCondition(SearchCondition searchCondition) {
		this.searchCondition = searchCondition;
	}
	public ExcessSearchCondition getExcessSearchCondition() {
		return excessSearchCondition;
	}
	public void setExcessSearchCondition(ExcessSearchCondition excessSearchCondition) {
		this.excessSearchCondition = excessSearchCondition;
	}
	@Override
	public String toString() {
		return "MultiSearchCondition [searchCondition=" + searchCondition + ", excessSearchCondition="
				+ excessSearchCondition + "]";
	}
	
	public String toJsonString(){
		Gson gson=new Gson();
		return gson.toJson(this);
	}
	
	public MultiSearchCondition(){}
	
	public MultiSearchCondition(SearchCondition searchCondition, ExcessSearchCondition excessSearchCondition) {
		this.searchCondition = searchCondition;
		this.excessSearchCondition = excessSearchCondition;
	}
	
}
