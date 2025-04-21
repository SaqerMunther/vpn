package com.app.dev.cmon.controllers;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import com.app.dev.cmon.cache.CacheService;
import com.app.dev.cmon.components.AllRadarData;
import com.app.dev.cmon.components.AssetInfo;
import com.app.dev.cmon.components.ComplianceInfo;
import com.app.dev.cmon.components.ViewInfo;
import com.app.dev.cmon.components.Views;
import com.app.dev.cmon.service.CycleValidationService;
import com.app.dev.cmon.service.ProcessExcluded;
import com.app.dev.cmon.service.ProcessHeaderAndCardMap;
import com.app.dev.cmon.service.ProcessMapHistory;
import com.app.dev.cmon.service.ProcessWeeklyData;
import com.app.dev.cmon.utilites.Commons;
import com.app.dev.cmon.utilites.RadarThresholds;
import com.arabbank.dev.utility.Pair;
import com.arabbank.devf.cmon.controllers.portal.CmonManagedBean;

@ManagedBean(name = "mbMain")
@ViewScoped
public class MB_Main extends CmonManagedBean {
	
	private String country = "";
	private String curCountry = "ALL";
	private int viewId;
	private int subViewId = -1;
	private String viewText;
	private String subViewText = "All";
	
	private DataAccess da = new DataAccess();
	
	private List<AssetInfo> assetList;
	private List<ComplianceInfo> complianceList;
	public Map<String, List<AssetInfo>> assetMap;
	public Map<String, List<AssetInfo>> excludeMap;
	public Map<String, List<ComplianceInfo>> complianceExcludeMap;
	public Map<String, List<ComplianceInfo>> complianceMap;
	private List<Views> viewList;
	private List<Views> symantecList = new ArrayList<Views>();
	
	private boolean isManual;
	private String COMPLIANCE_PATH_REDIRECT = "radar/inc/details-compliance.xhtml";
	private String COVERAGE_PATH_REDIRECT = "radar/inc/details-coverage.xhtml";
	private int selectedThresold;
	private LocalDate manualAssetScanDate;
	private LocalDate scanDate;
	private HashMap<String, List<RadarLeftCard>> radarLeftCardMap;
	private Views selectedView;
	HashMap<String, List<RadarLeftCard>> wsusLeftCardMap = new HashMap<String, List<RadarLeftCard>>();
	private LocalDate now;
	private List<ViewInfo> veiwDataComp;
	private List<ViewInfo> veiwDataCov;
	private List<AssetInfo> covDataList;
	private List<ComplianceInfo> compDataList;
	public Map<String, List<ViewInfo>> wsusleftCardDataMap = new HashMap<>();
	public List<ViewInfo> leftCardList = new ArrayList<>();
	public List<AssetInfo> timeList = new ArrayList<>();
	public HashMap<String, List<ViewInfo>> circleData = new HashMap<>();
	public List<AssetInfo> listPerWeek = new ArrayList<>();
	private Date date1 = new Date();
	private String contriName;
	private String circleDetail = "";
	public Map<String, Pair<Integer, Double>> scoreMap = new HashMap<String, Pair<Integer, Double>>();
	private int viewSymantecName;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String formattedDate;

	RadarController radar = ((RadarController) getManagedBeanByName("radarController"));
	RadarDataSource radarDataSource = ((RadarDataSource) getManagedBeanByName("RadarDataSource"));
	private AllRadarData filter;
	private List<String> countresAccess = new ArrayList<String>();

	Pair<Integer, Integer> pairCur;

	Commons commons = new Commons();

	/////////////////////////////////////////////////////////
	 // Injecting the session-scoped bean
	@ManagedProperty(value = "#{radarValidation}")
    private CycleValidationService radarValidation; 

	@ManagedProperty(value = "#{radarHistory}")
    private ProcessMapHistory processHistory; 

    @ManagedProperty(value = "#{dataMap}")
    private ProcessHeaderAndCardMap headerAndCardMap; 
    
    @ManagedProperty(value = "#{radarExcluded}")
    private ProcessExcluded radarExcluded; 
    
    @ManagedProperty(value = "#{radarWeekData}")
    private ProcessWeeklyData radarWeekData;
    
