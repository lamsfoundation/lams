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

import java.io.InputStream;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;

import org.lamsfoundation.lams.contentrepository.exception.FileException;
import org.lamsfoundation.lams.contentrepository.exception.ItemNotFoundException;

/**
 * The IVersionedNode interface represents a particular version of a node
 * in the hierarchy that makes up the repository. This interface represents
 * a very restricted version of a node - they are read only methods
 * so the node cannot be updated - only a new version may be created.
 *
 * A VersionedNode may have at most one file attached (ie one per version).
 */
public interface IVersionedNode {

    /** Get the uuid and version of this node/version */
    public NodeKey getNodeKey();

    /**
     * Returns the node at relPath, which is a child of this node.
     *
     * @param relPath
     *            The relative path of the node to retrieve.
     * @return The node
     * @throws ItemNotFoundException
     *             If no node exists at the
     *             specified path.
     * @throws RepositoryException
     *             If another error occurs.
     */
    public IVersionedNode getNode(String relPath) throws ItemNotFoundException;

    /**
     * Returns an Set of all child nodes of node. Returns an empty
     * set if no nodes found.
     *
     * @return Set of nodes.
     */
    public Set getChildNodes();

    /**
     * Does this node have a parent node?
     *
     * @return boolean
     */
    public boolean hasParentNode();

    /**
     * Indicates whether a node exists at relPath
     * Returns true if a node exists at relPath and
     * false otherwise.
     *
     * @param relPath
     *            The path of a (possible) node.
     * @return true if a node exists at relPath;
     *         false otherwise.
     */
    public boolean hasNode(String relPath);

    /**
     * Indicates whether this node has child nodes.
     * Returns true if this node has one or more child nodes;
     * false otherwise.
     *
     * @return true if this node has one or more child nodes;
     *         false otherwise.
     */
    public boolean hasNodes();

    /**
     * Returns the property at relPath relative to this
     * node.
     *
     * @param name
     * @return The property value at name, NULL if no such property exists.
     */
    public IValue getProperty(String name);

    /**
     * Returns all properties of this node.
     *
     * @return A Set of IValue objects
     */
    public Set getProperties();

    /**
     * Returns the UUID of this node as recorded in the node's jcr:UUID
     * property.
     *
     * @return the UUID of this node
     */
    public Long getUUID();

    public String getPortraitUuid();

    /**
     * Indicates whether a property exists for this name
     * Returns true if a property exists and false otherwise.
     *
     * @param name
     *            The name of a (possible) property.
     * @return true if a property exists at relPath;
     *         false otherwise.
     */
    public boolean hasProperty(String name);

    /**
     * Indicates whether this node has properties.
     * Returns true if this node has one or more properties;
     * false otherwise.
     *
     * @return true if this node has one or more properties;
     *         false otherwise.
     */
    public boolean hasProperties();

    /**
     * Gets the type of the current node. See {@link NodeType} for
     * possible node types. Note: the node type is shared across all
     * versions of a node
     *
     * @return node type name
     */
    public String getNodeType();

    /**
     * Indicates whether this node is of the specified node type.
     * Returns true if this node is of the specified node type/
     * Returns false otherwise.
     *
     * @param nodeTypeName
     *            the name of a node type.
     * @return true if this node is of the specified node type
     *         or a subtype of the specified node type; returns false otherwise.
     */
    public boolean isNodeType(String nodeTypeName);

    /**
     * Get the version history for this node, ordered by version id.
     *
     * @return Set of {@link IVersionDetail} objects
     */
    public SortedSet getVersionHistory();

    /**
     * Get the path of this node
     *
     * @return relative path (from parent)
     */
    public String getPath();

    /**
     * Get zip compatible filename.
     *
     * There are problems with zipping up files where the names are UTF-8 names - depending on what platform
     * and what zip utility you are using, the filename may be corrupted. To avoid this, we can produce a filename (and
     * path) where the normal "names" are replaced with the uuid but the extension is kept. This will allow downloaded
     * files to be opened by their applications
     *
     * @return a filename that may be used for naming a file put in a zip archive.
     */
    public String getZipCompatibleFilename();

    /**
     * Get the ticket through which this node was accessed.
     *
     * @return A (@link ITicket} object.
     */
    public ITicket getTicket();

    /**
     * Get the version of this node.
     *
     * @return The version number, which will be greater than 0.
     */
    public Long getVersion();

    /**
     * Get the version description.
     *
     * @return The version description
     */
    public String getVersionDescription();

    /**
     * Get the date/time of when this node was created.
     *
     * @return date/time stamp of creation
     */
    public Date getCreatedDateTime();

    /**
     * Get the file, as an inputstream. It is the responsibility
     * of the caller to close the stream.
     *
     * If the node is a package node, it will get the input stream
     * of the first file.
     */
    public InputStream getFile() throws FileException;

    /**
     * Get the user_id of the user who created this node.
     *
     * @return userId
     */
    public Integer getUserId();

}