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
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
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
import org.lamsfoundation.lams.tool.qa.ResponseRating;
import org.lamsfoundation.lams.tool.qa.dao.IQaConfigItemDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaContentDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaQueUsrDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaQuestionDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaSessionDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaUsrRespDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaWizardDAO;
import org.lamsfoundation.lams.tool.qa.dao.IResponseRatingDAO;
import org.lamsfoundation.lams.tool.qa.dto.AverageRatingDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;
import org.lamsfoundation.lams.tool.qa.dto.ReflectionDTO;
import org.lamsfoundation.lams.tool.qa.util.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.util.QaSessionComparator;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.dao.DataAccessException;

/**
 * The POJO implementation of Qa service. All business logics of Qa tool are implemented in this class. It translate the
 * request from presentation layer and perform approporiate database operation.
 * 
 * @author Ozgur Demirtas
 */
public class QaServicePOJO implements IQaService, ToolContentManager, ToolSessionManager, ToolContentImport102Manager,
	QaAppConstants {
    private static Logger logger = Logger.getLogger(QaServicePOJO.class.getName());

    private IQaContentDAO qaDAO;
    private IQaQuestionDAO qaQuestionDAO;

    private IQaSessionDAO qaSessionDAO;
    private IQaQueUsrDAO qaQueUsrDAO;
    private IQaUsrRespDAO qaUsrRespDAO;
    private IResponseRatingDAO qaResponseRatingDAO;

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
	if (user == null || toolSessionId == null) {
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
		response = new QaUsrResp(leaderResponse.getAnswer(), leaderResponse.getAttemptTime(), "", question,
			user, true);
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
    public List<QaQueUsr> getUsersBySessionId(Long toolSessionID) {
	QaSession session = qaSessionDAO.getQaSessionById(toolSessionID);
	return qaQueUsrDAO.getUserBySessionOnly(session);
    }

    public void createQaContent(QaContent qaContent) {
	qaDAO.saveQa(qaContent);
    }

    public QaContent getQaContent(long toolContentID) {
	return qaDAO.getQaByContentId(toolContentID);
    }

    public void saveOrUpdateQaContent(QaContent qa) {
	qaDAO.saveOrUpdateQa(qa);
    }

    public QaQueContent getQuestionByContentAndDisplayOrder(Long displayOrder, Long contentUid) {
	return qaQuestionDAO.getQuestionByDisplayOrder(displayOrder, contentUid);
    }
    
    public QaQueContent getQuestionByUid(Long questionUid) {
	if (questionUid == null) {
	    return null;
	}
	
	return qaQuestionDAO.getQuestionByUid(questionUid);
    }

    public void saveOrUpdateQuestion(QaQueContent question) {
	qaQuestionDAO.saveOrUpdateQaQueContent(question);
    }

    public void createQuestion(QaQueContent question) {
	qaQuestionDAO.createQueContent(question);
    }

    public void createSession(QaSession qaSession) {
	qaSessionDAO.createSession(qaSession);
    }

    public List getSessionsFromContent(QaContent qaContent) {
	return qaSessionDAO.getSessionsFromContent(qaContent);
    }

    public QaQueUsr createUser(Long toolSessionID) {
	HttpSession ss = SessionManager.getSession();
	UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userId = toolUser.getUserID().longValue();
	String userName = toolUser.getLogin();
	String fullName = toolUser.getFirstName() + " " + toolUser.getLastName();
	QaSession qaSession = getSessionById(toolSessionID.longValue());

	QaQueUsr user = new QaQueUsr(userId, userName, fullName, qaSession, new TreeSet());
	qaQueUsrDAO.createUsr(user);

	return user;
    }

    public QaQueUsr getUserByIdAndSession(final Long queUsrId, final Long qaSessionId) {
	return qaQueUsrDAO.getQaUserBySession(queUsrId, qaSessionId);
    }

    public QaUsrResp getResponseByUserAndQuestion(final Long queUsrId, final Long qaQueContentId) {
	return qaUsrRespDAO.getResponseByUserAndQuestion(queUsrId, qaQueContentId);
    }

    public void updateUserResponse(QaUsrResp resp) {
	qaUsrRespDAO.updateUserResponse(resp);
    }

    public void updateResponseWithNewAnswer(String newAnswer, String toolSessionID, Long questionDisplayOrder) {
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
	    response = new QaUsrResp(newAnswer, new Date(System.currentTimeMillis()), "", question, user, true);
	    createUserResponse(response);

	    // if answer has changed
	} else if (!newAnswer.equals(response.getAnswer())) {
	    response.setAnswer(newAnswer);
	    response.setAttemptTime(new Date(System.currentTimeMillis()));
	    response.setTimezone("");
	    updateUserResponse(response);
	}
    }

    public List getUserBySessionOnly(final QaSession qaSession) {
	return qaQueUsrDAO.getUserBySessionOnly(qaSession);
    }

    public void createUserResponse(QaUsrResp qaUsrResp) {
	qaUsrRespDAO.createUserResponse(qaUsrResp);
    }

    public void updateUser(QaQueUsr qaQueUsr) {
	qaQueUsrDAO.updateUsr(qaQueUsr);
    }

    public QaUsrResp getResponseById(Long responseId) {
	return qaUsrRespDAO.getResponseById(responseId);
    }

    public int countSessionComplete(QaContent qa) {
	return qaSessionDAO.countSessionComplete(qa);
    }

    public QaSession getSessionById(long qaSessionId) {
	return qaSessionDAO.getQaSessionById(qaSessionId);
    }

    public void updateQaContent(QaContent qa) {
	qaDAO.updateQa(qa);
    }

    public void updateSession(QaSession qaSession) {
	qaSessionDAO.UpdateQaSession(qaSession);
    }

    public void removeUserResponse(QaUsrResp resp) {
	auditService.logChange(QaAppConstants.MY_SIGNATURE, resp.getQaQueUser().getQueUsrId(), resp.getQaQueUser()
		.getUsername(), resp.getAnswer(), null);
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

    public int getTotalNumberOfUsers(QaContent qa) {
	return qaQueUsrDAO.getTotalNumberOfUsers(qa);
    }

    public List<QaQueContent> getAllQuestionEntries(final Long uid) {
	return qaQuestionDAO.getAllQuestionEntries(uid.longValue());
    }

    public List<QaQueContent> getAllQuestionEntriesSorted(final long contentUid) {
	return qaQuestionDAO.getAllQuestionEntriesSorted(contentUid);
    }

    public void removeQuestion(QaQueContent question) {
	qaQuestionDAO.removeQaQueContent(question);
    }

    /**
     * checks the paramter content in the user responses table
     * 
     * @param qa
     * @return boolean
     * @throws QaApplicationException
     */
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

    /**
     * gets called ONLY when a lesson is being created in monitoring mode. Should create the new content(toContent)
     * based on what the author has created her content with. In q/a tool's case that is content + question's content
     * but not user responses. The deep copy should go only as far as default content (or author created content)
     * already goes. ToolContentManager CONTRACT
     * 
     * similar to public void removeToolContent(Long toolContentID) gets called by Container+Flash
     * 
     */
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
	if (fromContentId.equals(defaultContentId) && fromContent != null && fromContent.getConditions().isEmpty()) {
	    fromContent.getConditions().add(getQaOutputFactory().createDefaultComplexUserAnswersCondition(fromContent));
	}
	QaContent toContent = QaContent.newInstance(fromContent, toContentId);
	if (toContent == null) {
	    QaServicePOJO.logger.error("throwing ToolException: WARNING!, retrieved toContent is null.");
	    throw new ToolException("WARNING! Fail to create toContent. Can't continue!");
	} else {
	    qaDAO.saveQa(toContent);
	}

    }

    /**
     * Will need an update on the core tool signature: reason : when qaContent is null throw an exception
     */
    public void removeToolContent(Long toolContentID, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException {
	if (toolContentID == null) {
	    throw new ToolException("toolContentID is missing");
	}

	QaContent qaContent = qaDAO.getQaByContentId(toolContentID.longValue());

	if (qaContent != null) {
	    Iterator sessionIterator = qaContent.getQaSessions().iterator();
	    while (sessionIterator.hasNext()) {
		if (removeSessionData == false) {
		    QaServicePOJO.logger.error("removeSessionData is false, throwing SessionDataExistsException.");
		    throw new SessionDataExistsException();
		}

		QaSession qaSession = (QaSession) sessionIterator.next();

		Iterator sessionUsersIterator = qaSession.getQaQueUsers().iterator();
		while (sessionUsersIterator.hasNext()) {
		    QaQueUsr qaQueUsr = (QaQueUsr) sessionUsersIterator.next();

		    Iterator sessionUsersResponsesIterator = qaQueUsr.getQaUsrResps().iterator();
		    while (sessionUsersResponsesIterator.hasNext()) {
			QaUsrResp qaUsrResp = (QaUsrResp) sessionUsersResponsesIterator.next();
			removeUserResponse(qaUsrResp);
		    }
		}
	    }

	    // removed all existing responses of toolContent with toolContentID
	    qaDAO.removeQa(toolContentID);
	} else {
	    QaServicePOJO.logger.error("Warning!!!, We should have not come here. qaContent is null.");
	    throw new ToolException("toolContentID is missing");
	}
    }

    @SuppressWarnings("unchecked")
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (logger.isDebugEnabled()) {
	    logger.debug("Removing Q&A answers for user ID " + userId + " and toolContentId " + toolContentId);
	}

	QaContent content = qaDAO.getQaByContentId(toolContentId);
	if (content != null) {
	    for (QaSession session : (Set<QaSession>) content.getQaSessions()) {
		QaQueUsr user = qaQueUsrDAO.getQaUserBySession(userId.longValue(), session.getQaSessionId());
		if (user != null) {
		    List<ResponseRating> ratings = qaResponseRatingDAO.getRatingsByUser(user.getUid());
		    for (ResponseRating rating : ratings) {
			qaResponseRatingDAO.removeResponseRating(rating);
		    }

		    for (QaUsrResp response : (Set<QaUsrResp>) user.getQaUsrResps()) {
			qaUsrRespDAO.removeUserResponse(response);
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

    public AverageRatingDTO rateResponse(Long responseId, Long userId, Long toolSessionID, float rating) {
	QaQueUsr imageGalleryUser = this.getUserByIdAndSession(userId, toolSessionID);
	ResponseRating responseRating = qaResponseRatingDAO.getRatingByResponseAndUser(responseId, userId);
	QaUsrResp response = qaUsrRespDAO.getResponseById(responseId);

	// persist ResponseRating changes in DB
	if (responseRating == null) { // add
	    responseRating = new ResponseRating();
	    responseRating.setUser(imageGalleryUser);
	    responseRating.setResponse(response);
	}
	responseRating.setRating(rating);
	qaResponseRatingDAO.saveObject(responseRating);

	// to make available new changes be visible in jsp page
	return qaResponseRatingDAO.getAverageRatingDTOByResponse(responseId);
    }

    public AverageRatingDTO getAverageRatingDTOByResponse(Long responseId) {
	return qaResponseRatingDAO.getAverageRatingDTOByResponse(responseId);
    }

    public List<ReflectionDTO> getReflectList(QaContent content, String userID) {

	// reflection data for all sessions
	List<ReflectionDTO> reflectionDTOs = new LinkedList<ReflectionDTO>();
	Set<QaSession> sessions = new TreeSet<QaSession>(new QaSessionComparator());
	sessions.addAll(content.getQaSessions());

	if (userID == null) {
	    // all users mode
	    for (QaSession session : sessions) {

		for (QaQueUsr user : (Set<QaQueUsr>) session.getQaQueUsers()) {

		    NotebookEntry notebookEntry = this.getEntry(session.getQaSessionId(),
			    CoreNotebookConstants.NOTEBOOK_TOOL, QaAppConstants.MY_SIGNATURE, new Integer(user
				    .getQueUsrId().toString()));

		    if (notebookEntry != null) {
			ReflectionDTO reflectionDTO = new ReflectionDTO();
			reflectionDTO.setUserId(user.getQueUsrId().toString());
			reflectionDTO.setSessionId(session.getQaSessionId().toString());
			reflectionDTO.setUserName(user.getFullname());
			reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
			Date postedDate = (notebookEntry.getLastModified() != null) ? notebookEntry.getLastModified()
				: notebookEntry.getCreateDate();
			reflectionDTO.setDate(postedDate);
			// String notebookEntryPresentable = QaUtils.replaceNewLines(notebookEntry.getEntry());
			reflectionDTO.setEntry(notebookEntry.getEntry());
			reflectionDTOs.add(reflectionDTO);
		    }
		}
	    }
	} else {
	    // single user mode
	    for (QaSession session : sessions) {

		for (QaQueUsr user : (Set<QaQueUsr>) session.getQaQueUsers()) {

		    if (user.getQueUsrId().toString().equals(userID)) {
			NotebookEntry notebookEntry = this.getEntry(session.getQaSessionId(),
				CoreNotebookConstants.NOTEBOOK_TOOL, QaAppConstants.MY_SIGNATURE, new Integer(user
					.getQueUsrId().toString()));

			if (notebookEntry != null) {
			    ReflectionDTO reflectionDTO = new ReflectionDTO();
			    reflectionDTO.setUserId(user.getQueUsrId().toString());
			    reflectionDTO.setSessionId(session.getQaSessionId().toString());
			    reflectionDTO.setUserName(user.getFullname());
			    reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
			    Date postedDate = (notebookEntry.getLastModified() != null) ? notebookEntry
				    .getLastModified() : notebookEntry.getCreateDate();
			    reflectionDTO.setDate(postedDate);
			    // String notebookEntryPresentable = QaUtils.replaceNewLines(notebookEntry.getEntry());
			    reflectionDTO.setEntry(notebookEntry.getEntry());
			    reflectionDTOs.add(reflectionDTO);
			}
		    }
		}
	    }
	}

	return reflectionDTOs;
    }

    @Override
    public void notifyTeachersOnResponseSubmit(Long sessionId) {
	final String NEW_LINE_CHARACTER = "<br>";

	HttpSession ss = SessionManager.getSession();
	UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userId = new Long(toolUser.getUserID().longValue());
	QaQueUsr user = getUserByIdAndSession(userId, new Long(sessionId));

	String fullName = user.getFullname();

	String message = NEW_LINE_CHARACTER + NEW_LINE_CHARACTER
		+ messageService.getMessage("label.user.has.answered.questions", new Object[] { fullName });

	List<QaUsrResp> responses = qaUsrRespDAO.getResponsesByUserUid(user.getUid());
	for (QaUsrResp response : responses) {
	    String question = response.getQaQuestion().getQuestion();
	    String answer = response.getAnswer();

	    message += NEW_LINE_CHARACTER + NEW_LINE_CHARACTER + question + answer;
	}

	message += NEW_LINE_CHARACTER + NEW_LINE_CHARACTER;

	eventNotificationService.notifyLessonMonitors(sessionId, message, true);
    }

    /**
     * Export the XML fragment for the tool's content, along with any files needed for the content.
     * 
     * @throws DataMissingException
     *             if no tool content matches the toolSessionId
     * @throws ToolException
     *             if any other error occurs
     */

    public void exportToolContent(Long toolContentID, String rootPath) {
	QaContent toolContentObj = qaDAO.getQaByContentId(toolContentID);
	if (toolContentObj == null) {
	    long defaultToolContentId = toolService.getToolDefaultContentIdBySignature(QaAppConstants.MY_SIGNATURE);
	    toolContentObj = getQaContent(defaultToolContentId);
	    if (toolContentObj != null && toolContentObj.getConditions().isEmpty()) {
		toolContentObj.getConditions().add(
			getQaOutputFactory().createDefaultComplexUserAnswersCondition(toolContentObj));
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

	    exportContentService.exportToolContent(toolContentID, toolContentObj, qaToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    /**
     * Import the XML fragment for the tool's content, along with any files needed for the content.
     * 
     * @throws ToolException
     *             if any other error occurs
     */
    public void importToolContent(Long toolContentID, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(QaImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, qaToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof QaContent)) {
		throw new ImportToolContentException("Import QA tool content failed. Deserialized object is "
			+ toolPOJO);
	    }
	    QaContent toolContentObj = (QaContent) toolPOJO;

	    // reset it to new toolContentID
	    toolContentObj.setQaContentId(toolContentID);
	    toolContentObj.setCreatedBy(newUserUid);

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

    /**
     * Get the definitions for possible output for an activity, based on the toolContentId. These may be definitions
     * that are always available for the tool (e.g. number of marks for Multiple Choice) or a custom definition created
     * for a particular activity such as the answer to the third question contains the word Koala and hence the need for
     * the toolContentId
     * 
     * @return SortedMap of ToolOutputDefinitions with the key being the name of each definition
     */
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	QaContent qaContent = qaDAO.getQaByContentId(toolContentId);
	if (qaContent == null) {
	    long defaultToolContentId = toolService.getToolDefaultContentIdBySignature(QaAppConstants.MY_SIGNATURE);
	    qaContent = getQaContent(defaultToolContentId);
	    if (qaContent != null && qaContent.getConditions().isEmpty()) {
		qaContent.getConditions().add(getQaOutputFactory().createDefaultComplexUserAnswersCondition(qaContent));
	    }
	}
	return getQaOutputFactory().getToolOutputDefinitions(qaContent, definitionType);
    }

    public String getToolContentTitle(Long toolContentId) {
	return qaDAO.getQaByContentId(toolContentId).getTitle();
    }

    /**
     * ToolSessionManager CONTRACT : creates a tool session with the incoming toolSessionId in the tool session table
     * 
     * gets called only in the Learner mode. All the learners in the same group have the same toolSessionId.
     */
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

    /**
     * Complete the tool session.
     * 
     * Part of the ToolSessionManager contract. Called by controller service to force complete the qa session, or by the
     * web front end to complete the qa session
     * 
     */
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

    /**
     * ToolSessionManager CONTRACT
     * 
     */
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	throw new ToolException("not yet implemented");
    }

    /**
     * ToolSessionManager CONTRACT
     * 
     */
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException,
	    ToolException {

	throw new ToolException("not yet implemented");
    }

    /**
     * Get the tool output for the given tool output names.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long,
     *      java.lang.Long)
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getQaOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long,
     *      java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getQaOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
    }

    public IToolVO getToolBySignature(String toolSignature) {
	IToolVO tool = toolService.getToolBySignature(toolSignature);
	return tool;
    }

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
	if (list == null || list.isEmpty()) {
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
     * @return Returns the qaQuestionDAO.
     */
    public IQaQuestionDAO getQaQuestionDAO() {
	return qaQuestionDAO;
    }

    /**
     * @return Returns the qaQueUsrDAO.
     */
    public IQaQueUsrDAO getQaQueUsrDAO() {
	return qaQueUsrDAO;
    }

    /**
     * @return Returns the toolService.
     */
    public ILamsToolService getToolService() {
	return toolService;
    }

    /**
     * @return Returns the userManagementService.
     */
    public IUserManagementService getUserManagementService() {
	return userManagementService;
    }

    public ILearnerService getLearnerService() {
	return learnerService;
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

    public void setQaResponseRatingDAO(IResponseRatingDAO responseRatingDAO) {
	this.qaResponseRatingDAO = responseRatingDAO;
    }

    /**
     * @return Returns the qaDAO.
     */
    public IQaContentDAO getQaDAO() {
	return qaDAO;
    }

    /**
     * @return Returns the qaSessionDAO.
     */
    public IQaSessionDAO getQaSessionDAO() {
	return qaSessionDAO;
    }

    /**
     * @return Returns the qaUsrRespDAO.
     */
    public IQaUsrRespDAO getQaUsrRespDAO() {
	return qaUsrRespDAO;
    }

    /**
     * @return Returns the IResponseRatingDAO.
     */
    public IResponseRatingDAO getQaResponseRatingDAO() {
	return qaResponseRatingDAO;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    /**
     * @return Returns the qaToolContentHandler.
     */
    public IToolContentHandler getQaToolContentHandler() {
	return qaToolContentHandler;
    }

    /**
     * @param qaToolContentHandler
     *            The qaToolContentHandler to set.
     */
    public void setQaToolContentHandler(IToolContentHandler qaToolContentHandler) {
	this.qaToolContentHandler = qaToolContentHandler;
    }

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

    /*
     * ===============Methods implemented from ToolContentImport102Manager ===============
     */

    /**
     * Import the data for a 1.0.2 Chat
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	QaContent toolContentObj = new QaContent();
	toolContentObj.setCreatedBy(user.getUserID().longValue());
	toolContentObj.setCreationDate(now);
	toolContentObj.setDefineLater(false);
	toolContentObj.setInstructions(null);
	toolContentObj.setReflect(false);
	toolContentObj.setReflectionSubject(null);
	toolContentObj.setTitle((String) importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
	toolContentObj.setQaContentId(toolContentId);
	toolContentObj.setUpdateDate(now);
	toolContentObj.setQuestionsSequenced(false); // there is only 1
	// question
	toolContentObj.setContent(null);
	toolContentObj.setReportTitle(null);
	toolContentObj.setMonitoringReportTitle(null);
	// in LAMS 2.0
	toolContentObj.setLockWhenFinished(true);
	toolContentObj.setShowOtherAnswers(true);
	toolContentObj.setAllowRateAnswers(false);
	toolContentObj.setNotifyTeachersOnResponseSubmit(false);

	Boolean bool;
	try {
	    bool = WDDXProcessor.convertToBoolean(importValues, ToolContentImport102Manager.CONTENT_SHOW_USER);
	    toolContentObj.setUsernameVisible(bool != null ? bool : false);
	} catch (WDDXProcessorConversionException e) {
	    QaServicePOJO.logger.error("Unable to content for activity " + toolContentObj.getTitle()
		    + "properly due to a WDDXProcessorConversionException.", e);
	    throw new ToolException(
		    "Invalid import data format for activity "
			    + toolContentObj.getTitle()
			    + "- WDDX caused an exception. Some data from the design will have been lost. See log for more details.");
	}

	// leave as empty, no need to set them to anything.
	// setQaSessions(Set qaSessions);

	// set up question from body
	QaQueContent question = new QaQueContent();
	String content = WebUtil.convertNewlines((String) importValues.get(ToolContentImport102Manager.CONTENT_BODY));
	question.setQuestion(content);
	question.setDisplayOrder(1);
	question.setQaContent(toolContentObj);
	toolContentObj.getQaQueContents().add(question);

	qaDAO.saveOrUpdateQa(toolContentObj);

    }

    /**
     * Set the description, throws away the title value as this is not supported in 2.0
     */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	QaContent qaContent = null;
	if (toolContentId != null) {
	    qaContent = getQaContent(toolContentId.longValue());
	}
	if (qaContent == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	qaContent.setReflect(true);
	qaContent.setReflectionSubject(description);
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

    public void setEventNotificationService(IEventNotificationService eventNotificationService) {
	this.eventNotificationService = eventNotificationService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

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

    public QaContent getQaContentBySessionId(Long sessionId) {
	QaSession session = qaSessionDAO.getQaSessionById(sessionId);
	// to skip CGLib problem
	Long contentId = session.getQaContent().getQaContentId();
	QaContent qaContent = qaDAO.getQaByContentId(contentId);
	return qaContent;
    }

    /**
     * {@inheritDoc}
     */
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

    public void deleteCondition(QaCondition condition) {
	if (condition != null && condition.getConditionId() != null) {
	    qaDAO.deleteCondition(condition);
	}
    }

    public QaCondition createDefaultComplexCondition(QaContent qaContent) {
	return getQaOutputFactory().createDefaultComplexUserAnswersCondition(qaContent);
    }

    /**
     * Gets the qa config item with the given key
     * 
     * @param configKey
     * @return
     */
    public QaConfigItem getConfigItem(String configKey) {
	return qaConfigItemDAO.getConfigItemByKey(configKey);
    }

    /**
     * Saves or updates a qa config item
     * 
     * @param configItem
     */
    public void saveOrUpdateConfigItem(QaConfigItem configItem) {
	qaConfigItemDAO.saveOrUpdate(configItem);
    }

    /**
     * Gets the set of wizard categories from the database
     * 
     * @return
     */
    public SortedSet<QaWizardCategory> getWizardCategories() {
	return qaWizardDAO.getWizardCategories();
    }

    /**
     * Saves the entire set of QaWizardCategories (including the child cognitive skills and questions)
     * 
     * @param categories
     */
    public void saveOrUpdateQaWizardCategories(SortedSet<QaWizardCategory> categories) {
	qaWizardDAO.saveOrUpdateCategories(categories);
    }

    /**
     * Deletes a wizard category from the db
     * 
     * @param uid
     */
    public void deleteWizardCategoryByUID(Long uid) {
	qaWizardDAO.deleteWizardCategoryByUID(uid);
    }

    /**
     * Deletes a wizard cognitive skill from the db
     * 
     * @param uid
     */
    public void deleteWizardSkillByUID(Long uid) {
	qaWizardDAO.deleteWizardSkillByUID(uid);
    }

    /**
     * Deletes a wizard question from the db
     * 
     * @param uid
     */
    public void deleteWizardQuestionByUID(Long uid) {
	qaWizardDAO.deleteWizardQuestionByUID(uid);
    }

    /**
     * Deletes all categories, sub skills and sub questions
     */
    public void deleteAllWizardCategories() {
	qaWizardDAO.deleteAllWizardCategories();
    }

    public void removeQuestionsFromCache(QaContent qaContent) {
	qaDAO.removeQuestionsFromCache(qaContent);
    }
    
    public void removeQaContentFromCache(QaContent qaContent) {
	qaDAO.removeQaContentFromCache(qaContent);
    }

    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getQaOutputFactory().getSupportedDefinitionClasses(definitionType);
    }
}
