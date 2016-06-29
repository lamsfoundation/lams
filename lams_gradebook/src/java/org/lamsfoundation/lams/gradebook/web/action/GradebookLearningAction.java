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


package org.lamsfoundation.lams.gradebook.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.security.ISecurityService;
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
 *         Handles the learner interfaces for gradebook
 *
 *         This is where marking for an activity/lesson takes place
 */
public class GradebookLearningAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(GradebookLearningAction.class);

    private static IUserManagementService userService;
    private static ISecurityService securityService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return null;
    }

    public ActionForward courseLearner(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	try {
	    Integer oranisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	    UserDTO user = getUser();
	    if (user == null) {
		GradebookLearningAction.logger.error("User missing from session. ");
		return mapping.findForward("error");
	    }
	    if (!getSecurityService().hasOrgRole(oranisationID, user.getUserID(),
		    new String[] { Role.GROUP_MANAGER, Role.MONITOR, Role.LEARNER }, "get course gradebook for learner",
		    false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a participant in the organisation");
		return null;
	    }
	    if (LamsDispatchAction.log.isDebugEnabled()) {
		GradebookLearningAction.logger.debug("Getting learner gradebook for organisation: " + oranisationID);
	    }

	    Organisation organisation = (Organisation) getUserService().findById(Organisation.class, oranisationID);
	    request.setAttribute("organisationID", oranisationID);
	    request.setAttribute("organisationName", organisation.getName());
	    request.setAttribute("fullName", user.getFirstName() + " " + user.getLastName());

	    return mapping.findForward("learnercoursegradebook");
	} catch (Exception e) {
	    GradebookLearningAction.logger.error("Failed to load learner gradebook", e);
	    return mapping.findForward("error");
	}
    }

    private UserDTO getUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    private IUserManagementService getUserService() {
	if (GradebookLearningAction.userService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    GradebookLearningAction.userService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return GradebookLearningAction.userService;
    }

    private ISecurityService getSecurityService() {
	if (GradebookLearningAction.securityService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    GradebookLearningAction.securityService = (ISecurityService) ctx.getBean("securityService");
	}
	return GradebookLearningAction.securityService;
    }
}