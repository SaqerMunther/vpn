package com.app.dev.cmon.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.faces.bean.ManagedBean;

import com.app.dev.cmon.cache.CacheService;
import com.app.dev.cmon.components.ViewInfo;
import com.app.dev.cmon.controllers.DataAccess;
import com.app.dev.cmon.controllers.RadarController;
import com.arabbank.devf.cmon.controllers.portal.CmonManagedBean;

@ManagedBean(name = "radarCalendar")
public class CaledarService extends CmonManagedBean {
	private DataAccess da = new DataAccess();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private RadarController radar = ((RadarController) getManagedBeanByName("radarController"));
	
	
	public List<Date> getDistinctDate() {
		LocalDate currDate = LocalDate.now(); 
		List<ViewInfo> veiwData;
		String key = "AvailabelDate" + currDate;
		List<ViewInfo> cachedResult = CacheService.getInstance().getFromCache(key);
		if (cachedResult != null && !cachedResult.isEmpty()) {
			veiwData = cachedResult;
		} else {
			veiwData = da.getAvailabelDate();
			CacheService.getInstance().putInCache(key, veiwData);
		}

		List<LocalDate> AvailabelDate = veiwData.stream().map(s -> LocalDate.parse(s.getDate(), DateTimeFormatter.ISO_DATE))
				.distinct().collect(Collectors.toList());

		List<Date> dates = AvailabelDate.stream()
				.map(localDate -> localDate.atStartOfDay(ZoneId.of("America/Halifax")).toInstant()).map(Date::from)
				.collect(Collectors.toList());
		return dates;
	}
	/////////////////////////////////////////////////////////////////////////////////
	 public List<Date> getDisabledDates() {
	        int currentYear = LocalDate.now().getYear();
	        int previousYear = currentYear - 1;
	        int nextYear = currentYear + 1;

	        LocalDate startDate = LocalDate.of(previousYear, 1, 1);
	        LocalDate endDate = LocalDate.of(nextYear, 12, 31);

	        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

	        List<LocalDate> allDates = Stream.iterate(startDate, date -> date.plusDays(1))
	                .limit(daysBetween)
	                .collect(Collectors.toList());

	        List<LocalDate> enabledDates = getDistinctDate().stream()
	                .map(date -> date.toInstant().atZone(ZoneId.of("America/Halifax")).toLocalDate())
	                .collect(Collectors.toList());

	        List<LocalDate> disabledDates = allDates.stream()
	                .filter(date -> !enabledDates.contains(date))
	                .collect(Collectors.toList());

	        LocalDate today = getMaxEnabledDate(enabledDates);
	        disabledDates = disabledDates.stream()
	                .filter(date -> !date.isEqual(today))
	                .collect(Collectors.toList());

	        List<Date> result = disabledDates.stream()
	                .map(localDate -> localDate.atStartOfDay(ZoneId.of("America/Halifax")).toInstant())
	                .map(Date::from)
	                .collect(Collectors.toList());
	        return result;
	    }
	 
		public LocalDate getMaxEnabledDate(List<LocalDate> enabledDates) {
			Optional<LocalDate> maxDate = enabledDates.stream().max(Comparator.naturalOrder());

			return maxDate.orElse(null);
		}
	 
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
