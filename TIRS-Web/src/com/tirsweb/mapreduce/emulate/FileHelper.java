package com.tirsweb.mapreduce.emulate;

/**
 * 文件名：FileWriter.java
 *
 * 版本信息： version 1.0
 * 日期：May 19, 2013
 * Copyright by menuz
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * 
 * 此类描述的是：help java file in dataminig package to store sql to sql file 
 * @author: dmnrei@gmail.com
 * @version: May 29, 2013 9:56:16 AM
 */
public class FileHelper {
	ArrayList<String> sqlList;
	String filename;
	String mode;

	FileWriter write = null;
	BufferedWriter bufferedWriter = null;

	public FileHelper(String filename, String mode, ArrayList<String> sqlList) {
		this.filename = filename;
		this.mode = mode;
		this.sqlList = sqlList;

		File f = new File(filename);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			write = null;
			try {
				if (mode.equals("override")) {
					write = new FileWriter(f, false);
				} else if (mode.equals("append")) {
					write = new FileWriter(f, true);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			bufferedWriter = new BufferedWriter(write);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public FileHelper(String filename, String mode) {
		this.filename = filename;
		this.mode = mode;

		File f = new File(filename);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			write = null;
			try {
				if (mode.equals("override")) {
					write = new FileWriter(f, false);
				} else if (mode.equals("append")) {
					write = new FileWriter(f, true);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			bufferedWriter = new BufferedWriter(write);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void write() {
		for (String sql : sqlList) {
			try {
				bufferedWriter.write(sql+"\n");
				bufferedWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		close();
	}
	
	public void write(String str) {
		try {
			bufferedWriter.write(str + "\n");
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		close();
	}

	public void close() {
		try {
			if (bufferedWriter != null) {
				bufferedWriter.close();
				bufferedWriter = null;
			} 
			
			if( write != null ) {
				write.close();
				write = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
