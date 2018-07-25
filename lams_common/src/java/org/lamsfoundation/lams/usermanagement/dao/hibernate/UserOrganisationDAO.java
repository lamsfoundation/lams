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
import org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO;
import org.springframework.stereotype.Repository;

/**
 * @author jliew
 */
@Repository
public class UserOrganisationDAO extends LAMSBaseDAO implements IUserOrganisationDAO {

    /*
     * Uses named query to retrieve list of userOrganisations to delete. Used by
     * ldap bulk update, which runs outside of OpenSessionInViewFilter's
     * hibernate session. This necessarily means that we can't delete these rows
     * using any lazy loading. Ideally the delete would be performed in one HQL
     * statement, but the HQL wouldn't work with UserOrganisation for some
     * reason (TODO). (non-Javadoc)
     *
     * @seeorg.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO#
     * userOrganisationsNotById(java.lang.Integer, java.lang.Integer)
     */
    @Override
    public List userOrganisationsNotById(final Integer userId, final Integer orgId) {
	Query query = getSession().getNamedQuery("userOrganisationsNotById");
	query.setInteger("userId", userId.intValue());
	query.setInteger("orgId", orgId.intValue());
	List result = query.list();
	return result;
    }
}
