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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.LearningLibraryGroup;
import org.lamsfoundation.lams.learningdesign.dto.LearningLibraryDTO;
import org.lamsfoundation.lams.learningdesign.dto.LibraryActivityDTO;
import org.lamsfoundation.lams.learningdesign.service.ILearningDesignService;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author jliew
 *
 *
 *
 *
 *
 *
 */
@Controller
@RequestMapping("/toolcontentlist")
public class ToolContentListController {

    private static ILearningDesignService learningDesignService;
    private static IUserManagementService userManagementService;
    private static DataSource dataSource;

    @Autowired
    private WebApplicationContext applicationContext;

    @RequestMapping(path = "/start")
    public String execute(HttpServletRequest request) throws Exception {
	// check permission
	if (!(request.isUserInRole(Role.SYSADMIN))) {
	    request.setAttribute("errorName", "ToolContentListAction");
	    request.setAttribute("errorMessage", AdminServiceProxy
		    .getMessageService(applicationContext.getServletContext()).getMessage("error.authorisation"));
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
	} else if (StringUtils.equals(param, "openLearningLibraryGroups")) {
	    return openLearningLibraryGroups(request);
	} else if (StringUtils.equals(param, "saveLearningLibraryGroups")) {
	    saveLearningLibraryGroups(request);
	    return null;
	}

	// get learning library dtos and their validity
	List<LearningLibraryDTO> learningLibraryDTOs = getLearningDesignService().getAllLearningLibraryDetails(false,
		getUserLanguage());
	// this is filled when executing following method, for efficiency purposes
	HashMap<Long, Boolean> learningLibraryValidity = new HashMap<>(learningLibraryDTOs.size());
	ArrayList<LibraryActivityDTO> toolLibrary = filterMultipleToolEntries(learningLibraryDTOs,
		learningLibraryValidity);
	request.setAttribute("toolLibrary", toolLibrary);
	request.setAttribute("learningLibraryValidity", learningLibraryValidity);

	// get tool versions
	HashMap<Long, String> toolVersions = new HashMap<>();
	List<Tool> tools = getUserManagementService().findAll(Tool.class);
	for (Tool tool : tools) {
	    toolVersions.put(tool.getToolId(), tool.getToolVersion());
	}
	request.setAttribute("toolVersions", toolVersions);

	// get tool database versions
	HashMap<String, Integer> dbVersions = new HashMap<>();
	Connection conn = getDataSource().getConnection();
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
	if (!getUserManagementService().isUserSysAdmin()) {
	    request.setAttribute("errorName", "ToolContentListAction");
	    request.setAttribute("errorMessage",
		    AdminServiceProxy.getMessageService(applicationContext.getServletContext())
			    .getMessage("error.no.sysadmin.priviledge"));
	    return false;
	}
	return true;
    }

    @RequestMapping("/disableLibrary")
    public void disableLibrary(HttpServletRequest request) {
	Long learningLibraryId = WebUtil.readLongParam(request, "libraryID", false);
	ILearningDesignService ldService = getLearningDesignService();
	ldService.setValid(learningLibraryId, false);
    }

    @RequestMapping("/enableLibrary")
    public void enableLibrary(HttpServletRequest request) {
	Long learningLibraryId = WebUtil.readLongParam(request, "libraryID", false);
	ILearningDesignService ldService = getLearningDesignService();
	ldService.setValid(learningLibraryId, true);
    }

    /**
     * Loads groups and libraries and displays the management dialog.
     */
    @RequestMapping(path = "/openLearningLibraryGroups")
    public String openLearningLibraryGroups(HttpServletRequest request) throws IOException {
	// build full list of available learning libraries
	List<LearningLibraryDTO> learningLibraries = getLearningDesignService()
		.getAllLearningLibraryDetails(getUserLanguage());
	ArrayNode learningLibrariesJSON = JsonNodeFactory.instance.arrayNode();
	for (LearningLibraryDTO learningLibrary : learningLibraries) {
	    ObjectNode learningLibraryJSON = JsonNodeFactory.instance.objectNode();
	    learningLibraryJSON.put("learningLibraryId", learningLibrary.getLearningLibraryID());
	    learningLibraryJSON.put("title", learningLibrary.getTitle());
	    learningLibrariesJSON.add(learningLibraryJSON);
	}
	request.setAttribute("learningLibraries", learningLibrariesJSON.toString());

	// build list of existing groups
	List<LearningLibraryGroup> groups = getLearningDesignService().getLearningLibraryGroups();
	ArrayNode groupsJSON = JsonNodeFactory.instance.arrayNode();
	for (LearningLibraryGroup group : groups) {
	    ObjectNode groupJSON = JsonNodeFactory.instance.objectNode();
	    groupJSON.put("groupId", group.getGroupId());
	    groupJSON.put("name", group.getName());
	    for (LearningLibrary learningLibrary : group.getLearningLibraries()) {
		ObjectNode learningLibraryJSON = JsonNodeFactory.instance.objectNode();
		learningLibraryJSON.put("learningLibraryId", learningLibrary.getLearningLibraryId());
		learningLibraryJSON.put("title", learningLibrary.getTitle());
		groupJSON.withArray("learningLibraries").add(learningLibraryJSON);
	    }
	    groupsJSON.add(groupJSON);
	}
	request.setAttribute("groups", groupsJSON.toString());

	return "forward:/toolcontent/learningLibraryGroup";
    }

    @RequestMapping("/saveLearningLibraryGroups")
    private void saveLearningLibraryGroups(HttpServletRequest request) throws IOException {
	// extract groups from JSON and persist them

	ArrayNode groupsJSON = JsonUtil.readArray(request.getParameter("groups"));
	List<LearningLibraryGroup> groups = new ArrayList<>(groupsJSON.size());
	for (JsonNode groupJSON : groupsJSON) {
	    LearningLibraryGroup group = new LearningLibraryGroup();
	    groups.add(group);

	    long groupId = groupJSON.get("groupId").asLong();
	    if (groupId > 0) {
		group.setGroupId(groupId);
	    }
	    group.setName(groupJSON.get("name").asText(null));

	    group.setLearningLibraries(new HashSet<LearningLibrary>());
	    ArrayNode learningLibrariesJSON = (ArrayNode) groupJSON.get("learningLibraries");
	    for (JsonNode learningLibraryJSON : learningLibrariesJSON) {
		long learningLibraryId = learningLibraryJSON.asLong();
		LearningLibrary learningLibrary = getLearningDesignService().getLearningLibrary(learningLibraryId);
		group.getLearningLibraries().add(learningLibrary);
	    }
	}

	getLearningDesignService().saveLearningLibraryGroups(groups);
    }

    private ILearningDesignService getLearningDesignService() {
	if (ToolContentListController.learningDesignService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(applicationContext.getServletContext());
	    ToolContentListController.learningDesignService = (ILearningDesignService) ctx
		    .getBean("learningDesignService");
	}
	return ToolContentListController.learningDesignService;
    }

    private IUserManagementService getUserManagementService() {
	if (ToolContentListController.userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(applicationContext.getServletContext());
	    ToolContentListController.userManagementService = (IUserManagementService) ctx
		    .getBean("userManagementService");
	}
	return ToolContentListController.userManagementService;
    }

    private DataSource getDataSource() {
	if (ToolContentListController.dataSource == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(applicationContext.getServletContext());
	    ToolContentListController.dataSource = (DataSource) ctx.getBean("dataSource");
	}
	return ToolContentListController.dataSource;
    }
}