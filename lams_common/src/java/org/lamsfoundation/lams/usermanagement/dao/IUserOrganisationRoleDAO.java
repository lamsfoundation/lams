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
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;

/**
 * <p>
 * <a href="IUserOrganisationRoleDAO.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public interface IUserOrganisationRoleDAO extends IBaseDAO{

    /**
     * Gets userOrganisationRole object based on userOrganisationRoleId.
     * @param userOrganisationRoleId the userOrganisationRole's userOrganisationRoleId
     * @return userOrganisationRole populated userOrganisationRole object
     */
    public UserOrganisationRole getUserOrganisationRoleById(Integer userOrganisationRoleId);

    /**
     * Gets userOrganisationRole object based on userId and organisationId.
     * @param userOrganisationId the userOrganisationRole's userOrganisationId
     * @param roleId the userOrganisationRole's roleId
     * @return userOrganisationRole populated userOrganisationRole object
     */
    public UserOrganisationRole getUserOrganisationRole(Integer userOrganisationId, Integer roleId);

    /**
     * Gets a list of userOrganisationRole objects based on userOrganisationId.
     * @param userOrganisationId the userOrganisationRole's userOrganisationId
     * @return list of userOrganisationRole objects
     */
    public List getUserOrganisationRoles(Integer userOrganisationId);
    
    /**
     * Deletes a userOrganisationRole from the database by id
     * @param userOrganisationRoleId the userOrganisationRole's userOrganisationRoleId
     */
    public void deleteUserOrganisationRoleById(Integer userOrganisationRoleId);
	
}
