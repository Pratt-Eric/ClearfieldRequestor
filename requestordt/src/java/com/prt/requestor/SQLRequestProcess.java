/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.requestor;

import com.prt.models.Budget;
import com.prt.models.Event;
import com.prt.models.Expense;
import com.prt.models.Reimbursement;
import com.prt.models.Request;
import com.prt.models.User;
import com.prt.utils.DBConnection;
import com.prt.utils.DateUtils;
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
public class SQLRequestProcess {

	public static boolean requestActivity(Event event) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String req = "SELECT COUNT(*) \"INDEX\" FROM ACTIVITY_REQUESTS";
			Statement reqStmt = conn.createStatement();
			int index = 0;
			ResultSet reqSet = reqStmt.executeQuery(req);
			while (reqSet.next()) {
				index = reqSet.getInt("INDEX");
			}
			reqSet.close();
			reqStmt.close();
			index++;

			String query = "INSERT INTO ACTIVITY_REQUESTS "
					+ "(TITLE, SUMMARY, ACTIVITY_TYPE_REF_GUID, \"START\", \"END\", AMOUNT, STATUS_TYPE_REF_GUID, REQUESTED_BY, \"INDEX\", CREATED) "
					+ "VALUES (?, ?, ?, ?, ?, ?, (SELECT GUID FROM STATUS_TYPE_REF WHERE NAME = 'Pending Review'), ?, ?, SYSTIMESTAMP)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, event.getTitle());
			stmt.setString(2, event.getSummary());
			stmt.setString(3, event.getActivity().getGuid());
			stmt.setDate(4, new java.sql.Date(event.getStart().getTime()));
			stmt.setDate(5, new java.sql.Date(event.getEnd().getTime()));
			stmt.setFloat(6, event.getBudget());
			stmt.setString(7, event.getUserGuid());
			stmt.setInt(8, index);
			stmt.executeUpdate();
			stmt.close();

			//email those associated with this activity type
			//get the users and groups associated with the activity type
//				ArrayList<String> users = new ArrayList<>();
//				String userQuery = ""
//						+ "SELECT U.EMAIL "
//						+ "FROM ACTIVITY_TYPE_ASSOCIATIONS ATA "
//						+ "JOIN USERS U ON ATA.USER_GUID = U.GUID "
//						+ "WHERE ATA.ACTIVITY_TYPE_REF_GUID = ? AND U.EMAIL IS NOT NULL "
//						+ "UNION SELECT U2.EMAIL "
//						+ "FROM ACTIVITY_TYPE_ASSOCIATIONS ATA "
//						+ "JOIN USER_GROUP_XREF UGX ON ATA.GROUP_GUID = UGX.GROUP_GUID "
//						+ "JOIN USERS U2 ON UGX.USER_GUID = U2.GUID "
//						+ "WHERE ATA.ACTIVITY_TYPE_REF_GUID = ? AND U2.EMAIL IS NOT NULL";
//				PreparedStatement emails = conn.prepareStatement(userQuery);
//				emails.setString(1, event.getActivity().getGuid());
//				emails.setString(2, event.getActivity().getGuid());
//				ResultSet set = emails.executeQuery();
//				while(set.next()){
//					String email = set.getString("EMAIL");
//					if(!users.contains(email)){
//						users.add(email);
//					}
//				}
//				set.close();
//				emails.close();
//				
//				String subject = "Activity Request: " + reqId;
//				String body = ""
//						+ "There has been a request for an activity that is waiting approval by you and your committee. The following are details on the activity request:\n\n"
//						+ "ID: " + reqId + "\n"
//						+ "Title: " + event.getTitle() + "\n"
//						+ "Summary: " + event.getSummary() + "\n\n"
//						+ "Start Date and Time: " + event.getStart().toString() + "\n"
//						+ "End Date and Time: " + event.getEnd().toString() + "\n\n"
//						+ "You may follow this link to approve or deny the request\n"
//						+ "{Link here}";
//				for(String email : users){
//					EmailUtils.sendEmail(email, subject, body);
//				}
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

