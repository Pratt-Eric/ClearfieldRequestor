/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.requestor;

import com.prt.models.Password;
import com.prt.models.User;
import com.prt.utils.EncryptionHelper;
import com.prt.utils.HibernateUtil;
import java.util.Base64;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author P-ratt
 */
public class SQLProcess {

	private static HibernateUtil conn;

	public static boolean initializeDefaultUser() {
		try {
			//Grab users
			//if users is empty then create admin user
			Session session = conn.getSessionFactory().openSession();
			List<User> users = session.createCriteria(User.class).list();

			if (users.isEmpty()) {
				//create admin user
				byte[] salt = EncryptionHelper.generateSalt();
				String saltStr = new String(Base64.getEncoder().encode(salt), "UTF-8");
				String admin = EncryptionHelper.encrypt("admin", salt);

				Password password = new Password(1, admin, saltStr);
				User user = new User(1, "admin", "admin", "", "", 1);
				session.beginTransaction();
				session.save(password);
				session.save(user);
				session.getTransaction().commit();

				session.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
