package org.lamsfoundation.lams.contentrepository;
/**
 * Administrative interface to the Repository. "Protects" 
 * the assignCredentials method and isTicketOkay method.
 */
public interface IRepositoryAdmin extends IRepository {

    public void assignCredentials(ICredentials credentials, String workspaceName)
		throws RepositoryCheckedException, WorkspaceNotFoundException;
    
    /** Is this ticket acceptable to the repository? 
     * Only the interceptor for an IRepositoryAdmin should call this!!!!!!
     * 
     * Do NOT declare this method as a transaction otherwise we will
     * end up with an endless loop of forever trying to check if 
     * the ticket is okay as this method would end up getting
     * checked itself. */
    public boolean isTicketOkay(ITicket ticket);

}
