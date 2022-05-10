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

package org.lamsfoundation.lams.tool.sbmt.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.sbmt.SbmtConstants;
import org.lamsfoundation.lams.tool.sbmt.dto.FileDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.SubmitUserDTO;
import org.lamsfoundation.lams.tool.sbmt.model.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.model.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.model.SubmitUser;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.util.SubmitFilesException;
import org.lamsfoundation.lams.tool.sbmt.web.form.LearnerForm;
import org.lamsfoundation.lams.tool.sbmt.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Manpreet Minhas
 * @author Steve.Ni
 */
@Controller
@RequestMapping("/learning")
public class LearningController implements SbmtConstants {
    public static Logger logger = Logger.getLogger(LearningController.class);

    @Autowired
    private ISubmitFilesService submitFilesService;

    @Autowired
    @Qualifier("sbmtMessageService")
    private MessageService messageService;

    /**
     * The initial page of learner in Submission tool. This page will list all uploaded files and learn
     */
    @RequestMapping("/learner")
    public String learner(@ModelAttribute LearnerForm learnerForm, HttpServletRequest request) {
	// initial session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(SbmtConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	learnerForm.setSessionMapID(sessionMap.getSessionID());

	// get parameters from Request
	ToolAccessMode mode = null;
	try {
	    mode = WebUtil.getToolAccessMode((String) request.getAttribute(AttributeNames.PARAM_MODE));
	} catch (Exception e) {
	}
	if (mode == null) {
	    mode = ToolAccessMode.LEARNER;
	}
	request.setAttribute("mode", mode);

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// get session from shared session.
	HttpSession ss = SessionManager.getSession();

	Integer userID = null;
	if (mode != null && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    userID = WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false);
	} else {
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    userID = user.getUserID();
	}
	// this must before getFileUploadByUser() method becuase getCurrentLearner()
	// will create session user if it does not exist.
	SubmitUser learner = getCurrentLearner(toolSessionID, submitFilesService);

	SubmitFilesSession session = submitFilesService.getSessionById(toolSessionID);
	SubmitFilesContent content = session.getContent();
	// support for leader select feature
	SubmitUser groupLeader = content.isUseSelectLeaderToolOuput()
		? submitFilesService.checkLeaderSelectToolForSessionLeader(learner, toolSessionID)
		: null;

	sessionMap.put(AttributeNames.PARAM_MODE, mode);
	sessionMap.put(SbmtConstants.ATTR_TITLE, content.getTitle());
	sessionMap.put(SbmtConstants.ATTR_INSTRUCTION, content.getInstruction());

	// if content in use, return special page.
	if (content.isDefineLater()) {
	    return "learner/definelater";
	}

	if (content.isUseSelectLeaderToolOuput() && !mode.isTeacher()) {

	    // forwards to the leaderSelection page
	    if (groupLeader == null) {
		request.setAttribute("groupUsers", submitFilesService.getUsersBySession(toolSessionID));
		request.setAttribute(SbmtConstants.PARAM_WAITING_MESSAGE_KEY, "label.waiting.for.leader");
		return "learner/waitforleader";
	    }

	    // forwards to the waitForLeader pages
	    boolean isNonLeader = !userID.equals(groupLeader.getUserID());

	    if (isNonLeader && !learner.isFinished()) {
		List<FileDetailsDTO> filesUploadedByLeader = submitFilesService
			.getFilesUploadedByUser(groupLeader.getUserID(), toolSessionID, request.getLocale(), false);

		if (filesUploadedByLeader == null) {
		    request.setAttribute(SbmtConstants.PARAM_WAITING_MESSAGE_KEY,
			    "label.waiting.for.leader.launch.time.limit");
		    request.setAttribute("groupUsers", submitFilesService.getUsersBySession(toolSessionID));
		    return "learner/waitforleader";
		}

		//if the time is up and leader hasn't submitted response - show waitForLeaderFinish page
		if (!groupLeader.isFinished()) {
		    request.setAttribute(SbmtConstants.PARAM_WAITING_MESSAGE_KEY, "label.waiting.for.leader.finish");
		    request.setAttribute("groupUsers", submitFilesService.getUsersBySession(toolSessionID));
		    return "learner/waitforleader";
		}
	    }
	}

