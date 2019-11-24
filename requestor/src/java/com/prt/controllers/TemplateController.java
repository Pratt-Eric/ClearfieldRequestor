package com.prt.controllers;

import com.prt.controllers.GuestPreferences;
import com.prt.models.Event;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author P-ratt
 */
@ManagedBean(name = "templateController")
@ViewScoped
public class TemplateController implements Serializable {

	@ManagedProperty("#{guestPreferences}")
	private GuestPreferences preferences;
	private String selectedActivity;
	private Event event;
	private float budget;
	private float reimbursementAmt;
	private float tax;
	private float total;
	private boolean chargedToWardAccount;
	private String wardAccount;
	private String org;
	private String orgName;
	private String orgLeader;
	private String expense;
	private String expenseDetails;
	private float amt;

	public String getExpense() {
		return expense;
	}

	public void setExpense(String expense) {
		this.expense = expense;
	}

	public String getExpenseDetails() {
		return expenseDetails;
	}

	public void setExpenseDetails(String expenseDetails) {
		this.expenseDetails = expenseDetails;
	}

	public float getAmt() {
		return amt;
	}

	public void setAmt(float amt) {
		this.amt = amt;
	}

	public String getOrgLeader() {
		return orgLeader;
	}

	public void setOrgLeader(String orgLeader) {
		this.orgLeader = orgLeader;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getWardAccount() {
		return wardAccount;
	}

	public void setWardAccount(String wardAccount) {
		this.wardAccount = wardAccount;
	}

	public boolean isChargedToWardAccount() {
		return chargedToWardAccount;
	}

	public void setChargedToWardAccount(boolean chargedToWardAccount) {
		this.chargedToWardAccount = chargedToWardAccount;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public float getTax() {
		return tax;
	}

	public void setTax(float tax) {
		this.tax = tax;
	}

	public float getReimbursementAmt() {
		return reimbursementAmt;
	}

	public void setReimbursementAmt(float reimbursementAmt) {
		this.reimbursementAmt = reimbursementAmt;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public float getBudget() {
		return budget;
	}

	public void setBudget(float budget) {
		this.budget = budget;
	}

	public String getSelectedActivity() {
		return selectedActivity;
	}

	public void setSelectedActivity(String selectedActivity) {
		this.selectedActivity = selectedActivity;
	}

	public GuestPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(GuestPreferences preferences) {
		this.preferences = preferences;
	}

	public void prepareRequest() {
		selectedActivity = "";
		event = new Event();
		budget = 0;
		reimbursementAmt = 0;
		tax = 0;
		total = 0;
		chargedToWardAccount = false;
		wardAccount = "";
		org = "";
		orgName = "";
		orgLeader = "";
		expense = "";
		expenseDetails = "";
		amt = 0;
	}

	public void submitActivityRequest() {

	}

	public void submitReimbursementRequest() {

	}

	public void submitExpenseRequest() {

	}

	public void calculateReimbursementTotal() {
		total = reimbursementAmt + tax;
	}

}
