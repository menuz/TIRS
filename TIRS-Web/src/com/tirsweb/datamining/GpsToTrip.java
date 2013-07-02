/**
 * 文件名：GpsToTrip.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-15
 * Copyright by menuz
 */
package com.tirsweb.datamining;

import java.util.ArrayList;

import com.tirsweb.dao.GpsToTripDAO;

/**
 * 
 * 此类描述的是：最原始的出租车GPS轨迹转换旅程。 
 *           invoke the procedure in oracle, 80 thread execute procedure, and the result no good than one thread
 * @author: dmnrei@gmail.com
 * @version: 2013-6-15 下午3:08:00
 */
public class GpsToTrip {
	public static void main(String[] args) {
		GpsToTripDAO dao = new GpsToTripDAO();
		ArrayList<String> vehicleList = dao.getAllVehicle();
		
		System.out.println("vehicle list size = " + vehicleList.size());
		
		int size = vehicleList.size();
		int threadNum = 100;
		int len = size /threadNum;
		
		int startIdx = 0;
		int endIdx = len;
		for(int i=1; i<=threadNum; i++) {
			GpsToTripThread thread = new GpsToTripThread(i, startIdx, endIdx, vehicleList);
			thread.start();
			startIdx = endIdx + 1;
			endIdx = endIdx + len;
			if(endIdx >= size -1 ) {
				endIdx = size - 1;
			}
		}
	}
}


