/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.utils;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author P-ratt
 */
public class EmailUtils {

	public static boolean sendEmail(String to, String subject, String body) {
		try {
			Properties properties = System.getProperties();
			properties.setProperty("mail.smtp.host", "localhost");
			Session session = Session.getDefaultInstance(properties);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("clearfieldrequestor@noreply.com"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setText(body);
			Transport.send(message);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
