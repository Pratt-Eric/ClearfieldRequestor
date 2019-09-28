/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.requestor;

import com.prt.models.Password;
import com.prt.models.User;
import com.prt.utils.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author P-ratt
 */
public class SQLUserProcess {

    public static User selectUser(String username) {
        try {
            if (username != null) {
                Session session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                Criteria criteria = session.createCriteria(User.class);
                criteria.add(Restrictions.eq("username", username));
                User user = (User) criteria.uniqueResult();
                criteria = session.createCriteria(Password.class);
                criteria.add(Restrictions.eq("uuid", user.getPassword_uuid()));
                Password password = (Password) criteria.uniqueResult();
                user.setPassword(password.getPassword());
                user.setSalt(password.getSalt());
                session.getTransaction().commit();
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
