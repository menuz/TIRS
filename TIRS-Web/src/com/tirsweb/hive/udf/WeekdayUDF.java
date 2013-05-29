package com.tirsweb.hive.udf;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.hive.ql.exec.UDF;

public class WeekdayUDF extends UDF {
	
	public int evaluate(String db_time) {
		DateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");        
		Date date = null;
		try {
			date = format.parse(db_time);
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
