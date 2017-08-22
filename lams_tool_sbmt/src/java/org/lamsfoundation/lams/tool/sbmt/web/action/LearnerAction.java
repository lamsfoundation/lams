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



package org.lamsfoundation.lams.tool.sbmt.web.action;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.sbmt.SbmtConstants;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.SubmitUser;
import org.lamsfoundation.lams.tool.sbmt.dto.FileDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.SubmitUserDTO;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;
import org.lamsfoundation.lams.tool.sbmt.util.SubmitFilesException;
import org.lamsfoundation.lams.tool.sbmt.web.form.LearnerForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.filter.LocaleFilter;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * @author Manpreet Minhas
 * @author Steve.Ni
 */
public class LearnerAction extends DispatchAction {

    private static final boolean MODE_OPTIONAL = false;

    public static Logger logger = Logger.getLogger(LearnerAction.class);
    public ISubmitFilesService submitFilesService;

    /**
     * The initial page of learner in Submission tool. This page will list all uploaded files and learn
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// initial session Map
	SessionMap sessionMap = new SessionMap();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(SbmtConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	((LearnerForm) form).setSessionMapID(sessionMap.getSessionID());

	// get parameters from Request
	ToolAccessMode mode = null;
	try {
	    mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, LearnerAction.MODE_OPTIONAL);
	} catch (Exception e) {
	}
	if (mode == null) {
	    mode = ToolAccessMode.LEARNER;
	}

	Long sessionID = new Long(request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID));

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

	ISubmitFilesService submitFilesService = getService();
	SubmitFilesSession session = submitFilesService.getSessionById(sessionID);
	SubmitFilesContent content = session.getContent();

	// this must before getFileUploadByUser() method becuase getCurrentLearner()
	// will create session user if it does not exist.
	SubmitUser learner = getCurrentLearner(sessionID, submitFilesService);
	List filesUploaded = submitFilesService.getFilesUploadedByUser(userID, sessionID, request.getLocale(), false);

	// check whehter finish lock is on/off
	boolean lock = content.isLockOnFinished() && learner.isFinished();

	sessionMap.put(AttributeNames.PARAM_MODE, mode);
	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, sessionID);
	sessionMap.put(SbmtConstants.ATTR_FINISH_LOCK, lock);
	sessionMap.put(SbmtConstants.ATTR_LOCK_ON_FINISH, content.isLockOnFinished());
	sessionMap.put(SbmtConstants.ATTR_REFLECTION_ON, content.isReflectOnActivity());
	sessionMap.put(SbmtConstants.ATTR_REFLECTION_INSTRUCTION, content.getReflectInstructions());
	sessionMap.put(SbmtConstants.ATTR_TITLE, content.getTitle());
	sessionMap.put(SbmtConstants.ATTR_INSTRUCTION, content.getInstruction());
	sessionMap.put(SbmtConstants.ATTR_LIMIT_UPLOAD, content.isLimitUpload());
	sessionMap.put(SbmtConstants.ATTR_LIMIT_UPLOAD_NUMBER, content.getLimitUploadNumber());
	sessionMap.put(SbmtConstants.ATTR_USER_FINISHED, learner.isFinished());

	sessionMap.put(SbmtConstants.ATTR_UPLOAD_MAX_FILE_SIZE,
		FileValidatorUtil.formatSize(Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)));
	setLearnerDTO(request, sessionMap, learner, filesUploaded, mode);

	// if content in use, return special page.
	if (content.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set contentInUse flag to true!
	content.setContentInUse(true);
	content.setDefineLater(false);
	submitFilesService.saveOrUpdateContent(content);

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(sessionID, request,
		getServlet().getServletContext());

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
		return mapping.findForward("submissionDeadline");
	    }
	}

	if (content.isNotifyLearnersOnMarkRelease()) {
	    boolean isHtmlFormat = false;
	    submitFilesService.getEventNotificationService().createEvent(SbmtConstants.TOOL_SIGNATURE,
		    SbmtConstants.EVENT_NAME_NOTIFY_LEARNERS_ON_MARK_RELEASE, content.getContentID(),
		    submitFilesService.getLocalisedMessage("event.mark.release.subject", null),
		    submitFilesService.getLocalisedMessage("event.mark.release.body", null), isHtmlFormat);

	    submitFilesService.getEventNotificationService().subscribe(SbmtConstants.TOOL_SIGNATURE,
		    SbmtConstants.EVENT_NAME_NOTIFY_LEARNERS_ON_MARK_RELEASE, content.getContentID(), userID,
		    IEventNotificationService.DELIVERY_METHOD_MAIL);
	}

	return mapping.findForward(SbmtConstants.SUCCESS);
    }

    /**
     * Implements learner upload submission function. This function also display the page again for learner uploading
     * more submission use.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward uploadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	LearnerForm learnerForm = (LearnerForm) form;
	String sessionMapID = learnerForm.getSessionMapID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(SbmtConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	// set the mode into http session
	Long sessionID = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(sessionID, request,
		getServlet().getServletContext());

	if (validateUploadForm(learnerForm, request)) {
	    // get session from shared session.
	    HttpSession ss = SessionManager.getSession();
	    // get back login user DTO
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Integer userID = user.getUserID();

	    ISubmitFilesService submitFilesService = getService();
	    List filesUploaded = submitFilesService.getFilesUploadedByUser(userID, sessionID, request.getLocale(),
		    false);

	    SubmitUser learner = getCurrentLearner(sessionID, submitFilesService);
	    ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	    setLearnerDTO(request, sessionMap, learner, filesUploaded, mode);

	    return mapping.getInputForward();
	}

	// get session from shared session.
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Integer userID = user.getUserID();

	FormFile uploadedFile = learnerForm.getFile();
	String fileDescription = learnerForm.getDescription();
	// reset fields and display a new form for next new file upload
	learnerForm.setDescription("");

	ISubmitFilesService submitFilesService = getService();

	submitFilesService.uploadFileToSession(sessionID, uploadedFile, fileDescription, userID);
	List filesUploaded = submitFilesService.getFilesUploadedByUser(userID, sessionID, request.getLocale(), false);
	SubmitUser learner = getCurrentLearner(sessionID, submitFilesService);
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	setLearnerDTO(request, sessionMap, learner, filesUploaded, mode);

	SubmitFilesContent content = submitFilesService.getSessionById(sessionID).getContent();
	if (content.isNotifyTeachersOnFileSubmit()) {

	    String message = submitFilesService.getLocalisedMessage("event.file.submit.body",
		    new Object[] { learner.getFullName() });
	    submitFilesService.getEventNotificationService().notifyLessonMonitors(sessionID, message, false);
	}

	return mapping.getInputForward();
    }

    /**
     * Learner choose finish upload button, will invoke this function. This function will mark the <code>finished</code>
     * field by special toolSessionID and userID.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	String sessionMapID = WebUtil.readStrParam(request, SbmtConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(SbmtConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long sessionID = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	if (mode == ToolAccessMode.LEARNER || mode.equals(ToolAccessMode.AUTHOR)) {
	    ToolSessionManager sessionMgrService = SubmitFilesServiceProxy
		    .getToolSessionManager(getServlet().getServletContext());
	    ISubmitFilesService submitFilesService = getService();

	    // get back login user DTO
	    // get session from shared session.
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Integer userID = user.getUserID();
	    submitFilesService.finishSubmission(sessionID, userID);

	    String nextActivityUrl;
	    try {
		nextActivityUrl = sessionMgrService.leaveToolSession(sessionID, new Long(userID.intValue()));
		response.sendRedirect(nextActivityUrl);
	    } catch (DataMissingException e) {
		throw new SubmitFilesException(e);
	    } catch (ToolException e) {
		throw new SubmitFilesException(e);
	    } catch (IOException e) {
		throw new SubmitFilesException(e);
	    }
	}
	return null;

    }

    // **********************************************************************************************
    // Private mehtods
    // **********************************************************************************************
    private ISubmitFilesService getService() {
	ISubmitFilesService submitFilesService = SubmitFilesServiceProxy
		.getSubmitFilesService(this.getServlet().getServletContext());
	return submitFilesService;
    }

    // validate uploaded form
    private boolean validateUploadForm(LearnerForm learnerForm, HttpServletRequest request) {
	ActionMessages errors = new ActionMessages();
	Locale preferredLocale = (Locale) request.getSession().getAttribute(LocaleFilter.PREFERRED_LOCALE_KEY);
	if (learnerForm.getFile() == null || StringUtils.isBlank(learnerForm.getFile().getFileName())) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.required",
		    this.getResources(request).getMessage(preferredLocale, "learner.form.filepath.displayname")));
	}
	if (StringUtils.isBlank(learnerForm.getDescription())) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.required",
		    this.getResources(request).getMessage(preferredLocale, "label.learner.fileDescription")));
	} else if (learnerForm.getDescription().length() > LearnerForm.DESCRIPTION_LENGTH) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage("errors.maxdescsize", LearnerForm.DESCRIPTION_LENGTH));
	}

	FileValidatorUtil.validateFileSize(learnerForm.getFile(), false, errors);

	if (learnerForm.getFile() != null) {
	    LearnerAction.logger.debug("Learner submit file : " + learnerForm.getFile().getFileName());
	}

	if (learnerForm.getFile() != null && FileUtil.isExecutableFile(learnerForm.getFile().getFileName())) {
	    LearnerAction.logger.debug("File is executatable : " + learnerForm.getFile().getFileName());
	    ActionMessage msg = new ActionMessage("error.attachment.executable");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, msg);
	}

	if (!errors.isEmpty()) {
	    this.addErrors(request, errors);
	    return true;
	}
	return false;
    }

    /**
     *
     * Set information into learner DTO object for page display. Fill file list uploaded by the special user into web
     * form. Remove the unauthorized mark and comments.
     *
     * @param request
     * @param sessionMap
     * @param sessionID
     * @param userID
     * @param content
     * @param filesUploaded
     */
    private void setLearnerDTO(HttpServletRequest request, SessionMap sessionMap, SubmitUser currUser,
	    List filesUploaded, ToolAccessMode mode) {

	SubmitUserDTO dto = new SubmitUserDTO(currUser);
	if (currUser != null) {
	    // if Monitoring does not release marks, then skip this mark and comment content.
	    if (filesUploaded != null) {
		Iterator iter = filesUploaded.iterator();
		while (iter.hasNext()) {
		    FileDetailsDTO filedto = (FileDetailsDTO) iter.next();
		    if (mode.isTeacher() || currUser.getUid().equals(filedto.getOwner().getUserUid())) {
			filedto.setCurrentLearner(true);
		    } else {
			filedto.setCurrentLearner(false);
		    }
//		    if (filedto.getDateMarksReleased() == null) {
//			filedto.setComments(null);
//			filedto.setMarks(null);
//		    }
		}
	    }
	    dto.setFilesUploaded(filesUploaded);
	}

	// preset
	boolean limitUpload = (Boolean) sessionMap.get(SbmtConstants.ATTR_LIMIT_UPLOAD);
	if (limitUpload && filesUploaded != null) {
	    int limit = (Integer) sessionMap.get(SbmtConstants.ATTR_LIMIT_UPLOAD_NUMBER);
	    if (limit == filesUploaded.size()) {
		sessionMap.put(SbmtConstants.ATTR_ARRIVE_LIMIT, true);
	    }
	    int limitUploadLeft = limit - filesUploaded.size();
	    dto.setLimitUploadLeft(limitUploadLeft);
	}

	// retrieve notebook reflection entry.
	ISubmitFilesService submitFilesService = getService();

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

    private ISubmitFilesService getSubmitFilesService() {
	return SubmitFilesServiceProxy.getSubmitFilesService(this.getServlet().getServletContext());
    }

    public ActionForward deleteLearnerFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	HttpSession ss = SessionManager.getSession();
	

	UserDTO currentUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	Long detailID = WebUtil.readLongParam(request, "detailId");

	if (submitFilesService == null) {
	    submitFilesService = getSubmitFilesService();
	}
	FileDetailsDTO fileDetail = submitFilesService.getFileDetails(detailID, request.getLocale());
	
	if (fileDetail.getOwner().getUserID().equals(currentUser.getUserID()) && (StringUtils.isBlank(fileDetail.getMarks()))) {

	    submitFilesService.removeLearnerFile(detailID,null);

	} else {
	  response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not allowed to delete this item");
	}
	return null;
    }

}
