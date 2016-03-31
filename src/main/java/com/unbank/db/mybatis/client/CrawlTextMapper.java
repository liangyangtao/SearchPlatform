package com.unbank.db.mybatis.client;

import com.unbank.db.mybatis.vo.CrawlText;
import com.unbank.db.mybatis.vo.CrawlTextExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CrawlTextMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int countByExample(CrawlTextExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int deleteByExample(CrawlTextExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int deleteByPrimaryKey(Integer crawlId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int insert(CrawlText record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int insertSelective(CrawlText record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	List<CrawlText> selectByExampleWithBLOBs(CrawlTextExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	List<CrawlText> selectByExample(CrawlTextExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	CrawlText selectByPrimaryKey(Integer crawlId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int updateByExampleSelective(@Param("record") CrawlText record,
			@Param("example") CrawlTextExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int updateByExampleWithBLOBs(@Param("record") CrawlText record,
			@Param("example") CrawlTextExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int updateByExample(@Param("record") CrawlText record,
			@Param("example") CrawlTextExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int updateByPrimaryKeySelective(CrawlText record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int updateByPrimaryKeyWithBLOBs(CrawlText record);
}