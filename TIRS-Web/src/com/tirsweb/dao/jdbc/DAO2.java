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

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.tirsweb.dao.JDBCDAO;
import com.tirsweb.model.NodePoint;

/**
 * 
 * 此类描述的是： dao from daixiuting, used for query path_test
 * @author: dmnrei@gmail.com
 * @version: May 15, 2013 4:24:23 PM
 */
public class DAO2 extends JDBCDAO{
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