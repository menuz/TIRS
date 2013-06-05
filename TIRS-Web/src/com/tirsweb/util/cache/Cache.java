package com.tirsweb.util.cache;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tirsweb.dao.datasource.CacheDAO;
import com.tirsweb.model.Arc;
import com.tirsweb.model.Node;
import com.tirsweb.model.ParkingLocationCluster;
import com.tirsweb.model.Point;
import com.tirsweb.model.Speed;
import com.tirsweb.model.Up;

public class Cache {
	private Map<String, Point> offsets;
	private Map<String, Speed> speeds;
	private Map<String, Up> ups;
	private Map<Integer, Arc> arcs;
	private Map<Integer, Node> nodes;
	
	// 
	private Map<Integer, ArrayList<Integer>> boxAndArcMap;
	// 
	private Map<Integer, ArrayList<ParkingLocationCluster>> pkClusterListMap;
	// 
	private Map<Integer, Integer> arcAndOppositeArcMap;
	
	private CacheDAO cacheDAO;
	
	public Cache() {
		offsets = new HashMap<String, Point>();
		speeds = new HashMap<String, Speed>();
		ups = new HashMap<String, Up>();
		arcs = new HashMap<Integer, Arc>();
		nodes = new HashMap<Integer, Node>();
		
		cacheDAO = new CacheDAO();
		
		// 加在box与arcid关系，一般一个box下面有若干arcid
		boxAndArcMap = new HashMap<Integer, ArrayList<Integer>>();
		initBoxAndArcListMap();
		
		// 加在arc相关信息,包括arcdetail等信息
		initArc();
		initNode();
		
		//
		pkClusterListMap = new HashMap<Integer, ArrayList<ParkingLocationCluster>>();
		// 
		arcAndOppositeArcMap = new HashMap<Integer, Integer>();
	}
	
	public void initArc() {
		cacheDAO.queryAllArc(arcs);
	}
	
	public void initNode() {
		cacheDAO.queryAllNode(nodes);
	}
	
	public void initBoxAndArcListMap() {
		cacheDAO.queryAllBoxAndArcMap(boxAndArcMap);
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
	
	public Point marGpsToGps(String latitude, String longitude) {
		return marGpsToGps(Double.parseDouble(latitude), Double.parseDouble(longitude));
	}
	
	public Point marGpsToGps(double latitude, double longitude) {
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
		
		return new Point(latitude - offsetGPS.getLat(),
				longitude - offsetGPS.getLon());
	}
	
	// queryMethod is public
	public Map<Integer, Integer> queryArcAndOppositeMap() {
		if(arcAndOppositeArcMap.size() == 0) {
			cacheDAO.getArcAndOppositeArcMap(arcAndOppositeArcMap);
		}
		return arcAndOppositeArcMap;
	}
	
	public Map<Integer, ArrayList<Integer>> queryBoxAndArcMap() {
		return boxAndArcMap;
	}
	
	public ArrayList<Integer> getArcListByBoxId(int boxid) {
		ArrayList<Integer> arcList = boxAndArcMap.get(boxid);
		return arcList;
	}
	
	public ArrayList<Integer> getArcListByBoxList(ArrayList<Integer> boxList) {
		//filter the same arc id 
		Set<Integer> arcSet = new HashSet<Integer>();
		
		for (Integer boxid : boxList) {
			ArrayList<Integer> arcList = getArcListByBoxId(boxid);
			if(arcList == null) // addAll(null) exception
				continue;
			arcSet.addAll(arcList);
		}
		ArrayList<Integer> returnArcList = new ArrayList<Integer>();
		returnArcList.addAll(arcSet);
		
		return returnArcList;
	}
	
	public Map<Integer, Node> queryAllNode() {
		return nodes;
	}
	
	public Map<Integer, Arc> queryAllArc() {
		return arcs;
	} 
	
	public Arc queryArcByArcId() {
		return null;
	}
	
	public ArrayList<ParkingLocationCluster> queryParkingLocationCluserListByArcId(int arcId) {
		ArrayList<ParkingLocationCluster> pkClusterList = pkClusterListMap.get(arcId);
		if(pkClusterList == null) {
			cacheDAO.queryParkingLocationCluster(arcId, pkClusterListMap);
			return pkClusterListMap.get(arcId);
		}
		
		return pkClusterList;
	}
	
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
	
	public Node queryNodeByNodeId(int nodeId) {
		return nodes.get(nodeId);
	}
	
	public Node queryNodeByNodeId(String nodeId) {
		return nodes.get(Integer.parseInt(nodeId));
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
