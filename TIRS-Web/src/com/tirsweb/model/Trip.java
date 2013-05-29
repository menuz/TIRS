/**
 * 文件名：Trip.java
 *
 * 版本信息： version 1.0
 * 日期：May 17, 2013
 * Copyright by menuz
 */
package com.tirsweb.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Trip {
	int id;
	String tripId;
	String vehicleId;
	String note;
	String state;
	// used for store gps extracted from note
	private ArrayList<Point> wayPoints;
	// used for store current arcid of corresponding gps
	private ArrayList<Point> pointArcs;
	// weekday/weekend + hour + arcid + speed
	private ArrayList<String> sqlList;
	
	int weekday = 0;
	int hour = 0;
	
	public Trip() {}
	
	public Trip(int id, String tripId, String vehicleId, String note, String state) {
		super();
		this.id = id;
		this.tripId = tripId;
		this.vehicleId = vehicleId;
		this.note = note;
		this.state = state;
		
		SimpleDateFormat bartDateFormat =  
				   new SimpleDateFormat("yyyyMMddhhmmss");
		try {
			Date d = bartDateFormat.parse(tripId);
			int day = d.getDay();
			if(day == 0 || day == 6) {
				this.weekday = 1;
			} else {
				this.weekday = 0;
			}
			this.hour = d.getHours();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		this.wayPoints = new ArrayList<Point>();
		String[] gpses = this.note.split(",");
		for (String gps : gpses) {
			int idx = gps.indexOf("+");
			String lat = gps.substring(0, idx);
			String lon = gps.substring(idx+1);
			Point p = new Point(lat,lon);
			this.wayPoints.add(p);
		}
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Point> getWayPoints() {
		return wayPoints;
	}

	public Trip(String tripId, String vehicleId, String note) {
		this.tripId = tripId;
		this.vehicleId = vehicleId;
		this.note = note;
	}
	
	public int getWeekday() {
		return weekday;
	}

	public int getHour() {
		return hour;
	}

	public String getTripId() {
		return tripId;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	public ArrayList<Point> getPointArcs() {
		return pointArcs;
	}

	public void setSqlList(ArrayList<String> sqlList) {
		this.sqlList = sqlList;
	}
	
	/*public static void main(String[] args) {
		String tripId = "20111130231155";
		SimpleDateFormat bartDateFormat =  
				   new SimpleDateFormat("yyyyMMddhhmmss");
		try {
			Date d = bartDateFormat.parse(tripId);
			System.out.println("day = " + d.getDay());
			System.out.println("day = " + d.getHours());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String gps = "30.325817+120.153933";
		int idx = gps.indexOf("+");
		String lat = gps.substring(0, idx);
		String lon = gps.substring(idx+1);
		System.out.println(lat);
		System.out.println(lon);
	}*/
}


