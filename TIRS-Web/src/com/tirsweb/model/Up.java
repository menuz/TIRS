package com.tirsweb.model;

import java.util.ArrayList;
import java.util.List;

public class Up {
	private int boxId;
	private int weekday;
	private int hour;
	
	private int count;
	
	public Up() {}
	
	public Up(int boxId, int weekday, int hour, int count) {
		super();
		this.boxId = boxId;
		this.weekday = weekday;
		this.hour = hour;
		this.count = count;
	}
	
	public static String getKey(int boxId, int weekday, int hour) {
		return "" + boxId + weekday + hour;
	}

	public String getKey() {
		return "" + boxId + weekday + hour;
	}
	
	public int getUpCount() {
		return count;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public List<Point> getPoints() {
		if(boxId == -1)  return null; 
		
		int row = -1;
		int col = -1;
		if(boxId % 40 == 0) {
			row = boxId / 40;
		} else {
			row = boxId / 40 + 1;
		}
		
		col = boxId - 40*(row - 1);
		
		double lati = 30.15;
		double longi = 120.00;
		
		lati = lati + 0.01 * row - 0.005;
		longi = longi + 0.01 * col - 0.005;
		
		Point p = new Point(lati, longi);
		ArrayList<Point> ps = new ArrayList<Point>();
		ps.add(p);
		
		return ps;
	}
}
