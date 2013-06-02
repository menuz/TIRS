/**
 * 文件名：Angle.java
 *
 * 版本信息： version 1.0
 * 日期：May 27, 2013
 * Copyright by menuz
 */
package com.tirsweb.util.gps;

/**
 * 
 * 此类描述的是：Used for detecting taxi driving direction and arc direction
 * @author: dmnrei@gmail.com
 * @version: May 27, 2013 2:07:47 PM
 */
public class Angle {
	
	class MyVector{
		public double x;
		public double y;
		
		public MyVector(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
	
	MyVector toVector(double lati1, double longi1, double lati2, double longi2) {
		double x = longi2*1000000 - longi1*1000000;
		double y = lati2*1000000 - lati1*1000000;
		MyVector vector = new MyVector(x, y); 
		return vector;
	}
	
	public MyVector getVector() {
		return null;
	} 
	
	public double angle(double lati1, double longi1, double lati2, double longi2,
			double lati3, double longi3, double lati4, double longi4) {
		MyVector vector1 = toVector(lati1, longi1, lati2, longi2);
		MyVector vector2 = toVector(lati3, longi3, lati4, longi4);
		
		double numerator = vector1.x*vector2.x + vector1.y*vector2.y;
		double denominator = Math.sqrt(vector1.x*vector1.x + vector1.y*vector1.y)*Math.sqrt(vector2.x*vector2.x + vector2.y*vector2.y);
		if(numerator == 0.0 || denominator == 0.0) return 360;
		// System.out.println(numerator + "   " + denominator + "    " + numerator/denominator);
		double angle = Math.acos(numerator/denominator);
		return angle*90*2/Math.PI;
	}
	
	public static void main(String[] args) {
		Angle a = new Angle();
		// vector a
		double lati1 = 30.05;
		double longi1 = 120.00;
		double lati2 = 30.10;
		double longi2 = 120.05;
		
		// vector b  a and b 45
		double lati3 = 30.05;
		double longi3 = 120.10;
		double lati4 = 30.10;
		double longi4 = 120.10;
		
		// vector c a and c 135
		double lati5 = 30.10;
		double longi5 = 120.10;
		double lati6 = 30.05;
		double longi6 = 120.10;
		
		double angle1 = a.angle(lati1, longi1, lati2, longi2, lati3, longi3, lati4, longi4);
		double angle2 = a.angle(lati1, longi1, lati2, longi2, lati5, longi5, lati6, longi6);
		
		System.out.println("angle1 = " + angle1);
		System.out.println("angle2 = " + angle2);
		
		
		lati1 = 30.27906;
		longi1 = 120.1253;
		
		lati2 = 30.27926;
		longi2 = 120.1286;
		
		lati3 = lati1;
		longi3 = longi1;
		
		lati4 = 30.27815;
		longi4 = 120.1277;
		
		angle1 = a.angle(lati1, longi1, lati2, longi2, lati3, longi3, lati4, longi4);
		System.out.println("angle1 = " + angle1);
		
		
		lati4 = 30.27682;
		longi4 = 120.1254;
		
		angle1 = a.angle(lati1, longi1, lati2, longi2, lati3, longi3, lati4, longi4);
		System.out.println("angle1 = " + angle1);
		
		
		lati4 = 30.27733;
		longi4 = 120.1231;
		
		angle1 = a.angle(lati1, longi1, lati2, longi2, lati3, longi3, lati4, longi4);
		System.out.println("angle1 = " + angle1);
		
		
		
	}
}


