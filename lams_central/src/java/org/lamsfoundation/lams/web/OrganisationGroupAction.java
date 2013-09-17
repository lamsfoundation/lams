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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.usermanagement.OrganisationGroup;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.WebUtils;

/**
 * @struts.action path = "/OrganisationGroup" parameter = "method" validate = "false"
 * @struts.action-forward name = "view" path = "/orgGroup.jsp"
 */
public class OrganisationGroupAction extends DispatchAction {
    private static Logger log = Logger.getLogger(OrganisationGroupAction.class);
    
    private static IUserManagementService userManagementService;
    
    private static final String MAPPING_VIEW = "view";

    @SuppressWarnings("unchecked")
    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException {
	// check if user is allowed to view and edit groups
	Integer userId = getUserDTO().getUserID();
	int organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	boolean isGroupSuperuser = getUserManagementService().isUserInRole(userId, organisationId, Role.GROUP_ADMIN) ||getUserManagementService().isUserInRole(userId, organisationId, Role.GROUP_MANAGER);
	if (!isGroupSuperuser && !getUserManagementService().isUserInRole(userId, organisationId, Role.AUTHOR) && !getUserManagementService().isUserInRole(userId, organisationId, Role.MONITOR)){
	    throw new UserAccessDeniedException("User " + userId + " may not view groups for course " + organisationId);
	}
	
	if(log.isDebugEnabled()) {
	   log.debug("Displaying course groups for user " + userId + " and organisation " + organisationId);
	}
	request.setAttribute(AttributeNames.PARAM_ORGANISATION_ID, organisationId);
	request.setAttribute("canEdit", isGroupSuperuser);
	
	// serialize database group objects into JSON
	Vector<User> learners = getUserManagementService().getUsersFromOrganisationByRole(organisationId, Role.LEARNER,
		false, true);
	List<OrganisationGroup> groups = getUserManagementService().findByProperty(OrganisationGroup.class,
		"organisationId", organisationId);
	JSONArray groupsJSON = new JSONArray();
	for (OrganisationGroup group : groups) {
	    JSONObject groupJSON = new JSONObject();
	    groupJSON.put("name", group.getName());
	    groupJSON.put("groupId", group.getGroupId());
	    for (User groupUser : group.getUsers()) {
		JSONObject groupUserJSON = WebUtil.userToJSON(groupUser);
		groupJSON.append("users", groupUserJSON);
		
		// remove the user who is already assigned to a group
		learners.remove(groupUser);
	    }
	    
	    groupsJSON.put(groupJSON);
	}
	request.setAttribute("groups", groupsJSON);
	
	// all the remaining users are unassigned to any group
	JSONArray unassignedUsersJSON  = new JSONArray();
	for (User unassignedUser : learners) {
	    JSONObject unassignedUserJSON = WebUtil.userToJSON(unassignedUser);
	    unassignedUsersJSON.put(unassignedUserJSON);
	}
	request.setAttribute("unassignedUsers", unassignedUsersJSON);
	
	response.setContentType("application/json;charset=utf-8");
	return mapping.findForward(MAPPING_VIEW);
    }
    
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException {
	// check if user is allowed to edit groups
	Integer userId = getUserDTO().getUserID();
	int organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	boolean isGroupSuperuser = getUserManagementService().isUserInRole(userId, organisationId, Role.GROUP_ADMIN) ||getUserManagementService().isUserInRole(userId, organisationId, Role.GROUP_MANAGER);
	if (!isGroupSuperuser){
	    throw new UserAccessDeniedException("User " + userId + " may not edit groups for course " + organisationId);
	}
	
	if(log.isDebugEnabled()) {
	   log.debug("Saving course groups for user " + userId + " and organisation " + organisationId);
	}
	
	JSONArray newGroups = new JSONArray(request.getParameter("groups"));
	List<OrganisationGroup> newGroupObjects = new LinkedList<OrganisationGroup>();
	
	// iterate over groups
	for (int i = 0; i <  newGroups.length(); i++) {
	    JSONObject newGroup = newGroups.getJSONObject(i);
	    OrganisationGroup newGroupObject = new OrganisationGroup();
	    
	    Long groupId = newGroup.optLong("groupId");
	    if (groupId == 0L) {
		groupId = null;
	    }
	    newGroupObject.setGroupId(groupId);
	    newGroupObject.setOrganisationId(organisationId);
	    newGroupObject.setName(newGroup.optString("name", null));
	    newGroupObject.setUsers(new HashSet<User>());
	    JSONArray newGroupUsers = newGroup.optJSONArray("users");
	    if (newGroupUsers != null) {
		// find user objects based on delivered IDs
		for (int j = 0; j< newGroupUsers.length(); j++) {
		    Integer learnerId = newGroupUsers.getInt(j);
		    User user = (User)getUserManagementService().findById(User.class, learnerId);
		    newGroupObject.getUsers().add(user);
		}
	    }
	    newGroupObjects.add(newGroupObject);
	}
	
	getUserManagementService().saveOrganisationGroups(organisationId, newGroupObjects);
	
	return null;
    }

    private IUserManagementService getUserManagementService() {
	if (userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    userManagementService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return userManagementService;
    }
    

    private UserDTO getUserDTO() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }
}