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
import com.tirsweb.model.Arc;
import com.tirsweb.model.ArcDetail;
import com.tirsweb.model.BaseArc;
import com.tirsweb.model.Box;
import com.tirsweb.model.Node;
import com.tirsweb.model.Point;
import com.tirsweb.model.Trip;
import com.tirsweb.util.FileHelper;
import com.tirsweb.util.cache.JCache;
import com.tirsweb.util.gps.Angle;
import com.tirsweb.util.gps.GeoDistance;

/**
 * 
 * 此类描述的是：
 * @author: dmnrei@gmail.com
 * @version: 2013-6-2 下午7:45:28
 */
public class ZZTripToSegmentList {
	static JCache cache;
	static Map<Integer, Arc> arcs;
	static Map<Integer, ArrayList<Integer>> boxAndArcmap;
	// arc to arc, transfer big arcid to small arcid
	static Map<Integer, Integer> arcAndOppositeArcMap;
	// 
	static Map<Integer, Node> nodes;
	static long change = 0;
	
	public static void main(String[] args) {
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
		for(int i=1; i <= 826218; i+=10000) {
			idxs.add(i);
		}
		idxs.add(826218);
		
		for(int i=0; i<idxs.size()-1; i++) {
			ArrayList<Trip> trips = cache.getTripsWithRange(idxs.get(i), idxs.get(i+1));
			
			int count = 0;
			for(Trip trip : trips) {
				// System.out.println("---------------------------------- Trip ----------------------------");
				handleTrip(trip);
				count ++;
				System.out.println(trip.getTripId() + "    " +  count + " done");
				//System.out.println("---------------------------------- Trip ----------------------------");
				//System.out.println();
			}
		}
		
		System.out.println("change = " + change);
		
		
		// get all trip from table temp_trip
		// use cache to handle 
		// gps to arc id  this can be extract from GPSToArc.java
		// exame arcid sequence of a trip 
		// -1,-1,2,2,2,2,3,3,3  
		// iterate this sequence compute average speed, from tripid we can know weekday, weekend, hour of day
	}
	
	/**
	 * 
		 * 此方法描述的是：handle one trip 
	     * @param trip
	     * @version: May 27, 2013 3:32:47 PM
	 */
	public static void handleTrip(Trip trip) {
		ArrayList<Point> wayPoints = trip.getWayPoints();
		ArrayList<Integer> arcList = new ArrayList<Integer>();
		ArrayList<String> sqlList = new ArrayList<String>();
		
		// step1: transfer gps to arc id list
		// g1->g1->g2->g2->g3->g3->g4->g4
		// transfer to 
		// 1->1->2->2->3->3->4->4
		//System.out.println("Arc list: ");
		int arcid = -2;
		StringBuffer sb = new StringBuffer(1024);
		for (Point point : wayPoints) {
			int arcidTmp = GpsToArcId2(point.getLat(), point.getLon());
			arcList.add(arcidTmp);
			if(arcid != arcidTmp) {
				arcid = arcidTmp;
				if(arcidTmp != -1) {
					sb.append(arcid + "  " + arcs.get(arcidTmp).getStart_node_id()+ "->" + arcs.get(arcidTmp).getEnd_node_id()+"\n"); 
				}
			}
			//System.out.print(arcid + "->");
		}
		//System.out.println();
		//System.out.println("Split: ArcId  StartNodeId->EndNodeId");
		//System.out.println(sb.toString());
		// System.out.println();
		
		// 111122223333 get 1111 2222 3333 split
		//System.out.println("ArcId  StartIndex->EndIndex");
		//System.out.println("ArcId+AverageSpeed");
		int arcId = -2;
		double totalDis = 0.0;
		int count = 0;
		int startIdx = 0;
		for (int i = 0; i < arcList.size(); i++) {
			int arcIdTmp = arcList.get(i);
			if(arcIdTmp != arcId) {
				if(arcId != -2 && arcId != -1) {
					//System.out.println(arcId + "   " + startIdx + "  " + (i-1));
					String arcAndAverageSpeed = getAverageSpeed(arcId, startIdx, i-1, arcList, wayPoints);
					String[] temp = arcAndAverageSpeed.split("[+]");
					//System.out.println(arcAndAverageSpeed);
					
					if(!temp[0].equals(arcId+"")) {
						change++;
					}
					
					String sql = generateSql(temp[0], trip.getWeekday(), trip.getHour(), temp[1]);
					sqlList.add(sql);
				}
				startIdx = i;
				arcId = arcIdTmp;
			}
		}
		
		if(arcId != -2 && arcId != -1) {
			//System.out.println(startIdx + "  " + (arcList.size()-1) + "  " + arcId);
			String arcAndAverageSpeed = getAverageSpeed(arcId, startIdx, (arcList.size()-1), arcList, wayPoints);
			String[] temp = arcAndAverageSpeed.split("[+]");
			//System.out.println(arcAndAverageSpeed);
			String sql = generateSql(temp[0], trip.getWeekday(), trip.getHour(), temp[1]);
			sqlList.add(sql);
		}
		
		/*int arcId = -2;
		double totalDis = 0.0;
		int count = 0;
		int startIdx = 0;
		for (int i = 0; i < arcList.size(); i++) {
			int arcIdTmp = arcList.get(i);
			if(arcIdTmp != arcId) {
				if(arcId != -2 && arcId != -1) {
					
				}
				
				startIdx = i;
				arcId = arcIdTmp;
				System.out.println(startIdx + "  " + i + "  " + arcId);
			}
			*/
			
			/*int arcIdTmp = arcList.get(i);
			if (arcIdTmp != arcId) {
				if (arcId != -2 && arcId != -1) {
					double speed = -1;
					if (count != 0) {
						speed = totalDis / (count * 20);
					}

					String sql = generateSql(arcId, trip.getWeekday(),
							trip.getHour(), speed);
					sqlList.add(sql);
					// System.out.println(sql);
				}
				arcId = arcIdTmp;
				totalDis = 0.0;
				count = 0;
			} else {
				if (i - 1 >= 0) {
					totalDis += GeoDistance.computeCompareDistance(wayPoints
							.get(i).getLat(), wayPoints.get(i).getLon(),
							wayPoints.get(i - 1).getLat(), wayPoints.get(i - 1)
									.getLon());
					count++;
				}
			}*/
		// }

		/*if (arcId != -2 && arcId != -1) {
			double speed = totalDis / (count * 20);
			String sql = generateSql(arcId, trip.getWeekday(), trip.getHour(),
					speed);
			sqlList.add(sql);
		}*/
		
		/*// judge direction  
		int arcId = -2;
		double totalDis = 0.0;
		int count = 0;
		for(int i=0; i<arcList.size(); i++) {
			int arcIdTmp = arcList.get(i);
			if(arcIdTmp != arcId) {
				if(arcId != -2 && arcId != -1) {
					double speed = -1;
					if(count != 0) {
						speed = totalDis / (count * 20);
					}
					
					String sql = generateSql(arcId, trip.getWeekday(), trip.getHour(), speed);
					sqlList.add(sql);
					// System.out.println(sql);
				}
				arcId = arcIdTmp;
				totalDis = 0.0;
				count = 0;
			} else {
				if(i-1 >= 0) {
					totalDis += GeoDistance.computeCompareDistance(wayPoints.get(i).getLat(), wayPoints.get(i).getLon(), 
						wayPoints.get(i-1).getLat(), wayPoints.get(i-1).getLon());
					count++;
				}
			}
		}
		
		if(arcId != -2 && arcId != -1) {
			double speed = totalDis / (count * 20);
			String sql = generateSql(arcId, trip.getWeekday(), trip.getHour(), speed);
			sqlList.add(sql);
		}*/
		
		FileHelper fileHelper = new FileHelper("tb_arc_speed_insert_0527.sql", "append", sqlList);
		fileHelper.write();
		fileHelper.close();
	}
	
