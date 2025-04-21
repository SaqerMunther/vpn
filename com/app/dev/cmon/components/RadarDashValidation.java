package com.app.dev.cmon.components;

import java.time.LocalDateTime;

public class RadarDashValidation {
	private long radarDashValidationID;
    private int syncID;
    private int locationID;
    private int controlID;
    private boolean isComplianceMatch;
    private boolean isCoverageMatch;
    private double radarCompliancePercent;
    private double dashCompliancePercent;
    private double radarCoveragePercent;
    private double dashboardCoveragePercent;
    private double validationThreshold;
    private LocalDateTime insertDate;
    
    
	public long getRadarDashValidationID() {
		return radarDashValidationID;
	}
	public void setRadarDashValidationID(long radarDashValidationID) {
		this.radarDashValidationID = radarDashValidationID;
	}
	public int getSyncID() {
		return syncID;
	}
	public void setSyncID(int syncID) {
		this.syncID = syncID;
	}
	public int getLocationID() {
		return locationID;
	}
	public void setLocationID(int locationID) {
		this.locationID = locationID;
	}
	public int getControlID() {
		return controlID;
	}
	public void setControlID(int controlID) {
		this.controlID = controlID;
	}
	public boolean isComplianceMatch() {
		return isComplianceMatch;
	}
	public void setComplianceMatch(boolean isComplianceMatch) {
		this.isComplianceMatch = isComplianceMatch;
	}
	public boolean isCoverageMatch() {
		return isCoverageMatch;
	}
	public void setCoverageMatch(boolean isCoverageMatch) {
		this.isCoverageMatch = isCoverageMatch;
	}
	public double getRadarCompliancePercent() {
		return radarCompliancePercent;
	}
	public void setRadarCompliancePercent(double radarCompliancePercent) {
		this.radarCompliancePercent = radarCompliancePercent;
	}
	public double getDashCompliancePercent() {
		return dashCompliancePercent;
	}
	public void setDashCompliancePercent(double dashCompliancePercent) {
		this.dashCompliancePercent = dashCompliancePercent;
	}
	public double getRadarCoveragePercent() {
		return radarCoveragePercent;
	}
	public void setRadarCoveragePercent(double radarCoveragePercent) {
		this.radarCoveragePercent = radarCoveragePercent;
	}
	public double getDashboardCoveragePercent() {
		return dashboardCoveragePercent;
	}
	public void setDashboardCoveragePercent(double dashboardCoveragePercent) {
		this.dashboardCoveragePercent = dashboardCoveragePercent;
	}
	public double getValidationThreshold() {
		return validationThreshold;
	}
	public void setValidationThreshold(double validationThreshold) {
		this.validationThreshold = validationThreshold;
	}
	public LocalDateTime getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(LocalDateTime insertDate) {
		this.insertDate = insertDate;
	}
    
}
