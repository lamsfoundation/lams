/*
 * Created on Dec 21, 2004
 */
package org.lamsfoundation.lams.contentrepository;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * SimpleTicket is a basic implementation of Ticket. 
 */
public class SimpleTicket implements ITicket {

	private String ticketId = null;
	private Long workspaceId = null;

	protected SimpleTicket(Long workspaceId) {
		this.workspaceId = workspaceId;
		this.ticketId = System.currentTimeMillis() + "_" + workspaceId
			+ Math.random();
	}
	
	/** Get the workspace associated with this ticket
	 * Should only be accessed by the content repository package members.
	 */
	public Long getWorkspaceId() {
		return workspaceId;
	}

	/**
	 * @return Returns the ticketId.
	 */
	public String getTicketId() {
		return ticketId;
	}

	/** Make this ticket unusable. Called by the repository on logout */
	public void clear() {
		workspaceId = null;
		ticketId = null;
	}
	
	   public String toString() {
        return new ToStringBuilder(this)
            .append("workspaceId", getWorkspaceId())
            .append("ticketId", getTicketId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof SimpleTicket) ) return false;
        SimpleTicket castOther = (SimpleTicket) other;
        return new EqualsBuilder()
            .append(this.getWorkspaceId(), castOther.getWorkspaceId())
            .append(this.getTicketId(), castOther.getTicketId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getWorkspaceId())
            .append(getTicketId())
            .toHashCode();
    }
	
}
