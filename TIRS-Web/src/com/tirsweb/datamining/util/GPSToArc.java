/**
 * 文件名：GPSToArc.java
 *
 * 版本信息： version 1.0
 * 日期：May 18, 2013
 * Copyright by menuz
 */
package com.tirsweb.datamining.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tirsweb.dao.JCacheDAO;
import com.tirsweb.model.Arc;
import com.tirsweb.model.ArcDetail;
import com.tirsweb.model.Box;
import com.tirsweb.model.Node;
import com.tirsweb.model.ParkingLocation;
import com.tirsweb.model.Point;
import com.tirsweb.util.FileHelper;
import com.tirsweb.util.cache.JCache;
import com.tirsweb.util.gps.Angle;
import com.tirsweb.util.gps.GeoDistance;

/**
 * 
 * 此类描述的是：map parking gps to arc 211 arc, 10gps per arc, if parking gps is
 * large, then 2110 * 50000 = 1,000,000,000 calculate time is too many, so i gonna
 * use boxid as a bridge.
 * gps -> boxid -> all arcs in box -> compute which arc the gps belongs to
 * Parking location mapping to Arc
 * @author: dmnrei@gmail.com
 * @version: May 18, 2013 10:10:18 AM
 */
public class GPSToArc {
	static JCache cache;
	static Map<Integer, Arc> arcs;
	static Map<Integer, ArrayList<Integer>> boxAndArcmap;
	// arc to arc, transfer big arcid to small arcid
	static Map<Integer, Integer> arcAndOppositeArcMap;
	
	static Map<Integer, Node> nodes;
	
	// miniestDis = 3000 14401
	// miniestDis = 300  10840
	// miniestDis = 150  7646
	static int MiniestDis = 150;
	// miniestDis = 150 angle = 15  4982*2
	// miniestDis = 150 angle = 10 3457*2s
	// miniestDis = 150 angle = 5 2278*2s
	static int MinAngle = 5;

	public static void main(String[] args) {
		cache = new JCache();
		nodes = cache.getAllNode();
		
		// load arc to cache
		arcs = cache.getArcs();
		
		/*int count = 0;
		for(int i=0; i<arcs.size(); i++) {
			Arc arc = arcs.get(i+1);
			if(arc != null) {
				System.out.println(arc.getId() +  "   "  + arc.getArcDetailList().size());
				count++;
			}
		}
		
		System.out.println("count = " + count);*/
		
		// load arc box mapping
		boxAndArcmap = cache.getBoxAndArcMap();
		/*Set<Integer> sets = boxAndArcmap.keySet();
		ArrayList<Integer> lists = new ArrayList<Integer>();
		lists.addAll(sets);
		Collections.sort(lists);
		
		int boxCount = 0;
		int ArcCount = 0;
		for (int i=0; i<lists.size(); i++) {
			int key = lists.get(i);
			System.out.println("[boxid:" + lists.get(i) + "]");
			
			ArrayList<Integer> ls = boxAndArcmap.get(key);
			for(int j=0; j<ls.size(); j++) {
				System.out.print("[arcid:"+ls.get(j)+"],");
			}
			System.out.println();
			
			boxCount++;
			ArcCount+=ls.size();
		}
		
		System.out.println("boxCount = " + boxCount);
		System.out.println("ArcCount = " + ArcCount);*/
		
		// load parking location
		ArrayList<ParkingLocation> plList = cache.getParkLocationList();
		/*for (ParkingLocation parkingLocation : plList) {
			double lati = parkingLocation.getLati();
			double longi = parkingLocation.getLongi();
			int id = parkingLocation.getId();
			System.out.println(lati + "  " + longi + "  " + id );
		}*/
		
//		// transfer arc to min arc
		JCacheDAO dao = new JCacheDAO();
		arcAndOppositeArcMap = new HashMap<Integer, Integer>();
		dao.getArcMap(arcAndOppositeArcMap);
		
		
//		// iterate all parking locations
		ArrayList<String> sqlList = new ArrayList<String>();
		ArrayList<String> logList = new ArrayList<String>();
		
		Map<Integer, Integer> maps = new HashMap<Integer, Integer>();
		maps.put(0, 0);
		
		int count = 0;
		int count1 = 0;
		int count2 = 0;
		for (ParkingLocation pl : plList) {
			int arcid = GpsToArcId(pl.getLati(), pl.getLongi(), maps);
			
			if(arcid != -1) count++;
			
			// compare the angle between gps and arc direction 
			// if angle > 170 or angle < 10  admit it to be on road
			// parking location has no direction, so generate two records
			if(arcid != -1) {
				Arc arc = arcs.get(arcid);
				Angle angle = new Angle();
				
				Node startNode = nodes.get(arc.getStart_node_id());
				Node endNode = nodes.get(arc.getEnd_node_id());
				
				double angle1 = angle.angle(startNode.getLati(), startNode.getLongi(), endNode.getLati(), endNode.getLongi(), 
						startNode.getLati(), startNode.getLongi(), pl.getLati(), pl.getLongi());
				
				String log = startNode.getLati() + " " + startNode.getLongi() + " " + endNode.getLati() + " " +  endNode.getLongi() + " " + 
						startNode.getLati() + " " + startNode.getLongi() + " " +  pl.getLati() + " " + pl.getLongi();
				logList.add(log);

				double angle2 = angle.angle(endNode.getLati(), endNode.getLongi(), startNode.getLati(), startNode.getLongi(), 
						endNode.getLati(), endNode.getLongi(), pl.getLati(), pl.getLongi());

				String log2 = endNode.getLati() + " " + endNode.getLongi() + " " + startNode.getLati() + " " +  startNode.getLongi() + " " + 
						endNode.getLati() + " " + endNode.getLongi() + " " +  pl.getLati() + " " + pl.getLongi();
				logList.add(log2);
				
				if(angle1 > MinAngle || angle2 > MinAngle) {
					arcid = -1;
				}
				
				logList.add("angle1:" + angle1);
				logList.add("angle2:" + angle2);
				logList.add("arcid:" + arcid);
				System.out.println("angle1:" + angle1);
				System.out.println("angle2:" + angle2);
				System.out.println("arcid:" + arcid);
			}
			
			System.out.println("arcid = " + arcid);
			if(arcid != -1) {
				String sql = "update tb_parking_location set arc_id = " + arcid + " where id = " + pl.getId() + " ;";
				Integer oppositeArcId = arcAndOppositeArcMap.get(arcid);
				if(oppositeArcId == null) {
					oppositeArcId = arcid;
				}
				int pkId = pl.getId() + 100000;
				String sql3 = "insert into tb_parking_location(trip_id, vehicle_id, lati, longi, arc_id, id) values(" + 22222 + ", " + pl.getVehicleId() + ", " + pl.getLati() + "," + pl.getLongi() + ", " + oppositeArcId + ", " + pkId + ");";
				// String sql2 = "update tb_parking_location set arc_id = " + oppositeArcId + " where id = " + pl.getId() + " ;";
				
				logList.add("sql:" + sql);
				// logList.add("sql2:" + sql2);
				logList.add("sql3:" + sql3);

				sqlList.add(sql);
				sqlList.add(sql3);
				
				count1++;
			}
		}
		FileHelper log = new FileHelper("log.txt", "append", logList);
		log.write();
		
		FileHelper fileHelper = new FileHelper("tb_pk_0530.sql", "append", sqlList);
		fileHelper.write();
		
		// 14400
		System.out.println("count = " + count);
		System.out.println("after angle filter count1 = " + count1);
		System.out.println("gps in 道路网络的数目:" + maps.get(0));
		System.out.println("point1:" + maps.get(1));
		System.out.println("point2:" + maps.get(2));
		System.out.println("point3:" + maps.get(3));
	}

