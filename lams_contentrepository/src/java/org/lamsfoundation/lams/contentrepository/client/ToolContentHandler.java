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
import java.util.UUID;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.exception.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.exception.FileException;
import org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.exception.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;

/**
 * Handles the connection to the content repository, and allows a file
 * to be stored and retrieved.
 * <p>
 * It can be used by LAMS tools to do the handling the files.
 * <p>
 * It is a very simple client, in that
 * it only handles files (and not packages) and will not keep versions
 * of files - to do a new version of a file, the old version is deleted
 * and the new version added.
 * <p>
 * To use this class:
 * <UL>
 * <LI>You must be using Spring. Include the Content Repository's
 * applicationContext.xml in your Spring context.
 * <LI>Create a class that extends ToolContentHandler and implements
 * getRepositoryWorkspaceName(), getRepositoryUser() and
 * repositoryId(). These methods are required to set up the credential
 * and workspace.
 * <LI>Define your Handler class as a bean in your own Spring context file.
 * It <em>must</em> include a parameter repositoryService, which references a local
 * value of repositoryService. The "repositoryService" is defined in the
 * Content Repository's applicationContext.xml. The name must be unique to this project.
 * </UL>
 * For example:
 *
 * <pre>
 * 	&lt;bean id="blahToolContentHandler" class="your class name here"&gt;
 * 		&lt;property name="repositoryService"&gt; &lt;ref bean="repositoryService"/&lt;/property&gt;
 *	&lt;/bean&gt;
 * </pre>
 *
 *
 * You do not need to include repositoryService as a instance variable
 * in your own class as it is already defined in the ToolContentHandler abstract class.
 * <p>
 * The methods you are likely to use are:
 * <UL>
 * <LI>NodeKey uploadFile(InputStream stream, String fileName, String mimeType)<BR>
 * Saves a file in the content repository
 * <LI>void deleteFile(Long uuid)<BR>
 * Gets rid of a file you don't want any more
 * <LI>IVersionedNode getFileNode(Long uuid)<BR>
 * Gets the file node from the repository. To get the file stream, do getFileNode().getFile()
 * For example:<BR>
 *
 * <pre>
 * NodeKey nodeKey = handler.uploadFile(istream, fileName, fileMimeType);<BR>
 * Long uuid = nodeKey.getUuid();<BR>
 * IVersionedNode node = handler.getFileNode(uuid);<BR>
 * IStream fileStreamToWriteSomewhere = node.getFile());<BR>
 * </pre>
 * <p>
 * Please read the javadoc on each method for more information on the exceptions thrown
 * and on the mandatory parameters.
 * <p>
 * If you want to see this class used, have a look at the test code in org.lamsfoundation.lams.contentrepository.client
 * in the test/java area.
 * <p>
 * You may be wondering why we don't make the workspaceName, user, id, etc
 * parameters in the Spring file, rather than creating a concrete class. Using
 * the Spring file would be easier, but then the id (equivalent to passsword)
 * is easier to hack. The id is a char[], rather than a String for
 * security. If you don't care that your tool's id is stored as a String
 * then you can include it in your Spring file.
 *
 * @author conradb, Fiona Malikoff
 */
public class ToolContentHandler implements IToolContentFullHandler {

    private IRepositoryService repositoryService;
    private ITicket ticket;

    private String repositoryWorkspaceName;
    private String repositoryUser;
    private String repositoryId;

    protected Logger log = Logger.getLogger(ToolContentHandler.class.getName());

    private String getRepositoryWorkspaceName() {
	if (repositoryWorkspaceName == null) {
	    log.error(
		    "Accessing ToolContentHandler bean, but repositoryWorkspaceName has not been defined. Please define this property in ApplicationContext.xml");
	}
	return repositoryWorkspaceName;
    }

    public void setRepositoryWorkspaceName(String repositoryWorkspaceName) {
	this.repositoryWorkspaceName = repositoryWorkspaceName;
    }

    private String getRepositoryUser() {
	if (repositoryUser == null) {
	    log.error(
		    "Accessing ToolContentHandler bean, but repositoryUser has not been defined. Please define this property in ApplicationContext.xml");
	}
	return repositoryUser;
    }

    public void setRepositoryUser(String repositoryUser) {
	this.repositoryUser = repositoryUser;
    }

    private char[] getRepositoryId() {
	if (repositoryId == null) {
	    log.error(
		    "Accessing ToolContentHandler bean, but repositoryWorkspaceName has not been defined. Please define this property in ApplicationContext.xml");
	}
	return repositoryId.toCharArray();
    }

    public void setRepositoryId(String repositoryId) {
	this.repositoryId = repositoryId;
    }

