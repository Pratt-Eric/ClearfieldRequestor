/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.models;

import java.io.Serializable;
import java.util.ArrayList;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author P-ratt
 */
public class Item implements Serializable {

	private String guid;
	private String calendarGuid;
	private String budgetGuid;
	private String name;
	private String type;
	private String desc;
	private int index = -1;
	private ScheduleModel calendar;
	private ArrayList<Event> events = new ArrayList<>();
	private ArrayList<BudgetTransaction> transactions = new ArrayList<>();

	public ArrayList<BudgetTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(ArrayList<BudgetTransaction> transactions) {
		this.transactions = transactions;
	}

	public ArrayList<Event> getEvents() {
		return events;
	}

	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}

	public ScheduleModel getCalendar() {
		return calendar;
	}

	public void setCalendar(ScheduleModel calendar) {
		this.calendar = calendar;
	}

	public String getCalendarGuid() {
		return calendarGuid;
	}

	public void setCalendarGuid(String calendarGuid) {
		this.calendarGuid = calendarGuid;
	}

	public String getBudgetGuid() {
		return budgetGuid;
	}

	public void setBudgetGuid(String budgetGuid) {
		this.budgetGuid = budgetGuid;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