	/**
	 * 
		 * 此方法描述的是：
	     * @param lati
	     * @param longi
	     * @return
	     * @version: May 27, 2013 11:11:21 AM
	 */
	public static int GpsToArcId(double lati, double longi, Map<Integer, Integer> maps) {
		// get parking location to define box
		int clostestArcId = -1;
		int boxId = Box.getBoxId(lati, longi);
		
		//point 1
		Integer times = maps.get(1);
		if(times == null) {
			maps.put(1, 1);
		} else {
			maps.put(1, ++times);
		}
		
		// make sure gps in the hangzhou area(30.15,120.00, 30.40, 120.40)
		if (boxId != -1) {
			// get arc list according to box id
			ArrayList<Integer> arcList = boxAndArcmap.get(boxId);
			ArrayList<Integer> newArcList = new ArrayList<Integer>();
			
			// gps处于测试数据以外的box
			if(arcList == null) {
				return -1;
			} 
			// gps处于测试数据之中
			else {
				int count = maps.get(0);
				maps.put(0, ++count);
			}
			
			// point 2
			times = maps.get(2);
			if(times == null) {
				maps.put(2, 1);
			} else {
				maps.put(2, ++times);
			}
			
			// don't consider the direction problem
			transferArc(arcList, newArcList);
			System.out.println(arcList.size() + " -> " + newArcList.size());
			
			if (newArcList == null || newArcList.size() == 0) {
				return -1;
			}
			
			// point 3
			
			times = maps.get(3);
			if(times == null) {
				maps.put(3, 1);
			} else {
				maps.put(3, ++times);
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
			
			// System.out.println();
			// System.out.println("mindis = " + minDis);
			// System.out.println("minIdx = " + minIdx);

			// get arcid by min idx
			if (minIdx != -1 && minDis <= MiniestDis) {
				clostestArcId = newArcList.get(minIdx);
			}
		}
		
		return clostestArcId;
	}
	
	/**
	 * 
		 * 此方法描述的是：invoke by TripToSegmentList.java
	     * @param lati
	     * @param longi
	     * @param boxAndArcmap
	     * @param type  used for distinguish GpsToArcId
	     * @return
	     * @author: dmnrei@gmail.com
	     * @version: 2013-6-2 下午9:07:33
	 */
	public static int GpsToArcId(double lati, double longi, Map<Integer, ArrayList<Integer>> boxAndArcmap, int type) {
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
			transferArc(arcList, newArcList);
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
			if (minIdx != -1 && minDis <= MiniestDis) {
				clostestArcId = newArcList.get(minIdx);
			}
		}
		
		return clostestArcId;
	}
	
	
	
	public static void transferArc(ArrayList<Integer> arcList, ArrayList<Integer> newArcList) {
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
