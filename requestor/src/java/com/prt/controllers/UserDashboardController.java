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
import com.prt.models.Event;
import com.prt.models.Item;
import com.prt.utils.RestUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author P-ratt
 */
@ManagedBean(name = "userDashboardController")
@ViewScoped
public class UserDashboardController implements Serializable {

	@ManagedProperty("#{guestPreferences}")
	private GuestPreferences preferences;
	private Dashboard dashboard;
	private String selectedDashboard;
	private ArrayList<Dashboard> dashboards = new ArrayList<>();

	public String getSelectedDashboard() {
		return selectedDashboard;
	}

	public void setSelectedDashboard(String selectedDashboard) {
		this.selectedDashboard = selectedDashboard;
	}

	public ArrayList<Dashboard> getDashboards() {
		return dashboards;
	}

	public void setDashboards(ArrayList<Dashboard> dashboards) {
		this.dashboards = dashboards;
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

	@PostConstruct
	void init() {
		try {
			Gson gson = new Gson();
			dashboard = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "dashboard/user/select/default", gson.toJson(preferences.userGuid)), Dashboard.class);
			if (dashboard != null) {
				selectedDashboard = dashboard.getGuid();
				buildDashboard();
			}

			//get all dashboards
			dashboards = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/dashboard/user/select/all", gson.toJson(preferences.userGuid)), new TypeToken<ArrayList<Dashboard>>() {
			}.getType());
			selectedDashboard = dashboard.getXrefGuid();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectDashboard(ValueChangeEvent event) {
		String guid = (String) event.getNewValue();
		if (guid != null) {
			Gson gson = new Gson();
			dashboard = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "dashboard/user/select", gson.toJson(new String[]{guid, preferences.userGuid})), Dashboard.class);

			if (dashboard != null) {
				//load up the things before refreshing form
				buildDashboard();
			}
		}
	}

	private void buildDashboard() {
		try {
			ArrayList<Item> items = new ArrayList<>();
			for (Calendar cal : dashboard.getCalendars()) {
				Item item = new Item();
				item.setGuid(cal.getXrefGuid());
				item.setCalendarGuid(cal.getGuid());
				item.setName(cal.getName());
				item.setDesc(cal.getDesc());
				item.setIndex(cal.getIndex());
				item.setEvents(cal.getEvents());
				item.setType("Calendar");
				items.add(item);
			}
			for (Budget bud : dashboard.getBudgets()) {
				Item item = new Item();
				item.setGuid(bud.getXrefGuid());
				item.setBudgetGuid(bud.getGuid());
				item.setName(bud.getName());
				item.setDesc(bud.getDesc());
				item.setIndex(bud.getIndex());
				item.setTransactions(bud.getTransactions());
				item.setType("Budget");
				items.add(item);
			}

			Collections.sort(items, (o1, o2) -> {
				return Integer.compare(o1.getIndex(), o2.getIndex());
			});
			dashboard.setItems(items);

			//loop through dashboard objects to display
			for (Item item : dashboard.getItems()) {
				switch (item.getType()) {
					case "Calendar":
						ScheduleModel calendar = new DefaultScheduleModel();
						for (Event event : item.getEvents()) {
							if (event.getStart() != null && event.getEnd() != null) {
								DefaultScheduleEvent se = new DefaultScheduleEvent(event.getTitle(), event.getStart(), event.getEnd());
								se.setDescription(event.getSummary());
								calendar.addEvent(se);
							}
						}
						item.setCalendar(calendar);
						break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateDashboard() {
		try {
			for (Dashboard d : dashboards) {
				if (d.getGuid().equals(selectedDashboard)) {
					dashboard = d;
					break;
				}
			}
			if (dashboard != null) {
				buildDashboard();
			}

			PrimeFaces.current().ajax().update("dashboardForm");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
