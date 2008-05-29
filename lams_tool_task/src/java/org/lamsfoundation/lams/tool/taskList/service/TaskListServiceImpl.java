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
import org.lamsfoundation.lams.tool.taskList.dao.TaskListConditionDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListItemAttachmentDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListItemCommentDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListItemDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListItemVisitDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListSessionDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListUserDAO;
import org.lamsfoundation.lams.tool.taskList.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.taskList.dto.Summary;
import org.lamsfoundation.lams.tool.taskList.dto.ItemSummary;
import org.lamsfoundation.lams.tool.taskList.model.TaskList;
import org.lamsfoundation.lams.tool.taskList.model.TaskListAttachment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListCondition;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemAttachment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemComment;
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
 * Class implements <code>org.lamsfoundation.lams.tool.taskList.service.ITaskListService</code>.
 * 
 * @author Dapeng.Ni
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.taskList.service.ITaskListService
 */
public class TaskListServiceImpl implements ITaskListService,ToolContentManager, ToolSessionManager, ToolContentImport102Manager {
	static Logger log = Logger.getLogger(TaskListServiceImpl.class.getName());
	private TaskListDAO taskListDao;
	private TaskListItemDAO taskListItemDao;
	private TaskListAttachmentDAO taskListAttachmentDao;
	private TaskListConditionDAO taskListConditionDAO;
	private TaskListUserDAO taskListUserDao;
	private TaskListSessionDAO taskListSessionDao;
	private TaskListItemVisitDAO taskListItemVisitDao;
	private TaskListItemAttachmentDAO taskListItemAttachmentDao;
	private TaskListItemCommentDAO taskListItemCommentDAO;
	//tool service
	private TaskListToolContentHandler taskListToolContentHandler;
	private MessageService messageService;
	private TaskListOutputFactory taskListOutputFactory;
	//system services
	private IRepositoryService repositoryService;
	private ILamsToolService toolService;
	private ILearnerService learnerService;
	private IAuditService auditService;
	private IUserManagementService userManagementService; 
	private IExportToolContentService exportContentService;
	private ICoreNotebookService coreNotebookService;
	
	//*******************************************************************************
	// Methods implements ITaskListService.
	//*******************************************************************************
	
	/**
	 * {@inheritDoc}
	 */
	public TaskList getTaskListByContentId(Long contentId) {
		TaskList rs = taskListDao.getByContentId(contentId);
		if(rs == null){
			log.error("Could not find the content by given ID:"+contentId);
		}
		return rs; 
	}

	/**
	 * {@inheritDoc}
	 */
	public TaskList getDefaultContent(Long contentId) throws TaskListException {
    	if (contentId == null)
    	{
    		String error=messageService.getMessage("error.msg.default.content.not.find");
    	    log.error(error);
    	    throw new TaskListException(error);
    	}
    	
    	TaskList defaultContent = getDefaultTaskList();
    	//save default content by given ID.
    	TaskList content = new TaskList();
    	content = TaskList.newInstance(defaultContent,contentId,taskListToolContentHandler);
		return content;
	}

	/**
	 * {@inheritDoc}
	 */
	public List getAuthoredItems(Long taskListUid) {
		return taskListItemDao.getAuthoringItems(taskListUid);
	}

	/**
	 * {@inheritDoc}
	 */
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
		file.setCreated(new Date());
		
		return file;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TaskListItemAttachment uploadTaskListItemFile(FormFile uploadFile, String fileType, TaskListUser user) throws UploadTaskListFileException {
		if(uploadFile == null || StringUtils.isEmpty(uploadFile.getFileName()))
			throw new UploadTaskListFileException(messageService.getMessage("error.msg.upload.file.not.found",new Object[]{uploadFile}));
		
		//upload file to repository
		NodeKey nodeKey = processFile(uploadFile,fileType);
		
		//create new attachement
		TaskListItemAttachment file = new TaskListItemAttachment();
		file.setFileType(fileType);
		file.setFileUuid(nodeKey.getUuid());
		file.setFileVersionId(nodeKey.getVersion());
		file.setFileName(uploadFile.getFileName());
		file.setCreated(new Timestamp(new Date().getTime()));
		file.setCreateBy(user);

		return file;
	}

