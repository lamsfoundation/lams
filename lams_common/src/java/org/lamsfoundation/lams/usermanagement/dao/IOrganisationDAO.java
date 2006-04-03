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

import org.lamsfoundation.lams.usermanagement.Organisation;

/**
 * Organisation Data Access Object (DAO) interface.
 *
 * <p>
 * <a href="IOrganisationDAO.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public interface IOrganisationDAO {

    /**
     * Gets a list of all the organisations.
     *
     * @return List populated list of organisations
     */
    public List getAllOrganisations();

    /**
     * Gets organisation object based on userId.
     * @param userId the organisation's userId
     * @return organisation populated organisation object
     */
    public Organisation getOrganisationById(Integer organisationId);

    /**
     * Gets organisation object based on name.
     * @param name the organisation's name
     * @return organisation populated organisation object
     */
    public List getOrganisationsByName(String name);

	/**
     * Retrieves child organisations of the parentOrg 
     * 
     * @param parentOrg the parent organisation
     * @return List of organisations
     */
    public List getChildOrganisations(Organisation parentOrg);

    /**
     * Saves the organisation
     * @param organisation the object to be saved
     * @return Organisation the saved organisation object
     */
    public void saveOrganisation(Organisation organisation);

    /**
     * Updates the organisation
     * @param organisation the object to be updated
     * @return Organisation the updated organisation object
     */
    public void updateOrganisation(Organisation organisation);

    /**
     * Saves or updates the organisation
     * @param organisation the object to be saved or updated
     * @return Organisation the saved or updated organisation object
     */
    public void saveOrUpdateOrganisation(Organisation organisation);

    /**
     * Deletes a organisation from the database
     * @param organisation the organisation to be deleted
     */
    public void deleteOrganisation(Organisation organisation);

    /**
     * Deletes a organisation from the database by id
     * @param organisationId the organisation's organisationId
     */
    public void deleteOrganisationById(Integer organisationId);
    
    public Organisation getOrganisationByWorkspaceID(Integer workspaceID);

}
