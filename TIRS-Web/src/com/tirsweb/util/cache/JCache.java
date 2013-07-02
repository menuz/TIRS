/**
 * 文件名：JCache.java
 *
 * 版本信息： version 1.0
 * 日期：May 18, 2013
 * Copyright by menuz
 */
package com.tirsweb.util.cache;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tirsweb.dao.JCacheDAO;
import com.tirsweb.model.Arc;
import com.tirsweb.model.ParkingLocation;
import com.tirsweb.model.Point;
import com.tirsweb.model.Speed;
import com.tirsweb.model.Trip;
import com.tirsweb.model.Up;
import com.tirsweb.model.Node;

public class JCache {
	private Map<String, Point> offsets;
	private Map<String, Speed> speeds;
	private Map<String, Up> ups;
	private Map<Integer, Node> nodes;
	
	private Map<Integer, Arc> arcs;
	// 1:n
	private Map<Integer, ArrayList<Integer>> boxAndArcMap;
	private ArrayList<ParkingLocation> parkLocationList;
	private ArrayList<Trip> trips;
	
	private JCacheDAO cacheDAO;
	
	public JCache() {
		offsets = new HashMap<String, Point>();
		speeds = new HashMap<String, Speed>();
		ups = new HashMap<String, Up>();
		nodes = new HashMap<Integer, Node>();
		
		arcs = new HashMap<Integer, Arc>();
		boxAndArcMap = new HashMap<Integer, ArrayList<Integer>>();
		parkLocationList = new ArrayList<ParkingLocation>();
		
		cacheDAO = new JCacheDAO();
		initArc();
		initNode();
		initBoxAndArcListMap();
		initParkLocation();
		// initTrip();
	}
	
	/**
	 * 
	 * @param type=1  init offsets
	 */
	public JCache(int type) {
		if(type == 1) {
			offsets = new HashMap<String, Point>();
		}
		cacheDAO = new JCacheDAO();
	}
	
	public void initNode() {
		cacheDAO.queryAllNode(nodes);
	}
	
	public void initArc() {
		cacheDAO.queryAllArc(arcs);
	}
	
	public void initBoxAndArcListMap() {
		cacheDAO.queryBoxAndArcMap(boxAndArcMap);
	}
	
	public void initParkLocation() {
		cacheDAO.queryParkingLocation(parkLocationList);
	}
	
	public void initTrip() {
		cacheDAO.queryTrip(trips);
	}
	
	public Map<Integer, Arc> getArcs() {
		return arcs;
	}

	public Map<Integer, ArrayList<Integer>> getBoxAndArcMap() {
		return boxAndArcMap;
	}
	
	public Map<Integer, Node> getAllNode() {
		return nodes;
	}

	public ArrayList<ParkingLocation> getParkLocationList() {
		return parkLocationList;
	}

	public ArrayList<Trip> getTripsWithRange(int startIdx, int endIdx) {
		trips = new ArrayList<Trip>();
		cacheDAO.queryTripWithRange(trips, startIdx, endIdx);
		return trips;
	}

	public Point gpsToMarGPS(double latitude, double longitude) {
		DecimalFormat df = new DecimalFormat("###.00");
		String df_lat = df.format(latitude);
		String df_lon = df.format(longitude);
		
		String key = df_lat + df_lon;
		Point offsetGPS = null;
		if(!offsets.containsKey(key)) {
			cacheDAO.addOffsetToCache(offsets, df_lat, df_lon);
System.out.println("offsets@Cache size = "  + offsets.size());
		} 
		offsetGPS = offsets.get(key);
		
		if(offsetGPS == null) {
System.out.println("@Cache.gpsToMarGPS 超过纠偏范围");
			return null;
		} 
		
		return new Point(latitude + offsetGPS.getLat(),
				longitude + offsetGPS.getLon());
	}
	
	
	// streetview to satellite 
	public Point marGpsToGps(double latitude, double longitude) {
		DecimalFormat df = new DecimalFormat("###.00");
		String df_lat = df.format(latitude);
		String df_lon = df.format(longitude);
		
		String key = df_lat + df_lon;
		Point offsetGPS = null;
		if(!offsets.containsKey(key)) {
			cacheDAO.addOffsetToCache(offsets, df_lat, df_lon);
// System.out.println("offsets@Cache size = "  + offsets.size());
		} 
		offsetGPS = offsets.get(key);
		
		if(offsetGPS == null) {
// System.out.println("@Cache.gpsToMarGPS 超过纠偏范围");
			return null;
		} 
		
		return new Point(latitude - offsetGPS.getLat(),
				longitude - offsetGPS.getLon());
	}
	
