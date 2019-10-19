/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.requestor;

import com.prt.models.User;
import com.prt.utils.DBConnection;
import com.prt.utils.EncryptionHelper;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;

/**
 *
 * @author P-ratt
 */
public class SQLUserProcess {

    public static User selectUser(String username) throws SQLException {
        Connection conn = null;
        try {
            if (username != null) {

                conn = DBConnection.getInstance().getDataSource().getConnection();
                conn.setAutoCommit(false);

                String query = "SELECT "
                        + "U.GUID, "
                        + "U.USERNAME, "
                        + "U.FIRSTNAME, "
                        + "U.LASTNAME, "
                        + "U.EMAIL, "
                        + "P.PASSWORD, "
                        + "P.SALT, "
                        + "PROF.CALLING, "
                        + "U.CREATED_BY, "
                        + "U.ADMINISTRATOR "
                        + "FROM USERS U "
                        + "JOIN PASSWORDS P ON U.PASSWORD_GUID = P.GUID "
                        + "JOIN PROFILE PROF ON U.GUID = PROF.USER_GUID "
                        + "WHERE U.USERNAME = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                ResultSet set = stmt.executeQuery();
                User user = new User();
                while (set.next()) {
                    user.setGuid(set.getString("GUID"));
                    user.setUsername(set.getString("USERNAME"));
                    user.setFirstname(set.getString("FIRSTNAME"));
                    user.setLastname(set.getString("LASTNAME"));
                    user.setEmail(set.getString("EMAIL"));
                    user.setPassword(set.getString("PASSWORD"));
                    user.setSalt(set.getString("SALT"));
                    user.setCalling(set.getString("CALLING"));
                    user.setAdmin(set.getString("ADMINISTRATOR").equals("1"));
                }
                set.close();
                stmt.close();
                return user;
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

    public static ArrayList<User> selectAllUsers() throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getDataSource().getConnection();
            conn.setAutoCommit(false);

            String query = "SELECT "
                    + "U.GUID, "
                    + "U.USERNAME, "
                    + "U.FIRSTNAME, "
                    + "U.LASTNAME, "
                    + "U.EMAIL, "
                    + "P.CALLING, "
                    + "U.CREATED_BY, "
                    + "U.ADMINISTRATOR "
                    + "FROM USERS U "
                    + "JOIN PROFILE P ON U.GUID = P.USER_GUID ";

            ArrayList<User> users = new ArrayList<>();
            Statement stmt = conn.createStatement();
            ResultSet set = stmt.executeQuery(query);
            while (set.next()) {
                User user = new User();
                user.setGuid(set.getString("GUID"));
                user.setUsername(set.getString("USERNAME"));
                user.setFirstname(set.getString("FIRSTNAME"));
                user.setLastname(set.getString("LASTNAME"));
                user.setEmail(set.getString("EMAIL"));
                user.setCalling(set.getString("CALLING"));
                user.setCreatedBy(set.getString("CREATED_BY"));
                user.setAdmin(set.getString("ADMINISTRATOR").equals("1"));
                users.add(user);
            }
            set.close();
            stmt.close();

            return users;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
        return null;
    }

    public static boolean addNewUser(User user) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getDataSource().getConnection();
            conn.setAutoCommit(false);

            //generate random password to create user account with
            byte[] newPassBytes = new byte[8];
            new Random().nextBytes(newPassBytes);
            String newPassword = new String(newPassBytes, "UTF-8");

            byte[] salt = EncryptionHelper.generateSalt();
            String saltStr = new String(Base64.getEncoder().encode(salt), "UTF-8");
            String password = EncryptionHelper.encrypt(newPassword, salt);

            String newPass = "{call INSERT INTO PASSWORDS (PASSWORD, SALT) VALUES (?, ?) RETURNING GUID INTO ?}";

            CallableStatement passStmt = conn.prepareCall(newPass);
            passStmt.setString(1, password);
            passStmt.setString(2, saltStr);
            passStmt.registerOutParameter(3, Types.VARCHAR);
            passStmt.executeUpdate();
            String newPassGuid = passStmt.getString(3);
            passStmt.close();

            String query = "{call INSERT INTO USERS (USERNAME, FIRSTNAME, LASTNAME, EMAIL, PASSWORD_GUID, CREATED_BY, CREATED_DATE, ADMINISTRATOR) VALUES (?, ?, ?, ?, ?, ?, SYSTIMESTAMP, ?) RETURNING GUID INTO ?}";

            CallableStatement userStmt = conn.prepareCall(query);
            userStmt.setString(1, user.getUsername());
            userStmt.setString(2, user.getFirstname());
            userStmt.setString(3, user.getLastname());
            userStmt.setString(4, user.getEmail());
            userStmt.setString(5, newPassGuid);
            userStmt.setString(6, user.getCreatedBy());
            userStmt.setString(7, user.isAdmin() ? "1" : "0");
            userStmt.registerOutParameter(8, Types.VARCHAR);
            userStmt.executeUpdate();
            String newUserGuid = userStmt.getString(8);
            userStmt.close();

            query = "INSERT INTO PROFILE (USER_GUID, CALLING) VALUES (?, ?)";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newUserGuid);
            stmt.setString(2, user.getCalling());
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

    public static boolean editExistingUser(User user) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getDataSource().getConnection();
            conn.setAutoCommit(false);

            String query = "UPDATE USERS SET USERNAME = ?, FIRSTNAME = ?, LASTNAME = ?, EMAIL = ?, ADMINISTRATOR = ? WHERE GUID = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getFirstname());
            stmt.setString(3, user.getLastname());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.isAdmin() ? "1" : "0");
            stmt.setString(6, user.getGuid());
            stmt.executeUpdate();
            stmt.close();

            query = "UPDATE PROFILE SET CALLING = ? WHERE USER_GUID = ?";

            stmt = conn.prepareStatement(query);
            stmt.setString(1, user.getCalling());
            stmt.setString(2, user.getGuid());
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

    public static boolean removeExistingUser(User user) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getDataSource().getConnection();
            conn.setAutoCommit(false);

            String query = "DELETE FROM USERS WHERE GUID = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, user.getGuid());
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
