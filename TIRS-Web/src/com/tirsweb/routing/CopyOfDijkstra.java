/**
 * 文件名：Dijkstra.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-3
 * Copyright by menuz
 *//*
package com.tirsweb.routing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tirsweb.model.Node;

public class CopyOfDijkstra {
	Set<Node> open=new HashSet<Node>();
	Set<Node> close=new HashSet<Node>();
	
	Map<Integer,Integer> path=new HashMap<Integer,Integer>();//封装路径距离
	Map<Integer,String> pathInfo=new HashMap<Integer,String>();//封装路径信息
	
	Node[] nodes = null;
	
	public CopyOfDijkstra(Node[] nodes) {
		this.nodes = nodes;
	}
	
	public String findShortPath(int startNodeId, int endNodeId) {
		return "";
	}
	
	public Node initPath(int startNodeId){
		//初始路径,因没有A->E这条路径,所以path(E)设置为Integer.MAX_VALUE
		Node startNode = this.nodes[startNodeId];
		Map<Node, Integer> childs = startNode.getChild();
		
		// fill neighboring nodes
		for (Node node : childs.keySet()) {
			int len = childs.get(node);
			path.put(node.getId(), len);
			pathInfo.put(node.getId(), startNodeId+"->"+node.getId());
		}
		
		// fill non-neighboring nodes
		for (int i=1; i<=66; i++) {
			Node node = nodes[i];
			Integer len = path.get(node.getId());
			if(len == null) {
				path.put(node.getId(), Integer.MAX_VALUE);
				pathInfo.put(node.getId(), startNodeId+"");
			}
		}
		
		// nodes are init in open set except startNodeId
		for(int i=1; i<=66; i++) {
			if(i==startNodeId) continue;
			open.add(nodes[i]);
		}
		
		close.add(startNode);
		return startNode;
	}
	
	public void computePath(Node start){
		Node nearest=getShortestPath(start);//取距离start节点最近的子节点,放入close
		if(nearest==null){
			return;
		}
		close.add(nearest);
		open.remove(nearest);
		Map<Node,Integer> childs=nearest.getChild();
		for(Node child:childs.keySet()){
			if(open.contains(child)){//如果子节点在open中
				Integer newCompute=path.get(nearest.getId())+childs.get(child);
				if(path.get(child.getId())>newCompute){//之前设置的距离大于新计算出来的距离
					path.put(child.getId(), newCompute);
					pathInfo.put(child.getId(), pathInfo.get(nearest.getId())+"->"+child.getId());
				}
			}
		}
		computePath(start);//重复执行自己,确保所有子节点被遍历
		computePath(nearest);//向外一层层递归,直至所有顶点被遍历
	}
	
	public void printPathInfo(){
		Set<Map.Entry<Integer, String>> pathInfos=pathInfo.entrySet();
		for(Map.Entry<Integer, String> pathInfo:pathInfos){
			System.out.println(pathInfo.getKey()+":"+pathInfo.getValue());
		}
	}
	
	*//**
	 * 获取与node最近的子节点
	 *//*
	private Node getShortestPath(Node node){
		Node res=null;
		int minDis=Integer.MAX_VALUE;
		Map<Node,Integer> childs=node.getChild();
		for(Node child:childs.keySet()){
			if(open.contains(child)){
				int distance=childs.get(child);
				if(distance<minDis){
					minDis=distance;
					res=child;
				}
			}
		}
		return res;
	}
	
	public static void main(String[] args) {
		MapBuilder mapBuilder = new MapBuilder();
		Node[] nodes = mapBuilder.getNodeArray();
		
		CopyOfDijkstra test = new CopyOfDijkstra(nodes);

		int startNodeId = 27;
		Node start = test.initPath(startNodeId);
		test.computePath(start);
		test.printPathInfo();
	}
}

*/