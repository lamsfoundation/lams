/**************************************************************** 
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org) 
 * ============================================================= 
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/ 
 * 
 * This program is free software; you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.scratchie.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.tool.scratchie.service.ScratchieServiceProxy;

public class PopulateMarksServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(PopulateMarksServlet.class);
    
    private IScratchieService service;

    @Override
    public void init() throws ServletException {
	service = ScratchieServiceProxy.getScratchieService(getServletContext());
	super.init();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	PrintWriter out = response.getWriter();
	int sessionCount = 0;
	try {

	    
	    for (int i = 1; i < 20000; i++) {
		ScratchieSession session = service.getScratchieSessionBySessionId(new Long(i));

		if (session != null) {
		    sessionCount++;

		    ScratchieUser leader = session.getGroupLeader();
		    if ((leader != null)) {
			service.recalculateMarkForSession(session.getSessionId(), true);
			log.debug("recalculateMarkForSession uid:" + session.getUid());
			out.println("recalculateMarkForSession uid:" + session.getUid());
		    }
		}

	    }

	} catch (Exception e) {
	    log.error("LAMS ERROR: " + e.getMessage() + e.getCause());
	    out.println("LAMS ERROR: " + e.getMessage() + e.getCause());
	    throw new ServletException("LAMS ERROR: " + e.getMessage() + e.getCause());
	}
	
	out.println("OK. Total number of sessions processed " + sessionCount);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	    IOException {
	doGet(request, response);
    }

}
