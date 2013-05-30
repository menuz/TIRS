/**
 * 文件名：NodePoint.java
 *
 * 版本信息： version 1.0
 * 日期：May 16, 2013
 * Copyright by menuz
 */
package com.tirsweb.model;

public class NodePoint extends Point {
	int row;
	int column;
	
	int id;
	
	public NodePoint() {
		
	}
	
	public NodePoint(int row, int column, int id) {
		super();
		this.row = row;
		this.column = column;
		this.id = id;
	}

	public NodePoint(double lati, double longi) {
		super(lati, longi);
	}
	
	public NodePoint(int id, double lati, double longi) {
		super(lati, longi);
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public NodePoint(String lati, String longi) {
		super(lati, longi);
	}
	
	public NodePoint(int row, int column, double lati, double longi) {
		super(lati, longi);
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
}


