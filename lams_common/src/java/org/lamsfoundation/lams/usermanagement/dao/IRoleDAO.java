/*
 * Created on Nov 21, 2004
 *
 * Last modified on Nov 21, 2004
 */
package org.lamsfoundation.lams.usermanagement.dao;

import java.util.List;

import org.lamsfoundation.lams.usermanagement.Role;

/**
 * Role Data Access Object (DAO) interface.
 *
 * <p>
 * <a href="IRoleDAO.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public interface IRoleDAO {
	
    /**
     * Gets a list of all the roles.
     *
     * @return List populated list of roles
     */
    public List getAllRoles();

    /**
     * Gets role object based on roleId.
     * @param roleId the role's roleId
     * @return role populated role object
     */
    public Role getRoleById(Integer roleId);

    /**
     * Gets role object based on name.
     * @param name the role's name
     * @return role populated role object
     */
    public Role getRoleByName(String name);

    /**
     * Saves the role
     * @param role the object to be saved
     * @return Role the saved role object
     */
    public void saveRole(Role role);

    /**
     * Updates the role
     * @param role the object to be updated
     * @return Role the updated role object
     */
    public void updateRole(Role role);

    /**
     * Saves or updates the role
     * @param role the object to be saved or updated
     * @return Role the saved or updated role object
     */
    public void saveOrUpdateRole(Role role);

    /**
     * Deletes a role from the database
     * @param role the role to be deleted
     */
    public void deleteRole(Role role);

    /**
     * Deletes a role from the database by id
     * @param roleId the role's roleId
     */
    public void deleteRoleById(Integer roleId);

    /**
     * Deletes a role from the database by name
     * @param name the role's name
     */
    public void deleteRoleByName(String name);

}
