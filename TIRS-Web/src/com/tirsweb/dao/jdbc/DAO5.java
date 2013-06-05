/**
 * 文件名：DAO3.java
 *
 * 版本信息： version 1.0
 * 日期：May 17, 2013
 * Copyright by menuz
 */
package com.tirsweb.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.tirsweb.dao.JDBCDAO;
import com.tirsweb.model.ParkingLocation;

/**
 * 
 * 此类描述的是：used for kmeans, query all parking place
 * @author: dmnrei@gmail.com
 * @version: May 17, 2013 12:35:07 PM
 */
public class DAO5 extends JDBCDAO {
	 public void getArcAndOppositeArcMap(Map<Integer, Integer> arcAndOppositeArcMap) {
	        Connection conn = null;
	        Statement stmt = null;
	        ResultSet rs = null;

	        String sql = "select v1.id as id1, v2.id as id2 from tb_arc v1, tb_arc v2 where v1.start_node_id = v2.end_node_id and v1.end_node_id = v2.start_node_id";
	        System.out.println(sql);
	        try {
	            conn = getConn();
	            stmt = conn.createStatement();
	            rs = stmt.executeQuery(sql);
	            while (rs.next()) {
	            	int id = rs.getInt("id1");
	            	int id_1= rs.getInt("id2");
	            	arcAndOppositeArcMap.put(id, id_1);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            releaseSource(conn, stmt, rs);
	        }
	    }
	
	
	public void getArcList(ArrayList<Integer> arcList) {
		 	Connection conn = null;
	        Statement stmt = null;
	        ResultSet rs = null;

	        String sql = "select unique(arc_id) from tb_parking_location where arc_id  != -1";
	        System.out.println(sql);
	        try {
	            conn = getConn();
	            stmt = conn.createStatement();
	            rs = stmt.executeQuery(sql);
	            while (rs.next()) {
	            	int arcId = rs.getInt("arc_id");
	            	arcList.add(arcId);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            releaseSource(conn, stmt, rs);
	        }
	}
	
	public void getParkingLocationUniqueArcId(ArrayList<Integer> arcList) {
		 Connection conn = null;
	        Statement stmt = null;
	        ResultSet rs = null;

	        String sql = "select unique(arc_id) from tb_parking_location";
	        System.out.println(sql);
	        try {
	            conn = getConn();
	            stmt = conn.createStatement();
	            rs = stmt.executeQuery(sql);
	            while (rs.next()) {
	            	int arcId = rs.getInt("arc_id");
	            	arcList.add(arcId);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            releaseSource(conn, stmt, rs);
	        }
	}
	
	
	public void getParkingLocation(ArrayList<ParkingLocation> pks) {
		    Connection conn = null;
	        Statement stmt = null;
	        ResultSet rs = null;

	        String sql = "select * from tb_parking_location";
	        System.out.println(sql);
	        try {
	            conn = getConn();
	            stmt = conn.createStatement();
	            rs = stmt.executeQuery(sql);
	            while (rs.next()) {
	            	String tripId = rs.getString("trip_id");
	            	String vehicleId = rs.getString("vehicle_id");
	            	double lati = rs.getDouble("lati");
	            	double longi = rs.getDouble("longi");
	            	int arcId = rs.getInt("arc_id");
	            	int id = rs.getInt("id");
	            	ParkingLocation pk = new ParkingLocation(tripId, vehicleId, lati, longi, arcId, id);
	            	pks.add(pk);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            releaseSource(conn, stmt, rs);
	        }
	}
	
	public void getParkingLocationByArcId(ArrayList<ParkingLocation> pks, String arc_id) {
		getParkingLocationByArcId(pks, Integer.parseInt(arc_id));
	}
	
	public void getParkingLocationByArcId(ArrayList<ParkingLocation> pks, int arc_id) {
	    Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        String sql = "select * from tb_parking_location where arc_id = " + arc_id;
        // System.out.println(sql);
        try {
            conn = getConn();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
            	String tripId = rs.getString("trip_id");
            	String vehicleId = rs.getString("vehicle_id");
            	double lati = rs.getDouble("lati");
            	double longi = rs.getDouble("longi");
            	int arcId = rs.getInt("arc_id");
            	int id = rs.getInt("id");
            	ParkingLocation pk = new ParkingLocation(tripId, vehicleId, lati, longi, arcId, id);
            	pks.add(pk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseSource(conn, stmt, rs);
        }
}
}