	List<FileDetailsDTO> filesUploaded = submitFilesService.getFilesUploadedByUser(userID, toolSessionID,
		request.getLocale(), false);

	// check whehter finish lock is on/off
	boolean lock = content.isLockOnFinished() && learner.isFinished();

	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionID);
	sessionMap.put(SbmtConstants.ATTR_FINISH_LOCK, lock);
	sessionMap.put(SbmtConstants.ATTR_LOCK_ON_FINISH, content.isLockOnFinished());
	sessionMap.put(SbmtConstants.ATTR_USE_SEL_LEADER, content.isUseSelectLeaderToolOuput());
	sessionMap.put(SbmtConstants.ATTR_REFLECTION_ON, content.isReflectOnActivity());
	sessionMap.put(SbmtConstants.ATTR_REFLECTION_INSTRUCTION, content.getReflectInstructions());
	sessionMap.put(SbmtConstants.ATTR_IS_MAX_LIMIT_UPLOAD_ENABLED, content.isLimitUpload());
	sessionMap.put(SbmtConstants.ATTR_MAX_LIMIT_UPLOAD_NUMBER, content.getLimitUploadNumber());
	sessionMap.put(SbmtConstants.ATTR_MIN_LIMIT_UPLOAD_NUMBER, content.getMinLimitUploadNumber());
	sessionMap.put(SbmtConstants.ATTR_USER_FINISHED, learner.isFinished());
	sessionMap.put(SbmtConstants.ATTR_IS_MARKS_RELEASED, session.isMarksReleased());

	sessionMap.put(SbmtConstants.ATTR_UPLOAD_MAX_FILE_SIZE,
		FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)));
	setLearnerDTO(request, sessionMap, learner, filesUploaded, mode);

	// set contentInUse flag to true!
	content.setContentInUse(true);
	content.setDefineLater(false);
	submitFilesService.saveOrUpdateContent(content);

	request.setAttribute(AttributeNames.ATTR_IS_LAST_ACTIVITY, submitFilesService.isLastActivity(toolSessionID));

	// check if there is submission deadline
	Date submissionDeadline = content.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    // store submission deadline to sessionMap
	    sessionMap.put(SbmtConstants.ATTR_SUBMISSION_DEADLINE, submissionDeadline);

	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    Date currentLearnerDate = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, new Date());

	    // calculate whether submission deadline has passed, and if so forward to "submissionDeadline"
	    if (currentLearnerDate.after(tzSubmissionDeadline)) {
		return "learner/submissionDeadline";
	    }
	}

	if (content.isUseSelectLeaderToolOuput() && groupLeader.isFinished() && !mode.isTeacher()) {
	    // check if leader has submitted all answers
	    submitFilesService.copyLearnerContent(groupLeader, learner);
	    filesUploaded = submitFilesService.getFilesUploadedByUser(learner.getUserID(), learner.getSessionID(),
		    request.getLocale(), false);
	    setLearnerDTO(request, sessionMap, learner, filesUploaded, mode);
	}

	sessionMap.put(SbmtConstants.ATTR_GROUP_LEADER, groupLeader);
	boolean isUserLeader = submitFilesService.isUserGroupLeader(learner.getUserID().longValue(), toolSessionID);
	sessionMap.put(SbmtConstants.ATTR_IS_USER_LEADER, isUserLeader);

	boolean hasEditRight = !content.isUseSelectLeaderToolOuput()
		|| content.isUseSelectLeaderToolOuput() && isUserLeader;
	sessionMap.put(SbmtConstants.ATTR_HAS_EDIT_RIGHT, hasEditRight);

	learnerForm.setTmpFileUploadId(FileUtil.generateTmpFileUploadId());

	return "learner/sbmtlearner";
    }

    @RequestMapping("/teacher")
    public String teacher(@ModelAttribute LearnerForm learnerForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("mode", "teacher");
	return learner(learnerForm, request);
    }

    @RequestMapping("/author")
    public String author(@ModelAttribute LearnerForm learnerForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("mode", "author");
	return learner(learnerForm, request);
    }

    /**
     * Loads the main learner page with the details currently in the session map
     */
    @RequestMapping("/refresh")
    public String refresh(@ModelAttribute LearnerForm learnerForm, HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, SbmtConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	learnerForm.setSessionMapID(sessionMap.getSessionID());
	request.setAttribute(SbmtConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	// get session from shared session.
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Integer userID = user.getUserID();
	Long sessionID = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	List<FileDetailsDTO> filesUploaded = submitFilesService.getFilesUploadedByUser(userID, sessionID,
		request.getLocale(), false);
	SubmitUser learner = getCurrentLearner(sessionID, submitFilesService);
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	setLearnerDTO(request, sessionMap, learner, filesUploaded, mode);
	SubmitFilesSession session = submitFilesService.getSessionById(sessionID);
	sessionMap.put(SbmtConstants.ATTR_IS_MARKS_RELEASED, session.isMarksReleased());

	learnerForm.setTmpFileUploadId(FileUtil.generateTmpFileUploadId());

	return "learner/sbmtlearner";
    }

    /**
     * Implements learner upload submission function. This function also display the page again for learner uploading
     * more submission use.
     */
    @RequestMapping("/uploadFile")
    public String uploadFile(@ModelAttribute LearnerForm learnerForm, HttpServletRequest request) {
	String sessionMapID = learnerForm.getSessionMapID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute("sessionMapID", sessionMapID);

	Long sessionID = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	request.setAttribute(AttributeNames.ATTR_IS_LAST_ACTIVITY, submitFilesService.isLastActivity(sessionID));

	File[] files = null;
	File uploadDir = FileUtil.getTmpFileUploadDir(learnerForm.getTmpFileUploadId());
	if (uploadDir.canRead()) {
	    files = uploadDir.listFiles();
	    if (files.length == 0) {
		files = null;
	    }
	}

	if (validateUploadForm(learnerForm, files, request)) {
	    // get session from shared session.
	    HttpSession ss = SessionManager.getSession();
	    // get back login user DTO
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Integer userID = user.getUserID();

	    List<FileDetailsDTO> filesUploaded = submitFilesService.getFilesUploadedByUser(userID, sessionID,
		    request.getLocale(), false);

	    SubmitUser learner = getCurrentLearner(sessionID, submitFilesService);
	    ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	    setLearnerDTO(request, sessionMap, learner, filesUploaded, mode);

	    learnerForm.setTmpFileUploadId(FileUtil.generateTmpFileUploadId());

	    return "learner/sbmtlearner";
	}

	// get session from shared session.
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Integer userID = user.getUserID();

	String fileDescription = learnerForm.getDescription();
	// reset fields and display a new form for next new file upload
	learnerForm.setDescription("");

	SubmitUser learner = getCurrentLearner(sessionID, submitFilesService);
	SubmitFilesContent content = submitFilesService.getSessionById(sessionID).getContent();
	try {
	    for (File file : files) {
		submitFilesService.uploadFileToSession(sessionID, file, fileDescription, userID);

		if (content.isNotifyTeachersOnFileSubmit()) {
		    String message = submitFilesService.getLocalisedMessage("event.file.submit.body",
			    new Object[] { learner.getFullName(), file.getName(), fileDescription });

		    submitFilesService.getEventNotificationService().notifyLessonMonitors(sessionID,
			    submitFilesService.getLocalisedMessage("event.file.submit.subject", new Object[] {}),
			    message, true);
		}
	    }
	} catch (SubmitFilesException e) {
	    MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	    logger.error("Error while uploading file", e);
	    errorMap.add("GLOBAL", messageService.getMessage("error.upload", new Object[] { e.getMessage() }));

	    request.setAttribute("errorMap", errorMap);
	    return "learner/sbmtlearner";
	} finally {
	    FileUtil.deleteTmpFileUploadDir(learnerForm.getTmpFileUploadId());
	}

	request.setAttribute(SbmtConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "learner/redirectAfterSubmit";
    }

    /**
     * Learner choose finish upload button, will invoke this function. This function will mark the <code>finished</code>
     * field by special toolSessionID and userID.
     */
    @RequestMapping("/finish")
    public String finish(HttpServletRequest request, HttpServletResponse response) {
	String sessionMapID = WebUtil.readStrParam(request, SbmtConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(SbmtConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long sessionID = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	if (mode == ToolAccessMode.LEARNER || mode.equals(ToolAccessMode.AUTHOR)) {
	    ToolSessionManager sessionMgrService = (ToolSessionManager) submitFilesService;
	    // get back login user DTO
	    // get session from shared session.
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Integer userID = user.getUserID();
	    submitFilesService.finishSubmission(sessionID, userID);

	    String nextActivityUrl;
	    try {
		nextActivityUrl = sessionMgrService.leaveToolSession(sessionID, new Long(userID.intValue()));
		return "redirect:" + nextActivityUrl;
	    } catch (DataMissingException e) {
		throw new SubmitFilesException(e);
	    } catch (ToolException e) {
		throw new SubmitFilesException(e);
	    }
	}
	return null;
    }

    // **********************************************************************************************
    // Private mehtods
    // **********************************************************************************************

    // validate uploaded form
    private boolean validateUploadForm(LearnerForm learnerForm, File[] files, HttpServletRequest request) {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	if (StringUtils.isBlank(learnerForm.getDescription())) {
	    errorMap.add("GLOBAL", messageService.getMessage("label.learner.fileDescription"));
	} else if (learnerForm.getDescription().length() > LearnerForm.DESCRIPTION_LENGTH) {
	    errorMap.add("GLOBAL", messageService.getMessage("errors.maxdescsize"));
	}

	if (files == null) {
	    errorMap.add("GLOBAL", messageService.getMessage("learner.form.filepath.displayname"));
	} else {
	    for (File file : files) {
		boolean fileSizeValid = FileValidatorUtil.validateFileSize(file.length(), false);
		if (!fileSizeValid) {
		    errorMap.add("GLOBAL", messageService.getMessage("errors.maxfilesize",
			    new Object[] { Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE) }));
		}

		if (FileUtil.isExecutableFile(file.getName())) {
		    logger.debug("File is executatable : " + file.getName());
		    errorMap.add("GLOBAL", messageService.getMessage("error.attachment.executable"));
		}

		if (!errorMap.isEmpty()) {
		    break;
		}
	    }
	}

	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    return true;
	}
	return false;
    }

    /**
     *
     * Set information into learner DTO object for page display. Fill file list uploaded by the special user into web
     * form. Remove the unauthorized mark and comments.
     */
    private void setLearnerDTO(HttpServletRequest request, SessionMap<String, Object> sessionMap, SubmitUser currUser,
	    List<FileDetailsDTO> filesUploaded, ToolAccessMode mode) {

	SubmitUserDTO dto = new SubmitUserDTO(currUser);
	if (currUser != null) {
	    // if Monitoring does not release marks, then skip this mark and comment content.
	    if (filesUploaded != null) {
		Iterator<FileDetailsDTO> iter = filesUploaded.iterator();
		while (iter.hasNext()) {
		    FileDetailsDTO filedto = iter.next();
		    if (mode.isTeacher() || currUser.getUid().equals(filedto.getOwner().getUserUid())) {
			filedto.setCurrentLearner(true);
		    } else {
			filedto.setCurrentLearner(false);
		    }
		}
	    }
	    dto.setFilesUploaded(filesUploaded);
	}

	// preset
	// Monitor can edit the activity and set a limit / decreased the limit with
	// the learner having already uploaded more files so ensure code handles that case.
	boolean limitUpload = (Boolean) sessionMap.get(SbmtConstants.ATTR_IS_MAX_LIMIT_UPLOAD_ENABLED);
	if (limitUpload && filesUploaded != null) {
	    int maxLimit = (Integer) sessionMap.get(SbmtConstants.ATTR_MAX_LIMIT_UPLOAD_NUMBER);
	    if (maxLimit <= filesUploaded.size()) {
		sessionMap.put(SbmtConstants.ATTR_MAX_LIMIT_REACHED, Boolean.TRUE);
	    }
	}

	// retrieve notebook reflection entry.
	NotebookEntry notebookEntry = submitFilesService.getEntry(
		(Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID), CoreNotebookConstants.NOTEBOOK_TOOL,
		SbmtConstants.TOOL_SIGNATURE, currUser.getUserID());

	if (notebookEntry != null) {
	    dto.setReflect(notebookEntry.getEntry());
	}

	request.setAttribute("learner", dto);
    }

    private SubmitUser getCurrentLearner(Long sessionID, ISubmitFilesService submitFilesService) {
	// get session from shared session.
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Integer userID = user.getUserID();

	SubmitUser learner = submitFilesService.getSessionUser(sessionID, userID);
	if (learner == null) {
	    learner = submitFilesService.createSessionUser(user, sessionID);
	}

	return learner;
    }

    @RequestMapping("/deleteLearnerFile")
    public void deleteLearnerFile(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	UserDTO currentUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	Long detailID = WebUtil.readLongParam(request, "detailId");

	FileDetailsDTO fileDetail = submitFilesService.getFileDetails(detailID, request.getLocale());

	if (fileDetail.getOwner().getUserID().equals(currentUser.getUserID())
		&& (StringUtils.isBlank(fileDetail.getMarks()))) {

	    submitFilesService.removeLearnerFile(detailID, null);

	} else {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not allowed to delete this item");
	}
    }

    /**
     * Display empty reflection form.
     */
    @RequestMapping("/newReflection")
    public String newReflection(@ModelAttribute("refForm") ReflectionForm refForm, HttpServletRequest request,
	    HttpServletResponse response) {

//		ISubmitFilesService submitFilesService = getService();
//		ActionErrors errors = validateBeforeFinish(request,submitFilesService);
//		if(!errors.isEmpty()){
//			this.addErrors(request,errors);
//			return mapping.getInputForward();
//		}

	//get session value
	String sessionMapID = WebUtil.readStrParam(request, SbmtConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(SbmtConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry

	SessionMap<String, Object> map = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = submitFilesService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		SbmtConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	return "learner/notebook";
    }

    /**
     * Submit reflection form input database.
     */
    @RequestMapping("/submitReflection")
    public String submitReflection(@ModelAttribute("refForm") ReflectionForm refForm, HttpServletRequest request,
	    HttpServletResponse response) {
	Integer userId = refForm.getUserID();

	String sessionMapID = WebUtil.readStrParam(request, SbmtConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(SbmtConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// check for existing notebook entry
	NotebookEntry entry = submitFilesService.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		SbmtConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    submitFilesService.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    SbmtConstants.TOOL_SIGNATURE, userId, refForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(refForm.getEntryText());
	    entry.setLastModified(new Date());
	    submitFilesService.updateEntry(entry);
	}

	return finish(request, response);
    }

    public void validateBeforeFinish(HttpServletRequest request, ISubmitFilesService submitFilesService) {
	String sessionMapID = WebUtil.readStrParam(request, SbmtConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Integer userID = user.getUserID();
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	List<FileDetailsDTO> list = submitFilesService.getFilesUploadedByUser(userID, sessionId, request.getLocale(),
		false);
	int minUpload = (Integer) sessionMap.get(SbmtConstants.PARAM_MIN_UPLOAD);
	if (minUpload > 0) {
	    errorMap.add("GLOBAL", messageService.getMessage("error.learning.minimum.upload.number.less"));
	}
    }
}