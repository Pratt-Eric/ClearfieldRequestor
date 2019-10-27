/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author P-ratt
 */
public class User implements Serializable {

	private String guid;
	private String username;
	private String firstname;
	private String lastname;
	private String email;
	private String password_guid;
	private String password;
	private String salt;
	private String picture;
	private String calling;
	private String createdBy;
	private boolean admin;
	private boolean editBudget;
	private boolean selected;
	private boolean editCalendar;
	private ArrayList<Group> groups = new ArrayList<>();
	private String city;
	private String zip;
	private String state;
	private String address1;
	private String address2;

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public ArrayList<Group> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<Group> groups) {
		this.groups = groups;
	}

	public boolean isEditCalendar() {
		return editCalendar;
	}

	public void setEditCalendar(boolean editCalendar) {
		this.editCalendar = editCalendar;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isEditBudget() {
		return editBudget;
	}

	public void setEditBudget(boolean editBudget) {
		this.editBudget = editBudget;
	}

	public User() {

	}

	public User(String createdBy) {
		this.createdBy = createdBy;
	}

	public User(String guid, String username, String firstname, String lastname, String email, String password_guid, boolean admin) {
		this.guid = guid;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password_guid = password_guid;
		this.admin = admin;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCalling() {
		return calling;
	}

	public void setCalling(String calling) {
		this.calling = calling;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getPassword_guid() {
		return password_guid;
	}

	public void setPassword_guid(String password_guid) {
		this.password_guid = password_guid;
	}

}
