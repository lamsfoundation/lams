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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.taskList.service;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

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
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.taskList.TaskListConstants;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListAttachmentDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListItemDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListItemVisitDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListSessionDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListUserDAO;
import org.lamsfoundation.lams.tool.taskList.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.taskList.dto.Summary;
import org.lamsfoundation.lams.tool.taskList.model.TaskList;
import org.lamsfoundation.lams.tool.taskList.model.TaskListAttachment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemVisitLog;
import org.lamsfoundation.lams.tool.taskList.model.TaskListSession;
import org.lamsfoundation.lams.tool.taskList.model.TaskListUser;
import org.lamsfoundation.lams.tool.taskList.util.TaskListToolContentHandler;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;

/**
 * 
 * @author Dapeng.Ni 
 * 
 */
public class TaskListServiceImpl implements
                              ITaskListService,ToolContentManager, ToolSessionManager, ToolContentImport102Manager
               
{
	static Logger log = Logger.getLogger(TaskListServiceImpl.class.getName());
	private TaskListDAO taskListDao;
	private TaskListItemDAO taskListItemDao;
	private TaskListAttachmentDAO taskListAttachmentDao;
	private TaskListUserDAO taskListUserDao;
	private TaskListSessionDAO taskListSessionDao;
	private TaskListItemVisitDAO taskListItemVisitDao;
	//tool service
	private TaskListToolContentHandler taskListToolContentHandler;
	private MessageService messageService;
	//system services
	private IRepositoryService repositoryService;
	private ILamsToolService toolService;
	private ILearnerService learnerService;
	private IAuditService auditService;
	private IUserManagementService userManagementService; 
	private IExportToolContentService exportContentService;
	private ICoreNotebookService coreNotebookService;
	
	
	private class ReflectDTOComparator implements Comparator<ReflectDTO>{
		public int compare(ReflectDTO o1, ReflectDTO o2) {
			if(o1 != null && o2 != null){
				return o1.getFullName().compareTo(o2.getFullName());
			}else if(o1 != null)
				return 1;
			else
				return -1;
		}
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
	private IVersionedNode getFile(Long uuid, Long versionId, String relativePath) throws TaskListApplicationException {
		
		ITicket tic = getRepositoryLoginTicket();
		
		try {
			
			return repositoryService.getFileItem(tic, uuid, versionId, relativePath);
			
		} catch (AccessDeniedException e) {
			
			String error = "Unable to access repository to get file uuid "+uuid
				+" version id "+versionId
				+" path " + relativePath+".";
			
			error = error+"AccessDeniedException: "+e.getMessage()+" Unable to retry further.";
			log.error(error);
			throw new TaskListApplicationException(error,e);
			
		} catch (Exception e) {
			
			String error = "Unable to access repository to get file uuid "+uuid
			+" version id "+versionId
			+" path " + relativePath+"."
			+" Exception: "+e.getMessage();
			log.error(error);
			throw new TaskListApplicationException(error,e);
			
		} 
	}
	/**
	 * This method verifies the credentials of the TaskList Tool and gives it
	 * the <code>Ticket</code> to login and access the Content Repository.
	 * 
	 * A valid ticket is needed in order to access the content from the
	 * repository. This method would be called evertime the tool needs to
	 * upload/download files from the content repository.
	 * 
	 * @return ITicket The ticket for repostory access
	 * @throws TaskListApplicationException
	 */
	private ITicket getRepositoryLoginTicket() throws TaskListApplicationException {
		ICredentials credentials = new SimpleCredentials(
				taskListToolContentHandler.getRepositoryUser(),
				taskListToolContentHandler.getRepositoryId());
		try {
			ITicket ticket = repositoryService.login(credentials,
					taskListToolContentHandler.getRepositoryWorkspaceName());
			return ticket;
		} catch (AccessDeniedException ae) {
			throw new TaskListApplicationException("Access Denied to repository."
					+ ae.getMessage());
		} catch (WorkspaceNotFoundException we) {
			throw new TaskListApplicationException("Workspace not found."
					+ we.getMessage());
		} catch (LoginException e) {
			throw new TaskListApplicationException("Login failed." + e.getMessage());
		}
	}


	public TaskList getTaskListByContentId(Long contentId) {
		TaskList rs = taskListDao.getByContentId(contentId);
		if(rs == null){
			log.error("Could not find the content by given ID:"+contentId);
		}
		return rs; 
	}


	public TaskList getDefaultContent(Long contentId) throws TaskListApplicationException {
    	if (contentId == null)
    	{
    		String error=messageService.getMessage("error.msg.default.content.not.find");
    	    log.error(error);
    	    throw new TaskListApplicationException(error);
    	}
    	
    	TaskList defaultContent = getDefaultTaskList();
    	//save default content by given ID.
    	TaskList content = new TaskList();
    	content = TaskList.newInstance(defaultContent,contentId,taskListToolContentHandler);
		return content;
	}

	public List getAuthoredItems(Long taskListUid) {
		return taskListItemDao.getAuthoringItems(taskListUid);
	}


	public TaskListAttachment uploadInstructionFile(FormFile uploadFile, String fileType) throws UploadTaskListFileException {
		if(uploadFile == null || StringUtils.isEmpty(uploadFile.getFileName()))
			throw new UploadTaskListFileException(messageService.getMessage("error.msg.upload.file.not.found",new Object[]{uploadFile}));
		
		//upload file to repository
		NodeKey nodeKey = processFile(uploadFile,fileType);
		
		//create new attachement
		TaskListAttachment file = new TaskListAttachment();
		file.setFileType(fileType);
		file.setFileUuid(nodeKey.getUuid());
		file.setFileVersionId(nodeKey.getVersion());
		file.setFileName(uploadFile.getFileName());
		
		return file;
	}


	public void createUser(TaskListUser taskListUser) {
		taskListUserDao.saveObject(taskListUser);
	}


	public TaskListUser getUserByIDAndContent(Long userId, Long contentId) {
		
		return (TaskListUser) taskListUserDao.getUserByUserIDAndContentID(userId,contentId);
		
	}
	public TaskListUser getUserByIDAndSession(Long userId, Long sessionId)  {
		
		return (TaskListUser) taskListUserDao.getUserByUserIDAndSessionID(userId,sessionId);
		
	}


	public void deleteFromRepository(Long fileUuid, Long fileVersionId) throws TaskListApplicationException {
		ITicket ticket = getRepositoryLoginTicket();
		try {
			repositoryService.deleteVersion(ticket, fileUuid,fileVersionId);
		} catch (Exception e) {
			throw new TaskListApplicationException(
					"Exception occured while deleting files from"
							+ " the repository " + e.getMessage());
		}
	}


	public void saveOrUpdateTaskList(TaskList taskList) {
		taskListDao.saveObject(taskList);
	}


	public void deleteTaskListAttachment(Long attachmentUid) {
		taskListAttachmentDao.removeObject(TaskListAttachment.class, attachmentUid);
		
	}


	public void saveOrUpdateTaskListItem(TaskListItem item) {
		taskListItemDao.saveObject(item);
	}


	public void deleteTaskListItem(Long uid) {
		taskListItemDao.removeObject(TaskListItem.class,uid);
	}

	public List<TaskListItem> getTaskListItemsBySessionId(Long sessionId) {
		TaskListSession session = taskListSessionDao.getSessionBySessionId(sessionId);
		if(session == null){
			log.error("Failed get TaskListSession by ID [" +sessionId + "]");
			return null;
		}
		//add taskList items from Authoring
		TaskList taskList = session.getTaskList();
		List<TaskListItem> items = new ArrayList<TaskListItem>(); 
		items.addAll(taskList.getTaskListItems());
		
		//add taskList items from TaskListSession
		items.addAll(session.getTaskListItems());
		
		return items;
	}
	public List<Summary> exportBySessionId(Long sessionId, boolean skipHide) {
		TaskListSession session = taskListSessionDao.getSessionBySessionId(sessionId);
		if(session == null){
			log.error("Failed get TaskListSession by ID [" +sessionId + "]");
			return null;
		}
		//initial taskList items list
		List<Summary> itemList = new ArrayList();
		Set<TaskListItem> resList = session.getTaskList().getTaskListItems();
		for(TaskListItem item:resList){
			if(skipHide && item.isHide())
				continue;
			//if item is create by author
			if(item.isCreateByAuthor()){
				Summary sum = new Summary(session.getSessionName(),item,false);
				itemList.add(sum);
			}
		}

		//get this session's all taskList items
		Set<TaskListItem> sessList =session.getTaskListItems();
		for(TaskListItem item:sessList){
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
	public List<List<Summary>> exportByContentId(Long contentId) {
		TaskList taskList = taskListDao.getByContentId(contentId);
		List<List<Summary>> groupList = new ArrayList();
		
		//create init taskList items list
		List<Summary> initList = new ArrayList();
		groupList.add(initList);
		Set<TaskListItem> resList = taskList.getTaskListItems();
		for(TaskListItem item:resList){
			if(item.isCreateByAuthor()){
				Summary sum = new Summary(null,item,true);
				initList.add(sum);
			}
		}
		
		//session by session
		List<TaskListSession> sessionList = taskListSessionDao.getByContentId(contentId);
		for(TaskListSession session:sessionList){
			List<Summary> group = new ArrayList<Summary>();
			//get this session's all taskList items
			Set<TaskListItem> sessList =session.getTaskListItems();
			for(TaskListItem item:sessList){
				//to skip all item create by author
				if(!item.isCreateByAuthor()){
					Summary sum = new Summary(session.getSessionName(),item,false);
					group.add(sum);
				}
			}
			if(group.size() == 0){
				group.add(new Summary(session.getSessionName(),null,false));
			}
			groupList.add(group);
		}
		
		return groupList;
	}
	public TaskList getTaskListBySessionId(Long sessionId){
		TaskListSession session = taskListSessionDao.getSessionBySessionId(sessionId);
		//to skip CGLib problem
		Long contentId = session.getTaskList().getContentId();
		TaskList res = taskListDao.getByContentId(contentId);
		return res;
	}
	public TaskListSession getTaskListSessionBySessionId(Long sessionId) {
		return taskListSessionDao.getSessionBySessionId(sessionId);
	}


	public void saveOrUpdateTaskListSession(TaskListSession resSession) {
		taskListSessionDao.saveObject(resSession);
	}



	public void retrieveComplete(SortedSet<TaskListItem> taskListItemList, TaskListUser user) {
		for(TaskListItem item:taskListItemList){
			TaskListItemVisitLog log = taskListItemVisitDao.getTaskListItemLog(item.getUid(),user.getUserId());
			if(log == null)
				item.setComplete(false);
			else
				item.setComplete(log.isComplete());
		}
	}


	public void setItemComplete(Long taskListItemUid, Long userId, Long sessionId) {
		TaskListItemVisitLog log = taskListItemVisitDao.getTaskListItemLog(taskListItemUid,userId);
		if(log == null){
			log = new TaskListItemVisitLog();
			TaskListItem item = taskListItemDao.getByUid(taskListItemUid);
			log.setTaskListItem(item);
			TaskListUser user = taskListUserDao.getUserByUserIDAndSessionID(userId, sessionId);
			log.setUser(user);
			log.setSessionId(sessionId);
			log.setAccessDate(new Timestamp(new Date().getTime()));
		}
		log.setComplete(true);
		taskListItemVisitDao.saveObject(log);
	}


	public void setItemAccess(Long taskListItemUid, Long userId, Long sessionId){
		TaskListItemVisitLog log = taskListItemVisitDao.getTaskListItemLog(taskListItemUid,userId);
		if(log == null){
			log = new TaskListItemVisitLog();
			TaskListItem item = taskListItemDao.getByUid(taskListItemUid);
			log.setTaskListItem(item);
			TaskListUser user = taskListUserDao.getUserByUserIDAndSessionID(userId, sessionId);
			log.setUser(user);
			log.setComplete(false);
			log.setSessionId(sessionId);
			log.setAccessDate(new Timestamp(new Date().getTime()));
			taskListItemVisitDao.saveObject(log);
		}
	}


	public String finishToolSession(Long toolSessionId, Long userId) throws TaskListApplicationException {
		TaskListUser user = taskListUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
		user.setSessionFinished(true);
		taskListUserDao.saveObject(user);
		
//		TaskListSession session = taskListSessionDao.getSessionBySessionId(toolSessionId);
//		session.setStatus(TaskListConstants.COMPLETED);
//		taskListSessionDao.saveObject(session);
		
		String nextUrl = null;
		try {
			nextUrl = this.leaveToolSession(toolSessionId,userId);
		} catch (DataMissingException e) {
			throw new TaskListApplicationException(e);
		} catch (ToolException e) {
			throw new TaskListApplicationException(e);
		}
		return nextUrl;
	}

//	public int checkMiniView(Long toolSessionId, Long userUid) {
//		int miniView = taskListItemVisitDao.getUserViewLogCount(toolSessionId,userUid);
//		TaskListSession session = taskListSessionDao.getSessionBySessionId(toolSessionId);
//		if(session == null){
//			log.error("Failed get session by ID [" + toolSessionId + "]");
//			return 0;
//		}
//		int reqView = session.getTaskList().getMiniViewTaskListNumber();
//		
//		return (reqView - miniView);
//	}

	public TaskListItem getTaskListItemByUid(Long itemUid) {
		return taskListItemDao.getByUid(itemUid);
	}
	public List<List<Summary>> getSummary(Long contentId) {
		List<List<Summary>> groupList = new ArrayList<List<Summary>>();
		List<Summary> group = new ArrayList<Summary>();
		
		//get all item which is accessed by user
		Map<Long,Integer> visitCountMap = taskListItemVisitDao.getSummary(contentId);
		
		TaskList taskList = taskListDao.getByContentId(contentId);
		Set<TaskListItem> resItemList = taskList.getTaskListItems();

		//get all sessions in a taskList and retrieve all taskList items under this session
		//plus initial taskList items by author creating (resItemList) 
		List<TaskListSession> sessionList = taskListSessionDao.getByContentId(contentId);
		for(TaskListSession session:sessionList){
			//one new group for one session.
			group = new ArrayList<Summary>();
			//firstly, put all initial taskList item into this group.
			for(TaskListItem item:resItemList){
				Summary sum = new Summary(session.getSessionId(),session.getSessionName(),item);
				//set viewNumber according visit log
				if(visitCountMap.containsKey(item.getUid()))
					sum.setViewNumber(visitCountMap.get(item.getUid()).intValue());
				group.add(sum);
			}
			//get this session's all taskList items
			Set<TaskListItem> sessItemList =session.getTaskListItems();
			for(TaskListItem item : sessItemList){
				//to skip all item create by author
				if(!item.isCreateByAuthor()){
					Summary sum = new Summary(session.getSessionId(),session.getSessionName(),item);
					//set viewNumber according visit log
					if(visitCountMap.containsKey(item.getUid()))
						sum.setViewNumber(visitCountMap.get(item.getUid()).intValue());					
					group.add(sum);
				}
			}
			//so far no any item available, so just put session name info to Summary
			if(group.size() == 0)
				group.add(new Summary(session.getSessionId(),session.getSessionName(),null));
			groupList.add(group);
		} 
		
		return groupList;

	}
	public Map<Long, Set<ReflectDTO>> getReflectList(Long contentId){
		Map<Long, Set<ReflectDTO>> map = new HashMap<Long, Set<ReflectDTO>>();

		List<TaskListSession> sessionList = taskListSessionDao.getByContentId(contentId);
		for(TaskListSession session:sessionList){
			Long sessionId = session.getSessionId();
			boolean hasRefection = session.getTaskList().isReflectOnActivity();
			Set<ReflectDTO> list = new TreeSet<ReflectDTO>(this.new ReflectDTOComparator());
			//get all users in this session
			List<TaskListUser> users = taskListUserDao.getBySessionID(sessionId);
			for(TaskListUser user : users){
				ReflectDTO ref = new ReflectDTO(user);
				ref.setHasRefection(hasRefection);
				list.add(ref);
			}
			map.put(sessionId, list);
		}
		
		return map;
	}
	public List<TaskListUser> getUserListBySessionItem(Long sessionId, Long itemUid) {
		List<TaskListItemVisitLog> logList = taskListItemVisitDao.getTaskListItemLogBySession(sessionId,itemUid);
		List<TaskListUser> userList = new ArrayList(logList.size());
		for(TaskListItemVisitLog visit : logList){
			TaskListUser user = visit.getUser();
			user.setAccessDate(visit.getAccessDate());
			userList.add(user);
		}
		return userList;
	}

	public void setItemVisible(Long itemUid, boolean visible) {
		TaskListItem item = taskListItemDao.getByUid(itemUid);
		if ( item != null ) {
			//createBy should be null for system default value.
			Long userId = 0L;
			String loginName = "No user"; 
			if(item.getCreateBy() != null){
				userId =  item.getCreateBy().getUserId();
				loginName = item.getCreateBy().getLoginName();
			}
			if ( visible ) {
				auditService.logShowEntry(TaskListConstants.TOOL_SIGNATURE,userId, 
						loginName, item.toString());
			} else {
				auditService.logHideEntry(TaskListConstants.TOOL_SIGNATURE, userId,
						loginName, item.toString());
			}
			item.setHide(!visible);
			taskListItemDao.saveObject(item);
		}
	}
	public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId, String entryText) {
		return coreNotebookService.createNotebookEntry(sessionId, notebookToolType, toolSignature, userId, "", entryText);
	}
	public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID){
		List<NotebookEntry> list = coreNotebookService.getEntry(sessionId, idType, signature, userID);
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}
	
	/**
	 * @param notebookEntry
	 */
	public void updateEntry(NotebookEntry notebookEntry) {
		coreNotebookService.updateEntry(notebookEntry);
	}
	
	public TaskListUser getUser(Long uid){
		return (TaskListUser) taskListUserDao.getObject(TaskListUser.class, uid);
	}
	//*****************************************************************************
	// private methods
	//*****************************************************************************
	private TaskList getDefaultTaskList() throws TaskListApplicationException {
    	Long defaultTaskListId = getToolDefaultContentIdBySignature(TaskListConstants.TOOL_SIGNATURE);
    	TaskList defaultTaskList = getTaskListByContentId(defaultTaskListId);
    	if(defaultTaskList == null)
    	{
    	    String error=messageService.getMessage("error.msg.default.content.not.find");
    	    log.error(error);
    	    throw new TaskListApplicationException(error);
    	}
    	
    	return defaultTaskList;
	}
    private Long getToolDefaultContentIdBySignature(String toolSignature) throws TaskListApplicationException
    {
        Long contentId = null;
    	contentId=new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));    
    	if (contentId == null)
    	{
    		String error=messageService.getMessage("error.msg.default.content.not.find");
    	    log.error(error);
    	    throw new TaskListApplicationException(error);
    	}
	    return contentId;
    }
    /**
     * Process an uploaded file.
     * 
     * @throws TaskListApplicationException 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RepositoryCheckedException
     * @throws InvalidParameterException
     */
    private NodeKey processFile(FormFile file, String fileType) throws UploadTaskListFileException {
    	NodeKey node = null;
        if (file!= null && !StringUtils.isEmpty(file.getFileName())) {
            String fileName = file.getFileName();
            try {
				node = taskListToolContentHandler.uploadFile(file.getInputStream(), fileName, 
				        file.getContentType(), fileType);
			} catch (InvalidParameterException e) {
				throw new UploadTaskListFileException (messageService.getMessage("error.msg.invaid.param.upload"));
			} catch (FileNotFoundException e) {
				throw new UploadTaskListFileException (messageService.getMessage("error.msg.file.not.found"));
			} catch (RepositoryCheckedException e) {
				throw new UploadTaskListFileException (messageService.getMessage("error.msg.repository"));
			} catch (IOException e) {
				throw new UploadTaskListFileException (messageService.getMessage("error.msg.io.exception"));
			}
          }
        return node;
    }
	private NodeKey processPackage(String packageDirectory, String initFile) throws UploadTaskListFileException {
		NodeKey node = null;
		try {
			node = taskListToolContentHandler.uploadPackage(packageDirectory, initFile);
		} catch (InvalidParameterException e) {
			throw new UploadTaskListFileException (messageService.getMessage("error.msg.invaid.param.upload"));
		} catch (RepositoryCheckedException e) {
			throw new UploadTaskListFileException (messageService.getMessage("error.msg.repository"));
		}
        return node;
	}

	/**
	 * Find out default.htm/html or index.htm/html in the given directory folder
	 * @param packageDirectory
	 * @return
	 */
	private String findWebsiteInitialItem(String packageDirectory) {
		File file = new File(packageDirectory);
		if(!file.isDirectory())
			return null;
		
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
			return null;
	}


	//*****************************************************************************
	// set methods for Spring Bean
	//*****************************************************************************
	public void setAuditService(IAuditService auditService) {
		this.auditService = auditService;
	}
	public void setLearnerService(ILearnerService learnerService) {
		this.learnerService = learnerService;
	}
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	public void setRepositoryService(IRepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	public void setTaskListAttachmentDao(TaskListAttachmentDAO taskListAttachmentDao) {
		this.taskListAttachmentDao = taskListAttachmentDao;
	}
	public void setTaskListDao(TaskListDAO taskListDao) {
		this.taskListDao = taskListDao;
	}
	public void setTaskListItemDao(TaskListItemDAO taskListItemDao) {
		this.taskListItemDao = taskListItemDao;
	}
	public void setTaskListSessionDao(TaskListSessionDAO taskListSessionDao) {
		this.taskListSessionDao = taskListSessionDao;
	}
	public void setTaskListToolContentHandler(TaskListToolContentHandler taskListToolContentHandler) {
		this.taskListToolContentHandler = taskListToolContentHandler;
	}
	public void setTaskListUserDao(TaskListUserDAO taskListUserDao) {
		this.taskListUserDao = taskListUserDao;
	}
	public void setToolService(ILamsToolService toolService) {
		this.toolService = toolService;
	}

	public TaskListItemVisitDAO getTaskListItemVisitDao() {
		return taskListItemVisitDao;
	}
	public void setTaskListItemVisitDao(TaskListItemVisitDAO taskListItemVisitDao) {
		this.taskListItemVisitDao = taskListItemVisitDao;
	}

	//*******************************************************************************
	//ToolContentManager, ToolSessionManager methods
	//*******************************************************************************
	
	public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
		TaskList toolContentObj = taskListDao.getByContentId(toolContentId);
 		if(toolContentObj == null) {
 			try {
				toolContentObj = getDefaultTaskList();
			} catch (TaskListApplicationException e) {
				throw new DataMissingException(e.getMessage());
			}
 		}
 		if(toolContentObj == null)
 			throw new DataMissingException("Unable to find default content for the taskList tool");
 		
 		//set TaskListToolContentHandler as null to avoid copy file node in repository again.
 		toolContentObj = TaskList.newInstance(toolContentObj,toolContentId,null);
 		toolContentObj.setToolContentHandler(null);
 		toolContentObj.setOfflineFileList(null);
 		toolContentObj.setOnlineFileList(null);
		try {
			exportContentService.registerFileClassForExport(TaskListAttachment.class.getName(),"fileUuid","fileVersionId");
			exportContentService.registerFileClassForExport(TaskListItem.class.getName(),"fileUuid","fileVersionId");
			exportContentService.exportToolContent( toolContentId, toolContentObj,taskListToolContentHandler, rootPath);
		} catch (ExportToolContentException e) {
			throw new ToolException(e);
		}
	}


	public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath ,String fromVersion,String toVersion) throws ToolException {
	
		try {
			exportContentService.registerFileClassForImport(TaskListAttachment.class.getName()
					,"fileUuid","fileVersionId","fileName","fileType",null,null);
			exportContentService.registerFileClassForImport(TaskListItem.class.getName()
					,"fileUuid","fileVersionId","fileName","fileType",null,"initialItem");
			
			Object toolPOJO =  exportContentService.importToolContent(toolContentPath,taskListToolContentHandler,fromVersion,toVersion);
			if(!(toolPOJO instanceof TaskList))
				throw new ImportToolContentException("Import Share taskList tool content failed. Deserialized object is " + toolPOJO);
			TaskList toolContentObj = (TaskList) toolPOJO;
			
//			reset it to new toolContentId
			toolContentObj.setContentId(toolContentId);
			TaskListUser user = taskListUserDao.getUserByUserIDAndContentID(new Long(newUserUid.longValue()), toolContentId);
			if(user == null){
				user = new TaskListUser();
				UserDTO sysUser = ((User)userManagementService.findById(User.class,newUserUid)).getUserDTO();
				user.setFirstName(sysUser.getFirstName());
				user.setLastName(sysUser.getLastName());
				user.setLoginName(sysUser.getLogin());
				user.setUserId(new Long(newUserUid.longValue()));
				user.setTaskList(toolContentObj);
			}
			toolContentObj.setCreatedBy(user);
			
			//reset all taskListItem createBy user
			Set<TaskListItem> items = toolContentObj.getTaskListItems();
			for(TaskListItem item:items){
				item.setCreateBy(user);
			}
			taskListDao.saveObject(toolContentObj);
		} catch (ImportToolContentException e) {
			throw new ToolException(e);
		}
	}

	  /** Get the definitions for possible output for an activity, based on the toolContentId. These may be definitions that are always
     * available for the tool (e.g. number of marks for Multiple Choice) or a custom definition created for a particular activity
     * such as the answer to the third question contains the word Koala and hence the need for the toolContentId
     * @return SortedMap of ToolOutputDefinitions with the key being the name of each definition
     */
	public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId) throws ToolException {
		return new TreeMap<String, ToolOutputDefinition>();
	}
 

	public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
		if (toContentId == null)
			throw new ToolException(
					"Failed to create the SharedTaskListFiles tool seession");

		TaskList taskList = null;
		if ( fromContentId != null ) {
			taskList = 	taskListDao.getByContentId(fromContentId);
		}
		if ( taskList == null ) {
			try {
				taskList = getDefaultTaskList();
			} catch (TaskListApplicationException e) {
				throw new ToolException(e);
			}
		}

		TaskList toContent = TaskList.newInstance(taskList,toContentId,taskListToolContentHandler);
		taskListDao.saveObject(toContent);
		
		//save taskList items as well
		Set items = toContent.getTaskListItems();
		if(items != null){
			Iterator iter = items.iterator();
			while(iter.hasNext()){
				TaskListItem item = (TaskListItem) iter.next();
//				createRootTopic(toContent.getUid(),null,msg);
			}
		}
	}


	public void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException {
		TaskList taskList = taskListDao.getByContentId(toolContentId);
		if(taskList == null){
			throw new ToolException("No found tool content by given content ID:" + toolContentId);
		}
		taskList.setDefineLater(value);
	}


	public void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException {
		TaskList taskList = taskListDao.getByContentId(toolContentId);
		if(taskList == null){
			throw new ToolException("No found tool content by given content ID:" + toolContentId);
		}
		taskList.setRunOffline(value);		
	}


	public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException, ToolException {
		TaskList taskList = taskListDao.getByContentId(toolContentId);
		if(removeSessionData){
			List list = taskListSessionDao.getByContentId(toolContentId);
			Iterator iter = list.iterator();
			while(iter.hasNext()){
				TaskListSession session = (TaskListSession ) iter.next();
				taskListSessionDao.delete(session);
			}
		}
		taskListDao.delete(taskList);
	}

	
	public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
		TaskListSession session = new TaskListSession();
		session.setSessionId(toolSessionId);
		session.setSessionName(toolSessionName);
		TaskList taskList = taskListDao.getByContentId(toolContentId);
		session.setTaskList(taskList);
		taskListSessionDao.saveObject(session);
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
		
		TaskListSession session = taskListSessionDao.getSessionBySessionId(toolSessionId);
		if(session != null){
			session.setStatus(TaskListConstants.COMPLETED);
			taskListSessionDao.saveObject(session);
		}else{
			log.error("Fail to leave tool Session.Could not find shared taskList " +
					"session by given session id: "+toolSessionId);
			throw new DataMissingException("Fail to leave tool Session." +
					"Could not find shared taskList session by given session id: "+toolSessionId);
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
		taskListSessionDao.deleteBySessionId(toolSessionId);
	}
	
	/** 
	 * Get the tool output for the given tool output names.
	 * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long, java.lang.Long)
	 */
	public SortedMap<String, ToolOutput> getToolOutput(List<String> names,
			Long toolSessionId, Long learnerId) {
		return new TreeMap<String,ToolOutput>();
	}

	/** 
	 * Get the tool output for the given tool output name.
	 * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long, java.lang.Long)
	 */
	public ToolOutput getToolOutput(String name, Long toolSessionId,
			Long learnerId) {
		return null;
	}

	/* ===============Methods implemented from ToolContentImport102Manager =============== */
	

    /**
     * Import the data for a 1.0.2 Noticeboard or HTMLNoticeboard
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues)
    {
    	Date now = new Date();
    	TaskList toolContentObj = new TaskList();

    	try {
	    	toolContentObj.setTitle((String)importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
	    	toolContentObj.setContentId(toolContentId);
	    	toolContentObj.setContentInUse(Boolean.FALSE);
	    	toolContentObj.setCreated(now);
	    	toolContentObj.setDefineLater(Boolean.FALSE);
	    	toolContentObj.setInstructions(WebUtil.convertNewlines((String)importValues.get(ToolContentImport102Manager.CONTENT_BODY)));
	    	toolContentObj.setOfflineInstructions(null);
	    	toolContentObj.setOnlineInstructions(null);
	    	toolContentObj.setRunOffline(Boolean.FALSE);
	    	toolContentObj.setUpdated(now);
   	    	toolContentObj.setReflectOnActivity(Boolean.FALSE);
	    	toolContentObj.setReflectInstructions(null);

	    	//TODO ?????????????????????????????????????
//	    	toolContentObj.setRunAuto(Boolean.FALSE);
//	    	Boolean bool = WDDXProcessor.convertToBoolean(importValues, ToolContentImport102Manager.CONTENT_URL_RUNTIME_LEARNER_SUBMIT_FILE);
//	    	toolContentObj.setAllowAddFiles(bool != null ? bool : Boolean.TRUE); 
//	    	bool = WDDXProcessor.convertToBoolean(importValues, ToolContentImport102Manager.CONTENT_URL_RUNTIME_LEARNER_SUBMIT_URL);
//	    	toolContentObj.setAllowAddUrls(bool != null ? bool : Boolean.TRUE); 
//	    	Integer minToComplete = WDDXProcessor.convertToInteger(importValues, ToolContentImport102Manager.CONTENT_URL_MIN_NUMBER_COMPLETE);
//	    	toolContentObj.setMiniViewTaskListNumber(minToComplete != null ? minToComplete.intValue() : 0); 
//	    	bool = WDDXProcessor.convertToBoolean(importValues, ToolContentImport102Manager.CONTENT_URL_RUNTIME_LEARNER_SUBMIT_URL);
//	    	toolContentObj.setLockWhenFinished(Boolean.FALSE);
//	    	toolContentObj.setRunAuto(Boolean.FALSE);
	    	
	    	// leave as empty, no need to set them to anything.
	    	//toolContentObj.setAttachments(attachments);
    	
/*	    	 unused entries from 1.0.2
             [directoryName=]  no equivalent in 2.0
             [runtimeSubmissionStaffFile=true] no equivalent in 2.0
             [contentShowUser=false]  no equivalent in 2.0
             [isHTML=false]  no equivalent in 2.0
             [showbuttons=false]  no equivalent in 2.0
             [isReusable=false]   not used in 1.0.2 (would be lock when finished)
*/
	    	TaskListUser ruser = new TaskListUser();
	    	ruser.setUserId(new Long(user.getUserID().longValue()));
	    	ruser.setFirstName(user.getFirstName());
	    	ruser.setLastName(user.getLastName());
	    	ruser.setLoginName(user.getLogin());
			createUser(ruser);
		    toolContentObj.setCreatedBy(ruser);
	
	    	// TaskList Items. They are ordered on the screen by create date so they need to be saved in the right order.
		    // So read them all in first, then go through and assign the dates in the correct order and then save.
	    	Vector tasks = (Vector) importValues.get(ToolContentImport102Manager.CONTENT_URL_URLS);
	    	SortedMap<Integer,TaskListItem> items = new TreeMap<Integer,TaskListItem>();
	    	if ( tasks != null ) {
	    		int dummySequenceNumber = tasks.size(); // dummy number in case we can't convert question order
	    		Iterator iter = tasks.iterator();
	    		while ( iter.hasNext() ) {
	    			Hashtable urlMap = (Hashtable) iter.next();
					Integer itemOrder = WDDXProcessor.convertToInteger(urlMap, ToolContentImport102Manager.CONTENT_URL_URL_VIEW_ORDER);
	    			TaskListItem item = new TaskListItem();
	    			item.setSequenceId(itemOrder!=null?itemOrder.intValue():dummySequenceNumber++);
	    			item.setTitle((String) urlMap.get(ToolContentImport102Manager.CONTENT_TITLE));
	    			item.setCreateBy(ruser);
	    			item.setCreateByAuthor(true);
	    			item.setHide(false);

//	    			String taskListType = (String) urlMap.get(ToolContentImport102Manager.CONTENT_URL_URL_TYPE);
//	    			if ( ToolContentImport102Manager.URL_RESOURCE_TYPE_URL.equals(taskListType) ) {
//	    				item.setType(TaskListConstants.RESOURCE_TYPE_URL);
//	    				item.setUrl((String) urlMap.get(ToolContentImport102Manager.CONTENT_URL_URL_URL));
//	    				item.setOpenUrlNewWindow(false);
//	    			} else {
//	    				throw new ToolException("Invalid shared taskList type. Type was "+taskListType);
//	    			}

	    			items.put(itemOrder, item);
	    		}
	    	}
	    	
    		Iterator iter = items.values().iterator();
		    Date itemDate = null;
    		while ( iter.hasNext() ) {
       			if ( itemDate != null ) {
    				try {
    					Thread.sleep(1000);
    				} catch (Exception e) {}
    			}
       			itemDate = new Date();
       			
       			TaskListItem item = (TaskListItem) iter.next();
    			item.setCreateDate(itemDate);
    			toolContentObj.getTaskListItems().add(item);
	    	}
	    	
    	} catch (WDDXProcessorConversionException e) {
    		log.error("Unable to content for activity "+toolContentObj.getTitle()+"properly due to a WDDXProcessorConversionException.",e);
    		throw new ToolException("Invalid import data format for activity "+toolContentObj.getTitle()+"- WDDX caused an exception. Some data from the design will have been lost. See log for more details.");
    	}

    	taskListDao.saveObject(toolContentObj);


    }
    
    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) 
    		throws ToolException, DataMissingException {
    	
    	TaskList toolContentObj = getTaskListByContentId(toolContentId);
    	if ( toolContentObj == null ) {
    		throw new DataMissingException("Unable to set reflective data titled "+title
	       			+" on activity toolContentId "+toolContentId
	       			+" as the tool content does not exist.");
    	}

    	toolContentObj.setReflectOnActivity(Boolean.TRUE);
    	toolContentObj.setReflectInstructions(description);
    }

    
	/* =================================================================================== */

    public IExportToolContentService getExportContentService() {
		return exportContentService;
	}


	public void setExportContentService(IExportToolContentService exportContentService) {
		this.exportContentService = exportContentService;
	}


	public IUserManagementService getUserManagementService() {
		return userManagementService;
	}


	public void setUserManagementService(IUserManagementService userManagementService) {
		this.userManagementService = userManagementService;
	}


	public ICoreNotebookService getCoreNotebookService() {
		return coreNotebookService;
	}


	public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
		this.coreNotebookService = coreNotebookService;
	}



}
