/**
 * 文件名：FindUpLocationUtil.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-4
 * Copyright by menuz
 */
package com.tirsweb.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import com.tirsweb.model.Arc;
import com.tirsweb.model.ArcDetail;
import com.tirsweb.model.ArcDis;
import com.tirsweb.model.Point;
import com.tirsweb.util.cache.Cache;
import com.tirsweb.util.gps.GeoDistance;
import com.tirsweb.util.sort.ComparatorArcDis;

/**
 * 
 * 此类描述的是：core api find up location util
 * 
 * @author: dmnrei@gmail.com
 * @version: 2013-6-4 下午7:13:37
 */
public class FindUpLocationUtil {
	Cache cache;
	
	public FindUpLocationUtil(Cache cache) {
		this.cache = cache;
	}
	
	public ArrayList<ArcDis> findNearestArc(double lati, double longi, ArrayList<Integer> arcList,
			Map<Integer, Arc> arcs) {
		int clostestArcId = -1;

		// gps处于测试数据以外的box
		if (arcList == null) {
			return null;
		}

		double minDis = Double.MAX_VALUE;
		int minIdx = -1;

		// iterate all arc
		
		ArrayList<Double> disList = new ArrayList<Double>();
		for (int i = 0; i < arcList.size(); i++) {
			int arcid = arcList.get(i);

			// get one arc's all info by arc id
			Arc arc = arcs.get(arcid);
			ArrayList<ArcDetail> adList = arc.getArcDetailList();

			// iterate gps of one arc
			double minDisBetPkAndCurrentArc = Double.MAX_VALUE;
			double minDisToArc = Double.MAX_VALUE;
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
				
				if(dis < minDisToArc) {
					minDisToArc = dis;
				}

				if (dis < minDisBetPkAndCurrentArc) {
					minDisBetPkAndCurrentArc = dis;
				}
			}

			if (minDisBetPkAndCurrentArc < minDis) {
				minDis = minDisBetPkAndCurrentArc;
				minIdx = i;
			}
			
			disList.add(minDisToArc);
		}
		
		
		ArrayList<ArcDis> lists = new ArrayList<ArcDis>();
		for(int i=0; i<disList.size(); i++) {
			ArcDis arcDis = new ArcDis(arcList.get(i), disList.get(i));
			lists.add(arcDis);
		}
		Collections.sort(lists, new ComparatorArcDis());

		// get arcid by min idx
		if (minIdx != -1) {
			clostestArcId = arcList.get(minIdx);
		}

		return lists;
	}
	
	public ArrayList<ArcDis> findNearestArc(String lati, String longi, ArrayList<Integer> arcList,
			Map<Integer, Arc> arcs) {
		return findNearestArc(Double.parseDouble(lati), Double.parseDouble(longi), arcList, arcs);
	}

}
