package com.tirsweb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tirsweb.dao.DBDAO;
import com.tirsweb.model.Arc;
import com.tirsweb.model.ArcDetail;
import com.tirsweb.model.ArcDis;
import com.tirsweb.model.Box;
import com.tirsweb.model.GPS;
import com.tirsweb.model.ParkingLocationCluster;
import com.tirsweb.model.Point;
import com.tirsweb.model.Speed;
import com.tirsweb.model.Up;
import com.tirsweb.model.xml.XMLParkingLocationCluster;
import com.tirsweb.model.xml.XMLPoint;
import com.tirsweb.routing.Dijkstra;
import com.tirsweb.util.ArcUtil;
import com.tirsweb.util.BoxUtil;
import com.tirsweb.util.FindUpLocationUtil;
import com.tirsweb.util.ParamConvertor;
import com.tirsweb.util.cache.Cache;
import com.tirsweb.util.gps.GeoDistance;
import com.tirsweb.util.gps.GpsConverter;
import com.tirsweb.util.xml.ErrorXML;
import com.tirsweb.util.xml.FindPassengerXML;
import com.tirsweb.util.xml.FindUpLocationXML;
import com.tirsweb.util.xml.GPSXML;
import com.tirsweb.util.xml.RouteScheduleXML;

/**
 * 
 * 此类描述的是：每个API url处理类
 * @author: dmnrei@gmail.com
 * @version: Nov 11, 2012 11:23:02 AM
 */
public class APIHandler {
	private Cache cache;
	private DBDAO db;
	
	public APIHandler() {}
	
	public APIHandler(Cache cache) {
		this.cache = cache;
		db = new DBDAO();
	}
	
