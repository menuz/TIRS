/**
 * 文件名：MapBuilder.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-3
 * Copyright by menuz
 */
package com.tirsweb.routing;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.tirsweb.dao.jdbc.RouteDAO;
import com.tirsweb.model.BaseArc;
import com.tirsweb.model.Node;
import com.tirsweb.model.Speed;
import com.tirsweb.util.FileHelper;
import com.tirsweb.util.TimeUtil;
import com.tirsweb.util.cache.Cache;

public class MapBuilder {
	Node[] nodes =  new Node[66+1];
	private long lastRefreshTime;
	Cache cache;
	
	public MapBuilder(Cache cache) {
		RouteDAO routeDAO = new RouteDAO();
		routeDAO.queryAllNode(nodes);
		routeDAO.initNodeRelation(nodes);
		this.cache = cache;
		
		refreshNodeRelation();
		lastRefreshTime = new Date().getTime();
	}
	
	public Node[] getNodeArray() {
		if(timeSatisfy()) {
			refreshNodeRelation();
			lastRefreshTime = new Date().getTime();
		}
		return nodes;
	}
	
	public void refreshNodeRelation() {
		ArrayList<BaseArc> arcList = cache.queryAllBaseArc();
		
		FileHelper fileHelper = new FileHelper("speed.sql", "append");
		StringBuffer sb = new StringBuffer(1024);
		
		for(BaseArc baseArc : arcList) {
			int arcid = baseArc.getId();
			int start_node_id = baseArc.getStart_node_id();
			int end_node_id = baseArc.getEnd_node_id();
			int len = baseArc.getLen();
			
			String key = Speed.getKey(arcid, TimeUtil.getWeekday(), TimeUtil.getHour());
			Speed speed = cache.querySpeedWithKey(key);
			
			if(speed == null) {
				String log = "arcid = " + arcid + ", weekday = " + TimeUtil.getWeekday() + ", hour = " + TimeUtil.getHour();
				System.err.println(log);
				sb.append(sb.toString() + "\n");
				nodes[start_node_id].getChild().put(nodes[end_node_id], Double.MAX_VALUE);
			} else {
				double time = (double)len / speed.getSpeed();
				nodes[start_node_id].getChild().put(nodes[end_node_id], time);
			}
		}
		
		fileHelper.write(sb.toString());
		fileHelper.close();
	}
	
	/**
	 * 
		 * 此方法描述的是：刷新时间间隔半小时以上
	     * @return
	     * @author: dmnrei@gmail.com
	     * @version: 2013-6-6 下午6:46:35
	 */
	public boolean timeIntervalSatisfy() {
		long now = new Date().getTime();
		
		int seconds = (int)((lastRefreshTime - now) / 1000);
		
		if(seconds > 2000) {
			return true;
		}
		
		return false;
	}
	
	public boolean timeSatisfy() {
		Calendar calendar = Calendar.getInstance();
		int minutes = calendar.get(Calendar.MINUTE);
		
		if(minutes > 55 && minutes < 59 && timeIntervalSatisfy()) {
			return true;
		}
		
		return false;
	}
}