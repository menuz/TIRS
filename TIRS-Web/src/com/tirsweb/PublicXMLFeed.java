package com.tirsweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tirsweb.dao.DBDAO;
import com.tirsweb.model.GPS;
import com.tirsweb.model.Point;
import com.tirsweb.util.cache.Cache;
import com.tirsweb.util.gps.GpsConverter;
import com.tirsweb.util.xml.GPSXML;

/**
 * Servlet implementation class PublicXMLFeed
 */
@WebServlet("/PublicXMLFeed")
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
		response.setContentType("text/xml"); 
		 
		String v_id = request.getParameter("vehicle_id");
		String v_num = request.getParameter("v_num");
		String time1 = request.getParameter("time1");
		String time2 = request.getParameter("time2");
		
System.out.println("v_id " + v_id + " v_num " + v_num + " time1 " + time1 + " time2 " + time2);
		  
		DBDAO db = new DBDAO();
		
		List<GPS> gpses = null;
		if(time1.equals("00:00:00") || time2.equals("00:00:00")) {
			 gpses = db.queryByVechicleId(v_id, v_num);
    	} else {
    		gpses = db.queryByVechicleId(v_id, v_num, time1, time2);
    	}
		
		GpsConverter gc = new GpsConverter();
		for (GPS gps : gpses) {
			double lat = gps.getLati();
			double lon = gps.getLongi();
			
			Point p = getCache().gpsToMarGPS(lat, lon);
			if(p != null) {
				gps.setLati_correct(p.getLat());
				gps.setLongi_correct(p.getLon());
			}
		}
		
		GPSXML gpsXML = new GPSXML(gpses);  
		
		PrintWriter pw = response.getWriter();  
		pw.write(gpsXML.toString());  
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
