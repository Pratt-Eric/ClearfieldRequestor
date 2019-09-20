package com.prt.utils;

import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author P-ratt
 */
public class EncryptionHelper {

	public static String encrypt(String password, byte[] salt) {
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 500, 256);
			SecretKey key = factory.generateSecret(spec);
			byte[] result = key.getEncoded();
			return new String(Base64.getEncoder().encode(result), "UTF-8");
		} catch (Exception e) {
		}
		return null;
	}
}
