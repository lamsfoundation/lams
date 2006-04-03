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

import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO;

/**
 * UserOrganisation DAO Hibernate Implementation.
 *
 * <p>
 * <a href="UserOrganisationDAO.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class UserOrganisationDAO extends HibernateDaoSupport implements
		IUserOrganisationDAO {

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO#getAllUserOrganisations()
	 */
	public List getAllUserOrganisations() {
		return getHibernateTemplate().find("from UserOrganisation");
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO#getUserOrganisationById(java.lang.Integer)
	 */
	public UserOrganisation getUserOrganisationById(Integer userOrganisationId) {
		return (UserOrganisation)getHibernateTemplate().get(UserOrganisation.class, userOrganisationId);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO#getUserOrganisation(java.lang.Integer,java.lang.Integer)
	 */
    public UserOrganisation getUserOrganisation(Integer userId, Integer orgnisationId){
		String queryString = "from UserOrganisation uo where uo.user.userId=? and uo.organisation.organisationId=?";
		List list = getHibernateTemplate().find(queryString,new Object[]{userId,orgnisationId});
		if (list.size()==0)
		{
			return null;
		}
		else
		{
			return (UserOrganisation)list.get(0);
		}
    }
	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO#getUserOrganisationsByUser(org.lamsfoundation.lams.usermanagement.User)
	 */
	public List getUserOrganisationsByUser(User user) {
		String queryString = "from UserOrganisation uo where uo.user.userId=?";
		return getHibernateTemplate().find(queryString,user.getUserId());
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO#getUserOrganisationsByOrganisation(org.lamsfoundation.lams.usermanagement.Organisation)
	 */
	public List getUserOrganisationsByOrganisation(Organisation organisation) {
		String queryString = "from UserOrganisation uo where uo.organisation.organisationId=?";
		return getHibernateTemplate().find(queryString,organisation.getOrganisationId());
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO#getUserOrganisationsByOrganisationId(int)
	 */
	public List getUserOrganisationsByOrganisationId(Integer organisationId) {
		String queryString = "from UserOrganisation uo where uo.organisation.organisationId=?";
		return getHibernateTemplate().find(queryString,organisationId);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO#saveUserOrganisation(org.lamsfoundation.lams.usermanagement.UserOrganisation)
	 */
	public void saveUserOrganisation(UserOrganisation userOrganisation) {
		getHibernateTemplate().save(userOrganisation);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO#updateUserOrganisation(org.lamsfoundation.lams.usermanagement.UserOrganisation)
	 */
	public void updateUserOrganisation(UserOrganisation userOrganisation) {
		getHibernateTemplate().update(userOrganisation);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO#saveOrUpdateUserOrganisation(org.lamsfoundation.lams.usermanagement.UserOrganisation)
	 */
	public void saveOrUpdateUserOrganisation(UserOrganisation userOrganisation) {
		getHibernateTemplate().saveOrUpdate(userOrganisation);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO#deleteUserOrganisation(org.lamsfoundation.lams.usermanagement.UserOrganisation)
	 */
	public void deleteUserOrganisation(UserOrganisation userOrganisation) {
		getHibernateTemplate().delete(userOrganisation);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO#deleteUserOrganisationById(java.lang.Integer)
	 */
	public void deleteUserOrganisationById(Integer userOrganisationId) {
		getHibernateTemplate().delete(getUserOrganisationById(userOrganisationId));
	}

}
