/**
 * 文件名：GPSToArc.java
 *
 * 版本信息： version 1.0
 * 日期：May 18, 2013
 * Copyright by menuz
 */
package com.tirsweb.datamining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tirsweb.dao.jdbc.DAO4;
import com.tirsweb.model.Arc;
import com.tirsweb.model.ArcDetail;
import com.tirsweb.model.Box;
import com.tirsweb.model.ParkingLocation;
import com.tirsweb.model.Point;
import com.tirsweb.util.cache.JCache;
import com.tirsweb.util.gps.GeoDistance;

/**
 * 
 * 此类描述的是：map parking gps to arc 211 arc, 10gps per arc, if parking gps is
 * large, then 2110 * 10000 = 21,100,000 calculate time is too many, so i gonna
 * use boxid as a bridge
 * 
 * @author: dmnrei@gmail.com
 * @version: May 18, 2013 10:10:18 AM
 */
public class GPSToArc {
	static JCache cache;
	static Map<Integer, Arc> arcs;
	static Map<Integer, ArrayList<Integer>> boxAndArcmap;
	// arc to arc, transfer big arcid to small arcid
	static Map<Integer, Integer> arcAndOppositeArcMap;

	public static void main(String[] args) {
		cache = new JCache();
		// load arc to cache
		arcs = cache.getArcs();
		// load arc box mapping
		boxAndArcmap = cache.getBoxAndArcMap();
		// load parking location
		ArrayList<ParkingLocation> plList = cache.getParkLocationList();
		// transfer arc to min arc
		DAO4 dao = new DAO4();
		arcAndOppositeArcMap = new HashMap<Integer, Integer>();
		dao.getArcMap(arcAndOppositeArcMap);
		
		// iterate all parking locations
		for (ParkingLocation pl : plList) {
			int arcid = GpsToArcId(pl.getLati(), pl.getLongi());
		}
	}

	/**
	 * 
		 * 此方法描述的是：
	     * @param lati
	     * @param longi
	     * @return
	     * @version: May 27, 2013 11:11:21 AM
	 */
	public static int GpsToArcId(double lati, double longi) {
		// get parking location to define box
		int clostestArcId = -1;
		int boxId = Box.getBoxId(lati, longi);

		if (boxId != -1) {
			// get arc list according to box id
			ArrayList<Integer> arcList = boxAndArcmap.get(boxId);
			ArrayList<Integer> newArcList = new ArrayList<Integer>();
			
			transferArc(arcList, newArcList);
			
			int i = 0;

			if (newArcList == null) {
				return -1;
			}

			//
			double minDis = Double.MAX_VALUE;
			int minIdx = -1;

			// iterate all arc
			for (i = 0; i < newArcList.size(); i++) {
				int arcid = newArcList.get(i);

				// get one arc's all info by arc id
				Arc arc = arcs.get(arcid);
				ArrayList<ArcDetail> adList = arc.getArcDetailList();

				double totalDis = 0;
				int count = 0;

				// iterate gps of one arc
				for (ArcDetail arcDetail : adList) {
					double dis = 0.0;

					Point p = cache.marGpsToGps(arcDetail.getLati(),
							arcDetail.getLongi());
					if (p != null) {
						dis = GeoDistance.computeCompareDistance(lati, longi,
								p.getLat(), p.getLon());
					} else {
						dis = GeoDistance.computeCompareDistance(lati, longi,
								arcDetail.getLati(), arcDetail.getLongi());
					}

					totalDis += dis;
					count++;
				}
				double averageDis = totalDis / count;

				// record min dis to pl's gps, both dis and idx
				if (averageDis < minDis) {
					minDis = averageDis;
					minIdx = i;
				}
			}

			// get arcid by min idx
			if (minIdx != -1) {
				clostestArcId = newArcList.get(minIdx);
			}

			System.out.println("   " + clostestArcId);
		} else {
			
		}
		return clostestArcId;
	}
	
	public static void transferArc(ArrayList<Integer> arcList, ArrayList<Integer> newArcList) {
		Set<Integer> newArcSet = new HashSet<Integer>();
		for (Integer integer : arcList) {
			int value = arcAndOppositeArcMap.get(integer);
			if(integer>=value) {
				newArcSet.add(value);
			}else {
				newArcSet.add(integer);
			}
		}
		for (Integer integer : newArcSet) {
			newArcList.add(integer);
		}
	}
}
