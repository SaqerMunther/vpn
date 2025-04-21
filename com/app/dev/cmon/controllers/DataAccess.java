package com.app.dev.cmon.controllers;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.*;

import org.apache.poi.ss.usermodel.Sheet;

import com.app.dev.cmon.components.AllRadarData;
import com.app.dev.cmon.components.AssetInfo;
import com.app.dev.cmon.components.ComplianceInfo;
import com.app.dev.cmon.components.Countres;
import com.app.dev.cmon.components.CountryLeftCardData;
import com.app.dev.cmon.components.ElementVisibility;
import com.app.dev.cmon.components.RadarDashValidation;
import com.app.dev.cmon.components.Reports;
import com.app.dev.cmon.components.Score;
import com.app.dev.cmon.components.ServiceSyncMain;
import com.app.dev.cmon.components.TotalAssetsHistory;
import com.app.dev.cmon.components.ViewInfo;
import com.app.dev.cmon.components.Views;
import com.arabbank.dev.atm.dashboard.dao.MsSqlServerDataAccess;
import com.arabbank.dev.utility.Pair;
import com.jk.db.dataaccess.core.JKFinder;
import com.jk.util.JKIOUtil;
import com.mysql.jdbc.Connection;

public class DataAccess extends MsSqlServerDataAccess {

	public static String date;

	public long getCountriesData(int startRange, int endRange, String interval, String status, String country) {
		country = country.equalsIgnoreCase("all") ? "" : country;
		return executeQueryAsLong("select ISACWEB.dbo.External_Cmon_Coverage_Count(?,?,?,?,?)", startRange, endRange,
				interval, status.equals("New") ? 1 : 0, country);
	}

