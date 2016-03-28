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
package org.lamsfoundation.lams.tool.qa.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.model.LearnerItemRatingCriteria;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.service.IRatingService;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaCondition;
import org.lamsfoundation.lams.tool.qa.QaConfigItem;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.QaWizardCategory;
import org.lamsfoundation.lams.tool.qa.dao.IQaConfigItemDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaContentDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaQueUsrDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaQuestionDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaSessionDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaUsrRespDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaWizardDAO;
import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;
import org.lamsfoundation.lams.tool.qa.util.QaApplicationException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.dao.DataAccessException;

/**
 * The POJO implementation of Qa service. All business logics of Qa tool are implemented in this class. It translate the
 * request from presentation layer and perform approporiate database operation.
 * 
 * @author Ozgur Demirtas
 */
public class QaServicePOJO
	implements IQaService, ToolContentManager, ToolSessionManager, ToolRestManager, QaAppConstants {
    private static Logger logger = Logger.getLogger(QaServicePOJO.class.getName());

    private IQaContentDAO qaDAO;
    private IQaQuestionDAO qaQuestionDAO;

    private IQaSessionDAO qaSessionDAO;
    private IQaQueUsrDAO qaQueUsrDAO;
    private IQaUsrRespDAO qaUsrRespDAO;

    private IToolContentHandler qaToolContentHandler = null;
    private IUserManagementService userManagementService;
    private ILamsToolService toolService;
    private ILearnerService learnerService;
    private IAuditService auditService;
    private IExportToolContentService exportContentService;
    private QaOutputFactory qaOutputFactory;
    private IQaConfigItemDAO qaConfigItemDAO;
    private IQaWizardDAO qaWizardDAO;

    private ICoreNotebookService coreNotebookService;
    private IRatingService ratingService;
    private IEventNotificationService eventNotificationService;
    private MessageService messageService;

    private Random generator = new Random();

    @Override
    public boolean isUserGroupLeader(QaQueUsr user, Long toolSessionId) {

	QaSession session = this.getSessionById(toolSessionId);
	QaQueUsr groupLeader = session.getGroupLeader();

	boolean isUserLeader = (groupLeader != null) && user.getUid().equals(groupLeader.getUid());
	return isUserLeader;
    }

    @Override
    public QaQueUsr checkLeaderSelectToolForSessionLeader(QaQueUsr user, Long toolSessionId) {
	if ((user == null) || (toolSessionId == null)) {
	    return null;
	}

	QaSession qaSession = this.getSessionById(toolSessionId);
	QaQueUsr leader = qaSession.getGroupLeader();
	// check leader select tool for a leader only in case QA tool doesn't know it. As otherwise it will screw
	// up previous scratches done
	if (leader == null) {

	    Long leaderUserId = toolService.getLeaderUserId(toolSessionId, user.getQueUsrId().intValue());
	    if (leaderUserId != null) {
		leader = getUserByIdAndSession(leaderUserId, toolSessionId);

		// create new user in a DB
		if (leader == null) {
		    User leaderDto = (User) getUserManagementService().findById(User.class, leaderUserId.intValue());
		    String userName = leaderDto.getLogin();
		    String fullName = leaderDto.getFirstName() + " " + leaderDto.getLastName();
		    leader = new QaQueUsr(leaderUserId, userName, fullName, qaSession, new TreeSet());
		    qaQueUsrDAO.createUsr(user);
		}

		// set group leader
		qaSession.setGroupLeader(leader);
		this.updateSession(qaSession);
	    }
	}

	return leader;
    }

    @Override
    public void copyAnswersFromLeader(QaQueUsr user, QaQueUsr leader) {

	if ((user == null) || (leader == null) || user.getUid().equals(leader.getUid())) {
	    return;
	}

	for (QaUsrResp leaderResponse : (Set<QaUsrResp>) leader.getQaUsrResps()) {
	    QaQueContent question = leaderResponse.getQaQuestion();
	    QaUsrResp response = qaUsrRespDAO.getResponseByUserAndQuestion(user.getQueUsrId(), question.getUid());

	    // if response doesn't exist
	    if (response == null) {
		response = new QaUsrResp(leaderResponse.getAnswer(), leaderResponse.getAnswerAutosaved(),
			leaderResponse.getAttemptTime(), "", question, user, true);
		createUserResponse(response);

		// if it's been changed by the leader
	    } else if (leaderResponse.getAttemptTime().compareTo(response.getAttemptTime()) != 0) {
		response.setAnswer(leaderResponse.getAnswer());
		response.setAttemptTime(leaderResponse.getAttemptTime());
		response.setTimezone("");
		updateUserResponse(response);
	    }
	}
    }

    @Override
    public void setDefineLater(String strToolContentID, boolean value) {

	QaContent qaContent = getQaContent(new Long(strToolContentID).longValue());

	if (qaContent != null) {
	    qaContent.setDefineLater(value);
	    updateQaContent(qaContent);
	}
    }

    @Override
    public List<QaQueUsr> getUsersBySessionId(Long toolSessionID) {
	QaSession session = qaSessionDAO.getQaSessionById(toolSessionID);
	return qaQueUsrDAO.getUserBySessionOnly(session);
    }

    @Override
    public void createQaContent(QaContent qaContent) {
	qaDAO.saveQa(qaContent);
    }

    @Override
    public QaContent getQaContent(long toolContentID) {
	return qaDAO.getQaByContentId(toolContentID);
    }

    @Override
    public void saveOrUpdateQaContent(QaContent qa) {
	qaDAO.saveOrUpdateQa(qa);
    }

    @Override
    public QaQueContent getQuestionByContentAndDisplayOrder(Long displayOrder, Long contentUid) {
	return qaQuestionDAO.getQuestionByDisplayOrder(displayOrder, contentUid);
    }

    @Override
    public QaQueContent getQuestionByUid(Long questionUid) {
	if (questionUid == null) {
	    return null;
	}

	return qaQuestionDAO.getQuestionByUid(questionUid);
    }

    @Override
    public void saveOrUpdateQuestion(QaQueContent question) {
	qaQuestionDAO.saveOrUpdateQaQueContent(question);
    }

    @Override
    public void createQuestion(QaQueContent question) {
	qaQuestionDAO.createQueContent(question);
    }

    @Override
    public QaQueUsr createUser(Long toolSessionID, Integer userId) {
	User user = (User) userManagementService.findById(User.class, userId);
	String userName = user.getLogin();
	String fullName = user.getFirstName() + " " + user.getLastName();
	QaSession qaSession = getSessionById(toolSessionID.longValue());

	QaQueUsr qaUser = new QaQueUsr(userId.longValue(), userName, fullName, qaSession, new TreeSet());
	// make sure the user was not created in the meantime
	QaQueUsr existingUser = getUserByIdAndSession(userId.longValue(), toolSessionID);
	if (existingUser == null) {
	    qaQueUsrDAO.createUsr(qaUser);
	    return qaUser;
	} else {
	    return existingUser;
	}
    }

    @Override
    public QaQueUsr getUserByIdAndSession(final Long queUsrId, final Long qaSessionId) {
	return qaQueUsrDAO.getQaUserBySession(queUsrId, qaSessionId);
    }

    @Override
    public List<QaUsrResp> getResponsesByUserUid(final Long userUid) {
	return qaUsrRespDAO.getResponsesByUserUid(userUid);
    }

    @Override
    public QaUsrResp getResponseByUserAndQuestion(final Long queUsrId, final Long qaQueContentId) {
	return qaUsrRespDAO.getResponseByUserAndQuestion(queUsrId, qaQueContentId);
    }

    @Override
    public List<QaUsrResp> getResponseBySessionAndQuestion(final Long qaSessionId, final Long questionId) {
	return qaUsrRespDAO.getResponseBySessionAndQuestion(qaSessionId, questionId);
    }

    @Override
    public List<QaUsrResp> getResponsesForTablesorter(final Long toolContentId, final Long qaSessionId,
	    final Long questionId, final Long excludeUserId, int page, int size, int sorting, String searchString) {
	return qaUsrRespDAO.getResponsesForTablesorter(toolContentId, qaSessionId, questionId, excludeUserId, page,
		size, sorting, searchString);
    }

    @Override
    public int getCountResponsesBySessionAndQuestion(final Long qaSessionId, final Long questionId,
	    final Long excludeUserId, String searchString) {
	return qaUsrRespDAO.getCountResponsesBySessionAndQuestion(qaSessionId, questionId, excludeUserId, searchString);
    }

    @Override
    public void updateUserResponse(QaUsrResp resp) {
	qaUsrRespDAO.updateUserResponse(resp);
    }

    @Override
    public void updateResponseWithNewAnswer(String newAnswer, String toolSessionID, Long questionDisplayOrder, boolean isAutosave) {
	HttpSession ss = SessionManager.getSession();
	UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userId = new Long(toolUser.getUserID().longValue());
	QaQueUsr user = getUserByIdAndSession(userId, new Long(toolSessionID));

	QaSession session = getSessionById(new Long(toolSessionID));
	QaContent qaContent = session.getQaContent();

	QaQueContent question = getQuestionByContentAndDisplayOrder(new Long(questionDisplayOrder), qaContent.getUid());

	QaUsrResp response = getResponseByUserAndQuestion(user.getQueUsrId(), question.getUid());
	// if response doesn't exist
	if (response == null) {
	    response = isAutosave
		    ? new QaUsrResp(null, newAnswer, new Date(System.currentTimeMillis()), "", question, user, true)
		    : new QaUsrResp(newAnswer, null, new Date(System.currentTimeMillis()), "", question, user, true);	    
	    createUserResponse(response);

	// if answer has changed
	} else if (!newAnswer.equals(response.getAnswer())) {
	    if (isAutosave) {
		response.setAnswerAutosaved(newAnswer);
	    } else {
		response.setAnswer(newAnswer);
		response.setAnswerAutosaved(null);
	    }
	    
	    response.setAttemptTime(new Date(System.currentTimeMillis()));
	    response.setTimezone("");
	    updateUserResponse(response);
	}
    }

    @Override
    public void createUserResponse(QaUsrResp qaUsrResp) {
	qaUsrRespDAO.createUserResponse(qaUsrResp);
    }

    @Override
    public void updateUser(QaQueUsr qaQueUsr) {
	qaQueUsrDAO.updateUsr(qaQueUsr);
    }

    @Override
    public QaUsrResp getResponseById(Long responseId) {
	return qaUsrRespDAO.getResponseById(responseId);
    }

    @Override
    public QaSession getSessionById(long qaSessionId) {
	return qaSessionDAO.getQaSessionById(qaSessionId);
    }

    @Override
    public void updateQaContent(QaContent qa) {
	qaDAO.updateQa(qa);
    }

    @Override
    public void updateSession(QaSession qaSession) {
	qaSessionDAO.UpdateQaSession(qaSession);
    }

    @Override
    public void removeUserResponse(QaUsrResp resp) {
	auditService.logChange(QaAppConstants.MY_SIGNATURE, resp.getQaQueUser().getQueUsrId(),
		resp.getQaQueUser().getUsername(), resp.getAnswer(), null);
	qaUsrRespDAO.removeUserResponse(resp);
    }

    @Override
    public void updateResponseVisibility(Long responseUid, boolean isHideItem) {

	QaUsrResp response = getResponseById(responseUid);
	if (response != null) {
	    // createBy should be null for system default value.
	    Long userId = 0L;
	    String loginName = "No user";
	    if (response.getQaQueUser() != null) {
		userId = response.getQaQueUser().getQueUsrId();
		loginName = response.getQaQueUser().getUsername();
	    }
	    if (isHideItem) {
		auditService.logHideEntry(QaAppConstants.MY_SIGNATURE, userId, loginName, response.getAnswer());
	    } else {
		auditService.logShowEntry(QaAppConstants.MY_SIGNATURE, userId, loginName, response.getAnswer());
	    }
	    response.setVisible(!isHideItem);
	    updateUserResponse(response);
	}
    }

    @Override
    public List<QaQueContent> getAllQuestionEntries(final Long uid) {
	return qaQuestionDAO.getAllQuestionEntries(uid.longValue());
    }

    @Override
    public List<QaQueContent> getAllQuestionEntriesSorted(final long contentUid) {
	return qaQuestionDAO.getAllQuestionEntriesSorted(contentUid);
    }

    @Override
    public void removeQuestion(QaQueContent question) {
	qaQuestionDAO.removeQaQueContent(question);
    }

    @Override
    public boolean isStudentActivityOccurredGlobal(QaContent qaContent) {
	int countResponses = 0;
	if (qaContent != null) {
	    countResponses = qaUsrRespDAO.getCountResponsesByQaContent(qaContent.getQaContentId());
	}
	return countResponses > 0;
    }

    @Override
    public void recalculateUserAnswers(QaContent content, Set<QaQueContent> oldQuestions,
	    List<QaQuestionDTO> questionDTOs, List<QaQuestionDTO> deletedQuestions) {

	// create list of modified questions
	List<QaQuestionDTO> modifiedQuestions = new ArrayList<QaQuestionDTO>();
	for (QaQueContent oldQuestion : oldQuestions) {
	    for (QaQuestionDTO questionDTO : questionDTOs) {
		if (oldQuestion.getUid().equals(questionDTO.getUid())) {

		    // question is different
		    if (!oldQuestion.getQuestion().equals(questionDTO.getQuestion())) {
			modifiedQuestions.add(questionDTO);
		    }
		}
	    }
	}

	Set<QaSession> sessionList = content.getQaSessions();
	for (QaSession session : sessionList) {
	    Long toolSessionId = session.getQaSessionId();
	    Set<QaQueUsr> sessionUsers = session.getQaQueUsers();

	    for (QaQueUsr user : sessionUsers) {

		// get all finished user results
		List<QaUsrResp> userAttempts = qaUsrRespDAO.getResponsesByUserUid(user.getUid());
		Iterator<QaUsrResp> iter = userAttempts.iterator();
		while (iter.hasNext()) {
		    QaUsrResp resp = iter.next();

		    QaQueContent question = resp.getQaQuestion();

		    boolean isRemoveQuestionResult = false;

		    // [+] if the question is modified
		    for (QaQuestionDTO modifiedQuestion : modifiedQuestions) {
			if (question.getUid().equals(modifiedQuestion.getUid())) {
			    isRemoveQuestionResult = true;
			    break;
			}
		    }

		    // [+] if the question was removed
		    for (QaQuestionDTO deletedQuestion : deletedQuestions) {
			if (question.getUid().equals(deletedQuestion.getUid())) {
			    isRemoveQuestionResult = true;
			    break;
			}
		    }

		    if (isRemoveQuestionResult) {
			iter.remove();
			qaUsrRespDAO.removeUserResponse(resp);
		    }

		    // [+] doing nothing if the new question was added

		}

	    }
	}

    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	QaContent qaContent = qaDAO.getQaByContentId(toolContentId.longValue());
	if (qaContent == null) {
	    QaServicePOJO.logger.error("throwing DataMissingException: WARNING!: retrieved qaContent is null.");
	    throw new DataMissingException("qaContent is missing");
	}
	qaContent.setDefineLater(false);
	updateQaContent(qaContent);
    }

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) {
	long defaultContentId = 0;
	if (fromContentId == null) {
	    defaultContentId = getToolDefaultContentIdBySignature(QaAppConstants.MY_SIGNATURE);
	    fromContentId = new Long(defaultContentId);
	}

	if (toContentId == null) {
	    QaServicePOJO.logger.error("throwing ToolException: toContentId is null");
	    throw new ToolException("toContentId is missing");
	}

	QaContent fromContent = qaDAO.getQaByContentId(fromContentId.longValue());

	if (fromContent == null) {
	    defaultContentId = getToolDefaultContentIdBySignature(QaAppConstants.MY_SIGNATURE);
	    fromContentId = new Long(defaultContentId);

	    fromContent = qaDAO.getQaByContentId(fromContentId.longValue());
	}
	if (fromContentId.equals(defaultContentId) && (fromContent != null) && fromContent.getConditions().isEmpty()) {
	    fromContent.getConditions().add(getQaOutputFactory().createDefaultComplexUserAnswersCondition(fromContent));
	}
	QaContent toContent = QaContent.newInstance(fromContent, toContentId);
	if (toContent == null) {
	    QaServicePOJO.logger.error("throwing ToolException: WARNING!, retrieved toContent is null.");
	    throw new ToolException("WARNING! Fail to create toContent. Can't continue!");
	} else {
	    // save questions first, because if Hibernate decides to flush Conditions first,
	    // there is no cascade to questions and it may trigger an error
	    for (QaQueContent question : toContent.getQaQueContents()) {
		qaQuestionDAO.saveOrUpdateQaQueContent(question);
	    }
	    qaDAO.saveQa(toContent);
	}

    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	QaContent qaContent = qaDAO.getQaByContentId(toolContentId.longValue());
	if (qaContent == null) {
	    QaServicePOJO.logger.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (QaSession session : (Set<QaSession>) qaContent.getQaSessions()) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getQaSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, QaAppConstants.MY_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	qaDAO.removeQa(toolContentId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (QaServicePOJO.logger.isDebugEnabled()) {
	    QaServicePOJO.logger
		    .debug("Removing Q&A answers for user ID " + userId + " and toolContentId " + toolContentId);
	}

	QaContent content = qaDAO.getQaByContentId(toolContentId);
	if (content != null) {
	    for (QaSession session : (Set<QaSession>) content.getQaSessions()) {
		QaQueUsr user = qaQueUsrDAO.getQaUserBySession(userId.longValue(), session.getQaSessionId());
		if (user != null) {
		    for (QaUsrResp response : (Set<QaUsrResp>) user.getQaUsrResps()) {
			qaUsrRespDAO.removeUserResponse(response);
		    }

		    if ((session.getGroupLeader() != null) && session.getGroupLeader().getUid().equals(user.getUid())) {
			session.setGroupLeader(null);
		    }

		    qaQueUsrDAO.deleteQaQueUsr(user);

		    NotebookEntry entry = getEntry(session.getQaSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			    QaAppConstants.MY_SIGNATURE, userId);
		    if (entry != null) {
			qaDAO.delete(entry);
		    }
		}
	    }
	}
    }

    @Override
    public List<Object[]> getUserReflectionsForTablesorter(Long toolSessionId, int page, int size, int sorting,
	    String searchString) {
	return qaQueUsrDAO.getUserReflectionsForTablesorter(toolSessionId, page, size, sorting, searchString,
		getCoreNotebookService());
    }

    @Override
    public int getCountUsersBySessionWithSearch(Long toolSessionId, String searchString) {
	return qaQueUsrDAO.getCountUsersBySessionWithSearch(toolSessionId, searchString);
    }

    @Override
    public void notifyTeachersOnResponseSubmit(Long sessionId) {
	final String NEW_LINE_CHARACTER = "<br>";

	HttpSession ss = SessionManager.getSession();
	UserDTO userDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userId = new Long(userDto.getUserID().longValue());
	QaQueUsr user = getUserByIdAndSession(userId, new Long(sessionId));
	String fullName = user.getFullname();

	// add question-answer pairs to email message
	List<QaUsrResp> responses = qaUsrRespDAO.getResponsesByUserUid(user.getUid());
	Date attemptTime = new Date();
	String message = new String();
	for (QaUsrResp response : responses) {
	    String question = response.getQaQuestion().getQuestion();
	    String answer = response.getAnswer();

	    message += NEW_LINE_CHARACTER + NEW_LINE_CHARACTER + question + " " + answer;
	    attemptTime = response.getAttemptTime();
	}

	message = NEW_LINE_CHARACTER + NEW_LINE_CHARACTER
		+ messageService.getMessage("label.user.has.answered.questions", new Object[] { fullName, attemptTime })
		+ message + NEW_LINE_CHARACTER + NEW_LINE_CHARACTER;

	eventNotificationService.notifyLessonMonitors(sessionId, message, true);
    }

    @Override
    public void exportToolContent(Long toolContentID, String rootPath) {
	QaContent toolContentObj = qaDAO.getQaByContentId(toolContentID);
	if (toolContentObj == null) {
	    long defaultToolContentId = toolService.getToolDefaultContentIdBySignature(QaAppConstants.MY_SIGNATURE);
	    toolContentObj = getQaContent(defaultToolContentId);
	    if ((toolContentObj != null) && toolContentObj.getConditions().isEmpty()) {
		toolContentObj.getConditions()
			.add(getQaOutputFactory().createDefaultComplexUserAnswersCondition(toolContentObj));
	    }
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the question and answer tool");
	}

	try {
	    // set ToolContentHandler as null to avoid copy file node in
	    // repository again.
	    toolContentObj = QaContent.newInstance(toolContentObj, toolContentID);

	    // don't export following fields value
	    toolContentObj.setQaSessions(null);
	    Set<QaQueContent> questions = toolContentObj.getQaQueContents();
	    for (QaQueContent question : questions) {
		question.setQaContent(null);
	    }
	    for (LearnerItemRatingCriteria criteria : toolContentObj.getRatingCriterias()) {
		criteria.setToolContentId(null);
	    }

	    exportContentService.exportToolContent(toolContentID, toolContentObj, qaToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentID, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(QaImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, qaToolContentHandler, fromVersion,
		    toVersion);
	    if (!(toolPOJO instanceof QaContent)) {
		throw new ImportToolContentException(
			"Import QA tool content failed. Deserialized object is " + toolPOJO);
	    }
	    QaContent toolContentObj = (QaContent) toolPOJO;

	    // reset it to new toolContentID
	    toolContentObj.setQaContentId(toolContentID);
	    toolContentObj.setCreatedBy(newUserUid);
	    for (LearnerItemRatingCriteria criteria : toolContentObj.getRatingCriterias()) {
		criteria.setToolContentId(toolContentID);
	    }
	    
	    // set back the tool content
	    Set<QaQueContent> questions = toolContentObj.getQaQueContents();
	    for (QaQueContent question : questions) {
		question.setQaContent(toolContentObj);
	    }
	    qaDAO.saveOrUpdateQa(toolContentObj);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	QaContent qaContent = qaDAO.getQaByContentId(toolContentId);
	if (qaContent == null) {
	    long defaultToolContentId = toolService.getToolDefaultContentIdBySignature(QaAppConstants.MY_SIGNATURE);
	    qaContent = getQaContent(defaultToolContentId);
	    if ((qaContent != null) && qaContent.getConditions().isEmpty()) {
		qaContent.getConditions().add(getQaOutputFactory().createDefaultComplexUserAnswersCondition(qaContent));
	    }
	}
	return getQaOutputFactory().getToolOutputDefinitions(qaContent, definitionType);
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return qaDAO.getQaByContentId(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return qaDAO.getQaByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	QaContent content = qaDAO.getQaByContentId(toolContentId);
	for (QaSession session : (Set<QaSession>) content.getQaSessions()) {
	    if (!session.getQaQueUsers().isEmpty()) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentID) throws ToolException {

	if (toolSessionId == null) {
	    QaServicePOJO.logger.error("toolSessionId is null");
	    throw new ToolException("toolSessionId is missing");
	}

	QaContent qaContent = qaDAO.getQaByContentId(toolContentID.longValue());

	/*
	 * create a new a new tool session if it does not already exist in the tool session table
	 */
	QaSession qaSession = getSessionById(toolSessionId);
	if (qaSession == null) {
	    try {
		qaSession = new QaSession(toolSessionId, new Date(System.currentTimeMillis()), QaSession.INCOMPLETE,
			toolSessionName, qaContent, new TreeSet());
		qaSessionDAO.createSession(qaSession);
	    } catch (Exception e) {
		QaServicePOJO.logger.error("Error creating new toolsession in the db");
		throw new ToolException("Error creating new toolsession in the db: " + e);
	    }
	}
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    QaServicePOJO.logger.error("toolSessionId is null");
	    throw new DataMissingException("toolSessionId is missing");
	}

	QaSession qaSession = null;
	try {
	    qaSession = getSessionById(toolSessionId.longValue());
	} catch (QaApplicationException e) {
	    throw new DataMissingException("error retrieving qaSession: " + e);
	} catch (Exception e) {
	    throw new ToolException("error retrieving qaSession: " + e);
	}

	if (qaSession == null) {
	    QaServicePOJO.logger.error("qaSession is null");
	    throw new DataMissingException("qaSession is missing");
	}

	try {
	    qaSessionDAO.deleteQaSession(qaSession);
	} catch (QaApplicationException e) {
	    throw new ToolException("error deleting qaSession:" + e);
	}
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {

	if (toolSessionId == null) {
	    QaServicePOJO.logger.error("toolSessionId is null");
	    throw new DataMissingException("toolSessionId is missing");
	}

	if (learnerId == null) {
	    QaServicePOJO.logger.error("learnerId is null");
	    throw new DataMissingException("learnerId is missing");
	}

	QaSession qaSession = getSessionById(toolSessionId.longValue());
	qaSession.setSession_end_date(new Date(System.currentTimeMillis()));
	qaSession.setSession_status(QaAppConstants.COMPLETED);
	updateSession(qaSession);

	try {
	    String nextUrl = learnerService.completeToolSession(toolSessionId, learnerId);
	    return nextUrl;
	} catch (DataAccessException e) {
	    throw new ToolException("Exception occured when user is leaving tool session: " + e);
	}

    }

    @Override
    public List<RatingCriteria> getRatingCriterias(Long toolContentId) {
	return ratingService.getCriteriasByToolContentId(toolContentId);
    }

    @Override
    public void saveRatingCriterias(HttpServletRequest request, Collection<RatingCriteria> oldCriterias,
	    Long toolContentId) {
	ratingService.saveRatingCriterias(request, oldCriterias, toolContentId);
    }

    @Override
    public boolean isCommentsEnabled(Long toolContentId) {
	return ratingService.isCommentsEnabled(toolContentId);
    }

    @Override
    public List<ItemRatingDTO> getRatingCriteriaDtos(Long contentId, Collection<Long> itemIds,
	    boolean isCommentsByOtherUsersRequired, Long userId) {
	return ratingService.getRatingCriteriaDtos(contentId, itemIds, isCommentsByOtherUsersRequired, userId);
    }

    @Override
    public int getCountItemsRatedByUser(Long toolContentId, Integer userId) {
	return ratingService.getCountItemsRatedByUser(toolContentId, userId);
    }

    /**
     * ToolSessionManager CONTRACT
     */
    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
	    throws DataMissingException, ToolException {
	throw new ToolException("not yet implemented");
    }

    /**
     * ToolSessionManager CONTRACT
     */
    @Override
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds)
	    throws DataMissingException, ToolException {
	throw new ToolException("not yet implemented");
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getQaOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getQaOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	Long userId = user.getUserId().longValue();

	QaSession session = getSessionById(toolSessionId);
	if ((session == null) || (session.getQaContent() == null)) {
	    return;
	}
	QaContent content = session.getQaContent();

	// copy answers only in case leader aware feature is ON
	if (content.isUseSelectLeaderToolOuput()) {

	    QaQueUsr qaUser = getUserByIdAndSession(userId, toolSessionId);
	    // create user if he hasn't accessed this activity yet
	    if (qaUser == null) {

		String userName = user.getLogin();
		String fullName = user.getFirstName() + " " + user.getLastName();
		qaUser = new QaQueUsr(userId, userName, fullName, session, new TreeSet());
		qaQueUsrDAO.createUsr(qaUser);
	    }

	    QaQueUsr groupLeader = session.getGroupLeader();

	    // check if leader has submitted answers
	    if ((groupLeader != null) && groupLeader.isResponseFinalized()) {

		// we need to make sure specified user has the same scratches as a leader
		copyAnswersFromLeader(qaUser, groupLeader);
	    }

	}

    }

    @Override
    public IToolVO getToolBySignature(String toolSignature) {
	IToolVO tool = toolService.getToolBySignature(toolSignature);
	return tool;
    }

    @Override
    public long getToolDefaultContentIdBySignature(String toolSignature) {
	long contentId = 0;
	contentId = toolService.getToolDefaultContentIdBySignature(toolSignature);
	return contentId;
    }

    @Override
    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry) {
	return coreNotebookService.createNotebookEntry(id, idType, signature, userID, "", entry);
    }

    @Override
    public NotebookEntry getEntry(Long id, Integer idType, String signature, Integer userID) {

	List<NotebookEntry> list = coreNotebookService.getEntry(id, idType, signature, userID);
	if ((list == null) || list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    @Override
    public String getLearnerContentFolder(Long toolSessionId, Long userId) {
	return toolService.getLearnerContentFolder(toolSessionId, userId);
    }

    /**
     * @return Returns the userManagementService.
     */
    public IUserManagementService getUserManagementService() {
	return userManagementService;
    }

    public void setLearnerService(ILearnerService learnerService) {
	this.learnerService = learnerService;
    }

    public void setQaDAO(IQaContentDAO qaDAO) {
	this.qaDAO = qaDAO;
    }

    public void setQaQuestionDAO(IQaQuestionDAO qaQuestionDAO) {
	this.qaQuestionDAO = qaQuestionDAO;
    }

    public void setQaSessionDAO(IQaSessionDAO qaSessionDAO) {
	this.qaSessionDAO = qaSessionDAO;
    }

    public void setQaQueUsrDAO(IQaQueUsrDAO qaQueUsrDAO) {
	this.qaQueUsrDAO = qaQueUsrDAO;
    }

    public void setQaUsrRespDAO(IQaUsrRespDAO qaUsrRespDAO) {
	this.qaUsrRespDAO = qaUsrRespDAO;
    }

    /**
     * @return Returns the qaDAO.
     */
    public IQaContentDAO getQaDAO() {
	return qaDAO;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    /**
     * @param qaToolContentHandler
     *            The qaToolContentHandler to set.
     */
    public void setQaToolContentHandler(IToolContentHandler qaToolContentHandler) {
	this.qaToolContentHandler = qaToolContentHandler;
    }

    @Override
    public IAuditService getAuditService() {
	return auditService;
    }

    public void setAuditService(IAuditService auditService) {
	this.auditService = auditService;
    }

    public IExportToolContentService getExportContentService() {
	return exportContentService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    // =========================================================================================
    /**
     * @return Returns the coreNotebookService.
     */
    public ICoreNotebookService getCoreNotebookService() {
	return coreNotebookService;
    }

    /**
     * @param coreNotebookService
     *            The coreNotebookService to set.
     */
    public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
	this.coreNotebookService = coreNotebookService;
    }

    public void setRatingService(IRatingService ratingService) {
	this.ratingService = ratingService;
    }

    public void setEventNotificationService(IEventNotificationService eventNotificationService) {
	this.eventNotificationService = eventNotificationService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    @Override
    public void updateEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }

    public QaOutputFactory getQaOutputFactory() {
	return qaOutputFactory;
    }

    public void setQaOutputFactory(QaOutputFactory qaOutputFactory) {
	this.qaOutputFactory = qaOutputFactory;
    }

    public IQaConfigItemDAO getQaConfigItemDAO() {
	return qaConfigItemDAO;
    }

    public void setQaConfigItemDAO(IQaConfigItemDAO qaConfigItemDAO) {
	this.qaConfigItemDAO = qaConfigItemDAO;
    }

    public IQaWizardDAO getQaWizardDAO() {
	return qaWizardDAO;
    }

    public void setQaWizardDAO(IQaWizardDAO qaWizardDAO) {
	this.qaWizardDAO = qaWizardDAO;
    }

    @Override
    public QaContent getQaContentBySessionId(Long sessionId) {
	QaSession session = qaSessionDAO.getQaSessionById(sessionId);
	// to skip CGLib problem
	Long contentId = session.getQaContent().getQaContentId();
	QaContent qaContent = qaDAO.getQaByContentId(contentId);
	return qaContent;
    }

    @Override
    public String createConditionName(Collection<QaCondition> existingConditions) {
	String uniqueNumber = null;
	do {
	    uniqueNumber = String.valueOf(Math.abs(generator.nextInt()));
	    for (QaCondition condition : existingConditions) {
		String[] splitedName = getQaOutputFactory().splitConditionName(condition.getName());
		if (uniqueNumber.equals(splitedName[1])) {
		    uniqueNumber = null;
		}
	    }
	} while (uniqueNumber == null);
	return getQaOutputFactory().buildUserAnswersConditionName(uniqueNumber);
    }

    @Override
    public void deleteCondition(QaCondition condition) {
	if ((condition != null) && (condition.getConditionId() != null)) {
	    qaDAO.deleteCondition(condition);
	}
    }

    @Override
    public QaCondition createDefaultComplexCondition(QaContent qaContent) {
	return getQaOutputFactory().createDefaultComplexUserAnswersCondition(qaContent);
    }

    @Override
    public QaConfigItem getConfigItem(String configKey) {
	return qaConfigItemDAO.getConfigItemByKey(configKey);
    }

    @Override
    public void saveOrUpdateConfigItem(QaConfigItem configItem) {
	qaConfigItemDAO.saveOrUpdate(configItem);
    }

    @Override
    public SortedSet<QaWizardCategory> getWizardCategories() {
	return qaWizardDAO.getWizardCategories();
    }

    @Override
    public void saveOrUpdateQaWizardCategories(SortedSet<QaWizardCategory> categories) {
	qaWizardDAO.saveOrUpdateCategories(categories);
    }

    @Override
    public void deleteWizardCategoryByUID(Long uid) {
	qaWizardDAO.deleteWizardCategoryByUID(uid);
    }

    @Override
    public void deleteWizardSkillByUID(Long uid) {
	qaWizardDAO.deleteWizardSkillByUID(uid);
    }

    @Override
    public void deleteWizardQuestionByUID(Long uid) {
	qaWizardDAO.deleteWizardQuestionByUID(uid);
    }

    @Override
    public void deleteAllWizardCategories() {
	qaWizardDAO.deleteAllWizardCategories();
    }

    @Override
    public void removeQuestionsFromCache(QaContent qaContent) {
	qaDAO.removeQuestionsFromCache(qaContent);
    }

    @Override
    public void removeQaContentFromCache(QaContent qaContent) {
	qaDAO.removeQaContentFromCache(qaContent);
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getQaOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    // ****************** REST methods *************************

    /**
     * Rest call to create a new Q&A content. Required fields in toolContentJSON: title, instructions, questions. The
     * questions entry should be JSONArray containing JSON objects, which in turn must contain "questionText",
     * "displayOrder" and may also contain feedback and required (boolean)
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, JSONObject toolContentJSON)
	    throws JSONException {

	QaContent qa = new QaContent();
	Date updateDate = new Date();

	qa.setCreationDate(updateDate);
	qa.setUpdateDate(updateDate);
	qa.setCreatedBy(userID.longValue());

	qa.setQaContentId(toolContentID);
	qa.setTitle(toolContentJSON.getString(RestTags.TITLE));
	qa.setInstructions(toolContentJSON.getString(RestTags.INSTRUCTIONS));

	qa.setDefineLater(false);

	qa.setLockWhenFinished(JsonUtil.opt(toolContentJSON, RestTags.LOCK_WHEN_FINISHED, Boolean.FALSE));
	qa.setNoReeditAllowed(JsonUtil.opt(toolContentJSON, "noReeditAllowed", Boolean.FALSE));
	qa.setAllowRichEditor(JsonUtil.opt(toolContentJSON, RestTags.ALLOW_RICH_TEXT_EDITOR, Boolean.FALSE));
	qa.setUseSelectLeaderToolOuput(
		JsonUtil.opt(toolContentJSON, RestTags.USE_SELECT_LEADER_TOOL_OUTPUT, Boolean.FALSE));
	qa.setMinimumRates(JsonUtil.opt(toolContentJSON, RestTags.MINIMUM_RATES, 0));
	qa.setMaximumRates(JsonUtil.opt(toolContentJSON, RestTags.MAXIMUM_RATES, 0));
	qa.setShowOtherAnswers(JsonUtil.opt(toolContentJSON, "showOtherAnswers", Boolean.TRUE));
	qa.setUsernameVisible(JsonUtil.opt(toolContentJSON, "usernameVisible", Boolean.FALSE));
	qa.setAllowRateAnswers(JsonUtil.opt(toolContentJSON, "allowRateAnswers", Boolean.FALSE));
	qa.setNotifyTeachersOnResponseSubmit(
		JsonUtil.opt(toolContentJSON, "notifyTeachersOnResponseSubmit", Boolean.FALSE));
	qa.setReflect(JsonUtil.opt(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	qa.setReflectionSubject(JsonUtil.opt(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS, (String) null));
	qa.setQuestionsSequenced(JsonUtil.opt(toolContentJSON, "questionsSequenced", Boolean.FALSE));

	// submissionDeadline is set in monitoring
	// qa.setMonitoringReportTitle(); Can't find this field in the database - assuming unused.
	// qa.setReportTitle(); Can't find this field in the database - assuming unused.
	// qa.setContent(content); Can't find this field in the database - assuming unused.

	saveOrUpdateQaContent(qa);
	// Questions
	JSONArray questions = toolContentJSON.getJSONArray(RestTags.QUESTIONS);
	for (int i = 0; i < questions.length(); i++) {
	    JSONObject questionData = (JSONObject) questions.get(i);
	    QaQueContent question = new QaQueContent(questionData.getString(RestTags.QUESTION_TEXT),
		    questionData.getInt(RestTags.DISPLAY_ORDER), JsonUtil.opt(questionData, "feedback", (String) null),
		    JsonUtil.opt(questionData, "required", Boolean.FALSE),
		    JsonUtil.opt(questionData, "minWordsLimit", 0), qa);
	    saveOrUpdateQuestion(question);
	}

	// TODO
	// qa.setConditions(conditions);

    }
}
