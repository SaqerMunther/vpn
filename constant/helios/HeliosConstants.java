package com.arabbank.hdf.digitalbackend.digital.constant.helios;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HeliosConstants {
	// charts
//	public static final Map<String, String> chartsMap = new HashMap<String, String>();
//
//	static {
//		chartsMap.put("calls", "EAB Dashboard - Calls LineChart");
//		chartsMap.put("loginToDashboard", "EAB Dashboard - Login to Dashboard");
//		chartsMap.put("accountINQ", "EAB Dashboard - Account Inquiry LineChart");
//		chartsMap.put("cards", "EAB Dashboard - Cards LineChart");
//		chartsMap.put("transfers", "EAB Dashboard - Transfers LineChart");
//		chartsMap.put("arabiPass", "EAB Dashboard - CIAM LineChart");
//		chartsMap.put("best", "EAB Dashboard - Best");
//		chartsMap.put("worst", "EAB Dashboard - Worst");
//	}
	
	// EAB charts
		public static final Map<String, String> eabChartsMap = new HashMap<String, String>();

		static {
			eabChartsMap.put("calls", "Calls");
			eabChartsMap.put("loginToDashboard", "Login to Dashboard");
			eabChartsMap.put("accountINQ", "Account Inquiry");
			eabChartsMap.put("cards", "Cards");
			eabChartsMap.put("transfers", "Transfers");
			eabChartsMap.put("arabiPass", "Ciam");
		}

	// Bar charts
		public static final Map<String, String> barChartsMap = new HashMap<String, String>();

		static {
			barChartsMap.put("productCatalog", "EAB Dashboard - Product Catalog BarChart");
			barChartsMap.put("transfers", "EAB Dashboard - Transfers BarChart");
			barChartsMap.put("registrations", "EAB Dashboard - Registrations");
		}

	// Active users
	public static final Map<String, String> activeUserMap = new LinkedHashMap<String, String>();

	static {
		activeUserMap.put("Android", "EAB Dashboard - Android Users");
		activeUserMap.put("IOS", "EAB Dashboard - IOS Users");
		activeUserMap.put("Last 30 min", "EAB Dashboard - Active users last 30 minutes");
		activeUserMap.put("Today", "EAB Dashboard - Active users Today");
		activeUserMap.put("Yesterday", "EAB Dashboard - Active users Yesterday");
	}

	// login/calls
	public static final Map<String, String> loginCallsrMap = new LinkedHashMap<String, String>();

	static {
		loginCallsrMap.put("Logins-min", "EAB Dashboard - Logins in last 24 hours");
		loginCallsrMap.put("Calls-min", "EAB Dashboard - Calls in last 24 hours");
		loginCallsrMap.put("Logins", "EAB Dashboard - Logins in last 24 hours");
		loginCallsrMap.put("Calls", "EAB Dashboard - Calls in last 24 hours");
		loginCallsrMap.put("Logins-TPH", "EAB Dashboard - Logins TPH");
		loginCallsrMap.put("Calls-TPH", "EAB Dashboard - Calls TPH");
		loginCallsrMap.put("Logins-APT", "Login to Dashboard");
		loginCallsrMap.put("Calls-APT", "Calls");
		loginCallsrMap.put("Logins-S/F", "EAB Dashboard - Logins S-F");
		loginCallsrMap.put("Calls-S/F", "EAB Dashboard - Calls S-F");
	}

	// registration
	public static final Map<String, String> registrationMap = new LinkedHashMap<String, String>();

	static {
		registrationMap.put("INDIVIDUAL", "INDIVIDUAL");
		registrationMap.put("JOINT", "JOINT");
		registrationMap.put("INDIVIDUAL WITH RELATION", "INDIVIDUAL_WITH_RELATION");
		registrationMap.put("COMPANY", "COMPANY");
	}

	
	// HitRegistration
	public static final Map<String, String> hitRegistrationMap = new LinkedHashMap<String, String>();

	static {
		hitRegistrationMap.put("hitRegistrations", "EAB Dashboard - Registration T/B Hits");
	}
	
	// HELIOS charts
	public static final Map<String, String> heliosChartsMap = new HashMap<String, String>();

	static {
		heliosChartsMap.put("calls", "Helios Dashboard- Calls Line Chart");
		heliosChartsMap.put("arabiPass", "Helios Dashboard- Arabi Pass Line Chart");
		heliosChartsMap.put("accountINQ", "Helios Dashboard- Account Inquiry LineChart");
		heliosChartsMap.put("facematch", "Facematch");
		heliosChartsMap.put("bills", "Helios Dashboard - Efawateercom LineChart");
		heliosChartsMap.put("transfers", "Helios Dashboard - Transfers LineChart");
		heliosChartsMap.put("cliq", "Helios Dashboard - CliQ LineChart");
		heliosChartsMap.put("cardsExperience", "Helios Dashboard- Cards ExperienceCards Experience");
		heliosChartsMap.put("loginToDashboard", "Helios Dashboard- Login to Dashboard");
		heliosChartsMap.put("bestLogin", "Helios Dashboard- Login to Dashboard Best");
		heliosChartsMap.put("WorstLogin", "Helios Dashboard- Login to Dashboard Worst");
	}
	
	// HELIOS Events charts
	public static final Map<String, String> heliosEventsChartsMap = new HashMap<String, String>();

	static {
		heliosEventsChartsMap.put("transactionHistEvents", "Helios Dashboard- Transaction Hist Events");
		heliosEventsChartsMap.put("accountEvents", "Helios Dashboard- Account Events");
		heliosEventsChartsMap.put("customersEvents", "Helios Dashboard- Customers Events");
	}
		
	// HELIOS Servers
	public static final Map<String, String> heliosServersMap = new HashMap<String, String>();

	static {
		heliosServersMap.put("ANTHOS1", "ANTHOS1");
		heliosServersMap.put("ANTHOS2", "ANTHOS2");
		heliosServersMap.put("KAFKA", "KAFKA");
		heliosServersMap.put("MONGO", "MONGO");
		heliosServersMap.put("CDC", "CDC");
		heliosServersMap.put("IIB", "IIB");
		heliosServersMap.put("APIGEE", "Helios Dashboard HB - Main");
		heliosServersMap.put("CIAM", "CIAM");
		heliosServersMap.put("AS400", "AS400");
	}
	
	// HELIOS Vitals
		public static final Map<String, List<String>> heliosVitalsMap = new HashMap<String, List<String>>();

		static {
			heliosVitalsMap.put("ANTHOS1", List.of("CPU", "RAM", "PAG", "DISK"));
			heliosVitalsMap.put("ANTHOS2", List.of("CPU", "RAM", "PAG", "DISK"));
			heliosVitalsMap.put("KAFKA", List.of("CPU", "RAM", "PAG", "DISK"));
			heliosVitalsMap.put("MONGO", List.of("CPU", "RAM", "PAG", "DISK"));
			heliosVitalsMap.put("CDC", List.of("CPU", "RAM", "PAG", "DISK"));
			heliosVitalsMap.put("IIB", List.of("CPU", "RAM", "PAG", "DISK"));
			
			heliosVitalsMap.put("DB1", List.of("CPU", "RAM", "PAG", "DISK"));
			heliosVitalsMap.put("DB2", List.of("CPU", "RAM", "PAG", "DISK"));
			heliosVitalsMap.put("CTS", List.of("CPU", "RAM", "PAG", "DISK"));
			heliosVitalsMap.put("DS", List.of("CPU", "RAM", "PAG", "DISK"));
			heliosVitalsMap.put("AM", List.of("CPU", "RAM", "PAG", "DISK"));
			heliosVitalsMap.put("CS", List.of("CPU", "RAM", "PAG", "DISK"));
		}
		
		// HELIOS Vitals IP Service
		public static final Map<String, List<String>> heliosVitalsServiceIPMap = new HashMap<String, List<String>>();

		static {

			heliosVitalsServiceIPMap.put("CTS", List.of("10.0.9.70", "10.0.9.71", "10.0.9.72"));
			heliosVitalsServiceIPMap.put("DS", List.of("10.0.9.37", "10.0.9.38", "10.0.9.39"));
		}
		
		// HELIOS Vitals
		public static final Map<String, List<String>> heliosServerServicesMap = new HashMap<String, List<String>>();

		static {
			heliosServerServicesMap.put("ANTHOS1", List.of("ACC", "TRX"));
			heliosServerServicesMap.put("ANTHOS2", List.of("ACC", "TRX"));
			heliosServerServicesMap.put("APIGEE", List.of("ARABI", "INTERNAL", "FACE", "APP"));
			heliosServerServicesMap.put("KAFKA", List.of());
			heliosServerServicesMap.put("MONGO", List.of());
			heliosServerServicesMap.put("CDC", List.of());
			heliosServerServicesMap.put("IIB", List.of("SMS", "TXN", "CUSTOMER", "TRX", "ECM"));
			heliosServerServicesMap.put("CIAM", List.of("DB1", "DB2", "CTS", "DS", "AM", "CS"));
			heliosServerServicesMap.put("AS400", List.of("ITA", "CP", "ABIHLSSBS"));
		}
		
		// HELIOS DB NAME
		public static final Map<String, String> heliosServerServicesDBMap = new HashMap<String, String>();

		static {
			heliosServerServicesDBMap.put("ARABI", "Helios Dashboard HB - Arabi Pass");
			heliosServerServicesDBMap.put("INTERNAL", "Helios Dashboard HB - Internal SC");
			heliosServerServicesDBMap.put("FACE", "Helios Dashboard HB - FACE");
			heliosServerServicesDBMap.put("APP", "Helios Dashboard HB - Validate APP");
		}
		
		public static final String TOTALCOUNT = "totalCount";
		public static final String LATENCY = "latency";
		// HELIOS TRX
		public static final Map<String, List<String>> heliosServerTRXMap = new HashMap<String, List<String>>();

		static {
			heliosServerTRXMap.put("", List.of("totalCount", "latency", "failure"));
			heliosServerTRXMap.put("ABIHLSSBS", List.of("JOBS", "status", TOTALCOUNT, LATENCY, "failure"));
		}
		
		// HELIOS Active users
		public static final Map<String, String> heliosActiveUserMap = new LinkedHashMap<String, String>();

		static {
			heliosActiveUserMap.put("Android", "Helios Dashboard- Android");
			heliosActiveUserMap.put("IOS", "Helios Dashboard- IOS");
			heliosActiveUserMap.put("Last 30 min", "Helios Dashboard- Active users last 30 minutes");
			heliosActiveUserMap.put("Today", "Helios Dashboard- Active users Today");
			heliosActiveUserMap.put("Yesterday", "Helios Dashboard- Active users Yesterday");
		}
		
		// Helios login/calls
		public static final Map<String, String> HeliosloginCallsrMap = new LinkedHashMap<String, String>();

		static {
			HeliosloginCallsrMap.put("Logins-min", "Helios Dashboard- Logins in last 24 hours");
			HeliosloginCallsrMap.put("Calls-min", "Helios Dashboard- Calls in last 24 hours");
			HeliosloginCallsrMap.put("Logins", "Helios Dashboard- Logins in last 24 hours");
			HeliosloginCallsrMap.put("Calls", "Helios Dashboard- Calls in last 24 hours");
			HeliosloginCallsrMap.put("Logins-TPH", "Helios Dashboard- Login TPH");
			HeliosloginCallsrMap.put("Calls-TPH", "Helios Dashboard- Calls TPH");
			HeliosloginCallsrMap.put("Logins-APT", "Helios Dashboard- Login to Dashboard");
			HeliosloginCallsrMap.put("Calls-APT", "Helios Dashboard- Calls APT");
			HeliosloginCallsrMap.put("Logins-S/F", "Helios Dashboard- Logins S-F");
			HeliosloginCallsrMap.put("Calls-S/F", "Helios Dashboard- Calls S-F");
		}
		
		// HELIOS Cherries
		public static final Map<String, String> heliosCherriessMap = new HashMap<String, String>();

		static {
			heliosCherriessMap.put("APIGEE-CIAM", "APIGEE-CIAM");
			heliosCherriessMap.put("APIGEE-ANTHOS1", "APIGEE-ANTHOS1");
			heliosCherriessMap.put("APIGEE-ANTHOS2", "APIGEE-ANTHOS2");
			heliosCherriessMap.put("ANTHOS1-IIB", "ANTHOS1-IIB");
			heliosCherriessMap.put("ANTHOS1-KAFKA", "ANTHOS1-KAFKA");
			heliosCherriessMap.put("ANTHOS1-MDB", "ANTHOS1-MDB");
			heliosCherriessMap.put("ANTHOS2-IIB", "ANTHOS2-IIB");
			heliosCherriessMap.put("ANTHOS2-KAFKA", "ANTHOS2-KAFKA");
			heliosCherriessMap.put("ANTHOS2-MDB", "ANTHOS2-MDB");
			heliosCherriessMap.put("IIB-AS400", "IIB-AS400");
			heliosCherriessMap.put("AS400-CDC", "AS400-CDC");
			heliosCherriessMap.put("CDC-KAFKA", "CDC-KAFKA");
			
		}
		
		
		// EAB Cache Name
		public static final String EAB_CHARTS_CACHE_NAME = "eabChartsData";
		public static final String EAB_BARCHARTS_CACHE_NAME = "eabBarChartsData";
		public static final String EAB_CARD_CACHE_NAME = "eabCardData";
		
		public final static List<String> EAB_CARDS_LIST = List.of("activeUsers", "hitregistrations");
		
		public final static List<String> EAB_REGISTRATIONS_CARD = List.of("registrations");
		public final static List<String> EAB_CONNECTION_CARD = List.of("connections");
		
		// Helios Cache Name
		public static final String HELIOS_CHARTS_CACHE_NAME = "heliosChartsData";
		public static final String HELIOS_CONNICTION_CARD_CACHE_NAME = "ConnictionCardData";
		public static final String HELIOS_ACTIVEUSER_CARD_CACHE_NAME = "ActiveUserCardData";
		public static final String HELIOS_SERVER_CACHE_NAME = "ServersData";
		
		public final static List<String> HELIOS_COUNTRY_LIST = List.of("plc", "jordan", "iraq", "morocco");

	//Helios Anthos app name
		public final static List<String> ANTHOS_INQUIRY_LIST = List.of("ab-account-processing-service" , "helios-account-experience-plc-service" , "ab-arabi-juniors-account-service");
		public final static List<String> ANTHOS_TRANSACTIONS_LIST = List.of("digitalbanking-payment-initation-experience", "digitalbanking-internal-payment-order", "digitalbanking-external-payment-order", "ab-ips-payment-initiation-java-ms", "helios-cc-payment-plc-experience");
		
		
		
		
}