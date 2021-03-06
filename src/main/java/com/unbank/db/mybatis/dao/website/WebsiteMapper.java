package com.unbank.db.mybatis.dao.website;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.unbank.db.mybatis.vo.BaseWebsiteInfo;
import com.unbank.db.mybatis.vo.Website;
import com.unbank.db.mybatis.vo.WebsiteExample;

@Service
public interface WebsiteMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_website
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int countByExample(WebsiteExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_website
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int deleteByExample(WebsiteExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_website
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int deleteByPrimaryKey(Integer websiteId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_website
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int insert(Website record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_website
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int insertSelective(Website record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_website
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	List<Website> selectByExample(WebsiteExample example);
	
	
	List<BaseWebsiteInfo> selectBaseInfoByExample(WebsiteExample example);
	
	int countBaseInfoByExample(WebsiteExample example);
	
	List<BaseWebsiteInfo> selectBaseInfoByIds(List<Integer> list);
	
	List<BaseWebsiteInfo> selectBaseInfoByWebNames(List<String> list);
	

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_website
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	Website selectByPrimaryKey(Integer websiteId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_website
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int updateByExampleSelective(@Param("record") Website record,
			@Param("example") WebsiteExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_website
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int updateByExample(@Param("record") Website record,
			@Param("example") WebsiteExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_website
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int updateByPrimaryKeySelective(Website record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ptf_website
	 * @mbggenerated  Tue Dec 02 10:33:10 CST 2014
	 */
	int updateByPrimaryKey(Website record);
	
	
	
	
	

	/**
	 * 依据条件搜索parser
	 * @param example
	 * @return
	 */
	List<Website> selectParser(WebsiteExample example);
	
	/**
	 * 依据条件搜索Parser数量
	 * @param example
	 * @return
	 */
	int selectParserCount(WebsiteExample example);
	
	List<String> selectSectionByWebName(String webname);

	/**
	 * 模糊查询资源网站名称
	 * @param map
	 * @return
	 */
	List<String> searchByWebName(Map<String, Object> map);

	/**
	 * 查询总数
	 * @param webName
	 * @return
	 */
	int searchCountByWebName(String webName);
}