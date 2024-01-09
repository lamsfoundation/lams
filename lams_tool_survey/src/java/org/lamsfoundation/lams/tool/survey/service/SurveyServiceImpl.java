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

package org.lamsfoundation.lams.tool.survey.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.dao.SurveyAnswerDAO;
import org.lamsfoundation.lams.tool.survey.dao.SurveyDAO;
import org.lamsfoundation.lams.tool.survey.dao.SurveyQuestionDAO;
import org.lamsfoundation.lams.tool.survey.dao.SurveySessionDAO;
import org.lamsfoundation.lams.tool.survey.dao.SurveyUserDAO;
import org.lamsfoundation.lams.tool.survey.dto.AnswerDTO;
import org.lamsfoundation.lams.tool.survey.model.Survey;
import org.lamsfoundation.lams.tool.survey.model.SurveyAnswer;
import org.lamsfoundation.lams.tool.survey.model.SurveyCondition;
import org.lamsfoundation.lams.tool.survey.model.SurveyOption;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
import org.lamsfoundation.lams.tool.survey.model.SurveySession;
import org.lamsfoundation.lams.tool.survey.model.SurveyUser;
import org.lamsfoundation.lams.tool.survey.util.QuestionsComparator;
import org.lamsfoundation.lams.tool.survey.util.SurveySessionComparator;
import org.lamsfoundation.lams.tool.survey.util.SurveyWebUtils;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Dapeng.Ni
 */
public class SurveyServiceImpl implements ISurveyService, ToolContentManager, ToolSessionManager, ToolRestManager {
    private static Logger log = Logger.getLogger(SurveyServiceImpl.class.getName());

    // DAO
    private SurveyDAO surveyDao;

    private SurveyQuestionDAO surveyQuestionDao;

    private SurveyAnswerDAO surveyAnswerDao;

    private SurveyUserDAO surveyUserDao;

    private SurveySessionDAO surveySessionDao;

    // tool service
    private IToolContentHandler surveyToolContentHandler;

    private MessageService messageService;

    // system services

    private ILamsToolService toolService;

    private IUserManagementService userManagementService;

    private IExportToolContentService exportContentService;

    private IEventNotificationService eventNotificationService;

    private SurveyOutputFactory surveyOutputFactory;

    private Random generator = new Random();

    // *******************************************************************************
    // Service method
    // *******************************************************************************

    @Override
    public Survey getSurveyByContentId(Long contentId) {
	Survey rs = surveyDao.getByContentId(contentId);
	return rs;
    }

