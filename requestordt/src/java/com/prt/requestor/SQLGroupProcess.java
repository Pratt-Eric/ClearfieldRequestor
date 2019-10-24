/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.requestor;

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
public class SQLGroupProcess {

	public static ArrayList<Group> selectAllGroups() throws SQLException {
		Connection conn = null;
		try {
			ArrayList<Group> groups = new ArrayList<>();
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String query = "SELECT "
					+ "G.GUID, "
					+ "G.NAME, "
					+ "G.\"DESC\", "
					+ "G.ADMINISTRATOR "
					+ "FROM GROUPS G ";

			Statement stmt = conn.createStatement();
			ResultSet set = stmt.executeQuery(query);
			while (set.next()) {
				Group group = new Group();
				group.setGuid(set.getString("GUID"));
				group.setName(set.getString("NAME"));
				group.setDesc(set.getString("DESC"));
				group.setAdmin(set.getString("ADMINISTRATOR").equals("1"));
				//grab possible users associated with the group
				String userQuery = "SELECT U.GUID, U.USERNAME FROM USER_GROUP_XREF UGX JOIN USERS U ON UGX.USER_GUID = U.GUID WHERE UGX.GROUP_GUID = ?";
				PreparedStatement stmt2 = conn.prepareStatement(userQuery);
				stmt2.setString(1, group.getGuid());
				ResultSet set2 = stmt2.executeQuery();
				while (set2.next()) {
					User user = new User();
					user.setGuid(set2.getString("GUID"));
					user.setUsername(set2.getString("USERNAME"));
					group.getUsers().add(user);
				}
				set2.close();
				stmt2.close();
				groups.add(group);
			}
			set.close();
			stmt.close();

			return groups;
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
		return null;
	}

	public static boolean addNewGroup(Group group) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String query = "INSERT INTO GROUPS (NAME, \"DESC\", ADMINISTRATOR) VALUES (?, ?, ?)";

			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, group.getName());
			stmt.setString(2, group.getDesc());
			stmt.setString(3, group.isAdmin() ? "1" : "0");
			stmt.executeUpdate();
			stmt.close();

			conn.commit();
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

	public static boolean editExistingGroup(Group group) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String query = "UPDATE GROUPS SET NAME = ?, \"DESC\" = ?, ADMINISTRATOR = ? WHERE GUID = ?";

			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, group.getName());
			stmt.setString(2, group.getDesc());
			stmt.setString(3, group.isAdmin() ? "1" : "0");
			stmt.setString(4, group.getGuid());
			stmt.executeUpdate();
			stmt.close();

			String deleteQuery = "DELETE FROM USER_GROUP_XREF WHERE GROUP_GUID = ?";
			PreparedStatement remove = conn.prepareStatement(deleteQuery);
			remove.setString(1, group.getGuid());
			remove.executeUpdate();
			remove.close();
			if (group.getUsers().size() > 0) {
				String addQuery = "INSERT INTO USER_GROUP_XREF (USER_GUID, GROUP_GUID) VALUES (?, ?)";

				PreparedStatement add = conn.prepareStatement(addQuery);
				for (User user : group.getUsers()) {
					add.setString(1, user.getGuid());
					add.setString(2, group.getGuid());
					add.addBatch();
				}
				add.executeBatch();
				add.close();
			}

			conn.commit();
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

	public static boolean removeExistingGroup(Group group) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String query = "DELETE FROM GROUPS WHERE GUID = ?";

			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, group.getGuid());
			stmt.executeUpdate();
			stmt.close();

			conn.commit();
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
