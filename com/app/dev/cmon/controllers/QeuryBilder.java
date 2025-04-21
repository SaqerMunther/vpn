package com.app.dev.cmon.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.app.dev.cmon.components.AllRadarData;
import com.app.dev.cmon.components.Views;
import com.app.dev.cmon.utilites.CommonsViewData;

public class QeuryBilder {
	DataAccess dataAccess;
	StringBuilder builder = new StringBuilder();
	String query;
	List<AllRadarData> allRadar = new ArrayList<>();
	AllRadarData greenNew = new AllRadarData();
	AllRadarData greenExist = new AllRadarData();
	AllRadarData orangeNew = new AllRadarData();
	AllRadarData orangeExist = new AllRadarData();
	AllRadarData redNew = new AllRadarData();
	AllRadarData redExist = new AllRadarData();
	AllRadarData totalSum = new AllRadarData();
	AllRadarData totalSumNew = new AllRadarData();
	AllRadarData riskExist = new AllRadarData();
	AllRadarData itNew = new AllRadarData();
	AllRadarData riskNew = new AllRadarData();
	AllRadarData itExist = new AllRadarData();
	AllRadarData filter = new AllRadarData();

	public QeuryBilder() {
		dataAccess = new DataAccess();
	}

	public void fillTheData(String idName, String viewName) {
		allRadar = dataAccess.getAllRadarData(idName, viewName);
		if (allRadar == null || allRadar.isEmpty() && allRadar.size() < 24) {
			allRadar = new ArrayList<AllRadarData>();
		}
		for (AllRadarData item : allRadar) {
			switch (item.getColorType()) {
			case "TotalSum":
				totalSum = item;
				break;
			case "TotalSumNew":
				totalSumNew = item;
				break;
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
			case "IT-Exist":
				itExist = item;
				break;
			case "IT-New":
				itNew = item;
				break;
			case "Risk-Exist":
				riskExist = item;
				break;
			case "Risk-New":
				riskNew = item;
				break;
			}
		}
	}

	public void getFilter(String type, String IDName, String viewName) {
		allRadar = dataAccess.getAllRadarData(IDName, viewName);
		for (AllRadarData item : allRadar) {
			if (item.getColorType().equalsIgnoreCase(type) && item.getIDName().equalsIgnoreCase(IDName)) {
				filter = item;
			}

		}
	}

	public AllRadarData setDefaultsIfNull(AllRadarData item) {
		if (item.getColorType() == null) {
			item = new AllRadarData();
			item.setIsScanned(-1);
			item.setTimePeriod("day");
			item.setIDName("Cov-");
			item.setEnd(30);
			item.setColumnDateName("CreateDate");
			item.setViewName("NA");
		} else if (item.getColorType() == null || item.getColumnDateName() == null || item.getIDName() == null) {
			if (item.getIDName() == null) {
				item.setIDName("Cov-");
			}
			if (!item.getIDName().contains("Cov")) {
				item.setIsScanned(-1);
			}

			if (item.getEnd() <= item.getStart()) {
				item.setEnd(30);
			}
			if (item.getTimePeriod() == null) {
				item.setTimePeriod("day");
			}
			if (item.getColumnDateName() == null) {
				item.setColumnDateName("CreateDate");
			}
			if (item.getViewName() == null) {
				item.setViewName("NA");
			}
		}
		return item;
	}

	////////////////////////////////////////////////
	/////////// return Details for circle////////////

	public String getDetails(String type, String idName, String viewName, String date) {
		getFilter(type, idName, viewName);
		if (idName.contains("Cov")) {
			builder.append(CommonsViewData.SELECT + " " + CommonsViewData.FROMCOVERAGEHISTORY + " Where ");
		} else {
			builder.append(CommonsViewData.SELECT + " " + CommonsViewData.FROMCOMPLIANCEHISTORY + " Where ");
		}
		filter = setDefaultsIfNull(filter);
		if (filter.getIsScanned() == -1) {
		} else {
			builder.append(" IsScanned = " + filter.getIsScanned() + " and ");
		}
		if (filter.getIsNew() == -1) {
		} else {
			builder.append(" IsNew = " + filter.getIsNew() + " and ");
		}
		if (filter.getIsComply() == -1) {
		} else {
			builder.append(" IsComply = " + filter.getIsComply() + " and ");
		}
		if (filter.getIsCovered() == -1) {
		} else {
			builder.append(" IsCoveredByControl = " + filter.getIsCovered() + " and ");
		}
		if (filter.getColumnDateName() != null && filter.getColumnDateName().equalsIgnoreCase("Score")) {
			builder.append(" (" + filter.getColumnDateName() + " >= " + filter.getStart() + " and "
					+ filter.getColumnDateName() + " < " + filter.getEnd() + " ) ");
		} else {
			if (filter.getColumnDateName() == null) {
				builder.append(" EvaluationDate  <= CAST(DATEADD(day,0, "+"'"+ date +"'"+") AS DATETIME) + '23:00:00' ");
			} else if (filter.getEnd() >= 1000) {
				builder.append(filter.getColumnDateName() + " <= CAST(DATEADD(" + filter.getTimePeriod() + "," + "-"
						+ filter.getStart() + ",  "+"'"+ date +"'"+")  AS DATETIME) + '23:00:00' ");
			} else if(filter.getColumnDateName() != null) {
				builder.append(filter.getColumnDateName() + " <= CAST(DATEADD(" + filter.getTimePeriod() + "," + "-"
						+ filter.getStart() + ",  "+"'"+ date +"'"+")  AS DATETIME) + '23:00:00' " + " and " + filter.getColumnDateName() + " > DATEADD("
						+ filter.getTimePeriod() + "," + "-" + filter.getEnd() + ",  "+"'"+ date +"'"+") ");
			}
		}
		if (filter.getTypeName() != null) {
			if (filter.getTypeOperation().contains("in")) {
				builder.append(" and " + filter.getTypeName() + " BETWEEN " + filter.getTypeStart() + " AND "
						+ filter.getTypeEnd());
			} else {
				builder.append(" and " + filter.getTypeName() + " NOT BETWEEN " + filter.getTypeStart() + " AND "
						+ filter.getTypeEnd());
			}
		} else {
		}
		if (filter.getViewName() != null && filter.getViewName().contains("WSUS")) {
			if (filter.getViewName().contains("Servers")) {
				builder.append(" and profileid in ('3','19','22','10','11') ");
			} else if (filter.getViewName().contains("Endpoints")) {
				builder.append(" and profileid in ('16','2','14','9') ");
			}
		}
		query = builder.toString();
		builder.delete(0, builder.length());
		return query;
	}

	///////////////////////////////////////////////////
	///////////// count of number in circle/////////////

