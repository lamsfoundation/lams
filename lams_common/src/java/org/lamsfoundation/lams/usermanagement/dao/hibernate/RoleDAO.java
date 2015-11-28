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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.dao.IRoleDAO;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * Hibernate implementation of IRoleDAO
 * 
 * @author chris
 */
public class RoleDAO extends BaseDAO implements IRoleDAO {
    private final static String LOAD_USER_BY_ORG_AND_ROLE = "select u "
	    + "from User u, UserOrganisation uo, UserOrganisationRole uor " + "where u.id = :userId and "
	    + "u.id = uo.user.id and " + "uo.organisation = :org and " + "uor.userOrganisation.id = uo.id and "
	    + "uor.role.id = :roleId";

    private final static String COUNT_ROLE = "select count(distinct userOrganisationRole.userOrganisation.user)"
	    + " from " + UserOrganisationRole.class.getName() + " userOrganisationRole"
	    + " where userOrganisationRole.role.roleId = :roleId";

    private final static String COUNT_ROLE_FOR_ORG = "select count(distinct uor.userOrganisation.user) from "
	    + UserOrganisationRole.class.getName() + " uor where uor.role.roleId = :roleId"
	    + " and uor.userOrganisation.organisation.organisationId = :orgId";

    @Override
    public User getUserByOrganisationAndRole(final Integer userId, final Integer roleId,
	    final Organisation organisation) {
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

	return (User) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		return session.createQuery(RoleDAO.LOAD_USER_BY_ORG_AND_ROLE).setInteger("userId", userId)
			.setEntity("org", organisation).setInteger("roleId", roleId).uniqueResult();
	    }
	});
    }

    @Override
    public Integer getCountRoleForSystem(final Integer roleId) {
	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

	return (Integer) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Query query = session.createQuery(RoleDAO.COUNT_ROLE);
		query.setInteger("roleId", roleId.intValue());
		Object value = query.uniqueResult();
		return new Integer(((Number) value).intValue());
	    }
	});

    }

    @Override
    public Integer getCountRoleForOrg(final Integer roleId, final Integer orgId, String searchPhrase) {
	StringBuilder queryTextBuilder = new StringBuilder(RoleDAO.COUNT_ROLE_FOR_ORG);
	if (!StringUtils.isBlank(searchPhrase)) {
	    String[] tokens = searchPhrase.trim().split("\\s+");
	    for (String token : tokens) {
		queryTextBuilder.append(" AND (uor.userOrganisation.user.firstName LIKE '%").append(token)
			.append("%' OR uor.userOrganisation.user.lastName LIKE '%").append(token)
			.append("%' OR uor.userOrganisation.user.login LIKE '%").append(token).append("%')");
	    }
	}
	final String queryText = queryTextBuilder.toString();

	HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	return (Integer) hibernateTemplate.execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		Query query = session.createQuery(queryText);
		query.setInteger("roleId", roleId.intValue());
		query.setInteger("orgId", orgId.intValue());
		Object value = query.uniqueResult();
		return new Integer(((Number) value).intValue());
	    }
	});
    }

}
