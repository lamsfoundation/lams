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
	    + " from Organisation o, UserOrganisation uo left join uo.userOrganisationCollapsed uoc"
	    + " where uo.organisation.organisationId = o.organisationId"
	    + " and o.organisationType.organisationTypeId = " + OrganisationType.COURSE_TYPE
	    + " and o.organisationState.organisationStateId = " + OrganisationState.ACTIVE
	    + " and uo.user.userId = :userId" + " order by name";

    private static final String GET_ALL_ARCHIVED_COURSE_IDS = "select distinct o1.organisationId"
	    + " from Organisation o1, Organisation o2 " + " where (o1.organisationType.organisationTypeId = "
	    + OrganisationType.COURSE_TYPE + " and o1.organisationState.organisationStateId = "
	    + OrganisationState.ACTIVE + " and o2.organisationType.organisationTypeId = " + OrganisationType.CLASS_TYPE
	    + " and o2.organisationState.organisationStateId = " + OrganisationState.ARCHIVED
	    + " and o1.organisationId = o2.parentOrganisation.organisationId)"
	    + " or (o1.organisationType.organisationTypeId = " + OrganisationType.COURSE_TYPE
	    + " and o1.organisationState.organisationStateId = " + OrganisationState.ARCHIVED + ")"
	    + " order by o1.name";

    private static final String GET_ARCHIVED_COURSE_IDS_BY_USER = "select distinct o1.organisationId, uoc.collapsed"
	    + " from UserOrganisation uo1, Organisation o1, Organisation o2 left join uo1.userOrganisationCollapsed uoc"
	    + " where (uo1.user.userId = :userId" + " and uo1.organisation.organisationId = o1.organisationId"
	    + " and o1.organisationType.organisationTypeId = " + OrganisationType.COURSE_TYPE
	    + " and o1.organisationState.organisationStateId = " + OrganisationState.ACTIVE
	    + " and o2.organisationType.organisationTypeId = " + OrganisationType.CLASS_TYPE
	    + " and o2.organisationState.organisationStateId = " + OrganisationState.ARCHIVED
	    + " and o1.organisationId = o2.parentOrganisation.organisationId)"
	    + " or (uo1.organisation.organisationId = o1.organisationId"
	    + " and o1.organisationType.organisationTypeId = " + OrganisationType.COURSE_TYPE
	    + " and o1.organisationState.organisationStateId = " + OrganisationState.ARCHIVED
	    + " and uo1.user.userId = :userId)" + " order by o1.name";

    public List getActiveCourseIdsByUser(final Integer userId, final boolean isSysadmin) {

	return (List) (isSysadmin ? getSession().createQuery(GET_ALL_ACTIVE_COURSE_IDS).list() : getSession()
		.createQuery(GET_ACTIVE_COURSE_IDS_BY_USER).setInteger("userId", userId).list());
    }

    public List getArchivedCourseIdsByUser(final Integer userId, final boolean isSysadmin) {

	return (List) (isSysadmin ? getSession().createQuery(GET_ALL_ARCHIVED_COURSE_IDS).list() : getSession()
		.createQuery(GET_ARCHIVED_COURSE_IDS_BY_USER).setInteger("userId", userId).list());
    }
    @Override
    public List<Organisation> getPagedCourses(final Integer parentOrgId, final Integer typeId, final Integer stateId,
	    int page, int size, String sortBy, String sortOrder, String searchString) {
	String GET_ORGS = "SELECT o FROM " + Organisation.class.getName() + " o "
		+ " WHERE o.organisationType.organisationTypeId =:typeId "
		+ " AND o.organisationState.organisationStateId =:stateId "
		+ " AND o.parentOrganisation.organisationId =:parentOrgId"
		+ " AND (o.name LIKE CONCAT('%', :searchString, '%')) "
		+ " ORDER BY ";
	
	String sortByParam = "o.name";
	if (sortBy == null) {
	    sortByParam = "o.name";
	    
	} else if (sortBy.equals("id")) {
	    sortByParam = "o.organisationId";
	    
	} else if (sortBy.equals("name")) {
	    sortByParam = "o.name";
	    
	} else if (sortBy.equals("code")) {
	    sortByParam = "o.code";
	    
	} else if (sortBy.equals("description")) {
	    sortByParam = "o.description";
	}
	
	Query query = getSession().createQuery(GET_ORGS + sortByParam + " " +sortOrder);
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
    public int getCountCoursesByParentCourseAndTypeAndState(final Integer parentOrgId, final Integer typeId,
	    final Integer stateId, String searchString) {
	final String GET_ORGS = "SELECT count(*) FROM Organisation o "
		+ " WHERE o.parentOrganisation.organisationId =:parentOrgId "
		+ " AND o.organisationType.organisationTypeId =:typeId "
		+ " AND o.organisationState.organisationStateId =:stateId "
		+ " AND (o.name LIKE CONCAT('%', :searchString, '%')) ";

	Query query = getSession().createQuery(GET_ORGS);
	query.setInteger("parentOrgId", parentOrgId);
	query.setInteger("typeId", typeId);
	query.setInteger("stateId", stateId);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setString("searchString", searchString);
	
	List list = query.list();
	if (list == null || list.size() == 0) {
	    return 0;
	} else {
	    return ((Number) list.get(0)).intValue();
	}
    }

}
