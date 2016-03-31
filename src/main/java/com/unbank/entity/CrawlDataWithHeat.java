package com.unbank.entity;

public class CrawlDataWithHeat {
	private int docId;
	private int hotValue;
	
	public CrawlDataWithHeat(int docId, int hotValue) {
		super();
		this.docId = docId;
		this.hotValue = hotValue;
	}
	public int getDocId() {
		return docId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
	public int getHotValue() {
		return hotValue;
	}
	public void setHotValue(int hotValue) {
		this.hotValue = hotValue;
	}
	
	@Override
	public String toString() {
		return "CrawlDataWithHeat [docId=" + docId + ", hotValue=" + hotValue + "]";
	}
	
}
