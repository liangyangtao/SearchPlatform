package com.unbank.es.journal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 期刊实体类
 * 
 * @author LiuLei
 * 
 */
public class Journal {

	@Override
	public String toString() {
		return String
				.format("Journal [esId=%s, journalId=%s, name=%s, skip=%s, label=%s, keyWord=%s, cover=%s, type=%s, typeId=%s, passType=%s, submitUserId=%s, submitUserName=%s, submitTime=%s, createTime=%s, passUserId=%s, passUserName=%s, passTime=%s, price=%s, status=%s, buyFlag=%s, haveData=%s, updateTime=%s]",
						esId, journalId, name, skip, label, keyWord, cover,
						type, typeId, passType, submitUserId, submitUserName,
						submitTime, createTime, passUserId, passUserName,
						passTime, price, status, buyFlag, haveData, updateTime);
	}

	public String toJsonString() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(this);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ESID
	 */
	private String esId;

	/**
	 * 期刊ID（数据库ID）
	 */
	private int journalId;

	/**
	 * 期刊名称
	 */
	private String name;

	/**
	 * 简介
	 */
	private String skip;

	/**
	 * 标签
	 */
	private String label;

	/**
	 * 关键字
	 */
	private String keyWord;

	/**
	 * 期刊封面图片URL
	 */
	private String cover;

	/**
	 * 期刊类型 日刊：2015-08-16 周刊：2015-08-1周 半月刊：2015-08上 月刊：2015-08 双月刊：2015-08
	 * 季刊：2015-1季度 半年刊：2015上半年 年刊：2015年
	 */
	private String type;

	/**
	 * 期刊类型ID 对应数据库
	 */
	private int typeId;

	/**
	 * 保存状态 SAVED:私有保存, SUBMITTED:审核中 PASSED:审核成功 FAILED:审核失败
	 */
	private String passType;

	/**
	 * 提交用户ID
	 */
	private int submitUserId;

	/**
	 * 提交用户名称
	 */
	private String submitUserName;

	/**
	 * 提交时间
	 */
	private Long submitTime;

	/**
	 * 创建时间（第一次提交的时间，一直不变的字段）
	 */
	private Long createTime;

	/**
	 * 审核人ID
	 */
	private int passUserId;

	/**
	 * 审核人名称
	 */
	private String passUserName;

	/**
	 * 审核通过时间
	 */
	private Long passTime;

	/**
	 * 价格
	 */
	private double price;

	/**
	 * 数据处理标记 1:top 2:delete
	 */
	private int status;

	/**
	 * 购买标识 1:已购买
	 */
	private int buyFlag;

	/**
	 * 状态默认0，有报告后为1
	 */
	private int haveData;

	/**
	 * 期刊增加报告时间
	 */
	private long updateTime;

	public String getEsId() {
		return esId;
	}

	public void setEsId(String esId) {
		this.esId = esId;
	}

	public int getJournalId() {
		return journalId;
	}

	public void setJournalId(int journalId) {
		this.journalId = journalId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSkip() {
		return skip;
	}

	public void setSkip(String skip) {
		this.skip = skip;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPassType() {
		return passType;
	}

	public void setPassType(String passType) {
		this.passType = passType;
	}

	public int getSubmitUserId() {
		return submitUserId;
	}

	public void setSubmitUserId(int submitUserId) {
		this.submitUserId = submitUserId;
	}

	public String getSubmitUserName() {
		return submitUserName;
	}

	public void setSubmitUserName(String submitUserName) {
		this.submitUserName = submitUserName;
	}

	public Long getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Long submitTime) {
		this.submitTime = submitTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public int getPassUserId() {
		return passUserId;
	}

	public void setPassUserId(int passUserId) {
		this.passUserId = passUserId;
	}

	public String getPassUserName() {
		return passUserName;
	}

	public void setPassUserName(String passUserName) {
		this.passUserName = passUserName;
	}

	public Long getPassTime() {
		return passTime;
	}

	public void setPassTime(Long passTime) {
		this.passTime = passTime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getHaveData() {
		return haveData;
	}

	public void setHaveData(int haveData) {
		this.haveData = haveData;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public int getBuyFlag() {
		return buyFlag;
	}

	public void setBuyFlag(int buyFlag) {
		this.buyFlag = buyFlag;
	}

}
