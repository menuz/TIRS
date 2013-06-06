/**
 * 文件名：BaseArc.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-6
 * Copyright by menuz
 */
package com.tirsweb.model;

public class BaseArc {

	public int id;
	public int start_node_id;
	public int end_node_id;
	public int len;
	
	public BaseArc(int id, int start_node_id, int end_node_id, int len) {
		super();
		this.id = id;
		this.start_node_id = start_node_id;
		this.end_node_id = end_node_id;
		this.len = len;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public BaseArc() {
		super();
	}

	public BaseArc(int id, int start_node_id, int end_node_id) {
		super();
		this.id = id;
		this.start_node_id = start_node_id;
		this.end_node_id = end_node_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStart_node_id() {
		return start_node_id;
	}

	public void setStart_node_id(int start_node_id) {
		this.start_node_id = start_node_id;
	}

	public int getEnd_node_id() {
		return end_node_id;
	}

	public void setEnd_node_id(int end_node_id) {
		this.end_node_id = end_node_id;
	}
}