	public String getCompAndCovInfo(int viewID, String type, String viewName) {
		String name = "";

		fillTheData(type + "-" + viewID, viewName);

		//// TotalSum////

		totalSum = setDefaultsIfNull(totalSum);
		if (totalSum.getViewName() == null) {

			query = "	select 'Existing' as Type, 0 as TotalSum ,0 as GreenCircle ,0 as OrangeCircle , 0 as RedCircle, 0 as IT, 0 as Risk \r\n"
					+ "	union\r\n"
					+ "	select 'New' as Type, 0 as TotalSum ,0 as GreenCircle ,0 as OrangeCircle , 0 as RedCircle, 0 as IT, 0 as Risk ";

		} else {
			builder.append(CommonsViewData.Existing + " " + CommonsViewData.SUMCASE);
			if (totalSum.getIsScanned() == -1) {
			} else {
				builder.append(" IsScanned = " + totalSum.getIsScanned() + " and ");
			}
			if (totalSum.getIsNew() == -1) {
			} else {
				builder.append(" IsNew = " + totalSum.getIsNew() + " and ");
			}
			if (totalSum.getIsComply() == -1) {
			} else {
				builder.append(" IsComply = " + totalSum.getIsComply() + " and ");
			}
			if (totalSum.getIsCovered() == -1) {
			} else {
				builder.append(" IsCoveredByControl = " + totalSum.getIsCovered() + " and ");
			}
			if (totalSum.getColumnDateName().equalsIgnoreCase("Score")) {
				builder.append(" (" + totalSum.getColumnDateName() + " >= " + totalSum.getStart() + " and "
						+ totalSum.getColumnDateName() + " < " + totalSum.getEnd() + " ) ");
			} else {
				if (totalSum.getColumnDateName() == null) {
					builder.append(" EvaluationDate  <= DATEADD(day,0, GETDATE()) ");
				} else if (totalSum.getEnd() >= 1000) {
					builder.append(totalSum.getColumnDateName() + " <= DATEADD(" + totalSum.getTimePeriod() + "," + "-"
							+ totalSum.getStart() + ", GETDATE()) ");
				} else {
					builder.append(totalSum.getColumnDateName() + " <= DATEADD(" + totalSum.getTimePeriod() + "," + "-"
							+ totalSum.getStart() + ", GETDATE()) " + " and " + totalSum.getColumnDateName()
							+ " > DATEADD(" + totalSum.getTimePeriod() + "," + "-" + totalSum.getEnd()
							+ ", GETDATE()) ");
				}
			}
			if (totalSum.getTypeName() != null) {
				if (totalSum.getTypeOperation().contains("in")) {
					builder.append(" and " + totalSum.getTypeName() + " BETWEEN " + totalSum.getTypeStart() + " AND "
							+ totalSum.getTypeEnd());
				} else {
					builder.append(" and " + totalSum.getTypeName() + " NOT BETWEEN " + totalSum.getTypeStart()
							+ " AND " + totalSum.getTypeEnd());
				}
			} else {
			}
			if (totalSum.getViewName() != null && totalSum.getViewName().contains("WSUS")) {
				if (totalSum.getViewName().contains("Servers")) {
					builder.append(" and profileid in ('3','19','22','10','11') ");
				} else if (totalSum.getViewName().contains("Endpoints")) {
					builder.append(" and profileid in ('16','2','14','9') ");
				}
			}
			builder.append(CommonsViewData.CONDITION + " " + CommonsViewData.TOTALSUM);

			//// GreenCircle////

			greenExist = setDefaultsIfNull(greenExist);
			builder.append(CommonsViewData.SUMCASE);
			if (greenExist.getIsScanned() == -1) {
			} else {
				builder.append(" IsScanned = " + greenExist.getIsScanned() + " and ");
			}
			if (greenExist.getIsNew() == -1) {
			} else {
				builder.append(" IsNew = " + greenExist.getIsNew() + " and ");
			}
			if (greenExist.getIsComply() == -1) {
			} else {
				builder.append(" IsComply = " + greenExist.getIsComply() + " and ");
			}
			if (greenExist.getIsCovered() == -1) {
			} else {
				builder.append(" IsCoveredByControl = " + greenExist.getIsCovered() + " and ");
			}
			if (greenExist.getColumnDateName().equalsIgnoreCase("Score")) {
				builder.append(" (" + greenExist.getColumnDateName() + " >= " + greenExist.getStart() + " and "
						+ greenExist.getColumnDateName() + " < " + greenExist.getEnd() + " ) ");
			} else {
				if (greenExist.getColumnDateName() == null) {
					builder.append(" EvaluationDate  <= DATEADD(day,0, GETDATE()) ");
				} else if (greenExist.getEnd() >= 1000) {
					builder.append(greenExist.getColumnDateName() + " <= DATEADD(" + greenExist.getTimePeriod() + ","
							+ "-" + greenExist.getStart() + ", GETDATE()) ");
				} else {
					builder.append(greenExist.getColumnDateName() + " <= DATEADD(" + greenExist.getTimePeriod() + ","
							+ "-" + greenExist.getStart() + ", GETDATE()) " + " and " + greenExist.getColumnDateName()
							+ " > DATEADD(" + greenExist.getTimePeriod() + "," + "-" + greenExist.getEnd()
							+ ", GETDATE()) ");
				}
			}
			if (greenExist.getTypeName() != null) {
				if (greenExist.getTypeOperation().contains("in")) {
					builder.append(" and " + greenExist.getTypeName() + " BETWEEN " + greenExist.getTypeStart()
							+ " AND " + greenExist.getTypeEnd());
				} else {
					builder.append(" and " + greenExist.getTypeName() + " NOT BETWEEN " + greenExist.getTypeStart()
							+ " AND " + greenExist.getTypeEnd());
				}
			} else {
			}
			if (totalSum.getViewName() != null && greenExist.getViewName().contains("WSUS")) {
				if (greenExist.getViewName().contains("Servers")) {
					builder.append(" and profileid in ('3','19','22','10','11') ");
				} else if (greenExist.getViewName().contains("Endpoints")) {
					builder.append(" and profileid in ('16','2','14','9') ");
				}
			}
			builder.append(CommonsViewData.CONDITION + " " + CommonsViewData.GREENCIRCLE);

			///// OrangeCircle////

			orangeExist = setDefaultsIfNull(orangeExist);
			builder.append(CommonsViewData.SUMCASE);
			if (orangeExist.getIsScanned() == -1) {
			} else {
				builder.append(" IsScanned = " + orangeExist.getIsScanned() + " and ");
			}
			if (orangeExist.getIsNew() == -1) {
			} else {
				builder.append(" IsNew = " + orangeExist.getIsNew() + " and ");
			}
			if (orangeExist.getIsComply() == -1) {
			} else {
				builder.append(" IsComply = " + orangeExist.getIsComply() + " and ");
			}
			if (orangeExist.getIsCovered() == -1) {
			} else {
				builder.append(" IsCoveredByControl = " + orangeExist.getIsCovered() + " and ");
			}
			if (orangeExist.getColumnDateName().equalsIgnoreCase("Score")) {
				builder.append(" (" + orangeExist.getColumnDateName() + " >= " + orangeExist.getStart() + " and "
						+ orangeExist.getColumnDateName() + " < " + orangeExist.getEnd() + " ) ");
			} else {
				if (orangeExist.getColumnDateName() == null) {
					builder.append(" EvaluationDate  <= DATEADD(day,0, GETDATE()) ");
				} else if (orangeExist.getEnd() >= 1000) {
					builder.append(orangeExist.getColumnDateName() + " <= DATEADD(" + orangeExist.getTimePeriod() + ","
							+ "-" + orangeExist.getStart() + ", GETDATE()) ");
				} else {
					builder.append(orangeExist.getColumnDateName() + " <= DATEADD(" + orangeExist.getTimePeriod() + ","
							+ "-" + orangeExist.getStart() + ", GETDATE()) " + " and " + orangeExist.getColumnDateName()
							+ " > DATEADD(" + orangeExist.getTimePeriod() + "," + "-" + orangeExist.getEnd()
							+ ", GETDATE()) ");
				}
			}
			if (orangeExist.getTypeName() != null) {
				if (orangeExist.getTypeOperation().contains("in")) {
					builder.append(" and " + orangeExist.getTypeName() + " BETWEEN " + orangeExist.getTypeStart()
							+ " AND " + orangeExist.getTypeEnd());
				} else {
					builder.append(" and " + orangeExist.getTypeName() + " NOT BETWEEN " + orangeExist.getTypeStart()
							+ " AND " + orangeExist.getTypeEnd());
				}
			} else {
			}
			if (orangeExist.getViewName() != null && orangeExist.getViewName().contains("WSUS")) {
				if (orangeExist.getViewName().contains("Servers")) {
					builder.append(" and profileid in ('3','19','22','10','11') ");
				} else if (orangeExist.getViewName().contains("Endpoints")) {
					builder.append(" and profileid in ('16','2','14','9') ");
				}
			}
			builder.append(CommonsViewData.CONDITION + " " + CommonsViewData.ORANGECIRCLE);

			//// RedCircle////

			redExist = setDefaultsIfNull(redExist);
			builder.append(CommonsViewData.SUMCASE);
			if (redExist.getIsScanned() == -1) {
			} else {
				builder.append(" IsScanned = " + redExist.getIsScanned() + " and ");
			}
			if (redExist.getIsNew() == -1) {
			} else {
				builder.append(" IsNew = " + redExist.getIsNew() + " and ");
			}
			if (redExist.getIsComply() == -1) {
			} else {
				builder.append(" IsComply = " + redExist.getIsComply() + " and ");
			}
			if (redExist.getIsCovered() == -1) {
			} else {
				builder.append(" IsCoveredByControl = " + redExist.getIsCovered() + " and ");
			}
			if (redExist.getColumnDateName().equalsIgnoreCase("Score")) {
				builder.append(" (" + redExist.getColumnDateName() + " >= " + redExist.getStart() + " and "
						+ redExist.getColumnDateName() + " < " + redExist.getEnd() + " ) ");
			} else {
				if (redExist.getColumnDateName() == null) {
					builder.append(" EvaluationDate  <= DATEADD(day,0, GETDATE()) ");
				} else if (redExist.getEnd() >= 1000) {
					builder.append(redExist.getColumnDateName() + " <= DATEADD(" + redExist.getTimePeriod() + "," + "-"
							+ redExist.getStart() + ", GETDATE()) ");
				} else {
					builder.append(redExist.getColumnDateName() + " <= DATEADD(" + redExist.getTimePeriod() + "," + "-"
							+ redExist.getStart() + ", GETDATE()) " + " and " + redExist.getColumnDateName()
							+ " > DATEADD(" + redExist.getTimePeriod() + "," + "-" + redExist.getEnd()
							+ ", GETDATE()) ");
				}
			}
			if (redExist.getTypeName() != null) {
				if (redExist.getTypeOperation().contains("in")) {
					builder.append(" and " + redExist.getTypeName() + " BETWEEN " + redExist.getTypeStart() + " AND "
							+ redExist.getTypeEnd());
				} else {
					builder.append(" and " + redExist.getTypeName() + " NOT BETWEEN " + redExist.getTypeStart()
							+ " AND " + redExist.getTypeEnd());
				}
			} else {
			}
			if (totalSum.getViewName() != null && redExist.getViewName().contains("WSUS")) {
				if (redExist.getViewName().contains("Servers")) {
					builder.append(" and profileid in ('3','19','22','10','11') ");
				} else if (redExist.getViewName().contains("Endpoints")) {
					builder.append(" and profileid in ('16','2','14','9') ");
				}
			}
			builder.append(CommonsViewData.CONDITION + " " + CommonsViewData.REDCIRCLE);

			///// ITQuery////

			itExist = setDefaultsIfNull(itExist);
			builder.append(CommonsViewData.SUMCASE);
			if (itExist.getIsScanned() == -1) {
			} else {
				builder.append(" IsScanned = " + itExist.getIsScanned() + " and ");
			}
			if (itExist.getIsNew() == -1) {
			} else {
				builder.append(" IsNew = " + itExist.getIsNew() + " and ");
			}
			if (itExist.getIsComply() == -1) {
			} else {
				builder.append(" IsComply = " + itExist.getIsComply() + " and ");
			}
			if (itExist.getIsCovered() == -1) {
			} else {
				builder.append(" IsCoveredByControl = " + itExist.getIsCovered() + " and ");
			}
			if (itExist.getColumnDateName().equalsIgnoreCase("Score")) {
				builder.append(" (" + itExist.getColumnDateName() + " >= " + itExist.getStart() + " and "
						+ itExist.getColumnDateName() + " < " + itExist.getEnd() + " ) ");
				if (itExist.getTypeName() != null) {
					if (itExist.getTypeOperation().contains("in")) {
						builder.append(" and " + itExist.getTypeName() + " BETWEEN " + itExist.getTypeStart() + " AND "
								+ itExist.getTypeEnd());
					} else {
						builder.append(" and " + itExist.getTypeName() + " NOT BETWEEN " + itExist.getTypeStart()
								+ " AND " + itExist.getTypeEnd());
					}
				} else {
				}
				if (itExist.getViewName() != null && itExist.getViewName().contains("WSUS")) {
					if (itExist.getViewName().contains("Servers")) {
						builder.append(" and profileid in ('3','19','22','10','11') ");
					} else if (itExist.getViewName().contains("Endpoints")) {
						builder.append(" and profileid in ('16','2','14','9') ");
					}
				}
				builder.append(CommonsViewData.SCORECONDITION + " ");
			} else {
				if (itExist.getColumnDateName() == null) {
					builder.append(" EvaluationDate  <= DATEADD(day,0, GETDATE()) ");
				} else if (itExist.getEnd() >= 1000) {
					builder.append(itExist.getColumnDateName() + " <= DATEADD(" + itExist.getTimePeriod() + "," + "-"
							+ itExist.getStart() + ", GETDATE()) ");
				} else {
					builder.append(itExist.getColumnDateName() + " <= DATEADD(" + itExist.getTimePeriod() + "," + "-"
							+ itExist.getStart() + ", GETDATE()) " + " and " + itExist.getColumnDateName()
							+ " > DATEADD(" + itExist.getTimePeriod() + "," + "-" + itExist.getEnd() + ", GETDATE()) ");
				}

				if (itExist.getTypeName() != null) {
					if (itExist.getTypeOperation().contains("in")) {
						builder.append(" and " + itExist.getTypeName() + " BETWEEN " + itExist.getTypeStart() + " AND "
								+ itExist.getTypeEnd());
					} else {
						builder.append(" and " + itExist.getTypeName() + " NOT BETWEEN " + itExist.getTypeStart()
								+ " AND " + itExist.getTypeEnd());
					}
				} else {
				}
				if (itExist.getViewName() != null && itExist.getViewName().contains("WSUS")) {
					if (itExist.getViewName().contains("Servers")) {
						builder.append(" and profileid in ('3','19','22','10','11') ");
					} else if (itExist.getViewName().contains("Endpoints")) {
						builder.append(" and profileid in ('16','2','14','9') ");
					}
				}

				builder.append(CommonsViewData.CONDITION + " ");
			}
			builder.append(" " + CommonsViewData.IT);

			//// RiskQuery /////

			riskExist = setDefaultsIfNull(riskExist);
			builder.append(CommonsViewData.SUMCASE);
			if (riskExist.getIsScanned() == -1) {
			} else {
				builder.append(" IsScanned = " + riskExist.getIsScanned() + " and ");
			}
			if (riskExist.getIsNew() == -1) {
			} else {
				builder.append(" IsNew = " + riskExist.getIsNew() + " and ");
			}
			if (riskExist.getIsComply() == -1) {
			} else {
				builder.append(" IsComply = " + riskExist.getIsComply() + " and ");
			}
			if (riskExist.getIsCovered() == -1) {
			} else {
				builder.append(" IsCoveredByControl = " + riskExist.getIsCovered() + " and ");
			}
			if (riskExist.getColumnDateName().equalsIgnoreCase("Score")) {
				builder.append(" (" + riskExist.getColumnDateName() + " >= " + riskExist.getStart() + " and "
						+ riskExist.getColumnDateName() + " < " + riskExist.getEnd() + " ) ");
				if (riskExist.getTypeName() != null) {
					if (riskExist.getTypeOperation().contains("in")) {
						builder.append(" and " + riskExist.getTypeName() + " BETWEEN " + riskExist.getTypeStart()
								+ " AND " + riskExist.getTypeEnd());
					} else {
						builder.append(" and " + riskExist.getTypeName() + " NOT BETWEEN " + riskExist.getTypeStart()
								+ " AND " + riskExist.getTypeEnd());
					}
				} else {
				}
				if (riskExist.getViewName() != null && riskExist.getViewName().contains("WSUS")) {
					if (riskExist.getViewName().contains("Servers")) {
						builder.append(" and profileid in ('3','19','22','10','11') ");
					} else if (riskExist.getViewName().contains("Endpoints")) {
						builder.append(" and profileid in ('16','2','14','9') ");
					}
				}
				builder.append(CommonsViewData.SCORECONDITION + " ");
			} else {
				if (riskExist.getColumnDateName() == null) {
					builder.append(" EvaluationDate  <= DATEADD(day,0, GETDATE()) ");
				} else if (riskExist.getEnd() >= 1000) {
					builder.append(riskExist.getColumnDateName() + " <= DATEADD(" + riskExist.getTimePeriod() + ","
							+ "-" + riskExist.getStart() + ", GETDATE()) ");
				} else {
					builder.append(riskExist.getColumnDateName() + " <= DATEADD(" + riskExist.getTimePeriod() + ","
							+ "-" + riskExist.getStart() + ", GETDATE()) " + " and " + riskExist.getColumnDateName()
							+ " > DATEADD(" + riskExist.getTimePeriod() + "," + "-" + riskExist.getEnd()
							+ ", GETDATE()) ");
				}
				if (riskExist.getTypeName() != null) {
					if (riskExist.getTypeOperation().contains("in")) {
						builder.append(" and " + riskExist.getTypeName() + " BETWEEN " + riskExist.getTypeStart()
								+ " AND " + riskExist.getTypeEnd());
					} else {
						builder.append(" and " + riskExist.getTypeName() + " NOT BETWEEN " + riskExist.getTypeStart()
								+ " AND " + riskExist.getTypeEnd());
					}
				} else {
				}
				if (riskExist.getViewName() != null && riskExist.getViewName().contains("WSUS")) {
					if (riskExist.getViewName().contains("Servers")) {
						builder.append(" and profileid in ('3','19','22','10','11') ");
					} else if (riskExist.getViewName().contains("Endpoints")) {
						builder.append(" and profileid in ('16','2','14','9') ");
					}
				}
				builder.append(CommonsViewData.CONDITION + " ");
			}

			builder.append(" " + CommonsViewData.RISK);
			if (type.equalsIgnoreCase("Comp")) {
				builder.append(CommonsViewData.FROMCOMPLIANCEHISTORY + " " + CommonsViewData.UNION);
			} else {
				builder.append(CommonsViewData.FROMCOVERAGEHISTORY + " " + CommonsViewData.UNION);
			}

			///////////////////

			///// NewQuery/////

			totalSumNew = setDefaultsIfNull(totalSumNew);
			builder.append(CommonsViewData.NEW + " " + CommonsViewData.SUMCASE);
			if (totalSumNew.getIsScanned() == -1) {
			} else {
				builder.append(" IsScanned = " + totalSumNew.getIsScanned() + " and ");
			}
			if (totalSumNew.getIsNew() == -1) {
			} else {
				builder.append(" IsNew = " + totalSumNew.getIsNew() + " and ");
			}
			if (totalSumNew.getIsComply() == -1) {
			} else {
				builder.append(" IsComply = " + totalSumNew.getIsComply() + " and ");
			}
			if (totalSumNew.getIsCovered() == -1) {
			} else {
				builder.append(" IsCoveredByControl = " + totalSumNew.getIsCovered() + " and ");
			}
			if (totalSumNew.getColumnDateName().equalsIgnoreCase("Score")) {
				builder.append(" (" + totalSumNew.getColumnDateName() + " >= " + totalSumNew.getStart() + " and "
						+ totalSumNew.getColumnDateName() + " < " + totalSumNew.getEnd() + " ) ");
			} else {
				if (totalSumNew.getColumnDateName() == null) {
					builder.append(" EvaluationDate  <= DATEADD(day,0, GETDATE()) ");
				} else if (totalSumNew.getEnd() >= 1000) {
					builder.append(totalSumNew.getColumnDateName() + " <= DATEADD(" + totalSumNew.getTimePeriod() + ","
							+ "-" + totalSumNew.getStart() + ", GETDATE()) ");
				} else {
					builder.append(totalSumNew.getColumnDateName() + " <= DATEADD(" + totalSumNew.getTimePeriod() + ","
							+ "-" + totalSumNew.getStart() + ", GETDATE()) " + " and " + totalSumNew.getColumnDateName()
							+ " > DATEADD(" + totalSumNew.getTimePeriod() + "," + "-" + totalSumNew.getEnd()
							+ ", GETDATE()) ");
				}
			}
			if (totalSumNew.getTypeName() != null) {
				if (totalSumNew.getTypeOperation().contains("in")) {
					builder.append(" and " + totalSumNew.getTypeName() + " BETWEEN " + totalSumNew.getTypeStart()
							+ " AND " + totalSumNew.getTypeEnd());
				} else {
					builder.append(" and " + totalSumNew.getTypeName() + " NOT BETWEEN " + totalSumNew.getTypeStart()
							+ " AND " + totalSumNew.getTypeEnd());
				}
			} else {
			}
			if (totalSum.getViewName() != null && totalSumNew.getViewName().contains("WSUS")) {
				if (totalSumNew.getViewName().contains("Servers")) {
					builder.append(" and profileid in ('3','19','22','10','11') ");
				} else if (totalSumNew.getViewName().contains("Endpoints")) {
					builder.append(" and profileid in ('16','2','14','9') ");
				}
			}
			builder.append(CommonsViewData.CONDITION + " " + CommonsViewData.TOTALSUM);

			///// GreenCircle/////

			greenNew = setDefaultsIfNull(greenNew);
			builder.append(CommonsViewData.SUMCASE);
			if (greenNew.getIsScanned() == -1) {
			} else {
				builder.append(" IsScanned = " + greenNew.getIsScanned() + " and ");
			}
			if (greenNew.getIsNew() == -1) {
			} else {
				builder.append(" IsNew = " + greenNew.getIsNew() + " and ");
			}
			if (greenNew.getIsComply() == -1) {
			} else {
				builder.append(" IsComply = " + greenNew.getIsComply() + " and ");
			}
			if (greenNew.getIsCovered() == -1) {
			} else {
				builder.append(" IsCoveredByControl = " + greenNew.getIsCovered() + " and ");
			}
			if (greenNew.getColumnDateName().equalsIgnoreCase("Score")) {
				builder.append(" (" + greenNew.getColumnDateName() + " >= " + greenNew.getStart() + " and "
						+ greenNew.getColumnDateName() + " < " + greenNew.getEnd() + " ) ");
			} else {
				if (greenNew.getColumnDateName() == null) {
					builder.append(" EvaluationDate  <= DATEADD(day,0, GETDATE()) ");
				} else if (greenNew.getEnd() >= 1000) {
					builder.append(greenNew.getColumnDateName() + " <= DATEADD(" + greenNew.getTimePeriod() + "," + "-"
							+ greenNew.getStart() + ", GETDATE()) ");
				} else {
					builder.append(greenNew.getColumnDateName() + " <= DATEADD(" + greenNew.getTimePeriod() + "," + "-"
							+ greenNew.getStart() + ", GETDATE()) " + " and " + greenNew.getColumnDateName()
							+ " > DATEADD(" + greenNew.getTimePeriod() + "," + "-" + greenNew.getEnd()
							+ ", GETDATE()) ");
				}
			}
			if (greenNew.getTypeName() != null) {
				if (greenNew.getTypeOperation().contains("in")) {
					builder.append(" and " + greenNew.getTypeName() + " BETWEEN " + greenNew.getTypeStart() + " AND "
							+ greenNew.getTypeEnd());
				} else {
					builder.append(" and " + greenNew.getTypeName() + " NOT BETWEEN " + greenNew.getTypeStart()
							+ " AND " + greenNew.getTypeEnd());
				}
			} else {
			}
			if (totalSum.getViewName() != null && greenNew.getViewName().contains("WSUS")) {
				if (greenNew.getViewName().contains("Servers")) {
					builder.append(" and profileid in ('3','19','22','10','11') ");
				} else if (greenNew.getViewName().contains("Endpoints")) {
					builder.append(" and profileid in ('16','2','14','9') ");
				}
			}
			builder.append(CommonsViewData.CONDITION + " " + CommonsViewData.GREENCIRCLE);

			///// OrangeCircle////

			orangeNew = setDefaultsIfNull(orangeNew);
			builder.append(CommonsViewData.SUMCASE);
			if (orangeNew.getIsScanned() == -1) {
			} else {
				builder.append(" IsScanned = " + orangeNew.getIsScanned() + " and ");
			}
			if (orangeNew.getIsNew() == -1) {
			} else {
				builder.append(" IsNew = " + orangeNew.getIsNew() + " and ");
			}
			if (orangeNew.getIsComply() == -1) {
			} else {
				builder.append(" IsComply = " + orangeNew.getIsComply() + " and ");
			}
			if (orangeNew.getIsCovered() == -1) {
			} else {
				builder.append(" IsCoveredByControl = " + orangeNew.getIsCovered() + " and ");
			}
			if (orangeNew.getColumnDateName().equalsIgnoreCase("Score")) {
				builder.append(" (" + orangeNew.getColumnDateName() + " >= " + orangeNew.getStart() + " and "
						+ orangeNew.getColumnDateName() + " < " + orangeNew.getEnd() + " ) ");
			} else {
				if (orangeNew.getColumnDateName() == null) {
					builder.append(" EvaluationDate  <= DATEADD(day,0, GETDATE()) ");
				} else if (orangeNew.getEnd() >= 1000) {
					builder.append(orangeNew.getColumnDateName() + " <= DATEADD(" + orangeNew.getTimePeriod() + ","
							+ "-" + orangeNew.getStart() + ", GETDATE()) ");
				} else {
					builder.append(orangeNew.getColumnDateName() + " <= DATEADD(" + orangeNew.getTimePeriod() + ","
							+ "-" + orangeNew.getStart() + ", GETDATE()) " + " and " + orangeNew.getColumnDateName()
							+ " > DATEADD(" + orangeNew.getTimePeriod() + "," + "-" + orangeNew.getEnd()
							+ ", GETDATE()) ");
				}
			}
			if (orangeNew.getTypeName() != null) {
				if (orangeNew.getTypeOperation().contains("in")) {
					builder.append(" and " + orangeNew.getTypeName() + " BETWEEN " + orangeNew.getTypeStart() + " AND "
							+ orangeNew.getTypeEnd());
				} else {
					builder.append(" and " + orangeNew.getTypeName() + " NOT BETWEEN " + orangeNew.getTypeStart()
							+ " AND " + orangeNew.getTypeEnd());
				}
			} else {
			}
			if (totalSum.getViewName() != null && orangeNew.getViewName().contains("WSUS")) {
				if (orangeNew.getViewName().contains("Servers")) {
					builder.append(" and profileid in ('3','19','22','10','11') ");
				} else if (orangeNew.getViewName().contains("Endpoints")) {
					builder.append(" and profileid in ('16','2','14','9') ");
				}
			}
			builder.append(CommonsViewData.CONDITION + " " + CommonsViewData.ORANGECIRCLE);

			///// RedCircle/////

			redNew = setDefaultsIfNull(redNew);
			builder.append(CommonsViewData.SUMCASE);
			if (redNew.getIsScanned() == -1) {
			} else {
				builder.append(" IsScanned = " + redNew.getIsScanned() + " and ");
			}
			if (redNew.getIsNew() == -1) {
			} else {
				builder.append(" IsNew = " + redNew.getIsNew() + " and ");
			}
			if (redNew.getIsComply() == -1) {
			} else {
				builder.append(" IsComply = " + redNew.getIsComply() + " and ");
			}
			if (redNew.getIsCovered() == -1) {
			} else {
				builder.append(" IsCoveredByControl = " + redNew.getIsCovered() + " and ");
			}
			if (redNew.getColumnDateName().equalsIgnoreCase("Score")) {
				builder.append(" (" + redNew.getColumnDateName() + " >= " + redNew.getStart() + " and "
						+ redNew.getColumnDateName() + " < " + redNew.getEnd() + " ) ");
			} else {
				if (redNew.getColumnDateName() == null) {
					builder.append(" EvaluationDate  <= DATEADD(day,0, GETDATE()) ");
				} else if (redNew.getEnd() >= 1000) {
					builder.append(redNew.getColumnDateName() + " <= DATEADD(" + redNew.getTimePeriod() + "," + "-"
							+ redNew.getStart() + ", GETDATE()) ");
				} else {
					builder.append(redNew.getColumnDateName() + " <= DATEADD(" + redNew.getTimePeriod() + "," + "-"
							+ redNew.getStart() + ", GETDATE()) " + " and " + redNew.getColumnDateName() + " > DATEADD("
							+ redNew.getTimePeriod() + "," + "-" + redNew.getEnd() + ", GETDATE()) ");
				}
			}
			if (redNew.getTypeName() != null) {
				if (redNew.getTypeOperation().contains("in")) {
					builder.append(" and " + redNew.getTypeName() + " BETWEEN " + redNew.getTypeStart() + " AND "
							+ redNew.getTypeEnd());
				} else {
					builder.append(" and " + redNew.getTypeName() + " NOT BETWEEN " + redNew.getTypeStart() + " AND "
							+ redNew.getTypeEnd());
				}
			} else {
			}
			if (totalSum.getViewName() != null && redNew.getViewName().contains("WSUS")) {
				if (redNew.getViewName().contains("Servers")) {
					builder.append(" and profileid in ('3','19','22','10','11') ");
				} else if (redNew.getViewName().contains("Endpoints")) {
					builder.append(" and profileid in ('16','2','14','9') ");
				}
			}
			builder.append(CommonsViewData.CONDITION + " " + CommonsViewData.REDCIRCLE);

			///// ITQuery/////

			itNew = setDefaultsIfNull(itNew);
			builder.append("(select " + " " + CommonsViewData.SUMCASE);
			if (itNew.getIsScanned() == -1) {
			} else {
				builder.append(" IsScanned = " + itNew.getIsScanned() + " and ");
			}
			if (itNew.getIsNew() == -1) {
			} else {
				builder.append(" IsNew = " + itNew.getIsNew() + " and ");
			}
			if (itNew.getIsComply() == -1) {
			} else {
				builder.append(" IsComply = " + itNew.getIsComply() + " and ");
			}
			if (itNew.getIsCovered() == -1) {
			} else {
				builder.append(" IsCoveredByControl = " + itNew.getIsCovered() + " and ");
			}
			if (itNew.getColumnDateName().equalsIgnoreCase("Score")) {
				builder.append(" (" + itNew.getColumnDateName() + " >= " + itNew.getStart() + " and "
						+ itNew.getColumnDateName() + " < " + itNew.getEnd() + " ) ");
			} else {
				if (itNew.getColumnDateName() == null) {
					builder.append(" EvaluationDate  <= DATEADD(day,0, GETDATE()) ");
				} else if (itNew.getEnd() >= 1000) {
					builder.append(itNew.getColumnDateName() + " <= DATEADD(" + itNew.getTimePeriod() + "," + "-"
							+ itNew.getStart() + ", GETDATE()) ");
				} else {
					builder.append(itNew.getColumnDateName() + " <= DATEADD(" + itNew.getTimePeriod() + "," + "-"
							+ itNew.getStart() + ", GETDATE()) " + " and " + itNew.getColumnDateName() + " > DATEADD("
							+ itNew.getTimePeriod() + "," + "-" + itNew.getEnd() + ", GETDATE()) ");
				}
			}
			if (itNew.getTypeName() != null) {
				if (itNew.getTypeOperation().contains("in")) {
					builder.append(" and " + itNew.getTypeName() + " BETWEEN " + itNew.getTypeStart() + " AND "
							+ itNew.getTypeEnd());
				} else {
					builder.append(" and " + itNew.getTypeName() + " NOT BETWEEN " + itNew.getTypeStart() + " AND "
							+ itNew.getTypeEnd());
				}
			} else {
			}
			if (totalSum.getViewName() != null && itNew.getViewName().contains("WSUS")) {
				if (itNew.getViewName().contains("Servers")) {
					builder.append(" and profileid in ('3','19','22','10','11') ");
				} else if (itNew.getViewName().contains("Endpoints")) {
					builder.append(" and profileid in ('16','2','14','9') ");
				}
			}
			if (itNew.getColumnDateName().equalsIgnoreCase("Score")) {
				builder.append(CommonsViewData.CONDITION + " " + CommonsViewData.FROMCOMPLIANCEHISTORY + ")" + " "
						+ CommonsViewData.IT);
			} else {
				builder.append(CommonsViewData.CONDITION + " " + CommonsViewData.FROMCOVERAGEHISTORY + ")" + " "
						+ CommonsViewData.IT);
			}

			///// RiskQuery /////

			riskNew = setDefaultsIfNull(riskNew);
			builder.append("(select " + " " + CommonsViewData.SUMCASE);
			if (riskNew.getIsScanned() == -1) {
			} else {
				builder.append(" IsScanned = " + riskNew.getIsScanned() + " and ");
			}
			if (riskNew.getIsNew() == -1) {
			} else {
				builder.append(" IsNew = " + riskNew.getIsNew() + " and ");
			}
			if (riskNew.getIsComply() == -1) {
			} else {
				builder.append(" IsComply = " + riskNew.getIsComply() + " and ");
			}
			if (riskNew.getIsCovered() == -1) {
			} else {
				builder.append(" IsCoveredByControl = " + riskNew.getIsCovered() + " and ");
			}
			if (riskNew.getColumnDateName().equalsIgnoreCase("Score")) {
				builder.append(" (" + riskNew.getColumnDateName() + " >= " + riskNew.getStart() + " and "
						+ riskNew.getColumnDateName() + " < " + riskNew.getEnd() + " ) ");
			} else {
				if (riskNew.getColumnDateName() == null) {
					builder.append(" EvaluationDate  <= DATEADD(day,0, GETDATE()) ");
				} else if (riskNew.getEnd() >= 1000) {
					builder.append(riskNew.getColumnDateName() + " <= DATEADD(" + riskNew.getTimePeriod() + "," + "-"
							+ riskNew.getStart() + ", GETDATE()) ");
				} else {
					builder.append(riskNew.getColumnDateName() + " <= DATEADD(" + riskNew.getTimePeriod() + "," + "-"
							+ riskNew.getStart() + ", GETDATE()) " + " and " + riskNew.getColumnDateName()
							+ " > DATEADD(" + riskNew.getTimePeriod() + "," + "-" + riskNew.getEnd() + ", GETDATE()) ");
				}
			}
			if (riskNew.getTypeName() != null) {
				if (riskNew.getTypeOperation().contains("in")) {
					builder.append(" and " + riskNew.getTypeName() + " BETWEEN " + riskNew.getTypeStart() + " AND "
							+ riskNew.getTypeEnd());
				} else {
					builder.append(" and " + riskNew.getTypeName() + " NOT BETWEEN " + riskNew.getTypeStart() + " AND "
							+ riskNew.getTypeEnd());
				}
			} else {
			}
			if (totalSum.getViewName() != null && riskNew.getViewName().contains("WSUS")) {
				if (riskNew.getViewName().contains("Servers")) {
					builder.append(" and profileid in ('3','19','22','10','11') ");
				} else if (riskNew.getViewName().contains("Endpoints")) {
					builder.append(" and profileid in ('16','2','14','9') ");
				}
			}
			if (riskNew.getColumnDateName().equalsIgnoreCase("Score")) {
				builder.append(CommonsViewData.CONDITION + " " + CommonsViewData.FROMCOMPLIANCEHISTORY + ")" + " "
						+ CommonsViewData.RISK);
			} else {
				builder.append(CommonsViewData.CONDITION + " " + CommonsViewData.FROMCOVERAGEHISTORY + ")" + " "
						+ CommonsViewData.RISK);
			}
			if (type.equalsIgnoreCase("Comp")) {
				builder.append(CommonsViewData.FROMCOMPLIANCEHISTORY);
			} else {
				builder.append(CommonsViewData.FROMCOVERAGEHISTORY);
			}

			query = builder.toString();
			builder.delete(0, builder.length());
		}
		return query;
	}

