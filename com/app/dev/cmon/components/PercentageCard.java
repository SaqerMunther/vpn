package com.app.dev.cmon.components;

import java.util.Random;

public class PercentageCard {
	private double percentage;
	private double curNumber;
	private int maxNumber;
	private String type;
	
	public PercentageCard(double num1 , int num2,String type , boolean isPercent ) {
		Random r = new Random();
		int low = 50;
		int high = 90;
		this.type = type;
		if(isPercent)
			this.percentage = num1/(double)num2 * 100;
		else 
		this.percentage = num1/(double)num2;
		this.curNumber = num1;
		this.maxNumber = num2;
	}
	
	public double getPercentage() {
		return percentage;
	}
	public double getCurNumber() {
		return curNumber;
	}
	public int getMaxNumber() {
		return maxNumber;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
}
