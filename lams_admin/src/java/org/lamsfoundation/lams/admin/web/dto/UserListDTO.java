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

import java.util.List;

import org.lamsfoundation.lams.usermanagement.dto.UserManageBean;

/**
 * @author Jun-Dir Liew
 *
 *         Created at 13:47:02 on 9/06/2006
 */
public class UserListDTO {
    private List<UserManageBean> userManageBeans;
    private Integer orgId;
    private String orgCode;
    private String orgName;
    private Boolean courseAdminCanAddNewUsers;
    private Boolean courseAdminCanBrowseAllUsers;
    private Boolean canResetOrgPassword;
    private Boolean canEditRole;

    public List<UserManageBean> getUserManageBeans() {
	return userManageBeans;
    }

    public void setUserManageBeans(List<UserManageBean> userManageBeans) {
	this.userManageBeans = userManageBeans;
    }

    public Integer getOrgId() {
	return orgId;
    }

    public void setOrgId(Integer orgId) {
	this.orgId = orgId;
    }

    public String getOrgName() {
	return orgName;
    }

    public void setOrgName(String orgName) {
	this.orgName = orgName;
    }

    public Boolean getCourseAdminCanAddNewUsers() {
	return courseAdminCanAddNewUsers;
    }

    public void setCourseAdminCanAddNewUsers(Boolean courseAdminCanAddNewUsers) {
	this.courseAdminCanAddNewUsers = courseAdminCanAddNewUsers;
    }

    public Boolean getCourseAdminCanBrowseAllUsers() {
	return courseAdminCanBrowseAllUsers;
    }

    public void setCourseAdminCanBrowseAllUsers(Boolean courseAdminCanBrowseAllUsers) {
	this.courseAdminCanBrowseAllUsers = courseAdminCanBrowseAllUsers;
    }

    public Boolean getCanResetOrgPassword() {
	return canResetOrgPassword;
    }

    public void setCanResetOrgPassword(Boolean canResetOrgPassword) {
	this.canResetOrgPassword = canResetOrgPassword;
    }

    public Boolean getCanEditRole() {
        return canEditRole;
    }

    public void setCanEditRole(Boolean canEditRole) {
        this.canEditRole = canEditRole;
    }

    public String getOrgCode() {
	return orgCode;
    }

    public void setOrgCode(String orgCode) {
	this.orgCode = orgCode;
    }

}