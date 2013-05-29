/**
 * 文件名：FindParkingCandidate.java
 *
 * 版本信息： version 1.0
 * 日期：May 15, 2013
 * Copyright by menuz
 */
package com.tirsweb.datamining;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.tirsweb.dao.jdbc.ArcToBoxDAO;
import com.tirsweb.model.ParkingLocation;
import com.tirsweb.model.Point;
import com.tirsweb.model.Trip;
import com.tirsweb.util.gps.GeoDistance;

public class FindParkingCandidate {
	public static int disThreshold = 2;
	public static int timeThreshold = 200;
	
	public static int timeInterval(Point p1, Point p2) {
		Time time1 = p1.getTime();
		Time time2 = p2.getTime();

		return 50;
	}
	
	public static int timeInterval(int idx1, int idx2) {
		return Math.abs(20 * (idx2 - idx1));
	}
	
	public static void main(String[] args) {
		ArcToBoxDAO dao = new ArcToBoxDAO();
		String rownum = "99999999";
		ArrayList<Trip> trips = dao.getFreeTrip(rownum);
		
System.out.println("Distance Threshold = " + disThreshold + "  TimeThreshold = " + timeThreshold);
		
		FindingParking fp = new FindingParking();
		for (Trip trip : trips) {
			fp.dosomething(trip);
			ArrayList<ParkingLocation> points = fp.getParkingPoints();
			System.out.println("size = " + points.size());
			dao.insertParkingLocation(points);
		}
	}
	
	/*public static Map<Integer, Integer> getCandidate(ArrayList<Point> points, Set<Integer> collections) {
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
	}*/
}


