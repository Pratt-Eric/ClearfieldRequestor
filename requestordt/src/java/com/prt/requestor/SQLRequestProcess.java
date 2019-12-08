/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.requestor;

import com.prt.models.Event;
import com.prt.models.Expense;
import com.prt.models.Reimbursement;
import com.prt.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
					+ "(AMOUNT, STATUS_TYPE_REF_GUID, REQUESTED_BY, TAX, WARD_ACCOUNT, WARD_ACCOUNT_DETAILS, ORGANIZATION, ORGANIZATION_LEADER, \"INDEX\", CREATED) "
					+ "VALUES (?, (SELECT GUID FROM STATUS_TYPE_REF WHERE NAME = 'Pending Review'), ?, ?, ?, ?, ?, ?, ?, SYSTIMESTAMP)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setFloat(1, reimbursement.getAmt());
			stmt.setString(2, reimbursement.getUserGuid());
			stmt.setFloat(3, reimbursement.getTax());
			stmt.setString(4, reimbursement.isWardAccount() ? "1" : "0");
			stmt.setString(5, reimbursement.getWardAccountDetails());
			stmt.setString(6, reimbursement.getOrg().equalsIgnoreCase("Other") || reimbursement.getOrg().equalsIgnoreCase("Council Expense") ? reimbursement.getOrgName() : reimbursement.getOrg());
			stmt.setString(7, reimbursement.getOrgLeader());
			stmt.setInt(8, index);
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
}
