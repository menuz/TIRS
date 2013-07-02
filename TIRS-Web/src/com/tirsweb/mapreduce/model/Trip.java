/**
 * 文件名：Trip.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-7
 * Copyright by menuz
 */
package com.tirsweb.mapreduce.model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Trip {
	public static String timestampToTripId(String time) {
		//DateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		Timestamp date = Timestamp.valueOf(time);
		DateFormat format= new SimpleDateFormat("yyyyMMddHHmmss");
//		System.out.println("time = " + format.format(date));
		return format.format(date);
	}
	
	public static void main(String[] args) {
		String str = Trip.timestampToTripId("2012-11-11 12:12:12.0");
	}
}


