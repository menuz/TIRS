/**
 * 文件名：SpeedManager.java
 *
 * 版本信息： version 1.0
 * 日期：2013-6-6
 * Copyright by menuz
 */
package com.tirsweb.util.cache.thread;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.tirsweb.dao.CacheDAO;
import com.tirsweb.model.Speed;
import com.tirsweb.util.FileHelper;
import com.tirsweb.util.TimeUtil;

public class SpeedManager implements Runnable {
	private Map<String, Speed> speeds;
	private long lastRefreshTime;
	
	
	public SpeedManager(Map<String, Speed> speeds) {
		this.speeds = speeds;
		
		//load three hours' average speed right now
		CacheDAO cacheDAO = new CacheDAO();
		cacheDAO.loadSpeed(speeds, generateSql());
		lastRefreshTime = new Date().getTime();
	}
	
	public String generateSql() {
		ArrayList<Integer> hours = getNeighboringHour();
		String hourSql = "";
		for (int i=0; i<hours.size(); i++) {
			int hour = hours.get(i);
			if(i == hours.size() - 1) {
				hourSql = hourSql + hour;
			} else {
				hourSql = hourSql + hour + ",";
			}
		}
		
		String sql = "select * from tb_arc_speed_avg where weekday = " + getWeekday() + " and hourofday in (" + hourSql + ")";
		
		return sql;
	}
	
	public int getWeekday() {
		return TimeUtil.getWeekday();
	}
	
	public ArrayList<Integer> getNeighboringHour() {
		int hour = TimeUtil.getHour();
		int onehourago = hour - 1;
		int onehourlater = hour + 1;
		ArrayList<Integer> hours = new ArrayList<Integer>();
		
		if(onehourago >= 0 || onehourago <= 23) {
			hours.add(onehourago);
		}
		
		if(hour >= 0 || hour <= 23) {
			hours.add(hour);
		}
		
		if(onehourlater >= 0 || onehourlater <= 23) {
			hours.add(onehourlater);
		}
		
		return hours;
	}
	
	
	public void refreshSpeed() {
		CacheDAO cacheDAO = new CacheDAO();
		cacheDAO.loadSpeed(speeds, generateSql());
	}
	
	/**
	 * 
		 * 此方法描述的是：刷新时间间隔半小时以上
	     * @return
	     * @author: dmnrei@gmail.com
	     * @version: 2013-6-6 下午6:46:35
	 */
	public boolean timeIntervalSatisfy() {
		long now = new Date().getTime();
		
		int seconds = (int)((lastRefreshTime - now) / 1000);
		
		if(seconds > 2000) {
			return true;
		}
		
		return false;
	}
	
	public boolean timeSatisfy() {
		Calendar calendar = Calendar.getInstance();
		int minutes = calendar.get(Calendar.MINUTE);
		
		if(minutes > 55 && minutes < 59 && timeIntervalSatisfy()) {
			return true;
		}
		
		return false;
	}

	@Override
	public void run() {
		
		try {
			while(true) {
				if(timeSatisfy()) {
					refreshSpeed();
					lastRefreshTime = new Date().getTime();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			FileHelper fileHelper = new FileHelper("SpeedManager.log", "append");
			String log = "Speed manager is dead @ " + TimeUtil.getFormattedTime();
			fileHelper.write(log);
			
		}
	}

}


