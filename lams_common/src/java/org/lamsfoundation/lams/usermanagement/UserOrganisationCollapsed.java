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
package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;

/**
 * @author jliew
 *
 */
public class UserOrganisationCollapsed implements Serializable {

    private Integer userOrganisationId;
    private Boolean collapsed;
    private UserOrganisation userOrganisation;

    public UserOrganisationCollapsed() {
    }

    public UserOrganisationCollapsed(Boolean collapsed, UserOrganisation uo) {
	this.userOrganisation = uo;
	this.collapsed = collapsed;
    }

    public Integer getUserOrganisationId() {
	return this.userOrganisationId;
    }

    public void setUserOrganisationId(Integer userOrganisationId) {
	this.userOrganisationId = userOrganisationId;
    }

    public Boolean getCollapsed() {
	return this.collapsed;
    }

    public void setCollapsed(Boolean collapsed) {
	this.collapsed = collapsed;
    }

    public UserOrganisation getUserOrganisation() {
	return this.userOrganisation;
    }

    public void setUserOrganisation(UserOrganisation userOrganisation) {
	this.userOrganisation = userOrganisation;
    }
}
