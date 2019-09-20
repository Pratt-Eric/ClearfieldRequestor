/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.utils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author P-ratt
 */
public class RestUtil {

	public static String post(String url, String params) {
		try {
			Client client = ClientBuilder.newClient();
			WebTarget resource = client.target(url);
			Response response = resource.request(MediaType.APPLICATION_JSON).post(Entity.json(params));
			return response.readEntity(String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
