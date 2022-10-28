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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.web.filter.AuditLogFilter;

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
    public boolean isLessonLearner(Long lessonId, Integer userId, String action) {
	return isLessonLearner(lessonId, userId, action, false);
    }

    @Override
    public boolean isLessonLearner(Long lessonId, Integer userId, String action, boolean skipLog) {
	return isLessonLearner(lessonId, userId, action, skipLog, false);
    }

    @Override
    public boolean ensureLessonLearner(Long lessonId, Integer userId, String action) {
	return isLessonLearner(lessonId, userId, action, false, true);
    }

    private boolean isLessonLearner(Long lessonId, Integer userId, String action, boolean skipLog, boolean escalate)
	    throws SecurityException {
	if (lessonId == null) {
	    String error = "Missing lesson ID when checking if user " + userId + " is learner and can \"" + action
		    + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}
	if (userId == null) {
	    String error = "Missing user ID when checking if is learner in lesson " + lessonId + " and can \"" + action
		    + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}

	Lesson lesson = (Lesson) securityDAO.find(Lesson.class, lessonId);
	if (lesson == null) {
	    String error = "Could not find lesson " + lessonId + " when checking if user " + userId
		    + " is learner and can \"" + action + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}

	Organisation org = lesson.getOrganisation();
	Integer orgId = org == null ? null : org.getOrganisationId();
	boolean hasSysadminRole = securityDAO.isSysadmin(userId);
	boolean hasOrgRole = orgId == null || securityDAO.hasOrgRole(orgId, userId, Role.LEARNER);

	if (!hasSysadminRole && !(hasOrgRole && securityDAO.isLessonLearner(lessonId, userId))) {
	    String error = "User " + userId + " is not learner in lesson " + lessonId + " and can not \"" + action
		    + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}

	return true;
    }

    @Override
    public boolean isLessonMonitor(Long lessonId, Integer userId, String action) {
	return isLessonMonitor(lessonId, userId, action, false);
    }

    @Override
    public boolean isLessonMonitor(Long lessonId, Integer userId, String action, boolean skipLog) {
	return isLessonMonitor(lessonId, userId, action, skipLog, false);
    }

    @Override
    public boolean ensureLessonMonitor(Long lessonId, Integer userId, String action) {
	return isLessonMonitor(lessonId, userId, action, false, true);
    }

    private boolean isLessonMonitor(Long lessonId, Integer userId, String action, boolean skipLog, boolean escalate)
	    throws SecurityException {
	if (lessonId == null) {
	    String error = "Missing lesson ID when checking if user " + userId + " is monitor and can \"" + action
		    + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}
	if (userId == null) {
	    String error = "Missing user ID when checking if is monitor in lesson " + lessonId + " and can \"" + action
		    + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}

	Lesson lesson = (Lesson) securityDAO.find(Lesson.class, lessonId);
	if (lesson == null) {
	    String error = "Could not find lesson " + lessonId + " when checking if user " + userId
		    + " is monitor and can \"" + action + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}

	Organisation org = lesson.getOrganisation();
	Integer orgId = org == null ? null : org.getOrganisationId();
	boolean hasSysadminRole = securityDAO.isSysadmin(userId);
	boolean hasGroupManagerRole = hasSysadminRole || (orgId != null && securityDAO.isGroupManager(orgId, userId));
	boolean hasMonitorRole = hasGroupManagerRole || orgId == null
		|| securityDAO.hasOrgRole(orgId, userId, Role.MONITOR);

	if (!hasGroupManagerRole && !(hasMonitorRole && securityDAO.isLessonMonitor(lessonId, userId, true))) {
	    String error = "User " + userId + " is not monitor in lesson " + lessonId + " and can not \"" + action
		    + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}

	return true;
    }

    @Override
    public boolean isLessonOwner(Long lessonId, Integer userId, String action) {
	return isLessonOwner(lessonId, userId, action, false);
    }

    @Override
    public boolean isLessonOwner(Long lessonId, Integer userId, String action, boolean skipLog) {
	return isLessonOwner(lessonId, userId, action, skipLog, false);
    }

    @Override
    public boolean ensureLessonOwner(Long lessonId, Integer userId, String action) {
	return isLessonOwner(lessonId, userId, action, false, true);
    }

    private boolean isLessonOwner(Long lessonId, Integer userId, String action, boolean skipLog, boolean escalate)
	    throws SecurityException {
	if (lessonId == null) {
	    String error = "Missing lesson ID when checking if user " + userId + " is owner and can \"" + action + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}
	if (userId == null) {
	    String error = "Missing user ID when checking if is owner of lesson " + lessonId + " and can \"" + action
		    + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}

	Lesson lesson = (Lesson) securityDAO.find(Lesson.class, lessonId);
	if (lesson == null) {
	    String error = "Could not find lesson " + lessonId + " when checking if user " + userId
		    + " is owner and can \"" + action + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}

	if (!lesson.getUser().getUserId().equals(userId)) {
	    String error = "User " + userId + " is not owner of lesson " + lessonId + " and can not \"" + action + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}

	return true;
    }

    @Override
    public boolean isLessonParticipant(Long lessonId, Integer userId, String action) {
	return isLessonParticipant(lessonId, userId, action, false);
    }

    @Override
    public boolean isLessonParticipant(Long lessonId, Integer userId, String action, boolean skipLog) {
	return isLessonParticipant(lessonId, userId, action, skipLog, false);
    }

    @Override
    public boolean ensureLessonParticipant(Long lessonId, Integer userId, String action) {
	return isLessonParticipant(lessonId, userId, action, false, true);
    }

    private boolean isLessonParticipant(Long lessonId, Integer userId, String action, boolean skipLog, boolean escalate)
	    throws SecurityException {
	if (lessonId == null) {
	    String error = "Missing lesson ID when checking if user " + userId + " is participant and can \"" + action
		    + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}
	if (userId == null) {
	    String error = "Missing user ID when checking if is participant in lesson " + lessonId + " and can \""
		    + action + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}

	Lesson lesson = (Lesson) securityDAO.find(Lesson.class, lessonId);
	if (lesson == null) {
	    String error = "Could not find lesson " + lessonId + " when checking if user " + userId
		    + " is participant and can \"" + action + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}

	Organisation org = lesson.getOrganisation();
	Integer orgId = org == null ? null : org.getOrganisationId();
	boolean hasSysadminRole = securityDAO.isSysadmin(userId);
	boolean hasGroupManagerRole = hasSysadminRole || (orgId != null && securityDAO.isGroupManager(orgId, userId));
	boolean hasRole = hasGroupManagerRole || orgId == null
		|| securityDAO.hasOrgRole(orgId, userId, Role.LEARNER, Role.MONITOR);

	if (!hasGroupManagerRole && !(hasRole && (securityDAO.isLessonLearner(lessonId, userId)
		|| securityDAO.isLessonMonitor(lessonId, userId, true)))) {
	    String error = "User " + userId + " is not participant in lesson " + lessonId + " and can not \"" + action
		    + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}

	return true;
    }

    @Override
    public boolean isSysadmin(Integer userId, String action) {
	return isSysadmin(userId, action, false);
    }

    @Override
    public boolean isSysadmin(Integer userId, String action, boolean skipLog) {
	return isSysadmin(userId, action, skipLog, false);
    }

    @Override
    public boolean ensureSysadmin(Integer userId, String action) {
	return isSysadmin(userId, action, false, true);
    }

    private boolean isSysadmin(Integer userId, String action, boolean skipLog, boolean escalate) {
	if (userId == null) {
	    String error = "Missing user ID when checking if is sysadmin and can \"" + action + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}

	if (!securityDAO.isSysadmin(userId)) {
	    String error = "User " + userId + " is not sysadmin and can not \"" + action + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}

	return true;

    }

    @Override
    public boolean isGroupMonitor(Integer orgId, Integer userId, String action) {
	return isGroupMonitor(orgId, userId, action, false);
    }

    @Override
    public boolean isGroupMonitor(Integer orgId, Integer userId, String action, boolean skipLog) {
	return isGroupMonitor(orgId, userId, action, skipLog, false);
    }

    @Override
    public boolean ensureGroupMonitor(Integer orgId, Integer userId, String action) {
	return isGroupMonitor(orgId, userId, action, false, true);
    }

    private boolean isGroupMonitor(Integer orgId, Integer userId, String action, boolean skipLog, boolean escalate)
	    throws SecurityException {
	return hasOrgRole(orgId, userId, SecurityService.GROUP_MONITOR_ROLES, action, skipLog, escalate);
    }

    @Override
    public boolean hasOrgRole(Integer orgId, Integer userId, String[] roles, String action) {
	return hasOrgRole(orgId, userId, roles, action, false);
    }

    @Override
    public boolean hasOrgRole(Integer orgId, Integer userId, String[] roles, String action, boolean skipLog) {
	return hasOrgRole(orgId, userId, roles, action, skipLog, false);
    }

    @Override
    public boolean ensureOrgRole(Integer orgId, Integer userId, String[] roles, String action) {
	return hasOrgRole(orgId, userId, roles, action, false, true);
    }

    private boolean hasOrgRole(Integer orgId, Integer userId, String[] roles, String action, boolean skipLog,
	    boolean escalate) throws SecurityException {
	if (orgId == null) {
	    String error = "Missing organisation ID when checking if user " + userId + " has any of "
		    + Arrays.toString(roles) + " roles in organisation " + orgId + " and can \"" + action + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}
	if (userId == null) {
	    String error = "Missing user ID when checking if has any of " + Arrays.toString(roles)
		    + " roles in organisation " + orgId + " and can \"" + action + "\"";
	    return processCheckFailure(userId, error, skipLog, escalate);
	}

	try {
	    if (securityDAO.isSysadmin(userId) || securityDAO.hasOrgRole(orgId, userId, roles)) {
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
	return processCheckFailure(userId, error, skipLog, escalate);
    }

    private boolean processCheckFailure(Integer userId, String error, boolean skipLog, boolean escalate) {
	// always log if an exception is going to be thrown
	skipLog |= escalate;
	skipLog &= StringUtils.isNotBlank(error);

	if (!skipLog) {
	    SecurityService.log.warn(error);
	    logEventService.logEvent(LogEvent.TYPE_ROLE_FAILURE, userId, userId, null, null, error);

	    User user = (User) securityDAO.find(User.class, userId);
	    AuditLogFilter.log(user.getUserDTO(), AuditLogFilter.ROLE_CHECK_ACTION,
		    "failed role check with message: " + error);
	}
	if (escalate) {
	    throw new SecurityException(error);
	}
	return false;
    }

    public void setSecurityDAO(ISecurityDAO securityDAO) {
	this.securityDAO = securityDAO;
    }

    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }
}