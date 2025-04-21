package com.app.dev.cmon.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.arabbank.devf.cmon.controllers.portal.CmonManagedBean;

@ManagedBean(name = "resetAll")
@ViewScoped
public class ResetAll extends CmonManagedBean{

	@Override
	public void reset() {
		reset("mbMain");
		reset("RadarDataSource");
		reset("mbLogin");
	}
}
