package com.prt.controllers;

import com.google.gson.Gson;
import com.prt.models.Activity;
import com.prt.models.Event;
import com.prt.models.Expense;
import com.prt.models.Reimbursement;
import com.prt.utils.RestUtil;
import java.io.Serializable;
import javax.annotation.PostConstruct;
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
	private Reimbursement reimbursement;
	private Expense expense;

	public Reimbursement getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}

	public Expense getExpense() {
		return expense;
	}

	public void setExpense(Expense expense) {
		this.expense = expense;
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

	@PostConstruct
	void init() {
		event = new Event();
		event.setUserGuid(preferences.userGuid);
		reimbursement = new Reimbursement();
		reimbursement.setUserGuid(preferences.userGuid);
		expense = new Expense();
		expense.setUserGuid(preferences.userGuid);
	}

	public void prepareRequest() {
		selectedActivity = "";
		event = new Event();
		reimbursement = new Reimbursement();
		expense = new Expense();
	}

	public void submitActivityRequest() {
		try {
			for (Activity activity : preferences.activities) {
				if (activity.getGuid().equals(selectedActivity)) {
					event.setActivity(activity);
					break;
				}
			}
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
			String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "request/reimbursement", gson.toJson(reimbursement)), String.class);
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
			String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "request/expense", gson.toJson(expense)), String.class);
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
		reimbursement.calculateReimbursementTotal();
	}

	public void refreshPage() {
		try {
			HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String uri = req.getRequestURI();
			if (uri.contains("request.xhtml")) {
				//get request controller
				FacesContext context = FacesContext.getCurrentInstance();
				RequestsController requestsController = context.getApplication().evaluateExpressionGet(context, "#{requestController}", RequestsController.class);
				if (requestsController != null) {
					requestsController.refreshLists();
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
