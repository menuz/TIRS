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
import com.tirsweb.model.BaseArc;
import com.tirsweb.model.Node;
import com.tirsweb.model.ParkingLocationCluster;
import com.tirsweb.model.Point;
import com.tirsweb.model.Speed;
import com.tirsweb.model.Up;
import com.tirsweb.routing.MapBuilder;
import com.tirsweb.util.cache.thread.SpeedManager;

public class Cache {
	/**
	 * load at once, so we can define map capacity, and prevent rehash cost
	 * default load factor 0.75
	 */
	private int ARCNUMBER = 211;
	private int	NODENUMBER = 66;
	private double loadfactor = 0.75; 
	
	private Map<String, Point> offsets;
	private Map<String, Up> ups;
	private Map<Integer, Arc> arcs;
	private ArrayList<BaseArc> arcList;
	private Map<Integer, Node> nodes;
	
	// 
	private Map<Integer, ArrayList<Integer>> boxAndArcMap;
	
	// 路段停靠点map 
	private Map<Integer, ArrayList<ParkingLocationCluster>> pkClusterListMap;
	
	// 路段速度map
	private Map<String, Speed> speeds;
	
	// 
	private Map<Integer, Integer> arcAndOppositeArcMap;
	
	//
	private Map<String, Arc> startAndEndNodeMapArc;
	
	private MapBuilder mapBuilder;
	
	
	private CacheDAO cacheDAO;
	
	public Cache() {
		offsets = new HashMap<String, Point>(128);
		speeds = new HashMap<String, Speed>(512);
		
		/**
		 * load speed at init function
		 */
		SpeedManager speedManager = new SpeedManager(speeds);
		Thread t = new Thread(speedManager);
		t.start();
		
		arcs = new HashMap<Integer, Arc>((int)(ARCNUMBER/loadfactor));
		arcList = new ArrayList<BaseArc>();
		startAndEndNodeMapArc = new HashMap<String, Arc>((int)(ARCNUMBER/loadfactor));
		
		nodes = new HashMap<Integer, Node>((int)(NODENUMBER/loadfactor));
		
		cacheDAO = new CacheDAO();
		
		// 加在box与arcid关系，一般一个box下面有若干arcid
		boxAndArcMap = new HashMap<Integer, ArrayList<Integer>>();
		initBoxAndArcListMap();
		
		// 加在arc相关信息,包括arcdetail等信息
		initArc();
		initArcList();
		initStartAndEndNodeMapArc();
		// 加在节点信息
		initNode();
		
		//
		pkClusterListMap = new HashMap<Integer, ArrayList<ParkingLocationCluster>>();
		// 
		arcAndOppositeArcMap = new HashMap<Integer, Integer>();
		
		//
		mapBuilder = new MapBuilder(this);
	}
	
	public void initArc() {
		cacheDAO.queryAllArc(arcs);
	}
	
	public void initArcList() {
		cacheDAO.queryAllArcList(arcList);
	}
	
	public void initStartAndEndNodeMapArc() {
		cacheDAO.initStartAndEndNodeMapArc(arcs, startAndEndNodeMapArc);
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
	public Speed querySpeedWithKey(String key) {
		return speeds.get(key);
	}
	
	
	public ArrayList<BaseArc> queryAllBaseArc() {
		return arcList;
	}
	
	public Map<Integer, Integer> queryArcAndOppositeMap() {
		if(arcAndOppositeArcMap.size() == 0) {
			cacheDAO.getArcAndOppositeArcMap(arcAndOppositeArcMap);
		}
		return arcAndOppositeArcMap;
	}
	
	public Arc queryArcWithStartAndEndNode(int startNodeId, int endNodeId) {
		String key = startNodeId + "+" + endNodeId;
		return startAndEndNodeMapArc.get(key);
	}
	
	public Map<Integer, ArrayList<Integer>> queryBoxAndArcMap() {
		return boxAndArcMap;
	}
	
	public void initNodeRelation(Node[] nodes) {
		cacheDAO.initNodeRelation(nodes);
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
	
	public Arc queryArcByArcId(int arcId) {
		return arcs.get(arcId);
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
	
	/* Dijkstra Map Routing Part */
	public Node[] getNodeArray() {
		return mapBuilder.getNodeArray();
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
