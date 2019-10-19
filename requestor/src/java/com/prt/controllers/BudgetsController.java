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
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author P-ratt
 */
@ManagedBean(name = "budgetsController")
@ViewScoped
public class BudgetsController {

	@ManagedProperty("#{guestPreferences}")
	private GuestPreferences preferences;
	private TreeNode budgets;
	private Budget newBudget;
	private String selectedBudgetGuid;
	private Budget selectedBudget;
	private ArrayList<String> usersToAdd = new ArrayList<>();
	private ArrayList<String> groupsToAdd = new ArrayList<>();
	private ArrayList<User> users = new ArrayList<>();
	private ArrayList<Group> groups = new ArrayList<>();

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

	public ArrayList<String> getUsersToAdd() {
		return usersToAdd;
	}

	public void setUsersToAdd(ArrayList<String> usersToAdd) {
		this.usersToAdd = usersToAdd;
	}

	public ArrayList<String> getGroupsToAdd() {
		return groupsToAdd;
	}

	public void setGroupsToAdd(ArrayList<String> groupsToAdd) {
		this.groupsToAdd = groupsToAdd;
	}

	public Budget getNewBudget() {
		return newBudget;
	}

	public void setNewBudget(Budget newBudget) {
		this.newBudget = newBudget;
	}

	public String getSelectedBudgetGuid() {
		return selectedBudgetGuid;
	}

	public void setSelectedBudgetGuid(String selectedBudgetGuid) {
		this.selectedBudgetGuid = selectedBudgetGuid;
	}

	public Budget getSelectedBudget() {
		return selectedBudget;
	}

	public void setSelectedBudget(Budget selectedBudget) {
		this.selectedBudget = selectedBudget;
	}

	public TreeNode getBudgets() {
		return budgets;
	}

	public void setBudgets(TreeNode budgets) {
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
		budgets = new DefaultTreeNode(null);
		Gson gson = new Gson();
		try {
			ArrayList<Budget> budgetList = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/budget/select/all", null), new TypeToken<ArrayList<Budget>>() {
			}.getType());
			groups = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/group/select/all", null), new TypeToken<ArrayList<Group>>() {
			}.getType());
			users = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/user/select/all", null), new TypeToken<ArrayList<User>>() {
			}.getType());
			//organize budgets into treeNode
			assignBudgetHierarchy(budgetList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void assignBudgetHierarchy(ArrayList<Budget> budgetList) {
		//first find all that don't have a parent
		for (Budget budget : budgetList) {
			if (budget.getParent() == null) {
				TreeNode newNode = new DefaultTreeNode(budget, budgets);
			}
		}

		//now loop through those that have a parent, find the parent and assign it to the parent accordingly
		for (Budget budget : budgetList) {
			if (budget.getParent() != null) {
				TreeNode node = findNode(budgets, budget);
				TreeNode newNode = new DefaultTreeNode(budget, node);
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

	public void prepareAddNewBudget(Budget parent) {
		newBudget = new Budget();
		newBudget.setParent(parent);
	}

	public void selectBudget(Budget budget) {
		selectedBudget = budget;
	}

	public void addNewBudget() {
		try {
			Gson gson = new Gson();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void editExistingBudget() {
		try {
			Gson gson = new Gson();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteExistingBudget() {
		try {
			Gson gson = new Gson();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addUsersAndGroupsToBudget() {
		try {
			Gson gson = new Gson();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void assignUsersAndGroupsToBudget() {
		try {
			Gson gson = new Gson();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
