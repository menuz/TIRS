package com.tirsweb.hive.udf.test;

import org.junit.Test;

import com.tirsweb.hive.udf.BoxUDF;

public class BoxTest {

	@Test
	public void test() {
		BoxUDF box = new BoxUDF();
		int boxid = box.evaluate("30.50", "119.90");
		org.junit.Assert.assertEquals(boxid,-1); 
		boxid = box.evaluate("30.50", "120.20");
		org.junit.Assert.assertEquals(boxid,-1); 
		boxid = box.evaluate("30.50", "120.50");
		org.junit.Assert.assertEquals(boxid,-1); 
		
		boxid = box.evaluate("30.20", "119.90");
		org.junit.Assert.assertEquals(boxid,-1); 
		boxid = box.evaluate("30.20", "120.50");
		org.junit.Assert.assertEquals(boxid,-1); 
		
		boxid = box.evaluate("30.10", "119.90");
		org.junit.Assert.assertEquals(boxid,-1); 
		boxid = box.evaluate("30.10", "120.20");
		org.junit.Assert.assertEquals(boxid,-1); 
		boxid = box.evaluate("30.10", "120.50");
		org.junit.Assert.assertEquals(boxid,-1); 
		
		
		boxid = box.evaluate("30.155", "120.005");
		org.junit.Assert.assertEquals(boxid,1);
		boxid = box.evaluate("30.155", "120.015");
		org.junit.Assert.assertEquals(boxid,2);
		boxid = box.evaluate("30.165", "120.005");
		org.junit.Assert.assertEquals(boxid,41);
		
		boxid = box.evaluate("30.2", "120.005");
		org.junit.Assert.assertEquals(boxid,201);
	}

}
