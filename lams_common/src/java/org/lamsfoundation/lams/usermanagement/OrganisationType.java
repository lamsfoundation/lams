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
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class OrganisationType implements Serializable {

    public static final Integer ROOT_TYPE = 1;
    public static final Integer COURSE_TYPE = 2;
    public static final Integer CLASS_TYPE = 3;

    public static final String ROOT_DESCRIPTION = "ROOT ORGANISATION";
    public static final String COURSE_DESCRIPTION = "COURSE ORGANISATION";
    public static final String CLASS_DESCRIPTION = "CLASS ORGANISATION";

    /** identifier field */
    private Integer organisationTypeId;

    /** persistent field */
    private String name;

    /** persistent field */
    private String description;

    /** persistent field */
    private Set organisations;

    /** full constructor */
    public OrganisationType(String name, String description, Set organisations) {
	this.name = name;
	this.description = description;
	this.organisations = organisations;
    }

    /** default constructor */
    public OrganisationType() {
    }

    public Integer getOrganisationTypeId() {
	return this.organisationTypeId;
    }

    public void setOrganisationTypeId(Integer organisationTypeId) {
	this.organisationTypeId = organisationTypeId;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return this.description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Set getOrganisations() {
	return this.organisations;
    }

    public void setOrganisations(Set organisations) {
	this.organisations = organisations;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("organisationTypeId", getOrganisationTypeId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof OrganisationType)) {
	    return false;
	}
	OrganisationType castOther = (OrganisationType) other;
	return new EqualsBuilder().append(this.getOrganisationTypeId(), castOther.getOrganisationTypeId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getOrganisationTypeId()).toHashCode();
    }

}
