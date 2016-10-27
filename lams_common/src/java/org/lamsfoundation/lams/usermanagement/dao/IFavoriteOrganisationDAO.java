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


package org.lamsfoundation.lams.usermanagement.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.usermanagement.FavoriteOrganisation;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * DAO methods for FavoriteOrganisation class.
 * 
 * @author Andrey Balan
 */
public interface IFavoriteOrganisationDAO extends IBaseDAO {
    
    /**
     * Returns FavoriteOrganisation object for given user and organisation.
     * 
     * @param organisationId
     * @param userId
     * @return
     */
    FavoriteOrganisation getFavoriteOrganisation(Integer organisationId, Integer userId);

    /**
     * Return all organisations that were marked as favorite by the specified user
     * 
     * @param userId
     * @return
     */
    List<Organisation> getFavoriteOrganisationsByUser(Integer userId);
    
    /**
     * Checks whether user marked this organisation as favorite.
     * 
     * @param organisationId
     * @param userId
     * @return
     */
    boolean isOrganisationFavorite(Integer organisationId, Integer userId);
}
