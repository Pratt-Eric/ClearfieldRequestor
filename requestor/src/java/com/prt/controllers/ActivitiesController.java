/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prt.models.Activity;
import com.prt.models.Budget;
import com.prt.models.Calendar;
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
@ManagedBean(name = "activitiesController")
@ViewScoped
public class ActivitiesController implements Serializable {

	@ManagedProperty("#{guestPreferences}")
	private GuestPreferences preferences;
	private ArrayList<Activity> activities = new ArrayList<>();
	private Activity selectedActivity;
	private String selectedActivityGuid;
	private Activity newActivity;
	private ArrayList<User> users = new ArrayList<>();
	private ArrayList<Group> groups = new ArrayList<>();
	private ArrayList<Budget> budgets = new ArrayList<>();
	private ArrayList<Calendar> calendars = new ArrayList<>();
	private ArrayList<String> activityTypes = new ArrayList<>();
	private ArrayList<String> selectedUsers = new ArrayList<>();
	private ArrayList<String> selectedGroups = new ArrayList<>();
	private ArrayList<String> selectedCalendars = new ArrayList<>();
	private String selectedBudget;

	public ArrayList<String> getActivityTypes() {
		return activityTypes;
	}

	public void setActivityTypes(ArrayList<String> activityTypes) {
		this.activityTypes = activityTypes;
	}

	public String getSelectedBudget() {
		return selectedBudget;
	}

	public void setSelectedBudget(String selectedBudget) {
		this.selectedBudget = selectedBudget;
	}

	public ArrayList<String> getSelectedCalendars() {
		return selectedCalendars;
	}

	public void setSelectedCalendars(ArrayList<String> selectedCalendars) {
		this.selectedCalendars = selectedCalendars;
	}

	public ArrayList<Budget> getBudgets() {
		return budgets;
	}

	public void setBudgets(ArrayList<Budget> budgets) {
		this.budgets = budgets;
	}

	public ArrayList<Calendar> getCalendars() {
		return calendars;
	}

	public void setCalendars(ArrayList<Calendar> calendars) {
		this.calendars = calendars;
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

	public GuestPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(GuestPreferences preferences) {
		this.preferences = preferences;
	}

	public ArrayList<Activity> getActivities() {
		return activities;
	}

	public void setActivities(ArrayList<Activity> activities) {
		this.activities = activities;
	}

	public Activity getSelectedActivity() {
		return selectedActivity;
	}

	public void setSelectedActivity(Activity selectedActivity) {
		this.selectedActivity = selectedActivity;
	}

	public String getSelectedActivityGuid() {
		return selectedActivityGuid;
	}

	public void setSelectedActivityGuid(String selectedActivityGuid) {
		this.selectedActivityGuid = selectedActivityGuid;
	}

	public Activity getNewActivity() {
		return newActivity;
	}

	public void setNewActivity(Activity newActivity) {
		this.newActivity = newActivity;
	}

	@PostConstruct
	void init() {
		try {
			newActivity = new Activity();
			Gson gson = new Gson();
			activities = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/activity/select/all", null), new TypeToken<ArrayList<Activity>>() {
			}.getType());
			groups = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/group/select/all", null), new TypeToken<ArrayList<Group>>() {
			}.getType());
			users = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/user/select/all", null), new TypeToken<ArrayList<User>>() {
			}.getType());
			budgets = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/budget/select/all", null), new TypeToken<ArrayList<Budget>>() {
			}.getType());
			calendars = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/calendar/select/all", null), new TypeToken<ArrayList<Calendar>>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startCreateActivity() {
		newActivity = new Activity();
	}

	public void updateSelectedActivity(Activity activity) {
		selectedActivity = activity;
		selectedUsers = new ArrayList<>();
		selectedGroups = new ArrayList<>();
		selectedCalendars = new ArrayList<>();
		for (User user : selectedActivity.getUsers()) {
			if (!selectedUsers.contains(user.getGuid())) {
				selectedUsers.add(user.getGuid());
			}
		}
		for (Group group : selectedActivity.getGroups()) {
			if (!selectedGroups.contains(group.getGuid())) {
				selectedGroups.add(group.getGuid());
			}
		}
		for (Calendar calendar : selectedActivity.getCalendars()) {
			if (!selectedCalendars.contains(calendar.getGuid())) {
				selectedCalendars.add(calendar.getGuid());
			}
		}
		if (selectedActivity.getBudget() != null) {
			selectedBudget = selectedActivity.getBudget().getGuid();
		}
	}

	public void addNewActivity() {
		try {
			Gson gson = new Gson();
			String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/activity/add", gson.toJson(newActivity)), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "The new activity was successfully added"));
				init();
				PrimeFaces.current().ajax().update("activityForm");
				PrimeFaces.current().executeScript("PF('activityAddDlg').hide()");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem adding the new activity"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void editExistingActivity() {
		try {
			Gson gson = new Gson();
			String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/activity/edit", gson.toJson(selectedActivity)), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "The existing activity was successfully modified"));
				init();
				PrimeFaces.current().ajax().update("activityForm");
				PrimeFaces.current().executeScript("PF('activityEditDlg').hide()");
				PrimeFaces.current().executeScript("PF('usersAndGroupsDlg').hide()");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem modified the existing activity"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteExistingActivity() {
		try {
			Gson gson = new Gson();
			String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/activity/delete", gson.toJson(selectedActivity)), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "The existing activity was successfully deleted"));
				init();
				PrimeFaces.current().ajax().update("activityForm");
				PrimeFaces.current().executeScript("PF('activityDeleteDlg').hide()");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem deleting the existing activity"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void adjustUsersAndGroups() {
		selectedActivity.setUsers(new ArrayList<>());
		selectedActivity.setGroups(new ArrayList<>());
		selectedActivity.setCalendars(new ArrayList<>());
		selectedActivity.setBudget(null);
		for (String user : selectedUsers) {
			for (User u : users) {
				if (u.getGuid().equals(user)) {
					boolean found = false;
					for (User u2 : selectedActivity.getUsers()) {
						if (u2.getGuid().equals(u.getGuid())) {
							found = true;
							break;
						}
					}
					if (!found) {
						selectedActivity.getUsers().add(u);
					}
				}
			}
		}
		for (String group : selectedGroups) {
			for (Group g : groups) {
				if (g.getGuid().equals(group)) {
					boolean found = false;
					for (Group g2 : selectedActivity.getGroups()) {
						if (g2.getGuid().equals(g.getGuid())) {
							found = true;
							break;
						}
					}
					if (!found) {
						selectedActivity.getGroups().add(g);
					}
				}
			}
		}
		for (String calendar : selectedCalendars) {
			for (Calendar c : calendars) {
				if (c.getGuid().equals(calendar)) {
					boolean found = false;
					for (Calendar c2 : selectedActivity.getCalendars()) {
						if (c2.getGuid().equals(c.getGuid())) {
							found = true;
							break;
						}
					}
					if (!found) {
						selectedActivity.getCalendars().add(c);
					}
				}
			}
		}
		for (Budget budget : budgets) {
			if (budget.getGuid().equals(selectedBudget)) {
				selectedActivity.setBudget(budget);
				break;
			}
		}
		editExistingActivity();
	}
}
