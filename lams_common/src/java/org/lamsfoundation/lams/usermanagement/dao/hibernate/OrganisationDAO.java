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


package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO;
import org.springframework.stereotype.Repository;

/**
 * @author jliew
 */
@Repository
public class OrganisationDAO extends LAMSBaseDAO implements IOrganisationDAO {

    private static final String GET_ALL_ACTIVE_COURSE_IDS = "select o.organisationId from Organisation o"
	    + " where o.organisationType.organisationTypeId = " + OrganisationType.COURSE_TYPE
	    + " and o.organisationState.organisationStateId = " + OrganisationState.ACTIVE + " order by name";

    private static final String GET_ACTIVE_COURSE_IDS_BY_USER = "select uo.organisation.organisationId, uoc.collapsed"
	    + " from UserOrganisation uo left join uo.userOrganisationCollapsed uoc"
	    + " where uo.organisation.organisationType.organisationTypeId = " + OrganisationType.COURSE_TYPE
	    + " and uo.organisation.organisationState.organisationStateId = " + OrganisationState.ACTIVE
	    + " and uo.user.userId = :userId order by name";

    private static final String GET_PAGED_COURSES = "SELECT o FROM Organisation o WHERE o.organisationType.organisationTypeId =:typeId "
	    + "AND o.organisationState.organisationStateId =:stateId AND o.parentOrganisation.organisationId =:parentOrgId "
	    + "AND (o.name LIKE CONCAT('%', :searchString, '%')) ORDER BY ";

    private static final String GET_COUNT_COURSES_BY_PARENT_TYPE_STATE = "SELECT count(*) FROM Organisation o "
	    + " WHERE o.parentOrganisation.organisationId =:parentOrgId "
	    + " AND o.organisationType.organisationTypeId =:typeId "
	    + " AND o.organisationState.organisationStateId =:stateId "
	    + " AND (o.name LIKE CONCAT('%', :searchString, '%')) ";

    @SuppressWarnings("unchecked")
    @Override
    public List<Integer> getActiveCourseIdsByUser(Integer userId, boolean isSysadmin) {
	return isSysadmin ? getSession().createQuery(OrganisationDAO.GET_ALL_ACTIVE_COURSE_IDS).list()
		: getSession().createQuery(OrganisationDAO.GET_ACTIVE_COURSE_IDS_BY_USER).setInteger("userId", userId)
			.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Organisation> getPagedCourses(Integer parentOrgId, Integer typeId, Integer stateId, int page, int size,
	    String sortBy, String sortOrder, String searchString) {
	String sortByParam = "o.name";
	switch (sortBy) {
	    case "id":
		sortByParam = "o.organisationId";
		break;
	    case "name":
		sortByParam = "o.name";
		break;
	    case "code":
		sortByParam = "o.code";
		break;
	    case "description":
		sortByParam = "o.description";
		break;
	}

	Query query = getSession().createQuery(OrganisationDAO.GET_PAGED_COURSES + sortByParam + " " + sortOrder);
	query.setInteger("typeId", typeId);
	query.setInteger("stateId", stateId);
	query.setInteger("parentOrgId", parentOrgId);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setString("searchString", searchString);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	return query.list();
    }

    @Override
    public int getCountCoursesByParentCourseAndTypeAndState(Integer parentOrgId, Integer typeId, Integer stateId,
	    String searchString) {
	Query query = getSession().createQuery(OrganisationDAO.GET_COUNT_COURSES_BY_PARENT_TYPE_STATE);
	query.setInteger("parentOrgId", parentOrgId);
	query.setInteger("typeId", typeId);
	query.setInteger("stateId", stateId);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setString("searchString", searchString);

	return ((Number) query.uniqueResult()).intValue();
    }
}