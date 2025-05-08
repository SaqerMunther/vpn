package com.arabbank.hdf.digitalbackend.digital.constant.omnify;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.arabbank.hdf.digitalbackend.digital.repository.sqlserver.omnify.OmnifyServiceRepository;

public class OmnifyConstants {
	
	// Omnify charts
	public static final Map<String, String> OMNIFY_CHARTS_MAP = new HashMap<String, String>();

	static {
		OMNIFY_CHARTS_MAP.put("calls", "Omnify Dashboard - Calls");
		OMNIFY_CHARTS_MAP.put("authentication", "Omnify Dashboard - Auth");
		OMNIFY_CHARTS_MAP.put("cardsAsService", "Omnify Dashboard - Cards");
		OMNIFY_CHARTS_MAP.put("kycAsService", "Omnify Dashboard - KYC");
		OMNIFY_CHARTS_MAP.put("kybAsService", "");
		OMNIFY_CHARTS_MAP.put("paymentsAsService", "");
		OMNIFY_CHARTS_MAP.put("loansAsService", "");
		OMNIFY_CHARTS_MAP.put("loyaltyAsService", "");
		OMNIFY_CHARTS_MAP.put("accountsAsService", "");

	}
	
	// Omnify Bar charts
	public static final Map<String, String> OMNIFY_BAR_CHARTS_MAP = new HashMap<String, String>();

	static {
		OMNIFY_BAR_CHARTS_MAP.put("worstService", "");
		OMNIFY_BAR_CHARTS_MAP.put("eod", "");
	}
	
	// Omnify Bar charts Top Companies
	public static final Map<String, String> OMNIFY_TOP_BAR_CHARTS_MAP = new HashMap<String, String>();

	static {
		OMNIFY_TOP_BAR_CHARTS_MAP.put("topCompanies", "");
	}
	
	// Omnify login/calls
	public static final Map<String, String> OMNIFY_CALLS_MAP = new LinkedHashMap<String, String>();

	static {
		OMNIFY_CALLS_MAP.put("Calls-min", "Omnify Dashboard - Calls in last 24 hours");
		OMNIFY_CALLS_MAP.put("Calls", "Omnify Dashboard - Calls in last 24 hours");
		OMNIFY_CALLS_MAP.put("Calls-TPH", "Omnify Dashboard - Calls TPH");
		OMNIFY_CALLS_MAP.put("Calls-APT", "Omnify Dashboard - Calls APT");
		OMNIFY_CALLS_MAP.put("Calls-S/F", "Omnify Dashboard - Calls S-F");
	}
	
	// Omnify Bar charts Top Companies
	public static final Map<String, List<String>> OMNIFY_TOP_COMPANIES = new HashMap<String, List<String>>();

	static {
		OMNIFY_TOP_COMPANIES.put("topCompanies", List.of("KYC", "KYB", "Cards", "Payments", "Loans"));
	}
	
	
	// OMNIFY Active users
	public static final Map<String, String> OMNIFY_PRODUCTS_MAP = new LinkedHashMap<String, String>();

	static {
		OMNIFY_PRODUCTS_MAP.put("KYC", "");
		OMNIFY_PRODUCTS_MAP.put("KYB", "");
		OMNIFY_PRODUCTS_MAP.put("Cards", "");
		OMNIFY_PRODUCTS_MAP.put("Payments", "");
		OMNIFY_PRODUCTS_MAP.put("Loans", "");
	}
	
	// OMNIFY Servers
	public static final Map<String, String> OMNIFY_SERVERS_MAP = new HashMap<String, String>();

