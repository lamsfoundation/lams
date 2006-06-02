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
package org.lamsfoundation.lams.usermanagement.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.usermanagement.OrganisationState;

/**
 * @version
 *
 * <p>
 * <a href="IOrganisationStateDAO.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 * 
 * Created at 11:01:56 on 2006-6-2
 */
public interface IOrganisationStateDAO extends IBaseDAO{

	/**
     * Gets a list of all the organisationStates.
     *
     * @return List populated list of organisationStates
     */
    public List<OrganisationState> getAllOrganisationStates();

    /**
     * Gets organisationState object based on organisationStateId.
     * @param organisationStateId the organisationState's organisationStateId
     * @return organisationState populated organisationState object
     */
    public OrganisationState getOrganisationStateById(Integer organisationStateId);

    /**
     * Gets organisationState object based on name.
     * @param name the organisationState's name
     * @return organisationState populated organisationState object
     */
    public OrganisationState getOrganisationStateByName(String name);

    /**
     * Deletes a organisationState from the database by id
     * @param organisationStateId the organisationState's organisationStateId
     */
    public void deleteOrganisationStateById(Integer organisationStateId);

    /**
     * Deletes a organisationState from the database by name
     * @param name the organisationState's name
     */
    public void deleteOrganisationStateByName(String name);

}
