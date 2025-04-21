
package com.app.dev.cmon.components;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.app.dev.cmon.controllers.DataAccess;
import com.app.dev.cmon.controllers.DataRepo;
import com.app.dev.cmon.controllers.MB_Main;
import com.app.dev.cmon.utilites.Intervals;
import com.app.dev.cmon.utilites.RadarThresholds;
import com.jk.util.JK;

public class CountryCardRow {
	private int tData;
	private int rData;
	private String dataLabel;
	private DataAccess da = new DataAccess();
	private  Map<String,List<AssetInfo>> assetMap ;
	private Map<String,List<ComplianceInfo>> complianceMap ;
	private int selectedThresold;
	private RadarThresholds radarThresholds = new RadarThresholds() ;
	public CountryCardRow(String label, String country, Map<String,List<AssetInfo>> assetMap ,Map<String,List<ComplianceInfo>> complianceMap,String thresholds) {
		this.assetMap=assetMap;
		this.complianceMap=complianceMap;
		country = country.equals("ALL") ? "" : country;
		int low = 1;
		int high = 10000;
		setSelectedThresold(thresholds);
		if (label.contains("COV") && label.contains("NEW")) {
			this.tData = (int) getCovCount(1, 2, country, 0);
			this.rData = (int) getCovCount(2, 1000, country, 0);
		} else if (label.contains("COV") && !label.contains("NEW")) {
			this.tData = (int) getCovCount(3, 6, country, 1);
			this.rData = (int) getCovCount(6, 1000, country, 1);
		} else if (label.contains("Comp") && label.contains("NEW")) {
			this.tData = (int) getCompLianceCountByScore(selectedThresold-10, selectedThresold, 1, country);
			this.rData = (int) getCompLianceCountByScore(0, selectedThresold-10, 1, country);
		} else {
			this.tData = (int) getCompLianceCountByScore(selectedThresold-10, selectedThresold, 0, country);
			this.rData = (int) getCompLianceCountByScore(0, selectedThresold-10, 0, country);
		}
		this.dataLabel = label;
	}

	public int gettData() {
		return tData;
	}

	public int getrData() {
		return rData;
	}

	public String getDataLabel() {
		return dataLabel;
	}

	private long getCovCount(int startRange, int endRange, String country, int isNew) {
		country = country.equalsIgnoreCase("all") ? "" : country;
		LocalDate now = assetMap.get(country).get(0).getSyncDate().toLocalDate();

		if (assetMap.get(country) == null)
			return 0;
		long num = assetMap.get(country).stream().filter(p -> { // check if date is not
			if (p.getScanDate() != null)
				if (((p.getScanDate().toLocalDate().isAfter(now.minusMonths(endRange))
						&& p.getScanDate().toLocalDate().isBefore(now.minusMonths(startRange))

				) || p.getScanDate().toLocalDate().isEqual(now.minusMonths(startRange))
						)
						&& p.getIsScanned() == isNew)
					return true;
			return false;

		}).count();
		return num;
	}

	public long getCompLianceCountByScore(int startRange , int endRange,int isNew,String country) {
		LocalDate now = assetMap.get(country).get(0).getSyncDate().toLocalDate();
			if (complianceMap.get(country) == null)
				return 0;
			
			List<ComplianceInfo> complianceList =  complianceMap.get(country).stream().filter(p -> {
				if (p.getIsNew() == isNew && (p.getScore() >= startRange && p.getScore() < endRange))
					return true;
				else
					return false;
			})
					.collect(Collectors.toList());
			long num = complianceList.size();
			return num;
	}
	public void setSelectedThresold(String viewName) {
		if(viewName.toLowerCase().contains("endpoints"))
			this.selectedThresold = radarThresholds.ENDPOINTS;
		else if(viewName.toLowerCase().contains("servers"))
			this.selectedThresold = radarThresholds.SERVERS;
		else
			this.selectedThresold = radarThresholds.DB;
	}
}
