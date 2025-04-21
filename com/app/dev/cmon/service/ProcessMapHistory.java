package com.app.dev.cmon.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.app.dev.cmon.cache.CacheService;
import com.app.dev.cmon.components.AllRadarData;
import com.app.dev.cmon.components.AssetInfo;
import com.app.dev.cmon.components.ViewInfo;
import com.app.dev.cmon.components.Views;
import com.app.dev.cmon.controllers.DataAccess;
import com.app.dev.cmon.controllers.RadarController;
import com.arabbank.dev.utility.Pair;
import com.arabbank.dev.utility.Triple;
import com.arabbank.devf.cmon.controllers.portal.CmonManagedBean;

@ManagedBean(name = "radarHistory")
@SessionScoped 
public class ProcessMapHistory extends CmonManagedBean implements Serializable {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private List<AllRadarData> radarInfo = new ArrayList<AllRadarData>();
	private List<AssetInfo> coverageAll;
	private Map<String, Pair<Integer, Integer>> oneMonthMap = new HashMap<String, Pair<Integer,Integer>>();
	private Map<String, Pair<Integer, Integer>> twoMonthMap = new HashMap<String, Pair<Integer,Integer>>();
	private Triple<Double, Integer, Integer> tripleOneMonth = new Triple<Double, Integer, Integer>();
	private Triple<Double, Integer, Integer> tripleTwoMonth = new Triple<Double, Integer, Integer>();
	private Pair<Integer, Integer> pairOneMonth;
	private Pair<Integer, Integer> pairTwoMonth;
	
	private List<ViewInfo> veiwDataAll;
	private List<ViewInfo> veiwDataCompOneMonth;
	private List<ViewInfo> veiwDataCovOneMonth;
	private List<ViewInfo> veiwDataCompTwoMonth;
	private List<ViewInfo> veiwDataCovTwoMonth;
	
	private DataAccess da = new DataAccess();
	private RadarController radar = ((RadarController) getManagedBeanByName("radarController"));
	
    @PostConstruct
    public void init() {
    	radarInfo = radar.getRadarInfo();
        processAndMapData();
        getHistoricalForUpperData();
    }

	/////////////////////////////////////////////////////////////////////////////
	public void getHistoricalForUpperData() {
		tripleOneMonth = getCovUpperDataHis(1);
		tripleTwoMonth = getCovUpperDataHis(2);
		pairOneMonth = getTotalAssetAndTotalCoverd(1);
		pairTwoMonth = getTotalAssetAndTotalCoverd(2);
	}
	/////////////////////////////////////////////////////////////////////////////
	public void processAndMapData() {
	    String currentDateKey = sdf.format(radar.getDate1()).trim();
	    coverageAll = getCoverageFromCacheOrFetch("Coverage", currentDateKey, radar.getViewId(), radar.getSubViewId(), radar.getCountry());

	    List<AssetInfo> listOne = getCoverageFromCacheOrFetch("CoverageOneMonth", sdf.format(getPreviousMonth(1)), radar.getViewId(), -1, "");
	    List<AssetInfo> listTwo = getCoverageFromCacheOrFetch("CoverageTwoMonth", sdf.format(getPreviousMonth(2)), radar.getViewId(), -1, "");

	    oneMonthMap = processAssetList(listOne);
	    twoMonthMap = processAssetList(listTwo);
	}
	
