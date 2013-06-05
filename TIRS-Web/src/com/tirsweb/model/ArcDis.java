/**
 * 文件名：ArcDis.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-5
 * Copyright by menuz
 */
package com.tirsweb.model;

/**
 * 
 * 此类描述的是：temp model for sorting
 * @author: dmnrei@gmail.com
 * @version: 2013-6-5 上午10:21:09
 */
public class ArcDis {
	int arcid;
	double minDis;
	
	public ArcDis() {}
	
	public ArcDis(int arcid, double minDis) {
		super();
		this.arcid = arcid;
		this.minDis = minDis;
	}
	public int getArcid() {
		return arcid;
	}
	public void setArcid(int arcid) {
		this.arcid = arcid;
	}
	public double getMinDis() {
		return minDis;
	}
	public void setMinDis(double minDis) {
		this.minDis = minDis;
	}

	@Override
	public String toString() {
		return "ArcDis [arcid=" + arcid + ", minDis=" + minDis + "]";
	}	
}


