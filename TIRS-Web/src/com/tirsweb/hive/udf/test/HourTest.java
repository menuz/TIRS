package com.tirsweb.hive.udf.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tirsweb.hive.udf.HourUDF;

public class HourTest {

	@Test
	public void test() {
		HourUDF hour = new HourUDF();
		
		int h = hour.evaluate("2013-05-09 00:03:02.0");
		org.junit.Assert.assertEquals(h,0);
		
		h = hour.evaluate("2013-05-09 23:03:02.0");
		org.junit.Assert.assertEquals(h,23);
	}

}