	private Map<String, Pair<Integer, Integer>> processAssetList(List<AssetInfo> assetList) {
		// Group by LocationCode + SubViewId
		Map<String, Pair<Integer, Integer>> resultMap = assetList.stream()
				.collect(Collectors.groupingBy(asset -> asset.getLocationCode() + asset.getSubViewId(),
						Collectors.collectingAndThen(Collectors.toList(),
								assets -> new Pair<>(assets.stream().mapToInt(AssetInfo::getIsCoveredByControl).sum(),
										assets.size()))));

		// Group by "ALL" location per SubViewId
		Map<String, Pair<Integer, Integer>> allGroupMapBySubViewId = assetList.stream()
				.collect(Collectors.groupingBy(asset -> "ALL" + asset.getSubViewId(),
						Collectors.collectingAndThen(Collectors.toList(),
								assets -> new Pair<>(assets.stream().mapToInt(AssetInfo::getIsCoveredByControl).sum(),
										assets.size()))));

		// Group by SubViewId = -1 for all locations
		Map<String, Pair<Integer, Integer>> allLocationsMap = assetList.stream()
				.collect(Collectors.groupingBy(asset -> asset.getLocationCode() + "-1",
						Collectors.collectingAndThen(Collectors.toList(),
								assets -> new Pair<>(assets.stream().mapToInt(AssetInfo::getIsCoveredByControl).sum(),
										assets.size()))));

		// Calculate the total for all data without grouping
		Pair<Integer, Integer> totalPair = assetList.stream().collect(Collectors.collectingAndThen(Collectors.toList(),
				assets -> new Pair<>(assets.stream().mapToInt(AssetInfo::getIsCoveredByControl).sum(), assets.size())));

		// Add the "ALL" group to the result map
		resultMap.putAll(allGroupMapBySubViewId);

		// Add the all locations grouped by SubViewId = -1 to the result map
		resultMap.putAll(allLocationsMap);

		// Add the overall total to the result map
		resultMap.put("ALL" + "-1", totalPair);

		return resultMap;
	}
	