	public List<Pair<Number, String>> getCoverageTotal(int viewId, int subViewId, int startRange, int endRange,
			int month, String country) {
		String query = "Select * from ISACWEB.dbo.External_Cmon_Coverage_Packets(?, ?, ?,?,?,?)";
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, viewId);
				ps.setInt(2, subViewId);
				ps.setString(3, country.equalsIgnoreCase("all") ? "" : country);
				ps.setInt(4, startRange);
				ps.setInt(5, endRange);
				ps.setInt(6, month);

			}

			@Override
			public Pair<Number, String> populate(ResultSet rs) throws SQLException {
				Pair<Number, String> item = new Pair<Number, String>();
				item.setFirst(rs.getDouble(1));
				item.setSecond(rs.getString(2));
				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});

	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	public List<ServiceSyncMain> getLatestSyncMain() {
		return getList(new JKFinder() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
			}

			@Override
			public ServiceSyncMain populate(ResultSet rs) throws SQLException {
				return mapServiceSyncMain(rs);
			}

			@Override
			public String getQuery() {
				// External SQL file retrieval
				return JKIOUtil.getSqlFile("latest-sync-main.sql");
			}
		});
	}

	private ServiceSyncMain mapServiceSyncMain(ResultSet rs) throws SQLException {
		ServiceSyncMain item = new ServiceSyncMain();
		item.setSyncID(rs.getInt("SyncID"));
		item.setSyncDate(rs.getTimestamp("SyncDate"));
		item.setCompletedDate(rs.getTimestamp("CompletedDate"));
		item.setFetching(rs.getBoolean("IsFetching"));
		item.setUpdateStartDate(rs.getTimestamp("UpdateStartDate"));
		item.setUpdateCompletedDate(rs.getTimestamp("UpdateCompletedDate"));
		item.setUpdateCount(rs.getInt("UpdateCount"));
		item.setFetchingUpdate(rs.getBoolean("IsFetchingUpdate"));
		item.setCycleID(rs.getInt("CycleID"));
		item.setCycle(rs.getBoolean("IsCycle"));
		return item;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	public List<RadarDashValidation> getRadarDashValidation() {
		return getList(new JKFinder() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
			}

			@Override
			public RadarDashValidation populate(ResultSet rs) throws SQLException {
				return radarDashValidation(rs);
			}

			@Override
			public String getQuery() {
				// External SQL file retrieval
				return JKIOUtil.getSqlFile("radar-data-validation.sql");
			}
		});
	}

	private RadarDashValidation radarDashValidation(ResultSet rs) throws SQLException {
		RadarDashValidation item = new RadarDashValidation();

		item.setRadarDashValidationID(rs.getLong("RadarDashValidationID"));
		item.setSyncID(rs.getInt("SyncID"));
		item.setLocationID(rs.getInt("LocationID"));
		item.setControlID(rs.getInt("ControlID"));
		item.setComplianceMatch(rs.getBoolean("IsComplianceMatch"));
		item.setCoverageMatch(rs.getBoolean("IsCoverageMatch"));
		item.setRadarCompliancePercent(rs.getDouble("RadarCompliancePercent"));
		item.setDashCompliancePercent(rs.getDouble("DashCompliancePercent"));
		item.setRadarCoveragePercent(rs.getDouble("RadarCoveragePercent"));
		item.setDashboardCoveragePercent(rs.getDouble("DashboardCoveragePercent"));
		item.setValidationThreshold(rs.getDouble("ValidationThreshold"));
		item.setInsertDate(rs.getTimestamp("InsertDate").toLocalDateTime());

		return item;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////

	public List<Pair<Integer, Double>> getCoverageUpper(int month, String country, int viewId, int subViewId) {
		String query = " select  * from ISACWEB.dbo.External_Cmon_Total_Coverage(?,?,?,?)";
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, month);
				ps.setString(2, country.equalsIgnoreCase("all") ? "" : country);
				ps.setInt(3, viewId);
				ps.setInt(4, subViewId);

			}

			@Override
			public Pair<Integer, Double> populate(ResultSet rs) throws SQLException {
				Pair<Integer, Double> item = new Pair<>();
				item.setFirst(rs.getInt(1));
				item.setSecond(rs.getDouble(2));
				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});
	}

	public List<Pair<Integer, Double>> getCompUpper(int month, int viewId, int subViewId, String country, int isRisk) {
		String query = "   select * from ISACWEB.dbo.External_Cmon_Total_Compliance(?,?,?,?,?)";
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, month);
				ps.setInt(2, viewId);
				ps.setInt(3, subViewId);
				ps.setString(4, country.equalsIgnoreCase("all") ? "" : country);
				ps.setInt(5, isRisk);
			}

			@Override
			public Pair<Integer, Double> populate(ResultSet rs) throws SQLException {
				Pair<Integer, Double> item = new Pair<Integer, Double>();
				item.setFirst(rs.getInt(1));
				item.setSecond(rs.getDouble(2));
				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});
	}

	public List<Score> getCompUpperPerCountry(int month, int viewId, int subViewId, int isRisk) {
		String query = "select * from ISACWEB.dbo.External_Cmon_Total_Compliance_NEW(?,?,?,?)\r\n" + "\r\n"
				+ "union \r\n" + "\r\n"
				+ "select sum(TotalComply) TotalComply , sum(TotalNotComply) as TotalNotComply ,'ALL' as locationCode from ISACWEB.dbo.External_Cmon_Total_Compliance_NEW(?,?,?,?)\r\n";
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, month);
				ps.setInt(2, viewId);
				ps.setInt(3, subViewId);
				ps.setInt(4, isRisk);
				ps.setInt(5, month);
				ps.setInt(6, viewId);
				ps.setInt(7, subViewId);
				ps.setInt(8, isRisk);
			}

			@Override
			public Score populate(ResultSet rs) throws SQLException {
				Score item = new Score();
				item.setLocation(rs.getString("locationCode"));
				item.setTotalComply(rs.getInt("TotalComply"));
				item.setTotalNotComply(rs.getDouble("TotalNotComply"));
				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});
	}

	public List<Views> getSubViews(int id) {
		String query = "select * from ISACWEB.dbo.External_Cmon_SubView_Select(?)   union   select -1 , 'ALL', 0 ";
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);
			}

			@Override
			public Views populate(ResultSet rs) throws SQLException {
				Views item = new Views();
				item.setId(rs.getInt(1));
				item.setName(rs.getString(2));
				item.setIsManual(rs.getInt(3));
				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});
	}

	/////////////////////////////////////////////////////////////////////////////////////////////

	public List<AssetInfo> getDataPerWeek(int viewId, int subViewId, String country, String type, String week,
			String idName, String viewName, String date) {
		String query;
		QeuryBilder com = new QeuryBilder();
		query = com.getDetailsPerWeek(viewId, type, week, idName, viewName, date);
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, viewId);
				ps.setInt(2, subViewId);
				ps.setString(3, country);
				ps.setString(4, date);
			}

			@Override
			public AssetInfo populate(ResultSet rs) throws SQLException {
				AssetInfo item = new AssetInfo();
				item.setDomain(rs.getString("Domain"));
				item.setHostName(rs.getString("HostName"));
				item.setIp(rs.getString("IPAddress"));
				item.setCountry(rs.getString("LocationName"));
				item.setScanDate(rs.getDate("ScanDate"));
				item.setIsScanned(rs.getInt("IsScanned"));
				item.setIsLaptop(rs.getInt("IsLaptop"));
				item.setCreateDate(rs.getDate("CreateDate"));
				item.setLastLogon(rs.getDate("LastLogon"));
				item.setOUName(rs.getString("OUName"));
				item.setIsComply(rs.getInt("IsComply"));
				item.setIsCoveredByControl(rs.getInt("IsCoveredByControl"));
				item.setIsManualCovered(rs.getInt("IsManualCovered"));
				item.setExtraValue(rs.getString("ExtraValue"));
				item.setEvaluationDate(rs.getDate("EvaluationDate"));
				item.setVersionParentName(rs.getString("VersionParentName"));
				item.setApplicationName(rs.getString("ApplicationName"));
				item.setExpireInDays(rs.getInt("ExpireInDays"));
				item.setIsNew(rs.getInt("isNew"));
				item.setSubViewId(rs.getInt("subViewId"));
				item.setLocationCode(rs.getString("locationCode"));
				item.setSyncDate(rs.getDate("syncDate"));
				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});

	}

	//////////////////////////////////////////////////////////////////////////////
	public List<AssetInfo> getRadarData(int viewId, int subViewId, String country, String type, String idName,
			String viewName, String date) {
		String query;
		QeuryBilder com = new QeuryBilder();
		query = com.getDetails(type, idName + "-" + viewId, viewName, date);

		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, viewId);
				ps.setInt(2, subViewId);
				ps.setString(3, country);
				ps.setString(4, date);
			}

			@Override
			public AssetInfo populate(ResultSet rs) throws SQLException {
				AssetInfo item = new AssetInfo();
				item.setDomain(rs.getString("Domain"));
				item.setHostName(rs.getString("HostName"));
				item.setIp(rs.getString("IPAddress"));
				item.setCountry(rs.getString("LocationName"));
				item.setScanDate(rs.getDate("ScanDate"));
				item.setIsScanned(rs.getInt("IsScanned"));
				item.setIsLaptop(rs.getInt("IsLaptop"));
				item.setCreateDate(rs.getDate("CreateDate"));
				item.setLastLogon(rs.getDate("LastLogon"));
				item.setOUName(rs.getString("OUName"));
				item.setIsComply(rs.getInt("IsComply"));
				item.setIsCoveredByControl(rs.getInt("IsCoveredByControl"));
				item.setIsManualCovered(rs.getInt("IsManualCovered"));
				item.setExtraValue(rs.getString("ExtraValue"));
				item.setEvaluationDate(rs.getDate("EvaluationDate"));
				item.setVersionParentName(rs.getString("VersionParentName"));
				item.setApplicationName(rs.getString("ApplicationName"));
				item.setExpireInDays(rs.getInt("ExpireInDays"));
				item.setIsNew(rs.getInt("isNew"));
				item.setSubViewId(rs.getInt("subViewId"));
				item.setLocationCode(rs.getString("locationCode"));
				item.setSyncDate(rs.getDate("syncDate"));
				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});

	}

	//////////////////////////////////////////////////////////////
	public List<ComplianceInfo> getCompDetailsPerWeek(int viewId, int subViewId, String country, String type,
			String week, String idName, String viewName, String date) {
		String query;
		QeuryBilder com = new QeuryBilder();
		query = com.getDetailsPerWeek(viewId, type, week, idName, viewName, date);
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, viewId);
				ps.setInt(2, subViewId);
				ps.setString(3, country);
				ps.setString(4, date);
			}

			@Override
			public ComplianceInfo populate(ResultSet rs) throws SQLException {

				ComplianceInfo item = new ComplianceInfo();
				item.setPacketId(rs.getInt("PacketID"));
				item.setHostName(rs.getString("HostName"));
				item.setDomain(rs.getString("Domain"));
				item.setIp(rs.getString("IPAddress"));
				item.setCountry(rs.getString("LocationName"));
				item.setAssetTypeName(rs.getString("AssetTypeName"));
				item.setVersionName(rs.getString("VersionName"));
				item.setInsertDate(rs.getDate("InsertDate"));
				item.setScore(rs.getDouble("Score"));
				item.setIsNew(rs.getInt("IsNew"));
				item.setIsLaptop(rs.getInt("IsLaptop"));
				item.setCreateDate(rs.getDate("CreateDate"));
				item.setLastLogon(rs.getDate("LastLogon"));
				item.setOUName(rs.getString("OUName"));
				item.setIsComply(rs.getInt("IsComply"));
				item.setIsCoveredByControl(rs.getInt("IsCoveredByControl"));
				item.setIsManualCovered(rs.getInt("IsManualCovered"));
				item.setExtraValue(rs.getString("ExtraValue"));

				item.setEvaluationDate(rs.getDate("EvaluationDate"));
				item.setVersionParentName(rs.getString("VersionParentName"));
				item.setApplicationName(rs.getString("ApplicationName"));
				item.setExpireInDays(rs.getInt("ExpireInDays"));
				item.setSubViewId(rs.getInt("subViewId"));
				item.setLocationCode(rs.getString("locationCode"));
				item.setControlValue(rs.getDouble("controlValue"));
				item.setSyncDate(rs.getDate("syncDate"));
				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});

	}

	///////////////////////////////////////////////////////////////////////

	public List<ComplianceInfo> getCompDetailsPerScore(int viewId, int subViewId, String country, String type,
			String score, String idName, String viewName, String date) {
		String query;
		QeuryBilder com = new QeuryBilder();
		query = com.getDetailsPerScore(viewId, type, score, idName, viewName);

		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, viewId);
				ps.setInt(2, subViewId);
				ps.setString(3, country);
				ps.setString(4, date);
			}

			@Override
			public ComplianceInfo populate(ResultSet rs) throws SQLException {

				ComplianceInfo item = new ComplianceInfo();
				item.setPacketId(rs.getInt("PacketID"));
				item.setHostName(rs.getString("HostName"));
				item.setDomain(rs.getString("Domain"));
				item.setIp(rs.getString("IPAddress"));
				item.setCountry(rs.getString("LocationName"));
				item.setAssetTypeName(rs.getString("AssetTypeName"));
				item.setVersionName(rs.getString("VersionName"));
				item.setInsertDate(rs.getDate("InsertDate"));
				item.setScore(rs.getDouble("Score"));
				item.setIsNew(rs.getInt("IsNew"));
				item.setIsLaptop(rs.getInt("IsLaptop"));
				item.setCreateDate(rs.getDate("CreateDate"));
				item.setLastLogon(rs.getDate("LastLogon"));
				item.setOUName(rs.getString("OUName"));
				item.setIsComply(rs.getInt("IsComply"));
				item.setIsCoveredByControl(rs.getInt("IsCoveredByControl"));
				item.setIsManualCovered(rs.getInt("IsManualCovered"));
				item.setExtraValue(rs.getString("ExtraValue"));

				item.setEvaluationDate(rs.getDate("EvaluationDate"));
				item.setVersionParentName(rs.getString("VersionParentName"));
				item.setApplicationName(rs.getString("ApplicationName"));
				item.setExpireInDays(rs.getInt("ExpireInDays"));
				item.setSubViewId(rs.getInt("subViewId"));
				item.setLocationCode(rs.getString("locationCode"));
				item.setControlValue(rs.getDouble("controlValue"));
				item.setSyncDate(rs.getDate("syncDate"));
				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});

	}

	//////////////////////////////////////////////////////////////

	public List<ComplianceInfo> getCompDetails(int viewId, int subViewId, String country, String type, String idName,
			String viewName, String date) {
		String query;
		QeuryBilder com = new QeuryBilder();
		query = com.getDetails(type, idName + "-" + viewId, viewName, date);
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, viewId);
				ps.setInt(2, subViewId);
				ps.setString(3, country);
				ps.setString(4, date);
			}

			@Override
			public ComplianceInfo populate(ResultSet rs) throws SQLException {

				ComplianceInfo item = new ComplianceInfo();
				item.setPacketId(rs.getInt("PacketID"));
				item.setHostName(rs.getString("HostName"));
				item.setDomain(rs.getString("Domain"));
				item.setIp(rs.getString("IPAddress"));
				item.setCountry(rs.getString("LocationName"));
				item.setAssetTypeName(rs.getString("AssetTypeName"));
				item.setVersionName(rs.getString("VersionName"));
				item.setInsertDate(rs.getDate("InsertDate"));
				item.setScore(rs.getDouble("Score"));
				item.setIsNew(rs.getInt("IsNew"));
				item.setIsLaptop(rs.getInt("IsLaptop"));
				item.setCreateDate(rs.getDate("CreateDate"));
				item.setLastLogon(rs.getDate("LastLogon"));
				item.setOUName(rs.getString("OUName"));
				item.setIsComply(rs.getInt("IsComply"));
				item.setIsCoveredByControl(rs.getInt("IsCoveredByControl"));
				item.setIsManualCovered(rs.getInt("IsManualCovered"));
				item.setExtraValue(rs.getString("ExtraValue"));

				item.setEvaluationDate(rs.getDate("EvaluationDate"));
				item.setVersionParentName(rs.getString("VersionParentName"));
				item.setApplicationName(rs.getString("ApplicationName"));
				item.setExpireInDays(rs.getInt("ExpireInDays"));
				item.setSubViewId(rs.getInt("subViewId"));
				item.setLocationCode(rs.getString("locationCode"));
				item.setControlValue(rs.getDouble("controlValue"));
				item.setSyncDate(rs.getDate("syncDate"));
				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});

	}

	//////////////////////////////////////////////////////////////
	public List<AssetInfo> getExcludeList(int viewId, int subViewId, String country) {
		String query = "select * from ISACWEB.[dbo].[External_Cmon_Exeluded_All](?,?,?)";
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, viewId);
				ps.setInt(2, subViewId);
				ps.setString(3, country);
			}

			@Override
			public AssetInfo populate(ResultSet rs) throws SQLException {
				AssetInfo item = new AssetInfo();
				item.setDomain(rs.getString("Domain"));
				item.setHostName(rs.getString("HostName"));
				item.setIp(rs.getString("IPAddress"));
				item.setCountry(rs.getString("LocationName"));
				item.setScanDate(rs.getDate("ScanDate"));
				item.setIsScanned(rs.getInt("IsScanned"));
				item.setIsLaptop(rs.getInt("IsLaptop"));
				item.setCreateDate(rs.getDate("CreateDate"));
				item.setLastLogon(rs.getDate("LastLogon"));
				item.setOUName(rs.getString("OUName"));
				item.setIsComply(rs.getInt("IsComply"));
				item.setIsCoveredByControl(rs.getInt("IsCoveredByControl"));
				item.setIsManualCovered(rs.getInt("IsManualCovered"));
				item.setExtraValue(rs.getString("ExtraValue"));
				item.setEvaluationDate(rs.getDate("EvaluationDate"));
				item.setVersionParentName(rs.getString("VersionParentName"));
				item.setApplicationName(rs.getString("ApplicationName"));
				item.setExpireInDays(rs.getInt("ExpireInDays"));
				item.setIsNew(rs.getInt("isNew"));
				item.setSubViewId(rs.getInt("subViewId"));
				item.setLocationCode(rs.getString("locationCode"));
				item.setExcludedReason(rs.getString("ExcludeReason"));
				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});

	}

	public List<AssetInfo> getAssetInfoHistory(int viewId, int subViewId, String country, int days) {
		String query = "select * from ISACWEB.[dbo].[External_Cmon_Coverage_All_historical](?,?,?,?) where isNew = 0";
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, viewId);
				ps.setInt(2, subViewId);
				ps.setString(3, country);
				ps.setInt(4, days);
			}

			@Override
			public AssetInfo populate(ResultSet rs) throws SQLException {
				AssetInfo item = new AssetInfo();
				item.setDomain(rs.getString("Domain"));
				item.setHostName(rs.getString("HostName"));
				item.setIp(rs.getString("IPAddress"));
				item.setCountry(rs.getString("LocationName"));
				item.setScanDate(rs.getDate("ScanDate"));
				item.setIsScanned(rs.getInt("IsScanned"));
				item.setIsLaptop(rs.getInt("IsLaptop"));
				item.setCreateDate(rs.getDate("CreateDate"));
				item.setLastLogon(rs.getDate("LastLogon"));
				item.setOUName(rs.getString("OUName"));
				item.setIsComply(rs.getInt("IsComply"));
				item.setIsCoveredByControl(rs.getInt("IsCoveredByControl"));
				item.setIsManualCovered(rs.getInt("IsManualCovered"));
				item.setExtraValue(rs.getString("ExtraValue"));
				item.setEvaluationDate(rs.getDate("EvaluationDate"));
				item.setVersionParentName(rs.getString("VersionParentName"));
				item.setApplicationName(rs.getString("ApplicationName"));
				item.setExpireInDays(rs.getInt("ExpireInDays"));
				item.setIsNew(rs.getInt("isNew"));
				item.setSubViewId(rs.getInt("subViewId"));
				item.setLocationCode(rs.getString("locationCode"));
				item.setSyncDate(rs.getDate("syncDate"));
				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});

	}

	////////////////////////////////////////////////

	public List<ViewInfo> getHistoryData(String user, String date) {
		String query = "";
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setString(1, date);
				ps.setString(2, user);
				ps.setString(3, date);
				ps.setString(4, date);
				ps.setString(5, user);
				ps.setString(6, date);
			}

			@Override
			public ViewInfo populate(ResultSet rs) throws SQLException {
				ViewInfo item = new ViewInfo();
				item.setControlName(rs.getString("ViewName"));
				item.setFullName(rs.getString("ViewName"));
				item.setId(rs.getInt("ViewID"));
				item.setCovFirst(rs.getInt("covFirst"));
				item.setCovSecond(rs.getInt("covSecound"));
				item.setCompFirst(rs.getInt("compFirst"));
				item.setCompSecond(rs.getInt("compSecound"));
				item.setCountry(rs.getString("Country"));
				item.setDate(rs.getString("Date"));
				return item;
			}

			@Override
			public String getQuery() {
				return JKIOUtil.getSqlFile("left-card-today-data.sql");
			}
		});

	}

	//////////////////////////////////////////////////////////////////////////
	public List<AssetInfo> getSyncDate(int viewId, int subViewId, String country, String date) {
		String query = "";

		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, viewId);
				ps.setInt(2, subViewId);
				ps.setString(3, country);
				ps.setString(4, date);
			}

			@Override
			public AssetInfo populate(ResultSet rs) throws SQLException {
				AssetInfo item = new AssetInfo();
				item.setSyncDate(rs.getDate("syncDate"));
				item.setScanDate(rs.getDate("ScanDate"));
				return item;
			}

			@Override
			public String getQuery() {
				return JKIOUtil.getSqlFile("coverage-all-last-date.sql");
			}
		});

	}

	///////////////////////////////////////////////
	public List<AssetInfo> getAllCoverage(int viewId, int subViewId, String country, String date) {
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, viewId);
				ps.setInt(2, subViewId);
				ps.setString(3, country);
				ps.setString(4, date);
			}

			@Override
			public AssetInfo populate(ResultSet rs) throws SQLException {
				AssetInfo item = new AssetInfo();
				item.setSubViewId(rs.getInt("SubviewID"));
				item.setLocationCode(rs.getString("LocationCode"));
				item.setIsCoveredByControl(rs.getInt("IsCoveredByControl"));
				item.setSyncDate(rs.getDate("SyncDate"));
				item.setScanDate(rs.getDate("ScanDate"));
				// Set other fields as necessary
				return item;
			}

			@Override
			public String getQuery() {
				return JKIOUtil.getSqlFile("all-coverage.sql");
			}
		});
	}
	///////////////////////////////////////////////

	public List<Countres> getCountryValue(String date) {
		String query = "";
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
			}

			@Override
			public Countres populate(ResultSet rs) throws SQLException {
				Countres item = new Countres();
				item.setViewID(rs.getInt("ControlID"));
				item.setViewName(rs.getString("ViewName"));
				item.setCountry(rs.getString("Country"));
				item.setValue(rs.getDouble("Percentage"));
				item.setDate(rs.getString("Date"));
				return item;
			}

			@Override
			public String getQuery() {
				return JKIOUtil.getSqlFile("left-card-percentage-per-country.sql").replaceAll("xDate", date);
			}
		});

	}
	///////////////////////////////////////////////

	public List<ElementVisibility> getVisibleElement() {
		String query = "SELECT * FROM ISACWEB.[dbo].[Cmon_Visability]";
		return getList(new JKFinder() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub

			}

			@Override
			public ElementVisibility populate(ResultSet rs) throws SQLException {
				ElementVisibility item = new ElementVisibility();

				item.setId(rs.getInt(1));
				item.setName(rs.getString(2));
				item.setIsVisible(rs.getInt(3));
				return item;

			}

			@Override
			public String getQuery() {
				return query;
			}
		});

	}

	///////////////////////////////////////////////////////////////////////////
	public List<ViewInfo> getDataInfo(int viewId, int subViewId, String country, String type, String viewName,
			String date) {
		QeuryBilder com = new QeuryBilder();
		String query = com.getCompAndCovInfo(viewId, type, viewName);
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				if (query.contains("Compliance") || query.contains("Coverage")) {
					ps.setInt(1, viewId);
					ps.setInt(2, subViewId);
					ps.setString(3, country);
					ps.setString(4, date);
					ps.setInt(5, viewId);
					ps.setInt(6, subViewId);
					ps.setString(7, country);
					ps.setString(8, date);
					ps.setInt(9, viewId);
					ps.setInt(10, subViewId);
					ps.setString(11, country);
					ps.setString(12, date);
					ps.setInt(13, viewId);
					ps.setInt(14, subViewId);
					ps.setString(15, country);
					ps.setString(16, date);
				}
			}

			@Override
			public ViewInfo populate(ResultSet rs) throws SQLException {

				ViewInfo item = new ViewInfo();
				item.setType(rs.getString("Type"));
				item.setTotalSum(rs.getLong("TotalSum"));
				item.setGreen(rs.getLong("GreenCircle"));
				item.setOrange(rs.getLong("OrangeCircle"));
				item.setRed(rs.getLong("RedCircle"));
				item.setIt(rs.getLong("IT"));
				item.setRisk(rs.getLong("Risk"));
				;

				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});
	}

	//////////////////////////////// New_Query//////////////////////////////////
	public List<ViewInfo> getDataInfoNew(int viewId, int subViewId, String country, String type, String date,
			String textName) {

		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {

				ps.setInt(1, viewId);
				if (subViewId == -1) {
					ps.setString(2, "%");
				} else {
					ps.setInt(2, subViewId);
				}
				if (country.equalsIgnoreCase("PLC") || country.equalsIgnoreCase("all")
						|| country.equalsIgnoreCase("")) {
					ps.setString(3, "%");
				} else {
					ps.setString(3, country);
				}

				ps.setString(4, "%");
				if (viewId == 4) {
					ps.setString(5, textName);
					ps.setString(6, date);
				} else {
					ps.setString(5, date);
				}

			}

			@Override
			public ViewInfo populate(ResultSet rs) throws SQLException {

				ViewInfo item = new ViewInfo();
				item.setDate(rs.getString("Date"));
				item.setTypeSort(rs.getString("TypeSort"));
				item.setType(rs.getString("Type"));
				item.setTotalSum(rs.getLong("TotalSum"));
				item.setGreen(rs.getLong("Green"));
				item.setOrange(rs.getLong("Orange"));
				item.setRed(rs.getLong("Red"));
				item.setIt(rs.getLong("IT"));
				item.setRisk(rs.getLong("RiskNew"));
				item.setSupViewID(rs.getInt("SubviewID"));
				return item;
			}

			@Override
			public String getQuery() {
				if (viewId == 4) {
					return JKIOUtil.getSqlFile("data-info-wsus.sql");
				} else {
					return JKIOUtil.getSqlFile("DataInfo.sql");
				}
			}
		});

	}

	//////////////////////////////////////////////////////////////////////////
	public List<ViewInfo> getAvailabelDate() {

		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
			}

			@Override
			public ViewInfo populate(ResultSet rs) throws SQLException {

				ViewInfo item = new ViewInfo();
				item.setDate(rs.getString("Date"));
				return item;
			}

			@Override
			public String getQuery() {
				return JKIOUtil.getSqlFile("available-date.sql");
			}
		});

	}

	//////////////////////////////////////////////////////////////////////////
	public List<ViewInfo> getDate() {
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {

			}

			@Override
			public ViewInfo populate(ResultSet rs) throws SQLException {

				ViewInfo item = new ViewInfo();
				item.setDate(rs.getString("Date"));

				return item;
			}

			@Override
			public String getQuery() {
				return JKIOUtil.getSqlFile("date.sql");
			}
		});
	}

	///////////////////////////////////////////////////////////////////////////
	public List<AllRadarData> getAllRadarData(String IDName, String viewName) {
		String query = " Select * from AllInfo where IDName = ? and ViewName = ?";
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setString(1, IDName);
				ps.setString(2, viewName);
			}

			@Override
			public AllRadarData populate(ResultSet rs) throws SQLException {

				AllRadarData item = new AllRadarData();
				item.setViewID(rs.getInt("ViewID"));
				item.setColorType(rs.getString("ColorType"));
				item.setIsNew(rs.getInt("IsNew"));
				item.setIsScanned(rs.getInt("IsScanned"));
				item.setIsCovered(rs.getInt("IsCovered"));
				item.setIsComply(rs.getInt("IsComply"));
				item.setStart(rs.getInt("StartNumber"));
				item.setEnd(rs.getInt("EndNumber"));
				item.setColumnDateName(rs.getString("ColumnDateName"));
				item.setIDName(rs.getString("IDName"));
				item.setTimePeriod(rs.getString("TimePeriod"));
				item.setViewName(rs.getString("ViewName"));
				item.setTypeName(rs.getString("TypeName"));
				item.setTypeOperation(rs.getString("TypeOperation"));
				item.setTypeStart(rs.getDouble("TypeStart"));
				item.setTypeEnd(rs.getDouble("TypeEnd"));

				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});

	}

	////////////////////////////////////////////////////////////////////////////////////////////
	public List<AllRadarData> getRadarData(int id) {
		String query = " Select * from AllInfo where ViewID = ?";
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);

			}

			@Override
			public AllRadarData populate(ResultSet rs) throws SQLException {

				AllRadarData item = new AllRadarData();
				item.setViewID(rs.getInt("ViewID"));
				item.setColorType(rs.getString("ColorType"));
				item.setIsNew(rs.getInt("IsNew"));
				item.setIsScanned(rs.getInt("IsScanned"));
				item.setIsCovered(rs.getInt("IsCovered"));
				item.setIsComply(rs.getInt("IsComply"));
				item.setStart(rs.getInt("StartNumber"));
				item.setEnd(rs.getInt("EndNumber"));
				item.setColumnDateName(rs.getString("ColumnDateName"));
				item.setIDName(rs.getString("IDName"));
				item.setTimePeriod(rs.getString("TimePeriod"));
				item.setViewName(rs.getString("ViewName"));
				item.setTypeName(rs.getString("TypeName"));
				item.setTypeOperation(rs.getString("TypeOperation"));
				item.setTypeStart(rs.getDouble("TypeStart"));
				item.setTypeEnd(rs.getDouble("TypeEnd"));

				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});

	}

	////////////////////////////////////////////////////////////////////////////////////////////
	public List<AllRadarData> getRadarData() {
		String query = " Select * from AllInfo ";
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {

			}

			@Override
			public AllRadarData populate(ResultSet rs) throws SQLException {

				AllRadarData item = new AllRadarData();
				item.setViewID(rs.getInt("ViewID"));
				item.setColorType(rs.getString("ColorType"));
				item.setIsNew(rs.getInt("IsNew"));
				item.setIsScanned(rs.getInt("IsScanned"));
				item.setIsCovered(rs.getInt("IsCovered"));
				item.setIsComply(rs.getInt("IsComply"));
				item.setStart(rs.getInt("StartNumber"));
				item.setEnd(rs.getInt("EndNumber"));
				item.setColumnDateName(rs.getString("ColumnDateName"));
				item.setIDName(rs.getString("IDName"));
				item.setTimePeriod(rs.getString("TimePeriod"));
				item.setViewName(rs.getString("ViewName"));
				item.setTypeName(rs.getString("TypeName"));
				item.setTypeOperation(rs.getString("TypeOperation"));
				item.setTypeStart(rs.getDouble("TypeStart"));
				item.setTypeEnd(rs.getDouble("TypeEnd"));

				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});

	}

	/////////////////////////////////////////////////////////////////////
	public List<ViewInfo> getCoveragePerWeek(int viewId, int subViewId, String country, String type, String viewName,
			String date) {
		QeuryBilder com = new QeuryBilder();
		final String query = com.getDataPerWeek(viewId, type, viewName, date);

		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {

				if (query.contains("?,?,?,?")) {
					final int groups = 5;
					for (int i = 0, v1 = 1, v2 = 2, v3 = 3,
							v4 = 4; i < groups; i++, v1 += 4, v2 += 4, v3 += 4, v4 += 4) {
						ps.setInt(v1, viewId);
						ps.setInt(v2, subViewId);
						ps.setString(v3, country);
						ps.setString(v4, date);
					}
				}
			}

			@Override
			public ViewInfo populate(ResultSet rs) throws SQLException {
				ViewInfo item = new ViewInfo();
				item.setType(rs.getString("Type"));
				item.setGreenExist(rs.getLong("GreenExist"));
				item.setGreenNew(rs.getLong("GreenNew"));
				item.setOrangeExist(rs.getLong("OrangeExist"));
				item.setOrangeNew(rs.getLong("OrangeNew"));
				item.setRedExist(rs.getLong("RedExist"));
				item.setRedNew(rs.getLong("RedNew"));
				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});
	}
	
	/////////////////////////////////////////////////////////////////////
	public List<ViewInfo> getCoveragePerWeekCache(int viewId, int subViewId, String country, String type, String viewName,
			String date) {
		QeuryBilder com = new QeuryBilder();
		final String query = com.getDataPerWeekCache(viewId, type, viewName, date);

		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {

				if (query.contains("?,?,?,?")) {
					final int groups = 5;
					for (int i = 0, v1 = 1, v2 = 2, v3 = 3,
							v4 = 4; i < groups; i++, v1 += 4, v2 += 4, v3 += 4, v4 += 4) {
						ps.setInt(v1, viewId);
						ps.setInt(v2, subViewId);
						ps.setString(v3, country);
						ps.setString(v4, date);
					}
				}
			}

			@Override
			public ViewInfo populate(ResultSet rs) throws SQLException {
				ViewInfo item = new ViewInfo();
				item.setType(rs.getString("Type"));
				item.setGreenExist(rs.getLong("GreenExist"));
				item.setGreenNew(rs.getLong("GreenNew"));
				item.setOrangeExist(rs.getLong("OrangeExist"));
				item.setOrangeNew(rs.getLong("OrangeNew"));
				item.setRedExist(rs.getLong("RedExist"));
				item.setRedNew(rs.getLong("RedNew"));
				item.setCountry(rs.getString("LocationCode"));
				item.setSupViewID(rs.getInt("SubviewID"));
				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});
	}
	/////////////////////////////////////////////////////////////////////
	public List<ViewInfo> getComplyCirclePerScore(int viewId, int subViewId, String country, String type, String viewName, String date) {
	    QeuryBilder com = new QeuryBilder();
	    final String query = com.getDataPerScore(viewId, type, viewName);
	    
	    return getList(new JKFinder() {
	        @Override
	        public void setParamters(PreparedStatement ps) throws SQLException {
	            if (query.contains("?,?,?,?")) {
	                final int groups = 2;
	                for (int i = 0, v1 = 1, v2 = 2, v3 = 3, v4 = 4; i < groups; i++, v1 += 4, v2 += 4, v3 += 4, v4 += 4) {
	                    ps.setInt(v1, viewId);
	                    ps.setInt(v2, subViewId);
	                    ps.setString(v3, country);
	                    ps.setString(v4, date);
	                }
	            }
	        }

	        @Override
	        public ViewInfo populate(ResultSet rs) throws SQLException {
	            ViewInfo item = new ViewInfo();
	            item.setType(rs.getString("Type"));
	            item.setGreenExist(rs.getLong("GreenExist"));
	            item.setGreenNew(rs.getLong("GreenNew"));
	            item.setOrangeExist(rs.getLong("OrangeExist"));
	            item.setOrangeNew(rs.getLong("OrangeNew"));
	            return item;
	        }

	        @Override
	        public String getQuery() {
	            return query;
	        }
	    });
	}

	////////////////////////////////////////////////
	public List<ViewInfo> getComplyCirclePerScoreCache(int viewId, int subViewId, String country, String type, String viewName, String date) {
	    QeuryBilder com = new QeuryBilder();
	    final String query = com.getDataPerScoreCache(viewId, type, viewName);
	    
	    return getList(new JKFinder() {
	        @Override
	        public void setParamters(PreparedStatement ps) throws SQLException {
	            if (query.contains("?,?,?,?")) {
	                final int groups = 2;
	                for (int i = 0, v1 = 1, v2 = 2, v3 = 3, v4 = 4; i < groups; i++, v1 += 4, v2 += 4, v3 += 4, v4 += 4) {
	                    ps.setInt(v1, viewId);
	                    ps.setInt(v2, subViewId);
	                    ps.setString(v3, country);
	                    ps.setString(v4, date);
	                }
	            }
	        }

	        @Override
	        public ViewInfo populate(ResultSet rs) throws SQLException {
	            ViewInfo item = new ViewInfo();
	            item.setType(rs.getString("Type"));
	            item.setGreenExist(rs.getLong("GreenExist"));
	            item.setGreenNew(rs.getLong("GreenNew"));
	            item.setOrangeExist(rs.getLong("OrangeExist"));
	            item.setOrangeNew(rs.getLong("OrangeNew"));
				item.setCountry(rs.getString("LocationCode"));
				item.setSupViewID(rs.getInt("SubviewID"));
	            return item;
	        }

	        @Override
	        public String getQuery() {
	            return query;
	        }
	    });
	}

	////////////////////////////////////////////////
	public List<ViewInfo> getHeaderData() {
		String query = "";
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
			}

			@Override
			public ViewInfo populate(ResultSet rs) throws SQLException {
				ViewInfo item = new ViewInfo();
				item.setId(rs.getInt("ControlID"));
				item.setSupViewID(rs.getInt("SubviewID"));
				item.setCovFirst(rs.getInt("covFirst"));
				item.setCovSecond(rs.getInt("covSecound"));
				item.setCompFirst(rs.getInt("compFirst"));
				item.setCompSecond(rs.getInt("compSecound"));
				item.setCountry(rs.getString("Country"));
				return item;
			}

			@Override
			public String getQuery() {
				return JKIOUtil.getSqlFile("hedar-data.sql");
			}
		});
	}

	//////////////////////////////////////////////////
	public List<ComplianceInfo> getComplianceExecludedList(int viewId, int subViewId, String country) {
		String query = "select * from ISACWEB.[dbo].[External_Cmon_ExeludedComp](?,?,?)";
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, viewId);
				ps.setInt(2, subViewId);
				ps.setString(3, country);
			}

			@Override
			public ComplianceInfo populate(ResultSet rs) throws SQLException {
				ComplianceInfo item = new ComplianceInfo();
				item.setPacketId(rs.getInt("PacketID"));
				item.setHostName(rs.getString("HostName"));
				item.setDomain(rs.getString("Domain"));
				item.setIp(rs.getString("IPAddress"));
				item.setCountry(rs.getString("LocationName"));
				item.setAssetTypeName(rs.getString("AssetTypeName"));
				item.setVersionName(rs.getString("VersionName"));
				item.setInsertDate(rs.getDate("InsertDate"));
				item.setScore(rs.getDouble("Score"));
				item.setIsNew(rs.getInt("IsNew"));
				item.setIsLaptop(rs.getInt("IsLaptop"));
				item.setCreateDate(rs.getDate("CreateDate"));
				item.setLastLogon(rs.getDate("LastLogon"));
				item.setOUName(rs.getString("OUName"));
				item.setIsComply(rs.getInt("IsComply"));
				item.setIsCoveredByControl(rs.getInt("IsCoveredByControl"));
				item.setIsManualCovered(rs.getInt("IsManualCovered"));
				item.setExtraValue(rs.getString("ExtraValue"));
				item.setEvaluationDate(rs.getDate("EvaluationDate"));
				item.setVersionParentName(rs.getString("VersionParentName"));
				item.setApplicationName(rs.getString("ApplicationName"));
				item.setExpireInDays(rs.getInt("ExpireInDays"));
				item.setSubViewId(rs.getInt("subViewId"));
				item.setLocationCode(rs.getString("locationCode"));
				item.setExcludedReason(rs.getString("ExcludeReason"));
				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});
	}

