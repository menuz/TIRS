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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import com.tirsweb.dao.jdbc.DAO4;
import com.tirsweb.dao.jdbc.DAO5;
import com.tirsweb.model.ParkingLocation;
import com.tirsweb.util.FileHelper;

/**
 * 
 * 此类描述的是：
 * @author: dmnrei@gmail.com
 * @version: 2013-6-3 下午3:29:57
 */
public class kMeans {

	/** Number of clusters */
	private int k;

	/** Array of clusters */
	private Cluster[] clusters;

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
		this.clusters = new Cluster[this.k];
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
		this.clusters = new Cluster[this.k];
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
			this.clusters[i] = new Cluster(i);
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
	public Cluster[] getClusters() {
		return this.clusters;
	}

	/**
	 * Returns the specified cluster by index
	 * 
	 * @param index
	 *            index of the cluster to be returned
	 * @return return the specified cluster by index
	 */
	public Cluster getCluster(int index) {
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
	
	
	public Map<Integer, ClusterInfo> getClusterInfoMap() {
		Map<Integer, ClusterInfo> clusterInfoMap = new HashMap<Integer, ClusterInfo>();
		for(int i=0; i<k; i++) {
			Cluster cluster = this.clusters[i];
			ClusterInfo clusterInfo = new ClusterInfo(cluster.getClusterNumber(), cluster.getMean().getX(), cluster.getMean().getY());
// System.out.println("clusterid = " +cluster.getClusterNumber());
			clusterInfoMap.put(cluster.getClusterNumber(), clusterInfo);
		}
		
		for(int i=0; i<this.kMeansPoints.size(); i++) {
			kMeansPoint kmPoint = (kMeansPoint)this.kMeansPoints.get(i);
// System.out.println(kmPoint.getClusterNumber());
			clusterInfoMap.get(kmPoint.getClusterNumber()).addKMeanPoint(kmPoint);
		}
		
		return clusterInfoMap;
	}

	/**
	 * Main method -- to test the kMeans class
	 * 
	 * @param args
	 *            command line arguments
	 */
	/*public static void main(String[] args) {
		DAO5 dao = new DAO5();
		
		ArrayList<Integer> arcIdList = new ArrayList<Integer>();
		
		arcIdList.add(58);
		arcIdList.add(90);
		arcIdList.add(123);
		arcIdList.add(185);
		
		for(int j=0; j<arcIdList.size(); j++) {
			int arcId = arcIdList.get(j);
			ArrayList<ParkingLocation> pks = new ArrayList<ParkingLocation>();
			dao.getParkingLocationByArcId(pks, arcId);
	
			List<kMeansPoint> ps = new ArrayList<kMeansPoint>();
			for (ParkingLocation parkingLocation : pks) {
				kMeansPoint p = new kMeansPoint(parkingLocation.getLati(), parkingLocation.getLongi());
				ps.add(p);
			}
			
			if(pks.size() == 0) {
				//System.out.println("there is no parking location located here");
				continue;
			}
			
			ArrayList<Double> disList = new ArrayList<Double>();
			int k = 8;
			for(int i=1; i<=k; i++) {
				kMeans km = new kMeans(i, ps);
				
				km.runKMeans();
				Cluster[] clusters = km.getClusters();
				for (Cluster cluster : clusters) {
					 // System.out.println(cluster.getMean());
				}
				
				Map<Integer, ClusterInfo> clusterInfoMap = km.getClusterInfoMap();
				Set<Integer> sets = clusterInfoMap.keySet();
				double totalDis = 0.0;
				for (Integer integer : sets) {
					// System.out.println(clusterInfoMap.get(integer));
					totalDis += clusterInfoMap.get(integer).getTotalDistance();
				}
				// System.out.println("totalDis = " + totalDis);
				disList.add(totalDis);
			}
			
			String x = "x" + j +" = [";
			for(int i=1; i<=k; i++) {
				if(i==k) {
					x += i;
				} else{
					x += (i + ",");
				}
			}
			x += "];";
			
			String y = "y" + j + " = [";
			for(int i=0; i<disList.size(); i++) {
				if(i == disList.size() - 1) {
					y += disList.get(i);
				} else {
					y += (disList.get(i) + ",");
				}
			}
			y += "];";
			
			int number = j+1;
			int row = 2;
			int column = 2;
			
			// xlabel('x/D');
		    // ylabel('R(x,x+r)');
			
			// System.out.println("arcid = " + arcId);
			System.out.println("subplot("+row+","+column+","+(number) +");");
			System.out.println(x);
			System.out.println(y);
			System.out.println("p"+j+" = plot(x" + j+ ", y" + j + ");");
			// set(gca,'FontSize',18);
		    // set(p,'LineWidth',2);
			System.out.println("set(gca,'FontSize',10);");
			System.out.println("set(p"+j+",'LineWidth',2);");
			System.out.println("xlabel('k')");
			System.out.println("ylabel('dis(m/s)')");
			System.out.println("title('ArcId = " + arcId +"');");
			System.out.println("legend('gps num = " + pks.size() + "');");
			// clf clear figure in octave
		}

	} // end of main()
*/
	
	/*public static void main(String[] args) {
		DAO5 dao = new DAO5();
		
			int arcId = 185;
			ArrayList<ParkingLocation> pks = new ArrayList<ParkingLocation>();
			dao.getParkingLocationByArcId(pks, arcId);
	
			List<kMeansPoint> ps = new ArrayList<kMeansPoint>();
			for (ParkingLocation parkingLocation : pks) {
				kMeansPoint p = new kMeansPoint(parkingLocation.getLati(), parkingLocation.getLongi());
				ps.add(p);
			}
			
			if(pks.size() == 0) {
				//System.out.println("there is no parking location located here");
				return;
			}
			
			ArrayList<Double> disList = new ArrayList<Double>();
			int k = 8;
			for(int i=1; i<=k; i++) {
				kMeans km = new kMeans(i, ps);
				
				km.runKMeans();
				Cluster[] clusters = km.getClusters();
				for (Cluster cluster : clusters) {
					 // System.out.println(cluster.getMean());
				}
				
				Map<Integer, ClusterInfo> clusterInfoMap = km.getClusterInfoMap();
				Set<Integer> sets = clusterInfoMap.keySet();
				double totalDis = 0.0;
				for (Integer integer : sets) {
					// System.out.println(clusterInfoMap.get(integer));
					totalDis += clusterInfoMap.get(integer).getTotalDistance();
				}
				// System.out.println("totalDis = " + totalDis);
				disList.add(totalDis);
			}
			
			K kObject = new K(pks.size(), disList);
			int bestK = kObject.getK();
			
			System.out.println("best k = " + bestK);
			
			String x = "x = [";
			for(int i=1; i<=k; i++) {
				if(i==k) {
					x += i;
				} else{
					x += (i + ",");
				}
			}
			x += "];";
			
			String y = "y = [";
			for(int i=0; i<disList.size(); i++) {
				if(i == disList.size() - 1) {
					y += disList.get(i);
				} else {
					y += (disList.get(i) + ",");
				}
			}
			y += "];";
			
			System.out.println(x);
			System.out.println(y);
	}*/
	
	public static void main(String[] args) {
		ArrayList<Integer> arcIdList = new ArrayList<Integer>();
		DAO5 dao = new DAO5();
		dao.getArcList(arcIdList);
		
		Map<Integer, Integer> arcAndOppositeArcMap;
		arcAndOppositeArcMap = new HashMap<Integer, Integer>();
		dao.getArcAndOppositeArcMap(arcAndOppositeArcMap);
		
		for(int j=0; j<arcIdList.size(); j++) {
			int arcId = arcIdList.get(j);
			
			System.out.println("arcId = " + arcId);
			
			ArrayList<ParkingLocation> pks = new ArrayList<ParkingLocation>();
			dao.getParkingLocationByArcId(pks, arcId);
			List<kMeansPoint> ps = new ArrayList<kMeansPoint>();
			for (ParkingLocation parkingLocation : pks) {
				kMeansPoint p = new kMeansPoint(parkingLocation.getLati(), parkingLocation.getLongi());
				ps.add(p);
			}
			
			if(pks.size() == 0) {
				//System.out.println("there is no parking location located here");
				continue;
			}
			
			ArrayList<Double> disList = new ArrayList<Double>();
			int k = 8;
			for(int i=1; i<=k; i++) {
				kMeans km = new kMeans(i, ps);
				km.runKMeans();
				Cluster[] clusters = km.getClusters();
				for (Cluster cluster : clusters) {
					 // System.out.println(cluster.getMean());
				}
				
				Map<Integer, ClusterInfo> clusterInfoMap = km.getClusterInfoMap();
				Set<Integer> sets = clusterInfoMap.keySet();
				double totalDis = 0.0;
				for (Integer integer : sets) {
					// System.out.println(clusterInfoMap.get(integer));
					totalDis += clusterInfoMap.get(integer).getTotalDistance();
				}
				// System.out.println("totalDis = " + totalDis);
				disList.add(totalDis);
			}
			
			
			pks = new ArrayList<ParkingLocation>();
			dao.getParkingLocationByArcId(pks, arcId);
			ps = new ArrayList<kMeansPoint>();
			for (ParkingLocation parkingLocation : pks) {
				kMeansPoint p = new kMeansPoint(parkingLocation.getLati(), parkingLocation.getLongi());
				ps.add(p);
			}
			
			K kObject = new K(pks.size(), disList);
			int bestK = kObject.getK();
			
			System.out.println("best k = " + bestK);
			
			kMeans km = new kMeans(bestK, ps);
			km.runKMeans();
			Cluster[] clusters = km.getClusters();
			Map<Integer, ClusterInfo> clusterInfoMap = km.getClusterInfoMap();
			
			System.out.println("here................");
			
			ArrayList<String> sqlList = new ArrayList<String>();
			for (Cluster cluster : clusters) {
				String sql = "insert into tb_parking_location_cluster(lati, longi, gpscount, arc_id) values(" + cluster.getMean().getX() + ", " +
			cluster.getMean().getY() + ", " + clusterInfoMap.get(cluster.getClusterNumber()).getGpsCount() + ", " + arcId + ");";
				
				Integer oppositeArcId = arcAndOppositeArcMap.get(arcId);
				if(oppositeArcId != null) {
					String sql2 = "insert into tb_parking_location_cluster(lati, longi, gpscount, arc_id) values(" + cluster.getMean().getX() + ", " +
							cluster.getMean().getY() + ", " + clusterInfoMap.get(cluster.getClusterNumber()).getGpsCount() + ", " + oppositeArcId + ");";
					sqlList.add(sql2);
				}
				
				sqlList.add(sql);
				
			}
			
			FileHelper fileHelper = new FileHelper("tb_parking_location_cluster_0530.sql", "append", sqlList);
			fileHelper.write();
			fileHelper.close();
		}

	} // end of main()
} // end of class
