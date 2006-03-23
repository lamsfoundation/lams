/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.rsrc.service;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.tool.rsrc.ResourceConstants;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceAttachmentDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceItemDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceSessionDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceUserDAO;
import org.lamsfoundation.lams.tool.rsrc.ims.ImscpApplicationException;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.tool.rsrc.util.ResourceToolContentHandler;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.util.MessageService;

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

		return getFile(item.getCrUuid(), item.getCrVersionId(), relPathString);
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
