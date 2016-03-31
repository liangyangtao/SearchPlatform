package com.unbank.es.document;

import java.util.List;

import org.jsoup.Jsoup;

import com.google.gson.Gson;
import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.CommonUtils;

public class SearchDocumentData {
	
	private Long count;
	private List<Document> data;
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public List<Document> getData() {
		return data;
	}
	public void setData(List<Document> data) {
		this.data = data;
	}
	
	public String toJsonString(){
		Gson gson=new Gson();
		return gson.toJson(this);
	}
	public SearchDocumentData(Long count, List<Document> data) {
		super();
		this.count = count;
		this.data = data;
	}
	
	public String toJsonString(boolean fullContent){
		if(fullContent){
			return toJsonString();
		}else{
			for(Document document:data){
				String content= document.getArticleContent();
				org.jsoup.nodes.Document doc = Jsoup.parse(content);
				String d_content=doc.body().text();
				if(SysParameters.CONTENTLENGHT>0){
					String _content=CommonUtils.substring(d_content, 0, SysParameters.CONTENTLENGHT)+"...";
					document.setArticleContent(_content);
				}
			}
			return this.toJsonString();
		}
	}
	
	public SearchDocumentData(){}

}
