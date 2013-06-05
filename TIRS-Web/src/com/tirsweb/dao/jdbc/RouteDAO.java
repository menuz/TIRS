/**
 * 文件名：DAO.java
 *
 * 版本信息： version 1.0
 * 日期：May 15, 2013
 * Copyright by menuz
 */
package com.tirsweb.dao.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.tirsweb.dao.JDBCDAO;
import com.tirsweb.model.Node;
import com.tirsweb.model.NodePoint;

/**
 * 
 * 此类描述的是： dao from daixiuting, used for query path_test
 * @author: dmnrei@gmail.com
 * @version: May 15, 2013 4:24:23 PM
 */
public class RouteDAO extends JDBCDAO{
	 public void queryAllNode(Node[] nodes) {
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
					nodes[arc_id] = node;
		        }
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
	            releaseSource(conn, stmt, rs);
	        }
	}
	 
	public void initNodeRelation(Node[] nodes) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		String sql = "select * from tb_arc order by id asc";
        try {
	    	conn = getConn();
	    	stmt = conn.prepareStatement(sql);
	    	rs = stmt.executeQuery(sql);
	    	
			while (rs.next()) {
				int start_node_id = rs.getInt("start_node_id");
				int end_node_id = rs.getInt("end_node_id");
				int len = rs.getInt("len2");
				nodes[start_node_id].getChild().put(nodes[end_node_id], len);
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
            releaseSource(conn, stmt, rs);
		}
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
            	NodePoint point = new NodePoint(
            			rs.getInt("rowidx"),
            			rs.getInt("columnidx"),
            			rs.getDouble("latitude"),
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
}