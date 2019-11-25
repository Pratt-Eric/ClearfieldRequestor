package com.prt.controllers;

import com.google.gson.Gson;
import com.prt.models.Event;
import com.prt.utils.RestUtil;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;

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
		try {
			Gson gson = new Gson();
			String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "request/activity", gson.toJson(event)), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Activity request was successfully submitted"));
				refreshPage();
				PrimeFaces.current().executeScript("PF('requestActivityForm').hide()");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem submitting your activity request"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void submitReimbursementRequest() {
		try {
			Gson gson = new Gson();
			String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "request/reimbursement", gson.toJson(event)), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Reimbursement request was successfully submitted"));
				refreshPage();
				PrimeFaces.current().executeScript("PF('requestReimbursementForm').hide()");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem submitting your reimbursement request"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void submitExpenseRequest() {
		try {
			Gson gson = new Gson();
			String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "request/expense", gson.toJson(event)), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Activity request was successfully submitted"));
				refreshPage();
				PrimeFaces.current().executeScript("PF('requestExpenseForm').hide()");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem submitting your reimbursement request"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void calculateReimbursementTotal() {
		total = reimbursementAmt + tax;
	}

	public void refreshPage() {
		try {
			HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String uri = req.getRequestURI();
			if (uri.contains("request.xhtml")) {
				//get request controller
				FacesContext context = FacesContext.getCurrentInstance();
				RequestController requestController = context.getApplication().evaluateExpressionGet(context, "#{requestController}", RequestController.class);
				if (requestController != null) {
					requestController.init();
					PrimeFaces.current().ajax().update("");
				} else {
					System.out.println("Request Controller was null in order to refresh the page");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
