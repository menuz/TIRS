/**
 * 文件名：DAO3.java
 *
 * 版本信息： version 1.0
 * 日期：May 17, 2013
 * Copyright by menuz
 */
package com.tirsweb.dao.jdbc;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.tirsweb.dao.JDBCDAO;
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
public class ArcToBoxDAO extends JDBCDAO {
	 public ArrayList<Trip> getFreeTrip(String rownum) {
	        ArrayList<Trip> trips = new ArrayList<Trip>();
	        Connection conn = null;
	        Statement stmt = null;
	        ResultSet rs = null;

	        String sql = "select trip_id, vehicle_id, note from temp_trip where state = 0 and rownum <= " + rownum;
	        System.out.println(sql);
	        try {
	            conn = getConn();
	            stmt = conn.createStatement();
	            rs = stmt.executeQuery(sql);
	            while (rs.next()) {
	            	String tripId = rs.getString("trip_id");
	            	String vehicle_id= rs.getString("vehicle_id");
	            	Clob clob = rs.getClob("note");
	            	String note = new String(clob.getSubString((long)1, (int)clob.length()));
	            	Trip trip = new Trip(tripId, vehicle_id, note);
	            	trips.add(trip);
	            }
	            return trips;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            releaseSource(conn, stmt, rs);
	        }
	        return null;
	    }
	 
	 
	 public void insertParkingLocation(ArrayList<ParkingLocation> points) {
		    Connection conn = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
		 
	        String sql = "insert into tb_parking_location values(?,?,?,?,-1)";
		    try {
		    	conn = getConn();
				conn.setAutoCommit(false);
				pstmt = conn.prepareStatement(sql);
				for(int i = 0; i<points.size(); i++) {
					ParkingLocation pl = points.get(i);
					pstmt.setString(1, pl.getTripId());
					pstmt.setString(2, pl.getVehicleId());
					pstmt.setDouble(3, pl.getLati());
					pstmt.setDouble(4, pl.getLongi());
				    pstmt.addBatch();
				}
				pstmt.executeBatch();
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
	            releaseSource(conn, pstmt, rs);
	        }
	 }
	 
	 /**
	  * 
	 	 * 此方法描述的是：get all arc, one arc has many point to form arc detail
	      * @return
	      * @version: May 29, 2013 10:10:50 AM
	  */
	 public ArrayList<Arc> queryArcList() {
		    Connection conn = null;
	        Statement stmt = null;
	        ResultSet rs = null;
	        
	        // String sql = "select * from (select * from tb_arc_detail tad, tb_arc ta where tad.arc_id = ta.id order by tad.arc_id asc, tad.idx asc) a where rownum < 20";
		    String sql = "select * from tb_arc_detail tad, tb_arc ta where tad.arc_id = ta.id order by tad.arc_id asc, tad.idx asc";
	        try {
		    	conn = getConn();
		    	stmt = conn.prepareStatement(sql);
		    	rs = stmt.executeQuery(sql);
		    	
				int arcid = -1;
				ArrayList<Arc> arcList = new ArrayList<Arc>();
				Arc arc = null;
				ArrayList<ArcDetail> arcDetailList = null;
				while (rs.next()) {
					int arc_id = rs.getInt("arc_id");
					double lati = rs.getDouble("lati");
					double longi = rs.getDouble("longi");
					int idx = rs.getInt("idx");
					if(arcid != arc_id) {
						if(arc != null) {
							arcList.add(arc);
							arc.setArcDetailList(arcDetailList);
						}
						arc = new Arc();
						arc.setId(arc_id);
						arcDetailList = new ArrayList<ArcDetail>();
						arcid = arc_id;
					} 
				    ArcDetail arcDetail = new ArcDetail(lati, longi, idx);
					arcDetailList.add(arcDetail);
		        }
				
				if(arc != null) {
					arcList.add(arc);
					arc.setArcDetailList(arcDetailList);
				}
							
				return arcList;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
	            releaseSource(conn, stmt, rs);
	        }
		    return null;
	 }
	 
	 public static void main(String[] args) {
		 ArcToBoxDAO dao = new ArcToBoxDAO();
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
		}
	}
}
