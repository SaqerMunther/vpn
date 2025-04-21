package com.app.dev.cmon.controllers;

import javax.faces.bean.ManagedBean;

import org.springframework.web.context.annotation.ApplicationScope;

import com.arabbank.devf.cmon.controllers.portal.CmonManagedBean;

@ApplicationScope
@ManagedBean(name="controller")
public class Controller extends CmonManagedBean{

	@Override
	public void reset() {
	}
	
	@Override
	public String getAppName() {
		return "Dashboard";
	}
	
	
	@Override
	public String getTitle() {
		return "Arab Bank Radar Dashboard  V.";
	}
	

}
