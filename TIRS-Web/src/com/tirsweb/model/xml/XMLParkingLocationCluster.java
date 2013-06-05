/**
 * 文件名：ParkingLocationCluster.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-5
 * Copyright by menuz
 */
package com.tirsweb.model.xml;

import com.tirsweb.model.ParkingLocationCluster;

public class XMLParkingLocationCluster extends ParkingLocationCluster{
	// 乘客靠近该路段的次
	int nearestIdx;
	
	public XMLParkingLocationCluster() {
		super();
		// TODO Auto-generated constructor stub
	}


	public XMLParkingLocationCluster(int id, double lati, double longi,
			int gpsCount, int arcId) {
		super(id, lati, longi, gpsCount, arcId);
		// TODO Auto-generated constructor stub
	}
	
	public XMLParkingLocationCluster(int id, double lati, double longi,
			int gpsCount, int arcId, int nearestIdx) {
		super(id, lati, longi, gpsCount, arcId);
		this.nearestIdx = nearestIdx;
	}


	public int getNearestIdx() {
		return nearestIdx;
	}


	public void setNearestIdx(int nearestIdx) {
		this.nearestIdx = nearestIdx;
	}


	@Override
	public String toString() {
		return "XMLParkingLocationCluster [nearestIdx=" + nearestIdx + ", id="
				+ id + ", lati=" + lati + ", longi=" + longi + ", gpsCount="
				+ gpsCount + ", arcId=" + arcId + "]";
	}
	
}
