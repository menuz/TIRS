/**
 * 文件名：Main.java
 *
 * 版本信息： version 1.0
 * 日期：Apr 19, 2013
 * Copyright by menuz
 */
package com.tirs.main;

import org.apache.hadoop.util.ToolRunner;

/**
 * 
 * 此类描述的是：
 * @author: dmnrei@gmail.com
 * @version: Apr 19, 2013 4:57:37 PM
 */
public class Main {

	/**
	 * 此方法描述的是：
	 * @param args
	 * @version: Apr 19, 2013 4:57:21 PM
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new DemoDriver(), args);
		System.exit(exitCode);
	}
}


