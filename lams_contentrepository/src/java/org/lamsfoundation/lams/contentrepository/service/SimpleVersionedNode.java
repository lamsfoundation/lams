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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.CrNode;
import org.lamsfoundation.lams.contentrepository.CrNodeVersion;
import org.lamsfoundation.lams.contentrepository.CrNodeVersionProperty;
import org.lamsfoundation.lams.contentrepository.CrWorkspace;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IValue;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.IVersionedNodeAdmin;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.NodeType;
import org.lamsfoundation.lams.contentrepository.PropertyName;
import org.lamsfoundation.lams.contentrepository.PropertyType;
import org.lamsfoundation.lams.contentrepository.dao.IFileDAO;
import org.lamsfoundation.lams.contentrepository.dao.INodeDAO;
import org.lamsfoundation.lams.contentrepository.exception.FileException;
import org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.exception.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryRuntimeException;
import org.lamsfoundation.lams.contentrepository.exception.ValidationException;
import org.lamsfoundation.lams.contentrepository.exception.ValueFormatException;
import org.lamsfoundation.lams.util.FileUtil;

/**
 * This is the default implementation for IVersionedNode.
 * All the properties in the database. One file can be attached.
 * <p>
 * The object should be created as follows:
 * <UL>
 * <LI>For an existing node: SimpleVersionedNode(), call setters for
 * uuid, versionId, nodeDAO and then call loadData(). Finally, set the workspace.
 * <LI>For a new node: SimpleVersionedNode(), call setter for
 * nodeDAO and then call loadData(). Finally, set the workspace.
 * </UL>
 * If using the Spring factory, you nodeDAO and loadData() are called
 * automatically, but you will still need to set the workspace.
 * <p>
 * Many methods in this class will throw a RepositoryRuntimeException
 * if the internal data is missing. This is not indicated
 * on the method signatures.
 *
 * TODO Should these be pooled, instead of creating a new prototype node
 * each time. Some comments on the web indicate that creating a Spring bean
 * and wiring it up may be expensive, so we don't want to do it. But looking
 * at the Spring reference manual, it appears the pooling may hand out
 * the instances on each method invocation. On other hand, other stuff I've
 * read suggest that Spring won't manage the lifecycle so destroy doesn't get
 * called. So, given these objects are being passed back to calling
 * programs and the difficulty of initialising the db data, its tempting
 * to make these POJOs not created by Spring.
 *
 * @author Fiona Malikoff
 */
public class SimpleVersionedNode implements IVersionedNodeAdmin {

    protected Logger log = Logger.getLogger(SimpleVersionedNode.class);

    private CrNode node = null;
    private CrNodeVersion nodeVersion = null;
    private List childNodes = null; // contains SimpleVersionedNode children
    private InputStream newIStream = null;
    private String filePath = null; // transient data - set when the input stream is written out.
    private ITicket ticket = null; // transient data - using for grouping nodes in a session

    /* Spring configured varibles */
    private INodeDAO nodeDAO = null;
    private IFileDAO fileDAO = null;
    private INodeFactory nodeFactory = null;

    // TODO This is a case for AOP!
    /**
     * Check that all the necessary objects exists - node, nodeVersion, nodeDAO and fileDAO
     * If one of them is missing, will throw an exception, appending the specialisedMessage
     * on the end. e.g specialisedMessage = "Unable to set property".
     */
    private void nodeObjectInitilised(String specialisedMessage) throws RepositoryRuntimeException {
	if (node == null) {
	    throw new RepositoryRuntimeException("Node details missing (node=null). " + specialisedMessage);
	}

	if (nodeVersion == null) {
	    throw new RepositoryRuntimeException("Node details missing (nodeVersion=null). " + specialisedMessage);
	}

	if (nodeDAO == null) {
	    throw new RepositoryRuntimeException("Node details missing (nodeDAO=null). " + specialisedMessage);
	}

	if (fileDAO == null) {
	    throw new RepositoryRuntimeException("Node details missing (fileDAO=null). " + specialisedMessage);
	}
    }

    /** Add a child to the node. To be used by SimpleVersionedNode and the NodeFactory only */
    protected void addChildNode(SimpleVersionedNode newChild) {
	if (childNodes == null) {
	    childNodes = new ArrayList();
	}
	childNodes.add(newChild);
    }

