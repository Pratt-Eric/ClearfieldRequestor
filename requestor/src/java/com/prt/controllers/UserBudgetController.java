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
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author P-ratt
 */
@ManagedBean(name = "userBudgetController")
@ViewScoped
public class UserBudgetController implements Serializable {

	@ManagedProperty("#{guestPreferences}")
	private GuestPreferences preferences;
	private Budget selectedBudget;
	private String selectedBudgetGuid;
	private double amountToAdjust = 0;
	private TreeNode budgets;

	public TreeNode getBudgets() {
		return budgets;
	}

	public void setBudgets(TreeNode budgets) {
		this.budgets = budgets;
	}

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

	public GuestPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(GuestPreferences preferences) {
		this.preferences = preferences;
	}

	@PostConstruct
	void init() {
		budgets = new DefaultTreeNode(null);
		budgets.setExpanded(true);
		try {
			Gson gson = new Gson();
			ArrayList<Budget> budgetList = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/budget/select/all/user", gson.toJson(preferences.userGuid)), new TypeToken<ArrayList<Budget>>() {
			}.getType());
			assignBudgetHierarchy(budgetList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void assignBudgetHierarchy(ArrayList<Budget> budgetList) {
		//first find all that don't have a parent
		for (Budget budget : budgetList) {
			if (budget.getParentGuid() == null) {
				TreeNode newNode = new DefaultTreeNode(budget, budgets);
				newNode.setExpanded(true);
			}
		}

		//now loop through those that have a parent, find the parent and assign it to the parent accordingly
		for (Budget budget : budgetList) {
			if (budget.getParent() != null) {
				TreeNode node = findNode(budgets, budget.getParent());
				TreeNode newNode = new DefaultTreeNode(budget, node);
				newNode.setExpanded(true);
			}
		}

	}

	public TreeNode findNode(TreeNode root, Budget toFind) {
		TreeNode result = null;
		for (TreeNode child : root.getChildren()) {
			Budget budget = (Budget) child.getData();
			if (budget.getGuid().equals(toFind.getGuid())) {
				result = child;
				break;
			} else {
				TreeNode found = findNode(child, toFind);
				if (found != null) {
					result = found;
					break;
				}
			}
		}
		return result;
	}

	public void updateSelectedBudget(Budget budget) {
		selectedBudget = budget;
		amountToAdjust = 0;
	}

	public void editBudget(Budget budget) {
		selectedBudget = budget;
		editBudget();
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
