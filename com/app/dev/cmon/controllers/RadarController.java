package com.app.dev.cmon.controllers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.app.dev.cmon.cache.CacheService;
import com.app.dev.cmon.components.AllRadarData;
import com.app.dev.cmon.components.AssetInfo;
import com.app.dev.cmon.components.ComplianceInfo;
import com.app.dev.cmon.components.Countres;
import com.app.dev.cmon.components.ElementVisibility;
import com.app.dev.cmon.components.Views;
import com.arabbank.devf.cmon.controllers.portal.CmonManagedBean;

@ManagedBean(name = "radarController")
@SessionScoped
public class RadarController extends CmonManagedBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private DataAccess da;
    private List<Views> viewList;
    private List<Countres> countres = new ArrayList<>();
    private List<Views> symantecList = new ArrayList<>();
    private String viewText;
    private int viewId;
    private int viewIndex = 0;
    private int subViewId = -1;
    private String country = "";
    private Views selectedView;
    private String curCountry = "ALL";
    private Map<String, Integer> visibileData = new HashMap<>();
    private String username;
    private Map<String, List<Views>> subViewsCache;
    private Map<Integer, List<String>> countryMap = new HashMap<>();
    private Map<Integer, String> viewNameMap = new HashMap<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Date date1 = new Date();
    private Date date2 = new Date();
    private List<AssetInfo> covDataList;
    private List<ComplianceInfo> compDataList;
    private List<AllRadarData> radarInfo = new ArrayList<AllRadarData>();
    
    @PostConstruct
    public void init() {
        List<Date> lastDates = da.getLastDate();
        if (!lastDates.isEmpty()) {
            setDate1(lastDates.get(0));
        }
        radarInfo = da.getRadarData();
        getElementFromDataAccess();
        setLists();
    }

    public RadarController() {
        this.da = new DataAccess();
        this.username = ((SubMB_Login) getManagedBeanByName("mbLogin")).getUsername();
        this.subViewsCache = new HashMap<>();
    }

    public void setLists() {
        List<Views> newViewList = da.getViews(username);
        if (viewList == null || viewList.isEmpty() || newViewList.size() - getSymantecList().size() > viewList.size()) {
            updateViewList(newViewList);
            updateSymantecList();
            if (!viewList.isEmpty()) {
                setSelectedView(viewList.get(viewIndex));
            }
        }
    }

    private void updateViewList(List<Views> viewList) {
        setViewList(viewList);
        setCountryList();
    }

    private void updateSymantecList() {
        symantecList.clear();
        viewList.removeIf(view -> {
            if (view.getName().contains("Symantec -")) {
                symantecList.add(view);
                return true;
            }
            return false;
        });
    }

    public void setCountryList() {
        for (Views view : viewList) {
            List<String> countries = Arrays.asList(view.getCountry().split("\\s*,\\s*"));
            countryMap.put(view.getId(), countries);
            viewNameMap.put(view.getId(), view.getName());
        }

        if (!viewList.isEmpty()) {
            List<String> countries = Arrays.asList(viewList.get(0).getCountry().split(",\\s*"));
            country = countries.get(0).equalsIgnoreCase("ALL") ? "" : countries.get(0);
            curCountry = countries.get(0);
            viewText = viewList.get(0).getName();
            viewId = viewList.get(0).getId();
        }
    }

    public List<AssetInfo> getCovData(String type, String idName) {
		covDataList = new ArrayList<>();
		Date date = ((MB_Main) getManagedBeanByName("mbMain")).getDate1();
		covDataList = da.getRadarData(viewId, subViewId, country, type, idName,viewText,sdf.format(date).trim());
		return covDataList;
	}

	public List<ComplianceInfo> getCompData(String type, String idName) {
		compDataList = new ArrayList<>();
		Date date = ((MB_Main) getManagedBeanByName("mbMain")).getDate1();
		compDataList = da.getCompDetails(viewId, subViewId, country, type, idName, viewText,sdf.format(date).trim());
		return compDataList;
	}
	
	public List<AssetInfo> getDetailsPerWeek(String type, String week) {
		Date date = ((MB_Main) getManagedBeanByName("mbMain")).getDate1();
		covDataList = new ArrayList<>();
		covDataList = da.getDataPerWeek(viewId, subViewId, country, type, week, "Cov", viewText,sdf.format(date).trim());
		return covDataList;
	}
	
	public List<ComplianceInfo> getCompDetailsPerWeek(String type, String week) {
		Date date = ((MB_Main) getManagedBeanByName("mbMain")).getDate1();
		compDataList = new ArrayList<>();
		compDataList = da.getCompDetailsPerWeek(viewId, subViewId, country, type, week, "Comp", viewText,sdf.format(date).trim());
		return compDataList;
	}
	
	public List<ComplianceInfo> getCompDetailsPerScore(String type, String score) {
		Date date = ((MB_Main) getManagedBeanByName("mbMain")).getDate1();
		compDataList = new ArrayList<>();
		compDataList = da.getCompDetailsPerScore(viewId, subViewId, country, type, score, "Comp", viewText,sdf.format(date).trim());
		return compDataList;
	}
	
    public List<Views> getSubViews(int id) {
        return subViewsCache.computeIfAbsent(String.valueOf(id), k -> {
            List<Views> subViewList = da.getSubViews(id);
            return subViewList;
        });
    }

    public void getElementFromDataAccess() {
        List<ElementVisibility> list = da.getVisibleElement();
        for (ElementVisibility elem : list) {
            visibileData.putIfAbsent(elem.getName(), elem.getIsVisible());
        }
    }

    public String getUsername() {
		return username;
	}

	public List<Countres> getCountres() {
		return countres;
	}

	public void setCountres(List<Countres> countres) {
		this.countres = countres;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurCountry() {
		return curCountry;
	}

	public void setCurCountry(String curCountry) {
		this.curCountry = curCountry;
	}

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}
	
	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	public String getViewText() {
		return viewText;
	}

	public void setViewText(String viewText) {
		this.viewText = viewText;
	}

	public int getViewId() {
		return viewId;
	}

	public void setViewId(int viewId) {
		this.viewId = viewId;
	}	

	public int getSubViewId() {
		return subViewId;
	}

	public void setSubViewId(int subViewId) {
		this.subViewId = subViewId;
	}

	public Views getSelectedView() {
		return selectedView;
	}

	public void setSelectedView(Views selectedView) {
		this.selectedView = selectedView;
	}

	public Map<Integer, List<String>> getCountryMap() {
		return countryMap;
	}

	public void setCountryMap(Map<Integer, List<String>> countryMap) {
		this.countryMap = countryMap;
	}

	public List<Views> getSymantecList() {
		return symantecList;
	}

	public void setSymantecList(List<Views> symantecList) {
		this.symantecList = symantecList;
	}

	public List<Views> getViewList() {
		return viewList;
	}

	public void setViewList(List<Views> viewList) {
		this.viewList = viewList;
	}

	public Map<String, Integer> getVisibileData() {
		return visibileData;
	}

	public void setVisibileData(Map<String, Integer> visibileData) {
		this.visibileData = visibileData;
	}

	public List<AssetInfo> getCovDataList() {
		return covDataList;
	}

	public List<ComplianceInfo> getCompDataList() {
		return compDataList;
	}

	public void setCovDataList(List<AssetInfo> covDataList) {
		this.covDataList = covDataList;
	}

	public void setCompDataList(List<ComplianceInfo> compDataList) {
		this.compDataList = compDataList;
	}

	public List<AllRadarData> getRadarInfo() {
		return radarInfo;
	}

	public void setRadarInfo(List<AllRadarData> radarInfo) {
		this.radarInfo = radarInfo;
	}

	@Override
    public void reset() {
		
    }

    @Override
    public String getAppName() {
        return "Dashboard";
    }

    @Override
    public String getTitle() {
        return "Arab Bank Radar Dashboard  V.";
    }
}