////////////////////////////////////////////
	public List<TotalAssetsHistory> getTotalAssetAndTotalCoverdHist(String oneMonth, String twoMonth, String country,
			int viewId, int subViewId) {

		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, viewId);
				ps.setInt(2, -1);
				ps.setString(3, "");
				ps.setString(4, oneMonth);
				ps.setInt(5, viewId);
				ps.setInt(6, -1);
				ps.setString(7, "");
				ps.setString(8, twoMonth);

			}

			@Override
			public TotalAssetsHistory populate(ResultSet rs) throws SQLException {
				TotalAssetsHistory item = new TotalAssetsHistory();
				item.setSubviewId(rs.getInt("SubviewID"));
				item.setLocationCode(rs.getString("LocationCode"));
				item.setTotalCovered(rs.getLong("TotalCovered"));
				item.setTotalCount(rs.getLong("TotalCount"));
				item.setLabel(rs.getString("Label"));
				return item;
			}

			@Override
			public String getQuery() {
				return JKIOUtil.getSqlFile("coverage-total-count.sql");
			}
		});
	}

	////////////////////////////////////////////////////////////////////////////////
	public List<TotalAssetsHistory> getTotalAssetAndTotalCoverd(String month, String country, int viewId,
			int subViewId) {
		String query = JKIOUtil.getSqlFile("total-assets-today.sql");
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, viewId);
				ps.setInt(2, subViewId);
				ps.setString(3, "all".equalsIgnoreCase(country) ? "" : country);
				ps.setString(4, month);
			}

			@Override
			public TotalAssetsHistory populate(ResultSet rs) throws SQLException {
				TotalAssetsHistory item = new TotalAssetsHistory();
				item.setTotalCovered(rs.getLong("TotalCovered"));
				item.setTotalCount(rs.getLong("TotalAssets"));
				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});
	}

	////////////////////////////////////////////////////////////////////////////////
	public List<Date> getLastDate() {
		String query = "SELECT TOP (1) CAST(SyncDate AS DATE) as SyncDate FROM Service_SyncMain ORDER BY SyncID DESC";
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
			}

			@Override
			public Date populate(ResultSet rs) throws SQLException {
				Date item = new Date();
				return rs.getDate("SyncDate");
			}

			@Override
			public String getQuery() {
				return query;
			}
		});
	}

