package com.app.dev.cmon.components;

import com.app.dev.cmon.controllers.RadarHistorical;

public class ViewInfo {
	
	private Long totalSum;
	private Long green;
	private Long Orange;
	private Long Red;
	private String Type;
	private int CovFirst;
	private int CovSecond;
	private int CompFirst;
	private int CompSecond;
	private String country;
	private String controlName;
	private String fullName;
	private RadarHistorical radarHistorical;
	private int id;
	private int supViewID;
	private Long it;
	private Long risk;
	
	private Long greenExist;
	private Long orangeExist;
	private Long greenNew;
	private Long orangeNew;
	private Long redExist;
	private Long redNew;
	private int controlId;
	private int count;
	private String color;
	private String typeSort;
	private String date;
	
	
	public Long getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(Long totalSum) {
		this.totalSum = totalSum;
	}

	public Long getGreen() {
		return green;
	}

	public void setGreen(Long green) {
		this.green = green;
	}

	public Long getOrange() {
		return Orange;
	}

	public void setOrange(Long orange) {
		Orange = orange;
	}

	public Long getRed() {
		return Red;
	}

	public void setRed(Long red) {
		Red = red;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public int getCovFirst() {
		return CovFirst;
	}

	public void setCovFirst(int covFirst) {
		CovFirst = covFirst;
	}

	public int getCovSecond() {
		return CovSecond;
	}

	public void setCovSecond(int covSecond) {
		CovSecond = covSecond;
	}

	public int getCompFirst() {
		return CompFirst;
	}

	public void setCompFirst(int compFirst) {
		CompFirst = compFirst;
	}

	public int getCompSecond() {
		return CompSecond;
	}

	public void setCompSecond(int compSecond) {
		CompSecond = compSecond;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSupViewID() {
		return supViewID;
	}

	public void setSupViewID(int supViewID) {
		this.supViewID = supViewID;
	}

	public Long getIt() {
		return it;
	}

	public void setIt(Long it) {
		this.it = it;
	}

	public Long getRisk() {
		return risk;
	}

	public void setRisk(Long risk) {
		this.risk = risk;
	}

	public Long getGreenExist() {
		return greenExist;
	}

	public void setGreenExist(Long greenExist) {
		this.greenExist = greenExist;
	}

	public Long getOrangeExist() {
		return orangeExist;
	}

	public void setOrangeExist(Long orangeExist) {
		this.orangeExist = orangeExist;
	}

	public Long getGreenNew() {
		return greenNew;
	}

	public void setGreenNew(Long greenNew) {
		this.greenNew = greenNew;
	}

	public Long getOrangeNew() {
		return orangeNew;
	}

	public void setOrangeNew(Long orangeNew) {
		this.orangeNew = orangeNew;
	}

	public Long getRedExist() {
		return redExist;
	}

	public void setRedExist(Long redExist) {
		this.redExist = redExist;
	}

	public Long getRedNew() {
		return redNew;
	}

	public void setRedNew(Long redNew) {
		this.redNew = redNew;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getControlId() {
		return controlId;
	}

	public void setControlId(int controlId) {
		this.controlId = controlId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public String getTypeSort() {
		return typeSort;
	}

	public void setTypeSort(String typeSort) {
		this.typeSort = typeSort;
	}
	
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "ViewInfo [totalSum=" + totalSum + ", green=" + green + ", Orange=" + Orange + ", Red=" + Red + ", Type="
				+ Type + ", country=" + country + ", supViewID=" + supViewID + ", it=" + it + ", risk=" + risk
				+ ", controlId=" + controlId + ", color=" + color + ", typeSort=" + typeSort + "]";
	}



}
