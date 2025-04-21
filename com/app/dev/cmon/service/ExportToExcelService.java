package com.app.dev.cmon.service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.app.dev.cmon.components.AssetInfo;
import com.app.dev.cmon.components.ComplianceInfo;
import com.app.dev.cmon.components.CountryLeftCardData;
import com.app.dev.cmon.controllers.DataAccess;
import com.app.dev.cmon.controllers.RadarController;
import com.arabbank.devf.cmon.controllers.portal.CmonManagedBean;

@ManagedBean(name = "radarExport")
public class ExportToExcelService extends CmonManagedBean {
	
	private DataAccess da = new DataAccess();
	private RadarController radar = ((RadarController) getManagedBeanByName("radarController"));

	private List<CountryLeftCardData> countryAssetList;
	private String circleDetail = "";
	private List<ComplianceInfo> compDataList;
	private List<AssetInfo> covDataList;
	
	public void exportToExcelForComplCircle() throws IOException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheet.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=" + radar.getViewText() + " - " + this.circleDetail
				+ " - " + "Gap For " + radar.getCurCountry() + " " + getCurrentDate() + ".xlsx");

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Data");
		// create Cell Style for header row
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		Font headerFont = workbook.createFont();

		headerFont.setColor(IndexedColors.WHITE.getIndex());
		headerFont.setBold(true);
		headerFont.setItalic(true);
		headerFont.setFontHeightInPoints((short) 12);

		headerCellStyle.setFont(headerFont);

		Row headerRow = sheet.createRow(0);
		Field[] fields = ComplianceInfo.class.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String headerName = field.getName();
			Cell headerCell = headerRow.createCell(i);
			headerCell.setCellValue(headerName);
			headerCell.setCellStyle(headerCellStyle);
		}

		// Date Format
		CreationHelper creationHelper = workbook.getCreationHelper();
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));

		// Sort
		Collections.sort(compDataList, Comparator.comparing(ComplianceInfo::getHostName));

		for (int i = 0; i < compDataList.size(); i++) {
			Row dataRow = sheet.createRow(i + 1);
			ComplianceInfo leftCardData = compDataList.get(i);
			for (int j = 0; j < fields.length; j++) {
				Field field = fields[j];
				field.setAccessible(true);
				Object value = null;
				try {
					value = field.get(leftCardData);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Cell dataCell = dataRow.createCell(j);
				if (value instanceof String) {
					dataCell.setCellValue((String) value);
				} else if (value instanceof Number) {
					dataCell.setCellValue(((Number) value).doubleValue());
				} else if (value instanceof Date) {
					dataCell.setCellValue((Date) value);
					dataCell.setCellStyle(dateCellStyle);
				}

			}
		}
		// add filter to file for all columns
		sheet.setAutoFilter(
				CellRangeAddress.valueOf("A1:" + getLastColumnName(fields.length) + (compDataList.size() + 1)));
		workbook.write(response.getOutputStream());
		facesContext.responseComplete();

	}
	
	/////////////////////////////////////////////////////////////////////////////////
	public void exportToExcel(int country, int viewId) throws IOException {
		countryAssetList = da.getCountryLeftCardData(radar.getCountry(), country, viewId, radar.getViewText());

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();

		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheet.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=" + radar.getViewText() + " - " + "Gap For "
				+ radar.getCurCountry() + " " + getCurrentDate() + ".xlsx");

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Data");
		// create Cell Style for header row
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		Font headerFont = workbook.createFont();

		headerFont.setColor(IndexedColors.WHITE.getIndex());
		headerFont.setBold(true);
		headerFont.setItalic(true);
		headerFont.setFontHeightInPoints((short) 12);

		headerCellStyle.setFont(headerFont);

		Row headerRow = sheet.createRow(0);
		Field[] fields = CountryLeftCardData.class.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String headerName = field.getName();
			Cell headerCell = headerRow.createCell(i);
			headerCell.setCellValue(headerName);
			headerCell.setCellStyle(headerCellStyle);
		}

		// Date Format
		CreationHelper creationHelper = workbook.getCreationHelper();
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));

		// Sort
		Collections.sort(countryAssetList, Comparator.comparing(CountryLeftCardData::getVersionParentName));

		for (int i = 0; i < countryAssetList.size(); i++) {
			Row dataRow = sheet.createRow(i + 1);
			CountryLeftCardData leftCardData = countryAssetList.get(i);
			for (int j = 0; j < fields.length; j++) {
				Field field = fields[j];
				field.setAccessible(true);
				Object value = null;
				try {
					value = field.get(leftCardData);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Cell dataCell = dataRow.createCell(j);
				if (value instanceof String) {
					dataCell.setCellValue((String) value);
				} else if (value instanceof Number) {
					dataCell.setCellValue(((Number) value).doubleValue());
				} else if (value instanceof Date) {
					dataCell.setCellValue((Date) value);
					dataCell.setCellStyle(dateCellStyle);
				}

			}
		}
		// add filter to file for all columns
		sheet.setAutoFilter(
				CellRangeAddress.valueOf("A1:" + getLastColumnName(fields.length) + (countryAssetList.size() + 1)));
		workbook.write(response.getOutputStream());
		facesContext.responseComplete();

	}
	//////////////////////////////////////////////////////////////
	public void getLocationId(String country, int viewId) throws IOException {
		switch (country) {
		case "JO":
			exportToExcel(2, viewId);
			break;
		case "AE":
			exportToExcel(3, viewId);
			break;
		case "QA":
			exportToExcel(4, viewId);
			break;
		case "BH":
			exportToExcel(5, viewId);
			break;
		case "LB":
			exportToExcel(6, viewId);
			break;
		case "EG":
			exportToExcel(7, viewId);
			break;
		case "PS":
			exportToExcel(8, viewId);
			break;
		case "YE":
			exportToExcel(9, viewId);
			break;
		case "MA":
			exportToExcel(10, viewId);
			break;
		case "DZ":
			exportToExcel(11, viewId);
			break;
		case "SG":
			exportToExcel(12, viewId);
			break;
		case "CN":
			exportToExcel(13, viewId);
			break;
		default:
			exportToExcel(1, viewId);
			break;
		}
	}
	
	/////////////////////////////////////////////////////////////////
	
	private String getLastColumnName(int columnCount) {
		StringBuilder columnName = new StringBuilder();
		while (columnCount > 0) {
			columnCount--;
			columnName.insert(0, (char) ('A' + (columnCount % 26)));
			columnCount /= 26;
		}
		return columnName.toString();
	}
	/////////////////////////////////////////////////////////////////

	public String getCurrentDate() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss "));
	}
	
	
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	
	
	public List<CountryLeftCardData> getJordanAssetList() {
		return countryAssetList;
	}

	public void setJordanAssetList(List<CountryLeftCardData> countryAssetList) {
		this.countryAssetList = countryAssetList;
	}

}
