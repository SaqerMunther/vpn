package com.app.dev.cmon.controllers;

public class RadarHistorical {
	private int covVal1 = 0;
	private int covVal2 = 0;
	private int compVal1 = 0;
	private int compVal2 = 0;
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
	@Override
	public String toString() {
		return "RadarHistorical [covVal1=" + covVal1 + ", covVal2=" + covVal2 + ", compVal1=" + compVal1 + ", compVal2="
				+ compVal2 + "]";
	}
	

}
