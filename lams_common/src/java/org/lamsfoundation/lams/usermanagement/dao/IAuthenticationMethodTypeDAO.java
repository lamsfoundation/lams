/*
 * Created on Dec 2, 2004
 *
 * Last modified on Dec 2, 2004
 */
package org.lamsfoundation.lams.usermanagement.dao;

import java.util.List;

import org.lamsfoundation.lams.usermanagement.AuthenticationMethodType;

/**
 * AuthenticationMethodType Data Access Object (DAO) interface.
 *
 * <p>
 * <a href="IAuthenticationMethodTypeTypeDAO.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public interface IAuthenticationMethodTypeDAO {

    /**
     * Gets a list of all the authenticationMethodTypes.
     *
     * @return List populated list of authenticationMethodTypes
     */
    public List getAllAuthenticationMethodTypes();

    /**
     * Gets authenticationMethodType object based on authenticationMethodTypeId.
     * @param authenticationMethodTypeId the authenticationMethodType's authenticationMethodTypeId
     * @return authenticationMethodType populated authenticationMethodType object
     */
    public AuthenticationMethodType getAuthenticationMethodTypeById(Integer authenticationMethodTypeId);

    /**
     * Saves the authenticationMethodType
     * @param authenticationMethodType the object to be saved
     * @return AuthenticationMethodType the saved authenticationMethodType object
     */
    public void saveAuthenticationMethodType(AuthenticationMethodType authenticationMethodType);

    /**
     * Updates the authenticationMethodType
     * @param authenticationMethodType the object to be updated
     * @return AuthenticationMethodType the updated authenticationMethodType object
     */
    public void updateAuthenticationMethodType(AuthenticationMethodType authenticationMethodType);

    /**
     * Saves or updates the authenticationMethodType
     * @param authenticationMethodType the object to be saved or updated
     * @return AuthenticationMethodType the saved or updated authenticationMethodType object
     */
    public void saveOrUpdateAuthenticationMethodType(AuthenticationMethodType authenticationMethodType);

    /**
     * Deletes a authenticationMethodType from the database
     * @param authenticationMethodType the authenticationMethodType to be deleted
     */
    public void deleteAuthenticationMethodType(AuthenticationMethodType authenticationMethodType);

    /**
     * Deletes a authenticationMethodType from the database by id
     * @param authenticationMethodTypeId the authenticationMethodType's authenticationMethodTypeId
     */
    public void deleteAuthenticationMethodTypeById(Integer authenticationMethodTypeId);

}
