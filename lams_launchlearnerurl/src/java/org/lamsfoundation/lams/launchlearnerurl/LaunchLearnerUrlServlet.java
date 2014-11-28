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
package org.lamsfoundation.lams.launchlearnerurl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

/**
 * Decodes lessonId from url and sends redirect to lams learner.
 * 
 * @author Andrey Balan
 */
public class LaunchLearnerUrlServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(LaunchLearnerUrlServlet.class);

    private static final String CODE = "jbdnuteywk";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	// encodedLessonId = "/sqwer"
	String encodedLessonId = request.getPathInfo();
	String lamsUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
	
	 // redirect to login page if accessing / URL
	if (StringUtils.isBlank(encodedLessonId) || encodedLessonId.length() < 2 ) {
	    response.sendRedirect(lamsUrl);
	    return;
	}

	// cut off the first '/' 
	encodedLessonId = encodedLessonId.substring(1);

	// it should contain now only the characters from that string
	if (!encodedLessonId.matches("[" + CODE + "]*")) {
	    log.warn("LessonId: " + encodedLessonId + " contains wrong characters.");
	    response.sendError(HttpServletResponse.SC_NOT_FOUND, "LessonId: " + encodedLessonId + " contains wrong characters.");
	    return;
	}
	encodedLessonId = encodedLessonId.replace(CODE.charAt(0), '0');
	encodedLessonId = encodedLessonId.replace(CODE.charAt(1), '1');
	encodedLessonId = encodedLessonId.replace(CODE.charAt(2), '2');
	encodedLessonId = encodedLessonId.replace(CODE.charAt(3), '3');
	encodedLessonId = encodedLessonId.replace(CODE.charAt(4), '4');
	encodedLessonId = encodedLessonId.replace(CODE.charAt(5), '5');
	encodedLessonId = encodedLessonId.replace(CODE.charAt(6), '6');
	encodedLessonId = encodedLessonId.replace(CODE.charAt(7), '7');
	encodedLessonId = encodedLessonId.replace(CODE.charAt(8), '8');
	encodedLessonId = encodedLessonId.replace(CODE.charAt(9), '9');

	Long lessonId;
	try {
	    lessonId = Long.valueOf(encodedLessonId);
	} catch (NumberFormatException e) {
	    log.warn("LessonId " + encodedLessonId + " is wrong.");
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	    return;
	}

	String redirectURL = lamsUrl + "launchlearner.do?lessonID=" + lessonId;
	response.sendRedirect(redirectURL);
	
    }

}
