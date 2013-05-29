/**
 * 文件名：PointConverter.java
 *
 * 版本信息： version 1.0
 * 日期：Apr 22, 2013
 * Copyright by menuz
 */
package com.tirsweb.util.gps;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import com.tirsweb.dao.JDBCDAO;
import com.tirsweb.model.Point;

public class GpsConverter extends JDBCDAO{
	
	public Point gpsToMarGps(double latitude, double longitude) {
		DecimalFormat df = new DecimalFormat("###.00");
		String df_lat = df.format(latitude);
		String df_lon = df.format(longitude);

		Connection conn = getConn();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select offsetlat, offsetlon from tb_gps_correct where lat="
					+ df_lat + " and lon=" + df_lon + "";
			
// System.out.println("sql = " + sql);  
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				double offsetlat = rs.getDouble("offsetlat");
				double offsetlon = rs.getDouble("offsetlon");
				return new Point(latitude + offsetlat, longitude + offsetlon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseSource(conn, stmt, rs);
		}
		return null;
	}
	
	public Point marGpsToGps(double latitude, double longitude) {
		DecimalFormat df = new DecimalFormat("###.00");
		String df_lat = df.format(latitude);
		String df_lon = df.format(longitude);

		Connection conn = getConn();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select offsetlat, offsetlon from tb_gps_correct where lat="
					+ df_lat + " and lon=" + df_lon + "";
			
// System.out.println("sql = " + sql);  
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				double offsetlat = rs.getDouble("offsetlat");
				double offsetlon = rs.getDouble("offsetlon");
				return new Point(latitude - offsetlat, longitude - offsetlon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseSource(conn, stmt, rs);
		}
		return null;
	}
}


