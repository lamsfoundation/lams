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
public interface IAuthenticationMethodDAO extends IBaseDAO{

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
     * Deletes a authenticationMethod from the database by id
     * @param authenticationMethodId the authenticationMethod's authenticationMethodId
     */
    public void deleteAuthenticationMethodById(Integer authenticationMethodId);

}
