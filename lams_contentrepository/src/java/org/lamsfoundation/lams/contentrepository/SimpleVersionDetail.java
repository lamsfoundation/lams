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


package org.lamsfoundation.lams.contentrepository;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryRuntimeException;

/**
 * Describes the version details - for displaying a version
 * history. Thise is a transient object.
 *
 * @author Fiona Malikoff
 */
public class SimpleVersionDetail implements IVersionDetail, Comparable {

    private Long versionId;
    private Date createdDateTime;
    private String description;

    /**
     * Create the detail object with all the necessary values.
     * 
     * @param versionId
     *            mandatory
     * @param createdDateTime
     *            mandatory
     * @param description
     *            optional, defaults to "" (empty string)
     * @throws RepositoryRuntimeException
     *             if versionId or createdDateTime is missing
     */
    protected SimpleVersionDetail(Long versionId, Date createdDateTime, String description) {

	if (versionId == null) {
	    throw new RepositoryRuntimeException("Version id missing, unable to create SimpleVersionDetail object.");
	}
	this.versionId = versionId;

	if (createdDateTime == null) {
	    throw new RepositoryRuntimeException(
		    "Created date time is missing, unable to create SimpleVersionDetail object.");
	}
	this.createdDateTime = createdDateTime;

	this.description = description != null ? description : "";
    }

    /**
     * @return Returns the createdDateTime.
     */
    @Override
    public Date getCreatedDateTime() {
	return createdDateTime;
    }

    /**
     * @return Returns the description.
     */
    @Override
    public String getDescription() {
	return description;
    }

    /**
     * @return Returns the versionId.
     */
    @Override
    public Long getVersionId() {
	return versionId;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("versionId", getVersionId())
		.append("createdDateTime", getCreatedDateTime()).append("description", getDescription()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if (!(other instanceof SimpleVersionDetail)) {
	    return false;
	}
	SimpleVersionDetail castOther = (SimpleVersionDetail) other;
	return new EqualsBuilder().append(this.getVersionId(), castOther.getVersionId())
		.append(this.getCreatedDateTime(), castOther.getCreatedDateTime())
		.append(this.getDescription(), castOther.getDescription()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getVersionId()).append(getCreatedDateTime()).append(getDescription())
		.toHashCode();
    }

    /**
     * Compares this object with the specified object for order.
     * Returns a negative integer, zero, or a positive integer as this
     * object is less than, equal to, or greater than the specified
     * object. If o is null, then -1 is returned.
     *
     * Compares version id first, then if both equals compares date.
     * If still both equals, compares strings. This means the comparator
     * should be compatible with the equals method
     */
    @Override
    public int compareTo(Object o) throws ClassCastException {
	IVersionDetail vdo = (IVersionDetail) o;
	if (vdo == null) {
	    return -1;
	} else {
	    int compValue = this.getVersionId().compareTo(vdo.getVersionId());
	    if (compValue != 0) {
		return compValue;
	    } else {
		compValue = this.getCreatedDateTime().compareTo(vdo.getCreatedDateTime());
		if (compValue != 0) {
		    return compValue;
		} else {
		    return this.getDescription().compareTo(vdo.getDescription());
		}
	    }
	}
    }

}
