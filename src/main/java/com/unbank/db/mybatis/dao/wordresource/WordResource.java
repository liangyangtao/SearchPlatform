package com.unbank.db.mybatis.dao.wordresource;

/**
 * 用户资源记录
 * 
 * @author LL
 * 
 */
public class WordResource {

	@Override
	public String toString() {
		return String
				.format("WordResource [resourceId=%s, userId=%s, resourceType=%s, buyType=%s, journalId=%s, articleId=%s, price=%s, cartId=%s, doId=%s, startTime=%s, endTime=%s, createTime=%s]",
						resourceId, userId, resourceType, buyType, journalId,
						articleId, price, cartId, doId, startTime, endTime,
						createTime);
	}

	/**
	 * 序列
	 */
	private int resourceId;

	/**
	 * 用户id
	 */
	private int userId;

	/**
	 * 资源类型:期刊,期刊文档,文档
	 */
	private String resourceType;

	/**
	 * 添加方式:前台,后台
	 */
	private String buyType;

	/**
	 * 期刊id
	 */
	private int journalId;

	/**
	 * 文档id
	 */
	private int articleId;
	/**
	 * 购买金币
	 */
	private double price;
	/**
	 * 购物车id
	 */
	private int cartId;

	/**
	 * 操作人员
	 */
	private int doId;
	/**
	 * 开始时间
	 */
	private String startTime;

	/**
	 * 结束时间
	 */
	private String endTime;

	/**
	 * 建立时间
	 */
	private String createTime;

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getBuyType() {
		return buyType;
	}

	public void setBuyType(String buyType) {
		this.buyType = buyType;
	}

	public int getJournalId() {
		return journalId;
	}

	public void setJournalId(int journalId) {
		this.journalId = journalId;
	}

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public int getDoId() {
		return doId;
	}

	public void setDoId(int doId) {
		this.doId = doId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
