/**
 * 文件名：ClusterInfo.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-3
 * Copyright by menuz
 */
package com.tirsweb.datamining.cluster;

import com.tirsweb.datamining.cluster.kMeansPoint;
import com.tirsweb.util.gps.GeoDistance;

public class ClusterInfo {
	int clusterId;
	int pointNum;
	double aveargeDis;
	double totalDis;
	
	double lati;
	double longi;
	
	public ClusterInfo(int clusterId, double lati, double longi) {
		this.clusterId = clusterId;
		this.lati = lati;
		this.longi = longi;
		
		totalDis = 0.0;
		aveargeDis = 0.0;
	}
	
	public void addKMeanPoint(kMeansPoint kmPoint) {
		double dis = GeoDistance.computeCompareDistance(lati, longi, kmPoint.getX(), kmPoint.getY());
		totalDis += dis;
		pointNum ++;
	}
	
	public String toString() {
		this.aveargeDis = totalDis/pointNum;
		if(pointNum == 0) {
			this.aveargeDis = -1;
		}
		return "[clusterId:" + clusterId + ", pointNum:" + pointNum + ", averageDis:" + this.aveargeDis +", totalDis:" + this.totalDis + "]";
	}
	
	public double getTotalDistance() {
		return this.totalDis;
	}
	
	public int getGpsCount() {
		return pointNum;
	}
}