///////////////////////////////////////////////////////

	public String getCycleDate() {
		if (date == null) {
			date = executeQueryAsString("select dbo.[MSDate]()").substring(0, 19);
		}
		return date;
	}

	public Pair<Double, Double> getComplianceTodayThresholds(int viewId, int subViewId) {
		String query = "select * from ISACWEB.dbo.[GetCmonCoverageThreshold](?,?)";
		List<Pair<Double, Double>> list = getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, viewId);
				ps.setInt(2, subViewId);

			}

			@Override
			public Pair<Integer, Integer> populate(ResultSet rs) throws SQLException {
				Pair<Integer, Integer> item = new Pair<Integer, Integer>();
				item.setFirst(rs.getInt("CriticalThreshold"));
				item.setSecond(rs.getInt("WarningThreshold"));
				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});

		if (list == null)
			return new Pair<Double, Double>(0.0, 0.0);
		if (list.size() == 0)
			return new Pair<Double, Double>(0.0, 0.0);

		return list.get(0);

	}

	public Date getBulkScanDate(int viewId, int subViewId, String country, int isNew) {
		return (Date) exeuteSingleOutputQuery("select dbo.[Cmon_BulkScanDate](?,?,?,?)", viewId, subViewId, country,
				isNew);
	}

	public static void main(String args[]) {
		DataAccess a = new DataAccess();
	}

