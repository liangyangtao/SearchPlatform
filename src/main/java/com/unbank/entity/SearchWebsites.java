package com.unbank.entity;

import java.util.List;

import com.unbank.db.mybatis.vo.BaseWebsiteInfo;

public class SearchWebsites {

	private int count;
	private List<BaseWebsiteInfo> data;
	
	public SearchWebsites(int count, List<BaseWebsiteInfo> data) {
		super();
		this.count = count;
		this.data = data;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<BaseWebsiteInfo> getData() {
		return data;
	}
	public void setData(List<BaseWebsiteInfo> data) {
		this.data = data;
	}
	
	
}
