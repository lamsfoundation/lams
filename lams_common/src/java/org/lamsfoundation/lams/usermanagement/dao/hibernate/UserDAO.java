/*
 * Created on Nov 20, 2004
 *
 * Last modified on 20 Nov, 2004 
 */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.Workspace;

/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve User objects.
 *
 * <p>
 * <a href="UserDAO.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
*/
public class UserDAO extends HibernateDaoSupport implements IUserDAO {

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#getAllUsers()
	 */
	public List getAllUsers() {
		return getHibernateTemplate().find("from User");
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#getUserById(java.lang.Integer)
	 */
	public User getUserById(Integer userId) {
		return (User)getHibernateTemplate().get(User.class, userId);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#getUserByLogin(java.lang.String)
	 */
	public User getUserByLogin(String login) {
		String queryString = "from User u where u.login=?";
		List users = getHibernateTemplate().find(queryString,login);
		if(users.size() == 0){
			return null;
		}else{
			return (User)users.get(0);
		}
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#getUsersByTitle(java.lang.String)
	 */
	public List getUsersByTitle(String title) {
		String queryString = "from User u where u.title=?";
		return getHibernateTemplate().find(queryString,title);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#getUsersByFirstName(java.lang.String)
	 */
	public List getUsersByFirstName(String firstName) {
		String queryString = "from User u where u.firstName=?";
		return getHibernateTemplate().find(queryString,firstName);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#getUsersByLastName(java.lang.String)
	 */
	public List getUsersByLastName(String lastName) {
		String queryString = "from User u where u.lastName=?";
		return getHibernateTemplate().find(queryString,lastName);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#getUsersByFullName(java.lang.String)
	 */
	public List getUsersByFullName(String firstName, String lastName) {
		String queryString = "from User u where u.firstName=? and u.lastName=?";
		return getHibernateTemplate().find(queryString, new Object[]{firstName,lastName});
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#getUsersByCity(java.lang.String)
	 */
	public List getUsersByCity(String city) {
		String queryString = "from User u where u.city=?";
		return getHibernateTemplate().find(queryString,city);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#getUsersByState(java.lang.String)
	 */
	public List getUsersByState(String state) {
		String queryString = "from User u where u.state=?";
		return getHibernateTemplate().find(queryString,state);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#getUsersByCountry(java.lang.String)
	 */
	public List getUsersByCountry(String country) {
		String queryString = "from User u where u.country=?";
		return getHibernateTemplate().find(queryString,country);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#getUsersByDisabledFlag(java.lang.String)
	 */
	public List getUsersByDisabledFlag(Boolean disabledFlag) {
		String queryString = "from User u where u.disabledFlag=?";
		return getHibernateTemplate().find(queryString,disabledFlag);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#getUsersByWorkspace(org.lamsfoundation.lams.usermanagement.Workspace)
	 */
	public List getUsersByWorkspace(Workspace workspace) {
		String queryString = "from User u where u.workspace.workspaceId=?";
		return getHibernateTemplate().find(queryString,workspace.getWorkspaceId());
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#getUsersByAuthenticationMethod(org.lamsfoundation.lams.usermanagement.AuthenticationMethod)
	 */
	public List getUsersByAuthenticationMethod(
			AuthenticationMethod authenticationMethod) {
		String queryString = "from User u where u.authenticationMethod.authenticationMethodId=?";
		return getHibernateTemplate().find(queryString,authenticationMethod.getAuthenticationMethodId());
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#saveUser(org.lamsfoundation.lams.usermanagement.User)
	 */
	public void saveUser(User user) {
		getHibernateTemplate().save(user);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#updateUser(org.lamsfoundation.lams.usermanagement.User)
	 */
	public void updateUser(User user) {
		getHibernateTemplate().update(user);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#saveOrUpdateUser(org.lamsfoundation.lams.usermanagement.User)
	 */
	public void saveOrUpdateUser(User user) {
		getHibernateTemplate().saveOrUpdate(user);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#deleteUser(org.lamsfoundation.lams.usermanagement.User)
	 */
	public void deleteUser(User user) {
		getHibernateTemplate().delete(user);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#deleteUserById(java.lang.Integer)
	 */
	public void deleteUserById(Integer userId) {
		getHibernateTemplate().delete(getUserById(userId));
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#deleteUserByLogin(java.lang.String)
	 */
	public void deleteUserByLogin(String login) {
		getHibernateTemplate().delete(getUserByLogin(login));
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserDAO#updatePassword(java.lang.String,java.lang.String)
	 */
    public void updatePassword(String login, String newPassword){
    	User user = getUserByLogin(login);
    	user.setPassword(newPassword);
    	updateUser(user);
    }
	
}