	/////////////////////////////////////////////////////////////
	///////////// count Details number per Week///////////////////
	public String getDataPerWeek(int viewID, String type, String viewName,String dateNew) {
		int start = 0;
		int end = 0;
		String date = "day";
		fillTheData(type + "-" + viewID, viewName);
		List<String> list = Arrays.asList(">", "W4", "W3", "W2", "W1");
		List<Integer> weeks = Arrays.asList(28, 21, 14, 7, 0);
		List<AllRadarData> types = new ArrayList<>();

		greenExist = setDefaultsIfNull(greenExist);
		types.add(greenExist);
		greenNew = setDefaultsIfNull(greenNew);
		types.add(greenNew);
		orangeExist = setDefaultsIfNull(orangeExist);
		types.add(orangeExist);
		orangeNew = setDefaultsIfNull(orangeNew);
		types.add(orangeNew);
		redExist = setDefaultsIfNull(redExist);
		types.add(redExist);
		redNew = setDefaultsIfNull(redNew);
		types.add(redNew);

		if (types.get(0).getColorType() == null) {
			query = "\r\n" + "select '>' as Type,   \r\n" + "0    as GreenExist ,\r\n" + "0    as GreenNew ,\r\n"
					+ "0    as OrangeExist ,\r\n" + "0    as OrangeNew ,\r\n" + "0    as RedExist ,\r\n"
					+ "0    as RedNew \r\n" + "union \r\n" + "select 'W4' as Type,\r\n" + "0  as GreenExist ,\r\n"
					+ "0  as GreenNew ,\r\n" + "0  as OrangeExist ,\r\n" + "0  as OrangeExist ,\r\n"
					+ "0  as RedExist ,\r\n" + "0  as RedNew \r\n" + "union\r\n" + "select 'W3' as Type, \r\n"
					+ "0  as GreenExist ,\r\n" + "0  as GreenNew ,\r\n" + "0  as OrangeExist ,\r\n"
					+ "0  as OrangeNew ,\r\n" + "0  as RedExist ,\r\n" + "0  as RedNew \r\n" + "union\r\n"
					+ "select 'W2' as Type, \r\n" + "0  as GreenExist,\r\n" + "0  as GreenNew ,  \r\n"
					+ "0  as OrangeExist , \r\n" + "0  as OrangeNew ,  \r\n" + "0  as RedExist ,  \r\n"
					+ "0  as RedNew\r\n" + "union\r\n" + "select 'W1' as Type,\r\n" + "0   as GreenExist ,\r\n"
					+ "0   as GreenNew , \r\n" + "0   as OrangeExist ,  \r\n" + "0   as OrangeNew , \r\n"
					+ "0   as RedExist ,  \r\n" + "0   as RedNew \r\n" + "";
		} else {

			for (int i = 0; i < list.size(); i++) {
				builder.append("select " + "'" + list.get(i) + "'" + " as Type, ");
				for (int j = 0; j < types.size(); j++) {
					if (types.get(j).getTimePeriod().equalsIgnoreCase("month")) {
						start = types.get(j).getStart() * 30;
						end = types.get(j).getEnd() * 30;
						date = "day";
					} else if (types.get(j).getTimePeriod().equalsIgnoreCase("week")) {
						start = types.get(j).getStart() * 7;
						end = types.get(j).getEnd() * 7;
						date = "day";
					} else {
						start = types.get(j).getStart();
						end = types.get(j).getEnd();
						date = types.get(j).getTimePeriod();
					}
					builder.append(" " + CommonsViewData.SUMCASE);

					if (types.get(j).getIsScanned() == -1) {
					} else {
						builder.append(" IsScanned = " + types.get(j).getIsScanned() + " and ");
					}
					if (types.get(j).getIsNew() == -1) {
					} else {
						builder.append(" IsNew = " + types.get(j).getIsNew() + " and ");
					}
					if (types.get(j).getIsComply() == -1) {
					} else {
						builder.append(" IsComply = " + types.get(j).getIsComply() + " and ");
					}
					if (types.get(j).getIsCovered() == -1) {
					} else {
						builder.append(" IsCoveredByControl = " + types.get(j).getIsCovered() + " and ");
					}
					if (types.get(j).getColumnDateName().equalsIgnoreCase("Score")) {
						builder.append(" (" + types.get(j).getColumnDateName() + " >= " + start + " and "
								+ types.get(j).getColumnDateName() + " < " + end + " ) ");
						if (list.get(i).equalsIgnoreCase(">")) {
							builder.append(" and " + " CreateDate  <= DATEADD(Week,-4,"+"'"+ dateNew+"'"+")");
						} else {
							builder.append(" and " + " CreateDate  < DATEADD(Week," + "-" + list.get(i).replace("W", "")
									+ "+ 1" + ","+"'"+ dateNew+"'"+") and CreateDate >= DATEADD(Week," + "-"
									+ list.get(i).replace("W", "") + ","+"'"+ dateNew+"'"+")");
						}
					} else {
						if (list.get(i).equalsIgnoreCase(">")) {
							builder.append(types.get(j).getColumnDateName() + " <= DATEADD(" + date + "," + "-"
									+ types.get(j).getStart() + ", "+"'"+ dateNew+"'"+") " + " and " + types.get(j).getColumnDateName()
									+ " > DATEADD(" + date + "," + "-" + (end - weeks.get(i)) + ", "+"'"+ dateNew+"'"+") ");
						} else {
							builder.append(types.get(j).getColumnDateName() + " <= DATEADD(" + date + "," + "-"
									+ (end - (weeks.get(i) + 7)) + ", "+"'"+ dateNew+"'"+") " + " and " + types.get(j).getColumnDateName()
									+ " > DATEADD(" + date + "," + "-" + (end - weeks.get(i)) + ", "+"'"+ dateNew+"'"+") ");
						}
					}
					builder.append(CommonsViewData.CONDITION + " ");
					builder.append(" " + " as " + types.get(j).getColorType().replace("-", ""));
					if (j + 1 == types.size()) {
					} else {
						builder.append(" , ");
					}
				}
				if (type.equalsIgnoreCase("Comp")) {
					builder.append(CommonsViewData.FROMCOMPLIANCEHISTORY);
				} else {
					builder.append(CommonsViewData.FROMCOVERAGEHISTORY);
				}
				if (i != 4) {
					builder.append(" union ");
				}
			}

			query = builder.toString();
			builder.delete(0, builder.length());
		}
		return query.replace("--", "-");
	}