    @Override
    public ITicket getTicket(boolean forceLogin) throws RepositoryCheckedException {
	ICredentials cred = null;
	boolean doLogin = ticket == null || forceLogin;
	if (!doLogin) {
	    // make sure workspace exists - sometimes it does not get created when creating a ticket
	    cred = new SimpleCredentials(getRepositoryUser(), getRepositoryId());
	    doLogin = !repositoryService.workspaceExists(cred, getRepositoryWorkspaceName());
	}
	if (doLogin) {
	    if (cred == null) {
		cred = new SimpleCredentials(getRepositoryUser(), getRepositoryId());
	    }
	    ticket = repositoryService.login(cred, getRepositoryWorkspaceName());
	}
	return ticket;
    }

    @Override
    public NodeKey uploadFile(InputStream stream, String fileName, String mimeType)
	    throws RepositoryCheckedException, InvalidParameterException, RepositoryCheckedException {
	NodeKey nodeKey = null;
	try {
	    try {
		nodeKey = repositoryService.addFileItem(getTicket(false), stream, fileName, mimeType, null);
	    } catch (AccessDeniedException e) {
		log.warn("Unable to access repository to add file " + fileName + "AccessDeniedException: "
			+ e.getMessage() + " Retrying login.");
		nodeKey = repositoryService.addFileItem(getTicket(true), stream, fileName, mimeType, null);
	    }

	} catch (RepositoryCheckedException e2) {
	    log.warn("Unable to to uploadFile " + fileName + ". Repository Exception: " + e2.getMessage()
		    + " Retry not possible.");
	    throw e2;
	}

	return nodeKey;
    }

    @Override
    public NodeKey updateFile(Long nodeId, InputStream stream, String fileName, String mimeType)
	    throws RepositoryCheckedException, InvalidParameterException, RepositoryCheckedException {
	NodeKey nodeKey = null;
	try {
	    try {
		nodeKey = repositoryService.updateFileItem(getTicket(false), nodeId, fileName, stream, mimeType, null);
	    } catch (AccessDeniedException e) {
		log.warn("Unable to access repository to add file " + fileName + "AccessDeniedException: "
			+ e.getMessage() + " Retrying login.");
		nodeKey = repositoryService.updateFileItem(getTicket(true), nodeId, fileName, stream, mimeType, null);
	    }

	} catch (RepositoryCheckedException e2) {
	    log.warn("Unable to to updateFile" + fileName + "Repository Exception: " + e2.getMessage()
		    + " Retry not possible.");
	    throw e2;
	}

	return nodeKey;
    }

    /**
     * TODO To be removed from the system and replaced with the call with the user id.
     */
    @Override
    public NodeKey uploadPackage(String dirPath, String startFile)
	    throws RepositoryCheckedException, InvalidParameterException, RepositoryCheckedException {
	log.error(
		"uploadPackage(String dirPath, String startFile)  to be removed - it sets the owner of files in the content repository to 1. Some tool needs to be updated.");
	return uploadPackage(dirPath, startFile, new Integer(1));
    }

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
    private NodeKey uploadPackage(String dirPath, String startFile, Integer userId)
	    throws RepositoryCheckedException, InvalidParameterException, RepositoryCheckedException {
	if (dirPath == null) {
	    throw new InvalidParameterException(
		    "uploadFile: dirPath parameter empty. Directory containing files must be supplied");
	}

	NodeKey nodeKey = null;
	try {
	    try {
		nodeKey = repositoryService.addPackageItem(getTicket(false), dirPath, startFile, null);
	    } catch (AccessDeniedException e) {
		log.warn("Unable to access repository to add directory of files. AccessDeniedException: "
			+ e.getMessage() + " Retrying login.");
		nodeKey = repositoryService.addPackageItem(getTicket(true), dirPath, startFile, null);
	    }

	} catch (RepositoryCheckedException e2) {
	    log.warn("Unable to access repository to add directory of files. Repository Exception: " + e2.getMessage()
		    + " Retry not possible.");
	    throw e2;
	}

	return nodeKey;
    }

    @Override
    public void saveFile(Long nodeId, String toFileName)
	    throws ItemNotFoundException, RepositoryCheckedException, IOException {
	try {
	    try {
		repositoryService.saveFile(getTicket(false), nodeId, null, toFileName);
	    } catch (AccessDeniedException e) {
		log.warn("Unable to access repository to add copy node " + nodeId + "AccessDeniedException: "
			+ e.getMessage() + " Retrying login.");
		repositoryService.saveFile(getTicket(false), nodeId, null, toFileName);
	    }
	} catch (ItemNotFoundException e) {
	    log.warn("Unable to to save node " + nodeId + " as the node cannot be found. Repository Exception: "
		    + e.getMessage() + " Retry not possible.");
	    throw e;
	} catch (RepositoryCheckedException e) {
	    log.warn("Unable to to save node " + nodeId + "Repository Exception: " + e.getMessage()
		    + " Retry not possible.");
	    throw e;
	}
    }

