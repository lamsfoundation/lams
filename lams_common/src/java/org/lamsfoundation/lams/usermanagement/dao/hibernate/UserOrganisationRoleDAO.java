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
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationRoleDAO;

/**
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
