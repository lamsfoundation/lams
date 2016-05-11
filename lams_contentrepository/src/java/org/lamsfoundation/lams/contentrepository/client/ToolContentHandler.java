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

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.ItemExistsException;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
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
public abstract class ToolContentHandler implements IToolContentHandler {

    private IRepositoryService repositoryService;
    private ITicket ticket;

    protected Logger log = Logger.getLogger(ToolContentHandler.class.getName());

    /**
     * @return Returns the repositoryWorkspaceName.
     */
    @Override
    public abstract String getRepositoryWorkspaceName();

    /**
     * @return Returns the repositoryUser.
     */
    @Override
    public abstract String getRepositoryUser();

    /**
     * @return Returns the repository identification string. This is the
     *         "password" field the credential.
     */
    @Override
    public abstract char[] getRepositoryId();

    private void configureContentRepository() throws RepositoryCheckedException {
	ICredentials cred = new SimpleCredentials(getRepositoryUser(), getRepositoryId());
	try {
	    getRepositoryService().createCredentials(cred);
	    getRepositoryService().addWorkspace(cred, getRepositoryWorkspaceName());
	} catch (ItemExistsException ie) {
	    log.warn("Tried to configure repository but it " + " appears to be already configured." + "Workspace name "
		    + getRepositoryWorkspaceName() + ". Exception thrown by repository being ignored. ", ie);
	} catch (RepositoryCheckedException e) {
	    log.error("Error occured while trying to configure repository." + "Workspace name "
		    + getRepositoryWorkspaceName() + " Unable to recover from error: " + e.getMessage(), e);
	    throw e;
	}
    }

    @Override
    public ITicket getTicket(boolean forceLogin) throws RepositoryCheckedException {
	if (ticket == null || forceLogin) {
	    ICredentials cred = new SimpleCredentials(getRepositoryUser(), getRepositoryId());
	    try {
		try {
		    ticket = getRepositoryService().login(cred, getRepositoryWorkspaceName());
		} catch (WorkspaceNotFoundException e) {
		    log.error("Content Repository workspace " + getRepositoryWorkspaceName()
			    + " not configured. Attempting to configure now.");
		    configureContentRepository();
		    ticket = getRepositoryService().login(cred, getRepositoryWorkspaceName());
		}
	    } catch (RepositoryCheckedException e) {
		log.error("Unable to get ticket for workspace " + getRepositoryWorkspaceName(), e);
		throw e;
	    }
	}
	return ticket;
    }

    @Override
    public NodeKey uploadFile(InputStream stream, String fileName, String mimeType)
	    throws RepositoryCheckedException, InvalidParameterException, RepositoryCheckedException {
	NodeKey nodeKey = null;
	try {
	    try {
		nodeKey = getRepositoryService().addFileItem(getTicket(false), stream, fileName, mimeType, null);
	    } catch (AccessDeniedException e) {
		log.warn("Unable to access repository to add file " + fileName + "AccessDeniedException: "
			+ e.getMessage() + " Retrying login.");
		nodeKey = getRepositoryService().addFileItem(getTicket(true), stream, fileName, mimeType, null);
	    }

	} catch (RepositoryCheckedException e2) {
	    log.warn("Unable to to uploadFile" + fileName + "Repository Exception: " + e2.getMessage()
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
		nodeKey = getRepositoryService().addPackageItem(getTicket(false), dirPath, startFile, null);
	    } catch (AccessDeniedException e) {
		log.warn("Unable to access repository to add directory of files. AccessDeniedException: "
			+ e.getMessage() + " Retrying login.");
		nodeKey = getRepositoryService().addPackageItem(getTicket(true), dirPath, startFile, null);
	    }

	} catch (RepositoryCheckedException e2) {
	    log.warn("Unable to access repository to add directory of files. Repository Exception: " + e2.getMessage()
		    + " Retry not possible.");
	    throw e2;
	}

	return nodeKey;
    }

    @Override
    public void saveFile(Long uuid, String toFileName)
	    throws ItemNotFoundException, RepositoryCheckedException, IOException {
	try {
	    try {
		getRepositoryService().saveFile(getTicket(false), uuid, null, toFileName);
	    } catch (AccessDeniedException e) {
		log.warn("Unable to access repository to add copy node " + uuid + "AccessDeniedException: "
			+ e.getMessage() + " Retrying login.");
		getRepositoryService().saveFile(getTicket(false), uuid, null, toFileName);
	    }
	} catch (ItemNotFoundException e) {
	    log.warn("Unable to to save node " + uuid + " as the node cannot be found. Repository Exception: "
		    + e.getMessage() + " Retry not possible.");
	    throw e;
	} catch (RepositoryCheckedException e) {
	    log.warn("Unable to to save node " + uuid + "Repository Exception: " + e.getMessage()
		    + " Retry not possible.");
	    throw e;
	}
    }

