//package com.tirsweb.util.xml;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.dom4j.Document;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;
//
//import com.tirsweb.datamining.Kmeans;
//import com.tirsweb.model.Point;
//import com.tirsweb.util.cache.Cache;

/**
 * 
 * 此类描述的是：kmeans produce result, clusterxml will get data from kmeans and produce xml 
 * @author: dmnrei@gmail.com
 * @version: May 28, 2013 1:57:55 PM
 */
//public class ClusterXML {
//	private Document document;
//	private Element body;
//	private Kmeans kmeans;
//	private Cache cache;
//	
//	public ClusterXML(Kmeans kmeans, Cache cache) {
//		document = DocumentHelper.createDocument();
//		body = document.addElement("body");
//		body.addAttribute("copyright", "All data copyright By Menuz 2013."); 
//		this.kmeans = kmeans;
//		this.cache = cache;
//	}
//
//	@Override
//	public String toString() {
//		Map<Integer, Integer> clusterNum = new HashMap<Integer, Integer>();
//		
//		for (int i = 0; i < kmeans.totaln; i++) {
//			/*System.out.println("sample:" + (i + 1) + "  " + kmeans.sample[i][0] + " "
//					+ kmeans.sample[i][1] + " " + "Class:" + ((int) kmeans.sample[i][2] + 1)
//					+ "\n");*/
//			Element element = body.addElement("gps");
//			element.addAttribute("id", (i+1)+"");
//			
//			double lati = kmeans.sample[i][0];
//			double longi = kmeans.sample[i][1];
//			
//			Point p = this.cache.gpsToMarGPS(lati, longi);
//			element.addAttribute("lati", p.getLat()+"");
//			element.addAttribute("longi", p.getLon()+"");
//			
//			element.addAttribute("clusterindex", kmeans.sample[i][2]+"");
//			
//			int clusterKey = (int)kmeans.sample[i][2];
//			
//			Integer clusterCount = clusterNum.get(clusterKey);
//			if(clusterCount == null) {
//				clusterNum.put(clusterKey, 1);
//			} else {
//				clusterNum.put(clusterKey, clusterCount+1);
//			}
//		}
//		
//		for (int i=0; i < kmeans.clusterkind; i++) {
//			/*System.out.println("cluster:" + (i + 1) + "  " + kmeans.center[i][0] + " "
//					+ kmeans.center[i][1] + " "  + clusterNum.get(i) 
//					+ "\n");*/
//			Element element = body.addElement("cluster");
//			element.addAttribute("id", (i+1)+"");
//			
//			double lati = kmeans.sample[i][0];
//			double longi = kmeans.sample[i][1];
//			
//			Point p = this.cache.gpsToMarGPS(lati, longi);
//			element.addAttribute("lati", p.getLat()+"");
//			element.addAttribute("longi", p.getLon()+"");
//			
//			element.addAttribute("clusternumber", + clusterNum.get(i)+"");
//		}
//		
//		return document.asXML();
//	}
//}*/
//
//
