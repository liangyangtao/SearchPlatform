package com.unbank.db.mybatis.client;

import java.util.List;
import java.util.Map;

import com.unbank.db.mybatis.vo.DayNumWeb;
import com.unbank.db.mybatis.vo.DayNumWebSyn;
import com.unbank.db.mybatis.vo.Website;

public interface StatisticsMapper {
   
	List<Website> getStatistics(Map<String,Object> map);
	
	int getSumByIDAndDate(Map<String,Object> map);
	
	int getSynSumByIDAndDate(Map<String,Object> map);
	
	int getAddWebsiteNum(Map<String,Object> map);
	
	int getUpWebsiteNum(Map<String,Object> map);
	
	int getSumByCIDAndDate(Map<String,Object> map);
	
	int getSynSumByCIDAndDate(Map<String,Object> map);
	
	List<DayNumWeb> getSumInfoByCIDAndDate(Map<String,Object> map);
	
	public List<DayNumWebSyn> getSynSumInfoByCIDAndDate(Map<String,Object> map);
	
}