package com.app.dev.cmon.utilites;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;


import com.app.dev.cmon.components.AssetInfo;
import com.app.dev.cmon.components.ComplianceInfo;


@ManagedBean(name = "Commons")
public class Commons {
	public static final String CYCLEISSUE = "Issue detected. Access previous data";
	public static final String CYCLEFETCHING = "Cycle is running. Access previous data";
	private  HashMap<String, String>countryMapsUrls = new HashMap<>();
	private  List<String> countries = Arrays.asList("ALL", "JO", "EG", "PS", "AE", "LB","QA","BH","YE","MA","DZ","SG","CN","GD","AB");
	private  List<String> countriesFullName = Arrays.asList("ALL", "JORDAN", "EGYPT", "PALESTINE", "EMIRATES", "LEBANON","QATAR","BAHRAIN","YEMEN","MOROCCO","ALGERIA","SINGAPORE","CHINA", "GDR", "ARAB BANK");
	public Commons() {
		String[] counrtiesUrls = {
				"images/ab-circle.png",
				"flags/jor-flag.png",
				"flags/egy-flag.png",
				"flags/pal-flag.png",
				"flags/uae-flag.png",
				"flags/leb-flag.png",
				"flags/qat-flag.png",
				"flags/bah-flag.png",
				"flags/yem-flag.png",
				"flags/mor-flag.png",
				"flags/alg-flag.png",
				"flags/sin-flag.png",
				"flags/chi-flag.png",
				"images/gdr-flag.png",
				"flags/ab-flag.png"
		};
		
		for (int i = 0; i < countries.size(); i++) {
			countryMapsUrls.put(countries.get(i), counrtiesUrls[i]);
		}
	}
	public long getCovCount(List<AssetInfo> list, int startDateInMonth,int endDateInMonth,int isNew) {
		if( list == null )
			 return 0;
		if(list.size() == 0)
			return 0;
		if(list == null)
			return 0 ;
		if(list.size() == 0)
			return 0;
		 LocalDate now = list.get(0).getSyncDate().toLocalDate();
	
		return list
					.stream()
					.filter(p-> { 
                      if(p.getScanDate() !=null)
						if(((p.getScanDate().toLocalDate().isAfter(now.minusMonths(endDateInMonth))
								&&
							p.getScanDate().toLocalDate().isBefore(now.minusMonths(startDateInMonth))	
								)
								|| p.getScanDate().toLocalDate().isEqual(now.minusMonths(startDateInMonth))
								)
								&& p.getIsScanned()== isNew) {
							return true;
						}
						return false;
							
					})
					.count();

}
	
	
	public long getCovCountSEP(List<AssetInfo> list, int startDateInMonth,int endDateInMonth,int isNew , int IsCov) {
		if( list == null )
			 return 0;
		if(list.size() == 0)
			return 0;
		if(list.size() == 0)
			return 0;
		 LocalDate now = list.get(0).getSyncDate().toLocalDate();
	
		return list
					.stream()
					.filter(p-> { 
						if (p.getCreateDate() != null)
							if (((p.getCreateDate().toLocalDate().isAfter(now.minusWeeks(endDateInMonth))
									&& p.getCreateDate().toLocalDate().isBefore(now.minusWeeks(startDateInMonth)))
									|| p.getCreateDate().toLocalDate().isEqual(now.minusWeeks(startDateInMonth)))
									&& p.getIsCoveredByControl() == IsCov
									&& p.getIsNew() == isNew) {
							return true;
						}
						return false;
							
					})
					.count();

}
	
	public long getCovCountDLP(List<AssetInfo> list, int startDateInMonth,int endDateInMonth,int isNew ,int isCov) {
		if( list == null )
			 return 0;
		if(list.size() == 0)
			return 0;
		 LocalDate now = list.get(0).getSyncDate().toLocalDate();
	
		return list
					.stream()
					.filter(p-> { 
                      if(p.getScanDate() !=null)
          				if (((p.getScanDate().toLocalDate().isAfter(now.minusWeeks(endDateInMonth))
        						&& p.getScanDate().toLocalDate().isBefore(now.minusWeeks(startDateInMonth)))
        						|| p.getScanDate().toLocalDate().isEqual(now.minusWeeks(startDateInMonth)))
        						&& p.getIsCoveredByControl() == isCov
        						&& p.getIsNew() == isNew) {
        					return true;
						}
						return false;
							
					})
					.count();

}
	
