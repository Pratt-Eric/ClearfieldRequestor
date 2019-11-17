/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.requestor;

import com.prt.models.Activity;
import com.prt.models.Budget;
import com.prt.models.BudgetTransaction;
import com.prt.models.Calendar;
import com.prt.models.Dashboard;
import com.prt.models.Event;
import com.prt.models.Group;
import com.prt.models.Item;
import com.prt.models.User;
import com.prt.utils.DBConnection;
import com.prt.utils.DateUtils;
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
					+ "WHERE DPX.DASHBOARD_GUID = ?";
			String calsAndBudgets = "SELECT "
					+ "DIX.GUID, "
					+ "DIX.CALENDAR_GUID, "
					+ "DIX.BUDGET_GUID, "
					+ "C.NAME CAL_NAME, "
					+ "C.\"DESC\" CAL_DESC, "
					+ "B.NAME BUD_NAME, "
					+ "C.\"DESC\" BUD_DESC, "
					+ "DIX.\"INDEX\" "
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
						c.setXrefGuid(cbSet.getString("GUID"));
						c.setName(cbSet.getString("CAL_NAME"));
						c.setDesc(cbSet.getString("CAL_DESC"));
						c.setIndex(Integer.parseInt(cbSet.getString("INDEX")));
						dashboard.getCalendars().add(c);
					} else if (budget != null && !budget.isEmpty()) {
						Budget b = new Budget();
						b.setGuid(budget);
						b.setXrefGuid(cbSet.getString("GUID"));
						b.setName(cbSet.getString("BUD_NAME"));
						b.setDesc(cbSet.getString("BUD_DESC"));
						b.setIndex(Integer.parseInt(cbSet.getString("INDEX")));
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

	public static Dashboard selectDashboard(String guid) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			Dashboard dashboard = new Dashboard();
			dashboard.setGuid(guid);

			String query = "SELECT NAME, \"DESC\" FROM DASHBOARDS WHERE GUID = ?";
			String associations = "SELECT "
					+ "DPX.USER_GUID, "
					+ "DPX.GROUP_GUID, "
					+ "U.USERNAME, "
					+ "G.NAME "
					+ "FROM DASHBOARD_PERMISSIONS_XREF DPX "
					+ "LEFT JOIN USERS U ON DPX.USER_GUID = U.GUID "
					+ "LEFT JOIN GROUPS G ON DPX.GROUP_GUID = G.GUID "
					+ "WHERE DPX.DASHBOARD_GUID = ?";
			String calsAndBudgets = "SELECT "
					+ "DIX.GUID, "
					+ "DIX.CALENDAR_GUID, "
					+ "DIX.BUDGET_GUID, "
					+ "C.NAME CAL_NAME, "
					+ "C.\"DESC\" CAL_DESC, "
					+ "B.NAME BUD_NAME, "
					+ "B.\"DESC\" BUD_DESC, "
					+ "DIX.\"INDEX\" "
					+ "FROM DASHBOARD_ITEM_XREF DIX "
					+ "LEFT JOIN CALENDARS C ON DIX.CALENDAR_GUID = C.GUID "
					+ "LEFT JOIN BUDGETS B ON DIX.BUDGET_GUID = B.GUID "
					+ "WHERE DIX.DASHBOARD_GUID = ?";

			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, guid);
			ResultSet set = stmt.executeQuery();
			while (set.next()) {
				dashboard.setName(set.getString("NAME"));
				dashboard.setDesc(set.getString("DESC"));

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
				PreparedStatement stmt2 = conn.prepareStatement(calsAndBudgets);
				stmt2.setString(1, dashboard.getGuid());
				ResultSet set2 = stmt2.executeQuery();
				while (set2.next()) {
					String calendar = set2.getString("CALENDAR_GUID");
					String budget = set2.getString("BUDGET_GUID");
					if (calendar != null && !calendar.isEmpty()) {
						Calendar c = new Calendar();
						c.setGuid(calendar);
						c.setXrefGuid(set2.getString("GUID"));
						c.setName(set2.getString("CAL_NAME"));
						c.setDesc(set2.getString("CAL_DESC"));
						c.setIndex(Integer.parseInt(set2.getString("INDEX")));
						dashboard.getCalendars().add(c);
					} else if (budget != null && !budget.isEmpty()) {
						Budget b = new Budget();
						b.setGuid(budget);
						b.setXrefGuid(set2.getString("GUID"));
						b.setName(set2.getString("BUD_NAME"));
						b.setDesc(set2.getString("BUD_DESC"));
						b.setIndex(Integer.parseInt(set2.getString("INDEX")));
						dashboard.getBudgets().add(b);
					}
				}
				set2.close();
				stmt2.close();
			}
			set.close();
			stmt.close();

			return dashboard;
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
				String remove = "DELETE FROM USER_DASHBOARD_XREF WHERE (DASHBOARD_GUID = ? AND USER_GUID NOT IN (SELECT USER_GUID FROM DASHBOARD_PERMISSIONS_XREF WHERE DASHBOARD_GUID = ? AND USER_GUID IS NOT NULL)) OR (DASHBOARD_GUID = ? AND GROUP_GUID NOT IN (SELECT GROUP_GUID FROM DASHBOARD_PERMISSIONS_XREF WHERE DASHBOARD_GUID = ? AND GROUP_GUID IS NOT NULL))";
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
			String items = "INSERT INTO DASHBOARD_ITEM_XREF (CALENDAR_GUID, BUDGET_GUID, DASHBOARD_GUID, \"INDEX\") VALUES (?, ?, ?, ?)";
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
			for (Item item : dashboard.getItems()) {
				addItems.setString(1, item.getCalendarGuid());
				addItems.setString(2, item.getBudgetGuid());
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

	public static ArrayList<Dashboard> selectUserDashboards(String userGuid) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			ArrayList<Dashboard> dashboards = new ArrayList<>();

			String query = "SELECT "
					+ "D.GUID, "
					+ "D.NAME, "
					+ "D.\"DESC\", "
					+ "UDX.GUID XREF_GUID, "
					+ "UDX.\"DEFAULT\" "
					+ "FROM USER_DASHBOARD_XREF UDX "
					+ "JOIN DASHBOARDS D ON UDX.DASHBOARD_GUID = D.GUID "
					+ "WHERE UDX.USER_GUID = ?";
			String calsAndBuds = "SELECT "
					+ "C.GUID CAL_GUID, "
					+ "C.NAME CAL_NAME, "
					+ "B.GUID BUD_GUID, "
					+ "B.NAME BUD_NAME "
					+ "FROM DASHBOARD_ITEM_XREF DIX "
					+ "LEFT JOIN CALENDAR_PERMISSIONS_XREF CPX ON DIX.CALENDAR_GUID = CPX.CALENDAR_GUID AND (CPX.USER_GUID = ? OR CPX.GROUP_GUID IN (SELECT GROUP_GUID FROM USER_GROUP_XREF WHERE USER_GUID = ?)) "
					+ "LEFT JOIN CALENDARS C ON CPX.CALENDAR_GUID = C.GUID "
					+ "LEFT JOIN BUDGET_PERMISSIONS_XREF BPX ON DIX.BUDGET_GUID = BPX.BUDGET_GUID AND (BPX.USER_GUID = ? OR CPX.GROUP_GUID IN (SELECT GROUP_GUID FROM USER_GROUP_XREF WHERE USER_GUID = ?)) "
					+ "LEFT JOIN BUDGETS B ON BPX.BUDGET_GUID = B.GUID "
					+ "WHERE DIX.DASHBOARD_GUID = ?";

			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, userGuid);
			ResultSet set = stmt.executeQuery();
			while (set.next()) {
				Dashboard dashboard = new Dashboard();

				dashboard.setGuid(set.getString("GUID"));
				dashboard.setName(set.getString("NAME"));
				dashboard.setDesc(set.getString("DESC"));
				dashboard.setXrefGuid(set.getString("XREF_GUID"));
				dashboard.setDefaultDashboard(set.getString("DEFAULT").equals("1"));

				PreparedStatement stmt2 = conn.prepareStatement(calsAndBuds);
				stmt2.setString(1, userGuid);
				stmt2.setString(2, userGuid);
				stmt2.setString(3, userGuid);
				stmt2.setString(4, userGuid);
				stmt2.setString(5, dashboard.getGuid());
				ResultSet set2 = stmt2.executeQuery();
				while (set2.next()) {
					String calGuid = set2.getString("CAL_GUID");
					String budGuid = set2.getString("BUD_GUID");
					if (calGuid != null && !calGuid.isEmpty()) {
						Calendar calendar = new Calendar();
						calendar.setGuid(calGuid);
						calendar.setName(set2.getString("CAL_NAME"));
						dashboard.getCalendars().add(calendar);
					} else if (budGuid != null && !budGuid.isEmpty()) {
						Budget budget = new Budget();
						budget.setGuid(budGuid);
						budget.setName(set2.getString("BUD_NAME"));
						dashboard.getBudgets().add(budget);
					}
				}
				set2.close();
				stmt2.close();

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
		return null;
	}

	public static Dashboard selectUserDefaultDashboard(String userGuid) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String query = "SELECT GUID FROM USER_DASHBOARD_XREF WHERE USER_GUID = ? AND \"DEFAULT\" = 1";

			String xrefGuid = null;
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, userGuid);
			ResultSet set = stmt.executeQuery();
			while (set.next()) {
				xrefGuid = set.getString("GUID");
			}
			set.close();
			stmt.close();

			if (xrefGuid != null && !xrefGuid.isEmpty()) {
				String[] params = new String[]{xrefGuid, userGuid};
				return selectUserDashboard(params);
			}
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

	public static Dashboard selectUserDashboard(String[] params) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String xrefGuid = params[0];
			String userGuid = params[1];
			String query = ""
					+ "SELECT "
					+ "D.GUID, "
					+ "D.NAME, "
					+ "D.\"DESC\" "
					+ "FROM USER_DASHBOARD_XREF UDX "
					+ "JOIN DASHBOARDS D ON UDX.DASHBOARD_GUID = D.GUID "
					+ "WHERE UDX.GUID = ?";
			String cals = ""
					+ "SELECT "
					+ "C.GUID, "
					+ "C.NAME, "
					+ "C.\"DESC\", "
					+ "DIX.\"INDEX\", "
					+ "DIX.GUID "
					+ "FROM DASHBOARD_ITEM_XREF DIX "
					+ "LEFT JOIN CALENDAR_PERMISSIONS_XREF CPX ON DIX.CALENDAR_GUID = CPX.CALENDAR_GUID AND (CPX.USER_GUID = ? OR CPX.GROUP_GUID IN (SELECT GROUP_GUID FROM USER_GROUP_XREF WHERE USER_GUID = ?)) "
					+ "LEFT JOIN CALENDARS C ON CPX.CALENDAR_GUID = C.GUID "
					+ "WHERE DIX.DASHBOARD_GUID = ?";
			String activities = ""
					+ "SELECT "
					+ "CE.TITLE, "
					+ "CE.SUMMARY, "
					+ "CE.\"START\", "
					+ "CE.\"END\", "
					+ "ATR.NAME, "
					+ "ATR.\"DESC\" "
					+ "FROM ACTIVITY_TYPE_ASSOCIATIONS ATA "
					+ "LEFT JOIN ACTIVITY_TYPE_REF ATR ON ATA.ACTIVITY_TYPE_REF_GUID = ATR.GUID "
					+ "LEFT JOIN CALENDAR_EVENTS CE ON ATR.GUID = CE.ACTIVITY_TYPE_REF_GUID "
					+ "WHERE ATA.CALENDAR_GUID = ?";
			String buds = ""
					+ "SELECT "
					+ "B.NAME, "
					+ "B.\"DESC\", "
					+ "B.REMAINING, "
					+ "DIX.\"INDEX\", "
					+ "DIX.GUID "
					+ "FROM DASHBOARD_ITEM_XREF DIX "
					+ "LEFT JOIN BUDGET_PERMISSIONS_XREF BPX ON DIX.BUDGET_GUID = BPX.BUDGET_GUID AND (BPX.USER_GUID = ? OR BPX.GROUP_GUID IN (SELECT GROUP_GUID FROM USER_GROUP_XREF WHERE USER_GUID = ?)) "
					+ "LEFT JOIN BUDGETS B ON BPX.BUDGET_GUID = B.GUID "
					+ "WHERE DIX.DASHBOARD_GUID = ?";
			String transactions = ""
					+ "SELECT "
					+ "T.GUID, "
					+ "T.TRANSACTION_NAME, "
					+ "T.TRANSACTION_DESC, "
					+ "T.JUSTIFICATION, "
					+ "T.\"DATE\", "
					+ "T.PAID_TO, "
					+ "T.AMOUNT, "
					+ "T.AUTHORIZED_BY, "
					+ "T.CHECK_NUMBER, "
					+ "T.FAST_OFFERING, "
					+ "BTTR.NAME "
					+ "FROM BUDGET_TRANSACTIONS T "
					+ "JOIN BUDGET_TRANSACTION_TYPE_REF BTTR ON T.BUDGET_TRANSACTION_TYPE_REF_GUID = BTTR.GUID "
					+ "WHERE T.BUDGET_GUID = ?";

			Dashboard dashboard = new Dashboard();

			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, xrefGuid);
			ResultSet set = stmt.executeQuery();
			while (set.next()) {
				dashboard.setGuid(set.getString("GUID"));
				dashboard.setName(set.getString("NAME"));
				dashboard.setDesc(set.getString("DESC"));
			}
			set.close();
			stmt.close();

			stmt = conn.prepareStatement(cals);
			stmt.setString(1, userGuid);
			stmt.setString(2, userGuid);
			stmt.setString(3, dashboard.getGuid());
			set = stmt.executeQuery();
			while (set.next()) {
				Calendar calendar = new Calendar();
				calendar.setGuid(set.getString("GUID"));
				calendar.setName(set.getString("NAME"));
				calendar.setDesc(set.getString("DESC"));
				calendar.setIndex(Integer.parseInt(set.getString("INDEX")));
				calendar.setXrefGuid(set.getString("GUID"));
				dashboard.getCalendars().add(calendar);
			}
			set.close();
			stmt.close();

			stmt = conn.prepareStatement(buds);
			stmt.setString(1, userGuid);
			stmt.setString(2, userGuid);
			stmt.setString(3, dashboard.getGuid());
			set = stmt.executeQuery();
			while (set.next()) {
				Budget budget = new Budget();
				budget.setGuid(set.getString("GUID"));
				budget.setName(set.getString("NAME"));
				budget.setDesc(set.getString("DESC"));
				budget.setRemaining(set.getFloat("REMAINING"));
				budget.setIndex(Integer.parseInt(set.getString("INDEX")));
				budget.setXrefGuid(set.getString("GUID"));
				dashboard.getBudgets().add(budget);
			}
			set.close();
			stmt.close();

			for (Calendar calendar : dashboard.getCalendars()) {
				stmt = conn.prepareStatement(activities);
				stmt.setString(1, calendar.getGuid());
				set = stmt.executeQuery();
				while (set.next()) {
					Event event = new Event();
					event.setTitle(set.getString("TITLE"));
					event.setSummary(set.getString("SUMMARY"));
					event.setStart(DateUtils.parseDate(set.getString("START")));
					event.setEnd(DateUtils.parseDate(set.getString("END")));
					Activity activity = new Activity();
					activity.setName(set.getString("NAME"));
					activity.setDesc(set.getString("DESC"));
					event.setActivity(activity);
					calendar.getEvents().add(event);
				}
				set.close();
				stmt.close();
			}

			for (Budget budget : dashboard.getBudgets()) {
				stmt = conn.prepareStatement(transactions);
				stmt.setString(1, budget.getGuid());
				set = stmt.executeQuery();
				while (set.next()) {
					BudgetTransaction t = new BudgetTransaction();
					t.setGuid(set.getString("GUID"));
					t.setName(set.getString("TRANSACTION_NAME"));
					t.setDesc(set.getString("TRANSACTION_DESC"));
					t.setJustification(set.getString("JUSTIFICATION"));
					t.setDate(set.getDate("DATE"));
					t.setPaidTo(set.getString("PAID_TO"));
					t.setAmount(set.getFloat("AMOUNT"));
					t.setAuthorizedBy(set.getString("AUTHORIZED_BY"));
					t.setCheckNumber(set.getString("CHECK_NUMBER"));
					t.setFastOfferingCode(set.getString("FAST_OFFERING"));
					t.setType(set.getString("NAME"));
					budget.getTransactions().add(t);
				}
				set.close();
				stmt.close();
			}

			return dashboard;
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

	public static boolean addUserDashboards(String[][] params) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String query = "INSERT INTO USER_DASHBOARD_XREF (USER_GUID, DASHBOARD_GUID) VALUES (?, ?)";

			PreparedStatement stmt = conn.prepareStatement(query);
			for (String[] param : params) {
				stmt.setString(1, param[0]);
				stmt.setString(2, param[1]);
				stmt.addBatch();
			}
			stmt.executeBatch();
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

	public static boolean editUserDashboard(String[] params) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			String query = "UPDATE USER_DASHBOARD_XREF SET \"DEFAULT\" = 0 WHERE USER_GUID = ?";
			String update = "UPDATE USER_DASHBOARD_XREF SET \"DEFAULT\" = 1 WHERE GUID = ?";

			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, params[0]);
			stmt.executeUpdate();
			stmt.close();

			stmt = conn.prepareStatement(update);
			stmt.setString(1, params[1]);
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

	public static ArrayList<Dashboard> selectAvailableUserDashboards(String userGuid) throws SQLException {
		Connection conn = null;
		try {
			conn = DBConnection.getInstance().getDataSource().getConnection();
			conn.setAutoCommit(false);

			ArrayList<Dashboard> dashboards = new ArrayList<>();

			String query = "SELECT "
					+ "D.GUID, "
					+ "D.NAME, "
					+ "D.\"DESC\" "
					+ "FROM DASHBOARDS D "
					+ "JOIN DASHBOARD_PERMISSIONS_XREF DPX ON D.GUID = DPX.DASHBOARD_GUID "
					+ "WHERE DPX.USER_GUID = ? OR DPX.GROUP_GUID IN (SELECT GROUP_GUID FROM USER_GROUP_XREF WHERE USER_GUID = ?)";

			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, userGuid);
			stmt.setString(2, userGuid);
			ResultSet set = stmt.executeQuery();
			while (set.next()) {
				Dashboard dashboard = new Dashboard();
				dashboard.setGuid(set.getString("GUID"));
				dashboard.setName(set.getString("NAME"));
				dashboard.setDesc(set.getString("DESC"));
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
		return null;
	}
}
