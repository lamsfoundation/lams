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

package org.lamsfoundation.lams.admin.web.form;

public class UserRolesForm {

    private Integer orgId;

    private Integer userId;

    private String[] roles; // String []

    private boolean addToLessons;

    public Integer getOrgId() {
	return orgId;
    }

    public void setOrgId(Integer orgId) {
	this.orgId = orgId;
    }

    public Integer getUserId() {
	return userId;
    }

    public void setUserId(Integer userId) {
	this.userId = userId;
    }

    public String[] getRoles() {
	return roles;
    }

    public void setRoles(String[] roles) {
	this.roles = roles;
    }

    public boolean isAddToLessons() {
	return addToLessons;
    }

    public void setAddToLessons(boolean addToLessons) {
	this.addToLessons = addToLessons;
    }
}