/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
package org.lamsfoundation.lams.usermanagement.dto;

import org.lamsfoundation.lams.themes.CSSThemeVisualElement;
import org.lamsfoundation.lams.themes.dto.CSSThemeBriefDTO;
/**
 * @author Manpreet Minhas
 */
public class UserDTO {
	
	private Integer userID;
	private String firstName;
	private String lastName;
	private String login;
    private String email;
   // private CSSThemeVisualElement theme;
    private CSSThemeBriefDTO theme;
	

//	public UserDTO(Integer userID, String firstName, String lastName,
//			String login, String email, CSSThemeVisualElement theme) {		
//		this.userID = userID;
//		this.firstName = firstName;
//		this.lastName = lastName;
//		this.login = login;
//		this.email = email;
//		this.theme = theme;
//	}
	
	public UserDTO(Integer userID, String firstName, String lastName,
			String login, String email, CSSThemeBriefDTO theme) {		
		this.userID = userID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.login = login;
		this.email = email;
		this.theme = theme;
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


	public CSSThemeBriefDTO getTheme() {
		return theme;
	}


	public void setTheme(CSSThemeBriefDTO theme) {
		this.theme = theme;
	}
	
//	public CSSThemeVisualElement getTheme() {
//		return theme;
//	}
//	
//	public void setTheme(CSSThemeVisualElement theme) {
//		this.theme = theme;
//	}
	
	
}
