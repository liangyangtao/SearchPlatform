package com.unbank.db.mybatis.client;

import java.util.List;
import java.util.Map;

import com.unbank.db.mybatis.vo.SqlAdapter;

public interface SqlAdapterMapper {

	List<String> selectWebNames(SqlAdapter sqlAdapter);

	List<Map<String, Object>> readWebNameNum(SqlAdapter sqlAdapter);

	List<Map<String, Object>> readSectionByWebName(SqlAdapter sqlAdapter);

	int conuntOfNum(SqlAdapter sqlAdapter);
}
