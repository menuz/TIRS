/**
 * 文件名：Box.java
 *
 * 版本信息： version 1.0
 * 日期：May 10, 2013
 * Copyright by menuz
 */
package com.tirsweb.model;


import java.util.List;

import com.tirsweb.util.BoxUtil;

public class Box {
	int boxId;
	double speed;
	int upcount;
	double speedSum;
	int upcountSum;
	// quan zhi
	double weights;
	
	public double getSpeedSum() {
		return speedSum;
	}

	public void setSpeedSum(double speedSum) {
		this.speedSum = speedSum;
	}

	public int getUpcountSum() {
		return upcountSum;
	}

	public void setUpcountSum(int upcountSum) {
		this.upcountSum = upcountSum;
	}

	public Box() {}
	
	public Box(int boxId, double speed, int upcount) {
		super();
		this.boxId = boxId;
		this.speed = speed;
		this.upcount = upcount;
	}
	
	public Box(int boxId, double speed, int upcount, double speedSum,
			int upcountSum) {
		super();
		this.boxId = boxId;
		this.speed = speed;
		this.upcount = upcount;
		this.speedSum = speedSum;
		this.upcountSum = upcountSum;
	}

	public int getBoxId() {
		return boxId;
	}
	public void setBoxId(int boxId) {
		this.boxId = boxId;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public int getUpcount() {
		return upcount;
	}
	public void setUpcount(int upcount) {
		this.upcount = upcount;
	}
	public double getWeight() {
		this.weights = upcount / upcountSum * 0.6 + speed / speedSum * 0.4;
		return this.weights;
	}
	
	public static int getBoxId(double lati, double longi) {
		return getBoxId(lati+"", longi+"");
	}
	
	/**
	 * 
		 * 此方法描述的是：Math.round() make sure that it works right due to double precision problem
	     * @param lati
	     * @param longi
	     * @return currrent located box id
	     * @author: dmnrei@gmail.com
	     * @version: 2013-6-4 下午6:18:21
	 */
	public static int getBoxId(String lati, String longi) {
		if(lati.trim().equals("0.0") || longi.trim().equals("0.0")) {
			return -1;
		}
		
		// System.out.println(lati + "  " + longi);
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		
		if(lati.length() >= 5) lati = lati.substring(0, 5);
		if(longi.length() >= 6)  longi = longi.substring(0, 6);
		
		double lat = Double.valueOf(lati);
		double lon = Double.valueOf(longi);
		
		double minLat = 30.15;
		double minLon = 120.00;
		
		// System.out.println("debug1: " + lat + "  " + lon);
		int row = (int)Math.round((lat - minLat) / 0.01) + 1;
		int col = (int)Math.round((lon - minLon) / 0.01) + 1;
		int box = 40*(row-1) + col;
		
		
		// System.out.println("debug2: " + row + "  " + col);
		if(row <= 0 || col <= 0)  return -1;
		if(row >= 25 || col >= 40) return -1;

		return box;
	}
	
	public static void main(String[] args) {
		BoxUtil util = new BoxUtil();
		List<NodePoint> nps = util.getFourNodeByBoxId(528);
		for (NodePoint nodePoint : nps) {
			System.out.println(nodePoint.getLat() + "  " + nodePoint.getLon());
		}
		
	}
	
}


