/**
 * 文件名：JCacheDAO.java
 *
 * 版本信息： version 1.0
 * 日期：May 18, 2013
 * Copyright by menuz
 */
package com.tirsweb.dao.jdbc;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import com.tirsweb.dao.JDBCDAO;
import com.tirsweb.model.Arc;
import com.tirsweb.model.ArcDetail;
import com.tirsweb.model.Node;
import com.tirsweb.model.ParkingLocation;
import com.tirsweb.model.Point;
import com.tirsweb.model.Speed;
import com.tirsweb.model.Trip;
import com.tirsweb.model.Up;

public class JCacheDAO extends JDBCDAO {
private final String TAG = "CacheDAO ";
	
	public JCacheDAO() {}
    
    public void addOffsetToCache(Map<String, Point> offsets, String lat, String lon) {
    	Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConn();
			String sql = "select offsetlat, offsetlon from tb_gps_correct where lat=" + lat + " and lon=" + lon + "";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()) { 
				double offsetlat = rs.getDouble("offsetlat");
				double offsetlon = rs.getDouble("offsetlon");
// System.out.println("offsetlat = " + offsetlat + " offsetlon = " + offsetlon);
				offsets.put(lat+lon, new Point(offsetlat, offsetlon));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(TAG + "function@gpsToMarGps");
		} finally {
			releaseSource(conn, stmt, rs);
		}
    }
    
    // load boxList, arcDetailList, arc_id
    public void queryAllArc(Map<Integer, Arc> arcMap) {
    	Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		String sql = "select * from tb_arc_detail tad, tb_arc ta where tad.arc_id = ta.id order by tad.arc_id asc, tad.idx asc";
		// String sql = "select * from (select * from tb_arc_detail tad, tb_arc ta where tad.arc_id = ta.id order by tad.arc_id asc, tad.idx asc) a where rownum < 20";
        try {
	    	conn = getConn();
	    	stmt = conn.prepareStatement(sql);
	    	rs = stmt.executeQuery(sql);
	    	
			int arcid = -1;
			//ArrayList<Arc> arcList = new ArrayList<Arc>();
			Arc arc = null;
			ArrayList<ArcDetail> arcDetailList = null;
			while (rs.next()) {
				int arc_id = rs.getInt("arc_id");
				double lati = rs.getDouble("lati");
				double longi = rs.getDouble("longi");
				int idx = rs.getInt("idx");
				double length = rs.getDouble("len");
				int start_node_id = rs.getInt("start_node_id");
				int end_node_id = rs.getInt("end_node_id");
				
				if(arcid != arc_id) {
					if(arc != null) {
						//arcList.add(arc);
						// add old arcid
						arcMap.put(arcid, arc);
						arc.setArcDetailList(arcDetailList);
						ArrayList<Integer> boxList = getBoxListByArcId(arc_id);
						arc.setBoxList(boxList);
					}
					arc = new Arc();
					arc.setId(arc_id);
					arc.setStart_node_id(start_node_id);
					arc.setEnd_node_id(end_node_id);
					arcDetailList = new ArrayList<ArcDetail>();
					arcid = arc_id;
				} 
				
			    ArcDetail arcDetail = new ArcDetail(lati, longi, idx);
				arcDetailList.add(arcDetail);
	        }
			if(arc != null) {
				// arcList.add(arc);
				arcMap.put(arc.getId(), arc);
				arc.setArcDetailList(arcDetailList);
				ArrayList<Integer> boxList = getBoxListByArcId(arc.getId());
				arc.setBoxList(boxList);
			}
					
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
            releaseSource(conn, stmt, rs);
        }
    }
    
    
    public void queryAllNode(Map<Integer, Node> nodes) {
    	Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		String sql = "select * from tb_node";
        try {
	    	conn = getConn();
	    	stmt = conn.prepareStatement(sql);
	    	rs = stmt.executeQuery(sql);
	    	
			while (rs.next()) {
				int arc_id = rs.getInt("id");
				double lati = rs.getDouble("lati");
				double longi = rs.getDouble("longi");
				Node node = new Node(arc_id, lati, longi);
				nodes.put(arc_id, node);
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
            releaseSource(conn, stmt, rs);
        }
    }
    
    
    public void queryBoxAndArcMap(Map<Integer, ArrayList<Integer>> boxAndArcMap) {
    	Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		String sql = "select * from tb_arc_box order by box_id asc";
        try {
	    	conn = getConn();
	    	stmt = conn.prepareStatement(sql);
	    	rs = stmt.executeQuery(sql);
	    	
			int arcid = -1;
			int boxid = -1;
			ArrayList<Integer> arcList = null;
			while (rs.next()) {
				int arc_id = rs.getInt("arc_id");
				int box_id = rs.getInt("box_id");
				
				if(boxid != box_id) {
					if(arcList != null) {
						boxAndArcMap.put(boxid, arcList);
					}
					arcid = arc_id;
					boxid = box_id;
					arcList = new ArrayList<Integer>();
				}
				arcList.add(arc_id);
	        }
			if(boxid != -1) {
				boxAndArcMap.put(boxid, arcList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
            releaseSource(conn, stmt, rs);
        }
    }
    
    public void queryParkingLocation(ArrayList<ParkingLocation> parkingList) {
    	Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		String sql = "select * from tb_parking_location where lati != 0";
        try {
	    	conn = getConn();
	    	stmt = conn.prepareStatement(sql);
	    	rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				int id = rs.getInt("id");
				String trip_id = rs.getString("trip_id");
				String vehicle_id = rs.getString("vehicle_id");
				double lati = rs.getDouble("lati");
				double longi = rs.getDouble("longi");
				int arc_id = rs.getInt("arc_id");
				
				ParkingLocation pl = new ParkingLocation(trip_id, vehicle_id, lati, longi, arc_id,id);
				parkingList.add(pl);
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
            releaseSource(conn, stmt, rs);
        }
    }
    
    
    
    public void queryTrip(ArrayList<Trip> trips) {
    	Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		String sql = "select * from temp_trip";
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
            releaseSource(conn, stmt, rs);
        }
    }
    
    /**
     * 
    	 * 此方法描述的是：query trip with range
         * @param trips
         * @param startIdx
         * @param endIdx
         * @version: May 19, 2013 12:29:18 PM
     */
    public void queryTripWithRange(ArrayList<Trip> trips, int startIdx, int endIdx) {
    	Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		String sql = "select * from temp_trip where id between " + startIdx + " and " + endIdx;
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
            releaseSource(conn, stmt, rs);
        }
    }
    
    public ArrayList<Integer> getBoxListByArcId(int arcId) {
    	Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		ArrayList<Integer> boxList = new ArrayList<Integer>();
		try {
			conn = getConn();
			String sql = "select * from tb_arc_box where arc_id = " + arcId;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) { 
				int _arcId = rs.getInt("arc_id");
				int _boxId = rs.getInt("box_id");
				boxList.add(_boxId);
			}
			return boxList;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(TAG + "function@gpsToMarGps");
		} finally {
			releaseSource(conn, stmt, rs);
		}
    	return null;
    }
    
    
    /**
     * 
    	 * 此方法描述的是： query tb_gps_average_speed, get record by boxid, weekday, hour
         * @param speeds
         * @param boxList
         * @param weekday
         * @param hour
         * @version: May 10, 2013 9:23:15 PM
     */
    public ArrayList<Speed> querySpeedListByKeys(ArrayList<Integer> boxList,
    		int weekday, int hour) {
    	Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<Speed> spdList = null;
		
		try {
			conn = getConn();
			stmt = conn.createStatement();
			spdList = new ArrayList<Speed>();
			
			String boxListStr = "";
	    	for (Integer boxid : boxList) {
				boxListStr += ( boxid + "," );
			}
	    	String sql = "select * from tb_gps_average_speed where weekday = " + weekday + 
	    			" and hour = " + hour + " and box in (" + boxList + ")";
			
			rs = stmt.executeQuery(sql);
			while(rs.next()) { 
				int _box_id = rs.getInt("box_id");
				int _weekday = rs.getInt("weekday");
				int _hour = rs.getInt("hour");
				int _speed = rs.getInt("speed");
				Speed spd = new Speed(_box_id, _weekday, _hour, _speed);
				spdList.add(spd);
			}
			
			return spdList;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(TAG + "function@gpsToMarGps");
		} finally {
			releaseSource(conn, stmt, rs);
		}
		return spdList;
    }
    
    /**
     * 
    	 * 此方法描述的是： query tb_gps_average_up, get record by boxid, weekday, hour
         * @param boxList
         * @param weekday
         * @param hour
         * @return
         * @version: May 11, 2013 10:55:20 AM
     */
    public ArrayList<Up> queryUpListByKeys(ArrayList<Integer> boxList,
    		int weekday, int hour) {
    	Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<Up> upList = null;
		
		try {
			conn = getConn();
			stmt = conn.createStatement();
			upList = new ArrayList<Up>();
			
			String boxListStr = "";
	    	for (Integer boxid : boxList) {
				boxListStr += ( boxid + "," );
			}
	    	String sql = "select * from tb_gps_average_up where weekday = " + weekday + 
	    			" and hour = " + hour + " and box in (" + boxList + ")";
			
			rs = stmt.executeQuery(sql);
			while(rs.next()) { 
				int _box_id = rs.getInt("box_id");
				int _weekday = rs.getInt("weekday");
				int _hour = rs.getInt("hour");
				int _count = rs.getInt("count");
				Up spd = new Up(_box_id, _weekday, _hour, _count);
				upList.add(spd);
			}  
			
			return upList;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(TAG + "function@gpsToMarGps");
		} finally {
			releaseSource(conn, stmt, rs);
		}
		return upList;
    }
    
    
    public Up queryUpByKeys(int boxid, int weekday, int hour) {
    	Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Up up = null;
		
		try {
			conn = getConn();
			stmt = conn.createStatement();
			up = new Up();
			
	    	String sql = "select * from tb_gps_average_up where weekday = " + weekday + 
	    			" and hour = " + hour + " and box = " + boxid;
			
			rs = stmt.executeQuery(sql);
			if(rs.next()) { 
				int _box_id = rs.getInt("box_id");
				int _weekday = rs.getInt("weekday");
				int _hour = rs.getInt("hour");
				int _count = rs.getInt("count");
				up = new Up(_box_id, _weekday, _hour, _count);
			}  
			
			return up;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(TAG + "function@gpsToMarGps");
		} finally {
			releaseSource(conn, stmt, rs);
		}
		return up;
    }

}


