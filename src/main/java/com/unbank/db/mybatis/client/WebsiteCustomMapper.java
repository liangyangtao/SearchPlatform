package com.unbank.db.mybatis.client;

import java.util.List;
import java.util.Map;

import com.unbank.db.mybatis.vo.Website;

public interface WebsiteCustomMapper {

	 List<Website> getNoFetchWebsites(Map<String, Object> map);  
	 
	 int getNoFetchCount(Map<String, Object> map);
}