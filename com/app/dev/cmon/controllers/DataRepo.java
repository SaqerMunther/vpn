package com.app.dev.cmon.controllers;

import java.util.HashMap;
import java.util.List;

import com.app.dev.cmon.components.AssetInfo;
import com.app.dev.cmon.components.ComplianceInfo;

public class DataRepo {

	public  List<AssetInfo> assetList ;
	public  List<ComplianceInfo> complianceList ;
	public  HashMap<String,List<AssetInfo>> assetMap = new HashMap<>() ; 
	public  HashMap<String,List<ComplianceInfo>> complianceMap = new HashMap<>() ; 


}
