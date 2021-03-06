/**
 * 文件名：MapInfo.java
 *
 * 版本信息： version 1.0
 * 日期：May 16, 2013
 * Copyright by menuz
 */
package com.tirsweb.network;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.tirsweb.dao.MapInfoDAO;
import com.tirsweb.model.Node;
import com.tirsweb.model.NodePoint;

/**
 * 
 * 此类描述的是：路径规划需要使用道路网络信息，利用path_test表提取信息生成道路网络信息，
 *            产生sql语句。其中path_test中的数据通过Google Map API获取。
 * @author: dmnrei@gmail.com
 * @version: 2013-6-3 上午8:44:03
 */
public class NetworkCreator {
	
	public static void main(String[] args) {
		MapInfoDAO dao = new MapInfoDAO();
		ArrayList<NodePoint> nodeList = dao.getpath_test();
		ArrayList<Node> allNode = new ArrayList<Node>();
		
		for(int i=1; i<=56; i++) {
			NodePoint nodePoint = nodeList.get(i-1);
			int nodeId = i;
			double lati = nodePoint.getLat();
			double longi = nodePoint.getLon();
			
			Node node = new Node(i);
			node.setLati(lati);
			node.setLongi(longi);
			
			int l = i - 1;
			int r = i + 1;
			int u = i - 14;
			int d = i + 14;
			
			if(l >=1 && l <=56) {
				if(i%14 != 1) {
					node.getChild().put(new Node(l), -1.0);
				}
			}
			
			if(r >=1 && r <=56) {
				if(i%14 != 0) {
					node.getChild().put(new Node(r), -1.0);
				}
			}
			
			if(u >=1 && u <=56) {
				node.getChild().put(new Node(u), -1.0);
			}
			
			if(d >=1 && d <=56) {
				node.getChild().put(new Node(d), -1.0);
			}
			
			if(i == 43) {
				node.getChild().put(new Node(57), -1.0);
			}
			
			allNode.add(node);
		}
		
		for(int i=57; i<=64; i++) {
			NodePoint nodePoint = nodeList.get(i-1);
			int nodeId = i;
			double lati = nodePoint.getLat();
			double longi = nodePoint.getLon();
			
			Node node = new Node(i);
			node.setLati(lati);
			node.setLongi(longi);
			
			if(i == 57) {
				node.getChild().put(new Node(43), -1.0);
				node.getChild().put(new Node(58), -1.0);
				node.getChild().put(new Node(65), -1.0);
			} else if (i==58) {
				node.getChild().put(new Node(57), -1.0);
				node.getChild().put(new Node(59), -1.0);
				node.getChild().put(new Node(66), -1.0);
			} else if(i == 64) {
				node.getChild().put(new Node(63), -1.0);
			} else {
				node.getChild().put(new Node(i+1), -1.0);
				node.getChild().put(new Node(i-1), -1.0);
			}
			
			allNode.add(node);
		}
		
		for(int i=65; i<=66; i++) {
			NodePoint nodePoint = nodeList.get(i-1);
			int nodeId = i;
			double lati = nodePoint.getLat();
			double longi = nodePoint.getLon();
			
			Node node = new Node(i);
			node.setLati(lati);
			node.setLongi(longi);
			
			if(i == 65) {
				node.getChild().put(new Node(57), -1.0);
				node.getChild().put(new Node(66), -1.0);
			} else if(i == 66) {
				node.getChild().put(new Node(65), -1.0);
				node.getChild().put(new Node(58), -1.0);
				node.getChild().put(new Node(60), -1.0);
			}
			
			allNode.add(node);
		}
		
		// create tb_node sql
		for (Node node : allNode) {
			String sql = "insert into tb_node values(" + node.getId() + ", " + node.getLati() + ", " + node.getLongi() + ");";
			// System.out.println(sql);
		}
		
		// create node to node sql
		int i=1;
		for (Node node : allNode) {
			
			Map<Node, Double> child = node.getChild();
			Set<Node> set = child.keySet();
			for (Node node2 : set) {
				String sql = "insert into tb_arc values(" + i + ", " + node.getId() + ", " + node2.getId() + ", 0);";
				System.out.println(sql);
				i++;
			}
		}
	}
}


