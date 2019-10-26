/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.requestor;

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
public class SQLCalendarProcess {

	public static ArrayList<Calendar> selectAllCalendars() throws SQLException {
		Connection conn = null;
		ArrayList<Calendar> calendars = new ArrayList<>();
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String query = "SELECT C.GUID, C.NAME, C.\"DESC\" FROM CALENDARS C ";
			Statement stmt = conn.createStatement();
			ResultSet set = stmt.executeQuery(query);
			while (set.next()) {
				Calendar calendar = new Calendar();
				calendar.setGuid(set.getString("GUID"));
				calendar.setName(set.getString("NAME"));
				calendar.setDesc(set.getString("DESC"));

				String userSelect = "SELECT CPX.USER_GUID, CPX.GROUP_GUID, CPX.EDITABLE, U.USERNAME, G.NAME "
						+ "FROM CALENDAR_PERMISSIONS_XREF CPX "
						+ "LEFT JOIN USERS U ON CPX.USER_GUID = U.GUID "
						+ "LEFT JOIN GROUPS G ON CPX.GROUP_GUID = G.GUID "
						+ "WHERE CPX.CALENDAR_GUID = ?";
				PreparedStatement select = conn.prepareStatement(userSelect);
				select.setString(1, calendar.getGuid());
				ResultSet set2 = select.executeQuery();
				while (set2.next()) {
					String userGuid = set2.getString("USER_GUID");
					String groupGuid = set2.getString("GROUP_GUID");
					if (userGuid != null && !userGuid.isEmpty()) {
						User user = new User();
						user.setGuid(userGuid);
						user.setUsername(set2.getString("USERNAME"));
						user.setEditCalendar(set2.getString("EDITABLE").equals("1"));
						calendar.getUsers().add(user);
					} else if (groupGuid != null && !groupGuid.isEmpty()) {
						Group group = new Group();
						group.setGuid(groupGuid);
						group.setName(set2.getString("NAME"));
						group.setEditCalendar(set2.getString("EDITABLE").equals("1"));
						calendar.getGroups().add(group);
					}
				}
				set2.close();
				select.close();

				calendars.add(calendar);
			}
			set.close();
			stmt.close();

			return calendars;
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
		return calendars;
	}

	public static boolean addNewCalendar(Calendar calendar) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String query = "INSERT INTO CALENDARS (NAME, \"DESC\", CREATED) VALUES (?, ?, SYSTIMESTAMP)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, calendar.getName());
			stmt.setString(2, calendar.getDesc());
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

	public static boolean editExistingCalendar(Calendar calendar) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String deletePermissions = "DELETE FROM CALENDAR_PERMISSIONS_XREF WHERE CALENDAR_GUID = ?";
			String insertPermissions = "INSERT INTO CALENDAR_PERMISSIONS_XREF (USER_GUID, GROUP_GUID, EDITABLE, CALENDAR_GUID) VALUES (?, ?, ?, ?)";
			String updateCalendar = "UPDATE CALENDARS SET NAME = ?, \"DESC\" = ? WHERE GUID = ?";

			PreparedStatement delete = conn.prepareStatement(deletePermissions);
			delete.setString(1, calendar.getGuid());
			delete.executeUpdate();
			delete.close();

			PreparedStatement update = conn.prepareStatement(updateCalendar);
			update.setString(1, calendar.getName());
			update.setString(2, calendar.getDesc());
			update.setString(3, calendar.getGuid());
			update.executeUpdate();

			PreparedStatement permissions = conn.prepareStatement(insertPermissions);
			for (User user : calendar.getUsers()) {
				permissions.setString(1, user.getGuid());
				permissions.setString(2, null);
				permissions.setString(3, user.isEditCalendar() ? "1" : "0");
				permissions.setString(4, calendar.getGuid());
				permissions.addBatch();
			}
			for (Group group : calendar.getGroups()) {
				permissions.setString(1, null);
				permissions.setString(2, group.getGuid());
				permissions.setString(3, group.isEditCalendar() ? "1" : "0");
				permissions.setString(4, calendar.getGuid());
				permissions.addBatch();
			}
			permissions.executeBatch();
			permissions.close();

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

	public static boolean deleteExistingCalendar(Calendar calendar) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String deletePermissions = "DELETE FROM CALENDAR_PERMISSIONS_XREF WHERE CALENDAR_GUID = ?";
			String deleteCalendar = "DELETE FROM CALENDARS WHERE GUID = ?";

			PreparedStatement stmt = conn.prepareStatement(deletePermissions);
			stmt.setString(1, calendar.getGuid());
			stmt.executeUpdate();
			stmt.close();

			stmt = conn.prepareStatement(deleteCalendar);
			stmt.setString(1, calendar.getGuid());
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

}
