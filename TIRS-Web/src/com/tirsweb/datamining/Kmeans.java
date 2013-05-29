/**
 * 文件名：Kmeans.java
 *
 * 版本信息： version 1.0
 * 日期：May 19, 2013
 * Copyright by menuz
 */
package com.tirsweb.datamining;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.tirsweb.dao.jdbc.DAO5;
import com.tirsweb.model.ParkingLocation;
import com.tirsweb.util.gps.GeoDistance;

/**
 * 
 * 此类描述的是：
 * @author: dmnrei@gmail.com
 * @version: May 28, 2013 10:34:53 AM
 */
public class Kmeans {
	public double[][] sample;
	public double[][] center;
	public int clusterkind, totaln;
	long clusterBeginTime;
	int expireTime;

	public Kmeans(int total, int clusterkd) {
		this.totaln = total;
		this.clusterkind = clusterkd;
		// clusterkind means how many cluster u want to cluster
		center = new double[clusterkind][2];
		sample = new double[totaln][3];
		expireTime = 30;
	}

	// find the nearest cluster of every sample, put idx of cluster in the [2]
	void Calssify() {
		int i, j;
		double dis, district;
		i = 0;
		while (i < totaln) {
			district = 10000;
			for (j = 0; j < clusterkind; j++) {
				 dis = Math.sqrt((sample[i][0] - center[j][0])
						* (sample[i][0] - center[j][0])
						+ (sample[i][1] - center[j][1])
						* (sample[i][1] - center[j][1]));
				
				//dis = GeoDistance.computeCompareDistance(sample[i][0], sample[i][1], center[j][0], center[j][1]);
				if (dis < district) {
					sample[i][2] = j;
					district = dis;
				}
			}
			i++;
		}
	}

	// to see cluster center has been altered, if ater return false,
	boolean centerChanged(double[][] center1, double[][] center2, int clusterkind)// ÅÐ¶ÏÒÀ´Î¼ÆËãµÄ¸÷¸öÀà¾ùÖµÊÇ·ñÓÐ¸Ä±ä
	{
		int cout = 0;
		while (cout < clusterkind) {
			if (center1[cout][0] != center2[cout][0]
					|| center1[cout][1] != center2[cout][1])
				return true;
			cout++;
		}
		return false;
	}

	boolean IsUpdataCenter()
	{
		int i, j;
		int[] kind = new int[clusterkind];
		double[] amoutx = new double[clusterkind];
		double[] amouty = new double[clusterkind];
		double[][] amoutxy = new double[clusterkind][2];
		
		// sample[1][0]=x1 sample[1][1]=y1  sample[1][2]=cluster1
		// sample[2][0]=x2 sample[2][1]=y2  sample[2][2]=cluster2
		// sample[3][0]=x3 sample[3][1]=y3  sample[3][2]=cluster3
		// sample[4][0]=x4 sample[4][1]=y4  sample[4][2]=cluster1
		// get all sample in cluster1, sum all sample.x as amoutX and all sample.y as amountY
		for (i = 0; i < totaln; i++) {
			amoutx[(int) sample[i][2]] += sample[i][0];
			amouty[(int) sample[i][2]] += sample[i][1];
			kind[(int) sample[i][2]]++;
		}
		
		// get new cluster center x=(x1+x2+...+xn)/n, y=(y1+y2+...+yn)/n 
		for (j = 0; j < clusterkind; j++) {
			amoutx[j] /= (double) kind[j];
			amouty[j] /= (double) kind[j];
			amoutxy[j][0] = amoutx[j];
			amoutxy[j][1] = amouty[j];
		}
		
		
		if(!WetherAlter(amoutxy,center,clusterkind))   
	    {   
	        for(i=0;i<clusterkind;i++)   
	        {   
	            center[i][0]=amoutxy[i][0];   
	            center[i][1]=amoutxy[i][1];   
	        }   
	        return false;   
	           
	    }   
	    else   
	        return true; 
		
	}
	
	 boolean WetherAlter(double[][] center1,double[][]center2,int clusterkind)   
	    {   
	        int cout=0;   
	        while(cout<clusterkind)   
	        {   
	            if(center1[cout][0]!=center2[cout][0] || center1[cout][1]!=center2[cout][1])   
	                return false;   
	            cout++;   
	        }   
	            return true;   
	    }   
		

