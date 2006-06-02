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

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;

import org.lamsfoundation.lams.usermanagement.AuthenticationMethodType;
import org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodTypeDAO;

/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve AuthenticationMethodType objects.
 * <p>
 * <a href="AuthenticationMethodTypeDAO.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class AuthenticationMethodTypeDAO extends BaseDAO
		implements IAuthenticationMethodTypeDAO {

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodTypeDAO#getAllAuthenticationMethodTypes()
	 */
	public List getAllAuthenticationMethodTypes() {
		return getHibernateTemplate().find("from AuthenticationMethodType");
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodTypeDAO#getAuthenticationMethodTypeById(java.lang.Integer)
	 */
	public AuthenticationMethodType getAuthenticationMethodTypeById(Integer authenticationMethodTypeId) {
		return (AuthenticationMethodType)getHibernateTemplate().get(AuthenticationMethodType.class,authenticationMethodTypeId);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodTypeDAO#deleteAuthenticationMethodTypeById(java.lang.Integer)
	 */
	public void deleteAuthenticationMethodTypeById(Integer authenticationMethodTypeId) {
		delete(getAuthenticationMethodTypeById(authenticationMethodTypeId));
	}

}
