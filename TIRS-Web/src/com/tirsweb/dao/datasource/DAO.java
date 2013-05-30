/**
 * 文件名：DAO.java
 *
 * 版本信息： version 1.0
 * 日期：May 15, 2013
 * Copyright by menuz
 */
package com.tirsweb.dao.datasource;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.tirsweb.dao.DataSourceDAO;
import com.tirsweb.model.Arc;
import com.tirsweb.model.NodePoint;

/**
 * 
 * 此类描述的是： dao from daixiuting
 * 
 * @author: dmnrei@gmail.com
 * @version: May 15, 2013 4:24:23 PM
 */
public class DAO extends DataSourceDAO {
	private static InitialContext context = null;

	private DataSource dataSource = null;

	// 返回测试的路线
	public ArrayList<NodePoint> getpath_test() {
		// TODO Auto-generated method stub
		ArrayList<NodePoint> points = new ArrayList<NodePoint>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "select * from path_test";
		System.out.println(sql);
		try {
			conn = getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				NodePoint point = new NodePoint(rs.getInt("rowidx"),
						rs.getInt("columnidx"), rs.getDouble("latitude"),
						rs.getDouble("longitude"));
				points.add(point);
			}
			return points;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseSource(conn, stmt, rs);
		}
		return null;
	}

	public ArrayList<NodePoint> getpathbyid(String id) {
		// TODO Auto-generated method stub
		ArrayList<NodePoint> points = new ArrayList<NodePoint>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "select * from path_test where rowidx = " + id;
		System.out.println(sql);
		try {
			conn = getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				NodePoint point = new NodePoint(rs.getInt("rowidx"),
						rs.getInt("columnidx"), rs.getDouble("latitude"),
						rs.getDouble("longitude"));
				points.add(point);
			}
			return points;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseSource(conn, stmt, rs);
		}
		return null;
	}
	
	public ArrayList<NodePoint> getAllNode() {
		// TODO Auto-generated method stub
		ArrayList<NodePoint> points = new ArrayList<NodePoint>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "select * from tb_node";
		System.out.println(sql);
		try {
			conn = getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				NodePoint point = new NodePoint(rs.getDouble("lati"),
						rs.getDouble("longi"));
				points.add(point);
			}
			return points;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseSource(conn, stmt, rs);
		}
		return null;
	}
	
	public ArrayList<NodePoint> getArcParkingLocationList(String arcid) {
		// TODO Auto-generated method stub
		ArrayList<NodePoint> points = new ArrayList<NodePoint>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "select * from tb_parking_location where arc_id = " + arcid;
		System.out.println(sql);
		try {
			conn = getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				NodePoint point = new NodePoint(
						rs.getDouble("lati"),
						rs.getDouble("longi")
				);
				points.add(point);
			}
			return points;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseSource(conn, stmt, rs);
		}
		return null;
	}
	
	/**
	 * 
		 * 此方法描述的是：return startnode endnode latitude longitude of one arc
	     * @param arcid
	     * @return
	     * @version: May 28, 2013 10:02:11 PM
	 */
	public ArrayList<NodePoint> getArcByArcId(String arcid) {
		ArrayList<NodePoint> points = new ArrayList<NodePoint>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "select ta.id, ta.start_node_id, ta.end_node_id,tn1.lati lati1, tn1.longi longi1, tn2.lati lati2, tn2.longi longi2 from tb_arc ta, tb_node tn1, tb_node tn2  where ta.start_node_id = tn1.id and ta.end_node_id = tn2.id and  ta.id = " + arcid;
		System.out.println(sql);
		try {
			conn = getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				NodePoint point1 = new NodePoint(
						rs.getDouble("lati1"),
						rs.getDouble("longi1")
				);
				NodePoint point2 = new NodePoint(
						rs.getDouble("lati2"),
						rs.getDouble("longi2")
				);
				points.add(point1);
				points.add(point2);
			}
			return points;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseSource(conn, stmt, rs);
		}
		return null;
	}
	
	/**
	 * 
		 * 此方法描述的是：return one arc detail info of gps
	     * @param arcid
	     * @return
	     * @version: May 28, 2013 10:19:08 PM
	 */
	public ArrayList<NodePoint> getArcDetailByArcId(String arcid) {
		ArrayList<NodePoint> points = new ArrayList<NodePoint>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "select * from tb_arc_detail where arc_id = " + arcid;
		System.out.println(sql);
		try {
			conn = getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				NodePoint point = new NodePoint(
						rs.getInt("arc_id"),
						rs.getDouble("lati"),
						rs.getDouble("longi")
				);
				points.add(point);
			}
			return points;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseSource(conn, stmt, rs);
		}
		return null;
	}
	
	
	/**
	 * 
		 * 此方法描述的是：get all arc startnode.lati, startnode.longi, endnode.lati, endnode.longi in box boxid
	     * @param arcid
	     * @return
	     * @version: May 29, 2013 8:39:46 AM
	 */
	public ArrayList<NodePoint> getCorrespondingArcListByBoxId(String boxid) {
		ArrayList<NodePoint> points = new ArrayList<NodePoint>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		//  select * from tb_arc_box  tab, tb_arc ta where tab.arc_id = ta.id and tab.box_id = 536
		// select tmp.arc_id as arc_id , tn1.lati as lati1, tn1.longi as longi1, tn2.lati as lati2, tn2.longi as longi2 from tb_node tn1, tb_node tn2 , (select * from tb_arc_box  tab, tb_arc ta where tab.arc_id = ta.id and tab.box_id = 536) tmp where tn1.id = tmp.start_node_id and tn2.id = tmp.end_node_id


		String sql = "select tmp.arc_id as arc_id, tn1.lati as lati1, tn1.longi as longi1, tn2.lati as lati2, tn2.longi as longi2 from tb_node tn1, tb_node tn2 , (select * from tb_arc_box  tab, tb_arc ta where tab.arc_id = ta.id and tab.box_id = " + boxid + ") tmp where tn1.id = tmp.start_node_id and tn2.id = tmp.end_node_id";
		System.out.println(sql);
		try {
			conn = getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				NodePoint point1 = new NodePoint(
						rs.getInt("arc_id"),
						rs.getDouble("lati1"),
						rs.getDouble("longi1")
				);
				NodePoint point2 = new NodePoint(
						rs.getInt("arc_id"),
						rs.getDouble("lati2"),
						rs.getDouble("longi2")
				);
				points.add(point1);
				points.add(point2);
			}
			return points;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseSource(conn, stmt, rs);
		}
		return null;
	}

	public ArrayList<Arc> getarcbyid() {
		ArrayList<Arc> points = new ArrayList<Arc>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "select ta.id, ta.start_node_id, ta.end_node_id,tn1.lati lati1, tn1.longi longi1, tn2.lati lati2, tn2.longi longi2 from tb_arc ta, tb_node tn1, tb_node tn2  where ta.start_node_id = tn1.id and ta.end_node_id = tn2.id";
		System.out.println(sql);
		try {
			conn = getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Arc arc = new Arc(
						rs.getInt("id"),
						rs.getInt("start_node_id"),
						rs.getInt("end_node_id"),
						rs.getDouble("lati1"),
						rs.getDouble("longi1"), 
						rs.getDouble("lati2"),
						rs.getDouble("longi2")
				);
				points.add(arc);
			}
			return points;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseSource(conn, stmt, rs);
		}
		return null;
	}
	
	public void insertArcDetail(int arcid, double lat, double lon, int idx) {
		ArrayList<Arc> points = new ArrayList<Arc>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "insert into tb_arc_detail values(" + arcid + "," + lat + "," + lon + "," + idx + ")";
		System.out.println(sql);
		try {
			conn = getConn();
			stmt = conn.createStatement();
			stmt.execute(sql);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseSource(conn, stmt, rs);
		}
	}
	
	public ArrayList<String> getFreeTrip(String tripId, String vehicleId, String rownum) {
        ArrayList<String> notes = new ArrayList<String>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        String condition = "";
        if(!tripId.trim().equals("")) {
        	condition += (" and trip_id = " + tripId);
        }
        if(!vehicleId.trim().equals("")) {
        	condition += (" and vehicle_id = " + vehicleId);
        }
        if(!rownum.trim().equals("")) {
        	condition += (" and rownum = " + rownum);
        }
        
        String sql = "select note from temp_trip where state = 0 " + condition;
        System.out.println(sql);
        try {
            conn = getConn();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
            	Clob clob = rs.getClob("note");
            	String note = new String(clob.getSubString((long)1, (int)clob.length()));
            	notes.add(note);
            }
            return notes;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseSource(conn, stmt, rs);
        }
        return null;
    }
}