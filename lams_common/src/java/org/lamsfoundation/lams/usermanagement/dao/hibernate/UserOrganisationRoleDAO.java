/*
 * Created on 2005-1-12
 *
 * Last modified on 2005-1-12
 */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationRoleDAO;

/**
 * TODO Add description here
 *
 * <p>
 * <a href="UserOrganisationRoleDAO.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class UserOrganisationRoleDAO extends HibernateDaoSupport implements
		IUserOrganisationRoleDAO {

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationRoleDAO#getUserOrganisationRoleById(java.lang.Integer)
	 */
	public UserOrganisationRole getUserOrganisationRoleById(Integer userOrganisationRoleId) {
		return (UserOrganisationRole)getHibernateTemplate().get(UserOrganisationRole.class, userOrganisationRoleId);	
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationRoleDAO#getUserOrganisationRole(java.lang.Integer, java.lang.Integer)
	 */
	public UserOrganisationRole getUserOrganisationRole(Integer userOrganisationId, Integer roleId) {
		String queryString = "from UserOrganisationRole uor where uor.userOrganisation.userOrganisationId=? and uor.role.roleId=?";
		List list = getHibernateTemplate().find(queryString,new Object[]{userOrganisationId,roleId});
		if (list.size()==0)
		{
			return null;
		}
		else
		{
			return (UserOrganisationRole)list.get(0);
		}
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationRoleDAO#getUserOrganisationRoles(java.lang.Integer)
	 */
    public List getUserOrganisationRoles(Integer userOrganisationId){
    	String queryString="from UserOrganisationRole uor where uor.userOrganisation.userOrganisationId=?";
    	return getHibernateTemplate().find(queryString,userOrganisationId);
    }
	
	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationRoleDAO#saveUserOrganisationRole(org.lamsfoundation.lams.usermanagement.UserOrganisationRole)
	 */
	public void saveUserOrganisationRole(UserOrganisationRole userOrganisationRole) {
		getHibernateTemplate().save(userOrganisationRole);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationRoleDAO#updateUserOrganisationRole(org.lamsfoundation.lams.usermanagement.UserOrganisationRole)
	 */
	public void updateUserOrganisationRole(UserOrganisationRole userOrganisationRole) {
		getHibernateTemplate().update(userOrganisationRole);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationRoleDAO#saveOrUpdateUserOrganisationRole(org.lamsfoundation.lams.usermanagement.UserOrganisationRole)
	 */
	public void saveOrUpdateUserOrganisationRole(UserOrganisationRole userOrganisationRole) {
		getHibernateTemplate().saveOrUpdate(userOrganisationRole);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationRoleDAO#deleteUserOrganisationRole(org.lamsfoundation.lams.usermanagement.UserOrganisationRole)
	 */
	public void deleteUserOrganisationRole(UserOrganisationRole userOrganisationRole) {
		getHibernateTemplate().delete(userOrganisationRole);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationRoleDAO#deleteUserOrganisationRoleById(java.lang.Integer)
	 */
	public void deleteUserOrganisationRoleById(Integer userOrganisationRoleId) {
		getHibernateTemplate().delete(getUserOrganisationRoleById(userOrganisationRoleId));
	}

}
