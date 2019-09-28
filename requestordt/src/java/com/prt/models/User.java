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
public class User implements Serializable {

    private UUID uuid;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private UUID password_uuid;
    private String password;
    private String salt;

    public User() {

    }

    public User(UUID uuid, String username, String firstname, String lastname, String email, UUID password_uuid) {
        this.uuid = uuid;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password_uuid = password_uuid;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getPassword_uuid() {
        return password_uuid;
    }

    public void setPassword_uuid(UUID password_uuid) {
        this.password_uuid = password_uuid;
    }

}