	static {
		OMNIFY_SERVERS_MAP.put("Anthos1", "Anthos1");
		OMNIFY_SERVERS_MAP.put("Anthos2", "Anthos2");
		OMNIFY_SERVERS_MAP.put("Kafka", "Kafka");
		OMNIFY_SERVERS_MAP.put("MongoDBNEO", "MongoDBNEO");
		OMNIFY_SERVERS_MAP.put("MongoDBODS", "MongoDBODS");
		OMNIFY_SERVERS_MAP.put("IIB", "IIB");
		OMNIFY_SERVERS_MAP.put("Apigee", "Apigee");
		OMNIFY_SERVERS_MAP.put("Ciam", "Ciam");
		OMNIFY_SERVERS_MAP.put("As400", "As400");
		OMNIFY_SERVERS_MAP.put("Event", "Event");
	}
	
	
	public static final String TOTALCOUNT = "totalCount";
	public static final String LATENCY = "latency";
	// OMNIFY TRX
	public static final Map<String, List<String>> OMNIFY_SERVER_TRX_MAP = new HashMap<String, List<String>>();

	static {
		OMNIFY_SERVER_TRX_MAP.put("", List.of("totalCount", "latency", "failure"));
		OMNIFY_SERVER_TRX_MAP.put("Common", List.of("JOBS", "status", TOTALCOUNT, LATENCY, "failure"));
	}
	
	// OMNIFY Vitals
	public static final Map<String, List<String>> OMNIFY_SERVER_SERVICES_MAP = new HashMap<String, List<String>>();

	static {
		OMNIFY_SERVER_SERVICES_MAP.put("Anthos1", List.of("Customer", "PositionKey", "Dashboard", "AppSettings", "Notifications"));
		OMNIFY_SERVER_SERVICES_MAP.put("Anthos2", List.of("Customer", "PositionKey", "Dashboard", "AppSettings", "Notifications"));
		OMNIFY_SERVER_SERVICES_MAP.put("Apigee", List.of("CIAM", "InternalSC", "Dashboard", "AppSettings", "Notifications"));
		OMNIFY_SERVER_SERVICES_MAP.put("Kafka", List.of());
		OMNIFY_SERVER_SERVICES_MAP.put("MongoDBNEO", List.of());
		OMNIFY_SERVER_SERVICES_MAP.put("MongoDBODS", List.of());
		OMNIFY_SERVER_SERVICES_MAP.put("IIB", List.of("SMS", "CustomerInq", "ODM", "FundTRx", "Bills"));
		OMNIFY_SERVER_SERVICES_MAP.put("Ciam", List.of("SqlDB1", "SqlDB2", "CTSServer", "DSServer", "AMServer", "CSServer"));
		OMNIFY_SERVER_SERVICES_MAP.put("As400", List.of("Common"));
		OMNIFY_SERVER_SERVICES_MAP.put("Event", List.of());
	}
	
	// OMNIFY DB NAME
	public static final Map<String, String> OMNIFY_SERVER_SERVICES_DB_MAP = new HashMap<String, String>();

	static {
		OMNIFY_SERVER_SERVICES_DB_MAP.put("CIAM", "OMNIFY");
		OMNIFY_SERVER_SERVICES_DB_MAP.put("InternalSC", "OMNIFY");
		OMNIFY_SERVER_SERVICES_DB_MAP.put("Dashboard", "OMNIFY");
		OMNIFY_SERVER_SERVICES_DB_MAP.put("AppSettings", "OMNIFY");
		OMNIFY_SERVER_SERVICES_DB_MAP.put("Notifications", "OMNIFY");
	}
	
	// HELIOS Vitals
	public static final Map<String, List<String>> OMNIFY_VITALS_MAP = new HashMap<String, List<String>>();

