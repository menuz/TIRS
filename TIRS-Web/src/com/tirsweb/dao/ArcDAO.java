/**
120.080856 * 文件名：DAO.java
 *
 * 版本信息： version 1.0
 * 日期：May 15, 2013
 * Copyright by menuz
 */
package com.tirsweb.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.tirsweb.model.NodePoint;
import com.tirsweb.util.gps.GeoDistance;

/**
 * 
 * 此类描述的是：Arc表相关的操作类
 * 
 * @author: dmnrei@gmail.com
 * @version: 2013-6-3 下午2:17:20
 */
public class ArcDAO extends BaseDAO {
	
	public ArcDAO() {
		super(BaseDAO.JDBC);
	}
	
	
	// 返回测试的路线
	public void updateLen() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "select ta.id as id, tn1.lati as lati1, tn1.longi as longi1, tn2.lati as lati2, tn2.longi as longi2 from tb_arc ta, tb_node tn1, tb_node tn2 where ta.START_NODE_ID = tn1.id and ta.END_NODE_ID = tn2.id";
		System.out.println(sql);
		try {
			conn = getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				double lati1 = rs.getDouble("lati1");
				double longi1 = rs.getDouble("longi1");
				double lati2 = rs.getDouble("lati2");
				double longi2 = rs.getDouble("longi2");
				
				double dis = GeoDistance.computeCompareDistance(lati1, longi1, lati2, longi2);
				
				updateArcLenByArcId(id, dis);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseSource(conn, stmt, rs);
		}
	}

	public int updateArcLenByArcId(int arcid, double len3) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "update tb_arc set len3 =" + len3 + " where id = " + arcid;
		System.out.println(sql);
		try {
			conn = getConn();
			stmt = conn.createStatement();
			int result = stmt.executeUpdate(sql);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			releaseSource(conn, stmt, rs);
		}
		return 0;
	}
}