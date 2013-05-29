/**
 * 文件名：LongitudeUDF.java
 *
 * 版本信息： version 1.0
 * 日期：May 11, 2013
 * Copyright by menuz
 */
package com.tirsweb.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

public class LongitudeUDF extends UDF {

	public String evaluate(int boxid) {
			if(boxid == -1)  return "0.0"; 
			
			int row = -1;
			int col = -1;
			if(boxid % 40 == 0) {
				row = boxid / 40;
			} else {
				row = boxid / 40 + 1;
			}
			
			col = boxid - 40*(row - 1);
			
			double lati = 30.15;
			double longi = 120.00;
			
			lati = lati + 0.01 * row - 0.005;
			longi = longi + 0.01 * col - 0.005;
			
			String lonStr = String.valueOf(longi);
			if(lonStr.length() >= 7) {
				lonStr = lonStr.substring(0, 7);
			}
			
			return lonStr;
		}
	
	public static void main(String[] args) {
		LongitudeUDF lu = new LongitudeUDF();
		System.out.println(lu.evaluate(1));
	}
}


