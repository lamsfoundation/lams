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

package org.lamsfoundation.lams.admin.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Marcin Cieslak
 */
@Controller
@RequestMapping("/cleanupPreviewLessons")
public class CleanupPreviewLessonsController {

    private static Logger log = Logger.getLogger(CleanupPreviewLessonsController.class);

    private static IMonitoringService monitoringService;
    private static ILessonService lessonService;
    private static ISecurityService securityService;

    @Autowired
    @Qualifier("adminMessageService")
    private MessageService adminMessageService;

    @Autowired
    private WebApplicationContext applicationContext;

    @RequestMapping(path = "/start", method = RequestMethod.POST)
    public String unspecified(HttpServletRequest request, HttpServletResponse response) throws IOException {
	if (!getSecurityService().isSysadmin(getUserID(), "display cleanup preview lessons", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a sysadmin");
	    return null;
	}

	if (!(request.isUserInRole(Role.SYSADMIN))) {
	    request.setAttribute("errorName", "CleanupPreviewLessonsAction");
	    request.setAttribute("errorMessage", adminMessageService.getMessage("error.need.sysadmin"));
	    return "error";
	}

	long[] lessonCount = getLessonService().getPreviewLessonCount();
	request.setAttribute("previewCount", lessonCount[0]);
	request.setAttribute("allLessonCount", lessonCount[1]);

	return "cleanupPreviewLessons";
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String deletePreviewLessons(HttpServletRequest request, HttpServletResponse response) throws IOException {
	Integer userID = getUserID();
	Integer limit = WebUtil.readIntParam(request, "limit", true);
	List<Long> lessonIDs = getLessonService().getPreviewLessons(limit);
	for (Long lessonID : lessonIDs) {
	    log.info("Deleting preview lesson: " + lessonID);
	    // role is checked in this method
	    getMonitoringService().removeLessonPermanently(lessonID, userID);
	}

	long[] lessonCount = getLessonService().getPreviewLessonCount();
	String responseJSON = JsonUtil.toString(lessonCount);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(responseJSON);
	return null;
    }

    private Integer getUserID() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user == null ? null : user.getUserID();
    }

    private IMonitoringService getMonitoringService() {
	if (monitoringService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(applicationContext.getServletContext());
	    monitoringService = (IMonitoringService) ctx.getBean("monitoringService");
	}
	return monitoringService;
    }

    private ILessonService getLessonService() {
	if (lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(applicationContext.getServletContext());
	    lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return lessonService;
    }

    private ISecurityService getSecurityService() {
	if (securityService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(applicationContext.getServletContext());
	    securityService = (ISecurityService) ctx.getBean("securityService");
	}
	return securityService;
    }
}