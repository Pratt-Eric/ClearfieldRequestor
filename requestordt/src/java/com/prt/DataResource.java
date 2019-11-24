/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt;

import com.google.gson.Gson;
import com.prt.models.Activity;
import com.prt.models.Budget;
import com.prt.models.BudgetTransaction;
import com.prt.models.Calendar;
import com.prt.models.Dashboard;
import com.prt.models.Group;
import com.prt.models.User;
import com.prt.requestor.SQLActivityProcess;
import com.prt.requestor.SQLBudgetProcess;
import com.prt.requestor.SQLCalendarProcess;
import com.prt.requestor.SQLDashboardProcess;
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

	@Path("user/select/byguid")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postSelectUserByGuid(String supplied) {
		try {
			Gson gson = new Gson();
			String guid = gson.fromJson(supplied, String.class);
			return gson.toJson(SQLUserProcess.selectUserByGuid(guid));
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

	@Path("user/password/update")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postUpdateUserPassword(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLUserProcess.updateUserPassword(gson.fromJson(content, User.class)));
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

	@Path("budget/select/all")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String selectAllBudgets(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLBudgetProcess.selectAllBudgets());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Path("budget/select/all/user")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String selectAllBudgetsForUser(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLBudgetProcess.selectAllBudgetsForUser(gson.fromJson(content, String.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

	@Path("budget/transaction/add")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postAddBudgetTransaction(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLBudgetProcess.addNewTransaction(gson.fromJson(content, BudgetTransaction.class)));
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

	@Path("budget/edit/user")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postEditBudgetFromUser(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLBudgetProcess.editUserBudget(gson.fromJson(content, Budget.class)));
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

	@Path("calendar/select/all")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postSelectAllCalendars(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLCalendarProcess.selectAllCalendars());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(false);
	}

	@Path("calendar/add")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postAddNewCalendar(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLCalendarProcess.addNewCalendar(gson.fromJson(content, Calendar.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(false);
	}

	@Path("calendar/edit")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postEditExistingCalendar(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLCalendarProcess.editExistingCalendar(gson.fromJson(content, Calendar.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(false);
	}

	@Path("calendar/remove")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postRemoveExistingCalendar(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLCalendarProcess.deleteExistingCalendar(gson.fromJson(content, Calendar.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(false);
	}

	@Path("activity/select/all")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postSelectAllActivities(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLActivityProcess.selectAllActivities());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(false);
	}

	@Path("activity/add")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postAddNewActivity(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLActivityProcess.addNewActivity(gson.fromJson(content, Activity.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(false);
	}

	@Path("activity/edit")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postEditExistingActivity(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLActivityProcess.editExistingActivity(gson.fromJson(content, Activity.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(false);
	}

	@Path("activity/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postDeleteExistingActivity(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLActivityProcess.deleteExistingActivity(gson.fromJson(content, Activity.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(false);
	}

	@Path("dashboard/select/all")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postSelectAllDashboards(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLDashboardProcess.selectAllDashboards());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Path("dashboard/select")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postSelectDashboard(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLDashboardProcess.selectDashboard(gson.fromJson(content, String.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Path("dashboard/add")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postAddNewDashboard(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLDashboardProcess.addNewDashboard(gson.fromJson(content, Dashboard.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Path("dashboard/edit")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postEditExistingDashboard(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLDashboardProcess.editExistingDashboard(gson.fromJson(content, Dashboard.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Path("dashboard/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postDeleteExistingDashboard(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLDashboardProcess.deleteExistingDashboard(gson.fromJson(content, Dashboard.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Path("dashboard/user/select/all")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postSelectUserDashboards(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLDashboardProcess.selectUserDashboards(gson.fromJson(content, String.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Path("dashboard/user/select")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postSelectUserDashboard(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLDashboardProcess.selectUserDashboard(gson.fromJson(content, String[].class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Path("dashboard/user/select/default")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postSelectUserDefaultDashboard(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLDashboardProcess.selectUserDefaultDashboard(gson.fromJson(content, String.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Path("dashboard/user/add")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postAddUserDashboards(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLDashboardProcess.addUserDashboards(gson.fromJson(content, String[][].class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Path("dashboard/user/edit")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postEditUserDashboard(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLDashboardProcess.editUserDashboard(gson.fromJson(content, String[].class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Path("dashboard/user/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postDeleteUserDashboard(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLDashboardProcess.deleteUserDashboard(gson.fromJson(content, String[].class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Path("dashboard/user/available")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postSelectAvailableUserDashboards(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLDashboardProcess.selectAvailableUserDashboards(gson.fromJson(content, String.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Path("types/request")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postSelectRequestTypes(String content) {
		Gson gson = new Gson();
		try {
			return gson.toJson(SQLProcess.selectRequestTypes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
