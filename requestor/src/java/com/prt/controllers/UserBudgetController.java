/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prt.models.Budget;
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
@ManagedBean(name = "userBudgetController")
@ViewScoped
public class UserBudgetController implements Serializable {

	@ManagedProperty("#{guestPreferences")
	private GuestPreferences preferences;
	private ArrayList<Budget> budgets = new ArrayList<>();
	private Budget selectedBudget;
	private String selectedBudgetGuid;
	private double amountToAdjust = 0;

	public Budget getSelectedBudget() {
		return selectedBudget;
	}

	public void setSelectedBudget(Budget selectedBudget) {
		this.selectedBudget = selectedBudget;
	}

	public String getSelectedBudgetGuid() {
		return selectedBudgetGuid;
	}

	public void setSelectedBudgetGuid(String selectedBudgetGuid) {
		this.selectedBudgetGuid = selectedBudgetGuid;
	}

	public double getAmountToAdjust() {
		return amountToAdjust;
	}

	public void setAmountToAdjust(double amountToAdjust) {
		this.amountToAdjust = amountToAdjust;
	}

	public ArrayList<Budget> getBudgets() {
		return budgets;
	}

	public void setBudgets(ArrayList<Budget> budgets) {
		this.budgets = budgets;
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
			budgets = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/budget/select/all/user", gson.toJson(preferences.getUserGuid())), new TypeToken<ArrayList<Budget>>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateSelectedBudget(Budget budget) {
		selectedBudget = budget;
		amountToAdjust = 0;
	}

	public void editBudget() {
		try {
			Gson gson = new Gson();
			String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/budget/edit/user", gson.toJson(selectedBudget)), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", selectedBudget.getName() + " was successfully modified"));
				PrimeFaces.current().ajax().update("budgetForm");
				PrimeFaces.current().executeScript("PF('editBudgetDlg').hide()");
				PrimeFaces.current().executeScript("PF('addDlg').hide()");
				PrimeFaces.current().executeScript("PF('minusDlg').hide()");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem saving the budget"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void adjustFunds(int amt) {
		selectedBudget.setRemaining(selectedBudget.getRemaining() + (amountToAdjust * amt));
		editBudget();
	}
}
