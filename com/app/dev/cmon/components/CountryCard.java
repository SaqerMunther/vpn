package com.app.dev.cmon.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CountryCard {
	private String cardImageUrl;
	private String countryName;
	private List<CountryCardRow> dataRows = new ArrayList<>();
	private  List<String> dataLables = new ArrayList<>();
	
	private Map<String,List<ComplianceInfo>> complianceMap;
	public CountryCard(String imageUrl, String countryName,Map<String,List<AssetInfo>> assetMap ,Map<String,List<ComplianceInfo>> complianceMap,String thresold) {
		dataLables.add("EndPoints");
		dataLables.add("Servers");
		dataLables.add("DataBases");
		this.cardImageUrl = imageUrl;
		this.countryName = countryName;
		
		for(String label : dataLables) {
			this.dataRows.add(new CountryCardRow(label,countryName,assetMap,complianceMap,thresold));
		}
	}

	public String getCardImageUrl() {
		return cardImageUrl;
	}
	
	public String getCountryName() {
		return countryName;
	}

	public List<CountryCardRow> getDataRows() {
		return dataRows;
	}
}
