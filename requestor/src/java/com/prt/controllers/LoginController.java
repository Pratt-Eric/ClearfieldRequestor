/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.controllers;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author P-ratt
 */
@ManagedBean(name = "loginController")
@ViewScoped
public class LoginController implements Serializable {

	@PostConstruct
	void init() {
		//create admin user if it doesn't already exist
	}

	public String login() {

		return "/main/dashboard.xhtml?faces-redirect=true";
	}
}
