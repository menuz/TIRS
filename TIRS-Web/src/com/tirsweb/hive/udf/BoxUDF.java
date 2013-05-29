package com.tirsweb.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

public class BoxUDF extends UDF {
	public int evaluate(String lati, String longi) {
		if(lati.trim().equals("0.0") || longi.trim().equals("0.0")) {
			return -1;
		}
		
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		
		if(lati.length() >= 5) lati = lati.substring(0, 5);
		if(longi.length() >= 6)  longi = longi.substring(0, 6);
		
		double lat = Double.valueOf(lati);
		double lon = Double.valueOf(longi);
		
		double minLat = 30.15;
		double minLon = 120.00;
		
		int row = (int)((lat - minLat) / 0.01) + 1;
		int col = (int)((lon - minLon) / 0.01) + 1;
		int box = 40*(row-1) + col;
		
		if(row <= 0 || col <= 0)  return -1;
		if(row >= 25 || col >= 40) return -1;

		return box;
	}
}
