/*
 * Created on Dec 2, 2004
 *
 * Last modified on Dec 2, 2004
 */
package org.lamsfoundation.lams.usermanagement.dao;

import java.util.List;

import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * AuthenticationMethod Data Access Object (DAO) interface.
 *
 * <p>
 * <a href="IAuthenticationMethodDAO.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public interface IAuthenticationMethodDAO {

    /**
     * Gets a list of all the authenticationMethods.
     *
     * @return List populated list of authenticationMethods
     */
    public List getAllAuthenticationMethods();

    /**
     * Gets authenticationMethod object based on authenticationMethodId.
     * @param authenticationMethodId the authenticationMethod's authenticationMethodId
     * @return authenticationMethod populated authenticationMethod object
     */
    public AuthenticationMethod getAuthenticationMethodById(Integer authenticationMethodId);

    /**
     * Gets authenticationMethod object based on its name.
     * @param name the authenticationMethod's name
     * @return authenticationMethod populated authenticationMethod object
     */
    public AuthenticationMethod getAuthenticationMethodByName(String name);
    
    /**
     * Gets authenticationMethod object based on user.
     * @param user the user
     * @return authenticationMethod populated authenticationMethod object
     */
    public AuthenticationMethod getAuthenticationMethodByUser(User user);
    
    /**
     * Saves the authenticationMethod
     * @param authenticationMethod the object to be saved
     * @return AuthenticationMethod the saved authenticationMethod object
     */
    public void saveAuthenticationMethod(AuthenticationMethod authenticationMethod);

    /**
     * Updates the authenticationMethod
     * @param authenticationMethod the object to be updated
     * @return AuthenticationMethod the updated authenticationMethod object
     */
    public void updateAuthenticationMethod(AuthenticationMethod authenticationMethod);

    /**
     * Saves or updates the authenticationMethod
     * @param authenticationMethod the object to be saved or updated
     * @return AuthenticationMethod the saved or updated authenticationMethod object
     */
    public void saveOrUpdateAuthenticationMethod(AuthenticationMethod authenticationMethod);

    /**
     * Deletes a authenticationMethod from the database
     * @param authenticationMethod the authenticationMethod to be deleted
     */
    public void deleteAuthenticationMethod(AuthenticationMethod authenticationMethod);

    /**
     * Deletes a authenticationMethod from the database by id
     * @param authenticationMethodId the authenticationMethod's authenticationMethodId
     */
    public void deleteAuthenticationMethodById(Integer authenticationMethodId);

}
