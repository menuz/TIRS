package com.tirsweb.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class TripGeneratorUDF2 extends UDF {
	String state;
	String tripId;
	String vehicleId;
	
	public TripGeneratorUDF2() {
		this.state = "";
		this.tripId = "";
		this.vehicleId = "";
	}
	
	public String evaluate(String vehicle_id, String state, String db_time) {
		if(this.vehicleId.equals("")) {
			this.vehicleId = vehicle_id;
			this.state = state;
			this.tripId = generateTripId(vehicle_id, db_time);
			return tripId;
		} else {
			if(this.vehicleId.equals(vehicle_id)){
				if(this.state.equals("")) {
			    	this.state = state;
			    	this.tripId = generateTripId(vehicle_id, db_time); 
			    	return tripId;
			    } else {
			    	if(this.state.trim().equals(state.trim())) {
			    		return tripId;
			    	} else {
			    		this.state = state;
			    		this.tripId = generateTripId(vehicle_id, db_time);
			    		return tripId;
			    	}
			    }
			} else if(!this.vehicleId.equals(vehicle_id)) {
				this.vehicleId = vehicle_id;
				this.state = state;
				this.tripId = generateTripId(vehicle_id, db_time);
				return tripId;
			}
		}
		
		return tripId;
	}
	
	// 2011-12-01 00:03:43.0
	public String generateTripId(String vehicle_id, String db_time) {
		return vehicle_id+"+"+db_time.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "").substring(0, 0+14);
	}
}
