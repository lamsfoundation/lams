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

package org.lamsfoundation.lams.admin.web.form;

public class UserOrgForm {

    private Integer orgId;

    private String orgCode;

    private String orgName;

    private String userIds;

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

    public String getUserIds() {
	return userIds;
    }

    public void setUserIds(String userIds) {
	this.userIds = userIds;
    }

    public String getOrgCode() {
	return orgCode;
    }

    public void setOrgCode(String orgCode) {
	this.orgCode = orgCode;
    }

}
