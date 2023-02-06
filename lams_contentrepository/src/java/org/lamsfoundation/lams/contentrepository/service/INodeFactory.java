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

import java.io.InputStream;
import java.util.UUID;

import org.lamsfoundation.lams.contentrepository.CrNode;
import org.lamsfoundation.lams.contentrepository.CrWorkspace;
import org.lamsfoundation.lams.contentrepository.dao.INodeDAO;
import org.lamsfoundation.lams.contentrepository.exception.FileException;
import org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.exception.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryRuntimeException;
import org.lamsfoundation.lams.contentrepository.exception.ValueFormatException;

public interface INodeFactory {

    /** String used to define node factory in Spring context */
    public static final String NODE_FACTORY_ID = "nodeFactory";

    /**
     * Create a new file node (which is assumed to be a newly created Spring
     * bean) with relPath and node type.
     *
     * @param relPath
     *            The path of the new Node that is to be created,
     *            the last item of this path will be the name of the new Node.
     * @throws org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException
     *             if the file parameters are invalid
     * @throws RepositoryRuntimeException
     *             if an internal error occurs.
     */
    public abstract SimpleVersionedNode createFileNode(CrWorkspace workspace, SimpleVersionedNode parentNode,
	    String relPath, InputStream istream, String filename, String mimeType, String versionDescription,
	    Integer userId)
	    throws org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;

    /**
     * Create a new package node (which is assumed to be a newly created Spring
     * bean) with the default file and node type. Package node cannot have a parent node.
     *
     * @param initialPath
     *            The path of the default content.
     * @throws org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException
     *             if the file parameters are invalid
     * @throws RepositoryRuntimeException
     *             if an internal error occurs.
     */
    public abstract SimpleVersionedNode createPackageNode(CrWorkspace workspace, String initialPath,
	    String versionDescription, Integer userId)
	    throws org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;

    /**
     * Create a new data node (which is assumed to be a newly created Spring
     * bean). This node may have a parent node.
     *
     * @throws org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException
     *             if the file parameters are invalid
     * @throws RepositoryRuntimeException
     *             if an internal error occurs.
     */
    public abstract SimpleVersionedNode createDataNode(CrWorkspace workspace, SimpleVersionedNode parentNode,
	    String versionDescription, Integer userId)
	    throws org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;

    /**
     * Build a SimpleVersionedNode, given a CrNode from the database. If versionId == null
     * then gets the latest version.
     *
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#getNode(String relPath)
     */
    public abstract SimpleVersionedNode getNode(CrNode databaseNode, Long versionId);

    /**
     * Build a SimpleVersionedNode, reading the data from the database.
     * Creates the CrNode and CrNodeVersion objects.
     * Equivalent of createFileNode/createPackageNode for existing nodes
     * Checks that the workspace found in the database is the expected workspace.
     *
     * If this node object is returned to a web app, then the
     * crNode and crNodeVersion objects will be disconnected
     * from the session, as the session will have been ended
     * with the Spring transaction.
     *
     * If versionId is null, then gets the latest version
     */
    public abstract SimpleVersionedNode getNode(Long workspaceId, Long uuid, Long versionId)
	    throws ItemNotFoundException;

    public abstract SimpleVersionedNode getNode(Long workspaceId, UUID portraitUuid, Long versionId)
	    throws ItemNotFoundException;

    /**
     * Build a SimpleVersionedNode, reading the data from the database.
     * Creates a new (empty) version ready for updating.
     * Equivalent of createFileNode/createPackageNode for existing nodes
     * Checks that the workspace found in the database is the expected workspace.
     * <p>
     * If this node object is returned to a web app, then the
     * crNode and crNodeVersion objects will be disconnected
     * from the session, as the session will have been ended
     * with the Spring transaction.
     *
     * @throws ItemNotFoundException
     *
     * @throws RepositoryRuntimeException
     *             if an internal error occurs.
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#createNewVersion(java.lang.String,
     *      java.lang.String)
     */
    public abstract SimpleVersionedNode getNodeNewVersion(Long workspaceId, Long uuid, Long versionId,
	    String versionDescription, Integer userId) throws ItemNotFoundException;

    /**
     * Copy the supplied node/version to a new node. Does not copy the history
     * of the node. Copies any child nodes of the current version. All files are duplicated.
     *
     * This method only works as we know that we have two levels of nodes - the
     * childNodes can't have their own childNodes. If this is no longer the case,
     * this method and SimpleVersionedNode.save() will need to be changed.
     *
     * @throws FileException
     *             will occur if there is a problem reading a file from the repository
     * @throws InvalidParameterException
     *             will only occur if there is an internal bug as it will only happen if the
     *             file, filename or mimetype properties are invalid.
     * @throws ValueFormatException
     *             will only occur if there is an internal bug as it will only happen if the filename or mimetype
     *             properties are not strings.
     */
    public abstract SimpleVersionedNode copy(SimpleVersionedNode originalNode, Integer userId)
	    throws FileException, ValueFormatException, InvalidParameterException;

    /**
     * @return Returns the nodeDAO.
     */
    public abstract INodeDAO getNodeDAO();

    /**
     * @param nodeDAO
     *            The nodeDAO to set.
     */
    public abstract void setNodeDAO(INodeDAO nodeDAO);

}