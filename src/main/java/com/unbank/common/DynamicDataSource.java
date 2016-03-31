package com.unbank.common;

import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.unbank.common.utils.CommonUtils;


public class DynamicDataSource extends AbstractRoutingDataSource {
	
	private Map<Object, Object> _targetDataSources;
	
	@Override
	protected Object determineCurrentLookupKey() {
		String dataSourceName = DBContextHolder.getDBType();
		if (CommonUtils.isEmpty(dataSourceName)){
			dataSourceName = "word_db";
		}
		return dataSourceName;
	}
	
	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}
	
	public void setTargetDataSources(Map<Object, Object> targetDataSources) {
		this._targetDataSources = targetDataSources;
		super.setTargetDataSources(this._targetDataSources);
		afterPropertiesSet();
	}

}
