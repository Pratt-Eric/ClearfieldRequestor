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
import java.util.Collections;
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
	private ArrayList<Budget> availBuds = new ArrayList<>();
	private ArrayList<Calendar> availCals = new ArrayList<>();
	private ArrayList<String> selectedBudgets = new ArrayList<>();
	private ArrayList<String> selectedCalendars = new ArrayList<>();
	private ArrayList<Item> items = new ArrayList<>();
	private Dashboard dashboard;

	public ArrayList<Budget> getAvailBuds() {
		return availBuds;
	}

	public void setAvailBuds(ArrayList<Budget> availBuds) {
		this.availBuds = availBuds;
	}

	public ArrayList<Calendar> getAvailCals() {
		return availCals;
	}

	public void setAvailCals(ArrayList<Calendar> availCals) {
		this.availCals = availCals;
	}

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
			loadDashboardItems();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadDashboardItems() {
		items = new ArrayList<>();
		for (Calendar calendar : dashboard.getCalendars()) {
			Item item = new Item();
			item.setGuid(calendar.getXrefGuid());
			item.setName(calendar.getName());
			item.setType("Calendar");
			item.setDesc(calendar.getDesc());
			item.setIndex(calendar.getIndex());
			item.setCalendarGuid(calendar.getGuid());
			items.add(item);
		}
		for (Budget budget : dashboard.getBudgets()) {
			Item item = new Item();
			item.setGuid(budget.getXrefGuid());
			item.setName(budget.getName());
			item.setType("Budget");
			item.setDesc(budget.getDesc());
			item.setIndex(budget.getIndex());
			item.setBudgetGuid(budget.getGuid());
			items.add(item);
		}

		Collections.sort(items, (o1, o2) -> {
			return Integer.compare(o1.getIndex(), o2.getIndex());
		});

		dashboard.setItems(items);
	}

	private void reloadDashboardProperties() {
		try {
			Gson gson = new Gson();
			dashboard = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/dashboard/select", gson.toJson(dashboard.getGuid())), Dashboard.class);
			loadDashboardItems();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addItems() {
		try {
			//Add selected cals and buds to existing lists but only if the cal and bud is not already in the list
			for (String calendar : selectedCalendars) {
				boolean found = false;
				for (Calendar cal : dashboard.getCalendars()) {
					if (cal.getGuid().equals(calendar)) {
						found = true;
						break;
					}
				}
				if (!found) {
					for (Calendar cal : availCals) {
						boolean calFound = false;
						for (Calendar c : dashboard.getCalendars()) {
							if (c.getGuid().equals(cal.getGuid())) {
								calFound = true;
								break;
							}
						}
						if (cal.getGuid().equals(calendar) && !calFound) {
							dashboard.getCalendars().add(cal);
						}
					}
				}
			}
			for (String budget : selectedBudgets) {
				boolean found = false;
				for (Budget bud : dashboard.getBudgets()) {
					if (bud.getGuid().equals(budget)) {
						found = true;
						break;
					}
				}
				if (!found) {
					for (Budget bud : availBuds) {
						boolean budFound = false;
						for (Budget b : dashboard.getBudgets()) {
							if (b.getGuid().equals(bud.getGuid())) {
								budFound = true;
								break;
							}
						}
						if (bud.getGuid().equals(budget) && !budFound) {
							dashboard.getBudgets().add(bud);
						}
					}
				}
			}
			loadDashboardItems();
			saveDashboard();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveDashboard() {
		try {
			Gson gson = new Gson();
			String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/dashboard/edit", gson.toJson(dashboard)), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Dashboard was successfully modified"));
				reloadDashboardProperties();
				PrimeFaces.current().executeScript("PF('addItemsDlg').hide()");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem modifying your dashboard"));
			}
			PrimeFaces.current().ajax().update("dashboardForm");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void move(Item item, int dir) {
		try {
			int currIndex = items.indexOf(item);
			int newIndex = currIndex + dir;
			if (newIndex > -1 && newIndex < items.size()) {
				items.remove(item);
				items.add(newIndex, item);
				saveDashboard();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Item item) {
		try {
			items.remove(item);
			saveDashboard();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startAddComponents() {
		//only add calendars and budgets that aren't already added to the dashboard
		selectedBudgets = new ArrayList<>();
		selectedCalendars = new ArrayList<>();
		availCals = new ArrayList<>();
		availBuds = new ArrayList<>();
		for (Calendar cal : calendars) {
			boolean found = false;
			for (Calendar c : dashboard.getCalendars()) {
				if (c.getGuid().equals(cal.getGuid())) {
					found = true;
					break;
				}
			}
			if (!found) {
				availCals.add(cal);
			}
		}
		for (Budget bud : budgets) {
			boolean found = false;
			for (Budget b : dashboard.getBudgets()) {
				if (b.getGuid().equals(bud.getGuid())) {
					found = true;
					break;
				}
			}
			if (!found) {
				availBuds.add(bud);
			}
		}
	}
}
