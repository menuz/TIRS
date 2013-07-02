package com.tirsweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.tirsweb.dao.APIDAO;
import com.tirsweb.dao.JCacheDAO;
import com.tirsweb.datamining.cluster.kMeans;
import com.tirsweb.datamining.cluster.kMeansPoint;
import com.tirsweb.model.Arc;
import com.tirsweb.model.Node;
import com.tirsweb.model.NodePoint;
import com.tirsweb.model.ParkingLocation;
import com.tirsweb.model.Point;
import com.tirsweb.util.APIParser;
import com.tirsweb.util.APIVar;
import com.tirsweb.util.BoxUtil;
import com.tirsweb.util.cache.Cache;
import com.tirsweb.util.xml.ClusterXML2;

/**
 * Servlet implementation class PublicXMLFeed
 */
public class PublicXMLFeed extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cache cache = null;

    /**
     * Default constructor. 
     */
    public PublicXMLFeed() {
    	
    }
    
    private Cache getCache() {
		ServletContext servletContext=this.getServletContext();
		Cache cache = (Cache)servletContext.getAttribute("cache");
		if(cache == null) {
			cache = new Cache();
			servletContext.setAttribute("cache", cache);
		}
		return cache;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("GBK");
		response.setContentType("text/xml");    
		response.setCharacterEncoding("GBK");
		 
		String query_string = request.getQueryString();
		
		// 解析url变量
		APIParser apiParser = new APIParser(query_string);
		Map<String, String> keyValueArr = apiParser.getParseResult();
		
		String cmd = keyValueArr.get(APIVar.CMD);
		PrintWriter out = response.getWriter();  
		
		APIHandler apiHandler = new APIHandler(getCache());
		
		System.out.println("cmd = " + cmd);
		
		if(cmd.equals(APIVar.TRAJECTORY)) 
		{
			String time1 = new String(request.getParameter("time1").getBytes("ISO-8859-1"), "utf-8");
			String time2 = new String(request.getParameter("time2").getBytes("ISO-8859-1"), "utf-8");
			out.println(apiHandler.trajectory(keyValueArr.get("vehicle_id"), keyValueArr.get("v_num"), 
					time1, time2));   
		}
		
		else if(cmd.equals(APIVar.FINDUPLOCATION)) {
			String time = new String(request.getParameter("time").getBytes("ISO-8859-1"), "utf-8");
			out.println(apiHandler.finduplocation(keyValueArr.get("lati"), keyValueArr.get("longi"), 
					time));
		}
		
		else if(cmd.equals(APIVar.FINDPASSENGER)) 
		{
			String time = new String(request.getParameter("time").getBytes("ISO-8859-1"), "utf-8");
			out.println(apiHandler.findpassenger(keyValueArr.get("lati"), keyValueArr.get("longi"), 
					time));
		}
		
		else if(cmd.equals(APIVar.ROUTESCHEDULE)) 
		{
			String time = new String(request.getParameter("time").getBytes("ISO-8859-1"), "utf-8");
			out.println(apiHandler.routeschedule(keyValueArr.get("lati1"),keyValueArr.get("longi1"), 
					keyValueArr.get("lati2"), keyValueArr.get("longi2"), time));
		}
		
		else if ("line".equalsIgnoreCase(cmd)) {
			String content = new String(request.getParameter("content")
					.getBytes("ISO-8859-1"), "utf-8");
System.out.println("content = " + content);
			 // get trip from tb_trip to display on the map
			 // freetrip+tripid+vehicleid+rownum
			 // freetrip+20111201231249+20999+1

			 // collect node and arc info, use nodelist to check the node, apparently it will be less useful after collecting
			 // nodelist+dirId
			 // nodelist+4

			 // arcpoint use to check parking location in arc, arcpoint+1 means query all parking location in ssssarc_id = 1 
			 // arcpoint+arcid
			 // arcpoint+1

			// box use to display box in map  and display the arc in box
			// box+boxid
			// box+528
              
             // arcdetail use to display arc's detail indeed
			 // arcdetail+arcid 
             // arcdetail+1

			 // route display node list composed of a route
			 // route+routeSeq
			 // route+1->2->3
			 String[] strs = content.split("[+]");		    
			 for (String string : strs) {
				System.out.println(string);
			}
			
			 ArrayList<NodePoint> points = null;
			 APIDAO dao = new APIDAO();
			 if(strs[0].equals("freetrip")) {

System.out.println("1 = " + strs[1] + " 2 = " + strs[2] + " 3 = " + strs[3]);

				 ArrayList<String> notes = dao.getFreeTrip(strs[1], strs[2], strs[3]);
				 
				 for (String note : notes) {
						String[] gpses = note.split(",");
						
						int i=0;
						points = new ArrayList<NodePoint>();
						for(i=0; i<gpses.length; i++) {
							String gps = gpses[i];
							int idx = gps.indexOf('+');
							String latitude = gps.substring(0, idx);
							String longitude = gps.substring(idx + 1);
							NodePoint p = new NodePoint(latitude, longitude);
							points.add(p);
						}
				}
				 
			 } else if(strs[0].equals("nodelist")) {
				 if(strs[1].equals("all")) {
					 points = dao.getAllNode();
				 } else {
					 points = dao.getpathbyid(strs[1]);
				 }
			 } else if(strs[0].equals("arcpoint")) {
				 points = dao.getArcParkingLocationList(strs[1]);
				 /**
				  * node to gps correct
				  *//*
				 for (NodePoint nodePoint : points) {
					Point p = getCache().gpsToMarGPS(nodePoint.getLat(), nodePoint.getLon());
					if(p!=null) {
						nodePoint.setLat(p.getLat());
						nodePoint.setLon(p.getLon());
					}
				}*/
				 
			 } 
			 
			 else if(strs[0].equals("onearc")) {
				 points = dao.getArcByArcId(strs[1]);
			 } 
		
			 
			 else if(strs[0].equals("onearcdetail")) {
				 points = dao.getArcDetailByArcId(strs[1]);
			 } 
			 
			 else if(strs[0].equals("arc")) {
				 points = dao.getArcDetailByArcId(strs[1]);
			 } 
			 
			 else if(strs[0].equals("arcdetail")) {
				 points = dao.getArcDetailByArcId(strs[1]);
			 }
			 
			 else if(strs[0].equals("box")) {
				 points = dao.getCorrespondingArcListByBoxId(strs[1]);
			 }
			 
			 // len1 correct
			 // 1->2->3->4->5->19->20->34->35->36->37->38->39->25->26->27->28
			 
			 // len2 wrong
			 // 1->2->3->4->5->6->7->8->9->10->11->25->39->40->41->42->28
			 
			 // len3 correct
			 // 1->2->3->4->5->19->20->34->35->36->37->38->39->25->26->27->28
			 else if(strs[0].equals("route")) {
				 String[] nodeList = strs[1].split("->");
				 points = new ArrayList<NodePoint>();
				 for(String nodeId : nodeList) {
					 Node node = getCache().queryNodeByNodeId(nodeId);
					 NodePoint np = new NodePoint(node.getId(), node.getLati(), node.getLongi());
					 points.add(np);
				 }
			 }

             Document document = DocumentHelper.createDocument();
             Element body = document.addElement("body");
             body.addAttribute("copyright", "All data copyright ZJUT 2011.");

             Element path = body.addElement("path");
             path.addAttribute("dirtag", "");

             if (points != null) {
                 for (int i = 0; i < points.size(); i++) {
                     Element point = path.addElement("point");
                     double lat = points.get(i).getLat();
                     double lon = points.get(i).getLon();
                     double lat_correct=lat, lon_correct=lon;
                     
                     if(!strs[0].equals("onearcdetail")) {
                    	 if(strs[0].equals("arcdetail")) {
                    		 
                    	 } else {
							Point p = getCache().gpsToMarGPS(lat, lon);
							if (p != null) {
								lat_correct = p.getLat();
								lon_correct = p.getLon();
							}
                    	 }
                     }
                     
                     // arc detail info is correct data, so i need to re-correct data to origin
                     if(strs[0].equals("arcdetail")) {
                    	 Point p = getCache().marGpsToGps(lat_correct, lon_correct);  
                    	 if(p != null) {
                    		 lat = p.getLat();
                    		 lon = p.getLon();
                    	 }
                    	 point.addAttribute("id", strs[1]);
                      }
                     
                     if(strs[0].equals("box") || strs[0].equals("route") || strs[0].equals("nodelist")) {
                    	 point.addAttribute("id", points.get(i).getId()+"");
                      }
                     
                     point.addAttribute("latitude",String.valueOf(lat));
                     point.addAttribute("longitude",String.valueOf(lon));
                     point.addAttribute("latitude_correct",String.valueOf(lat_correct));
                     point.addAttribute("longitude_correct",String.valueOf(lon_correct));
                 }
             }
             
             // box has two type return  box and arc(only start and end point)
             // here return the box four nodes
             if(strs[0].equals("box")) {
            	 BoxUtil boxUtil = new BoxUtil();
            	 List<NodePoint> nps = boxUtil.getFourNodeByBoxId(strs[1]);
            	 
            	 for (NodePoint nodePoint : nps) {
            		 Element point = path.addElement("node");
                     double lat = nodePoint.getLat();
                     double lon = nodePoint.getLon();
                     double lat_correct=lat, lon_correct=lon;
                     
					Point p = getCache().gpsToMarGPS(lat, lon);
					if (p != null) {
						lat_correct = p.getLat();
						lon_correct = p.getLon();
					}
                     
					 point.addAttribute("id", strs[1]);
                     point.addAttribute("latitude",String.valueOf(lat));
                     point.addAttribute("longitude",String.valueOf(lon));
                     point.addAttribute("latitude_correct",String.valueOf(lat_correct));
                     point.addAttribute("longitude_correct",String.valueOf(lon_correct));
				}
             }
             out.println(document.asXML());
	     }
		
		// Arc_Query
		else if ("arc".equalsIgnoreCase(cmd)) {
			APIDAO dao = new APIDAO();
            
            ArrayList<Arc> points = dao.getarcbyid();

            Document document = DocumentHelper.createDocument();
            Element body = document.addElement("body");
            body.addAttribute("copyright", "All data copyright ZJUT 2011.");

            Element path = body.addElement("arc");

            if (points != null) {
                for (int i = 0; i < points.size(); i++) {
                    Element point = path.addElement("point");
                    int id = points.get(i).getId();
                    double lat1 = points.get(i).getLati1();
                    double lon1 = points.get(i).getLongi1();
                    double lat2 = points.get(i).getLati2();
                    double lon2 = points.get(i).getLongi2();
                    int sni = points.get(i).getStart_node_id();
                    int eni = points.get(i).getEnd_node_id();
                    
                    Point p1 = getCache().gpsToMarGPS(lat1, lon1);
                    Point p2 = getCache().gpsToMarGPS(lat2, lon2);
                    
        			if(p1 != null) {
        				lat1 = p1.getLat();
        				lon1 = p1.getLon();
        			}
        			
        			if(p2 != null) {
        				lat2 = p2.getLat();
        				lon2 = p2.getLon();
        			}
                    
        			point.addAttribute("id", String.valueOf(id));
        			point.addAttribute("start_node_id", String.valueOf(sni));
        			point.addAttribute("end_node_id", String.valueOf(eni));
                    point.addAttribute("lati1",String.valueOf(lat1));
                    point.addAttribute("longi1",String.valueOf(lon1));
                    point.addAttribute("lati2",String.valueOf(lat2));
                    point.addAttribute("longi2",String.valueOf(lon2));
                }
            }
            out.println(document.asXML());
	     }
		
		// push arc detail to db from map.jsp
		else if ("arcpush".equalsIgnoreCase(cmd)) {
			  
			String arcid = keyValueArr.get("arcid");
			String latStr =  new String(request.getParameter("latstr").getBytes("ISO-8859-1"), "utf-8");
			String lonStr =  new String(request.getParameter("lonstr").getBytes("ISO-8859-1"), "utf-8");
			
			String[] latArray = latStr.split(",");
			String[] lonArray = lonStr.split(",");
			
			APIDAO dao = new APIDAO();
			
			for(int i=0; i<latArray.length; i++){
				dao.insertArcDetail(Integer.parseInt(arcid), 
						Double.parseDouble(latArray[i]), 
						Double.parseDouble(lonArray[i]), 
						i);
			}
	     }
		
		// kmeans-cluster
		/*else if ("cluster".equalsIgnoreCase(cmd)) {
			DAO5 dao = new DAO5();
			ArrayList<ParkingLocation> pks = new ArrayList<ParkingLocation>();
			dao.getParkingLocationByArcId(pks, 57);
			
			// loop until get the result
			int clusterkind = 3;
			Kmeans kmeans = null;
			while(true) {
				kmeans = new Kmeans(pks.size(), clusterkind);
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
					kmeans.Prin)tSave();
					break;
				} else {
					clusterkind++;
				}
			}
			
			ClusterXML clusterXML = new ClusterXML(kmeans, getCache());
			out.println(clusterXML);
	     }*/
		
		// cluster used for display clustering algorithm result
		// return point type: parking place in arc and the cluster point in arc
		else if ("cluster".equalsIgnoreCase(cmd)) {
			String content = new String(request.getParameter("content").getBytes("ISO-8859-1"), "utf-8");
			String[] temp = content.split("[+]");
			
			JCacheDAO dao = new JCacheDAO();
			ArrayList<ParkingLocation> pks = new ArrayList<ParkingLocation>();
			//58-62 
			
			String arcid = temp[0];
			String clusterK = temp[1];
			int k = Integer.parseInt(clusterK);
			dao.getParkingLocationByArcId(pks, temp[0]);
			
			List<kMeansPoint> ps = new ArrayList<kMeansPoint>();
			for (ParkingLocation parkingLocation : pks) {
				kMeansPoint p = new kMeansPoint(parkingLocation.getLati(), parkingLocation.getLongi());
				ps.add(p);
			}
			
			if(ps == null || ps.size() == 0) {
				out.println("no point in the arc");  
			} else {  
				kMeans km = new kMeans(k, ps);  
  
				km.runKMeans();

				ClusterXML2 cluster = new ClusterXML2(km, getCache());
				out.println(cluster);
			}
			/*System.out.println(km.getCluster(0).getMean());
			System.out.println(km.getCluster(1).getMean());
			System.out.println(km.getCluster(2).getMean());
			System.out.println(km.getCluster(3).getMean());*/
	     }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