	public long getComCountDLP(List<ComplianceInfo> list, int startDateInMonth,int endDateInMonth,int isNew ,int isComply) {
		if( list == null )
			 return 0;
		if(list.size() == 0)
			return 0;

		 LocalDate now = list.get(0).getSyncDate().toLocalDate();
	
		return list
					.stream()
					.filter(p-> { 
                      if(p.getEvaluationDate() !=null)
          				if (((p.getEvaluationDate().toLocalDate().isAfter(now.minusWeeks(endDateInMonth))
        						&& p.getEvaluationDate().toLocalDate().isBefore(now.minusWeeks(startDateInMonth)))
        						|| p.getEvaluationDate().toLocalDate().isEqual(now.minusWeeks(startDateInMonth)))
        						&& p.getIsComply() == isComply
        						&& p.getIsNew() == isNew) {
        					return true;
						}
						return false;
							
					})
					.count();

}
	
	public long getComCountSEP(List<ComplianceInfo> list, int startDateInMonth,int endDateInMonth,int isNew ,int isComply) {
		if( list == null )
			 return 0;
		if(list.size() == 0)
			return 0;

		 LocalDate now = list.get(0).getSyncDate().toLocalDate();
	
		return list
					.stream()
					.filter(p-> { 
                      if(p.getEvaluationDate() !=null)
          				if (((p.getEvaluationDate().toLocalDate().isAfter(now.minusWeeks(endDateInMonth))
        						&& p.getEvaluationDate().toLocalDate().isBefore(now.minusWeeks(startDateInMonth)))
        						|| p.getEvaluationDate().toLocalDate().isEqual(now.minusWeeks(startDateInMonth)))
        						&& p.getIsComply() == isComply
        						&& p.getIsNew() == isNew) {
        					return true;
						}
						return false;
							
					})
					.count();

}


    public long getCompLianceCountByScore(List<ComplianceInfo> list,int lowerScore , int upperScore,int isNew,String country) {
    	
		if (list == null)
			return 0;
		return list.stream().filter(p -> {
			if (p.getIsNew() == isNew && (p.getScore() >= lowerScore && p.getScore() < upperScore))
				return true;
			else
				return false;
		})
				.count();
}
    
    public long getCompLianceCountComp(List<ComplianceInfo> list,int lowerScore , int upperScore,int isNew,String country) {
    	
		if (list == null)
			return 0;
		return list.stream().filter(p -> {
			if (p.getIsNew() == isNew && p.getIsComply() == 0 && (p.getScore() >= lowerScore && p.getScore() < upperScore))
				return true;
			else
				return false;
		})
				.count();
}
    
    public long getCompCountSEP(List<ComplianceInfo> list, int startDateInMonth,int endDateInMonth,int isNew , int IsCom) {
		if( list == null )
			 return 0;
		if(list.size() == 0)
			return 0;

		 LocalDate now = list.get(0).getSyncDate().toLocalDate();
	
		return list
					.stream()
					.filter(p-> { 
                      if(p.getCreateDate() !=null)
						if(((p.getCreateDate().toLocalDate().isAfter(now.minusMonths(endDateInMonth))
								&&
							p.getCreateDate().toLocalDate().isBefore(now.minusMonths(startDateInMonth))	
								)
								|| p.getCreateDate().toLocalDate().isEqual(now.minusMonths(startDateInMonth))
								)
								&& p.getIsNew()== isNew
								&& p.getIsComply()== IsCom) {
							return true;
						}
						return false;
							
					})
					.count();

}
    
    public  String getCountryFlagPath(String country) {
    	return countryMapsUrls.get(country);
    }


	public  List<String> getCountries() {
		return countries;
	}
	public List<String> getCountriesFullName() {
		return countriesFullName;
	}
	
	
    
}
