/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author P-ratt
 */
public class Activity implements Serializable {

    private UUID uuid;
    private String name;
    private String desc;
    private ArrayList<Budget> budgets = new ArrayList<>();
    private ArrayList<Calendar> calendars = new ArrayList<>();
    private ArrayList<User> userAssociations = new ArrayList<>();
    private ArrayList<Group> groupAssociations = new ArrayList<>();

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public ArrayList<Budget> getBudgets() {
        return budgets;
    }

    public void setBudgets(ArrayList<Budget> budgets) {
        this.budgets = budgets;
    }

    public ArrayList<Calendar> getCalendars() {
        return calendars;
    }

    public void setCalendars(ArrayList<Calendar> calendars) {
        this.calendars = calendars;
    }

    public ArrayList<User> getUserAssociations() {
        return userAssociations;
    }

    public void setUserAssociations(ArrayList<User> userAssociations) {
        this.userAssociations = userAssociations;
    }

    public ArrayList<Group> getGroupAssociations() {
        return groupAssociations;
    }

    public void setGroupAssociations(ArrayList<Group> groupAssociations) {
        this.groupAssociations = groupAssociations;
    }
}
