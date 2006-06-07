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
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.Organisation;

/**
 * UserOrganisation Data Access Object (DAO) interface.
 *
 * <p>
 * <a href="IUserOrganisationDAO.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public interface IUserOrganisationDAO extends IBaseDAO{
    /**
     * Gets a list of all the userOrganisations.
     *
     * @return List populated list of userOrganisations
     */
    public List getAllUserOrganisations();

    /**
     * Gets userOrganisation object based on userOrganisationId.
     * @param userOrganisationId the userOrganisation's userOrganisationId
     * @return userOrganisation populated userOrganisation object
     */
    public UserOrganisation getUserOrganisationById(Integer userOrganisationId);

    /**
     * Gets userOrganisation object based on userId and organisationId.
     * @param userId the userOrganisation's userId
     * @param organisationId the userOrganisation's organisationId
     * @return userOrganisation populated userOrganisation object
     */
    public UserOrganisation getUserOrganisation(Integer userId, Integer orgnisationId);
    
    /**
     * Gets userOrganisation objects based on user.
     * @param user the userOrganisation's user
     * @return List populated list of userOrganisations
     */
    public List getUserOrganisationsByUser(User user);

	/** 
     * Gets userOrganisation objects for this user, where the organisations are child organisations of the given parent organisation
     * @param user the userOrganisation's user
     * @param parentOrganisation the parent organisation
     * @return List populated list of userOrganisations
	 */
	public List getChildUserOrganisationsByUser(User user, Organisation parentOrganisation);

    /**
     * Gets userOrganisation objects based on organisation.
     * @param organisation the userOrganisation's organisation
     * @return List populated list of userOrganisations
     */
    public List getUserOrganisationsByOrganisation(Organisation organisation);

    /**
     * Gets userOrganisation objects based on organisationId.
     * @param organisationId the userOrganisation's organisationId
     * @return List populated list of userOrganisations
     */
    public List getUserOrganisationsByOrganisationId(Integer organisationId);
    
    /**
     * Deletes a userOrganisation from the database by id
     * @param userOrganisationId the userOrganisation's userOrganisationId
     */
    public void deleteUserOrganisationById(Integer userOrganisationId);

    public List getUserOrganisationsByType(Integer organisationTypeId);
}
