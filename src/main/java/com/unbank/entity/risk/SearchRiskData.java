package com.unbank.entity.risk;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.CommonUtils;

public class SearchRiskData {
	private Long count;
	private List<SearchRisk> data;

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public List<SearchRisk> getData() {
		return data;
	}

	public SearchRiskData(Long count, List<SearchRisk> data) {
		super();
		this.count = count;
		this.data = data;
	}

	public void setData(List<SearchRisk> data) {
		this.data = data;
	}

	public String toJsonString(){
		Gson gson=new Gson();
		return gson.toJson(this);
	}
	
	public String toJsonString(boolean fullContent){
		if(fullContent){
			return toJsonString();
		}else{
			for(SearchRisk sn:data){
				String content= sn.getContent();
				Document doc = Jsoup.parse(content);
				String d_content=doc.body().text();
				if(SysParameters.CONTENTLENGHT>0){
					String _content=CommonUtils.substring(d_content, 0, SysParameters.CONTENTLENGHT)+"...";
					sn.setContent(_content);
				}
			}
			return this.toJsonString();
		}
	}
	
	public SearchRiskData(){}
	
}
