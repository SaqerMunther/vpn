package com.app.dev.cmon.utilites;

public enum Intervals {
    MONTH("month"),DAY("day"),YEAR("year"),WEEK("week");
	private String val ;
	
	Intervals(String val) {
		this.val = val;
	}
	
	public String getVal() {
		return val;
	}
	
}
