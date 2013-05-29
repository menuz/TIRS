/**
 * 文件名：KMeans.java
 *
 * 版本信息： version 1.0
 * 日期：May 28, 2013
 * Copyright by menuz
 */
package com.tirsweb.datamining;

/*  
 * Implements the k-means algorithm  
 *  
 * Manas Somaiya  
 * Computer and Information Science and Engineering  
 * University of Florida  
 *  
 * Created: October 29, 2003  
 * Last updated: October 30, 2003  
 *  
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import com.tirsweb.dao.jdbc.DAO5;
import com.tirsweb.model.ParkingLocation;

/**
 * Implements the k-means algorithm
 * 
 * @author Manas Somaiya mhs@cise.ufl.edu
 */
public class kMeans {

	/** Number of clusters */
	private int k;

	/** Array of clusters */
	private cluster[] clusters;

	/** Number of iterations */
	private int nIterations;

	/** Vector of data points */
	private Vector kMeansPoints;

	/** Name of the input file */
	private String inputFileName;

	/**
	 * Returns a new instance of kMeans algorithm
	 * 
	 * @param k
	 *            number of clusters
	 * @param inputFileName
	 *            name of the file containing input data
	 */
	public kMeans(int k, String inputFileName) {
		this.k = k;
		this.inputFileName = inputFileName;
		this.clusters = new cluster[this.k];
		this.nIterations = 0;
		this.kMeansPoints = new Vector();

	} // end of kMeans()

	/**
	 * Returns a new instance of kMeans algorithm
	 * 
	 * @param k
	 *            number of clusters
	 * @param kMeansPoints
	 *            List containing objects of type kMeansPoint
	 */
	public kMeans(int k, List kMeansPoints) {

		this.k = k;
		this.inputFileName = inputFileName;
		this.clusters = new cluster[this.k];
		this.nIterations = 0;
		this.kMeansPoints = new Vector(kMeansPoints);

	} // end of kMeans()

	/**
	 * Reads the input data from the file and stores the data points in the
	 * vector
	 */
	public void readData() throws IOException {

		BufferedReader in = new BufferedReader(new FileReader(
				this.inputFileName));
		String line = "";
		while ((line = in.readLine()) != null) {

			StringTokenizer st = new StringTokenizer(line, " \t\n\r\f,");
			if (st.countTokens() == 2) {

				kMeansPoint dp = new kMeansPoint(Integer.parseInt(st
						.nextToken()), Integer.parseInt(st.nextToken()));
				dp.assignToCluster(0);
				this.kMeansPoints.add(dp);

			}

		}

		in.close();

	} // end of readData()

	/**
	 * Runs the k-means algorithm over the data set
	 */
	public void runKMeans() {

		// Select k points as initial means
		for (int i = 0; i < k; i++) {
			this.clusters[i] = new cluster(i);
			this.clusters[i].setMean((kMeansPoint) (this.kMeansPoints
					.get((int) (Math.random() * this.kMeansPoints.size()))));
		}

		do {
			// Form k clusters
			Iterator i = this.kMeansPoints.iterator();
			while (i.hasNext())
				this.assignToCluster((kMeansPoint) (i.next()));

			this.nIterations++;
		}
		// Repeat while centroids do not change
		while (this.updateMeans());

	} // end of runKMeans()

	/**
	 * Assigns a data point to one of the k clusters based on its distance from
	 * the means of the clusters
	 * 
	 * @param dp
	 *            data point to be assigned
	 */
	private void assignToCluster(kMeansPoint dp) {
		int currentCluster = dp.getClusterNumber();
		double minDistance = kMeansPoint.distance(dp,
				this.clusters[currentCluster].getMean());
		;

		for (int i = 0; i < this.k; i++)
			if (kMeansPoint.distance(dp, this.clusters[i].getMean()) < minDistance) {
				minDistance = kMeansPoint.distance(dp,
						this.clusters[i].getMean());
				currentCluster = i;
			}

		dp.assignToCluster(currentCluster);

	} // end of assignToCluster

