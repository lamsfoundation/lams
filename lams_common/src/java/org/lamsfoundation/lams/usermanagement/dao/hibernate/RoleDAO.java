/*
 * Created on Nov 22, 2004
 *
 * Last modified on Nov 22, 2004
 */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import org.lamsfoundation.lams.usermanagement.dao.IRoleDAO;
import org.lamsfoundation.lams.usermanagement.Role;

/**
 * TODO Add description here
 *
 * <p>
 * <a href="RoleDAO.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class RoleDAO extends HibernateDaoSupport implements IRoleDAO {

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IRoleDAO#getAllRoles()
	 */
	public List getAllRoles() {
		return getHibernateTemplate().find("from Role");
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IRoleDAO#getRoleById(java.lang.Integer)
	 */
	public Role getRoleById(Integer roleId) {
		return (Role)getHibernateTemplate().get(Role.class, roleId);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IRoleDAO#getRoleByName(java.lang.String)
	 */
	public Role getRoleByName(String name) {
		String queryString = "from Role r where r.name=?";
		List roles = getHibernateTemplate().find(queryString,name);
		if(roles.size() == 0){
			return null;
		}else{
			return (Role)roles.get(0);
		}
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IRoleDAO#saveRole(org.lamsfoundation.lams.usermanagement.Role)
	 */
	public void saveRole(Role role) {
		getHibernateTemplate().save(role);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IRoleDAO#updateRole(org.lamsfoundation.lams.usermanagement.Role)
	 */
	public void updateRole(Role role) {
		getHibernateTemplate().update(role);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IRoleDAO#saveOrUpdateRole(org.lamsfoundation.lams.usermanagement.Role)
	 */
	public void saveOrUpdateRole(Role role) {
		getHibernateTemplate().saveOrUpdate(role);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IRoleDAO#deleteRole(org.lamsfoundation.lams.usermanagement.Role)
	 */
	public void deleteRole(Role role) {
		getHibernateTemplate().delete(role);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IRoleDAO#deleteRoleById(java.lang.Integer)
	 */
	public void deleteRoleById(Integer roleId) {
		deleteRole(getRoleById(roleId));
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IRoleDAO#deleteRoleByName(java.lang.String)
	 */
	public void deleteRoleByName(String name) {
		deleteRole(getRoleByName(name));
	}

}
