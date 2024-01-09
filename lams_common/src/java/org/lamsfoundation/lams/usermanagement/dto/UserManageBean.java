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

import org.lamsfoundation.lams.usermanagement.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jun-Dir Liew
 *
 * 	Created at 13:34:33 on 9/06/2006
 */
public class UserManageBean implements Comparable {

    private Integer userId;
    private String login;
    private String email;
    private String title;
    private String firstName;
    private String lastName;
    private List<Role> roles = new ArrayList<Role>();
    ;

    public Integer getUserId() {
	return this.userId;
    }

    public void setUserId(Integer userId) {
	this.userId = userId;
    }

    public String getLogin() {
	return this.login;
    }

    public void setLogin(String login) {
	this.login = login;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getTitle() {
	return this.title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getFirstName() {
	return this.firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return this.lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public List<Role> getRoles() {
	return this.roles;
    }

    public void setRoles(List<Role> roles) {
	this.roles = roles;
    }

    @Override
    public boolean equals(Object otherBean) {
	if (!(otherBean instanceof UserManageBean)) {
	    return false;
	}
	return (userId.equals(((UserManageBean) otherBean).getUserId()));
    }

    @Override
    public int compareTo(Object o) {
	UserManageBean u = (UserManageBean) o;
	return login.compareTo(u.getLogin());
    }

}