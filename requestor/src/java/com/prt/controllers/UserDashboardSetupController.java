/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prt.models.Dashboard;
import com.prt.utils.RestUtil;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

/**
 *
 * @author P-ratt
 */
@ManagedBean(name = "userDashboardSetupController")
@ViewScoped
public class UserDashboardSetupController implements Serializable {

	@ManagedProperty("#{guestPreferences}")
	private GuestPreferences preferences;
	private ArrayList<Dashboard> allUserDashboards = new ArrayList<>();
	private ArrayList<Dashboard> allAvailableUserDashboards = new ArrayList<>();
	private ArrayList<Dashboard> availableDashboardsToAdd = new ArrayList<>();
	private ArrayList<String> selectedDashboards = new ArrayList<>();

	public ArrayList<Dashboard> getAllAvailableUserDashboards() {
		return allAvailableUserDashboards;
	}

	public void setAllAvailableUserDashboards(ArrayList<Dashboard> allAvailableUserDashboards) {
		this.allAvailableUserDashboards = allAvailableUserDashboards;
	}

	public ArrayList<Dashboard> getAllUserDashboards() {
		return allUserDashboards;
	}

	public void setAllUserDashboards(ArrayList<Dashboard> allUserDashboards) {
		this.allUserDashboards = allUserDashboards;
	}

	public ArrayList<String> getSelectedDashboards() {
		return selectedDashboards;
	}

	public void setSelectedDashboards(ArrayList<String> selectedDashboards) {
		this.selectedDashboards = selectedDashboards;
	}

	public ArrayList<Dashboard> getAvailableDashboardsToAdd() {
		return availableDashboardsToAdd;
	}

	public void setAvailableDashboardsToAdd(ArrayList<Dashboard> availableDashboardsToAdd) {
		this.availableDashboardsToAdd = availableDashboardsToAdd;
	}

	public GuestPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(GuestPreferences preferences) {
		this.preferences = preferences;
	}

	@PostConstruct
	void init() {
		try {
			Gson gson = new Gson();
			allUserDashboards = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/dashboard/user/select/all", gson.toJson(preferences.userGuid)), new TypeToken<ArrayList<Dashboard>>() {
			}.getType());
			allAvailableUserDashboards = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/dashboard/user/available", gson.toJson(preferences.userGuid)), new TypeToken<ArrayList<Dashboard>>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void prepareAddDashboards() {
		//be sure to only display dashboards that aren't already added
		availableDashboardsToAdd = new ArrayList<>();
		for (Dashboard dashboard : allAvailableUserDashboards) {
			boolean found = false;
			for (Dashboard dash : allUserDashboards) {
				if (dash.getGuid().equals(dashboard.getGuid())) {
					found = true;
					break;
				}
			}
			if (!found) {
				availableDashboardsToAdd.add(dashboard);
			}
		}
	}

	public void addDashboards() {
		try {
			String[][] params = new String[selectedDashboards.size()][];
			for (int i = 0; i < selectedDashboards.size(); i++) {
				params[i] = new String[]{preferences.userGuid, selectedDashboards.get(i)};
			}

			Gson gson = new Gson();
			String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/dashboard/user/add", gson.toJson(params)), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Dashboards were successfully added"));
				init();
				PrimeFaces.current().executeScript("PF('dashAddDlg').hide()");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem adding your dashboards"));
			}
			PrimeFaces.current().ajax().update("dashboardsForm");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void makeDefault(Dashboard dashboard) {
		try {
			Gson gson = new Gson();
			String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/dashboard/user/edit", gson.toJson(new String[]{preferences.userGuid, dashboard.getXrefGuid()})), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Dashboard was successfully modified"));
				init();
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem modifying your dashboard"));
			}
			PrimeFaces.current().ajax().update("dashboardsForm");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
