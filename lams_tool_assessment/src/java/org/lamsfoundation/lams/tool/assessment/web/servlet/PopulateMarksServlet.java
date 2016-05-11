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


package org.lamsfoundation.lams.tool.assessment.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.assessment.service.AssessmentServiceProxy;
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class PopulateMarksServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(PopulateMarksServlet.class);

    private IAssessmentService service;

    @Override
    public void init() throws ServletException {
	service = AssessmentServiceProxy.getAssessmentService(getServletContext());
	super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

	PrintWriter out = response.getWriter();
	try {
	    Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	    HttpSession ss = SessionManager.getSession();
	    UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);

	    service.recalculateMarkForLesson(userDTO, lessonId);
	    log.debug("recalculateMarkForLesson lessonId:" + lessonId);
	    out.println("recalculateMarkForLesson uid:" + lessonId);

	} catch (Exception e) {
	    log.error("LAMS ERROR: " + e.getMessage() + e.getCause());
	    out.println("LAMS ERROR: " + e.getMessage() + e.getCause());
	    throw new ServletException("LAMS ERROR: " + e.getMessage() + e.getCause());
	}

	out.println("OK. User marks have been updated.");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	doGet(request, response);
    }

}
