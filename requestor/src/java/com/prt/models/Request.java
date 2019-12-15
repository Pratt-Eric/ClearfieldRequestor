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
public class Request implements Serializable {

	private String guid;
	private String title;
	private String summary;
	private String activity_type_guid;
	private String activityName;
	private Date start;
	private Date end;
	private double amt = 0;
	private String status_type_guid;
	private String userGuid;
	private float tax;
	private float total;
	private boolean wardAccount;
	private String wardAccountDetails;
	private String org;
	private String orgName;
	private String orgLeader;
	private String name;
	private String details;
	private String id;
	private String status;
	private User user;
	private Budget budget;
	private String comment;
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getTax() {
		return tax;
	}

	public void setTax(float tax) {
		this.tax = tax;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public boolean isWardAccount() {
		return wardAccount;
	}

	public void setWardAccount(boolean wardAccount) {
		this.wardAccount = wardAccount;
	}

	public String getWardAccountDetails() {
		return wardAccountDetails;
	}

	public void setWardAccountDetails(String wardAccountDetails) {
		this.wardAccountDetails = wardAccountDetails;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgLeader() {
		return orgLeader;
	}

	public void setOrgLeader(String orgLeader) {
		this.orgLeader = orgLeader;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getUserGuid() {
		return userGuid;
	}

	public void setUserGuid(String userGuid) {
		this.userGuid = userGuid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public double getAmt() {
		return amt;
	}

	public void setAmt(double amt) {
		this.amt = amt;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getActivity_type_guid() {
		return activity_type_guid;
	}

	public void setActivity_type_guid(String activity_type_guid) {
		this.activity_type_guid = activity_type_guid;
	}

	public String getStatus_type_guid() {
		return status_type_guid;
	}

	public void setStatus_type_guid(String status_type_guid) {
		this.status_type_guid = status_type_guid;
	}
}
