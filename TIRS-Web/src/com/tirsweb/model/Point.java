/**
 * 文件名：Point.java
 *
 * 版本信息： version 1.0
 * 日期：Apr 22, 2013
 * Copyright by menuz
 */
package com.tirsweb.model;

import java.sql.Time;

public class Point {
	public double lat;
	public double lon;
	public Time time;
	
	public Point() {}
	
	public Point(double lat, double lon) {
		super();
		this.lat = lat;
		this.lon = lon;
	}

	public Point(double lat, double lon, Time time) {
		super();
		this.lat = lat;
		this.lon = lon;
		this.time = time;
	}
	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Point(String lati, String longi) {
		this.lat = Double.parseDouble(lati);
		this.lon = Double.parseDouble(longi);
	} 
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
}


