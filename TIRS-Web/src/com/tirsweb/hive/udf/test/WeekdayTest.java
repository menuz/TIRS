package com.tirsweb.hive.udf.test;

import org.junit.Test;

import com.tirsweb.hive.udf.WeekdayUDF;

public class WeekdayTest {

	@Test
	public void test() {
		WeekdayUDF weekday = new WeekdayUDF();
		int day = weekday.evaluate("2013-05-09 00:03:02.0");
		org.junit.Assert.assertEquals(day,0); 
		
		day = weekday.evaluate("2013-05-11 00:03:02.0");
		org.junit.Assert.assertEquals(day,1); 
	}

}
