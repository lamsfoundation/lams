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

package org.lamsfoundation.lams.tool.taskList.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.taskList.TaskListConstants;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListItemDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListItemVisitDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListSessionDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListUserDAO;
import org.lamsfoundation.lams.tool.taskList.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.taskList.dto.SessionDTO;
import org.lamsfoundation.lams.tool.taskList.dto.TaskListUserDTO;
import org.lamsfoundation.lams.tool.taskList.model.TaskList;
import org.lamsfoundation.lams.tool.taskList.model.TaskListCondition;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemAttachment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemComment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemVisitLog;
import org.lamsfoundation.lams.tool.taskList.model.TaskListSession;
import org.lamsfoundation.lams.tool.taskList.model.TaskListUser;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;

/**
 * Class implements <code>org.lamsfoundation.lams.tool.taskList.service.ITaskListService</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.taskList.service.ITaskListService
 */
public class TaskListServiceImpl implements ITaskListService, ToolContentManager, ToolSessionManager {
    private static Logger log = Logger.getLogger(TaskListServiceImpl.class.getName());
    private TaskListDAO taskListDao;
    private TaskListItemDAO taskListItemDao;
    private TaskListUserDAO taskListUserDao;
    private TaskListSessionDAO taskListSessionDao;
    private TaskListItemVisitDAO taskListItemVisitDao;
    // tool service
    private IToolContentHandler taskListToolContentHandler;
    private MessageService messageService;
    private TaskListOutputFactory taskListOutputFactory;
    // system services
    private ILamsToolService toolService;
    private IUserManagementService userManagementService;
    private IExportToolContentService exportContentService;
    private ICoreNotebookService coreNotebookService;

    // *******************************************************************************
    // Methods implements ITaskListService.
    // *******************************************************************************

    @Override
    public TaskList getTaskListByContentId(Long contentId) {
	TaskList rs = taskListDao.getByContentId(contentId);
	return rs;
    }

    @Override
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

    @Override
    public List getAuthoredItems(Long taskListUid) {
	return taskListItemDao.getAuthoringItems(taskListUid);
    }

    @Override
    public TaskListItemAttachment uploadTaskListItemFile(File uploadFile, TaskListUser user)
	    throws UploadTaskListFileException {
	if ((uploadFile == null) || StringUtils.isEmpty(uploadFile.getName())) {
	    throw new UploadTaskListFileException(
		    messageService.getMessage("error.msg.upload.file.not.found", new Object[] { uploadFile }));
	}

	// upload file to repository
	NodeKey nodeKey = processFile(uploadFile);

	// create new attachement
	TaskListItemAttachment file = new TaskListItemAttachment();
	file.setFileUuid(nodeKey.getNodeId());
	file.setFileVersionId(nodeKey.getVersion());
	file.setFileDisplayUuid(nodeKey.getUuid());
	file.setFileName(uploadFile.getName());
	file.setCreated(new Timestamp(new Date().getTime()));
	file.setCreateBy(user);

	return file;
    }

    @Override
    public void createUser(TaskListUser taskListUser) {
	taskListUserDao.saveObject(taskListUser);
    }

    @Override
    public TaskListUser getUserByIDAndContent(Long userId, Long contentId) {
	return taskListUserDao.getUserByUserIDAndContentID(userId, contentId);
    }

    @Override
    public TaskListUser getUserByIDAndSession(Long userId, Long sessionId) {
	return taskListUserDao.getUserByUserIDAndSessionID(userId, sessionId);
    }

    @Override
    public TaskListUser getUser(Long uid) {
	return (TaskListUser) taskListUserDao.getObject(TaskListUser.class, uid);
    }

    @Override
    public void saveOrUpdateTaskList(TaskList taskList) {
	taskListDao.saveObject(taskList);
    }

    @Override
    public void deleteTaskListCondition(Long conditionUid) {
	taskListItemDao.removeObject(TaskListCondition.class, conditionUid);
    }

    @Override
    public void saveOrUpdateTaskListItem(TaskListItem item) {
	taskListItemDao.saveObject(item);
    }

    @Override
    public void deleteTaskListItem(Long uid) {
	taskListItemDao.removeObject(TaskListItem.class, uid);
    }

