package com.app.dev.cmon.utilites;

import java.util.*;

import com.app.dev.cmon.components.Views;
import com.app.dev.cmon.controllers.DataAccess;
import com.app.dev.cmon.controllers.SubMB_Login;
import com.arabbank.devf.cmon.controllers.portal.CmonManagedBean;

public class Controls extends CmonManagedBean {
	private DataAccess da;
	private List<Views> viewList;
	SubMB_Login mbLogin = new SubMB_Login();

	public Controls() {

		da = new DataAccess();
		viewList = da.getViews(mbLogin.getCurrentUser().getUsername());

	}

	public List<Views> getViewList() {
		return viewList;
	}

	public void setViewList(List<Views> viewList) {
		this.viewList = viewList;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}
}