package com.tirsweb.util;

import java.util.ArrayList;

import com.tirsweb.model.Point;

/**
 * @author roger, from xizhan to shuikou 
 *  从html代码中提取 path经纬度信息
 */
public class GetPathPoint2 {

    public static void main(String[] args) {
        String table = "path";
        String dirid = "1253";
        String line = "30.289241, 120.080797</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: 1673px; top: -4922px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='211'>30.289081, 120.087634</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: 4013px; top: -4758px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='211'>30.288701, 120.093910</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: 5776px; top: -4852px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='211'>30.288919, 120.098639</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: 7382px; top: -4940px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='211'>30.289123, 120.102947</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: 9656px; top: -5084px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='211'>30.289456, 120.109046</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: 11601px; top: -5218px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='211'>30.289767, 120.114263</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: 15351px; top: -5459px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='211'>30.290325, 120.124321</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: 17624px; top: -5624px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='211'>30.290707, 120.130418</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: 20070px; top: -5741px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='211'>30.290978, 120.136979</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: 21342px; top: -5901px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='211'>30.291348, 120.140390</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: 22806px; top: -6549px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='211'>30.292849, 120.144317</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: 26942px; top: -8485px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='211'>30.297333, 120.155411</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: 29879px; top: -9945px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='211'>30.300714, 120.163288";
        line = "30.263033, 120.071983</span></div></div><div class='close-button' jstcache='0'></div></div><div class='kd-bubble tto' jstcache='0' style='display: none;'><div id='tto-cc' jstcache='0'></div><div jsdisplay='tt.show_pointer' class='bubble-point-white' id='tto-ptr' jstcache='164' style='display: none;'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: -9215px; top: 2122px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='163'>30.254860, 120.054088</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: -10935px; top: 3818px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='163'>30.250930, 120.049474</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: -11803px; top: 6582px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='163'>30.244526, 120.047146</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: -11407px; top: 9966px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='163'>30.236685, 120.048208</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: -11739px; top: 10362px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='163'>30.235767, 120.047318</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: -13547px; top: 12474px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='163'>30.230873, 120.042468</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: -15651px; top: 13890px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='163'>30.227591, 120.036825</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: -1615px; top: 1770px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='163'>30.255675, 120.074472</span></div></div><div class='close-button' jstcache='0'></div></div><div class='ftr-misc-latlng-marker' jstcache='0' style='position: absolute; left: -6003px; top: 5282px;'><div jstcache='0'><div jstcache='0'><span jscontent='latlng' jstcache='163'>30.247538, 120.062703";
        ArrayList<ArrayList<Point>> pListList = new ArrayList<ArrayList<Point>>();
        ArrayList<Point> pList = null;
        
        int i=0;
        while (line.indexOf("30.") != -1) {
        	
            int a = line.indexOf("30.");
            String lat = line.substring(a, a + 9);
            int b = line.indexOf("120.");
            String lon = line.substring(b, b + 10);
            
            if(i%8 == 0) {
            	pList = new ArrayList<Point>();
            	pListList.add(pList);
            }
            pList.add(new Point(lat, lon));
            
            System.out.println("insert into " + table
                    + "(directionId,latitude,longitude) VALUES(" + dirid + ","
                    + lat + "," + lon + ");");
            //每次拿掉一个点就剪掉一段
            line = line.substring(b + 10);
            i = i+1;
        }
        System.out.println("i = " + i);
        
        int idx = 0;
        int row = 5;
        int column = 1;
        for(ArrayList<Point> pListTemp : pListList) {
        	for(Point p : pListTemp) {
        		System.out.println("insert into path_test values(" + row + "," + column + "," + p.getLat() + "," + p.getLon() + ");");
        		idx++;
        		column++;
        	}
        	row ++;
        	column = 1;
        }
    }
}
