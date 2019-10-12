/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
@ManagedBean(name = "usersController")
@ViewScoped
public class UsersController implements Serializable {

    @ManagedProperty("#{guestPreferences}")
    private GuestPreferences preferences;
    private ArrayList<User> users = new ArrayList<>();
    private User newUser;
    private String selectedUserGuid;
    private User selectedUser;

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
        newUser = new User(preferences.getUserGuid());
    }

    public void prepareEdit(User selected) {
        selectedUser = selected;
    }

    public void addUser() {
        try {
            if (newUser.getUsername().contains(" ")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username cannot have any white space in it"));
            } else {
                Gson gson = new Gson();
                String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/user/add", gson.toJson(newUser)), String.class);
                if (result != null && result.equalsIgnoreCase("true")) {
                    init();
                    PrimeFaces.current().ajax().update("userForm");
                    PrimeFaces.current().executeScript("PF('addUserDlg').hide()");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User was successfully added"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem adding the new user"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    PrimeFaces.current().ajax().update("userForm");
                    PrimeFaces.current().executeScript("PF('editUserDlg').hide()");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User was successfully edited"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem editing the existing user"));
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
                PrimeFaces.current().ajax().update("userForm");
                PrimeFaces.current().executeScript("PF('deleteUserDlg').hide()");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User was successfully deleted"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem deleting the existing user"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPasswordReset(User user) {
        try {
            //do the things to email a password reset link to the user
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Email Sent", "An email has been sent to the user to reset their password"));
            PrimeFaces.current().ajax().update("userForm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
