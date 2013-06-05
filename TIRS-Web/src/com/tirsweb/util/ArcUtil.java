/**
 * 文件名：ArcUtil.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-5
 * Copyright by menuz
 */
package com.tirsweb.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tirsweb.model.Arc;
import com.tirsweb.model.ArcDetail;
import com.tirsweb.model.Box;
import com.tirsweb.model.Point;
import com.tirsweb.util.cache.Cache;
import com.tirsweb.util.gps.GeoDistance;

public class ArcUtil {
	public static ArrayList<Integer> transfer(ArrayList<Integer> arcList, Map<Integer, Integer> arcAndOppositeArcMap) {
		Set<Integer> newArcSet = new HashSet<Integer>();
		
		for (Integer integer : arcList) {
			Integer value = arcAndOppositeArcMap.get(integer);
			if(value != null) {
				if(integer>=value) {
					newArcSet.add(value);
				}else {
					newArcSet.add(integer);
				}
			} else {
				newArcSet.add(integer);
				System.err.println("arc map fail due to arc id " + integer);
			}
		}
		ArrayList<Integer> returnArcList = new ArrayList<Integer>();
		
		for (Integer integer : newArcSet) {
			returnArcList.add(integer);
		}
		
		return returnArcList;
	}
	
	public int GpsToArcId(Cache cache, double lati, double longi) {
		Map<Integer, Arc> arcs = cache.queryAllArc();
		Map<Integer, ArrayList<Integer>> boxAndArcmap = cache.queryBoxAndArcMap();
		
		// get parking location to define box
		int clostestArcId = -1;
		int boxId = Box.getBoxId(lati, longi);
		
		// make sure gps in the hangzhou area(30.15,120.00, 30.40, 120.40)
		if (boxId != -1) {
			// get arc list according to box id
			ArrayList<Integer> arcList = boxAndArcmap.get(boxId);
			ArrayList<Integer> newArcList = new ArrayList<Integer>();
			
			// gps处于测试数据以外的box
			if(arcList == null) {
				return -1;
			} 
			
			// don't consider the direction problem
			transferArc(cache, arcList, newArcList);
			System.out.println(arcList.size() + " -> " + newArcList.size());
			
			if (newArcList == null || newArcList.size() == 0) {
				return -1;
			}

			double minDis = Double.MAX_VALUE;
			int minIdx = -1;

			// iterate all arc
			for (int i = 0; i < newArcList.size(); i++) {
				int arcid = newArcList.get(i);

				// get one arc's all info by arc id
				Arc arc = arcs.get(arcid);
				ArrayList<ArcDetail> adList = arc.getArcDetailList();

				// iterate gps of one arc
				double minDisBetPkAndCurrentArc = Double.MAX_VALUE;
				for (ArcDetail arcDetail : adList) {
					double dis = 0.0;

					Point p = cache.marGpsToGps(arcDetail.getLati(),
							arcDetail.getLongi());
					if (p != null) {
						dis = GeoDistance.computeCompareDistance(lati, longi,
								p.getLat(), p.getLon());
					} else {
						System.err.println("p == null");
					}

					if(dis < minDisBetPkAndCurrentArc) {
						minDisBetPkAndCurrentArc = dis;
					}
					
					// System.out.print(dis  + "  " );
				}
				
				if(minDisBetPkAndCurrentArc < minDis) {
					minDis = minDisBetPkAndCurrentArc;
					minIdx =  i;
				}
			}

			// get arcid by min idx
			if (minIdx != -1) {
				clostestArcId = newArcList.get(minIdx);
			}
		}
		
		return clostestArcId;
	}
	
	
	
	public void transferArc(Cache cache, ArrayList<Integer> arcList, ArrayList<Integer> newArcList) {
		Map<Integer, Integer> arcAndOppositeArcMap = cache.queryArcAndOppositeMap();
		
		Set<Integer> newArcSet = new HashSet<Integer>();
		for (Integer integer : arcList) {
			Integer value = arcAndOppositeArcMap.get(integer);
			if(value != null) {
				if(integer>=value) {
					newArcSet.add(value);
				}else {
					newArcSet.add(integer);
				}
			} else {
				newArcSet.add(integer);
				System.err.println("arc map fail due to arc id " + integer);
			}
		}
		for (Integer integer : newArcSet) {
			newArcList.add(integer);
		}
	}
}


