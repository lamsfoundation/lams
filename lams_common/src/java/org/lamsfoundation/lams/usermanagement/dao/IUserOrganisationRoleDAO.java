/*
 * Created on 2005-1-12
 *
 * Last modified on 2005-1-12
 */
package org.lamsfoundation.lams.usermanagement.dao;

import java.util.List;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;

/**
 * TODO Add description here
 *
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
