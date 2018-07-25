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

import java.util.Comparator;

/**
 * @author Mitchell Seaton
 */
public class UserBasicDTO {

    // Compares first, then last name, then login
    public static final Comparator<UserBasicDTO> USER_BASIC_DTO_COMPARATOR = new Comparator<UserBasicDTO>() {
	@Override
	public int compare(UserBasicDTO o1, UserBasicDTO o2) {
	    if ((o1 == null) || (o2 == null)) {
		return compareNull(o1, o2);
	    } else {
		int retValue = compareFirstName(o1, o2);
		if (retValue == 0) {
		    retValue = compareLastName(o1, o2);
		}
		if (retValue == 0) {
		    retValue = compareLogin(o1, o2);
		}
		return retValue;
	    }
	}

	private int compareLastName(UserBasicDTO o1, UserBasicDTO o2) {
	    String surname1 = o1.getLastName();
	    String surname2 = o2.getLastName();

	    int retValue = -1;
	    if ((surname1 == null) || (surname2 == null)) {
		retValue = compareNull(surname1, surname2);
	    } else {
		retValue = surname1.compareTo(surname2);
	    }
	    return retValue;
	}

	private int compareFirstName(UserBasicDTO o1, UserBasicDTO o2) {
	    String name1 = o1.getFirstName();
	    String name2 = o2.getFirstName();

	    int retValue = -1;
	    if ((name1 == null) || (name2 == null)) {
		retValue = compareNull(name1, name2);
	    } else {
		retValue = name1.compareTo(name2);
	    }
	    return retValue;
	}

	private int compareLogin(UserBasicDTO o1, UserBasicDTO o2) {
	    String name1 = o1.getLogin();
	    String name2 = o2.getLogin();

	    int retValue = -1;
	    if ((name1 == null) || (name2 == null)) {
		retValue = compareNull(name1, name2);
	    } else {
		retValue = name1.compareTo(name2);
	    }
	    return retValue;
	}

	private int compareNull(Object o1, Object o2) {
	    if (o1 == null) {
		return (o2 != null ? -1 : 0);
	    } else {
		return 1;
	    }
	}
    };

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
