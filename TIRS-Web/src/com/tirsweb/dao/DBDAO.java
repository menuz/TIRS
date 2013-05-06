/**
 * 文件名：DBDAO.java
 *
 * 版本信息： version 1.0
 * 日期：Apr 22, 2013
 * Copyright by menuz
 */
package com.tirsweb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tirsweb.model.GPS;
import com.tirsweb.util.TableLoader;

public class DBDAO extends JDBCDAO{


	/**
	 * 
		 * 此方法描述的是：Query
	     * @return
	     * @author: dmnrei@gmail.com
	     * @version: 2013-3-31 下午6:31:40
	 */
    public List<GPS> queryByVechicleId(int v_id, int v_num, String time1, String time2) {
    	List<GPS> gpses = new ArrayList<GPS>();
		String sql = "select * from tb_gps_1112 where vehicle_id = "+v_id+" and to_char(speed_time, 'hh24:mi:ss') between '"
				+ time1
				+ "' and '"
				+ time2
				+ "' and rownum <= " + v_num;
		
System.out.println("sql = " + sql);		
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
        	conn = getConn();
        	stmt = conn.createStatement();
        	rs = stmt.executeQuery(sql);
            while (rs.next()) {
               gpses.add(TableLoader.loadGPS(rs));
            }      
System.out.println("gps count = " + gpses.size());
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	releaseSource(conn, stmt, rs);
        }
        return gpses;
    }
    
    public List<GPS> queryByVechicleId(int v_id, int v_num) {
    	List<GPS> gpses = new ArrayList<GPS>();
		// String sql = "select * from tb_gps_1112 where vehicle_id = " + v_id + " and rownum <= " + v_num + " order by message_id asc";
		String sql = "select * from tb_gps_1112 where vehicle_id = " + v_id + " and rownum <= " + v_num + " order by message_id asc";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
System.out.println("sql = " + sql);    
        try {
        	conn = getConn();
        	stmt = conn.createStatement();
        	rs = stmt.executeQuery(sql);
            while (rs.next()) {
               gpses.add(TableLoader.loadGPS(rs));
            }      
System.out.println("gps count = " + gpses.size());
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	releaseSource(conn, stmt, rs);
        }
        return gpses;
    }
    
    public List<GPS> queryByVechicleId(String v_id, String v_num, String time1, String time2) {
    	if(v_id == null || v_id.trim().equals("") ||
    			v_num == null || v_num.trim().equals("")) {
    		return null;
    	}
    	return queryByVechicleId(Integer.parseInt(v_id), Integer.parseInt(v_num), time1, time2);
    }
    
    public List<GPS> queryByVechicleId(String v_id, String v_num) {
    	if(v_id == null || v_id.trim().equals("") ||
    			v_num == null || v_num.trim().equals("")) {
    		return null;
    	}
    	return queryByVechicleId(Integer.parseInt(v_id), Integer.parseInt(v_num));
    }
}


