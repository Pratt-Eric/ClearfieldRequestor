/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.requestor;

import com.prt.models.User;
import com.prt.utils.HibernateUtil;
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

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
