package com.tirsweb.hive.udf.test;

import org.junit.Test;

import com.tirsweb.hive.udf.TripGeneratorUDF;

public class TripGeneratorTest {

	@Test
	public void testGenerateTripId() {
		TripGeneratorUDF tg = new TripGeneratorUDF();
		String tripId = tg.evaluate("10001", "1", "2011-12-01 00:03:43.0");
		String actual_tripId = "10001+20111201000343";
		org.junit.Assert.assertEquals(tripId,actual_tripId);
		
		tripId = tg.evaluate("10001", "1", "2011-12-01 00:44:43.0");
		actual_tripId = "10001+20111201000343";
		org.junit.Assert.assertEquals(tripId,actual_tripId);
		
		tripId = tg.evaluate("10001", "0", "2011-12-01 00:55:43.0");
		actual_tripId = "10001+20111201005543";
		org.junit.Assert.assertEquals(tripId,actual_tripId);
	}

}