    private Date getPreviousMonth(int months) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.MONTH, -months);
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }
	
	private List<AssetInfo> getCoverageFromCacheOrFetch(String cacheType, String dateKey, int viewId, int subViewId, String country) {
	    String cacheKey = CacheService.getInstance().getCacheKey(cacheType, "All", viewId, subViewId, country, dateKey);
	    List<AssetInfo> coverageList = CacheService.getInstance().getFromCache(cacheKey);

	    if (coverageList == null || coverageList.isEmpty()) {
	        coverageList = da.getAllCoverage(viewId, subViewId, country, dateKey);
	        CacheService.getInstance().putInCache(cacheKey, coverageList);
	    }

	    return coverageList;
	}
	//////////////////////////////////////////////////////////////////////////////////
	
	public void getVeiwDataHis() {
	    veiwDataAll = da.getDataInfoNew(radar.getViewId(), radar.getSubViewId(), 
	    		radar.getCountry().equalsIgnoreCase("all") ? "" : radar.getCountry(), "%", "%", radar.getViewText());

	    String oneMonth = sdf.format(getPreviousMonth(1));
	    String twoMonth = sdf.format(getPreviousMonth(2));

	    Map<String, List<ViewInfo>> dataByDateAndType = veiwDataAll.stream()
	        .collect(Collectors.groupingBy(
	            o -> o.getDate() + "_" + (o.getType().contains("Comp") ? "Comp" : "Cov")
	        ));

	    veiwDataCompTwoMonth = dataByDateAndType.getOrDefault(twoMonth + "_Comp", Collections.emptyList());
	    veiwDataCovTwoMonth = dataByDateAndType.getOrDefault(twoMonth + "_Cov", Collections.emptyList());
	    veiwDataCompOneMonth = dataByDateAndType.getOrDefault(oneMonth + "_Comp", Collections.emptyList());
	    veiwDataCovOneMonth = dataByDateAndType.getOrDefault(oneMonth + "_Cov", Collections.emptyList());
	}
	////////////////////////////////////////////////////////////
	public Pair<Integer, Integer> getTotalAssetAndTotalCoverd(int month) {
		Pair<Integer, Integer> pair = new Pair<Integer, Integer>();
		if (month == 1) {
			pair = getOneMonthMap().get((radar.getCountry().toUpperCase() == "" ? "ALL" : radar.getCountry()) + radar.getSubViewId());
		} else {
			pair = getTwoMonthMap().get((radar.getCountry().toUpperCase() == "" ? "ALL" : radar.getCountry()) + radar.getSubViewId());
		}
		if (pair == null)
			return new Pair<Integer, Integer>();

		return pair;

	}
	/////////////////////////////////////////////////
	public Triple<Double, Integer, Integer> getCovUpperDataHis(int cur) {
		Triple<Double, Integer, Integer> triple = new Triple<>();
		if (cur == 1) {
			triple.setFirst(
					divideTwoNumbers(getCovBeforOneMonth("IT", "Existing"), getCovBeforOneMonth("IT", "New")) * 100);
			triple.setSecond(getMaxFourDigits((int) (getCovBeforOneMonth("IT", "Existing"))));
			triple.setThird(getMaxFourDigits((int) (getCovBeforOneMonth("IT", "New"))));
		} else if (cur == 2) {
			triple.setFirst(
					divideTwoNumbers(getCovBeforTwoMonth("IT", "Existing"), getCovBeforTwoMonth("IT", "New")) * 100);
			triple.setSecond(getMaxFourDigits((int) (getCovBeforTwoMonth("IT", "Existing"))));
			triple.setThird(getMaxFourDigits((int) (getCovBeforTwoMonth("IT", "New"))));
		}
		return triple;
	}
	
	/////////////////////////////////////////////////////////////////////
	public long getCovBeforOneMonth(String type, String isNew) {
		if (veiwDataCovOneMonth == null) {
			getVeiwDataHis();
		}
		
	    if (veiwDataCovOneMonth.isEmpty() || veiwDataCompOneMonth.isEmpty() || veiwDataCovOneMonth.size() != veiwDataCompOneMonth.size()) {
	        return 0;
	    }
	    
		if(radar.getSubViewId() == -1) {
			return filterSummation(veiwDataCovOneMonth,type,isNew);
			}
		for (int i = 0; i < veiwDataCovOneMonth.size(); i++) {
			if (veiwDataCovOneMonth.get(i).getTypeSort().equalsIgnoreCase(isNew)  && veiwDataCompOneMonth.get(i).getSupViewID() == radar.getSubViewId()) {
				switch (type) {
				case "TotalSum":
					return veiwDataCovOneMonth.get(i).getTotalSum();
				case "Green":
					return veiwDataCovOneMonth.get(i).getGreen();
				case "Orange":
					return veiwDataCovOneMonth.get(i).getOrange();
				case "Red":
					return veiwDataCovOneMonth.get(i).getRed();
				case "IT":
					return veiwDataCovOneMonth.get(i).getIt();
				}
			}
		}
		return 0;
	}
	/////////////////////////////////////////////////////////////////////
	
	public long getCovBeforTwoMonth(String type, String isNew) {
		if (veiwDataCovTwoMonth == null) {
			getVeiwDataHis();
		}
		
	    if (veiwDataCovTwoMonth.isEmpty() || veiwDataCompOneMonth.isEmpty() || veiwDataCovTwoMonth.size() != veiwDataCompOneMonth.size()) {
	        return 0;
	    }
	    
		if(radar.getSubViewId() == -1) {
			return filterSummation(veiwDataCovTwoMonth,type,isNew);
			}
		for (int i = 0; i < veiwDataCovTwoMonth.size(); i++) {
			if (veiwDataCovTwoMonth.get(i).getTypeSort().equalsIgnoreCase(isNew)  && veiwDataCompOneMonth.get(i).getSupViewID() == radar.getSubViewId()) {
				switch (type) {
				case "TotalSum":
					return veiwDataCovTwoMonth.get(i).getTotalSum();
				case "Green":
					return veiwDataCovTwoMonth.get(i).getGreen();
				case "Orange":
					return veiwDataCovTwoMonth.get(i).getOrange();
				case "Red":
					return veiwDataCovTwoMonth.get(i).getRed();
				case "IT":
					return veiwDataCovTwoMonth.get(i).getIt();
				}
			}
		}
		return 0;
	}
	//////////////////////////////////////////////////////////////////////
	public Triple<Double, Integer, Integer> getCompUpperDataHis(int cur, int isRisk) {
	    String type = isRisk == 1 ? "Risk" : "IT";
	    String colorType = isRisk == 1 ? "IT-Exist" : "Risk-Exist";
	    String columnDateExist = getColumnDateExist(colorType);

	    if (cur == 1) {
	        return calculateTriple(type, "Existing", "New", columnDateExist, this::getCompBeforOneMoth);
	    } else if (cur == 2) {
	        return calculateTriple(type, "Existing", "New", columnDateExist, this::getCompBeforTwoMoth);
	    }

	    return new Triple<>();
	}
	
	private Triple<Double, Integer, Integer> calculateTriple(String mainType, String existingType, String newType, String columnDateExist, BiFunction<String, String, Long> dataFetcher) {
	    Triple<Double, Integer, Integer> triple = new Triple<>();

	    double existingValue = dataFetcher.apply(mainType, existingType);
	    double newValue = dataFetcher.apply(mainType, newType);
	    newValue = columnDateExist.equalsIgnoreCase("Score") ? newValue * 100 : newValue;
	    double ratio = divideTwoNumbers(existingValue, newValue) * 100;

	    triple.setFirst(ratio);
	    triple.setSecond(getMaxFourDigits((int) existingValue));
	    triple.setThird(getMaxFourDigits((int) (columnDateExist.equalsIgnoreCase("Score") ? newValue * 100 : newValue)));

	    return triple;
	}

	private String getColumnDateExist(String colorType) {
	    return radarInfo.stream()
	            .filter(o -> o.getColorType().equalsIgnoreCase(colorType) && o.getIDName().contains("Comp") && o.getViewID() == radar.getViewId())
	            .findFirst()
	            .map(AllRadarData::getColumnDateName)
	            .orElse("");
	}
	//////////////////////////////////////////////////////////
	public long getCompBeforOneMoth(String type, String isNew) {
		if (veiwDataCompOneMonth == null) {
			getVeiwDataHis();
		}
		
	    if (veiwDataCompOneMonth.isEmpty() || veiwDataCompTwoMonth.isEmpty() || veiwDataCompOneMonth.size() != veiwDataCompTwoMonth.size()) {
	        return 0;
	    }
		
		if(radar.getSubViewId() == -1) {
			return filterSummation(veiwDataCompOneMonth,type,isNew);
			}
		for (int i = 0; i < veiwDataCompOneMonth.size(); i++) {
			if (veiwDataCompOneMonth.get(i).getTypeSort().equalsIgnoreCase(isNew) && veiwDataCompTwoMonth.get(i).getSupViewID() == radar.getSubViewId()) {
				switch (type) {
				case "TotalSum":
					return veiwDataCompOneMonth.get(i).getTotalSum();
				case "Green":
					return veiwDataCompOneMonth.get(i).getGreen();
				case "Orange":
					return veiwDataCompOneMonth.get(i).getOrange();
				case "Red":
					return veiwDataCompOneMonth.get(i).getRed();
				case "IT":
					return veiwDataCompOneMonth.get(i).getIt();
				case "Risk":
					return veiwDataCompOneMonth.get(i).getRisk();
				}
			}
		}

		return 0;
	}
	////////////////////////////////////////////////////////////
	public long getCompBeforTwoMoth(String type, String isNew) {
		if (veiwDataCompTwoMonth == null) {
			getVeiwDataHis();
		}
		
	    if (veiwDataCompTwoMonth.isEmpty() || veiwDataCompOneMonth.isEmpty() || veiwDataCompTwoMonth.size() != veiwDataCompOneMonth.size()) {
	        return 0;
	    }
	    
	    
		if(radar.getSubViewId() == -1) {
			return filterSummation(veiwDataCompTwoMonth,type,isNew);
			}
		for (int i = 0; i < veiwDataCompTwoMonth.size(); i++) {
			if (veiwDataCompTwoMonth.get(i).getTypeSort().equalsIgnoreCase(isNew) && veiwDataCompOneMonth.get(i).getSupViewID() == radar.getSubViewId()) {
				switch (type) {
				case "TotalSum":
					return veiwDataCompTwoMonth.get(i).getTotalSum();
				case "Green":
					return veiwDataCompTwoMonth.get(i).getGreen();
				case "Orange":
					return veiwDataCompTwoMonth.get(i).getOrange();
				case "Red":
					return veiwDataCompTwoMonth.get(i).getRed();
				case "IT":
					return veiwDataCompTwoMonth.get(i).getIt();
				case "Risk":
					return veiwDataCompTwoMonth.get(i).getRisk();
				}
			}
		}

		return 0;
	}
	//////////////////////////////////////////////////////////////////////
	
	private double divideTwoNumbers(Number a, Number b) {
		if (b == null || a == null)
			return 0;
		if (b.doubleValue() == 0)
			return 0;

		return a.doubleValue() / b.doubleValue();
	}
	
	public Integer getMaxFourDigits(Integer number) {
		if (number > 9999999) {
			number = (number / 10000);
		}
		if (number > 999999) {
			number = (number / 1000);
		}
		if (number > 99999) {
			number = (number / 100);
		}
		if (number > 9999) {
			number = (number / 10);
		}
		return number;
	}
	
	public long filterSummation(List<ViewInfo> lst,String type,String isNew) {
		long sum = 0;
		sum = lst.stream()
                .filter(data -> data.getTypeSort().equalsIgnoreCase(isNew))
                .map(data -> {
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
                })
                .collect(Collectors.summingLong(Long::longValue));
		return sum;	
	}
	/////////////////////////////////////////////////////////////////////
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		veiwDataCovOneMonth = null;
		veiwDataCovTwoMonth = null;
		veiwDataCompOneMonth = null;
		veiwDataCompTwoMonth = null;
        processAndMapData();
        getHistoricalForUpperData();
	}

	public List<AssetInfo> getCoverageAll() {
		return coverageAll;
	}

	public void setCoverageAll(List<AssetInfo> coverageAll) {
		this.coverageAll = coverageAll;
	}

	public Map<String, Pair<Integer, Integer>> getOneMonthMap() {
		return oneMonthMap;
	}

	public void setOneMonthMap(Map<String, Pair<Integer, Integer>> oneMonthMap) {
		this.oneMonthMap = oneMonthMap;
	}

	public Map<String, Pair<Integer, Integer>> getTwoMonthMap() {
		return twoMonthMap;
	}

	public void setTwoMonthMap(Map<String, Pair<Integer, Integer>> twoMonthMap) {
		this.twoMonthMap = twoMonthMap;
	}


	public List<ViewInfo> getVeiwDataAll() {
		return veiwDataAll;
	}


	public void setVeiwDataAll(List<ViewInfo> veiwDataAll) {
		this.veiwDataAll = veiwDataAll;
	}


	public List<ViewInfo> getVeiwDataCompOneMonth() {
		return veiwDataCompOneMonth;
	}


	public void setVeiwDataCompOneMonth(List<ViewInfo> veiwDataCompOneMonth) {
		this.veiwDataCompOneMonth = veiwDataCompOneMonth;
	}


	public List<ViewInfo> getVeiwDataCovOneMonth() {
		return veiwDataCovOneMonth;
	}


	public void setVeiwDataCovOneMonth(List<ViewInfo> veiwDataCovOneMonth) {
		this.veiwDataCovOneMonth = veiwDataCovOneMonth;
	}


	public List<ViewInfo> getVeiwDataCompTwoMonth() {
		return veiwDataCompTwoMonth;
	}


	public void setVeiwDataCompTwoMonth(List<ViewInfo> veiwDataCompTwoMonth) {
		this.veiwDataCompTwoMonth = veiwDataCompTwoMonth;
	}


	public List<ViewInfo> getVeiwDataCovTwoMonth() {
		return veiwDataCovTwoMonth;
	}


	public void setVeiwDataCovTwoMonth(List<ViewInfo> veiwDataCovTwoMonth) {
		this.veiwDataCovTwoMonth = veiwDataCovTwoMonth;
	}

	public Triple<Double, Integer, Integer> getTripleOneMonth() {
		return tripleOneMonth;
	}
	public void setTripleOneMonth(Triple<Double, Integer, Integer> tripleOneMonth) {
		this.tripleOneMonth = tripleOneMonth;
	}
	public Triple<Double, Integer, Integer> getTripleTwoMonth() {
		return tripleTwoMonth;
	}
	public void setTripleTwoMonth(Triple<Double, Integer, Integer> tripleTwoMonth) {
		this.tripleTwoMonth = tripleTwoMonth;
	}
	public Pair<Integer, Integer> getPairOneMonth() {
		if(pairOneMonth == null) {
			pairOneMonth = getTotalAssetAndTotalCoverd(1);
		} 
		return pairOneMonth;
	}
	public void setPairOneMonth(Pair<Integer, Integer> pairOneMonth) {
		this.pairOneMonth = pairOneMonth;
	}
	public Pair<Integer, Integer> getPairTwoMonth() {
		if(pairTwoMonth == null) {
			pairTwoMonth = getTotalAssetAndTotalCoverd(2);
		} 
		return pairTwoMonth;
	}

	public void setPairTwoMonth(Pair<Integer, Integer> pairTwoMonth) {
		this.pairTwoMonth = pairTwoMonth;
	}

	
	
	
}
