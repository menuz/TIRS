/**
 * 文件名：ParkingLocation.java
 *
 * 版本信息： version 1.0
 * 日期：May 17, 2013
 * Copyright by menuz
 */
package com.tirsweb.model;

/**
 * 
 * 此类描述的是： Tb_Parking_Location
 * @author: dmnrei@gmail.com
 * @version: May 18, 2013 4:26:38 PM
 */
public class ParkingLocation {
	int id;
	String tripId;
	String vehicleId;
	double lati;
	double longi;
	int arcId = -1;
	
	public ParkingLocation() {}
	
	public ParkingLocation(String tripId, String vehicleId, double lati,
			double longi, int arcId, int id) { 
		this(tripId, vehicleId, lati, longi, arcId);
		this.id = id;
	}
	
	public ParkingLocation(String tripId, String vehicleId, double lati,
			double longi, int arcId) {
		super();
		this.tripId = tripId;
		this.vehicleId = vehicleId;
		this.lati = lati;
		this.longi = longi;
		this.arcId = arcId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTripId() {
		return tripId;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public double getLati() {
		return lati;
	}

	public double getLongi() {
		return longi;
	}

	public int getArcId() {
		return arcId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public void setLati(double lati) {
		this.lati = lati;
	}

	public void setLongi(double longi) {
		this.longi = longi;
	}

	public void setArcId(int arcId) {
		this.arcId = arcId;
	}
}


