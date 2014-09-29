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
package org.lamsfoundation.lams.usermanagement.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.usermanagement.ForgotPasswordRequest;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationGroup;
import org.lamsfoundation.lams.usermanagement.OrganisationGrouping;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserManageBean;

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
	 * @param clazz
	 * @param properties
	 * @return a list of objects which have similar property values
	 */
	public List searchByStringProperties(Class clazz, Map<String,String> properties);
	
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
	 * @param isFlashCall
	 * @param getUser
	 * @return UserFlashDTO objects (in a Vector to suit WDDX)
	 */
	public Vector getUsersFromOrganisationByRole(Integer organisationID, String roleName, boolean isFlashCall, boolean getUser);	

	public Organisation getRootOrganisation();
	
	/**
	 * @param userId
	 * @param orgId
	 * @param roleName
	 * @return true or false
	 */
	public boolean isUserInRole(Integer userId,Integer orgId, String roleName);
	
	/**
	 * @param typeId
	 * @param stateId
	 * @return a list of organisations
	 */
	public List getOrganisationsByTypeAndStatus(Integer typeId, Integer stateId);
	
	/**
	 * @param orgId
	 * @param login user's login
	 * @return a list of UserOrganisationRoles
	 */
	public List getUserOrganisationRoles(Integer orgId, String login);
	
	/**
	 * @param login
	 * @param typeId
	 * @param stateId
	 * @return a list of UserOrganisations
	 */
	public List getUserOrganisationsForUserByTypeAndStatus(String login, Integer typeId, Integer stateId);

	/**
	 * 
	 * @param login
	 * @param typeId
	 * @param stateId
	 * @param parentOrgId
	 * @return a list of UserOrganisations
	 */
	public List getUserOrganisationsForUserByTypeAndStatusAndParent(String login, Integer typeId, Integer stateId, Integer parentOrgId);
	
	/**
	 * @param login
	 * @return the User
	 */
	public User getUserByLogin(String login);
	
	
	/**
	 * @param login user's login
	 * @param password new password
	 * @return void
	 */
	public void updatePassword(String login, String password);
	
	/**
	 * @param userId
	 * @param orgId
	 * @return UserOrganisation
	 */
	public UserOrganisation getUserOrganisation(Integer userId, Integer orgId);
	
	 /** 	 
     * Save an organisation. If it is a new course organisation then it will create a workspace and 	 
     * its workspace folders. 	 
     * 	 
     * @param organisation 	 
     * @param userID 	 
     * @return 	 
     */ 	 
    public Organisation saveOrganisation( Organisation organisation, Integer userID );
    
    /**
     * Update and organisation's name, used for integrations.
     * It also updates the workspace name and workspace folders
     * @param organisation
     */
    public void updateOrganisationandWorkspaceNames(Organisation organisation);
    
    public List<UserManageBean> getUserManageBeans(Integer orgId);
    
	/** Remove a user from the system completely. Only able to be done if they don't have any 
	 * related learning designs, etc.
	 * @param userId
	 */
	public void removeUser(Integer userId) throws Exception;
	
	/** 
	 * Determines whether a user has associated learning designs, progress data, etc.
	 * @param user
	 * @return Boolean
	 */
	public Boolean userHasData(User user);
	
	/** 
	 * Sets user's disabled flag to true, and removes their organisation memberships.
	 * @param userId
	 */
	public void disableUser(Integer userId);

	/**
	 * Set the roles for the specified user and organisation using the roleIds in rolesList.
	 * If userOrganisation exists, will also remove roles that are not in rolesList.
	 * @param user
	 * @param orgId
	 * @param rolesList
	 */
	public void setRolesForUserOrganisation(User user, Integer organisationId, List<String> rolesList);
	
	/**
	 * Returns list of roles suitable for the given orgType.
	 * @param rolelist
	 * @param isSysadmin
	 * @param orgType
	 * @return
	 */
	public List<Role> filterRoles(List<Role> rolelist, Boolean isSysadmin, OrganisationType orgType);
	

	public boolean hasRoleInOrganisation(User user,  Integer roleId);
	
	/**
	 * Returns true if user has the role in the given organisation. Otherwise false.
	 * @param user
	 * @param roleId
	 * @param organisation
	 * @return
	 */
	public boolean hasRoleInOrganisation(User user,  Integer roleId, Organisation organisation);
	
	/**
	 * Given a user and a group, removes user from subgroups.
	 * @param user
	 * @param org
	 */
	public void deleteChildUserOrganisations(User user, Organisation org);
	
	/**
	 * Removes user from this group and its subgroups.
	 * @param user
	 * @param org
	 */
	public void deleteUserOrganisation(User user, Organisation org);
	
	/**
	 * Return true if user is a global group admin.
	 * @return
	 */
	public boolean isUserGlobalGroupAdmin();
	
	/**
	 * Return true if user has sysadmin role in root organisation.
	 * @return
	 */
	public boolean isUserSysAdmin();
	
	/**
	 * Count the number of unique (distinct) users with a particular role in the system. 
	 * @param roleId	Role ID
	 * @return
	 */
	public Integer getCountRoleForSystem(Integer roleId);
	
	/**
	 * Count then umber of users with a particular role in a given org.
	 * @param orgId
	 * @param roleId
	 * @return
	 */
	public Integer getCountRoleForOrg(Integer orgId, Integer roleId);
	
	/**
	 * Get default flash theme of server.
	 * @return default flash theme object
	 */
	public Theme getDefaultFlashTheme();
	
	/**
	 * Get default html theme of server.
	 * @return default html theme object
	 */
	public Theme getDefaultHtmlTheme();
	
	public void auditPasswordChanged(User user, String moduleName);
	
	public void auditUserCreated(User user, String moduleName);
	
	public Integer getCountUsers();
	
	public Integer getCountUsers(Integer authenticationMethodId);
	
	public List getActiveCourseIdsByUser(Integer userId, boolean isSysadmin);
	
	public List getArchivedCourseIdsByUser(Integer userId, boolean isSysadmin);
	
	/**
	 * Search users across login, first name, last name and email fields using the search term.
	 * Filters out disabled users.
	 * @param term
	 * @return list of Users
	 */
	public List searchUserSingleTerm(String term);
	
	/**
	 * Search users across login, first name, last name and email fields using the search term.
	 * Filters out disabled users, and users that are members of filteredOrg.
	 * @param term
	 * @param filteredOrgId
	 * @return list of Users
	 */
	public List searchUserSingleTerm(String term, Integer filteredOrgId);
	
	
	/**
	 * Search users across login, first name, last name and email fields using the search term.
	 * Filters out disabled users.  Optionally include child organisations in the results.
	 * @param term
	 * @param OrgId
	 * @param includeChildOrgs
	 * @return list of Users
	 */
	public List searchUserSingleTerm(String term, Integer orgId, boolean includeChildOrgs);
	
	/**
	 * Search user members in orgId across login, first name, last name and email fields using the search term.
	 * Filters out disabled users, and users that are members of filteredOrg.
	 * @param term
	 * @param orgId
	 * @param filteredOrgId
	 * @return list of Users
	 */
	public List searchUserSingleTerm(String term, Integer orgId, Integer filteredOrgId);
	
	/**
	 * Get all users, except for disabled users.
	 * @return list of Users
	 */
	public List getAllUsers();
	
	/**
	 * Get all users, except for disabled users and users that are members of filteredOrg.
	 * @param optionalOrgId
	 * @return list of Users
	 */
	public List getAllUsers(Integer filteredOrgId);
	
	/**
	 * Get all users with the given Email. Email must not be empty
	 * Use a regex check to make sure the string is in correct xxx@xxx.xxx form, 
	 * see lams-central/web/forgotPassword.jsp javascript methods
	 * * @param email a non empty email string
	 * @return list of Users
	 */
	public List getAllUsersWithEmail(String email);
	
	/**
	 * Get all users in orgId, except for users that are members of filteredOrg.
	 * @param orgId
	 * @param filteredOrgId
	 * @return list of Users
	 */
	public List getUsersFromOrganisation(Integer orgId, Integer filteredOrgId);
	
	/**
	 * Tests whether user can edit the given org's details.
	 * @param userId
	 * @param orgId
	 * @return boolean
	 */
	public boolean canEditGroup(Integer userId, Integer orgId);
	
	/**
	 * Gets a forgot password request from the db with the given key
	 * @param key
	 * @return
	 */
	public ForgotPasswordRequest getForgotPasswordRequest(String key);
	
	/**
	 * Remove given user from groups other than the specified one.  Doesn't remove
	 * user from subgroups of the specified group if any.
	 * @param userId
	 * @param orgId
	 * @return number of deleted rows.
	 */
	public int removeUserFromOtherGroups(Integer userId, Integer orgId);
	
	
	/**
	 * Returns the user dto for the given openidURL if one exists
	 * used for sif openid login
	 * 
	 * @param openidURL
	 * @return
	 */
	public User getUserDTOByOpenidURL(String openidURL);
	
	/**
	 * Stores organisation (course) groups and removes the unnecessary ones.
	 */
	public void saveOrganisationGrouping(OrganisationGrouping grouping, Collection<OrganisationGroup> newGroups);
}