package com.app.dev.cmon.service;

import java.util.List;

import javax.faces.bean.ManagedBean;

import com.app.dev.cmon.components.AllRadarData;
import com.app.dev.cmon.controllers.DataAccess;
import com.app.dev.cmon.controllers.MB_Main;
import com.app.dev.cmon.controllers.RadarController;
import com.arabbank.dev.utility.Pair;
import com.arabbank.dev.utility.Triple;
import com.arabbank.devf.cmon.controllers.portal.CmonManagedBean;

@ManagedBean(name = "radarUpperData")
public class UpperDataService extends CmonManagedBean{
	
	private DataAccess da = new DataAccess();
	private RadarController radar = ((RadarController) getManagedBeanByName("radarController"));
	private MB_Main mbMain = ((MB_Main) getManagedBeanByName("mbMain"));
	
	

	public Triple<Double, Integer, Integer> getCoverageFirstRow(int curr) {
		String key = curr + "" + radar.getCountry() + "" + radar.getViewId() + "" + radar.getSubViewId();

		Triple<Double, Integer, Integer> triple = new Triple<>();
		Pair<Integer, Double> item = getFirstObject(da.getCoverageUpper(curr, radar.getCountry(), radar.getViewId(), radar.getSubViewId()));
		triple.setFirst(divideTwoNumbers(item.getFirst(), item.getSecond()) * 100);
		triple.setSecond(item.getFirst());
		triple.setThird(item.getSecond().intValue());
		return triple;
	}
	//////////////////////////////////////////////

	public Triple<Double, Integer, Integer> getCompUpperData(int cur, int isRisk) {
		Triple<Double, Integer, Integer> triple = new Triple<>();
		String columnDateExist = "";

		if (isRisk == 1) {
			columnDateExist = radar.getRadarInfo()
					.stream().filter(o -> o.getColorType().equalsIgnoreCase("IT-Exist")
							&& o.getIDName().contains("Comp") && o.getViewID() == radar.getViewId())
					.findFirst().map(AllRadarData::getColumnDateName).orElse("");
			triple.setFirst(columnDateExist.equalsIgnoreCase("Score")
					? divideTwoNumbers(mbMain.getComp("Risk", "Existing"), mbMain.getComp("Risk", "New") * 100) * 100
					: (divideTwoNumbers(mbMain.getComp("Risk", "Existing"), mbMain.getComp("Risk", "New")) * 100));
			triple.setSecond(
					getMaxFourDigits(columnDateExist.equalsIgnoreCase("Score") ? (int) (mbMain.getComp("Risk", "Existing"))
							: (int) (mbMain.getComp("Risk", "Existing"))));
			triple.setThird(
					getMaxFourDigits((int) (columnDateExist.equalsIgnoreCase("Score") ? mbMain.getComp("Risk", "New") * 100
							: mbMain.getComp("Risk", "New"))));
		} else {
			columnDateExist = radar.getRadarInfo()
					.stream().filter(o -> o.getColorType().equalsIgnoreCase("Risk-Exist")
							&& o.getIDName().contains("Comp") && o.getViewID() == radar.getViewId())
					.findFirst().map(AllRadarData::getColumnDateName).orElse("");
			triple.setFirst(columnDateExist.equalsIgnoreCase("Score")
					? divideTwoNumbers(mbMain.getComp("IT", "Existing"), mbMain.getComp("IT", "New") * 100) * 100
					: (divideTwoNumbers(mbMain.getComp("IT", "Existing"), mbMain.getComp("IT", "New")) * 100));
			triple.setSecond(
					getMaxFourDigits((int) (columnDateExist.equalsIgnoreCase("Score") ? mbMain.getComp("IT", "Existing")
							: mbMain.getComp("IT", "Existing"))));
			triple.setThird(
					getMaxFourDigits((int) (columnDateExist.equalsIgnoreCase("Score") ? mbMain.getComp("IT", "New") * 100
							: mbMain.getComp("IT", "New"))));
		}
		return triple;
	}
	////////////////////////////////////////////////////////////////////

	public Triple<Double, Integer, Integer> getCovUpperData(int cur) {
		Triple<Double, Integer, Integer> triple = new Triple<>();
		triple.setFirst(divideTwoNumbers(mbMain.getCov("IT", "Existing"), mbMain.getCov("IT", "New")) * 100);
		triple.setSecond(getMaxFourDigits((int) (mbMain.getCov("IT", "Existing"))));
		triple.setThird(getMaxFourDigits((int) (mbMain.getCov("IT", "New"))));
		return triple;
	}

	/////////////////////////////////////////////////

	private Pair getFirstObject(List<Pair<Integer, Double>> list) {
		if (list == null)
			return new Pair<Integer, Double>(0, 1.0);

		if (list.size() == 0)
			return new Pair<Integer, Double>(0, 1.0);

		return list.get(0);
	}

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
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