	/**
	 * 
		 * 此方法描述的是： bus trajectory
	     * @param vehicle_id
	     * @param vehicle_number
	     * @param time1
	     * @param time2
	     * @return
	     * @version: May 11, 2013 1:58:32 PM
	 */
	public String trajectory(String vehicle_id, String vehicle_number, 
			String time1, String time2) {
System.out.println("v_id " + vehicle_id + " v_num " + vehicle_number + " time1 " + time1 + " time2 " + time2);
		
		List<GPS> gpses = null;
		if(time1.equals("00:00:00") || time2.equals("00:00:00")) {
			 gpses = db.queryByVechicleId(vehicle_id, vehicle_number);
    	} else {
    		gpses = db.queryByVechicleId(vehicle_id, vehicle_number, time1, time2);
    	}
		
		GpsConverter gc = new GpsConverter();
		for (GPS gps : gpses) {
			double lat = gps.getLati();
			double lon = gps.getLongi();
			
			Point p = cache.gpsToMarGPS(lat, lon);
			if(p != null) {
				gps.setLati_correct(p.getLat());
				gps.setLongi_correct(p.getLon());
			}
		}
		
		GPSXML gpsXML = new GPSXML(gpses);  		
		return gpsXML.toString();
	}
	
	
	/**
	 * 
		 * 此方法描述的是：core api -- finduplocation  乘客寻找上客点
	     * @param latitude  map gps.latitude
	     * @param longitude  map gps.longitude
	     * @param uploadTime
	     * @return
	     * @author: dmnrei@gmail.com
	     * @version: 2013-6-4 下午6:14:17
	 */
	public String finduplocation(String latitude, String longitude, String uploadTime) {
		System.out.println("lati " + latitude + " longi " + longitude + " uploadTime " + uploadTime );
		// map gps -> real gps
		Point p = cache.marGpsToGps(latitude, longitude);
		double lati = p.getLat();
		double longi = p.getLon();
		
		// step 1
		// find the box location, get all arc in current box
		int boxid = Box.getBoxId(lati, longi);
		if(boxid == -1) {
			return new ErrorXML().errorType(ErrorXML.BOXIDERROR);
		}
		
		// one box is small which may cause bad result
		// so let's get neighboring box to get result more accurate
		ArrayList<Integer> boxList = BoxUtil.getNearBoxList(boxid);

		// filter the same road segment
		ArrayList<Integer> arcList = cache.getArcListByBoxList(boxList);
		// System.out.println("before size = " + arcList.size());
		
		Map<Integer, Integer> arcAndOppositeArcMap = cache.queryArcAndOppositeMap();
		arcList = ArcUtil.transfer(arcList, arcAndOppositeArcMap);
		// System.out.println("after size = " + arcList.size());
		
		if(arcList == null) {
			return new ErrorXML().errorType(ErrorXML.BOXNOARCLISTERROR);
		}
		Map<Integer, Arc> arcs = cache.queryAllArc();
		FindUpLocationUtil util = new FindUpLocationUtil(cache);
		ArrayList<ArcDis> arcDisList = util.findNearestArc(lati, longi, arcList, arcs);
		if(arcDisList == null) {
			return new ErrorXML().errorType(ErrorXML.NearARCLISTERROR);
		}
		
		// step 2
		// find the nearest arc parking location, and get the hostest spot in 3 nearest arc
		
		double lati2 = 0;
		double longi2 = 0;
		
		// object list for xml displaying
		ArrayList<XMLParkingLocationCluster> xmlObjectList = new ArrayList<XMLParkingLocationCluster>();
		for(int i=0; i<arcDisList.size(); i++) {
			if(i==3) break;
			
			// get clustered parking location by arcid
			ArrayList<ParkingLocationCluster> plClusterList = cache.queryParkingLocationCluserListByArcId(arcDisList.get(i).getArcid());
			if(plClusterList == null) {
				continue;
			}
			
			// change to xml format
			for (ParkingLocationCluster plc : plClusterList) {
				XMLParkingLocationCluster xmlObject = new XMLParkingLocationCluster(plc.getId(), plc.getLati(), plc.getLongi(), 
						plc.getGpsCount(), plc.getArcId(), i+1);
				xmlObjectList.add(xmlObject);
			}
			
			// get the nearest arc and get the hottest spot in this arc
			if(i==0) {
				int idx = -1;
				int maxClusterNum = Integer.MIN_VALUE;
				double minDis = Double.MAX_VALUE;
				for(int j=0; j<plClusterList.size(); j++) {
					ParkingLocationCluster plCluster = plClusterList.get(j);
					Point pTemp = cache.gpsToMarGPS(plCluster.getLati(), plCluster.getLongi());
					double dis = GeoDistance.computeCompareDistance(lati, longi, plCluster.getLati(), plCluster.getLongi());
					// int gpscount = plCluster.getGpsCount();
//					if(gpscount > maxClusterNum) {
//						maxClusterNum = gpscount;
//						idx = j;
//					}
					  
					if(dis < minDis) {
						minDis = dis;
						idx = j;
					}
				}
				
				lati2 = plClusterList.get(idx).getLati();
				longi2 = plClusterList.get(idx).getLongi();
			}
		}
		
		if(xmlObjectList == null) {
			return new ErrorXML().errorType(ErrorXML.ARCPARKINGLOCATIONCLUSTERERROR);
		}
		
		// step 3 
		// return passenger gps and the potential parking location
		
		// 乘客起点
		double lati1 = Double.parseDouble(latitude);
		double longi1 = Double.parseDouble(longitude);
		
		// 推荐上客点
		// real gps to map gps
		p = cache.gpsToMarGPS(lati2, longi2);
		lati2 = p.getLat();
		longi2 = p.getLon();
		
		FindUpLocationXML fulXML = new FindUpLocationXML(cache, lati1, longi1, lati2, longi2, xmlObjectList);
		return fulXML.toString();
		
		/*
				
				int boxId = ParamConvertor.getBoxLocation(lati, longi);
				int weekday = ParamConvertor.getWeekday(uploadTime);
				int hour = ParamConvertor.getHour(uploadTime);
				
				ArrayList<Integer> boxFilter = new ArrayList<Integer>();
				boxFilter.add(boxId);
				Map<Integer, Speed> boxSpeedMap = new HashMap<Integer, Speed>();
				Map<Integer, Up> boxUpMap = new HashMap<Integer, Up>();
				
				ArrayList<Point> pointList = new ArrayList<Point>();
				Point p = new Point(lati, longi);
				pointList.add(p);
				pointList.add(new Point(30.26263, 120.09160));
				
				int boxid1 = getBoxId(boxSpeedMap, boxUpMap, boxFilter, boxId, weekday, hour);
				getGPSByBoxId(pointList, boxid1, weekday, hour);
				
				int boxid2 = getBoxId(boxSpeedMap, boxUpMap, boxFilter, boxId, weekday, hour);
				getGPSByBoxId(pointList, boxid2, weekday, hour);
				
				RouteXML routeXML = new RouteXML(pointList);
				return routeXML.toString();*/
			}
	
