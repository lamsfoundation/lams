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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * @author jliew
 *
 *
 *
 *
 */
public class UserSearchAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(UserSearchAction.class);
    private static IUserManagementService service;
    private static MessageService messageService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	initServices();

	if (!(request.isUserInRole(Role.SYSADMIN) || service.isUserGlobalGroupAdmin())) {
	    log.debug("user not sysadmin or global group admin");

	    request.setAttribute("errorName", "UserSearchAction authorisation");
	    request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
	    return mapping.findForward("error");
	}

	return mapping.findForward("usersearchlist");
    }

    /**
     * Returns list of paged users.
     */
    public ActionForward getPagedUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse res) throws IOException, ServletException, JSONException {
	initServices();

	// the organisation type of the children
	String searchString = WebUtil.readStrParam(request, "fcol[1]", true);

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer isSort1 = WebUtil.readIntParam(request, "column[0]", true);
	Integer isSort2 = WebUtil.readIntParam(request, "column[1]", true);
	Integer isSort3 = WebUtil.readIntParam(request, "column[2]", true);
	Integer isSort4 = WebUtil.readIntParam(request, "column[3]", true);
	Integer isSort5 = WebUtil.readIntParam(request, "column[4]", true);

	String sortBy = "userId";
	String sortOrder = "DESC";
	if (isSort1 != null) {
	    sortBy = "userId";
	    sortOrder = isSort1.equals(0) ? "ASC" : "DESC";

	} else if (isSort2 != null) {
	    sortBy = "login";
	    sortOrder = isSort2.equals(0) ? "ASC" : "DESC";

	} else if (isSort3 != null) {
	    sortBy = "firstName";
	    sortOrder = isSort3.equals(0) ? "ASC" : "DESC";

	} else if (isSort4 != null) {
	    sortBy = "lastName";
	    sortOrder = isSort4.equals(0) ? "ASC" : "DESC";

	} else if (isSort5 != null) {
	    sortBy = "email";
	    sortOrder = isSort5.equals(0) ? "ASC" : "DESC";
	}

	List<UserDTO> userDtos = service.getAllUsers(page, size, sortBy, sortOrder, searchString);

	JSONObject responcedata = new JSONObject();
	responcedata.put("total_rows", service.getCountUsers(searchString));

	JSONArray rows = new JSONArray();
	for (UserDTO userDto : userDtos) {

	    JSONObject responseRow = new JSONObject();
	    responseRow.put("userId", userDto.getUserID());
	    responseRow.put("login", StringEscapeUtils.escapeHtml(userDto.getLogin()));
	    String firstName = userDto.getFirstName() == null ? "" : userDto.getFirstName();
	    responseRow.put("firstName", StringEscapeUtils.escapeHtml(firstName));
	    String lastName = userDto.getLastName() == null ? "" : userDto.getLastName();
	    responseRow.put("lastName", StringEscapeUtils.escapeHtml(lastName));
	    String email = userDto.getEmail() == null ? "" : userDto.getEmail();
	    responseRow.put("email", StringEscapeUtils.escapeHtml(email));
	    if ( userDto.getPortraitUuid() != null )
		responseRow.put("portraitId", userDto.getPortraitUuid());
	    rows.put(responseRow);
	}
	responcedata.put("rows", rows);
	res.setContentType("application/json;charset=utf-8");
	res.getWriter().print(new String(responcedata.toString()));
	return null;
    }

    private void initServices() {
	if (service == null) {
	    service = AdminServiceProxy.getService(getServlet().getServletContext());
	}
	if (messageService == null) {
	    messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());
	}
    }

}
