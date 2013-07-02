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

public class MyJob extends Configured implements Tool {

public static class MyMapper 
       extends Mapper<Object, Text, Text, Text>{
	int counter = 0;
	double randomnumber = Math.random();
    
    // key will genreate random
    // value is the input string
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	String[] strs = value.toString().split(" ");
    	String newkey = randomnumber + strs[0];
    	context.write(new Text(newkey), new Text(strs[0]));
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
	    job.setJarByClass(MyJob.class);
	    
	    // set mapper and reducer
	    job.setMapperClass(MyMapper.class);
	    job.setCombinerClass(MyReducer.class);
	    job.setReducerClass(MyReducer.class);
	    
	    // output key value type
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	    
	    // add input output dir
	    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
	    
	    return (job.waitForCompletion(true) ? 0 : 1);
	}

  public static void main(String[] args) throws Exception {
	  int res = ToolRunner.run(new Configuration(), new MyJob(), args);
      System.exit(res);
  }
}




