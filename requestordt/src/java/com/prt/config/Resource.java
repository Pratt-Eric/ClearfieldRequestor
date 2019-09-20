/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.config;

import com.google.gson.Gson;
import com.prt.requestor.SQLProcess;
import com.prt.requestor.SQLUserProcess;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author P-ratt
 */
@Path("/repository")
public class Resource {

	public Resource() {
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

}
