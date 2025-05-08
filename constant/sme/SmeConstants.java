package com.arabbank.hdf.digitalbackend.digital.constant.sme;

import java.util.*;

public class SmeConstants {
	
	private SmeConstants() {}

	public static final String SMEDIG = "SMEDIG";

	public static final String STABLE = "stable";
	public static final String WARNING = "warning";
	public static final String ERROR = "error";

	// SME Cache Name
	public static final String SME_CHARTS_CACHE_NAME = "smeChartsData";
	public static final List<String> SME_COUNTRY_LIST = List.of("plc","jordan","palestine");

	//SME CHARTS
	public static final Map<String, String> SME_CHARTS_MAP = new LinkedHashMap<>();

	static {
		SME_CHARTS_MAP.put("Calls", "Calls LineChart");
		SME_CHARTS_MAP.put("Profile Retrieval", "Login LineChart");
		SME_CHARTS_MAP.put("Best 10% Login", "SME - Login to Dashboard - Best");
		SME_CHARTS_MAP.put("Worst 10% Login", "SME - Login to Dashboard - Worst");
		SME_CHARTS_MAP.put("Bill Payment", "SME - Bill Payments LineChart");
		SME_CHARTS_MAP.put("Transfers", "SME - Transfers LineChart");
		SME_CHARTS_MAP.put("Account INQ", "Account Inqury LineChart");
		SME_CHARTS_MAP.put("Payment Validation", "Payment Validation");
		SME_CHARTS_MAP.put("Cliq", "CliQ LineChart");
		SME_CHARTS_MAP.put("FaceMatch", "Facematch");
		SME_CHARTS_MAP.put("CIAM", "CIAM");
	}
	
	//SME CHARTS_PS
	public static final Map<String, String> SME_CHARTS_MAP_PS = new LinkedHashMap<>();

	static {
		SME_CHARTS_MAP_PS.put("Calls", "SME PS Dashboard - Calls LineChart");
		SME_CHARTS_MAP_PS.put("Profile Retrieval", "SME PS Dashboard - Login LineChart");
		SME_CHARTS_MAP_PS.put("Best 10% Login", "SME PS Dashboard - Login to Dashboard - Best");
		SME_CHARTS_MAP_PS.put("Worst 10% Login", "SME PS Dashboard - Login to Dashboard - Worst");
		SME_CHARTS_MAP_PS.put("Bill Payment", "SME PS Dashboard - Bill Payments LineChart");
		SME_CHARTS_MAP_PS.put("Transfers", "SME PS Dashboard - Transfers LineChart");
		SME_CHARTS_MAP_PS.put("Account INQ", "SME PS Dashboard - Account Inqury LineChart");
		SME_CHARTS_MAP_PS.put("Payment Validation", "SME PS Dashboard - Payment Validation");
		SME_CHARTS_MAP_PS.put("Cliq", "SME PS Dashboard - CliQ LineChart");
		SME_CHARTS_MAP_PS.put("FaceMatch", "Facematch");
		SME_CHARTS_MAP_PS.put("CIAM", "SME PS Dashboard - Ciam");
	}

	//SME ACTIVE USERS
	public static final Map<String, String> SME_ACTIVE_USER_MAP = new LinkedHashMap();
	public static final Map<String, String> SME_ACTIVE_USER_MAP_PS = new LinkedHashMap();

	static {
		SME_ACTIVE_USER_MAP.put("Android Count", "Android");
		SME_ACTIVE_USER_MAP.put("IOS Count", "IOS");
		SME_ACTIVE_USER_MAP.put("Active users Last 30 minutes", "Last 30 min");
		SME_ACTIVE_USER_MAP.put("Active users Today", "Today");
		SME_ACTIVE_USER_MAP.put("Active users Yesterday", "Yesterday");

		SME_ACTIVE_USER_MAP_PS.put("PS Android Count", "Android");
		SME_ACTIVE_USER_MAP_PS.put("PS IOS Count", "IOS");
		SME_ACTIVE_USER_MAP_PS.put("PS Active users last 30 mins", "Last 30 min");
		SME_ACTIVE_USER_MAP_PS.put("PS Active users Today", "Today");
		SME_ACTIVE_USER_MAP_PS.put("PS Active users Yesterday", "Yesterday");
	}

	//SME REGISTRATIONS
	public static final Map<String, String> SME_REGISTRATIONS_MAP = new LinkedHashMap();
	public static final Map<String, String> SME_REGISTRATIONS_MAP_PS = new LinkedHashMap();

