package com.app.dev.cmon.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.app.dev.cmon.components.Countres;
import com.app.dev.cmon.components.ViewInfo;
import com.app.dev.cmon.components.Views;
import com.app.dev.cmon.controllers.DataAccess;
import com.app.dev.cmon.controllers.RadarController;
import com.app.dev.cmon.controllers.RadarHistorical;
import com.app.dev.cmon.utilites.Commons;
import com.arabbank.dev.utility.Triple;
import com.arabbank.devf.cmon.controllers.portal.CmonManagedBean;

import javax.annotation.PostConstruct;

@SessionScoped 
@ManagedBean(name = "dataMap")
public class ProcessHeaderAndCardMap extends CmonManagedBean implements Serializable {
	private Commons commons = new Commons();
	private DataAccess da = new DataAccess();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private RadarController radar = ((RadarController) getManagedBeanByName("radarController"));
	
	private Map<String, List<ViewInfo>> leftCardDataMap = new HashMap<String, List<ViewInfo>>();
	private List<ViewInfo> listOfTodayData;
	private List<ViewInfo> headeData;
	private HashMap<String, ViewInfo> headerMap = new HashMap<String, ViewInfo>();
	private HashMap<String, Double> countryValueMap = new HashMap<>();
	private HashMap<String, Integer> covHeaderList1;
	private HashMap<String, Integer> compHeaderList1;
	
	@PostConstruct
	public void init() {
		filterHeaderData();	
		getLeftCardInfo();
		setCountryValue();
	}
    
	private void filterHeaderData() {
		if(headeData == null) {
			headeData = da.getHeaderData();
		}
		
		for (ViewInfo item : headeData) {
			item.setRadarHistorical(getRadarHistoryLeftCard(item.getId(), item.getControlName(), item.getCountry()));
			headerMap.put(item.getId() + "-" + item.getSupViewID() + "-" + item.getCountry(), item);
		}
	}
	
	private void getLeftCardInfo() {
		listOfTodayData = da.getHistoryData(radar.getUsername(),sdf.format(radar.getDate1()).trim());
	    RadarHistorical radarHist = new RadarHistorical();
	    radarHist.setCompVal1(0);
	    radarHist.setCompVal2(0);
	    radarHist.setCovVal1(0);
	    radarHist.setCovVal2(0);

	    leftCardDataMap = new HashMap<>();
	    
	    List<String> countries = commons.getCountries();

	    for (String country : countries) {
	        List<ViewInfo> filteredList = listOfTodayData.stream()
	                .filter(item -> !item.getControlName().contains("Symantec -") && item.getCountry().equalsIgnoreCase(country))
	                .map(item -> {
	                    item.setRadarHistorical(radarHist);
	                    return item;
	                })
	                .collect(Collectors.toList());

	        leftCardDataMap.put(country, filteredList);
	    }
	}
	
	private void setCountryValue() {
		String dateStr = sdf.format(radar.getDate1()).trim();
		List<Countres> valueList = da.getCountryValue(dateStr);
		if(valueList != null && valueList.size() >0 ) {
			for(Countres item: valueList) {
				countryValueMap.put(item.getViewID() + "-" + item.getViewName() + "-" + item.getCountry() + "-" + item.getDate(), item.getValue());
			}
		}
	}
	////////////////////////////////////////////////////////////////////////

