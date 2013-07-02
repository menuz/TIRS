/**
 * 文件名：Test.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-8
 * Copyright by menuz
 */
package com.tirsweb.mapreduce.emulate;


public class Test {
	/*public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		FileSystem hdfs = FileSystem.get(conf);
		byte[] buff = "hello hadoop world!\n".getBytes();
		Path dfs = new Path("file:///test");
		FSDataOutputStream outputStream = hdfs.create(dfs);
		outputStream.write(buff, 0, buff.length);
		
		String str = "/Users/menuz/Software/hadoop-1.0.4/conf/core-site.xml";
		FileInputStream fin = null;
		try {
			File f = new File(str);
			fin = new FileInputStream(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		 String dst = "hdfs://127.0.0.1:9000/user/menuz/test.txt"; 

		  Configuration conf = new Configuration();
		  conf.addResource(fin);

		  System.out.println("support = " + conf.get("dfs.append.support"));
		  System.out.println("support = " + conf.get("dfs.replication"));
		  System.out.println("support = " + conf.get("dfs.permissions"));
		  System.out.println("support = " + conf.get("fs.default.name"));
		  
		  
		  FileSystem fs = FileSystem.get(URI.create(dst), conf); 

		  FSDataOutputStream out = fs.append(new Path(dst));

		  int readLen = "zhangzk add by hdfs java api".getBytes().length;

		  while(-1 != readLen){

		  out.write("zhangzk add by hdfs java api".getBytes(), 0, readLen);

		  }

		  out.close();

		  fs.close();
		
	}*/


		public static void main(String[] args) throws Exception {
			// The URI set the file you want from HDFS
			/*String hdfs_uri = "hdfs://localhost:9000";
			String file_uri = "hdfs://localhost:9000/tmp/test.txt";
			
			Configuration config = new Configuration();
			config.addDefaultResource("core-default.xml");
			config.addDefaultResource("core-site.xml");
			
			System.out.println("fs = " + config.get("fs.default.name"));
			
			FileSystem fs = FileSystem.get(URI.create(hdfs_uri),
					config);
			FSDataInputStream in = null;
			FSDataOutputStream out = null;
			try {
				in = fs.open(new Path(file_uri));
				IOUtils.copyBytes(in, System.out, 1024);
				IOUtils.closeStream(in);
				
				out = fs.append(new Path(file_uri));
				out.writeChars("hello world");
				IOUtils.closeStream(out);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}*/
			
			int k = 30000000;
			
			String str = "adfsssssssssssssslkjdflaksjdflkajsdflkajsdflkajsldfkjalskdfjalskdjflaksdjf";
			int len = str.length();
			int result = 1024*1024*10/len;
			
			System.out.println(len + "  " + result);
		
			StringBuffer sb = new StringBuffer(20*1024*1024);
			for(int i=0; i<k; i++) {
				
				if(i % 100000 == 0) {
					FileHelper fileHelper = new FileHelper("test.txt", "append");
					fileHelper.write(sb.toString());
					fileHelper.close();
					
					sb = new StringBuffer(20*1024*1024);
				}
				String sql = i + " adfsssssssssssssslkjdflaksjdflkajsdflkajsdflkajsldfkjalskdfjalskdjflaksdjf";
				sb.append(sql + "\n");
				
				System.out.println("i = " + i);
			}
			
			FileHelper fileHelper = new FileHelper("test.txt", "append");
			fileHelper.write(sb.toString());
			fileHelper.close();
		}
	
	
	/*public static void main(String[] args) {
		File f = new File("cmd");
		try {
			FileInputStream fin = new FileInputStream(f);
			byte[] b = new byte[1024];
			fin.read(b);
			
			System.out.println(new String(b));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
}
