/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.controllers;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

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

	public String retrieveRequests(boolean myRequests) {
		preferences.myRequests = myRequests;
		return "/main/user/requests.xhtml?faces-redirect=true";
	}
}
