/**
 * 文件名：WordCount.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-7
 * Copyright by menuz
 */
package com.tirsweb.mapreduce;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.tirsweb.mapreduce.model.RawGPS;
import com.tirsweb.mapreduce.model.Trip;

public class DealRawGPSMR extends Configured implements Tool {

public static class MyMapper 
       extends Mapper<Object, Text, Text, Text>{
	int counter = 0;
	ArrayList<RawGPS> trip = new ArrayList<RawGPS>();
    
    // key will genreate random
    // value is the input string
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	String[] strs = value.toString().split("\t");
    	
    	RawGPS rawGPS = RawGPS.parse(strs[1]);
    	if(trip.size() > 0) {
    		RawGPS lastRawGPS = trip.get(trip.size()-1);
    		// vehicle
    		if(lastRawGPS.getVehicle_id().equals(rawGPS.getVehicle_id())) {
    			if(rawGPS.getState().equals(lastRawGPS.getState())) {
    				trip.add(rawGPS);
    			} else {
    				StringBuffer sb = new StringBuffer(2048);
    				
    				/***
    				 *  同一辆车有上客，下客
    				 */
    				StringBuffer tmp = new StringBuffer(128);
    				tmp.append(rawGPS.getVehicle_id()+"+");
    				tmp.append(lastRawGPS.getMessage_id()+";"+lastRawGPS.getSuffixLati()+";"+lastRawGPS.getSuffixLongi()+";"+lastRawGPS.getState()+";"+lastRawGPS.getSpeed_time()+"+");
    				tmp.append(rawGPS.getMessage_id()+";"+rawGPS.getSuffixLati()+";"+rawGPS.getSuffixLongi()+";"+rawGPS.getState()+";"+rawGPS.getSpeed_time());
    				
    				/**
    				 * trip
    				 */
    				String vehicle = lastRawGPS.getVehicle_id();
        			String tripId = vehicle + "+" +trip.get(0).getMessage_id();
        			String state = lastRawGPS.getState();
        			String startTime = trip.get(0).getSpeed_time().toString();
        			String endTime = trip.get(trip.size()-1).getSpeed_time().toString();
        			
        			StringBuffer note = new StringBuffer(1024);
        			for(int i=0; i<trip.size(); i++) {
        				RawGPS gps = trip.get(i);
        				note.append(gps.getGPS());
        			}
        			String _note = note.toString();
        			
        			if(_note.length() > 3990) {
        				_note = _note.substring(0, 3988);
        			}
        			sb.append(tripId + ";" + vehicle + ";" + state + ";" + startTime + ";" + endTime + ";" + _note);
        			
        			trip = new ArrayList<RawGPS>();
        			trip.add(rawGPS);
    				
        			Text newKey = new Text(tmp.toString());
        		 	Text newValue = new Text(sb.toString());
        	    	context.write(newKey, newValue);
    			}
    			
    		} else {
    			StringBuffer sb = new StringBuffer(2048);
    			
    			/**
    			 *  车辆变换的时候没有上下车之分。
    			 */
    			
    			String vehicle = lastRawGPS.getVehicle_id();
    			String tripId = vehicle + "+" +trip.get(0).getMessage_id();
    			String state = lastRawGPS.getState();
    			String startTime = trip.get(0).getSpeed_time().toString();
    			String endTime = trip.get(trip.size()-1).getSpeed_time().toString();
    			
    			StringBuffer note = new StringBuffer(1024);
    			for(int i=0; i<trip.size(); i++) {
    				RawGPS gps = trip.get(i);
    				note.append(gps.getGPS());
    			}
    			String _note = note.toString();
    			
    			if(_note.length() > 3990) {
    				_note = _note.substring(0, 3988);
    			}
    			sb.append(tripId + ";" + vehicle + ";" + state + ";" + startTime + ";" + endTime + ";" + _note);
    			
    			
    			trip = new ArrayList<RawGPS>();
    			trip.add(rawGPS);
    			
    		 	Text newKey = new Text("1");
    		 	Text newValue = new Text(sb.toString());
    	    	context.write(newKey, newValue);
    		}
    	} else {
    		trip.add(rawGPS);
    	}
    }  
  }
  
  public static class MyReducer 
       extends Reducer<Text, Text, Text, Text> {

    public void reduce(Text key, Text value, 
                       Context context
                       ) throws IOException, InterruptedException {
      context.write(key, value);
    }
  }
  
  
  @Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
	  Configuration conf = new Configuration();
	    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	    if (otherArgs.length != 2) {
	      System.err.println("Usage: wordcount <in> <out>");
	      return 2;
	      // System.exit(2);
	    }
	    Job job = new Job(conf, "read and write");
	    job.setJarByClass(DealRawGPSMR.class);
	    
	    // set mapper and reducer
	    job.setMapperClass(MyMapper.class);
	    job.setCombinerClass(MyReducer.class);
	    job.setReducerClass(MyReducer.class);
	    
	    // output key value type
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	    
	    job.setNumReduceTasks(3);  
	    
	    // add input output dir
	    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    
	    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
	    
	    return (job.waitForCompletion(true) ? 0 : 1);
	}

  public static void main(String[] args) throws Exception {
	  int res = ToolRunner.run(new Configuration(), new DealRawGPSMR(), args);
      System.exit(res);
  }
}




