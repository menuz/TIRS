/**
 * 文件名：TbArcSpeed.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-11
 * Copyright by menuz
 */
package com.tirsweb.model;

public class TbArcSpeed {
	// String sql = "insert into tb_arc_speed(arc_id, weekday, hourofday, speed) values ("
		//	+ arcid + ", " + weekday + ", " + hour + ", " + average + ");";
	//return sql;
	
	int arc_id;
	int weekday;
	int hourofday;
	double speed;
	
	public TbArcSpeed(int arc_id, int weekday, int hourofday, double speed) {
		super();
		this.arc_id = arc_id;
		this.weekday = weekday;
		this.hourofday = hourofday;
		this.speed = speed;
	}
	public int getArc_id() {
		return arc_id;
	}
	public void setArc_id(int arc_id) {
		this.arc_id = arc_id;
	}
	public int getWeekday() {
		return weekday;
	}
	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}
	public int getHourofday() {
		return hourofday;
	}
	public void setHourofday(int hourofday) {
		this.hourofday = hourofday;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
}


