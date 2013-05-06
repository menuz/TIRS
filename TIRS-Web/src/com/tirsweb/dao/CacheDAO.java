package com.tirsweb.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.tirsweb.model.GPS;
import com.tirsweb.model.Point;
import com.tirsweb.util.TableLoader;
import com.tirsweb.util.cache.Cache;

/**
 * 
 * 此类描述的是：Cache类的数据库操作类
 * @author: dmnrei@gmail.com
 * @version: Nov 12, 2012 6:41:12 PM
 */
public class CacheDAO extends JDBCDAO {
	private final String TAG = "CacheDAO ";
	
    public CacheDAO() {
       super();
    }
    
    /**
     * 
    	 * 此方法描述的是：抓取route表到内存
    	 * @author: dmnrei@gmail.com
    	 * @version: Nov 12, 2012 6:40:10 PM
     */
   /* public Map<Integer, Route> queryAllRoute(Cache cache) {
    	Map<Integer,Route> routeMap = new HashMap<Integer, Route>();
        String sql = "select * from route";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
        	conn = dataSource.getConnection();
        	stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                Route route = TableLoader.loadRoute(rs);
                
                addDirectionListToRoute(route, cache.getDirections(), id);
                
                route.setHtManager(new HistoryTimeManager(id));
                
                routeMap.put(id, route);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	releaseSource(conn, stmt, rs);
        }
        return routeMap;
    }*/
    
    public void addOffsetToCache(Map<String, Point> offsets, String lat, String lon) {
    	Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConn();
			String sql = "select offsetlat, offsetlon from tb_gps_correct where lat=" + lat + " and lon=" + lon + "";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()) { 
				double offsetlat = rs.getDouble("offsetlat");
				double offsetlon = rs.getDouble("offsetlon");
System.out.println("offsetlat = " + offsetlat + " offsetlon = " + offsetlon);
				offsets.put(lat+lon, new Point(offsetlat, offsetlon));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(TAG + "function@gpsToMarGps");
		} finally {
			releaseSource(conn, stmt, rs);
		}
    }
    
    
/*    *//**
     * 
    	 * 此方法描述的是：抓取direction表到内存
    	 * @author: dmnrei@gmail.com
    	 * @version: Nov 12, 2012 6:40:31 PM
     *//*
    public Map<Integer, Direction> queryAllDirection(Cache cache) {
    	Map<Integer,Direction> directionMap = new HashMap<Integer, Direction>();
        String sql = "select * from direction";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
        	conn = dataSource.getConnection();
        	stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                Direction direction = TableLoader.loadDirection(rs);
                
                // 将站点列表加载到direction中
                addStopListToDirection(direction, cache.getStops(), id);
                
                directionMap.put(id, direction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	releaseSource(conn, stmt, rs);
        }
        return directionMap;
    }
    
    *//**
     * 
    	 * 此方法描述的是：抓取stop表到内存
    	 * @author: dmnrei@gmail.com
    	 * @version: Nov 12, 2012 6:40:40 PM
     *//*
    public Map<Integer, Stop> queryAllStop() {
    	Map<Integer,Stop> stopMap = new HashMap<Integer, Stop>();
        String sql = "select * from stop";
        List list = new ArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
        	conn = dataSource.getConnection();
        	stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                stopMap.put(id, TableLoader.loadStop(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	releaseSource(conn, stmt, rs);
        }
        return stopMap;
    }
    
    *//**
     * 
    	 * 此方法描述的是：抓取bus表到内存
    	 * @author: dmnrei@gmail.com
    	 * @version: Nov 12, 2012 6:40:49 PM
     *//*
    public Map<Integer, Bus> queryAllBus(Cache cache) {
    	Map<Integer,Bus> buses = new HashMap<Integer, Bus>();
        String sql = "select * from bus";
        List list = new ArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
        	conn = dataSource.getConnection();
        	stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                Bus bus = TableLoader.loadBus(cache, rs);
                buses.put(id, bus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	releaseSource(conn, stmt, rs);
        }
        return buses;
    }
    
    *//**
     * 
    	 * 此方法描述的是：添加常用gpsoffset
    	 * @author: dmnrei@gmail.com
    	 * @version: Nov 12, 2012 6:39:44 PM
     *//*
    public void addOffsetToCache(Map<String, GPS> offsets, String lat, String lon) {
    	Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			String sql = "select offsetlat, offsetlon from gps_correct where lat=" + lat + " and lon=" + lon + ";";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()) { 
				double offsetlat = rs.getDouble("offsetlat");
				double offsetlon = rs.getDouble("offsetlon");
				offsets.put(lat+lon, new GPS(offsetlat, offsetlon));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(TAG + "function@gpsToMarGps");
		} finally {
			releaseSource(conn, stmt, rs);
		}
    }
    
    *//**
     * 
    	 * 此方法描述的是：添加direction-stop之间的索引
    	 * @author: dmnrei@gmail.com
    	 * @version: Nov 12, 2012 6:34:38 PM
     *//*
    public void addStopListToDirection(Direction direction, Map<Integer, Stop> stops, int direction_id) {
		Direction dir = null;
		Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Stop> stopList = new ArrayList<Stop>();
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			String sql = "select stop_id from stop_direction_mapping where direction_id = " + direction_id
					+ "";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int stop_id = rs.getInt("stop_id");
				stopList.add(stops.get(stop_id));
			}
			
			direction.setStops(stopList);
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			releaseSource(conn, stmt, rs);
		}
	}
    
    *//**
     * 
    	 * 此方法描述的是：添加route-bus之间的索引
    	 * @author: dmnrei@gmail.com
    	 * @version: Nov 12, 2012 6:38:49 PM
     *//*
    public void addPathToDirection(Direction direction, Map<Integer, Direction> directions, int direction_id) {
		Direction dir = null;
		Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Bus> busList = new ArrayList<Bus>();
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			String sql = "select * from path where directionId = " + direction_id;
			rs = stmt.executeQuery(sql);
			
			Path path = new Path(direction_id);
			ArrayList<Point> points = new ArrayList<Point>();
			while (rs.next()) {
				double lat = rs.getDouble("latitude");
				double lon = rs.getDouble("longitude");
				Point p = new Point(lat, lon);
				points.add(p);
			}
			path.setPoints(points);
			   
			directions.get(direction_id).setPath(path);
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			releaseSource(conn, stmt, rs);
		}
	}
    
    *//**
     * 
    	 * 此方法描述的是：添加route-direction之间的索引
    	 * @author: dmnrei@gmail.com
    	 * @version: Nov 12, 2012 6:39:17 PM
     *//*
    public void addDirectionListToRoute(Route route, Map<Integer, Direction> directions, int route_id) {
		Direction dir = null;
		Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Direction> directionList = new ArrayList<Direction>();
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			String sql = "select id from direction where route_id = " + route_id;
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int directionId = rs.getInt("id");
				directionList.add(directions.get(directionId));
			}
			   
			route.setDirections(directionList);
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			releaseSource(conn, stmt, rs);
		}
	}
    
    *//**
     * 
    	 * 此方法描述的是：添加route-bus之间的索引
    	 * @author: dmnrei@gmail.com
    	 * @version: Nov 12, 2012 6:38:49 PM
     *//*
    public void addBusListToRoute(Route route, Map<Integer, Bus> buses, int route_id) {
		Direction dir = null;
		Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Bus> busList = new ArrayList<Bus>();
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			String sql = "select id from bus where route_id = " + route_id;
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int busId = rs.getInt("id");
				busList.add(buses.get(busId));
			}
			   
			route.setBuses(busList);
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			releaseSource(conn, stmt, rs);
		}
	}*/
}
