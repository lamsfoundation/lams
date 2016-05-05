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
package org.lamsfoundation.lams.admin.web.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author jliew
 *
 * @struts:action path="/user/searchsingle" validate="false"
 *
 * @struts:action-forward name="basiclist" path="/user/basiclist.jsp"
 */
public class UserSearchSingleTermAction extends Action {

    private static IUserManagementService service;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	service = AdminServiceProxy.getService(getServlet().getServletContext());
	String term = WebUtil.readStrParam(request, "term", true);
	Integer orgId = WebUtil.readIntParam(request, "orgId", true);

	if (StringUtils.isNotBlank(term)) {
	    List users = new ArrayList();
	    if (orgId != null) {
		// filter results according to user's roles
		Organisation org = (Organisation) service.findById(Organisation.class, orgId);
		Organisation group;
		if (org != null) {
		    HttpSession session = SessionManager.getSession();
		    if (session != null) {
			UserDTO userDto = (UserDTO) session.getAttribute(AttributeNames.USER);
			if (userDto != null) {
			    Integer userId = userDto.getUserID();
			    if (org.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)) {
				group = org.getParentOrganisation();
			    } else {
				group = org;
			    }
			    // get search results, filtered according to orgId
			    if (request.isUserInRole(Role.SYSADMIN) || service.isUserGlobalGroupAdmin()) {
				users = service.searchUserSingleTerm(term, orgId);
			    } else if (service.isUserInRole(userId, group.getOrganisationId(), Role.GROUP_ADMIN)
				    || service.isUserInRole(userId, group.getOrganisationId(), Role.GROUP_MANAGER)) {
				if (group.getCourseAdminCanBrowseAllUsers()) {
				    users = service.searchUserSingleTerm(term, orgId);
				} else if (org.getOrganisationType().getOrganisationTypeId()
					.equals(OrganisationType.CLASS_TYPE)) {
				    users = service.searchUserSingleTerm(term, group.getOrganisationId(), orgId);
				}
			    }
			}
		    }
		}
	    } else {
		// if there's no orgId param, search all users
		users = service.searchUserSingleTerm(term);
	    }
	    request.setAttribute("users", users);
	}

	return mapping.findForward("basiclist");
    }
}
