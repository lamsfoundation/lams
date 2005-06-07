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
package org.lamsfoundation.lams.usermanagement.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;
import org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodDAO;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationTypeDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationRoleDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.usermanagement.dao.IRoleDAO;
import org.lamsfoundation.lams.usermanagement.dao.IWorkspaceDAO;
import org.lamsfoundation.lams.usermanagement.dao.IWorkspaceFolderDAO;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Workspace;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.util.wddx.FlashMessage;

/**
 * <p>
 * <a href="UserManagementService.java.html"> <i>View Source </i> </a>
 * </p>
 * 
 * @author Fei Yang, Manpreet Minhas
 */
public class UserManagementService implements IUserManagementService {

	private IUserDAO userDAO;

	private IRoleDAO roleDAO;

	private IOrganisationDAO organisationDAO;

	private IOrganisationTypeDAO organisationTypeDAO;

	private IUserOrganisationDAO userOrganisationDAO;

	private IUserOrganisationRoleDAO userOrganisationRoleDAO;

	private IAuthenticationMethodDAO authenticationMethodDAO;

	protected IWorkspaceDAO workspaceDAO;

	protected IWorkspaceFolderDAO workspaceFolderDAO;

	protected ILearningDesignDAO learningDesignDAO;

	private FlashMessage flashMessage;

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
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getUserById(java.lang.Integer)
	 */
	public User getUserById(Integer userId) {
		return userDAO.getUserById(userId);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getUserByLogin(java.lang.String)
	 */
	public User getUserByLogin(String login) {
		return userDAO.getUserByLogin(login);
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
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getBaseOrganisation(org.lamsfoundation.lams.usermanagement.Organisation)
	 */
	public Organisation getBaseOrganisation(Organisation organisation) {
		if (organisation.getOrganisationType().getName().equals(
				OrganisationType.ROOT)) {
			return null;
		} else if (organisation.getOrganisationType().getName().equals(
				OrganisationType.BASE)) {
			return organisation;
		} else {
			return getBaseOrganisation(organisation.getParentOrganisation());
		}
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
	public UserOrganisationRole getUserOrganisationRole(String login,
			Integer organisationId, String roleName) {
		User user = userDAO.getUserByLogin(login);
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
		return authenticationMethodDAO.getAuthenticationMethodByUser(userDAO
				.getUserByLogin(login));
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
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getOrganisationsForUserByRole(org.lamsfoundation.lams.usermanagement.User,
	 *      java.lang.String)
	 */
	public List getOrganisationsForUserByRole(User user, String roleName) {
		List list = new ArrayList();
		Iterator i = userOrganisationDAO.getUserOrganisationsByUser(user)
				.iterator();
		while (i.hasNext()) {
			UserOrganisation userOrganisation = (UserOrganisation) i.next();
			Iterator i2 = userOrganisation.getUserOrganisationRoles()
					.iterator();
			while (i2.hasNext()) {
				UserOrganisationRole userOrgansiationRole = (UserOrganisationRole) i2
						.next();
				if (userOrgansiationRole.getRole().getName().equals(roleName)) {
					list.add(userOrgansiationRole.getUserOrganisation()
							.getOrganisation());
				}
			}
		}
		return list;
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
	public List getRolesForUserByOrganisation(User user, Integer orgId) {
		List list = new ArrayList();
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
	public List getUsersFromOrganisation(Integer orgId) {
		List list = new ArrayList();
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
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#saveOrUpdateUser(org.lamsfoundation.lams.usermanagement.User)
	 */
	public void saveOrUpdateUser(User user) {
		userDAO.saveOrUpdateUser(user);
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
		WorkspaceFolder runSequencesFolder = createWorkspaceFolder(workspace,
				user.getUserId(), WorkspaceFolder.RUN_SEQUENCES);
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
		userOrganisation.setOrganisation(user.getBaseOrganisation());
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
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getWDDXForOrganisationsForUserByRole(java.lang.Integer,
	 *      java.lang.String)
	 */
	public String getWDDXForOrganisationsForUserByRole(Integer userID,
			String roleName) throws IOException {
		User user = userDAO.getUserById(userID);
		Vector organisations = new Vector();
		if (user != null) {
			Iterator iterator = getOrganisationsForUserByRole(user, roleName)
					.iterator();
			while (iterator.hasNext()) {
				Organisation organisation = (Organisation) iterator.next();
				organisations.add(organisation.getOrganisationDTO());
			}
			flashMessage = new FlashMessage(
					"getWDDXForOrganisationsForUserByRole", organisations);
		} else
			flashMessage = FlashMessage.getNoSuchUserExists(
					"getWDDXForOrganisationsForUserByRole", userID);

		return flashMessage.serializeMessage();
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getUsersFromOrganisationByRole(java.lang.Integer,
	 *      java.lang.String)
	 */
	public String getUsersFromOrganisationByRole(Integer organisationID,
			String roleName) throws IOException {
		Vector users = new Vector();
		Organisation organisation = organisationDAO
				.getOrganisationById(organisationID);
		if (organisation != null) {
			Iterator iterator = organisation.getUserOrganisations().iterator();
			while (iterator.hasNext()) {
				UserOrganisation userOrganisation = (UserOrganisation) iterator
						.next();
				Iterator userOrganisationRoleIterator = userOrganisation
						.getUserOrganisationRoles().iterator();
				while (userOrganisationRoleIterator.hasNext()) {
					UserOrganisationRole userOrganisationRole = (UserOrganisationRole) userOrganisationRoleIterator
							.next();
					if (userOrganisationRole.getRole().getName().equals(
							roleName))
						users.add(userOrganisation.getUser().getUserDTO());
				}
			}
			flashMessage = new FlashMessage("getUsersFromOrganisationByRole",
					users);

		} else
			flashMessage = new FlashMessage("getUsersFromOrganisationByRole",
					"No such Organisation with an organisation_id of:"
							+ organisationID + " exists", FlashMessage.ERROR);

		return flashMessage.serializeMessage();
	}	
}
