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
package org.lamsfoundation.lams.usermanagement.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.usermanagement.Role;

/**
 * Role Data Access Object (DAO) interface.
 *
 * <p>
 * <a href="IRoleDAO.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public interface IRoleDAO extends IBaseDAO {
	
    /**
     * Gets a list of all the roles.
     *
     * @return List populated list of roles
     */
    public List getAllRoles();

    /**
     * Gets role object based on roleId.
     * @param roleId the role's roleId
     * @return role populated role object
     */
    public Role getRoleById(Integer roleId);

    /**
     * Gets role object based on name.
     * @param name the role's name
     * @return role populated role object
     */
    public Role getRoleByName(String name);

    /**
     * Deletes a role from the database by id
     * @param roleId the role's roleId
     */
    public void deleteRoleById(Integer roleId);

    /**
     * Deletes a role from the database by name
     * @param name the role's name
     */
    public void deleteRoleByName(String name);

}
