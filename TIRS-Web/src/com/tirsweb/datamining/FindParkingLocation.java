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
public class FindParkingLocation {
	public int disThreshold = 2;
	public int timeThreshold = 200;
	ArrayList<ParkingLocation> returnPointList;

	/*public int timeInterval(Point p1, Point p2) {
		Time time1 = p1.getTime();
		Time time2 = p2.getTime();
		return 50;
	}*/

	public int timeInterval(int idx1, int idx2) {
		return Math.abs(20 * (idx2 - idx1));
	}
	
	/**
	 * 
		 * 此方法描述的是：main function
	     * @param trip
	     * @author: dmnrei@gmail.com
	     * @version: 2013-6-3 上午8:35:35
	 */
	public void dosomething(Trip trip) {
		// return all gpses by time order asc
		String note = trip.getNote();
		String[] gpses = note.split(",");
		
		// parse string to Point List
		int i=0;
		ArrayList<Point> points = new ArrayList<Point>();
		for(i=0; i<gpses.length; i++) {
			String gps = gpses[i];
			int idx = gps.indexOf('+');
			String latitude = gps.substring(0, idx);
			String longitude = gps.substring(idx + 1);
			Point p = new Point(latitude, longitude);
			points.add(p);
		}
		
		Set<Integer> collections = new TreeSet<Integer>();
		Map<Integer, Integer> clusters = getCandidate(points, collections);
		
		Set<Integer> keyList = clusters.keySet();
		returnPointList = new ArrayList<ParkingLocation>();
		
		for (Integer integer : keyList) {
			Point p = points.get(integer);
			ParkingLocation pl = new ParkingLocation(trip.getTripId(), trip.getVehicleId(), p.getLat(), p.getLon(), -1);
			returnPointList.add(pl);
		}
	}
	
	/**
	 * 
		 * 此方法描述的是：寻找潜在停靠点的具体算法实现
	     * @param points
	     * @param collections
	     * @return
	     * @author: dmnrei@gmail.com
	     * @version: 2013-6-3 上午8:38:23
	 */
	public Map<Integer, Integer> getCandidate(ArrayList<Point> points, Set<Integer> collections) {
		int i=0, j=0;
		int m = points.size();
		
		for( i=0; i<m-1; i++) {
			double dis = 0.0;
			boolean inrangeFlag = false;
			
			for( j=i+1; j<m; j++) {
				dis = GeoDistance.computeCompareDistance(points.get(i), points.get(j));
				if(dis < disThreshold) {
					j++;
					inrangeFlag = true;
				} else {
					break;
				}
			}
			
			if(inrangeFlag && timeInterval(j-1, i) >= timeThreshold) {
				for(int k=i; k<j; k++) {
					collections.add(k);
				}
			}
			
			i++;
		}
		
		Map<Integer, Integer> clusters = new HashMap<Integer, Integer>();
		
		Iterator<Integer> itr=collections.iterator();
		int clusterNum = 0;
		int curIdx = -1;
		int baseIdx = -1;
		while(itr.hasNext()){
		    Integer idx = itr.next();
		    // System.out.println("idx: " + idx);
		    if(curIdx == -1) {
		    	curIdx = idx;
		    	baseIdx = idx;
		    	clusterNum ++;
		    	continue;
		    }
		    
		    if(curIdx != idx - 1) {
		    	int count = curIdx-baseIdx+1;
		    	clusters.put(baseIdx, count);
		    	
		    	baseIdx = idx;
		    	clusterNum ++;
		    } 
		    
	    	curIdx = idx;
		}
		
		if(baseIdx != -1 && curIdx != -1) {
			int count = curIdx-baseIdx+1;
			clusters.put(baseIdx, count);
		}
		
		for (Integer key : clusters.keySet()) {
			int value = clusters.get(key);
		}
		
		return clusters;
	}
	
	/**
	 * 
		 * 此方法描述的是：返回结果
	     * @return
	     * @author: dmnrei@gmail.com
	     * @version: 2013-6-3 上午8:39:34
	 */
	public ArrayList<ParkingLocation> getParkingPoints() {
		return this.returnPointList;
	}
	
	public static void main(String[] args) {
		long begin = new Date().getTime();
		
		ArrayList<Integer> idxs = new ArrayList<Integer>();
		for(int i=1; i <= 826218; i+=10000) {
			idxs.add(i);
		}
		idxs.add(826218);
		
		int threadId = 1;
		for(int i=0; i<idxs.size()-1; i++) {
			if(i==0) {
				FindParkingLocationThread thread = new FindParkingLocationThread(threadId, idxs.get(i), idxs.get(i+1));
				thread.start();
			} else {
				FindParkingLocationThread thread = new FindParkingLocationThread(threadId, idxs.get(i)+1, idxs.get(i+1));
				thread.start();
			}
			threadId ++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		long end = new Date().getTime();
		int seconds = (int)((end - begin) / 1000);
		
		System.out.println("cost time = " + seconds);
	}
}