	//////////////////////////////////////////////////////////
	///////////// count Details number per Week Cache///////////////////
	public String getDataPerWeekCache(int viewID, String type, String viewName,String dateNew) {
		int start = 0;
		int end = 0;
		String date = "day";
		fillTheData(type + "-" + viewID, viewName);
		List<String> list = Arrays.asList(">", "W4", "W3", "W2", "W1");
		List<Integer> weeks = Arrays.asList(28, 21, 14, 7, 0);
		List<AllRadarData> types = new ArrayList<>();

		greenExist = setDefaultsIfNull(greenExist);
		types.add(greenExist);
		greenNew = setDefaultsIfNull(greenNew);
		types.add(greenNew);
		orangeExist = setDefaultsIfNull(orangeExist);
		types.add(orangeExist);
		orangeNew = setDefaultsIfNull(orangeNew);
		types.add(orangeNew);
		redExist = setDefaultsIfNull(redExist);
		types.add(redExist);
		redNew = setDefaultsIfNull(redNew);
		types.add(redNew);

		if (types.get(0).getColorType() == null) {
			query = "\r\n" + "select '>' as Type,   \r\n" + "0    as GreenExist ,\r\n" + "0    as GreenNew ,\r\n"
					+ "0    as OrangeExist ,\r\n" + "0    as OrangeNew ,\r\n" + "0    as RedExist ,\r\n"
					+ "0    as RedNew \r\n" + "union \r\n" + "select 'W4' as Type,\r\n" + "0  as GreenExist ,\r\n"
					+ "0  as GreenNew ,\r\n" + "0  as OrangeExist ,\r\n" + "0  as OrangeExist ,\r\n"
					+ "0  as RedExist ,\r\n" + "0  as RedNew \r\n" + "union\r\n" + "select 'W3' as Type, \r\n"
					+ "0  as GreenExist ,\r\n" + "0  as GreenNew ,\r\n" + "0  as OrangeExist ,\r\n"
					+ "0  as OrangeNew ,\r\n" + "0  as RedExist ,\r\n" + "0  as RedNew \r\n" + "union\r\n"
					+ "select 'W2' as Type, \r\n" + "0  as GreenExist,\r\n" + "0  as GreenNew ,  \r\n"
					+ "0  as OrangeExist , \r\n" + "0  as OrangeNew ,  \r\n" + "0  as RedExist ,  \r\n"
					+ "0  as RedNew\r\n" + "union\r\n" + "select 'W1' as Type,\r\n" + "0   as GreenExist ,\r\n"
					+ "0   as GreenNew , \r\n" + "0   as OrangeExist ,  \r\n" + "0   as OrangeNew , \r\n"
					+ "0   as RedExist ,  \r\n" + "0   as RedNew \r\n" + "";
		} else {
			builder.append("select * from ( ");
			for (int i = 0; i < list.size(); i++) {
				builder.append("select " + "'" + list.get(i) + "'" + " as Type, ");
				for (int j = 0; j < types.size(); j++) {
					if (types.get(j).getTimePeriod().equalsIgnoreCase("month")) {
						start = types.get(j).getStart() * 30;
						end = types.get(j).getEnd() * 30;
						date = "day";
					} else if (types.get(j).getTimePeriod().equalsIgnoreCase("week")) {
						start = types.get(j).getStart() * 7;
						end = types.get(j).getEnd() * 7;
						date = "day";
					} else {
						start = types.get(j).getStart();
						end = types.get(j).getEnd();
						date = types.get(j).getTimePeriod();
					}
					builder.append(" " + CommonsViewData.SUMCASE);

					if (types.get(j).getIsScanned() == -1) {
					} else {
						builder.append(" IsScanned = " + types.get(j).getIsScanned() + " and ");
					}
					if (types.get(j).getIsNew() == -1) {
					} else {
						builder.append(" IsNew = " + types.get(j).getIsNew() + " and ");
					}
					if (types.get(j).getIsComply() == -1) {
					} else {
						builder.append(" IsComply = " + types.get(j).getIsComply() + " and ");
					}
					if (types.get(j).getIsCovered() == -1) {
					} else {
						builder.append(" IsCoveredByControl = " + types.get(j).getIsCovered() + " and ");
					}
					if (types.get(j).getColumnDateName().equalsIgnoreCase("Score")) {
						builder.append(" (" + types.get(j).getColumnDateName() + " >= " + start + " and "
								+ types.get(j).getColumnDateName() + " < " + end + " ) ");
						if (list.get(i).equalsIgnoreCase(">")) {
							builder.append(" and " + " CreateDate  <= DATEADD(Week,-4,"+"'"+ dateNew+"'"+")");
						} else {
							builder.append(" and " + " CreateDate  < DATEADD(Week," + "-" + list.get(i).replace("W", "")
									+ "+ 1" + ","+"'"+ dateNew+"'"+") and CreateDate >= DATEADD(Week," + "-"
									+ list.get(i).replace("W", "") + ","+"'"+ dateNew+"'"+")");
						}
					} else {
						if (list.get(i).equalsIgnoreCase(">")) {
							builder.append(types.get(j).getColumnDateName() + " <= DATEADD(" + date + "," + "-"
									+ types.get(j).getStart() + ", "+"'"+ dateNew+"'"+") " + " and " + types.get(j).getColumnDateName()
									+ " > DATEADD(" + date + "," + "-" + (end - weeks.get(i)) + ", "+"'"+ dateNew+"'"+") ");
						} else {
							builder.append(types.get(j).getColumnDateName() + " <= DATEADD(" + date + "," + "-"
									+ (end - (weeks.get(i) + 7)) + ", "+"'"+ dateNew+"'"+") " + " and " + types.get(j).getColumnDateName()
									+ " > DATEADD(" + date + "," + "-" + (end - weeks.get(i)) + ", "+"'"+ dateNew+"'"+") ");
						}
					}
					builder.append(CommonsViewData.CONDITION + " ");
					builder.append(" " + " as " + types.get(j).getColorType().replace("-", ""));
					if (j + 1 == types.size()) {
					} else {
						builder.append(" , ");
					}
				}
				if (type.equalsIgnoreCase("Comp")) {
					builder.append(" ,LocationCode as LocationCode, SubviewID as SubviewID ");
					builder.append(CommonsViewData.FROMCOMPLIANCEHISTORY);
					builder.append(" group by LocationCode,SubviewID ");
				} else {
					builder.append(" ,LocationCode as LocationCode, SubviewID as SubviewID ");
					builder.append(CommonsViewData.FROMCOVERAGEHISTORY);
					builder.append(" group by LocationCode,SubviewID ");
				}
				if (i != 4) {
					builder.append(" union ");
				}
			}
			builder.append(" ) query  ");

			query = builder.toString();
			builder.delete(0, builder.length());
		}
		return query.replace("--", "-");
	}

