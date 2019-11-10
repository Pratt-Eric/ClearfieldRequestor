/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prt.models.Budget;
import com.prt.models.Calendar;
import com.prt.models.Dashboard;
import com.prt.models.Group;
import com.prt.models.User;
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
@ManagedBean(name = "dashboardsController")
@ViewScoped
public class DashboardsController implements Serializable {

	@ManagedProperty("#{guestPreferences}")
	private GuestPreferences preferences;
	private ArrayList<User> users = new ArrayList<>();
	private ArrayList<Group> groups = new ArrayList<>();
	private ArrayList<Dashboard> dashboards = new ArrayList<>();
	private Dashboard newDashboard;
	private Dashboard selectedDashboard;
	private ArrayList<String> selectedUsers = new ArrayList<>();
	private ArrayList<String> selectedGroups = new ArrayList<>();

	public ArrayList<String> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(ArrayList<String> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public ArrayList<String> getSelectedGroups() {
		return selectedGroups;
	}

	public void setSelectedGroups(ArrayList<String> selectedGroups) {
		this.selectedGroups = selectedGroups;
	}

	public Dashboard getNewDashboard() {
		return newDashboard;
	}

	public void setNewDashboard(Dashboard newDashboard) {
		this.newDashboard = newDashboard;
	}

	public Dashboard getSelectedDashboard() {
		return selectedDashboard;
	}

	public void setSelectedDashboard(Dashboard selectedDashboard) {
		this.selectedDashboard = selectedDashboard;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public ArrayList<Group> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<Group> groups) {
		this.groups = groups;
	}

	public ArrayList<Dashboard> getDashboards() {
		return dashboards;
	}

	public void setDashboards(ArrayList<Dashboard> dashboards) {
		this.dashboards = dashboards;
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
			dashboards = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/dashboard/select/all", null), new TypeToken<ArrayList<Dashboard>>() {
			}.getType());
			groups = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/group/select/all", null), new TypeToken<ArrayList<Group>>() {
			}.getType());
			users = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/user/select/all", null), new TypeToken<ArrayList<User>>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startAddDashboard() {
		newDashboard = new Dashboard();
	}

	public void updateSelectedDashboard(Dashboard dashboard) {
		selectedDashboard = dashboard;
		//update the selected users, groups, calendars, and budgets
		selectedUsers = new ArrayList<>();
		selectedGroups = new ArrayList<>();
		for (User user : selectedDashboard.getUsers()) {
			if (!selectedUsers.contains(user.getGuid())) {
				selectedUsers.add(user.getGuid());
			}
		}
		for (Group group : selectedDashboard.getGroups()) {
			if (!selectedGroups.contains(group.getGuid())) {
				selectedGroups.add(group.getGuid());
			}
		}
	}

	public void addDashboard() {
		try {
			updateSelectedItems(newDashboard);
			Gson gson = new Gson();
			String result = gson.toJson(RestUtil.post(RestUtil.BASEURL + "/dashboard/add", gson.toJson(newDashboard)), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Dashboard was successfully created"));
				init();
				PrimeFaces.current().executeScript("PF('addDashboardDlg').hide()");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem creating your dashboard"));
			}
			PrimeFaces.current().ajax().update("dashboardsForm");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void editDashboard() {
		try {
			updateSelectedItems(selectedDashboard);
			Gson gson = new Gson();
			String result = gson.toJson(RestUtil.post(RestUtil.BASEURL + "/dashboard/edit", gson.toJson(selectedDashboard)), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Dashboard was successfully modified"));
				init();
				PrimeFaces.current().executeScript("PF('editDashboardDlg').hide()");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem modifying your dashboard"));
			}
			PrimeFaces.current().ajax().update("dashboardsForm");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteDashboard() {
		try {
			Gson gson = new Gson();
			String result = gson.toJson(RestUtil.post(RestUtil.BASEURL + "/dashboard/delete", gson.toJson(newDashboard)), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Dashboard was successfully deleted"));
				init();
				PrimeFaces.current().executeScript("PF('deleteDashboardDlg').hide()");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem deleting your dashboard"));
			}
			PrimeFaces.current().ajax().update("dashboardsForm");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateSelectedItems(Dashboard dashboard) {
		dashboard.setUsers(new ArrayList<>());
		dashboard.setGroups(new ArrayList<>());
		for (String user : selectedUsers) {
			for (User u : users) {
				if (u.getGuid().equals(user)) {
					if (!dashboard.getUsers().contains(u)) {
						dashboard.getUsers().add(u);
					}
					break;
				}
			}
		}
		for (String group : selectedGroups) {
			for (Group g : groups) {
				if (g.getGuid().equals(group)) {
					if (!dashboard.getGroups().contains(g)) {
						dashboard.getGroups().add(g);
					}
					break;
				}
			}
		}
	}

	public String customize(Dashboard dashboard) {
		preferences.selectedDashboard = dashboard;
		return "/main/dashboardsetup.xhtml?faces-redirect=true";
	}
}
