package com.app.dev.cmon.controllers;

public class RadarLeftCard {
	
	 private double complianceTodayPercentage;
	 private String controlName;
     private int covVal1;
     private int covVal2;
     private int compVal1;
     private int compVal2;
     private int selectedThreshold;
     private RadarHistorical radarHistorical;
	 public RadarLeftCard() {
		 
	 }
	public double getComplianceTodayPercentage() {
		return complianceTodayPercentage;
	}
	public void setComplianceTodayPercentage(double complianceTodayPercentage) {
		this.complianceTodayPercentage = complianceTodayPercentage;
	}
	public int getCovVal1() {
		return covVal1;
	}
	public void setCovVal1(int covVal1) {
		this.covVal1 = covVal1;
	}
	public int getCovVal2() {
		return covVal2;
	}
	public void setCovVal2(int covVal2) {
		this.covVal2 = covVal2;
	}
	public int getCompVal1() {
		return compVal1;
	}
	public void setCompVal1(int compVal1) {
		this.compVal1 = compVal1;
	}
	public int getCompVal2() {
		return compVal2;
	}
	public void setCompVal2(int compVal2) {
		this.compVal2 = compVal2;
	}
	public int getSelectedThreshold() {
		return selectedThreshold;
	}
	public void setSelectedThreshold(int selectedThreshold) {
		this.selectedThreshold = selectedThreshold;
	}
	public String getControlName() {
		return controlName;
	}
	public void setControlName(String controlName) {
		this.controlName = controlName;
	}
	public RadarHistorical getRadarHistorical() {
		return radarHistorical;
	}
	public void setRadarHistorical(RadarHistorical radarHistorical) {
		this.radarHistorical = radarHistorical;
	}
	 
	 
	 
	 

}
