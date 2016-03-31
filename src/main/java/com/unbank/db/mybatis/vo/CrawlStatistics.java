package com.unbank.db.mybatis.vo;

import java.util.Date;

public class CrawlStatistics {
	private int crawlNum;
	private int crawlSynNum;
	private Date crawlDate;
	
	public int getCrawlNum() {
		return crawlNum;
	}
	public void setCrawlNum(int crawlNum) {
		this.crawlNum = crawlNum;
	}
	public int getCrawlSynNum() {
		return crawlSynNum;
	}
	public void setCrawlSynNum(int crawlSynNum) {
		this.crawlSynNum = crawlSynNum;
	}
	public Date getCrawlDate() {
		return crawlDate;
	}
	public void setCrawlDate(Date crawlDate) {
		this.crawlDate = crawlDate;
	}
	@Override
	public String toString() {
		return "CrawlStatistics [crawlNum=" + crawlNum + ", crawlSynNum=" + crawlSynNum + ", crawlDate=" + crawlDate
				+ "]";
	}
	
	
}
