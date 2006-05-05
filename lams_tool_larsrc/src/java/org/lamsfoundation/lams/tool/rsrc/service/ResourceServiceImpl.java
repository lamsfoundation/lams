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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.rsrc.ResourceConstants;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceAttachmentDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceItemDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceItemVisitDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceSessionDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceUserDAO;
import org.lamsfoundation.lams.tool.rsrc.dto.Summary;
import org.lamsfoundation.lams.tool.rsrc.ims.IContentPackageConverter;
import org.lamsfoundation.lams.tool.rsrc.ims.IMSManifestException;
import org.lamsfoundation.lams.tool.rsrc.ims.ImscpApplicationException;
import org.lamsfoundation.lams.tool.rsrc.ims.SimpleContentPackageConverter;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceAttachment;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItemVisitLog;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceSession;
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
                              IResourceService,ToolContentManager, ToolSessionManager
               
{
	static Logger log = Logger.getLogger(ResourceServiceImpl.class.getName());
	private ResourceDAO resourceDao;
	private ResourceItemDAO resourceItemDao;
	private ResourceAttachmentDAO resourceAttachmentDao;
	private ResourceUserDAO resourceUserDao;
	private ResourceSessionDAO resourceSessionDao;
	private ResourceItemVisitDAO resourceItemVisitDao;
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
	// Service method
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
	 * This method verifies the credentials of the Share Resource Tool and gives it
	 * the <code>Ticket</code> to login and access the Content Repository.
	 * 
	 * A valid ticket is needed in order to access the content from the
	 * repository. This method would be called evertime the tool needs to
	 * upload/download files from the content repository.
	 * 
	 * @return ITicket The ticket for repostory access
	 * @throws ResourceApplicationException
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
    		String error=messageService.getMessage("error.msg.default.content.not.find");
    	    log.error(error);
    	    throw new ResourceApplicationException(error);
    	}
    	
    	Resource defaultContent = getDefaultResource();
    	//save default content by given ID.
    	Resource content = new Resource();
    	content = Resource.newInstance(defaultContent,contentId,resourceToolContentHandler);
		return content;
	}

	public List getAuthoredItems(Long resourceUid) {
		return resourceItemDao.getAuthoringItems(resourceUid);
	}


	public ResourceAttachment uploadInstructionFile(FormFile uploadFile, String fileType) throws UploadResourceFileException {
		if(uploadFile == null || StringUtils.isEmpty(uploadFile.getFileName()))
			throw new UploadResourceFileException(messageService.getMessage("error.msg.upload.file.not.found",new Object[]{uploadFile}));
		
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
		
		return (ResourceUser) resourceUserDao.getUserByUserID(userUid);
		
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

	public List<ResourceItem> getResourceItemsBySessionId(Long sessionId) {
		ResourceSession session = resourceSessionDao.getSessionBySessionId(sessionId);
		if(session == null){
			log.error("Failed get ResourceSession by ID [" +sessionId + "]");
			return null;
		}
		//add resource items from Authoring
		Resource resource = session.getResource();
		List<ResourceItem> items = new ArrayList<ResourceItem>(); 
		items.addAll(resource.getResourceItems());
		
		//add resource items from ResourceSession
		items.addAll(session.getResourceItems());
		
		return items;
	}
	public List<Summary> exportBySessionId(Long sessionId, boolean skipHide) {
		ResourceSession session = resourceSessionDao.getSessionBySessionId(sessionId);
		if(session == null){
			log.error("Failed get ResourceSession by ID [" +sessionId + "]");
			return null;
		}
		//initial resource items list
		List<Summary> itemList = new ArrayList();
		Set<ResourceItem> resList = session.getResource().getResourceItems();
		for(ResourceItem item:resList){
			if(skipHide && item.isHide())
				continue;
			//if item is ha
			if(item.isCreateByAuthor()){
				Summary sum = new Summary(session.getSessionName(),item,false);
				itemList.add(sum);
			}
		}

		//get this session's all resource items
		Set<ResourceItem> sessList =session.getResourceItems();
		for(ResourceItem item:sessList){
			if(skipHide && item.isHide())
				continue;
			
			//to skip all item create by author
			if(!item.isCreateByAuthor()){
				Summary sum = new Summary(session.getSessionName(),item,false);
				itemList.add(sum);
			}
		}
		
		return itemList;
	}
	public List<List> exportByContentId(Long contentId) {
		Resource resource = resourceDao.getByContentId(contentId);
		List<List> groupList = new ArrayList();
		
		//create init resource items list
		List<Summary> initList = new ArrayList();
		groupList.add(initList);
		Set<ResourceItem> resList = resource.getResourceItems();
		for(ResourceItem item:resList){
			if(item.isCreateByAuthor()){
				Summary sum = new Summary(null,item,true);
				initList.add(sum);
			}
		}
		
		//session by session
		List<ResourceSession> sessionList = resourceSessionDao.getByContentId(contentId);
		for(ResourceSession session:sessionList){
			List<Summary> group = new ArrayList<Summary>();
			//get this session's all resource items
			Set<ResourceItem> sessList =session.getResourceItems();
			for(ResourceItem item:sessList){
				//to skip all item create by author
				if(!item.isCreateByAuthor()){
					Summary sum = new Summary(session.getSessionName(),item,false);
					group.add(sum);
				}
			}
			groupList.add(group);
		}
		
		return groupList;
	}
	public Resource getResourceBySessionId(Long sessionId){
		ResourceSession session = resourceSessionDao.getSessionBySessionId(sessionId);
		//to skip CGLib problem
		Long contentId = session.getResource().getContentId();
		Resource res = resourceDao.getByContentId(contentId);
		int miniView = res.getMiniViewResourceNumber();
		//construct dto fields;
		res.setMiniViewNumberStr(messageService.getMessage("label.learning.minimum.review",new Object[]{new Integer(miniView)}));
		return res;
	}
	public ResourceSession getResourceSessionBySessionId(Long sessionId) {
		return resourceSessionDao.getSessionBySessionId(sessionId);
	}


	public void saveOrUpdateResourceSession(ResourceSession resSession) {
		resourceSessionDao.saveObject(resSession);
	}



	public void retrieveComplete(List<ResourceItem> resourceItemList, ResourceUser user) {
		for(ResourceItem item:resourceItemList){
			ResourceItemVisitLog log = resourceItemVisitDao.getResourceItemLog(item.getUid(),user.getUserId());
			if(log == null)
				item.setComplete(false);
			else
				item.setComplete(log.isComplete());
		}
	}


	public void setItemComplete(Long resourceItemUid, Long userId, Long sessionId) {
		ResourceItemVisitLog log = resourceItemVisitDao.getResourceItemLog(resourceItemUid,userId);
		if(log == null){
			log = new ResourceItemVisitLog();
			ResourceItem item = resourceItemDao.getByUid(resourceItemUid);
			log.setResourceItem(item);
			ResourceUser user = resourceUserDao.getUserByUserID(userId); 
			log.setUser(user);
			log.setSessionId(sessionId);
			log.setAccessDate(new Timestamp(new Date().getTime()));
		}
		log.setComplete(true);
		resourceItemVisitDao.saveObject(log);
	}


	public void setItemAccess(Long resourceItemUid, Long userId, Long sessionId){
		ResourceItemVisitLog log = resourceItemVisitDao.getResourceItemLog(resourceItemUid,userId);
		if(log == null){
			log = new ResourceItemVisitLog();
			ResourceItem item = resourceItemDao.getByUid(resourceItemUid);
			log.setResourceItem(item);
			ResourceUser user = resourceUserDao.getUserByUserID(userId); 
			log.setUser(user);
			log.setComplete(false);
			log.setSessionId(sessionId);
			log.setAccessDate(new Timestamp(new Date().getTime()));
			resourceItemVisitDao.saveObject(log);
		}
	}


	public String finishToolSession(Long toolSessionId, Long userId) throws ResourceApplicationException {
		ResourceSession session = resourceSessionDao.getSessionBySessionId(toolSessionId);
		session.setStatus(ResourceConstants.COMPLETED);
		resourceSessionDao.saveObject(session);
		
		String nextUrl = null;
		try {
			nextUrl = this.leaveToolSession(toolSessionId,userId);
		} catch (DataMissingException e) {
			throw new ResourceApplicationException(e);
		} catch (ToolException e) {
			throw new ResourceApplicationException(e);
		}
		return nextUrl;
	}

	public int checkMiniView(Long toolSessionId, Long userUid) {
		int miniView = resourceItemVisitDao.getUserViewLogCount(toolSessionId,userUid);
		ResourceSession session = resourceSessionDao.getSessionBySessionId(toolSessionId);
		if(session == null){
			log.error("Failed get session by ID [" + toolSessionId + "]");
			return 0;
		}
		int reqView = session.getResource().getMiniViewResourceNumber();
		
		return (reqView - miniView);
	}

	public ResourceItem getResourceItemByUid(Long itemUid) {
		return resourceItemDao.getByUid(itemUid);
	}
	public List<List> getSummary(Long contentId) {
		List<List> groupList = new ArrayList<List>();
		List<Summary> group = new ArrayList<Summary>();
		
		//get all item which is accessed by user
		Map<Long,Integer> visitCountMap = resourceItemVisitDao.getSummary(contentId);
		
		Resource resource = resourceDao.getByContentId(contentId);
		Set<ResourceItem> resItemList = resource.getResourceItems();

		//get all sessions in a resource and retrieve all resource items under this session
		//plus initial resource items by author creating (resItemList) 
		List<ResourceSession> sessionList = resourceSessionDao.getByContentId(contentId);
		for(ResourceSession session:sessionList){
			//one new group for one session.
			group = new ArrayList<Summary>();
			//firstly, put all initial resource item into this group.
			for(ResourceItem item:resItemList){
				Summary sum = new Summary(session.getSessionId(),session.getSessionName(),item);
				//set viewNumber according visit log
				if(visitCountMap.containsKey(item.getUid()))
					sum.setViewNumber(visitCountMap.get(item.getUid()).intValue());
				group.add(sum);
			}
			//get this session's all resource items
			Set<ResourceItem> sessItemList =session.getResourceItems();
			for(ResourceItem item : sessItemList){
				//to skip all item create by author
				if(!item.isCreateByAuthor()){
					Summary sum = new Summary(session.getSessionId(),session.getSessionName(),item);
					//set viewNumber according visit log
					if(visitCountMap.containsKey(item.getUid()))
						sum.setViewNumber(visitCountMap.get(item.getUid()).intValue());					
					group.add(sum);
				}
			}
			groupList.add(group);
		}
		
		return groupList;

	}

	public List<ResourceUser> getUserListBySessionItem(Long sessionId, Long itemUid) {
		List<ResourceItemVisitLog> logList = resourceItemVisitDao.getResourceItemLogBySession(sessionId,itemUid);
		List<ResourceUser> userList = new ArrayList(logList.size());
		for(ResourceItemVisitLog visit : logList){
			userList.add(visit.getUser());
		}
		return userList;
	}

	public void setItemVisible(Long itemUid, boolean visible) {
		ResourceItem item = resourceItemDao.getByUid(itemUid);
		item.setHide(!visible);
		resourceItemDao.saveObject(item);
	}

	//*****************************************************************************
	// private methods
	//*****************************************************************************
	private Resource getDefaultResource() throws ResourceApplicationException {
    	Long defaultResourceId = getToolDefaultContentIdBySignature(ResourceConstants.TOOL_SIGNNATURE);
    	Resource defaultResource = getResourceByContentId(defaultResourceId);
    	if(defaultResource == null)
    	{
    	    String error=messageService.getMessage("error.msg.default.content.not.find");
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
    		String error=messageService.getMessage("error.msg.default.content.not.find");
    	    log.error(error);
    	    throw new ResourceApplicationException(error);
    	}
	    return contentId;
    }
    /**
     * Process an uploaded file.
     * 
     * @throws ResourceApplicationException 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RepositoryCheckedException
     * @throws InvalidParameterException
     */
    private NodeKey processFile(FormFile file, String fileType) throws UploadResourceFileException {
    	NodeKey node = null;
        if (file!= null && !StringUtils.isEmpty(file.getFileName())) {
            String fileName = file.getFileName();
            try {
				node = resourceToolContentHandler.uploadFile(file.getInputStream(), fileName, 
				        file.getContentType(), fileType);
			} catch (InvalidParameterException e) {
				throw new UploadResourceFileException (messageService.getMessage("error.msg.invaid.param.upload"));
			} catch (FileNotFoundException e) {
				throw new UploadResourceFileException (messageService.getMessage("error.msg.file.not.found"));
			} catch (RepositoryCheckedException e) {
				throw new UploadResourceFileException (messageService.getMessage("error.msg.repository"));
			} catch (IOException e) {
				throw new UploadResourceFileException (messageService.getMessage("error.msg.io.exception"));
			}
          }
        return node;
    }
	private NodeKey processPackage(String packageDirectory, String initFile) throws UploadResourceFileException {
		NodeKey node = null;
		try {
			node = resourceToolContentHandler.uploadPackage(packageDirectory, initFile);
		} catch (InvalidParameterException e) {
			throw new UploadResourceFileException (messageService.getMessage("error.msg.invaid.param.upload"));
		} catch (RepositoryCheckedException e) {
			throw new UploadResourceFileException (messageService.getMessage("error.msg.repository"));
		}
        return node;
	}

	public void uploadResourceItemFile(ResourceItem item, FormFile file) throws UploadResourceFileException {
		try {
			InputStream is = file.getInputStream();
			String fileName = file.getFileName();
			String fileType = file.getContentType();
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
			item.setFileType(fileType);
			item.setFileName(fileName);
		} catch (ZipFileUtilException e) {
			log.error(messageService.getMessage("error.msg.zip.file.exception") + " : " + e.toString());
			throw new UploadResourceFileException(messageService.getMessage("error.msg.zip.file.exception"));
		} catch (FileNotFoundException e) {
			log.error(messageService.getMessage("error.msg.file.not.found")+":" + e.toString());
			throw new UploadResourceFileException(messageService.getMessage("error.msg.file.not.found"));
		} catch (IOException e) {
			log.error(messageService.getMessage("error.msg.io.exception")+ ":" + e.toString());
			throw new UploadResourceFileException(messageService.getMessage("error.msg.io.exception"));
		} catch (IMSManifestException e) {
			log.error(messageService.getMessage("error.msg.ims.package")+":" + e.toString());
			throw new UploadResourceFileException(messageService.getMessage("error.msg.ims.package"));
		} catch (ImscpApplicationException e) {
			log.error( messageService.getMessage("error.msg.ims.application")+":" + e.toString());
			throw new UploadResourceFileException(messageService.getMessage("error.msg.ims.application"));
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

	public ResourceItemVisitDAO getResourceItemVisitDao() {
		return resourceItemVisitDao;
	}
	public void setResourceItemVisitDao(ResourceItemVisitDAO resourceItemVisitDao) {
		this.resourceItemVisitDao = resourceItemVisitDao;
	}

	//*******************************************************************************
	//ToolContentManager, ToolSessionManager methods
	//*******************************************************************************
	public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
		if (fromContentId == null || toContentId == null)
			throw new ToolException(
					"Failed to create the SharedResourceFiles tool seession");

		Resource resource = resourceDao.getByContentId(fromContentId);
		if ( resource == null ) {
			try {
				resource = getDefaultContent(fromContentId);
			} catch (ResourceApplicationException e) {
				throw new ToolException(e);
			}
		}
		Resource toContent = Resource.newInstance(resource,toContentId,resourceToolContentHandler);
		resourceDao.saveObject(toContent);
		
		//save resource items as well
		Set items = toContent.getResourceItems();
		if(items != null){
			Iterator iter = items.iterator();
			while(iter.hasNext()){
				ResourceItem item = (ResourceItem) iter.next();
//				createRootTopic(toContent.getUid(),null,msg);
			}
		}
	}


	public void setAsDefineLater(Long toolContentId) throws DataMissingException, ToolException {
		Resource resource = resourceDao.getByContentId(toolContentId);
		if(resource == null){
			throw new ToolException("No found tool content by given content ID:" + toolContentId);
		}
		resource.setDefineLater(true);
	}


	public void setAsRunOffline(Long toolContentId) throws DataMissingException, ToolException {
		Resource resource = resourceDao.getByContentId(toolContentId);
		if(resource == null){
			throw new ToolException("No found tool content by given content ID:" + toolContentId);
		}
		resource.setRunOffline(true);		
	}


	public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException, ToolException {
		Resource resource = resourceDao.getByContentId(toolContentId);
		if(removeSessionData){
			List list = resourceSessionDao.getByContentId(toolContentId);
			Iterator iter = list.iterator();
			while(iter.hasNext()){
				ResourceSession session = (ResourceSession ) iter.next();
				resourceSessionDao.delete(session);
			}
		}
		resourceDao.delete(resource);
	}


	public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
		ResourceSession session = new ResourceSession();
		session.setSessionId(toolSessionId);
		session.setSessionName(toolSessionName);
		Resource resource = resourceDao.getByContentId(toolContentId);
		session.setResource(resource);
		resourceSessionDao.saveObject(session);
	}


	public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
		if(toolSessionId == null){
			log.error("Fail to leave tool Session based on null tool session id.");
			throw new ToolException("Fail to remove tool Session based on null tool session id.");
		}
		if(learnerId == null){
			log.error("Fail to leave tool Session based on null learner.");
			throw new ToolException("Fail to remove tool Session based on null learner.");
		}
		
		ResourceSession session = resourceSessionDao.getSessionBySessionId(toolSessionId);
		if(session != null){
			session.setStatus(ResourceConstants.COMPLETED);
			resourceSessionDao.saveObject(session);
		}else{
			log.error("Fail to leave tool Session.Could not find shared resources " +
					"session by given session id: "+toolSessionId);
			throw new DataMissingException("Fail to leave tool Session." +
					"Could not find shared resource session by given session id: "+toolSessionId);
		}
		return learnerService.completeToolSession(toolSessionId,learnerId);
	}


	public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
		return null;
	}


	public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException, ToolException {
		return null;
	}


	public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
		resourceSessionDao.deleteBySessionId(toolSessionId);
	}


}
