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

package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.usermanagement.FavoriteOrganisation;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.dao.IFavoriteOrganisationDAO;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation of IFavoriteOrganisationDAO
 *
 * @author Andrey Balan
 */
@Repository
public class FavoriteOrganisationDAO extends LAMSBaseDAO implements IFavoriteOrganisationDAO {
    
    @Override
    public FavoriteOrganisation getFavoriteOrganisation(Integer organisationId, Integer userId) {
	final String query = "SELECT fav from FavoriteOrganisation fav WHERE fav.organisation.organisationId=" + organisationId
		+ " AND fav.user.userId=" + userId;
	return (FavoriteOrganisation) getSession().createQuery(query).uniqueResult();
    }

    @Override
    public List<Organisation> getFavoriteOrganisationsByUser(Integer userId) {
	final String query = "SELECT fav.organisation from FavoriteOrganisation fav WHERE fav.user.userId=" + userId
		+ " AND fav.organisation.organisationState.organisationStateId = " + OrganisationState.ACTIVE
		+ " ORDER BY fav.organisation.name";
	return find(query);
    }
    
    @Override
    public boolean isOrganisationFavorite(Integer organisationId, Integer userId) {

	final String query = "SELECT COUNT(*) from FavoriteOrganisation fav WHERE fav.organisation.organisationId=" + organisationId
		+ " AND fav.user.userId=" + userId;
	return ((Number) getSession().createQuery(query).uniqueResult()).intValue() == 1;
    }
}