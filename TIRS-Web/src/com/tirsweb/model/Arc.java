/**
 * 文件名：Arc.java
 *
 * 版本信息： version 1.0
 * 日期：May 16, 2013
 * Copyright by menuz
 */
package com.tirsweb.model;

import java.util.ArrayList;


public class Arc {
	public int id;
	
	public int start_node_id;
	public int end_node_id;
	
	public double lati1;
	public double longi1;
	public double lati2;
	public double longi2;
	
	public double length;
	
	public ArrayList<ArcDetail> arcDetailList;
	public ArrayList<Integer> boxList;

	public Arc() {}
	
	public Arc(int id, int start_node_id, int end_node_id, 
			double lati1, double longi1, 
			double lati2, double longi2) {
		super();
		this.id = id;
		this.start_node_id = start_node_id;
		this.end_node_id = end_node_id;
		this.lati1 = lati1;
		this.longi1 = longi1;
		this.lati2 = lati2;
		this.longi2 = longi2;
	}
	
	public int getStart_node_id() {
		return start_node_id;
	}

	public int getEnd_node_id() {
		return end_node_id;
	}

	public void setStart_node_id(int start_node_id) {
		this.start_node_id = start_node_id;
	}

	public void setEnd_node_id(int end_node_id) {
		this.end_node_id = end_node_id;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getLati1() {
		return lati1;
	}
	public void setLati1(double lati1) {
		this.lati1 = lati1;
	}
	public double getLongi1() {
		return longi1;
	}
	public void setLongi1(double longi1) {
		this.longi1 = longi1;
	}
	public double getLati2() {
		return lati2;
	}
	public void setLati2(double lati2) {
		this.lati2 = lati2;
	}
	public double getLongi2() {
		return longi2;
	}
	public void setLongi2(double longi2) {
		this.longi2 = longi2;
	}

	public ArrayList<ArcDetail> getArcDetailList() {
		return arcDetailList;
	}

	public void setArcDetailList(ArrayList<ArcDetail> arcDetailList) {
		this.arcDetailList = arcDetailList;
	}
	
	public ArrayList<Integer> getBoxList() {
		return boxList;
	}

	public void setBoxList(ArrayList<Integer> boxList) {
		this.boxList = boxList;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}
	
	public String getTbArcBoxInsertSql() {
		ArrayList<String> sqlList = new ArrayList<String>();
		
		for(Integer box : boxList) {
			String sqlTemp = "insert into tb_arc_box values(" + id + ", " + box + ");";
			sqlList.add(sqlTemp);
		}
		
		String allSql = "";
		for (String string : sqlList) {
			allSql = allSql + string + "\n";
		}
		
		return allSql;
	}
	
	public String getTbArcUpdateSql() {
		return "update tb_arc set len = " + length + " where id = " + id + ";";
	}
}


