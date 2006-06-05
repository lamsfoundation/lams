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

import java.util.List;
import java.util.Vector;

import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.Workspace;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodDAO;
import org.lamsfoundation.lams.usermanagement.dao.ILocaleDAO;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationStateDAO;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationTypeDAO;
import org.lamsfoundation.lams.usermanagement.dao.IRoleDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationRoleDAO;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * User Management Service Interface to handle communication between 
 * web and persistence layer.
 * 
 * <p>
 * <a href="IUserManagementService.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
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
     * Set IUserDAO
     *
     * @param userDao
     */
	public void setUserDAO(IUserDAO userDao);

    /**
     * Set IRoleDAO
     *
     * @param roleDao
     */
	public void setRoleDAO(IRoleDAO roleDao);
	
    /**
     * Set ILocaleDAO
     *
     * @param localeDao 
     */
	public void setLocaleDAO(ILocaleDAO localeDao);

	
    /**
     * Set IOrganisationDAO
     *
     * @param organisationDao 
     */
	public void setOrganisationDAO(IOrganisationDAO organisationDao);

    /**
     * Set IOrganisationTypeDAO
     *
     * @param organisationTypeDao 
     */
	public void setOrganisationTypeDAO(IOrganisationTypeDAO organisationTypeDao);
	
    /**
     * Set IOrganisationStateDAO
     *
     * @param organisationStateDao 
     */
	public void setOrganisationStateDAO(IOrganisationStateDAO organisationStateDao);

	
    /**
     * Set IUserOrganisationDAO
     *
     * @param organisationDao 
     */
	public void setUserOrganisationDAO(IUserOrganisationDAO userOrganisationDao);

    /**
     * Set IUserOrganisationRoleDAO
     *
     * @param organisationRoleDao 
     */
	public void setUserOrganisationRoleDAO(IUserOrganisationRoleDAO userOrganisationRoleDao);
	
    /**
     * Set IAuthenticationMethodDAO
     *
     * @param authenticationMethodDao
     */
	public void setAuthenticationMethodDAO(IAuthenticationMethodDAO authenticationMethodDao);

	/**
     * Retrieves a user by userId. Null will be returned 
     * if no user with the userId is found. This method is more
     * efficient than getUserByLogin() as the value is cached.
     * 
     * @param userId the user's userId
     * @return User
     */
    public User getUserById(Integer userId);

    
	/**
     * Retrieves a user by login. Null will be returned 
     * if no user with the login is found. This method is not as
     * efficient than getUserById() as it is not cached.
     * 
     * @param login the user's login
     * @return User
     */
    public User getUserByLogin(String login);

	/**
     * Retrieves a organisation by id.  null will be returned 
     * if no organisation with the id is found
     * 
     * @param organisationId the organisation's id
     * @return Organisation
     */
    public Organisation getOrganisationById(Integer organisationId);

	/**
     * Retrieves a organisationType by Id.  null will be returned 
     * if no organisationType with the id is found
     * 
     * @param typeId the organisation type's Id
     * @return OrganisationType
     */
    public OrganisationType getOrganisationTypeById(Integer typeId);

    /**
     * Retrieves a organisationType by name.  null will be returned 
     * if no organisationType with the id is found
     * 
     * @param name the organisation type's name
     * @return OrganisationType
     */
    public OrganisationType getOrganisationTypeByName(String name);
    
	/**
     * Retrieves a role by name.  null will be returned 
     * if no role with the name is found
     * 
     * @param roleName role's name
     * @return Role
     */
    public Role getRoleByName(String roleName);
    
	/**
     * Retrieves a userOrganisationRole by ids and name.  null will be returned 
     * if no userOrganisationRole is found
     * 
     * @param userId the id of the user
     * @param organisationId the organisation's id
     * @param roleName the role's name
     * @return UserOrganisationRole
     */
    public UserOrganisationRole getUserOrganisationRole(Integer userId,Integer organisationId,String roleName);

	/**
     * Retrieves a userOrganisation by login and id.  null will be returned 
     * if no userOrganisation is found
     * 
     * @param userId the user's Id
     * @param organisationId the organisation's id
     * @return UserOrganisation
     */
    public UserOrganisation getUserOrganisation(Integer userId,Integer organisationId);
    
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
     * Retrieves child organisations of the parentOrg 
     * 
     * @param parentOrg the parent organisation
     * @return List of organisations
     */
    public List getChildOrganisations(Organisation parentOrg);
    
	/**
     * Retrieves userOrganisations for the user 
     * 
     * @param user the user
     * @return List of organisations
     */
    public List getUserOrganisationsForUser(User user);

    
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
     * Retrieves All the AuthenticationMethods 
     * 
     * @return List of AuthenticationMethods
     */
    public List getAllAuthenticationMethods();

    /**
     * Retrieves AuthenticationMethod for the user
     * specified by login. 
     * 
     * @param login the user's login
     * @return AuthenticationMethod for this user
     */
    public AuthenticationMethod getAuthenticationMethodForUser(String login);

    /**
     * Retrieves AuthenticationMethod for the user
     * specified by login. This implementation is more efficient 
     * than getAuthenticationMethodForUser(String login).
     * 
     * @param User user
     * @return AuthenticationMethod for this user
     */
    public AuthenticationMethod getAuthenticationMethodForUser(User user);
    
    /**
     * Retrieves AuthenticationMethod 
     * specified by name
     * 
     * @param name the method's name
     * @return AuthenticationMethod with the name
     */
    public AuthenticationMethod getAuthenticationMethodByName(String name);
    
    
    /**
     * Retrieves a list of all the OrganisationState objects
     * 
     * @return OrganisationState object list
     */
    public List<OrganisationState> getAllOrgnisationStates();
    
    /**
     * Retrieve the OrganisationState by the Id
     * 
     * @param oranisationStateId the Id of the organisationState
     * @return the OrganisationState object specified by the organisationStateId
     */
    public OrganisationState getOrganisationStateById(Integer organisationStateId);
   
     /**
      * 
      * @param user the user to be created
      */
    public void createUser(User user);

    /**
     * Updates a user's information
     *
     * @param user the user
     */
    public void updateUser(User user);

    /**
     * Save or Updates a user's information
     *
     * @param user the user
     */
    public void saveOrUpdateUser(User user);

    /**
     * Updates user's password
     * @param login the user's login
     * @param newPassword the user's new password
     */
    public void updatePassword(String login, String newPassword);    
    
    /**
     * Removes a user from the organisation
     *
     * @param userOrganisation the user's memebership in the organisation
     */
    public void removeUserOrganisation(UserOrganisation userOrganisation);
    
    /**
     * Saves or updates an organisation
     * 
     * @param organisation the organisation to be saved or updated
     */
    public void saveOrUpdateOrganisation(Organisation organisation);
    
    /**
     * Saves or updates an userOrganisation
     * 
     * @param userOrganisation the userOrganisation to be saved or updated
     */
    public void saveOrUpdateUserOrganisation(UserOrganisation userOrganisation);

    /**
     * Saves or updates an userOrganisationRole
     * 
     * @param userOrganisationRole the userOrganisationRole to be saved or updated
     */
    public void saveOrUpdateUserOrganisationRole(UserOrganisationRole userOrganisationRole);
    
    /**
     * Saves an Organisation while creating its corresponding Workspace
     * and WorkspaceFolder  
     * 
     * @param organisation The Organisation to be saved
     * @param userID The user_id of the user who creates this organisation 
     * @return Integer The organisation_id of the new Organisation
     */
    public Integer saveOrganisation(Organisation organisation,Integer userID);
    
    /***********************************
     * Methods added by Manpreet Minhas
     * ********************************/
    
	/**
	 * This method saves a new User to the underlying database while
	 * creating his default workspace and workspaceFolder
	 *  
	 * @param user The User object to be persisted
	 * @param roleID What kind of user he is (AUTHOR/LEARNER/STAFF/ADMIN) 
	 * @return Integer The user_id of the User
	 */
	public Integer saveUser(User user, Integer roleID);
	
	/**
	 * This method creates a new Workspace with a given name
	 * 
	 * @param name The name with which workspace should be created
	 * @return Workspace The new Workspace object
	 */
	public Workspace createWorkspace(String name);
	
	/**
	 * This method creates a WorkspaceFolder for a given workspace and user. 
	 * 
	 * @param workspace The Workspace in which this WorkspaceFolder will be contained
	 * @param userID The user_id of the user who creates the above organisation
	 * @param workspaceFolderType The type of folder to be created. 
	 * @return WorkspaceFolder The new WorkspaceFolder object
	 */
	public WorkspaceFolder createWorkspaceFolder(Workspace workspace,Integer userID, Integer workspaceFolderType);		
	
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
	
	public List getAllCountries();
	
	public List getAllLanguages();

}
