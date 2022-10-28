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
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Looks for [login].png images in /tmp/portraits of user IDs within given range and starting with the given prefix
 *
 * @author Marcin Cieslak
 */
@Controller
public class PortraitBatchUploadController {

    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ISecurityService securityService;

    @RequestMapping("/uploadPortraits")
    @ResponseBody
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
	if (!securityService.isSysadmin(getUserID(), "batch upload portraits")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a sysadmin");
	    return null;
	}

	Integer minUserId = WebUtil.readIntParam(request, "minUserID");
	Integer maxUserId = WebUtil.readIntParam(request, "maxUserID");
	String prefix = request.getParameter("prefix");

	List<String> uploadedUserNames = userManagementService.uploadPortraits(minUserId, maxUserId, prefix);
	if (uploadedUserNames != null) {
	    response.setCharacterEncoding("UTF-8");
	    response.setContentType("text/plain");
	    Writer responseWriter = response.getWriter();
	    responseWriter.write("Uploaded portraits for users:\n");
	    for (String userName : uploadedUserNames) {
		responseWriter.write(userName + "\n");
	    }
	    responseWriter.close();
	}

	return null;
    }

    private Integer getUserID() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user == null ? null : user.getUserID();
    }
}