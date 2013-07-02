package com.tirsweb.dao.test;

import static org.junit.matchers.JUnitMatchers.hasItems;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.tirsweb.dao.CacheDAO;
import com.tirsweb.model.Point;

public class CacheDaoTest {

	@Test
	public void test() {
		CacheDAO cacheDAO = new CacheDAO();
		Map<String, Point> offsets = new HashMap<String, Point>();
		cacheDAO.addOffsetToCache(offsets, "30.10", "120.10");
		Point p = offsets.get("30.10120.10");
		int lat = (int)p.getLat();
		int actual_lat = 30;
	    org.junit.Assert.assertSame("should be same", lat, actual_lat);
	}

}
