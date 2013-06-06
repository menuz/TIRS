/**
 * 文件名：TimeUtil.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-6
 * Copyright by menuz
 */
package com.tirsweb.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
	public static String getFormattedTime() {
		DateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		String time = format.format(new Date());
		return time;
	} 
	
	public static int getWeekday() {
		Calendar calendar = Calendar.getInstance();
		
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		if(day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
			return 1;
		}
		
		return 0;
	}
	
	public static int getHour() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		return hour;
	}
	
}