	public static boolean requestReimbursement(Reimbursement reimbursement) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String req = "SELECT COUNT(*) \"INDEX\" FROM REIMBURSEMENT_REQUESTS";
			Statement reqStmt = conn.createStatement();
			int index = 0;
			ResultSet reqSet = reqStmt.executeQuery(req);
			while (reqSet.next()) {
				index = reqSet.getInt("INDEX");
			}
			reqSet.close();
			reqStmt.close();
			index++;

			String query = ""
					+ "INSERT INTO REIMBURSEMENT_REQUESTS "
					+ "(AMOUNT, STATUS_TYPE_REF_GUID, REQUESTED_BY, TAX, WARD_ACCOUNT, WARD_ACCOUNT_DETAILS, ORGANIZATION, ORGANIZATION_LEADER, \"INDEX\", CREATED, RELATED_ACTIVITY) "
					+ "VALUES (?, (SELECT GUID FROM STATUS_TYPE_REF WHERE NAME = 'Pending Review'), ?, ?, ?, ?, ?, ?, ?, SYSTIMESTAMP, ?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setFloat(1, reimbursement.getAmt());
			stmt.setString(2, reimbursement.getUserGuid());
			stmt.setFloat(3, reimbursement.getTax());
			stmt.setString(4, reimbursement.isWardAccount() ? "1" : "0");
			stmt.setString(5, reimbursement.getWardAccountDetails());
			stmt.setString(6, reimbursement.getOrg().equalsIgnoreCase("Other") || reimbursement.getOrg().equalsIgnoreCase("Council Expense") ? reimbursement.getOrgName() : reimbursement.getOrg());
			stmt.setString(7, reimbursement.getOrgLeader());
			stmt.setInt(8, index);
			stmt.setString(9, reimbursement.getRelatedActivityGuid());
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

	public static boolean requestExpense(Expense expense) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String req = "SELECT COUNT(*) \"INDEX\" FROM REIMBURSEMENT_REQUESTS";
			Statement reqStmt = conn.createStatement();
			int index = 0;
			ResultSet reqSet = reqStmt.executeQuery(req);
			while (reqSet.next()) {
				index = reqSet.getInt("INDEX");
			}
			reqSet.close();
			reqStmt.close();
			index++;

			String query = "INSERT INTO EXPENSE_REQUESTS "
					+ "(AMOUNT, NAME, DETAILS, REQUESTED_BY, \"INDEX\", STATUS_TYPE_REF_GUID, CREATED) "
					+ "VALUES (?, ?, ?, ?, ?, (SELECT GUID FROM STATUS_TYPE_REF WHERE NAME = 'Pending Review'), SYSTIMESTAMP)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setFloat(1, expense.getAmt());
			stmt.setString(2, expense.getName());
			stmt.setString(3, expense.getDetails());
			stmt.setString(4, expense.getUserGuid());
			stmt.setInt(5, index);
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

	public static ArrayList<Request> selectAllUserRequests(String userGuid) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			ArrayList<Request> requests = new ArrayList<>();

