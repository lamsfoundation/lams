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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents the two part key - UUID and version.
 * Version may be null;
 *
 * @author Fiona Malikoff
 */
public class NodeKey {

    private Long uuid = null;
    private Long version = null;
    private String portraitUuid;

    /**
     *
     */
    public NodeKey(Long uuid, Long version, String portraitUuid) {
	this.uuid = uuid;
	this.version = version;
	this.portraitUuid = portraitUuid;
    }

    /**
     * @return Returns the uuid.
     */
    public Long getUuid() {
	return uuid;
    }

    /**
     * @return Returns the version.
     */
    public Long getVersion() {
	return version;
    }

    public String getPortraitUuid() {
	return portraitUuid;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uuid", uuid).append("version", version)
		.append("portraitUuid", portraitUuid).toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if (!(other instanceof NodeKey)) {
	    return false;
	}
	NodeKey castOther = (NodeKey) other;
	return new EqualsBuilder().append(uuid, castOther.getUuid()).append(version, castOther.getVersion()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uuid).append(version).toHashCode();
    }
}
