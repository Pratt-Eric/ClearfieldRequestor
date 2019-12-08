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
	@ManagedProperty("#{requestController}")
	private RequestsController requestController;

	public RequestsController getRequestController() {
		return requestController;
	}

	public void setRequestController(RequestsController requestController) {
		this.requestController = requestController;
	}

	public GuestPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(GuestPreferences preferences) {
		this.preferences = preferences;
	}

	public String retrieveMyRequests() {

		return "/main/user/myrequests.xhtml?faces-redirect=true";
	}

	public String retrieveActionableRequests() {

		return "/main/user/requests.xhtml?faces-redirect=true";
	}
}
