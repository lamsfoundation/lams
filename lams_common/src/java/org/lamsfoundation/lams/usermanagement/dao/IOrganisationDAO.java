/*
 * Created on 21 Nov, 2004
 *
 * Last modified on 21 Nov, 2004 
 */
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
