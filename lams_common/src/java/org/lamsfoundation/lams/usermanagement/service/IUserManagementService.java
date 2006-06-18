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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.usermanagement.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * @version
 *
 * <p>
 * <a href="IUserManagementService.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 * Created at 11:53:53 on 2006-6-2
 */
public interface IUserManagementService {


    /**
     * Retrieves a tree of organisations for a user. The top of the tree is a "dummy" root
     * organisation, just so that we have a real tree. This makes life easier for Flash.
     * 
     * If restrictToRoleNames contains any role names (ie not null and size > 0 )
     * then it will restrict the organisations to those in which the user has one of the
     * given roles. If restrictToRoleNames is null/empty then till return all organisations
     * to which the user belongs. 
     * 
	 * @param user mandatory
     * @param restrictToRoleNames role names to which to restrict the user
     * @return List of organisationDTOs
     */
    public OrganisationDTO getOrganisationsForUserByRole(User user, List<String> restrictToRoleNames);
    
	/**
	 * Retrieves a tree of child of organisations in which the user has the specified role.
	 * If courseID only is set, then return course organisationDTO and its children as its nodes 
	 * If courseID && restrictToClassIds are set, then course organisationDTO and its the nominated class 
	 * organisationDTOs - most cases will have only single classID
	 * 
     * If restrictToRoleNames contains any role names (ie not null and size > 0 )
     * then it will restrict the organisations to those in which the user has one of the
     * given roles.  
     * 
	 * @param user mandatory
	 * @param roles optional
	 * @param courseID mandatory
	 * @param restrictToClassIds optional
	 * @return organisationDTO hierarchy, in WDDX format. 
	 */
	public OrganisationDTO getOrganisationsForUserByRole(User user, List<String> restrictToRoleNames, Integer courseId, List<Integer> restrictToClassIds);

	/**
     * Gets an organisation for a user, with the user's roles. Doesn't not return a tree of organisations 
	 */
	public OrganisationDTO getOrganisationForUserWithRole(User user, Integer organisationId);

	/**
     * Retrieves roles in which the user 
     * has the specified role 
     * 
     * @param user the user
     * @param orgId organisation's id
     * @return List of roles
     */
    public List<Role> getRolesForUserByOrganisation(User user, Integer orgId);

	/**
     * Retrieves users from the specified organisation
     * 
     * @param orgId organisation's id
     * @return List of users
     */
    public List<User> getUsersFromOrganisation(Integer orgId);
    
	/**
	 * This method returns the users in the Organisation with
	 * given <code> organisationID</code> and <code>roleName</code>
	 * 
	 * @param organisationID
	 * @param roleName
	 * @return UserDTO objects (in a Vector to suit WDDX)
	 */
	public Vector<UserDTO> getUsersFromOrganisationByRole(Integer organisationID, String roleName);	

	/** Get all the lessons in an organisation that this user can monitor.
	 * This is a temporary method to support the dummy index page. 
	 * TODO modify/remove when the index page is implemented properly
	 */
	public List getMonitorLessonsFromOrganisation(Integer userID, Integer organisationID);
	/** Get all the lessons in an organisation for which this user is a learner.
	 * This is a temporary method to support the dummy index page. 
	 * TODO modify/remove when the index page is implemented properly
	 */
	public List getLearnerLessonsFromOrganisation(Integer userID, Integer organisationID);
	
	/**
	 * save(insert or update)
	 * @param object The object to be inserted
	 */
	public void save(Object object);

	/**
	 * @param objects
	 * @return void
	 */
	public void saveAll(Collection objects);

	/**
	 * Remove an object from the database.
	 * @param object The object to be deleted
	 */
	public void delete(Object object);
	
	
	/**
	 * Be careful to use this method. It will clean up the whole table for the Class
	 * @param clazz 
	 * @return void
	 */
	public void deleteAll(Class clazz);
	
	/**
	 * @param objects to delete
	 * @return void
	 */
	public void deleteAll(Collection objects);
	
	/**
	 * @param clazz  java Class
	 * @param id  identifier
	 * @return void
	 */
	public void deleteById(Class clazz, Serializable id);
	
	/**
	 * @param clazz
	 * @param name
	 * @param value
	 * @return void
	 */
	public void deleteByProperty(Class clazz, String name, Object value);
	
	/**
	 * @param properties  a map of property names and values
	 * @return void
	 */
	public void deleteByProperties(Class clazz, Map<String,Object> properties);
	
	/**
	 * Delete any object which has the same non-null property values as the object 
	 * @param object
	 * @return void
	 */
	public void deleteAnythingLike(Object object);
	
	/**
	 * Find an object. If the object is not found 
	 * then it will return null
	 * @param clazz
	 * @param id
	 */
	public Object findById(Class clazz, Serializable id);
	
	/**
	 * @param clazz
	 * @return all of rows in the table for the Class as objects
	 */
	public List findAll(Class clazz);
	
	/**
	 * @param clazz
	 * @param name
	 * @param value
	 * @return a list of objects which have the same propery value
	 */
	public List findByProperty(Class clazz, String name, Object value);
	
	/**
	 * @param properties  a map of property names and values
	 * @return a list of objects which have the same property values
	 */
	public List findByProperties(Class clazz, Map<String,Object> properties);
	
	/**
	 * Find any object which has the same non-null property values as the object 
	 * @param object
	 * @return a list of objects which has the same non-null property values as the object
	 */
	public List findAnythingLike(Object object);
	
	/**
	 * @param userId
	 * @param orgId
	 * @param roleName
	 * @return true or false
	 */
	public boolean isUserInRole(Integer userId,Integer orgId, String roleName);
	
	public List getOrganisationsByTypeAndStatus(Integer typeId, Integer stateId);
	
	public List getUserOrganisationRoles(Integer orgId, String login);
	
	public List getUserOrganisationsForUserByTypeAndStatus(String login, Integer typeId, Integer stateId);
	
	public User getUserByLogin(String login);
	
	public void updatePassword(String login, String password);
	
	public Organisation getOrganisationById(Integer orgId);
	
	public List getOrganisationsByType(Integer typeId);
	
	public UserOrganisation getUserOrganisation(Integer userId, Integer orgId);
	
}