    //////////////////////////////////////////////////////////////
	public ProcessMapHistory getProcessHistory() {
		return processHistory;
	}

	public void setProcessHistory(ProcessMapHistory processHistory) {
		this.processHistory = processHistory;
	}

	public ProcessHeaderAndCardMap getHeaderAndCardMap() {
		return headerAndCardMap;
	}

	public void setHeaderAndCardMap(ProcessHeaderAndCardMap headerAndCardMap) {
		this.headerAndCardMap = headerAndCardMap;
	}

	public ProcessExcluded getRadarExcluded() {
		return radarExcluded;
	}

	public void setRadarExcluded(ProcessExcluded radarExcluded) {
		this.radarExcluded = radarExcluded;
	}
	
	public ProcessWeeklyData getRadarWeekData() {
		return radarWeekData;
	}

	public void setRadarWeekData(ProcessWeeklyData radarWeekData) {
		this.radarWeekData = radarWeekData;
	}
	
	public CycleValidationService getRadarValidation() {
		return radarValidation;
	}

	public void setRadarValidation(CycleValidationService radarValidation) {
		this.radarValidation = radarValidation;
	}
	//////////////////////////////////////////////////////////////////

	@PostConstruct
	public void init() {
		getLastValidDate();
		setRadarData();
		isSymantec();
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	private void getLastValidDate() {
		Date currentDate = new Date();
		
		if(radarValidation.getLastValidDate().before(normalizeToMidnight(currentDate))) {
			radarValidation.setDateValidations();
			radarValidation.validateCycle();
			radar.init();
		}
		now = radarValidation.getLastValidDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	///////////////////////////////////////////////////////////////////////////////
	private Date normalizeToMidnight(Date date) {
	    if (date == null) {
	        return null;
	    }
	    
	    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	    
	    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	///////////////////////////////////////////////////////////////////////////////
	private void setRadarData() {
		setSelectedView(radar.getSelectedView());
		setCountry(radar.getCountry());
		setCurCountry(radar.getCurCountry());
		setViewId(radar.getViewId());
		setViewText(radar.getViewText());
		setSubViewId(radar.getSubViewId());
		setViewList(radar.getViewList());
		setSelectedView(radar.getSelectedView());
		setSymantecList(symantecList = radar.getSymantecList());
	}

	public static Date getPreviousDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar.getTime();
	}

	////////////////////////////////////
	public List<String> getMapCountryAccess() {
		return radar.getCountryMap().get(viewId);
	}
	//////////////////////////////////////

	public void changeCountry(String country) {
		this.curCountry = country;
		this.country = country.equalsIgnoreCase("ALL") ? "" : country;
		radar.setCountry(this.country);
		radar.setCurCountry(this.curCountry);
		reset();
		processHistory.processAndMapData();		
		processHistory.getHistoricalForUpperData();
	}

	////////////////////////////////////////////////
	public void submit(AjaxBehaviorEvent event) {
		reset();
		processHistory.processAndMapData();		
		processHistory.getHistoricalForUpperData();
		now = radarValidation.getLastValidDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		headerAndCardMap.getLeftCardInfo();

	}
	//////////////////////////////////////////////
	
	public void changeView(int viewId, String viewName ) {
		radar.setViewId(viewId);
		if (viewName.equalsIgnoreCase("CIS - EndPoints")) {
			radar.setSubViewId(-1);
			this.setSubViewText("Windows");
		} else {
			radar.setSubViewId(-1);
			this.setSubViewText("ALL");
		}
		radar.setViewText(viewName);
		radar.getSelectedView().setId(viewId);
		radar.getSelectedView().setName(viewName);
		
		
		viewSymantecName = 0;
		reset();
		setRadarData();
		processHistory.processAndMapData();		
		processHistory.getHistoricalForUpperData();
	}
	////////////////////////////////////////////////////////////////////
	public void changeSubView(Views view, Views subView) {
		radar.setSubViewId(subView.getId());
		this.subViewText = subView.getName();
		this.isManual = subView.getIsManual() == 1 ? true : false;
		
		isSymantec();
		reset();
		setRadarData();
 		processHistory.processAndMapData();	
 		processHistory.getHistoricalForUpperData();
	}
	////////////////////////////////////////////////
	public void setSymantecViewId(Views viewId, int subViewId) {
		radar.setViewId(viewId.getId());
		radar.setSubViewId(-1);
		radar.setViewText(viewId.getName());
		radar.setSelectedView(viewId);
		isSymantec();
		this.subViewText = "";
		reset();
		setRadarData();
		processHistory.processAndMapData();	
 		processHistory.getHistoricalForUpperData();
	}
	////////////////////////////////////////////////////////////

	public void isSymantec() {
		if (viewText != null && viewText.contains("Symantec -"))
			viewSymantecName = 1;
		else
			viewSymantecName = 0;
	}


	public void getVeiwData() {
		List<ViewInfo> veiwData = da.getDataInfoNew(viewId, subViewId, country.equalsIgnoreCase("all") ? "" : country,
				"%", "%", viewText);
		veiwDataComp = veiwData.stream()
				.filter(o -> o.getType().contains("Comp") && o.getDate().equalsIgnoreCase(sdf.format(date1).trim()))
				.collect(Collectors.toList());

		veiwDataCov = veiwData.stream()
				.filter(o -> o.getType().contains("Cov") && o.getDate().equalsIgnoreCase(sdf.format(date1).trim()))
				.collect(Collectors.toList());
	}

	public List<ViewInfo> getCompCircleScore(String type) {
		String dateStr = sdf.format(radar.getDate1()).trim();
		String key = CacheService.getInstance().getCacheKey("CompCircleScore", type, viewId, subViewId, country,
				dateStr);

		List<ViewInfo> cachedResult = CacheService.getInstance().getFromCache(key);
		if (cachedResult != null && !cachedResult.isEmpty()) {
			return cachedResult;
		}

		List<ViewInfo> data = da.getComplyCirclePerScore(viewId, subViewId,
				country.equalsIgnoreCase("all") ? "" : country, type, viewText, dateStr);
		CacheService.getInstance().putInCache(key, data);
		return data;
	}

	public long filterSummation(List<ViewInfo> lst, String type, String isNew) {
		long sum = 0;
		sum = lst.stream().filter(data -> data.getTypeSort().equalsIgnoreCase(isNew)).map(data -> {
			switch (type) {
			case "TotalSum":
				return data.getTotalSum();
			case "Green":
				return data.getGreen();
			case "Orange":
				return data.getOrange();
			case "Red":
				return data.getRed();
			case "IT":
				return data.getIt();
			case "Risk":
				return data.getRisk();
			default:
				throw new IllegalArgumentException("Invalid type: " + type);
			}
		}).collect(Collectors.summingLong(Long::longValue));
		return sum;
	}

	public long getComp(String type, String isNew) {
		if (veiwDataComp == null) {
			getVeiwData();
		}
		if (subViewId == -1) {
			return filterSummation(veiwDataComp, type, isNew);
		}
		for (int i = 0; i < veiwDataComp.size(); i++) {
			if (veiwDataComp.get(i).getTypeSort().equalsIgnoreCase(isNew)) {
				switch (type) {
				case "TotalSum":
					return veiwDataComp.get(i).getTotalSum();
				case "Green":
					return veiwDataComp.get(i).getGreen();
				case "Orange":
					return veiwDataComp.get(i).getOrange();
				case "Red":
					return veiwDataComp.get(i).getRed();
				case "IT":
					return veiwDataComp.get(i).getIt();
				case "Risk":
					return veiwDataComp.get(i).getRisk();
				}
			}
		}

		return 0;
	}

	public long getCov(String type, String isNew) {
		if (veiwDataCov == null || veiwDataCov.isEmpty()) {
			logger.debug("get view data");
			getVeiwData();
			logger.debug("End get view data");
			logger.debug("//////////////////////////////////");
		}
		if (subViewId == -1) {
			return filterSummation(veiwDataCov, type, isNew);
		}
		for (int i = 0; i < veiwDataCov.size(); i++) {
			if (veiwDataCov.get(i).getTypeSort().equalsIgnoreCase(isNew)) {
				switch (type) {
				case "TotalSum":
					return veiwDataCov.get(i).getTotalSum();
				case "Green":
					return veiwDataCov.get(i).getGreen();
				case "Orange":
					return veiwDataCov.get(i).getOrange();
				case "Red":
					return veiwDataCov.get(i).getRed();
				case "IT":
					return veiwDataCov.get(i).getIt();
				}
			}
		}
		return 0;
	}

	public String getViewText() {
		if (viewText == null || viewText.length() == 0)
			return "CIS-EndPoints";
		return viewText;
	}

	public void setViewText(String viewText) {
		this.viewText = viewText;
	}

	public String getSubViewText() {
		if (subViewText == null || subViewText.length() == 0)
			return "ALL";
		return subViewText;
	}

	public void setSubViewText(String subViewText) {
		this.subViewText = subViewText;
	}

	public boolean getIsManual() {
		return isManual;
	}

	public void setIsManual(boolean isManual) {
		this.isManual = isManual;
	}

	public void redirectToComplianceDetails() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect(COMPLIANCE_PATH_REDIRECT);
	}

	public void redirectToCoverageDetails() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect(COVERAGE_PATH_REDIRECT);
	}

