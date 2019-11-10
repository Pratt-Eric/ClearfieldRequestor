package com.prt.controllers;

import com.prt.models.Budget;
import com.prt.models.Calendar;
import com.prt.models.Dashboard;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class GuestPreferences implements Serializable {

	private String menuMode = "layout-slim";

	private String theme = "bluegrey-teal";

	private String menuColor = "layout-menu-light";

	private String topBarColor = "layout-topbar-bluegrey";

	private String logo = "logo-olympia-white";

	public String username;
	public String firstname;
	public String lastname;
	public String calling;
	public String userGuid;
	public ByteArrayOutputStream stream = new ByteArrayOutputStream();
	public String imgExt = "";
	public Budget selectedBudget = null;
	public Calendar selectedCalendar = null;
	public Dashboard selectedDashboard = null;

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCalling() {
		return calling;
	}

	public void setCalling(String calling) {
		this.calling = calling;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getMenuMode() {
		return this.menuMode;
	}

	public void setMenuMode(String menuMode) {
		this.menuMode = menuMode;
	}

	public String getMenuColor() {
		return this.menuColor;
	}

	public void setMenuColor(String menuColor) {
		this.menuColor = menuColor;
	}

	public String getTopBarColor() {
		return this.topBarColor;
	}

	public void setTopBarColor(String topBarColor, String logo) {
		this.topBarColor = topBarColor;
		this.logo = logo;
	}

	public String getLogo() {
		return this.logo;
	}

	public String isLoggedInForwardHome() {

		return null;
	}
}
