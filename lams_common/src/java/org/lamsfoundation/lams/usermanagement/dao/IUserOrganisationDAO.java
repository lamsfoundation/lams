/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.usermanagement.dao;

import java.util.List;

import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.Organisation;

/**
 * UserOrganisation Data Access Object (DAO) interface.
 *
 * <p>
 * <a href="IUserOrganisationDAO.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public interface IUserOrganisationDAO {
    /**
     * Gets a list of all the userOrganisations.
     *
     * @return List populated list of userOrganisations
     */
    public List getAllUserOrganisations();

    /**
     * Gets userOrganisation object based on userOrganisationId.
     * @param userOrganisationId the userOrganisation's userOrganisationId
     * @return userOrganisation populated userOrganisation object
     */
    public UserOrganisation getUserOrganisationById(Integer userOrganisationId);

    /**
     * Gets userOrganisation object based on userId and organisationId.
     * @param userId the userOrganisation's userId
     * @param organisationId the userOrganisation's organisationId
     * @return userOrganisation populated userOrganisation object
     */
    public UserOrganisation getUserOrganisation(Integer userId, Integer orgnisationId);
    
    /**
     * Gets userOrganisation objects based on user.
     * @param user the userOrganisation's user
     * @return List populated list of userOrganisations
     */
    public List getUserOrganisationsByUser(User user);

    /**
     * Gets userOrganisation objects based on organisation.
     * @param organisation the userOrganisation's organisation
     * @return List populated list of userOrganisations
     */
    public List getUserOrganisationsByOrganisation(Organisation organisation);

    /**
     * Gets userOrganisation objects based on organisationId.
     * @param organisationId the userOrganisation's organisationId
     * @return List populated list of userOrganisations
     */
    public List getUserOrganisationsByOrganisationId(Integer organisationId);
    
    /**
     * Saves the userOrganisation
     * @param userOrganisation the object to be saved
     * @return UserOrganisation the saved userOrganisation object
     */
    public void saveUserOrganisation(UserOrganisation userOrganisation);

    /**
     * Updates the userOrganisation
     * @param userOrganisation the object to be updated
     * @return UserOrganisation the updated userOrganisation object
     */
    public void updateUserOrganisation(UserOrganisation userOrganisation);

    /**
     * Saves or updates the userOrganisation
     * @param userOrganisation the object to be saved or updated
     * @return UserOrganisation the saved or updated userOrganisation object
     */
    public void saveOrUpdateUserOrganisation(UserOrganisation userOrganisation);

    /**
     * Deletes a userOrganisation from the database
     * @param userOrganisation the userOrganisation to be deleted
     */
    public void deleteUserOrganisation(UserOrganisation userOrganisation);

    /**
     * Deletes a userOrganisation from the database by id
     * @param userOrganisationId the userOrganisation's userOrganisationId
     */
    public void deleteUserOrganisationById(Integer userOrganisationId);

}