	public String findpassenger(String latitude, String longitude, String uploadTime) {
System.out.println("lati " + latitude + " longi " + longitude + " uploadTime " + uploadTime );

		// Step1  find current boxid and get the neigboring box list
		//map gps -> real gps
		Point p = cache.marGpsToGps(latitude, longitude);
		double lati = p.getLat();
		double longi = p.getLon();
		
		int boxid = Box.getBoxId(lati, longi);
		if(boxid == -1) {
			return new ErrorXML().errorType(ErrorXML.BOXIDERROR);
		}
		
		ArrayList<Integer> boxList = BoxUtil.getNearBoxList(boxid);

		// filter the same road segment
		ArrayList<Integer> arcList = cache.getArcListByBoxList(boxList);
		// System.out.println("before size = " + arcList.size());
		
		Map<Integer, Integer> arcAndOppositeArcMap = cache.queryArcAndOppositeMap();
		arcList = ArcUtil.transfer(arcList, arcAndOppositeArcMap);
		// System.out.println("after size = " + arcList.size());
		
		if(arcList == null) {
			return new ErrorXML().errorType(ErrorXML.BOXNOARCLISTERROR);
		}
		Map<Integer, Arc> arcs = cache.queryAllArc();
		FindUpLocationUtil util = new FindUpLocationUtil(cache);
		ArrayList<ArcDis> arcDisList = util.findNearestArc(lati, longi, arcList, arcs);
		if(arcDisList == null) {
			return new ErrorXML().errorType(ErrorXML.NearARCLISTERROR);
		}
		
		// Step2  get potential parking location same as API-finduplocation
		double lati2 = 0;
		double longi2 = 0;
		
		// iterate all arcList, and find the maximum cluster point
		double maxCount = Double.MIN_VALUE;
		int maxCountIdx = -1;
		int maxCountIdxIdx = -1;
		for(int i=0; i<arcDisList.size(); i++) {
			// get clustered parking location by arcid
			ArrayList<ParkingLocationCluster> plClusterList = cache.queryParkingLocationCluserListByArcId(arcDisList.get(i).getArcid());
			if(plClusterList == null) {
				continue;
			}
			
				// get the nearest arc and get the hottest spot in this arc
				for(int j=0; j<plClusterList.size(); j++) {
					ParkingLocationCluster plCluster = plClusterList.get(j);
					Point pTemp = cache.gpsToMarGPS(plCluster.getLati(), plCluster.getLongi());
					double dis = GeoDistance.computeCompareDistance(lati, longi, plCluster.getLati(), plCluster.getLongi());
					int gpscount = plCluster.getGpsCount();
					
					int count = plCluster.getGpsCount();
					
					if(count > maxCount) {
						maxCount = count;
						maxCountIdx = i;
						maxCountIdxIdx = j;
					}
				}
		}
		
		ParkingLocationCluster plCluster = cache.queryParkingLocationCluserListByArcId(arcDisList.get(maxCountIdx).getArcid()).get(maxCountIdxIdx);
		lati2 = plCluster.getLati();
		longi2 = plCluster.getLongi();
		int arcid2 = arcDisList.get(maxCountIdx).getArcid();
		
		// Step3  get the neigboring node as startnode and endnode 
		// 出租车起点
		double lati1 = Double.parseDouble(latitude);
		double longi1 = Double.parseDouble(longitude);
		int arcid1 = ArcUtil.GpsToArcId(cache, lati, longi);
		
		// 前往的停靠点
		// real gps to map gps
		arcid2 = arcid2;
		
		ArrayList<Integer> startAndEndNode = ArcUtil.calStartNodeAndEndNode(cache, arcid1, arcid2);
		
		int startNodeId = startAndEndNode.get(0);
		int endNodeId = startAndEndNode.get(1);
		
		// Step4  use kmeans to build up route between startnode and endnode
		ArrayList<Integer> pathList = new Dijkstra(cache).findShortestPath(startNodeId, endNodeId);
		
		// Step5  return points
		// start point
		lati1 = lati1;
		longi1 = longi1;
		
		// interval point 
		ArrayList<XMLPoint> points = new ArrayList<XMLPoint>();
		for(int i=0; i<pathList.size() - 1; i++) {
			int _startNodeId = pathList.get(i);
			int _endNodeId = pathList.get(i+1);
			Arc arc = cache.queryArcWithStartAndEndNode(_startNodeId, _endNodeId);
			ArrayList<ArcDetail> arcDetailList = arc.getArcDetailList();
			for(ArcDetail arcDetail : arcDetailList) {
				XMLPoint point = new XMLPoint(arcDetail.getLati(), arcDetail.getLongi(), arc.getId(), arcDetail.getIdx());
				points.add(point);
			}
		}
		
		// end point
		p = cache.gpsToMarGPS(lati2, longi2);
		lati2 = p.getLat();
		longi2 = p.getLon();

		FindPassengerXML findPassengerXML = new FindPassengerXML(lati1, longi1, points, lati2, longi2);
		return findPassengerXML.toString();
	}
	
