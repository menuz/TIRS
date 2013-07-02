/**
 * 文件名：DAO3.java
 *
 * 版本信息： version 1.0
 * 日期：May 17, 2013
 * Copyright by menuz
 */
package com.tirsweb.dao;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.tirsweb.model.Arc;
import com.tirsweb.model.ArcDetail;
import com.tirsweb.model.ParkingLocation;
import com.tirsweb.model.Trip;

/**
 * 
 * 此类描述的是：dao file for ArcToBox.java
 * @author: dmnrei@gmail.com
 * @version: May 17, 2013 12:35:07 PM
 */
public class GpsToTripDAO {
	
	 /** Oracle数据库连接URL*/
    private final static String url = "jdbc:oracle:thin:@192.168.1.111:1521:tirs1";
	// private final static String url = "jdbc:oracle:thin:@60.191.28.14:1521:webgps3";
    /** Oracle数据库连接驱动*/
    private final static String driver = "oracle.jdbc.driver.OracleDriver";
    /** 数据库用户名*/
    private final static String user = "HZGPS_TAXI";
    /** 数据库密码*/
    private final static String password = "HZGPS_TAXI";
    
    
	Connection conn = null;
    
    
    /** Oracle数据库连接URL*/
   /* private final static String url = "jdbc:hive://127.0.0.1:10000/default";
    *//** Oracle数据库连接驱动*//*
    private final static String driver = "org.apache.hadoop.hive.jdbc.HiveDriver";
    *//** 数据库用户名*//*
    private final static String user = "";
    *//** 数据库密码*//*
    private final static String password = "";*/

	/**
	 * 
	 */
	public GpsToTripDAO() {
		try {
			Class.forName(driver);
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	/**
	 * 
	 */
	public Connection getConn() {
		if(conn == null) {
			try {
				conn = DriverManager.getConnection(url, user, password);
				if (conn!= null && !conn.isClosed());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
	
	
	 public void gpsToTripByVehicleId(String vehicle_id) {
	        ArrayList<Trip> trips = new ArrayList<Trip>();
	        Connection conn = null;
	        Statement stmt = null;
	        ResultSet rs = null;

	        // String sql = "select * from temp_trip where id between " + startIdx + " and " + endIdx;
	        // String sql = "select trip_id, vehicle_id, note from tb_gps_1112_trip where state = 0 and id between " + startIdx + " and " + endIdx;
	        // System.out.println(sql);
	        try {
	            conn = getConn();
	            CallableStatement proc = null;
	            proc = conn.prepareCall("{ call HZGPS_TAXI.SP_TRIP_CREATE2(?) }");
	            proc.setString(1, vehicle_id);
	            proc.execute();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	           //  releaseSource(conn, stmt, rs);
	        }
	    }
	 
	 
	 public ArrayList<String> getAllVehicle() {
	        ArrayList<String> vehicleList = new ArrayList<String>();
	        Connection conn = null;
	        Statement stmt = null;
	        ResultSet rs = null;

	        String sql = "select * from tb_vehicle";
	        // String sql = "select trip_id, vehicle_id, note from tb_gps_1112_trip where state = 0 and id between " + startIdx + " and " + endIdx;
	        // System.out.println(sql);
	        try {
	            conn = getConn();
	            stmt = conn.createStatement();
	            rs = stmt.executeQuery(sql);
	            while (rs.next()) {
	            	String vehicle_id= rs.getString("vehicle_id");
	            	vehicleList.add(vehicle_id);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	           //  releaseSource(conn, stmt, rs);
	        }
	        
	        return vehicleList;
	    }
	 
	 public static void main(String[] args) {
		 /*GpsToTripDAO dao = new GpsToTripDAO();
		 ArrayList<Arc>  arcs = dao.queryArcList();
		 int i=0; 
		 for (Arc arc : arcs) {
			 if(i==3) break;
			
			System.out.println("------------------------------------------------");
			System.out.println(arc.getArcDetailList().size());
			ArrayList<ArcDetail> arcDetailList = arc.getArcDetailList();
			for (ArcDetail arcDetail : arcDetailList) {
				System.out.println(arcDetail.getLati() + "  " + arcDetail.getLongi());
			}
			i++;
		}*/
	}
}
