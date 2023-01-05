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

package org.lamsfoundation.lams.web;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Sends redirect to lams learner. Format: launchlearner.do?lessonID=<encodedLessonId>
 */
public class LaunchLearnerUrlServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(LaunchLearnerUrlServlet.class);

    @Autowired
    private ILessonService lessonService;
    
    /*
     * Request Spring to lookup the applicationContext tied to the current ServletContext and inject service beans
     * available in that applicationContext.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String encodedLessonId = request.getPathInfo();

	// redirect to login page if accessing / URL
	if (StringUtils.isBlank(encodedLessonId) || encodedLessonId.length() < 2) {
	    String lamsUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
	    response.sendRedirect(lamsUrl);
	    return;
	}

	// cut off the first '/'
	encodedLessonId = encodedLessonId.substring(1);

	Long lessonId;
	try {
	    String decodedLessonId = WebUtil.decodeIdForDirectLaunch(encodedLessonId);
	    lessonId = Long.valueOf(decodedLessonId);

	} catch (IllegalArgumentException e) {
	    log.warn("Supplied lessonId: " + encodedLessonId + " has wrong format.");
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
		    "Supplied lessonId: " + encodedLessonId + " has wrong format.");
//	     return mapping.findForward("error");
//	    displayMessage(request, response, "error.lessonid.has.wrong.format");
	    return;
	}

	Lesson lesson = lessonId != null ? lessonService.getLesson(lessonId) : null;
	if (lesson != null) {
	    // return displayMessage(mapping, req, "message.lesson.not.started.cannot.participate");
	}

	request.setAttribute("name", lesson.getLessonName());
	request.setAttribute("description", lesson.getLessonDescription());
	request.setAttribute("status", lesson.getLessonStateId());
	request.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId);

	request.getRequestDispatcher("/launchlearner.jsp").forward(request, response);

	// getServletContext().getContext(serverURLContextPath + "learning")
	// .getRequestDispatcher("/main.jsp").forward(req, res);
	// return null;
	//
	//
	// request.setAttribute( AttributeNames.PARAM_LESSON_ID, encodedLessonId);
	// request.getRequestDispatcher("/launchlearner.do").forward(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doPost(request, response);
    }

    private void displayMessage(HttpServletRequest request, HttpServletResponse response, String messageKey)
	    throws ServletException, IOException {
	request.setAttribute("messageKey", messageKey);
	request.getRequestDispatcher("/.message").forward(request, response);
    }
}