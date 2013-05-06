/*package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.model.GPS;

public class RealGPSToMarGPS {
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://127.0.0.1:3306/restbus";
	private static String user = "root";
	private static String password = "root";
	private Connection conn = null;

	public RealGPSToMarGPS() {
		try {
			Class.forName(driver);
			if (null != conn && !conn.isClosed())
				return;
			conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public static void main(String[] args) {
		RealGPSToMarGPS convertor = new RealGPSToMarGPS();
		convertor.getRealDataFromFile();
	}

	public Map<String, String> getRealDataFromFile() {
		File f = new File("realData.txt");
		Map<String, String> datas = new HashMap<String, String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] data = line.split("\t");
				
				 GPS gps = gpsToMarGps(Double.parseDouble(data[0]), Double.parseDouble(data[1]));
				 System.out.println("insert into path values(null, 31,"+ gps.getLatitude() +"," +gps.getLongitude() + ");");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return datas;
	}

	public void transfer() {
		Map<String, String> datas = getRealDataFromFile();

		Iterator it = datas.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			
			String _key = (String)key;
			String _value = (String)value;
			
			System.out.println(_key + "  " + _value);
			
//			GPS gps = gpsToMarGps((String)key, (String)value);
		
//			System.out.println(gps.getLatitude() + "  " + gps.getLongitude() );
//			System.out.println(	"insert into path values(null, 30,"+ gps.getLatitude() +"," +gps.getLongitude() + ");");
		}

	}

	public GPS gpsToMarGps(double latitude, double longitude) {
		DecimalFormat df = new DecimalFormat("###.00");
		String df_lat = df.format(latitude);
		String df_lon = df.format(longitude);

		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select offsetlat, offsetlon from gps_correct where lat="
					+ df_lat + " and lon=" + df_lon + ";";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				double offsetlat = rs.getDouble("offsetlat");
				double offsetlon = rs.getDouble("offsetlon");
				return new GPS(latitude + offsetlat, longitude + offsetlon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}

	public void releaseSource(Connection conn, Statement stmt) {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void releaseSource(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void releaseSource(Connection conn, Statement stmt, ResultSet rs,
			ResultSet rs2) {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (rs2 != null) {
				rs2.close();
				rs2 = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
*/