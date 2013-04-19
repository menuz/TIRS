package com.tirs.main;

import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

public class DemoMapper extends MapReduceBase
  implements Mapper<LongWritable, Text, IntWritable, IntWritable> {

	@Override
	public void map(LongWritable key, Text value,
			OutputCollector<IntWritable, IntWritable> output, Reporter reporter)
			throws IOException {
		String line = value.toString();
		String[] colums = line.split("\t");
		int msgid = Integer.parseInt(colums[0]);
		int busid = Integer.parseInt(colums[1]);
		
		output.collect(new IntWritable(busid), new IntWritable(1));
	}
	
}