	/**
	 * {@inheritDoc}
	 */
	public void createUser(TaskListUser taskListUser) {
		taskListUserDao.saveObject(taskListUser);
	}

	/**
	 * {@inheritDoc}
	 */
	public TaskListUser getUserByIDAndContent(Long userId, Long contentId) {
		return (TaskListUser) taskListUserDao.getUserByUserIDAndContentID(userId,contentId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TaskListUser getUserByIDAndSession(Long userId, Long sessionId)  {
		return (TaskListUser) taskListUserDao.getUserByUserIDAndSessionID(userId,sessionId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TaskListUser getUser(Long uid){
		return (TaskListUser) taskListUserDao.getObject(TaskListUser.class, uid);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteFromRepository(Long fileUuid, Long fileVersionId) throws TaskListException {
		ITicket ticket = getRepositoryLoginTicket();
		try {
			repositoryService.deleteVersion(ticket, fileUuid,fileVersionId);
		} catch (Exception e) {
			throw new TaskListException(
					"Exception occured while deleting files from"
							+ " the repository " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveOrUpdateTaskList(TaskList taskList) {
		taskListDao.saveObject(taskList);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteTaskListAttachment(Long attachmentUid) {
		taskListAttachmentDao.removeObject(TaskListAttachment.class, attachmentUid);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deleteTaskListCondition(Long conditionUid) {
		taskListConditionDAO.removeObject(TaskListCondition.class, conditionUid);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deleteTaskListItemAttachment(Long attachmentUid) {
		taskListItemAttachmentDao.removeObject(TaskListItemAttachment.class, attachmentUid);
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveOrUpdateTaskListItem(TaskListItem item) {
		taskListItemDao.saveObject(item);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteTaskListItem(Long uid) {
		taskListItemDao.removeObject(TaskListItem.class,uid);
	}

	/**
	 * {@inheritDoc}
	 */
	public TaskList getTaskListBySessionId(Long sessionId){
		TaskListSession session = taskListSessionDao.getSessionBySessionId(sessionId);
		//to skip CGLib problem
		Long contentId = session.getTaskList().getContentId();
		TaskList res = taskListDao.getByContentId(contentId);
		return res;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TaskListItem getTaskListItemByUid(Long itemUid) {
		return taskListItemDao.getByUid(itemUid);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TaskListSession getTaskListSessionBySessionId(Long sessionId) {
		return taskListSessionDao.getSessionBySessionId(sessionId);
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveOrUpdateTaskListSession(TaskListSession resSession) {
		taskListSessionDao.saveObject(resSession);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String finishToolSession(Long toolSessionId, Long userId) throws TaskListException {
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
			throw new TaskListException(e);
		} catch (ToolException e) {
			throw new TaskListException(e);
		}
		return nextUrl;
	}
	
	public int checkMinimumNumberTasksComplete(Long toolSessionId, Long userUid) {
		int completedItems = taskListItemVisitDao.getTasksCompletedCountByUser(toolSessionId, userUid);
		TaskListSession session = taskListSessionDao.getSessionBySessionId(toolSessionId);
		if(session == null){
			log.error("Failed get session by ID [" + toolSessionId + "]");
			return 0;
		}
		int minimumNumberTasksComplete = session.getTaskList().getMinimumNumberTasksComplete();
		
		return (minimumNumberTasksComplete - completedItems);
	}


	/**
	 * {@inheritDoc}
	 */
	public void retrieveComplete(Set<TaskListItem> taskListItemList, TaskListUser user) {
		for(TaskListItem item:taskListItemList){
			TaskListItemVisitLog log = taskListItemVisitDao.getTaskListItemLog(item.getUid(),user.getUserId());
			if(log == null)
				item.setComplete(false);
			else
				item.setComplete(log.isComplete());
		}
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */	
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

	/** 
	 * {@inheritDoc}
	 */
	public List<Summary> getTaskListSummary(Long contentId) {
		
		List<Summary> summaryList = new ArrayList<Summary>();
		
		//retrieve all the sessions associated with this taskList
		List<TaskListSession> sessionList = taskListSessionDao.getByContentId(contentId);
		
		TaskList taskList = taskListDao.getByContentId(contentId);
		ArrayList<TaskListItem> itemList = new ArrayList<TaskListItem>();
		itemList.addAll(taskList.getTaskListItems());
		//create the list containing all taskListItems  
		for(TaskListSession session:sessionList) {

			Set<TaskListItem> newItems = session.getTaskListItems();
			for(TaskListItem item : newItems) {
				if (!itemList.contains(item)) itemList.add(item);
			}
		}
		
		
		//create the user list of all whom were started this task
		for(TaskListSession session:sessionList) {
			
			List<TaskListUser> userList = taskListUserDao.getBySessionID(session.getSessionId());
			
			//Fill up the copmletion table
			boolean[][] complete = new boolean[userList.size()][itemList.size()];
			//Fill up the array of visitNumbers
			int[] visitNumbers = new int[itemList.size()];
			for (int i = 0; i < userList.size(); i++) {
				TaskListUser user = userList.get(i);
				
				for (int j = 0; j < itemList.size(); j++) {
					TaskListItem item = itemList.get(j);
					
					//retreiving TaskListItemVisitLog for current taskList and user
					TaskListItemVisitLog visitLog = taskListItemVisitDao.getTaskListItemLog(item.getUid(), user.getUserId());
					if (visitLog !=null) {
						complete[i][j] = visitLog.isComplete();	
						if (visitLog.isComplete()) visitNumbers[j]++;
					} else {
						complete[i][j] = false;
					}
				}
			}
			
			Summary summary = new Summary(session.getSessionId(), session.getSessionName(), itemList, userList, complete, visitNumbers, taskList.isMonitorVerificationRequired());
			summaryList.add(summary);
		}

		return summaryList;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public int getNumTasksCompletedByUser(Long toolSessionId, Long userUid) {
		return getTaskListItemVisitDao().getTasksCompletedCountByUser(toolSessionId, userUid);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public boolean checkCondition(String conditionName, Long toolSessionId, Long userUid) {
		TaskListUser user = taskListUserDao.getUserByUserIDAndSessionID(userUid, toolSessionId);
		TaskList taskList = taskListSessionDao.getSessionBySessionId(toolSessionId).getTaskList();
		Set<TaskListCondition> conditions = taskList.getConditions();
		TaskListCondition condition = null;
		for (TaskListCondition cond:conditions) {
			if (cond.getName().equals(conditionName)) {
				condition = cond;
				break;
			}
		}

		boolean result = true;
		if (condition != null) {
			Iterator it = condition.getTaskListItems().iterator();
			while(it.hasNext()) {
				TaskListItem item = (TaskListItem) it.next();
				
				TaskListItemVisitLog visitLog = taskListItemVisitDao.getTaskListItemLog(item.getUid(), userUid);
				if (visitLog != null) {
					//result is being calculated depending on visitLog value
					result &= visitLog.isComplete();
				} else {
					//user hadn't complete this task. So this means the condition isn't met.
					result = false;
					break;
				}
			}
		} else {
			//there is no such a condition
			result = false;
		}
		
		return result;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public List<List<ItemSummary>> getTaskListItemSummary(Long contentId, Long taskListItemUid) {
		
		TaskListItem taskListItem = taskListItemDao.getByUid(taskListItemUid);
		
		List<List<ItemSummary>> resultSummaryList = new ArrayList<List<ItemSummary>>();
		
		//create the user list of all whom were started this task
		List<TaskListSession> sessionList = taskListSessionDao.getByContentId(contentId);
		for(TaskListSession session:sessionList) {
			List<ItemSummary> groupSummary = new ArrayList<ItemSummary>();
			
			List<TaskListUser> grouppedUsers = taskListUserDao.getBySessionID(session.getSessionId());
			for(TaskListUser user : grouppedUsers) {
				
				ItemSummary userItemSummary = new ItemSummary();
				userItemSummary.setUser(user);
				userItemSummary.setSessionId(session.getSessionId());
				userItemSummary.setSessionName(session.getSessionName());
				
				TaskListItemVisitLog visitLog = taskListItemVisitDao.getTaskListItemLog(taskListItem.getUid(), user.getUserId());
				//If TaskListItemVisitLog exists then fill up taskSummaryItem otherwise put false in a completed field
				if (visitLog !=null) {
					userItemSummary.setCompleted(visitLog.isComplete());
					if (visitLog.isComplete()) userItemSummary.setDate(visitLog.getAccessDate());
					
					//fill up with comments and attachments made by this user
					Set<TaskListItemComment> itemComments = taskListItem.getComments();
					for(TaskListItemComment comment : itemComments) {
						if (user.getUserId().equals(comment.getCreateBy().getUserId())) userItemSummary.getComments().add(comment);
					}

					Set<TaskListItemAttachment> itemAttachments = taskListItem.getAttachments();
					for(TaskListItemAttachment attachment : itemAttachments) {
						if (user.getUserId().equals(attachment.getCreateBy().getUserId())) userItemSummary.getAttachments().add(attachment);
					}

				} else {
					userItemSummary.setCompleted(false);
				}
				groupSummary.add(userItemSummary);
			}
			resultSummaryList.add(groupSummary);
		}
		
		
		return resultSummaryList;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<ItemSummary> exportForTeacher(Long contentId) {
//		TaskList taskList = taskListDao.getByContentId(contentId);
//		ArrayList<TaskListItem> itemList = new ArrayList<TaskListItem>();
//		itemList.addAll(taskList.getTaskListItems());
//		
//		//retrieve all the sessions associated with this taskList
//		List<TaskListSession> sessionList = taskListSessionDao.getByContentId(contentId);
//		//create the list containing all taskListItems  
//		for(TaskListSession session:sessionList) {
//			Set<TaskListItem> newItems = session.getTaskListItems();
//			for(TaskListItem item : newItems) {
//				if (!itemList.contains(item)) itemList.add(item);
//			}
//		}
//		
//		List<TaskSummary> taskSummaries = new ArrayList<TaskSummary>();
//		for(TaskListItem item:itemList) {
//			taskSummaries.add(getTaskSummary(contentId, item.getUid()));
//		}
//		
//		return taskSummaries;
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<ItemSummary> exportForLearner(Long sessionId, TaskListUser learner) {
//		Long contentId = getTaskListBySessionId(sessionId).getContentId();
//		
//		TaskList taskList = taskListDao.getByContentId(contentId);
//		ArrayList<TaskListItem> itemList = new ArrayList<TaskListItem>();
//		itemList.addAll(taskList.getTaskListItems());
//		
//		//retrieve all the sessions associated with this taskList
//		List<TaskListSession> sessionList = taskListSessionDao.getByContentId(contentId);
//		//create the list containing all taskListItems  
//		for(TaskListSession session:sessionList) {
//			Set<TaskListItem> newItems = session.getTaskListItems();
//			for(TaskListItem item : newItems) {
//				if (!itemList.contains(item)) itemList.add(item);
//			}
//		}
//		
//		List<TaskSummary> taskSummaries = new ArrayList<TaskSummary>();
//		for(TaskListItem item:itemList) {
//			taskSummaries.add(getTaskSummary(contentId, item.getUid()));
//		}
//		
//		//get rid of information that is not allowed to be shown to the current user
//		for(TaskSummary taskSummary:taskSummaries) {
//			
//			//get rid of taskSummaryItems belong to another users
//			List<TaskListItemSummary> newTaskSummaryItems = new  ArrayList<TaskListItemSummary>();
//			for(TaskListItemSummary taskSummaryItem:taskSummary.getTaskSummaryItems()) {
//				if (learner.equals(taskSummaryItem.getUser())) newTaskSummaryItems.add(taskSummaryItem);
//			}
//			taskSummary.setTaskSummaryItems(newTaskSummaryItems);
//
//			//get rid of TaskListItemComments and TaskListItemAttachments belong to another users
//			if (taskSummary.getTaskListItem().isCommentsFilesAllowed() && !taskSummary.getTaskListItem().getShowCommentsToAll()) {
//				TaskListItemSummary taskSummaryItem = taskSummary.getTaskSummaryItems().get(0);
//				
//				List<TaskListItemComment> newComments = new  ArrayList<TaskListItemComment>();
//				for(TaskListItemComment comment:taskSummaryItem.getComments()) {
//					if (learner.equals(comment.getCreateBy())) newComments.add(comment);
//				}
//				taskSummaryItem.setComments(newComments);
//
//				List<TaskListItemAttachment> newAttachments = new  ArrayList<TaskListItemAttachment>();
//				for(TaskListItemAttachment attachment:taskSummaryItem.getAttachments()) {
//					if (learner.equals(attachment.getCreateBy())) newAttachments.add(attachment);
//				}
//				taskSummaryItem.setAttachments(newAttachments);
//			}
//		}
//		
//		return taskSummaries;
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
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

	/** 
	 * {@inheritDoc}
	 */
	public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId, String entryText) {
		return coreNotebookService.createNotebookEntry(sessionId, notebookToolType, toolSignature, userId, "", entryText);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID){
		List<NotebookEntry> list = coreNotebookService.getEntry(sessionId, idType, signature, userID);
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public void updateEntry(NotebookEntry notebookEntry) {
		coreNotebookService.updateEntry(notebookEntry);
	}
	
	//*****************************************************************************
	// Set methods for Spring Bean
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
	public TaskListOutputFactory getTaskListOutputFactory() {
		return taskListOutputFactory;
	}
	public void setTaskListOutputFactory(TaskListOutputFactory taskListOutputFactory) {
		this.taskListOutputFactory = taskListOutputFactory;
	}
	public void setRepositoryService(IRepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	public void setTaskListAttachmentDao(TaskListAttachmentDAO taskListAttachmentDao) {
		this.taskListAttachmentDao = taskListAttachmentDao;
	}
	public void setTaskListConditionDao(TaskListConditionDAO taskListConditionDAO) {
		this.taskListConditionDAO = taskListConditionDAO;
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

	//*******************************************************************************
	//  Methods implementing ToolContentManager, ToolSessionManager 
	//*******************************************************************************
	
	public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
		TaskList toolContentObj = taskListDao.getByContentId(toolContentId);
 		if(toolContentObj == null) {
 			try {
				toolContentObj = getDefaultTaskList();
			} catch (TaskListException e) {
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
 		Set<TaskListItem> taskListItems = toolContentObj.getTaskListItems();
 	    for (TaskListItem taskListItem : taskListItems) {  
 	    	taskListItem.setComments(null);
 	    	taskListItem.setAttachments(null);
 	    }  
 	    
		try {
			exportContentService.registerFileClassForExport(TaskListAttachment.class.getName(),"fileUuid","fileVersionId");
			exportContentService.exportToolContent( toolContentId, toolContentObj,taskListToolContentHandler, rootPath);
		} catch (ExportToolContentException e) {
			throw new ToolException(e);
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath ,String fromVersion,String toVersion) throws ToolException {
	
		try {
			exportContentService.registerFileClassForImport(TaskListAttachment.class.getName(),"fileUuid","fileVersionId","fileName","fileType",null,null);
			
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
	
	/**
	 * Get the definitions for possible output for an activity, based on the
	 * toolContentId. These may be definitions that are always available for the
	 * tool (e.g. number of marks for Multiple Choice) or a custom definition
	 * created for a particular activity such as the answer to the third
	 * question contains the word Koala and hence the need for the toolContentId
	 * 
	 * @return SortedMap of ToolOutputDefinitions with the key being the name of
	 *         each definition
	 * @throws TaskListException 
	 */
	public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId) throws ToolException {
		TaskList taskList = getTaskListByContentId(toolContentId);
		if ( taskList == null ) {
			taskList = getDefaultTaskList();
		}
		return getTaskListOutputFactory().getToolOutputDefinitions(taskList);
	}
 
	/** 
	 * {@inheritDoc}
	 */
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
			} catch (TaskListException e) {
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

	/** 
	 * {@inheritDoc}
	 */
	public void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException {
		TaskList taskList = taskListDao.getByContentId(toolContentId);
		if(taskList == null){
			throw new ToolException("No found tool content by given content ID:" + toolContentId);
		}
		taskList.setDefineLater(value);
	}

	/** 
	 * {@inheritDoc}
	 */
	public void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException {
		TaskList taskList = taskListDao.getByContentId(toolContentId);
		if(taskList == null){
			throw new ToolException("No found tool content by given content ID:" + toolContentId);
		}
		taskList.setRunOffline(value);		
	}

	/** 
	 * {@inheritDoc}
	 */
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

	/** 
	 * {@inheritDoc}
	 */
	public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
		TaskListSession session = new TaskListSession();
		session.setSessionId(toolSessionId);
		session.setSessionName(toolSessionName);
		TaskList taskList = taskListDao.getByContentId(toolContentId);
		session.setTaskList(taskList);
		taskListSessionDao.saveObject(session);
	}

	/** 
	 * {@inheritDoc}
	 */
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

	/** 
	 * {@inheritDoc}
	 */
	public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException, ToolException {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
		taskListSessionDao.deleteBySessionId(toolSessionId);
	}
	
	/** 
	 * Get the tool output for the given tool output names.
	 * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long, java.lang.Long)
	 */
	public SortedMap<String, ToolOutput> getToolOutput(List<String> names,	Long toolSessionId, Long learnerId) {
		return taskListOutputFactory.getToolOutput(names, this, toolSessionId, learnerId);
	}

	/** 
	 * Get the tool output for the given tool output name.
	 * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long, java.lang.Long)
	 */
	public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
		return taskListOutputFactory.getToolOutput(name, this, toolSessionId, learnerId);
	}

	//*******************************************************************************
	//  Methods implementing ToolContentImport102Manager 
	//*******************************************************************************
	
    /**
	 * Import the data for a 1.0.2 Noticeboard or HTMLNoticeboard. Was left only
	 * for the reasons of implementing ToolContentImport102Manager interface.
	 */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {  }
    
    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException, DataMissingException {
    	
    	TaskList toolContentObj = getTaskListByContentId(toolContentId);
    	if ( toolContentObj == null ) {
    		throw new DataMissingException("Unable to set reflective data titled "+title
	       			+" on activity toolContentId "+toolContentId
	       			+" as the tool content does not exist.");
    	}

    	toolContentObj.setReflectOnActivity(Boolean.TRUE);
    	toolContentObj.setReflectInstructions(description);
    }

	//*****************************************************************************
	// Private methods
	//*****************************************************************************
    
	private TaskList getDefaultTaskList() throws TaskListException {
    	Long defaultTaskListId = getToolDefaultContentIdBySignature(TaskListConstants.TOOL_SIGNATURE);
    	TaskList defaultTaskList = getTaskListByContentId(defaultTaskListId);
    	if(defaultTaskList == null)	{
    	    String error=messageService.getMessage("error.msg.default.content.not.find");
    	    log.error(error);
    	    throw new TaskListException(error);
    	}
    	
    	return defaultTaskList;
	}
	
    private Long getToolDefaultContentIdBySignature(String toolSignature) throws TaskListException {
        Long contentId = null;
    	contentId=new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));    
    	if (contentId == null)
    	{
    		String error=messageService.getMessage("error.msg.default.content.not.find");
    	    log.error(error);
    	    throw new TaskListException(error);
    	}
	    return contentId;
    }
    
    /**
     * Process an uploaded file.
     * 
     * @throws TaskListException 
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
	private IVersionedNode getFile(Long uuid, Long versionId, String relativePath) throws TaskListException {
		
		ITicket tic = getRepositoryLoginTicket();
		
		try {
			return repositoryService.getFileItem(tic, uuid, versionId, relativePath);
		} catch (AccessDeniedException e) {
			
			String error = "Unable to access repository to get file uuid "+uuid
				+" version id "+versionId
				+" path " + relativePath+".";
			
			error = error+"AccessDeniedException: "+e.getMessage()+" Unable to retry further.";
			log.error(error);
			throw new TaskListException(error,e);
			
		} catch (Exception e) {
			
			String error = "Unable to access repository to get file uuid "+uuid
			+" version id "+versionId
			+" path " + relativePath+"."
			+" Exception: "+e.getMessage();
			log.error(error);
			throw new TaskListException(error,e);
			
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
	 * @throws TaskListException
	 */
	private ITicket getRepositoryLoginTicket() throws TaskListException {
		ICredentials credentials = new SimpleCredentials(taskListToolContentHandler.getRepositoryUser(), taskListToolContentHandler.getRepositoryId());
		try {
			ITicket ticket = repositoryService.login(credentials, taskListToolContentHandler.getRepositoryWorkspaceName());
			return ticket;
		} catch (AccessDeniedException ae) {
			throw new TaskListException("Access Denied to repository." + ae.getMessage());
		} catch (WorkspaceNotFoundException we) {
			throw new TaskListException("Workspace not found." + we.getMessage());
		} catch (LoginException e) {
			throw new TaskListException("Login failed." + e.getMessage());
		}
	}
}