    @Override
    public Survey getDefaultContent(Long contentId) throws SurveyApplicationException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    SurveyServiceImpl.log.error(error);
	    throw new SurveyApplicationException(error);
	}

	Survey defaultContent = getDefaultSurvey();
	// save default content by given ID.
	Survey content = new Survey();
	content = Survey.newInstance(defaultContent, contentId);
	return content;
    }

    @Override
    public void createUser(SurveyUser surveyUser) {
	surveyUserDao.saveObject(surveyUser);
    }

    @Override
    public SurveyUser getUserByIDAndContent(Long userId, Long contentId) {
	return surveyUserDao.getUserByUserIDAndContentID(userId, contentId);
    }

    @Override
    public SurveyUser getUserByIDAndSession(Long userId, Long sessionId) {
	return surveyUserDao.getUserByUserIDAndSessionID(userId, sessionId);
    }

    @Override
    public int getCountFinishedUsers(Long sessionId) {
	return surveyUserDao.getCountFinishedUsers(sessionId);
    }

    @Override
    public void saveOrUpdateSurvey(Survey survey) {
	surveyDao.saveObject(survey);
    }

    @Override
    public Survey getSurveyBySessionId(Long sessionId) {
	SurveySession session = surveySessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getSurvey().getContentId();
	Survey res = surveyDao.getByContentId(contentId);
	return res;
    }

    @Override
    public SurveySession getSurveySessionBySessionId(Long sessionId) {
	return surveySessionDao.getSessionBySessionId(sessionId);
    }

    @Override
    public void saveOrUpdateSurveySession(SurveySession resSession) {
	surveySessionDao.saveObject(resSession);
    }

    @Override
    public String finishToolSession(Long toolSessionId, Long userId) throws SurveyApplicationException {
	SurveyUser user = surveyUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	user.setSessionFinished(true);
	surveyUserDao.saveObject(user);

	// SurveySession session = surveySessionDao.getSessionBySessionId(toolSessionId);
	// session.setStatus(SurveyConstants.COMPLETED);
	// surveySessionDao.saveObject(session);

	String nextUrl = null;
	try {
	    nextUrl = this.leaveToolSession(toolSessionId, userId);
	} catch (DataMissingException e) {
	    throw new SurveyApplicationException(e);
	} catch (ToolException e) {
	    throw new SurveyApplicationException(e);
	}
	return nextUrl;
    }

    @Override
    public void setResponseFinalized(Long userUid) {
	SurveyUser user = (SurveyUser) surveyUserDao.getObject(SurveyUser.class, userUid);
	user.setResponseFinalized(true);
	surveyUserDao.saveObject(user);
    }

    @Override
    public SurveyUser getUser(Long uid) {
	return (SurveyUser) surveyUserDao.getObject(SurveyUser.class, uid);
    }

    @Override
    public List<SurveyUser> getSessionUsers(Long sessionId) {
	return surveyUserDao.getBySessionID(sessionId);
    }

    @Override
    public void deleteQuestion(Long uid) {
	surveyQuestionDao.removeObject(SurveyQuestion.class, uid);

    }

    @Override
    public List<AnswerDTO> getQuestionAnswers(Long sessionId, Long userUid) {
	List<SurveyQuestion> questions = new ArrayList<>();
	SurveySession session = surveySessionDao.getSessionBySessionId(sessionId);
	if (session != null) {
	    Survey survey = session.getSurvey();
	    if (survey != null) {
		questions = new ArrayList<>(survey.getQuestions());
	    }
	}

	// set answer for this question acoording
	List<AnswerDTO> answers = new ArrayList<>();
	for (SurveyQuestion question : questions) {
	    AnswerDTO answerDTO = new AnswerDTO(question);
	    SurveyAnswer answer = surveyAnswerDao.getAnswer(question.getUid(), userUid);
	    if (answer != null) {
		answer.setChoices(SurveyWebUtils.getChoiceList(answer.getAnswerChoices()));
	    }
	    answerDTO.setAnswer(answer);
	    answerDTO.setReplier((SurveyUser) surveyUserDao.getObject(SurveyUser.class, userUid));
	    answers.add(answerDTO);
	}
	return answers;
    }

    @Override
    public List<Object[]> getQuestionAnswersForTablesorter(Long sessionId, Long questionId, int page, int size,
	    int sorting, String searchString) {

	return surveyUserDao.getUsersForTablesorter(sessionId, questionId, page, size, sorting, searchString,
		userManagementService);
    }

    @Override
    public int getCountUsersBySession(final Long sessionId, String searchString) {
	return surveyUserDao.getCountUsersBySession(sessionId, searchString);
    }

    @Override
    public void updateAnswerList(List<SurveyAnswer> answerList) {
	for (SurveyAnswer ans : answerList) {
	    surveyAnswerDao.saveObject(ans);
	}
    }

    @Override
    public AnswerDTO getQuestionResponse(Long sessionId, Long questionUid) {
	SurveyQuestion question = surveyQuestionDao.getByUid(questionUid);
	AnswerDTO answerDto = new AnswerDTO(question);

	// create a map to hold Option UID and the counts for that choice
	Map<Long, Integer> optMap = new HashMap<>();

	// total number of answers - used for the percentage calculations
	int numberAnswers = 0;

	// go through all the choices and find out how many options for the choices.
	Set<SurveyOption> options = answerDto.getOptions();
	for (SurveyOption option : options) {
	    Long optUid = option.getUid();
	    int numChoice = surveyAnswerDao.getAnswerCount(sessionId, questionUid, optUid.toString());
	    optMap.put(optUid, numChoice);
	    numberAnswers += numChoice;
	}

	Integer numFreeChoice = null;
	if (answerDto.isAppendText() || (answerDto.getType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY)) {
	    numFreeChoice = getCountResponsesBySessionAndQuestion(sessionId, questionUid);
	    numberAnswers += numFreeChoice;
	}

	// calculate the percentage of answer response
	if (numberAnswers == 0) {
	    numberAnswers = 1;
	}
	for (SurveyOption option : options) {
	    int count = optMap.get(option.getUid());
	    double percentage = ((double) count / (double) numberAnswers) * 100d;
	    option.setResponse(percentage);
	    option.setResponseFormatStr(new Long(Math.round(percentage)).toString());
	    option.setResponseCount(count);
	}
	if (numFreeChoice != null) {
	    double percentage = ((double) numFreeChoice / (double) numberAnswers) * 100d;
	    answerDto.setOpenResponse(percentage);
	    answerDto.setOpenResponseFormatStr(new Long(Math.round(percentage)).toString());
	    answerDto.setOpenResponseCount(numFreeChoice);
	}

	return answerDto;
    }

    @Override
    public List<String> getOpenResponsesForTablesorter(final Long qaSessionId, final Long questionId, int page,
	    int size, int sorting) {
	return surveyAnswerDao.getOpenResponsesForTablesorter(qaSessionId, questionId, page, size, sorting);
    }

    @Override
    public int getCountResponsesBySessionAndQuestion(final Long qaSessionId, final Long questionId) {
	return surveyAnswerDao.getCountResponsesBySessionAndQuestion(qaSessionId, questionId);
    }

    @Override
    public SortedMap<SurveySession, List<AnswerDTO>> getSummary(Long toolContentId) {

	SortedMap<SurveySession, List<AnswerDTO>> summary = new TreeMap<>(new SurveySessionComparator());

	Survey survey = surveyDao.getByContentId(toolContentId);
	// get all question under this survey
	Set<SurveyQuestion> questionList = survey.getQuestions();
	List<SurveySession> sessionList = surveySessionDao.getByContentId(toolContentId);
	// iterator all sessions under this survey content, and get all questions and its answers.
	for (SurveySession session : sessionList) {
	    List<AnswerDTO> responseList = new ArrayList<>();
	    for (SurveyQuestion question : questionList) {
		AnswerDTO response = getQuestionResponse(session.getSessionId(), question.getUid());
		responseList.add(response);
	    }
	    summary.put(session, responseList);
	}

	return summary;
    }

    @Override
    public SortedMap<SurveySession, Integer> getStatistic(Long contentId) {
	SortedMap<SurveySession, Integer> result = new TreeMap<>(new SurveySessionComparator());

	List<Object[]> stats = surveyUserDao.getStatisticsBySession(contentId);
	for (Object[] stat : stats) {
	    SurveySession session = (SurveySession) stat[0];
	    Integer numUsers = (Integer) stat[1];
	    result.put(session, numUsers);
	}
	return result;

    }

    @Override
    public SurveyQuestion getQuestion(Long questionUid) {
	return surveyQuestionDao.getByUid(questionUid);
    }

    @Override
    public SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> exportBySessionId(Long toolSessionID) {

	SurveySession session = surveySessionDao.getSessionBySessionId(toolSessionID);
	List<SurveyUser> users = surveyUserDao.getBySessionID(toolSessionID);

	Map<SurveySession, List<SurveyUser>> sessionToUsersMap = new HashMap<>();
	sessionToUsersMap.put(session, users);

	return getExportSummary(sessionToUsersMap);
    }

    /**
     * Creates data for export methods. Suitable both for single/multiple users
     *
     * @param sessionToUsersMap
     * 	map containing all session to users pairs that require data to be exported
     * @return
     */
    private SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> getExportSummary(
	    Map<SurveySession, List<SurveyUser>> sessionToUsersMap) {

	SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> summary = new TreeMap<>(
		new SurveySessionComparator());

	// all questions
	List<SurveyQuestion> questions = new ArrayList<>();
	if (!sessionToUsersMap.isEmpty()) {
	    SurveySession session = sessionToUsersMap.keySet().iterator().next();
	    Survey survey = session.getSurvey();
	    questions.addAll(survey.getQuestions());
	}

	// traverse all sessions
	for (SurveySession session : sessionToUsersMap.keySet()) {

	    SortedMap<SurveyQuestion, List<AnswerDTO>> questionMap = new TreeMap<>(new QuestionsComparator());

	    // traverse all questions
	    for (SurveyQuestion question : questions) {

		List<SurveyUser> users = sessionToUsersMap.get(session);
		List<AnswerDTO> answerDtos = new ArrayList<>();

		// if it's for a single user - query DB for only one answer for this current user
		if (users.size() == 1) {
		    SurveyUser user = users.get(0);

		    AnswerDTO answerDTO = new AnswerDTO(question);
		    SurveyAnswer answer = surveyAnswerDao.getAnswer(question.getUid(), user.getUid());
		    if (answer != null) {
			answer.setChoices(SurveyWebUtils.getChoiceList(answer.getAnswerChoices()));
		    }
		    answerDTO.setAnswer(answer);
		    answerDTO.setReplier(user);
		    answerDtos.add(answerDTO);

		    // if it's for more than one user - query DB for all answers for this session to this question
		} else {

		    // get all question answers for this session
		    List<SurveyAnswer> answers = surveyAnswerDao.getSessionAnswer(session.getSessionId(),
			    question.getUid());
		    // traverse all users
		    for (SurveyUser user : users) {

			// find user's answer
			SurveyAnswer learnerAnswer = null;
			for (SurveyAnswer answer : answers) {
			    if (answer.getUser().getUid().equals(user.getUid())) {
				learnerAnswer = answer;
				break;
			    }
			}

			AnswerDTO answerDTO = new AnswerDTO(question);
			if (learnerAnswer != null) {
			    learnerAnswer.setChoices(SurveyWebUtils.getChoiceList(learnerAnswer.getAnswerChoices()));
			}
			answerDTO.setAnswer(learnerAnswer);
			answerDTO.setReplier(user);
			answerDtos.add(answerDTO);
		    }

		}

		questionMap.put(question, answerDtos);
	    }

	    summary.put(session, questionMap);
	}

	return summary;
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
    public String createConditionName(Collection<SurveyCondition> existingConditions) {
	String uniqueNumber = null;
	do {
	    uniqueNumber = String.valueOf(Math.abs(generator.nextInt()));
	    for (SurveyCondition condition : existingConditions) {
		String[] splitedName = getSurveyOutputFactory().splitConditionName(condition.getName());
		if (uniqueNumber.equals(splitedName[1])) {
		    uniqueNumber = null;
		}
	    }
	} while (uniqueNumber == null);
	return getSurveyOutputFactory().buildConditionName(uniqueNumber);
    }

    @Override
    public void deleteCondition(SurveyCondition condition) {
	surveyDao.deleteCondition(condition);
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getSurveyOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    @Override
    public void notifyTeachersOnAnswerSumbit(Long sessionId, SurveyUser surveyUser) {

	// it appears surveyUser can be null (?)
	if (surveyUser == null) {
	    return;
	}

	String userName = surveyUser.getFullName();
	String message = getLocalisedMessage("event.answer.submit.body", new Object[] { userName });
	eventNotificationService.notifyLessonMonitors(sessionId, message, false);
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************

    private Survey getDefaultSurvey() throws SurveyApplicationException {
	Long defaultSurveyId = getToolDefaultContentIdBySignature(SurveyConstants.TOOL_SIGNATURE);
	Survey defaultSurvey = getSurveyByContentId(defaultSurveyId);
	if (defaultSurvey == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    SurveyServiceImpl.log.error(error);
	    throw new SurveyApplicationException(error);
	}

	return defaultSurvey;
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws SurveyApplicationException {
	Long contentId = null;
	contentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    SurveyServiceImpl.log.error(error);
	    throw new SurveyApplicationException(error);
	}
	return contentId;
    }

    // *******************************************************************************
    // ToolContentManager, ToolSessionManager methods
    // *******************************************************************************

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Survey toolContentObj = surveyDao.getByContentId(toolContentId);
	if (toolContentObj == null) {
	    try {
		toolContentObj = getDefaultSurvey();
	    } catch (SurveyApplicationException e) {
		throw new DataMissingException(e.getMessage());
	    }
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the survey tool");
	}

	// set SurveyToolContentHandler as null to avoid copy file node in repository again.
	toolContentObj = Survey.newInstance(toolContentObj, toolContentId);
	try {
	    exportContentService.exportToolContent(toolContentId, toolContentObj, surveyToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(SurveyImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, surveyToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Survey)) {
		throw new ImportToolContentException(
			"Import survey tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Survey toolContentObj = (Survey) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentId(toolContentId);
	    SurveyUser user = surveyUserDao.getUserByUserIDAndContentID(new Long(newUserUid.longValue()),
		    toolContentId);
	    if (user == null) {
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user = new SurveyUser(sysUser, toolContentObj);
	    }
	    toolContentObj.setCreatedBy(user);

	    // reset all surveyItem createBy user
	    Set<SurveyQuestion> items = toolContentObj.getQuestions();
	    for (SurveyQuestion item : items) {
		item.setCreateBy(user);
	    }
	    surveyDao.saveObject(toolContentObj);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Survey survey = surveyDao.getByContentId(toolContentId);
	if (survey == null) {
	    try {
		survey = getDefaultSurvey();
	    } catch (SurveyApplicationException e) {
		throw new ToolException(e);
	    }
	}
	return getSurveyOutputFactory().getToolOutputDefinitions(survey, definitionType);
    }

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	if (toContentId == null) {
	    throw new ToolException("Failed to create the SharedSurveyFiles tool seession");
	}

	Survey survey = null;
	if (fromContentId != null) {
	    survey = surveyDao.getByContentId(fromContentId);
	}
	if (survey == null) {
	    try {
		survey = getDefaultSurvey();
	    } catch (SurveyApplicationException e) {
		throw new ToolException(e);
	    }
	}

	Survey toContent = Survey.newInstance(survey, toContentId);
	surveyDao.saveObject(toContent);

	// save survey items as well
	Set items = toContent.getQuestions();
	if (items != null) {
	    Iterator iter = items.iterator();
	    while (iter.hasNext()) {
		SurveyQuestion item = (SurveyQuestion) iter.next();
		// createRootTopic(toContent.getUid(),null,msg);
	    }
	}
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getSurveyByContentId(toolContentId).getTitle();
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Survey survey = surveyDao.getByContentId(toolContentId);
	if (survey == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	survey.setDefineLater(false);
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getSurveyByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	List<SurveySession> sessions = surveySessionDao.getByContentId(toolContentId);
	for (SurveySession session : sessions) {
	    if (!surveyUserDao.getBySessionID(session.getSessionId()).isEmpty()) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Survey survey = surveyDao.getByContentId(toolContentId);
	if (survey == null) {
	    SurveyServiceImpl.log.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	surveyDao.delete(survey);
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId, boolean resetActivityCompletionOnly)
	    throws ToolException {
	if (log.isDebugEnabled()) {
	    if (resetActivityCompletionOnly) {
		log.debug("Resetting Survey completion for user ID " + userId + " and toolContentId " + toolContentId);
	    } else {
		log.debug("Removing Survey answers for user ID " + userId + " and toolContentId " + toolContentId);
	    }
	}

	if (!resetActivityCompletionOnly) {
	    List<SurveyAnswer> answers = surveyAnswerDao.getByToolContentIdAndUserId(toolContentId, userId.longValue());
	    for (SurveyAnswer answer : answers) {
		surveyAnswerDao.removeObject(SurveyAnswer.class, answer.getUid());
	    }
	}

	List<SurveySession> sessions = surveySessionDao.getByContentId(toolContentId);
	for (SurveySession session : sessions) {
	    SurveyUser user = surveyUserDao.getUserByUserIDAndSessionID(userId.longValue(), session.getSessionId());
	    if (user != null) {
		if (resetActivityCompletionOnly) {
		    user.setSessionFinished(false);
		    surveyUserDao.saveObject(user);
		} else {
		    surveyUserDao.removeObject(SurveyUser.class, user.getUid());
		}
	    }
	}
    }

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	SurveySession session = new SurveySession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	Survey survey = surveyDao.getByContentId(toolContentId);
	session.setSurvey(survey);
	surveySessionDao.saveObject(session);
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    SurveyServiceImpl.log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    SurveyServiceImpl.log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
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
	surveySessionDao.deleteBySessionId(toolSessionId);
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getSurveyOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getSurveyOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
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
    // set methods for Spring Bean
    // *****************************************************************************

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public void setSurveyDao(SurveyDAO surveyDao) {
	this.surveyDao = surveyDao;
    }

    public void setSurveyQuestionDao(SurveyQuestionDAO surveyItemDao) {
	surveyQuestionDao = surveyItemDao;
    }

    public void setSurveySessionDao(SurveySessionDAO surveySessionDao) {
	this.surveySessionDao = surveySessionDao;
    }

    public void setSurveyToolContentHandler(IToolContentHandler surveyToolContentHandler) {
	this.surveyToolContentHandler = surveyToolContentHandler;
    }

    public void setSurveyUserDao(SurveyUserDAO surveyUserDao) {
	this.surveyUserDao = surveyUserDao;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

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

    public void setSurveyAnswerDao(SurveyAnswerDAO surveyAnswerDao) {
	this.surveyAnswerDao = surveyAnswerDao;
    }

    public IEventNotificationService getEventNotificationService() {
	return eventNotificationService;
    }

    public void setEventNotificationService(IEventNotificationService eventNotificationService) {
	this.eventNotificationService = eventNotificationService;
    }

    public String getLocalisedMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
    }

    public SurveyOutputFactory getSurveyOutputFactory() {
	return surveyOutputFactory;
    }

    public void setSurveyOutputFactory(SurveyOutputFactory surveyOutputFactory) {
	this.surveyOutputFactory = surveyOutputFactory;
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	SurveyUser learner = getUserByIDAndSession(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	return new ToolCompletionStatus(learner.isSessionFinished()
		? ToolCompletionStatus.ACTIVITY_COMPLETED
		: ToolCompletionStatus.ACTIVITY_ATTEMPTED, null, null);
    }

    // ****************** REST methods *************************

    /**
     * Used by the Rest calls to create content.
     *
     * Mandatory fields in toolContentJSON: title, instructions, questions. Optional fields are lockWhenFinished
     * (default true), showOnePage (default true), notifyTeachersOnAnswerSumbit (default false), showOtherUsersAnswers
     * (default false), reflectOnActivity, reflectInstructions, submissionDeadline
     *
     * Questions must contain a ArrayNode of ObjectNode objects, which have the following mandatory fields:
     * questionText, type (1=one answer,2=multiple answers,3=free text entry) and answers. Answers is a ArrayNode of
     * strings, which are the answer text. A question may also have the optional fields: allowOtherTextEntry (default
     * false), required (default true)
     *
     * There should be at least one question object in the Questions array and at least one option in the Options
     * array.
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, ObjectNode toolContentJSON) {

	Survey survey = new Survey();
	Date updateDate = new Date();
	survey.setCreated(updateDate);
	survey.setUpdated(updateDate);

	survey.setContentId(toolContentID);
	survey.setTitle(JsonUtil.optString(toolContentJSON, RestTags.TITLE));
	survey.setInstructions(JsonUtil.optString(toolContentJSON, RestTags.INSTRUCTIONS));

	survey.setContentInUse(false);
	survey.setDefineLater(false);
	survey.setLockWhenFinished(JsonUtil.optBoolean(toolContentJSON, RestTags.LOCK_WHEN_FINISHED, Boolean.TRUE));
	survey.setNotifyTeachersOnAnswerSumbit(
		JsonUtil.optBoolean(toolContentJSON, "notifyTeachersOnAnswerSumbit", Boolean.FALSE));
	survey.setShowOnePage(JsonUtil.optBoolean(toolContentJSON, "showOnePage", Boolean.TRUE));
	survey.setShowOtherUsersAnswers(JsonUtil.optBoolean(toolContentJSON, "showOtherUsersAnswers", Boolean.FALSE));

	// submissionDeadline is set in monitoring

	SurveyUser surveyUser = new SurveyUser(userID.longValue(), JsonUtil.optString(toolContentJSON, "firstName"),
		JsonUtil.optString(toolContentJSON, "lastName"), JsonUtil.optString(toolContentJSON, "loginName"),
		survey);
	survey.setCreatedBy(surveyUser);

	// **************************** Handle Survey Questions *********************

	ArrayNode questions = JsonUtil.optArray(toolContentJSON, RestTags.QUESTIONS);
	for (int i = 0; i < questions.size(); i++) {
	    ObjectNode questionData = (ObjectNode) questions.get(i);
	    SurveyQuestion newQuestion = new SurveyQuestion();
	    newQuestion.setCreateBy(surveyUser);
	    newQuestion.setCreateDate(updateDate);
	    newQuestion.setDescription(JsonUtil.optString(questionData, RestTags.QUESTION_TEXT));
	    newQuestion.setType((short) questionData.get("type").asInt());
	    newQuestion.setAppendText(JsonUtil.optBoolean(questionData, "allowOtherTextEntry", Boolean.FALSE));
	    Boolean required = JsonUtil.optBoolean(questionData, "required", Boolean.TRUE);
	    newQuestion.setOptional(!required);
	    newQuestion.setSequenceId(i + 1); // sequence number starts at 1

	    Set<SurveyOption> newOptions = new HashSet<>();
	    ArrayNode options = JsonUtil.optArray(questionData, RestTags.ANSWERS);
	    for (int j = 0; j < options.size(); j++) {
		SurveyOption newOption = new SurveyOption();
		newOption.setDescription(options.get(j).asText());
		newOption.setSequenceId(j); // sequence number starts at 0
		newOptions.add(newOption);
	    }
	    newQuestion.setOptions(newOptions);

	    survey.getQuestions().add(newQuestion);
	}

	saveOrUpdateSurvey(survey);
	// *******************************
	// TODO - investigate conditions
	// survey.setConditions(conditions);
    }
}