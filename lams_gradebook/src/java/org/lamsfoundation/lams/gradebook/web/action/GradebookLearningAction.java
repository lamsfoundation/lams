/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
package org.lamsfoundation.lams.gradebook.web.action; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author lfoxton
 * 
 * Handles the learner interfaces for gradebook
 * 
 * This is where marking for an activity/lesson takes place
 * 
 * 
 * @struts.action path="/gradebookLearning" parameter="dispatch"
 *                scope="request" validate="false"
 * 
 * @struts:action-forward name="learnercoursegradebook"
 *                        path="/gradebookCourseLearner.jsp"
 * @struts:action-forward name="error" path=".error"
 * @struts:action-forward name="message" path=".message"
 */
public class GradebookLearningAction extends LamsDispatchAction{
    
    private static Logger logger = Logger.getLogger(GradebookLearningAction.class);
    
    private static IGradebookService gradebookService;
    private static IUserManagementService userService;
    private static ILessonService lessonService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return null;
    }
    
    @SuppressWarnings("unchecked")
    public ActionForward courseLearner(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	try {
	    initServices();
	    Integer oranisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	    
	    logger.debug("request learnerGradebook for organisation: " + oranisationID.toString());
	    UserDTO user = getUser();
	    if (user == null) {
		logger.error("User missing from session. ");
		return mapping.findForward("error");
	    } else {
		
		Organisation organisation = (Organisation)userService.findById(Organisation.class, oranisationID);
		if (organisation == null) {
		    logger.error("Organisation " + oranisationID + " does not exist. Unable to load gradebook");
		    return mapping.findForward("error");
		}
		
		// Validate whether this user is a monitor for this organisation
		if (!userService.isUserInRole(user.getUserID(), oranisationID, Role.MONITOR)) {
		    logger.error("User " + user.getLogin()
			    + " is not a learner in the requested course. Cannot access the course for gradebook.");
		    return displayMessage(mapping, request, "error.authorisation");
		}

		request.setAttribute("organisationID", oranisationID);
		request.setAttribute("organisationName", organisation.getName());
		
		return mapping.findForward("learnercoursegradebook");
	    }
	} catch (Exception e) {
	    logger.error("Failed to load gradebook monitor", e);
	    return mapping.findForward("error");
	}
    }
    
    private UserDTO getUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }
    
    private ActionForward displayMessage(ActionMapping mapping, HttpServletRequest req, String messageKey) {
	req.setAttribute("messageKey", messageKey);
	return mapping.findForward("message");
    }

    private void initServices() {
	getUserService();
	getLessonService();
	getGradebookService();
    }
    
    private IUserManagementService getUserService() {
	if (userService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    userService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return userService;
    }

    private ILessonService getLessonService() {
	if (lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return lessonService;
    }
    
    private IGradebookService getGradebookService() {
	if (gradebookService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    gradebookService = (IGradebookService) ctx.getBean("gradebookService");
	}
	return gradebookService;
    }

}
 
