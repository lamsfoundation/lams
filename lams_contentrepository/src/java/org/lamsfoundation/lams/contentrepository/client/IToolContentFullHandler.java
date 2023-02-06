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


package org.lamsfoundation.lams.contentrepository.client;

import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.exception.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;

/**
 * IToolContentHandler defines the ContentHandler interface used by the tools.
 * This interface exists so that the ToolDownload servlet can get to the
 * Repository via ToolContentHandler. It needs to call the tool's
 * concrete class, which must be defined in the servlet's init parameters.
 *
 * @see org.lamsfoundation.lams.contentrepository.client.ToolContentHandler
 * @see org.lamsfoundation.lams.contentrepository.client.Download
 * @author Fiona Malikoff
 */
public interface IToolContentFullHandler extends IToolContentHandler {

    /**
     * Get the ticket to access the repository. If the workspace/credential
     * hasn't been set up, then it will be set up automatically.
     *
     * @param forceLogin
     *            set to true if tried to do something and got access denied. This may happen
     *            if the repository loses the ticket.
     * @return the repository ticket
     */
    public ITicket getTicket(boolean forceLogin) throws RepositoryCheckedException;

    /**
     * Copy an entry in the content repository.
     *
     * @param nodeId
     *            id of the file node. Mandatory
     * @throws ItemNotFoundException
     *             Node to copy cannot be found
     * @throws RepositoryCheckedException
     *             Some other error occured.
     */
    public NodeKey copyFile(Long nodeId) throws ItemNotFoundException, RepositoryCheckedException;

    public IRepositoryService getRepositoryService();
}