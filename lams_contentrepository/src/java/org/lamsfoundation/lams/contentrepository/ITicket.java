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
	/** Get the workspace associated with this ticket
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