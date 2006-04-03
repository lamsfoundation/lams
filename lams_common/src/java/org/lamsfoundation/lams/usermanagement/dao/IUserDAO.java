/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.Workspace;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;

/**
 * User Data Access Object (DAO) interface.
 *
 * <p>
 * <a href="IUserDAO.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public interface IUserDAO {

	
    /**
     * Gets a list of all the users.
     *
     * @return List populated list of users
     */
    public List getAllUsers();

    /**
     * Gets user object based on userId.
     * @param userId the user's userId
     * @return user populated user object
     */
    public User getUserById(Integer userId);

    /**
     * Gets user object based on login.
     * @param login the user's login
     * @return user populated user object
     */
    public User getUserByLogin(String login);

    /**
     * Gets a list of all the users based on title.
     * @param title the user's title
     * @return List populated list of users
     */
    public List getUsersByTitle(String title);
    
    /**
     * Gets a list of all the users based on first name.
     * @param firstName the user's first name
     * @return List populated list of users
     */
    public List getUsersByFirstName(String firstName);

    /**
     * Gets a list of all the users based on last name.
     * @param lastName the user's last name
     * @return List populated list of users
     */
    public List getUsersByLastName(String lastName);

    /**
     * Gets a list of all the users based on full name.
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @return List populated list of users
     */
    public List getUsersByFullName(String firstName, String lastName);

    /**
     * Gets a list of all the users based on city.
     * @param city the user's city
     * @return List populated list of users
     */
    public List getUsersByCity(String city);

    /**
     * Gets a list of all the users based on state.
     * @param state the user's state
     * @return List populated list of users
     */
    public List getUsersByState(String state);

    /**
     * Gets a list of all the users based on country.
     * @param country the user's country
     * @return List populated list of users
     */
    public List getUsersByCountry(String country);

    /**
     * Gets a list of all the users based on disabledFlag.
     * @param disabledFlag the user's disabledFlag
     * @return List populated list of users
     */
    public List getUsersByDisabledFlag(Boolean disabledFlag);
    
    /**
     * Gets a list of all the users based on workspace.
     * @param workspace the user's workspace
     * @return List populated list of users
     */
    public List getUsersByWorkspace(Workspace workspace);

    /**
     * Gets a list of all the users based on authenticationMethod.
     * @param authenticationMethod the user's authenticationMethod
     * @return List populated list of users
     */
    public List getUsersByAuthenticationMethod(AuthenticationMethod authenticationMethod);
    
    /**
     * Saves the user
     * @param user the object to be saved
     * @return User the saved user object
     */
    public void saveUser(User user);

    /**
     * Updates the user
     * @param user the object to be updated
     * @return User the updated user object
     */
    public void updateUser(User user);

    /**
     * Saves or updates the user
     * @param user the object to be saved or updated
     * @return User the saved or updated user object
     */
    public void saveOrUpdateUser(User user);

    /**
     * Deletes a user from the database
     * @param user the user to be deleted
     */
    public void deleteUser(User user);

    /**
     * Deletes a user from the database by id
     * @param userId the user's userId
     */
    public void deleteUserById(Integer userId);

    /**
     * Deletes a user from the database by login
     * @param login the user's login
     */
    public void deleteUserByLogin(String login);

    /**
     * Updates user's password
     * @param login the user's login
     * @param newPassword the user's new password
     */
    public void updatePassword(String login, String newPassword);    
}