	//////////////////////////////////////////////////////////
	
	
	///////////////// return details per Week//////////////////
	public String getDetailsPerWeek(int viewID, String type, String week, String idName, String viewName, String date) {
		getFilter(type, idName + "-" + viewID, viewName);
		int weekNo = 0;
		switch (week) {
		case ">":
			weekNo = 28;
			break;
		case "W4":
			weekNo = 21;
			break;
		case "W3":
			weekNo = 14;
			break;
		case "W2":
			weekNo = 7;
			break;
		case "W1":
			weekNo = 0;
			break;
		default:
			break;
		}
		filter = setDefaultsIfNull(filter);
		if (filter.getTimePeriod().equalsIgnoreCase("month")) {
			filter.setStart(filter.getStart() * 30);
			filter.setEnd(filter.getEnd() * 30);
			filter.setTimePeriod("day");
		} else if (filter.getTimePeriod().equalsIgnoreCase("week")) {
			filter.setStart(filter.getStart() * 7);
			filter.setEnd(filter.getEnd() * 7);
			filter.setTimePeriod("day");
		}
		builder.append("select  *  ");
		if (idName.equalsIgnoreCase("Comp")) {
			builder.append(CommonsViewData.FROMCOMPLIANCEHISTORY);
		} else {
			builder.append(CommonsViewData.FROMCOVERAGEHISTORY);
		}
		builder.append(" Where ");

		if (filter.getIsScanned() == -1) {
		} else {
			builder.append(" IsScanned = " + filter.getIsScanned() + " and ");
		}
		if (filter.getIsNew() == -1) {
		} else {
			builder.append(" IsNew = " + filter.getIsNew() + " and ");
		}
		if (filter.getIsComply() == -1) {
		} else {
			builder.append(" IsComply = " + filter.getIsComply() + " and ");
		}
		if (filter.getIsCovered() == -1) {
		} else {
			builder.append(" IsCoveredByControl = " + filter.getIsCovered() + " and ");
		}
		if (filter.getColumnDateName().equalsIgnoreCase("Score")) {
			builder.append(" (" + filter.getColumnDateName() + " >= " + filter.getStart() + " and "
					+ filter.getColumnDateName() + " < " + filter.getEnd() + " ) ");
			if (week.equalsIgnoreCase(">")) {
				builder.append(" and " + " CreateDate  <= DATEADD(Week,-4, "+"'"+ date +"'"+" )");
			} else {
				builder.append(" and " + " CreateDate  < DATEADD(Week," + "-" + week.replace("W", "") + "+ 1"
						+ ", "+"'"+ date +"'"+") and CreateDate >= DATEADD(Week," + "-" + week.replace("W", "") + ", "+"'"+ date +"'"+")");
			}
		} else {
			if (week.equalsIgnoreCase(">")) {
				builder.append(filter.getColumnDateName() + " <= DATEADD(" + filter.getTimePeriod() + "," + "-"
						+ filter.getStart() + ",  "+"'"+ date +"'"+") " + " and " + filter.getColumnDateName() + " > DATEADD("
						+ filter.getTimePeriod() + "," + "-" + (filter.getEnd() - weekNo) + ",  "+"'"+ date +"'"+") ");
			} else {
				builder.append(filter.getColumnDateName() + " <= DATEADD(" + filter.getTimePeriod() + "," + "-"
						+ (filter.getEnd() - (weekNo + 7)) + ",  "+"'"+ date +"'"+") " + " and " + filter.getColumnDateName()
						+ " > DATEADD(" + filter.getTimePeriod() + "," + "-" + (filter.getEnd() - weekNo)
						+ ",  "+"'"+ date +"'"+") ");
			}
		}

		query = builder.toString();
		builder.delete(0, builder.length());
		return query;
	}

