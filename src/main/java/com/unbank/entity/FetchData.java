package com.unbank.entity;

import java.util.List;

public class FetchData {
	private int count;
	private List<CrawlDataWithHeat> data;

	public List<CrawlDataWithHeat> getData() {
		return data;
	}

	public void setData(List<CrawlDataWithHeat> data) {
		this.data = data;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "FetchData [count=" + count + ", data=" + data + "]";
	}

}
