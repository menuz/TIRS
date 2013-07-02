/**
 * 文件名：DataSourceDAO.java
 *
 * 版本信息： version 1.0
 * 日期：May 10, 2013
 * Copyright by menuz
 */
package com.tirsweb.dao;

//import java.sql.Connection;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ZZDataSourceDAO extends AbstractDAO {
	private Context initContext;
	private Context envContext;
	private DataSource ds;
	
	public ZZDataSourceDAO() {
		try {
			initContext=new InitialContext();
			envContext=(Context)initContext.lookup("java:/comp/env");
			ds=(DataSource)envContext.lookup("jdbc/myoracle");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Connection getConn() {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			if(conn != null && !conn.isClosed()) 
				System.out.println("ping success");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void main(String[] args) {
		ZZDataSourceDAO dsDAO = new ZZDataSourceDAO();
		dsDAO.getConn();
	}
}