//////////////////////////////////////////////////////////
	public List<CountryLeftCardData> getCountryLeftCardData(String countryName, int country, int viewId,
			String viewName) {
		String query;
		String baseQuery = "declare @syncid int set @syncid = dbo.msid() ";

		String condition = "";
		if (viewName.equalsIgnoreCase("WSUS - Servers")) {
			condition = " where VersionParentName like '%Server%'";
		} else if (viewName.equalsIgnoreCase("WSUS - Endpoints")) {
			condition = " where VersionParentName not like '%Server%'";
		}

		String procedure = countryName.equalsIgnoreCase("") ? "External_Cmon_Gap_Select_ALL(?,?,@syncid)"
				: "External_Cmon_Gap_Select_By_Location(?,?,?,@syncid)";

		query = baseQuery + " select * from ISACWEB.dbo." + procedure + condition;

		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				if (countryName.equalsIgnoreCase("")) {
					ps.setInt(1, 1);
					ps.setInt(2, viewId);
				} else {
					ps.setInt(1, 1);
					ps.setInt(2, country);
					ps.setInt(3, viewId);
				}
			}

			@Override
			public CountryLeftCardData populate(ResultSet rs) throws SQLException {
				CountryLeftCardData item = new CountryLeftCardData();
				if (countryName.equalsIgnoreCase("")) {
					item = populateAllCountryExport(rs, item);
				} else {
					item = populateJordanCountryExport(rs, item);
				}
				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});
	}

	private CountryLeftCardData populateJordanCountryExport(ResultSet rs, CountryLeftCardData item)
			throws SQLException {
		item.setAssetTypeName(rs.getString("AssetTypeName"));
		item.setLocationName(rs.getString("locationName"));
		item.setHostName(rs.getString("HostName"));
		item.setDomain(rs.getString("Domain"));
		item.setVersionParentName(rs.getString("VersionParentName"));
		item.setIsLaptop(rs.getInt("IsLaptop"));
		item.setIpAddress(rs.getString("IPAddress"));
		item.setAlternativeIpAddress(rs.getString("AlternativeIpAddress"));
		item.setCreateDate(rs.getDate("CreateDate"));
		item.setLastLogon(rs.getDate("LastLogon"));
		item.setOutOfCurrentGap(rs.getString("OutOfCurrentGap"));
		item.setCriticalityName(rs.getString("CriticalityName"));
		item.setOwnerName(rs.getString("OwnerName"));
		item.setPhysicalName(rs.getString("PhysicalName"));
		item.setApplicationName(rs.getString("ApplicationName"));
		item.setInstalled(rs.getString("Installed"));
		item.setIsCoveredByControl(rs.getInt("IsCoveredByControl"));
		item.setIsManualCovered(rs.getInt("IsManualCovered"));
		item.setIsComply(rs.getInt("IsComply"));
		item.setControlValue(rs.getString("ControlValue"));
		item.setEvaluationDate(rs.getDate("EvaluationDate"));
		item.setExpireInDays(rs.getInt("ExpireInDays"));
		item.setOuName(rs.getString("OUName"));
		item.setSource(rs.getString("Source"));
		item.setIsActive(rs.getInt("IsActive"));
		item.setSourceName(rs.getString("SourceName"));
		item.setJan(rs.getString("jan"));
		item.setFeb(rs.getString("Feb"));
		item.setMar(rs.getString("Mar"));
		item.setApr(rs.getString("Apr"));
		item.setMay(rs.getString("May"));
		item.setJun(rs.getString("Jun"));
		item.setJul(rs.getString("Jul"));
		item.setAug(rs.getString("Aug"));
		item.setSep(rs.getString("Sep"));
		item.setOct(rs.getString("Oct"));
		item.setNov(rs.getString("Nov"));
		item.setDec(rs.getString("Dec"));
		item.setLastCoveredDate(rs.getDate("LastCoveredDate"));
		item.setCoveredSinceDays(rs.getInt("CoveredSinceDays"));
		item.setLastComplyDate(rs.getDate("LastComplyDate"));
		item.setLastComplyScore(rs.getString("LastComplyScore"));
		item.setComplySinceDays(rs.getInt("ComplySinceDays"));
		item.setHighestComplyDate(rs.getDate("HighestComplyDate"));
		item.setHighestComplyScore(rs.getString("HighestComplyScore"));
		item.setLowestComplyDate(rs.getDate("LowestComplyDate"));
		item.setLowestComplyScore(rs.getString("LowestComplyScore"));
		item.setServerUptime(rs.getInt("ServerUptime"));
		item.setServerRebootDate(rs.getDate("ServerRebootDate"));

		return item;
	}

	private CountryLeftCardData populateAllCountryExport(ResultSet rs, CountryLeftCardData item) throws SQLException {
		item.setAssetTypeName(rs.getString("AssetTypeName"));
		item.setLocationName(rs.getString("locationName"));
		item.setAssestTypeID(rs.getInt("AssetTypeID"));
		item.setHostName(rs.getString("HostName"));
		item.setDomain(rs.getString("Domain"));
		item.setVersionParentName(rs.getString("VersionParentName"));
		item.setIsLaptop(rs.getInt("IsLaptop"));
		item.setIpAddress(rs.getString("IPAddress"));
		item.setAlternativeIpAddress(rs.getString("AlternativeIpAddress"));
		item.setCreateDate(rs.getDate("CreateDate"));
		item.setLastLogon(rs.getDate("LastLogon"));
		item.setOutOfCurrentGap(rs.getString("OutOfCurrentGap"));
		item.setCriticalityName(rs.getString("CriticalityName"));
		item.setOwnerName(rs.getString("OwnerName"));
		item.setPhysicalName(rs.getString("PhysicalName"));
		item.setApplicationName(rs.getString("ApplicationName"));
		item.setInstalled(rs.getString("Installed"));
		item.setIsCoveredByControl(rs.getInt("IsCoveredByControl"));
		item.setIsManualCovered(rs.getInt("IsManualCovered"));
		item.setIsComply(rs.getInt("IsComply"));
		item.setControlValue(rs.getString("ControlValue"));
		item.setEvaluationDate(rs.getDate("EvaluationDate"));
		item.setOuName(rs.getString("OUName"));
		item.setSource(rs.getString("Source"));
		item.setLastLogonUser(rs.getString("LastLogonUser"));
		item.setIsActive(rs.getInt("IsActive"));
		item.setOwnerTeam(rs.getString("OwnerTeam"));
		item.setJan(rs.getString("jan"));
		item.setFeb(rs.getString("Feb"));
		item.setMar(rs.getString("Mar"));
		item.setApr(rs.getString("Apr"));
		item.setMay(rs.getString("May"));
		item.setJun(rs.getString("Jun"));
		item.setJul(rs.getString("Jul"));
		item.setAug(rs.getString("Aug"));
		item.setSep(rs.getString("Sep"));
		item.setOct(rs.getString("Oct"));
		item.setNov(rs.getString("Nov"));
		item.setDec(rs.getString("Dec"));
		item.setLastCoveredDate(rs.getDate("LastCoveredDate"));
		item.setCoveredSinceDays(rs.getInt("CoveredSinceDays"));
		item.setLastComplyDate(rs.getDate("LastComplyDate"));
		item.setLastComplyScore(rs.getString("LastComplyScore"));
		item.setComplySinceDays(rs.getInt("ComplySinceDays"));
		item.setHighestComplyDate(rs.getDate("HighestComplyDate"));
		item.setHighestComplyScore(rs.getString("HighestComplyScore"));
		item.setLowestComplyDate(rs.getDate("LowestComplyDate"));
		item.setLowestComplyScore(rs.getString("LowestComplyScore"));
		item.setServerUptime(rs.getInt("ServerUptime"));
		item.setServerRebootDate(rs.getDate("ServerRebootDate"));

		return item;
	}

