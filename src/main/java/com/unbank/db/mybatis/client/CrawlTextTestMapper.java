package com.unbank.db.mybatis.client;

import com.unbank.db.mybatis.vo.CrawlTextTest;
import com.unbank.db.mybatis.vo.CrawlTextTestExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CrawlTextTestMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text_test
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int countByExample(CrawlTextTestExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text_test
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int deleteByExample(CrawlTextTestExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text_test
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int deleteByPrimaryKey(Integer crawlId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text_test
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int insert(CrawlTextTest record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text_test
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int insertSelective(CrawlTextTest record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text_test
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	List<CrawlTextTest> selectByExampleWithBLOBs(CrawlTextTestExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text_test
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	List<CrawlTextTest> selectByExample(CrawlTextTestExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text_test
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	CrawlTextTest selectByPrimaryKey(Integer crawlId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text_test
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int updateByExampleSelective(@Param("record") CrawlTextTest record,
			@Param("example") CrawlTextTestExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text_test
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int updateByExampleWithBLOBs(@Param("record") CrawlTextTest record,
			@Param("example") CrawlTextTestExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text_test
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int updateByExample(@Param("record") CrawlTextTest record,
			@Param("example") CrawlTextTestExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text_test
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int updateByPrimaryKeySelective(CrawlTextTest record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_crawl_text_test
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int updateByPrimaryKeyWithBLOBs(CrawlTextTest record);
}