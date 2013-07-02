/**
 * 文件名：ComparatorPLCluster.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-5
 * Copyright by menuz
 */
package com.tirsweb.util.sort;

import java.util.Comparator;

import com.tirsweb.model.ParkingLocationCluster;

public class ComparatorPLCluster implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		ParkingLocationCluster plCluster1 = (ParkingLocationCluster)o1;
		ParkingLocationCluster plCluster2 = (ParkingLocationCluster)o2;
		return 0;
	}

}


