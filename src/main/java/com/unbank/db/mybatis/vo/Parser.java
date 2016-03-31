package com.unbank.db.mybatis.vo;

public class Parser {
	private Website website;
	private WebsiteParser websiteParser;
	
	
	private String listPath;
	   
	
	public String getListPath() {
		return listPath;
	}

	public void setListPath(String listPath) {
		this.listPath = listPath;
	}

	public Parser(){}
	
	public Parser( Website website,WebsiteParser websiteParser){
		this.website=website;
		this.websiteParser=websiteParser;
	}
	public Website getWebsite() {
		return website;
	}
	public void setWebsite(Website website) {
		this.website = website;
	}
	public WebsiteParser getWebsiteParser() {
		return websiteParser;
	}
	public void setWebsiteParser(WebsiteParser websiteParser) {
		this.websiteParser = websiteParser;
	}
}
