/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.models;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author P-ratt
 */
public class Password implements Serializable {

    private UUID uuid;
    private String password;
    private String salt;

    public Password() {

    }

    public Password(UUID uuid, String password, String salt) {
        this.uuid = uuid;
        this.password = password;
        this.salt = salt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
