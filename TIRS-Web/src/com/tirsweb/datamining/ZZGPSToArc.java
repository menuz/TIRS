/**
 * 文件名：GPSToArc.java
 *
 * 版本信息： version 1.0
 * 日期：May 18, 2013
 * Copyright by menuz
 */
package com.tirsweb.datamining;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.tirsweb.model.Arc;
import com.tirsweb.model.ArcDetail;
import com.tirsweb.model.BaseArc;
import com.tirsweb.model.Box;
import com.tirsweb.model.ParkingLocation;
import com.tirsweb.model.Point;
import com.tirsweb.util.cache.JCache;
import com.tirsweb.util.gps.GeoDistance;

/**
 * 
 * 此类描述的是：map parking gps to arc
 *             211 arc, 10gps per arc, if parking gps is large, then 2110 * 10000 = 21,100,000
 *             calculate time is too many, so i gonna use boxid as a bridge
 * @author: dmnrei@gmail.com
 * @version: May 18, 2013 10:10:18 AM
 */
public class ZZGPSToArc {
	
	public static void main(String[] args) {
		JCache cache = new JCache();
		Map<Integer, Arc> arcs = cache.getArcs();
		
		// load arc to cache
		Set<Integer> keys = arcs.keySet();
		// System.out.println("key size = " + keys.size());
		for (Integer integer : keys) {
			BaseArc arc = arcs.get(integer);
		}
		
		// load arc box mapping
		Map<Integer, ArrayList<Integer>> boxAndArcmap = cache.getBoxAndArcMap();
		
		/*System.out.println(boxAndArcmap.size());
		Set<Integer> keySet = boxAndArcmap.keySet();
		
		for (Integer integer : keySet) {
			System.out.println(integer);
			ArrayList<Integer> values = boxAndArcmap.get(integer);
			for (Integer integer2 : values) {
				System.out.print(integer2 +  "   ");
			}
			System.out.println();
			System.out.println();
		}*/
		
		// load parking location
		ArrayList<ParkingLocation> plList = cache.getParkLocationList();
		
		File f = new File("tb_parking_location_update.sql");
				if(!f.exists()) {
					try {
						f.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				FileWriter write = null;
				BufferedWriter bufferedWriter = null;
				try {
					write = null;
					try {
						write = new FileWriter(f,false);
					} catch (IOException e) {
						e.printStackTrace();
					}
					bufferedWriter = new BufferedWriter(write);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
		
		// iterate all parking locations
		for (ParkingLocation pl : plList) {
			
			// get parking location to define box 
			int boxId = Box.getBoxId(pl.getLati(), pl.getLongi());
			
			if(boxId != -1) {
				// get arc list according to box id
				ArrayList<Integer> arcList = boxAndArcmap.get(boxId);
				int i = 0;
				
				if(arcList == null) {
					// System.out.println(pl.getId() + "   " + "-1");
					continue;
				}
				
				// 
				double minDis =  Double.MAX_VALUE;
				int minIdx = -1;
			
				// iterate all arc 
				for (i=0; i<arcList.size(); i++) {
					int arcid = arcList.get(i);
					
					// get one arc's all info by arc id
					Arc arc = arcs.get(arcid);
					ArrayList<ArcDetail> adList = arc.getArcDetailList();
					
					double totalDis = 0;
					int count = 0;
					
					// iterate gps of one arc
					for (ArcDetail arcDetail : adList) {
						double dis = 0.0;
						
						Point p = cache.marGpsToGps(arcDetail.getLati(), arcDetail.getLongi());
		         		if(p != null) {
		         			dis = GeoDistance.computeCompareDistance(pl.getLati(), pl.getLongi(), 
									p.getLat(), p.getLon());
		         		} else {
		         			dis = GeoDistance.computeCompareDistance(pl.getLati(), pl.getLongi(), 
									arcDetail.getLati(), arcDetail.getLongi());
		         		}
						
						totalDis += dis;
						count++;
					}
					double averageDis = totalDis / count;
					
					// record min dis to pl's gps, both dis and idx
					if(averageDis < minDis) {
						minDis = averageDis;
						minIdx = i;
					}
				}
				
				// get arcid by min idx
				int clostestArcId = -1;
				if(minIdx != -1){
					clostestArcId = arcList.get(minIdx);
				}
				
				try {
					bufferedWriter.write("update tb_parking_location set arc_id =" + clostestArcId + " where id = " + pl.getId() + ";"+"\n");
					bufferedWriter.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				System.out.println(pl.getId() + "   " + clostestArcId);
			} else {
				// System.out.println(pl.getId() + "   " + "-1");
			}
		}
	}
}


