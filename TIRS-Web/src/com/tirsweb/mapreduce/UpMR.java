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
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.util.GenericOptionsParser;

import com.tirsweb.mapreduce.model.RawGPS;
import com.tirsweb.mapreduce.model.Trip;

public class UpMR extends Configured implements Tool {

	public static class MyRKMapper extends Mapper<Object, Text, NullWritable, Text> {
		
		// key will genreate random
		// value is the input string
		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] strs = value.toString().split("\t");
			String updown = strs[0];
			
			// System.out.println("strs[0] = " + strs[0]);
			// System.out.println("strs[1] = " + strs[1]);
			
			
			if(!updown.equals("1")) {
				String[] split = strs[0].split("[+]");
				String lastgps = split[1];
				int lastindex = lastgps.lastIndexOf(";");
				char lastc = lastgps.charAt(lastindex-1);

				String gps = split[2];
				int index = gps.lastIndexOf(";");
				char c = gps.charAt(index-1);
				
				// System.out.println("lastc = " + lastc + " c = " + c);
				
				if(lastc == '0' && c == '1') {
					context.write(NullWritable.get(), new Text(split[0]+";"+split[1]+";"+split[2]));
					return;
				}
			} 
		}
	}

	public static class MyRKReducer extends Reducer<NullWritable, Text, NullWritable, Text> {

		public void reduce(NullWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			context.write(key.get(), value);
		}
	}

	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 4) {
			System.err.println("Usage: wordcount <in> <out>");
			return 2;
			// System.exit(2);
		}
		Job job = new Job(conf, "read and write");
		job.setJarByClass(UpMR.class);

		// set mapper and reducer
		job.setMapperClass(MyRKMapper.class);
		job.setCombinerClass(MyRKReducer.class);
		job.setReducerClass(MyRKReducer.class);

		// output key value type
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);

		job.setNumReduceTasks(1);

		// add input output dir
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileInputFormat.addInputPath(job, new Path(otherArgs[1]));
		FileInputFormat.addInputPath(job, new Path(otherArgs[2]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[3]));

		return (job.waitForCompletion(true) ? 0 : 1);
	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new UpMR(), args);
		System.exit(res);
	}
}
