/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

import org.lamsfoundation.lams.usermanagement.dao.IRoleDAO;
import org.lamsfoundation.lams.usermanagement.Role;

/**
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
