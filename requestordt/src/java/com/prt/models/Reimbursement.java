/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.models;

import java.io.Serializable;

/**
 *
 * @author P-ratt
 */
public class Reimbursement implements Serializable {

	private float amt;
	private float tax;
	private float total;
	private boolean wardAccount;
	private String wardAccountDetails;
	private String org;
	private String orgName;
	private String orgLeader;
	private String userGuid;

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getWardAccountDetails() {
		return wardAccountDetails;
	}

	public void setWardAccountDetails(String wardAccountDetails) {
		this.wardAccountDetails = wardAccountDetails;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public String getUserGuid() {
		return userGuid;
	}

	public void setUserGuid(String userGuid) {
		this.userGuid = userGuid;
	}

	public float getAmt() {
		return amt;
	}

	public void setAmt(float amt) {
		this.amt = amt;
	}

	public float getTax() {
		return tax;
	}

	public void setTax(float tax) {
		this.tax = tax;
	}

	public boolean isWardAccount() {
		return wardAccount;
	}

	public void setWardAccount(boolean wardAccount) {
		this.wardAccount = wardAccount;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getOrgLeader() {
		return orgLeader;
	}

	public void setOrgLeader(String orgLeader) {
		this.orgLeader = orgLeader;
	}

	public void calculateReimbursementTotal() {
		total = amt + tax;
	}
}
