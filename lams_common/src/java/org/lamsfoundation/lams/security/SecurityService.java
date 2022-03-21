/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;

/**
 * Contains methods for checking and logging user access to LAMS content. Should be used throughout the whole project.
 * Calls with escalate=false are for soft checking in Actions. Onet with escalate=true are for checking in Services so
 * the exception can bubble to Actions.
 */
public class SecurityService implements ISecurityService {
    private static Logger log = Logger.getLogger(SecurityService.class);

    private static final String[] GROUP_MONITOR_ROLES = new String[] { Role.GROUP_MANAGER, Role.MONITOR };

    private ISecurityDAO securityDAO;
    private ILogEventService logEventService;

    @Override
    public boolean isLessonLearner(Long lessonId, Integer userId, String action, boolean escalate)
	    throws SecurityException {
	if (lessonId == null) {
	    String error = "Missing lesson ID when checking if user " + userId + " is learner and can \"" + action
		    + "\"";
	    SecurityService.log.error(error);
	    logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, lessonId, null, error);
	    if (escalate) {
		throw new SecurityException(error);
	    } else {
		return false;
	    }
	}
	if (userId == null) {
	    String error = "Missing user ID when checking if is learner in lesson " + lessonId + " and can \"" + action
		    + "\"";
	    SecurityService.log.error(error);
	    logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, lessonId, null, error);
	    if (escalate) {
		throw new SecurityException(error);
	    } else {
		return false;
	    }
	}

	Lesson lesson = (Lesson) securityDAO.find(Lesson.class, lessonId);
	if (lesson == null) {
	    String error = "Could not find lesson " + lessonId + " when checking if user " + userId
		    + " is learner and can \"" + action + "\"";
	    SecurityService.log.error(error);
	    logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, lessonId, null, error);
	    if (escalate) {
		throw new SecurityException(error);
	    } else {
		return false;
	    }
	}

	Organisation org = lesson.getOrganisation();
	Integer orgId = org == null ? null : org.getOrganisationId();
	boolean hasAppadminRole = securityDAO.isAppadmin(userId);
	boolean hasOrgRole = orgId == null || securityDAO.hasOrgRole(orgId, userId, Role.LEARNER);

	if (!hasAppadminRole && !(hasOrgRole && securityDAO.isLessonLearner(lessonId, userId))) {
	    String error = "User " + userId + " is not learner in lesson " + lessonId + " and can not \"" + action
		    + "\"";
	    SecurityService.log.debug(error);
	    logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, lessonId, null, error);
	    if (escalate) {
		throw new SecurityException(error);
	    } else {
		return false;
	    }
	}

	return true;
    }

    @Override
    public boolean isLessonMonitor(Long lessonId, Integer userId, String action, boolean escalate)
	    throws SecurityException {
	if (lessonId == null) {
	    String error = "Missing lesson ID when checking if user " + userId + " is monitor and can \"" + action
		    + "\"";
	    SecurityService.log.error(error);
	    logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, lessonId, null, error);
	    if (escalate) {
		throw new SecurityException(error);
	    } else {
		return false;
	    }
	}
	if (userId == null) {
	    String error = "Missing user ID when checking if is monitor in lesson " + lessonId + " and can \"" + action
		    + "\"";
	    SecurityService.log.error(error);
	    logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, lessonId, null, error);
	    if (escalate) {
		throw new SecurityException(error);
	    } else {
		return false;
	    }
	}

	Lesson lesson = (Lesson) securityDAO.find(Lesson.class, lessonId);
	if (lesson == null) {
	    String error = "Could not find lesson " + lessonId + " when checking if user " + userId
		    + " is monitor and can \"" + action + "\"";
	    SecurityService.log.error(error);
	    logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, lessonId, null, error);
	    if (escalate) {
		throw new SecurityException(error);
	    } else {
		return false;
	    }
	}

	Organisation org = lesson.getOrganisation();
	Integer orgId = org == null ? null : org.getOrganisationId();
	boolean hasAppadminRole = securityDAO.isAppadmin(userId);
	boolean hasGroupManagerRole = hasAppadminRole || (orgId != null && securityDAO.isGroupManager(orgId, userId));
	boolean hasMonitorRole = hasGroupManagerRole || orgId == null
		|| securityDAO.hasOrgRole(orgId, userId, Role.MONITOR);

	if (!hasGroupManagerRole && !(hasMonitorRole && securityDAO.isLessonMonitor(lessonId, userId, true))) {
	    String error = "User " + userId + " is not monitor in lesson " + lessonId + " and can not \"" + action
		    + "\"";
	    //no logging needed, if action parameter is empty
	    if (action != null) {
		SecurityService.log.debug(error);
		logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, lessonId, null, error);
	    }
	    if (escalate) {
		throw new SecurityException(error);
	    } else {
		return false;
	    }
	}

	return true;
    }

    @Override
    public boolean isLessonOwner(Long lessonId, Integer userId, String action, boolean escalate)
	    throws SecurityException {
	if (lessonId == null) {
	    String error = "Missing lesson ID when checking if user " + userId + " is owner and can \"" + action + "\"";
	    SecurityService.log.error(error);
	    logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, lessonId, null, error);
	    if (escalate) {
		throw new SecurityException(error);
	    } else {
		return false;
	    }
	}
	if (userId == null) {
	    String error = "Missing user ID when checking if is owner of lesson " + lessonId + " and can \"" + action
		    + "\"";
	    SecurityService.log.error(error);
	    logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, lessonId, null, error);
	    if (escalate) {
		throw new SecurityException(error);
	    } else {
		return false;
	    }
	}

	Lesson lesson = (Lesson) securityDAO.find(Lesson.class, lessonId);
	if (lesson == null) {
	    String error = "Could not find lesson " + lessonId + " when checking if user " + userId
		    + " is owner and can \"" + action + "\"";
	    SecurityService.log.error(error);
	    logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, lessonId, null, error);
	    if (escalate) {
		throw new SecurityException(error);
	    } else {
		return false;
	    }
	}

	if (!lesson.getUser().getUserId().equals(userId)) {
	    String error = "User " + userId + " is not owner of lesson " + lessonId + " and can not \"" + action + "\"";
	    SecurityService.log.debug(error);
	    logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, lessonId, null, error);
	    if (escalate) {
		throw new SecurityException(error);
	    } else {
		return false;
	    }
	}

	return true;
    }

    @Override
    public boolean isLessonParticipant(Long lessonId, Integer userId, String action, boolean escalate)
	    throws SecurityException {
	if (lessonId == null) {
	    String error = "Missing lesson ID when checking if user " + userId + " is participant and can \"" + action
		    + "\"";
	    SecurityService.log.error(error);
	    logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, lessonId, null, error);
	    if (escalate) {
		throw new SecurityException(error);
	    } else {
		return false;
	    }
	}
	if (userId == null) {
	    String error = "Missing user ID when checking if is participant in lesson " + lessonId + " and can \""
		    + action + "\"";
	    SecurityService.log.error(error);
	    logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, lessonId, null, error);
	    if (escalate) {
		throw new SecurityException(error);
	    } else {
		return false;
	    }
	}

	Lesson lesson = (Lesson) securityDAO.find(Lesson.class, lessonId);
	if (lesson == null) {
	    String error = "Could not find lesson " + lessonId + " when checking if user " + userId
		    + " is participant and can \"" + action + "\"";
	    SecurityService.log.error(error);
	    logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, lessonId, null, error);
	    if (escalate) {
		throw new SecurityException(error);
	    } else {
		return false;
	    }
	}

	Organisation org = lesson.getOrganisation();
	Integer orgId = org == null ? null : org.getOrganisationId();
	boolean hasAppadminRole = securityDAO.isAppadmin(userId);
	boolean hasGroupManagerRole = hasAppadminRole || (orgId != null && securityDAO.isGroupManager(orgId, userId));
	boolean hasRole = hasGroupManagerRole || orgId == null
		|| securityDAO.hasOrgRole(orgId, userId, Role.LEARNER, Role.MONITOR);

	if (!hasGroupManagerRole && !(hasRole && (securityDAO.isLessonLearner(lessonId, userId)
		|| securityDAO.isLessonMonitor(lessonId, userId, true)))) {
	    String error = "User " + userId + " is not participant in lesson " + lessonId + " and can not \"" + action
		    + "\"";
	    SecurityService.log.debug(error);
	    logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, lessonId, null, error);
	    if (escalate) {
		throw new SecurityException(error);
	    } else {
		return false;
	    }
	}

	return true;
    }

    @Override
    public boolean isAppadmin(Integer userId, String action, boolean escalate) {
	if (userId == null) {
	    String error = "Missing user ID when checking if is appadmin and can \"" + action + "\"";
	    SecurityService.log.error(error);
	    logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, null, null, error);
	    throw new SecurityException(error);
	}

	if (!securityDAO.isAppadmin(userId)) {
	    String error = "User " + userId + " is not appadmin and can not \"" + action + "\"";
	    SecurityService.log.debug(error);
	    logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, null, null, error);
	    if (escalate) {
		throw new SecurityException(error);
	    } else {
		return false;
	    }
	}

	return true;
    }

    @Override
    public boolean isGroupMonitor(Integer orgId, Integer userId, String action, boolean escalate)
	    throws SecurityException {
	return hasOrgRole(orgId, userId, SecurityService.GROUP_MONITOR_ROLES, action, escalate);
    }

    @Override
    public boolean hasOrgRole(Integer orgId, Integer userId, String[] roles, String action, boolean escalate)
	    throws SecurityException {
	if (orgId == null) {
	    String error = "Missing organisation ID when checking if user " + userId + " has any of "
		    + Arrays.toString(roles) + " roles in organisation " + orgId + " and can \"" + action + "\"";
	    SecurityService.log.error(error);
	    if (escalate) {
		throw new SecurityException(error);
	    } else {
		return false;
	    }
	}
	if (userId == null) {
	    String error = "Missing user ID when checking if has any of " + Arrays.toString(roles)
		    + " roles in organisation " + orgId + " and can \"" + action + "\"";
	    SecurityService.log.error(error);
	    if (escalate) {
		throw new SecurityException(error);
	    } else {
		return false;
	    }
	}

	try {
	    if (securityDAO.isAppadmin(userId) || securityDAO.hasOrgRole(orgId, userId, roles)) {
		return true;
	    }

	    // check for super roles in the parent organisations
	    List<String> roleList = new ArrayList<>(Arrays.asList(roles));
	    if (roleList.contains(Role.GROUP_MANAGER)) {
		Organisation organisation = (Organisation) securityDAO.find(Organisation.class, orgId);
		if (OrganisationType.CLASS_TYPE.equals(organisation.getOrganisationType().getOrganisationTypeId())) {
		    organisation = organisation.getParentOrganisation();
		}

		if (securityDAO.hasOrgRole(organisation.getOrganisationId(), userId,
			new String[] { Role.GROUP_MANAGER })) {
		    return true;
		}
	    }
	} catch (Exception e) {
	    SecurityService.log.error("Error while checking user " + userId + " role in organisation " + orgId, e);
	}

	String error = "User " + userId + " does not have any of " + Arrays.toString(roles) + " roles in organisation "
		+ orgId + " and can not \"" + action + "\"";
	SecurityService.log.debug(error);
	logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, null, null, error);
	if (escalate) {
	    throw new SecurityException(error);
	} else {
	    return false;
	}
    }

    public void setSecurityDAO(ISecurityDAO securityDAO) {
	this.securityDAO = securityDAO;
    }

    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }
}