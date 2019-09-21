/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.config;

import com.google.gson.Gson;
import com.prt.requestor.SQLProcess;
import com.prt.requestor.SQLUserProcess;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author P-ratt
 */
@Path("data")
public class DataResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of DataResource
     */
    public DataResource() {
    }

    @Path("ugh")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String ugh(String supplied) {
        try {
            System.out.println("ugh");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Path("initialize")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postInitialize(String supplied) {
        try {
            Gson gson = new Gson();

            return gson.toJson(SQLProcess.initializeDefaultUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Path("user/select")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postSelectUserCredentials(String supplied) {
        try {
            Gson gson = new Gson();
            String username = gson.fromJson(supplied, String.class);
            return gson.toJson(SQLUserProcess.selectUser(username));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Path("user/select/all")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postSelectAllUsers() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Path("group/select")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postSelectGroup() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Path("group/select/all")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postSelectAllGroups() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Path("user/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postAddNewUser(String content) {
        Gson gson = new Gson();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.toJson(false);
    }

    @Path("user/remove")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postRemoveUser(String content) {
        Gson gson = new Gson();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.toJson(false);
    }

    @Path("group/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postAddNewGroup(String content) {
        Gson gson = new Gson();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.toJson(false);
    }

    @Path("user/remove")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postRemoveGroup(String content) {
        Gson gson = new Gson();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.toJson(false);
    }

    /**
     * Retrieves representation of an instance of com.prt.config.DataResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of DataResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