			String activityQuery = "SELECT GUID, TITLE, SUMMARY, ACTIVITY_TYPE_REF_GUID, \"START\", \"END\", AMOUNT, STR.NAME STATUS, \"INDEX\", ATR.NAME ACTIVITY_NAME "
					+ "FROM ACTIVITY_REQUESTS AR "
					+ "JOIN STATUS_TYPE_REF STR ON AR.STATUS_TYPE_REF_GUID = STR.GUID "
					+ "JOIN ACTIVITY_TYPE_REF ATR ON AR.ACTIVITY_TYPE_REF_GUID = ATR.GUID "
					+ "WHERE AR.REQUESTED_BY = ?";
			String reimbursementQuery = "SELECT GUID, RR.AMOUNT, STR.NAME STATUS, RR.TAX, RR.WARD_ACCOUNT, RR.WARD_ACCOUNT_DETAILS, RR.ORGANIZATION, RR.ORGANIZATION_LEADER, RR.\"INDEX\", ATR.NAME ACTIVITY_NAME "
					+ "FROM REIMBURSEMENT_REQUESTS RR "
					+ "JOIN STATUS_TYPE_REF STR ON RR.STATUS_TYPE_REF_GUID = STR.GUID "
					+ "LEFT JOIN ACTIVITY_TYPE_REF ATR ON RR.RELATED_ACTIVITY = ATR.GUID "
					+ "WHERE RR.REQUESTED_BY = ?";
			String expenseQuery = "SELECT GUID, ER.AMOUNT, ER.NAME, ER.DETAILS, ER.\"INDEX\", STR.NAME STATUS "
					+ "FROM EXPENSE_REQUESTS ER "
					+ "JOIN STATUS_TYPE_REF STR ON ER.STATUS_TYPE_REF_GUID = STR.GUID "
					+ "WHERE REQUESTED_BY = ?";
			String userQuery = "SELECT USERNAME, FIRSTNAME, LASTNAME, EMAIL, P.CALLING "
					+ "FROM USERS U "
					+ "JOIN PROFILE P ON U.GUID = P.USER_GUID "
					+ "WHERE U.GUID = ?";
			String budgetQuery = "SELECT B.GUID, B.NAME, B.\"DESC\", B.REMAINING, B.PARENT "
					+ "FROM ACTIVITY_TYPE_ASSOCIATIONS ATA "
					+ "JOIN BUDGETS B ON ATA.BUDGET_GUID = B.GUID "
					+ "WHERE ATA.ACTIVITY_TYPE_REF_GUID = ?";

			User user = new User();
			PreparedStatement stmt2 = conn.prepareStatement(userQuery);
			stmt2.setString(1, userGuid);
			ResultSet set2 = stmt2.executeQuery();
			while (set2.next()) {
				user.setGuid(userGuid);
				user.setUsername(set2.getString("USERNAME"));
				user.setFirstname(set2.getString("FIRSTNAME"));
				user.setLastname(set2.getString("LASTNAME"));
				user.setEmail(set2.getString("EMAIL"));
				user.setCalling(set2.getString("CALLING"));
			}
			set2.close();
			stmt2.close();

			PreparedStatement stmt = conn.prepareStatement(activityQuery);
			stmt.setString(1, userGuid);
			ResultSet set = stmt.executeQuery();
			while (set.next()) {
				Request request = new Request();
				request.setType("Activity");
				request.setGuid(set.getString("GUID"));
				request.setId("ACT-" + set.getString("INDEX"));
				request.setTitle(set.getString("TITLE"));
				request.setSummary(set.getString("SUMMARY"));
				request.setActivityName(set.getString("ACTIVITY_NAME"));
				request.setStart(DateUtils.parseDate(set.getString("START")));
				request.setEnd(DateUtils.parseDate(set.getString("END")));
				request.setAmt(set.getDouble("AMOUNT"));
				request.setStatus(set.getString("STATUS"));
				request.setActivity_type_guid(set.getString("ACTIVITY_TYPE_REF_GUID"));
				request.setUser(user);
				stmt2 = conn.prepareStatement(budgetQuery);
				stmt2.setString(1, request.getActivity_type_guid());
				set2 = stmt2.executeQuery();
				while (set2.next()) {
					Budget budget = new Budget();
					budget.setGuid(set2.getString("GUID"));
					budget.setName(set2.getString("NAME"));
					budget.setDesc(set2.getString("DESC"));
					budget.setRemaining(set2.getDouble("REMAINING"));
					budget.setParentGuid(set2.getString("PARENT"));
					setParent(conn, budget);
					request.setBudget(budget);
				}
				set2.close();
				stmt2.close();
				requests.add(request);
			}
			set.close();
			stmt.close();