	public int getSelectedThresold() {
		return selectedThresold;
	}

	public Pair<Integer, Integer> getTotalAssetAndTotalCoverdToday() {
		Pair<Integer, Integer> pair;
		List<AssetInfo> assetInfos = da.getAllCoverage(viewId, subViewId, country, sdf.format(radar.getDate1()).trim());
		int totalCovered = 0;
		int totalCount = assetInfos.size();

		for (AssetInfo asset : assetInfos) {
			totalCovered += asset.getIsCoveredByControl();
		}

		return new Pair<>(totalCovered, totalCount);
	}

	public String getCycleDate() {
		List<AssetInfo> SyncDate;
		SyncDate = da.getSyncDate(viewId, subViewId, country, sdf.format(radar.getDate1()).trim());
		LocalDate now = null;
		if (SyncDate.size() > 0) {
			now = SyncDate.get(0).getSyncDate().toLocalDate();
		} else {
			now = LocalDate.now();
		}
		return now.toString().substring(0, 10);
	}

	public String getCoverageTodaythreshold(double val) {
		if (viewText.toLowerCase().contains("endpoints"))
			if (val >= 90)
				return RadarThresholds.NORMAL_COLOR;
			else if (val >= 85)
				return RadarThresholds.WARNING_COLOR;
			else
				return RadarThresholds.CRITICAL_COLOR;

		else if (viewText.toLowerCase().contains("servers"))
			if (val >= 99)
				return RadarThresholds.NORMAL_COLOR;
			else if (val >= 95)
				return RadarThresholds.WARNING_COLOR;
			else
				return RadarThresholds.CRITICAL_COLOR;
		else if (val >= 99)
			return RadarThresholds.NORMAL_COLOR;
		else if (val >= 95)
			return RadarThresholds.WARNING_COLOR;
		else
			return RadarThresholds.CRITICAL_COLOR;
	}

