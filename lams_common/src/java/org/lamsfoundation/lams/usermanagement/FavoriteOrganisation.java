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

package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class FavoriteOrganisation implements Serializable {

    /** identifier field */
    private Integer favoriteOrganisationId;

    /** persistent field */
    private User user;

    /** persistent field */
    private Organisation organisation;

    /** full constructor */
    public FavoriteOrganisation(User user, Organisation organisation) {
	this.user = user;
	this.organisation = organisation;
    }

    /** default constructor */
    public FavoriteOrganisation() {
    }

    public Integer getFavoriteOrganisationId() {
	return this.favoriteOrganisationId;
    }

    public void setFavoriteOrganisationId(Integer userOrganisationId) {
	this.favoriteOrganisationId = userOrganisationId;
    }

    public User getUser() {
	return this.user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Organisation getOrganisation() {
	return this.organisation;
    }

    public void setOrganisation(Organisation organisation) {
	this.organisation = organisation;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("favoriteOrganisationId", getFavoriteOrganisationId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof FavoriteOrganisation)) {
	    return false;
	}
	FavoriteOrganisation castOther = (FavoriteOrganisation) other;
	return new EqualsBuilder().append(this.getFavoriteOrganisationId(), castOther.getFavoriteOrganisationId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getFavoriteOrganisationId()).toHashCode();
    }

}
