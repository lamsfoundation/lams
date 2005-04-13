/*
 * Created on Mar 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.usermanagement.dto;

import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UserDTO {
	
	private Integer userID;
	private String firstName;
	private String lastName;
	private String login;
	

	public UserDTO(Integer userID, String firstName, String lastName,
			String login) {		
		this.userID = userID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.login = login;
	}
	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName!=null?firstName:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return lastName!=null?lastName:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the login.
	 */
	public String getLogin() {
		return login!=null?login:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the userID.
	 */
	public Integer getUserID() {
		return userID;
	}
}
