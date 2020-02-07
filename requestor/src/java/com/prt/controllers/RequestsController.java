/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prt.models.Budget;
import com.prt.models.Reimbursement;
import com.prt.models.Request;
import com.prt.models.User;
import com.prt.utils.RestUtil;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.PrimeFaces;

/**
 *
 * @author P-ratt
 */
@ManagedBean(name = "reqController")
@ViewScoped
public class RequestsController implements Serializable {

	@ManagedProperty("#{guestPreferences}")
	private GuestPreferences preferences;
	private String title;
	private ArrayList<Request> requests = new ArrayList<>();
	private boolean approver;
	private Request selectedRequest;
	private Reimbursement newReimbursement;
	private ArrayList<User> users = new ArrayList<>();
	private ArrayList<Budget> budgets = new ArrayList<>();
	private User user;
	private Budget budget;

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public ArrayList<Budget> getBudgets() {
		return budgets;
	}

	public void setBudgets(ArrayList<Budget> budgets) {
		this.budgets = budgets;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public Reimbursement getNewReimbursement() {
		return newReimbursement;
	}

	public void setNewReimbursement(Reimbursement newReimbursement) {
		this.newReimbursement = newReimbursement;
	}

	public Request getSelectedRequest() {
		return selectedRequest;
	}

	public void setSelectedRequest(Request selectedRequest) {
		this.selectedRequest = selectedRequest;
	}

	public boolean isApprover() {
		return approver;
	}

	public void setApprover(boolean approver) {
		this.approver = approver;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<Request> getRequests() {
		return requests;
	}

	public void setRequests(ArrayList<Request> requests) {
		this.requests = requests;
	}

	public GuestPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(GuestPreferences preferences) {
		this.preferences = preferences;
	}

	public void refreshLists() {
		try {
			Gson gson = new Gson();
			users = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "user/select/all", null), new TypeToken<ArrayList<User>>() {
			}.getType());
			budgets = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "budget/select/all", null), new TypeToken<ArrayList<Budget>>() {
			}.getType());
			if (preferences.myRequests) {
				title = "My Submitted Requests";
				requests = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "request/select/my/all", gson.toJson(preferences.userGuid)), new TypeToken<ArrayList<Request>>() {
				}.getType());
			} else {
				title = "Actionable Requests";
				requests = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "request/select/actionable/all", gson.toJson(preferences.userGuid)), new TypeToken<ArrayList<Request>>() {
				}.getType());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateSelectedUser(User user) {
		this.user = null;
		for (User u : users) {
			if (u.getGuid().equals(user.getGuid())) {
				this.user = u;
				break;
			}
		}
		PrimeFaces.current().ajax().update("userDetailsForm");
	}

	public void updateSelectedBudget(Budget budget) {
		this.budget = null;
		for (Budget b : budgets) {
			if (b.getGuid().equals(budget.getGuid())) {
				this.budget = b;
				break;
			}
		}
		PrimeFaces.current().ajax().update("");
	}
}
