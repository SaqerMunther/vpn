package com.app.dev.cmon.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.app.dev.cmon.cache.CacheService;
import com.app.dev.cmon.components.AllRadarData;
import com.app.dev.cmon.components.ViewInfo;
import com.app.dev.cmon.controllers.DataAccess;
import com.app.dev.cmon.controllers.RadarController;
import com.arabbank.devf.cmon.controllers.portal.CmonManagedBean;

@SessionScoped
@ManagedBean(name = "radarWeekData")
public class ProcessWeeklyData extends CmonManagedBean {
	private DataAccess da = new DataAccess();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private RadarController radar = ((RadarController) getManagedBeanByName("radarController"));
	private CycleValidationService radarValidation = ((CycleValidationService) getManagedBeanByName("radarValidation"));
	private LocalDate now;
	private AllRadarData filter;
    private List<AllRadarData> allRadar = new ArrayList<>();
	private AllRadarData greenExist = new AllRadarData();
	private AllRadarData greenNew = new AllRadarData();
	private AllRadarData orangeExist = new AllRadarData();
	private AllRadarData orangeNew = new AllRadarData();
	private AllRadarData redExist = new AllRadarData();
	private AllRadarData redNew = new AllRadarData();
	
    @PostConstruct
    public void init() {
    	allRadar = da.getRadarData(radar.getViewId());
    }
	
    public List<ViewInfo> getDataPerWeek(String type, String isNew) {
        String dateStr = sdf.format(radar.getDate1()).trim();
        String key = CacheService.getInstance().getCacheKey("DataPerWeek", type + "_" + isNew, radar.getViewId(), radar.getSubViewId(), radar.getCountry(), dateStr);
        
        List<ViewInfo> cachedResult = CacheService.getInstance().getFromCache(key);
        if (cachedResult != null && !cachedResult.isEmpty()) {
            return cachedResult;
        }
        
        List<ViewInfo> data = da.getCoveragePerWeek(radar.getViewId(), radar.getSubViewId(), 
        		radar.getCountry().equalsIgnoreCase("all") ? "" : radar.getCountry(), type, radar.getViewText(), dateStr);
        CacheService.getInstance().putInCache(key, data);
        return data;
    }
    
    //////////////////////////////////////////////////////////////////////////////

	public String getWeekBox(String weeknum, String type) {
	    long end = 0;
	    String weekCode = "W";
	    String code = "";
	    String periodType = "";
	    LocalDate lastDate;

	    switch (type) {
	        case "Green-Exist":
	            end = greenExist.getEnd();
	            periodType = greenExist.getTimePeriod();
	            break;
	        case "Green-New":
	            end = greenNew.getEnd();
	            periodType = greenNew.getTimePeriod();
	            break;
	        case "Orange-Exist":
	            end = orangeExist.getEnd();
	            periodType = orangeExist.getTimePeriod();
	            break;
	        case "Orange-New":
	            end = orangeNew.getEnd();
	            periodType = orangeNew.getTimePeriod();
	            break;
	        case "Red-Exist":
	            end = redExist.getEnd();
	            periodType = redExist.getTimePeriod();
	            break;
	        case "Red-New":
	            end = redNew.getEnd();
	            periodType = redNew.getTimePeriod();
	            break;
	    }

	    lastDate = calculateLastDate(end, periodType);

	    if (weeknum.equalsIgnoreCase(">")) {
	        code = getCodeForGreaterThanWeek(lastDate);
	    } else {
	        code = getCodeForSpecificWeek(lastDate, weeknum, weekCode);
	    }

	    return code;
	}
	
	
	private LocalDate calculateLastDate(long end, String periodType) {
		now = radarValidation.getLastValidDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	    if (periodType.contains("month")) {
	        return now.minusMonths(end);
	    } else if (periodType.contains("week")) {
	        return now.minusWeeks(end);
	    } else {
	        return now.minusDays(end);
	    }
	}
	
	private String getCodeForGreaterThanWeek(LocalDate lastDate) {
	    long day = ChronoUnit.DAYS.between(lastDate, now);
	    day = Math.abs(day);

	    LocalDate date = now.minusDays(day - (4 * 7) + 1);
	    int weekNumber = getWeekNum(date.getDayOfMonth());
	    String month = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

	    return ">" + " " + month;
	}

	private String getCodeForSpecificWeek(LocalDate lastDate, String weeknum, String weekCode) {
	    int week = Integer.parseInt(weeknum.substring(1));
	    long day = ChronoUnit.DAYS.between(lastDate, now);
	    day = Math.abs(day);

	    LocalDate date = now.minusDays(day - (week * 7) + 1);
	    int weekNumber = getWeekNum(date.getDayOfMonth());
	    String month = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

	    return month + " " + weekCode + weekNumber;
	}


