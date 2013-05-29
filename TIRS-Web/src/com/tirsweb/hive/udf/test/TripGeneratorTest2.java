package com.tirsweb.hive.udf.test;

import org.junit.Test;

import com.tirsweb.hive.udf.TripGeneratorUDF2;

public class TripGeneratorTest2 {

	@Test
	public void test() {
		TripGeneratorUDF2 tg = new TripGeneratorUDF2();
		String tripId = tg.evaluate("10001", "1", "2011-12-01 00:03:43.0");
		String actual_tripId = "10001+20111201000343";
		org.junit.Assert.assertEquals(tripId,actual_tripId);
		
		tripId = tg.evaluate("10001", "1", "2011-12-01 00:44:43.0");
		actual_tripId = "10001+20111201000343";
		org.junit.Assert.assertEquals(tripId,actual_tripId);
		
		tripId = tg.evaluate("10001", "0", "2011-12-01 00:55:43.0");
		actual_tripId = "10001+20111201005543";
		org.junit.Assert.assertEquals(tripId,actual_tripId);
		
		tripId = tg.evaluate("10002", "0", "2011-12-01 00:05:43.0");
		actual_tripId = "10002+20111201000543";
		org.junit.Assert.assertEquals(tripId,actual_tripId);
		
		tripId = tg.evaluate("10002", "0", "2011-12-01 00:06:43.0");
		actual_tripId = "10002+20111201000543";
		org.junit.Assert.assertEquals(tripId,actual_tripId);
		
		tripId = tg.evaluate("10002", "1", "2011-12-01 00:07:43.0");
		actual_tripId = "10002+20111201000743";
		org.junit.Assert.assertEquals(tripId,actual_tripId);
		
		/*tripId = tg.evaluate("10001", "0", "2011-12-01 00:55:43.0");
		actual_tripId = "10001+20111201005543";
		org.junit.Assert.assertEquals(tripId,actual_tripId);
		
		tripId = tg.evaluate("10002", "0", "2011-12-01 00:05:43.0");
		actual_tripId = "10002+20111201000543";
		org.junit.Assert.assertEquals(tripId,actual_tripId);
		
		tripId = tg.evaluate("10002", "1", "2011-12-01 00:15:43.0");
		actual_tripId = "10002+20111201001543";
		org.junit.Assert.assertEquals(tripId,actual_tripId);
		
		tripId = tg.evaluate("10003", "1", "2011-12-01 00:04:43.0");
		actual_tripId = "10003+20111201000443";
		org.junit.Assert.assertEquals(tripId,actual_tripId);*/
	}

}


