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

/**
 * Ticket represents the "authorisation" key to the repository. When
 * the tool logs in, a ticket is given. This ticket must be supplied
 * whenever a node is accessed. When the tool is finished, it should call
 * logout, which invalidates the ticket.
 *
 * A ticket is for one workspace only - to access more than one
 * workspace requires more than one ticket.
 *
 * @author Fiona Malikoff
 */
public interface ITicket {
    /**
     * Get the workspace associated with this ticket
     * Should only be accessed by the content repository package members.
     */
    abstract Long getWorkspaceId();

    /**
     * @return Returns the ticketId.
     */
    abstract String getTicketId();

    /** Make this ticket unusable. Called by the repository on logout */
    abstract void clear();
}