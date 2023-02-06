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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.CrNode;
import org.lamsfoundation.lams.contentrepository.CrNodeVersion;
import org.lamsfoundation.lams.contentrepository.CrNodeVersionProperty;
import org.lamsfoundation.lams.contentrepository.CrWorkspace;
import org.lamsfoundation.lams.contentrepository.IValue;
import org.lamsfoundation.lams.contentrepository.NodeType;
import org.lamsfoundation.lams.contentrepository.PropertyName;
import org.lamsfoundation.lams.contentrepository.dao.INodeDAO;
import org.lamsfoundation.lams.contentrepository.exception.FileException;
import org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.exception.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.exception.ValueFormatException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Singleton spring bean that creates new SimpleVersionedNodes. Would
 * be nice if it was a Spring factory. Must be created by Spring, so that
 * it gets the Spring bean factory, which allows it to create Node objects
 * via Spring.
 *
 * @author Fiona Malikoff
 *
 */
public class NodeFactory implements INodeFactory, BeanFactoryAware {

    protected Logger log = Logger.getLogger(NodeFactory.class);

    private BeanFactory beanFactory = null;
    private INodeDAO nodeDAO = null;

    public NodeFactory() {
	super();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.contentrepository.service.INodeFactory#createFileNode(org.lamsfoundation.lams.
     * contentrepository.CrWorkspace, org.lamsfoundation.lams.contentrepository.service.SimpleVersionedNode,
     * java.lang.String, java.io.InputStream, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public SimpleVersionedNode createFileNode(CrWorkspace workspace, SimpleVersionedNode parentNode, String relPath,
	    InputStream istream, String filename, String mimeType, String versionDescription, Integer userId)
	    throws InvalidParameterException {

	SimpleVersionedNode initialNodeVersion = createBasicNode(NodeType.FILENODE, workspace, parentNode, relPath,
		versionDescription, userId);
	initialNodeVersion.setFile(istream, filename, mimeType);

	return initialNodeVersion;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.contentrepository.service.INodeFactory#createPackageNode(org.lamsfoundation.lams.
     * contentrepository.CrWorkspace, java.lang.String, java.lang.String)
     */
    @Override
    public SimpleVersionedNode createPackageNode(CrWorkspace workspace, String initialPath, String versionDescription,
	    Integer userId) throws org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException {

	SimpleVersionedNode initialNodeVersion = createBasicNode(NodeType.PACKAGENODE, workspace, null, null,
		versionDescription, userId);
	initialNodeVersion.setProperty(PropertyName.INITIALPATH, initialPath);

	return initialNodeVersion;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.contentrepository.service.INodeFactory#createDataNode(org.lamsfoundation.lams.
     * contentrepository.CrWorkspace, org.lamsfoundation.lams.contentrepository.service.SimpleVersionedNode,
     * java.lang.String)
     */
    @Override
    public SimpleVersionedNode createDataNode(CrWorkspace workspace, SimpleVersionedNode parentNode,
	    String versionDescription, Integer userId)
	    throws org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException {

	SimpleVersionedNode initialNodeVersion = createBasicNode(NodeType.DATANODE, workspace, parentNode, null,
		versionDescription, userId);

	return initialNodeVersion;
    }

    /** Create the core part of a node */
    private SimpleVersionedNode createBasicNode(String nodeType, CrWorkspace workspace, SimpleVersionedNode parentNode,
	    String relPath, String versionDescription, Integer userId) {

	SimpleVersionedNode initialNodeVersion = beanFactory.getBean("node", SimpleVersionedNode.class);

	Date createdDate = new Date(System.currentTimeMillis());
	CrNodeVersion parentNodeVersion = parentNode != null ? parentNode.getNodeVersion() : null;
	CrNode node = new CrNode(relPath, nodeType, createdDate, userId, workspace, parentNodeVersion,
		versionDescription);
	node.setUuid(UUID.randomUUID());
	CrNodeVersion nodeVersion = node.getNodeVersion(null);

	initialNodeVersion.setNode(node);
	initialNodeVersion.setNodeVersion(nodeVersion);

	if (parentNode != null) {
	    parentNode.addChildNode(initialNodeVersion);
	}

	return initialNodeVersion;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.lamsfoundation.lams.contentrepository.service.INodeFactory#getNode(org.lamsfoundation.lams.contentrepository.
     * CrNode, java.lang.Long)
     */
    @Override
    public SimpleVersionedNode getNode(CrNode databaseNode, Long versionId) {
	SimpleVersionedNode newNode = beanFactory.getBean("node", SimpleVersionedNode.class);
	newNode.setNode(databaseNode);
	newNode.setNodeVersion(databaseNode.getNodeVersion(versionId));
	return newNode;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.contentrepository.service.INodeFactory#getNode(java.lang.Long, java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public SimpleVersionedNode getNode(Long workspaceId, Long uuid, Long versionId) throws ItemNotFoundException {

	if (uuid == null) {
	    throw new ItemNotFoundException("UUID is null, unable to find node.");
	}

	if (workspaceId == null) {
	    throw new ItemNotFoundException("Workspace Id is null, unable to find node.");
	}

	CrNode node = null;

	node = nodeDAO.find(CrNode.class, uuid);
	if (node == null) {

	    throw new ItemNotFoundException("Node " + uuid + " not found.");

	} else if (!workspaceId.equals(node.getCrWorkspace().getWorkspaceId())) {

	    log.error("Security warning. User of workspace " + workspaceId + " is trying to access node " + uuid
		    + " which is in workspace " + node.getCrWorkspace().getWorkspaceId()
		    + " Request for node will be rejected.");
	    throw new ItemNotFoundException("Node " + uuid + " does not exist in workspace " + workspaceId);
	}

	return getNode(node, versionId);
    }

    @Override
    public SimpleVersionedNode getNode(Long workspaceId, UUID portraitUuid, Long versionId)
	    throws ItemNotFoundException {

	if (portraitUuid == null) {
	    throw new ItemNotFoundException("UUID is null, unable to find node.");
	}

	if (workspaceId == null) {
	    throw new ItemNotFoundException("Workspace Id is null, unable to find node.");
	}

	CrNode node = null;
	List<CrNode> result = nodeDAO.findByProperty(CrNode.class, "uuid", portraitUuid);
	node = result.isEmpty() ? null : result.get(0);
	if (node == null) {

	    throw new ItemNotFoundException("Node " + portraitUuid + " not found.");

	} else if (!workspaceId.equals(node.getCrWorkspace().getWorkspaceId())) {

	    log.error("Security warning. User of workspace " + workspaceId + " is trying to access node " + portraitUuid
		    + " which is in workspace " + node.getCrWorkspace().getWorkspaceId()
		    + " Request for node will be rejected.");
	    throw new ItemNotFoundException("Node " + portraitUuid + " does not exist in workspace " + workspaceId);
	}

	return getNode(node, versionId);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.contentrepository.service.INodeFactory#getNode(java.lang.Long, java.lang.Long,
     * java.lang.Long, java.lang.String)
     */
    @Override
    public SimpleVersionedNode getNodeNewVersion(Long workspaceId, Long uuid, Long versionId, String versionDescription,
	    Integer userId) throws ItemNotFoundException {

	SimpleVersionedNode existingNode = getNode(workspaceId, uuid, versionId);
	CrNode existingCrNode = existingNode.getNode();

	// get next version id
	Long nextVersionId = existingNode.getNode().incrementNextVersionId();

	SimpleVersionedNode newNode = beanFactory.getBean("node", SimpleVersionedNode.class);
	newNode.setNode(existingCrNode);
	CrNodeVersion newVersion = new CrNodeVersion(existingCrNode, new Date(System.currentTimeMillis()),
		nextVersionId, versionDescription, userId);
	newNode.setNodeVersion(newVersion);
	existingCrNode.addCrNodeVersion(newVersion);

	return newNode;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.lamsfoundation.lams.contentrepository.service.INodeFactory#copy(org.lamsfoundation.lams.contentrepository.
     * service.SimpleVersionedNode)
     */
    @Override
    public SimpleVersionedNode copy(SimpleVersionedNode originalNode, Integer userId)
	    throws FileException, ValueFormatException, InvalidParameterException {

	return copy(originalNode, null, userId);
    }

    /**
     * Private method to handle the recursive copy. The parent node is needed to set up the
     * node -> parent link in the CrNode object.
     *
     * @param originalNode
     * @param parentNode
     * @return new Node
     * @throws FileException
     * @throws ValueFormatException
     * @throws InvalidParameterException
     */
    private SimpleVersionedNode copy(SimpleVersionedNode originalNode, CrNodeVersion parentNodeVersion, Integer userId)
	    throws FileException, ValueFormatException, InvalidParameterException {

	SimpleVersionedNode newNode = beanFactory.getBean("node", SimpleVersionedNode.class);

	// copy the basic CrNode/CrNodeVersion fields. Set a new timestamp and new ids
	Date createdDate = new Date(System.currentTimeMillis());

	CrNode newCrNode = new CrNode(originalNode.getPath(), originalNode.getNodeType(), createdDate, userId,
		originalNode.getNode().getCrWorkspace(), parentNodeVersion, originalNode.getVersionDescription());
	newNode.setNode(newCrNode);

	CrNodeVersion newCrNodeVersion = newCrNode.getNodeVersion(null);
	newNode.setNodeVersion(newCrNodeVersion);

	// copy properties - have to clone the properties items as well
	Set origProperties = originalNode.getNodeVersion().getCrNodeVersionProperties();
	if (origProperties != null) {
	    Iterator iterProp = origProperties.iterator();
	    while (iterProp.hasNext()) {
		CrNodeVersionProperty property = (CrNodeVersionProperty) iterProp.next();
		newCrNodeVersion.setProperty(property.getName(), property.getValue(), property.getType());
	    }
	}

	// copy any attached file. don't actually copy the file - set up
	// and input stream and the file will be copied when the node is saved.
	// this is likely to recopy the Filename and Mimetype properties.
	if (originalNode.isNodeType(NodeType.FILENODE)) {
	    InputStream istream = originalNode.getFile();
	    IValue filenameProperty = originalNode.getProperty(PropertyName.FILENAME);
	    IValue mimetypeProperty = originalNode.getProperty(PropertyName.MIMETYPE);
	    newNode.setFile(istream, filenameProperty != null ? filenameProperty.getString() : null,
		    mimetypeProperty != null ? mimetypeProperty.getString() : null);
	}

	// now copy the child nodes
	Iterator iter = originalNode.getChildNodes().iterator();
	while (iter.hasNext()) {
	    SimpleVersionedNode childNode = (SimpleVersionedNode) iter.next();
	    SimpleVersionedNode newChildNode = this.copy(childNode, newCrNodeVersion, userId);
	    newNode.addChildNode(newChildNode);
	}

	return newNode;

    }

    /*
     * **********************************************************
     * Following methods are required for Spring framework to set up
     * the DAO object(s).
     * **********************************************************
     */

    /*
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.contentrepository.service.INodeFactory#getNodeDAO()
     */
    @Override
    public INodeDAO getNodeDAO() {
	return nodeDAO;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.contentrepository.service.INodeFactory#setNodeDAO(org.lamsfoundation.lams.
     * contentrepository.dao.INodeDAO)
     */
    @Override
    public void setNodeDAO(INodeDAO nodeDAO) {
	this.nodeDAO = nodeDAO;
    }

    /* **** Method for BeanFactoryAware interface *****************/
    /*
     * (non-Javadoc)
     *
     * @see
     * org.lamsfoundation.lams.contentrepository.service.INodeFactory#setBeanFactory(org.springframework.beans.factory.
     * BeanFactory)
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
	this.beanFactory = beanFactory;
    }

}
