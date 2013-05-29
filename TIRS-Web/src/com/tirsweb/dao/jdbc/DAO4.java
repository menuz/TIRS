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
 * 此类描述的是：used for test algorithm for detecting candidate parking place
 * @author: dmnrei@gmail.com
 * @version: May 17, 2013 12:35:07 PM
 */
public class DAO4 extends JDBCDAO {
	 public void getArcMap(Map<Integer, Integer> arcAndOppositeArcMap) {
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
	 
	 
	 public void insertOneDirectionArc(ArrayList<Integer> arcList) {
		    Connection conn = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
		 
	        String sql = "insert into tb_arc_one values(?)";
		    try {
		    	conn = getConn();
				conn.setAutoCommit(false);
				pstmt = conn.prepareStatement(sql);
				for(int i = 0; i<arcList.size(); i++) {
					Integer id = arcList.get(i);
					pstmt.setInt(1, id);
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
}
