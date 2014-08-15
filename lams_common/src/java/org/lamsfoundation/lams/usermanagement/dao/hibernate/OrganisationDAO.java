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

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
 
/**
 * @author jliew
 *
 */
public class OrganisationDAO extends BaseDAO implements IOrganisationDAO {
	
	private static final String GET_ALL_ACTIVE_COURSE_IDS = "select o.organisationId from Organisation o"
		+ " where o.organisationType.organisationTypeId = " + OrganisationType.COURSE_TYPE
		+ " and o.organisationState.organisationStateId = " + OrganisationState.ACTIVE
		+ " order by name";
	
	private static final String GET_ACTIVE_COURSE_IDS_BY_USER = "select uo.organisation.organisationId, uoc.collapsed"
		+ " from Organisation o, UserOrganisation uo left join uo.userOrganisationCollapsed uoc"
		+ " where uo.organisation.organisationId = o.organisationId"
		+ " and o.organisationType.organisationTypeId = " + OrganisationType.COURSE_TYPE
		+ " and o.organisationState.organisationStateId = " + OrganisationState.ACTIVE
		+ " and uo.user.userId = :userId"
		+ " order by name";
	
	private static final String GET_ALL_ARCHIVED_COURSE_IDS = "select distinct o1.organisationId"
		+ " from Organisation o1, Organisation o2 "
		+ " where (o1.organisationType.organisationTypeId = " + OrganisationType.COURSE_TYPE
		+ " and o1.organisationState.organisationStateId = " + OrganisationState.ACTIVE
		+ " and o2.organisationType.organisationTypeId = " + OrganisationType.CLASS_TYPE
		+ " and o2.organisationState.organisationStateId = " + OrganisationState.ARCHIVED
		+ " and o1.organisationId = o2.parentOrganisation.organisationId)"
		+ " or (o1.organisationType.organisationTypeId = " + OrganisationType.COURSE_TYPE
		+ " and o1.organisationState.organisationStateId = " + OrganisationState.ARCHIVED + ")"
		+ " order by o1.name";
	
	private static final String GET_ARCHIVED_COURSE_IDS_BY_USER = "select distinct o1.organisationId, uoc.collapsed"
		+ " from UserOrganisation uo1, Organisation o1, Organisation o2 left join uo1.userOrganisationCollapsed uoc"
		+ " where (uo1.user.userId = :userId"
		+ " and uo1.organisation.organisationId = o1.organisationId"
		+ " and o1.organisationType.organisationTypeId = " + OrganisationType.COURSE_TYPE
		+ " and o1.organisationState.organisationStateId = " + OrganisationState.ACTIVE
		+ " and o2.organisationType.organisationTypeId = " + OrganisationType.CLASS_TYPE
		+ " and o2.organisationState.organisationStateId = " + OrganisationState.ARCHIVED
		+ " and o1.organisationId = o2.parentOrganisation.organisationId)"
		+ " or (uo1.organisation.organisationId = o1.organisationId"
		+ " and o1.organisationType.organisationTypeId = " + OrganisationType.COURSE_TYPE
		+ " and o1.organisationState.organisationStateId = " + OrganisationState.ARCHIVED
		+ " and uo1.user.userId = :userId)"
		+ " order by o1.name";
	
	public List getActiveCourseIdsByUser(final Integer userId, final boolean isSysadmin) {
		
		HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

        return (List)hibernateTemplate.execute(
             new HibernateCallback() 
             {
                 public Object doInHibernate(Session session) throws HibernateException 
                 {
                	 return (isSysadmin 
                			 ? session.createQuery(GET_ALL_ACTIVE_COURSE_IDS).list() 
                			 : session.createQuery(GET_ACTIVE_COURSE_IDS_BY_USER).setInteger("userId",userId).list()
                	 );
                 }
             }
       );
	}
	
	public List getArchivedCourseIdsByUser(final Integer userId, final boolean isSysadmin) {
		
		HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

        return (List)hibernateTemplate.execute(
             new HibernateCallback() 
             {
                 public Object doInHibernate(Session session) throws HibernateException 
                 {
                	 return (isSysadmin 
                			 ? session.createQuery(GET_ALL_ARCHIVED_COURSE_IDS).list() 
                			 : session.createQuery(GET_ARCHIVED_COURSE_IDS_BY_USER).setInteger("userId",userId).list()
                	 );
                 }
             }
       );
	}

}
 