    /**
     * TODO To be removed from the system and replaced with the call with the user id.
     */
    @Override
    public NodeKey copyFile(Long uuid) throws ItemNotFoundException, RepositoryCheckedException {
	log.error(
		"copyFile(Long uuid) to be removed - it sets the owner of files in the content repository to 1. Some tool needs to be updated.");
	return copyFile(uuid, new Integer(1));
    }

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
    private NodeKey copyFile(Long uuid, Integer userId) throws ItemNotFoundException, RepositoryCheckedException {
	NodeKey nodeKey = null;
	try {
	    try {
		nodeKey = getRepositoryService().copyNodeVersion(getTicket(false), uuid, null);
	    } catch (AccessDeniedException e) {
		log.warn("Unable to access repository to add copy node " + uuid + "AccessDeniedException: "
			+ e.getMessage() + " Retrying login.");
		nodeKey = getRepositoryService().copyNodeVersion(getTicket(true), uuid, null);
	    }
	} catch (ItemNotFoundException e) {
	    log.warn("Unable to to copy node " + uuid + " as the node cannot be found. Repository Exception: "
		    + e.getMessage() + " Retry not possible.");
	    throw e;
	} catch (RepositoryCheckedException e) {
	    log.warn("Unable to to copy node " + uuid + "Repository Exception: " + e.getMessage()
		    + " Retry not possible.");
	    throw e;
	}
	return nodeKey;
    }

    @Override
    public void deleteFile(Long uuid) throws InvalidParameterException, RepositoryCheckedException {
	try {
	    try {
		getRepositoryService().deleteNode(getTicket(false), uuid);
	    } catch (AccessDeniedException e) {
		log.warn("Unable to access repository to delete file id" + uuid + "AccessDeniedException: "
			+ e.getMessage() + " Retrying login.");
		getRepositoryService().deleteNode(getTicket(true), uuid);
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
    public IVersionedNode getFileNode(Long uuid)
	    throws ItemNotFoundException, FileException, RepositoryCheckedException {
	try {
	    try {
		return getRepositoryService().getFileItem(getTicket(false), uuid, null);
	    } catch (AccessDeniedException e) {
		log.warn("Unable to access repository to get file id" + uuid + "AccessDeniedException: "
			+ e.getMessage() + " Retrying login.");
		return getRepositoryService().getFileItem(getTicket(true), uuid, null);
	    }
	} catch (RepositoryCheckedException e2) {
	    log.warn("Unable to to get file id" + uuid + "Repository Exception: " + e2.getMessage()
		    + " Retry not possible.");
	    throw e2;
	}
    }

    @Override
    public Set getFileProperties(Long uuid) throws ItemNotFoundException, FileException, RepositoryCheckedException {
	try {
	    try {
		IVersionedNode node = getRepositoryService().getFileItem(getTicket(false), uuid, null);
		return node != null ? node.getProperties() : null;
	    } catch (AccessDeniedException e) {
		log.warn("Unable to access repository to get file id" + uuid + "AccessDeniedException: "
			+ e.getMessage() + " Retrying login.");
		IVersionedNode node = getRepositoryService().getFileItem(getTicket(true), uuid, null);
		return node != null ? node.getProperties() : null;
	    }
	} catch (RepositoryCheckedException e2) {
	    log.warn("Unable to to get file id" + uuid + "Repository Exception: " + e2.getMessage()
		    + " Retry not possible.");
	    throw e2;
	}
    }

/*
 * protected File getFile(String fileName, InputStream is) throws FileNotFoundException, Exception {
 * InputStream in = new BufferedInputStream(is, 500);
 * File file = new File(fileName);
 * OutputStream out = new BufferedOutputStream(new FileOutputStream(file), 500);
 * int bytes;
 * while ((bytes = in.available()) > 0) {
 * byte[] byteArray = new byte[bytes];
 * in.read(byteArray);
 * out.write(byteArray);
 * }
 * in.close();
 * out.close();
 * out.flush();
 * return file;
 * }
 * 
 * protected byte[] getBytes(File file) throws FileNotFoundException, Exception {
 * byte[] byteArray = new byte[(int) file.length()];
 * FileInputStream stream = new FileInputStream(file);
 * stream.read(byteArray);
 * stream.close();
 * return byteArray;
 * }
 */

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
    @Override
    public void setRepositoryService(IRepositoryService repositoryService) {
	this.repositoryService = repositoryService;
    }
}
