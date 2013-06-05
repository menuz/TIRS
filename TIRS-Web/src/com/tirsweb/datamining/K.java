/**
 * 文件名：K.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-3
 * Copyright by menuz
 */
package com.tirsweb.datamining;

import java.util.ArrayList;

/**
 * 此类描述的是：
 * @author: dmnrei@gmail.com
 * @version: 2013-6-5 上午9:52:04 
 */
public class K {
	
	public static int ClusterDis = 20;
	int gpsNumber;
	ArrayList<Double> disList;
	
	public K(){
		
	}
	
	public K(int gpsNumber, ArrayList<Double> disList) {
		this.gpsNumber = gpsNumber;
		this.disList = disList;
		
		
	}
	
	public int getK() {
		int totalClusterDis = gpsNumber * ClusterDis;
		
		/**
		 * 
		 */
		for(int i=0; i<disList.size(); i++) {
			if(i==0 || i==1) {
				double distance = disList.get(i);
				if(distance < totalClusterDis) {
					return i+1;
				}
			}
		}
		
		/**
		 * 
		 */
		/*ArrayList<Double> diffList = new ArrayList<Double>();
		for(int i=0; i<disList.size()-1; i++) {
			diffList.add(disList.get(i+1) - disList.get(i));
		}
		
		for(int i=0; i<diffList.size(); i++) {
			double first = diffList.get(i);
			double second = diffList.get(i+1);
			
			double n = first/second;
			if(n < 2) {
				
			} else {
				
			}
		}*/
		
		double max = disList.get(0);
		double min = disList.get(disList.size() - 1);
		
		ArrayList<Double> bzfcList = new ArrayList<Double>();
		for(int i=0; i<disList.size()-1; i++) {
			double sum = 0.0;
			int count = 0;
			for(int j=i; j<disList.size(); j++) {
				double dis = disList.get(j);
				sum += dis;
				count++;
			}
			double average = sum / count;
			double fancha = 0.0;
			for(int j=i; j<disList.size(); j++) {
				fancha += Math.pow(average-disList.get(j), 2);
			}
			fancha = fancha/count;
			double biaozhunfancha = Math.sqrt(fancha);
			
			bzfcList.add(biaozhunfancha);
			
			System.out.println((i+1) + "    "  + biaozhunfancha);
		}
		
		for(int i=0; i<bzfcList.size() - 1; i++) {
			double rate = (bzfcList.get(i) -  bzfcList.get(i+1))/bzfcList.get(i);
			if(rate < 0.5) {
				System.out.println("rate < 0.5 k = " + (i+2));
				return i+2;
			}
		}
		
		return 3;
	}
}


