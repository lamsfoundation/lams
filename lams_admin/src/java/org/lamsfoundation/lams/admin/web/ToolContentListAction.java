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

package org.lamsfoundation.lams.admin.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author jliew
 */
public class ToolContentListAction extends Action {

    private static final String PARAM_ACTION = "action";
    private static final String PARAM_LIBRARY_ID = "libraryID";

    private static final String ATTRIBUTE_ERROR_NAME = "errorName";
    private static final String ATTRIBUTE_ERROR_MESSAGE = "errorMessage";
    private static final String ATTRIBUTE_LIBRARY = "toolLibrary";
    private static final String ATTRIBUTE_VALIDITY = "learningLibraryValidity";
    private static final String ATTRIBUTE_TOOL_VERSIONS = "toolVersions";
    private static final String ATTRIBUTE_DATABASE_VERSIONS = "dbVersions";

    private static final String FORWARD_SUCCESS = "toolcontentlist";
    private static final String FORWARD_GROUPS = "groups";
    private static final String FORWARD_ERROR = "error";

    private static final String ACTION_ENABLE = "enable";
    private static final String ACTION_DISABLE = "disable";
    private static final String ACTION_OPEN_GROUPS = "openLearningLibraryGroups";
    private static final String ACTION_SAVE_GROUPS = "saveLearningLibraryGroups";

    private static final String QUERY_DATABASE_VERSIONS = "select system_name, patch_level from patches";

    private static ILearningDesignService learningDesignService;
    private static IUserManagementService userManagementService;
    private static DataSource dataSource;

    @SuppressWarnings("unchecked")
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	// check permission
	if (!(request.isUserInRole(Role.SYSADMIN))) {
	    request.setAttribute(ToolContentListAction.ATTRIBUTE_ERROR_NAME, "ToolContentListAction");
	    request.setAttribute(ToolContentListAction.ATTRIBUTE_ERROR_MESSAGE, AdminServiceProxy
		    .getMessageService(getServlet().getServletContext()).getMessage("error.authorisation"));
	    return mapping.findForward(ToolContentListAction.FORWARD_ERROR);
	}

	// not just display, but enable/disable a learning library
	String param = request.getParameter(ToolContentListAction.PARAM_ACTION);
	if (StringUtils.equals(param, ToolContentListAction.ACTION_ENABLE)) {
	    if (checkPriviledge(request)) {
		enableLibrary(mapping, form, request, response);
	    } else {
		return mapping.findForward(ToolContentListAction.FORWARD_ERROR);
	    }
	} else if (StringUtils.equals(param, ToolContentListAction.ACTION_DISABLE)) {
	    if (checkPriviledge(request)) {
		disableLibrary(mapping, form, request, response);
	    } else {
		return mapping.findForward(ToolContentListAction.FORWARD_ERROR);
	    }
	} else if (StringUtils.equals(param, ToolContentListAction.ACTION_OPEN_GROUPS)) {
	    return openLearningLibraryGroups(mapping, form, request, response);
	} else if (StringUtils.equals(param, ToolContentListAction.ACTION_SAVE_GROUPS)) {
	    saveLearningLibraryGroups(mapping, form, request, response);
	    return null;
	}

	// get learning library dtos and their validity
	List<LearningLibraryDTO> learningLibraryDTOs = getLearningDesignService().getAllLearningLibraryDetails(false,
		getUserLanguage());
	// this is filled when executing following method, for efficiency purposes
	HashMap<Long, Boolean> learningLibraryValidity = new HashMap<>(learningLibraryDTOs.size());
	ArrayList<LibraryActivityDTO> toolLibrary = filterMultipleToolEntries(learningLibraryDTOs,
		learningLibraryValidity);
	request.setAttribute(ToolContentListAction.ATTRIBUTE_LIBRARY, toolLibrary);
	request.setAttribute(ToolContentListAction.ATTRIBUTE_VALIDITY, learningLibraryValidity);

	// get tool versions
	HashMap<Long, String> toolVersions = new HashMap<>();
	List<Tool> tools = getUserManagementService().findAll(Tool.class);
	for (Tool tool : tools) {
	    toolVersions.put(tool.getToolId(), tool.getToolVersion());
	}
	request.setAttribute(ToolContentListAction.ATTRIBUTE_TOOL_VERSIONS, toolVersions);

	// get tool database versions
	HashMap<String, Integer> dbVersions = new HashMap<>();
	Connection conn = getDataSource().getConnection();
	PreparedStatement query = conn.prepareStatement(ToolContentListAction.QUERY_DATABASE_VERSIONS);
	ResultSet results = query.executeQuery();
	while (results.next()) {
	    dbVersions.put(results.getString("system_name"), results.getInt("patch_level"));
	}
	request.setAttribute(ToolContentListAction.ATTRIBUTE_DATABASE_VERSIONS, dbVersions);

	return mapping.findForward(ToolContentListAction.FORWARD_SUCCESS);
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
	    request.setAttribute(ToolContentListAction.ATTRIBUTE_ERROR_NAME, "ToolContentListAction");
	    request.setAttribute(ToolContentListAction.ATTRIBUTE_ERROR_MESSAGE, AdminServiceProxy
		    .getMessageService(getServlet().getServletContext()).getMessage("error.no.sysadmin.priviledge"));
	    return false;
	}
	return true;
    }

    private void disableLibrary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Long learningLibraryId = WebUtil.readLongParam(request, ToolContentListAction.PARAM_LIBRARY_ID, false);
	ILearningDesignService ldService = getLearningDesignService();
	ldService.setValid(learningLibraryId, false);
    }

    private void enableLibrary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Long learningLibraryId = WebUtil.readLongParam(request, ToolContentListAction.PARAM_LIBRARY_ID, false);
	ILearningDesignService ldService = getLearningDesignService();
	ldService.setValid(learningLibraryId, true);

    }

    /**
     * Loads groups and libraries and displays the management dialog.
     */
    private ActionForward openLearningLibraryGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
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

	return mapping.findForward(ToolContentListAction.FORWARD_GROUPS);
    }

    private void saveLearningLibraryGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
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
	if (ToolContentListAction.learningDesignService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    ToolContentListAction.learningDesignService = (ILearningDesignService) ctx.getBean("learningDesignService");
	}
	return ToolContentListAction.learningDesignService;
    }

    private IUserManagementService getUserManagementService() {
	if (ToolContentListAction.userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    ToolContentListAction.userManagementService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return ToolContentListAction.userManagementService;
    }

    private DataSource getDataSource() {
	if (ToolContentListAction.dataSource == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    ToolContentListAction.dataSource = (DataSource) ctx.getBean("dataSource");
	}
	return ToolContentListAction.dataSource;
    }
}