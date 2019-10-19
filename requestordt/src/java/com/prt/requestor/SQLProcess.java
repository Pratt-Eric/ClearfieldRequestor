/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.requestor;

import com.prt.models.Password;
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
import java.util.UUID;

/**
 *
 * @author P-ratt
 */
public class SQLProcess {

    public static boolean initializeDefaultUser() throws SQLException {
        Connection conn = null;

        try {

            conn = DBConnection.getInstance().getDataSource().getConnection();
            conn.setAutoCommit(false);
            //Grab users
            //if users is empty then create admin user
            ArrayList<User> users = new ArrayList<>();
            Statement stmt = conn.createStatement();
            ResultSet set = stmt.executeQuery("SELECT * FROM USERS");
            while (set.next()) {
                users.add(new User(set.getString("GUID"), set.getString("USERNAME"), set.getString("FIRSTNAME"), set.getString("LASTNAME"), set.getString("EMAIL"), set.getString("PASSWORD_GUID"), set.getString("ADMINISTRATOR").equals("1")));
            }
            set.close();
            stmt.close();

            if (users.isEmpty()) {
                //create admin user
                byte[] salt = EncryptionHelper.generateSalt();
                String saltStr = new String(Base64.getEncoder().encode(salt), "UTF-8");
                String admin = EncryptionHelper.encrypt("admin", salt);

                String query = "{call INSERT INTO PASSWORDS (PASSWORD, SALT) values (?, ?) RETURNING GUID INTO ?}";
                CallableStatement newPass = conn.prepareCall(query);
                newPass.setString(1, admin);
                newPass.setString(2, saltStr);
                newPass.registerOutParameter(3, Types.VARCHAR);
                newPass.executeUpdate();
                String newPasswordGuid = newPass.getString(3);
                newPass.close();

                query = "{call INSERT INTO USERS (USERNAME, FIRSTNAME, PASSWORD_GUID) VALUES (?, ?, ?) RETURNING GUID INTO ?}";
                CallableStatement newUser = conn.prepareCall(query);
                newUser.setString(1, "admin");
                newUser.setString(2, "admin");
                newUser.setString(3, newPasswordGuid);
                newUser.registerOutParameter(4, Types.VARCHAR);
                newUser.executeUpdate();
                String newUserGuid = newUser.getString(4);
                newUser.close();

                query = "INSERT INTO PROFILE (USER_GUID, CALLING) VALUES (?, ?)";

                PreparedStatement stmt2 = conn.prepareStatement(query);
                stmt2.setString(1, newUserGuid);
                stmt2.setString(2, "Administrator");
                stmt2.executeUpdate();
                stmt2.close();
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
        return false;
    }
}
