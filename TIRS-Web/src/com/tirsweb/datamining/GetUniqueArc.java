/**
 * 文件名：GetUniqueArc.java
 *
 * 版本信息： version 1.0
 * 日期：May 27, 2013
 * Copyright by menuz
 */
package com.tirsweb.datamining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tirsweb.dao.jdbc.DAO4;

/**
 * 
 * 此类描述的是：arcid=1 startnode=1 endnode=2  arcid=41 startnode=2 endnode=1
 * 	          if get arcid = 41 return arcid=1 
 * 			  if get arcid = 1 return arcid = 1
 *            get the min value
 * @author: dmnrei@gmail.com
 * @version: May 27, 2013 10:31:51 AM
 */
public class GetUniqueArc {
	public static void main(String[] args) {
		DAO4 dao = new DAO4();
		Map<Integer, Integer> maps = new HashMap<Integer, Integer>();
		dao.getArcMap(maps);
		
		Set keys = maps.keySet();
		ArrayList<Integer> keyList = new ArrayList<Integer>(keys);
		Set<Integer> arcSet = new HashSet<Integer>();
		for (Integer key : keyList) {
			// System.out.println(key);
			int value = maps.get(key);
			
			if(value >= key) {
				arcSet.add(key);
			} else {
				arcSet.add(value);
			}
		}
		
		for (Integer integer : arcSet) {
			System.out.println(integer + "     "  + maps.get(integer));
		}
		
		ArrayList<Integer> arcSetList = new ArrayList<Integer>(arcSet);
		dao.insertOneDirectionArc(arcSetList);
	}
}


