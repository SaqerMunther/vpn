package com.app.dev.cmon.controllers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.io.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.app.dev.cmon.components.Reports;
import com.arabbank.devf.cmon.controllers.portal.CmonManagedBean;

@ManagedBean(name = "builtInReports")
@ViewScoped
public class BuiltInReports extends CmonManagedBean {

	private static DataAccess da = new DataAccess();
	public static List<Reports> report = new ArrayList<>();
	private String BUILTIN_REPORT_REDIRECT = "radar/inc/builtin-report.xhtml";
	RadarController radar = ((RadarController) getManagedBeanByName("radarController"));
	@PostConstruct
	public void init() {
		getValue();
	}
	
	public List<Reports> getValue(){
		report = da.getReports(radar.getUsername());		
		return report;
	}

	public static void downloadExcel(int id, String name) throws IOException, SQLException {
	    Optional<String> filterList = report.stream()
	        .filter(o -> id == o.getReportId())
	        .map(Reports::getQuery)
	        .findFirst();
	    
	    String query = filterList.orElse("No");
	    
	    try (Workbook workbook = new XSSFWorkbook()) {
	        List<List<Object>> resultSet = da.exQuery(query); 
	        
	        Sheet sheet = workbook.createSheet("Report "+"Id #"+ id); 
	        
	        if (!resultSet.isEmpty()) {
	            // Write header
	            List<Object> headerList = resultSet.get(0); 
	            Row headerRow = sheet.createRow(0);
	            int cellIndex = 0;
	            for (Object header : headerList) {
	                Cell cell = headerRow.createCell(cellIndex++);
	                cell.setCellValue(header != null ? header.toString() : ""); 
	            }  
	            // Format header row
	            CellStyle headerStyle = workbook.createCellStyle();
	            Font font = workbook.createFont();
	            font.setBold(true);
	            headerStyle.setFont(font);
	            for (int i = 0; i < headerList.size(); i++) {
	                headerRow.getCell(i).setCellStyle(headerStyle);
	            }
	            
	            // Write data rows
	            for (int rowIndex = 1; rowIndex < resultSet.size(); rowIndex++) {
	                List<Object> row = resultSet.get(rowIndex); 
	                Row dataRow = sheet.createRow(rowIndex); 
	                cellIndex = 0; 
	                
	                for (Object value : row) {
	                    Cell cell = dataRow.createCell(cellIndex++); 
	                    if (value instanceof String) {
	                        cell.setCellValue((String) value);
	                    } else if (value instanceof Number) {
	                        cell.setCellValue(((Number) value).doubleValue());
	                    } else if (value instanceof Boolean) {
	                        cell.setCellValue((Boolean) value);
	                    } else {
	                        cell.setCellValue(value != null ? value.toString() : ""); 
	                    }
	                }
	            }
	        }
	        
	        // Auto-size columns
	        for (int i = 0; i < resultSet.get(0).size(); i++) {
	            sheet.autoSizeColumn(i);
	        }

	        // Prepare response for download
	        FacesContext facesContext = FacesContext.getCurrentInstance();
	        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
	        
	        response.reset(); 
	        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	        response.setHeader("Content-Disposition", "attachment; filename="+" "+ name +".xlsx");

	        try (OutputStream out = response.getOutputStream()) {
	            workbook.write(out);
	            out.flush(); 
	        }

	        facesContext.responseComplete(); 
	        
	    } catch (IOException e) {
	        e.printStackTrace(); 
	    } 
	}
	public void redirectToBuiltInReports() throws IOException {
		logger.debug("HELLLLLO");
		FacesContext.getCurrentInstance().getExternalContext().redirect(BUILTIN_REPORT_REDIRECT);
		logger.debug("BYEE");
	}
	
	
	public List<Reports> getReport() {
		return report;
	}

	public void setReport(List<Reports> report) {
		this.report = report;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	
	
}