	public void PrintSave() throws java.io.IOException
	{
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
				"Result.txt")));
		Map<Integer, Integer> clusterNum = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < totaln; i++) {
			System.out.println("sample:" + (i + 1) + "  " + sample[i][0] + " "
					+ sample[i][1] + " " + "Class:" + ((int) sample[i][2] + 1)
					+ "\n");
			int clusterKey = (int)sample[i][2];
			
			Integer clusterCount = clusterNum.get(clusterKey);
			if(clusterCount == null) {
				clusterNum.put(clusterKey, 1);
			} else {
				clusterNum.put(clusterKey, clusterCount+1);
			}
			out.println("sample:" + (i + 1) + "  " + sample[i][0] + " "
					+ sample[i][1] + " " + "Class:" + ((int) sample[i][2] + 1)
					+ "\n");
		}
		
		for (int i=0; i < clusterkind; i++) {
			System.out.println("cluster:" + (i + 1) + "  " + center[i][0] + " "
					+ center[i][1] + " "  + clusterNum.get(i) 
					+ "\n");
			out.println("cluster:" + (i + 1) + "  " + center[i][0] + " "
					+ center[i][1] + " "  + clusterNum.get(i) 
					+ "\n");
		}
	}

	public boolean Cluster() throws java.io.IOException {
		clusterBeginTime = new Date().getTime();
		int change = 1;
		int clusterTime = 0;
		while (change == 1 ) {
			if(timeExpired()) {
				return false;
			}
			Calssify();
			if (IsUpdataCenter()) {
				// PrintSave();
				change = 0;
			}
			clusterTime++;
		}
		System.out.println("cluster time = " + clusterTime);
		return true;
	}
	
	/**
	 * 
		 * 此方法描述的是：cluster number is predefined, it can not get the result all the time,
		 * 	            so i set time expire to prevent dead loop, and change cluster number to recalculate.
	     * @return
	     * @version: May 28, 2013 10:35:03 AM
	 */
	boolean timeExpired() {
		long now = new Date().getTime();
		int interval = (int)((now - clusterBeginTime) / 1000);
		if(interval > 20) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

	public static void main(String[] args) throws java.io.IOException {
		DAO5 dao = new DAO5();
		ArrayList<ParkingLocation> pks = new ArrayList<ParkingLocation>();
		dao.getParkingLocationByArcId(pks, 57);
		// test 
		/*
		 * 1 1
			1 2
			2 1
			2 2
			1.5 1.5
			9 9
			9 10
			10 9
			10 10
			9.5 9.5
		 */
		/*ArrayList<ParkingLocation> pks = new ArrayList<ParkingLocation>();
		ParkingLocation pk1 = new ParkingLocation("", "", 1, 1, -1);
		ParkingLocation pk2 = new ParkingLocation("", "", 1, 2, -1);
		ParkingLocation pk3 = new ParkingLocation("", "", 2, 1, -1);
		ParkingLocation pk4 = new ParkingLocation("", "", 2, 2, -1);
		ParkingLocation pk5 = new ParkingLocation("", "", 1.5, 1.5, -1);
		ParkingLocation pk6 = new ParkingLocation("", "", 9, 9, -1);
		ParkingLocation pk7 = new ParkingLocation("", "", 9, 10, -1);
		ParkingLocation pk8 = new ParkingLocation("", "", 10, 9, -1);
		ParkingLocation pk9 = new ParkingLocation("", "", 10, 10, -1);
		ParkingLocation pk10 = new ParkingLocation("", "", 9.5, 9.5, -1);
		pks.add(pk1);pks.add(pk2);pks.add(pk3);pks.add(pk4);pks.add(pk5);
		pks.add(pk6);pks.add(pk7);pks.add(pk8);pks.add(pk9);pks.add(pk10);*/
		// loop until get the result
		int clusterkind = 10;
		while(true) {
			Kmeans kmeans = new Kmeans(pks.size(), clusterkind);
			int cout = 0;
			// init data martix from source file
			while (cout < kmeans.totaln) {
				kmeans.sample[cout][0] = pks.get(cout).getLati();
				kmeans.sample[cout][1] = pks.get(cout).getLongi();
				kmeans.sample[cout][2] = -1;
				if (cout < kmeans.clusterkind) {
					kmeans.center[cout][0] = pks.get(cout).getLati();
					kmeans.center[cout][1] = pks.get(cout).getLongi();
				}
				cout++;
			}
			// run cluster
			if(kmeans.Cluster()) {
				kmeans.PrintSave();
				break;
			} else {
				clusterkind++;
				System.out.println("clusterkind = " + clusterkind);
			}
		}
	}
}