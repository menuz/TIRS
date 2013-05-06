/**
 * 文件名：GPSXML.java
 *
 * 版本信息： version 1.0
 * 日期：Apr 22, 2013
 * Copyright by menuz
 */
package com.tirsweb.util.xml;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.tirsweb.model.GPS;

public class GPSXML {
	private Document document;
	private Element body;
	private List<GPS> gpses;

	public GPSXML(List<GPS> gpses) {
		document = DocumentHelper.createDocument();
		body = document.addElement("body");
		body.addAttribute("copyright", "All data copyright By Menuz 2013."); 
		this.gpses = gpses;
	}

	/*
	 * public void addRoute(String tag, String title, String versionId) { route
	 * = body.addElement("route").addAttribute("tag", tag).addAttribute("title",
	 * title).addAttribute("versionId", versionId); }
	 */
	/*
	 * public void addStop(String tag, String title, String shortTitle, String
	 * latitude, String longitude, String stopId) {
	 * route.addElement("stop").addAttribute("tag", tag).addAttribute("title",
	 * title) .addAttribute("shortTitle", shortTitle).addAttribute("lat",
	 * latitude) .addAttribute("lon", longitude).addAttribute("stopId", stopId);
	 * }
	 * 
	 * public void addDirection(String tag, String title) { direction =
	 * route.addElement("direction").addAttribute("tag",
	 * tag).addAttribute("title", title); }
	 * 
	 * public void addStop(String tag) {
	 * direction.addElement("stop").addAttribute("tag", tag); }
	 */

	@Override
	public String toString() {
		for (GPS gps : gpses) {
			Element element = body.addElement("gps");
			Class g = gps.getClass();
			Method[] ms = g.getDeclaredMethods();
			Field[] fs = g.getFields();

			for (Field field : fs) {
				String property = field.getName();
				String methodName = "get";
				char c = Character.toUpperCase(property.charAt(0));
				methodName = methodName + c + property.substring(1);
				try {
					Method sm = g.getMethod(methodName, null);
					// System.out.println(sm.getReturnType().toString());
					// System.out.println(sm.getDefaultValue());
					try {
						Object obj = sm.invoke(gps, null);
						String result = "";
						if(obj != null) {
							result = obj.toString();
						}
						// System.out.println("result = " + result);
						element.addAttribute(property, result);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		return document.asXML();
	}
	
}
