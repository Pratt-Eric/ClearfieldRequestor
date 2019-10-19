/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prt.models.Budget;
import com.prt.models.Group;
import com.prt.models.User;
import com.prt.utils.RestUtil;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author P-ratt
 */
@ManagedBean(name = "budgetPermissionsController")
@ViewScoped
public class BudgetPermissionsController implements Serializable {

	@ManagedProperty("#{guestPreferences}")
	private GuestPreferences preferences;
	private ArrayList<User> users = new ArrayList<>();
	private ArrayList<Group> groups = new ArrayList<>();
	private Budget budget;
	private ArrayList<String> selectedUsers = new ArrayList<>();
	private ArrayList<String> selectedGroups = new ArrayList<>();
	private User selectedUser;
	private Group selectedGroup;

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

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
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

	@PostConstruct
	void init() {
		try {
			budget = preferences.selectedBudget;
			Gson gson = new Gson();
			groups = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/group/select/all", null), new TypeToken<ArrayList<Group>>() {
			}.getType());
			users = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/user/select/all", null), new TypeToken<ArrayList<User>>() {
			}.getType());
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

	public void editBudgetPermissions() {
		try {
			budget.setUsers(new ArrayList<>());
			budget.setGroups(new ArrayList<>());
			for (String selected : selectedUsers) {
				for (User user : users) {
					if (user.getGuid().equals(selected)) {
						boolean found = false;
						for (User u : budget.getUsers()) {
							if (u.getGuid().equals(user.getGuid())) {
								found = true;
								break;
							}
						}
						if (!found) {
							budget.getUsers().add(user);
						}
						break;
					}
				}
			}
			for (String selected : selectedGroups) {
				for (Group group : groups) {
					if (group.getGuid().equals(selected)) {
						boolean found = false;
						for (Group g : budget.getGroups()) {
							if (g.getGuid().equals(group.getGuid())) {
								found = true;
								break;
							}
						}
						if (!found) {
							budget.getGroups().add(group);
						}
						break;
					}
				}
			}
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
