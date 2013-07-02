/**
 * 文件名：TripToSegmentList.java
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

import com.tirsweb.dao.JCacheDAO;
import com.tirsweb.datamining.util.SpeedFilter;
import com.tirsweb.model.Arc;
import com.tirsweb.model.ArcDetail;
import com.tirsweb.model.Box;
import com.tirsweb.model.Node;
import com.tirsweb.model.Point;
import com.tirsweb.model.TbArcSpeed;
import com.tirsweb.model.Trip;
import com.tirsweb.util.cache.JCache;
import com.tirsweb.util.gps.Angle;
import com.tirsweb.util.gps.GeoDistance;

/**
 * 
 * 此类描述的是：
 * 
 * @author: dmnrei@gmail.com
 * @version: 2013-6-2 下午7:45:28
 */
public class TripToSegmentList {
	
	int MiniestDis = 150;
	int MinAngle = 5;

	public static void main(String[] args) {
		JCache cache;
		Map<Integer, Arc> arcs;
		Map<Integer, ArrayList<Integer>> boxAndArcmap;
		// arc to arc, transfer big arcid to small arcid
		Map<Integer, Integer> arcAndOppositeArcMap;
		Map<Integer, Node> nodes;

		cache = new JCache();
		arcs = cache.getArcs();
		boxAndArcmap = cache.getBoxAndArcMap();
		nodes = cache.getAllNode();

		// transfer arc to min arc
		JCacheDAO dao = new JCacheDAO();
		//
		arcAndOppositeArcMap = new HashMap<Integer, Integer>();
		dao.getArcMap(arcAndOppositeArcMap);

		ArrayList<Integer> idxs = new ArrayList<Integer>();
		for (int i = 1; i <= 826218; i += 20000) {
			idxs.add(i);
		}
		idxs.add(826218);
		/*
		 * idxs.add(1); idxs.add(1000);
		 */

		int count = 0;
		
		
		System.out.println("hello");
		
		int threadId = 1;
		for(int i=0; i<idxs.size()-1; i++) {
			if(i==0) {
				TripToSegmentThread thread = new TripToSegmentThread(threadId, idxs.get(i), idxs.get(i+1), arcs, arcAndOppositeArcMap, nodes, boxAndArcmap, cache);
				thread.start();
			} else {
				TripToSegmentThread thread = new TripToSegmentThread(threadId, idxs.get(i)+1, idxs.get(i+1), arcs, arcAndOppositeArcMap, nodes, boxAndArcmap, cache);
				thread.start();
			}
			threadId ++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 
	 * 此方法描述的是：handle one trip
	 * 
	 * @param trip
	 * @version: May 27, 2013 3:32:47 PM
	 */
	public void handleTrip(Trip trip, Map<Integer, Arc> arcs, Map<Integer, Integer> arcAndOppositeArcMap,
			Map<Integer, Node> nodes, Map<Integer, ArrayList<Integer>> boxAndArcmap, JCache cache, ArrayList<TbArcSpeed> arcSpeedContainer) {
		ArrayList<Point> wayPoints = trip.getWayPoints();
		ArrayList<Integer> arcList = new ArrayList<Integer>();
		// ArrayList<String> sqlList = new ArrayList<String>();
		
		ArrayList<TbArcSpeed> arcSpeedList = new ArrayList<TbArcSpeed>();
		

		// step1: transfer gps to arc id list
		// g1->g1->g2->g2->g3->g3->g4->g4
		// transfer to
		// 1->1->2->2->3->3->4->4
		// System.out.println("Arc list: ");
		int arcid = -2;
		StringBuffer sb = new StringBuffer(1024);
		for (Point point : wayPoints) {

			// int arcidTmp = GpsToArcId2(point.getLat(), point.getLon());
			// arcidTmp return the mini arcid, the detail is handled by GPSToArc
			int arcidTmp = GpsToArcId(point.getLat(), point.getLon(), boxAndArcmap, arcs, cache, arcAndOppositeArcMap);

			arcList.add(arcidTmp);
			if (arcid != arcidTmp) {
				arcid = arcidTmp;
				if (arcidTmp != -1) {
					sb.append(arcid + "  "
							+ arcs.get(arcidTmp).getStart_node_id() + "->"
							+ arcs.get(arcidTmp).getEnd_node_id() + "\n");
				}
			}
			// System.out.print(arcid + "->");
		}
		// System.out.println();
		/*
		 * System.out.println("Split: ArcId  StartNodeId->EndNodeId");
		 * System.out.println(sb.toString()); System.out.println();
		 */

		// 111122223333 get 1111 2222 3333 split
		// System.out.println("ArcId  StartIndex->EndIndex");
		// System.out.println("ArcId+AverageSpeed");
		int arcId = -2;
		double totalDis = 0.0;
		int count = 0;
		int startIdx = 0;

		SpeedFilter speedFilter = new SpeedFilter();
		sb = new StringBuffer(1024);
		sb.append("[arcid,speed]\n");
		for (int i = 0; i < arcList.size(); i++) {
			int arcIdTmp = arcList.get(i);
			if (arcIdTmp != arcId) {
				if (arcId != -2 && arcId != -1) {
					// System.out.println(arcId + "   " + startIdx + "  " +
					// (i-1));
					String arcAndAverageSpeed = getAverageSpeed(arcId,
							startIdx, i - 1, arcList, wayPoints, arcs, arcAndOppositeArcMap, nodes);
					String[] temp = arcAndAverageSpeed.split("[+]");
					// System.out.println(arcAndAverageSpeed);

					if (!temp[0].equals(arcId + "")) {
						// change++;
					}

					if (speedFilter.filter(temp[1]) != -1) {
						String sql = generateSql(temp[0], trip.getWeekday(),
								trip.getHour(), temp[1]);
						// sqlList.add(sql);
						
						TbArcSpeed arcSpeed = new TbArcSpeed(Integer.parseInt(temp[0]), trip.getWeekday(),
								trip.getHour(), Double.parseDouble(temp[1]));
						arcSpeedList.add(arcSpeed);
					}

					sb.append("[arcid:" + temp[0] + ",speed:" + temp[1] + "]->");
				}
				startIdx = i;
				arcId = arcIdTmp;
			}
		}

		if (arcId != -2 && arcId != -1) {
			// System.out.println(startIdx + "  " + (arcList.size()-1) + "  " +
			// arcId);
			String arcAndAverageSpeed = getAverageSpeed(arcId, startIdx,
					(arcList.size() - 1), arcList, wayPoints, arcs, arcAndOppositeArcMap, nodes);
			String[] temp = arcAndAverageSpeed.split("[+]");
			// System.out.println(arcAndAverageSpeed);
			if (!temp[0].equals(arcId + "")) {
				// change++;
			}

			if (speedFilter.filter(temp[1]) != -1) {
				String sql = generateSql(temp[0], trip.getWeekday(),
						trip.getHour(), temp[1]);
				// sqlList.add(sql);
				
				TbArcSpeed arcSpeed = new TbArcSpeed(Integer.parseInt(temp[0]), trip.getWeekday(),
						trip.getHour(), Double.parseDouble(temp[1]));
				arcSpeedList.add(arcSpeed);
			}

			sb.append("[arcid:" + temp[0] + ",speed:" + temp[1] + "];");
		}
		// System.out.println(sb.toString());

		/*FileHelper fileHelper = new FileHelper("tb_arc_speed_insert_0530.sql",
				"append", sqlList);
		fileHelper.write();
		fileHelper.close();*/
		
		for(TbArcSpeed arcSpeed : arcSpeedList) {
			arcSpeedContainer.add(arcSpeed);
		}
		
		/*for(String sql : sqlList) {
			sqlContainer.add(sql);
		}*/
	}
	
	
	public String getInsertToDB() {
		return  "";
	}

	// for example, use 1111 split to compute the average speed of arc
	// meanwhile judge the driving direction with angle. the angle is between
	// dirving direction and arc direction.
	/**
	 * 
	 * 此方法描述的是：
	 * 
	 * @param arcId
	 * @param startIndex
	 * @param endIndex
	 * @param arcList
	 * @param wayPoints
	 * @return return real driving direction and speed
	 * @author: dmnrei@gmail.com
	 * @version: 2013-6-2 下午8:35:41
	 */
	public String getAverageSpeed(int arcId, int startIndex, int endIndex,
			ArrayList<Integer> arcList, ArrayList<Point> wayPoints,
			Map<Integer, Arc> arcs, Map<Integer, Integer> arcAndOppositeArcMap,
			Map<Integer, Node> nodes) {
		Angle angle = new Angle();

		int arcCount = 0;
		int oppositeArcCount = 0;
		double totalDis = 0.0;
		int count = 0;
		for (int i = startIndex; i <= endIndex - 1; i++) {
			Point p1 = wayPoints.get(i);
			Point p2 = wayPoints.get(i + 1);

			if (i - 1 >= 0) {
				totalDis += GeoDistance
						.computeCompareDistance(p1.getLat(), p1.getLon(),
								p2.getLat(), wayPoints.get(i - 1).getLon());
			}
			count++;

			Arc arc = arcs.get(arcList.get(i));
			Integer oppositeArcId = arcAndOppositeArcMap.get(arcList.get(i));
			Arc oppositeArc = null;
			if (oppositeArcId == null) {
				oppositeArc = arcs.get(arcList.get(i));
			} else {
				oppositeArc = arcs.get(oppositeArcId);
			}

			Node start = nodes.get(arc.getStart_node_id());
			Node end = nodes.get(arc.getEnd_node_id());
			double _angle = angle.angle(p1.getLat(), p1.getLon(), p2.getLat(),
					p2.getLon(), start.getLati(), start.getLongi(),
					end.getLati(), end.getLongi());

			start = nodes.get(oppositeArc.getStart_node_id());
			end = nodes.get(oppositeArc.getEnd_node_id());
			double _oppositeAngle = angle.angle(p1.getLat(), p1.getLon(),
					p2.getLat(), p2.getLon(), start.getLati(),
					start.getLongi(), end.getLati(), end.getLongi());

			if (_angle <= _oppositeAngle) {
				arcCount++;
			} else {
				oppositeArcCount++;
			}
		}

		String returnStr = "";
		double averageSpeed = -1;
		if (count != 0) {
			averageSpeed = totalDis / (20 * count);
		}

		int returnArcId = arcId;
		if (arcCount >= oppositeArcCount) {
			returnArcId = arcId;
		} else {
			Integer oppositeArc = arcAndOppositeArcMap.get(arcId);
			if (oppositeArc == null) {
				returnArcId = arcId;
			} else {
				returnArcId = oppositeArc;
			}
		}
		returnStr = returnArcId + "+" + averageSpeed;
		return returnStr;
	}

	public String generateSql(int arcid, int weekday, int hour,
			double average) {
		String sql = "insert into tb_arc_speed(arc_id, weekday, hourofday, speed) values ("
				+ arcid + ", " + weekday + ", " + hour + ", " + average + ");";
		return sql;
	}

	public String generateSql(String arcid, int weekday, int hour,
			String average) {
		String sql = "insert into tb_arc_speed(arc_id, weekday, hourofday, speed) values ("
				+ arcid + ", " + weekday + ", " + hour + ", " + average + ");";
		return sql;
	}

	/*
	 * public static void transferArc(ArrayList<Integer> arcList,
	 * ArrayList<Integer> newArcList) { Set<Integer> newArcSet = new
	 * HashSet<Integer>(); for (Integer integer : arcList) { Integer value =
	 * arcAndOppositeArcMap.get(integer); if(value == null) { continue; }
	 * 
	 * if(integer>=value) { newArcSet.add(value); }else {
	 * newArcSet.add(integer); } } for (Integer integer : newArcSet) {
	 * newArcList.add(integer); } }
	 */

	/**
	 * 
	 * 此方法描述的是：invoke by TripToSegmentList.java
	 * 
	 * @param lati
	 * @param longi
	 * @param boxAndArcmap
	 * @param type
	 *            used for distinguish GpsToArcId
	 * @return
	 * @author: dmnrei@gmail.com
	 * @version: 2013-6-2 下午9:07:33
	 */
	public int GpsToArcId(double lati, double longi, Map<Integer, ArrayList<Integer>> boxAndArcmap, Map<Integer, Arc> arcs, JCache cache,Map<Integer, Integer> arcAndOppositeArcMap ) {
		// get parking location to define box
		int clostestArcId = -1;
		int boxId = Box.getBoxId(lati, longi);

		// make sure gps in the hangzhou area(30.15,120.00, 30.40, 120.40)
		if (boxId != -1) {
			// get arc list according to box id
			ArrayList<Integer> arcList = boxAndArcmap.get(boxId);
			ArrayList<Integer> newArcList = new ArrayList<Integer>();

			// gps处于测试数据以外的box
			if (arcList == null) {
				return -1;
			}

			// don't consider the direction problem
			transferArc(arcList, newArcList, arcAndOppositeArcMap);
			// System.out.println(arcList.size() + " -> " + newArcList.size());

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

					if (dis < minDisBetPkAndCurrentArc) {
						minDisBetPkAndCurrentArc = dis;
					}

					// System.out.print(dis + "  " );
				}

				if (minDisBetPkAndCurrentArc < minDis) {
					minDis = minDisBetPkAndCurrentArc;
					minIdx = i;
				}
			}

			// get arcid by min idx
			if (minIdx != -1 && minDis <= MiniestDis) {
				clostestArcId = newArcList.get(minIdx);
			}
		}

		return clostestArcId;
	}

