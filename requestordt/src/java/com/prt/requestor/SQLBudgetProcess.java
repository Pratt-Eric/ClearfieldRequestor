/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.requestor;

import com.prt.models.Budget;
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
public class SQLBudgetProcess {

	public static ArrayList<Budget> selectAllBudgets() throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();

			ArrayList<Budget> budgets = new ArrayList<>();

			String query = "SELECT B.GUID, B.NAME, B.\"DESC\", B.PARENT FROM BUDGETS B";
			String usersAndGroups = "SELECT "
					+ "BPX.GUID, "
					+ "BPX.USER_GUID, "
					+ "BPX.GROUP_GUID, "
					+ "BPX.EDITABLE, "
					+ "U.USERNAME, "
					+ "G.NAME GROUP_NAME "
					+ "FROM BUDGET_PERMISSIONS_XREF BPX "
					+ "LEFT JOIN USERS U ON BPX.USER_GUID = U.GUID "
					+ "LEFT JOIN GROUPS G ON BPX.GROUP_GUID = G.GUID "
					+ "WHERE BPX.BUDGET_GUID = ?";
			Statement stmt = conn.createStatement();
			ResultSet set = stmt.executeQuery(query);
			while (set.next()) {
				Budget budget = new Budget();
				budget.setGuid(set.getString("GUID"));
				budget.setName(set.getString("NAME"));
				budget.setDesc(set.getString("DESC"));
				budget.setParentGuid(set.getString("PARENT"));
				//add users and groups to budget result set
				PreparedStatement select = conn.prepareStatement(usersAndGroups);
				select.setString(1, budget.getGuid());
				ResultSet set2 = select.executeQuery();
				while (set2.next()) {
					String userGuid = set2.getString("USER_GUID");
					String groupGuid = set2.getString("GROUP_GUID");
					if (userGuid != null && !userGuid.isEmpty()) {
						User user = new User();
						user.setGuid(userGuid);
						user.setUsername(set2.getString("USERNAME"));
						budget.getUsers().add(user);
					}
					if (groupGuid != null && !groupGuid.isEmpty()) {
						Group group = new Group();
						group.setGuid(groupGuid);
						group.setName(set2.getString("GROUP_NAME"));
						budget.getGroups().add(group);
					}
				}
				set2.close();
				select.close();
			}
			set.close();
			stmt.close();

			return budgets;
		} catch (Exception e) {
			e.printStackTrace();
			if (conn != null) {
				conn.rollback();
			}
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}

	public static boolean addNewBudget(Budget budget) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();

			String query = "INSERT INTO BUDGETS (NAME, \"DESC\", PARENT) VALUES (?, ?, ?)";

			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, budget.getName());
			stmt.setString(2, budget.getDesc());
			stmt.setString(3, budget.getParentGuid());
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
				conn.close();
			}
		}
		return false;
	}

	public static boolean editExistingBudget(Budget budget) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();

			String update = "UPDATE BUDGETS SET NAME = ?, \"DESC\" = ?, REMAINING = ?, PARENT = ? WHERE GUID = ?";
			PreparedStatement stmt = conn.prepareStatement(update);
			stmt.setString(1, budget.getName());
			stmt.setString(2, budget.getDesc());
			stmt.setString(3, String.valueOf(budget.getRemaining()));
			stmt.setString(4, budget.getParentGuid());
			stmt.setString(5, budget.getGuid());
			stmt.executeUpdate();
			stmt.close();

			//get the users and groups associated with the budget and assign them accordingly
			String delete = "DELETE FROM BUDGET_PERMISSIONS_XREF WHERE BUDGET_GUID = ?";
			PreparedStatement deleteStmt = conn.prepareStatement(delete);
			deleteStmt.setString(1, budget.getGuid());
			deleteStmt.executeUpdate();
			deleteStmt.close();

			String insert = "INSERT INTO BUDGET_PERMISSIONS_XREF (USER_GUID, GROUP_GUID, EDITABLE, BUDGET_GUID) VALUES (?, ?, ?, ?)";
			PreparedStatement insertStmt = conn.prepareStatement(insert);
			for (User user : budget.getUsers()) {
				insertStmt.setString(1, user.getGuid());
				insertStmt.setString(2, null);
				insertStmt.setString(3, user.isEditBudget() ? "1" : "0");
				insertStmt.setString(4, budget.getGuid());
				insertStmt.addBatch();
			}
			for (Group group : budget.getGroups()) {
				insertStmt.setString(1, null);
				insertStmt.setString(2, group.getGuid());
				insertStmt.setString(3, group.isEditBudget() ? "1" : "0");
				insertStmt.setString(4, budget.getGuid());
				insertStmt.addBatch();
			}
			insertStmt.executeBatch();
			insertStmt.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (conn != null) {
				conn.rollback();
			}
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return false;
	}

	public static boolean removeExistingBudget(Budget budget) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();

			String query1 = "DELETE FROM BUDGET_PERMISSIONS_XREF WHERE BUDGET_GUID = ?";
			String query2 = "DELETE FROM BUDGETS WHERE GUID = ?";

			PreparedStatement stmt = conn.prepareStatement(query1);
			stmt.setString(1, budget.getGuid());
			stmt.executeUpdate();
			stmt.close();

			PreparedStatement stmt2 = conn.prepareStatement(query2);
			stmt2.setString(1, budget.getGuid());
			stmt2.executeUpdate();
			stmt2.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (conn != null) {
				conn.rollback();
			}
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return false;
	}
}
