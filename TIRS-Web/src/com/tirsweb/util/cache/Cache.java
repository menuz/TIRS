package com.tirsweb.util.cache;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.tirsweb.dao.CacheDAO;
import com.tirsweb.model.Point;

public class Cache {
	private Map<String, Point> offsets;
	private CacheDAO cacheDAO;
	
	public Cache() {
		offsets = new HashMap<String, Point>();
		cacheDAO = new CacheDAO();
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
	
	/*private Map<Integer, Route> routes;
	private Map<Integer, Direction> directions;
	private Map<Integer, Stop> stops;
	private Map<Integer, Bus> buses;
	
	// 根据latitude,longitude前两个可以确定偏移值，偏移值保存在offsets中
	private Map<String, GPS> offsets;
	private CacheDAO cacheDAO;
	
	public Cache() {
		cacheDAO = new CacheDAO();
		initAll();
	}
	
	public void initAll() {
		stops = cacheDAO.queryAllStop();
		directions = cacheDAO.queryAllDirection(this);
		routes = cacheDAO.queryAllRoute(this);
		buses = cacheDAO.queryAllBus(this);
		offsets = new HashMap<String, GPS>();
	}

	public Map<Integer, Route> getRoutes() {
		return routes;
	}

	public void setRoutes(Map<Integer, Route> route) {
		this.routes = routes;
	}

	public Map<Integer, Direction> getDirections() {
		return directions;
	}

	public void setDirections(Map<Integer, Direction> directions) {
		this.directions = directions;
	}

	public Map<Integer, Stop> getStops() {
		return stops;
	}

	public void setStops(Map<Integer, Stop> stops) {
		this.stops = stops;
	}
	
	public Route getRouteById(int routeId) {
		if(routes.containsKey(routeId)) {
			return routes.get(routeId);
		} else {
			return null;
		}
	}
	
	public Route getRouteById(String routeId) {
		return getRouteById(Integer.parseInt(routeId));
	}
	
	public Direction getDirectionById(int directionId) {
		if(directions.containsKey(directionId)) {
			return directions.get(directionId);
		} else {
			return null;
		}
	}
	
	public Direction getDirectionById(String directionId) {
		return getDirectionById(Integer.parseInt(directionId));
	}
	
	public Stop getStopById(int stopId) {
		if(stops.containsKey(stopId)) {
			return stops.get(stopId);
		} else {
			return null;
		}
	}
	
	public Stop getStopById(String stopId) {
		return getStopById(Integer.parseInt(stopId));
	}
	
	
	*//**
	 * 
		 * 此方法描述的是： 根据路线编号找到下面方向列表
		 * @param 
	     * @return List<Direction> 
		 * @version: Oct 22, 2012 6:06:54 PM
		 * @author: dmnrei@gmail.com
	 *//*
	public List<Direction> getDirectionListByRouteId(int routeId) {
		Route route = getRouteById(routeId);
		
		if(route.getDirections() == null) {
			cacheDAO.addDirectionListToRoute(route, directions, routeId);
		}
		
		return route.getDirections();
	}
	
	*//**
	 * 
		 * 此方法描述的是： 
		 * @param 
	     * @return List<Direction> 
		 * @version: Oct 22, 2012 6:07:36 PM
		 * @author: dmnrei@gmail.com
	 *//*
	public List<Direction> getDirectionListByRouteId(String routeId) {
		return getDirectionListByRouteId(Integer.parseInt(routeId));
	}
	
	
	*//**
	 * 
		 * 此方法描述的是： 根据路线编号找到下面方向列表
		 * @param 
	     * @return List<Direction> 
		 * @version: Oct 22, 2012 6:06:54 PM
		 * 
		 * 
		 * @author: dmnrei@gmail.com
	 *//*
	public List<Bus> getBusListByRouteId(int routeId) {
		Route route = getRouteById(routeId);
		
		if(route.getBuses() == null) {
			cacheDAO.addBusListToRoute(route, buses, routeId);
		}
		
		return route.getBuses();
	}
	
	*//**
	 * 
		 * 此方法描述的是： 
		 * @param 
	     * @return List<Direction> 
		 * @version: Oct 22, 2012 6:07:36 PM
		 * @author: dmnrei@gmail.com
	 *//*
	public List<Bus> getBusListByRouteId(String routeId) {
		return getBusListByRouteId(Integer.parseInt(routeId));
	}
	
	
	*//**
	 * 
		 * 此方法描述的是： 
		 * @param 
	     * @return 
		 * @version: Oct 22, 2012 6:06:54 PM
		 * 
		 * 
		 * @author: dmnrei@gmail.com
	 *//*
	public Bus getBusById(int busId) {
		Bus bus = buses.get(busId);
		return bus;
	}
	
	*//**
	 * 
		 * 此方法描述的是： 
		 * @param 
	     * @return 
		 * @version: Oct 22, 2012 6:06:54 PM
		 * 
		 * 
		 * @author: dmnrei@gmail.com
	 *//*
	public Bus getBusById(String busId) {
		return getBusById(Integer.parseInt(busId));
	}
	
	
	*//**
	 * 
		 * 此方法描述的是： 根据方向编号找到下面站点列表
		 * @param 
	     * @return List<Stop> 
		 * @version: Oct 22, 2012 5:56:25 PM
		 * @author: dmnrei@gmail.com
	 *//*
	public List<Stop> getStopListByDirectionId(int directionId) {
		Direction direction = getDirectionById(directionId);
		
		if(direction.getStops() == null) {
			cacheDAO.addStopListToDirection(direction, this.stops, directionId);
		}
		return direction.getStops();
	}
	
	*//**
	 * 
		 * 此方法描述的是： List<Stop> getStopListByDirectionId(int directionId)
		 * @param 
	     * @return List<Stop> 
		 * @version: Oct 22, 2012 5:57:13 PM
		 * @author: dmnrei@gmail.com
	 *//*
	public List<Stop> getStopListByDirectionId(String directionId) {
		return getStopListByDirectionId(Integer.parseInt(directionId));
	}
	
	
	*//**
	 * 
		 * 此方法描述的是： 根据方向编号找到path路线信息
		 * @param 
	     * @return List<Stop> 
		 * @version: Oct 22, 2012 5:56:25 PM
		 * @author: dmnrei@gmail.com
	 *//*
	public Path getPathByDirectionId(int directionId) {
		Direction direction = getDirectionById(directionId);
		
		if(direction.getPath() == null) {
			cacheDAO.addPathToDirection(direction, directions, directionId);
		}
		return direction.getPath();
	}
	
	*//**
	 * 
		 * 此方法描述的是：
		 * @param 
	     * @return List<Stop> 
		 * @version: Oct 22, 2012 5:57:13 PM
		 * @author: dmnrei@gmail.com
	 *//*
	public Path getPathByDirectionId(String directionId) {
		return getPathByDirectionId(Integer.parseInt(directionId));
	}
	
	*//**
	 * 
		 * 此方法描述的是： 根据方向编号找到path路线信息
		 * @param 
	     * @return List<Stop> 
		 * @version: Oct 22, 2012 5:56:25 PM
		 * @author: dmnrei@gmail.com
	 *//*
	public HistoryTimeManager getHistoryTimeManagerByRouteId(int routeId) {
		Route route = getRouteById(routeId);
		return route.getHtManager();
	}
	
	*//**
	 * 
		 * 此方法描述的是：
		 * @param 
	     * @return List<Stop> 
		 * @version: Oct 22, 2012 5:57:13 PM
		 * @author: dmnrei@gmail.com
	 *//*
	public HistoryTimeManager getHistoryTimeManagerByRouteId(String routeId) {
		return getHistoryTimeManagerByRouteId(Integer.parseInt(routeId));
	}
	
	
	public GPS gpsToMarGPS(double latitude, double longitude) {
		DecimalFormat df = new DecimalFormat("###.00");
		String df_lat = df.format(latitude);
		String df_lon = df.format(longitude);
		
		String key = df_lat + df_lon;
		GPS offsetGPS = null;
		if(!offsets.containsKey(key)) {
			cacheDAO.addOffsetToCache(offsets, df_lat, df_lon);
		} 
		offsetGPS = offsets.get(key);
		
		if(offsetGPS == null) {
System.out.println("@Cache.gpsToMarGPS 超过纠偏范围");
			return null;
		} 
		
		return new GPS(latitude + offsetGPS.getLatitude(),
				longitude + offsetGPS.getLongitude());
	}*/
}
