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
	
	public static void main(String[] args) {
		BoxUtil util = new BoxUtil();
		
		List<NodePoint> nps = util.getFourNodeByBoxId(528);
		for (NodePoint nodePoint : nps) {
			System.out.println(nodePoint.getLat() + "  "  + nodePoint.getLon());
		}
		
	}
	
	
}