	static {
		SME_REGISTRATIONS_MAP.put("Count:Arabi next COB - Count of Completed leads ", "completedApp");
		SME_REGISTRATIONS_MAP.put("Count:Arabi next COB - Count of Pending leads", "pendingApp");
		SME_REGISTRATIONS_MAP.put("RegisteredCompanies", "companies");
		SME_REGISTRATIONS_MAP.put("RegisteredUsers", "users");
		SME_REGISTRATIONS_MAP_PS.put("PSRegisteredCompanies", "companies");
		SME_REGISTRATIONS_MAP_PS.put("PSRegisteredUsers", "users");
		SME_REGISTRATIONS_MAP.put("PS Completed leads ", "completedApp");
		SME_REGISTRATIONS_MAP.put("PS Count of Pending leads", "pendingApp");
	}


	//SME TRANSACTIONS
	public static final Map<String, String> SME_TRANSACTIONS_MAP = new LinkedHashMap();

	static {
		SME_TRANSACTIONS_MAP.put("TRX_Bank_International", "international");
		SME_TRANSACTIONS_MAP.put("TRX_Bank_Domestic", "domestic");
		SME_TRANSACTIONS_MAP.put("Charge_Holds", "chargePeriod");
		SME_TRANSACTIONS_MAP.put("Reversals_Today", "reversals");
		SME_TRANSACTIONS_MAP.put("Rejections_Today", "rejections");
	}


	// SME Servers
	public static final Map<String, String> SME_SERVER_SERVICES_MAP = new LinkedHashMap<>();

	static {
		SME_SERVER_SERVICES_MAP.put("AS400", "AS400");
		SME_SERVER_SERVICES_MAP.put("CDC", "CDC");
		SME_SERVER_SERVICES_MAP.put("IIB", "IIB");
		SME_SERVER_SERVICES_MAP.put("MONGOMS", "MONGOMS");
		SME_SERVER_SERVICES_MAP.put("MONGOODS", "ODS");
		SME_SERVER_SERVICES_MAP.put("KAFKA", "KAFKA");
		SME_SERVER_SERVICES_MAP.put("APIGEE", "APIGEE");
		SME_SERVER_SERVICES_MAP.put("ANTHOS1", "ANTHOS-1");
		SME_SERVER_SERVICES_MAP.put("ANTHOS2", "ANTHOS-2");
		SME_SERVER_SERVICES_MAP.put("CIAM", "CIAM");
	}
	
	
	public static final String TOTALCOUNT = "totalCount";
	public static final String LATENCY = "latency";
	// SME TRX
	public static final Map<String, List<String>> SME_SERVER_TRX_MAP = new HashMap();

	static {
		SME_SERVER_TRX_MAP.put("", List.of("totalCount", "latency", "failure"));
		SME_SERVER_TRX_MAP.put(SMEDIG, List.of("JOBS", "status", TOTALCOUNT, LATENCY, "failure"));
	}



	// SME SERVER
	public static final Map<String, List<String>> SME_SERVER_MAP = new LinkedHashMap<>();

	static {
		SME_SERVER_MAP.put("CDC", List.of());
		SME_SERVER_MAP.put("IIB", List.of("SMS", "CUSTOMER", "TXN",  "TRX", "ECM"));
		SME_SERVER_MAP.put("MONGOMS", List.of());
		SME_SERVER_MAP.put("MONGOODS", List.of());
		SME_SERVER_MAP.put("AS400", List.of("ITA", SMEDIG, "CP"));
		SME_SERVER_MAP.put("KAFKA", List.of());
		SME_SERVER_MAP.put("APIGEE", List.of("VALIDATE", "INTERNAL", "DIGITAL", "CIAM", "FACE"));
		SME_SERVER_MAP.put("ANTHOS1", List.of("ACC", "TRX"));
		SME_SERVER_MAP.put("ANTHOS2", List.of("ACC", "TRX"));
		SME_SERVER_MAP.put("CIAM", List.of("CS", "DB1", "CTS", "DB2", "AM", "DS"));
	}

	// SME STATISTICS DESCRIPTION (DB)
	public static final Map<String, String> SME_STATISTICS_DESCRIPTION_MAP = new LinkedHashMap<>();

	static {
		//Apigee
		SME_STATISTICS_DESCRIPTION_MAP.put("VALIDATE", "SME Heartbeat Chart - Validate App");
		SME_STATISTICS_DESCRIPTION_MAP.put("INTERNAL", "SME Heartbeat Chart - Internal SC");
		SME_STATISTICS_DESCRIPTION_MAP.put("DIGITAL", "SME Heartbeat Chart - Digital Profile");
		SME_STATISTICS_DESCRIPTION_MAP.put("CIAM", "SME Heartbeat Chart - CIAM");
		SME_STATISTICS_DESCRIPTION_MAP.put("FACE", "SME Heartbeat Chart - FaceMatch");

		//Anthos
		SME_STATISTICS_DESCRIPTION_MAP.put("ACC", "ADO Inquiry");
		SME_STATISTICS_DESCRIPTION_MAP.put("TRX", "Transactions");

		//IIB
		SME_STATISTICS_DESCRIPTION_MAP.put("SMS", "SMS");
		SME_STATISTICS_DESCRIPTION_MAP.put("CUSTOMER", "CUSTOMER");
		SME_STATISTICS_DESCRIPTION_MAP.put("TXN", "TXN");
		SME_STATISTICS_DESCRIPTION_MAP.put("TRX", "TRANSACTIONS");
		SME_STATISTICS_DESCRIPTION_MAP.put("ECM", "ECM");

		//AS400
		SME_STATISTICS_DESCRIPTION_MAP.put("ITA", "SME HB - AS400 ITA");
		SME_STATISTICS_DESCRIPTION_MAP.put("CP", "SME HB - AS400 CP");
		SME_STATISTICS_DESCRIPTION_MAP.put(SMEDIG, "ABISMESBS");
	}

