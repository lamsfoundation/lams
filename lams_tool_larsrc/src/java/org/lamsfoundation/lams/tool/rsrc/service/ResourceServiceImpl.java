/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.rsrc.service;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.tool.rsrc.ResourceConstants;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceAttachmentDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceItemDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceSessionDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceUserDAO;
import org.lamsfoundation.lams.tool.rsrc.ims.IContentPackageConverter;
import org.lamsfoundation.lams.tool.rsrc.ims.IMSManifestException;
import org.lamsfoundation.lams.tool.rsrc.ims.ImscpApplicationException;
import org.lamsfoundation.lams.tool.rsrc.ims.SimpleContentPackageConverter;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceAttachment;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceUser;
import org.lamsfoundation.lams.tool.rsrc.util.ResourceToolContentHandler;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;

/**
 * 
 * @author Dapeng.Ni 
 * 
 */
public class ResourceServiceImpl implements
                              IResourceService
               
{
	static Logger log = Logger.getLogger(ResourceServiceImpl.class.getName());
	private ResourceDAO resourceDao;
	private ResourceItemDAO resourceItemDao;
	private ResourceAttachmentDAO resourceAttachmentDao;
	private ResourceUserDAO resourceUserDao;
	private ResourceSessionDAO resourceSessionDao;
	//tool service
	private ResourceToolContentHandler resourceToolContentHandler;
	private MessageService messageService;
	//system services
	private IRepositoryService repositoryService;
	private ILamsToolService toolService;
	private ILearnerService learnerService;

	public IVersionedNode getFileNode(Long itemUid, String relPathString) throws ResourceApplicationException {
		ResourceItem item = (ResourceItem) resourceItemDao.getObject(ResourceItem.class,itemUid);
		if ( item == null )
			throw new ResourceApplicationException("Reource item "+itemUid+" not found.");

		return getFile(item.getFileUuid(), item.getFileVersionId(), relPathString);
	}
	
	
	//*******************************************************************************
	// Private method
	//*******************************************************************************
	/** Try to get the file. If forceLogin = false and an access denied exception occurs, call this method
	 * again to get a new ticket and retry file lookup. If forceLogin = true and it then fails 
	 * then throw exception.
	 * @param uuid
	 * @param versionId
	 * @param relativePath
	 * @param attemptCount
	 * @return file node
	 * @throws ImscpApplicationException
	 */
	private IVersionedNode getFile(Long uuid, Long versionId, String relativePath) throws ResourceApplicationException {
		
		ITicket tic = getRepositoryLoginTicket();
		
		try {
			
			return repositoryService.getFileItem(tic, uuid, versionId, relativePath);
			
		} catch (AccessDeniedException e) {
			
			String error = "Unable to access repository to get file uuid "+uuid
				+" version id "+versionId
				+" path " + relativePath+".";
			
			error = error+"AccessDeniedException: "+e.getMessage()+" Unable to retry further.";
			log.error(error);
			throw new ResourceApplicationException(error,e);
			
		} catch (Exception e) {
			
			String error = "Unable to access repository to get file uuid "+uuid
			+" version id "+versionId
			+" path " + relativePath+"."
			+" Exception: "+e.getMessage();
			log.error(error);
			throw new ResourceApplicationException(error,e);
			
		} 
	}
	/**
	 * This method verifies the credentials of the SubmitFiles Tool and gives it
	 * the <code>Ticket</code> to login and access the Content Repository.
	 * 
	 * A valid ticket is needed in order to access the content from the
	 * repository. This method would be called evertime the tool needs to
	 * upload/download files from the content repository.
	 * 
	 * @return ITicket The ticket for repostory access
	 * @throws SubmitFilesException
	 */
	private ITicket getRepositoryLoginTicket() throws ResourceApplicationException {
		ICredentials credentials = new SimpleCredentials(
				resourceToolContentHandler.getRepositoryUser(),
				resourceToolContentHandler.getRepositoryId());
		try {
			ITicket ticket = repositoryService.login(credentials,
					resourceToolContentHandler.getRepositoryWorkspaceName());
			return ticket;
		} catch (AccessDeniedException ae) {
			throw new ResourceApplicationException("Access Denied to repository."
					+ ae.getMessage());
		} catch (WorkspaceNotFoundException we) {
			throw new ResourceApplicationException("Workspace not found."
					+ we.getMessage());
		} catch (LoginException e) {
			throw new ResourceApplicationException("Login failed." + e.getMessage());
		}
	}


	public Resource getResourceByContentId(Long contentId) {
		Resource rs = resourceDao.getByContentId(contentId);
		if(rs == null){
			log.error("Could not find the content by given ID:"+contentId);
		}
		return rs; 
	}


	public Resource getDefaultContent(Long contentId) throws ResourceApplicationException {
    	if (contentId == null)
    	{
    	    String error="Could not retrieve default content id for Shared Resource tool";
    	    log.error(error);
    	    throw new ResourceApplicationException(error);
    	}
    	
    	Resource defaultContent = getDefaultResource();
    	//save default content by given ID.
    	Resource content = new Resource();
    	content = Resource.newInstance(defaultContent,contentId,resourceToolContentHandler);
    	//TODO: does it need replicate the content's resource items?
		return content;
	}

	public Set getAuthoredItems(Long resourceUid) {
		Resource resource = resourceDao.getByUid(resourceUid);
		
		return resource.getResourceItems();
	}


	public ResourceAttachment uploadInstructionFile(FormFile uploadFile, String fileType) throws ResourceApplicationException {
		if(uploadFile == null || StringUtils.isEmpty(uploadFile.getFileName()))
			throw new ResourceApplicationException("Could not find upload file: " + uploadFile);
		
		//upload file to repository
		NodeKey nodeKey = processFile(uploadFile,fileType);
		
		//create new attachement
		ResourceAttachment file = new ResourceAttachment();
		file.setFileType(fileType);
		file.setFileUuid(nodeKey.getUuid());
		file.setFileVersionId(nodeKey.getVersion());
		file.setFileName(uploadFile.getFileName());
		
		return file;
	}


	public void createUser(ResourceUser resourceUser) {
		resourceUserDao.saveObject(resourceUser);
	}


	public ResourceUser getUserByID(Long userUid) {
		
		return (ResourceUser) resourceUserDao.getUserByUserID(ResourceUser.class,userUid);
		
	}


	public void deleteFromRepository(Long fileUuid, Long fileVersionId) throws ResourceApplicationException {
		ITicket ticket = getRepositoryLoginTicket();
		try {
			repositoryService.deleteVersion(ticket, fileUuid,fileVersionId);
		} catch (Exception e) {
			throw new ResourceApplicationException(
					"Exception occured while deleting files from"
							+ " the repository " + e.getMessage());
		}
	}


	public void saveOrUpdateResource(Resource resource) {
		resourceDao.saveObject(resource);
	}


	public void deleteResourceAttachment(Long attachmentUid) {
		resourceAttachmentDao.removeObject(ResourceAttachment.class, attachmentUid);
		
	}


	public void saveOrUpdateResourceItem(ResourceItem item) {
		resourceItemDao.saveObject(item);
	}


	public void deleteResourceItem(Long uid) {
		resourceItemDao.removeObject(ResourceItem.class,uid);
	}


	//*****************************************************************************
	// private methods
	//*****************************************************************************
	private Resource getDefaultResource() throws ResourceApplicationException {
    	Long defaultResourceId = getToolDefaultContentIdBySignature(ResourceConstants.TOOL_SIGNNATURE);
    	Resource defaultResource = getResourceByContentId(defaultResourceId);
    	if(defaultResource == null)
    	{
    	    String error="Could not retrieve default content record for this tool";
    	    log.error(error);
    	    throw new ResourceApplicationException(error);
    	}
    	
    	return defaultResource;
	}
    private Long getToolDefaultContentIdBySignature(String toolSignature) throws ResourceApplicationException
    {
        Long contentId = null;
    	contentId=new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));    
    	if (contentId == null)
    	{
    	    String error="Could not retrieve default content id for this tool";
    	    log.error(error);
    	    throw new ResourceApplicationException(error);
    	}
	    return contentId;
    }
    /**
     * Process an uploaded file.
     * 
     * @param forumForm
     * @throws ResourceApplicationException 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RepositoryCheckedException
     * @throws InvalidParameterException
     */
    private NodeKey processFile(FormFile file, String fileType) throws ResourceApplicationException{
    	NodeKey node = null;
        if (file!= null && !StringUtils.isEmpty(file.getFileName())) {
            String fileName = file.getFileName();
            try {
				node = resourceToolContentHandler.uploadFile(file.getInputStream(), fileName, 
				        file.getContentType(), fileType);
			} catch (InvalidParameterException e) {
				throw new ResourceApplicationException("InvalidParameterException occured while trying to upload File" + e.getMessage());
			} catch (FileNotFoundException e) {
				throw new ResourceApplicationException("FileNotFoundException occured while trying to upload File" + e.getMessage());
			} catch (RepositoryCheckedException e) {
				throw new ResourceApplicationException("RepositoryCheckedException occured while trying to upload File" + e.getMessage());
			} catch (IOException e) {
				throw new ResourceApplicationException("IOException occured while trying to upload File" + e.getMessage());
			}
          }
        return node;
    }
	private NodeKey processPackage(String packageDirectory, String initFile) throws ResourceApplicationException {
		NodeKey node = null;
		try {
			node = resourceToolContentHandler.uploadPackage(packageDirectory, initFile);
		} catch (InvalidParameterException e) {
			throw new ResourceApplicationException("InvalidParameterException occured while trying to upload Package:" + e.getMessage());
		} catch (RepositoryCheckedException e) {
			throw new ResourceApplicationException("RepositoryCheckedException occured while trying to upload Package:" + e.getMessage());
		}
        return node;
	}

	public void uploadResourceItemFile(ResourceItem item, FormFile file) throws ResourceApplicationException {
		try {
			InputStream is = file.getInputStream();
			String fileName = file.getFileName();
			String fileType = file.getContentType();
			item.setFileType(fileType);
			item.setFileName(fileName);
			//For file only upload one sigle file
			if(item.getType() == ResourceConstants.RESOURCE_TYPE_FILE){
				NodeKey nodeKey = processFile(file,IToolContentHandler.TYPE_ONLINE);
				item.setFileUuid(nodeKey.getUuid());
				item.setFileVersionId(nodeKey.getVersion());
			}
			//need unzip upload, and check the initial item :default.htm/html or index.htm/html 
			if(item.getType() == ResourceConstants.RESOURCE_TYPE_WEBSITE){
				String packageDirectory = ZipFileUtil.expandZip(is, fileName);
				String initFile = findWebsiteInitialItem(packageDirectory);
				item.setInitialItem(initFile);
				//upload package
				NodeKey nodeKey = processPackage(packageDirectory,initFile);
				item.setFileUuid(nodeKey.getUuid());
				item.setFileVersionId(nodeKey.getVersion());
			}
			//need unzip upload, and parse learning object information from XML file. 
			if(item.getType() == ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT){
				String packageDirectory = ZipFileUtil.expandZip(is, fileName);
				IContentPackageConverter cpConverter = new SimpleContentPackageConverter(packageDirectory);
				String initFile = cpConverter.getDefaultItem();
				item.setInitialItem(initFile);
				item.setImsSchema(cpConverter.getSchema());
				item.setOrganizationXml(cpConverter.getOrganzationXML());
//				upload package
				NodeKey nodeKey = processPackage(packageDirectory,initFile);
				item.setFileUuid(nodeKey.getUuid());
				item.setFileVersionId(nodeKey.getVersion());
			}
			// create the package from the directory contents  
		
		} catch (ZipFileUtilException e) {
			log.error("ZipFileUtilException occurs when Uploading Resource Item :" + e.toString());
			throw new ResourceApplicationException(e);
		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException occurs when Uploading Resource Item:" + e.toString());
			throw new ResourceApplicationException(e);
		} catch (IOException e) {
			log.error("IOException occurs when Uploading Resource Item:" + e.toString());
			throw new ResourceApplicationException(e);
		} catch (IMSManifestException e) {
			log.error("IMSManifestException occurs when Uploading Resource Item:" + e.toString());
			throw new ResourceApplicationException(e);
		} catch (ImscpApplicationException e) {
			log.error("ImscpApplicationException occurs when Uploading Resource Item:" + e.toString());
			throw new ResourceApplicationException(e);
		}
	}


	/**
	 * Find out default.htm/html or index.htm/html in the given directory folder
	 * @param packageDirectory
	 * @return
	 */
	private String findWebsiteInitialItem(String packageDirectory) {
		File file = new File(packageDirectory);
		if(!file.isDirectory())
			return "";
		
		File[] initFiles = file.listFiles(new FileFilter(){
			public boolean accept(File pathname) {
				if(pathname == null || pathname.getName() == null)
					return false;
				String name = pathname.getName();
				if(name.endsWith("default.html")
					||name.endsWith("default.htm")
					||name.endsWith("index.html")
					||name.endsWith("index.htm"))
					return true;
				return false;
			}
		});
		if(initFiles != null && initFiles.length > 0)
			return initFiles[0].getName();
		else
			return "";
	}


	//*****************************************************************************
	// set methods for Spring Bean
	//*****************************************************************************
	public void setLearnerService(ILearnerService learnerService) {
		this.learnerService = learnerService;
	}
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	public void setRepositoryService(IRepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	public void setResourceAttachmentDao(ResourceAttachmentDAO resourceAttachmentDao) {
		this.resourceAttachmentDao = resourceAttachmentDao;
	}
	public void setResourceDao(ResourceDAO resourceDao) {
		this.resourceDao = resourceDao;
	}
	public void setResourceItemDao(ResourceItemDAO resourceItemDao) {
		this.resourceItemDao = resourceItemDao;
	}
	public void setResourceSessionDao(ResourceSessionDAO resourceSessionDao) {
		this.resourceSessionDao = resourceSessionDao;
	}
	public void setResourceToolContentHandler(ResourceToolContentHandler resourceToolContentHandler) {
		this.resourceToolContentHandler = resourceToolContentHandler;
	}
	public void setResourceUserDao(ResourceUserDAO resourceUserDao) {
		this.resourceUserDao = resourceUserDao;
	}
	public void setToolService(ILamsToolService toolService) {
		this.toolService = toolService;
	}

}
