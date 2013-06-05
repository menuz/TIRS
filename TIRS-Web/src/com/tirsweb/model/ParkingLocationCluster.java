/**
 * 文件名：ParkingLocationCluster.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-4
 * Copyright by menuz
 */
package com.tirsweb.model;

public class ParkingLocationCluster {
	protected int id;
	protected double lati;
	protected double longi;
	protected int gpsCount;
	protected int arcId;
	
	public ParkingLocationCluster() {}
	
	public ParkingLocationCluster(int id, double lati, double longi,
			int gpsCount, int arcId) {
		super();
		this.id = id;
		this.lati = lati;
		this.longi = longi;
		this.gpsCount = gpsCount;
		this.arcId = arcId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getGpsCount() {
		return gpsCount;
	}

	public void setGpsCount(int gpsCount) {
		this.gpsCount = gpsCount;
	}

	public int getArcId() {
		return arcId;
	}

	public void setArcId(int arcId) {
		this.arcId = arcId;
	}

	@Override
	public String toString() {
		return "ParkingLocationCluster [id=" + id + ", lati=" + lati
				+ ", longi=" + longi + ", gpsCount=" + gpsCount + ", arcId="
				+ arcId + "]";
	}
}


