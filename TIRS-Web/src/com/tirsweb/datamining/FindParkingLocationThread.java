/**
 * 文件名：FindingParking.java
 *
 * 版本信息： version 1.0
 * 日期：May 17, 2013
 * Copyright by menuz
 */
package com.tirsweb.datamining;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.tirsweb.dao.ArcToBoxDAO;
import com.tirsweb.model.ParkingLocation;
import com.tirsweb.model.Point;
import com.tirsweb.model.Trip;
import com.tirsweb.util.gps.GeoDistance;

/**
 * 
 * 此类描述的是：对80+万条trip数据进行处理，找出每次trip中的潜在停靠点
 * @author: dmnrei@gmail.com
 * @version: 2013-6-3 上午8:30:32
 */

/**
 * 
 * 此类描述的是：
 * @author: dmnrei@gmail.com
 * @version: 2013-6-11 下午5:58:33
 */


/**
 * 
 * 此类描述的是： 每个线程处理一万个，每次1000个
 * @author: dmnrei@gmail.com
 * @version: 2013-6-11 下午5:59:12
 */
public class FindParkingLocationThread extends Thread{
	ArcToBoxDAO dao;
	int threadId;
	int startIdx;
	int endIdx;
	
	public FindParkingLocationThread(int threadId, int startIdx, int endIdx) {
		this.threadId = threadId;
		this.startIdx = startIdx;
		this.endIdx = endIdx;
	}

	@Override
	public void run() {
		super.run();
		
		FindParkingLocation fp = new FindParkingLocation();
		dao = new ArcToBoxDAO();
		
		int count = 0;
		for(int i=startIdx; i<endIdx; i=i+1000) {
			int j=i+1000;
			if(j>=endIdx) {
				j=endIdx;
			}
			ArrayList<Trip> trips = dao.getFreeTrip(i, j);
			
			ArrayList<ParkingLocation> pointsBuffer = new ArrayList<ParkingLocation>();
			int tripcount = 0;
			for (Trip trip : trips) {
				tripcount++;
				
				fp.dosomething(trip);
				ArrayList<ParkingLocation> points = fp.getParkingPoints();
				
				for(ParkingLocation pk : points) {
					pointsBuffer.add(pk);
				}
				
				if(tripcount % 100 == 0) {
					dao.insertParkingLocation(pointsBuffer);
					pointsBuffer = new ArrayList<ParkingLocation>();
				}
				
			}
			count += 1000;
			System.out.println("threadId = " + threadId + " deal count = " + count);
		}
	}
}
