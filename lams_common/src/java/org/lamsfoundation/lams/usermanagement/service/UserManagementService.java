/*
 * Created on Nov 22, 2004
 *
 * Last modified on Nov 22, 2004
 */
package org.lamsfoundation.lams.usermanagement.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodDAO;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationTypeDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationRoleDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.usermanagement.dao.IRoleDAO;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;

/**
 * TODO Add description here
 *
 * <p>
 * <a href="UserManagementService.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class UserManagementService implements IUserManagementService {

	private IUserDAO userDAO;
	private IRoleDAO roleDAO;
	private IOrganisationDAO organisationDAO;
	private IOrganisationTypeDAO organisationTypeDAO;
	private IUserOrganisationDAO userOrganisationDAO;
	private IUserOrganisationRoleDAO userOrganisationRoleDAO;
	private IAuthenticationMethodDAO authenticationMethodDAO;
	
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
	public void setOrganisationTypeDAO(IOrganisationTypeDAO organisationTypeDAO){
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
	public void setUserOrganisationRoleDAO(IUserOrganisationRoleDAO userOrganisationRoleDAO) {
		this.userOrganisationRoleDAO = userOrganisationRoleDAO;
	}
	
	/** 
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#setOrganisationDAO(org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodDAO)
	 */
	public void setAuthenticationMethodDAO(IAuthenticationMethodDAO authenticationMethodDAO){
		this.authenticationMethodDAO = authenticationMethodDAO;
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getUserById(java.lang.Integer)
	 */
    public User getUserById(Integer userId){
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
    public Organisation getOrganisationById(Integer organisationId){
    	return organisationDAO.getOrganisationById(organisationId);
    }

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getOrganisationTypeByName(java.lang.String)
	 */
    public OrganisationType getOrganisationTypeByName(String name){
    	return organisationTypeDAO.getOrganisationTypeByName(name);
    }

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getBaseOrganisation(org.lamsfoundation.lams.usermanagement.Organisation)
	 */
    public Organisation getBaseOrganisation(Organisation organisation){
    	if (organisation.getOrganisationType().getName().equals(OrganisationType.ROOT)){
    		return null;
    	}else if(organisation.getOrganisationType().getName().equals(OrganisationType.BASE)){
    		return organisation;
    	}else{
    		return getBaseOrganisation(organisation.getParentOrganisation());
    	}
    }
    
	/** 
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getRoleByName(java.lang.String)
	 */
    public Role getRoleByName(String roleName){
    	return roleDAO.getRoleByName(roleName);
    }
    
	/** 
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getUserOrganisationRole(java.lang.String,java.lang.Integer,java.lang.String)
	 */
    public UserOrganisationRole getUserOrganisationRole(String login, Integer organisationId, String roleName){
    	User user = userDAO.getUserByLogin(login);
    	if(user == null)
    		return null;
    	UserOrganisation userOrganisation = userOrganisationDAO.getUserOrganisation(user.getUserId(),organisationId);
    	if(userOrganisation==null)
    		return null;
    	Role role = roleDAO.getRoleByName(roleName);
    	if(role==null)
    		return null;
    	return userOrganisationRoleDAO.getUserOrganisationRole(userOrganisation.getUserOrganisationId(),role.getRoleId());
    }

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getUserOrganisationRole(java.lang.String,java.lang.Integer,java.lang.String)
	 */
    public UserOrganisation getUserOrganisation(Integer userId,Integer organisationId){
    	return userOrganisationDAO.getUserOrganisation(userId,organisationId);
    }
    
    /** 
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getAllAuthenticationMethods()
	 */
    public List getAllAuthenticationMethods(){
    	return authenticationMethodDAO.getAllAuthenticationMethods();
    }
    
    /** 
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getAuthenticationMethodForUser(java.lang.String)
	 */
	public AuthenticationMethod getAuthenticationMethodForUser(String login) {
		return authenticationMethodDAO.getAuthenticationMethodByUser(userDAO.getUserByLogin(login));
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getAuthenticationMethodByName(java.lang.String)
	 */
	public AuthenticationMethod getAuthenticationMethodByName(String name){
    	return authenticationMethodDAO.getAuthenticationMethodByName(name);
    }

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getUserOrganisationsForUser(org.lamsfoundation.lams.usermanagement.User)
	 */
	public List getUserOrganisationsForUser(User user){
    	return userOrganisationDAO.getUserOrganisationsByUser(user);	
    }
	
	/** 
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getOrganisationsForUserByRole(org.lamsfoundation.lams.usermanagement.User, java.lang.String)
	 */
    public List getOrganisationsForUserByRole(User user, String roleName){
    	List list = new ArrayList();
    	Iterator i = userOrganisationDAO.getUserOrganisationsByUser(user).iterator();
    	while(i.hasNext()){
    		UserOrganisation userOrganisation = (UserOrganisation)i.next();
    		Iterator i2 = userOrganisation.getUserOrganisationRoles().iterator();
    		while(i2.hasNext()){
    			UserOrganisationRole userOrgansiationRole = (UserOrganisationRole)i2.next();
    			if(userOrgansiationRole.getRole().getName().equals(roleName)){
    				list.add(userOrgansiationRole.getUserOrganisation().getOrganisation());
    			}
    		}
    	}
    	return list;
    }

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getChildOrganisations(org.lamsfoundation.lams.usermanagement.Organisation)
	 */
    public List getChildOrganisations(Organisation parentOrg){
    	return organisationDAO.getChildOrganisations(parentOrg);
    }
    
	/** 
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getRolesForUserByOrganisation(org.lamsfoundation.lams.usermanagement.User, java.lang.Integer)
	 */
    public List getRolesForUserByOrganisation(User user, Integer orgId){
    	List list = new ArrayList();
    	UserOrganisation userOrg = userOrganisationDAO.getUserOrganisation(user.getUserId(),orgId);
    	if(userOrg==null)
    		return null;
    	Iterator i = userOrganisationRoleDAO.getUserOrganisationRoles(userOrg.getUserOrganisationId()).iterator();
    	while(i.hasNext()){
    		UserOrganisationRole userOrgRole = (UserOrganisationRole)i.next();
    		list.add(userOrgRole.getRole());
    	}
    	return list;
    }
    
	/** 
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#getUsersFromOrganisation(int)
	 */
    public List getUsersFromOrganisation(Integer orgId){
    	List list = new ArrayList();
    	Iterator i = userOrganisationDAO.getUserOrganisationsByOrganisationId(orgId).iterator();
    	while(i.hasNext()){
    		UserOrganisation userOrganisation = (UserOrganisation)i.next();
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
    public void saveOrUpdateUser(User user){
    	userDAO.saveOrUpdateUser(user);
    }
	
	/** 
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#updatePassword(java.lang.String,java.lang.String)
	 */
    public void updatePassword(String login, String newPassword){
    	userDAO.updatePassword(login,newPassword);
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
    public void saveOrUpdateUserOrganisation(UserOrganisation userOrganisation){
    	userOrganisationDAO.saveOrUpdateUserOrganisation(userOrganisation);
    }
	
	/** 
	 * @see org.lamsfoundation.lams.usermanagement.service.IUserManagementService#saveOrUpdateUserOrganisationRole(org.lamsfoundation.lams.usermanagement.UserOrganisationRole)
	 */
    public void saveOrUpdateUserOrganisationRole(UserOrganisationRole userOrganisationRole){
    	userOrganisationRoleDAO.saveOrUpdateUserOrganisationRole(userOrganisationRole);
    }
}