	// queryMethod is public
	public Up queryUpByKeys(int boxid, int weekday, int hour) {
		String key = "" + boxid + weekday + hour;
		Up up = ups.get(key);
		if(up != null) {
			up = getUpByKeys(boxid, weekday, hour);
			if(up != null)
				addUpToCache(up); 
			else {
System.out.println("boxid, weekday, hour " + boxid + weekday + hour + " not exist");
			}
		}
		return up;
	}
	
	public ArrayList<Up> queryUpListByKeys(ArrayList<Integer> boxList, int weekday, int hour) {
				ArrayList<Up> upList = new ArrayList<Up>();
				ArrayList<Integer> unLoadBoxList = new ArrayList<Integer>();
				for (Integer boxId : boxList) {
					String key = Up.getKey((int)(boxId), weekday, hour);
					Up up = this.ups.get(key);
					if(up != null) upList.add(up);
					else {
						unLoadBoxList.add(boxId);
					}
				}
				
				// load data from database
				ArrayList<Up> unloadUpList = (ArrayList<Up>)getUnLoadUpListByKeys(
						unLoadBoxList, weekday, hour);
				
				// load data to cache
				addUpListToCache(unloadUpList);
				
				// add data to return list
				for (Up up : unloadUpList) {
					upList.add(up);
				}
				
				return upList;
			}
	
	public ArrayList<Speed> querySpeedListByKeys(ArrayList<Integer> boxList, int weekday, int hour) {
		ArrayList<Speed> speedList = new ArrayList<Speed>();
		ArrayList<Integer> unLoadBoxList = new ArrayList<Integer>();
		for (Integer boxId : boxList) {
			String key = Speed.getKey((int)(boxId), weekday, hour);
			Speed spd = this.speeds.get(key);
			if(spd != null) speedList.add(spd);
			else {
				unLoadBoxList.add(boxId);
			}
		}
		
		// load data from database
		ArrayList<Speed> unloadSpeedList = (ArrayList<Speed>)getUnLoadSpeedListByKeys(
				unLoadBoxList, weekday, hour);
		// load data to cache
		addSpeedListToCache(unloadSpeedList);
		
		// add data to return list
		for (Speed speed : unloadSpeedList) {
			speedList.add(speed);
		}
		
		return speedList;
	}
	
	// getUnloadMethod invoke CacheDAO to get data from db
	private List<Speed> getUnLoadSpeedListByKeys(ArrayList<Integer> boxList, int weekday, int hour) {
		return cacheDAO.querySpeedListByKeys(boxList, weekday, hour);
	}
	
	private List<Up> getUnLoadUpListByKeys(ArrayList<Integer> boxList, int weekday, int hour) {
		return cacheDAO.queryUpListByKeys(boxList, weekday, hour);
	}
	
	private Up getUpByKeys(int boxid, int weekday, int hour) {
		return cacheDAO.queryUpByKeys(boxid, weekday, hour);
	}
	
	// addMethod add object to cache
	private void addSpeedToCache(Speed spd) {
		this.speeds.put(spd.getKey(), spd);
	}
	
	private void addUpToCache(Up up) {
		this.ups.put(up.getKey(), up);
	}
	
	private void addSpeedListToCache(ArrayList<Speed> unloadSpeedList) {
		for (Speed speed : unloadSpeedList) {
			addSpeedToCache(speed);
		}
	}
	
	private void addUpListToCache(ArrayList<Up> unloadUpList) {
		for (Up up : unloadUpList) {
			addUpToCache(up);
		}
	}
}


