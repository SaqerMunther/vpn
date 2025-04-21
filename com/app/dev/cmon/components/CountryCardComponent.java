package com.app.dev.cmon.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jk.util.JK;

public class CountryCardComponent {
	private List<CountryCard> cCards = new ArrayList<>();
	private static HashMap<String, String>countryMapsUrls = new HashMap<>();
	
	static {
		String[] countries = {"ALL", "JO", "EG", "PS", "AE", "LB","QA","BH","YE","MA","DZ","SG","CN","GD","AB"};
		//{"qa","bh","ye","ma","dz","sg","cn"}
		String[] counrtiesUrls = {
				"images/ab-circle.png",
				"flags/jor-flag.png",
				"flags/egy-flag.png",
				"flags/pal-flag.png",
				"flags/uae-flag.png",
				"flags/leb-flag.png",
				"flags/qat-flag.png",
				"flags/bah-flag.png",
				"flags/yem-flag.png",
				"flags/mor-flag.png",
				"flags/alg-flag.png",
				"flags/sin-flag.png",
				"flags/chi-flag.png",
				"images/gdr-flag.png",
				""
		};
		
		for (int i = 0; i < countries.length; i++) {
			countryMapsUrls.put(countries[i], counrtiesUrls[i]);
		}
	}
	
	public CountryCardComponent(Map<String, List<AssetInfo>> assetMap, Map<String, List<ComplianceInfo>> complianceMap,String threshold) {
		cCards.add(new CountryCard(countryMapsUrls.get("ALL"), "ALL",assetMap,complianceMap,threshold));
		cCards.add(new CountryCard(countryMapsUrls.get("JO"), "JO",assetMap,complianceMap,threshold));
		cCards.add(new CountryCard(countryMapsUrls.get("EG"), "EG",assetMap,complianceMap,threshold));
		cCards.add(new CountryCard(countryMapsUrls.get("PS"), "PS",assetMap,complianceMap,threshold));
		cCards.add(new CountryCard(countryMapsUrls.get("AE"), "AE",assetMap,complianceMap,threshold));
		cCards.add(new CountryCard(countryMapsUrls.get("LB"), "LB",assetMap,complianceMap,threshold));
		cCards.add(new CountryCard(countryMapsUrls.get("QA"), "QA",assetMap,complianceMap,threshold));
		cCards.add(new CountryCard(countryMapsUrls.get("BH"), "BH",assetMap,complianceMap,threshold));
		cCards.add(new CountryCard(countryMapsUrls.get("YE"), "YE",assetMap,complianceMap,threshold));
		cCards.add(new CountryCard(countryMapsUrls.get("MA"), "MA",assetMap,complianceMap,threshold));
		cCards.add(new CountryCard(countryMapsUrls.get("DZ"), "DZ",assetMap,complianceMap,threshold));
		cCards.add(new CountryCard(countryMapsUrls.get("SG"), "SG",assetMap,complianceMap,threshold));
		cCards.add(new CountryCard(countryMapsUrls.get("CN"), "CN",assetMap,complianceMap,threshold));
		cCards.add(new CountryCard(countryMapsUrls.get("AB"), "AB",assetMap,complianceMap,threshold));


	}

	public List<CountryCard> getcCards() {
		return cCards;
	}

	public static HashMap<String, String> getCountryMapsUrls() {
		return countryMapsUrls;
	}
}
