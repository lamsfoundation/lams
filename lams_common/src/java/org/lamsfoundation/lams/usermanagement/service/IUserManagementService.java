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

package org.lamsfoundation.lams.usermanagement.service;

import java.io.IOException;
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
import org.lamsfoundation.lams.usermanagement.UserOrganisationCollapsed;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserManageBean;

/**
 * <p>
 * <a href="IUserManagementService.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public interface IUserManagementService {

    /**
     * save(insert or update)
     *
     * @param object
     *            The object to be inserted
     */
    void save(Object object);

    User saveUser(User user);

    /**
     * Remove an object from the database.
     *
     * @param object
     *            The object to be deleted
     */
    void delete(Object object);

    /**
     * @param objects
     *            to delete
     * @return void
     */
    void deleteAll(Collection objects);

    /**
     * @param clazz
     *            java Class
     * @param id
     *            identifier
     * @return void
     */
    void deleteById(Class clazz, Serializable id);

    /**
     * Find an object. If the object is not found then it will return null
     *
     * @param clazz
     * @param id
     */
    Object findById(Class clazz, Serializable id);

    /**
     * @param clazz
     * @return all of rows in the table for the Class as objects
     */
    List findAll(Class clazz);

    /**
     * @param clazz
     * @param name
     * @param value
     * @return a list of objects which have the same propery value
     */
    List findByProperty(Class clazz, String name, Object value);

    List findByProperty(Class clazz, String name, Object value, boolean cache);

    <T> List<T> findByPropertyValues(Class<T> clazz, String name, Collection<?> values);

    /**
     * @param properties
     *            a map of property names and values
     * @return a list of objects which have the same property values
     */
    List findByProperties(Class clazz, Map<String, Object> properties);

    List findByProperties(Class clazz, Map<String, Object> properties, boolean cache);

    /**
     * Retrieves users from the specified organisation
     *
     * @param orgId
     *            organisation's id
     * @return List of users
     */
    List<User> getUsersFromOrganisation(Integer orgId);

    /**
     * This method returns the users in the Organisation with given <code> organisationID</code> and
     * <code>roleName</code>
     *
     * @param organisationID
     * @param roleName
     * @param getUser
     * @return UserBasicDTO objectsin a Vector
     */
    Vector getUsersFromOrganisationByRole(Integer organisationID, String roleName, boolean getUser);

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

    /**
     * Toggles whether organisation is marked as favorite
     *
     * @param organisation
     * @param user
     *            user performing toggling
     */
    void toggleOrganisationFavorite(Integer orgId, Integer userId);

    Organisation getRootOrganisation();

    /**
     * @param userId
     * @param orgId
     * @param roleName
     * @return true or false
     */
    boolean isUserInRole(Integer userId, Integer orgId, String roleName);

    /**
     * @param organisationId
     * @return organisation by its id
     */
    Organisation getOrganisationById(Integer organisationId);

    /**
     * @param typeId
     * @param stateId
     * @return a list of organisations
     */
    List getOrganisationsByTypeAndStatus(Integer typeId, Integer stateId);

    /**
     * Returns courses with specified type, state and parent course.
     *
     * @param parentOrgId
     * @param typeId
     * @param stateId
     * @param page
     * @param size
     * @param sortBy
     * @param sortOrder
     * @param searchString
     *            filters results by course name. It can be null and then doesn't affect results
     * @return
     */
    List<Organisation> getPagedCourses(final Integer parentOrgId, final Integer typeId, final Integer stateId, int page,
	    int size, String sortBy, String sortOrder, String searchString);

    /**
     * Counts courses with specified type, state and parent course.
     *
     * @param parentOrgId
     * @param typeId
     * @param stateId
     * @param searchString
     *            filters results by course name. It can be null and then doesn't affect results
     * @return
     */
    int getCountCoursesByParentCourseAndTypeAndState(final Integer parentOrgId, final Integer typeId,
	    final Integer stateId, String searchString);

    /**
     * @param orgId
     * @param login
     *            user's login
     * @return a list of UserOrganisationRoles
     */
    List<UserOrganisationRole> getUserOrganisationRoles(Integer orgId, String login);

    /**
     * Returns list of UserOrganisationCollapsed, indicating which sub-courses was collapsed by the given user.
     *
     * @param parentOrganisationId
     * @param userId
     * @return
     */
    List<UserOrganisationCollapsed> getChildOrganisationsCollapsedByUser(Integer parentOrganisationId, Integer userId);

    /**
     * Returns mapping org ID -> role IDs
     */
    Map<Integer, Set<Integer>> getRolesForUser(Integer userId);

    /**
     * @param login
     * @param typeId
     * @param stateId
     * @return a list of UserOrganisations
     */
    List getUserOrganisationsForUserByTypeAndStatus(String login, Integer typeId, Integer stateId);

    /**
     *
     * @param login
     * @param typeId
     * @param stateId
     * @param parentOrgId
     * @return a list of UserOrganisations
     */
    List getUserOrganisationsForUserByTypeAndStatusAndParent(String login, Integer typeId, Integer stateId,
	    Integer parentOrgId);

    /**
     * @param login
     * @return the User
     */
    User getUserByLogin(String login);

    User getUserById(Integer userId);

    void updatePassword(User user, String password);

    /**
     * @param userId
     * @param orgId
     * @return UserOrganisation
     */
    UserOrganisation getUserOrganisation(Integer userId, Integer orgId);

    /**
     * @param userId
     * @param orgId
     * @return
     */
    UserOrganisationCollapsed getUserOrganisationCollapsed(Integer userId, Integer orgId);

    /**
     * Save an organisation. If it is a new course organisation then it will create a workspace and its workspace
     * folders.
     *
     * @param organisation
     * @param userID
     * @return
     */
    Organisation saveOrganisation(Organisation organisation, Integer userID);

    /**
     * Update and organisation's name, used for integrations. It also updates the workspace name and workspace folders
     *
     * @param organisation
     */
    void updateOrganisationAndWorkspaceFolderNames(Organisation organisation);

    List<UserManageBean> getUserManageBeans(Integer orgId);

    /**
     * Remove a user from the system completely. Only able to be done if they don't have any related learning designs,
     * etc.
     *
     * @param userId
     */
    void removeUser(Integer userId) throws Exception;

    /**
     * Determines whether a user has associated learning designs, progress data, etc.
     *
     * @param user
     * @return Boolean
     */
    Boolean userHasData(User user);

    /**
     * Sets user's disabled flag to true, and removes their organisation memberships.
     *
     * @param userId
     */
    void disableUser(Integer userId);

    /**
     * Set the roles for the specified user and organisation using the roleIds in rolesList. If userOrganisation exists,
     * will also remove roles that are not in rolesList.
     *
     * @param user
     * @param orgId
     * @param rolesList
     */
    void setRolesForUserOrganisation(User user, Integer organisationId, List<String> rolesList);

    /**
     * Set the roles for the specified user and organisation using the roleIds in rolesList. If userOrganisation
     * exists,
     * will also remove roles that are not in rolesList.
     *
     * @param checkGroupManagerRoles
     *            whether check if user is a group manager, he should also have other roles in organisation; can be
     *            false to avoid extra check if we know that user can not be a group manager
     */
    void setRolesForUserOrganisation(User user, Organisation org, List<String> rolesList,
	    boolean checkGroupManagerRoles);

    void setRolesForUserOrganisation(Integer userId, Integer organisationId, Set<Integer> roleIDList);

    /**
     * Returns list of roles suitable for the given orgType.
     *
     * @param rolelist
     * @param isAppadmin
     * @param orgType
     * @return
     */
    List<Role> filterRoles(List<Role> rolelist, Boolean isAppadmin, OrganisationType orgType);

    boolean hasRoleInOrganisation(User user, Integer roleId);

    /**
     * Returns true if user has the role in the given organisation. Otherwise false.
     *
     * @param user
     * @param roleId
     * @param organisation
     * @return
     */
    boolean hasRoleInOrganisation(User user, Integer roleId, Organisation organisation);

    /**
     * Given a user and a group, removes user from subgroups.
     *
     * @param user
     * @param org
     */
    void deleteChildUserOrganisations(User user, Organisation org);

    /**
     * Removes user from this group and its subgroups.
     *
     * @param user
     * @param org
     */
    void deleteUserOrganisation(User user, Organisation org);

    /**
     * Return true if user is a global group admin.
     */
    boolean isUserGlobalGroupManager();

    /**
     * Return true if user has appadmin role in root organisation.
     */
    boolean isUserAppAdmin();

    boolean isUserSysAdmin();

    boolean isUserSysAdmin(Integer userId);

    /**
     * Count then umber of users with a particular role in a given org.
     *
     * @param orgId
     * @param roleId
     * @return
     */
    Integer getCountRoleForOrg(Integer orgId, Integer roleId, String searchPhrase);

    Integer getCountRoleForOrg(Integer orgId, Integer[] roleIds, String searchPhrase);

    /**
     * Get default html theme of server.
     *
     * @return default html theme object
     */
    Theme getDefaultTheme();

    void logPasswordChanged(User user, User modifiedBy);

    void logUserCreated(User user, User createdBy);

    void logUserCreated(User user, UserDTO createdBy);

    Integer getCountUsers();

    Integer getCountUsers(Integer authenticationMethodId);

    /**
     * Count total number of users excluding disabled ones and applying searchString filter.
     *
     * @param searchString
     * @return
     */
    int getCountUsers(String searchString);

    List<OrganisationDTO> getActiveCoursesByUser(Integer userId, boolean isAppadmin, int page, int size,
	    String searchString);

    int getCountActiveCoursesByUser(Integer userId, boolean isAppadmin, String searchString);

    /**
     * Search users across login, first name, last name and email fields using the search term. Filters out disabled
     * users.
     *
     * @param term
     * @return list of Users
     */
    List<User> findUsers(String term);

    /**
     * Search users across login, first name, last name and email fields using the search term. Filters out disabled
     * users, and users that are members of filteredOrg.
     *
     * @param term
     * @param filteredOrgId
     * @return list of Users
     */
    List<User> findUsers(String term, Integer filteredOrgId);

    /**
     * Search users across login, first name, last name and email fields using the search term. Filters out disabled
     * users. Optionally include child organisations in the results.
     *
     * @param term
     * @param OrgId
     * @param includeChildOrgs
     * @return list of Users
     */
    List<User> findUsers(String term, Integer orgId, boolean includeChildOrgs);

    /**
     * Search user members in orgId across login, first name, last name and email fields using the search term. Filters
     * out disabled users, and users that are members of filteredOrg.
     *
     * @param term
     * @param orgId
     * @param filteredOrgId
     * @return list of Users
     */
    List<User> findUsers(String term, Integer orgId, Integer filteredOrgId);

    /**
     * Get all users, except for disabled users.
     *
     * @return list of Users
     */
    List<User> getAllUsers();

    /**
     * Get all users (paged), except for disabled users.
     *
     * @param page
     * @param size
     * @param sortBy
     * @param sortOrder
     * @param searchString
     *            filters results by course name. It can be null and then doesn't affect results
     * @return paged list of users
     */
    List<UserDTO> getAllUsers(Integer page, Integer size, String sortBy, String sortOrder, String searchString);

    List<UserDTO> getAllUsers(Integer organisationID, String[] roleNames, Integer page, Integer size, String sortBy,
	    String sortOrder, String searchString);

    /**
     * Get all users, except for disabled users and users that are members of filteredOrg.
     *
     * @param optionalOrgId
     * @return list of Users
     */
    List<User> getAllUsers(Integer filteredOrgId);

    /**
     * Get all users with the given Email. Email must not be empty Use a regex check to make sure the string is in
     * correct xxx@xxx.xxx form, see lams-central/web/forgotPassword.jsp javascript methods * @param email a non empty
     * email string
     *
     * @return list of Users
     */
    List<User> getAllUsersWithEmail(String email);

    /**
     * Tests whether user can edit the given org's details.
     *
     * @param userId
     * @param orgId
     * @return boolean
     */
    boolean canEditGroup(Integer userId, Integer orgId);

    /**
     * Gets a forgot password request from the db with the given key
     *
     * @param key
     * @return
     */
    ForgotPasswordRequest getForgotPasswordRequest(String key);

    /**
     * Remove given user from groups other than the specified one. Doesn't remove user from subgroups of the specified
     * group if any.
     *
     * @param userId
     * @param orgId
     * @return number of deleted rows.
     */
    int removeUserFromOtherGroups(Integer userId, Integer orgId);

    /**
     * Returns the user dto for the given openidURL if one exists used for sif openid login
     *
     * @param openidURL
     * @return
     */
    User getUserDTOByOpenidURL(String openidURL);

    /**
     * Stores organisation (course) groups and removes the unnecessary ones.
     */
    void saveOrganisationGrouping(OrganisationGrouping grouping, Collection<OrganisationGroup> newGroups);

    /**
     * Returns the SQL needed to look up portrait details for a given user. This is an efficient way to get the entries
     * at the same time as retrieving the tool data, rather than making a separate lookup.
     */
    String[] getPortraitSQL(String userIdString);

    /**
     * Looks for [login].png images in /tmp/portraits of user IDs within given range and starting with the given prefix
     */
    List<String> uploadPortraits(Integer minUserId, Integer maxUserId, String prefix) throws IOException;
}