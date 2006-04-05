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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	
package org.lamsfoundation.lams.contentrepository.service;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.contentrepository.ITicket;

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
