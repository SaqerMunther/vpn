package com.app.dev.cmon.components;

import java.util.List;

import com.arabbank.dev.utility.Pair;

public class RadarBox {
	
	private Number count ; 
	private Number count2 ; 
	private int isNew;
	private List<Pair<String,Number>> rangeValue; 
	
	
	public Number getCount() {
		return count;
	}
	public void setCount(Number count) {
		this.count = count;
	}
	public List<Pair<String, Number>> getRangeValue() {
		return rangeValue;
	}
	public void setRangeValue(List<Pair<String, Number>> rangeValue) {
		this.rangeValue = rangeValue;
	}
	public int getIsNew() {
		return isNew;
	}
	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}
	public Number getCount2() {
		return count2;
	}
	public void setCount2(Number count2) {
		this.count2 = count2;
	}
	
	
	
	
}
