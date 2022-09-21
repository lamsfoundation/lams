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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.UserOrganisationCollapsed;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationDTO;
import org.springframework.stereotype.Repository;

/**
 * @author jliew
 */
@Repository
public class OrganisationDAO extends LAMSBaseDAO implements IOrganisationDAO {

    private static final String GET_PAGED_COURSES = "SELECT o FROM Organisation o WHERE o.organisationType.organisationTypeId =:typeId "
	    + "AND o.organisationState.organisationStateId =:stateId AND o.parentOrganisation.organisationId =:parentOrgId "
	    + "AND (o.name LIKE CONCAT('%', :searchString, '%')) ORDER BY ";

    private static final String GET_COUNT_COURSES_BY_PARENT_TYPE_STATE = "SELECT count(*) FROM Organisation o "
	    + " WHERE o.parentOrganisation.organisationId =:parentOrgId "
	    + " AND o.organisationType.organisationTypeId =:typeId "
	    + " AND o.organisationState.organisationStateId =:stateId "
	    + " AND (o.name LIKE CONCAT('%', :searchString, '%')) ";

    private static final String GET_COURSES_BY_CODES = "FROM Organisation WHERE code IN (:codes)";

    @SuppressWarnings("unchecked")
    @Override
    public List<OrganisationDTO> getActiveCoursesByUser(Integer userId, boolean isAppadmin, int page, int size,
	    String searchString) {

	final String GET_ALL_ACTIVE_COURSE_IDS = "SELECT o.organisationId, o.name FROM Organisation o"
		+ " WHERE o.organisationType.organisationTypeId = " + OrganisationType.COURSE_TYPE
		+ " AND o.organisationState.organisationStateId = " + OrganisationState.ACTIVE
		+ " AND (o.name LIKE CONCAT('%', :searchString, '%')) ORDER BY o.name";

	final String GET_ACTIVE_COURSE_IDS_BY_USER = "SELECT uo.organisation.organisationId, uo.organisation.name"
		+ " FROM UserOrganisation uo " + " WHERE uo.organisation.organisationType.organisationTypeId = "
		+ OrganisationType.COURSE_TYPE + " AND uo.organisation.organisationState.organisationStateId = "
		+ OrganisationState.ACTIVE + " AND uo.user.userId = :userId "
		+ " AND (uo.organisation.name LIKE CONCAT('%', :searchString, '%')) ORDER BY uo.organisation.name";

	String queryStr = isAppadmin ? GET_ALL_ACTIVE_COURSE_IDS : GET_ACTIVE_COURSE_IDS_BY_USER;
	Query query = getSession().createQuery(queryStr);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setString("searchString", searchString);
	if (!isAppadmin) {
	    query.setInteger("userId", userId);
	}
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	List<Object[]> list = query.list();

	List<OrganisationDTO> orgDtos = new ArrayList<>();
	if (list != null && list.size() > 0) {
	    for (Object[] element : list) {

		Integer orgId = ((Number) element[0]).intValue();
		String name = (String) element[1];

		OrganisationDTO orgDto = new OrganisationDTO(orgId, name);
		orgDtos.add(orgDto);
	    }

	}

	return orgDtos;
    }

    @Override
    public List<UserOrganisationCollapsed> getChildOrganisationsCollapsedByUser(Integer parentOrganisationId,
	    Integer userId) {
	final String GET_USER_ORGANISATION_COLLAPSED_BY_USER_AND_PARENT_ORGANISATION = "SELECT uoc FROM UserOrganisationCollapsed uoc "
		+ " WHERE uoc.organisation.organisationType.organisationTypeId = " + OrganisationType.CLASS_TYPE
		+ " AND uoc.organisation.organisationState.organisationStateId = " + OrganisationState.ACTIVE
		+ " AND uoc.organisation.parentOrganisation.organisationId = :organisationId "
		+ " AND uoc.user.userId = :userId ";

	Query<UserOrganisationCollapsed> query = getSession().createQuery(
		GET_USER_ORGANISATION_COLLAPSED_BY_USER_AND_PARENT_ORGANISATION, UserOrganisationCollapsed.class);
	query.setInteger("organisationId", parentOrganisationId);
	query.setInteger("userId", userId);
	return query.list();
    }

    @Override
    public int getCountActiveCoursesByUser(Integer userId, boolean isAppadmin, String searchString) {

	final String GET_ALL_ACTIVE_COURSE_IDS = "SELECT COUNT(o) FROM Organisation o"
		+ " WHERE o.organisationType.organisationTypeId = " + OrganisationType.COURSE_TYPE
		+ " AND o.organisationState.organisationStateId = " + OrganisationState.ACTIVE
		+ " AND (o.name LIKE CONCAT('%', :searchString, '%'))";

	final String GET_ACTIVE_COURSE_IDS_BY_USER = "SELECT COUNT(uo)" + " FROM UserOrganisation uo "
		+ " WHERE uo.organisation.organisationType.organisationTypeId = " + OrganisationType.COURSE_TYPE
		+ " AND uo.organisation.organisationState.organisationStateId = " + OrganisationState.ACTIVE
		+ " AND uo.user.userId = :userId " + " AND (uo.organisation.name LIKE CONCAT('%', :searchString, '%'))";

	String queryStr = isAppadmin ? GET_ALL_ACTIVE_COURSE_IDS : GET_ACTIVE_COURSE_IDS_BY_USER;
	Query query = getSession().createQuery(queryStr);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setString("searchString", searchString);
	if (!isAppadmin) {
	    query.setInteger("userId", userId);
	}

	return ((Number) query.uniqueResult()).intValue();
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
		sortByParam = "o.createDate";
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
	query.setCacheable(true);
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

    @Override
    public List<Organisation> getOrganisationsByCodes(Collection<String> codes) {
	return getSession().createQuery(GET_COURSES_BY_CODES, Organisation.class).setParameter("codes", codes)
		.setCacheable(true).getResultList();
    }
}