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
 * 此类描述的是： invoke the procedure in oracle, 80 thread to 
 * @author: dmnrei@gmail.com
 * @version: 2013-6-15 下午3:08:00
 */
public class GpsToTripThread extends Thread{
	int threadId;
	int startIdx;
	int endIdx;
	ArrayList<String> vehicleList;
	
	public GpsToTripThread(int threadId, int startIdx, int endIdx,
			ArrayList<String> vehicleList) {
		this.threadId = threadId;
		this.startIdx = startIdx;
		this.endIdx = endIdx;
		
		this.vehicleList = vehicleList;
	}

	@Override
	public void run() {
		super.run();
		
		GpsToTripDAO dao = new GpsToTripDAO();
		int tripcount = 1;
		for(int i=startIdx; i<=endIdx; i++) {
			dao.gpsToTripByVehicleId(this.vehicleList.get(startIdx));
			
			tripcount += 1;
			System.out.println("thread id = " + this.threadId + " has deal = " + tripcount + " vehicle");
		}
		
		System.out.println("thread id = " + this.threadId + " done ");
	}
	
}