    @Override
    public NodeKey getNodeKey() {
	return new NodeKey(getUUID(), getVersion(), getPortraitUuid());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#setProperty(java.lang.String, java.lang.String,
     * int)
     */
    @Override
    public void setProperty(String name, Object value, int type) {
	nodeObjectInitilised("Unable to set property " + name + " to value " + value);
	nodeVersion.setProperty(name, value, type);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#setProperty(java.lang.String, java.lang.String)
     */
    @Override
    public void setProperty(String name, String value) throws RepositoryRuntimeException {
	nodeObjectInitilised("Unable to set property " + name + " to value " + value);
	nodeVersion.setProperty(name, value, PropertyType.STRING);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#setProperty(java.lang.String, boolean)
     */
    @Override
    public void setProperty(String name, boolean value) {
	nodeObjectInitilised("Unable to set property " + name + " to value " + value);
	nodeVersion.setProperty(name, Boolean.toString(value), PropertyType.BOOLEAN);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#setProperty(java.lang.String, double)
     */
    @Override
    public void setProperty(String name, double value) {
	nodeObjectInitilised("Unable to set property " + name + " to value " + value);
	nodeVersion.setProperty(name, Double.toString(value), PropertyType.DOUBLE);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#setProperty(java.lang.String, long)
     */
    @Override
    public void setProperty(String name, long value) {
	nodeObjectInitilised("Unable to set property " + name + " to value " + value);
	nodeVersion.setProperty(name, Long.toString(value), PropertyType.LONG);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#setProperty(java.lang.String, java.util.Calendar)
     */
    @Override
    public void setProperty(String name, Calendar value) {
	nodeObjectInitilised("Unable to set property " + name + " to value " + value);
	nodeVersion.setProperty(name, value.toString(), PropertyType.DATE);
    }

    /**
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#getProperty(java.lang.String)
     */
    @Override
    public IValue getProperty(String name) {
	nodeObjectInitilised("Unable to get property " + name);

	return nodeVersion.getProperty(name);
    }

    /**
     * Returns a set of IValue objects - this method could cause a problem
     * when lazy initialised.
     */
    @Override
    public Set getProperties() {
	nodeObjectInitilised("Unable to get properties.");
	return nodeVersion.getCrNodeVersionProperties();
    }

    /**
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#getUUID()
     */
    @Override
    public Long getUUID() {
	nodeObjectInitilised("Unable to get uuid.");
	return node.getNodeId();
    }

    @Override
    public String getPortraitUuid() {
	nodeObjectInitilised("Unable to get portrait UUID");
	return node.getPortraitUuid() == null ? null : node.getPortraitUuid().toString();
    }

    /**
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#hasProperty(java.lang.String)
     */
    @Override
    public boolean hasProperty(String name) {
	nodeObjectInitilised("Unable to check properties.");
	return nodeVersion.hasProperty(name);
    }

    /**
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#hasProperties()
     */
    @Override
    public boolean hasProperties() {
	nodeObjectInitilised("Unable to check properties.");
	return nodeVersion.hasProperties();
    }

    /**
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#getNodeType()
     */
    @Override
    public String getNodeType() {
	nodeObjectInitilised("Unable to get node type.");
	return node.getType();
    }

    /**
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#isNodeType(java.lang.String)
     */
    @Override
    public boolean isNodeType(String nodeTypeName) {
	nodeObjectInitilised("Unable to get check node type.");
	return node.isNodeType(nodeTypeName);
    }

    /**
     * Get the history for this node. Quite intensive operation
     * as it has to build all the data structures. Can't be easily
     * cached.
     *
     * @return SortedSet of IVersionDetail objects, ordered by version
     */
    @Override
    public SortedSet getVersionHistory() {
	nodeObjectInitilised("Unable to get version history.");
	return node.getVersionHistory();
    }

    /**
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#getPath()
     */
    @Override
    public String getPath() {
	nodeObjectInitilised("Unable to get path.");
	return node.getPath();
    }

    /**
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#getZipCompatiblePath()
     */
    @Override
    public String getZipCompatibleFilename() {
	nodeObjectInitilised("Unable to get path.");

	CrNodeVersionProperty filenameProperty = nodeVersion.getProperty(PropertyName.FILENAME);
	String filename = filenameProperty != null ? filenameProperty.getString() : null;

	if (filename == null) {
	    return null;
	} else {
	    String uuidString = node.getNodeId().toString();
	    return uuidString + '.' + FileUtil.getFileExtension(filename);
	}
    }

    /**
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#getTicket()
     */
    @Override
    public ITicket getTicket() {
	return ticket;
    }

    /**
     * Set the ticket. Should be called when the versioned node is added to
     * the ticket. Not part of the IVersionedNode interface as is is only
     * set by the repository.
     */
    protected void setTicket(ITicket ticket) {
	this.ticket = ticket;
    }

    /**
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#getVersion()
     */
    @Override
    public Long getVersion() {
	nodeObjectInitilised("Unable to get version.");
	return nodeVersion.getVersionId();
    }

    /**
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#getVersionDescription()
     */
    @Override
    public String getVersionDescription() {
	nodeObjectInitilised("Unable to get version description.");
	return nodeVersion.getVersionDescription();
    }

    /**
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#getCreatedDateTime()
     */
    @Override
    public Date getCreatedDateTime() {
	nodeObjectInitilised("Unable to get version.");
	return nodeVersion.getCreatedDateTime();

    }

    /**
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#getUserId()
     */
    @Override
    public Integer getUserId() {
	nodeObjectInitilised("Unable to get user id.");
	return nodeVersion.getUserId();

    }

    /**
     * Get the file, as an inputstream. It is the responsibility
     * of the caller to close the stream. Note: this should only be
     * called once the node is saved - do not call it directly after
     * setting the file stream
     *
     * If the node is a package node, it will get the input stream
     * of the first file.
     */
    @Override
    public InputStream getFile() throws FileException {
	nodeObjectInitilised("Unable to get file");

	if (isNodeType(NodeType.FILENODE)) {

	    return fileDAO.getFile(node.getNodeId(), nodeVersion.getVersionId());

	} else if (isNodeType(NodeType.PACKAGENODE)) {

	    try {
		IValue value = getProperty(PropertyName.INITIALPATH);
		String lookupPath = value != null ? value.getString() : null;
		IVersionedNode startNode = getNode(lookupPath);
		return startNode.getFile();

	    } catch (ValueFormatException vfe) {
		// if this is thrown, then it is bug - nothing external should cause it.
		throw new RepositoryRuntimeException("Internal error: unable to get file." + vfe.getMessage(), vfe);
	    } catch (ItemNotFoundException e) {
		throw new FileException(
			"Unable to find initial page for package. Initial path indicated " + PropertyName.INITIALPATH);
	    }

	} else {
	    throw new FileException("Node is not a file or a package. No stream to return.");
	}
    }

    /**
     * Set the file, passed in as an inputstream. The stream will be closed
     * when the file is saved. Only nodes of type FILENODE can have a file!
     *
     * @param iStream
     *            mandatory
     * @param filename
     *            mandatory
     * @param mimeType
     *            optional
     */
    @Override
    public void setFile(InputStream iStream, String filename, String mimeType) throws InvalidParameterException {

	nodeObjectInitilised("Unable to set the file stream.");

	/* Perform basic validation */
	if (!NodeType.FILENODE.equals(node.getType())) {
	    throw new InvalidParameterException(
		    "Node must be of type FILE_NODE for a file to be added to the node. Unable to set the file stream.");
	}

	if (iStream == null) {
	    throw new InvalidParameterException("InputStream is required.");
	}

	String trimmedFilename = filename != null ? filename.trim() : null;
	if (trimmedFilename == null || trimmedFilename.length() == 0) {
	    throw new InvalidParameterException("Filename is required.");
	}

	try {
	    // copy the stream contents as it has to be read twice - once for scan, then for file processing
	    byte[] fileByteArray = IOUtils.toByteArray(iStream);
	    iStream.close();
	    iStream = new ByteArrayInputStream(fileByteArray);
	    InputStream copy = new ByteArrayInputStream(fileByteArray);
	    boolean isVirusFree = FileUtil.isVirusFree(copy);
	    if (!isVirusFree) {
		throw new InvalidParameterException("File contains a virus: " + filename);
	    }
	} catch (IOException e) {
	    throw new InvalidParameterException("Could not scan file: " + filename, e);
	}

	/* Validation passed, set up the file details */
	this.filePath = null; // will be set when the stream is written out
	this.newIStream = iStream;
	setProperty(PropertyName.FILENAME, trimmedFilename);

	if (mimeType != null && mimeType.length() > 0) {
	    setProperty(PropertyName.MIMETYPE, mimeType);
	} else {
	    setProperty(PropertyName.MIMETYPE, (String) null);
	}
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("node", node).append("nodeVersion", nodeVersion)
		.append("newIStream", newIStream).toString();
    }

    /*
     * **********************************************************
     * Handles the actual persistence of nodes to the database
     * **********************************************************
     */

    /**
     * Validate that this node is in a state that may be saved.
     * Rules:
     * <UL>
     * <LI>All nodes must have a node type and created date.
     * <LI>Root nodes must not have a file
     * <LI>File nodes must have a file, must have a MIMETYPE property. Note:
     * we could just be doing a setProperty save, in which case the file
     * will already exist.
     * <LI>Package nodes must not have a file, must have a INITIALPATH property
     *
     * @throws ValidationException
     *             if problems exist.
     */
    protected void validateNode() throws ValidationException {
	String errors = "";

	if (node == null) {
	    errors = errors + "\nInternal node object (node) is missing - node just doesn't exist properly! ";
	}

	if (nodeVersion == null) {
	    errors = errors + "\nInternal node object (nodeVersion) is missing - node just doesn't exist properly! ";
	}

	if (errors.length() == 0) {

	    if (!NodeType.isValidNodeType(node.getType())) {
		errors = errors + "\nNode type " + node.getType() + " is not a valid node type. ";
	    }

	    if (node.getCreatedDateTime() == null) {
		errors = errors + "\nCreated datetimestamp is missing. ";
	    }

	    if (isNodeType(NodeType.FILENODE)) {
		Long uuid = node.getNodeId();
		Long versionId = nodeVersion.getVersionId();
		try {
		    // if it is a new node or a new version then it must have a file, otherwise check on disk.
		    // version id will always be set. uuid isn't set until the record is written to the db,
		    // but don't want to rely on that if we can help it in case it changes in future.
		    if (newIStream == null && (uuid == null || !fileDAO.fileExists(uuid, versionId))) {
			errors = errors + "\nNode is a file node but the file is missing. ";
		    }
		} catch (FileException fe) {
		    errors = "Unable to validation node due to file exception: " + fe.getMessage();
		    log.error("File exception occured while validating node " + this.toString(), fe);
		}
		if (!hasProperty(PropertyName.FILENAME)) {
		    errors = errors + "\nNode is a file node but the filename is unknown";
		}
	    } else {
		if (newIStream != null) {
		    errors = errors + "\nNode is a " + node.getType() + " type but a file is attached. ";
		}
	    }

	    if (isNodeType(NodeType.PACKAGENODE) && !hasProperty(PropertyName.INITIALPATH)) {
		errors = errors + "\nNode is a package node but the initial path is unknown";
	    }

	}

	if (errors.length() > 0) {
	    throw new ValidationException(errors);
	}
    }

    /**
     * Save the changes to this node. This method must be called when saving a file
     * or package node for the first time - it does both the database and the file
     * saves.
     *
     * If it is a file node, then it writes out the db changes and then saves
     * the file.
     *
     * If is is a package node, then it writes out the db changes for all the nodes,
     * then saves all the file. Why do it this way - we want to do all the file
     * changes at the end as they cannot be rolled back if there is a db error.
     *
     * This method only works as we know that we have two levels of nodes - the
     * childNodes can't have their own childNodes. If this is no longer the case,
     * this method and copy() will need to be changed.
     *
     *
     * TODO This needs a lot of testing
     */
    protected Long save() throws ValidationException, FileException {

	nodeObjectInitilised("Unable to save node.");

	this.saveDB();
	if (childNodes != null) {
	    Iterator iter = childNodes.iterator();
	    while (iter.hasNext()) {
		SimpleVersionedNode childNode = (SimpleVersionedNode) iter.next();
		childNode.saveDB();
	    }
	}

	// okay, db updated successfully, so now we can start writing out the files.
	// keep track of all the written files in case we fail and need to delete them
	Set fileNodesWritten = new HashSet();
	String nodePaths = null;
	try {
	    this.writeFile();
	    if (childNodes != null) {
		Iterator iter = childNodes.iterator();
		while (iter.hasNext()) {
		    SimpleVersionedNode childNode = (SimpleVersionedNode) iter.next();
		    childNode.writeFile();
		    fileNodesWritten.add(childNode);
		    nodePaths = nodePaths != null ? nodePaths + File.pathSeparator + childNode.getFilePath()
			    : childNode.getFilePath();
		}
	    }
	} catch (Exception e) {
	    // Some error has occured, so we need to delete the files we already wrote
	    // and then rethrow the error.

	    if (fileNodesWritten.size() == 0) {

		log.error("Error occured while writing out files. No files already written so no files to delete. ");

	    } else {

		log.error(
			"Error occured while writing out files. Trying to delete already written files for the following nodes: "
				+ nodePaths);

		String deleted = null;
		String failedDeleted = null;
		Iterator writtenIter = fileNodesWritten.iterator();
		while (writtenIter.hasNext()) {
		    SimpleVersionedNode element = (SimpleVersionedNode) writtenIter.next();
		    int delStatus = -1;
		    try {
			delStatus = fileDAO.delete(element.getUUID(), element.getVersion());
		    } catch (Exception e2) {
			// things are getting bad - throwing exceptions on the delete!
		    }
		    if (delStatus == 1) {
			deleted = deleted != null ? deleted + File.pathSeparator + element.getFilePath()
				: element.getFilePath();
		    } else {
			failedDeleted = failedDeleted != null
				? failedDeleted + File.pathSeparator + element.getFilePath()
				: element.getFilePath();
		    }
		}
		String msg = "Result of rolling back file changes:";
		if (deleted != null && deleted.length() > 0) {
		    msg = msg + "   deleted file(s) " + deleted;
		}
		if (failedDeleted != null && failedDeleted.length() > 0) {
		    msg = msg + "   unable to delete file(s) " + failedDeleted;
		}
		log.error(msg);
	    }

	    // now rethrow the exception. If it was a FileException then throw
	    // the original - don't rewrap it.
	    if (FileException.class.isInstance(e)) {
		throw (FileException) e;
	    } else {
		throw new FileException("Unable to write file " + e.getMessage(), e);
	    }
	}

	return node.getNodeId();
    }

    /**
     * Validate the node and save the db changes to the current node.
     */
    protected void saveDB() throws ValidationException {

	validateNode();

	// nodeDAO to take care of insert or update (uses saveOrUpdate)
	// the nodeVersion and nodeVersionProperty collections cascade
	// updates and deletes, so we can just save the node!
	nodeDAO.saveOrUpdate(node);

	// child nodes are done manually as the set is lazy loaded
	// and can't work out how to do that properly using the DAO template!
	// 'cause the session goes away.
	if (childNodes != null) {
	    Iterator iter = childNodes.iterator();
	    while (iter.hasNext()) {
		SimpleVersionedNode node = (SimpleVersionedNode) iter.next();
		nodeDAO.saveOrUpdate(node.getNode());
	    }
	}

    }

    /**
     * Write the file out (if one exists). Sets the private attribute filePath.
     *
     * @return the path to which the file was written
     */
    private void writeFile() throws FileException {
	String filePath = null;
	if (newIStream != null) {
	    filePath = fileDAO.writeFile(node.getNodeId(), nodeVersion.getVersionId(), newIStream);
	}
	setFilePath(filePath);
    }

    /**
     * @return Returns the filePath.
     */
    private String getFilePath() {
	return filePath;
    }

    /**
     * @param filePath
     *            The filePath to set.
     */
    private void setFilePath(String filePath) {
	this.filePath = filePath;
    }

    /**
     * Another case for the factory?
     *
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#getNode(String relPath)
     */
    @Override
    public IVersionedNode getNode(String relPath) throws ItemNotFoundException {

	nodeObjectInitilised("Unable to get child node.");

	if (log.isDebugEnabled()) {
	    log.debug("getNode for path " + relPath + " start.");
	}

	CrNode childNode = nodeDAO.findChildNode(nodeVersion, relPath);

	if (childNode != null) {
	    return nodeFactory.getNode(childNode, null);
	} else {
	    throw new ItemNotFoundException(
		    "Unable to find node with path " + relPath + " as a child of node " + getUUID());
	}
    }

    /**
     * If no nodes are found, returns an empty set.
     *
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#getChildNodes()
     */
    @Override
    public Set getChildNodes() {
	List childCrNodes = nodeDAO.findChildNodes(nodeVersion);
	Set childNodes = new HashSet();

	if (childCrNodes != null) {
	    Iterator iter = childCrNodes.iterator();
	    while (iter.hasNext()) {
		CrNode element = (CrNode) iter.next();
		SimpleVersionedNode newNode = nodeFactory.getNode(element, null);
		childNodes.add(newNode);
	    }
	}
	if (log.isDebugEnabled()) {
	    log.debug("getNodes returning set of " + (childNodes != null ? Integer.toString(childNodes.size()) : "0")
		    + " nodes.");
	}

	return childNodes;
    }

    /**
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#hasParentNode()
     */
    @Override
    public boolean hasParentNode() {

	nodeObjectInitilised("Unable to check if there is a parent node.");

	return (node.getParentNodeVersion() != null);

    }

    /**
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#hasNode(String relPath)
     */
    @Override
    public boolean hasNode(String relPath) {
	try {
	    IVersionedNode node = getNode(relPath);
	    // don't really expect to get node == null here - if not found then should
	    // throw exception.
	    return node != null;
	} catch (ItemNotFoundException e) {
	    return false;
	}
    }

    /**
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNode#hasNodes()
     */
    @Override
    public boolean hasNodes() {
	List childNodes = nodeDAO.findChildNodes(nodeVersion);
	return (childNodes != null && childNodes.size() > 0);
    }

    /**
     * Delete all versions of this node, returning the number of nodes
     * deleted. If it is a package node, all child nodes will be deleted.
     *
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNodeAdmin#deleteNode()
     */
    @Override
    public List deleteNode() {

	// first make a list of all the versions to delete.
	// don't iterate over the set, deleting as we go so that
	// we can't run into any issues trying to access something
	// that is deleted or belongs to an iterator.
	Long versions[] = node.getVersionIds();

	List problemPaths = new ArrayList();
	for (int i = 0; i < versions.length; i++) {
	    // get the SimpleVersionedNode for this version
	    SimpleVersionedNode newNode = nodeFactory.getNode(getNode(), versions[i]);
	    problemPaths.addAll(newNode.deleteVersion());
	}
	return problemPaths;
    }

    /**
     * @see org.lamsfoundation.lams.contentrepository.IVersionedNodeAdmin#deleteVersion()
     */
    @Override
    public List deleteVersion() {

	Long workspaceId = node.getCrWorkspace().getWorkspaceId();
	Long uuid = getUUID();
	Long version = getVersion();
	String nodeDescription = "workspace " + workspaceId + " uuid " + uuid + " version " + version;

	log.info("Deleting database and file entries for " + nodeDescription);

	ArrayList nodeKeysDeleted = new ArrayList();

	// handle the db first. That way any db locks will stop us
	// doing file system changes if the db would fail later.
	deleteVersionFromDB(nodeKeysDeleted);

	// now delete the files. If it fails due to the file not being found, then
	// that's fine.
	ArrayList failedList = new ArrayList();
	Iterator iter = nodeKeysDeleted.iterator();
	while (iter.hasNext()) {
	    NodeKey nk = (NodeKey) iter.next();
	    try {
		int delStatus = fileDAO.delete(nk.getUuid(), nk.getVersion());
		if (delStatus == -1) {
		    failedList.add(fileDAO.getFilePath(nk.getUuid(), nk.getVersion()));
		}
	    } catch (FileException e) {
		log.error("FileException occured while deleting files for " + nodeDescription, e);
		failedList.add("Filename unknown uuid " + nk.getUuid() + " version " + nk.getVersion());
	    }
	}

	if (failedList.size() > 0) {
	    String filenames = null;
	    Iterator failedIter = failedList.iterator();
	    while (failedIter.hasNext()) {
		String path = (String) failedIter.next();
		filenames = filenames != null ? filenames + "," + path : path;
	    }
	    log.error(
		    "Failed to delete the following files relating to workspace " + nodeDescription + ": " + filenames);
	}
	return failedList;
    }

    /*
     * Delete just the db details for the current node/version and any child nodes.
     * Split from the file delete to allow all the db updates to be done before the
     * file updates. Adds the NodeKeys for the delete nodes to the supplied arraylist.
     * Assumes nodeKeysDeleted will not be null
     * <p>
     * We expect that deleting the version node will delete all the properties.
     */
    private void deleteVersionFromDB(ArrayList nodeKeysDeleted) {

	Set childNodes = getChildNodes();
	if (childNodes != null) {
	    // 	First delete the child nodes
	    Iterator iter = childNodes.iterator();
	    while (iter.hasNext()) {
		SimpleVersionedNode element = (SimpleVersionedNode) iter.next();
		element.deleteVersionFromDB(nodeKeysDeleted);
	    }
	}

	NodeKey nk = getNodeKey();
	if (node.getCrNodeVersions() != null) {
	    boolean removed = node.removeCrNodeVersion(nodeVersion);
	    if (removed) {
		nodeKeysDeleted.add(nk);
	    }
	}

	// if this was the last version for the node, delete the node
	if (node.getCrNodeVersions() == null || node.getCrNodeVersions().size() == 0) {
	    nodeDAO.delete(node);
	}

    }

    /**
     * Process files in the package. Create a List of file nodes but do not persist
     * the nodes.
     *
     * @param dirPath:
     *            the directory from which to get files. Mandatory.
     * @param packageNode:
     *            node representing the package. Mandatory.
     * @parm versionDescription: version description for the child nodes.
     * @return nodeSet: set of child nodes for the package node
     * @throws InvalidParameterException
     * @throws FileException
     */
    protected void addPackageFiles(CrWorkspace workspace, String dirPath, String versionDescription, Integer userId)
	    throws InvalidParameterException, FileException, ValidationException {

	File directory = new File(dirPath);
	if (!directory.exists() || !directory.isDirectory() || !directory.canRead()) {
	    throw new FileException("Directory " + dirPath + " does not exist, is not a directory or cannot be read.");
	}

	// set up the path to be removed from file paths, to make a relative path.
	// this is a directory but we may need to add the directory separator on the end
	String removePathToMakeRelPath = directory.getAbsolutePath();
	if (removePathToMakeRelPath.charAt(removePathToMakeRelPath.length() - 1) != File.separatorChar) {
	    removePathToMakeRelPath += File.separatorChar;
	}

	processDirectory(workspace, removePathToMakeRelPath, directory, versionDescription, userId);
    }

    /**
     * Process Directory of files. This method is called recursively to process
     * files in the initial directory and in all subdirectories of this directory.
     *
     * @param removePathToMakeRelPath:
     *            string to remove from a files absolute
     *            path to create relPath, the path relative to the package node. This is
     *            the absolute path to the directory that contains all the files for the package.
     *            This value stays the same across all recursive calls. Mandatory.
     * @param dirFile:
     *            the directory from which to get files. Initially this will
     *            be the directory that contains the package but as recursive calls are made,
     *            this value will change. Mandatory.
     * @param versionDescription:
     *            version description for node. Optional.
     *            This value stays the same across all recursive calls.
     * @param nodeSet:
     *            set of nodes to update with the new nodes. Passed in through
     *            the recursion for efficiency (rather than keep creating new collections). Must
     *            not be null.
     * @throws FileException
     */
    private void processDirectory(CrWorkspace workspace, String removePathToMakeRelPath, File dirFile,
	    String versionDescription, Integer userId)
	    throws InvalidParameterException, FileException, ValidationException {

	if (!dirFile.exists() || !dirFile.isDirectory() || !dirFile.canRead()) {
	    throw new FileException("Directory " + dirFile.getAbsolutePath()
		    + " does not exist, is not a directory or cannot be read.");
	}

	File file = null; // declare outside try so available in catch
	try {

	    File[] files = dirFile.listFiles();
	    for (int i = 0; i < files.length; i++) {
		file = files[i];

		log.debug("Processing file " + file.getAbsolutePath());
		log.debug("Name is " + file.getName());

		if (file.isDirectory()) {

		    // recurse to get files in this directory
		    processDirectory(workspace, removePathToMakeRelPath, file, versionDescription, userId);

		} else {

		    // get the name and relative path (from the package directory)
		    // for this node. convert any \ in the relative path to /
		    // as / is needed on retrieval.
		    String filename = file.getName();
		    String filePath = file.getPath();
		    String relPath = StringUtils.replace(filePath, removePathToMakeRelPath, "");
		    if (filePath.length() == relPath.length()) {
			// path hasn't shortened so something has gone wrong!
			throw new FileException("Unable to determine relative path of file. " + "Path to package is "
				+ removePathToMakeRelPath + "Path to file is " + filePath + "Attempted relPath is "
				+ relPath);
		    }
		    relPath = relPath.replace(File.separatorChar, '/');

		    // Open the file ready for reading then create
		    // the file node. Mime type is unknown.
		    // no need to the new node as a child node, as createFileNode will do it.
		    FileInputStream istream = new FileInputStream(file);
		    nodeFactory.createFileNode(workspace, this, relPath, istream, filename, null, versionDescription,
			    userId, false);
		}
	    }

	} catch (FileNotFoundException fe) {
	    // how can this be when we just read them in? Maybe a privilege problem
	    String message = "FileNotFoundException thrown while trying to read file in package. File path=\""
		    + (file != null ? file.getAbsolutePath() : "") + "\"";
	    log.error(message, fe);
	    throw new FileException("Internal error: unable to add package. " + message, fe);
	} catch (FileException e) {
	    // catch this so we can document it against the file details (unknown
	    // further down in the guts) - make sure we rethrow it.
	    String message = "FileException thrown while trying to read file in package. File path=\""
		    + (file != null ? file.getAbsolutePath() : "") + "\"";
	    log.error(message, e);
	    throw e;
	}

    }

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
    public SimpleVersionedNode copy(Integer userId)
	    throws FileException, ValueFormatException, InvalidParameterException {

	return nodeFactory.copy(this, userId);

    }

    /*
     * **********************************************************
     * Methods used by the Node Factory
     * **********************************************************
     */

    /** Get the "internal" CrNode. Should only be used by the NodeFactory */
    protected CrNode getNode() {
	return node;
    }

    /** Set the "internal" CrNode. Should only be used by the NodeFactory */
    public void setNode(CrNode node) {
	this.node = node;
    }

    /** Get the "internal" CrNodeVersion. Should only be used by the NodeFactory */
    public CrNodeVersion getNodeVersion() {
	return nodeVersion;
    }

    /** Set the "internal" CrNodeVersion. Should only be used by thef */
    public void setNodeVersion(CrNodeVersion nodeVersion) {
	this.nodeVersion = nodeVersion;
    }

    /*
     * **********************************************************
     * Following methods are required for Spring framework to set up
     * the DAO object(s) and to trigger a cleanup of the filestream
     * if the object is destroyed.
     * **********************************************************
     */

    /**
     * @return Returns the nodeDAO.
     */
    public INodeDAO getNodeDAO() {
	return nodeDAO;
    }

    /**
     * @param nodeDAO
     *            The nodeDAO to set.
     */
    public void setNodeDAO(INodeDAO nodeDAO) {
	this.nodeDAO = nodeDAO;
    }

    /**
     * @return Returns the fileDAO.
     */
    public IFileDAO getFileDAO() {
	return fileDAO;
    }

    /**
     * @param fileDAO
     *            The fileDAO to set.
     */
    public void setFileDAO(IFileDAO fileDAO) {
	this.fileDAO = fileDAO;
    }

    public INodeFactory getNodeFactory() {
	return nodeFactory;
    }

    public void setNodeFactory(INodeFactory nodeFactory) {
	this.nodeFactory = nodeFactory;
    }

    /**
     * Clean up any resources that will not be cleaned up by the garbage
     * collector after this object is destroyed. At present, all it does is
     * close the filestream if needed
     */
    public void destroy() {
	System.out.println("SimpleVersionedNode destroy called!");
	try {
	    if (newIStream != null) {
		newIStream.close();
	    }
	} catch (IOException e) {
	    log.debug("Unable to close stream - was it already closed perhaps?", e);
	}
    }

}
