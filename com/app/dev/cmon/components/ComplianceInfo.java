package com.app.dev.cmon.components;

import java.sql.Date;

public class ComplianceInfo {

	private int packetId;
	private String hostName;
	private String domain ;
	private String country;
	private String assetTypeName;
	private String versionName;
	private Date insertDate;
	private double score;
	private int isNew;
	private String ip; 
	private int isLaptop;
	private Date createDate;
	private Date lastLogon;
	private String OUName;
	private int isComply;
	private int isCoveredByControl;
	private int isManualCovered;
	private String extraValue;
	private Date evaluationDate;
	private String versionParentName;
	private String applicationName;
	private int expireInDays;
	private int subViewId;
	private String locationCode ;
	public double controlValue ;
	private String excludedReason;
	private Date syncDate;

	
	public int getPacketId() {
		return packetId;
	}
	public void setPacketId(int packetId) {
		this.packetId = packetId;
	}
	public String getFull() {
		return this.getHostName()+" "+this.getExtraValue()+" "+this.getControlValue()+" "+this.getIsComply();
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getAssetTypeName() {
		return assetTypeName;
	}
	public void setAssetTypeName(String assetTypeName) {
		this.assetTypeName = assetTypeName;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public Date getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public int getIsNew() {
		return isNew;
	}
	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}
	
	
	public int getIsLaptop() {
		return isLaptop;
	}
	public void setIsLaptop(int isLaptop) {
		this.isLaptop = isLaptop;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getLastLogon() {
		return lastLogon;
	}
	public void setLastLogon(Date lastLogon) {
		this.lastLogon = lastLogon;
	}
	public String getOUName() {
		return OUName;
	}
	public void setOUName(String oUName) {
		OUName = oUName;
	}
	public int getIsComply() {
		return isComply;
	}
	public void setIsComply(int isComply) {
		this.isComply = isComply;
	}
	public int getIsCoveredByControl() {
		return isCoveredByControl;
	}
	public void setIsCoveredByControl(int isCoveredByControl) {
		this.isCoveredByControl = isCoveredByControl;
	}
	public int getIsManualCovered() {
		return isManualCovered;
	}
	public void setIsManualCovered(int isManualCovered) {
		this.isManualCovered = isManualCovered;
	}
	public String getExtraValue() {
		
		return extraValue;
	}
	public void setExtraValue(String extraValue) {
		this.extraValue = extraValue;
	}
	public Date getEvaluationDate() {
		return evaluationDate;
	}
	public void setEvaluationDate(Date evaluationDate) {
		this.evaluationDate = evaluationDate;
	}
	public String getVersionParentName() {
		return versionParentName;
	}
	public void setVersionParentName(String versionParentName) {
		this.versionParentName = versionParentName;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public int getExpireInDays() {
		return expireInDays;
	}
	public void setExpireInDays(int expireInDays) {
		this.expireInDays = expireInDays;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getSubViewId() {
		return subViewId;
	}
	public void setSubViewId(int subViewId) {
		this.subViewId = subViewId;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public double getControlValue() {
		return controlValue;
	}
	public void setControlValue(double controlValue) {
		this.controlValue = controlValue;
	}

	public String getExcludedReason() {
		return excludedReason;
	}
	public void setExcludedReason(String excludedReason) {
		this.excludedReason = excludedReason;
	}
	public Date getSyncDate() {
		return syncDate;
	}
	public void setSyncDate(Date syncDate) {
		this.syncDate = syncDate;
	}
}
