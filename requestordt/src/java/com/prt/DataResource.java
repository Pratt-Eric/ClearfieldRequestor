/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt;

import com.google.gson.Gson;
import com.prt.models.Budget;
import com.prt.models.Group;
import com.prt.models.User;
import com.prt.requestor.SQLBudgetProcess;
import com.prt.requestor.SQLGroupProcess;
import com.prt.requestor.SQLProcess;
import com.prt.requestor.SQLUserProcess;
import com.prt.utils.DBConnection;
import javax.annotation.PostConstruct;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
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

	@PostConstruct
	public void init() {
		if (DBConnection.getInstance().getDataSource() == null) {
			String[] ourContext = context.getRequestUri().getPath().split("/");
			DBConnection.getInstance().init(ourContext[1]);
		}
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
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLUserProcess.selectAllUsers());
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
			return gson.toJson(SQLUserProcess.addNewUser(gson.fromJson(content, User.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(false);
	}

	@Path("user/edit")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postEditUser(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLUserProcess.editExistingUser(gson.fromJson(content, User.class)));
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
			return gson.toJson(SQLUserProcess.removeExistingUser(gson.fromJson(content, User.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(false);
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
			Gson gson = new Gson();
			return gson.toJson(SQLGroupProcess.selectAllGroups());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Path("group/add")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postAddNewGroup(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLGroupProcess.addNewGroup(gson.fromJson(content, Group.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(false);
	}

	@Path("group/edit")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postEditGroup(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLGroupProcess.editExistingGroup(gson.fromJson(content, Group.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(false);
	}

	@Path("group/remove")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postRemoveGroup(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLGroupProcess.removeExistingGroup(gson.fromJson(content, Group.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(false);
	}

	@Path("budget/add")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postAddBudget(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLBudgetProcess.addNewBudget(gson.fromJson(content, Budget.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(false);
	}

	@Path("budget/edit")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postEditBudget(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLBudgetProcess.editExistingBudget(gson.fromJson(content, Budget.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(false);
	}

	@Path("budget/remove")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postRemoveBudget(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLBudgetProcess.removeExistingBudget(gson.fromJson(content, Budget.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(false);
	}

	/**
	 * Retrieves representation of an instance of com.prt.DataResource
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
