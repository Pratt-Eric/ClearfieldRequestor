/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
@ManagedBean(name = "calendarPermissionsController")
@ViewScoped
public class CalendarPermissionsController implements Serializable {

	@ManagedProperty("#{guestPreferences}")
	private GuestPreferences preferences;
	private ArrayList<User> users = new ArrayList<>();
	private ArrayList<Group> groups = new ArrayList<>();
	private ArrayList<User> availableUsers = new ArrayList<>();
	private ArrayList<Group> availableGroups = new ArrayList<>();
	private ArrayList<String> selectedUsers = new ArrayList<>();
	private ArrayList<String> selectedGroups = new ArrayList<>();
	private Calendar calendar;
	private User selectedUser;
	private Group selectedGroup;

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public GuestPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(GuestPreferences preferences) {
		this.preferences = preferences;
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

	public ArrayList<User> getAvailableUsers() {
		return availableUsers;
	}

	public void setAvailableUsers(ArrayList<User> availableUsers) {
		this.availableUsers = availableUsers;
	}

	public ArrayList<Group> getAvailableGroups() {
		return availableGroups;
	}

	public void setAvailableGroups(ArrayList<Group> availableGroups) {
		this.availableGroups = availableGroups;
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

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	public Group getSelectedGroup() {
		return selectedGroup;
	}

	public void setSelectedGroup(Group selectedGroup) {
		this.selectedGroup = selectedGroup;
	}

	@PostConstruct
	void init() {
		calendar = preferences.selectedCalendar;
		try {
			Gson gson = new Gson();
			groups = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "group/select/all", null), new TypeToken<ArrayList<Group>>() {
			}.getType());
			users = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "user/select/all", null), new TypeToken<ArrayList<User>>() {
			}.getType());

			for (User user : users) {
				boolean found = false;
				for (User u : calendar.getUsers()) {
					if (u.getGuid().equals(user.getGuid())) {
						found = true;
						break;
					}
				}
				if (!found) {
					availableUsers.add(user);
				}
			}

			for (Group group : groups) {
				boolean found = false;
				for (Group g : calendar.getGroups()) {
					if (g.getGuid().equals(group.getGuid())) {
						found = true;
						break;
					}
				}
				if (!found) {
					availableGroups.add(group);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startAddUsers() {
		selectedUsers = new ArrayList<>();
	}

	public void startAddGroups() {
		selectedGroups = new ArrayList<>();
	}

	public void removeUser() {
		for (User user : calendar.getUsers()) {
			if (user.getGuid().equals(selectedUser.getGuid())) {
				calendar.getUsers().remove(user);
				break;
			}
		}
		editCalendarPermissions();
	}

	public void removeGroup() {
		for (Group group : calendar.getGroups()) {
			if (group.getGuid().equals(selectedGroup.getGuid())) {
				calendar.getGroups().remove(group);
				break;
			}
		}
		editCalendarPermissions();
	}

	public void editCalendarPermissions() {
		try {
			Gson gson = new Gson();
			String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "calendar/edit", gson.toJson(calendar)), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", calendar.getName() + " was successfully modified"));
				init();
				PrimeFaces.current().executeScript("PF('addUsersDlg').hide()");
				PrimeFaces.current().executeScript("PF('addGroupsDlg').hide()");
				PrimeFaces.current().executeScript("PF('deleteUserDlg').hide()");
				PrimeFaces.current().executeScript("PF('deleteGroupDlg').hide()");
				PrimeFaces.current().ajax().update("calendarForm");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem modifying " + calendar.getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addUsersAndGroups() {
		try {
			for (String selected : selectedUsers) {
				for (User user : users) {
					if (user.getGuid().equals(selected)) {
						boolean found = false;
						for (User u : calendar.getUsers()) {
							if (u.getGuid().equals(user.getGuid())) {
								found = true;
								break;
							}
						}
						if (!found) {
							calendar.getUsers().add(user);
						}
						break;
					}
				}
			}
			for (String selected : selectedGroups) {
				for (Group group : groups) {
					if (group.getGuid().equals(selected)) {
						boolean found = false;
						for (Group g : calendar.getGroups()) {
							if (g.getGuid().equals(group.getGuid())) {
								found = true;
								break;
							}
						}
						if (!found) {
							calendar.getGroups().add(group);
						}
						break;
					}
				}
			}
			editCalendarPermissions();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectUser(User user) {
		selectedUser = user;
	}

	public void selectGroup(Group group) {
		selectedGroup = group;
	}
}
