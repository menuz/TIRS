/**
 * 文件名：ComparatorArcDis.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-5
 * Copyright by menuz
 */
package com.tirsweb.util.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.tirsweb.model.ArcDis;

public class ComparatorArcDis implements Comparator<ArcDis> {

	@Override
	public int compare(ArcDis o1, ArcDis o2) {
		if(o1.getMinDis() > o2.getMinDis()) {
			return 1;
		} else if(o1.getMinDis() == o2.getMinDis()) {
			return 0;
		} else {
			return -1;
		}
	}
	
	/*public static void main(String[] args) {
		ArcDis o1 = new ArcDis(1, 20);
		ArcDis o2 = new ArcDis(2, 10);
		ArrayList<ArcDis> list = new ArrayList<ArcDis>();
		
		list.add(o1);
		list.add(o2);
		
		
		Collections.sort(list, new ComparatorArcDis());
		
		for (ArcDis arcDis : list) {
			System.out.println(arcDis);
		}
	}*/
}


