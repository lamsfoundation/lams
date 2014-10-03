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

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.Role;

public class SecurityService implements ISecurityService {

    private ISecurityDAO securityDAO;

    private static Logger log = Logger.getLogger(SecurityService.class);

    @Override
    public void checkIsLessonLearner(Long lessonId, Integer userId) throws SecurityException {
	if (lessonId == null) {
	    throw new SecurityException("Lesson ID is NULL");
	}
	if (userId == null) {
	    throw new SecurityException("User ID is NULL");
	}

	Lesson lesson = (Lesson) securityDAO.find(Lesson.class, lessonId);
	if (lesson == null) {
	    throw new SecurityException("Could not find lesson with ID: " + lessonId);
	}

	hasOrgRole(lesson.getOrganisation().getOrganisationId(), userId, Role.LEARNER, Role.MONITOR);

	if (!securityDAO.isSysadmin(userId) && !securityDAO.isLessonLearner(lessonId, userId)) {
	    throw new SecurityException("User with ID: " + userId + " is not a learner in lesson with ID: " + lessonId);
	}
    }

    @Override
    public void checkIsLessonMonitor(Long lessonId, Integer userId) throws SecurityException {
	if (lessonId == null) {
	    throw new SecurityException("Lesson ID is NULL");
	}
	if (userId == null) {
	    throw new SecurityException("User ID is NULL");
	}

	Lesson lesson = (Lesson) securityDAO.find(Lesson.class, lessonId);
	if (lesson == null) {
	    throw new SecurityException("Could not find lesson with ID: " + lessonId);
	}

	hasOrgRole(lesson.getOrganisation().getOrganisationId(), userId, Role.MONITOR, Role.GROUP_MANAGER);

	if (!securityDAO.isSysadmin(userId) && !securityDAO.isLessonMonitor(lessonId, userId)) {
	    throw new SecurityException("User with ID: " + userId + " is not a monitor in lesson with ID: " + lessonId);
	}
    }

    @Override
    public void checkIsLessonParticipant(Long lessonId, Integer userId) throws SecurityException {
	if (lessonId == null) {
	    throw new SecurityException("Lesson ID is NULL");
	}
	if (userId == null) {
	    throw new SecurityException("User ID is NULL");
	}

	Lesson lesson = (Lesson) securityDAO.find(Lesson.class, lessonId);
	if (lesson == null) {
	    throw new SecurityException("Could not find lesson with ID: " + lessonId);
	}

	hasOrgRole(lesson.getOrganisation().getOrganisationId(), userId, Role.LEARNER, Role.MONITOR, Role.GROUP_MANAGER);

	if (!securityDAO.isSysadmin(userId) && !securityDAO.isLessonLearner(lessonId, userId)
		&& !securityDAO.isLessonMonitor(lessonId, userId)) {
	    throw new SecurityException("User with ID: " + userId + " is not a learner in lesson with ID: " + lessonId);
	}
    }

    @Override
    public void checkIsSysadmin(Integer userId) {
	if (userId == null) {
	    throw new SecurityException("User ID is NULL");
	}

	if (!securityDAO.isSysadmin(userId)) {
	    throw new SecurityException("User with ID: " + userId + " is not a sysadmin.");
	}
    }

    @Override
    public void hasOrgRole(Integer orgId, Integer userId, String... roles) throws SecurityException {
	if (orgId == null) {
	    throw new SecurityException("Organisation ID is NULL");
	}
	if (userId == null) {
	    throw new SecurityException("User ID is NULL");
	}

	if (!securityDAO.isSysadmin(userId) && !securityDAO.hasOrgRole(orgId, userId, roles)) {
	    throw new SecurityException("User with ID: " + userId + " is not any of " + Arrays.toString(roles)
		    + " in organisation with ID: " + orgId);
	}
    }

    public void setSecurityDAO(ISecurityDAO securityDAO) {
	this.securityDAO = securityDAO;
    }
}