	/**
	 * Updates the means of all k clusters, and returns if they have changed or
	 * not
	 * 
	 * @return have the updated means of the clusters changed or not
	 */
	private boolean updateMeans() {
		boolean reply = false;

		double[] x = new double[this.k];
		double[] y = new double[this.k];
		int[] size = new int[this.k];
		kMeansPoint[] pastMeans = new kMeansPoint[this.k];

		for (int i = 0; i < this.k; i++) {
			x[i] = 0;
			y[i] = 0;
			size[i] = 0;
			pastMeans[i] = this.clusters[i].getMean();
		}

		Iterator i = this.kMeansPoints.iterator();
		while (i.hasNext()) {
			kMeansPoint dp = (kMeansPoint) (i.next());
			int currentCluster = dp.getClusterNumber();
			x[currentCluster] += dp.getX();
			y[currentCluster] += dp.getY();
			size[currentCluster]++;
		}

		for (int j = 0; j < this.k; j++)
			if (size[j] != 0) {
				x[j] /= size[j];
				y[j] /= size[j];
				kMeansPoint temp = new kMeansPoint(x[j], y[j]);
				temp.assignToCluster(j);
				this.clusters[j].setMean(temp);
				if (kMeansPoint.distance(pastMeans[j],
						this.clusters[j].getMean()) != 0)
					reply = true;
			}

		return reply;

	} // end of updateMeans()

	/**
	 * Returns the value of k
	 * @return the value of k
	 */
	public int getK() {
		return this.k;

	} // end of getK()
	
	
	/**
	 * 
		 * 此方法描述的是：return all cluster
	     * @return
	     * @version: May 28, 2013 7:10:49 PM
	 */
	public cluster[] getClusters() {
		return this.clusters;
	}

	/**
	 * Returns the specified cluster by index
	 * 
	 * @param index
	 *            index of the cluster to be returned
	 * @return return the specified cluster by index
	 */
	public cluster getCluster(int index) {
		return this.clusters[index];
	} // end of getCluster()

	/**
	 * Returns the string output of the data points
	 * 
	 * @return the string output of the data points
	 */
	public String toString() {
		return this.kMeansPoints.toString();
	} // end of toString()

	/**
	 * Returns the data points
	 * 
	 * @return the data points
	 */
	public Vector getDataPoints() {
		return this.kMeansPoints;
	} // end of getDataPoints()

	/**
	 * Main method -- to test the kMeans class
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
/*		kMeansPoint p1 = new kMeansPoint(1, 1);
		kMeansPoint p2 = new kMeansPoint(1, 2);
		kMeansPoint p3 = new kMeansPoint(2, 1);
		kMeansPoint p4 = new kMeansPoint(2, 2);
		kMeansPoint p5 = new kMeansPoint(1.5, 1.5);
		kMeansPoint p6 = new kMeansPoint(9, 9);
		kMeansPoint p7 = new kMeansPoint(9, 10);
		kMeansPoint p8 = new kMeansPoint(10, 9);
		kMeansPoint p9 = new kMeansPoint(10, 10);
		kMeansPoint p10 = new kMeansPoint(9.5, 9.5);
		List<kMeansPoint> ps = new ArrayList<kMeansPoint>();
		ps.add(p1);ps.add(p2);ps.add(p3);ps.add(p4);ps.add(p5);ps.add(p6);
		ps.add(p7);ps.add(p8);ps.add(p9);ps.add(p10);
*/
		DAO5 dao = new DAO5();
		ArrayList<ParkingLocation> pks = new ArrayList<ParkingLocation>();
		dao.getParkingLocationByArcId(pks, 57);

		List<kMeansPoint> ps = new ArrayList<kMeansPoint>();
		for (ParkingLocation parkingLocation : pks) {
			kMeansPoint p = new kMeansPoint(parkingLocation.getLati(), parkingLocation.getLongi());
			ps.add(p);
		}
		
		kMeans km = new kMeans(25, ps);
		/*try {
			km.readData();
		} catch (Exception e) {
			System.err.println(e);
			System.exit(-1);
		}*/

		km.runKMeans();
		cluster[] clusters = km.getClusters();
		for (cluster cluster : clusters) {
			System.out.println(cluster.getMean());
		}
		/*System.out.println(km.getCluster(0).getMean());
		System.out.println(km.getCluster(1).getMean());
		System.out.println(km.getCluster(2).getMean());
		System.out.println(km.getCluster(3).getMean());*/
		//System.out.println(km);

	} // end of main()

} // end of class
