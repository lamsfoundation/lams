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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.gradebook.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Recalculates total marks for all users in a lesson. Then stores that mark in a gradebookUserLesson. Doesn't
 * affect anyhow gradebookUserActivity objects. Servlet accepts only one parameter - "lessonID".
 * 
 * @author Andrey Balan
 */
@SuppressWarnings("serial")
public class RecalculateTotalMarksForLessonServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(RecalculateTotalMarksForLessonServlet.class);

    private static IAuditService auditService;
    private static ILessonService lessonService;
    private static IGradebookService gradebookService;

    @Override
    public void init() throws ServletException {
	WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	auditService = (IAuditService) ctx.getBean("auditService");
	lessonService = (ILessonService) ctx.getBean("lessonService");
	gradebookService = (IGradebookService) ctx.getBean("gradebookService");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	PrintWriter out = response.getWriter();

	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Lesson lesson = lessonService.getLesson(lessonId);
	if (lesson == null) {
	    log.error("RecalculateTotalMarksForLessonServlet: lesson not found " + lessonId);
	    out.println("ERROR: lesson not found " + lessonId);
	    out.close();
	    return;
	}

	try {
	    gradebookService.recalculateTotalMarksForLesson(lessonId);
	} catch (Throwable e) {
	    String errorMsg = "Error occured " + e.getMessage() + e.getCause();
	    out.println(errorMsg);
	    out.close();
	    return;
	}

	String msg = "Total marks have been successfully recalculated for lessonId " + lessonId;
	auditService.log("RecalculateTotalMarksForLessonServlet", msg);
	out.println(msg);
	out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	doGet(request, response);
    }
}