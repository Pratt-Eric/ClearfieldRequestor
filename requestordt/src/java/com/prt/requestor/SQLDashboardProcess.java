/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.requestor;

import com.prt.models.Budget;
import com.prt.models.Calendar;
import com.prt.models.Dashboard;
import com.prt.models.Group;
import com.prt.models.User;
import com.prt.utils.DBConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

/**
 *
 * @author P-ratt
 */
public class SQLDashboardProcess {

	public static ArrayList<Dashboard> selectAllDashboards() throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			ArrayList<Dashboard> dashboards = new ArrayList<>();
			String query = "SELECT GUID, NAME, \"DESC\" FROM DASHBOARDS ORDER BY CREATED";
			String associations = "SELECT "
					+ "DPX.USER_GUID, "
					+ "DPX.GROUP_GUID, "
					+ "U.USERNAME, "
					+ "G.NAME "
					+ "FROM DASHBOARD_PERMISSIONS_XREF DPX "
					+ "LEFT JOIN USERS U ON DPX.USER_GUID = U.GUID "
					+ "LEFT JOIN GROUPS G ON DPX.GROUP_GUID = G.GUID "
					+ "WHERE DPXDASHBOARD_GUID = ?";
			String calsAndBudgets = "SELECT "
					+ "DIX.CALENDAR_GUID, "
					+ "DIX.BUDGET_GUID, "
					+ "C.NAME CAL_NAME, "
					+ "B.NAME BUD_NAME "
					+ "FROM DASHBOARD_ITEM_XREF DIX "
					+ "LEFT JOIN CALENDARS C ON DIX.CALENDAR_GUID = C.GUID "
					+ "LEFT JOIN BUDGETS B ON DIX.BUDGET_GUID = B.GUID "
					+ "WHERE DIX.DASHBOARD_GUID = ?";
			Statement stmt = conn.createStatement();
			ResultSet set = stmt.executeQuery(query);
			while (set.next()) {
				Dashboard dashboard = new Dashboard();
				dashboard.setGuid(set.getString("GUID"));
				dashboard.setName(set.getString("NAME"));
				dashboard.setDesc(set.getString("DESC"));
				//get related users and groups for dashboard
				PreparedStatement ug = conn.prepareStatement(associations);
				ug.setString(1, dashboard.getGuid());
				ResultSet ugSet = ug.executeQuery();
				while (ugSet.next()) {
					String user = ugSet.getString("USER_GUID");
					String group = ugSet.getString("GROUP_GUID");
					if (user != null && !user.isEmpty()) {
						User u = new User();
						u.setGuid(user);
						u.setUsername(ugSet.getString("USERNAME"));
						dashboard.getUsers().add(u);
					} else if (group != null && !group.isEmpty()) {
						Group g = new Group();
						g.setGuid(group);
						g.setName(ugSet.getString("NAME"));
						dashboard.getGroups().add(g);
					}
				}
				ugSet.close();
				ug.close();

				PreparedStatement cb = conn.prepareStatement(calsAndBudgets);
				cb.setString(1, dashboard.getGuid());
				ResultSet cbSet = cb.executeQuery();
				while (cbSet.next()) {
					String calendar = cbSet.getString("CALENDAR_GUID");
					String budget = cbSet.getString("BUDGET_GUID");
					if (calendar != null && !calendar.isEmpty()) {
						Calendar c = new Calendar();
						c.setGuid(calendar);
						c.setName(cbSet.getString("CAL_NAME"));
						dashboard.getCalendars().add(c);
					} else if (budget != null && !budget.isEmpty()) {
						Budget b = new Budget();
						b.setGuid(budget);
						b.setName(cbSet.getString("BUD_NAME"));
						dashboard.getBudgets().add(b);
					}
				}
				cbSet.close();
				cb.close();

				dashboards.add(dashboard);
			}
			set.close();
			stmt.close();

