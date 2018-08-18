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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.UserInfoValidationException;
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

    private static final String PARAM_GROUPING_NAME = "groupingName";
    private static final String PARAM_GROUP_NAME = "groupName";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String serverId = request.getParameter(CentralConstants.PARAM_SERVER_ID);
	String datetime = request.getParameter(CentralConstants.PARAM_DATE_TIME);
	String hashValue = request.getParameter(CentralConstants.PARAM_HASH_VALUE);
	String username = request.getParameter(CentralConstants.PARAM_USERNAME);
	String courseId = request.getParameter(CentralConstants.PARAM_COURSE_ID);

	// first, check if request contains all the details required for authentication
	if ((username == null) || (serverId == null) || (datetime == null) || (hashValue == null)
		|| (courseId == null)) {
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
		    "Course group action failed - authentication parameters missing");
	    return;
	}

	User user = null;
	Organisation organisation = null;
	ExtServer extServer = getService().getExtServer(serverId);

	try {
	    // authenticate the request
	    Authenticator.authenticate(extServer, datetime, username, hashValue);

	    // get local user and organisation
	    ExtUserUseridMap userMap = OrganisationGroupServlet.integrationService.getExtUserUseridMap(extServer,
		    username);
	    ExtCourseClassMap orgMap = OrganisationGroupServlet.integrationService.getExtCourseClassMap(extServer,
		    userMap, courseId, null, LoginRequestDispatcher.METHOD_MONITOR);
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
	    response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
		    "Course group action failed - authentication error");
	    return;
	} catch (UserInfoFetchException e) {
	    OrganisationGroupServlet.log.error(e);
	    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Course group action failed - user does not exist");
	    return;
	} catch (UserInfoValidationException e) {
	    OrganisationGroupServlet.log.error(e);
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course group action failed." + e.getMessage());
	    return;
	}

	try {
	    String method = request.getParameter(CentralConstants.PARAM_METHOD);
	    if (OrganisationGroupServlet.METHOD_ADD_GROUPING.equals(method)) {
		addGrouping(request, organisation);
	    } else if (OrganisationGroupServlet.METHOD_REMOVE_GROUPING.equals(method)) {
		removeGrouping(request, organisation.getOrganisationId());
	    } else if (OrganisationGroupServlet.METHOD_ADD_GROUP.equals(method)) {
		addGroup(request, organisation.getOrganisationId());
	    } else if (OrganisationGroupServlet.METHOD_REMOVE_GROUP.equals(method)) {
		removeGroup(request, organisation.getOrganisationId());
	    } else if (OrganisationGroupServlet.METHOD_ADD_LEARNERS.equals(method)) {
		addLearners(request, extServer, organisation.getOrganisationId());
	    } else if (OrganisationGroupServlet.METHOD_REMOVE_LEARNERS.equals(method)) {
		removeLearners(request, extServer, organisation.getOrganisationId());
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

    private void addGrouping(HttpServletRequest request, Organisation organisation) throws ServletException {
	String groupingName = request.getParameter(OrganisationGroupServlet.PARAM_GROUPING_NAME);
	if (StringUtils.isBlank(groupingName)) {
	    throw new ServletException("Missing \"" + CentralConstants.ATTR_NAME + "\" parameter");
	}

	// check if grouping with such name does not exist already
	OrganisationGrouping grouping = findGrouping(organisation.getOrganisationId(), groupingName);
	if (grouping != null) {
	    throw new ServletException("Grouping with name \"" + groupingName + "\" exists in organisation with ID "
		    + organisation.getOrganisationId());
	}

	grouping = new OrganisationGrouping(organisation.getOrganisationId(), groupingName);
	getUserManagementService().saveOrganisationGrouping(grouping, null);

	Long groupingId = grouping.getGroupingId();
	if (groupingId == null) {
	    throw new ServletException("Grouping could not be created");
	}

	if (OrganisationGroupServlet.log.isDebugEnabled()) {
	    OrganisationGroupServlet.log.debug("Created course grouping \"" + groupingName + "\" with ID " + groupingId
		    + " in organisation with ID " + organisation.getOrganisationId());
	}
    }

    private void removeGrouping(HttpServletRequest request, Integer organisationId) throws ServletException {
	String groupingName = request.getParameter(OrganisationGroupServlet.PARAM_GROUPING_NAME);
	if (StringUtils.isBlank(groupingName)) {
	    throw new ServletException("Missing \"" + OrganisationGroupServlet.PARAM_GROUPING_NAME + "\" parameter");
	}

	OrganisationGrouping grouping = findGrouping(organisationId, groupingName);
	if (grouping == null) {
	    // if grouping does not exist, just ignore it
	    return;
	}

	getUserManagementService().delete(grouping);

	if (OrganisationGroupServlet.log.isDebugEnabled()) {
	    OrganisationGroupServlet.log.debug(
		    "Deleted course grouping with ID " + groupingName + " from organisation with ID " + organisationId);
	}
    }

    private void addGroup(HttpServletRequest request, Integer organisationId) throws ServletException {
	String groupingName = request.getParameter(OrganisationGroupServlet.PARAM_GROUPING_NAME);
	if (StringUtils.isBlank(groupingName)) {
	    throw new ServletException("Missing \"" + OrganisationGroupServlet.PARAM_GROUPING_NAME + "\" parameter");
	}
	String groupName = request.getParameter(OrganisationGroupServlet.PARAM_GROUP_NAME);
	if (StringUtils.isBlank(groupName)) {
	    throw new ServletException("Missing \"" + OrganisationGroupServlet.PARAM_GROUP_NAME + "\" parameter");
	}

	OrganisationGrouping grouping = findGrouping(organisationId, groupingName);
	if (grouping == null) {
	    throw new ServletException("Grouping with name \"" + groupingName
		    + "\" does not exist in organisation with ID " + organisationId);
	}

	// check group name uniqueness
	Set<OrganisationGroup> groups = new HashSet<OrganisationGroup>(grouping.getGroups());
	for (OrganisationGroup group : groups) {
	    if (group.getName().equals(groupName)) {
		throw new ServletException("Group with name \"" + groupingName + "\" exists in grouping with ID "
			+ grouping.getGroupingId());
	    }
	}

	OrganisationGroup newGroup = new OrganisationGroup();
	newGroup.setName(groupName);
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
    }

    private void removeGroup(HttpServletRequest request, Integer organisationId) throws ServletException {
	String groupingName = request.getParameter(OrganisationGroupServlet.PARAM_GROUPING_NAME);
	if (StringUtils.isBlank(groupingName)) {
	    throw new ServletException("Missing \"" + OrganisationGroupServlet.PARAM_GROUPING_NAME + "\" parameter");
	}
	String groupName = request.getParameter(OrganisationGroupServlet.PARAM_GROUP_NAME);
	if (StringUtils.isBlank(groupName)) {
	    throw new ServletException("Missing \"" + OrganisationGroupServlet.PARAM_GROUP_NAME + "\" parameter");
	}
	OrganisationGrouping grouping = findGrouping(organisationId, groupingName);
	if (grouping == null) {
	    // ignore if grouping does not exist
	    return;
	}
	OrganisationGroup group = findGroup(grouping.getGroupingId(), groupName);
	if (group == null) {
	    // ignore if group does not exist
	    return;
	}

	Set<OrganisationGroup> groups = new HashSet<OrganisationGroup>(grouping.getGroups());
	groups.remove(group);
	getUserManagementService().saveOrganisationGrouping(grouping, groups);

	if (OrganisationGroupServlet.log.isDebugEnabled()) {
	    OrganisationGroupServlet.log.debug("Deleted course group with ID " + group.getGroupId()
		    + " from organisation with ID " + organisationId);
	}
    }

    private void addLearners(HttpServletRequest request, ExtServer extServer, Integer organisationId)
	    throws ServletException {
	String groupingName = request.getParameter(OrganisationGroupServlet.PARAM_GROUPING_NAME);
	if (StringUtils.isBlank(groupingName)) {
	    throw new ServletException("Missing \"" + OrganisationGroupServlet.PARAM_GROUPING_NAME + "\" parameter");
	}
	String groupName = request.getParameter(OrganisationGroupServlet.PARAM_GROUP_NAME);
	if (StringUtils.isBlank(groupName)) {
	    throw new ServletException("Missing \"" + OrganisationGroupServlet.PARAM_GROUP_NAME + "\" parameter");
	}
	String learnerIds = request.getParameter(CentralConstants.PARAM_LEARNER_IDS);
	if (StringUtils.isBlank(learnerIds)) {
	    throw new ServletException("Missing \"" + CentralConstants.PARAM_LEARNER_IDS + "\" parameter");
	}

	OrganisationGrouping grouping = findGrouping(organisationId, groupingName);
	if (grouping == null) {
	    throw new ServletException("Grouping with name \"" + groupingName + "\" does not exist");
	}
	OrganisationGroup group = findGroup(grouping.getGroupingId(), groupName);
	if (group == null) {
	    throw new ServletException("Group with name \"" + groupName + "\" does not exist in grouping with ID "
		    + grouping.getGroupingId());
	}

	String[] learnerLoginArray = learnerIds.split(",");
	boolean learnersAdded = false;
	StringBuilder addedLearnerLogins = new StringBuilder();
	for (String learnerLogin : learnerLoginArray) {
	    User learner = null;
	    try {
		ExtUserUseridMap userMap = OrganisationGroupServlet.integrationService.getExtUserUseridMap(extServer,
			learnerLogin);
		learner = userMap.getUser();
	    } catch (UserInfoFetchException e) {
		throw new ServletException("Learner with ID \"" + learnerLogin + "\" does not exist");
	    } catch (UserInfoValidationException e) {
		throw new ServletException(e.getMessage());
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
			+ group.getGroupId() + " in organisation with ID " + organisationId);
	    }
	}
	return;
    }

    private void removeLearners(HttpServletRequest request, ExtServer extServer, Integer organisationId)
	    throws ServletException {
	String groupingName = request.getParameter(OrganisationGroupServlet.PARAM_GROUPING_NAME);
	if (StringUtils.isBlank(groupingName)) {
	    throw new ServletException("Missing \"" + OrganisationGroupServlet.PARAM_GROUPING_NAME + "\" parameter");
	}
	String groupName = request.getParameter(OrganisationGroupServlet.PARAM_GROUP_NAME);
	if (StringUtils.isBlank(groupName)) {
	    throw new ServletException("Missing \"" + OrganisationGroupServlet.PARAM_GROUP_NAME + "\" parameter");
	}
	String learnerIds = request.getParameter(CentralConstants.PARAM_LEARNER_IDS);
	if (StringUtils.isBlank(learnerIds)) {
	    throw new ServletException("Missing \"" + CentralConstants.PARAM_LEARNER_IDS + "\" parameter");
	}
	OrganisationGrouping grouping = findGrouping(organisationId, groupingName);
	if (grouping == null) {
	    throw new ServletException("Grouping with name \"" + groupingName + "\" does not exist");
	}
	OrganisationGroup group = findGroup(grouping.getGroupingId(), groupName);
	if (group == null) {
	    throw new ServletException("Group with name \"" + groupName + "\" does not exist in grouping with ID "
		    + grouping.getGroupingId());
	}

	String[] learnerLoginArray = learnerIds.split(",");
	boolean learnersRemoved = false;
	StringBuilder removedLearnerLogins = new StringBuilder();
	for (String learnerLogin : learnerLoginArray) {
	    User learner = null;
	    try {
		ExtUserUseridMap userMap = OrganisationGroupServlet.integrationService.getExtUserUseridMap(extServer,
			learnerLogin);
		learner = userMap.getUser();
	    } catch (UserInfoFetchException e) {
		// if user does not exist, ignore
		continue;
	    } catch (UserInfoValidationException e) {
		// if user can't be created, ignore
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
			+ group.getGroupId() + " in organisation with ID " + organisationId);
	    }
	}
	return;
    }

    @SuppressWarnings("unchecked")
    private OrganisationGrouping findGrouping(Integer organisationId, String groupingName) {
	Map<String, Object> queryProperties = new TreeMap<String, Object>();
	queryProperties.put("organisationId", organisationId);
	queryProperties.put("name", groupingName);
	List<OrganisationGrouping> result = getUserManagementService().findByProperties(OrganisationGrouping.class,
		queryProperties);
	return (result == null) || result.isEmpty() ? null : result.get(0);
    }

    @SuppressWarnings("unchecked")
    private OrganisationGroup findGroup(Long groupingId, String groupName) {
	Map<String, Object> queryProperties = new TreeMap<String, Object>();
	queryProperties.put("groupingId", groupingId);
	queryProperties.put("name", groupName);
	List<OrganisationGroup> result = getUserManagementService().findByProperties(OrganisationGroup.class,
		queryProperties);
	return (result == null) || result.isEmpty() ? null : result.get(0);
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