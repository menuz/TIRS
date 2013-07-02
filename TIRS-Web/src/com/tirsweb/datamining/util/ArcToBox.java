/**
 * 文件名：ArcToBox.java
 *
 * 版本信息： version 1.0
 * 日期：May 18, 2013
 * Copyright by menuz
 */
package com.tirsweb.datamining.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.tirsweb.dao.ArcToBoxDAO;
import com.tirsweb.model.Arc;
import com.tirsweb.model.ArcDetail;
import com.tirsweb.model.Box;
import com.tirsweb.model.Point;
import com.tirsweb.util.FileHelper;
import com.tirsweb.util.cache.JCache;
import com.tirsweb.util.gps.GeoDistance;

/**
 * 
 * 此类描述的是： box has several arcs in box, and arc may also cross several boxes. 
 * @author: dmnrei@gmail.com
 * @version: May 27, 2013 9:45:19 AM
 */
public class ArcToBox {
	ArrayList<Arc> arcList;
	JCache cache;
	
	public ArcToBox(ArrayList<Arc> arcList) {
		this.arcList = arcList;
		this.cache = new JCache(1);
	}
	
	/**
	 * 
		 * 此方法描述的是：split arc, deal one arc one time
	     * @version: May 29, 2013 10:20:49 AM
	 */
	public void to() {
		for(int i=0; i<arcList.size(); i++) {
			mapping(arcList.get(i));
		}
	}
	
	/**
	 * 
		 * 此方法描述的是：map arc to box
	     * @param arc
	     * @version: May 29, 2013 10:21:11 AM
	 */
	public void mapping(Arc arc) {
		ArrayList<ArcDetail> arcDetailList = arc.getArcDetailList();
		Set<Integer> boxSet = new HashSet<Integer>();	
		
// System.out.println("arc_id "  + arc.getId());
		
		// get boxlist
		double length = 0;
		int i = 0;
		for (i = 0; i < arcDetailList.size() - 1; i++) {
			ArcDetail arcDetail1 = arcDetailList.get(i);
			ArcDetail arcDetail2 = arcDetailList.get(i+1);
// System.out.println(arcDetail1.getLati() + "    " + arcDetail1.getLongi());
			Point p = cache.marGpsToGps(arcDetail1.getLati(), arcDetail1.getLongi());
//System.out.println(p.getLat() + "    "  + p.getLon());

			// need to gaijin
			int boxId = Box.getBoxId(p.getLat(), p.getLon());
			
//System.out.println("boxid = " + boxId);
			
			double tmp = GeoDistance.computeCompareDistance(arcDetail1.getLati(), arcDetail1.getLongi(), 
					arcDetail2.getLati(), arcDetail2.getLongi());
			length += tmp;
			boxSet.add(boxId);
		}
		
		ArcDetail arcDetail = arcDetailList.get(arcDetailList.size() - 1);
		Point p = cache.marGpsToGps(arcDetail.getLati(), arcDetail.getLongi());
		int boxId = Box.getBoxId(p.getLat(), p.getLon());
		boxSet.add(boxId);
		
		// parse set to list
		ArrayList<Integer> boxList = new ArrayList<Integer>();
		boxList.addAll(boxSet);
//System.out.println("123");
		
		System.out.println("----------" + arc.getId() + "----------");
		for (Integer integer : boxList) {
			System.out.print(integer+"->");
		}
		System.out.println();
//System.out.println("456");
		
		// set result to arc
		arc.setBoxList(boxList);
		arc.setLength(length);
	}
	
	public static void main(String[] args) {
		// 1. init info
		ArcToBoxDAO dao =  new ArcToBoxDAO();
		// get all arc, arc has arc detail
		ArrayList<Arc> arcList = dao.queryArcList();

		// use to make sure right load sequence 
		/*for (Arc arc : arcList) {
			ArrayList<ArcDetail> adList = arc.getArcDetailList();
			for (ArcDetail arcDetail : adList) {
				int idx = arcDetail.getIdx();
System.out.println("index = " + idx);
			}
		}*/

		// 2. new ArcToBox to exec mapping relation
		ArcToBox arcToBox = new ArcToBox(arcList);
		arcToBox.to();
		
		// check exec result
		/*System.out.println("size = " + arcList.size());
		for (Arc arc : arcList) {
			System.out.println(arc.getLength() + " " + arc.getArcDetailList().size());
			ArrayList<Integer> boxList = arc.getBoxList();
			for (Integer boxId : boxList) {
				System.out.println("                  " + boxId);
			}
		}*/
		
		// get len of arc, create sql to update tb_arc
		ArrayList<String> sqlList = new ArrayList<String>();
		for(Arc arc : arcList) {
			String sql = arc.getTbArcUpdateSql();			
			sqlList.add(sql);
		}
		FileHelper fileHelper = new FileHelper("tb_arc_update_0530.sql", "append", sqlList);
		fileHelper.write();
		
		// get box and arc mapping relationship , one box has many arc mapping
		sqlList = new ArrayList<String>();
		for(Arc arc : arcList) {
			String sql = arc.getTbArcBoxInsertSql();
			sqlList.add(sql);
		}
		fileHelper = new FileHelper("tb_arc_box_insert_0530.sql", "append", sqlList);
		fileHelper.write();
	}
}