///////////////////////////////////////////////////////
	public List<Views> getViews() {
		String query = "select * from ISACWEB.dbo.Cmon_Views_PreProd";
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
			}

			@Override
			public Views populate(ResultSet rs) throws SQLException {
				Views item = new Views();
				item.setId(rs.getInt(1));
				item.setName(rs.getString(2));
				return item;
			}

			@Override
			public String getQuery() {
				return query;
			}
		});
	}

////////////////////////////////////////////
	public List<Views> getViews(String username) {

		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setString(1, username);
				ps.setString(2, username);

			}

			@Override
			public Views populate(ResultSet rs) throws SQLException {
				Views item = new Views();
				item.setId(rs.getInt(1));
				item.setName(rs.getString(2));
				item.setDomain(rs.getString(3));
				item.setCountry(rs.getString(4));
				return item;
			}

			@Override
			public String getQuery() {
				return JKIOUtil.getSqlFile("view-preprod.sql");
			}
		});
	}

////////////////////////////////////////////
	public List<Countres> getCountry(String username) {

		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setString(1, username);

			}

			@Override
			public Countres populate(ResultSet rs) throws SQLException {
				Countres item = new Countres();
				item.setCountry(rs.getString(1));
				return item;
			}

			@Override
			public String getQuery() {
				return JKIOUtil.getSqlFile("country-access.sql");
			}
		});
	}

