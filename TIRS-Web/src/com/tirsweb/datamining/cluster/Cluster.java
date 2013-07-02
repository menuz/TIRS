/**
 * 文件名：Cluster.java
 *
 * 版本信息： version 1.0
 * 日期：May 28, 2013
 * Copyright by menuz
 */
package com.tirsweb.datamining.cluster;

import com.tirsweb.datamining.cluster.kMeansPoint;

/*
 * Represents an abstraction for a cluster of data points in two dimensional
 * space
 * 
 * Manas Somaiya Computer and Information Science and Engineering University of
 * Florida
 * 
 * Created: October 29, 2003 Last updated: October 30, 2003
 */

/**
 * Represents an abstraction for a cluster of data points in two dimensional
 * space
 * 
 * @author Manas Somaiya mhs@cise.ufl.edu
 */
public class Cluster {

	/** Cluster Number */
	private int clusterNumber;

	/** Mean data point of this cluster */
	private kMeansPoint mean;

	/**
	 * Returns a new instance of cluster
	 * 
	 * @param _clusterNumber
	 *            the cluster number of this cluster
	 */
	public Cluster(int _clusterNumber) {

		this.clusterNumber = _clusterNumber;

	} // end of cluster()

	/**
	 * Sets the mean data point of this cluster
	 * 
	 * @param meanDataPoint
	 *            the new mean data point for this cluster
	 */
	public void setMean(kMeansPoint meanDataPoint) {

		this.mean = meanDataPoint;

	} // end of setMean()

	/**
	 * Returns the mean data point of this cluster
	 * 
	 * @return the mean data point of this cluster
	 */
	public kMeansPoint getMean() {

		return this.mean;

	} // end of getMean()

	/**
	 * Returns the cluster number of this cluster
	 * 
	 * @return the cluster number of this cluster
	 */
	public int getClusterNumber() {

		return this.clusterNumber;

	} // end of getClusterNumber()

	/**
	 * Main method -- to test the cluster class
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		Cluster c1 = new Cluster(1);
		c1.setMean(new kMeansPoint(3, 4));
		System.out.println(c1.getMean());

	} // end of main()
}