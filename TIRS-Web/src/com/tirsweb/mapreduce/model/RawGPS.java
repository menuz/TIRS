/**
 * 文件名：RawGPS.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-7
 * Copyright by menuz
 */
package com.tirsweb.mapreduce.model;

import java.sql.Timestamp;

// MESSAGE_ID,VEHICLE_ID,VEHICLE_NUM,LONGI,LATI,SPEED,DIRECTION,STATE,SPEED_TIME,DB_TIME,NOTE

public class RawGPS {
	public String message_id;
	public String vehicle_id;
	public String vehicle_num;
	public String longi;
	public String lati;
	public double speed;
	public double direction;
	public String state;
	public Timestamp speed_time;
	public Timestamp db_time;
	public String note;

	public String str;

	public RawGPS() {
	}

	private RawGPS(String message_id, String vehicle_id, String vehicle_num,
			String longi, String lati, double speed, double direction,
			String state, Timestamp speed_time, Timestamp db_time, String note,
			String str) {
		super();
		this.message_id = message_id;
		this.vehicle_id = vehicle_id;
		this.vehicle_num = vehicle_num;
		this.longi = longi;
		this.lati = lati;
		this.speed = speed;
		this.direction = direction;
		this.state = state;
		this.speed_time = speed_time;
		this.db_time = db_time;
		this.note = note;
		this.str = str;

	}

	public static RawGPS parse(String str) {
		String[] cols = str.split(";");
		String message_id = cols[0];
		String vehicle_id = cols[1];
		String vehicle_num = cols[2];
		String longi = cols[3];
		String lati = cols[4];
		double speed = Double.parseDouble(cols[5]);
		double direction = Double.parseDouble(cols[6]);
		String state = cols[7];
		Timestamp speed_time = Timestamp.valueOf(cols[8]);
		Timestamp db_time = Timestamp.valueOf(cols[9]);
		String note = cols[10];

		RawGPS raw = new RawGPS(message_id, vehicle_id, vehicle_num, longi,
				lati, speed, direction, state, speed_time, db_time, note, str);

		return raw;
	}

	@Override
	public String toString() {
		return "RawGPS [message_id=" + message_id + ", vehicle_id="
				+ vehicle_id + ", vehicle_num=" + vehicle_num + ", longi="
				+ longi + ", lati=" + lati + ", speed=" + speed
				+ ", direction=" + direction + ", state=" + state
				+ ", speed_time=" + speed_time + ", db_time=" + db_time
				+ ", note=" + note + ", str=" + str + "]";
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

	public String getSuffixLongi() {
		// 120.123

		if (this.longi.equals("0")) {
			return "-1";
		}

		if (this.longi.equals("120") || this.longi.equals("119")
				|| this.longi.equals("121")) {
			return "0";
		}

		String returnLongi = this.longi;

		try {
			returnLongi = this.longi.substring(4);
		} catch (Exception e) {
			System.out.println("error");
			System.out.println("longi = " + this.longi);

			return "0";

		}

		return returnLongi;
	}

	public String getSuffixLati() {
		// 30.123
		if (this.lati.equals("0")) {
			return "-1";
		}

		if (this.lati.equals("30") || this.lati.equals("31")
				|| this.lati.equals("32") || this.lati.equals("29")) {
			return "0";
		}

		String returnLati = this.lati;
		try {
			returnLati = this.lati.substring(3);
		} catch (Exception e) {
			System.out.println("error");
			System.out.println("lati = " + this.lati);

			System.out.println(toString());

		}

		return returnLati;
	}

	public String getGPS() {
		return getSuffixLati() + "+" + getSuffixLongi() + ",";
	}

	public String getLongi() {
		return longi;
	}

	public void setLongi(String longi) {
		this.longi = longi;
	}

	public String getLati() {
		return lati;
	}

	public void setLati(String lati) {
		this.lati = lati;
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