	public void transferArc(ArrayList<Integer> arcList,
			ArrayList<Integer> newArcList, Map<Integer, Integer> arcAndOppositeArcMap) {
		Set<Integer> newArcSet = new HashSet<Integer>();
		for (Integer integer : arcList) {
			Integer value = arcAndOppositeArcMap.get(integer);
			if (value != null) {
				if (integer >= value) {
					newArcSet.add(value);
				} else {
					newArcSet.add(integer);
				}
			} else {
				newArcSet.add(integer);
				// System.err.println("arc map fail due to arc id " + integer);
			}
		}
		for (Integer integer : newArcSet) {
			newArcList.add(integer);
		}
	}

	/*
	 * public static void main(String[] args) { ArrayList<Integer> arcList = new
	 * ArrayList<Integer>();
	 * 
	 * arcList.add(-1); arcList.add(-1); arcList.add(1); arcList.add(1);
	 * arcList.add(1); arcList.add(2); arcList.add(2); arcList.add(3);
	 * arcList.add(4); arcList.add(5); arcList.add(5);
	 * 
	 * for (Integer integer : arcList) { System.out.print(integer + "->"); }
	 * System.out.println();
	 * 
	 * int arcId = -2; double totalDis = 0.0; int count = 0; for(int i=0;
	 * i<arcList.size(); i++) { int arcIdTmp = arcList.get(i); if(arcIdTmp !=
	 * arcId) { if(arcId != -2 && arcId != -1) { double speed = totalDis /
	 * count; System.out.println(arcId + "  " + totalDis + "  " + count); }
	 * arcId = arcIdTmp; totalDis = 0.0; count = 0; } else { if(i-1 >= 0) {
	 * totalDis += 1; count++; } } }
	 * 
	 * if(arcId != -1) { System.out.println(arcId + "  " + totalDis + "  " +
	 * count); } }
	 */
}
