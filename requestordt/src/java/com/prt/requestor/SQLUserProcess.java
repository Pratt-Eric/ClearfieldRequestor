/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.requestor;

import com.prt.models.User;
import com.prt.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

                String query = "SELECT * FROM USERS U JOIN PASSWORDS P ON U.PASSWORD_GUID = P.GUID WHERE U.USERNAME = ?";
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
                }
                return user;
            }
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
}
