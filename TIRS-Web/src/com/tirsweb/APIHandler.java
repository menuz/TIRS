package com.tirsweb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tirsweb.dao.jdbc.DBDAO;
import com.tirsweb.model.Box;
import com.tirsweb.model.GPS;
import com.tirsweb.model.Point;
import com.tirsweb.model.Speed;
import com.tirsweb.model.Up;
import com.tirsweb.util.ParamConvertor;
import com.tirsweb.util.cache.Cache;
import com.tirsweb.util.gps.GpsConverter;
import com.tirsweb.util.xml.GPSXML;
import com.tirsweb.util.xml.RouteXML;

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
	
	public String findpassenger(String lati, String longi, String uploadTime) {
System.out.println("lati " + lati + " longi " + longi + " uploadTime " + uploadTime );
		
		/*int boxId = ParamConvertor.getBoxLocation(lati, longi);
		int weekday = ParamConvertor.getWeekday(uploadTime);
		int hour = ParamConvertor.getHour(uploadTime);
		
		ArrayList<Integer> boxFilter = new ArrayList<Integer>();
		boxFilter.add(boxId);
		Map<Integer, Speed> boxSpeedMap = new HashMap<Integer, Speed>();
		Map<Integer, Up> boxUpMap = new HashMap<Integer, Up>();*/
		
		ArrayList<Point> pointList = new ArrayList<Point>();
		Point p = new Point(lati, longi);
		pointList.add(p);
		pointList.add(new Point(30.26263, 120.09160));
		
		/*int boxid1 = getBoxId(boxSpeedMap, boxUpMap, boxFilter, boxId, weekday, hour);
		getGPSByBoxId(pointList, boxid1, weekday, hour);
		
		int boxid2 = getBoxId(boxSpeedMap, boxUpMap, boxFilter, boxId, weekday, hour);
		getGPSByBoxId(pointList, boxid2, weekday, hour);*/
		
		RouteXML routeXML = new RouteXML(pointList);
		return routeXML.toString();
	}
	
	public String routeschedule(double latiS,double longiS, double latiE, double longiE, String uploadTime) {
		
		return "";
	}
	
	/*public String findpassenger(String lati, String longi, String uploadTime) {
		System.out.println("lati " + lati + " longi " + longi + " uploadTime " + uploadTime );
				
				int boxId = ParamConvertor.getBoxLocation(lati, longi);
				int weekday = ParamConvertor.getWeekday(uploadTime);
				int hour = ParamConvertor.getHour(uploadTime);
				
				ArrayList<Integer> boxFilter = new ArrayList<Integer>();
				boxFilter.add(boxId);
				Map<Integer, Speed> boxSpeedMap = new HashMap<Integer, Speed>();
				Map<Integer, Up> boxUpMap = new HashMap<Integer, Up>();
				
				StringBuffer sb = new StringBuffer();
				sb.append(lati + "+" + longi + ",");
				
				int boxid1 = getBoxId(boxSpeedMap, boxUpMap, boxFilter, boxId, weekday, hour);
				getGPSByBoxId(sb, boxid1, weekday, hour);
				
				int boxid2 = getBoxId(boxSpeedMap, boxUpMap, boxFilter, boxId, weekday, hour);
				getGPSByBoxId(sb, boxid2, weekday, hour);
				
				return sb.toString();
			}*/
	
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