	//////////////////////////////////////////////////////////
	////////////////// return details per score///////////////
	public String getDetailsPerScore(int viewID, String type, String score, String idName, String viewName) {
		getFilter(type, idName + "-" + viewID, viewName);

		builder.append("select  *  ");
		if (idName.equalsIgnoreCase("Comp")) {
			builder.append(CommonsViewData.FROMCOMPLIANCEHISTORY);
		} else {
			builder.append(CommonsViewData.FROMCOVERAGEHISTORY);
		}
		builder.append(" Where ");
		filter = setDefaultsIfNull(filter);
		if (filter.getIsScanned() == -1) {
		} else {
			builder.append(" IsScanned = " + filter.getIsScanned() + " and ");
		}
		if (filter.getIsNew() == -1) {
		} else {
			builder.append(" IsNew = " + filter.getIsNew() + " and ");
		}
		if (filter.getIsComply() == -1) {
		} else {
			builder.append(" IsComply = " + filter.getIsComply() + " and ");
		}
		if (filter.getIsCovered() == -1) {
		} else {
			builder.append(" IsCoveredByControl = " + filter.getIsCovered() + " ");
		}
		if (filter.getColumnDateName().equalsIgnoreCase("Score")) {
			if (score.equalsIgnoreCase("first")) {
				builder.append(
						"and (" + filter.getColumnDateName() + " >= " + ((int) (filter.getStart() + filter.getEnd()) / 2)
								+ " and " + filter.getColumnDateName() + " < " + filter.getEnd() + " ) ");
			} else if (score.equalsIgnoreCase("second")) {
				builder.append("and  (" + filter.getColumnDateName() + " >= " + filter.getStart() + " and "
						+ filter.getColumnDateName() + " < " + ((int) (filter.getStart() + filter.getEnd()) / 2)
						+ " ) ");
			}
		}
		query = builder.toString();
		builder.delete(0, builder.length());
		return query;
	}

