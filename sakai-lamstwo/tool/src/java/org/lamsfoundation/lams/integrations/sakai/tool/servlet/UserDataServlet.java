/******************************************************************************
 * UserDataServlet.java
 * 
 * Copyright (c) 2007 LAMS Foundation
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

package org.lamsfoundation.lams.integrations.sakai.tool.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic;
import org.sakaiproject.component.cover.ComponentManager;

public class UserDataServlet extends HttpServlet {

	private static final long serialVersionUID = 1182398377731015444L;
	
	private static Log log = LogFactory.getLog(UserDataServlet.class);
	
	private static final String PARAM_USERNAME		= "username";
	private static final String PARAM_TIMESTAMP		= "timestamp";
	private static final String PARAM_HASH			= "hash";

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		LamstwoLogic logic = (LamstwoLogic)ComponentManager.get("org.lamsfoundation.lams.integrations.sakai.logic.LamstwoLogic");
		
		// get parameters
		String usernameParam = request.getParameter(PARAM_USERNAME);
		String timestampParam = request.getParameter(PARAM_TIMESTAMP);
		String hashParam = request.getParameter(PARAM_HASH);

		// check parameters
		if (usernameParam == null || timestampParam == null
				|| hashParam == null) {
			String message = "missing expected parameters";
			log.error("Unable to retrieve user data: missing expected parameters");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					message);
			return;
		}
		
		String serverKey = logic.getServerKey();
		String serverID = logic.getServerID();
		
		if (serverID == null || serverKey == null) {
			log.error("Unable to retrieve user data, server id or server key is null");
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
			"LAMS tool configuration error");
		}

		// check whether hash is valid
		String[] elements = {timestampParam, usernameParam, serverID, serverKey};
		if (!logic.isHashValid(elements, hashParam)) {
			log.error("Unable to process request, invalid hash");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "authentication failed");
		}
		
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String userInfo = logic.getUserInfo(usernameParam);
        
        if (userInfo == null) {
        	throw new ServletException("Could not find user with id: " + usernameParam);
        }
		out.println(userInfo);
	}
}