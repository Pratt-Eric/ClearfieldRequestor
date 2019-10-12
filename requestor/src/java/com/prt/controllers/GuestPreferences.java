package com.prt.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class GuestPreferences implements Serializable {

    private String menuMode = "layout-slim";

    private String theme = "bluegrey-teal";

    private String menuColor = "layout-menu-light";

    private String topBarColor = "layout-topbar-bluegrey";

    private String logo = "logo-olympia-white";

    private String username;
    private String calling;
    private StreamedContent picture;
    public ByteArrayOutputStream stream = new ByteArrayOutputStream();
    public String imgExt = "";

    public StreamedContent getPicture() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else if (stream.size() > 0) {
            return new DefaultStreamedContent(new ByteArrayInputStream(stream.toByteArray()), "image/" + imgExt.toLowerCase());
        }
        return null;
    }

    public void setPicture(StreamedContent picture) {
        this.picture = picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCalling() {
        return calling;
    }

    public void setCalling(String calling) {
        this.calling = calling;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getMenuMode() {
        return this.menuMode;
    }

    public void setMenuMode(String menuMode) {
        this.menuMode = menuMode;
    }

    public String getMenuColor() {
        return this.menuColor;
    }

    public void setMenuColor(String menuColor) {
        this.menuColor = menuColor;
    }

    public String getTopBarColor() {
        return this.topBarColor;
    }

    public void setTopBarColor(String topBarColor, String logo) {
        this.topBarColor = topBarColor;
        this.logo = logo;
    }

    public String getLogo() {
        return this.logo;
    }
}
