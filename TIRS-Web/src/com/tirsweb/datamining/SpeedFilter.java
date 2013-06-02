/**
 * 文件名：SpeedFilter.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-2
 * Copyright by menuz
 */
package com.tirsweb.datamining;

public class SpeedFilter {
	double speed;
	int MinSpeed = 0;
	int MaxSpeed = 30;
	public SpeedFilter() {
		
	}
	
	public double filter(double speed) {
		if(speed > MaxSpeed || speed < MinSpeed) {
			return -1;
		}
		return speed;
	}

	public double filter(String speed) {
		return filter(Double.parseDouble(speed));
	}
}


