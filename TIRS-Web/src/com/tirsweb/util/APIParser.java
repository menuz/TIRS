package com.tirsweb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 此类描述的是：负责对API进行解析
 * @author: dmnrei@gmail.com
 * @version: Nov 11, 2012 10:45:16 AM
 */
public class APIParser {
	public String query;
	public Map<String, String> keyValue;
	
	public APIParser() {}
	
	public APIParser(String query) {
		this.query = query;
	}
	
	private void parse() {
		this.keyValue = new HashMap<String, String>();
		
		String[] key_value_arr = this.query.split("&");
		for(String key_value : key_value_arr) {
			int idx = key_value.indexOf("=");
			
			String key = key_value.substring(0, idx);
			String value = key_value.substring(idx + 1);
			
			this.keyValue.put(key, value);
		}
	}
	
	public Map<String, String> getParseResult() {
		parse();
		return this.keyValue;
	}
	

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}
