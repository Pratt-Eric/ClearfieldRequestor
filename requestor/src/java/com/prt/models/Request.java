/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.models;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author P-ratt
 */
public class Request implements Serializable {

	private String guid;
	private String title;
	private String summary;
	private String activity_type_guid;
	private Date start;
	private Date end;
	private double budget_amount;
	private String status_type_guid;
	private String userGuid;

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

	public double getBudget_amount() {
		return budget_amount;
	}

	public void setBudget_amount(double budget_amount) {
		this.budget_amount = budget_amount;
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
