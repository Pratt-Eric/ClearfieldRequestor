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
@ManagedBean(name = "calendarsController")
@ViewScoped
public class CalendarsController implements Serializable {

	@ManagedProperty("#{guestPreferences}")
	private GuestPreferences preferences;
	private ArrayList<Calendar> calendars = new ArrayList<>();
	private String selectedCalGuid;
	private Calendar selectedCalendar;
	private Calendar newCalendar;

	public Calendar getNewCalendar() {
		return newCalendar;
	}

	public void setNewCalendar(Calendar newCalendar) {
		this.newCalendar = newCalendar;
	}

	public GuestPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(GuestPreferences preferences) {
		this.preferences = preferences;
	}

	public ArrayList<Calendar> getCalendars() {
		return calendars;
	}

	public void setCalendars(ArrayList<Calendar> calendars) {
		this.calendars = calendars;
	}

	public String getSelectedCalGuid() {
		return selectedCalGuid;
	}

	public void setSelectedCalGuid(String selectedCalGuid) {
		this.selectedCalGuid = selectedCalGuid;
	}

	public Calendar getSelectedCalendar() {
		return selectedCalendar;
	}

	public void setSelectedCalendar(Calendar selectedCalendar) {
		this.selectedCalendar = selectedCalendar;
	}

	@PostConstruct
	void init() {
		try {
			newCalendar = new Calendar();
			Gson gson = new Gson();
			calendars = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/calendar/select/all", null), new TypeToken<ArrayList<Calendar>>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateSelectedCalendar(Calendar calendar) {
		selectedCalendar = calendar;
	}

	public void prepareAddNewCalendar() {
		newCalendar = new Calendar();
	}

	public void addNewCalendar() {
		try {
			Gson gson = new Gson();
			String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/calendar/add", gson.toJson(newCalendar)), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "The new calendar was successfully added"));
				init();
				PrimeFaces.current().ajax().update("calendarForm");
				PrimeFaces.current().executeScript("PF('calendarAddDlg').hide()");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem adding your new calendar"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void editExistingCalendar() {
		try {
			Gson gson = new Gson();
			String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/calendar/edit", gson.toJson(selectedCalendar)), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "The existing calendar was successfully modified"));
				init();
				PrimeFaces.current().ajax().update("calendarForm");
				PrimeFaces.current().executeScript("PF('calendarEditDlg').hide()");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem modifying the existing calendar"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteExistingCalendar() {
		try {
			Gson gson = new Gson();
			String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/calendar/remove", gson.toJson(newCalendar)), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "The existing calendar was successfully deleted"));
				init();
				PrimeFaces.current().ajax().update("calendarForm");
				PrimeFaces.current().executeScript("PF('calendarDeleteDlg').hide()");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem deleting the existing calendar"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dumb(Calendar calendar) {
		System.out.println("stupid");
	}

	public String prepareAddUsersAndGroups(Calendar calendar) {
		preferences.selectedCalendar = calendar;
		return "/main/calendarpermissions.xhtml?faces-redirect=true";
	}

}
