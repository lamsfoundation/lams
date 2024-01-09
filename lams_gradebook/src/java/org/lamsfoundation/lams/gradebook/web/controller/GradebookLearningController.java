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

package org.lamsfoundation.lams.gradebook.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles the learner interfaces for gradebook. This is where marking for an activity/lesson takes place
 *
 * @author lfoxton
 */
@Controller
@RequestMapping("/gradebookLearning")
public class GradebookLearningController {
    private static Logger logger = Logger.getLogger(GradebookLearningController.class);

    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ISecurityService securityService;

    @RequestMapping("")
    public String unspecified() throws Exception {
	return "gradebookCourseLearner";
    }

    @RequestMapping("/courseLearner")
    public String courseLearner(HttpServletRequest request, HttpServletResponse response) throws Exception {

	try {
	    Integer oranisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	    UserDTO user = getUser();
	    if (user == null) {
		logger.error("User missing from session. ");
		return "error";
	    }
	    if (!securityService.hasOrgRole(oranisationID, user.getUserID(),
		    new String[] { Role.GROUP_MANAGER, Role.MONITOR, Role.LEARNER },
		    "get course gradebook for learner")) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a participant in the organisation");
		return null;
	    }
	    if (logger.isDebugEnabled()) {
		logger.debug("Getting learner gradebook for organisation: " + oranisationID);
	    }

	    Organisation organisation = (Organisation) userManagementService.findById(Organisation.class,
		    oranisationID);
	    request.setAttribute("organisationID", oranisationID);
	    request.setAttribute("organisationName", organisation.getName());
	    request.setAttribute("fullName", user.getFullName());

	    return "gradebookCourseLearner";
	} catch (Exception e) {
	    logger.error("Failed to load learner gradebook", e);
	    return "error";
	}
    }

    private UserDTO getUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }
}