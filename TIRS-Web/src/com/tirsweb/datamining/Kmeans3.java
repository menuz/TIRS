/**
 * 文件名：Kmeans.java
 *
 * 版本信息： version 1.0
 * 日期：May 19, 2013
 * Copyright by menuz
 *//*
package com.tirsweb.datamining;

import java.io.*;

*//**
 * 
 * @author menuz disable
 *
 *//*
public class Kmeans3 {
	double[][] sample;// ËùÓÐÑù±¾Êý¾Ý
	double[][] center;// Ã¿ÖÖÀàÖÐÐÄµãµÄXYÖµ
	int clusterkind, totaln;// ¾ÛÀàÖÖÀàÊý¼°Ñù±¾×ÜÊý

	public Kmeans3(int total, int clusterkd) {
		this.totaln = total;
		this.clusterkind = clusterkd;
		// clusterkind means how many cluster u want to cluster
		center = new double[clusterkind][2];
		sample = new double[totaln][3];
	}

	// find the nearest cluster of every sample, put idx of cluster in the [2]
	void Calssify() {
		int i, j;
		double dis, district;
		i = 0;
		while (i < totaln) {
			district = 10000;
			for (j = 0; j < clusterkind; j++) {
				dis = Math.sqrt((sample[i][0] - center[j][0])
						* (sample[i][0] - center[j][0])
						+ (sample[i][1] - center[j][1])
						* (sample[i][1] - center[j][1]));
				if (dis < district) {
					sample[i][2] = j;
					district = dis;
				}
			}
			i++;
		}
	}

	// to see cluster center has been altered, if ater return false,
	boolean centerChanged(double[][] center1, double[][] center2, int clusterkind)// ÅÐ¶ÏÒÀ´Î¼ÆËãµÄ¸÷¸öÀà¾ùÖµÊÇ·ñÓÐ¸Ä±ä
	{
		int cout = 0;
		while (cout < clusterkind) {
			if (center1[cout][0] != center2[cout][0]
					|| center1[cout][1] != center2[cout][1])
				return true;
			cout++;
		}
		return false;
	}

	boolean IsUpdataCenter()
	{
		int i, j;
		int[] kind = new int[clusterkind];
		double[] amoutx = new double[clusterkind];
		double[] amouty = new double[clusterkind];
		double[][] amoutxy = new double[clusterkind][2];
		
		// sample[1][0]=x1 sample[1][1]=y1  sample[1][2]=cluster1
		// sample[2][0]=x2 sample[2][1]=y2  sample[2][2]=cluster2
		// sample[3][0]=x3 sample[3][1]=y3  sample[3][2]=cluster3
		// sample[4][0]=x4 sample[4][1]=y4  sample[4][2]=cluster1
		// get all sample in cluster1, sum all sample.x as amoutX and all sample.y as amountY
		for (i = 0; i < totaln; i++) {
			amoutx[(int) sample[i][2]] += sample[i][0];
			amouty[(int) sample[i][2]] += sample[i][1];
			kind[(int) sample[i][2]]++;
		}
		
		// get new cluster center x=(x1+x2+...+xn)/n, y=(y1+y2+...+yn)/n 
		for (j = 0; j < clusterkind; j++) {
			amoutx[j] /= (double) kind[j];
			amouty[j] /= (double) kind[j];
			amoutxy[j][0] = amoutx[j];
			amoutxy[j][1] = amouty[j];
		}
		
		if (centerChanged(amoutxy, center, clusterkind)) {
			for (i = 0; i < clusterkind; i++) {
				center[i][0] = amoutxy[i][0];
				center[i][1] = amoutxy[i][1];
			}
			return false;
		} else
			return true;
	}

	void PrintSave() throws java.io.IOException
	{
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
				"Result.txt")));
		for (int i = 0; i < totaln; i++) {
			System.out.println("sample:" + (i + 1) + "  " + sample[i][0] + " "
					+ sample[i][1] + " " + "Class:" + ((int) sample[i][2] + 1)
					+ "\n");
			out.println("sample:" + (i + 1) + "  " + sample[i][0] + " "
					+ sample[i][1] + " " + "Class:" + ((int) sample[i][2] + 1)
					+ "\n");
		}
	}

	void Cluster() throws java.io.IOException {
		int change = 1;
		while (change == 1) {
			Calssify();
			if (IsUpdataCenter()) {
				PrintSave();
				change = 0;
			}
		}
	}

	public static void main(String[] args) throws java.io.IOException {
		String sourcefile;
		System.out.print("Input the sourcefile's path:");
		BufferedReader tempread = new BufferedReader(new InputStreamReader(
				System.in));
		sourcefile = tempread.readLine();
		int clusterkind;
		System.out.print("Input the number of kind to cluster :");
		clusterkind = Integer.parseInt(tempread.readLine());
		InputStream inputstr = new FileInputStream(sourcefile);
		BufferedReader readline = new BufferedReader(new InputStreamReader(
				inputstr));
		String everyline;
		Kmeans3 kmeans = new Kmeans3(100000, clusterkind);

		int cout = 0;
		// totaln = 100000

		// init data martix from source file
		while (cout < kmeans.totaln) {
			everyline = readline.readLine();
			if (everyline == null)
				break;
			String[] line = everyline.split(" ");
			kmeans.sample[cout][0] = Double.parseDouble(line[0]);
			kmeans.sample[cout][1] = Double.parseDouble(line[1]);
			kmeans.sample[cout][2] = -1;
			if (cout < kmeans.clusterkind) {
				kmeans.center[cout][0] = Double.parseDouble(line[0]);
				kmeans.center[cout][1] = Double.parseDouble(line[1]);
			}
			cout++;
		}

		// run cluster
		kmeans.Cluster();

	}
}*/