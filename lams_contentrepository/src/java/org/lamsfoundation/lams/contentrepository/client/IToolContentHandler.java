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

import java.io.IOException;
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
 * concrete class, which must be defined in the servlet's init parameters.
 *
 * @see org.lamsfoundation.lams.contentrepository.client.ToolContentHandler
 * @see org.lamsfoundation.lams.contentrepository.client.Download
 * @author Fiona Malikoff
 */
public interface IToolContentHandler {

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
     *         "password" field the credential.
     */
    public abstract char[] getRepositoryId();

    /**
     * Get the ticket to access the repository. If the workspace/credential
     * hasn't been set up, then it will be set up automatically.
     *
     * @param forceLogin
     *            set to true if tried to do something and got access denied. This may happen
     *            if the repository loses the ticket.
     * @return the repository ticket
     */
    public abstract ITicket getTicket(boolean forceLogin) throws RepositoryCheckedException;

    /**
     * Save a file in the content repository.
     *
     * @param stream
     *            Input filestream. Mandatory.
     * @param fileName
     *            Input filename. Mandatory.
     * @param mimeType
     *            Mimetype of file. Optional.
     * @return key to the new content repository node
     * @throws InvalidParameterException
     *             One of the mandatory parameters is missing.
     * @throws FileException
     *             An error occured writing the input stream to disk.
     * @throws RepositoryCheckedException
     *             Some other error occured.
     */
    public abstract NodeKey uploadFile(InputStream stream, String fileName, String mimeType)
	    throws RepositoryCheckedException, InvalidParameterException, RepositoryCheckedException;

    /**
     * Update an existing file in the repository. This will create a new version of this file (its version number will
     * be equal to the current one incremented by 1).
     *
     * @param uuid
     *            unique id of the updated file. Mandatory
     * 
     * @param stream
     *            Input filestream. Mandatory.
     * @param fileName
     *            Input filename. Mandatory.
     * @param mimeType
     *            Mimetype of file. Optional.
     * @return key to the new content repository node
     * @throws InvalidParameterException
     *             One of the mandatory parameters is missing.
     * @throws FileException
     *             An error occured writing the input stream to disk.
     * @throws RepositoryCheckedException
     *             Some other error occured.
     */
    public abstract NodeKey updateFile(Long uuid, InputStream stream, String fileName, String mimeType)
	    throws RepositoryCheckedException, InvalidParameterException, RepositoryCheckedException;

    /**
     * Save a directory of files in the content repository.
     * 
     * @param ticket
     *            ticket issued on login. Identifies tool and workspace - mandatory
     * @param dirPath
     *            directory path containing files - mandatory
     * @param startFile
     *            relative path of initial file - optional
     * @return nodeKey (uuid and version)
     * @throws InvalidParameterException
     *             One of the mandatory parameters is missing.
     * @throws FileException
     *             An error occured writing the files to disk.
     * @throws RepositoryCheckedException
     *             Some other error occured.
     */
    public abstract NodeKey uploadPackage(String dirPath, String startFile)
	    throws RepositoryCheckedException, InvalidParameterException, RepositoryCheckedException;

    /**
     * Delete a file node. If the node does not exist, then nothing happens (ie ItemNotFoundException is NOT thrown).
     * 
     * @param uuid
     *            id of the file node. Mandatory
     * @throws InvalidParameterException
     *             One of the mandatory parameters is missing.
     * @throws RepositoryCheckedException
     *             Some other error occured.
     */
    public abstract void deleteFile(Long uuid) throws InvalidParameterException, RepositoryCheckedException;

    /**
     * Copy an entry in the content repository.
     *
     * @param uuid
     *            id of the file node. Mandatory
     * @throws ItemNotFoundException
     *             Node to copy cannot be found
     * @throws RepositoryCheckedException
     *             Some other error occured.
     */
    public abstract NodeKey copyFile(Long uuid) throws ItemNotFoundException, RepositoryCheckedException;

    /**
     * Get a file node.
     * 
     * @param uuid
     *            id of the file node. Mandatory
     * @throws FileException
     *             An error occured writing the input stream to disk.
     * @throws ItemNotFoundException
     *             This file node does not exist, so cannot delete it.
     * @throws RepositoryCheckedException
     *             Some other error occured.
     */
    public abstract IVersionedNode getFileNode(Long uuid)
	    throws ItemNotFoundException, FileException, RepositoryCheckedException;

    /**
     * Get just the properties of a file. Convenience method - equivalent of
     * calling getFileNode(uuid).getProperties(). Useful if all you want are
     * the properties and you don't want to access the file itself.
     *
     * @param uuid
     *            id of the file node. Mandatory
     * @throws FileException
     *             An error occured writing the input stream to disk.
     * @throws ItemNotFoundException
     *             This file node does not exist, so cannot delete it.
     * @throws RepositoryCheckedException
     *             Some other error occured.
     */
    public abstract Set getFileProperties(Long uuid)
	    throws ItemNotFoundException, FileException, RepositoryCheckedException;

    public abstract IRepositoryService getRepositoryService();

    /**
     * @param repositoryService
     *            The repositoryService to set.
     */
    public abstract void setRepositoryService(IRepositoryService repositoryService);

    /**
     * Save content in repository into local file by given <code>toFileName</code>.
     *
     * <p>
     * If the <code>toFileName</code> is null, file name use original file name instead
     * and file save path will be system temporary directory.
     *
     * @param uuid
     * @param toFileName
     *            file name to save. Using the original file name instead if null value given.
     * @throws ItemNotFoundException
     * @throws RepositoryCheckedException
     * @throws IOException
     */
    public void saveFile(Long uuid, String toFileName)
	    throws ItemNotFoundException, RepositoryCheckedException, IOException;
}