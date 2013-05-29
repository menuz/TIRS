package com.tirsweb.model;

public class Speed {
	private int boxId;
	private int weekday;
	private int hour;
	
	private double speed;
	
	public Speed() {}
	
	public Speed(int boxId, int weekday, int hour, double speed) {
		super();
		this.boxId = boxId;
		this.weekday = weekday;
		this.hour = hour;
		this.speed = speed;
	}

	public static String getKey(int boxId, int weekday, int hour) {
		return "" + boxId + weekday + hour;
	}
	
	public String getKey() {
		return "" + boxId + weekday + hour;
	}
	
	public double getSpeed() {
		return speed;
	}

	public int getBoxId() {
		return boxId;
	}

	public void setBoxId(int boxId) {
		this.boxId = boxId;
	}

	public int getWeekday() {
		return weekday;
	}

	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
}
