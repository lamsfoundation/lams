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


package org.lamsfoundation.lams.admin.web.dto;

/**
 * Bean used as member of UserOrgRoleForm, representing a user's roles.
 *         
 * @author jliew
 */
public class UserBean {

    private Integer userId;
    private String login;
    private String[] roleIds = {};
    private Boolean memberOfParent;

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

    public String[] getRoleIds() {
	return this.roleIds;
    }

    public void setRoleIds(String[] roleIds) {
	this.roleIds = roleIds;
    }

    public Boolean getMemberOfParent() {
	return this.memberOfParent;
    }

    public void setMemberOfParent(Boolean memberOfParent) {
	this.memberOfParent = memberOfParent;
    }

}
