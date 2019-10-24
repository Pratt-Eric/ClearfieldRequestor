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
			conn.setAutoCommit(false);

			ArrayList<Budget> budgets = new ArrayList<>();

			String query = "SELECT B.GUID, B.NAME, B.\"DESC\", B.PARENT FROM BUDGETS B";
			Statement stmt = conn.createStatement();
			ResultSet set = stmt.executeQuery(query);
			while (set.next()) {
				Budget budget = new Budget();
				budget.setGuid(set.getString("GUID"));
				budget.setName(set.getString("NAME"));
				budget.setDesc(set.getString("DESC"));
				budget.setParentGuid(set.getString("PARENT"));
				//add users and groups to budget result set
				assignUsersAndGroups(conn, budget);

				if (budget.getParentGuid() != null && !budget.getParentGuid().isEmpty()) {
					assignParents(conn, budget);
				}
				budgets.add(budget);
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
				conn.setAutoCommit(true);
				conn.close();
			}
		}
		return null;
	}

	public static ArrayList<Budget> selectAllBudgetsForUser(String userGuid) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			ArrayList<Budget> budgets = new ArrayList<>();

			String query = ""
					+ "SELECT DISTINCT "
					+ "TBL.GUID, "
					+ "TBL.NAME, "
					+ "TBL.\"DESC\", "
					+ "TBL.PARENT, "
					+ "TBL.REMAINING, "
					+ "TBL.PARENT_NAME, "
					+ "CASE WHEN BPX1.GUID IS NOT NULL THEN 1 ELSE 0 END AS EDITABLE "
					+ "FROM "
					+ "		(SELECT DISTINCT B.GUID, B.NAME, B.\"DESC\", B.PARENT, B.REMAINING, B2.NAME PARENT_NAME "
					+ "		 FROM BUDGETS B LEFT JOIN BUDGETS B2 ON B.PARENT = B2.GUID) TBL "
					+ "JOIN BUDGET_PERMISSIONS_XREF BPX1 ON BPX1.BUDGET_GUID = TBL.GUID AND BPX1.EDITABLE = 1 "
					+ "JOIN BUDGET_PERMISSIONS_XREF BPX2 ON BPX2.BUDGET_GUID = TBL.GUID AND BPX2.EDITABLE = 0 "
					+ "WHERE (BPX1.USER_GUID = ? OR BPX2.USER_GUID = ?) OR (BPX1.GROUP_GUID IN (SELECT UGX.GROUP_GUID FROM USER_GROUP_XREF UGX WHERE UGX.USER_GUID = ?) OR BPX2.GROUP_GUID IN (SELECT UGX.GROUP_GUID FROM USER_GROUP_XREF UGX WHERE UGX.USER_GUID = ?))";

			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, userGuid);
			stmt.setString(2, userGuid);
			stmt.setString(3, userGuid);
			stmt.setString(4, userGuid);
			ResultSet set = stmt.executeQuery();
			while (set.next()) {
				Budget budget = new Budget();
				budget.setGuid(set.getString("GUID"));
				budget.setName(set.getString("NAME"));
				budget.setDesc(set.getString("DESC"));
				budget.setParentGuid(set.getString("PARENT"));
				budget.setRemaining(Double.parseDouble(set.getString("REMAINING")));
				budget.setEditable(set.getString("EDITABLE").equals("1"));
				budget.setParentName(set.getString("PARENT_NAME"));

				budgets.add(budget);
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
				conn.setAutoCommit(true);
				conn.close();
			}
		}
		return null;
	}

	private static void assignParents(Connection conn, Budget budget) {
		try {
			String query = "SELECT B.NAME, B.\"DESC\", B.PARENT FROM BUDGETS B WHERE B.GUID = ?";

			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, budget.getParentGuid());
			ResultSet set = stmt.executeQuery();
			while (set.next()) {
				Budget parent = new Budget();
				parent.setGuid(budget.getParentGuid());
				parent.setName(set.getString("NAME"));
				parent.setDesc(set.getString("DESC"));
				parent.setParentGuid(set.getString("PARENT"));
				assignUsersAndGroups(conn, parent);

				if (parent.getParentGuid() != null && !parent.getParentGuid().isEmpty()) {
					assignParents(conn, parent);
				}
				budget.setParent(parent);
			}
			set.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void assignUsersAndGroups(Connection conn, Budget budget) {
		try {
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

			PreparedStatement stmt = conn.prepareStatement(usersAndGroups);
			stmt.setString(1, budget.getGuid());
			ResultSet set = stmt.executeQuery();
			while (set.next()) {
				String userGuid = set.getString("USER_GUID");
				String groupGuid = set.getString("GROUP_GUID");
				if (userGuid != null && !userGuid.isEmpty()) {
					User user = new User();
					user.setGuid(userGuid);
					user.setUsername(set.getString("USERNAME"));
					user.setEditBudget(set.getString("EDITABLE").equals("1"));
					budget.getUsers().add(user);
				}
				if (groupGuid != null && !groupGuid.isEmpty()) {
					Group group = new Group();
					group.setGuid(groupGuid);
					group.setName(set.getString("GROUP_NAME"));
					group.setEditBudget(set.getString("EDITABLE").equals("1"));
					budget.getGroups().add(group);
				}
			}
			set.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean addNewBudget(Budget budget) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String query = "INSERT INTO BUDGETS (NAME, \"DESC\", PARENT) VALUES (?, ?, ?)";

			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, budget.getName());
			stmt.setString(2, budget.getDesc());
			stmt.setString(3, budget.getParentGuid());
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

	public static boolean editUserBudget(Budget budget) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String query = "UPDATE BUDGETS SET REMAINING = ? WHERE GUID = ?";

			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setDouble(1, budget.getRemaining());
			stmt.setString(2, budget.getGuid());
			stmt.executeUpdate();
			stmt.close();

			//any children need to be adjusted if their amount exceeds the maximum of the parent
			if (budget.getParentGuid() != null && !budget.getParentGuid().isEmpty()) {
				adjustChildBudgets(conn, budget.getParentGuid(), budget.getRemaining());
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

	public static void adjustChildBudgets(Connection conn, String parentGuid, double max) {
		try {
			//select the budget based on parent guid
			//if the remaining amount is above the max, then we need to adjust it to the max
			String select = "SELECT PARENT, REMAINING FROM BUDGETS WHERE GUID = ?";
			String update = "UPDATE BUDGETS SET REMAINING = ? WHERE GUID = ?";
			PreparedStatement selectStmt = conn.prepareStatement(select);
			selectStmt.setString(1, parentGuid);
			ResultSet set = selectStmt.executeQuery();
			while (set.next()) {
				String parent = set.getString("PARENT");
				double amt = Double.parseDouble(set.getString("REMAINING"));
				if (amt > max) {
					amt = max;
					PreparedStatement updateStmt = conn.prepareStatement(update);
					updateStmt.setString(1, String.valueOf(max));
					updateStmt.setString(2, parentGuid);
					updateStmt.executeUpdate();
					updateStmt.close();
				}
				if (parent != null && !parent.isEmpty()) {
					adjustChildBudgets(conn, parent, max - amt);
				}
			}
			set.close();
			selectStmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean editExistingBudget(Budget budget) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

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

			removeChildren(conn, budget.getGuid());

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

	private static boolean removeChildren(Connection conn, String parentGuid) {
		try {
			//if the parent was deleted we need to find all children that have that parent guid and delete them as well
			//grab the parent guid of the one we're going to delete so we can delete it's children
			ArrayList<String> budgets = new ArrayList<>();
			String query = "SELECT GUID FROM BUDGETS WHERE PARENT = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, parentGuid);
			ResultSet set = stmt.executeQuery();
			while (set.next()) {
				budgets.add(set.getString("GUID"));
			}
			set.close();
			stmt.close();

			String delete = "DELETE FROM BUDGETS WHERE PARENT = ?";
			PreparedStatement stmt2 = conn.prepareStatement(delete);
			stmt2.setString(1, parentGuid);
			stmt2.executeUpdate();
			stmt2.close();

			for (String budget : budgets) {
				removeChildren(conn, budget);
			}

			conn.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
