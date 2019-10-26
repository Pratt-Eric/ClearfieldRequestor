/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.requestor;

import com.prt.models.Activity;
import com.prt.models.Budget;
import com.prt.models.Calendar;
import com.prt.models.Group;
import com.prt.models.User;
import com.prt.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author P-ratt
 */
public class SQLActivityProcess {

	public static ArrayList<Activity> selectAllActivities() throws SQLException {
		ArrayList<Activity> activities = new ArrayList<>();
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String query = "SELECT GUID, NAME, \"DESC\" FROM ACTIVITY_TYPE_REF";
			String query2 = "SELECT "
					+ "ATA.USER_GUID, "
					+ "ATA.GROUP_GUID, "
					+ "ATA.BUDGET_GUID, "
					+ "ATA.CALENDAR_GUID, "
					+ "U.USERNAME, "
					+ "G.NAME GROUP_NAME, "
					+ "B.NAME BUDGET_NAME, "
					+ "C.NAME CALENDAR_NAME "
					+ "FROM ACTIVITY_TYPE_ASSOCIATIONS ATA "
					+ "LEFT JOIN USERS U ON U.GUID = ATA.USER_GUID "
					+ "LEFT JOIN GROUPS G ON G.GUID = ATA.GROUP_GUID "
					+ "LEFT JOIN BUDGETS B ON ATA.BUDGET_GUID = B.GUID "
					+ "LEFT JOIN CALENDARS C ON ATA.CALENDAR_GUID = C.GUID "
					+ "WHERE ATA.ACTIVITY_TYPE_REF_GUID = ?";
			Statement select = conn.createStatement();
			ResultSet set = select.executeQuery(query);
			while (set.next()) {
				Activity activity = new Activity();
				activity.setGuid(set.getString("GUID"));
				activity.setName(set.getString("NAME"));
				activity.setDesc(set.getString("DESC"));

				PreparedStatement stmt = conn.prepareStatement(query2);
				stmt.setString(1, activity.getGuid());
				ResultSet set2 = stmt.executeQuery();
				while (set2.next()) {
					String userGuid = set2.getString("USER_GUID");
					String groupGuid = set2.getString("GROUP_GUID");
					String budgetGuid = set2.getString("BUDGET_GUID");
					String calendarGuid = set2.getString("CALENDAR_GUID");
					if (userGuid != null && !userGuid.isEmpty()) {
						User user = new User();
						user.setGuid(userGuid);
						user.setUsername(set2.getString("USERNAME"));
						activity.getUsers().add(user);
					} else if (groupGuid != null && !groupGuid.isEmpty()) {
						Group group = new Group();
						group.setGuid(groupGuid);
						group.setName(set2.getString("GROUP_NAME"));
						activity.getGroups().add(group);
					} else if (budgetGuid != null && !budgetGuid.isEmpty()) {
						Budget budget = new Budget();
						budget.setGuid(budgetGuid);
						budget.setName(set2.getString("BUDGET_NAME"));
						activity.setBudget(budget);
					} else if (calendarGuid != null && !calendarGuid.isEmpty()) {
						Calendar calendar = new Calendar();
						calendar.setGuid(calendarGuid);
						calendar.setName(set2.getString("CALENDAR_NAME"));
						activity.getCalendars().add(calendar);
					}
				}
				set2.close();
				stmt.close();

				activities.add(activity);
			}
			set.close();
			select.close();

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
		return activities;
	}

	public static boolean addNewActivity(Activity activity) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String query = "INSERT INTO ACTIVITY_TYPE_REF (NAME, \"DESC\", CREATED) VALUES (?, ?, SYSTIMESTAMP)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, activity.getName());
			stmt.setString(2, activity.getDesc());
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

	public static boolean editExistingActivity(Activity activity) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String deleteRefs = "DELETE FROM ACTIVITY_TYPE_ASSOCIATIONS WHERE ACTIVITY_TYPE_REF_GUID = ?";
			String update = "UPDATE ACTIVITY_TYPE_REF SET NAME = ?, \"DESC\" = ? WHERE GUID = ?";
			String insertRefs = "INSERT INTO ACTIVITY_TYPE_ASSOCIATIONS (ACTIVITY_TYPE_REF_GUID, USER_GUID, GROUP_GUID, BUDGET_GUID, CALENDAR_GUID) VALUES (?, ?, ?, ?, ?)";

			PreparedStatement stmt = conn.prepareStatement(deleteRefs);
			stmt.setString(1, activity.getGuid());
			stmt.executeUpdate();
			stmt.close();

			stmt = conn.prepareStatement(update);
			stmt.setString(1, activity.getName());
			stmt.setString(2, activity.getDesc());
			stmt.setString(3, activity.getGuid());
			stmt.executeUpdate();
			stmt.close();

			stmt = conn.prepareStatement(insertRefs);

			for (User user : activity.getUsers()) {
				stmt.setString(1, activity.getGuid());
				stmt.setString(2, user.getGuid());
				stmt.setString(3, null);
				stmt.setString(4, null);
				stmt.setString(5, null);
				stmt.addBatch();
			}
			for (Group group : activity.getGroups()) {
				stmt.setString(1, activity.getGuid());
				stmt.setString(2, null);
				stmt.setString(3, group.getGuid());
				stmt.setString(4, null);
				stmt.setString(5, null);
				stmt.addBatch();
			}
			for (Calendar calendar : activity.getCalendars()) {
				stmt.setString(1, activity.getGuid());
				stmt.setString(2, null);
				stmt.setString(3, null);
				stmt.setString(4, null);
				stmt.setString(5, calendar.getGuid());
				stmt.addBatch();
			}
			stmt.setString(1, activity.getGuid());
			stmt.setString(2, null);
			stmt.setString(3, null);
			stmt.setString(4, activity.getBudget().getGuid());
			stmt.setString(5, null);
			stmt.addBatch();

			stmt.executeBatch();

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

	public static boolean deleteExistingActivity(Activity activity) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String query = "DELETE FROM ACTIVITY_TYPE_ASSOCIATIONS WHERE ACTIVITY_TYPE_REF_GUID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, activity.getGuid());
			stmt.executeUpdate();

			query = "DELETE FROM ACTIVITY_TYPE_REF WHERE GUID = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, activity.getGuid());
			stmt.executeUpdate();

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

}
