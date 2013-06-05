/**
 * 文件名：BoxUtil.java
 *
 * 版本信息： version 1.0
 * 日期：May 29, 2013
 * Copyright by menuz
 */
package com.tirsweb.util;

import java.util.ArrayList;
import java.util.List;

import com.tirsweb.model.NodePoint;

public class BoxUtil {
	public BoxUtil(){
		
	}
	
	
	/**
	 * 
		 * 此方法描述的是：获得临近的8个box
	     * @param box
	     * @return
	     * @author: dmnrei@gmail.com
	     * @version: 2013-6-5 上午9:04:30
	 */
	public static ArrayList<Integer> getNearBoxList(int box) {
		int a8=box+40; int a7=a8-1; int a9=a8+1;
		int a5=box; int a4=a5-1; int a6=a5+1;
		int a2=box-40; int a1=a2-1; int a3=a2+1;
		
		ArrayList<Integer> boxlist = new ArrayList<Integer>();
		
		if(a1 >= 1 && a1 <= 1000) boxlist.add(a1);
		if(a2 >= 1 && a2 <= 1000) boxlist.add(a2);
		if(a3 >= 1 && a3 <= 1000) boxlist.add(a3);
		if(a4 >= 1 && a4 <= 1000) boxlist.add(a4);
		if(a5 >= 1 && a5 <= 1000) boxlist.add(a5);
		if(a6 >= 1 && a6 <= 1000) boxlist.add(a6);
		if(a7 >= 1 && a7 <= 1000) boxlist.add(a7);
		if(a8 >= 1 && a8 <= 1000) boxlist.add(a8);
		if(a9 >= 1 && a9 <= 1000) boxlist.add(a9);
		
		return boxlist;
	}
	
	public List<NodePoint> getFourNodeByBoxId(String boxid) {
		return getFourNodeByBoxId(Integer.parseInt(boxid));
	}
	
	public List<NodePoint> getFourNodeByBoxId(int boxid) {
		List<NodePoint> nps = new ArrayList<NodePoint>();
		
		String lati = getCenterLati(boxid);
		String longi = getCenterLongi(boxid);
		
		double lati_d = Double.parseDouble(lati);
		double longi_d = Double.parseDouble(longi);
		
		// l = left r = right u = up d = down
		double lati_l_u = lati_d +0.005;
		double longi_l_u = longi_d - 0.005;
		
		NodePoint np_l_u = new NodePoint(lati_l_u, longi_l_u);
		
		double lati_r_u = lati_d + 0.005;
		double longi_r_u = longi_d + 0.005;
		
		NodePoint np_r_u = new NodePoint(lati_r_u, longi_r_u);
		
		double lati_r_d = lati_d - 0.005;
		double longi_r_d = longi_d + 0.005;
		
		NodePoint np_r_d = new NodePoint(lati_r_d, longi_r_d);
		
		double lati_l_d = lati_d - 0.005;
		double longi_l_d = longi_d - 0.005;
		
		NodePoint np_l_d = new NodePoint(lati_l_d, longi_l_d);
		
		nps.add(np_l_u);
		nps.add(np_r_u);
		nps.add(np_r_d);
		nps.add(np_l_d);
		nps.add(np_l_u);
		
		return nps;
	}
	
	public String getCenterLati(int boxid) {
		if(boxid == -1)  return "0.0"; 
		
		int row = -1;
		int col = -1;
		if(boxid % 40 == 0) {
			row = boxid / 40;
		} else {
			row = boxid / 40 + 1;
		}
		
		col = boxid - 40*(row - 1);
		
		double lati = 30.15;
		double longi = 120.00;
		
		lati = lati + 0.01 * row - 0.005;
		longi = longi + 0.01 * col - 0.005;
		
		String latStr = String.valueOf(lati);
		if(latStr.length() >= 6) {
			latStr = latStr.substring(0, 6);
		}
		
		return latStr;
	}
	
	public String getCenterLongi(int boxid) {
		if(boxid == -1)  return "0.0"; 
		
		int row = -1;
		int col = -1;
		if(boxid % 40 == 0) {
			row = boxid / 40;
		} else {
			row = boxid / 40 + 1;
		}
		
		col = boxid - 40*(row - 1);
		
		double lati = 30.15;
		double longi = 120.00;
		
		lati = lati + 0.01 * row - 0.005;
		longi = longi + 0.01 * col - 0.005;
		
		String lonStr = String.valueOf(longi);
		if(lonStr.length() >= 7) {
			lonStr = lonStr.substring(0, 7);
		}
		
		return lonStr;
	}
	
	/*public static void main(String[] args) {
		BoxUtil util = new BoxUtil();
		
		List<NodePoint> nps = util.getFourNodeByBoxId(528);
		for (NodePoint nodePoint : nps) {
			System.out.println(nodePoint.getLat() + "  "  + nodePoint.getLon());
		}
		
	}*/
	
	
	/*public static void main(String[] args) {
		BoxUtil util = new BoxUtil();
		ArrayList<Integer> boxlist = (ArrayList<Integer>)util.getNearBoxList(533);
		for (Integer integer : boxlist) {
			System.out.println(integer);
		}
	}*/
	
	
}


