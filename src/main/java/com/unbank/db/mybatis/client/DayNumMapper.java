package com.unbank.db.mybatis.client;

import com.unbank.db.mybatis.vo.DayNum;
import com.unbank.db.mybatis.vo.DayNumExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DayNumMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_day_num
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int countByExample(DayNumExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_day_num
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int deleteByExample(DayNumExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_day_num
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int deleteByPrimaryKey(Integer numId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_day_num
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int insert(DayNum record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_day_num
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int insertSelective(DayNum record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_day_num
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	List<DayNum> selectByExample(DayNumExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_day_num
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	DayNum selectByPrimaryKey(Integer numId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_day_num
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int updateByExampleSelective(@Param("record") DayNum record,
			@Param("example") DayNumExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_day_num
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int updateByExample(@Param("record") DayNum record,
			@Param("example") DayNumExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_day_num
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int updateByPrimaryKeySelective(DayNum record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_day_num
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int updateByPrimaryKey(DayNum record);
}