package com.unbank.entity;

public class TagNode {
	
	private String name;
	private TagNode childNode;
	private TagNode nearNode;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TagNode getChildNode() {
		return childNode;
	}
	public void setChildNode(TagNode childNode) {
		this.childNode = childNode;
	}
	public TagNode getNearNode() {
		return nearNode;
	}
	public void setNearNode(TagNode nearNode) {
		this.nearNode = nearNode;
	}
	
}