	// for example, use 1111 split to compute the average speed of arc
	// meanwhile judge the driving direction with angle. the angle is between dirving direction and arc direction.
	public static String getAverageSpeed(int arcId, int startIndex, int endIndex, ArrayList<Integer> arcList, ArrayList<Point> wayPoints) {
		Angle angle = new Angle();
		int arcCount = 0;
		int oppositeArcCount = 0;
		double totalDis = 0.0;
		int count = 0;
		for(int i=startIndex; i<=endIndex-1; i++) {
			Point p1 = wayPoints.get(i);
			Point p2 = wayPoints.get(i+1);
			
			if(i-1>=0) {
				totalDis += GeoDistance.computeCompareDistance(p1.getLat(), p1.getLon(), 
					p2.getLat(), wayPoints.get(i-1).getLon());
			}
			count++;
			
			Arc arc = arcs.get(arcList.get(i));
			BaseArc oppositeArc = arcs.get(arcAndOppositeArcMap.get(arcList.get(i)));
			Node start = nodes.get(arc.getStart_node_id());
			Node end = nodes.get(arc.getEnd_node_id());
			double _angle = angle.angle(p1.getLat(), p1.getLon(), p2.getLat(), p2.getLon(), 
					start.getLati(), start.getLongi(), end.getLati(), end.getLongi());
			
			start = nodes.get(arc.getStart_node_id());
			end = nodes.get(arc.getEnd_node_id());
			double _oppositeAngle = angle.angle(p1.getLat(), p1.getLon(), p2.getLat(), p2.getLon(),
					start.getLati(), start.getLongi(), end.getLati(), end.getLongi());
			
			if(_angle <= _oppositeAngle) {
				arcCount ++;
			} else {
				oppositeArcCount ++;
			}
		}
		
		String returnStr = "";
		double averageSpeed = totalDis / (20*count);
		if(arcCount >= oppositeArcCount) {
			returnStr += (arcId + "");
		} else {
			int oppositeArc = arcAndOppositeArcMap.get(arcId);
			returnStr += (oppositeArc + "");
		}
		returnStr = returnStr + "+" + averageSpeed;		
		return returnStr;
	}
	
