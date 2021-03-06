package com.unbank.db.mybatis.client;

import com.unbank.db.mybatis.vo.DayNumWebDup;
import com.unbank.db.mybatis.vo.DayNumWebDupExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DayNumWebDupMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_day_num_web_dup
     *
     * @mbggenerated Fri Dec 12 10:54:58 CST 2014
     */
    int countByExample(DayNumWebDupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_day_num_web_dup
     *
     * @mbggenerated Fri Dec 12 10:54:58 CST 2014
     */
    int deleteByExample(DayNumWebDupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_day_num_web_dup
     *
     * @mbggenerated Fri Dec 12 10:54:58 CST 2014
     */
    int deleteByPrimaryKey(Integer numId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_day_num_web_dup
     *
     * @mbggenerated Fri Dec 12 10:54:58 CST 2014
     */
    int insert(DayNumWebDup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_day_num_web_dup
     *
     * @mbggenerated Fri Dec 12 10:54:58 CST 2014
     */
    int insertSelective(DayNumWebDup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_day_num_web_dup
     *
     * @mbggenerated Fri Dec 12 10:54:58 CST 2014
     */
    List<DayNumWebDup> selectByExample(DayNumWebDupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_day_num_web_dup
     *
     * @mbggenerated Fri Dec 12 10:54:58 CST 2014
     */
    DayNumWebDup selectByPrimaryKey(Integer numId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_day_num_web_dup
     *
     * @mbggenerated Fri Dec 12 10:54:58 CST 2014
     */
    int updateByExampleSelective(@Param("record") DayNumWebDup record, @Param("example") DayNumWebDupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_day_num_web_dup
     *
     * @mbggenerated Fri Dec 12 10:54:58 CST 2014
     */
    int updateByExample(@Param("record") DayNumWebDup record, @Param("example") DayNumWebDupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_day_num_web_dup
     *
     * @mbggenerated Fri Dec 12 10:54:58 CST 2014
     */
    int updateByPrimaryKeySelective(DayNumWebDup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_day_num_web_dup
     *
     * @mbggenerated Fri Dec 12 10:54:58 CST 2014
     */
    int updateByPrimaryKey(DayNumWebDup record);
}