    @Override
    public TaskList getTaskListBySessionId(Long sessionId) {
	TaskListSession session = taskListSessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getTaskList().getContentId();
	TaskList taskList = taskListDao.getByContentId(contentId);
	for (TaskListItem item : taskList.getTaskListItems()) {
	    for (TaskListItemAttachment attachment : item.getAttachments()) {
		attachment.setFileDisplayUuid(taskListToolContentHandler.getFileUuid(attachment.getFileUuid()));
	    }
	}
	return taskList;
    }

    @Override
    public TaskListItem getTaskListItemByUid(Long itemUid) {
	TaskListItem item = taskListItemDao.getByUid(itemUid);
	for (TaskListItemAttachment attachment : item.getAttachments()) {
	    attachment.setFileDisplayUuid(taskListToolContentHandler.getFileUuid(attachment.getFileUuid()));
	}
	return item;
    }

    @Override
    public List<TaskListSession> getSessionsByContentId(Long contentId) {
	return taskListSessionDao.getByContentId(contentId);
    }

    @Override
    public TaskListSession getSessionBySessionId(Long sessionId) {
	return taskListSessionDao.getSessionBySessionId(sessionId);
    }

    @Override
    public void saveOrUpdateSession(TaskListSession resSession) {
	taskListSessionDao.saveObject(resSession);
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public Collection<TaskListUserDTO> getPagedUsersBySession(Long sessionId, int page, int size, String sortBy,
	    String sortOrder, String searchString) {
	return taskListUserDao.getPagedUsersBySession(sessionId, page, size, sortBy, sortOrder, searchString,
		userManagementService);
    }

    @Override
    public Collection<TaskListUserDTO> getPagedUsersBySessionAndItem(Long sessionId, Long taskListItemUid, int page,
	    int size, String sortBy, String sortOrder, String searchString) {
	return taskListUserDao.getPagedUsersBySessionAndItem(sessionId, taskListItemUid, page, size, sortBy, sortOrder,
		searchString);
    }

    @Override
    public int getCountPagedUsersBySession(Long sessionId, String searchString) {
	return taskListUserDao.getCountPagedUsersBySession(sessionId, searchString);
    }

    @Override
    public List<SessionDTO> getSummary(Long contentId) {

	List<TaskListSession> sessionList = taskListSessionDao.getByContentId(contentId);

	List<SessionDTO> summaryList = new ArrayList<>();

	// create the user list of all whom were started this task
	for (TaskListSession session : sessionList) {
	    Long toolSessionId = session.getSessionId();

	    List<TaskListItem> itemList = getItemListForGroup(contentId, toolSessionId);
	    List<TaskListUser> userList = taskListUserDao.getBySessionID(toolSessionId);

	    // Fill up the array of visitNumbers
	    int[] visitNumbers = new int[itemList.size()];
	    for (int j = 0; j < itemList.size(); j++) {
		TaskListItem item = itemList.get(j);

		// retreiving TaskListItemVisitLog for current taskList and user
		visitNumbers[j] = taskListItemVisitDao.getCountCompletedTasksBySessionAndItem(toolSessionId,
			item.getUid());
	    }

	    SessionDTO summary = new SessionDTO(toolSessionId, session.getSessionName(), itemList, visitNumbers);
	    summaryList.add(summary);
	}

	return summaryList;
    }

    @Override
    public NotebookEntry getEntry(Long sessionId, Integer userId) {
	List<NotebookEntry> list = coreNotebookService.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		TaskListConstants.TOOL_SIGNATURE, userId);
	if ((list == null) || list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    public List<ReflectDTO> getReflectList(Long contentId) {
	List<ReflectDTO> reflectList = new LinkedList<>();

	List<TaskListSession> sessionList = taskListSessionDao.getByContentId(contentId);
	for (TaskListSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    // get all users in this session
	    List<TaskListUser> users = taskListUserDao.getBySessionID(sessionId);
	    for (TaskListUser user : users) {

		NotebookEntry entry = getEntry(sessionId, user.getUserId().intValue());
		if (entry != null) {
		    ReflectDTO ref = new ReflectDTO(user);
		    ref.setReflect(entry.getEntry());
		    Date postedDate = (entry.getLastModified() != null) ? entry.getLastModified()
			    : entry.getCreateDate();
		    ref.setDate(postedDate);
		    reflectList.add(ref);
		}

	    }
	}

	return reflectList;
    }

    @Override
    public int getNumTasksCompletedByUser(Long toolSessionId, Long userId) {
	return getTaskListItemVisitDao().getCountCompletedTasksByUser(toolSessionId, userId);
    }

    @Override
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

    @Override
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

    @Override
    public List<TaskListUser> getUserListBySessionId(Long sessionId) {
	return taskListUserDao.getBySessionID(sessionId);
    }

    @Override
    public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
	    String entryText) {
	return coreNotebookService.createNotebookEntry(sessionId, notebookToolType, toolSignature, userId, "",
		entryText);
    }

    @Override
    public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID) {
	List<NotebookEntry> list = coreNotebookService.getEntry(sessionId, idType, signature, userID);
	if ((list == null) || list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    public void updateEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }

    @Override
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    @Override
    public void auditLogStartEditingActivityInMonitor(long toolContentID) {
	toolService.auditLogStartEditingActivityInMonitor(toolContentID);
    }

    @Override
    public boolean isLastActivity(Long toolSessionId) {
	return toolService.isLastActivity(toolSessionId);
    }

    @Override
    public String getMessage(String key) {
	return messageService.getMessage(key);
    }

    // *****************************************************************************
    // Set methods for Spring Bean
    // *****************************************************************************

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public TaskListOutputFactory getTaskListOutputFactory() {
	return taskListOutputFactory;
    }

    public void setTaskListOutputFactory(TaskListOutputFactory taskListOutputFactory) {
	this.taskListOutputFactory = taskListOutputFactory;
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

    public void setTaskListToolContentHandler(IToolContentHandler taskListToolContentHandler) {
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

    @Override
    public MessageService getMessageService() {
	return messageService;
    }

    // *******************************************************************************
    // Methods implementing ToolContentManager, ToolSessionManager
    // *******************************************************************************

    @Override
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

    @Override
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

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	TaskList taskList = getTaskListByContentId(toolContentId);
	if (taskList == null) {
	    taskList = getDefaultTaskList();
	}
	return getTaskListOutputFactory().getToolOutputDefinitions(taskList, definitionType);
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getTaskListByContentId(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getTaskListByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	List<TaskListSession> sessions = taskListSessionDao.getByContentId(toolContentId);
	for (TaskListSession session : sessions) {
	    if (!taskListUserDao.getBySessionID(session.getSessionId()).isEmpty()) {
		return true;
	    }
	}

	return false;
    }

    @Override
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

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	TaskList taskList = taskListDao.getByContentId(toolContentId);
	if (taskList == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	taskList.setDefineLater(false);
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	TaskList taskList = taskListDao.getByContentId(toolContentId);
	if (taskList == null) {
	    TaskListServiceImpl.log.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (TaskListSession session : taskListSessionDao.getByContentId(toolContentId)) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, TaskListConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	taskListDao.delete(taskList);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (TaskListServiceImpl.log.isDebugEnabled()) {
	    TaskListServiceImpl.log
		    .debug("Removing Task List contents for user ID " + userId + " and toolContentId " + toolContentId);
	}
	TaskList taskList = taskListDao.getByContentId(toolContentId);
	if (taskList == null) {
	    TaskListServiceImpl.log
		    .warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	Set<TaskListItem> items = taskList.getTaskListItems();
	for (TaskListItem item : items) {
	    if (TaskListServiceImpl.log.isDebugEnabled()) {
		TaskListServiceImpl.log.debug("Removing visit log, comments and attachments for user ID " + userId
			+ " and item UID " + item.getUid());
	    }

	    TaskListItemVisitLog visitLog = taskListItemVisitDao.getTaskListItemLog(item.getUid(), userId.longValue());
	    if (visitLog != null) {
		taskListDao.removeObject(TaskListItemVisitLog.class, visitLog.getUid());
	    }

	    Iterator<TaskListItemAttachment> attachmentIter = item.getAttachments().iterator();
	    while (attachmentIter.hasNext()) {
		TaskListItemAttachment attachment = attachmentIter.next();
		if (attachment.getCreateBy().getUserId().equals(userId.longValue())) {
		    try {
			taskListToolContentHandler.deleteFile(attachment.getFileUuid());
		    } catch (Exception e) {
			throw new ToolException("Error while removing Task List attachment", e);
		    }
		    taskListDao.removeObject(TaskListItemAttachment.class, attachment.getUid());
		    attachmentIter.remove();
		}
	    }

	    Iterator<TaskListItemComment> commentIter = item.getComments().iterator();
	    while (commentIter.hasNext()) {
		TaskListItemComment comment = commentIter.next();
		if (comment.getCreateBy().getUserId().equals(userId.longValue())) {
		    taskListDao.removeObject(TaskListItemComment.class, comment.getUid());
		    commentIter.remove();
		}
	    }
	}

	List<TaskListSession> sessions = taskListSessionDao.getByContentId(toolContentId);
	for (TaskListSession session : sessions) {
	    TaskListUser user = taskListUserDao.getUserByUserIDAndSessionID(userId.longValue(), session.getSessionId());
	    if (user != null) {
		NotebookEntry entry = getEntry(user.getSession().getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			TaskListConstants.TOOL_SIGNATURE, userId);
		if (entry != null) {
		    taskListDao.removeObject(NotebookEntry.class, entry.getUid());
		}

		taskListUserDao.removeObject(TaskListUser.class, user.getUid());
	    }
	}
    }

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	TaskListSession session = new TaskListSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	TaskList taskList = taskListDao.getByContentId(toolContentId);
	session.setTaskList(taskList);
	taskListSessionDao.saveObject(session);
    }

    @Override
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
	return toolService.completeToolSession(toolSessionId, learnerId);
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	taskListSessionDao.deleteBySessionId(toolSessionId);
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return taskListOutputFactory.getToolOutput(names, this, toolSessionId, learnerId);
    }

    /**
     * Get the tool output for the given tool output name.
     *
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long,
     *      java.lang.Long)
     */
    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return taskListOutputFactory.getToolOutput(name, this, toolSessionId, learnerId);
    }

    @Override
    public List<ToolOutput> getToolOutputs(String name, Long toolContentId) {
	return new ArrayList<>();
    }

    @Override
    public List<ConfidenceLevelDTO> getConfidenceLevels(Long toolSessionId) {
	return null;
    }

    @Override
    public boolean isUserGroupLeader(Long userId, Long toolSessionId) {
	return false;
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	//no actions required
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
     *            sessionId which defines Group
     * @return
     */
    private List<TaskListItem> getItemListForGroup(Long contentId, Long sessionId) {

	// create the list containing all taskListItems
	TaskList taskList = taskListDao.getByContentId(contentId);
	ArrayList<TaskListItem> itemList = new ArrayList<>();
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

	ArrayList<TaskListItem> groupItemList = new ArrayList<>();
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
    private NodeKey processFile(File file) throws UploadTaskListFileException {
	NodeKey node = null;
	if ((file != null) && !StringUtils.isEmpty(file.getName())) {
	    String fileName = file.getName();
	    try {
		node = taskListToolContentHandler.uploadFile(new FileInputStream(file), fileName, null);
	    } catch (InvalidParameterException e) {
		throw new UploadTaskListFileException(
			messageService.getMessage("error.msg.invaid.param.upload") + " " + e.getMessage());
	    } catch (FileNotFoundException e) {
		throw new UploadTaskListFileException(
			messageService.getMessage("error.msg.file.not.found") + " " + e.getMessage());
	    } catch (RepositoryCheckedException e) {
		throw new UploadTaskListFileException(
			messageService.getMessage("error.msg.repository") + " " + e.getMessage());
	    }
	}
	return node;
    }

    // *******************************************************************************
    // Service method
    // *******************************************************************************

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getTaskListOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	TaskListUser learner = getUserByIDAndSession(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	Object[] dates = getTaskListItemVisitDao().getDateRangeOfTasks(learner.getUid(), toolSessionId);
	if (learner.isSessionFinished()) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_COMPLETED, (Date) dates[0], (Date) dates[1]);
	} else {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_ATTEMPTED, (Date) dates[0], null);
	}
    }

}
