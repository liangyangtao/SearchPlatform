package com.unbank.db.mybatis.client;

import com.unbank.db.mybatis.vo.MonitorUrlError;
import com.unbank.db.mybatis.vo.MonitorUrlErrorExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MonitorUrlErrorMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_monitor_urlerror
     *
     * @mbggenerated Wed Mar 04 15:15:18 GMT+08:00 2015
     */
    int countByExample(MonitorUrlErrorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_monitor_urlerror
     *
     * @mbggenerated Wed Mar 04 15:15:18 GMT+08:00 2015
     */
    int deleteByExample(MonitorUrlErrorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_monitor_urlerror
     *
     * @mbggenerated Wed Mar 04 15:15:18 GMT+08:00 2015
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_monitor_urlerror
     *
     * @mbggenerated Wed Mar 04 15:15:18 GMT+08:00 2015
     */
    int insert(MonitorUrlError record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_monitor_urlerror
     *
     * @mbggenerated Wed Mar 04 15:15:18 GMT+08:00 2015
     */
    int insertSelective(MonitorUrlError record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_monitor_urlerror
     *
     * @mbggenerated Wed Mar 04 15:15:18 GMT+08:00 2015
     */
    List<MonitorUrlError> selectByExample(MonitorUrlErrorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_monitor_urlerror
     *
     * @mbggenerated Wed Mar 04 15:15:18 GMT+08:00 2015
     */
    MonitorUrlError selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_monitor_urlerror
     *
     * @mbggenerated Wed Mar 04 15:15:18 GMT+08:00 2015
     */
    int updateByExampleSelective(@Param("record") MonitorUrlError record, @Param("example") MonitorUrlErrorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_monitor_urlerror
     *
     * @mbggenerated Wed Mar 04 15:15:18 GMT+08:00 2015
     */
    int updateByExample(@Param("record") MonitorUrlError record, @Param("example") MonitorUrlErrorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_monitor_urlerror
     *
     * @mbggenerated Wed Mar 04 15:15:18 GMT+08:00 2015
     */
    int updateByPrimaryKeySelective(MonitorUrlError record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_monitor_urlerror
     *
     * @mbggenerated Wed Mar 04 15:15:18 GMT+08:00 2015
     */
    int updateByPrimaryKey(MonitorUrlError record);
}