package com.app.dev.cmon.utilites;

public class CommonsViewData {
	public static final String SELECT = " select * ";
	public static final String Existing = " select 'Existing' as Type, ";
	public static final String SUMCASE = " SUM(case when ";
	public static final String CONDITION = " then 1 else 0 end) ";
	public static final String SCORECONDITION = " then Score else 0 end) ";
	public static final String TOTALSUM = " as TotalSum , ";
	public static final String GREENCIRCLE = " as GreenCircle , ";
	public static final String ORANGECIRCLE = " as OrangeCircle ,";
	public static final String REDCIRCLE = " as RedCircle, ";
	public static final String IT = " as IT, ";
	public static final String RISK = " as Risk  ";
	public static final String FROMCOVERAGE = " from ISACWEB.dbo.[External_Cmon_Coverage_All](?,?,?) ";
	public static final String FROMCOMPLIANCE = " from ISACWEB.dbo.[External_Cmon_Compliance](?,?,?) ";
	public static final String FROMCOVERAGEHISTORY = " from ISACWEB.dbo.[External_Cmon_Coverage_All_Dynamic](?,?,?,?) ";
	public static final String FROMCOMPLIANCEHISTORY = " from ISACWEB.dbo.[External_Cmon_Compliance_Dynamic](?,?,?,?) ";
	public static final String UNION = " union ";
	public static final String NEW = "select 'New' as Type,";

}
