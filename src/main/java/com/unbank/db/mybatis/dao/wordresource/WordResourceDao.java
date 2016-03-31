package com.unbank.db.mybatis.dao.wordresource;

import java.util.List;

/**
 * 用户资源记录接口
 * 
 * @author LL
 * 
 */
public interface WordResourceDao {
	
	/**
	 * 根据用户ID查询资源信息
	 * 
	 * @param userId
	 * @return
	 */
	List<WordResource> queryByUserId(int userId);
}