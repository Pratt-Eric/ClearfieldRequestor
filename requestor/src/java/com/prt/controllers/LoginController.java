/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.controllers;

import com.google.gson.Gson;
import com.prt.models.User;
import com.prt.utils.EncryptionHelper;
import com.prt.utils.RestUtil;
import java.io.Serializable;
import java.util.Base64;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author P-ratt
 */
@ManagedBean(name = "loginController")
@ViewScoped
public class LoginController implements Serializable {

    private String username;
    private String password;

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
        String success = gson.fromJson(RestUtil.post("http://localhost:8080/requestordt/webresources/data/ugh", gson.toJson(null)), String.class);
        if (success == null || success.equalsIgnoreCase("false")) {
            //There was an error initializing the system
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was an error initializing the system"));
        }
    }

    public String login() {
        //Get user information and check login info
        try {
            Gson gson = new Gson();
            User user = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "repository/user/select", gson.toJson(username)), User.class);
            if (user != null) {
                //compare the password returned with the password provided
                //first, hash the given password
                //then compare
                if (user.getPassword() != null && user.getSalt() != null) {
                    String compare = EncryptionHelper.encrypt(password, Base64.getDecoder().decode(user.getSalt()));
                    if (compare != null && compare.equals(user.getPassword())) {
                        return "/main/dashboard.xhtml?faces-redirect=true";
                    }
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username and/or Password don't match"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
