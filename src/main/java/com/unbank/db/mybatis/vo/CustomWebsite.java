package com.unbank.db.mybatis.vo;

import java.util.Date;

public class CustomWebsite {
    /**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_website.website_id
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	private Integer websiteId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_website.istask
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	private Integer istask;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_website.rank
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	private Integer rank;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_website.url_home
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	private String urlHome;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_website.web_name
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	private String webName;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_website.status
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	private String status;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_website.url
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	private String url;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_website.url_second
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	private String urlSecond;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_website.section_name
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	private String sectionName;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_website.url_third
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	private String urlThird;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_website.url_rule
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	private String urlRule;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_website.reg_datetime
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	private String regDatetime;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_website.msg
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	private String msg;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_website.crawl_rule
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	private String crawlRule;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_website.c_id
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	private Integer cId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_website.c_name
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	private String cName;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_website.c_date
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	private Date cDate;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_website.c_ip
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	private Integer cIp;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ptf_website.isOrder
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	private Integer isorder;
	
	
	public int getTestNewsCount() {
		return testNewsCount;
	}

	public void setTestNewsCount(int testNewsCount) {
		this.testNewsCount = testNewsCount;
	}
	//测试环境已经抓取的新闻总数
	private int testNewsCount;
	//不通过的新闻数
	private int errorNewsCount;
	//已经通过的新闻数
	private int passedNewsCount;
	
	
	public int getErrorNewsCount() {
		return errorNewsCount;
	}

	public void setErrorNewsCount(int errorNewsCount) {
		this.errorNewsCount = errorNewsCount;
	}

	public int getPassedNewsCount() {
		return passedNewsCount;
	}

	public void setPassedNewsCount(int passedNewsCount) {
		this.passedNewsCount = passedNewsCount;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_website.website_id
	 * @return  the value of ptf_website.website_id
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public Integer getWebsiteId() {
		return websiteId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_website.website_id
	 * @param websiteId  the value for ptf_website.website_id
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public void setWebsiteId(Integer websiteId) {
		this.websiteId = websiteId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_website.istask
	 * @return  the value of ptf_website.istask
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public Integer getIstask() {
		return istask;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_website.istask
	 * @param istask  the value for ptf_website.istask
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public void setIstask(Integer istask) {
		this.istask = istask;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_website.rank
	 * @return  the value of ptf_website.rank
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public Integer getRank() {
		return rank;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_website.rank
	 * @param rank  the value for ptf_website.rank
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public void setRank(Integer rank) {
		this.rank = rank;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_website.url_home
	 * @return  the value of ptf_website.url_home
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public String getUrlHome() {
		return urlHome;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_website.url_home
	 * @param urlHome  the value for ptf_website.url_home
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public void setUrlHome(String urlHome) {
		this.urlHome = urlHome;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_website.web_name
	 * @return  the value of ptf_website.web_name
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public String getWebName() {
		return webName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_website.web_name
	 * @param webName  the value for ptf_website.web_name
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public void setWebName(String webName) {
		this.webName = webName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_website.status
	 * @return  the value of ptf_website.status
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_website.status
	 * @param status  the value for ptf_website.status
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_website.url
	 * @return  the value of ptf_website.url
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_website.url
	 * @param url  the value for ptf_website.url
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_website.url_second
	 * @return  the value of ptf_website.url_second
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public String getUrlSecond() {
		return urlSecond;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_website.url_second
	 * @param urlSecond  the value for ptf_website.url_second
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public void setUrlSecond(String urlSecond) {
		this.urlSecond = urlSecond;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_website.section_name
	 * @return  the value of ptf_website.section_name
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public String getSectionName() {
		return sectionName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_website.section_name
	 * @param sectionName  the value for ptf_website.section_name
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_website.url_third
	 * @return  the value of ptf_website.url_third
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public String getUrlThird() {
		return urlThird;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_website.url_third
	 * @param urlThird  the value for ptf_website.url_third
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public void setUrlThird(String urlThird) {
		this.urlThird = urlThird;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_website.url_rule
	 * @return  the value of ptf_website.url_rule
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public String getUrlRule() {
		return urlRule;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_website.url_rule
	 * @param urlRule  the value for ptf_website.url_rule
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public void setUrlRule(String urlRule) {
		this.urlRule = urlRule;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_website.reg_datetime
	 * @return  the value of ptf_website.reg_datetime
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public String getRegDatetime() {
		return regDatetime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_website.reg_datetime
	 * @param regDatetime  the value for ptf_website.reg_datetime
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public void setRegDatetime(String regDatetime) {
		this.regDatetime = regDatetime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_website.msg
	 * @return  the value of ptf_website.msg
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_website.msg
	 * @param msg  the value for ptf_website.msg
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_website.crawl_rule
	 * @return  the value of ptf_website.crawl_rule
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public String getCrawlRule() {
		return crawlRule;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_website.crawl_rule
	 * @param crawlRule  the value for ptf_website.crawl_rule
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public void setCrawlRule(String crawlRule) {
		this.crawlRule = crawlRule;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_website.c_id
	 * @return  the value of ptf_website.c_id
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public Integer getcId() {
		return cId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_website.c_id
	 * @param cId  the value for ptf_website.c_id
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public void setcId(Integer cId) {
		this.cId = cId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_website.c_name
	 * @return  the value of ptf_website.c_name
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public String getcName() {
		return cName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_website.c_name
	 * @param cName  the value for ptf_website.c_name
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public void setcName(String cName) {
		this.cName = cName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_website.c_date
	 * @return  the value of ptf_website.c_date
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public Date getcDate() {
		return cDate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_website.c_date
	 * @param cDate  the value for ptf_website.c_date
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public void setcDate(Date cDate) {
		this.cDate = cDate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_website.c_ip
	 * @return  the value of ptf_website.c_ip
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public Integer getcIp() {
		return cIp;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_website.c_ip
	 * @param cIp  the value for ptf_website.c_ip
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public void setcIp(Integer cIp) {
		this.cIp = cIp;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ptf_website.isOrder
	 * @return  the value of ptf_website.isOrder
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public Integer getIsorder() {
		return isorder;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ptf_website.isOrder
	 * @param isorder  the value for ptf_website.isOrder
	 * @mbggenerated  Thu Nov 27 13:57:13 CST 2014
	 */
	public void setIsorder(Integer isorder) {
		this.isorder = isorder;
	}

	@Override
	public String toString() {
		return "Website [websiteId=" + websiteId + ", istask=" + istask
				+ ", rank=" + rank + ", urlHome=" + urlHome + ", webName="
				+ webName + ", status=" + status + ", url=" + url
				+ ", urlSecond=" + urlSecond + ", sectionName=" + sectionName
				+ ", urlThird=" + urlThird + ", urlRule=" + urlRule
				+ ", regDatetime=" + regDatetime + ", msg=" + msg
				+ ", crawlRule=" + crawlRule + ", cId=" + cId + ", cName="
				+ cName + ", cDate=" + cDate + ", cIp=" + cIp + ", isorder="
				+ isorder + "]";
	}
    
}