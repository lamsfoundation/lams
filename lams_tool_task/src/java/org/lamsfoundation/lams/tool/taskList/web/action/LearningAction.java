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


package org.lamsfoundation.lams.tool.taskList.web.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.learning.web.bean.ActivityPositionDTO;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.taskList.TaskListConstants;
import org.lamsfoundation.lams.tool.taskList.dto.TasListItemDTO;
import org.lamsfoundation.lams.tool.taskList.model.TaskList;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemAttachment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemComment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListSession;
import org.lamsfoundation.lams.tool.taskList.model.TaskListUser;
import org.lamsfoundation.lams.tool.taskList.service.ITaskListService;
import org.lamsfoundation.lams.tool.taskList.service.TaskListException;
import org.lamsfoundation.lams.tool.taskList.service.UploadTaskListFileException;
import org.lamsfoundation.lams.tool.taskList.util.TaskListItemAttachmentComparator;
import org.lamsfoundation.lams.tool.taskList.util.TaskListItemCommentComparator;
import org.lamsfoundation.lams.tool.taskList.util.TaskListItemComparator;
import org.lamsfoundation.lams.tool.taskList.web.form.ReflectionForm;
import org.lamsfoundation.lams.tool.taskList.web.form.TaskListItemForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Steve.Ni
 */
public class LearningAction extends Action {