	static {
		OMNIFY_VITALS_MAP.put("Anthos1", List.of("CPU", "RAM"));
		OMNIFY_VITALS_MAP.put("Anthos2", List.of("CPU", "RAM"));
		OMNIFY_VITALS_MAP.put("Kafka", List.of("CPU", "RAM", "PAG", "DISK"));
		OMNIFY_VITALS_MAP.put("MongoDBNEO", List.of("CPU", "RAM", "PAG", "DISK"));
		OMNIFY_VITALS_MAP.put("MongoDBODS", List.of("CPU", "RAM", "PAG", "DISK"));
		OMNIFY_VITALS_MAP.put("IIB", List.of("CPU", "RAM", "PAG", "DISK"));
		
		OMNIFY_VITALS_MAP.put("SqlDB1", List.of("CPU", "RAM", "PAG", "DISK"));
		OMNIFY_VITALS_MAP.put("SqlDB2", List.of("CPU", "RAM", "PAG", "DISK"));
		OMNIFY_VITALS_MAP.put("CTSServer", List.of("CPU", "RAM", "PAG", "DISK"));
		OMNIFY_VITALS_MAP.put("DSServer", List.of("CPU", "RAM", "PAG", "DISK"));
		OMNIFY_VITALS_MAP.put("AMServer", List.of("CPU", "RAM", "PAG", "DISK"));
		OMNIFY_VITALS_MAP.put("CSServer", List.of("CPU", "RAM", "PAG", "DISK"));
	}
	
	
	// HELIOS Vitals IP Service
	public static final Map<String, List<String>> OMNIFY_VITALS_SERVICE_IP_MAP = new HashMap<String, List<String>>();

	static {

//		OMNIFY_VITALS_SERVICE_IP_MAP.put("CTS", List.of("10.0.9.70", "10.0.9.71", "10.0.9.72"));
//		OMNIFY_VITALS_SERVICE_IP_MAP.put("DS", List.of("10.0.9.37", "10.0.9.38", "10.0.9.39"));
	}
	
	
	// HELIOS Cherries
	public static final Map<String, String> OMNIFY_CHERRIESS_MAP = new HashMap<String, String>();

	static {
		OMNIFY_CHERRIESS_MAP.put("APIGEE-CIAM", "APIGEE-CIAM");
		OMNIFY_CHERRIESS_MAP.put("APIGEE-ANTHOS1", "APIGEE-ANTHOS1");
		OMNIFY_CHERRIESS_MAP.put("APIGEE-ANTHOS2", "APIGEE-ANTHOS2");
		OMNIFY_CHERRIESS_MAP.put("ANTHOS1-IIB", "ANTHOS1-IIB");
		OMNIFY_CHERRIESS_MAP.put("ANTHOS1-KAFKA", "ANTHOS1-KAFKA");
		OMNIFY_CHERRIESS_MAP.put("ANTHOS1-MDBNEO", "ANTHOS1-MDBNEO");
		OMNIFY_CHERRIESS_MAP.put("ANTHOS2-IIB", "ANTHOS2-IIB");
		OMNIFY_CHERRIESS_MAP.put("ANTHOS2-KAFKA", "ANTHOS2-KAFKA");
		OMNIFY_CHERRIESS_MAP.put("ANTHOS2-MDBODS", "ANTHOS2-MDBODS");
		OMNIFY_CHERRIESS_MAP.put("IIB-AS400", "IIB-AS400");
		OMNIFY_CHERRIESS_MAP.put("AS400-EVENT", "AS400-EVENT");
		
	}
	
	// Omnify Cache Name
	public static final String OMNIFY_CHARTS_CACHE_NAME = "omnifyChartsData";
	public static final String OMNIFY_TOP_COMPANIES_CHARTS_CACHE_NAME = "omnifyTopCopmaniesChartsData";
	public static final String OMNIFY_CONNICTION_CARD_CACHE_NAME = "OmnifyConnictionCardData";
	public static final String OMNIFY_PRODUCTS_CARD_CACHE_NAME = "productsCardData";
	public static final String OMNIFY_COMPANY_LIST_CACHE_NAME = "companiesName";
	public static final String OMNIFY_SERVER_CACHE_NAME = "ServersData";
	
	
	public final static List<String> OMNIFY_COMPANY_LIST = List.of("ALL", "AE010000", "portal");
	public static final List<String> OMNIFY_TOP_COMPANIES_CHARTS_LIST_CACHE_NAME = List.of("topCompanies");
}