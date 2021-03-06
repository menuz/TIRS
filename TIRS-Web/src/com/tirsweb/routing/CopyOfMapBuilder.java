/**
 * 文件名：MapBuilder.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-3
 * Copyright by menuz
 *//*
package com.tirsweb.routing;

import java.util.Map;
import java.util.Set;

import com.tirsweb.dao.jdbc.RouteDAO;
import com.tirsweb.model.Node;
import com.tirsweb.util.cache.Cache;

public class CopyOfMapBuilder {
	Node[] nodes =  new Node[66+1];
	RouteDAO routeDAO;
	public CopyOfMapBuilder(Cache cache) {
		//init all node
		// routeDAO = new RouteDAO();
		// routeDAO.queryAllNode(nodes);
		
		Map<Integer, Node> nodeList = cache.queryAllNode();
		for(int i=1; i<=66; i++) {
			Node node = nodeList.get(i);
			nodes[node.getId()] = node;
		}
		
		
		//init node relationship
		routeDAO.initNodeRelation(nodes);
	}
	
	public Node build(Set<Node> open, Set<Node> close, int startNodeId, int endNodeId){
		for(int i=1; i<=66; i++) {
			open.add(nodes[i]);
		}
		
		return nodes[startNodeId];
	}
	
	public Node[] getNodeArray() {
		return nodes;
	}
}*/