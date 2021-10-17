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

package org.lamsfoundation.lams.security;

import java.io.Serializable;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.springframework.stereotype.Repository;

@Repository
public class SecurityDAO extends LAMSBaseDAO implements ISecurityDAO {

    /**
     * Checks if the user is a staff member in the given lesson.
     */
    private static final String CHECK_LESSON_MONITOR = "FROM " + Lesson.class.getName()
	    + " AS lesson INNER JOIN lesson.lessonClass.staffGroup.users AS monitor "
	    + "WHERE lesson.lessonId = ? AND monitor.userId = ?";

    /**
     * Checks if the user is a learner in the given lesson.
     */
    private static final String CHECK_LESSON_LEARNER = "SELECT 1 FROM lams_lesson AS l JOIN lams_grouping AS ging "
	    + "ON l.lesson_id = :lessonId AND l.class_grouping_id = ging.grouping_id JOIN lams_group AS g USING (grouping_id) "
	    + "JOIN lams_user_group AS ug USING (group_id) WHERE ug.user_id = :user_id";

    /**
     * Checks if the user has any of the given roles in the given organisation.
     */
    private static final String CHECK_ORG_ROLE = "FROM " + UserOrganisation.class.getName()
	    + " AS userOrganisation INNER JOIN userOrganisation.userOrganisationRoles AS userOrganisationRole "
	    + "WHERE userOrganisation.organisation.organisationId = :orgId AND userOrganisation.user.userId = :userId "
	    + "AND userOrganisationRole.role.name IN (:roles)";

    /**
     * Checks if user has a global role of SYSADMIN.
     */
    private static final String CHECK_SYSADMIN = "FROM " + UserOrganisation.class.getName()
	    + " AS userOrganisation INNER JOIN userOrganisation.userOrganisationRoles AS userOrganisationRole "
	    + "WHERE userOrganisation.organisation.organisationType.organisationTypeId = 1 AND userOrganisation.user.userId = ? "
	    + "AND userOrganisationRole.role.name = '" + Role.SYSADMIN + "'";

    @Override
    @SuppressWarnings("rawtypes")
    public Object find(Class clazz, Serializable id) {
	return getSession().get(clazz, id);
    }

    @Override
    public boolean hasOrgRole(Integer orgId, Integer userId, String... roles) {
	Query query = getSession().createQuery(SecurityDAO.CHECK_ORG_ROLE);
	query.setParameter("orgId", orgId);
	query.setParameter("userId", userId);
	query.setParameterList("roles", roles);
	return !query.setCacheable(true).list().isEmpty();
    }

    @Override
    public boolean isGroupManager(Integer orgId, Integer userId) {
	Organisation organisation = (Organisation) find(Organisation.class, orgId);
	if (organisation == null) {
	    return false;
	}
	if (OrganisationType.CLASS_TYPE.equals(organisation.getOrganisationType().getOrganisationTypeId())) {
	    organisation = organisation.getParentOrganisation();
	}
	return hasOrgRole(organisation.getOrganisationId(), userId, Role.GROUP_MANAGER);
    }

    @Override
    public boolean isLessonLearner(Long lessonId, Integer userId) {
	SQLQuery query = getSession().createSQLQuery(SecurityDAO.CHECK_LESSON_LEARNER);
	query.setLong("lessonId", lessonId);
	query.setInteger("user_id", userId);
	return !query.setCacheable(true).list().isEmpty();
    }

    @Override
    public boolean isLessonMonitor(Long lessonId, Integer userId, boolean ownerAccepted) {
	boolean result = !doFindCacheable(SecurityDAO.CHECK_LESSON_MONITOR, new Object[] { lessonId, userId })
		.isEmpty();
	Lesson lesson = null;
	if (!result && ownerAccepted) {
	    lesson = (Lesson) find(Lesson.class, lessonId);
	    result = (lesson != null) && userId.equals(lesson.getUser().getUserId());
	}
	return result;
    }

    @Override
    public boolean isSysadmin(Integer userId) {
	return !doFindCacheable(SecurityDAO.CHECK_SYSADMIN, new Object[] { userId }).isEmpty();
    }
}