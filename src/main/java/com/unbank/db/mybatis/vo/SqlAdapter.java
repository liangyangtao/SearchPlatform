package com.unbank.db.mybatis.vo;

import java.util.List;
import java.util.Map;

public class SqlAdapter {

	private Integer prikey;
	private String sql;
	private Map<String, Object> obj;
	private List<Integer> list;
	private String value;

	public SqlAdapter(Map<String, Object> objects) {
		this.obj = objects;
	}

	public SqlAdapter(String sql) {
		this.sql = sql;
	}

	public SqlAdapter(String sql, Map<String, Object> objects) {
		this.sql = sql;
		this.obj = objects;
	}

	public SqlAdapter(List<Integer> list) {
		this.list = list;
	}

	public SqlAdapter() {
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Map<String, Object> getObj() {
		return obj;
	}

	public void setObj(Map<String, Object> obj) {
		this.obj = obj;
	}

	public List<Integer> getList() {
		return list;
	}

	public void setList(List<Integer> list) {
		this.list = list;
	}

	public Integer getPrikey() {
		return prikey;
	}

	public void setPrikey(Integer prikey) {
		this.prikey = prikey;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


	

}
