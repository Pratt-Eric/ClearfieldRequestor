/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prt.models.Request;
import com.prt.utils.RestUtil;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author P-ratt
 */
@ManagedBean(name = "menuController")
@ViewScoped
public class MenuController implements Serializable {

	@ManagedProperty("#{guestPreferences}")
	private GuestPreferences preferences;
	@ManagedProperty("#{requestsController}")
	private RequestsController requestsController;

	public RequestsController getRequestsController() {
		return requestsController;
	}

	public void setRequestsController(RequestsController requestsController) {
		this.requestsController = requestsController;
	}

	public GuestPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(GuestPreferences preferences) {
		this.preferences = preferences;
	}

	public String retrieveMyRequests() {
		preferences.myRequests = true;
		return "/main/user/myrequests.xhtml?faces-redirect=true";
	}

	public String retrieveActionableRequests() {
		preferences.myRequests = false;
		return "/main/user/requests.xhtml?faces-redirect=true";
	}
}
