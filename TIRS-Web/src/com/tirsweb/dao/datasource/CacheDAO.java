package com.tirsweb.dao.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import com.tirsweb.dao.DataSourceDAO;
import com.tirsweb.model.Arc;
import com.tirsweb.model.ArcDetail;
import com.tirsweb.model.Point;
import com.tirsweb.model.Speed;
import com.tirsweb.model.Up;

/**
 * 
 * 此类描述的是：Cache类的数据库操作类
 * @author: dmnrei@gmail.com
 * @version: Nov 12, 2012 6:41:12 PM
 */
public class CacheDAO extends DataSourceDAO {
	private final String TAG = "CacheDAO ";
	
    public CacheDAO() {
       super();
    }
    
    
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
System.out.println("offsetlat = " + offsetlat + " offsetlon = " + offsetlon);
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
    public Map<Integer, Arc> queryAllArc() {
    	Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<Integer, Arc> arcMap = null;
		
		// String sql = "select * from tb_arc_detail tad, tb_arc ta where tad.arc_id = ta.id order by tad.arc_id asc, tad.idx asc";
		String sql = "select * from (select * from tb_arc_detail tad, tb_arc ta where tad.arc_id = ta.id order by tad.arc_id asc, tad.idx asc) a where rownum < 20";
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
				
				if(arcid != arc_id) {
					if(arc != null) {
						//arcList.add(arc);
						arcMap.put(arc_id, arc);
						arc.setArcDetailList(arcDetailList);
						ArrayList<Integer> boxList = getBoxListByArcId(arc_id);
						arc.setBoxList(boxList);
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
				// arcList.add(arc);
				arcMap.put(arc.getId(), arc);
				arc.setArcDetailList(arcDetailList);
				ArrayList<Integer> boxList = getBoxListByArcId(arc.getId());
				arc.setBoxList(boxList);
			}
						
			return arcMap;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
            releaseSource(conn, stmt, rs);
        }
	    return null;
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
			
			System.out.println(sql);
			
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