//////////////////////////////////////////////
	public List<Reports> getReports(String userName) {
		String query = "select br.ReportID,br.ReportName,br.ReportDescription,br.Query from [ISACWeb].[dbo].SecurityUsers su\r\n"
				+ "inner join [ISACWeb].[dbo].SecurityUserGroups sug on su.UserID = sug.UserID\r\n"
				+ "inner join [ISACWeb].[dbo].SecurityGroupsBuiltInReports sgbr on sgbr.SecurityGroupID = sug.SecurityGroupID\r\n"
				+ "inner join [ISACWeb].[dbo].BuiltInReports br on br.ReportID = sgbr.ReportID\r\n"
				+ "where UserName = ?";
		return getList(new JKFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setString(1, userName);
			}

			@Override
			public Reports populate(ResultSet rs) throws SQLException {

				Reports item = new Reports();
				item.setReportId(rs.getInt("ReportID"));
				item.setReportName(rs.getString("ReportName"));
				item.setReportDesc(rs.getString("ReportDescription"));
				item.setQuery(rs.getString("Query"));
				return item;

			}

			@Override
			public String getQuery() {
				return query;
			}
		});

	}

	public List<List<Object>> exQuery(String queryExcel) {
		String query = queryExcel;
		return getList(new JKFinder() {
			int i = 0;
			
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
			}
			
			@Override
			public List<Object> populate(ResultSet rs) throws SQLException {
				List<Object> row = new Vector<>(); // Main list to hold all rows
				// Get metadata and column count
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				if (i == 0) {
					// Create a list for column names
					for (int i = 1; i <= columnCount; i++) {
						row.add(metaData.getColumnName(i)); // Get column name
					}
				} else {
					// Loop through each column and add the value to the row list
					for (int i = 1; i <= columnCount; i++) {
						Object value = rs.getObject(i); // Get the value for the current column
						row.add(value); // Add the value to the row list
					}

				}
				i++;
				return row; // Return the list of rows
			}

			@Override
			public String getQuery() {
				return query;
			}
		});
	}
}
