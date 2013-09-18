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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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
import org.lamsfoundation.lams.usermanagement.OrganisationGrouping;
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

/**
 * @struts.action path = "/OrganisationGroup" parameter = "method" validate = "false"
 * @struts.action-forward name = "viewGroupings" path = "/orgGrouping.jsp"
 * @struts.action-forward name = "viewGroups" path = "/orgGroup.jsp"
 */
public class OrganisationGroupAction extends DispatchAction {
    /**
     * Class for displaying on groupings page.
     */
    public class OrganisationGroupingDTO implements Comparable<OrganisationGroupingDTO> {
	private Long groupingId;
	private String name;
	private Integer groupCount;

	public OrganisationGroupingDTO(OrganisationGrouping grouping) {
	    this.groupingId = grouping.getGroupingId();
	    this.name = grouping.getName();
	    this.groupCount = grouping.getGroups().size();
	}

	public Long getGroupingId() {
	    return groupingId;
	}

	public void setGroupingId(Long groupingId) {
	    this.groupingId = groupingId;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public Integer getGroupCount() {
	    return groupCount;
	}

	public void setGroupCount(Integer groupCount) {
	    this.groupCount = groupCount;
	}

	@Override
	public int compareTo(OrganisationGroupingDTO o) {
	    if (o == null) {
		return 1;
	    }
	    if (this.name == null) {
		return o.name == null ? 0 : 1;
	    }
	    return this.name.compareTo(o.name);
	}
    }

    private static final Comparator<OrganisationGroup> GROUP_COMPARATOR = new Comparator<OrganisationGroup>() {
	public int compare(OrganisationGroup o1, OrganisationGroup o2) {
	    if (o1 == null && o2 == null) {
		return 0;
	    }
	    if (o1 == null) {
		return -1;
	    }
	    if (o2 == null) {
		return 1;
	    }
	    if (o1.getName() == null) {
		return o2.getName() == null ? 0 : -1;
	    }
	    return o1.getName().compareTo(o2.getName());
	}
    };

    private static Logger log = Logger.getLogger(OrganisationGroupAction.class);

    private static IUserManagementService userManagementService;

    private static final String MAPPING_VIEW_GROUPINGS = "viewGroupings";
    private static final String MAPPING_VIEW_GROUPS = "viewGroups";

    @SuppressWarnings("unchecked")
    public ActionForward viewGroupings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException {
	// check if user is allowed to view and edit groups
	Integer userId = getUserDTO().getUserID();
	int organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	boolean isGroupSuperuser = getUserManagementService().isUserInRole(userId, organisationId, Role.GROUP_ADMIN)
		|| getUserManagementService().isUserInRole(userId, organisationId, Role.GROUP_MANAGER);
	if (!isGroupSuperuser && !getUserManagementService().isUserInRole(userId, organisationId, Role.AUTHOR)
		&& !getUserManagementService().isUserInRole(userId, organisationId, Role.MONITOR)) {
	    throw new UserAccessDeniedException("User " + userId + " may not view groupings for course "
		    + organisationId);
	}

	if (OrganisationGroupAction.log.isDebugEnabled()) {
	    OrganisationGroupAction.log.debug("Displaying course groupings for user " + userId + " and organisation "
		    + organisationId);
	}
	request.setAttribute(AttributeNames.PARAM_ORGANISATION_ID, organisationId);
	request.setAttribute("canEdit", isGroupSuperuser);

	Set<OrganisationGroupingDTO> groupingDTOs = new TreeSet<OrganisationGroupingDTO>();
	List<OrganisationGrouping> groupings = getUserManagementService().findByProperty(OrganisationGrouping.class,
		"organisationId", organisationId);
	for (OrganisationGrouping grouping : groupings) {
	    groupingDTOs.add(new OrganisationGroupingDTO(grouping));
	}
	request.setAttribute("groupings", groupingDTOs);

	return mapping.findForward(OrganisationGroupAction.MAPPING_VIEW_GROUPINGS);
    }

