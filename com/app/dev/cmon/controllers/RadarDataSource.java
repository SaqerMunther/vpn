package com.app.dev.cmon.controllers;

import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.app.dev.cmon.components.AssetInfo;
import com.app.dev.cmon.components.ComplianceInfo;
import com.app.dev.cmon.components.LeftCard;
import com.app.dev.cmon.components.ViewInfo;
import com.app.dev.cmon.components.Views;
import com.app.dev.cmon.utilites.Commons;
import com.arabbank.dev.utility.Pair;
import com.arabbank.devf.cmon.controllers.portal.CmonManagedBean;
import com.arabbank.devf.cmon.controllers.portal.MB_Login;

import jakarta.inject.Inject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@ManagedBean(name = "RadarDataSource")
@SessionScoped
public class RadarDataSource extends CmonManagedBean{


	private DataAccess da;
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private Map<String, List<Pair<Integer, Double>>> compUpperValuesCache;
	private Map<String, List<Pair<Number, String>>> covTotalsCache;
	private List<Views> viewList;
	private List<ViewInfo> listOfTodayData;


	
	
	public RadarDataSource() {
		da = new DataAccess();
		reset();
	}

	@PostConstruct
	public void init() {
	}
	
	
	List<Pair<Integer, Double>> getComplianceUpperValue(int month, int viewId, int subViewId, String country,
			int isRisk) {
		String key = month + "" + viewId + "" + subViewId + "" + country + "" + isRisk;
		if (!compUpperValuesCache.containsKey(key)) {
			synchronized (this) {
				compUpperValuesCache.put(key, da.getCompUpper(month, viewId, subViewId, country, isRisk));
			}
		}
		return compUpperValuesCache.get(key);
	}

	public List<Pair<Number, String>> getCoverageTotal(int viewId, int subViewId, int startRange, int endRange,
			int month, String country) {
		String key = viewId + "" + subViewId + "" + startRange + "" + endRange + "" + month + "" + country;
		if (!covTotalsCache.containsKey(key)) {
			synchronized (this) {
				covTotalsCache.put(key, da.getCoverageTotal(viewId, subViewId, startRange, endRange, month, country));
			}
		}
		return covTotalsCache.get(key);
	}

	public List<ViewInfo> getListOfTodayData() {
		return listOfTodayData;
	}

	public void setListOfTodayData(List<ViewInfo> listOfTodayData) {
		this.listOfTodayData = listOfTodayData;
	}
	
	

	@Override
	public void reset() {
		compUpperValuesCache = new HashMap<String, List<Pair<Integer, Double>>>();
		covTotalsCache = new HashMap<String, List<Pair<Number, String>>>();
		
	}

}
