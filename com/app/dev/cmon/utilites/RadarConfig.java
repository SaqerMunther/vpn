package com.app.dev.cmon.utilites;



public class RadarConfig {
	
	public static class RadarThresholds {
		public final int ENDPOINTS = 95;
		public  final int SERVERS = 90;
		public  final int DB = 85;
		public static final String CRITICAL_COLOR= "#D34864";
		public static final String WARNING_COLOR= "#FFAA00";
		public static final String NORMAL_COLOR= "white";
		public int getENDPOINTS() {
			return ENDPOINTS;
		}
		public int getSERVERS() {
			return SERVERS;
		}
		public int getDB() {
			return DB;
		}
	}
	
}
