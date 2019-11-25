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
	private boolean wardAccount;
	private String org;
	private String orgLeader;

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
}
