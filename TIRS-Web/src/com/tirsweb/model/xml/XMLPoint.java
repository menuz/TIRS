/**
 * 文件名：ParkingLocationCluster.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-5
 * Copyright by menuz
 */
package com.tirsweb.model.xml;

import com.tirsweb.model.Point;

public class XMLPoint extends Point{
	int arcid;
	int idx;

	public XMLPoint(double lat, double lon, int arcid, int idx) {
		super(lat, lon);
		this.arcid = arcid;
		this.idx = idx;
	}

	public int getArcid() {
		return arcid;
	}

	public void setArcid(int arcid) {
		this.arcid = arcid;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}
	
	
	
}
