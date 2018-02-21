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

package org.lamsfoundation.lams.admin.web.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.admin.AdminConstants;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.integration.security.RandomPasswordGenerator;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class OrgPasswordChangeAction extends DispatchAction {

    private static Logger log = Logger.getLogger(OrgPasswordChangeAction.class);

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Integer organisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	DynaActionForm passForm = (DynaActionForm) form;
	passForm.set(AttributeNames.PARAM_ORGANISATION_ID, organisationID);
	IUserManagementService userManagementService = AdminServiceProxy.getService(getServlet().getServletContext());
	Organisation organisation = (Organisation) userManagementService.findById(Organisation.class, organisationID);
	passForm.set(AttributeNames.PARAM_ORGANISATION_NAME, organisation.getName());

	passForm.set("staffPass", RandomPasswordGenerator.nextPasswordValidated());
	passForm.set("learnerPass", RandomPasswordGenerator.nextPasswordValidated());

	return mapping.findForward("start");
    }

    public ActionForward generatePassword(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().print(RandomPasswordGenerator.nextPasswordValidated());
	return null;
    }

    public ActionForward getGridUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JSONException {
	Integer organisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	String role = WebUtil.readStrParam(request, AttributeNames.PARAM_ROLE);

	UserDTO userDTO = getUserDTO();
	Integer currentUserId = userDTO.getUserID();
	if (!AdminServiceProxy.getSecurityService(getServlet().getServletContext()).isSysadmin(currentUserId,
		"get grid users for org password change", false)) {
	    String warning = "User " + currentUserId + " is not a sysadmin";
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}

	int page = WebUtil.readIntParam(request, AdminConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, AdminConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, AdminConstants.PARAM_SORD);
	String sortColumn = WebUtil.readStrParam(request, AdminConstants.PARAM_SIDX, true);

	// fetch staff or learners
	IUserManagementService userManagementService = AdminServiceProxy.getService(getServlet().getServletContext());
	String[] roles = role.equals("staff") ? new String[] { Role.AUTHOR, Role.MONITOR }
		: new String[] { Role.LEARNER };
	List<UserDTO> users = userManagementService.getAllUsersPaged(organisationID, roles, page - 1, rowLimit,
		sortColumn, sortOrder, null);

	// prepare data needed for paging
	int totalUsers = userManagementService.getCountRoleForOrg(organisationID,
		role.equals("staff") ? new Integer[] { Role.ROLE_AUTHOR, Role.ROLE_MONITOR }
			: new Integer[] { Role.ROLE_LEARNER },
		null);
	int totalPages = new Double(
		Math.ceil(new Integer(totalUsers).doubleValue() / new Integer(rowLimit).doubleValue())).intValue();

	JSONObject resultJSON = new JSONObject();

	resultJSON.put(AdminConstants.ELEMENT_PAGE, page);
	resultJSON.put(AdminConstants.ELEMENT_TOTAL, totalPages);
	resultJSON.put(AdminConstants.ELEMENT_RECORDS, totalUsers);

	JSONArray rowsJSON = new JSONArray();

	// build rows for grid
	for (UserDTO user : users) {
	    JSONObject rowJSON = new JSONObject();
	    rowJSON.put(AdminConstants.ELEMENT_ID, user.getUserID());

	    JSONArray cellJSON = new JSONArray();
	    cellJSON.put(user.getFirstName() + " " + user.getLastName());
	    cellJSON.put(user.getLogin());
	    cellJSON.put(user.getEmail());

	    rowJSON.put(AdminConstants.ELEMENT_CELL, cellJSON);
	    rowsJSON.put(rowJSON);
	}

	resultJSON.put(AdminConstants.ELEMENT_ROWS, rowsJSON);

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(resultJSON.toString());
	return null;
    }

    private UserDTO getUserDTO() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }
}
