/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prt.models.User;
import com.prt.utils.EmailUtils;
import com.prt.utils.EncryptionHelper;
import com.prt.utils.RestUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.RandomStringUtils;
import org.primefaces.PrimeFaces;

/**
 *
 * @author P-ratt
 */
@ManagedBean(name = "usersController")
@ViewScoped
public class UsersController implements Serializable {

	@ManagedProperty("#{guestPreferences}")
	private GuestPreferences preferences;
	private ArrayList<User> users = new ArrayList<>();
	private User newUser;
	private String selectedUserGuid;
	private User selectedUser;
	private String tempPass;

	public String getTempPass() {
		return tempPass;
	}

	public void setTempPass(String tempPass) {
		this.tempPass = tempPass;
	}

	public GuestPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(GuestPreferences preferences) {
		this.preferences = preferences;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public User getNewUser() {
		return newUser;
	}

	public void setNewUser(User newUser) {
		this.newUser = newUser;
	}

	public String getSelectedUserGuid() {
		return selectedUserGuid;
	}

	public void setSelectedUserGuid(String selectedUserGuid) {
		this.selectedUserGuid = selectedUserGuid;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	@PostConstruct
	void init() {
		try {
			//grab all users and display them
			Gson gson = new Gson();
			users = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/user/select/all", null), new TypeToken<ArrayList<User>>() {
			}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startAddUser() {
		newUser = new User(preferences.userGuid);
	}

	public void updateSelectedUser(User selected) {
		selectedUser = selected;
	}

	public void addUser() {
		try {
			if (newUser.getUsername().contains(" ")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username cannot have any white space in it"));
			} else {
				//put together password on this side so it can be emailed
				String newPassword = RandomStringUtils.random(8, true, true);

				byte[] salt = EncryptionHelper.generateSalt();
				String saltStr = new String(Base64.getEncoder().encode(salt), "UTF-8");
				String password = EncryptionHelper.encrypt(newPassword, salt);
				newUser.setPassword(password);
				newUser.setSalt(saltStr);
				tempPass = newPassword;

				Gson gson = new Gson();
				String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/user/add", gson.toJson(newUser)), String.class);
				if (result != null && result.equalsIgnoreCase("true")) {
					init();
					PrimeFaces.current().executeScript("PF('addUserDlg').hide()");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User was successfully added"));
					PrimeFaces.current().ajax().update("userForm");
					PrimeFaces.current().ajax().update("tempPasswordForm");
					PrimeFaces.current().executeScript("PF('tempPasswordDlg').show()");
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem adding the new user"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void editUser(User user) {
		selectedUser = user;
		editUser();
	}

	public void editUser() {
		try {
			if (selectedUser.getUsername().contains(" ")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username cannot have any white space in it"));
			} else {
				Gson gson = new Gson();
				String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/user/edit", gson.toJson(selectedUser)), String.class);
				if (result != null && result.equalsIgnoreCase("true")) {
					init();
					PrimeFaces.current().executeScript("PF('editUserDlg').hide()");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User was successfully modified"));
					PrimeFaces.current().ajax().update("userForm");
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem modifying the existing user"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteUser() {
		try {
			Gson gson = new Gson();
			String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/user/remove", gson.toJson(selectedUser)), String.class);
			if (result != null && result.equalsIgnoreCase("true")) {
				init();
				PrimeFaces.current().executeScript("PF('deleteUserDlg').hide()");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User was successfully removed"));
				PrimeFaces.current().ajax().update("userForm");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem removing the existing user"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendPasswordReset() {
		try {
			if (selectedUser.getEmail() != null && !selectedUser.getEmail().isEmpty()) {
				String message = "Hello " + selectedUser.getFirstname() + ",\n\nWe were informed that you need to reset your password. You may do so by following the link below.\n\n{link here}";
				boolean success = EmailUtils.sendEmail(selectedUser.getEmail(), "Password Reset", message);
				if (success) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Email Sent", "An email has been sent to the user to reset their password"));
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem sending an email to the recipient"));
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There is no email registered with the user to send an email"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void emailPassword() {
		try {
			//email password to registered email
			if (newUser.getEmail() != null && !newUser.getEmail().isEmpty()) {
				String message = "Hello " + newUser.getFirstname() + ",\n\nA new account has been created for you on Clearfield Requestor.\n\nYour temporary password is:\n" + tempPass + "\n\nYou may log in and change your password immediately.\n\nThank you,\nThe Clearfield Requestor Team";
				boolean success = EmailUtils.sendEmail(newUser.getEmail(), "Temporary Password", message);
				if (success) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Email Sent", "Email successfully sent"));
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem sending an email to the recipient"));
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There is no email registered with the user to send an email"));
			}
			PrimeFaces.current().ajax().update("userForm");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
