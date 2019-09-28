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

    private UUID uuid;
    private String title;
    private String summary;
    private UUID activity_uuid;
    private Date start;
    private Date end;
    private double budget_amount;
    private UUID status_uuid;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public UUID getActivity_uuid() {
        return activity_uuid;
    }

    public void setActivity_uuid(UUID activity_uuid) {
        this.activity_uuid = activity_uuid;
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

    public UUID getStatus_uuid() {
        return status_uuid;
    }

    public void setStatus_uuid(UUID status_uuid) {
        this.status_uuid = status_uuid;
    }
}