	// SME STATISTICS THRESHOLD LABEL
	public static final Map<String, String> SME_STATISTICS_THRESHOLD_LABEL_MAP = new LinkedHashMap<>();
	static {
		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("APIGEE-SME Heartbeat Chart - All", "APIGEE");
		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("APIGEE-SME Heartbeat Chart - Validate App", "APIGEE_VALID");
		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("APIGEE-SME Heartbeat Chart - Internal SC", "APIGEE_SC");
		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("APIGEE-SME Heartbeat Chart - Digital Profile", "APIGEE_DIGITAL");
		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("APIGEE-SME Heartbeat Chart - CIAM", "APIGEE_CIAM");
		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("APIGEE-SME Heartbeat Chart - FaceMatch", "APIGEE_FAC");

		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("ANTHOS1-MAIN", "Anthos_1");
		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("ANTHOS1-ADO Inquiry", "ANTHOS_1_ADO");
		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("ANTHOS1-Transactions", "ANTHOS_1_Transactions");

		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("ANTHOS2-MAIN", "Anthos_1");
		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("ANTHOS2-ADO Inquiry", "ANTHOS_1_ADO");
		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("ANTHOS2-Transactions", "ANTHOS_1_Transactions");

		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("MONGOODS-ODS", "MongoDB_ODS");
		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("MONGOMS-MONGOMS", "MongoDB_MS");

		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("AS400-SME HB - AS400", "AS400");
		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("AS400-ITA", "ITA");
		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("AS400-CP", "CP");
		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("AS400-ABISMESBS", "8MECIG");

		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("IIB-MAIN", "MW");
		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("IIB-TRX", "TRANSACTIONS");
		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("IIB-TXN", "Txn_Tlistory");
		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("IIB-ECM", "MW_ECM");
		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("IIB-CUSTOMER", "CUSTOMER");
		SME_STATISTICS_THRESHOLD_LABEL_MAP.put("IIB-SMS", "MW_SMS");

	}

	public static final String SME_SERVER_CACHE_NAME = "SmeServersData";
	
	// SME Vitals
	public static final Map<String, List<String>> SME_VITALS_MAP = new LinkedHashMap<>();

	static {
		SME_VITALS_MAP.put("ANTHOS1", List.of("CPU", "RAM"));
		SME_VITALS_MAP.put("ANTHOS2", List.of("CPU", "RAM"));
		SME_VITALS_MAP.put("KAFKA", List.of("CPU", "RAM", "PAG", "DISK"));
		SME_VITALS_MAP.put("MONGOMS", List.of("CPU", "RAM", "PAG", "DISK"));
		SME_VITALS_MAP.put("MONGOODS", List.of("CPU", "RAM", "PAG", "DISK"));
		SME_VITALS_MAP.put("CDC", List.of("CPU", "RAM", "PAG", "DISK"));
		SME_VITALS_MAP.put("IIB", List.of("CPU", "RAM", "PAG", "DISK"));
		
		SME_VITALS_MAP.put("DB1", List.of("CPU", "RAM", "PAG", "DISK"));
		SME_VITALS_MAP.put("DB2", List.of("CPU", "RAM", "PAG", "DISK"));
		SME_VITALS_MAP.put("CTS", List.of("CPU", "RAM", "PAG", "DISK"));
		SME_VITALS_MAP.put("DS", List.of("CPU", "RAM", "PAG", "DISK"));
		SME_VITALS_MAP.put("AM", List.of("CPU", "RAM", "PAG", "DISK"));
		SME_VITALS_MAP.put("CS", List.of("CPU", "RAM", "PAG", "DISK"));

	}