    /**
     * TODO To be removed from the system and replaced with the call with the user id.
     */
    @Override
    public NodeKey copyFile(Long nodeId) throws ItemNotFoundException, RepositoryCheckedException {
	log.error(
		"copyFile(Long uuid) to be removed - it sets the owner of files in the content repository to 1. Some tool needs to be updated.");
	return copyFile(nodeId, 1);
    }

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
    private NodeKey copyFile(Long nodeId, Integer userId) throws ItemNotFoundException, RepositoryCheckedException {
	NodeKey nodeKey = null;
	try {
	    try {
		nodeKey = repositoryService.copyNodeVersion(getTicket(false), nodeId, null);
	    } catch (AccessDeniedException e) {
		log.warn("Unable to access repository to add copy node " + nodeId + "AccessDeniedException: "
			+ e.getMessage() + " Retrying login.");
		nodeKey = repositoryService.copyNodeVersion(getTicket(true), nodeId, null);
	    }
	} catch (ItemNotFoundException e) {
	    log.warn("Unable to to copy node " + nodeId + " as the node cannot be found. Repository Exception: "
		    + e.getMessage() + " Retry not possible.");
	    throw e;
	} catch (RepositoryCheckedException e) {
	    log.warn("Unable to to copy node " + nodeId + "Repository Exception: " + e.getMessage()
		    + " Retry not possible.");
	    throw e;
	}
	return nodeKey;
    }

    @Override
    public void deleteFile(Long nodeId) throws InvalidParameterException, RepositoryCheckedException {
	try {
	    try {
		repositoryService.deleteNode(getTicket(false), nodeId);
	    } catch (AccessDeniedException e) {
		log.warn("Unable to access repository to delete file id" + nodeId + "AccessDeniedException: "
			+ e.getMessage() + " Retrying login.");
		repositoryService.deleteNode(getTicket(true), nodeId);
	    }
	} catch (ItemNotFoundException e1) {
	    // didn't exist so don't need to delete. Ignore problem.
	} catch (RepositoryCheckedException e2) {
	    log.error("Unable delete file id" + nodeId + "Repository Exception: " + e2.getMessage()
		    + " Retry not possible.");
	    throw e2;
	}
    }

    @Override
    public void deleteFile(UUID uuid) throws InvalidParameterException, RepositoryCheckedException {
	try {
	    try {
		repositoryService.deleteNode(getTicket(false), uuid);
	    } catch (AccessDeniedException e) {
		log.warn("Unable to access repository to delete file id" + uuid + "AccessDeniedException: "
			+ e.getMessage() + " Retrying login.");
		repositoryService.deleteNode(getTicket(true), uuid);
	    }
	} catch (ItemNotFoundException e1) {
	    // didn't exist so don't need to delete. Ignore problem.
	} catch (RepositoryCheckedException e2) {
	    log.error("Unable delete file id" + uuid + "Repository Exception: " + e2.getMessage()
		    + " Retry not possible.");
	    throw e2;
	}
    }

    @Override
    public InputStream getFileInputStream(Long nodeId)
	    throws ItemNotFoundException, FileException, RepositoryCheckedException {
	try {
	    try {
		return repositoryService.getFileItem(getTicket(false), nodeId, null).getFile();
	    } catch (AccessDeniedException e) {
		log.warn("Unable to access repository to get file id" + nodeId + "AccessDeniedException: "
			+ e.getMessage() + " Retrying login.");
		return repositoryService.getFileItem(getTicket(true), nodeId, null).getFile();
	    }
	} catch (RepositoryCheckedException e2) {
	    log.warn("Unable to to get file id" + nodeId + "Repository Exception: " + e2.getMessage()
		    + " Retry not possible.");
	    throw e2;
	}
    }

    @Override
    public String getFileUuid(Long nodeId) {
	if (nodeId == null) {
	    return null;
	}
	NodeKey nodeKey = null;
	try {
	    try {
		nodeKey = repositoryService.getFileItem(getTicket(false), nodeId, null).getNodeKey();
	    } catch (AccessDeniedException e) {
		log.warn("Unable to access repository to get file id" + nodeId + "AccessDeniedException: "
			+ e.getMessage() + " Retrying login.");
		nodeKey = repositoryService.getFileItem(getTicket(true), nodeId, null).getNodeKey();
	    }
	    return nodeKey.getUuid();
	} catch (RepositoryCheckedException e2) {
	    log.warn("Unable to to get file id" + nodeId + "Repository Exception: " + e2.getMessage()
		    + " Retry not possible.");
	    return null;
	}
    }

    /* *** Required for Spring bean creation **************************/

    /**
     * @return Returns the repositoryService.
     */
    @Override
    public IRepositoryService getRepositoryService() {
	return repositoryService;
    }

    /**
     * @param repositoryService
     *            The repositoryService to set.
     */
    public void setRepositoryService(IRepositoryService repositoryService) {
	this.repositoryService = repositoryService;
    }
}
