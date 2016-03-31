package com.unbank.db.mybatis.vo;


public class StatisticsObj {
	
	private String dept;
	private int userID;
	private String userName;
	private int website_add_num;
	private int website_up_num;
	private int crawl_num;
	private int crawl_syn_num;
	
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	
	public int getWebsite_add_num() {
		return website_add_num;
	}
	public void setWebsite_add_num(int website_add_num) {
		this.website_add_num = website_add_num;
	}
	public int getWebsite_up_num() {
		return website_up_num;
	}
	public void setWebsite_up_num(int website_up_num) {
		this.website_up_num = website_up_num;
	}
	public int getCrawl_num() {
		return crawl_num;
	}
	public void setCrawl_num(int crawl_num) {
		this.crawl_num = crawl_num;
	}
	public int getCrawl_syn_num() {
		return crawl_syn_num;
	}
	public void setCrawl_syn_num(int crawl_syn_num) {
		this.crawl_syn_num = crawl_syn_num;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public String toString() {
		return "StatisticsObj [dept=" + dept + ", userID=" + userID + ", userName=" + userName + ", website_add_num="
				+ website_add_num + ", website_up_num=" + website_up_num + ", crawl_num=" + crawl_num
				+ ", crawl_syn_num=" + crawl_syn_num + "]";
	}
	
	
}
