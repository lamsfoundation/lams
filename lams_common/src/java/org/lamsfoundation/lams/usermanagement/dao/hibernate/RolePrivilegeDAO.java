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
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.usermanagement.IRolePrivilegeDAO;

/**
 * @version
 *
 * <p>
 * <a href="RolePrivilegeDAO.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 * Created at 19:29:26 on 2006-6-5
 */
public class RolePrivilegeDAO extends BaseDAO implements IRolePrivilegeDAO {

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.usermanagement.IRolePrivilegeDAO#getAllRolePrivileges()
	 */
	public List getAllRolePrivileges() {
		return getHibernateTemplate().find("from RolePrivilege");
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.usermanagement.IRolePrivilegeDAO#getRolePrivilegesByRoleId(java.lang.Integer)
	 */
	public List getRolePrivilegesByRoleId(Integer roleId) {
		String queryString = "from RolePrivilege rp where rp.role.roleId=?";
		return getHibernateTemplate().find(queryString,roleId); 
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.usermanagement.IRolePrivilegeDAO#getRolePrivilegesByPrivilegeCode(java.lang.String)
	 */
	public List getRolePrivilegesByPrivilegeCode(String code) {
		String queryString = "from RolePrivilege rp where rp.privilege.code=?";
		return getHibernateTemplate().find(queryString,code); 
	}

}
