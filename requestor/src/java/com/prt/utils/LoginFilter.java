/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prt.utils;

import com.prt.controllers.GuestPreferences;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author P-ratt
 */
public class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig fc) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) sr;
		HttpServletResponse servletResponse = (HttpServletResponse) sr1;
		HttpSession session = servletRequest.getSession(false);
		GuestPreferences preferences = null;
		if (session == null) {
			session = servletRequest.getSession();
		} else {
			preferences = (GuestPreferences) session.getAttribute("guestPreferences");
		}
		if (preferences == null || preferences.userGuid == null || preferences.userGuid.isEmpty()) {
			servletResponse.sendRedirect("http://localhost:8080/requestor/login.xhtml");
		} else {
			//first check if the user is allowed to go to the page. If not, then redirect
//			if (!preferences.isAdmin() && preferences.adminScreens.contains(servletRequest.getRequestURI().replace("/requestor/main/", ""))) {
//				servletResponse.sendRedirect("http://localhost:8080/requestor/access.xhtml");
//			}
			if (!preferences.isAdmin() && !servletRequest.getRequestURI().replace("/requestor/main/", "").contains("user/")) {
				servletResponse.sendRedirect("http://localhost:8080/requestor/access.xhtml");
			}

			fc.doFilter(sr, sr1);
		}
	}

	@Override
	public void destroy() {

	}

}
