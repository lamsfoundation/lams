/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
package org.lamsfoundation.lams.usermanagement.dao;

import java.util.List;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;

/**
 * <p>
 * <a href="IUserOrganisationRoleDAO.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public interface IUserOrganisationRoleDAO {

    /**
     * Gets userOrganisationRole object based on userOrganisationRoleId.
     * @param userOrganisationRoleId the userOrganisationRole's userOrganisationRoleId
     * @return userOrganisationRole populated userOrganisationRole object
     */
    public UserOrganisationRole getUserOrganisationRoleById(Integer userOrganisationRoleId);

    /**
     * Gets userOrganisationRole object based on userId and organisationId.
     * @param userOrganisationId the userOrganisationRole's userOrganisationId
     * @param roleId the userOrganisationRole's roleId
     * @return userOrganisationRole populated userOrganisationRole object
     */
    public UserOrganisationRole getUserOrganisationRole(Integer userOrganisationId, Integer roleId);

    /**
     * Gets a list of userOrganisationRole objects based on userOrganisationId.
     * @param userOrganisationId the userOrganisationRole's userOrganisationId
     * @return list of userOrganisationRole objects
     */
    public List getUserOrganisationRoles(Integer userOrganisationId);
    
    /**
     * Saves the userOrganisationRole
     * @param userOrganisationRole the object to be saved
     * @return UserOrganisationRole the saved userOrganisationRole object
     */
    public void saveUserOrganisationRole(UserOrganisationRole userOrganisationRole);

    /**
     * Updates the userOrganisationRole
     * @param userOrganisationRole the object to be updated
     * @return UserOrganisationRole the updated userOrganisationRole object
     */
    public void updateUserOrganisationRole(UserOrganisationRole userOrganisationRole);

    /**
     * Saves or updates the userOrganisationRole
     * @param userOrganisationRole the object to be saved or updated
     * @return UserOrganisationRole the saved or updated userOrganisationRole object
     */
    public void saveOrUpdateUserOrganisationRole(UserOrganisationRole userOrganisationRole);

    /**
     * Deletes a userOrganisationRole from the database
     * @param userOrganisationRole the userOrganisationRole to be deleted
     */
    public void deleteUserOrganisationRole(UserOrganisationRole userOrganisationRole);

    /**
     * Deletes a userOrganisationRole from the database by id
     * @param userOrganisationRoleId the userOrganisationRole's userOrganisationRoleId
     */
    public void deleteUserOrganisationRoleById(Integer userOrganisationRoleId);
	
}
