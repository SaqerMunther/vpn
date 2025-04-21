package com.app.dev.cmon.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.app.dev.cmon.components.Views;
import com.arabbank.devf.cmon.security.ABSecurityManager;
import com.jk.util.JK;
import com.jk.util.exceptions.JKSecurityException;
import com.jk.web.faces.util.JKJsfUtil;

@ManagedBean(name = "mbLogin")
@SessionScoped
public class SubMB_Login extends com.arabbank.devf.cmon.controllers.portal.MB_Login {
	public List<Views> viewList;
	DataAccess da = new DataAccess();
	
	protected void validateLoginRadar() {
		logger.debug("validate login:");
			viewList = new ArrayList<>();
			viewList = da.getViews(getCurrentUser().getUsername());
			if(viewList == null || viewList.isEmpty()) {
				throw new JKSecurityException("THE USER HAVE NO ACCESS TO RADAR");
			}
			logger.debug("End validate login:");
			logger.debug("//////////////////////////////");
	}

	@Override
	public void login() {
		logger.debug("login()", "entry");
		try {
			logger.debug("login()", "try");
			if (request().getContextPath().contains("ABNET") && username.equalsIgnoreCase("abportal"))
				throw new JKSecurityException("NOT AUTHORIZED. 50002");

			validateLoginRadar();
			validateLoginPlannedActivity();
			ABSecurityManager.checkAuthenticated(username, password, isDebug());
			loggedIn = true;
			loadUserInformation();
			password = null;
			infoDao.UpdateAutoLogin(username, this.getIP(), true);
			session().setAttribute("username", getUsername());
			redirect(getCurrentURL());
		} catch (JKSecurityException e) {
			logger.debug("login()", "catch");
			reset();
			error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public String validateAutoLogin() {
		JK.fixMe("@Jalal: please refactor...");
		viewList = new ArrayList<>();
		viewList = da.getViews(username);
		if(viewList == null || viewList.isEmpty()) {
			logger.debug("logout()", username);
			username = null;
			loggedIn = false;
			userSeverity = 0;
			password = null;
			JKJsfUtil.invalidateSession();
		}
		
		return "";
	}

	@Override
	public String logout() {
		logger.debug("logout()", username);
		
		reset();
		JKJsfUtil.invalidateSession();
		return getUrl();
	}

	public List<Views> getViewList() {
		return viewList;
	}
	
}