	/////////////////////////////////////////////////////////
	/////////////// count of details per Score///////////////
	public String getDataPerScore(int viewID, String type, String viewName) {
		fillTheData(type + "-" + viewID, viewName);
		List<String> list = Arrays.asList("first", "second");
		List<AllRadarData> types = new ArrayList<>();
		types.add(greenExist);
		types.add(greenNew);
		types.add(orangeExist);
		types.add(orangeNew);
		
		if (types.get(0).getColorType() == null) {
			query = "\r\n" + "select 'first' as Type,   \r\n" + "0    as GreenExist ,\r\n" + "0    as GreenNew ,\r\n"
					+ "0    as OrangeExist ,\r\n" + "0    as OrangeNew \r\n" + "union \r\n"
					+ "select 'second' as Type,\r\n" + "0  as GreenExist ,\r\n" + "0  as GreenNew ,\r\n"
					+ "0  as OrangeExist ,\r\n" + "0  as OrangeExist ";
		} else {
			for (int i = 0; i < list.size(); i++) {
				builder.append("select " + "'" + list.get(i) + "'" + " as Type, ");
				for (AllRadarData item : types) {
					builder.append(" " + CommonsViewData.SUMCASE);

					if (item.getIsScanned() == -1) {
					} else {
						builder.append(" IsScanned = " + item.getIsScanned() + " and ");
					}
					if (item.getIsNew() == -1) {
					} else {
						builder.append(" IsNew = " + item.getIsNew() + " and ");
					}
					if (item.getIsComply() == -1) {
					} else {
						builder.append(" IsComply = " + item.getIsComply() + " and ");
					}
					if (item.getIsCovered() == -1) {
					} else {
						builder.append(" IsCoveredByControl = " + item.getIsCovered() + " and ");
					}
					if (item.getColumnDateName().equalsIgnoreCase("Score")) {
						if (list.get(i).equalsIgnoreCase("first")) {
							builder.append(" (" + item.getColumnDateName() + " >= "
									+ ((int) (item.getStart() + item.getEnd()) / 2) + " and " + item.getColumnDateName()
									+ " < " + item.getEnd() + " ) ");
						} else if (list.get(i).equalsIgnoreCase("second")) {
							builder.append(" (" + item.getColumnDateName() + " >= " + item.getStart() + " and "
									+ item.getColumnDateName() + " < " + ((int) (item.getStart() + item.getEnd()) / 2)
									+ " ) ");
						}
					} else {
						String x = (String) builder.toString().subSequence( builder.toString().length() - 4, builder.toString().length());
						if (x.contains("and")) {
							builder.delete(builder.length() - 4, builder.length());
						}
					}
					builder.append(CommonsViewData.CONDITION + " ");
					builder.append(" " + " as " + item.getColorType().replace("-", ""));
					if (item.getColorType().equalsIgnoreCase("Orange-New")) {
					} else {
						builder.append(" , ");
					}
				}
				if (type.equalsIgnoreCase("Comp")) {
					builder.append(CommonsViewData.FROMCOMPLIANCEHISTORY);
				} else {
					builder.append(CommonsViewData.FROMCOVERAGEHISTORY);
				}
				if (i != 1) {
					builder.append(" union ");
				}
			}

		query = builder.toString();
		builder.delete(0, builder.length());
		}
		return query;
	}
	
