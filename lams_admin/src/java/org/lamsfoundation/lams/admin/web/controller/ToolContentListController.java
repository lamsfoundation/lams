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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.learningdesign.dto.LearningLibraryDTO;
import org.lamsfoundation.lams.learningdesign.dto.LibraryActivityDTO;
import org.lamsfoundation.lams.learningdesign.service.ILearningDesignService;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author jliew
 */
@Controller
@RequestMapping("/toolcontentlist")
public class ToolContentListController {

    @Autowired
    private ILearningDesignService learningDesignService;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    @Qualifier("adminMessageService")
    private MessageService messageService;

    @RequestMapping(path = "/start")
    public String execute(HttpServletRequest request) throws Exception {
	// check permission
	if (!(request.isUserInRole(Role.SYSADMIN))) {
	    request.setAttribute("errorName", "ToolContentListAction");
	    request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
	    return "error";
	}

	// not just display, but enable/disable a learning library
	String param = request.getParameter("action");
	if (StringUtils.equals(param, "enable")) {
	    if (checkPriviledge(request)) {
		enableLibrary(request);
	    } else {
		return "error";
	    }
	} else if (StringUtils.equals(param, "disable")) {
	    if (checkPriviledge(request)) {
		disableLibrary(request);
	    } else {
		return "error";
	    }
	}

	// get learning library dtos and their validity
	List<LearningLibraryDTO> learningLibraryDTOs = learningDesignService.getAllLearningLibraryDetails(false,
		getUserLanguage());
	// this is filled when executing following method, for efficiency purposes
	HashMap<Long, Boolean> learningLibraryValidity = new HashMap<>(learningLibraryDTOs.size());
	ArrayList<LibraryActivityDTO> toolLibrary = filterMultipleToolEntries(learningLibraryDTOs,
		learningLibraryValidity);
	request.setAttribute("toolLibrary", toolLibrary);
	request.setAttribute("learningLibraryValidity", learningLibraryValidity);

	// get tool versions
	HashMap<Long, String> toolVersions = new HashMap<>();
	List<Tool> tools = userManagementService.findAll(Tool.class);
	for (Tool tool : tools) {
	    toolVersions.put(tool.getToolId(), tool.getToolVersion());
	}
	request.setAttribute("toolVersions", toolVersions);

	// get tool database versions
	HashMap<String, Integer> dbVersions = new HashMap<>();
	Connection conn = dataSource.getConnection();
	PreparedStatement query = conn.prepareStatement("select system_name, patch_level from patches");
	ResultSet results = query.executeQuery();
	while (results.next()) {
	    dbVersions.put(results.getString("system_name"), results.getInt("patch_level"));
	}
	request.setAttribute("dbVersions", dbVersions);

	return "toolcontent/toolcontentlist";
    }

    // returns full list of learning libraries, valid or not
    @SuppressWarnings("unchecked")
    private ArrayList<LibraryActivityDTO> filterMultipleToolEntries(List<LearningLibraryDTO> learningLibraryDTOs,
	    HashMap<Long, Boolean> learningLibraryValidity) {
	ArrayList<LibraryActivityDTO> activeTools = new ArrayList<>();
	ArrayList<LibraryActivityDTO> activeCombinedTools = new ArrayList<>();
	for (LearningLibraryDTO learningLibraryDTO : learningLibraryDTOs) {
	    // populate information about learning libary validity
	    learningLibraryValidity.put(learningLibraryDTO.getLearningLibraryID(), learningLibraryDTO.getValidFlag());
	    for (LibraryActivityDTO template : (List<LibraryActivityDTO>) learningLibraryDTO.getTemplateActivities()) {
		// no learning library ID = a part of combined learning library, we already have it in the list
		if (template.getLearningLibraryID() != null) {
		    // combined libraries do not have tool content ID set
		    if (template.getToolContentID() == null) {
			if (!toolExists(template, activeCombinedTools)) {
			    activeCombinedTools.add(template);
			}
		    } else {
			if (!toolExists(template, activeTools)) {
			    activeTools.add(template);
			}
		    }
		}
	    }
	}
	// put combined libraries at the end, purely for easy of use
	activeTools.addAll(activeCombinedTools);
	return activeTools;
    }

    private boolean toolExists(LibraryActivityDTO newItem, ArrayList<LibraryActivityDTO> list) {
	for (LibraryActivityDTO libraryActivityDTO : list) {
	    if (newItem.getLearningLibraryID().equals(libraryActivityDTO.getLearningLibraryID())) {
		return true;
	    }
	}
	return false;
    }

    private String getUserLanguage() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user == null ? "" : user.getLocaleLanguage();
    }

    private boolean checkPriviledge(HttpServletRequest request) {
	if (!userManagementService.isUserSysAdmin()) {
	    request.setAttribute("errorName", "ToolContentListAction");
	    request.setAttribute("errorMessage", messageService.getMessage("error.no.sysadmin.priviledge"));
	    return false;
	}
	return true;
    }

    @RequestMapping(path = "/disable", method = RequestMethod.POST)
    public String disableLibrary(HttpServletRequest request) {
	Long learningLibraryId = WebUtil.readLongParam(request, "libraryID", false);
	ILearningDesignService ldService = learningDesignService;
	ldService.setValid(learningLibraryId, false);
	return "forward:/toolcontentlist/start.do";
    }

    @RequestMapping(path = "/enable", method = RequestMethod.POST)
    public String enableLibrary(HttpServletRequest request) {
	Long learningLibraryId = WebUtil.readLongParam(request, "libraryID", false);
	ILearningDesignService ldService = learningDesignService;
	ldService.setValid(learningLibraryId, true);
	return "forward:/toolcontentlist/start.do";
    }
}