			stmt = conn.prepareStatement(reimbursementQuery);
			stmt.setString(1, userGuid);
			set = stmt.executeQuery();
			while (set.next()) {
				Request request = new Request();
				request.setType("Reimbursement");
				request.setGuid(set.getString("GUID"));
				request.setAmt(set.getDouble("AMOUNT"));
				request.setStatus(set.getString("STATUS"));
				request.setTax(set.getFloat("TAX"));
				request.setWardAccount(set.getString("WARD_ACCOUNT").equals("1"));
				request.setWardAccountDetails(set.getString("WARD_ACCOUNT_DETAILS"));
				request.setOrg(set.getString("ORGANIZATION"));
				request.setOrgLeader(set.getString("ORGANIZATION_LEADER"));
				request.setId("REI-" + set.getString("INDEX"));
				request.setActivityName(set.getString("ACTIVITY_NAME"));
				request.setUser(user);
				requests.add(request);
			}
			set.close();
			stmt.close();

			stmt = conn.prepareStatement(expenseQuery);
			stmt.setString(1, userGuid);
			set = stmt.executeQuery();
			while (set.next()) {
				Request request = new Request();
				request.setType("Expense");
				request.setGuid(set.getString("GUID"));
				request.setAmt(set.getDouble("AMOUNT"));
				request.setName(set.getString("NAME"));
				request.setDetails(set.getString("DETAILS"));
				request.setId("EXP-" + set.getString("INDEX"));
				request.setStatus(set.getString("STATUS"));
				request.setUser(user);
				requests.add(request);
			}
			set.close();
			stmt.close();

			return requests;
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

