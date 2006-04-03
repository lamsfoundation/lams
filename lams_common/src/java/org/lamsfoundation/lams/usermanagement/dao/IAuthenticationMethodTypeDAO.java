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
