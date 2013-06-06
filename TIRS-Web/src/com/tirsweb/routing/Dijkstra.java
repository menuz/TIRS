/**
 * 文件名：Dijkstra.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-3
 * Copyright by menuz
 */
package com.tirsweb.routing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tirsweb.model.Node;
import com.tirsweb.util.cache.Cache;

/**
 * 
 * 此类描述的是：路由选择核心类
 * @author: dmnrei@gmail.com
 * @version: 2013-6-6 下午7:34:00
 */
public class Dijkstra {
	Set<Node> open=new HashSet<Node>();
	Set<Node> close=new HashSet<Node>();
	
	Map<Integer,Double> path=new HashMap<Integer,Double>();//封装路径距离
	Map<Integer,String> pathInfo=new HashMap<Integer,String>();//封装路径信息
	
	Node[] nodes = null;
	
	public Dijkstra(Cache cache) {
		this.nodes = cache.getNodeArray();
	}
	
	private Node initPath(int startNodeId){
		//初始路径,因没有A->E这条路径,所以path(E)设置为Integer.MAX_VALUE
		Node startNode = this.nodes[startNodeId];
		Map<Node, Double> childs = startNode.getChild();
		
		// fill neighboring nodes
		for (Node node : childs.keySet()) {
			Double len = childs.get(node);
			path.put(node.getId(), len);
			pathInfo.put(node.getId(), startNodeId+"->"+node.getId());
		}
		
		// fill non-neighboring nodes
		for (int i=1; i<=66; i++) {
			Node node = nodes[i];
			Double len = path.get(node.getId());
			if(len == null) {
				path.put(node.getId(), Double.MAX_VALUE);
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
	
	private void computePath(Node start){
		Node nearest=getShortestPath(start);//取距离start节点最近的子节点,放入close
		if(nearest==null){
			return;
		}
		close.add(nearest);
		open.remove(nearest);
		Map<Node,Double> childs=nearest.getChild();
		for(Node child:childs.keySet()){
			if(open.contains(child)){//如果子节点在open中
				Double newCompute=path.get(nearest.getId())+childs.get(child);
				if(path.get(child.getId())>newCompute){//之前设置的距离大于新计算出来的距离
					path.put(child.getId(), newCompute);
					pathInfo.put(child.getId(), pathInfo.get(nearest.getId())+"->"+child.getId());
				}
			}
		}
		computePath(start);//重复执行自己,确保所有子节点被遍历
		computePath(nearest);//向外一层层递归,直至所有顶点被遍历
	}
	
	private void printPathInfo(){
		Set<Map.Entry<Integer, String>> pathInfos=pathInfo.entrySet();
		for(Map.Entry<Integer, String> pathInfo:pathInfos){
			System.out.println(pathInfo.getKey()+":"+pathInfo.getValue());
		}
	}
	
	/**
	 * 获取与node最近的子节点
	 */
	private Node getShortestPath(Node node){
		Node res=null;
		Double minDis=Double.MAX_VALUE;
		Map<Node,Double> childs=node.getChild();
		for(Node child:childs.keySet()){
			if(open.contains(child)){
				Double distance=childs.get(child);
				if(distance<minDis){
					minDis=distance;
					res=child;
				}
			}
		}
		return res;
	}
	
	/**
	 * 
		 * 此方法描述的是： 
	     * @param startNodeId
	     * @param endNodeId
	     * @return
	     * @author: dmnrei@gmail.com
	     * @version: 2013-6-5 下午8:17:50
	 */
	public ArrayList<Integer> findShortestPath(int startNodeId, int endNodeId) {
		Node start = initPath(startNodeId);
		computePath(start);
		
		String shortestpath = pathInfo.get(endNodeId);
		String[] split = shortestpath.split("->");
		ArrayList<Integer> pathList = new ArrayList<Integer>();
		for(String node : split) {
			pathList.add(Integer.parseInt(node));
		}
		
		return pathList;
	}
}

