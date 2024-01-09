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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.usermanagement.dto;

import org.lamsfoundation.lams.usermanagement.service.IUserDetails;

import java.util.Comparator;

/**
 * @author Mitchell Seaton
 */
public class UserBasicDTO implements IUserDetails {

    private Integer userID;
    private String firstName;
    private String lastName;
    private String login;

    public UserBasicDTO(Integer userID, String firstName, String lastName, String login) {
	this.userID = userID;
	this.firstName = firstName;
	this.lastName = lastName;
	this.login = login;
    }

    /**
     * Equality test of UserDTO objects
     */
    @Override
    public boolean equals(Object o) {
	if ((o != null) && (o.getClass() == this.getClass())) {
	    return ((UserBasicDTO) o).userID == this.userID;
	} else {
	    return false;
	}
    }

    /**
     * Returns the hash code value for this object.
     */
    @Override
    public int hashCode() {
	// TODO this might be an ineffcient implementation since userIDs are likely to be sequential,
	// hence we dont get a good spread of values
	return userID.intValue();
    }

    /**
     * @return Returns the firstName.
     */
    public String getFirstName() {
	return firstName;
    }

    /**
     * @return Returns the lastName.
     */
    public String getLastName() {
	return lastName;
    }

    /**
     * @return Returns the login.
     */
    public String getLogin() {
	return login;
    }

    /**
     * @return Returns the userID.
     */
    public Integer getUserID() {
	return userID;
    }

}