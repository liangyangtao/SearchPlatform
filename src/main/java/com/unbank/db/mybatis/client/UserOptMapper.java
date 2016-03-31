package com.unbank.db.mybatis.client;

import com.unbank.db.mybatis.vo.UserOpt;
import com.unbank.db.mybatis.vo.UserOptExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserOptMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_user_opt
     * @mbggenerated Thu Mar 19 10:13:17 CST 2015
     */
    int countByExample(UserOptExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_user_opt
     *
     * @mbggenerated Thu Mar 19 10:13:17 CST 2015
     */
    int deleteByExample(UserOptExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_user_opt
     *
     * @mbggenerated Thu Mar 19 10:13:17 CST 2015
     */
    int deleteByPrimaryKey(Integer optId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_user_opt
     *
     * @mbggenerated Thu Mar 19 10:13:17 CST 2015
     */
    int insert(UserOpt record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_user_opt
     *
     * @mbggenerated Thu Mar 19 10:13:17 CST 2015
     */
    int insertSelective(UserOpt record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_user_opt
     *
     * @mbggenerated Thu Mar 19 10:13:17 CST 2015
     */
    List<UserOpt> selectByExample(UserOptExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_user_opt
     *
     * @mbggenerated Thu Mar 19 10:13:17 CST 2015
     */
    UserOpt selectByPrimaryKey(Integer optId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_user_opt
     *
     * @mbggenerated Thu Mar 19 10:13:17 CST 2015
     */
    int updateByExampleSelective(@Param("record") UserOpt record, @Param("example") UserOptExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_user_opt
     *
     * @mbggenerated Thu Mar 19 10:13:17 CST 2015
     */
    int updateByExample(@Param("record") UserOpt record, @Param("example") UserOptExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_user_opt
     *
     * @mbggenerated Thu Mar 19 10:13:17 CST 2015
     */
    int updateByPrimaryKeySelective(UserOpt record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ptf_user_opt
     *
     * @mbggenerated Thu Mar 19 10:13:17 CST 2015
     */
    int updateByPrimaryKey(UserOpt record);
}