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

import javax.servlet.ServletConfig;
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
import org.lamsfoundation.lams.integration.util.IntegrationConstants;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationGroup;
import org.lamsfoundation.lams.usermanagement.OrganisationGrouping;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

public class OrganisationGroupServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(OrganisationGroupServlet.class);

    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private IUserManagementService userManagementService;

    private static final String METHOD_ADD_GROUPING = "addGrouping";
    private static final String METHOD_REMOVE_GROUPING = "removeGrouping";
    private static final String METHOD_ADD_GROUP = "addGroup";
    private static final String METHOD_REMOVE_GROUP = "removeGroup";
    private static final String METHOD_ADD_LEARNERS = "addLearners";
    private static final String METHOD_REMOVE_LEARNERS = "removeLearners";
    private static final String PARAM_GROUPING_NAME = "groupingName";
    private static final String PARAM_GROUP_NAME = "groupName";

    /*
     * Request Spring to lookup the applicationContext tied to the current ServletContext and inject service beans
     * available in that applicationContext.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

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
	ExtServer extServer = integrationService.getExtServer(serverId);

	try {
	    // authenticate the request
	    Authenticator.authenticate(extServer, datetime, username, hashValue);

	    // get local user and organisation
	    ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(extServer, username);
	    ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(extServer, userMap, courseId, null,
		    IntegrationConstants.METHOD_MONITOR);
	    user = userMap.getUser();
	    organisation = orgMap.getOrganisation();

	    // check if user is allowed to modify course groups for the given organisation
	    boolean isGroupSuperuser = userManagementService.isUserInRole(user.getUserId(),
		    organisation.getOrganisationId(), Role.MONITOR)
		    || userManagementService.isUserInRole(user.getUserId(), organisation.getOrganisationId(),
			    Role.AUTHOR)
		    || userManagementService.isUserInRole(user.getUserId(), organisation.getOrganisationId(),
			    Role.GROUP_MANAGER);
	    if (!isGroupSuperuser) {
		log.error("User " + user.getUserId() + " may not perform group actions for course "
			+ organisation.getOrganisationId());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
			"Course group action failed - user may not perform group actions");
		return;
	    }
	} catch (AuthenticationException e) {
	    log.error(e);
	    response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
		    "Course group action failed - authentication error");
	    return;
	} catch (UserInfoFetchException e) {
	    log.error(e);
	    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Course group action failed - user does not exist");
	    return;
	} catch (UserInfoValidationException e) {
	    log.error(e);
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course group action failed." + e.getMessage());
	    return;
	}

	try {
	    String method = request.getParameter(CentralConstants.PARAM_METHOD);
	    if (METHOD_ADD_GROUPING.equals(method)) {
		addGrouping(request, organisation);
	    } else if (METHOD_REMOVE_GROUPING.equals(method)) {
		removeGrouping(request, organisation.getOrganisationId());
	    } else if (METHOD_ADD_GROUP.equals(method)) {
		addGroup(request, organisation.getOrganisationId());
	    } else if (METHOD_REMOVE_GROUP.equals(method)) {
		removeGroup(request, organisation.getOrganisationId());
	    } else if (METHOD_ADD_LEARNERS.equals(method)) {
		addLearners(request, extServer, organisation.getOrganisationId());
	    } else if (METHOD_REMOVE_LEARNERS.equals(method)) {
		removeLearners(request, extServer, organisation.getOrganisationId());
	    }
	} catch (ServletException e) {
	    log.error(e);
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course group action failed: " + e.getMessage());
	    return;
	} catch (Exception e) {
	    log.error(e);
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
	String groupingName = request.getParameter(PARAM_GROUPING_NAME);
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
	userManagementService.saveOrganisationGrouping(grouping, null);

	Long groupingId = grouping.getGroupingId();
	if (groupingId == null) {
	    throw new ServletException("Grouping could not be created");
	}

	if (log.isDebugEnabled()) {
	    log.debug("Created course grouping \"" + groupingName + "\" with ID " + groupingId
		    + " in organisation with ID " + organisation.getOrganisationId());
	}
    }

    private void removeGrouping(HttpServletRequest request, Integer organisationId) throws ServletException {
	String groupingName = request.getParameter(PARAM_GROUPING_NAME);
	if (StringUtils.isBlank(groupingName)) {
	    throw new ServletException("Missing \"" + PARAM_GROUPING_NAME + "\" parameter");
	}

	OrganisationGrouping grouping = findGrouping(organisationId, groupingName);
	if (grouping == null) {
	    // if grouping does not exist, just ignore it
	    return;
	}

	userManagementService.delete(grouping);

	if (log.isDebugEnabled()) {
	    log.debug(
		    "Deleted course grouping with ID " + groupingName + " from organisation with ID " + organisationId);
	}
    }

    private void addGroup(HttpServletRequest request, Integer organisationId) throws ServletException {
	String groupingName = request.getParameter(PARAM_GROUPING_NAME);
	if (StringUtils.isBlank(groupingName)) {
	    throw new ServletException("Missing \"" + PARAM_GROUPING_NAME + "\" parameter");
	}
	String groupName = request.getParameter(PARAM_GROUP_NAME);
	if (StringUtils.isBlank(groupName)) {
	    throw new ServletException("Missing \"" + PARAM_GROUP_NAME + "\" parameter");
	}

	OrganisationGrouping grouping = findGrouping(organisationId, groupingName);
	if (grouping == null) {
	    throw new ServletException("Grouping with name \"" + groupingName
		    + "\" does not exist in organisation with ID " + organisationId);
	}

	// check group name uniqueness
	Set<OrganisationGroup> groups = new HashSet<>(grouping.getGroups());
	for (OrganisationGroup group : groups) {
	    if (group.getName().equals(groupName)) {
		throw new ServletException("Group with name \"" + groupingName + "\" exists in grouping with ID "
			+ grouping.getGroupingId());
	    }
	}

	OrganisationGroup newGroup = new OrganisationGroup();
	newGroup.setName(groupName);
	groups.add(newGroup);

	userManagementService.saveOrganisationGrouping(grouping, groups);
	Long groupId = newGroup.getGroupId();
	if (groupId == null) {
	    throw new ServletException("Group could not be created");
	}

	if (log.isDebugEnabled()) {
	    log.debug("Created course group \"" + groupName + "\" with ID " + groupId + " in organisation with ID "
		    + organisationId);
	}
    }

    private void removeGroup(HttpServletRequest request, Integer organisationId) throws ServletException {
	String groupingName = request.getParameter(PARAM_GROUPING_NAME);
	if (StringUtils.isBlank(groupingName)) {
	    throw new ServletException("Missing \"" + PARAM_GROUPING_NAME + "\" parameter");
	}
	String groupName = request.getParameter(PARAM_GROUP_NAME);
	if (StringUtils.isBlank(groupName)) {
	    throw new ServletException("Missing \"" + PARAM_GROUP_NAME + "\" parameter");
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

	Set<OrganisationGroup> groups = new HashSet<>(grouping.getGroups());
	groups.remove(group);
	userManagementService.saveOrganisationGrouping(grouping, groups);

	if (log.isDebugEnabled()) {
	    log.debug("Deleted course group with ID " + group.getGroupId() + " from organisation with ID "
		    + organisationId);
	}
    }

    private void addLearners(HttpServletRequest request, ExtServer extServer, Integer organisationId)
	    throws ServletException {
	String groupingName = request.getParameter(PARAM_GROUPING_NAME);
	if (StringUtils.isBlank(groupingName)) {
	    throw new ServletException("Missing \"" + PARAM_GROUPING_NAME + "\" parameter");
	}
	String groupName = request.getParameter(PARAM_GROUP_NAME);
	if (StringUtils.isBlank(groupName)) {
	    throw new ServletException("Missing \"" + PARAM_GROUP_NAME + "\" parameter");
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
		ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(extServer, learnerLogin);
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
	    userManagementService.saveOrganisationGrouping(grouping, grouping.getGroups());

	    if (log.isDebugEnabled()) {
		log.debug("Added learners " + addedLearnerLogins + " to course group with ID " + group.getGroupId()
			+ " in organisation with ID " + organisationId);
	    }
	}
	return;
    }

    private void removeLearners(HttpServletRequest request, ExtServer extServer, Integer organisationId)
	    throws ServletException {
	String groupingName = request.getParameter(PARAM_GROUPING_NAME);
	if (StringUtils.isBlank(groupingName)) {
	    throw new ServletException("Missing \"" + PARAM_GROUPING_NAME + "\" parameter");
	}
	String groupName = request.getParameter(PARAM_GROUP_NAME);
	if (StringUtils.isBlank(groupName)) {
	    throw new ServletException("Missing \"" + PARAM_GROUP_NAME + "\" parameter");
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
		ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(extServer, learnerLogin);
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
	    userManagementService.saveOrganisationGrouping(grouping, grouping.getGroups());
	    if (log.isDebugEnabled()) {
		log.debug("Removed learners " + removedLearnerLogins + " from group with ID " + group.getGroupId()
			+ " in organisation with ID " + organisationId);
	    }
	}
	return;
    }

    @SuppressWarnings("unchecked")
    private OrganisationGrouping findGrouping(Integer organisationId, String groupingName) {
	Map<String, Object> queryProperties = new TreeMap<>();
	queryProperties.put("organisationId", organisationId);
	queryProperties.put("name", groupingName);
	List<OrganisationGrouping> result = userManagementService.findByProperties(OrganisationGrouping.class,
		queryProperties);
	return (result == null) || result.isEmpty() ? null : result.get(0);
    }

    @SuppressWarnings("unchecked")
    private OrganisationGroup findGroup(Long groupingId, String groupName) {
	Map<String, Object> queryProperties = new TreeMap<>();
	queryProperties.put("groupingId", groupingId);
	queryProperties.put("name", groupName);
	List<OrganisationGroup> result = userManagementService.findByProperties(OrganisationGroup.class,
		queryProperties);
	return (result == null) || result.isEmpty() ? null : result.get(0);
    }
}