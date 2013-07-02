/**
 * 文件名：DAO3.java
 *
 * 版本信息： version 1.0
 * 日期：May 17, 2013
 * Copyright by menuz
 */
package com.tirsweb.dao;

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
import com.tirsweb.model.TbArcSpeed;
import com.tirsweb.model.Trip;

/**
 * 
 * 此类描述的是：dao file for ArcToBox.java
 * @author: dmnrei@gmail.com
 * @version: May 17, 2013 12:35:07 PM
 */
public class TripToSegmentListDAO {
	
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
	public TripToSegmentListDAO() {
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
	
	
	 public ArrayList<Trip> queryTripWithRange(int startIdx, int endIdx) {
	    	Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			
			ArrayList<Trip> trips = new ArrayList<Trip>();
			
			String sql = "select * from tb_gps_1112_trip where id between " + startIdx + " and " + endIdx;
	        try {
		    	conn = getConn();
		    	stmt = conn.prepareStatement(sql);
		    	rs = stmt.executeQuery(sql);
				
				while (rs.next()) {
					int id = rs.getInt("id");
					String trip_id = rs.getString("trip_id");
					String vehicle_id = rs.getString("vehicle_id");
					Clob clob = rs.getClob("note");
	            	String note = new String(clob.getSubString((long)1, (int)clob.length()));
	            	String state = rs.getString("state");
	            	Trip trip = new Trip(id, trip_id, vehicle_id, note, state);

	            	trips.add(trip);
		        }
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
//	            releaseSource(conn, stmt, rs);
	        }
	        
	        return trips;
	    }
	
	
	 public ArrayList<Trip> getFreeTrip(int startIdx, int endIdx) {
	        ArrayList<Trip> trips = new ArrayList<Trip>();
	        Connection conn = null;
	        Statement stmt = null;
	        ResultSet rs = null;

	        // String sql = "select * from temp_trip where id between " + startIdx + " and " + endIdx;
	        String sql = "select trip_id, vehicle_id, note from tb_gps_1112_trip where state = 0 and id between " + startIdx + " and " + endIdx;
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
	           //  releaseSource(conn, stmt, rs);
	        }
	        return null;
	    }
	 
	 
	 public void insertArcSpeed(ArrayList<TbArcSpeed> arcSpeedList) {
		    Connection conn = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
		 
	        String sql = "insert into tb_arc_speed_copy(arc_id, weekday, hourofday, speed) values(?,?,?,?)";
		    try {
		    	conn = getConn();
				conn.setAutoCommit(false);
				pstmt = conn.prepareStatement(sql);
				for(int i = 0; i<arcSpeedList.size(); i++) {
					TbArcSpeed pl = arcSpeedList.get(i);
					pstmt.setInt(1, pl.getArc_id());
					pstmt.setInt(2, pl.getWeekday());
					pstmt.setInt(3, pl.getHourofday());
					pstmt.setDouble(4, pl.getSpeed());
				    pstmt.addBatch();
				}
				pstmt.executeBatch();
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
	           //  releaseSource(conn, pstmt, rs);
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
	           //  releaseSource(conn, stmt, rs);
	        }
		    return null;
	 }
	 
	 public static void main(String[] args) {
		 TripToSegmentListDAO dao = new TripToSegmentListDAO();
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