	private RadarHistorical getRadarHistoryLeftCard(int id, String name, String country) {
		RadarHistorical radarHist = new RadarHistorical();
		long covVal1 = 0;
		long covVal2 = 0;
		radarHist.setCovVal1((int) covVal1);
		radarHist.setCovVal2((int) covVal2);
		long compVal1 = 0;
		long compVal2 = 0;
		radarHist.setCompVal1((int) compVal1);
		radarHist.setCompVal2((int) compVal2);
		return radarHist;
	}
	///////////////////////////////////////////////////////////////////////
	public Triple<Integer, Integer, Double> getHedarTodayAndYesterday(Views view, int subViewId) {
		if(headerMap == null) {
			filterHeaderData();
		}
		Triple<Integer, Integer, Double> item = new Triple<>();
		Commons comm = new Commons();
		int val1 = 0;
		int val2 = 0;
		if ( !radar.getViewText().contains("Symantec")) {
			val1 = headerMap.get(view.getId() + "-" + subViewId + "-" + (radar.getCountry().equals("") ? "ALL" : radar.getCountry())) == null ? 0 :
				headerMap.get(view.getId() + "-" + subViewId + "-" + (radar.getCountry().equals("") ? "ALL" : radar.getCountry())).getCovFirst();
			val2 = headerMap.get(view.getId() + "-" + subViewId + "-" + (radar.getCountry().equals("") ? "ALL" : radar.getCountry())) == null ? 0 :
				headerMap.get(view.getId() + "-" + subViewId + "-" + (radar.getCountry().equals("") ? "ALL" : radar.getCountry())).getCovSecond();
		}
		item.setFirst(val1 + val2);
		int covVal1 = 0;
		int covVal2 = 0;
		item.setSecond(covVal1 + covVal2);
		item.setThird(item.getFirst() / (double) (item.getSecond() + item.getFirst()) * 100);

		return item;
	}
	///////////////////////////////////////////////////////////
	public Triple<Integer, Integer, Double> getCompHedarTodayAndYesterday(Views view, int subViewId) {
		if(headerMap == null) {
			filterHeaderData();
		}
		Triple<Integer, Integer, Double> item = new Triple<>();
		int val1 = 0;
		int val2 = 0;
		if ( !radar.getViewText().contains("Symantec")) {
			val1 = headerMap.get(view.getId() + "-" + subViewId + "-" + (radar.getCountry().equals("") ? "ALL" : radar.getCountry())) == null ? 0 :
				headerMap.get(view.getId() + "-" + subViewId + "-" + (radar.getCountry().equals("") ? "ALL" : radar.getCountry())).getCompFirst();
			val2 = headerMap.get(view.getId() + "-" + subViewId + "-" + (radar.getCountry().equals("") ? "ALL" : radar.getCountry())) == null ? 0 :
				headerMap.get(view.getId() + "-" + subViewId + "-" + (radar.getCountry().equals("") ? "ALL" : radar.getCountry())).getCompSecond();
		}
		item.setFirst(val1 + val2);
		int compVal1 = 0;
		int compVal2 = 0;
		item.setSecond( compVal1 + compVal2);
		item.setThird(item.getFirst() / (double) (item.getSecond() + item.getFirst()) * 100);

		return item;
	}
	
	//////////////////////////////////////////////////////////

	public Triple<Integer, Integer, Double> getCovTodayAndYesterday(Views view, int subViewId) {
		Triple<Integer, Integer, Double> item = new Triple<>();
		Commons comm = new Commons();
		int val1 = 0;
		int val2 = 0;
		if ( !radar.getViewText().contains("Symantec")) {
			val1 = covHeaderList1.get(view.getId() + "-" + (radar.getCountry().equals("") ? -1 : subViewId) + "-"
					+ (radar.getCountry().equals("") ? "ALL" : radar.getCountry())) == null ? 0
							: covHeaderList1.get(view.getId() + "-" + (radar.getCountry().equals("") ? -1 : subViewId) + "-"
									+ (radar.getCountry().equals("") ? "ALL" : radar.getCountry()));
		}
		item.setFirst(val1);

		int covVal1 = 0;
		int covVal2 = 0;
		item.setSecond(covVal1 + covVal2);
		item.setThird(item.getFirst() / (double) (item.getSecond() + item.getFirst()) * 100);

		return item;
	}

