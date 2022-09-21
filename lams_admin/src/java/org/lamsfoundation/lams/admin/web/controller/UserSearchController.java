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

package org.lamsfoundation.lams.admin.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author jliew
 */
@Controller
public class UserSearchController {
    private static Logger log = Logger.getLogger(UserSearchController.class);

    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    @Qualifier("adminMessageService")
    private MessageService messageService;

    @RequestMapping("/usersearch")
    public String unspecified(HttpServletRequest request) throws Exception {
	if (!(request.isUserInRole(Role.APPADMIN) || userManagementService.isUserGlobalGroupManager())) {
	    log.debug("user not appadmin or global group admin");

	    request.setAttribute("errorName", "UserSearchAction authorisation");
	    request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
	    return "error";
	}

	boolean loginAsEnable = Configuration.getAsBoolean(ConfigurationKeys.LOGIN_AS_ENABLE)
		&& request.isUserInRole(Role.SYSADMIN);
	request.setAttribute("loginAsEnable", loginAsEnable);

	return "usersearchlist";
    }

    /**
     * Returns list of paged users.
     */
    @RequestMapping("/usersearch/getPagedUsers")
    @ResponseBody
    public String getPagedUsers(HttpServletRequest request, HttpServletResponse res)
	    throws IOException, ServletException {
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

	List<UserDTO> userDtos = userManagementService.getAllUsers(page, size, sortBy, sortOrder, searchString);

	ObjectNode responcedata = JsonNodeFactory.instance.objectNode();
	responcedata.put("total_rows", userManagementService.getCountUsers(searchString));

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	for (UserDTO userDto : userDtos) {
	    ObjectNode responseRow = JsonNodeFactory.instance.objectNode();
	    responseRow.put("userId", userDto.getUserID());
	    responseRow.put("login", HtmlUtils.htmlEscape(userDto.getLogin()));
	    String firstName = userDto.getFirstName() == null ? "" : userDto.getFirstName();
	    responseRow.put("firstName", HtmlUtils.htmlEscape(firstName));
	    String lastName = userDto.getLastName() == null ? "" : userDto.getLastName();
	    responseRow.put("lastName", HtmlUtils.htmlEscape(lastName));
	    String email = userDto.getEmail() == null ? "" : userDto.getEmail();
	    responseRow.put("email", HtmlUtils.htmlEscape(email));
	    if (userDto.getPortraitUuid() != null) {
		responseRow.put("portraitId", userDto.getPortraitUuid());
	    }
	    rows.add(responseRow);
	}
	responcedata.set("rows", rows);
	res.setContentType("application/json;charset=utf-8");
	return responcedata.toString();
    }

}