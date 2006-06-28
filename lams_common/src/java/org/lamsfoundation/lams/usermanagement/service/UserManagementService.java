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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.Workspace;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationDTO;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationDTOFactory;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.MessageService;

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

	private Logger log = Logger.getLogger(UserManagementService.class);
	private static final String SEQUENCES_FOLDER_NAME_KEY = "runsequences.folder.name";

	private IBaseDAO baseDAO;
	protected MessageService messageService;

	/**
	 * Set i18n MessageService
	 */
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	/**
	 * Get i18n MessageService
	 */
	public MessageService getMessageService() {
		return this.messageService;
	}

	public void setBaseDAO(IBaseDAO baseDAO){
		this.baseDAO = baseDAO;
	}
	
	public void save(Object object) {
		try{
			if(object instanceof User){
				User user = (User)object;
				if(user.getUserId()==null){
					user.setPassword(HashUtil.sha1(user.getPassword()));
					baseDAO.insertOrUpdate(user);  // creating a workspace needs a userId
					object = createWorkspaceForUser(user);
				}
			}
			baseDAO.insertOrUpdate(object);
		}catch(Exception e){
			log.debug(e);
		}
	}

	public void saveAll(Collection objects) {
		for(Object o:objects){
			if(o instanceof User){
				o = createWorkspaceForUser((User)o);
			}
		}
		baseDAO.insertOrUpdateAll(objects);
	}

	public void delete(Object object) {
		baseDAO.delete(object);
	}

	public void deleteAll(Class clazz) {
		baseDAO.deleteAll(clazz);
	}

	public void deleteAll(Collection objects) {
		baseDAO.deleteAll(objects);
	}

	public void deleteById(Class clazz, Serializable id) {
		baseDAO.deleteById(clazz,id);
	}

	public void deleteByProperty(Class clazz, String name, Object value) {
		baseDAO.deleteByProperty(clazz,name,value);
	}

	public void deleteByProperties(Class clazz, Map<String, Object> properties) {
		baseDAO.deleteByProperties(clazz,properties);
	}

	public void deleteAnythingLike(Object object) {
		baseDAO.deleteAnythingLike(object);
	}

	public Object findById(Class clazz, Serializable id) {
		return baseDAO.find(clazz,id);
	}

	public List findAll(Class clazz) {
		return baseDAO.findAll(clazz);
	}

	public List findByProperty(Class clazz, String name, Object value) {
		return baseDAO.findByProperty(clazz,name,value);
	}

	public List findByProperties(Class clazz, Map<String, Object> properties) {
		return baseDAO.findByProperties(clazz,properties);
	}

	public List findAnythingLike(Object object) {
		return baseDAO.findAnythingLike(object);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getOrganisationRolesForUser(org.lamsfoundation.lams.usermanagement.User, java.util.List<String>)
	 */
	public OrganisationDTO getOrganisationsForUserByRole(User user, List<String> restrictToRoleNames) {
		List<OrganisationDTO> list = new ArrayList<OrganisationDTO>();
		Iterator i = user.getUserOrganisations().iterator();
		
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
		List<OrganisationDTO> dtolist = new ArrayList<OrganisationDTO>();
		Organisation org = (Organisation)baseDAO.find(Organisation.class,courseId);
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
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("user.userId",user.getUserId());
			map.put("organisation.parentOrganisation.organisationId",org.getOrganisationId());
			List<UserOrganisation> childOrgs = baseDAO.findByProperties(UserOrganisation.class, map);
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
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("user.userId",user.getUserId());
			map.put("organisation.organisationId",organisationId);
			UserOrganisation userOrganisation = (UserOrganisation)baseDAO.findByProperties(UserOrganisation.class,map).get(0);
			OrganisationDTO dto = userOrganisation.getOrganisation().getOrganisationDTO();
			addRolesToDTO(null, userOrganisation, dto);
			return dto;
		}
		return null;
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getRolesForUserByOrganisation(org.lamsfoundation.lams.usermanagement.User,
	 *      java.lang.Integer)
	 */
	public List<Role> getRolesForUserByOrganisation(User user, Integer orgId) {
		List<Role> list = new ArrayList<Role>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("user.userId",user.getUserId());
		map.put("organisation.organisationId",orgId);
		UserOrganisation userOrg = (UserOrganisation)baseDAO.findByProperties(UserOrganisation.class,map).get(0);
		if (userOrg == null)
			return null;
		Iterator i = userOrg.getUserOrganisationRoles().iterator();
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
		Iterator i = baseDAO.findByProperty(UserOrganisation.class,"organisation.organisationId",orgId).iterator();
		while (i.hasNext()) {
			UserOrganisation userOrganisation = (UserOrganisation) i.next();
			list.add(userOrganisation.getUser());
		}
		return list;
	}

	
	/**
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getUsersFromOrganisationByRole(java.lang.Integer,
	 *      java.lang.String)
	 */
	public Vector<UserDTO> getUsersFromOrganisationByRole(Integer organisationID, String roleName) {
		
		Vector<UserDTO> users = new Vector<UserDTO>();
		Organisation organisation = (Organisation)baseDAO.find(Organisation.class,organisationID);
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


	public Organisation getRootOrganisation() {
		return (Organisation)baseDAO.findByProperty(Organisation.class,"organisationType.organisationTypeId",OrganisationType.ROOT_TYPE).get(0);
	}

	public boolean isUserInRole(Integer userId, Integer orgId, String roleName) {
		Map<String,Object> properties = new HashMap<String,Object>();
		properties.put("userOrganisation.user.userId",userId);
		properties.put("userOrganisation.organisation.organisationId",orgId);
		properties.put("role.name",roleName);
		if (baseDAO.findByProperties(UserOrganisationRole.class,properties).size()==0)
			return false;
		return true;
	}

	public List getOrganisationsByTypeAndStatus(Integer typeId, Integer stateId) {
		Map<String,Object> properties = new HashMap<String,Object>();
		properties.put("organisationType.organisationTypeId",typeId);
		properties.put("organisationState.organisationStateId",stateId);
		return baseDAO.findByProperties(Organisation.class,properties);
	}

	public List getUserOrganisationRoles(Integer orgId, String login) {
		Map<String,Object> properties = new HashMap<String,Object>();
		properties.put("userOrganisation.organisation.organisationId",orgId);
		properties.put("userOrganisation.user.login",login);
		return baseDAO.findByProperties(UserOrganisationRole.class,properties);
	}

	public List getUserOrganisationsForUserByTypeAndStatus(String login, Integer typeId, Integer stateId) {
		Map<String,Object> properties = new HashMap<String,Object>();
		properties.put("user.login",login);
		properties.put("organisation.organisationType.organisationTypeId",typeId);
		properties.put("organisation.organisationState.organisationStateId",stateId);
		return baseDAO.findByProperties(UserOrganisation.class,properties);
	}

	public User getUserByLogin(String login) {
		return (User)baseDAO.findByProperty(User.class,"login",login).get(0);
	}

	public void updatePassword(String login, String password) {
		try{
			User user = getUserByLogin(login);
			user.setPassword(HashUtil.sha1(password));
			baseDAO.update(user);
		}catch(Exception e){
			log.debug(e);
		}
	}

	public UserOrganisation getUserOrganisation(Integer userId, Integer orgId) {
		Map<String,Object> properties = new HashMap<String,Object>();
		properties.put("user.userId",userId);
		properties.put("organisation.organisationId",orgId);
		return (UserOrganisation)baseDAO.findByProperties(UserOrganisation.class,properties).get(0);
	}

	private User createWorkspaceForUser(User user) {
		Workspace workspace = new Workspace(user.getFullName());
		save(workspace);
		WorkspaceFolder folder = new WorkspaceFolder(workspace.getName(),user.getUserId(),new Date(),new Date(),WorkspaceFolder.NORMAL);
		save(folder);
		if ( workspace.getFolders() == null )
			workspace.setFolders(new HashSet());
		workspace.getFolders().add(folder);
		user.setWorkspace(workspace);
		return user;
	}

	public Workspace createWorkspaceForOrganisation(String workspaceName, Integer userID, Date createDateTime ) {
		
		// this method is public so it can be accessed from the junit test

		WorkspaceFolder workspaceFolder = new WorkspaceFolder(workspaceName,userID, createDateTime, createDateTime, WorkspaceFolder.NORMAL);
		save(workspaceFolder);

		String description = messageService.getMessage(SEQUENCES_FOLDER_NAME_KEY, new Object[] {workspaceName});
		if ( description != null && description.startsWith("???") ) {
			log.warn("Problem in the language file - can't find an entry for "+SEQUENCES_FOLDER_NAME_KEY+
					". Creating folder as \"run sequences\" ");
			description = "run sequences";
		}
		WorkspaceFolder workspaceFolder2 = new WorkspaceFolder(description,userID, createDateTime, createDateTime, WorkspaceFolder.RUN_SEQUENCES);
		workspaceFolder2.setParentWorkspaceFolder(workspaceFolder);
		save(workspaceFolder2);

		Workspace workspace =  new Workspace(workspaceName);
		if ( workspace.getFolders() == null )
			workspace.setFolders(new HashSet());

		workspace.getFolders().add(workspaceFolder);
		workspace.setDefaultFolder(workspaceFolder);
		workspace.getFolders().add(workspaceFolder2);
		workspace.setDefaultRunSequencesFolder(workspaceFolder2);

		save(workspace);

		return workspace;
	}
	
    public Organisation saveOrganisation( Organisation organisation, Integer userID ) 	 
    { 	 
	 
            if ( organisation.getOrganisationId() == null ) { 	 
                    Date createDateTime = new Date(); 	 
                    organisation.setCreateDate(createDateTime); 	 
	 
                    if(organisation.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.COURSE_TYPE)){ 	 
                            Workspace workspace = createWorkspaceForOrganisation(organisation.getName(), userID, createDateTime); 	 
                            organisation.setWorkspace(workspace); 	 
                    } 	 
            } 	 
	 
            save(organisation); 	 
            return organisation; 	 
    } 	 

	
}
