/*
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


package org.lamsfoundation.lams.contentrepository.service;

import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.exception.WorkspaceNotFoundException;

/**
 * Administrative interface to the Repository. "Protects"
 * the assignCredentials method and isTicketOkay method.
 */
public interface IRepositoryAdmin extends IRepositoryService {

    public void assignCredentials(ICredentials credentials, String workspaceName)
	    throws RepositoryCheckedException, WorkspaceNotFoundException;

    /**
     * Is this ticket acceptable to the repository?
     * <p>
     * Only the interceptor for an IRepositoryAdmin should call this!!!!!!
     * <p>
     * Do NOT declare this method as a transaction otherwise we will
     * end up with an endless loop of forever trying to check if
     * the ticket is okay as this method would end up getting
     * checked itself.
     */
    public boolean isTicketOkay(ITicket ticket);

}
