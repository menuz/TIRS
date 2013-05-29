
package com.tirsweb.util.xml;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.tirsweb.model.GPS;
import com.tirsweb.model.Point;

/**
 * 
 * 此类描述的是：
 * @author: dmnrei@gmail.com
 * @version: May 28, 2013 1:57:36 PM
 */
public class RouteXML {
	private Document document;
	private Element body;
	private List<Point> points;

	public RouteXML(List<Point> pointList) {
		document = DocumentHelper.createDocument();
		body = document.addElement("body");
		body.addAttribute("copyright", "All data copyright By Menuz 2013."); 
		
		this.points = pointList;
	}

	@Override
	public String toString() {
		int i=0;
		int len = this.points.size();
		
		if(len == 1) {
			Element element = body.addElement("startpoint");
			
			Point p = this.points.get(0);
			double lat = p.getLat();
			double lon = p.getLon();
			element.addAttribute("lati", lat+"");
			element.addAttribute("longi", lon+"");
			
			element = body.addElement("endpoint");
			element.addAttribute("lati", lat+"");
			element.addAttribute("longi", lon+"");
			return document.asXML();
		}
		
		for (i=0; i<len; i++) {
			Element element = null;
			if(i==0) {
				element = body.addElement("startpoint");
			} else if(i==len-1) {
				element = body.addElement("endpoint");
			} else {
				element = body.addElement("waypoint");
			}
			
			Point p = this.points.get(i);
			double lat = p.getLat();
			double lon = p.getLon();
			element.addAttribute("lati", lat+"");
			element.addAttribute("longi", lon+"");
		}
		
		return document.asXML();
	}
}


