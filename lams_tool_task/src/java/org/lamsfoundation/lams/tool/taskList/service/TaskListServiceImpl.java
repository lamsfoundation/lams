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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
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
import org.lamsfoundation.lams.tool.taskList.dao.TaskListConditionDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListItemAttachmentDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListItemCommentDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListItemDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListItemVisitDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListSessionDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListUserDAO;
import org.lamsfoundation.lams.tool.taskList.dto.GroupSummary;
import org.lamsfoundation.lams.tool.taskList.dto.ItemSummary;
import org.lamsfoundation.lams.tool.taskList.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.taskList.dto.Summary;
import org.lamsfoundation.lams.tool.taskList.dto.TaskListItemVisitLogSummary;
import org.lamsfoundation.lams.tool.taskList.model.TaskList;
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

/**
 * Class implements <code>org.lamsfoundation.lams.tool.taskList.service.ITaskListService</code>.
 * 
 * @author Dapeng.Ni
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.taskList.service.ITaskListService
 */
public class TaskListServiceImpl implements ITaskListService, ToolContentManager, ToolSessionManager,
	ToolContentImport102Manager {
    static Logger log = Logger.getLogger(TaskListServiceImpl.class.getName());
    private TaskListDAO taskListDao;
    private TaskListItemDAO taskListItemDao;
    private TaskListConditionDAO taskListConditionDAO;
    private TaskListUserDAO taskListUserDao;
    private TaskListSessionDAO taskListSessionDao;
    private TaskListItemVisitDAO taskListItemVisitDao;
    private TaskListItemAttachmentDAO taskListItemAttachmentDao;
    private TaskListItemCommentDAO taskListItemCommentDAO;
    // tool service
    private TaskListToolContentHandler taskListToolContentHandler;
    private MessageService messageService;
    private TaskListOutputFactory taskListOutputFactory;
    // system services
    private ILamsToolService toolService;
    private ILearnerService learnerService;
    private IUserManagementService userManagementService;
    private IExportToolContentService exportContentService;
    private ICoreNotebookService coreNotebookService;

    // *******************************************************************************
    // Methods implements ITaskListService.
    // *******************************************************************************

    /**
     * {@inheritDoc}
     */
    public TaskList getTaskListByContentId(Long contentId) {
	TaskList rs = taskListDao.getByContentId(contentId);
	if (rs == null) {
	    TaskListServiceImpl.log.debug("Could not find the content by given ID:" + contentId);
	}
	return rs;
    }

    /**
     * {@inheritDoc}
     */
    public TaskList getDefaultContent(Long contentId) throws TaskListException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    TaskListServiceImpl.log.error(error);
	    throw new TaskListException(error);
	}

	TaskList defaultContent = getDefaultTaskList();
	// save default content by given ID.
	TaskList content = new TaskList();
	content = TaskList.newInstance(defaultContent, contentId);
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
    public TaskListItemAttachment uploadTaskListItemFile(FormFile uploadFile, TaskListUser user)
	    throws UploadTaskListFileException {
	if (uploadFile == null || StringUtils.isEmpty(uploadFile.getFileName())) {
	    throw new UploadTaskListFileException(messageService.getMessage("error.msg.upload.file.not.found",
		    new Object[] { uploadFile }));
	}

	// upload file to repository
	NodeKey nodeKey = processFile(uploadFile);

	// create new attachement
	TaskListItemAttachment file = new TaskListItemAttachment();
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
	return taskListUserDao.getUserByUserIDAndContentID(userId, contentId);
    }

    /**
     * {@inheritDoc}
     */
    public TaskListUser getUserByIDAndSession(Long userId, Long sessionId) {
	return taskListUserDao.getUserByUserIDAndSessionID(userId, sessionId);
    }

    /**
     * {@inheritDoc}
     */
    public TaskListUser getUser(Long uid) {
	return (TaskListUser) taskListUserDao.getObject(TaskListUser.class, uid);
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
	taskListItemDao.removeObject(TaskListItem.class, uid);
    }

    /**
     * {@inheritDoc}
     */
    public TaskList getTaskListBySessionId(Long sessionId) {
	TaskListSession session = taskListSessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getTaskList().getContentId();
	TaskList taskList = taskListDao.getByContentId(contentId);
	return taskList;
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

	// TaskListSession session = taskListSessionDao.getSessionBySessionId(toolSessionId);
	// session.setStatus(TaskListConstants.COMPLETED);
	// taskListSessionDao.saveObject(session);

	String nextUrl = null;
	try {
	    nextUrl = this.leaveToolSession(toolSessionId, userId);
	} catch (DataMissingException e) {
	    throw new TaskListException(e);
	} catch (ToolException e) {
	    throw new TaskListException(e);
	}
	return nextUrl;
    }

    /**
     * {@inheritDoc}
     */
    public void setItemComplete(Long taskListItemUid, Long userId, Long sessionId) {
	TaskListItemVisitLog log = taskListItemVisitDao.getTaskListItemLog(taskListItemUid, userId);
	if (log == null) {
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
    public void setItemAccess(Long taskListItemUid, Long userId, Long sessionId) {
	TaskListItemVisitLog log = taskListItemVisitDao.getTaskListItemLog(taskListItemUid, userId);
	if (log == null) {
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
    public void retrieveComplete(Set<TaskListItem> taskListItemList, TaskListUser user) {
	for (TaskListItem item : taskListItemList) {
	    TaskListItemVisitLog log = taskListItemVisitDao.getTaskListItemLog(item.getUid(), user.getUserId());
	    if (log == null) {
		item.setComplete(false);
	    } else {
		item.setComplete(log.isComplete());
	    }
	}
    }

    /**
     * {@inheritDoc}
     */
    public List<Summary> getSummary(Long contentId) {

	TaskList taskList = taskListDao.getByContentId(contentId);
	List<TaskListSession> sessionList = taskListSessionDao.getByContentId(contentId);

	List<Summary> summaryList = new ArrayList<Summary>();

	// create the user list of all whom were started this task
	for (TaskListSession session : sessionList) {

	    List<TaskListItem> itemList = getItemListForGroup(contentId, session.getSessionId());

	    List<TaskListUser> userList = taskListUserDao.getBySessionID(session.getSessionId());

	    // Fill up the copmletion table
	    boolean[][] complete = new boolean[userList.size()][itemList.size()];
	    // Fill up the array of visitNumbers
	    int[] visitNumbers = new int[itemList.size()];
	    for (int i = 0; i < userList.size(); i++) {
		TaskListUser user = userList.get(i);

		for (int j = 0; j < itemList.size(); j++) {
		    TaskListItem item = itemList.get(j);

		    // retreiving TaskListItemVisitLog for current taskList and user
		    TaskListItemVisitLog visitLog = taskListItemVisitDao.getTaskListItemLog(item.getUid(), user
			    .getUserId());
		    if (visitLog != null) {
			complete[i][j] = visitLog.isComplete();
			if (visitLog.isComplete()) {
			    visitNumbers[j]++;
			}
		    } else {
			complete[i][j] = false;
		    }
		}
	    }

	    Summary summary = new Summary(session.getSessionName(), itemList, userList, complete, visitNumbers,
		    taskList.isMonitorVerificationRequired());
	    summaryList.add(summary);
	}

	return summaryList;
    }

    /**
     * {@inheritDoc}
     */
    public ItemSummary getItemSummary(Long contentId, Long taskListItemUid, boolean isExportProcessing) {

	TaskListItem taskListItem = taskListItemDao.getByUid(taskListItemUid);

	ItemSummary itemSummary = new ItemSummary();
	itemSummary.setTaskListItem(taskListItem);
	List<GroupSummary> groupSummaries = itemSummary.getGroupSummaries();

	// create sessionList depending on if taskListItem created be author or created during learning
	List<TaskListSession> sessionList = new ArrayList<TaskListSession>();
	if (taskListItem.isCreateByAuthor()) {
	    sessionList = taskListSessionDao.getByContentId(contentId);
	} else {
	    TaskListSession userSession = taskListItem.getCreateBy().getSession();
	    sessionList.add(userSession);
	}

	// create the user list of all whom were started this task
	for (TaskListSession session : sessionList) {

	    GroupSummary groupSummary = new GroupSummary();
	    groupSummary.setSessionName(session.getSessionName());

	    List<TaskListUser> usersBelongToGroup = taskListUserDao.getBySessionID(session.getSessionId());
	    for (TaskListUser user : usersBelongToGroup) {

		TaskListItemVisitLogSummary taskListItemVisitLogSummary = new TaskListItemVisitLogSummary();
		taskListItemVisitLogSummary.setUser(user);

		TaskListItemVisitLog visitLog = taskListItemVisitDao.getTaskListItemLog(taskListItem.getUid(), user
			.getUserId());
		// If TaskListItemVisitLog exists then fill up taskSummaryItem otherwise put false in a completed field
		if (visitLog != null) {
		    taskListItemVisitLogSummary.setCompleted(visitLog.isComplete());
		    if (visitLog.isComplete()) {
			taskListItemVisitLogSummary.setDate(visitLog.getAccessDate());
		    }

		    // fill up with comments and attachments made by this user
		    Set<TaskListItemComment> itemComments = taskListItem.getComments();
		    for (TaskListItemComment comment : itemComments) {
			if (user.getUserId().equals(comment.getCreateBy().getUserId())) {
			    taskListItemVisitLogSummary.getComments().add(comment);
			}
		    }

		    Set<TaskListItemAttachment> itemAttachments = taskListItem.getAttachments();
		    for (TaskListItemAttachment attachment : itemAttachments) {
			if (user.getUserId().equals(attachment.getCreateBy().getUserId())) {
			    taskListItemVisitLogSummary.getAttachments().add(attachment);
			}
		    }
		} else {
		    taskListItemVisitLogSummary.setCompleted(false);
		}

		// if we're doing export then fill up all itemSummaries with reflection information
		if (isExportProcessing) {

		    NotebookEntry notebookEntry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			    TaskListConstants.TOOL_SIGNATURE, user.getUserId().intValue());

		    ReflectDTO reflectDTO = new ReflectDTO(user);
		    if (notebookEntry == null) {
			reflectDTO.setFinishReflection(false);
			reflectDTO.setReflect(null);
		    } else {
			reflectDTO.setFinishReflection(true);
			reflectDTO.setReflect(notebookEntry.getEntry());
		    }
		    reflectDTO.setReflectInstructions(session.getTaskList().getReflectInstructions());

		    taskListItemVisitLogSummary.setReflectDTO(reflectDTO);
		}

		groupSummary.getTaskListItemVisitLogSummaries().add(taskListItemVisitLogSummary);
	    }
	    groupSummaries.add(groupSummary);
	}

	return itemSummary;
    }

    /**
     * {@inheritDoc}
     */
    public List<ItemSummary> exportForTeacher(Long contentId) {
	TaskList taskList = taskListDao.getByContentId(contentId);
	ArrayList<TaskListItem> itemList = new ArrayList<TaskListItem>();
	itemList.addAll(taskList.getTaskListItems());

	// retrieve all the sessions associated with this taskList
	List<TaskListSession> sessionList = taskListSessionDao.getByContentId(contentId);
	// create the list containing all taskListItems
	for (TaskListSession session : sessionList) {
	    Set<TaskListItem> newItems = session.getTaskListItems();
	    for (TaskListItem item : newItems) {
		if (!itemList.contains(item)) {
		    itemList.add(item);
		}
	    }
	}

	List<ItemSummary> itemSummaries = new ArrayList<ItemSummary>();
	for (TaskListItem item : itemList) {
	    itemSummaries.add(getItemSummary(contentId, item.getUid(), true));
	}

	return itemSummaries;
    }

    /**
     * {@inheritDoc}
     */
    public List<ItemSummary> exportForLearner(Long sessionId, TaskListUser learner) {
	Long contentId = getTaskListBySessionId(sessionId).getContentId();

	TaskList taskList = taskListDao.getByContentId(contentId);
	List<TaskListItem> itemList = getItemListForGroup(contentId, sessionId);

	List<ItemSummary> itemSummaries = new ArrayList<ItemSummary>();
	for (TaskListItem item : itemList) {
	    itemSummaries.add(getItemSummary(contentId, item.getUid(), true));
	}

	// get rid of information that doesn't belong to the current user
	for (ItemSummary itemSummary : itemSummaries) {

	    // get rid of groups that user doesn't belong to
	    GroupSummary newGroupSummary = new GroupSummary();
	    for (GroupSummary groupSummary : itemSummary.getGroupSummaries()) {

		for (TaskListItemVisitLogSummary taskListItemVisitLogSummary : groupSummary
			.getTaskListItemVisitLogSummaries()) {
		    if (learner.equals(taskListItemVisitLogSummary.getUser())) {
			newGroupSummary.setSessionName(groupSummary.getSessionName());
			newGroupSummary.getTaskListItemVisitLogSummaries().add(taskListItemVisitLogSummary);
		    }
		}
	    }
	    itemSummary.getGroupSummaries().clear();
	    itemSummary.getGroupSummaries().add(newGroupSummary);

	}

	return itemSummaries;
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
	for (TaskListCondition cond : conditions) {
	    if (cond.getName().equals(conditionName)) {
		condition = cond;
		break;
	    }
	}

	boolean result = true;
	if (condition != null) {
	    Iterator it = condition.getTaskListItems().iterator();
	    while (it.hasNext()) {
		TaskListItem item = (TaskListItem) it.next();

		TaskListItemVisitLog visitLog = taskListItemVisitDao.getTaskListItemLog(item.getUid(), userUid);
		if (visitLog != null) {
		    // result is being calculated depending on visitLog value
		    result &= visitLog.isComplete();
		} else {
		    // user hadn't complete this task. So this means the condition isn't met.
		    result = false;
		    break;
		}
	    }
	} else {
	    // there is no such a condition
	    result = false;
	}

	return result;
    }

    /**
     * {@inheritDoc}
     */
    public List<TaskListUser> getUserListBySessionItem(Long sessionId, Long itemUid) {
	List<TaskListItemVisitLog> logList = taskListItemVisitDao.getTaskListItemLogBySession(sessionId, itemUid);
	List<TaskListUser> userList = new ArrayList(logList.size());
	for (TaskListItemVisitLog visit : logList) {
	    TaskListUser user = visit.getUser();
	    user.setAccessDate(visit.getAccessDate());
	    userList.add(user);
	}
	return userList;
    }

    /**
     * {@inheritDoc}
     */
    public List<TaskListUser> getUserListBySessionId(Long sessionId) {
	return taskListUserDao.getBySessionID(sessionId);
    }

    /**
     * {@inheritDoc}
     */
    public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
	    String entryText) {
	return coreNotebookService.createNotebookEntry(sessionId, notebookToolType, toolSignature, userId, "",
		entryText);
    }

    /**
     * {@inheritDoc}
     */
    public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID) {
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
    
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    // *****************************************************************************
    // Set methods for Spring Bean
    // *****************************************************************************

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

    public void setTaskListAttachmentDao(TaskListItemAttachmentDAO taskListItemAttachmentDao) {
	this.taskListItemAttachmentDao = taskListItemAttachmentDao;
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

    public MessageService getMessageService() {
	return messageService;
    }

    // *******************************************************************************
    // Methods implementing ToolContentManager, ToolSessionManager
    // *******************************************************************************

    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	TaskList toolContentObj = taskListDao.getByContentId(toolContentId);
	if (toolContentObj == null) {
	    try {
		toolContentObj = getDefaultTaskList();
	    } catch (TaskListException e) {
		throw new DataMissingException(e.getMessage());
	    }
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the taskList tool");
	}

	// set TaskListToolContentHandler as null to avoid copy file node in repository again.
	toolContentObj = TaskList.newInstance(toolContentObj, toolContentId);
	Set<TaskListItem> taskListItems = toolContentObj.getTaskListItems();
	for (TaskListItem taskListItem : taskListItems) {
	    taskListItem.setComments(null);
	    taskListItem.setAttachments(null);
	}

	try {
	    exportContentService.exportToolContent(toolContentId, toolContentObj, taskListToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    /**
     * {@inheritDoc}
     */
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(TaskListImportContentVersionFilter.class);
	
	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, taskListToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof TaskList)) {
		throw new ImportToolContentException(
			"Import Share taskList tool content failed. Deserialized object is " + toolPOJO);
	    }
	    TaskList toolContentObj = (TaskList) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentId(toolContentId);
	    TaskListUser user = taskListUserDao.getUserByUserIDAndContentID(new Long(newUserUid.longValue()),
		    toolContentId);
	    if (user == null) {
		user = new TaskListUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(new Long(newUserUid.longValue()));
		user.setTaskList(toolContentObj);
	    }
	    toolContentObj.setCreatedBy(user);

	    // reset all taskListItem createBy user
	    Set<TaskListItem> items = toolContentObj.getTaskListItems();
	    for (TaskListItem item : items) {
		item.setCreateBy(user);
	    }
	    taskListDao.saveObject(toolContentObj);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    /**
     * Get the definitions for possible output for an activity, based on the toolContentId. These may be definitions
     * that are always available for the tool (e.g. number of marks for Multiple Choice) or a custom definition created
     * for a particular activity such as the answer to the third question contains the word Koala and hence the need for
     * the toolContentId
     * 
     * @return SortedMap of ToolOutputDefinitions with the key being the name of each definition
     * @throws TaskListException
     */
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	TaskList taskList = getTaskListByContentId(toolContentId);
	if (taskList == null) {
	    taskList = getDefaultTaskList();
	}
	return getTaskListOutputFactory().getToolOutputDefinitions(taskList, definitionType);
    }
    
    public String getToolContentTitle(Long toolContentId) {
	return getTaskListByContentId(toolContentId).getTitle();
    }
   
    /**
     * {@inheritDoc}
     */
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	if (toContentId == null) {
	    throw new ToolException("Failed to create the SharedTaskListFiles tool seession");
	}

	TaskList taskList = null;
	if (fromContentId != null) {
	    taskList = taskListDao.getByContentId(fromContentId);
	}
	if (taskList == null) {
	    try {
		taskList = getDefaultTaskList();
	    } catch (TaskListException e) {
		throw new ToolException(e);
	    }
	}

	TaskList toContent = TaskList.newInstance(taskList, toContentId);
	taskListDao.saveObject(toContent);

	// save taskList items as well
	Set items = toContent.getTaskListItems();
	if (items != null) {
	    Iterator iter = items.iterator();
	    while (iter.hasNext()) {
		TaskListItem item = (TaskListItem) iter.next();
		// createRootTopic(toContent.getUid(),null,msg);
	    }
	}
    }

    /**
     * {@inheritDoc}
     */
    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException {
	TaskList taskList = taskListDao.getByContentId(toolContentId);
	if (removeSessionData) {
	    List list = taskListSessionDao.getByContentId(toolContentId);
	    Iterator iter = list.iterator();
	    while (iter.hasNext()) {
		TaskListSession session = (TaskListSession) iter.next();
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
	if (toolSessionId == null) {
	    TaskListServiceImpl.log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    TaskListServiceImpl.log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}

	TaskListSession session = taskListSessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(TaskListConstants.COMPLETED);
	    taskListSessionDao.saveObject(session);
	} else {
	    TaskListServiceImpl.log.error("Fail to leave tool Session.Could not find shared taskList "
		    + "session by given session id: " + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find shared taskList session by given session id: " + toolSessionId);
	}
	return learnerService.completeToolSession(toolSessionId, learnerId);
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
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException,
	    ToolException {
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
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long,
     *      java.lang.Long)
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return taskListOutputFactory.getToolOutput(names, this, toolSessionId, learnerId);
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long,
     *      java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return taskListOutputFactory.getToolOutput(name, this, toolSessionId, learnerId);
    }

    // *******************************************************************************
    // Methods implementing ToolContentImport102Manager
    // *******************************************************************************

    /**
     * Import the data for a 1.0.2 Noticeboard or HTMLNoticeboard. Was left only for the reasons of implementing
     * ToolContentImport102Manager interface.
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
    }

    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	TaskList toolContentObj = getTaskListByContentId(toolContentId);
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	toolContentObj.setReflectOnActivity(Boolean.TRUE);
	toolContentObj.setReflectInstructions(description);
    }

    // *****************************************************************************
    // Private methods
    // *****************************************************************************

    private TaskList getDefaultTaskList() throws TaskListException {
	Long defaultTaskListId = getToolDefaultContentIdBySignature(TaskListConstants.TOOL_SIGNATURE);
	TaskList defaultTaskList = getTaskListByContentId(defaultTaskListId);
	if (defaultTaskList == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    TaskListServiceImpl.log.error(error);
	    throw new TaskListException(error);
	}

	return defaultTaskList;
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws TaskListException {
	Long contentId = null;
	contentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    TaskListServiceImpl.log.error(error);
	    throw new TaskListException(error);
	}
	return contentId;
    }

    /**
     * Returns list of tasks from authoring + the tasks added by members of that group.
     * 
     * @param contentId
     * @param sessionId
     *                sessionId which defines Group
     * @return
     */
    private List<TaskListItem> getItemListForGroup(Long contentId, Long sessionId) {

	// create the list containing all taskListItems
	TaskList taskList = taskListDao.getByContentId(contentId);
	ArrayList<TaskListItem> itemList = new ArrayList<TaskListItem>();
	itemList.addAll(taskList.getTaskListItems());

	List<TaskListSession> sessionList = taskListSessionDao.getByContentId(contentId);
	for (TaskListSession session : sessionList) {
	    Set<TaskListItem> newItems = session.getTaskListItems();
	    for (TaskListItem item : newItems) {
		if (!itemList.contains(item)) {
		    itemList.add(item);
		}
	    }
	}

	List<TaskListUser> userList = taskListUserDao.getBySessionID(sessionId);

	ArrayList<TaskListItem> groupItemList = new ArrayList<TaskListItem>();
	for (TaskListItem item : itemList) {

	    if (item.isCreateByAuthor()) {
		groupItemList.add(item);
	    } else {
		for (TaskListUser user : userList) {
		    if (user.getUserId().equals(item.getCreateBy().getUserId())) {
			groupItemList.add(item);
			break;
		    }
		}
	    }
	}

	return groupItemList;
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
    private NodeKey processFile(FormFile file) throws UploadTaskListFileException {
	NodeKey node = null;
	if (file != null && !StringUtils.isEmpty(file.getFileName())) {
	    String fileName = file.getFileName();
	    try {
		node = taskListToolContentHandler.uploadFile(file.getInputStream(), fileName, file.getContentType());
	    } catch (InvalidParameterException e) {
		throw new UploadTaskListFileException(messageService.getMessage("error.msg.invaid.param.upload"));
	    } catch (FileNotFoundException e) {
		throw new UploadTaskListFileException(messageService.getMessage("error.msg.file.not.found"));
	    } catch (RepositoryCheckedException e) {
		throw new UploadTaskListFileException(messageService.getMessage("error.msg.repository"));
	    } catch (IOException e) {
		throw new UploadTaskListFileException(messageService.getMessage("error.msg.io.exception"));
	    }
	}
	return node;
    }

    // *******************************************************************************
    // Service method
    // *******************************************************************************

    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getTaskListOutputFactory().getSupportedDefinitionClasses(definitionType);
    }
}