			return dashboards;
		} catch (Exception e) {
			e.printStackTrace();
			if (conn != null) {
				conn.rollback();
			}
		} finally {
			if (conn != null) {
				conn.setAutoCommit(true);
				conn.close();
			}
		}
		return new ArrayList<>();
	}

	public static boolean addNewDashboard(Dashboard dashboard) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String insert = "{call INSERT INTO DASHBOARDS (NAME, \"DESC\", CREATED) VALUES (?, ?, SYSTIMESTAMP) RETURNING GUID INTO ?}";
			CallableStatement stmt = conn.prepareCall(insert);
			stmt.setString(1, dashboard.getName());
			stmt.setString(2, dashboard.getDesc());
			stmt.registerOutParameter(3, Types.VARCHAR);
			stmt.executeUpdate();
			dashboard.setGuid(stmt.getString(3));
			stmt.close();

			return addReferences(conn, dashboard);
		} catch (Exception e) {
			e.printStackTrace();
			if (conn != null) {
				conn.rollback();
			}
		} finally {
			if (conn != null) {
				conn.setAutoCommit(true);
				conn.close();
			}
		}
		return false;
	}

	public static boolean editExistingDashboard(Dashboard dashboard) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String deletePerms = "DELETE FROM DASHBOARD_PERMISSIONS_XREF WHERE DASHBOARD_GUID = ?";
			String deleteItems = "DELETE FROM DASHBOARD_ITEM_XREF WHERE DASHBOARD_GUID = ?";
			String update = "UPDATE DASHBOARDS SET NAME = ?, \"DESC\" = ? WHERE GUID = ?";

			PreparedStatement dp = conn.prepareStatement(deletePerms);
			dp.setString(1, dashboard.getGuid());
			dp.executeUpdate();
			dp.close();

			PreparedStatement di = conn.prepareStatement(deleteItems);
			di.setString(1, dashboard.getGuid());
			di.executeUpdate();
			di.close();

			PreparedStatement stmt = conn.prepareStatement(update);
			stmt.setString(1, dashboard.getName());
			stmt.setString(2, dashboard.getDesc());
			stmt.setString(3, dashboard.getGuid());
			stmt.executeQuery();
			stmt.close();

			boolean success = addReferences(conn, dashboard);
			if (!success) {
				return false;
			} else {
				//find any user_dashboard_xref records that a user may not have access to anymore and remove them
				String remove = "DELETE FROM USER_DASHBOARD_XREF WHERE (DASHBOARD_GUID = ? AND USER_GUID NOT IN (SELECT USER_GUID FROM DASHBOARD_PERMISSIONS_XREF WHERE DASHBOARD_GUID = ? AND USER_GUID IS NOT NULL)) OR (DASHBOARD_GUID = ? AND GROUP_GUID NOT IN (SELECT GROUP_GUID FROM DASHBOARD_PERMISSIONS_XREF WHERE DASHBOARD_GUID = ? AND GROUP_GUID IS NOT NULL)";
				PreparedStatement delete = conn.prepareStatement(remove);
				delete.setString(1, dashboard.getGuid());
				delete.setString(2, dashboard.getGuid());
				delete.setString(3, dashboard.getGuid());
				delete.setString(4, dashboard.getGuid());
				delete.executeUpdate();
				delete.close();
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (conn != null) {
				conn.rollback();
			}
		} finally {
			if (conn != null) {
				conn.setAutoCommit(true);
				conn.close();
			}
		}
		return false;
	}

	public static boolean deleteExistingDashboard(Dashboard dashboard) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String users = "DELETE FROM USER_DASHBOARD_XREF WHERE DASHBOARD_GUID = ?";
			String perms = "DELETE FROM DASHBOARD_PERMISSIONS_XREF WHERE DASHBOARD_GUID = ?";
			String items = "DELETE FROM DASHBOARD_ITEM_XREF WHERE DASHBOARD_GUID = ?";
			String dash = "DELETE FROM DASHBOARDS WHERE GUID = ?";

			PreparedStatement stmt = conn.prepareStatement(users);
			stmt.setString(1, dashboard.getGuid());
			stmt.executeUpdate();
			stmt.close();

			stmt = conn.prepareStatement(perms);
			stmt.setString(1, dashboard.getGuid());
			stmt.executeUpdate();
			stmt.close();

			stmt = conn.prepareStatement(items);
			stmt.setString(1, dashboard.getGuid());
			stmt.executeUpdate();
			stmt.close();

			stmt = conn.prepareStatement(dash);
			stmt.setString(1, dashboard.getGuid());
			stmt.executeUpdate();
			stmt.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (conn != null) {
				conn.rollback();
			}
		} finally {
			if (conn != null) {
				conn.setAutoCommit(true);
				conn.close();
			}
		}
		return false;
	}

	public static boolean addReferences(Connection conn, Dashboard dashboard) {
		try {
			String perms = "INSERT INTO DASHBOARD_PERMISSIONS_XREF (USER_GUID, GROUP_GUID, DASHBOARD_GUID) VALUES (?, ?, ?)";
			String items = "INSERT INTO DASBHOARD_ITEM_XREF (CALENDAR_GUID, BUDGET_GUID, DASHBOARD_GUID, \"INDEX\") VALUES (?, ?, ?, ?)";
			PreparedStatement addPerms = conn.prepareStatement(perms);
			PreparedStatement addItems = conn.prepareStatement(items);

			for (User user : dashboard.getUsers()) {
				addPerms.setString(1, user.getGuid());
				addPerms.setString(2, null);
				addPerms.setString(3, dashboard.getGuid());
				addPerms.addBatch();
			}
			for (Group group : dashboard.getGroups()) {
				addPerms.setString(1, null);
				addPerms.setString(2, group.getGuid());
				addPerms.setString(3, dashboard.getGuid());
				addPerms.addBatch();
			}
			int index = 0;
			for (Calendar calendar : dashboard.getCalendars()) {
				addItems.setString(1, calendar.getGuid());
				addItems.setString(2, null);
				addItems.setString(3, dashboard.getGuid());
				addItems.setInt(4, index++);
				addItems.addBatch();
			}
			for (Budget budget : dashboard.getBudgets()) {
				addItems.setString(1, null);
				addItems.setString(2, budget.getGuid());
				addItems.setString(3, dashboard.getGuid());
				addItems.setInt(4, index++);
				addItems.addBatch();
			}

			addPerms.executeBatch();
			addItems.executeBatch();
			addPerms.close();
			addItems.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
