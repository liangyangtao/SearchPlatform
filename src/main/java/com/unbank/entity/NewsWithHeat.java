package com.unbank.entity;

public class NewsWithHeat implements Comparable<NewsWithHeat>{
	private int crawl_id;
	private int priority;
	
	public int getCrawl_id() {
		return crawl_id;
	}
	public void setCrawl_id(int crawl_id) {
		this.crawl_id = crawl_id;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public NewsWithHeat(int crawl_id, int priority) {
		super();
		this.crawl_id = crawl_id;
		this.priority = priority;
	}
	
	 @Override
	public int compareTo(NewsWithHeat newsWithHeat){
		 if(this.priority>newsWithHeat.priority){
			 return 1;
		 }else if(this.priority<newsWithHeat.priority){
			 return -1;
		 }else{
			 return 0;
		 }
		 
	 }
	@Override
	public String toString() {
		return "NewsWithHeat [crawl_id=" + crawl_id + ", priority=" + priority + "]";
	}
	
	
}
