/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author P-ratt
 */
public class DateUtils {

	public static Date parseDate(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss.SSS a");
			return sdf.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