	// SME VITAL SYSTEM (DB)
	public static final Map<String, String> SME_VITAL_SYSTEM_MAP = new LinkedHashMap<>();
	static {
		//CIAM
		SME_VITAL_SYSTEM_MAP.put("CS", "CIAM_CFG");
		SME_VITAL_SYSTEM_MAP.put("DB1", "JO00-CIAMSQL03");
		SME_VITAL_SYSTEM_MAP.put("CTS", "JO00-CTS");
		SME_VITAL_SYSTEM_MAP.put("DB2", "JO00-CIAMSQL04");
		SME_VITAL_SYSTEM_MAP.put("AM", null);
		SME_VITAL_SYSTEM_MAP.put("DS", "CIAM_DS");

		//Anthos
		SME_VITAL_SYSTEM_MAP.put("ANTHOS1", "SME1");
		SME_VITAL_SYSTEM_MAP.put("ANTHOS2", "SME2");

		//Mongo
		SME_VITAL_SYSTEM_MAP.put("MONGOMS", "MONGO_SYS");
		SME_VITAL_SYSTEM_MAP.put("MONGOODS", "ODS");

		//Kafka
		SME_VITAL_SYSTEM_MAP.put("KAFKA", "KAFKA_SYS");

		//CDC
		SME_VITAL_SYSTEM_MAP.put("CDC", "CDC");

		//IIB
		SME_VITAL_SYSTEM_MAP.put("IIB", "IIB");
	}


	// SME VITAL SYSTEM (DB)
	public static final Map<String, String> SME_VITAL_THRESHOLD_LABEL_MAP = new LinkedHashMap<>();
	static {
		//CIAM
		SME_VITAL_THRESHOLD_LABEL_MAP.put("CS-CIAM_CFG", "CIAM_CS");
		SME_VITAL_THRESHOLD_LABEL_MAP.put("DB1-JO00-CIAMSQL03", "CIAM_SQL_DB1");
		SME_VITAL_THRESHOLD_LABEL_MAP.put("CTS-JO00-CTS", "CIAM_CTS");
		SME_VITAL_THRESHOLD_LABEL_MAP.put("DB2-JO00-CIAMSQL04", "CIAM_SQL_DB2");
		SME_VITAL_THRESHOLD_LABEL_MAP.put("AM", null);
		SME_VITAL_THRESHOLD_LABEL_MAP.put("DS-CIAM_DS", "CIAM_DS");

		//Anthos
		SME_VITAL_SYSTEM_MAP.put("ANTHOS1-SME1", "ANTHOS1");
		SME_VITAL_SYSTEM_MAP.put("ANTHOS2-SME2", "ANTHOS2");

		//Mongo
		SME_VITAL_SYSTEM_MAP.put("MONGOMS-MONGO_SYS", "MongoDBMS");
		SME_VITAL_SYSTEM_MAP.put("MONGOODS-ODS", "MongoDBODS");

		//Kafka
		SME_VITAL_SYSTEM_MAP.put("KAFKA-KAFKA_SYS", "KAFKA");

		//CDC
		SME_VITAL_SYSTEM_MAP.put("CDC-CDC", "CDC");

		//IIB
		SME_VITAL_SYSTEM_MAP.put("IIB-IIB", "IIB");
	}


	public static final Map<String, Integer> SME_THRESHOLD_PRIORITY = new LinkedHashMap<>();
	static {
		SME_THRESHOLD_PRIORITY.put(STABLE, 1);
		SME_THRESHOLD_PRIORITY.put(WARNING, 2);
		SME_THRESHOLD_PRIORITY.put(ERROR, 3);
	}

	// SME Cherries
	public static final Map<String, String> SME_CHERRIES_MAP = new LinkedHashMap<>();

	static {
		SME_CHERRIES_MAP.put("APIGEE-CIAM", "APIGEE");
		SME_CHERRIES_MAP.put("APIGEE-ANTHOS1", "APIGEE");
		SME_CHERRIES_MAP.put("APIGEE-ANTHOS2", "APIGEE");
		SME_CHERRIES_MAP.put("ANTHOS1-IIB", "ANTHOS2");
		SME_CHERRIES_MAP.put("ANTHOS2-IIB", "ANTHOS2");
		SME_CHERRIES_MAP.put("ANTHOS1-MONGOMS", "ANTHOS1");
		SME_CHERRIES_MAP.put("ANTHOS1-MONGOODS", "ANTHOS1");
		SME_CHERRIES_MAP.put("ANTHOS1-KAFKA", "ANTHOS1");
		SME_CHERRIES_MAP.put("ANTHOS2-MONGOMS", "ANTHOS2");
		SME_CHERRIES_MAP.put("ANTHOS2-MONGOODS", "ANTHOS2");
		SME_CHERRIES_MAP.put("ANTHOS2-KAFKA", "ANTHOS2");
		SME_CHERRIES_MAP.put("IIB-AS400", "IIB-AS400");
		SME_CHERRIES_MAP.put("AS400-CDC", "AS400-CDC");
		SME_CHERRIES_MAP.put("CDC-KAFKA", "CDC-KAFKA");
	}

}