	public String getComplianceTodaythreshold(double val) {

		if (viewText.toLowerCase().contains("endpoints"))
			if (val >= 95)
				return RadarThresholds.NORMAL_COLOR;
			else if (val >= 90)
				return RadarThresholds.WARNING_COLOR;
			else
				return RadarThresholds.CRITICAL_COLOR;

		else if (viewText.toLowerCase().contains("servers"))
			if (val >= 90)
				return RadarThresholds.NORMAL_COLOR;
			else if (val >= 80)
				return RadarThresholds.WARNING_COLOR;
			else
				return RadarThresholds.CRITICAL_COLOR;
		else if (val >= 85)
			return RadarThresholds.NORMAL_COLOR;
		else if (val >= 80)
			return RadarThresholds.WARNING_COLOR;
		else
			return RadarThresholds.CRITICAL_COLOR;
	}

	private LocalDate convertDateToLocalDate(Date date) {
		LocalDate localDate;
		try {
			if (date == null || date.equals(null))
				localDate = now;
			localDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		} catch (Exception e) {
			localDate = now;
		}

		return localDate;
	}

	private int calculateBulkScanFill(LocalDate scanDate) {

		if (scanDate.isEqual(now) || scanDate.isAfter(now))
			return 0;
		int percent = 25;
		int week = 0;
		for (int i = 1; i < 4; i++) {
			if (scanDate != null)
				if ((scanDate.isBefore(now.minusWeeks(week)) && scanDate.isAfter(now.minusWeeks(week + 1)))
						|| scanDate.isEqual(now.minusWeeks(week + 1)))
					return percent;
			percent += 25;
			week++;
		}
		return percent;
	}

