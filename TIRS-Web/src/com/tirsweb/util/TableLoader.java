/**
 * 文件名：TableLoader.java
 *
 * 版本信息： version 1.0
 * 日期：Apr 22, 2013
 * Copyright by menuz
 */
package com.tirsweb.util;

import java.sql.SQLException;
import java.sql.Timestamp;

import com.tirsweb.model.BaseArc;
import com.tirsweb.model.GPS;
import com.tirsweb.model.ParkingLocationCluster;
import com.tirsweb.model.Speed;

/**
 * 
 * 此类描述的是：提取公共部分
 * 
 * @author: dmnrei@gmail.com
 * @version: Nov 12, 2012 6:49:09 PM
 */
public class TableLoader {
	public static GPS loadGPS(java.sql.ResultSet rs) throws SQLException {
		String message_id = rs.getString("message_id");
		String vehicle_id = rs.getString("vehicle_id");
		String vehicle_num = rs.getString("vehicle_num");

		double longi = rs.getDouble("longi");
		double lati = rs.getDouble("lati");
		double px = rs.getDouble("px");
		double py = rs.getDouble("py");
		double speed = rs.getDouble("speed");
		double direction = rs.getDouble("direction");
		String state = rs.getString("state");
		String carState = rs.getString("carState");

		Timestamp speed_time = rs.getTimestamp("speed_time");
		Timestamp db_time = rs.getTimestamp("db_time");
		
/*System.out.println("speed time = " + speed_time);
System.out.println("db time = " + db_time);*/

		String note = rs.getString("note");

		GPS gps = new GPS(message_id, vehicle_id, vehicle_num, longi, lati, px,
				py, speed, direction, state, carState, speed_time, db_time,
				note);

		return gps;
	}
	
	
	public static ParkingLocationCluster loadParkingLocationCluster(java.sql.ResultSet rs) throws SQLException {
		double lati = rs.getDouble("lati");
		double longi = rs.getDouble("longi");
		
		int gpscount = rs.getInt("gpscount");
		int arcid = rs.getInt("arc_id");
		int id = rs.getInt("id");

		ParkingLocationCluster pkCluster = new ParkingLocationCluster(id, lati, longi, gpscount, arcid);

		return pkCluster;
	}
	
	public static Speed loadSpeed(java.sql.ResultSet rs) throws SQLException {
		double lati = rs.getDouble("lati");
		double longi = rs.getDouble("longi");
		
		int arcid = rs.getInt("arc_id");
		int id = rs.getInt("id");
		int weekday = rs.getInt("weekday");
		int hourofday = rs.getInt("hourofday");
		double speed = rs.getDouble("speed");

		Speed s = new Speed(id,arcid,weekday,hourofday, speed);

		return s;
	}
	
	public static BaseArc loadBaseArc(java.sql.ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		int start_node_id = rs.getInt("start_node_id");
		int end_node_id = rs.getInt("end_node_id");
		int len = rs.getInt("len");

		BaseArc arc = new BaseArc(id, start_node_id, end_node_id, len);
		return arc;
	}
	
}
