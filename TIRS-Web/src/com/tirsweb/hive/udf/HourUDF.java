package com.tirsweb.hive.udf;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.hive.ql.exec.UDF;

public class HourUDF extends UDF {
	public int evaluate(String db_time) {
		DateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");        
		Date date = null;
		try {
			date = format.parse(db_time);
			int hour = date.getHours();
			return hour;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}
}