	public List<AssetInfo> getAssetList() {
		return assetList;
	}

	public void setAssetList(List<AssetInfo> assetList) {
		this.assetList = assetList;
	}

	public List<ComplianceInfo> getComplianceList() {
		return complianceList;
	}

	public void setComplianceList(List<ComplianceInfo> complianceList) {
		this.complianceList = complianceList;
	}

	public List<RadarLeftCard> getLeftCardItem(String country) {
		return radarLeftCardMap.get(country);
	}

	public List<RadarLeftCard> geWsustLeftCardItem(String country) {
		return wsusLeftCardMap.get(country);
	}

	public LocalDate getManualAssetScanDate() {
		return manualAssetScanDate;
	}

	public List<ViewInfo> getLeftCardList() {
		return leftCardList;
	}

	public List<ViewInfo> getWSUSLeftCardData(String country) {
		return wsusleftCardDataMap.get(country);
	}

	public LocalDate getScanDate() {
		Date date = da.getBulkScanDate(viewId, subViewId, country.equals("ALL") ? "" : country, 0);
		scanDate = convertDateToLocalDate(date);
		return scanDate;
	}

	public void setScanDate(LocalDate scanDate) {
		this.scanDate = scanDate;
	}

	public String formatDouble(double num) {
		NumberFormat formatter = new DecimalFormat("#0.00");
		return formatter.format(num);
	}

	public Views getSelectedView() {
		if (selectedView == null) {
			selectedView = radar.getSelectedView();
		}
		return selectedView;
	}

	public int getViewId() {
		return viewId;
	}

	public void setViewId(int viewId) {
		this.viewId = viewId;
	}

	public void setSelectedView(Views selectedView) {
		this.selectedView = selectedView;
	}

	public Pair<Integer, Integer> getCovCountPerControl(int subViewId) {
		int Val1;
		int Val2;

		Pair<Integer, Integer> item = new Pair<Integer, Integer>();

		Val1 = 0;
		Val2 = 0;

		item.setFirst(Val1);
		item.setSecond(Val2);
		return item;
	}

	private void getCovCircelColorDetails(String check) {
		switch (check) {
		case "Coverage-Green-Existing": {
			circleDetail = check;
			break;
		}
		case "Coverage-Orange-Existing": {
			circleDetail = check;
			break;
		}
		case "Coverage-Red-Existing": {
			circleDetail = check;
			break;
		}
		case "Coverage-Green-New": {
			circleDetail = check;
			break;
		}
		case "Coverage-Orange-New": {
			circleDetail = check;
			break;
		}
		case "Coverage-Red-New": {
			circleDetail = check;
			break;
		}

		}
	}

	private void getComplCircelColorDetails(String check) {
		switch (check) {
		case "Compliance-Green-Existing": {
			circleDetail = check;
			break;
		}
		case "Compliance-Orange-Existing": {
			circleDetail = check;
			break;
		}
		case "Compliance-Red-Existing": {
			circleDetail = check;
			break;
		}
		case "Compliance-Green-New": {
			circleDetail = check;
			break;
		}
		case "Compliance-Orange-New": {
			circleDetail = check;
			break;
		}
		case "Compliance-Red-New": {
			circleDetail = check;
			break;
		}

		}
	}

	private void getWsusCovCircelColorDetails(String check) {
		switch (check) {
		case "first-Coverage-WSUS-Green": {
			circleDetail = check;
			break;
		}
		case "first-Coverage-WSUS-Orange": {
			circleDetail = check;
			break;
		}
		case "first-Coverage-WSUS-Red": {
			circleDetail = check;
			break;
		}
		case "second-Coverage-WSUS-Green": {
			circleDetail = check;
			break;
		}
		case "second-Coverage-WSUS-Orange": {
			circleDetail = check;
			break;
		}
		case "second-Coverage-WSUS-Red": {
			circleDetail = check;
			break;
		}

		}

	}

