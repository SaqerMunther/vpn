package com.app.dev.cmon.service;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.app.dev.cmon.components.AllRadarData;
import com.app.dev.cmon.components.AssetInfo;
import com.app.dev.cmon.components.ComplianceInfo;
import com.app.dev.cmon.controllers.DataAccess;
import com.app.dev.cmon.controllers.RadarController;
import com.arabbank.devf.cmon.controllers.portal.CmonManagedBean;

@ManagedBean(name = "radarExcluded")
@SessionScoped
public class ProcessExcluded extends CmonManagedBean{
	private DataAccess da = new DataAccess();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private RadarController radar = ((RadarController) getManagedBeanByName("radarController"));
	
	private List<AllRadarData> radarInfo = new ArrayList<AllRadarData>();
	private List<AssetInfo> excludedCovList;
	private List<ComplianceInfo> excludedCompList;
	private List<String> colorTypes = Arrays.asList("Orange-Exist", "Orange-New", "Red-Exist", "Red-New");
	private Map<String, List<AssetInfo>> exeludedCovMap = new HashMap<String, List<AssetInfo>>();
	private Map<String, List<ComplianceInfo>> exeludedCompMap = new HashMap<String, List<ComplianceInfo>>();
	
    @PostConstruct
    public void init() {
    	setExcludedAssets();
    	radarInfo = radar.getRadarInfo();
    }
    ////////////////////////////////////////////////////////
	
	private void setExcludedAssets() {
		excludedCovList = da.getExcludeList(radar.getViewId(), radar.getSubViewId(), radar.getCountry());
		excludedCompList = da.getComplianceExecludedList(radar.getViewId(), radar.getSubViewId(), radar.getCountry());
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("UTC")).plusHours(3);

		for (String colorType : colorTypes) {
			AllRadarData circleCovInfo = getCircleInfo(colorType, "Cov");
			AllRadarData circleCompInfo = getCircleInfo(colorType, "Comp");
			if (isCircleInfoValid(circleCovInfo)) {
				List<AssetInfo> filteredAssets = filterAssets(excludedCovList, circleCovInfo, currentTime);
				exeludedCovMap.put(colorType, filteredAssets);
			}
			if (isCircleInfoValid(circleCompInfo)) {
				List<ComplianceInfo> filteredAssets = filterCompliance(excludedCompList, circleCovInfo, currentTime);
				exeludedCompMap.put(colorType, filteredAssets);
			}
		}
	}
	
	private boolean isCircleInfoValid(AllRadarData circleInfo) {
		return circleInfo != null;
	}

	private AllRadarData getCircleInfo(String colorType, String asset) {
		return radarInfo.stream()
				.filter(radar -> radar.getViewID() == radar.getViewID() && radar.getColorType().equalsIgnoreCase(colorType) && radar.getIDName().contains(asset))
				.findFirst().orElse(new AllRadarData());
	}

	
	private List<AssetInfo> filterAssets(List<AssetInfo> assets, AllRadarData circleInfo, LocalDateTime currentTime) {
		
		if (assets == null || assets.isEmpty() || assets.size() == 0) {
	        return Collections.emptyList(); 
	    }
		
		return assets.stream().filter(asset -> {
			Date date;
			try {
				Method method = asset.getClass().getMethod("get" + circleInfo.getColumnDateName());
				date = (Date) method.invoke(asset);
			} catch (Exception e) {
				date = asset.getScanDate();
			}
			return (circleInfo.getIsScanned() == -1 || asset.getIsScanned() == circleInfo.getIsScanned())
					&& (circleInfo.getIsNew() == -1 || asset.getIsNew() == circleInfo.getIsNew())
					&& (circleInfo.getIsComply() == -1 || asset.getIsComply() == circleInfo.getIsComply())
					&& (circleInfo.getIsCovered() == -1 || asset.getIsCoveredByControl() == circleInfo.getIsCovered())
					&& date.after(convertToDate(currentTime.minusMonths(circleInfo.getEnd())))
					&& date.before(convertToDate(currentTime.minusMonths(circleInfo.getStart())));
		}).collect(Collectors.toList());
	}
	
	
	private List<ComplianceInfo> filterCompliance(List<ComplianceInfo> assets, AllRadarData circleInfo, LocalDateTime currentTime) {
		
		if (assets == null || assets.isEmpty() || assets.size() == 0) {
	        return Collections.emptyList(); 
	    }
		
		return assets.stream().filter(asset -> {
			double score;
			try {
				Method method = asset.getClass().getMethod("get" + circleInfo.getColumnDateName());
				score = (double) method.invoke(asset);
			} catch (Exception e) {
				score = asset.getScore();
			}
			return (circleInfo.getIsNew() == -1 || asset.getIsNew() == circleInfo.getIsNew())
					&& (circleInfo.getIsComply() == -1 || asset.getIsComply() == circleInfo.getIsComply())
					&& (circleInfo.getIsCovered() == -1 || asset.getIsCoveredByControl() == circleInfo.getIsCovered())
					&& score < circleInfo.getEnd()
					&& score > circleInfo.getStart();
		}).collect(Collectors.toList());
	}
	

	private static Date convertToDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	/////////////////////////////////////////////////////////////////////////////
	public List<ComplianceInfo> getExcludedCompliance(String name) {
		if (exeludedCompMap.get(name) == null) {
			return new ArrayList<ComplianceInfo>();
		} else {
			radar.setCompDataList(exeludedCompMap.get(name));
			return exeludedCompMap.get(name);
		}
	}
	
	public List<AssetInfo> getExcludedCoverage(String name) {
		if (exeludedCovMap.get(name) == null) {
			return new ArrayList<AssetInfo>();
		} else {
			radar.setCovDataList(exeludedCovMap.get(name));
			return exeludedCovMap.get(name);
		}
	}	
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
