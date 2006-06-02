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
/* $Id$ */
package org.lamsfoundation.lams.usermanagement.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.cache.ICacheManager;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
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
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationStateDAO;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationTypeDAO;
import org.lamsfoundation.lams.usermanagement.dao.IRoleDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationRoleDAO;
import org.lamsfoundation.lams.usermanagement.dao.IWorkspaceDAO;
import org.lamsfoundation.lams.usermanagement.dao.IWorkspaceFolderDAO;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationDTO;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationDTOFactory;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * <p>
 * <a href="UserManagementService.java.html"> <i>View Source </i> </a>
 * </p>
 *  
 * Manually caches the user objects (by user id) in the shared cache.
 * Whenever a user object is modified, the cached version must be 
 * removed. 
 * TODO complete the caching - need to remove the user from the cache on modification
 * of user/organisation details.
 * 
 * @author Fei Yang, Manpreet Minhas
 */
public class UserManagementService implements IUserManagementService {

	protected Logger log = Logger.getLogger(UserManagementService.class);	

	private IUserDAO userDAO;

	private IRoleDAO roleDAO;

	private IOrganisationDAO organisationDAO;

	private IOrganisationTypeDAO organisationTypeDAO;
	
	private IOrganisationStateDAO organisationStateDAO;

	private IUserOrganisationDAO userOrganisationDAO;

	private IUserOrganisationRoleDAO userOrganisationRoleDAO;

	private IAuthenticationMethodDAO authenticationMethodDAO;

	protected IWorkspaceDAO workspaceDAO;

	protected IWorkspaceFolderDAO workspaceFolderDAO;

	protected ILearningDesignDAO learningDesignDAO;

	protected ILessonDAO lessonDAO;

	protected ICacheManager cacheManager;

	private String[] userClassParts = null;
	
