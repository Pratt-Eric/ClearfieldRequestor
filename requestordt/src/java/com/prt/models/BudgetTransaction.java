/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.models;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author P-ratt
 */
public class BudgetTransaction implements Serializable {

	private String guid;
	private String name;
	private String desc;
	private String justification;
	private Date date;
	private String paidTo;
	private User paidToUser;
	private double amount = 0;
	private String authorizedBy;
	private User authorizedByUser;
	private String checkNumber;
	private String fastOfferingCode;
	private Budget budget;
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPaidTo() {
		return paidTo;
	}

	public void setPaidTo(String paidTo) {
		this.paidTo = paidTo;
	}

	public User getPaidToUser() {
		return paidToUser;
	}

	public void setPaidToUser(User paidToUser) {
		this.paidToUser = paidToUser;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getAuthorizedBy() {
		return authorizedBy;
	}

	public void setAuthorizedBy(String authorizedBy) {
		this.authorizedBy = authorizedBy;
	}

	public User getAuthorizedByUser() {
		return authorizedByUser;
	}

	public void setAuthorizedByUser(User authorizedByUser) {
		this.authorizedByUser = authorizedByUser;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public String getFastOfferingCode() {
		return fastOfferingCode;
	}

	public void setFastOfferingCode(String fastOfferingCode) {
		this.fastOfferingCode = fastOfferingCode;
	}

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

}
