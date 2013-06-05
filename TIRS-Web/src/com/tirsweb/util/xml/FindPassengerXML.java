
package com.tirsweb.util.xml;

import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.tirsweb.model.Point;
import com.tirsweb.model.xml.XMLParkingLocationCluster;
import com.tirsweb.model.xml.XMLPoint;

/**
 * 
 * 此类描述的是：core api -- findUplocation create xml file
 * @author: dmnrei@gmail.com
 * @version: May 28, 2013 1:57:36 PM
 */
public class FindPassengerXML {
	private Document document;
	private Element body;
	
	public FindPassengerXML(double lati1, double longi1,
			 ArrayList<XMLPoint> points, double lati2, double longi2) {
		document = DocumentHelper.createDocument();
		body = document.addElement("body");
		body.addAttribute("copyright", "All data copyright By Menuz 2013."); 
		
		Element element = body.addElement("startpoint");
		element.addAttribute("lati", lati1+"");
		element.addAttribute("longi", longi1+"");
		
		for(int i=0; i<points.size(); i++) {
			XMLPoint object = points.get(i);
			element = body.addElement("intervalpoint");
			element.addAttribute("lati", object.getLat()+"");
			element.addAttribute("longi", object.getLon()+"");
			element.addAttribute("arcid", object.getArcid()+"");
			element.addAttribute("idx", object.getIdx()+"");
		}
		
		element = body.addElement("endpoint");
		element.addAttribute("lati", lati2+"");
		element.addAttribute("longi", longi2+"");
	}

	@Override
	public String toString() {
		return document.asXML();
	}
}


