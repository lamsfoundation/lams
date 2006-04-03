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

import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodDAO;

/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve AuthenticationMethodType objects.
 *
 * <p>
 * <a href="AuthenticationMethodDAO.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class AuthenticationMethodDAO extends HibernateDaoSupport
		implements IAuthenticationMethodDAO {

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodDAO#getAllAuthenticationMethods()
	 */
	public List getAllAuthenticationMethods() {
		return getHibernateTemplate().find("from AuthenticationMethod");
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodDAO#getAuthenticationMethodById(java.lang.Integer)
	 */
	public AuthenticationMethod getAuthenticationMethodById(Integer authenticationMethodId) {
		return (AuthenticationMethod)getHibernateTemplate().get(AuthenticationMethod.class, authenticationMethodId);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodDAO#getAuthenticationMethodByName(java.lang.String)
	 */
    public AuthenticationMethod getAuthenticationMethodByName(String name){
		String queryString = "from AuthenticationMethod am where am.authenticationMethodName=?";
		List list = getHibernateTemplate().find(queryString,name);
		return (AuthenticationMethod)list.get(0);
    }
	
	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodDAO#getAuthenticationMethodByUser(org.lamsfoundation.lams.usermanagement.User)
	 */
	public AuthenticationMethod getAuthenticationMethodByUser(User user){
		String queryString = "from AuthenticationMethod am where am.authenticationMethodId=?";
		List list = getHibernateTemplate().find(queryString,user.getAuthenticationMethod().getAuthenticationMethodId());
		return (AuthenticationMethod)list.get(0);
	}
	
	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodDAO#saveAuthenticationMethod(org.lamsfoundation.lams.usermanagement.AuthenticationMethod)
	 */
	public void saveAuthenticationMethod(AuthenticationMethod authenticationMethod) {
		getHibernateTemplate().save(authenticationMethod);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodDAO#updateAuthenticationMethod(org.lamsfoundation.lams.usermanagement.AuthenticationMethod)
	 */
	public void updateAuthenticationMethod(AuthenticationMethod authenticationMethod) {
		getHibernateTemplate().update(authenticationMethod);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodDAO#saveOrUpdateAuthenticationMethod(org.lamsfoundation.lams.usermanagement.AuthenticationMethod)
	 */
	public void saveOrUpdateAuthenticationMethod(AuthenticationMethod authenticationMethod) {
		getHibernateTemplate().saveOrUpdate(authenticationMethod);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodDAO#deleteAuthenticationMethod(org.lamsfoundation.lams.usermanagement.AuthenticationMethod)
	 */
	public void deleteAuthenticationMethod(AuthenticationMethod authenticationMethod) {
		getHibernateTemplate().delete(authenticationMethod);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodDAO#deleteAuthenticationMethodById(java.lang.Integer)
	 */
	public void deleteAuthenticationMethodById(Integer authenticationMethodId) {
		getHibernateTemplate().delete(getAuthenticationMethodById(authenticationMethodId));
	}

}
