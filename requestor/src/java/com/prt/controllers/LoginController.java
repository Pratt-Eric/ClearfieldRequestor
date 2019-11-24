/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prt.models.Activity;
import com.prt.models.User;
import com.prt.utils.EncryptionHelper;
import com.prt.utils.RestUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;

/**
 *
 * @author P-ratt
 */
@ManagedBean(name = "loginController")
@ViewScoped
public class LoginController implements Serializable {

	@ManagedProperty("#{guestPreferences}")
	private GuestPreferences preferences;
	private String username;
	private String password;

	public GuestPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(GuestPreferences preferences) {
		this.preferences = preferences;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@PostConstruct
	void init() {
		//create admin user if it doesn't already exist
		Gson gson = new Gson();
		String success = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/initialize", gson.toJson(null)), String.class);
		if (success == null || success.equalsIgnoreCase("false")) {
			//There was an error initializing the system
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was an error initializing the system"));
		}
	}

	public String login() {
		//Get user information and check login info
		try {
			Gson gson = new Gson();
			User user = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/user/select", gson.toJson(username)), User.class);
			if (user != null) {
				//compare the password returned with the password provided
				//first, hash the given password
				//then compare
				if (user.getPassword() != null && user.getSalt() != null) {
					String compare = EncryptionHelper.encrypt(password, Base64.getDecoder().decode(user.getSalt()));
					if (compare != null && compare.equals(user.getPassword())) {
						//grab user information including avatar if it exists
						//set user preferences before redirecting
						preferences.username = user.getUsername();
						preferences.calling = user.getCalling();
						preferences.userGuid = user.getGuid();
						preferences.firstname = user.getFirstname();
						preferences.lastname = user.getLastname();
						preferences.admin = user.isAdmin();
						//get all request types to load preferences
						ArrayList<String> requestTypes = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/types/request", null), new TypeToken<ArrayList<String>>() {
						}.getType());
						preferences.menu = new DefaultMenuModel();
						for (String rt : requestTypes) {
							DefaultMenuItem item = new DefaultMenuItem(rt);
							item.setOncomplete("PF('" + (rt.equalsIgnoreCase("Activity") ? "requestActivityDlg" : rt.equalsIgnoreCase("Reimbursement") ? "requestReimbursementDlg" : rt.equalsIgnoreCase("Expense") ? "requestExpenseDlg" : "") + "').show()");
							item.setUpdate(rt.equalsIgnoreCase("Activity") ? "requestActivityForm" : rt.equalsIgnoreCase("Reimbursement") ? "requestReimbursementForm" : rt.equalsIgnoreCase("Expense") ? "requestExpenseForm" : "");
							item.setCommand("#{templateController.prepareRequest}");
							//grab activity types and assign them to guestPreferences.activities
							preferences.activities = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/activity/select/all", null), new TypeToken<ArrayList<Activity>>() {
							}.getType());
							preferences.menu.addElement(item);
						}
						preferences.users = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/user/select/all", null), new TypeToken<ArrayList<User>>() {
						}.getType());

						return "/main/user/dashboard.xhtml?faces-redirect=true";
					}
				}
			}
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username and/or Password don't match"));
			PrimeFaces.current().ajax().update("loginform");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String logout() {
		preferences.username = null;
		preferences.calling = null;
		preferences.userGuid = null;
		return "/logout.xhtml?faces-redirect=true";
	}

	public String navigate(String location) {
		return "/main/user/" + location + "?faces-redirect=true";
	}
}
