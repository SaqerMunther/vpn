package com.app.dev.cmon.components;

public class TotalAssetsHistory {
	
	private int subviewId;
    private String locationCode;
    private String label;
    private long totalCovered;
    private long totalCount;
	public int getSubviewId() {
		return subviewId;
	}
	public void setSubviewId(int subviewId) {
		this.subviewId = subviewId;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public long getTotalCovered() {
		return totalCovered;
	}
	public void setTotalCovered(long totalCovered) {
		this.totalCovered = totalCovered;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
    
}
