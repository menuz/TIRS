/**
 * 文件名：ArcDetail.java
 *
 * 版本信息： version 1.0
 * 日期：May 18, 2013
 * Copyright by menuz
 */
package com.tirsweb.model;

public class ArcDetail {
	double lati;
	double longi;
	int idx;
	
    public ArcDetail() {}  
	
	public ArcDetail(double lati, double longi, int idx) {
		super();
		this.lati = lati;
		this.longi = longi;
		this.idx = idx;
	}
	
	public double getLati() {
		return lati;
	}
	public double getLongi() {
		return longi;
	}
	public int getIdx() {
		return idx;
	}
	public void setLati(double lati) {
		this.lati = lati;
	}
	public void setLongi(double longi) {
		this.longi = longi;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
}


