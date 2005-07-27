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
package org.lamsfoundation.lams.contentrepository.client;

import java.io.InputStream;
import java.util.Set;

import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;

/**
 * IToolContentHandler defines the ContentHandler interface used by the tools.
 * This interface exists so that the ToolDownload servlet can get to the 
 * Repository via ToolContentHandler. It needs to call the tool's 
 * concrete class,  which must be defined in the Spring context as "toolContentHandler" 
 * (see SPRING_BEAN_NAME). 
 * 
 * @see org.lamsfoundation.lams.contentrepository.client.ToolContentHandler
 * @see org.lamsfoundation.lams.contentrepository.client.Download
 * @author Fiona Malikoff
 */
public interface IToolContentHandler {
    /** File is for Online Instructions */
    public final static String TYPE_ONLINE = "ONLINE";

    /** File is for Offline Instructions */
    public final static String TYPE_OFFLINE = "OFFLINE";

    /** The "name" used to store the online/offline property in the repository */
    public final static String FILE_TYPE_PROPERTY_NAME = "TYPE";

    /** The concrete implementation must be configured as a bean in Spring, using 
     * this value as the name. */
    public final static String SPRING_BEAN_NAME = "toolContentHandler";

    /**
     * @return Returns the repositoryWorkspaceName.
     */
    public abstract String getRepositoryWorkspaceName();

    /**
     * @return Returns the repositoryUser.
     */
    public abstract String getRepositoryUser();

    /**
     * @return Returns the repository identification string. This is the
     * "password" field the credential.
     */
    public abstract char[] getRepositoryId();

    /**
     * Get the ticket to access the repository. If the workspace/credential
     * hasn't been set up, then it will be set up automatically.
     * 
     * @forceLogin set to true if tried to do something and got access denied. This may happen
     * if the repository loses the ticket.
     * @return the repository ticket 
     */
    public abstract ITicket getTicket(boolean forceLogin)
            throws RepositoryCheckedException;

    /**
     * Save a file in the content repository.
     * 
     * @param stream Input filestream. Mandatory.
     * @param fileName Input filename. Mandatory.
     * @param mimeType Mimetype of file. Optional.
     * @param fileProperty is this for online or offline instructions? Should be TYPE_ONLINE or TYPE_OFFLINE. Mandatory.
     * @return key to the new content repository node
     * @throws InvalidParameterException One of the mandatory parameters is missing.
     * @throws FileException An error occured writing the input stream to disk.
     * @throws RepositoryCheckedException Some other error occured.
     */
    public abstract NodeKey uploadFile(InputStream stream, String fileName,
            String mimeType, String fileProperty)
            throws RepositoryCheckedException, InvalidParameterException,
            RepositoryCheckedException;

    /** 
     * Delete a file node.  If the node does not exist, then nothing happens (ie ItemNotFoundException is NOT thrown). 
     * @param uuid id of the file node. Mandatory
     * @throws InvalidParameterException One of the mandatory parameters is missing.
     * @throws RepositoryCheckedException Some other error occured.
     */
    public abstract void deleteFile(Long uuid)
            throws InvalidParameterException, RepositoryCheckedException;

    /** Get a file node. 
     * @param uuid id of the file node. Mandatory
     * @throws FileException An error occured writing the input stream to disk.
     * @throws ItemNotFoundException This file node does not exist, so cannot delete it.
     * @throws RepositoryCheckedException Some other error occured.
     */
    public abstract IVersionedNode getFileNode(Long uuid)
            throws ItemNotFoundException, FileException,
            RepositoryCheckedException;

    /** Get just the properties of a file. Convenience method - equivalent of 
     * calling getFileNode(uuid).getProperties(). Useful if all you want are 
     * the properties and you don't want to access the file itself.
     * 
     * @param uuid id of the file node. Mandatory
     * @throws FileException An error occured writing the input stream to disk.
     * @throws ItemNotFoundException This file node does not exist, so cannot delete it.
     * @throws RepositoryCheckedException Some other error occured.
     */
    public abstract Set getFileProperties(Long uuid)
            throws ItemNotFoundException, FileException,
            RepositoryCheckedException;

    public abstract boolean isOffline(IVersionedNode node);

    public abstract boolean isOnline(IVersionedNode node);

    public abstract IRepositoryService getRepositoryService();

    /**
     * @param repositoryService The repositoryService to set.
     */
    public abstract void setRepositoryService(
            IRepositoryService repositoryService);
}