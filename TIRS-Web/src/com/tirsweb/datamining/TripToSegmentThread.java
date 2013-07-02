/**
 * 文件名：FindingParking.java
 *
 * 版本信息： version 1.0
 * 日期：May 17, 2013
 * Copyright by menuz
 */
package com.tirsweb.datamining;

import java.util.ArrayList;
import java.util.Map;

import com.tirsweb.dao.TripToSegmentListDAO;
import com.tirsweb.model.Arc;
import com.tirsweb.model.Node;
import com.tirsweb.model.TbArcSpeed;
import com.tirsweb.model.Trip;
import com.tirsweb.util.cache.JCache;


/**
 * 
 * 此类描述的是： 每个线程处理一万个，每次1000个
 * @author: dmnrei@gmail.com
 * @version: 2013-6-11 下午5:59:12
 */
public class TripToSegmentThread extends Thread{
	int threadId;
	int startIdx;
	int endIdx;
	
	Map<Integer, Arc> arcs = null;
	Map<Integer, Integer> arcAndOppositeArcMap = null;
	Map<Integer, Node> nodes = null;
	Map<Integer, ArrayList<Integer>> boxAndArcmap = null;
	JCache cache = null;
	
	TripToSegmentListDAO dao= null;
	
	
	public TripToSegmentThread(int threadId, int startIdx, int endIdx, Map<Integer, Arc> arcs, Map<Integer, Integer> arcAndOppositeArcMap,
			Map<Integer, Node> nodes, Map<Integer, ArrayList<Integer>> boxAndArcmap, JCache cache) {
		this.threadId = threadId;
		this.startIdx = startIdx;
		this.endIdx = endIdx;
		
		this.arcs = arcs;
		this.arcAndOppositeArcMap = arcAndOppositeArcMap;
		this.nodes = nodes;
		this.boxAndArcmap = boxAndArcmap;
		this.cache = cache;
	}

	@Override
	public void run() {
		super.run();
		
		dao = new TripToSegmentListDAO();
		
		TripToSegmentList tripHandler = new TripToSegmentList();
		int count = 0;
		
		FindParkingLocation fp = new FindParkingLocation();
		
		int tripcount = 1;
		for(int i=startIdx; i<endIdx; i=i+1000) {
			int j=i+1000;
			if(j>=endIdx) {
				j=endIdx;
			}
			
			ArrayList<Trip> trips = dao.queryTripWithRange(i,j);
			ArrayList<TbArcSpeed> arcSpeedContainer = new ArrayList<TbArcSpeed>();
			
			for (Trip trip : trips) {
				// System.out.println("---------------------------------- Trip ----------------------------");
				tripHandler.handleTrip(trip, arcs, arcAndOppositeArcMap, nodes, boxAndArcmap, cache, arcSpeedContainer);
				
				if(arcSpeedContainer.size() > 100) {
					dao.insertArcSpeed(arcSpeedContainer);
					arcSpeedContainer = new ArrayList<TbArcSpeed>();
				}
			}
			
			tripcount += 1000;
			System.out.println("thread id = " + this.threadId + " tripcount = " + tripcount);
		}
	}
}
