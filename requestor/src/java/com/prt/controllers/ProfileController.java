/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.controllers;

import com.google.gson.Gson;
import com.prt.models.Password;
import com.prt.models.User;
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
@ManagedBean(name = "profileController")
@ViewScoped
public class ProfileController implements Serializable {

	@ManagedProperty("#{guestPreferences}")
	private GuestPreferences preferences;
	private User currUser;
	private Password newPassword;

	public Password getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(Password newPassword) {
		this.newPassword = newPassword;
	}

	public GuestPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(GuestPreferences preferences) {
		this.preferences = preferences;
	}

	public User getCurrUser() {
		return currUser;
	}

	public void setCurrUser(User currUser) {
		this.currUser = currUser;
	}

	@PostConstruct
	void init() {
		try {
			Gson gson = new Gson();
			currUser = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/user/select/byguid", gson.toJson(preferences.userGuid)), User.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void preparePasswordChange() {
		newPassword = new Password();
	}

	public void updatePassword() {
		try {
			String regex = "((?=.*[a-z])(?=.*d)(?=.*[@#$%])(?=.*[A-Z]).{8,24})";
			//check both passwords to make sure they match
			ArrayList<String> errors = new ArrayList<>();
			if (!newPassword.getPassword().equals(newPassword.getRepeatPassword())) {
				errors.add("The two passwords don't match");
			}
			//check regex to make sure all criteria is met
			if (!newPassword.getPassword().matches(regex)) {
				errors.add("The password provided does not meet the password requirements");
			}

			if (errors.isEmpty()) {
				Gson gson = new Gson();
				currUser.setPassword(newPassword.getPassword());
				String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/user/password/update", gson.toJson(currUser)), String.class);
				if (result != null && result.equalsIgnoreCase("true")) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Your password was successfully updated"));
					PrimeFaces.current().executeScript("PF('passwordChangeDlg').hide()");
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem updating your password"));
				}
			} else {
				for (String error : errors) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", error));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void editUser() {
		try {
			Gson gson = new Gson();
			String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/user/edit", gson.toJson(currUser)), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Your profile was successfully updated"));
				init();
				preferences.firstname = currUser.getFirstname();
				preferences.lastname = currUser.getLastname();
				preferences.calling = currUser.getCalling();
				PrimeFaces.current().ajax().update("profileForm");
				PrimeFaces.current().ajax().update("headerForm");
				PrimeFaces.current().ajax().update("topbarNameForm");
				PrimeFaces.current().ajax().update("topbarPopupNameForm");
				PrimeFaces.current().ajax().update("topbarPopupCallingForm");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem updating your profile"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
