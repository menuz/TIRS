/**
 * 文件名：LatitudeTest.java
 *
 * 版本信息： version 1.0
 * 日期：May 11, 2013
 * Copyright by menuz
 */
package com.tirsweb.hive.udf.test;

import org.junit.Test;

import com.tirsweb.hive.udf.LatitudeUDF;
import com.tirsweb.hive.udf.LongitudeUDF;

public class LatitudeTest {

	@Test
	public void test() {	
		LatitudeUDF latiUDF = new LatitudeUDF();
		String lati = latiUDF.evaluate(41);
		
		org.junit.Assert.assertEquals(lati,"30.165"); 
		
		LongitudeUDF longiUDF = new LongitudeUDF();
		String longi = longiUDF.evaluate(41);
		
		org.junit.Assert.assertEquals(longi,"120.005"); 
	}

}


