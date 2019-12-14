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
	private Date datePaid;
	private String checkNumber;
	private String budgetGuid;
	private String missionary;
	private String fastOffering;
	private String relatedActivityGuid;
	private Activity relatedActivity;

	public String getRelatedActivityGuid() {
		return relatedActivityGuid;
	}

	public void setRelatedActivityGuid(String relatedActivityGuid) {
		this.relatedActivityGuid = relatedActivityGuid;
	}

	public Activity getRelatedActivity() {
		return relatedActivity;
	}

	public void setRelatedActivity(Activity relatedActivity) {
		this.relatedActivity = relatedActivity;
	}

	public Date getDatePaid() {
		return datePaid;
	}

	public void setDatePaid(Date datePaid) {
		this.datePaid = datePaid;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public String getBudgetGuid() {
		return budgetGuid;
	}

	public void setBudgetGuid(String budgetGuid) {
		this.budgetGuid = budgetGuid;
	}

	public String getMissionary() {
		return missionary;
	}

	public void setMissionary(String missionary) {
		this.missionary = missionary;
	}

	public String getFastOffering() {
		return fastOffering;
	}

	public void setFastOffering(String fastOffering) {
		this.fastOffering = fastOffering;
	}

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
