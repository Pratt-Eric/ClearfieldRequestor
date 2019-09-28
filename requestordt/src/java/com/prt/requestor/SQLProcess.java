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
import java.util.UUID;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author P-ratt
 */
public class SQLProcess {

    public static boolean initializeDefaultUser() {
        try {
            //Grab users
            //if users is empty then create admin user
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<User> users = session.createCriteria(User.class).list();

            if (users.isEmpty()) {
                //create admin user
                byte[] salt = EncryptionHelper.generateSalt();
                String saltStr = new String(Base64.getEncoder().encode(salt), "UTF-8");
                String admin = EncryptionHelper.encrypt("admin", salt);

                session.beginTransaction();
                Password password = new Password(UUID.randomUUID(), admin, saltStr);
                session.save(password);
                session.getTransaction().commit();

                session.beginTransaction();
                Criteria criteria = session.createCriteria(Password.class);
                criteria.add(Restrictions.eq("password", admin));
                password = (Password) criteria.uniqueResult();
                User user = new User(UUID.randomUUID(), "admin", "admin", "", "", password.getUuid());
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