	public String routeschedule(String latiS,String longiS, String latiE, String longiE, String uploadTime) {
		return routeschedule(Double.parseDouble(latiS), Double.parseDouble(longiS), Double.parseDouble(latiE), 
				Double.parseDouble(longiE), uploadTime);
	}
	
	public String routeschedule(double latiS,double longiS, double latiE, double longiE, String uploadTime) {
		System.out.println(latiS + "  " + longiS);
		System.out.println(latiE + "  " + longiE);
		
		// Step3 get the neigboring node as startnode and endnode
		Point ps = cache.marGpsToGps(latiS, longiS);
		Point pe = cache.marGpsToGps(latiE, longiE);
		
		System.out.println(ps.getLat() + "  " + ps.getLon());
		System.out.println(pe.getLat() + "  " + pe.getLon());
		
		// 出租车起点
		int arcid1 = ArcUtil.GpsToArcId(cache, ps.getLat(), ps.getLon());

		// 前往的停靠点
		// real gps to map gps
		int arcid2 = ArcUtil.GpsToArcId(cache, pe.getLat(), pe.getLon());
		
		if(arcid1 == -1 || arcid2 == -1) {
			return new ErrorXML().errorType(ErrorXML.ARCNOTFOUND);  
		}

		ArrayList<Integer> startAndEndNode = ArcUtil.calStartNodeAndEndNode(
				cache, arcid1, arcid2);

		int startNodeId = startAndEndNode.get(0);
		int endNodeId = startAndEndNode.get(1);

		// Step4 use kmeans to build up route between startnode and endnode
		Dijkstra routing = new Dijkstra(cache);
		ArrayList<Integer> pathList = routing.findShortestPath(startNodeId, endNodeId);

		// Step5 return points
		// start point
		double lati1 = latiS;
		double longi1 = longiS;

		// interval point
		ArrayList<XMLPoint> points = new ArrayList<XMLPoint>();
		for (int i = 0; i < pathList.size() - 1; i++) {
			int _startNodeId = pathList.get(i);
			int _endNodeId = pathList.get(i + 1);
			Arc arc = cache.queryArcWithStartAndEndNode(_startNodeId,
					_endNodeId);
			ArrayList<ArcDetail> arcDetailList = arc.getArcDetailList();
			for (ArcDetail arcDetail : arcDetailList) {
				XMLPoint point = new XMLPoint(arcDetail.getLati(),
						arcDetail.getLongi(), arc.getId(), arcDetail.getIdx());
				points.add(point);
			}
		}

		// end point
		double lati2 = latiE;
		double longi2 = longiE;

		RouteScheduleXML routeScheduleXML = new RouteScheduleXML(lati1, longi1,
				points, lati2, longi2);
		return routeScheduleXML.toString();
	}
	
