/*
 * Created on Dec 2, 2004
 *
 * Last modified on Dec 2, 2004
 */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

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
public class AuthenticationMethodTypeDAO extends HibernateDaoSupport
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
	 * @see org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodTypeDAO#saveAuthenticationMethodType(org.lamsfoundation.lams.usermanagement.AuthenticationMethodType)
	 */
	public void saveAuthenticationMethodType(AuthenticationMethodType authenticationMethodType) {
		getHibernateTemplate().save(authenticationMethodType);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodTypeDAO#updateAuthenticationMethodType(org.lamsfoundation.lams.usermanagement.AuthenticationMethodType)
	 */
	public void updateAuthenticationMethodType(AuthenticationMethodType authenticationMethodType) {
		getHibernateTemplate().update(authenticationMethodType);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodTypeDAO#saveOrUpdateAuthenticationMethodType(org.lamsfoundation.lams.usermanagement.AuthenticationMethodType)
	 */
	public void saveOrUpdateAuthenticationMethodType(AuthenticationMethodType authenticationMethodType) {
		getHibernateTemplate().saveOrUpdate(authenticationMethodType);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodTypeDAO#deleteAuthenticationMethodType(org.lamsfoundation.lams.usermanagement.AuthenticationMethodType)
	 */
	public void deleteAuthenticationMethodType(AuthenticationMethodType authenticationMethodType) {
		getHibernateTemplate().delete(authenticationMethodType);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IAuthenticationMethodTypeDAO#deleteAuthenticationMethodTypeById(java.lang.Integer)
	 */
	public void deleteAuthenticationMethodTypeById(Integer authenticationMethodTypeId) {
		getHibernateTemplate().delete(getAuthenticationMethodTypeById(authenticationMethodTypeId));
	}

}
