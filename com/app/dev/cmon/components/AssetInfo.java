package com.app.dev.cmon.components;

import java.io.Serializable;
import java.sql.Date;

public class AssetInfo{
   
	private String domain;
	private String hostName;
	private String country;
	private Date scanDate ; 
	private int isNew ; 
	private int isScanned ;
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
	private String excludedReason;
	private Date syncDate;
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Date getScanDate() {
		return scanDate;
	}
	public void setScanDate(Date scanDate) {
		this.scanDate = scanDate;
	}
	public int getIsNew() {
		return isNew;
	}
	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}
	public int getIsScanned() {
		return isScanned;
	}
	public void setIsScanned(int isScanned) {
		this.isScanned = isScanned;
	}

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
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
