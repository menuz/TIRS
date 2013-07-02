/**
 * 文件名：DAO.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-21
 * Copyright by menuz
 */
package com.tirsweb.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * 
 * 此类描述的是：兼容JDBC，DataSource两种连接数据库
 * @author: dmnrei@gmail.com
 * @version: 2013-6-21 下午8:28:16
 */
public class BaseDAO extends AbstractDAO {
	public static int DATASOURCE = 0;
	public static int JDBC = 1;
	private final int connectType;

	/** Oracle数据库连接URL */
	// private final static String url = "jdbc:oracle:thin:@192.168.1.111:1521:tirs1";
	private final static String url = "jdbc:oracle:thin:@60.191.28.14:1521:webgps3";
	/** Oracle数据库连接驱动 */
	private final static String driver = "oracle.jdbc.driver.OracleDriver";
	/** 数据库用户名 */
	private final static String user = "HZGPS_TAXI";
	/** 数据库密码 */
	private final static String password = "HZGPS_TAXI";

	private Context initContext;
	private Context envContext;
	private DataSource ds;
	
	Connection conn = null;

	public BaseDAO() {
		this.connectType = BaseDAO.DATASOURCE;

		register();
	}

	public BaseDAO(int type) {
		this.connectType = type;
		
		register();
	}
	
	private void register() {
		if (this.connectType == BaseDAO.DATASOURCE) {
			try {
				initContext = new InitialContext();
				envContext = (Context) initContext.lookup("java:/comp/env");
				ds = (DataSource) envContext.lookup("jdbc/myoracle");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		} else if (this.connectType == BaseDAO.JDBC) {
			try {
				Class.forName(driver);
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}
	
	@Override
	public Connection getConn() {
		try {
			/**
			 * make sure to reuse conn if conn is not shutted.
			 */
			if(conn != null &&
					!conn.isClosed()) {
				return this.conn;
			} 
			
			if(this.connectType == BaseDAO.DATASOURCE) {
				
				try {
					conn = ds.getConnection();
					if(conn != null && !conn.isClosed());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			} else if (this.connectType == BaseDAO.JDBC) {
				
				try {
					conn = DriverManager.getConnection(url, user, password);
					if (conn!= null && !conn.isClosed());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return this.conn;
	}
}