    private static Logger log = Logger.getLogger(LearningAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, UploadTaskListFileException {

	String param = mapping.getParameter();
	// -----------------------TaskList Learner function ---------------------------
	if (param.equals("start")) {
	    return start(mapping, form, request, response);
	}

	if (param.equals("complete")) {
	    return complete(mapping, form, request, response);
	}

	if (param.equals("finish")) {
	    return finish(mapping, form, request, response);
	}

	if (param.equals("addtask")) {
	    return addTask(mapping, form, request, response);
	}
	if (param.equals("saveNewTask")) {
	    return saveNewTask(mapping, form, request, response);
	}

	if (param.equals("addNewComment")) {
	    return addNewComment(mapping, form, request, response);
	}
	if (param.equals("uploadFile")) {
	    return uploadFile(mapping, form, request, response);
	}

	// ================ Reflection =======================
	if (param.equals("newReflection")) {
	    return newReflection(mapping, form, request, response);
	}
	if (param.equals("submitReflection")) {
	    return submitReflection(mapping, form, request, response);
	}

	return mapping.findForward(TaskListConstants.ERROR);
    }

    /**
     * Read taskList data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     *
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	// save toolContentID into HTTPSession
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);

	Long sessionId = new Long(request.getParameter(TaskListConstants.PARAM_TOOL_SESSION_ID));

	request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);

	// get back the taskList and item list and display them on page
	ITaskListService service = getTaskListService();
	TaskListUser taskListUser = null;
	if (mode != null && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // taskListUser may be null if the user was force completed.
	    taskListUser = getSpecifiedUser(service, sessionId,
		    WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    taskListUser = getCurrentUser(service, sessionId);
	}

	TaskList taskList = service.getTaskListBySessionId(sessionId);

	// Create set of TaskListItems besides this filtering out items added by users from other groups
	TreeSet<TaskListItem> items = new TreeSet<TaskListItem>(new TaskListItemComparator());
	if (mode.isLearner()) {

	    List<TaskListUser> grouppedUsers = service.getUserListBySessionId(sessionId);
	    Set<TaskListItem> allTaskListItems = taskList.getTaskListItems();

	    for (TaskListItem item : allTaskListItems) {
		for (TaskListUser grouppedUser : grouppedUsers) {
		    if (item.isCreateByAuthor() || grouppedUser.getUserId().equals(item.getCreateBy().getUserId())) {
			items.add(item);
		    }
		}
	    }

	} else {
	    items.addAll(taskList.getTaskListItems());
	}

	// check whehter finish lock is on/off
	boolean lock = taskList.getLockWhenFinished() && taskListUser != null && taskListUser.isSessionFinished();

	// get notebook entry
	String entryText = new String();
	if (taskListUser != null) {
	    NotebookEntry notebookEntry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    TaskListConstants.TOOL_SIGNATURE, taskListUser.getUserId().intValue());
	    if (notebookEntry != null) {
		entryText = notebookEntry.getEntry();
	    }
	}

	// set complete flag for display purpose
	if (taskListUser != null) {
	    service.retrieveComplete(items, taskListUser);
	}

	TreeSet<TasListItemDTO> itemDTOs = new TreeSet<TasListItemDTO>(new Comparator<TasListItemDTO>() {
	    @Override
	    public int compare(TasListItemDTO o1, TasListItemDTO o2) {
		if (o1 != null && o2 != null) {
		    return o1.getTaskListItem().getSequenceId() - o2.getTaskListItem().getSequenceId();
		} else if (o1 != null) {
		    return 1;
		} else {
		    return -1;
		}
	    }
	});

	boolean isPreviousTaskCompleted = true;
	for (TaskListItem item : items) {
	    TasListItemDTO itemDTO = new TasListItemDTO(item);

	    // checks if this item met all comment requirements
	    boolean isCommentRequirementsMet = true;
	    if (item.isCommentsRequired()) {
		isCommentRequirementsMet = false;
		Set<TaskListItemComment> comments = item.getComments();
		for (TaskListItemComment comment : comments) {
		    if (taskListUser.getUserId().equals(comment.getCreateBy().getUserId())) {
			isCommentRequirementsMet = true;
		    }
		}
	    }
	    itemDTO.setCommentRequirementsMet(isCommentRequirementsMet);

	    // checks if this item met all attachment requirements
	    boolean isAttachmentRequirementsMet = true;
	    if (item.isFilesRequired()) {
		isAttachmentRequirementsMet = false;
		Set<TaskListItemAttachment> attachments = item.getAttachments();
		for (TaskListItemAttachment attachment : attachments) {
		    if (taskListUser.getUserId().equals(attachment.getCreateBy().getUserId())) {
			isAttachmentRequirementsMet = true;
		    }
		}
	    }
	    itemDTO.setAttachmentRequirementsMet(isAttachmentRequirementsMet);

	    // checks if this item is allowed by its parent
	    boolean isAllowedByParent = true;
	    if (item.isChildTask()) {
		for (TaskListItem parentItem : items) {
		    if (parentItem.getTitle().equals(item.getParentTaskName()) && !parentItem.isComplete()) {
			isAllowedByParent = false;
		    }
		}
	    }
	    itemDTO.setAllowedByParent(isAllowedByParent);

	    // checks whether this TaskListItem shoud be displayed open or close
	    boolean isDisplayedOpen = true;
	    if ((item.getDescription() != null) && (item.getDescription().length() > 1000)) {
		isDisplayedOpen = false;
	    }
	    itemDTO.setDisplayedOpen(isDisplayedOpen);

	    // sets whether the previous TaskListItem was completed
	    itemDTO.setPreviousTaskCompleted(isPreviousTaskCompleted);
	    isPreviousTaskCompleted = item.isComplete();

	    // filter out comments and attachments which belong to another group
	    Set filteredComments = new TreeSet<TaskListItemComment>(new TaskListItemCommentComparator());
	    Set filteredAttachments = new TreeSet<TaskListItemAttachment>(new TaskListItemAttachmentComparator());
	    if (mode.isLearner()) {

		List<TaskListUser> grouppedUsers = service.getUserListBySessionId(sessionId);
		Set<TaskListItemComment> comments = item.getComments();
		Set<TaskListItemAttachment> attachments = item.getAttachments();

		for (TaskListItemComment comment : comments) {
		    for (TaskListUser grouppedUser : grouppedUsers) {
			if (grouppedUser.getUserId().equals(comment.getCreateBy().getUserId())) {
			    filteredComments.add(comment);
			}
		    }
		}

		for (TaskListItemAttachment attachment : attachments) {
		    for (TaskListUser grouppedUser : grouppedUsers) {
			if (grouppedUser.getUserId().equals(attachment.getCreateBy().getUserId())) {
			    filteredAttachments.add(attachment);
			}
		    }
		}
	    } else {
		filteredComments = item.getComments();
		filteredAttachments = item.getAttachments();
	    }
	    itemDTO.setComments(filteredComments);
	    itemDTO.setAttachments(filteredAttachments);

	    itemDTOs.add(itemDTO);
	}

	// construct taskList dto field

	Integer numberCompletedTasks = service.getNumTasksCompletedByUser(sessionId, taskListUser.getUserId());
	Integer minimumNumberTasks = taskList.getMinimumNumberTasks();
	if ((minimumNumberTasks - numberCompletedTasks) > 0) {
	    String MinimumNumberTasksStr = service.getMessageService().getMessage("lable.learning.minimum.view.number",
		    new Object[] { minimumNumberTasks, numberCompletedTasks });
	    taskList.setMinimumNumberTasksErrorStr(MinimumNumberTasksStr);
	}

	// basic information
	sessionMap.put(TaskListConstants.ATTR_TITLE, taskList.getTitle());
	sessionMap.put(TaskListConstants.ATTR_FINISH_LOCK, lock);
	sessionMap.put(TaskListConstants.ATTR_USER_FINISHED, taskListUser != null && taskListUser.isSessionFinished());
	sessionMap.put(TaskListConstants.ATTR_USER_VERIFIED_BY_MONITOR, taskListUser.isVerifiedByMonitor());
	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	sessionMap.put(TaskListConstants.ATTR_TASK_LIST_ITEM_DTOS, itemDTOs);
	// reflection information
	sessionMap.put(TaskListConstants.ATTR_REFLECTION_ON, taskList.isReflectOnActivity());
	sessionMap.put(TaskListConstants.ATTR_REFLECTION_INSTRUCTION, taskList.getReflectInstructions());
	sessionMap.put(TaskListConstants.ATTR_REFLECTION_ENTRY, entryText);

	// add define later support
	if (taskList.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set contentInUse flag to true!
	taskList.setContentInUse(true);
	taskList.setDefineLater(false);
	service.saveOrUpdateTaskList(taskList);

	ActivityPositionDTO activityPosition = LearningWebUtil.putActivityPositionInRequestByToolSessionId(sessionId,
		request, getServlet().getServletContext());
	sessionMap.put(AttributeNames.ATTR_ACTIVITY_POSITION, activityPosition);

	// check if there is submission deadline
	Date submissionDeadline = taskList.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    Date currentLearnerDate = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, new Date());
	    sessionMap.put(TaskListConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline);

	    // calculate whether deadline has passed, and if so forward to "submissionDeadline"
	    if (currentLearnerDate.after(tzSubmissionDeadline)) {
		return mapping.findForward("submissionDeadline");
	    }

	}

	sessionMap.put(TaskListConstants.ATTR_TASKLIST, taskList);

	return mapping.findForward(TaskListConstants.SUCCESS);
    }

    /**
     * Mark taskList item as complete status.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward complete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String mode = request.getParameter(AttributeNames.ATTR_MODE);
	String sessionMapID = request.getParameter(TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(TaskListConstants.ATTR_TOOL_SESSION_ID);

	doComplete(request);

	ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig(TaskListConstants.SUCCESS));
	redirect.addParameter(AttributeNames.ATTR_MODE, mode);
	redirect.addParameter(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	return redirect;
    }

    /**
     * Finish learning session.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back SessionMap
	String sessionMapID = request.getParameter(TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);

	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// auto run mode, when use finish the only one taskList item, mark it as complete then finish this activity as
	// well.
	String taskListItemUid = request.getParameter(TaskListConstants.PARAM_ITEM_UID);
	if (taskListItemUid != null) {
	    doComplete(request);
	    // NOTE:So far this flag is useless(31/08/2006).
	    // set flag, then finish page can know redir target is parent(AUTO_RUN) or self(normal)
	    request.setAttribute(TaskListConstants.ATTR_RUN_AUTO, true);
	} else {
	    request.setAttribute(TaskListConstants.ATTR_RUN_AUTO, false);
	}

	if (!validateBeforeFinish(request, sessionMapID)) {
	    return mapping.getInputForward();
	}

	ITaskListService service = getTaskListService();
	// get sessionId from HttpServletRequest
	String nextActivityUrl = null;
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userID = new Long(user.getUserID().longValue());

	    nextActivityUrl = service.finishToolSession(sessionId, userID);
	    request.setAttribute(TaskListConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (TaskListException e) {
	    log.error("Failed get next activity url:" + e.getMessage());
	}

	return mapping.findForward(TaskListConstants.SUCCESS);
    }

    /**
     * Initial page for add taskList item (single file or URL).
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward addTask(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	TaskListItemForm itemForm = (TaskListItemForm) form;
	itemForm.setMode(WebUtil.readStrParam(request, AttributeNames.ATTR_MODE));
	itemForm.setSessionMapID(WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID));
	return mapping.findForward(TaskListConstants.SUCCESS);
    }

    /**
     * Save new user task into database.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward saveNewTask(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// get back SessionMap
	String sessionMapID = request.getParameter(TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);

	TaskListItemForm itemForm = (TaskListItemForm) form;
	ActionErrors errors = validateTaskListItem(itemForm);

	if (!errors.isEmpty()) {
	    this.addErrors(request, errors);
	    request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	    return mapping.findForward("task");
	}

	// create a new TaskListItem
	TaskListItem item = new TaskListItem();
	ITaskListService service = getTaskListService();
	Long sessionId = (Long) sessionMap.get(TaskListConstants.ATTR_TOOL_SESSION_ID);
	TaskListUser taskListUser = getCurrentUser(service, sessionId);
	item.setTitle(itemForm.getTitle());
	item.setDescription(itemForm.getDescription());
	item.setCreateDate(new Timestamp(new Date().getTime()));
	item.setCreateByAuthor(false);
	item.setCreateBy(taskListUser);

	// setting SequenceId
	TaskList taskList = (TaskList) sessionMap.get(TaskListConstants.ATTR_TASKLIST);
	Set<TaskListItem> taskListItems = taskList.getTaskListItems();
	int maxSeq = 0;
	for (TaskListItem dbItem : taskListItems) {
	    if (dbItem.getSequenceId() > maxSeq) {
		maxSeq = dbItem.getSequenceId();
	    }
	}
	maxSeq++;
	item.setSequenceId(maxSeq);

	taskListItems.add(item);
	service.saveOrUpdateTaskList(taskList);

	// redirect
	String mode = request.getParameter(AttributeNames.ATTR_MODE);
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	return mapping.findForward(TaskListConstants.SUCCESS);
    }

    /**
     * Adds new user commment.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward addNewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	TaskListItemForm taskListItemForm = (TaskListItemForm) form;
	String mode = request.getParameter(AttributeNames.ATTR_MODE);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(taskListItemForm.getSessionMapID());
	request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	Long sessionId = (Long) sessionMap.get(TaskListConstants.ATTR_TOOL_SESSION_ID);

	boolean isTestHarness = Boolean.valueOf(request.getParameter("testHarness"));
	String commentMessage = isTestHarness ? request.getParameter("comment__textarea")
		: taskListItemForm.getComment();
	if (commentMessage == null || StringUtils.isBlank(commentMessage)) {
	    return mapping.findForward("refresh");
	}

	TaskListItemComment comment = new TaskListItemComment();
	comment.setComment(commentMessage);
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	ITaskListService service = getTaskListService();
	TaskListUser taskListUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()), sessionId);
	comment.setCreateBy(taskListUser);
	comment.setCreateDate(new Timestamp(new Date().getTime()));

	// persist TaskListItem changes in DB
	Long itemUid = new Long(request.getParameter(TaskListConstants.PARAM_ITEM_UID));
	TaskListItem dbItem = service.getTaskListItemByUid(itemUid);
	Set<TaskListItemComment> dbComments = dbItem.getComments();
	dbComments.add(comment);
	service.saveOrUpdateTaskListItem(dbItem);

	// to make available new changes be visible in jsp page
	sessionMap.put(TaskListConstants.ATTR_TASK_LIST_ITEM, dbItem);

	// form.reset(mapping, request);

	ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig(TaskListConstants.SUCCESS));
	redirect.addParameter(AttributeNames.ATTR_MODE, mode);
	redirect.addParameter(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	return redirect;
    }

    /**
     * Uploads specified file to repository and associates it with current TaskListItem.
     *
     * @param mapping
     * @param form
     * @param type
     * @param request
     * @return
     * @throws UploadTaskListFileException
     */
    private ActionForward uploadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws UploadTaskListFileException {

	TaskListItemForm taskListItemForm = (TaskListItemForm) form;
	String mode = request.getParameter(AttributeNames.ATTR_MODE);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(taskListItemForm.getSessionMapID());
	request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	Long sessionId = (Long) sessionMap.get(TaskListConstants.ATTR_TOOL_SESSION_ID);

	FormFile file = taskListItemForm.getUploadedFile();

	if (file == null || StringUtils.isBlank(file.getFileName())) {
	    return mapping.findForward("refresh");
	}

	// validate file size
	ActionMessages errors = new ActionMessages();
	FileValidatorUtil.validateFileSize(file, false, errors);
	if (!errors.isEmpty()) {
	    this.addErrors(request, errors);
	    this.saveErrors(request, errors);
	    return mapping.findForward("refresh");
	}

	// upload to repository
	ITaskListService service = getTaskListService();
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	TaskListUser taskListUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()), sessionId);
	TaskListItemAttachment att = service.uploadTaskListItemFile(file, taskListUser);

