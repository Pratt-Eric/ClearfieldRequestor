/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prt.models.Budget;
import com.prt.models.Calendar;
import com.prt.models.Dashboard;
import com.prt.models.Item;
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
@ManagedBean(name = "dashboardSetupController")
@ViewScoped
public class DashboardSetupController implements Serializable {

	@ManagedProperty("#{guestPreferences}")
	private GuestPreferences preferences;
	private ArrayList<Budget> budgets = new ArrayList<>();
	private ArrayList<Calendar> calendars = new ArrayList<>();
	private ArrayList<String> selectedBudgets = new ArrayList<>();
	private ArrayList<String> selectedCalendars = new ArrayList<>();
	private ArrayList<Item> items = new ArrayList<>();
	private Dashboard dashboard;

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public ArrayList<String> getSelectedBudgets() {
		return selectedBudgets;
	}

	public void setSelectedBudgets(ArrayList<String> selectedBudgets) {
		this.selectedBudgets = selectedBudgets;
	}

	public ArrayList<String> getSelectedCalendars() {
		return selectedCalendars;
	}

	public void setSelectedCalendars(ArrayList<String> selectedCalendars) {
		this.selectedCalendars = selectedCalendars;
	}

	public Dashboard getDashboard() {
		return dashboard;
	}

	public void setDashboard(Dashboard dashboard) {
		this.dashboard = dashboard;
	}

	public GuestPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(GuestPreferences preferences) {
		this.preferences = preferences;
	}

	public ArrayList<Budget> getBudgets() {
		return budgets;
	}

	public void setBudgets(ArrayList<Budget> budgets) {
		this.budgets = budgets;
	}

	public ArrayList<Calendar> getCalendars() {
		return calendars;
	}

	public void setCalendars(ArrayList<Calendar> calendars) {
		this.calendars = calendars;
	}

	@PostConstruct
	void init() {
		try {
			dashboard = preferences.selectedDashboard;
			Gson gson = new Gson();
			budgets = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/budget/select/all", null), new TypeToken<ArrayList<Budget>>() {
			}.getType());
			calendars = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/calendar/select/all", null), new TypeToken<ArrayList<Calendar>>() {
			}.getType());

			for (Calendar calendar : dashboard.getCalendars()) {
				Item item = new Item();
				item.setName(calendar.getName());
				item.setType("Calendar");
				item.setDesc(calendar.getDesc());
				items.add(item);
			}
			for (Budget budget : dashboard.getBudgets()) {
				Item item = new Item();
				item.setName(budget.getName());
				item.setType("Budget");
				item.setDesc(budget.getDesc());
				items.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void editDashboard() {
		try {
			//this won't work because we need to be able to add new items without affecting the ones that are alraedy added
			//they also need to be in order when I send the dashboard back to be edited.
			dashboard.setCalendars(new ArrayList<>());
			dashboard.setBudgets(new ArrayList<>());
			for (String calendar : selectedCalendars) {
				for (Calendar c : calendars) {
					if (c.getGuid().equals(calendar)) {
						if (!dashboard.getCalendars().contains(c)) {
							dashboard.getCalendars().add(c);
						}
						break;
					}
				}
			}
			for (String budget : selectedBudgets) {
				for (Budget b : budgets) {
					if (b.getGuid().equals(budget)) {
						if (!dashboard.getBudgets().contains(b)) {
							dashboard.getBudgets().add(b);
						}
						break;
					}
				}
			}
			Gson gson = new Gson();
			String result = gson.toJson(RestUtil.post(RestUtil.BASEURL + "/dashboard/edit", gson.toJson(dashboard)), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Dashboard was successfully modified"));
				init();
				PrimeFaces.current().executeScript("PF('addItemsForm').hide()");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem modifying your dashboard"));
			}
			PrimeFaces.current().ajax().update("dashboardForm");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void move(int dir) {
		try {
			Gson gson = new Gson();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Item item) {
		try {
			Gson gson = new Gson();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startAddComponents() {
		selectedCalendars = new ArrayList<>();
		selectedBudgets = new ArrayList<>();
		for (Calendar calendar : dashboard.getCalendars()) {
			if (!selectedCalendars.contains(calendar.getGuid())) {
				selectedCalendars.add(calendar.getGuid());
			}
		}
		for (Budget budget : dashboard.getBudgets()) {
			if (!selectedBudgets.contains(budget.getGuid())) {
				selectedBudgets.add(budget.getGuid());
			}
		}
	}
}
