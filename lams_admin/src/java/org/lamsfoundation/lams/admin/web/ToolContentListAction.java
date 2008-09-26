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
package org.lamsfoundation.lams.admin.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.learningdesign.dto.LearningLibraryDTO;
import org.lamsfoundation.lams.learningdesign.dto.LibraryActivityDTO;
import org.lamsfoundation.lams.learningdesign.service.ILearningDesignService;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author jliew
 * 
 * @struts:action path="/toolcontentlist" scope="request" validate="false"
 * 
 * @struts:action-forward name="toolcontentlist" path=".toolcontentlist"
 * @struts.action-forward name="error" path=".error"
 */
public class ToolContentListAction extends Action {

    private String getUserLanguage() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getLocaleLanguage() : "";
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// check permission
	if (!(request.isUserInRole(Role.SYSADMIN) || request.isUserInRole(Role.AUTHOR_ADMIN))) {
	    request.setAttribute("errorName", "ToolContentListAction");
	    request.setAttribute("errorMessage", AdminServiceProxy.getMessageService(getServlet().getServletContext())
		    .getMessage("error.authorisation"));
	    return mapping.findForward("error");
	}

	// get learning library dtos
	WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	ILearningDesignService learningDesignService = (ILearningDesignService) ctx.getBean("learningDesignService");

	List learningLibraryDTOs = learningDesignService.getAllLearningLibraryDetails(getUserLanguage());

	ArrayList<LibraryActivityDTO> activeTools = filterActiveTools(learningLibraryDTOs);
	request.setAttribute("activeTools", activeTools);

	// get tool versions
	IUserManagementService usermanagementService = (IUserManagementService) ctx.getBean("userManagementService");

	HashMap<Long, String> toolVersions = new HashMap<Long, String>();
	List<Tool> tools = usermanagementService.findAll(Tool.class);
	for (Tool tool : tools) {
	    toolVersions.put(tool.getToolId(), tool.getToolVersion());
	}
	request.setAttribute("toolVersions", toolVersions);

	// get tool database versions
	HashMap<String, Integer> dbVersions = new HashMap<String, Integer>();
	DataSource dataSource = (DataSource) ctx.getBean("dataSource");
	Connection conn = dataSource.getConnection();
	PreparedStatement query = conn.prepareStatement("select system_name, patch_level from patches");
	ResultSet results = query.executeQuery();
	while (results.next()) {
	    dbVersions.put(results.getString("system_name"), results.getInt("patch_level"));
	}
	request.setAttribute("dbVersions", dbVersions);

	return mapping.findForward("toolcontentlist");
    }

    // returns LibraryActivityDTOs of valid tools from full list of tools
    private ArrayList<LibraryActivityDTO> filterActiveTools(List learningLibraryDTOs) {
	ArrayList<LibraryActivityDTO> activeTools = new ArrayList<LibraryActivityDTO>();
	for (int i = 0; i < learningLibraryDTOs.size(); i++) {
	    LearningLibraryDTO dto = (LearningLibraryDTO) learningLibraryDTOs.get(i);
	    if (dto.getValidFlag()) {
		List templateActivities = dto.getTemplateActivities();
		for (int j = 0; j < templateActivities.size(); j++) {
		    LibraryActivityDTO template = (LibraryActivityDTO) templateActivities.get(j);
		    if (template.getToolContentID() != null) {
			if (!toolExists(template, activeTools))
			    activeTools.add(template);
		    }
		}
	    }
	}
	return activeTools;
    }

    private boolean toolExists(LibraryActivityDTO item, ArrayList<LibraryActivityDTO> list) {
	for (LibraryActivityDTO l : list) {
	    if (StringUtils.equals(item.getToolSignature(), l.getToolSignature()))
		return true;
	}
	return false;
    }

}