	private void getGPSByBoxId(ArrayList<Point> pointList, int boxId, int weekday, int hour) {
		Up up = cache.queryUpByKeys(boxId, weekday, hour);
		ArrayList<Point> ps = (ArrayList<Point>)up.getPoints();
		for (Point point : ps) {
			pointList.add(point);
		}
	}
	
	/**
	 * 
		 * 此方法描述的是： get max weight of near by box id, meantime fullfill box info
	     * @param boxSpeedMap
	     * @param boxUpMap
	     * @param boxFilter
	     * @param boxid
	     * @param weekday
	     * @param hour
	     * @return
	     * @version: May 11, 2013 1:56:12 PM
	 */
	private int getBoxId(Map<Integer, Speed> boxSpeedMap, Map<Integer, Up> boxUpMap,
			ArrayList<Integer> boxFilter, int boxid, int weekday, int hour) {
		int innerBoxId = -1;
		ArrayList<Integer> nearBoxList = (ArrayList<Integer>)ParamConvertor.getNearBoxList(boxid);
		ArrayList<Speed> speedList = cache.querySpeedListByKeys(nearBoxList, weekday, hour);
		ArrayList<Up> upList = cache.queryUpListByKeys(nearBoxList, weekday, hour);
		
		// calculate sum, add list to map
		double speedSum = 0.0; 
		for (Speed speed : speedList) {
			boxSpeedMap.put(speed.getBoxId(), speed);
			speedSum += speed.getSpeed();
		}
		
		int upcountSum = 0;
		for (Up up : upList) {
			boxUpMap.put(up.getBoxId(), up);
			upcountSum += up.getUpCount();
		}
		
		// init map for calculate weight
		Map<Integer, Box> boxMap = new HashMap<Integer, Box>();
		for(Speed speed : speedList) {
			int _boxid = speed.getBoxId();
			double _speed = speed.getSpeed();
			int _upcount = boxUpMap.get(speed.getBoxId()).getUpCount();
			Box box = new Box(_boxid, _speed, _upcount, speedSum, upcountSum);
			boxMap.put(_boxid, box);
		}
		
		// use collection sort map.values() by weight
		Collection<Box> c = boxMap.values();
		List list = new ArrayList<Box>(c);
		Collections.sort(list,new Comparator<Box>(){   
	           public int compare(Box arg0, Box arg1) {   
	        	   Double a0 = new Double(arg0.getWeight());
	        	   Double a1 = new Double(arg1.getWeight());
	               return a0.compareTo(a1);
	            }   
	    });
		
		
		// get the max weight except in box filter
		Box box = null;
		for( int i=0; i<list.size(); i++) {
			box = (Box)list.get(i);
			if(!exist(boxFilter, box.getBoxId())) {
				break;
			}
		}
		innerBoxId = box.getBoxId();
		boxFilter.add(innerBoxId);
		
		return innerBoxId;
	}
	
	/**
	 * 
		 * 此方法描述的是：if boxid has appear in box filter
	     * @param boxList
	     * @param boxid
	     * @return
	     * @version: May 11, 2013 1:57:10 PM
	 */
	private boolean exist(ArrayList<Integer> boxList, int boxid) {
		for (Integer i : boxList) {
			if(boxid == i) 
				return true;
		}
		return false;
	}
}