	private void getWsusComplCircelColorDetails(String check) {
		switch (check) {
		case "Complaince-WSUS-Green": {
			circleDetail = check;
			break;
		}
		case "Complaince-WSUS-Orange": {
			circleDetail = check;
			break;
		}
		case "Complaince-WSUS-Red": {
			circleDetail = check;
			break;
		}
		}
	}

	private void getWsusThirdPartyCircelColorDetails(String check) {
		switch (check) {
		case "ThirdParty-WSUS-Green": {
			circleDetail = check;
			break;
		}
		case "ThirdParty-WSUS-Orange": {
			circleDetail = check;
			break;
		}
		case "ThirdParty-WSUS-Red": {
			circleDetail = check;
			break;
		}
		}
	}

	public void getCircelColor(String color, String isNew, String type) {
		String check = null;
		if (!type.contains("WSUS")) {
			check = type + "-" + color + "-" + isNew;
		} else {
			check = type + "-" + color;
		}

		switch (type) {
		case "Coverage": {
			getCovCircelColorDetails(check);
			break;
		}
		case "Compliance": {
			getComplCircelColorDetails(check);
			break;
		}
		case "first-Coverage-WSUS": {
			getWsusCovCircelColorDetails(check);
			break;
		}
		case "second-Coverage-WSUS": {
			getWsusCovCircelColorDetails(check);
			break;
		}
		case "Complaince-WSUS": {
			getWsusComplCircelColorDetails(check);
			break;
		}
		case "ThirdParty-WSUS": {
			getWsusThirdPartyCircelColorDetails(check);
			break;
		}
		default:
			break;
		}
	}




	@Override
	public void reset() {
		veiwDataComp = null;
		veiwDataCov = null;
		pairCur = null;
	}
	/////////////////////////////////////////////////////

	public void redirectToExcludeDetails() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("radar/inc/cov-exclude-dialog.xhtml");
	}

	public void redirectToCompExcludeDetails() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("radar/inc/comp-exclude-dialog.xhtml");
	}

	public List<AssetInfo> getCovDataList() {
		return covDataList;
	}

	public void setCovDataList(List<AssetInfo> covDataList) {
		this.covDataList = covDataList;
	}

	public List<ComplianceInfo> getCompDataList() {
		return compDataList;
	}

	public void setCompDataList(List<ComplianceInfo> compDataList) {
		this.compDataList = compDataList;
	}

	public String getContriName() {
		return contriName;
	}
	
	public String getCircleDetail() {
		return circleDetail;
	}

	public void setCircleDetail(String circleDetail) {
		this.circleDetail = circleDetail;
	}

	public List<Views> getSymantecList() {
		return radar.getSymantecList();
	}

	public void setSymantecList(List<Views> symantecList) {
		this.symantecList = symantecList;
	}	

	public int getViewSymantecName() {
		return viewSymantecName;
	}

	public void setViewSymantecName(int viewSymantecName) {
		this.viewSymantecName = viewSymantecName;
	}

	public List<String> getCountresAccess() {
		return countresAccess;
	}

	public void setCountresAccess(List<String> countresAccess) {
		this.countresAccess = countresAccess;
	}

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Pair<Integer, Integer> getPairCur() {
		if (pairCur == null) {
			pairCur = getTotalAssetAndTotalCoverdToday();
		}
		return pairCur;
	}

	public void setPairCur(Pair<Integer, Integer> pairCur) {
		this.pairCur = pairCur;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getSubViewId() {
		return subViewId;
	}

	public void setSubViewId(int subViewId) {
		this.subViewId = subViewId;
	}
	public String getCurCountry() {
		return curCountry;
	}

	public void setCurCountry(String curCountry) {
		this.curCountry = curCountry;
	}

	public void setViewList(List<Views> viewList) {
		this.viewList = viewList;
	}

	public List<Views> getViewList() {
		return viewList;
	}

}
