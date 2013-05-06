/**
 * 文件名：GPS.java
 *
 * 版本信息： version 1.0
 * 日期：Apr 22, 2013
 * Copyright by menuz
 */
package com.tirsweb.model;

import java.sql.Date;
import java.sql.Timestamp;

public class GPS {
	public String message_id;
	public String vehicle_id;
	public String vehicle_num;
	public double longi;
	public double lati;
	public double longi_correct;
	public double lati_correct;
	public double px;
	public double py;
	public double speed;
	public double direction;
	public String state;
	public String carState;
	public Timestamp speed_time;
	public Timestamp db_time;
	public String note;
	public GPS() {}
	
	public GPS(String message_id, String vehicle_id, String vehicle_num,
			double longi, double lati, double px, double py, double speed,
			double direction, String state, String carState,
			Timestamp speed_time, Timestamp db_time, String note) {
		super();
		this.message_id = message_id;
		this.vehicle_id = vehicle_id;
		this.vehicle_num = vehicle_num;
		this.longi = longi;
		this.lati = lati;
		this.px = px;
		this.py = py;
		this.speed = speed;
		this.direction = direction;
		this.state = state;
		this.carState = carState;
		this.speed_time = speed_time;
		this.db_time = db_time;
		this.note = note;
	}
	
	@Override
	public String toString() {
		return "message_id: " + message_id + "\n" + 
				"vehicle_id: " + vehicle_id + "\n" +
				"vehicle_num: " + vehicle_num + "\n" +
				"longi: " + longi + "\n" + 
				"lati: " + lati + "\n" +
				"px: " + px + "\n" + 
				"py: " + py + "\n" +
				"speed: " + speed + "\n" + 
				"direction: " + direction + "\n" + 
				"state: " + state + "\n" + 
				"carState: " + carState + "\n" + 
				"speed_time: " + speed_time + "\n" +
				"db_time: " + db_time + "\n" + 
				"note: " + note + "\n";
	}
	public double getLongi_correct() {
		return longi_correct;
	}

	public void setLongi_correct(double longi_correct) {
		this.longi_correct = longi_correct;
	}

	public double getLati_correct() {
		return lati_correct;
	}

	public void setLati_correct(double lati_correct) {
		this.lati_correct = lati_correct;
	}
	public String getMessage_id() {
		return message_id;
	}
	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}
	public String getVehicle_id() {
		return vehicle_id;
	}
	public void setVehicle_id(String vehicle_id) {
		this.vehicle_id = vehicle_id;
	}
	public String getVehicle_num() {
		return vehicle_num;
	}
	public void setVehicle_num(String vehicle_num) {
		this.vehicle_num = vehicle_num;
	}
	public double getLongi() {
		return longi;
	}
	public void setLongi(double longi) {
		this.longi = longi;
	}
	public double getLati() {
		return lati;
	}
	public void setLati(double lati) {
		this.lati = lati;
	}
	public double getPx() {
		return px;
	}
	public void setPx(double px) {
		this.px = px;
	}
	public double getPy() {
		return py;
	}
	public void setPy(double py) {
		this.py = py;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public double getDirection() {
		return direction;
	}
	public void setDirection(double direction) {
		this.direction = direction;
	}
 	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCarState() {
		return carState;
	}
	public void setCarState(String carState) {
		this.carState = carState;
	}
	public Timestamp getSpeed_time() {
		return speed_time;
	}
	public void setSpeed_time(Timestamp speed_time) {
		this.speed_time = speed_time;
	}
	public Timestamp getDb_time() {
		return db_time;
	}
	public void setDb_time(Timestamp db_time) {
		this.db_time = db_time;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}


