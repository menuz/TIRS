
package com.tirsweb.util.xml;

import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.tirsweb.model.Point;
import com.tirsweb.model.xml.XMLParkingLocationCluster;
import com.tirsweb.util.cache.Cache;

/**
 * 
 * 此类描述的是：core api -- findUplocation create xml file
 * @author: dmnrei@gmail.com
 * @version: May 28, 2013 1:57:36 PM
 */
public class FindUpLocationXML {
	private Document document;
	private Element body;
	
	public FindUpLocationXML(Cache cache, double lati1, double longi1, 
			double lati2, double longi2, ArrayList<XMLParkingLocationCluster> xmlObjectList) {
		document = DocumentHelper.createDocument();
		body = document.addElement("body");
		body.addAttribute("copyright", "All data copyright By Menuz 2013."); 
		
		Element element = body.addElement("startpoint");
		element.addAttribute("lati", lati1+"");
		element.addAttribute("longi", longi1+"");
		
		for(int i=0; i<xmlObjectList.size(); i++) {
			XMLParkingLocationCluster object = xmlObjectList.get(i);
			
			Point p = cache.gpsToMarGPS(object.getLati(), object.getLongi());
			
			element = body.addElement("clusterpoint");
			element.addAttribute("lati", p.getLat()+"");
			element.addAttribute("longi", p.getLon()+"");
			element.addAttribute("arcid", object.getArcId()+"");
			element.addAttribute("nearestidx", object.getNearestIdx()+"");
			element.addAttribute("gpscount", object.getGpsCount()+"");
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