	public static String generateSql(int arcid, int weekday, int hour, double average) {
		String sql = "insert into tb_arc_speed(arc_id, weekday, hourofday, speed) values (" + arcid + ", " 
				+ weekday + ", " + hour + ", " + average + ");";
		return sql;
	}
	
	public static String generateSql(String arcid, int weekday, int hour, String average) {
		String sql = "insert into tb_arc_speed(arc_id, weekday, hourofday, speed) values (" + arcid + ", " 
				+ weekday + ", " + hour + ", " + average + ");";
		return sql;
	}
	
	
/*	public static int GpsToArcId(double lati, double longi) {
		// get parking location to define box
		int clostestArcId = -1;
		int boxId = Box.getBoxId(lati, longi);

		if (boxId != -1) {
			// get arc list according to box id
			ArrayList<Integer> arcList = boxAndArcmap.get(boxId);
			int i = 0;

			if (arcList == null) {
				return -1;
			}

			//
			double minDis = Double.MAX_VALUE;
			int minIdx = -1;

			// iterate all arc
			for (i = 0; i < arcList.size(); i++) {
				int arcid = arcList.get(i);

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
				clostestArcId = arcList.get(minIdx);
			}
		} else {
			
		}
		return clostestArcId;
	}*/
	
	/**
	 * 
		 * 此方法描述的是：find which arc gps lies in
		 *              gps will compare distance to all gpses in one arc and record the average dis
	     * @param lati
	     * @param longi
	     * @return
	     * @version: May 27, 2013 3:36:28 PM
	 */
	public static int GpsToArcId(double lati, double longi) {
		// get parking location to define box
		int clostestArcId = -1;
		int boxId = Box.getBoxId(lati, longi);

		if (boxId != -1) {
			// get arc list according to box id
			ArrayList<Integer> arcList = boxAndArcmap.get(boxId);
			ArrayList<Integer> newArcList = new ArrayList<Integer>();
			
			if (arcList == null) {
				return -1;
			}
			
			transferArc(arcList, newArcList);
			
			int i = 0;

			if (newArcList == null) {
				return -1;
			}

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

			// System.out.println("   " + clostestArcId);
		} else {
			
		}
		return clostestArcId;
	}
	
	/**
	 * 
		 * 此方法描述的是：find which arc gps lies in
		 * 	            gps will compare distance to all gpses in one arc and record the miniest dis
	     * @param lati
	     * @param longi
	     * @return
	     * @version: May 27, 2013 3:39:57 PM
	 */
	public static int GpsToArcId2(double lati, double longi) {
		// get parking location to define box
		int clostestArcId = -1;
		int boxId = Box.getBoxId(lati, longi);

		if (boxId != -1) {
			// get arc list according to box id
			ArrayList<Integer> arcList = boxAndArcmap.get(boxId);
			ArrayList<Integer> newArcList = new ArrayList<Integer>();
			
			if (arcList == null) {
				return -1;
			}
			
			transferArc(arcList, newArcList);
			
			int i = 0;

			if (newArcList == null) {
				return -1;
			}

			double minDis = Double.MAX_VALUE;
			int minIdx = -1;

			// iterate all arc
			for (i = 0; i < newArcList.size(); i++) {
				int arcid = newArcList.get(i);

				// get one arc's all info by arc id
				Arc arc = arcs.get(arcid);
				ArrayList<ArcDetail> adList = arc.getArcDetailList();

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
					
					if(dis < minDis) {
						minDis = dis;
						minIdx = i;
					}
				}
			}

			// get arcid by min idx
			if (minIdx != -1) {
				clostestArcId = newArcList.get(minIdx);
			}

			// System.out.println("   " + clostestArcId);
		} else {
			
		}
		return clostestArcId;
	}
	
	public static void transferArc(ArrayList<Integer> arcList, ArrayList<Integer> newArcList) {
		Set<Integer> newArcSet = new HashSet<Integer>();
		for (Integer integer : arcList) {
			Integer value = arcAndOppositeArcMap.get(integer);
			if(value == null) {
				continue;
			}
			
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
	
	/*public static void main(String[] args) {
		ArrayList<Integer> arcList = new ArrayList<Integer>();
		
		arcList.add(-1);
		arcList.add(-1);
		arcList.add(1);
		arcList.add(1);
		arcList.add(1);
		arcList.add(2);
		arcList.add(2);
		arcList.add(3);
		arcList.add(4);
		arcList.add(5);
		arcList.add(5);
		
		for (Integer integer : arcList) {
			System.out.print(integer + "->");
		}
		System.out.println();
		
		int arcId = -2;
		double totalDis = 0.0;
		int count = 0;
		for(int i=0; i<arcList.size(); i++) {
			int arcIdTmp = arcList.get(i);
			if(arcIdTmp != arcId) {
				if(arcId != -2 && arcId != -1) {
					double speed = totalDis / count;
					System.out.println(arcId + "  " + totalDis + "  " + count);
				}
				arcId = arcIdTmp;
				totalDis = 0.0;
				count = 0;
			} else {
				if(i-1 >= 0) {
					totalDis += 1;
					count++;
				}
			}
		}
		
		if(arcId != -1) {
			System.out.println(arcId + "  " + totalDis + "  " + count);
		}
	}*/
}


