
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
public class ErrorXML {
	public static int BOXIDERROR = 1;
	public static int NearARCLISTERROR = 2;
	public static int BOXNOARCLISTERROR = 3;
	public static int ARCPARKINGLOCATIONCLUSTERERROR = 4;
	public static int CLUSTERNOTFOUND = 5;
	
	/**
	 * route schedule
	 */
	public static int ARCNOTFOUND = 6; 
	
	private Document document;
	private Element body;

	public ErrorXML() {
		document = DocumentHelper.createDocument();
		body = document.addElement("body");
		body.addAttribute("copyright", "All data copyright By Menuz 2013."); 
	}
	
	public String errorType(int type) {
		Element element = body.addElement("error");
		if(type == BOXIDERROR) {
			element.addAttribute("erorrtype", type + "");
			element.addAttribute("msg", "超过box，box＝－1");
		} else if(type == NearARCLISTERROR) {
			element.addAttribute("erorrtype", type + "");
			element.addAttribute("msg", "没找到最近的街道，box＝－1");
		} else if(type == BOXNOARCLISTERROR) {
			element.addAttribute("erorrtype", type + "");
			element.addAttribute("msg", "box没有街道存在，box＝－1");
		} else if(type == ARCPARKINGLOCATIONCLUSTERERROR) {
			element.addAttribute("erorrtype", type + "");
			element.addAttribute("msg", "该街道不存在聚簇点");
		} else if(type == CLUSTERNOTFOUND) {
			element.addAttribute("erorrtype", type + "");
			element.addAttribute("msg", "该街道不存在聚簇点");
		} else if(type == ARCNOTFOUND) {
			element.addAttribute("erorrtype", "routeschedule " + type + " ");
			element.addAttribute("msg", "找不到就近的Arc");
		}
		
		return document.asXML();
	}

	/*@Override
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
	}*/
}


