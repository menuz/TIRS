/**
 * 文件名：ParamConvertor.java
 *
 * 版本信息： version 1.0
 * 日期：May 10, 2013
 * Copyright by menuz
 */
package com.tirsweb.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * 此类描述的是: extract useful part of request parameter 
 * @author: dmnrei@gmail.com
 * @version: May 10, 2013 7:59:03 PM
 */
public class ParamConvertor {
	
	/**
	 * 
		 * 此方法描述的是：
	     * @param lati   taxi latitude
	     * @param longi  taxi longitude
	     * @return  hangzhou divided into 1000 box, get box id by lati, longi
	     * @version: May 10, 2013 8:02:52 PM
	 */
	public static int getBoxLocation(String lati, String longi) {
		if(lati.trim().equals("0.0") || longi.trim().equals("0.0")) {
			return -1;
		}
		
		java.text.DecimalFormat df =new   java.text.DecimalFormat("#.00");
		
		if(lati.length() >= 5) lati = lati.substring(0, 5);
		if(longi.length() >= 6)  longi = longi.substring(0, 6);
		
		double lat = Double.valueOf(lati);
		double lon = Double.valueOf(longi);
		
		double minLat = 30.15;
		double minLon = 120.00;
		
		int row = (int)((lat - minLat) / 0.01) + 1;
		int col = (int)((lon - minLon) / 0.01) + 1;
		int box = 40*(row-1) + col;
		
		if(row <= 0 || col <= 0)  return -1;
		if(row >= 25 || col >= 40) return -1;
		
		return box;
	}
	
	/**
	 * 
		 * 此方法描述的是：get box 
	     * @param box
	     * @return
	     * @version: May 10, 2013 8:19:12 PM
	 */
	public static List<Integer> getNearBoxList(int box) {
		int a8=box+40; int a7=a8-1; int a9=a8+1;
		int a5=box; int a4=a5-1; int a6=a5+1;
		int a2=box-40; int a1=a2-1; int a3=a2+1;
		
		List<Integer> boxlist = new ArrayList<Integer>();
		
		if(a1 >= 1 && a1 <= 1000) boxlist.add(a1);
		if(a2 >= 1 && a2 <= 1000) boxlist.add(a2);
		if(a3 >= 1 && a3 <= 1000) boxlist.add(a3);
		if(a4 >= 1 && a4 <= 1000) boxlist.add(a4);
		if(a5 >= 1 && a5 <= 1000) boxlist.add(a5);
		if(a6 >= 1 && a6 <= 1000) boxlist.add(a6);
		if(a7 >= 1 && a7 <= 1000) boxlist.add(a7);
		if(a8 >= 1 && a8 <= 1000) boxlist.add(a8);
		
		return boxlist;
	}
	
	/**
	 * 
		 * 此方法描述的是：return current hour of day
	     * @param uploadTime
	     * @return 
	     * @version: May 10, 2013 8:03:36 PM
	 */
	public static int getHour(String uploadTime) {
		DateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");        
		Date date = null;
		try {
			date = format.parse(uploadTime);
			int hour = date.getHours();
			return hour;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * 
		 * 此方法描述的是：weekday return 0  weekend return 1
	     * @param uploadTime
	     * @return
	     * @version: May 10, 2013 8:03:47 PM
	 */
	public static int getWeekday(String uploadTime) {
		DateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");        
		Date date = null;
		try {
			date = format.parse(uploadTime);
			int day = date.getDay();
			if(day == 0 || day == 6) {
				return 1;
			} else {
				return 0;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}
}	