	private int getWeekNum(int dayOfMonth) {
	    return (dayOfMonth - 1) / 7 + 1;
	}

	
	
	public void getFilterAll(String type, String IDName) {
		if (allRadar == null || allRadar.isEmpty()) {
			allRadar = da.getRadarData(radar.getViewId());
		} else if (allRadar.get(0).getViewID() != radar.getViewId()) {
			allRadar = da.getRadarData(radar.getViewId());
		}

		for (AllRadarData item : allRadar) {
			if (item.getColorType().equalsIgnoreCase(type) && item.getIDName().equalsIgnoreCase(IDName)) {
				filter = item;
			}
			if (item.getIDName().contains("Cov")) {
				switch (item.getColorType()) {
				case "Green-Exist":
					greenExist = item;
					break;
				case "Green-New":
					greenNew = item;
					break;
				case "Orange-Exist":
					orangeExist = item;
					break;
				case "Orange-New":
					orangeNew = item;
					break;
				case "Red-Exist":
					redExist = item;
					break;
				case "Red-New":
					redNew = item;
					break;
				}
			}
		}

	}
	
	
	private String formatRange(String columnDateName, int start, int end, String idName, String period) {
		if ("Score".equalsIgnoreCase(columnDateName)) {
			if (start == 0) {
				return "<" + end + "%";
			} else {
				return start + "% - " + (end == 101 ? 100 : end) + "%";
			}
		} else if ("day".equalsIgnoreCase(period)) {
			if (start == 0 && end >= 30) {
				if ((end - start) >= 30 && end < 1000) {
					return "< " + (end / 30) + " Month";
				}
			} else if ((end >= 60) && (start >= 30) && (end < 1000)) {
				return start / 30 + " - " + end / 30 + " Month";
			} else if (start != 0 && start < 30 && end <= 30) {
				return (int) (start / 7) + " - " + (int) (end / 7) + " Week";
			} else if (end >= 1000 && start >= 30) {
				return "> " + (start / 30) + " Month";
			}
		} else if ("week".equalsIgnoreCase(period)) {
			if (start == 0 && end < 500) {
				return "< " + (end) + " Week";
			} else if(end < 500) {
				return start + " - " + (end) + " Week";	
			} else if(start != 0 && end > 500) {
				return "> " + (start) + " Week";	
			}
		} else if ("month".equalsIgnoreCase(period)) {
			if (start == 0 && end < 500) {
				return "< " + (end) + " Month";
			} else if(start != 0 && end < 500) {
				return start + " - " + (end) + " Month";	
			} else if(start != 0 && end > 500) {
				return "> " + (start) + " Month";	
			}
		}

		if (idName.contains("Cov")) {
			return "Coverage";
		} else {
			return "Comply";
		}
	}
	
	public String getDeurationAndDate(String colorType, String type) {
		AllRadarData info;
		getFilterAll(colorType, type + "-" + radar.getViewId());
		info = filter;
		String tooltip = "";
		if(info == null) {
			if(type.equalsIgnoreCase("Cov")) {
				return "Coverage";
			}else {
				return "Comply";
			}
		} else {
			tooltip = formatRange(info.getColumnDateName(), info.getStart(), info.getEnd(), info.getIDName(),
					info.getTimePeriod());
			if (tooltip.equalsIgnoreCase("Comply") || tooltip.equalsIgnoreCase("Coverage")) {
				return tooltip;
			} else {
				return separateCamelCase(info.getColumnDateName()) + " " + tooltip;
			}
		}
	}
	
	private static String separateCamelCase(String input) {
		return input.replaceAll(String.format("%s|%s|%s",
				"(?<=[A-Z])(?=[A-Z][a-z])",
				"(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"),
				" ");
	}
	
	
	public String getDeuration(String colorType, String type) {
		AllRadarData info;
		getFilterAll(colorType, type + "-" + radar.getViewId());
		info = filter;
		if(info == null) {
			if(type.equalsIgnoreCase("Cov")) {
				return "Coverage";
			}else {
				return "Comply";
			}
		} else {
			return formatRange(info.getColumnDateName(), info.getStart(),
					info.getEnd(), info.getIDName(), info.getTimePeriod());
		}
	}
	///////////////////////////////////////////////////////////////////////////////
	
	public String getLabel(String name) {
		String lable = "";
		if (filter != null) {
			if (name.equalsIgnoreCase("first")) {
				lable = (int) (((filter.getEnd() - filter.getStart()) / 2) + filter.getStart()) + "%-"
						+ (filter.getEnd() == 101 ? filter.getEnd() - 1 : filter.getEnd()) + "%";
			} else {
				lable = filter.getStart() + "%-"
						+ ((int) (((filter.getEnd() - filter.getStart()) / 2) + filter.getStart()) + "%");
			}
		}

		return lable;
	}
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}
    
}
