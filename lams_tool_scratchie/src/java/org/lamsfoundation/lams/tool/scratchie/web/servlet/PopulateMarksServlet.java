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


package org.lamsfoundation.lams.tool.scratchie.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

public class PopulateMarksServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(PopulateMarksServlet.class);

    @Autowired
    private static IScratchieService scratchieService;

    /*
     * Request Spring to lookup the applicationContext tied to the current ServletContext and inject service beans
     * available in that applicationContext.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    /*
     * Expects arbitrary parameter "toolContentId". And when it's available - it recalculates marks only for the
     * sessions belonging to that toolContent, if this parameter is missing - it goes in a loop and recalculates for
     * session uids from 1 to 20 000.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

	PrintWriter out = response.getWriter();
	int sessionCount = 0;
	try {
	    List<ScratchieSession> sessions = new ArrayList<ScratchieSession>();

	    Long toolContentId = WebUtil.readLongParam(request, "toolContentId", true);
	    //in case toolContentId is not specified traverse the first 20 000 session ids
	    if (toolContentId == null) {

		for (int i = 1; i < 20000; i++) {
		    ScratchieSession session = scratchieService.getScratchieSessionBySessionId(new Long(i));

		    if (session != null) {
			sessions.add(session);
		    }

		}

		//otherwise - only the ones that belong to specified toolContentId
	    } else {
		List<ScratchieSession> dbSessions = scratchieService.getSessionsByContentId(toolContentId);
		sessions.addAll(dbSessions);
	    }

	    //recalculate marks for all selected sessions
	    for (ScratchieSession session : sessions) {

		ScratchieUser leader = session.getGroupLeader();
		if ((leader != null)) {
		    scratchieService.recalculateMarkForSession(session.getSessionId(), true);
		    log.debug("recalculateMarkForSession uid:" + session.getUid());
		    out.println("recalculateMarkForSession uid:" + session.getUid());
		}
	    }

	    sessionCount = sessions.size();

	} catch (Exception e) {
	    log.error("LAMS ERROR: " + e.getMessage() + e.getCause());
	    out.println("LAMS ERROR: " + e.getMessage() + e.getCause());
	    out.close();
	    throw new ServletException("LAMS ERROR: " + e.getMessage() + e.getCause());
	}

	out.println("OK. Total number of sessions processed " + sessionCount);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	doGet(request, response);
    }

}
