/**
 * 文件名：Test.java
 *
 * 版本信息： version 1.0
 * 日期：May 15, 2013
 * Copyright by menuz
 */
package com.tirsweb.datamining;

import java.util.ArrayList;

public class Test {
	/*public static void main(String[] args) {
		Time t1 = Time.valueOf("16:13:02");
		System.out.println(t1.getHours());
		System.out.println(t1.getMinutes());
		System.out.println(t1.getSeconds());
		
		Time t2 = Time.valueOf("16:13:12");
		System.out.println(t2.getHours());
		System.out.println(t2.getMinutes());
		System.out.println(t2.getSeconds());
		
		long tt1 = t1.getTime();
		long tt2 = t2.getTime();
		
		System.out.println(Math.abs((tt2-tt1)/1000));
	}*/
	
	public static void main(String[] args) {
		ArrayList<Integer> arcList = new ArrayList<Integer>();
		arcList.add(3);
		arcList.add(3);
		arcList.add(3);
		arcList.add(3);
		
		arcList.add(4);
		arcList.add(4);
		arcList.add(4);
		arcList.add(4);
		
		arcList.add(-1);
		arcList.add(-1);
		arcList.add(-1);
		
		arcList.add(5);
		arcList.add(5);
		arcList.add(5);
		arcList.add(5);
		arcList.add(5);
		arcList.add(5);
		
		int arcId = -2;
		double totalDis = 0.0;
		int count = 0;
		int startIdx = 0;
		
		
		for (int i = 0; i < arcList.size(); i++) {
			int arcIdTmp = arcList.get(i);
			if(arcIdTmp != arcId) {
				if(arcId != -2 && arcId != -1) {
					System.out.println(startIdx + "  " + (i-1) + "  " + arcId);
				}
				startIdx = i;
				arcId = arcIdTmp;
			}
		}
		if(arcId != -2 && arcId != -1) {
			System.out.println(startIdx + "  " + (arcList.size()-1) + "  " + arcId);
		}
	}
	
	/*public static void main(String[] args) {
		String arcAndAverageSpeed = "4+20.9";
		String[] temp = arcAndAverageSpeed.split("[+]");
		System.out.println(temp[0]+"  "+temp[1]);
	}*/
	
	/*public static void main(String[] args) {
		double a = 0.0;
		double b = 0.0;
		
		double c = a/b;
		
		if(1 <= c) {
			System.out.println("1<=c");
		} else {
			System.out.println("1>c");
		}
		// System.out.println(c);
	}*/
	
	/*public static void main(String[] args) {
		String str = "1";
		int arcid = 1;
		
		if(str.equals(arcid+"")) {
			System.out.println("1=1");
		} else {
			System.out.println("1!=1");
		}
	}*/
}