	/**
	 * @param workspaceFolderDAO
	 *            The workspaceFolderDAO to set.
	 */
	public void setWorkspaceFolderDAO(IWorkspaceFolderDAO workspaceFolderDAO) {
		this.workspaceFolderDAO = workspaceFolderDAO;
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#setUserDAO(org.lamsfoundation.lams.usermanagement.dao.IUserDAO)
	 */
	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#setRoleDAO(org.lamsfoundation.lams.usermanagement.dao.IRoleDAO)
	 */
	public void setRoleDAO(IRoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#setOrganisationDAO(org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO)
	 */
	public void setOrganisationDAO(IOrganisationDAO organisationDAO) {
		this.organisationDAO = organisationDAO;
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#setOrganisationTypeDAO(org.lamsfoundation.lams.usermanagement.dao.IOrganisationTypeDAO)
	 */
	public void setOrganisationTypeDAO(IOrganisationTypeDAO organisationTypeDAO) {
		this.organisationTypeDAO = organisationTypeDAO;
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#setOrganisationStateDAO(org.lamsfoundation.lams.usermanagement.dao.IOrganisationStateDAO)
	 */
	public void setOrganisationStateDAO(IOrganisationStateDAO organisationStateDAO) {
		this.organisationStateDAO = organisationStateDAO;
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#setUserOrganisationDAO(org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO)
	 */
	public void setUserOrganisationDAO(IUserOrganisationDAO userOrganisationDAO) {
		this.userOrganisationDAO = userOrganisationDAO;
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#setUserOrganisationRoleDAO(org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationRoleDAO)
	 */
	public void setUserOrganisationRoleDAO(
			IUserOrganisationRoleDAO userOrganisationRoleDAO) {
		this.userOrganisationRoleDAO = userOrganisationRoleDAO;
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#setOrganisationDAO(org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodDAO)
	 */
	public void setAuthenticationMethodDAO(
			IAuthenticationMethodDAO authenticationMethodDAO) {
		this.authenticationMethodDAO = authenticationMethodDAO;
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#setLessonDAO(org.lamsfoundation.lams.lesson.dao.ILessonDAO)
	 */
	public void setLessonDAO(ILessonDAO lessonDAO) {
		this.lessonDAO = lessonDAO;
	}

	public void setCacheManager(ICacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}	

	/**
	 * Tries to get the user from the cache and if that fails then reads it from the database and puts it in the cache.
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getUserById(java.lang.Integer)
	 */
	public User getUserById(Integer userId) {
		if ( userClassParts == null ) { 
			userClassParts = cacheManager.getPartsFromClass(User.class);
		}

		User user = (User) cacheManager.getItem(this.userClassParts, userId);
		if ( user == null ) {
			user = userDAO.getUserById(userId);
			if ( user != null ) {
				cacheManager.addItem(userClassParts,user.getUserId(),user);
				if ( log.isDebugEnabled() ){
					log.debug("getUserById retrieved user from database "+user.getUserId());
				}
			}
		} else if ( log.isDebugEnabled() ){
			log.debug("getUserById retrieved user from cache "+userId);
		}
		return user;
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getUserByLogin(java.lang.String)
	 */
	public User getUserByLogin(String login) {
		User user = userDAO.getUserByLogin(login);
		if ( user != null ) {
			if ( userClassParts == null ) { 
				userClassParts = cacheManager.getPartsFromClass(User.class);
			}
			cacheManager.addItem(userClassParts,user.getUserId(),user);
		}
		return user;
	}

	/** 
	 * Clear the user from the cache - presumably the user has been updated. 
	 */
	private void clearUserFromCache(User user) {
		cacheManager.removeItem(this.userClassParts,user.getUserId());
		cacheManager.removeItem(this.userClassParts,user.getLogin());

	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getOrganisationById(java.lang.Integer)
	 */
	public Organisation getOrganisationById(Integer organisationId) {
		return organisationDAO.getOrganisationById(organisationId);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getOrganisationTypeByName(java.lang.String)
	 */
	public OrganisationType getOrganisationTypeByName(String name) {
		return organisationTypeDAO.getOrganisationTypeByName(name);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getRoleByName(java.lang.String)
	 */
	public Role getRoleByName(String roleName) {
		return roleDAO.getRoleByName(roleName);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getUserOrganisationRole(java.lang.String,java.lang.Integer,java.lang.String)
	 */
	public UserOrganisationRole getUserOrganisationRole(Integer userID, Integer organisationId, String roleName) {
		User user = this.getUserById(userID);
		if (user == null)
			return null;
		UserOrganisation userOrganisation = userOrganisationDAO
				.getUserOrganisation(user.getUserId(), organisationId);
		if (userOrganisation == null)
			return null;
		Role role = roleDAO.getRoleByName(roleName);
		if (role == null)
			return null;
		return userOrganisationRoleDAO.getUserOrganisationRole(userOrganisation
				.getUserOrganisationId(), role.getRoleId());
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getUserOrganisationRole(java.lang.String,java.lang.Integer,java.lang.String)
	 */
	public UserOrganisation getUserOrganisation(Integer userId,
			Integer organisationId) {
		return userOrganisationDAO.getUserOrganisation(userId, organisationId);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getAllAuthenticationMethods()
	 */
	public List getAllAuthenticationMethods() {
		return authenticationMethodDAO.getAllAuthenticationMethods();
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getAuthenticationMethodForUser(java.lang.String)
	 */
	public AuthenticationMethod getAuthenticationMethodForUser(String login) {
		return authenticationMethodDAO.getAuthenticationMethodByUser(this.getUserByLogin(login));
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getAuthenticationMethodForUser(org.lamsfoundation.lams.usermanagement.User)
	 */
	public AuthenticationMethod getAuthenticationMethodForUser(User user) {
		return authenticationMethodDAO.getAuthenticationMethodByUser(user);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getAuthenticationMethodByName(java.lang.String)
	 */
	public AuthenticationMethod getAuthenticationMethodByName(String name) {
		return authenticationMethodDAO.getAuthenticationMethodByName(name);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getUserOrganisationsForUser(org.lamsfoundation.lams.usermanagement.User)
	 */
	public List getUserOrganisationsForUser(User user) {
		return userOrganisationDAO.getUserOrganisationsByUser(user);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getOrganisationRolesForUser(org.lamsfoundation.lams.usermanagement.User, java.util.List<String>)
	 */
	public OrganisationDTO getOrganisationsForUserByRole(User user, List<String> restrictToRoleNames) {
		// TODO optimise db access
		List<OrganisationDTO> list = new ArrayList<OrganisationDTO>();
		Iterator i = userOrganisationDAO.getUserOrganisationsByUser(user).iterator();
		
		while (i.hasNext()) {
			UserOrganisation userOrganisation = (UserOrganisation) i.next();
			OrganisationDTO dto = userOrganisation.getOrganisation().getOrganisationDTO();
			boolean aRoleFound = addRolesToDTO(restrictToRoleNames, userOrganisation, dto);
			if ( aRoleFound ) {
				list.add(dto);
			}
		}
		return OrganisationDTOFactory.createTree(list);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getOrganisationRolesForUser(org.lamsfoundation.lams.usermanagement.User, java.util.List<String>, java.util.Integer)
	 */
	public OrganisationDTO getOrganisationsForUserByRole(User user, List<String> restrictToRoleNames, Integer courseId, List<Integer> restrictToClassIds) {
		// TODO optimise db access
		List<OrganisationDTO> dtolist = new ArrayList<OrganisationDTO>();
		Organisation org = organisationDAO.getOrganisationById(courseId);
		dtolist.add(org.getOrganisationDTO());
		getChildOrganisations(user, org, restrictToRoleNames, restrictToClassIds, dtolist);
		OrganisationDTO dtoTree = OrganisationDTOFactory.createTree(dtolist);
		
		// Want to return the course as the main node, not the dummy root.
		Vector nodes = dtoTree.getNodes();
		return (OrganisationDTO) nodes.get(0);
		
	}
	
	private void getChildOrganisations(User user, Organisation org, List<String> restrictToRoleNames, List<Integer> restrictToClassIds, List<OrganisationDTO> dtolist) {
		if ( org != null ) {
			boolean notCheckClassId = restrictToClassIds == null || restrictToClassIds.size() == 0;
			List<UserOrganisation> childOrgs = userOrganisationDAO.getChildUserOrganisationsByUser(user, org);
			for ( UserOrganisation userOrganisation : childOrgs) {
				OrganisationDTO dto = userOrganisation.getOrganisation().getOrganisationDTO();
				if ( notCheckClassId || restrictToClassIds.contains(dto.getOrganisationID()) ) {
					boolean aRoleFound = addRolesToDTO(restrictToRoleNames, userOrganisation, dto);
					if ( aRoleFound ) {
						dtolist.add(dto);
					}
					
					// now, process any children of this org
					Organisation childOrganisation = userOrganisation.getOrganisation();
					if ( org.getChildOrganisations().size() > 0 ) {
						getChildOrganisations(user, childOrganisation, restrictToRoleNames, restrictToClassIds, dtolist);
					}
				}
			}
		}
	}

	/**
	 * Go through the roles for this user organisation and add the roles to the dto.
	 * @param restrictToRoleNames
	 * @param userOrganisation
	 * @param dto
	 * @return true if a role is found, false otherwise
	 */
	private boolean addRolesToDTO(List<String> restrictToRoleNames, UserOrganisation userOrganisation, OrganisationDTO dto) {
		Iterator iter = userOrganisation.getUserOrganisationRoles().iterator();

		boolean roleFound = false;
		while (iter.hasNext()) {
			
			UserOrganisationRole userOrganisationRole = (UserOrganisationRole) iter.next();
			String roleName = userOrganisationRole.getRole().getName();
			if ( restrictToRoleNames == null || restrictToRoleNames.size()==0 || restrictToRoleNames.contains(roleName) ) {
				dto.addRoleName(roleName);
				roleFound = true;
			}
		}
		return roleFound;
	}

	/**
     * Gets an organisation for a user, with the user's roles. Doesn't not return a tree of organisations.
     * Will not return the organisation if there isn't any roles for this user.
	 */
	public OrganisationDTO getOrganisationForUserWithRole(User user, Integer organisationId) {
		if ( user != null && organisationId !=null ) {
			UserOrganisation userOrganisation = getUserOrganisation(user.getUserId(), organisationId);
			OrganisationDTO dto = userOrganisation.getOrganisation().getOrganisationDTO();
			addRolesToDTO(null, userOrganisation, dto);
			return dto;
		}
		return null;
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getChildOrganisations(org.lamsfoundation.lams.usermanagement.Organisation)
	 */
	public List getChildOrganisations(Organisation parentOrg) {
		return organisationDAO.getChildOrganisations(parentOrg);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getRolesForUserByOrganisation(org.lamsfoundation.lams.usermanagement.User,
	 *      java.lang.Integer)
	 */
	public List<Role> getRolesForUserByOrganisation(User user, Integer orgId) {
		List<Role> list = new ArrayList<Role>();
		UserOrganisation userOrg = userOrganisationDAO.getUserOrganisation(user
				.getUserId(), orgId);
		if (userOrg == null)
			return null;
		Iterator i = userOrganisationRoleDAO.getUserOrganisationRoles(
				userOrg.getUserOrganisationId()).iterator();
		while (i.hasNext()) {
			UserOrganisationRole userOrgRole = (UserOrganisationRole) i.next();
			list.add(userOrgRole.getRole());
		}
		return list;
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getUsersFromOrganisation(int)
	 */
	public List<User> getUsersFromOrganisation(Integer orgId) {
		List<User> list = new ArrayList<User>();
		Iterator i = userOrganisationDAO.getUserOrganisationsByOrganisationId(
				orgId).iterator();
		while (i.hasNext()) {
			UserOrganisation userOrganisation = (UserOrganisation) i.next();
			list.add(userOrganisation.getUser());
		}
		return list;
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#createUser(org.lamsfoundation.lams.usermanagement.User)
	 */
	public void createUser(User user) {
		userDAO.saveUser(user);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#updateUser(org.lamsfoundation.lams.usermanagement.User)
	 */
	public void updateUser(User user) {
		userDAO.updateUser(user);
		clearUserFromCache(user);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#saveOrUpdateUser(org.lamsfoundation.lams.usermanagement.User)
	 */
	public void saveOrUpdateUser(User user) {
		userDAO.saveOrUpdateUser(user);
		clearUserFromCache(user);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#updatePassword(java.lang.String,java.lang.String)
	 */
	public void updatePassword(String login, String newPassword) {
		userDAO.updatePassword(login, newPassword);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#removeUserOrganisation(org.lamsfoundation.lams.usermanagement.UserOrganisation)
	 */
	public void removeUserOrganisation(UserOrganisation userOrganisation) {
		userOrganisationDAO.deleteUserOrganisation(userOrganisation);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#saveOrUpdateOrganisation(org.lamsfoundation.lams.usermanagement.Organisation)
	 */
	public void saveOrUpdateOrganisation(Organisation organisation) {
		organisationDAO.saveOrUpdateOrganisation(organisation);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#saveOrUpdateUserOrganisation(org.lamsfoundation.lams.usermanagement.UserOrganisation)
	 */
	public void saveOrUpdateUserOrganisation(UserOrganisation userOrganisation) {
		userOrganisationDAO.saveOrUpdateUserOrganisation(userOrganisation);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#saveOrUpdateUserOrganisationRole(org.lamsfoundation.lams.usermanagement.UserOrganisationRole)
	 */
	public void saveOrUpdateUserOrganisationRole(
			UserOrganisationRole userOrganisationRole) {
		userOrganisationRoleDAO
				.saveOrUpdateUserOrganisationRole(userOrganisationRole);
	}

	/**
	 * @param workspaceDAO
	 *            The workspaceDAO to set.
	 */
	public void setUserManagementWorkspaceDAO(IWorkspaceDAO workspaceDAO) {
		this.workspaceDAO = workspaceDAO;
	}
	
	/*********************************************
	 * Utility Methods added by Manpreet Minhas
	 *********************************************/

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#saveOrganisation(org.lamsfoundation.lams.usermanagement.Organisation,
	 *      java.lang.Integer)
	 */
	public Integer saveOrganisation(Organisation organisation, Integer userID) {
		Workspace workspace = createWorkspace(organisation.getName());
		WorkspaceFolder workspaceFolder = createWorkspaceFolder(workspace,
				userID, WorkspaceFolder.NORMAL);
		workspace.setRootFolder(workspaceFolder);
		workspaceDAO.update(workspace);
		organisation.setWorkspace(workspace);
		organisationDAO.saveOrganisation(organisation);
		return organisation.getOrganisationId();
	}

	/**
	 * This method creates a new Workspace with a given name
	 * 
	 * @param name
	 *            The name with which workspace should be created
	 * @return Workspace The new Workspace object
	 */
	public Workspace createWorkspace(String name) {
		Workspace workspace = new Workspace(name);
		workspaceDAO.insert(workspace);
		return workspace;

	}

	/**
	 * This method creates a WorkspaceFolder for a given workspace and user.
	 * 
	 * @param workspace
	 *            The Workspace in which this WorkspaceFolder will be contained
	 * @param userID
	 *            The user_id of the user who creates the above organisation
	 * @param workspaceFolderType
	 *            The type of folder to be created.
	 * @return WorkspaceFolder The new WorkspaceFolder object
	 */
	public WorkspaceFolder createWorkspaceFolder(Workspace workspace,
			Integer userID, Integer workspaceFolderType) {
		WorkspaceFolder workspaceFolder = new WorkspaceFolder(workspace
				.getName(), workspace.getWorkspaceId(), userID, new Date(),
				new Date(), workspaceFolderType);
		workspaceFolderDAO.insert(workspaceFolder);
		return workspaceFolder;

	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#saveUser(org.lamsfoundation.lams.usermanagement.User)
	 */
	public Integer saveUser(User user, Integer roleID) {
		userDAO.saveUser(user);
		createUserOrganisation(user, roleID);
		Workspace workspace = createWorkspace(user.getLogin());
		WorkspaceFolder workspaceFolder = createWorkspaceFolder(workspace, user
				.getUserId(), WorkspaceFolder.NORMAL);
		createWorkspaceFolder(workspace, user.getUserId(), WorkspaceFolder.RUN_SEQUENCES);
		workspace.setRootFolder(workspaceFolder);
		workspaceDAO.update(workspace);
		user.setWorkspace(workspace);
		userDAO.updateUser(user);
		return user.getUserId();
	}

	/**
	 * This is a utility method required by the above method
	 * <code>saveUser</code>. It adds a new record to the  
	 * underlying database table indicating the organisation
	 * to which this user belongs 
	 * 
	 * @param user 
	 * @param roleID
	 * @return Integer 
	 */
	private Integer createUserOrganisation(User user, Integer roleID) {
		UserOrganisation userOrganisation = new UserOrganisation();
		userOrganisation.setUser(user);
		userOrganisationDAO.saveUserOrganisation(userOrganisation);
		userOrganisation.addUserOrganisationRole(createUserOrganisationRole(
				userOrganisation, roleID));
		userOrganisationDAO.saveOrUpdateUserOrganisation(userOrganisation);
		return userOrganisation.getUserOrganisationId();
	}

	/**
	 * This is a utility method required by the above method
	 * <code>createUserOrganisation</code>. It adds a new record 
	 * to the underlying database table indicating the Role that
	 * the given user has in the Organisation.
	 *  
	 * @param userOrganisation
	 * @param roleID
	 * @return UserOrganisationRole
	 */
	private UserOrganisationRole createUserOrganisationRole(
			UserOrganisation userOrganisation, Integer roleID) {
		UserOrganisationRole userOrganisationRole = new UserOrganisationRole();
		userOrganisationRole.setUserOrganisation(userOrganisation);
		userOrganisationRole.setRole(roleDAO.getRoleById(roleID));
		userOrganisationRoleDAO.saveUserOrganisationRole(userOrganisationRole);
		return userOrganisationRole;
	}
	/**
	 * @param learningDesignDAO
	 *            The learningDesignDAO to set.
	 */
	public void setLearningDesignDAO(ILearningDesignDAO learningDesignDAO) {
		this.learningDesignDAO = learningDesignDAO;
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getUsersFromOrganisationByRole(java.lang.Integer,
	 *      java.lang.String)
	 */
	public Vector<UserDTO> getUsersFromOrganisationByRole(Integer organisationID, String roleName) {
		
		Vector<UserDTO> users = new Vector<UserDTO>();
		Organisation organisation = organisationDAO.getOrganisationById(organisationID);
		if (organisation != null) {
			Iterator iterator = organisation.getUserOrganisations().iterator();
			while (iterator.hasNext()) {
				UserOrganisation userOrganisation = (UserOrganisation) iterator.next();
				Iterator userOrganisationRoleIterator = userOrganisation.getUserOrganisationRoles().iterator();
				while (userOrganisationRoleIterator.hasNext()) {
					UserOrganisationRole userOrganisationRole = (UserOrganisationRole) userOrganisationRoleIterator.next();
					if (userOrganisationRole.getRole().getName().equals(roleName))
						users.add(userOrganisation.getUser().getUserDTO());
				}
			}
		}
		return users;
	}

	/** Get all the lessons in an organisation that this user can monitor.
	 * This is a temporary method to support the dummy index page. 
	 * TODO modify/remove when the index page is implemented properly
	 */
	public List getMonitorLessonsFromOrganisation(Integer userID, Integer organisationID) {
		return lessonDAO.getLessonsForMonitoring(userID, organisationID);
	}
	/** Get all the lessons in an organisation for which this user is a learner.
	 * This is a temporary method to support the dummy index page. 
	 * TODO modify/remove when the index page is implemented properly
	 */
	public List getLearnerLessonsFromOrganisation(Integer userID, Integer organisationID) {
		return lessonDAO.getActiveLessonsForLearner(userID, organisationID);
	}

	public List<OrganisationState> getAllOrgnisationStates() {
		return organisationStateDAO.getAllOrganisationStates();
	}

	public OrganisationState getOrganisationStateById(Integer organisationStateId) {
		return organisationStateDAO.getOrganisationStateById(organisationStateId);
	}
}