	public static void setParent(Connection conn, Budget child) {
		try {
			String query = "SELECT B.NAME, B.\"DESC\", B.REMAINING, B.PARENT FROM BUDGETS WHERE GUID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, child.getParentGuid());
			ResultSet set = stmt.executeQuery();
			while (set.next()) {
				Budget parent = new Budget();
				parent.setGuid(child.getParentGuid());
				parent.setName(set.getString("NAME"));
				parent.setDesc(set.getString("DESC"));
				parent.setRemaining(set.getDouble("REMAINING"));
				parent.setParentGuid(set.getString("PARENT"));
				setParent(conn, parent);
				child.setParent(parent);
			}
			set.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Request> selectAllActionableRequests(String userGuid) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			ArrayList<Request> requests = new ArrayList<>();

			//Grab all activities that I have access to
			//then grab all requests for those activity types
			String activityQuery = "SELECT ACTIVITY_TYPE_REF_GUID "
					+ "FROM ACTIVITY_TYPE_ASSOCIATIONS ATA "
					+ "WHERE ATA.USER_GUID = ? OR ATA.GROUP_GUID = (SELECT GROUP_GUID FROM USER_GROUP_XREF WHERE USER_GUID = ?)";

			ArrayList<String> activities = new ArrayList<>();
			PreparedStatement stmt = conn.prepareStatement(activityQuery);
			stmt.setString(1, userGuid);
			stmt.setString(2, userGuid);
			ResultSet set = stmt.executeQuery();
			while (set.next()) {
				activities.add(set.getString("ACTIVITY_TYPE_REF_GUID"));
			}
			set.close();
			stmt.close();

			String activityRequests = ""
					+ "SELECT "
					+ "AR.GUID, "
					+ "AR.TITLE, "
					+ "AR.SUMMARY, "
					+ "AR.\"START\", "
					+ "AR.\"END\", "
					+ "AR.AMOUNT, "
					+ "STR.NAME STATUS, "
					+ "U.USERNAME, "
					+ "AR.\"INDEX\", "
					+ "ATR.NAME ACTIVITY_NAME, "
					+ "AR.REQUESTED_BY "
					+ "FROM ACTIVITY_REQUESTS AR "
					+ "JOIN STATUS_TYPE_REF STR ON AR.STATUS_TYPE_REF_GUID = STR.GUID "
					+ "JOIN ACTIVITY_TYPE_REF ATR ON AR.ACTIVITY_TYPE_REF_GUID = ATR.GUID "
					+ "JOIN USERS U ON U.GUID = AR.REQUESTED_BY "
					+ ""
					+ "WHERE AR.ACTIVITY_TYPE_REF_GUID = ?";
			String reimbursementRequests = ""
					+ "SELECT "
					+ "RR.GUID, "
					+ "RR.AMOUNT, "
					+ "STR.NAME STATUS, "
					+ "U.USERNAME, "
					+ "RR.TAX, "
					+ "RR.WARD_ACCOUNT, "
					+ "RR.WARD_ACCOUNT_DETAILS, "
					+ "RR.ORGANIZATION, "
					+ "RR.ORGANIZATION_LEADER, "
					+ "RR.\"INDEX\", "
					+ "ATR.NAME ACTIVITY_NAME "
					+ "FROM REIMBURSEMENT_REQUESTS RR "
					+ "JOIN STATUS_TYPE_REF STR ON RR.STATUS_TYPE_REF_GUID = STR.GUID "
					+ "JOIN USERS U ON U.GUID = RR.REQUESTED_BY "
					+ "LEFT JOIN ACTIVITY_TYPE_REF ATR ON ATR.GUID = RR.RELATED_ACTIVITY "
					+ "WHERE RR.ACTIVITY_TYPE_REF_GUID = ?";
			String expenseRequests = ""
					+ "SELECT "
					+ "ER.GUID, "
					+ "ER.AMOUNT, "
					+ "ER.NAME, "
					+ "ER.DETAILS, "
					+ "U.USERNAME, "
					+ "ER.\"INDEX\", "
					+ "STR.NAME STATUS "
					+ "FROM EXPENSE_REQUESTS ER "
					+ "JOIN STATUS_TYPE_REF STR ON RR.STATUS_TYPE_REF_GUID = STR.GUID "
					+ "JOIN USERS U ON U.GUID = RR.REQUESTED_BY "
					+ "WHERE RR.ACTIVITY_TYPE_REF_GUID = ?";
			String userQuery = "SELECT USERNAME, FIRSTNAME, LASTNAME, EMAIL, P.CALLING "
					+ "FROM USERS U "
					+ "JOIN PROFILE P ON U.GUID = P.USER_GUID "
					+ "WHERE U.GUID = ?";
			String budgetQuery = "SELECT B.GUID, B.NAME, B.\"DESC\", B.REMAINING, B.PARENT "
					+ "FROM ACTIVITY_TYPE_ASSOCIATIONS ATA "
					+ "JOIN BUDGETS B ON ATA.BUDGET_GUID = B.GUID "
					+ "WHERE ATA.ACTIVITY_TYPE_REF_GUID = ?";

			for (String activity : activities) {
				stmt = conn.prepareStatement(activityRequests);
				stmt.setString(1, activity);
				set = stmt.executeQuery();
				while (set.next()) {
					Request request = new Request();
					request.setType("Activity");
					request.setGuid(set.getString("GUID"));
					request.setId("ACT-" + set.getString("INDEX"));
					request.setTitle(set.getString("TITLE"));
					request.setSummary(set.getString("SUMMARY"));
					request.setStart(DateUtils.parseDate(set.getString("START")));
					request.setEnd(DateUtils.parseDate(set.getString("END")));
					request.setAmt(set.getDouble("AMOUNT"));
					request.setStatus(set.getString("STATUS"));
					request.setActivity_type_guid(activity);
					request.setActivityName(set.getString("ACTIVITY_NAME"));
					request.setUserGuid(set.getString("REQUESTED_BY"));
					request.setActivity_type_guid(activity);
					PreparedStatement stmt2 = conn.prepareStatement(userQuery);
					stmt2.setString(1, request.getUserGuid());
					ResultSet set2 = stmt2.executeQuery();
					while (set2.next()) {
						User user = new User();
						user.setGuid(request.getUserGuid());
						user.setUsername(set2.getString("USERNAME"));
						user.setFirstname(set2.getString("FIRSTNAME"));
						user.setLastname(set2.getString("LASTNAME"));
						user.setEmail(set2.getString("EMAIL"));
						user.setCalling(set2.getString("CALLING"));
						request.setUser(user);
					}
					set2.close();
					stmt2.close();

					stmt2 = conn.prepareStatement(budgetQuery);
					stmt2.setString(1, activity);
					set2 = stmt2.executeQuery();
					while (set2.next()) {
						Budget budget = new Budget();
						budget.setGuid(set2.getString("GUID"));
						budget.setName(set2.getString("NAME"));
						budget.setDesc(set2.getString("DESC"));
						budget.setRemaining(set2.getDouble("REMAINING"));
						budget.setParentGuid(set2.getString("PARENT"));
						setParent(conn, budget);
						request.setBudget(budget);
					}
					set2.close();
					stmt2.close();
					requests.add(request);
				}
				set.close();
				stmt.close();

				stmt = conn.prepareStatement(reimbursementRequests);
				stmt.setString(1, activity);
				set = stmt.executeQuery();
				while (set.next()) {
					Request request = new Request();
					request.setType("Reimbursement");
					request.setGuid(set.getString("GUID"));
					request.setAmt(set.getDouble("AMOUNT"));
					request.setStatus(set.getString("STATUS"));
					request.setTax(set.getFloat("TAX"));
					request.setWardAccount(set.getString("WARD_ACCOUNT").equals("1"));
					request.setWardAccountDetails(set.getString("WARD_ACCOUNT_DETAILS"));
					request.setOrg(set.getString("ORGANIZATION"));
					request.setOrgLeader(set.getString("ORGANIZATION_LEADER"));
					request.setId("REI-" + set.getString("INDEX"));
					request.setActivityName(set.getString("ACTIVITY_NAME"));

					PreparedStatement stmt2 = conn.prepareStatement(userQuery);
					stmt2.setString(1, request.getUserGuid());
					ResultSet set2 = stmt2.executeQuery();
					while (set2.next()) {
						User user = new User();
						user.setGuid(request.getUserGuid());
						user.setUsername(set2.getString("USERNAME"));
						user.setFirstname(set2.getString("FIRSTNAME"));
						user.setLastname(set2.getString("LASTNAME"));
						user.setEmail(set2.getString("EMAIL"));
						user.setCalling(set2.getString("CALLING"));
						request.setUser(user);
					}
					set2.close();
					stmt2.close();

					requests.add(request);
				}
				set.close();
				stmt.close();

				stmt = conn.prepareStatement(expenseRequests);
				stmt.setString(1, activity);
				set = stmt.executeQuery();
				while (set.next()) {
					Request request = new Request();
					request.setType("Expense");
					request.setGuid(set.getString("GUID"));
					request.setAmt(set.getDouble("AMOUNT"));
					request.setName(set.getString("NAME"));
					request.setDetails(set.getString("DETAILS"));
					request.setId("EXP-" + set.getString("INDEX"));
					request.setStatus(set.getString("STATUS"));

					PreparedStatement stmt2 = conn.prepareStatement(userQuery);
					stmt2.setString(1, request.getUserGuid());
					ResultSet set2 = stmt2.executeQuery();
					while (set2.next()) {
						User user = new User();
						user.setGuid(request.getUserGuid());
						user.setUsername(set2.getString("USERNAME"));
						user.setFirstname(set2.getString("FIRSTNAME"));
						user.setLastname(set2.getString("LASTNAME"));
						user.setEmail(set2.getString("EMAIL"));
						user.setCalling(set2.getString("CALLING"));
						request.setUser(user);
					}
					set2.close();
					stmt2.close();

					requests.add(request);
				}
				set.close();
				stmt.close();
			}

			return requests;
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
}
