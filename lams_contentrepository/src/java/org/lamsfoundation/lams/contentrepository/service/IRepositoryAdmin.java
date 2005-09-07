/* 
  Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt 
*/

package org.lamsfoundation.lams.contentrepository.service;

import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;

/**
 * Administrative interface to the Repository. "Protects" 
 * the assignCredentials method and isTicketOkay method.
 */
public interface IRepositoryAdmin extends IRepositoryService {

    public void assignCredentials(ICredentials credentials, String workspaceName)
		throws RepositoryCheckedException, WorkspaceNotFoundException;
    
    /** Is this ticket acceptable to the repository? 
     * <p>
     * Only the interceptor for an IRepositoryAdmin should call this!!!!!!
     * <p>
     * Do NOT declare this method as a transaction otherwise we will
     * end up with an endless loop of forever trying to check if 
     * the ticket is okay as this method would end up getting
     * checked itself. */
    public boolean isTicketOkay(ITicket ticket);

}