    @SuppressWarnings("unchecked")
    public ActionForward viewGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException {
	// check if user is allowed to view and edit groups
	Integer userId = getUserDTO().getUserID();
	int organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	boolean isGroupSuperuser = getUserManagementService().isUserInRole(userId, organisationId, Role.GROUP_ADMIN)
		|| getUserManagementService().isUserInRole(userId, organisationId, Role.GROUP_MANAGER);
	if (!isGroupSuperuser && !getUserManagementService().isUserInRole(userId, organisationId, Role.AUTHOR)
		&& !getUserManagementService().isUserInRole(userId, organisationId, Role.MONITOR)) {
	    throw new UserAccessDeniedException("User " + userId + " may not view groups for course " + organisationId);
	}

	if (OrganisationGroupAction.log.isDebugEnabled()) {
	    OrganisationGroupAction.log.debug("Displaying course groups for user " + userId + " and organisation "
		    + organisationId);
	}
	request.setAttribute("canEdit", isGroupSuperuser);

	JSONObject groupingJSON = new JSONObject();
	groupingJSON.put("organisationId", organisationId);

	Long groupingId = WebUtil.readLongParam(request, "groupingId", true);
	OrganisationGrouping grouping = null;
	// check if grouping already exists
	if (groupingId != null) {
	    grouping = (OrganisationGrouping) getUserManagementService().findById(OrganisationGrouping.class,
		    groupingId);
	    if (grouping != null) {
		groupingJSON.put("groupingId", groupingId);
		groupingJSON.put("name", grouping.getName());
	    }
	}

	// serialize database group objects into JSON
	Vector<User> learners = getUserManagementService().getUsersFromOrganisationByRole(organisationId, Role.LEARNER,
		false, true);
	Set<OrganisationGroup> groups = grouping == null ? null : grouping.getGroups();
	JSONArray groupsJSON = new JSONArray();
	if (groups != null) {
	    // sort groups by their name
	    List<OrganisationGroup> groupList = new LinkedList<OrganisationGroup>(groups);
	    Collections.sort(groupList, GROUP_COMPARATOR);
	    
	    for (OrganisationGroup group : groupList) {
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
	}
	groupingJSON.put("groups", groupsJSON);
	request.setAttribute("grouping", groupingJSON);

	// all the remaining users are unassigned to any group
	JSONArray unassignedUsersJSON = new JSONArray();
	for (User unassignedUser : learners) {
	    JSONObject unassignedUserJSON = WebUtil.userToJSON(unassignedUser);
	    unassignedUsersJSON.put(unassignedUserJSON);
	}
	request.setAttribute("unassignedUsers", unassignedUsersJSON);

	return mapping.findForward(OrganisationGroupAction.MAPPING_VIEW_GROUPS);
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException {
	// check if user is allowed to edit groups
	Integer userId = getUserDTO().getUserID();
	int organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	boolean isGroupSuperuser = getUserManagementService().isUserInRole(userId, organisationId, Role.GROUP_ADMIN)
		|| getUserManagementService().isUserInRole(userId, organisationId, Role.GROUP_MANAGER);
	if (!isGroupSuperuser) {
	    throw new UserAccessDeniedException("User " + userId + " may not edit groups for course " + organisationId);
	}

	if (OrganisationGroupAction.log.isDebugEnabled()) {
	    OrganisationGroupAction.log.debug("Saving course groups for user " + userId + " and organisation "
		    + organisationId);
	}

	// deserialize grouping
	JSONObject groupingJSON = new JSONObject(request.getParameter("grouping"));
	// check if already exists
	Long groupingId = groupingJSON.optLong("groupingId");
	if (groupingId == 0L) {
	    groupingId = null;
	}

	// iterate over groups
	List<OrganisationGroup> groups = new LinkedList<OrganisationGroup>();
	JSONArray groupsJSON = groupingJSON.optJSONArray("groups");
	if (groups != null) {
	    for (int i = 0; i < groupsJSON.length(); i++) {
		// just overwrite existing groups; they will be updated if already exist
		Set<User> users = new HashSet<User>();
		JSONObject groupJSON = groupsJSON.getJSONObject(i);
		JSONArray newGroupUsers = groupJSON.optJSONArray("users");
		if (newGroupUsers != null) {
		    // find user objects based on delivered IDs
		    for (int j = 0; j < newGroupUsers.length(); j++) {
			Integer learnerId = newGroupUsers.getInt(j);
			User user = (User) getUserManagementService().findById(User.class, learnerId);
			users.add(user);
		    }
		}

		OrganisationGroup group = new OrganisationGroup();
		Long groupId = groupJSON.optLong("groupId");
		if (groupId > 0) {
		    group.setGroupId(groupId);
		    group.setGroupingId(groupingId);
		}
		group.setName(groupJSON.optString("name", null));
		group.setUsers(users);

		groups.add(group);
	    }
	}

	OrganisationGrouping grouping = null;
	if (groupingId != null) {
	    grouping = (OrganisationGrouping) getUserManagementService().findById(OrganisationGrouping.class,
		    groupingId);
	}
	if (grouping == null) {
	    grouping = new OrganisationGrouping();
	    grouping.setOrganisationId(organisationId);
	}
	grouping.setName(groupingJSON.getString("name"));

	getUserManagementService().saveOrganisationGrouping(grouping, groups);
	return null;
    }

    /**
     * Deletes Organisation Grouping with the given ID.
     */
    public ActionForward removeGrouping(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException {
	// check if user is allowed to edit groups
	Integer userId = getUserDTO().getUserID();
	int organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	boolean isGroupSuperuser = getUserManagementService().isUserInRole(userId, organisationId, Role.GROUP_ADMIN)
		|| getUserManagementService().isUserInRole(userId, organisationId, Role.GROUP_MANAGER);
	if (!isGroupSuperuser) {
	    throw new UserAccessDeniedException("User " + userId + " may not remove groupings for course "
		    + organisationId);
	}

	Long groupingId = WebUtil.readLongParam(request, "groupingId");
	if (OrganisationGroupAction.log.isDebugEnabled()) {
	    OrganisationGroupAction.log.debug("Removing grouping " + groupingId + " for user " + userId
		    + " and organisation " + organisationId);
	}

	getUserManagementService().deleteById(OrganisationGrouping.class, groupingId);

	return viewGroupings(mapping, form, request, response);
    }

    private IUserManagementService getUserManagementService() {
	if (OrganisationGroupAction.userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    OrganisationGroupAction.userManagementService = (IUserManagementService) ctx
		    .getBean("userManagementService");
	}
	return OrganisationGroupAction.userManagementService;
    }

    private UserDTO getUserDTO() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }
}