/*
 * Created on Jan 11, 2005
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
	
	/**
	 * 
	 */
	public NodeKey(Long uuid, Long version) {
		this.uuid = uuid;
		this.version = version;
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
	
    public String toString() {
        return new ToStringBuilder(this)
            .append("uuid", uuid)
            .append("version", version)
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof NodeKey) ) return false;
        NodeKey castOther = (NodeKey) other;
        return new EqualsBuilder()
            .append(uuid, castOther.getUuid())
            .append(version, castOther.getVersion())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(uuid)
            .append(version)
            .toHashCode();
    }	
}
