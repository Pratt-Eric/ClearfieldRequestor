/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prt.models.Group;
import com.prt.models.User;
import com.prt.utils.RestUtil;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

/**
 *
 * @author P-ratt
 */
@ManagedBean(name = "groupsController")
@ViewScoped
public class GroupsController implements Serializable {

    @ManagedProperty("#{guestPreferences}")
    private GuestPreferences preferences;
    private ArrayList<Group> groups = new ArrayList<>();
    private Group newGroup;
    private String selectedGroupGuid;
    private Group selectedGroup;
    private ArrayList<String> selectedUsers = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();

    public ArrayList<String> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(ArrayList<String> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public Group getNewGroup() {
        return newGroup;
    }

    public void setNewGroup(Group newGroup) {
        this.newGroup = newGroup;
    }

    public String getSelectedGroupGuid() {
        return selectedGroupGuid;
    }

    public void setSelectedGroupGuid(String selectedGroupGuid) {
        this.selectedGroupGuid = selectedGroupGuid;
    }

    public Group getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(Group selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public GuestPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(GuestPreferences preferences) {
        this.preferences = preferences;
    }

    @PostConstruct
    void init() {
        try {
            //get existing groups
            Gson gson = new Gson();
            groups = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/group/select/all", null), new TypeToken<ArrayList<Group>>() {
            }.getType());
            users = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/user/select/all", null), new TypeToken<ArrayList<User>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startAddGroup() {
        newGroup = new Group();
    }

    public void addGroup() {
        try {
            Gson gson = new Gson();
            String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/group/add", gson.toJson(newGroup)), String.class);
            if (result != null && result.equalsIgnoreCase("true")) {
                init();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Group has been added successfully"));
                PrimeFaces.current().executeScript("PF('groupAddDlg').hide()");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem adding the new group"));
            }
            PrimeFaces.current().ajax().update("groupForm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editGroup(Group group) {
        selectedGroup = group;
        editGroup();
    }

    public void editGroup() {
        try {
            Gson gson = new Gson();
            String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/group/edit", gson.toJson(selectedGroup)), String.class);
            if (result != null && result.equalsIgnoreCase("true")) {
                init();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Group has been modified successfully"));
                PrimeFaces.current().executeScript("PF('groupEditDlg').hide()");
                PrimeFaces.current().executeScript("PF('userGroupDlg').hide()");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem modifying the selected group"));
            }
            PrimeFaces.current().ajax().update("groupForm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteGroup() {
        try {
            Gson gson = new Gson();
            String result = gson.fromJson(RestUtil.post(RestUtil.BASEURL + "/group/remove", gson.toJson(selectedGroup)), String.class);
            if (result != null && result.equalsIgnoreCase("true")) {
                init();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Group has been removed successfully"));
                PrimeFaces.current().executeScript("PF('groupDeleteDlg').hide()");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "There was a problem removing the selected group"));
            }
            PrimeFaces.current().ajax().update("groupForm");
        } catch (Exception e) {
        }
    }

    public void selectGroup(Group group) {
        selectedGroup = group;
        for (User user : selectedGroup.getUsers()) {
            selectedUsers.add(user.getGuid());
        }
    }

    public void addUsersToGroup() {
        try {
            selectedGroup.setUsers(new ArrayList<>());
            for (String user : selectedUsers) {
                for (User u : users) {
                    if (u.getGuid().equals(user)) {
                        boolean found = false;
                        for (User u2 : selectedGroup.getUsers()) {
                            if (u2.getGuid().equals(u.getGuid())) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            selectedGroup.getUsers().add(u);
                            break;
                        }
                    }
                }
            }
            editGroup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
