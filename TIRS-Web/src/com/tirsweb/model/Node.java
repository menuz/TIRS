/**
 * 文件名：Node.java
 *
 * 版本信息： version 1.0
 * 日期：May 16, 2013
 * Copyright by menuz
 */
package com.tirsweb.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Node {
	private int id;
	private String tag;
	private double lati;
	private double longi;
	
	public Node() {
	}
	
	public Node(String tag) {
		this.tag = tag;
	}
	
	public Node(int id, double lati, double longi) {
		this.id = id;
		this.lati = lati;
		this.longi = longi;
	}
	
	// child  Integer is distance of two nodes
	private Map<Node,Integer> child=new HashMap<Node,Integer>();
	// refresh by time
	private long lastAccessTime;
	
	public Node(int id){
		this.id=id;
		lastAccessTime = new Date().getTime();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Map<Node, Integer> getChild() {
		return child;
	}
	public void setChild(Map<Node, Integer> child) {
		this.child = child;
	}
	
	public double getLati() {
		return lati;
	}
	public void setLati(double lati) {
		this.lati = lati;
	}
	public double getLongi() {
		return longi;
	}
	public void setLongi(double longi) {
		this.longi = longi;
	}
	// 
	private boolean needRefresh() {
		return false;
	}
	
	// quanzhi will refresh by time
	public void refreshChild() {
		
	}

	public String getName() {
		return this.tag;
	}
}