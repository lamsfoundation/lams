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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.admin.web.dto;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author jliew
 *
 */
public class V1UserDTO {

	private String login;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private List<String> roleIds;
	private List<V1OrgRightDTO> orgRights;
	
	public static final String ROLE_ANONYMOUS = "0";
	public static final String ROLE_ADMINISTRATOR = "1";
	public static final String ROLE_CORE = "2";
	public static final String ROLE_LEARNER = "3";
	public static final String ROLE_STAFF = "4";
	public static final String ROLE_AUTHOR = "5";
	
	public V1UserDTO() {
	}
	
	public V1UserDTO(String login) {
		this.login = login;
	}
	
	public V1UserDTO(String login, String password, String firstName, String lastName) {
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public int getRolesSize() {
		return roleIds.size();
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<String> getRoleIds() {
		return roleIds;
	}
	
	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}
	
	public List<V1OrgRightDTO> getOrgRights() {
		return orgRights;
	}
	
	public void setOrgRights(List<V1OrgRightDTO> orgRights) {
		this.orgRights = orgRights;
	}
	
	public boolean equals(Object userDTO) {
		if (userDTO instanceof V1UserDTO) {
			String login = ((V1UserDTO)userDTO).getLogin();
			if (StringUtils.equals(login, this.login)) {
				return true;
			}
		}
		return false;
	}
}
