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

import java.util.ArrayList;

import org.lamsfoundation.lams.admin.web.dto.UserBean;

/**
 * @author jliew
 *
 * This is the Action Form that is used when a user has added users
 * to an organisation via UserOrgForm.  Used to record roles for each
 * of the added users.
 *
 */

/**
 *
 */
public class UserOrgRoleForm {

    private ArrayList userBeans = new ArrayList();
    private Integer orgId;
    private boolean addToLessons;

    public ArrayList getUserBeans() {
	return userBeans;
    }

    public void setUserBeans(ArrayList userBeans) {
	this.userBeans = userBeans;
    }

    public Integer getOrgId() {
	return orgId;
    }

    public void setOrgId(Integer orgId) {
	this.orgId = orgId;
    }

    public void addUserBean(UserBean userBean) {
	userBeans.add(userBean);
    }

    public boolean isAddToLessons() {
	return addToLessons;
    }

    public void setAddToLessons(boolean addToLessons) {
	this.addToLessons = addToLessons;
    }
}