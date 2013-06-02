/**
 * 文件名：JDBCDAO.java
 *
 * 版本信息： version 1.0
 * 日期：Apr 22, 2013
 * Copyright by menuz
 */
package com.tirsweb.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCDAO extends BaseDAO{
	/*private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://127.0.0.1:3306/restbus";
	private static String user = "root";
	private static String password = "gpsserver!@#$%";*/	
	
	 /** Oracle数据库连接URL*/
    private final static String url = "jdbc:oracle:thin:@192.168.1.111:1521:tirs1";
	// private final static String url = "jdbc:oracle:thin:@60.191.28.14:1521:webgps3";
    /** Oracle数据库连接驱动*/
    private final static String driver = "oracle.jdbc.driver.OracleDriver";
    /** 数据库用户名*/
    private final static String user = "HZGPS_TAXI";
    /** 数据库密码*/
    private final static String password = "HZGPS_TAXI";
    
    
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
	public JDBCDAO() {
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
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			if (conn!= null && !conn.isClosed());
				// System.out.println("Succeeded connecting to the Database!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void main(String[] args) {
		JDBCDAO dao = new JDBCDAO();
		dao.getConn();
	}
}