	// persist TaskListItem changes in DB
	Long itemUid = new Long(request.getParameter(TaskListConstants.PARAM_ITEM_UID));
	TaskListItem dbItem = service.getTaskListItemByUid(itemUid);
	Set<TaskListItemAttachment> dbAttachments = dbItem.getAttachments();
	dbAttachments.add(att);
	service.saveOrUpdateTaskListItem(dbItem);

	// to make available new changes be visible in jsp page
	sessionMap.put(TaskListConstants.ATTR_TASK_LIST_ITEM, dbItem);

	// form.reset(mapping, request);

	ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig(TaskListConstants.SUCCESS));
	redirect.addParameter(AttributeNames.ATTR_MODE, mode);
	redirect.addParameter(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	return redirect;
    }

    /**
     * Display empty reflection form.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get session value
	String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);

	if (!validateBeforeFinish(request, sessionMapID)) {
	    return mapping.getInputForward();
	}

	ReflectionForm refForm = (ReflectionForm) form;
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry
	ITaskListService submitFilesService = getTaskListService();

	SessionMap<String, Object> map = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = submitFilesService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		TaskListConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	return mapping.findForward(TaskListConstants.SUCCESS);
    }

    /**
     * Submit reflection form input database.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ReflectionForm refForm = (ReflectionForm) form;
	Integer userId = refForm.getUserID();

	String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	ITaskListService service = getTaskListService();

	// check for existing notebook entry
	NotebookEntry entry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		TaskListConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    service.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    TaskListConstants.TOOL_SIGNATURE, userId, refForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(refForm.getEntryText());
	    entry.setLastModified(new Date());
	    service.updateEntry(entry);
	}

	return finish(mapping, form, request, response);
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    private ITaskListService getTaskListService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (ITaskListService) wac.getBean(TaskListConstants.TASKLIST_SERVICE);
    }

    private TaskListUser getCurrentUser(ITaskListService service, Long sessionId) {
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	TaskListUser taskListUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()), sessionId);

	if (taskListUser == null) {
	    TaskListSession session = service.getSessionBySessionId(sessionId);
	    taskListUser = new TaskListUser(user, session);
	    service.createUser(taskListUser);
	}
	return taskListUser;
    }

    private TaskListUser getSpecifiedUser(ITaskListService service, Long sessionId, Integer userId) {
	TaskListUser taskListUser = service.getUserByIDAndSession(new Long(userId.intValue()), sessionId);
	if (taskListUser == null) {
	    log.error("Unable to find specified user for taskList activity. Screens are likely to fail. SessionId="
		    + sessionId + " UserId=" + userId);
	}
	return taskListUser;
    }

    /**
     * @param itemForm
     * @return
     */
    private ActionErrors validateTaskListItem(TaskListItemForm itemForm) {
	ActionErrors errors = new ActionErrors();
	if (StringUtils.isBlank(itemForm.getTitle())) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(TaskListConstants.ERROR_MSG_TITLE_BLANK));
	}

	return errors;
    }

    private boolean validateBeforeFinish(HttpServletRequest request, String sessionMapID) {
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userID = new Long(user.getUserID().longValue());

	ITaskListService service = getTaskListService();

	int numberCompletedTasks = service.getNumTasksCompletedByUser(sessionId, userID);
	int minimumNumberTasks = service.getTaskListBySessionId(sessionId).getMinimumNumberTasks();
	// if current user view less than reqired view count number, then just return error message.
	if ((minimumNumberTasks - numberCompletedTasks) > 0) {
	    ActionErrors errors = new ActionErrors();
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage("lable.learning.minimum.view.number", minimumNumberTasks, numberCompletedTasks));
	    this.addErrors(request, errors);
	    return false;
	}

	return true;
    }

    /**
     * Set complete flag for given taskList item.
     *
     * @param request
     * @param sessionId
     */
    private void doComplete(HttpServletRequest request) {
	// get back sessionMap
	String sessionMapID = request.getParameter(TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	Long taskListItemUid = new Long(request.getParameter(TaskListConstants.PARAM_ITEM_UID));
	ITaskListService service = getTaskListService();
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	Long sessionId = (Long) sessionMap.get(TaskListConstants.ATTR_TOOL_SESSION_ID);
	service.setItemComplete(taskListItemUid, new Long(user.getUserID().intValue()), sessionId);
	sessionMapID = "4";
    }

}
