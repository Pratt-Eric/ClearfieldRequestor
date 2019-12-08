/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.controllers;

import com.google.gson.Gson;
import com.prt.models.Expense;
import com.prt.models.Reimbursement;
import com.prt.models.Request;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author P-ratt
 */
@ManagedBean(name = "requestsController")
@ViewScoped
public class RequestsController implements Serializable {

	@ManagedProperty("#{guestPreferences}")
	private GuestPreferences preferences;
	public ArrayList<Request> activities = new ArrayList<>();
	public ArrayList<Reimbursement> reimbursements = new ArrayList<>();
	public ArrayList<Expense> expenses = new ArrayList<>();

	public ArrayList<Reimbursement> getReimbursements() {
		return reimbursements;
	}

	public void setReimbursements(ArrayList<Reimbursement> reimbursements) {
		this.reimbursements = reimbursements;
	}

	public ArrayList<Expense> getExpenses() {
		return expenses;
	}

	public void setExpenses(ArrayList<Expense> expenses) {
		this.expenses = expenses;
	}

	public GuestPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(GuestPreferences preferences) {
		this.preferences = preferences;
	}

	public ArrayList<Request> getActivities() {
		return activities;
	}

	public void setActivities(ArrayList<Request> activities) {
		this.activities = activities;
	}

	public void refreshLists() {
		try {
			Gson gson = new Gson();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
