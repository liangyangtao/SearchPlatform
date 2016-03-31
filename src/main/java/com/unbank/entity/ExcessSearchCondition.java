package com.unbank.entity;

import java.util.List;

import com.google.gson.Gson;

public class ExcessSearchCondition {
	
	/**
	 * 必须被包含在正文中的词语
	 */
	private List<String> mustContentWords;
	
	/**
	 * 不能被包含在正文中的词语
	 */
	private List<String> mustNotContentWords;
	
	/**
	 * 可以出现在正文中的词语
	 */
	private List<String> shouldContentWords;
	
	/**
	 * 标题中必须出现的词语
	 */
	private List<String> mustTitleWords;
	
	/**
	 * 标题中不能出现的词语
	 */
	private List<String> mustNotTitleWords;
	
	/**
	 * 标题中可以出现的词语
	 */
	private List<String> shouldTitleWords;
	
	
	public List<String> getMustContentWords() {
		return mustContentWords;
	}

	public void setMustContentWords(List<String> mustContentWords) {
		this.mustContentWords = mustContentWords;
	}

	public List<String> getMustNotContentWords() {
		return mustNotContentWords;
	}

	public void setMustNotContentWords(List<String> mustNotContentWords) {
		this.mustNotContentWords = mustNotContentWords;
	}

	public List<String> getShouldContentWords() {
		return shouldContentWords;
	}

	public void setShouldContentWords(List<String> shouldContentWords) {
		this.shouldContentWords = shouldContentWords;
	}

	
	public List<String> getMustTitleWords() {
		return mustTitleWords;
	}

	public void setMustTitleWords(List<String> mustTitleWords) {
		this.mustTitleWords = mustTitleWords;
	}

	public List<String> getMustNotTitleWords() {
		return mustNotTitleWords;
	}

	public void setMustNotTitleWords(List<String> mustNotTitleWords) {
		this.mustNotTitleWords = mustNotTitleWords;
	}

	public List<String> getShouldTitleWords() {
		return shouldTitleWords;
	}

	public void setShouldTitleWords(List<String> shouldTitleWords) {
		this.shouldTitleWords = shouldTitleWords;
	}


	@Override
	public String toString() {
		return "ExcessSearchCondition [mustContentWords=" + mustContentWords + ", mustNotContentWords="
				+ mustNotContentWords + ", shouldContentWords=" + shouldContentWords + ", mustTitleWords="
				+ mustTitleWords + ", mustNotTitleWords=" + mustNotTitleWords + ", shouldTitleWords="
				+ shouldTitleWords + "]";
	}

	public String toJsonString(){
		Gson gson=new Gson();
		return gson.toJson(this);
	}
}