	public Triple<Integer, Integer, Double> getCompTodayAndYesterday(Views view, int subViewId) {
		Triple<Integer, Integer, Double> item = new Triple<>();
		int val1 = 0;

		if (view.getId() != 4 && view.getId() != 2 && view.getId() != 3 && view.getId() != 7
				&& !radar.getViewText().contains("Symantec")) {
			val1 = compHeaderList1.get(view.getId() + "-" + (radar.getCountry().equals("") ? -1 : subViewId) + "-"
					+ (radar.getCountry().equals("") ? "ALL" : radar.getCountry())) == null ? 0
							: compHeaderList1.get(view.getId() + "-" + (radar.getCountry().equals("") ? -1 : subViewId) + "-"
									+ (radar.getCountry().equals("") ? "ALL" : radar.getCountry()));
		}
		item.setFirst(val1);
		int compVal1 = 0;
		int compVal2 = 0;
		item.setSecond(compVal1 + compVal2);
		item.setThird(item.getFirst() / (double) (item.getSecond() + item.getFirst()) * 100);

		return item;
	}
	
	////////////////////////////////////
	
	public List<Integer> getCountPerControl(int subViewId) {
		if(headerMap == null) {
			filterHeaderData();
		}
		int Val1;
		int Val2;
		int Val3;
		int Val4;
		List<Integer> item = new ArrayList<Integer>();
		Val1 = headerMap.get(radar.getViewId() + "-" + subViewId + "-" + (radar.getCountry().equalsIgnoreCase("") ? "ALL" : radar.getCountry())) == null ? 0 :
			headerMap.get(radar.getViewId() + "-" + subViewId + "-" + (radar.getCountry().equalsIgnoreCase("") ? "ALL" : radar.getCountry())).getCovFirst();
		Val2 = headerMap.get(radar.getViewId() + "-" + subViewId + "-" + (radar.getCountry().equalsIgnoreCase("") ? "ALL" : radar.getCountry())) == null ? 0 :
				headerMap.get(radar.getViewId() + "-" + subViewId + "-" + (radar.getCountry().equalsIgnoreCase("") ? "ALL" : radar.getCountry())).getCovSecond();
		Val3 = headerMap.get(radar.getViewId() + "-" + subViewId + "-" + (radar.getCountry().equalsIgnoreCase("") ? "ALL" : radar.getCountry())) == null ? 0 :
				headerMap.get(radar.getViewId() + "-" + subViewId + "-" + (radar.getCountry().equalsIgnoreCase("") ? "ALL" : radar.getCountry())).getCompFirst();
		Val4 = headerMap.get(radar.getViewId() + "-" + subViewId + "-" + (radar.getCountry().equalsIgnoreCase("") ? "ALL" : radar.getCountry())) == null ? 0 :
				headerMap.get(radar.getViewId() + "-" + subViewId + "-" + (radar.getCountry().equalsIgnoreCase("") ? "ALL" : radar.getCountry())).getCompSecond();

		item.add(Val1);
		item.add(Val2);
		item.add(Val3);
		item.add(Val4);
		return item;
	}
	
	
	//////////////////////////////////////////////////////////////

	public double getCompScorePerCountry(String country) {
		String key = radar.getViewId() + "-" + radar.getViewText() + "-" + country + "-" + sdf.format(radar.getDate1()).trim();
		double result = 0.0;
			if (countryValueMap.get(key) != null) {
				result = countryValueMap.get(key);
			}
		return result;
	}
	
	//////////////////////////////////////////////////////////////
	
	public List<ViewInfo> getLeftCardData() {
		return leftCardDataMap.get(radar.getCurCountry());
	}
	
	/////////////////////////////////////////////////////////
	public void setViewIdAll(int viewId, String viewName) {
		radar.setViewId(viewId);
		radar.setSubViewId(-1);
		radar.setViewText(viewName);
		radar.getSelectedView().setId(viewId);
		radar.getSelectedView().setName(viewName);
//		excluded.getExcludedAssets();
	}

	////////////////////////////////////////////////////
	public void setViewData(Views view) {
		radar.setViewId(view.getId());
		radar.setSubViewId(-1);
		radar.setViewText(view.getName());
		radar.setSelectedView(view);
//		excluded.getExcludedAssets();
	}
	/////////////////////////////////////////////////////////	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
