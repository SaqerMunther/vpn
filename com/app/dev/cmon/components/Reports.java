package com.app.dev.cmon.components;

public class Reports {

	private int reportId;
	private String reportName;
	private String reportDesc;
	private String query;
	private int downloadable;
	
	public int getReportId() {
		return reportId;
	}
	public void setReportId(int reportId) {
		this.reportId = reportId;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getReportDesc() {
		return reportDesc;
	}
	public void setReportDesc(String reportDesc) {
		this.reportDesc = reportDesc;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public int getDownloadable() {
		return downloadable;
	}
	public void setDownloadable(int downloadable) {
		this.downloadable = downloadable;
	}
	
	
}