	////////////////////////////////////////////////////////////////////////
	/////////////// count of details per Score Cache///////////////
	public String getDataPerScoreCache(int viewID, String type, String viewName) {
		fillTheData(type + "-" + viewID, viewName);
		List<String> list = Arrays.asList("first", "second");
		List<AllRadarData> types = new ArrayList<>();
		types.add(greenExist);
		types.add(greenNew);
		types.add(orangeExist);
		types.add(orangeNew);
		
		if (types.get(0).getColorType() == null) {
			query = "\r\n" + "select 'first' as Type,   \r\n" + "0    as GreenExist ,\r\n" + "0    as GreenNew ,\r\n"
					+ "0    as OrangeExist ,\r\n" + "0    as OrangeNew \r\n" + "union \r\n"
					+ "select 'second' as Type,\r\n" + "0  as GreenExist ,\r\n" + "0  as GreenNew ,\r\n"
					+ "0  as OrangeExist ,\r\n" + "0  as OrangeExist ";
		} else {
			builder.append("select * from ( ");
			for (int i = 0; i < list.size(); i++) {
				builder.append("select " + "'" + list.get(i) + "'" + " as Type, ");
				for (AllRadarData item : types) {
					builder.append(" " + CommonsViewData.SUMCASE);

					if (item.getIsScanned() == -1) {
					} else {
						builder.append(" IsScanned = " + item.getIsScanned() + " and ");
					}
					if (item.getIsNew() == -1) {
					} else {
						builder.append(" IsNew = " + item.getIsNew() + " and ");
					}
					if (item.getIsComply() == -1) {
					} else {
						builder.append(" IsComply = " + item.getIsComply() + " and ");
					}
					if (item.getIsCovered() == -1) {
					} else {
						builder.append(" IsCoveredByControl = " + item.getIsCovered() + " and ");
					}
					if (item.getColumnDateName().equalsIgnoreCase("Score")) {
						if (list.get(i).equalsIgnoreCase("first")) {
							builder.append(" (" + item.getColumnDateName() + " >= "
									+ ((int) (item.getStart() + item.getEnd()) / 2) + " and " + item.getColumnDateName()
									+ " < " + item.getEnd() + " ) ");
						} else if (list.get(i).equalsIgnoreCase("second")) {
							builder.append(" (" + item.getColumnDateName() + " >= " + item.getStart() + " and "
									+ item.getColumnDateName() + " < " + ((int) (item.getStart() + item.getEnd()) / 2)
									+ " ) ");
						}
					} else {
						String x = (String) builder.toString().subSequence( builder.toString().length() - 4, builder.toString().length());
						if (x.contains("and")) {
							builder.delete(builder.length() - 4, builder.length());
						}
					}
					builder.append(CommonsViewData.CONDITION + " ");
					builder.append(" " + " as " + item.getColorType().replace("-", ""));
					if (item.getColorType().equalsIgnoreCase("Orange-New")) {
					} else {
						builder.append(" , ");
					}
				}
				if (type.equalsIgnoreCase("Comp")) {
					builder.append(" ,LocationCode as LocationCode, SubviewID as SubviewID ");
					builder.append(CommonsViewData.FROMCOMPLIANCEHISTORY);
					builder.append(" group by LocationCode,SubviewID ");
				} else {
					builder.append(" ,LocationCode as LocationCode, SubviewID as SubviewID ");
					builder.append(CommonsViewData.FROMCOVERAGEHISTORY);
					builder.append(" group by LocationCode,SubviewID ");
				}
				if (i != 1) {
					builder.append(" union ");
				}
			}
			builder.append(" ) query  ");

			query = builder.toString();
			builder.delete(0, builder.length());
		}
		return query;
	}

}
