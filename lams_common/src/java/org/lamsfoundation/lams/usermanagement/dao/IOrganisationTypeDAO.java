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

import org.lamsfoundation.lams.usermanagement.OrganisationType;

/**
 * OrganisationType Data Access Object (DAO) interface.
 *
 * <p>
 * <a href="IOrganisationTypeDAO.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public interface IOrganisationTypeDAO {

    /**
     * Gets a list of all the organisationTypes.
     *
     * @return List populated list of organisationTypes
     */
    public List getAllOrganisationTypes();

    /**
     * Gets organisationType object based on organisationTypeId.
     * @param organisationTypeId the organisationType's organisationTypeId
     * @return organisationType populated organisationType object
     */
    public OrganisationType getOrganisationTypeById(Integer organisationTypeId);

    /**
     * Gets organisationType object based on name.
     * @param name the organisationType's name
     * @return organisationType populated organisationType object
     */
    public OrganisationType getOrganisationTypeByName(String name);

    /**
     * Saves the organisationType
     * @param organisationType the object to be saved
     * @return OrganisationType the saved organisationType object
     */
    public void saveOrganisationType(OrganisationType organisationType);

    /**
     * Updates the organisationType
     * @param organisationType the object to be updated
     * @return OrganisationType the updated organisationType object
     */
    public void updateOrganisationType(OrganisationType organisationType);

    /**
     * Saves or updates the organisationType
     * @param organisationType the object to be saved or updated
     * @return OrganisationType the saved or updated organisationType object
     */
    public void saveOrUpdateOrganisationType(OrganisationType organisationType);

    /**
     * Deletes a organisationType from the database
     * @param organisationType the organisationType to be deleted
     */
    public void deleteOrganisationType(OrganisationType organisationType);

    /**
     * Deletes a organisationType from the database by id
     * @param organisationTypeId the organisationType's organisationTypeId
     */
    public void deleteOrganisationTypeById(Integer organisationTypeId);

    /**
     * Deletes a organisationType from the database by name
     * @param name the organisationType's name
     */
    public void deleteOrganisationTypeByName(String name);

}
