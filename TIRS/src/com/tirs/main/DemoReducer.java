package com.tirs.main;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;


public class DemoReducer extends MapReduceBase
  implements Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {

	@Override
	public void reduce(IntWritable key, Iterator<IntWritable> values,
			OutputCollector<IntWritable, IntWritable> output, Reporter reporter)
			throws IOException {
		int sum = 0;
		while(values.hasNext()) {
			IntWritable iw = values.next();
			int i = iw.get();
			sum += i;
		}
		output.collect(key, new IntWritable(sum));
	}
	
}
// ^^ MaxTemperatureReducerV1
