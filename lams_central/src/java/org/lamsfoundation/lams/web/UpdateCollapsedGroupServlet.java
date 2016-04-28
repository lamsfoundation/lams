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
package org.lamsfoundation.lams.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationCollapsed;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author jliew
 *
 *
 *
 */
public class UpdateCollapsedGroupServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	// get request parameters
	Integer orgId = WebUtil.readIntParam(request, "orgId", false);
	String collapsed = request.getParameter("collapsed");

	WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(SessionManager
		.getServletContext());
	UserManagementService service = (UserManagementService) ctx.getBean("userManagementService");

	Organisation org = (Organisation) service.findById(Organisation.class, orgId);
	User user = service.getUserByLogin(request.getRemoteUser());

	// sysadmins always have non-collapsed groups; they aren't always members of the org anyway
	if (request.isUserInRole(Role.SYSADMIN)) {
	    return;
	}

	// insert or update userorg's collapsed status
	if ((org != null) && (collapsed != null) && (user != null)) {
	    UserOrganisation uo = service.getUserOrganisation(user.getUserId(), orgId);
	    UserOrganisationCollapsed uoc = uo.getUserOrganisationCollapsed();
	    if (uoc != null) {
		uoc.setCollapsed(new Boolean(collapsed));
	    } else {
		// new row in lams_user_organisation_collapsed
		uoc = new UserOrganisationCollapsed(new Boolean(collapsed), uo);
	    }
	    service.save(uoc);
	}
    }

}
