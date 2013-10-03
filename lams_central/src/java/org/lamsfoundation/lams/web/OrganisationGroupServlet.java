/**
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 */
package org.lamsfoundation.lams.web;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.security.AuthenticationException;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.integration.util.LoginRequestDispatcher;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationGroup;
import org.lamsfoundation.lams.usermanagement.OrganisationGrouping;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class OrganisationGroupServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(OrganisationGroupServlet.class);

    private static IntegrationService integrationService;
    private static IUserManagementService userManagementService;

    private static final String METHOD_ADD_GROUPING = "addGrouping";
    private static final String METHOD_REMOVE_GROUPING = "removeGrouping";
    private static final String METHOD_ADD_GROUP = "addGroup";
    private static final String METHOD_REMOVE_GROUP = "removeGroup";
    private static final String METHOD_ADD_LEARNERS = "addLearners";
    private static final String METHOD_REMOVE_LEARNERS = "removeLearners";

    private static final String PARAM_GROUPING_ID = "groupingId";
    private static final String PARAM_GROUP_ID = "groupId";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String serverId = request.getParameter(CentralConstants.PARAM_SERVER_ID);
	String datetime = request.getParameter(CentralConstants.PARAM_DATE_TIME);
	String hashValue = request.getParameter(CentralConstants.PARAM_HASH_VALUE);
	String username = request.getParameter(CentralConstants.PARAM_USERNAME);
	String courseId = request.getParameter(CentralConstants.PARAM_COURSE_ID);

	// first, check if request contains all the details required for authentication
	if ((username == null) || (serverId == null) || (datetime == null) || (hashValue == null) || (courseId == null)) {
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
		    "Course group action failed - authentication parameters missing");
	    return;
	}

	User user = null;
	Organisation organisation = null;
	ExtServerOrgMap serverMap = getService().getExtServerOrgMap(serverId);

	try {
	    // authenticate the request
	    Authenticator.authenticate(serverMap, datetime, username, hashValue);

	    // get local user and organisation
	    ExtUserUseridMap userMap = OrganisationGroupServlet.integrationService.getExtUserUseridMap(serverMap,
		    username);
	    ExtCourseClassMap orgMap = OrganisationGroupServlet.integrationService.getExtCourseClassMap(serverMap,
		    userMap, courseId, null, null, null, LoginRequestDispatcher.METHOD_MONITOR);
	    user = userMap.getUser();
	    organisation = orgMap.getOrganisation();

	    // check if user is allowed to modify course groups for the given organisation
	    boolean isGroupSuperuser = getUserManagementService().isUserInRole(user.getUserId(),
		    organisation.getOrganisationId(), Role.MONITOR)
		    || getUserManagementService().isUserInRole(user.getUserId(), organisation.getOrganisationId(),
			    Role.AUTHOR);
	    if (!isGroupSuperuser) {
		OrganisationGroupServlet.log.error("User " + user.getUserId()
			+ " may not perform group actions for course " + organisation.getOrganisationId());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
			"Course group action failed - user may not perform group actions");
		return;
	    }
	} catch (AuthenticationException e) {
	    OrganisationGroupServlet.log.error(e);
	    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Course group action failed - authentication error");
	    return;
	} catch (UserInfoFetchException e) {
	    OrganisationGroupServlet.log.error(e);
	    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Course group action failed - user does not exist");
	    return;
	}

	try {
	    String method = request.getParameter(CentralConstants.PARAM_METHOD);
	    String responseString = null;
	    if (OrganisationGroupServlet.METHOD_ADD_GROUPING.equals(method)) {
		responseString = addGrouping(request, organisation);
	    } else if (OrganisationGroupServlet.METHOD_REMOVE_GROUPING.equals(method)) {
		responseString = removeGrouping(request, organisation.getOrganisationId());
	    } else if (OrganisationGroupServlet.METHOD_ADD_GROUP.equals(method)) {
		responseString = addGroup(request, organisation.getOrganisationId());
	    } else if (OrganisationGroupServlet.METHOD_REMOVE_GROUP.equals(method)) {
		responseString = removeGroup(request, organisation.getOrganisationId());
	    } else if (OrganisationGroupServlet.METHOD_ADD_LEARNERS.equals(method)) {
		responseString = addLearners(request, serverMap, organisation.getOrganisationId());
	    } else if (OrganisationGroupServlet.METHOD_REMOVE_LEARNERS.equals(method)) {
		responseString = removeLearners(request, serverMap, organisation.getOrganisationId());
	    }

	    if (responseString != null) {
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(responseString);
	    }
	} catch (ServletException e) {
	    OrganisationGroupServlet.log.error(e);
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course group action failed: " + e.getMessage());
	    return;
	} catch (Exception e) {
	    OrganisationGroupServlet.log.error(e);
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
		    "Course group action failed: internal error");
	    return;
	}
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
    }

    private String addGrouping(HttpServletRequest request, Organisation organisation) throws ServletException {
	String groupingName = request.getParameter(CentralConstants.ATTR_NAME);
	if (StringUtils.isBlank(groupingName)) {
	    throw new ServletException("Missing \"" + CentralConstants.ATTR_NAME + "\" parameter");
	}
	OrganisationGrouping grouping = new OrganisationGrouping(organisation.getOrganisationId(), groupingName);
	getUserManagementService().saveOrganisationGrouping(grouping, null);
	Long groupingId = grouping.getGroupingId();
	if (groupingId == null) {
	    throw new ServletException("Grouping could not be created");
	}

	if (OrganisationGroupServlet.log.isDebugEnabled()) {
	    OrganisationGroupServlet.log.debug("Created course grouping \"" + groupingName + "\" with ID " + groupingId
		    + " in organisation with ID " + organisation.getOrganisationId());
	}
	return groupingId.toString();
    }

    private String removeGrouping(HttpServletRequest request, Integer organisationId) throws ServletException {
	String groupingId = request.getParameter(OrganisationGroupServlet.PARAM_GROUPING_ID);
	if (StringUtils.isBlank(groupingId)) {
	    throw new ServletException("Missing \"" + OrganisationGroupServlet.PARAM_GROUPING_ID + "\" parameter");
	}
	OrganisationGrouping grouping = (OrganisationGrouping) getUserManagementService().findById(
		OrganisationGrouping.class, Long.valueOf(groupingId));
	if (grouping == null) {
	    return null;
	}
	if (!grouping.getOrganisationId().equals(organisationId)) {
	    throw new ServletException("Grouping with ID " + groupingId + " is not a part of the given course");
	}
	getUserManagementService().delete(grouping);

	if (OrganisationGroupServlet.log.isDebugEnabled()) {
	    OrganisationGroupServlet.log.debug("Deleted course grouping with ID " + groupingId
		    + " from organisation with ID " + organisationId);
	}
	return null;
    }

    private String addGroup(HttpServletRequest request, Integer organisationId) throws ServletException {
	String groupingId = request.getParameter(OrganisationGroupServlet.PARAM_GROUPING_ID);
	if (StringUtils.isBlank(groupingId)) {
	    throw new ServletException("Missing \"" + OrganisationGroupServlet.PARAM_GROUPING_ID + "\" parameter");
	}
	String groupName = request.getParameter(CentralConstants.ATTR_NAME);
	if (StringUtils.isBlank(groupName)) {
	    throw new ServletException("Missing \"" + CentralConstants.ATTR_NAME + "\" parameter");
	}
	OrganisationGrouping grouping = (OrganisationGrouping) getUserManagementService().findById(
		OrganisationGrouping.class, Long.valueOf(groupingId));
	if (grouping == null) {
	    throw new ServletException("Grouping with ID " + groupingId + " does not exist");
	}
	if (!grouping.getOrganisationId().equals(organisationId)) {
	    throw new ServletException("Grouping with ID " + groupingId + " is not a part of the given course");
	}

	OrganisationGroup newGroup = new OrganisationGroup();
	newGroup.setName(groupName);
	Set<OrganisationGroup> groups = new HashSet<OrganisationGroup>(grouping.getGroups());
	groups.add(newGroup);

	getUserManagementService().saveOrganisationGrouping(grouping, groups);
	Long groupId = newGroup.getGroupId();
	if (groupId == null) {
	    throw new ServletException("Group could not be created");
	}

	if (OrganisationGroupServlet.log.isDebugEnabled()) {
	    OrganisationGroupServlet.log.debug("Created course group \"" + groupName + "\" with ID " + groupId
		    + " in organisation with ID " + organisationId);
	}
	return groupId.toString();
    }

    private String removeGroup(HttpServletRequest request, Integer organisationId) throws ServletException {
	String groupId = request.getParameter(OrganisationGroupServlet.PARAM_GROUP_ID);
	if (StringUtils.isBlank(groupId)) {
	    throw new ServletException("Missing \"" + OrganisationGroupServlet.PARAM_GROUP_ID + "\" parameter");
	}
	OrganisationGroup group = (OrganisationGroup) getUserManagementService().findById(OrganisationGroup.class,
		Long.valueOf(groupId));
	if (group == null) {
	    return null;
	}

	OrganisationGrouping grouping = (OrganisationGrouping) getUserManagementService().findById(
		OrganisationGrouping.class, group.getGroupingId());
	if (!grouping.getOrganisationId().equals(organisationId)) {
	    throw new ServletException("Group with ID " + groupId + " is not a part of the given course");
	}

	Set<OrganisationGroup> groups = new HashSet<OrganisationGroup>(grouping.getGroups());
	groups.remove(group);
	getUserManagementService().saveOrganisationGrouping(grouping, groups);

	if (OrganisationGroupServlet.log.isDebugEnabled()) {
	    OrganisationGroupServlet.log.debug("Deleted course group with ID " + groupId
		    + " from organisation with ID " + organisationId);
	}
	return null;
    }

    private String addLearners(HttpServletRequest request, ExtServerOrgMap serverMap, Integer organisationId)
	    throws ServletException {
	String groupId = request.getParameter(OrganisationGroupServlet.PARAM_GROUP_ID);
	if (StringUtils.isBlank(groupId)) {
	    throw new ServletException("Missing \"" + OrganisationGroupServlet.PARAM_GROUP_ID + "\" parameter");
	}
	String learnerIds = request.getParameter(CentralConstants.PARAM_LEARNER_IDS);
	if (StringUtils.isBlank(learnerIds)) {
	    throw new ServletException("Missing \"" + CentralConstants.PARAM_LEARNER_IDS + "\" parameter");
	}
	OrganisationGroup group = (OrganisationGroup) getUserManagementService().findById(OrganisationGroup.class,
		Long.valueOf(groupId));
	if (group == null) {
	    throw new ServletException("Group with ID " + groupId + " does not exist");
	}
	OrganisationGrouping grouping = (OrganisationGrouping) getUserManagementService().findById(
		OrganisationGrouping.class, group.getGroupingId());
	if (!grouping.getOrganisationId().equals(organisationId)) {
	    throw new ServletException("Group with ID " + groupId + " is not a part of the given course");
	}

	String[] learnerLoginArray = learnerIds.split(",");

	boolean learnersAdded = false;
	StringBuilder addedLearnerLogins = new StringBuilder();
	for (String learnerLogin : learnerLoginArray) {
	    User learner = null;
	    try {
		ExtUserUseridMap userMap = OrganisationGroupServlet.integrationService.getExtUserUseridMap(serverMap,
			learnerLogin);
		learner = userMap.getUser();
	    } catch (UserInfoFetchException e) {
		throw new ServletException("Learner with ID \"" + learnerLogin + "\" does not exist");
	    }

	    boolean learnerAdded = group.getUsers().add(learner);
	    if (learnerAdded) {
		learnersAdded = true;
		addedLearnerLogins.append(learnerLogin).append(',');
	    }
	}

	if (learnersAdded) {
	    getUserManagementService().saveOrganisationGrouping(grouping, grouping.getGroups());

	    if (OrganisationGroupServlet.log.isDebugEnabled()) {
		OrganisationGroupServlet.log.debug("Added learners " + addedLearnerLogins + " to course group with ID "
			+ groupId + " in organisation with ID " + organisationId);
	    }
	}
	return null;
    }

    private String removeLearners(HttpServletRequest request, ExtServerOrgMap serverMap, Integer organisationId)
	    throws ServletException {
	String groupId = request.getParameter(OrganisationGroupServlet.PARAM_GROUP_ID);
	if (StringUtils.isBlank(groupId)) {
	    throw new ServletException("Missing \"" + OrganisationGroupServlet.PARAM_GROUP_ID + "\" parameter");
	}
	String learnerIds = request.getParameter(CentralConstants.PARAM_LEARNER_IDS);
	if (StringUtils.isBlank(learnerIds)) {
	    throw new ServletException("Missing \"" + CentralConstants.PARAM_LEARNER_IDS + "\" parameter");
	}
	OrganisationGroup group = (OrganisationGroup) getUserManagementService().findById(OrganisationGroup.class,
		Long.valueOf(groupId));
	if (group == null) {
	    throw new ServletException("Group with ID " + groupId + " does not exist");
	}
	OrganisationGrouping grouping = (OrganisationGrouping) getUserManagementService().findById(
		OrganisationGrouping.class, group.getGroupingId());
	if (!grouping.getOrganisationId().equals(organisationId)) {
	    throw new ServletException("Group with ID " + groupId + " is not a part of the given course");
	}

	String[] learnerLoginArray = learnerIds.split(",");

	boolean learnersRemoved = false;
	StringBuilder removedLearnerLogins = new StringBuilder();
	for (String learnerLogin : learnerLoginArray) {
	    User learner = null;
	    try {
		ExtUserUseridMap userMap = OrganisationGroupServlet.integrationService.getExtUserUseridMap(serverMap,
			learnerLogin);
		learner = userMap.getUser();
	    } catch (UserInfoFetchException e) {
		// if user does not exist, ignore
		continue;
	    }

	    boolean learnerRemoved = group.getUsers().remove(learner);
	    if (learnerRemoved) {
		learnersRemoved = true;
		removedLearnerLogins.append(learnerLogin).append(',');
	    }

	}

	if (learnersRemoved) {
	    getUserManagementService().saveOrganisationGrouping(grouping, grouping.getGroups());
	    if (OrganisationGroupServlet.log.isDebugEnabled()) {
		OrganisationGroupServlet.log.debug("Removed learners " + removedLearnerLogins + " from group with ID "
			+ groupId + " in organisation with ID " + organisationId);
	    }
	}
	return null;
    }

    private IntegrationService getService() {
	if (OrganisationGroupServlet.integrationService == null) {
	    OrganisationGroupServlet.integrationService = (IntegrationService) WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServletContext()).getBean("integrationService");
	}
	return OrganisationGroupServlet.integrationService;
    }

    private IUserManagementService getUserManagementService() {
	if (OrganisationGroupServlet.userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServletContext());
	    OrganisationGroupServlet.userManagementService = (IUserManagementService) ctx
		    .getBean("userManagementService");
	}
	return OrganisationGroupServlet.userManagementService;
    }
}