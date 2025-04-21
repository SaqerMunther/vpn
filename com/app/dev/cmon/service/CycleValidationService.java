package com.app.dev.cmon.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.app.dev.cmon.cache.CacheService;
import com.app.dev.cmon.components.RadarDashValidation;
import com.app.dev.cmon.components.ServiceSyncMain;
import com.app.dev.cmon.controllers.DataAccess;
import com.app.dev.cmon.controllers.RadarController;
import com.app.dev.cmon.utilites.Commons;
import com.arabbank.devf.cmon.controllers.portal.CmonManagedBean;

@ManagedBean(name = "radarValidation")
@SessionScoped
public class CycleValidationService extends CmonManagedBean implements Serializable {
	private static final long serialVersionUID = 1L;
	 
	private Commons commons = new Commons();
	private DataAccess da = new DataAccess();
	private RadarController radar = ((RadarController) getManagedBeanByName("radarController"));
	private List<RadarDashValidation> radarValidation;
	private String cycleStatus;
	private List<ServiceSyncMain> syncList;
	private Date LastValidDate;
	private boolean validateCycle;
	
    @PostConstruct
    public void init() {
    	radarValidation = da.getRadarDashValidation();
    	syncList = da.getLatestSyncMain();
    	setDateValidations();
    }
	
	public void setDateValidations() {
		radarValidation = da.getRadarDashValidation();
		if (radarValidation == null || radarValidation.isEmpty()) {
			setLastValidDate(new Date());
		}
			
		Map<Integer, List<RadarDashValidation>> validationMap = radarValidation.stream()
				.collect(Collectors.groupingBy(RadarDashValidation::getSyncID));

		Optional<Date> lastValidDate = validationMap.values().stream().flatMap(List::stream)
				.filter(validation -> validation.isComplianceMatch() && validation.isCoverageMatch())
				.map(validation -> convertLocalDateTimeToMidnightDate(validation.getInsertDate())).max(Date::compareTo);
		if (new Date().before(lastValidDate.orElse(new Date()))) {
			setLastValidDate(new Date());
		} else {
			setLastValidDate(lastValidDate.orElse(new Date()));
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	public void validateCycle() {
		syncList = da.getLatestSyncMain();
		ServiceSyncMain lastSync = syncList.stream().max(Comparator.comparingInt(ServiceSyncMain::getSyncID))
				.orElse(new ServiceSyncMain());
		if (lastSync.isFetching()) {
			setCycleStatus(commons.CYCLEFETCHING);
			setValidateCycle(true);
			return;
		}

		if (normalizeToMidnight(getLastValidDate()).before(normalizeToMidnight(lastSync.getCompletedDate()))) {
			setCycleStatus(commons.CYCLEISSUE);
			setValidateCycle(true);
			return;
		}
		
		if(lastSync.getCycleID() != CacheService.getInstance().getLastSyncID()) {
			setCycleStatus(commons.CYCLEISSUE);
			setValidateCycle(true);
			return;
		}
	}
	
	//////////////////////////////////////////
	
	public void validateCaching() {
		syncList = da.getLatestSyncMain();
		LocalDate completedDate;
		LocalDate currentDate = LocalDate.now();
		ServiceSyncMain lastSync = syncList.stream().max(Comparator.comparingInt(ServiceSyncMain::getSyncID))
				.orElse(new ServiceSyncMain());
		if (lastSync.getCompletedDate() != null) {
			completedDate = lastSync.getCompletedDate().toInstant().atZone(ZoneId.systemDefault())
					.toLocalDate();
			if(!currentDate.isEqual(completedDate)) {
				CacheService.getInstance().clearCache();
				CacheService.getInstance().setLastSyncID(lastSync.getSyncID());
				setValidateCycle(false);
				return;
			}
		}
	}
	
	//////////////////////////////////////////
	
	public boolean checkValidations(List<RadarDashValidation> validations ,long lastSynkId) {
		if (validations == null || validations.isEmpty()) {
			return true;
		}
		
        for (RadarDashValidation validation : validations) {
            if (validation.getSyncID() == lastSynkId) {
                if (!validation.isComplianceMatch() || !validation.isCoverageMatch()) {
                    return true;
                }
            }
        }
		return false;
	}
	
	
	public Date convertLocalDateTimeToMidnightDate(LocalDateTime localDateTime) {
	    if (localDateTime == null) {
	        return null; 
	    }
	    LocalDateTime startOfDay = localDateTime.toLocalDate().atStartOfDay();
	    return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	private Date normalizeToMidnight(Date date) {
	    if (date == null) {
	        return null;
	    }
	    
	    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	    
	    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}


	@Override
	public void reset() {
	}

	public List<RadarDashValidation> getRadarValidation() {
		return radarValidation;
	}

	public void setRadarValidation(List<RadarDashValidation> radarValidation) {
		this.radarValidation = radarValidation;
	}

	public String getCycleStatus() {
		return cycleStatus;
	}

	public void setCycleStatus(String cycleStatus) {
		this.cycleStatus = cycleStatus;
	}

	public Date getLastValidDate() {
		return LastValidDate;
	}

	public void setLastValidDate(Date lastValidDate) {
		LastValidDate = lastValidDate;
	}

	public boolean isValidateCycle() {
		return validateCycle;
	}

	public void setValidateCycle(boolean validateCycle) {
		this.validateCycle = validateCycle;
	}
	

	
	////////////////////////////////////////////////////////////////////////////////

	
}
