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
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
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
import org.lamsfoundation.lams.tool.survey.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.survey.model.Survey;
import org.lamsfoundation.lams.tool.survey.model.SurveyAnswer;
import org.lamsfoundation.lams.tool.survey.model.SurveyCondition;
import org.lamsfoundation.lams.tool.survey.model.SurveyOption;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
import org.lamsfoundation.lams.tool.survey.model.SurveySession;
import org.lamsfoundation.lams.tool.survey.model.SurveyUser;
import org.lamsfoundation.lams.tool.survey.util.QuestionsComparator;
import org.lamsfoundation.lams.tool.survey.util.ReflectDTOComparator;
import org.lamsfoundation.lams.tool.survey.util.SurveySessionComparator;
import org.lamsfoundation.lams.tool.survey.util.SurveyToolContentHandler;
import org.lamsfoundation.lams.tool.survey.util.SurveyWebUtils;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;

/**
 * @author Dapeng.Ni
 */
public class SurveyServiceImpl implements ISurveyService, ToolContentManager, ToolSessionManager,
	ToolContentImport102Manager, ToolRestManager {
    private static Logger log = Logger.getLogger(SurveyServiceImpl.class.getName());

    // DAO
    private SurveyDAO surveyDao;

    private SurveyQuestionDAO surveyQuestionDao;

    private SurveyAnswerDAO surveyAnswerDao;

    private SurveyUserDAO surveyUserDao;

    private SurveySessionDAO surveySessionDao;

    // tool service
    private SurveyToolContentHandler surveyToolContentHandler;

    private MessageService messageService;

    // system services

    private ILamsToolService toolService;

    private ILearnerService learnerService;

    private IUserManagementService userManagementService;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

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
	if (defaultContent.getConditions().isEmpty()) {
	    defaultContent.getConditions().add(getSurveyOutputFactory().createDefaultComplexCondition(defaultContent));
	}
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
    public Map<Long, Set<ReflectDTO>> getReflectList(Long contentId, boolean setEntry) {
	Map<Long, Set<ReflectDTO>> map = new HashMap<Long, Set<ReflectDTO>>();

	List<SurveySession> sessionList = surveySessionDao.getByContentId(contentId);
	for (SurveySession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    boolean hasRefection = session.getSurvey().isReflectOnActivity();
	    Set<ReflectDTO> list = new TreeSet<ReflectDTO>(new ReflectDTOComparator());
	    // get all users in this session
	    List<SurveyUser> users = surveyUserDao.getBySessionID(sessionId);
	    for (SurveyUser user : users) {
		ReflectDTO ref = new ReflectDTO(user);

		if (setEntry) {
		    NotebookEntry entry = getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
			    SurveyConstants.TOOL_SIGNATURE, user.getUserId().intValue());
		    if (entry != null) {
			ref.setReflect(entry.getEntry());
		    }
		}

		ref.setHasRefection(hasRefection);
		list.add(ref);
	    }
	    map.put(sessionId, list);
	}

	return map;
    }

    @Override
    public List<Object[]> getUserReflectionsForTablesorter(final Long sessionId, int page, int size, int sorting,
	    String searchString) {
	return surveyUserDao.getUserReflectionsForTablesorter(sessionId, page, size, sorting, searchString,
		coreNotebookService);
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
	List<SurveyQuestion> questions = new ArrayList<SurveyQuestion>();
	SurveySession session = surveySessionDao.getSessionBySessionId(sessionId);
	if (session != null) {
	    Survey survey = session.getSurvey();
	    if (survey != null) {
		questions = new ArrayList<SurveyQuestion>(survey.getQuestions());
	    }
	}

	// set answer for this question acoording
	List<AnswerDTO> answers = new ArrayList<AnswerDTO>();
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

	return surveyUserDao.getUsersForTablesorter(sessionId, questionId, page, size, sorting, searchString);
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
	Map<Long, Integer> optMap = new HashMap<Long, Integer>();

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

	SortedMap<SurveySession, List<AnswerDTO>> summary = new TreeMap<SurveySession, List<AnswerDTO>>(
		new SurveySessionComparator());

	Survey survey = surveyDao.getByContentId(toolContentId);
	// get all question under this survey
	Set<SurveyQuestion> questionList = survey.getQuestions();
	List<SurveySession> sessionList = surveySessionDao.getByContentId(toolContentId);
	// iterator all sessions under this survey content, and get all questions and its answers.
	for (SurveySession session : sessionList) {
	    List<AnswerDTO> responseList = new ArrayList<AnswerDTO>();
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
	SortedMap<SurveySession, Integer> result = new TreeMap<SurveySession, Integer>(new SurveySessionComparator());

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
    public SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> exportClassPortfolio(
	    Long toolContentID) {

	// construct sessionToUsers Map
	Map<SurveySession, List<SurveyUser>> sessionToUsersMap = new HashMap<SurveySession, List<SurveyUser>>();
	List<SurveySession> sessions = surveySessionDao.getByContentId(toolContentID);
	if (sessions != null) {
	    for (SurveySession session : sessions) {
		// get all users under this session
		List<SurveyUser> users = surveyUserDao.getBySessionID(session.getSessionId());
		sessionToUsersMap.put(session, users);
	    }
	}

	return getExportSummary(sessionToUsersMap);
    }

    @Override
    public SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> exportBySessionId(Long toolSessionID) {

	SurveySession session = surveySessionDao.getSessionBySessionId(toolSessionID);
	List<SurveyUser> users = surveyUserDao.getBySessionID(toolSessionID);

	Map<SurveySession, List<SurveyUser>> sessionToUsersMap = new HashMap<SurveySession, List<SurveyUser>>();
	sessionToUsersMap.put(session, users);

	return getExportSummary(sessionToUsersMap);
    }

    @Override
    public SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> exportLearnerPortfolio(
	    SurveyUser learner) {

	Map<SurveySession, List<SurveyUser>> sessionToUsersMap = new HashMap<SurveySession, List<SurveyUser>>();
	SurveySession session = learner.getSession();
	sessionToUsersMap.put(session, Arrays.asList(learner));

	return getExportSummary(sessionToUsersMap);
    }

    /**
     * Creates data for export methods. Suitable both for single/multiple users
     *
     * @param sessionToUsersMap
     *            map containing all session to users pairs that require data to be exported
     * @return
     */
    private SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> getExportSummary(
	    Map<SurveySession, List<SurveyUser>> sessionToUsersMap) {

	SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> summary = new TreeMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>>(
		new SurveySessionComparator());

	// all questions
	List<SurveyQuestion> questions = new ArrayList<SurveyQuestion>();
	if (!sessionToUsersMap.isEmpty()) {
	    SurveySession session = sessionToUsersMap.keySet().iterator().next();
	    Survey survey = session.getSurvey();
	    questions.addAll(survey.getQuestions());
	}

	// traverse all sessions
	for (SurveySession session : sessionToUsersMap.keySet()) {

	    SortedMap<SurveyQuestion, List<AnswerDTO>> questionMap = new TreeMap<SurveyQuestion, List<AnswerDTO>>(
		    new QuestionsComparator());

	    // traverse all questions
	    for (SurveyQuestion question : questions) {

		List<SurveyUser> users = sessionToUsersMap.get(session);
		List<AnswerDTO> answerDtos = new ArrayList<AnswerDTO>();

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

	String userName = surveyUser.getLastName() + " " + surveyUser.getFirstName();
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
    public void removeToolContent(Long toolContentId) throws ToolException {
	Survey survey = surveyDao.getByContentId(toolContentId);
	if (survey == null) {
	    SurveyServiceImpl.log.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (SurveySession session : surveySessionDao.getByContentId(toolContentId)) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, SurveyConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	surveyDao.delete(survey);
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (SurveyServiceImpl.log.isDebugEnabled()) {
	    SurveyServiceImpl.log
		    .debug("Removing Survey answers for user ID " + userId + " and toolContentId " + toolContentId);
	}

	List<SurveyAnswer> answers = surveyAnswerDao.getByToolContentIdAndUserId(toolContentId, userId.longValue());
	for (SurveyAnswer answer : answers) {
	    surveyAnswerDao.removeObject(SurveyAnswer.class, answer.getUid());
	}

	List<SurveySession> sessions = surveySessionDao.getByContentId(toolContentId);
	for (SurveySession session : sessions) {
	    SurveyUser user = surveyUserDao.getUserByUserIDAndSessionID(userId.longValue(), session.getSessionId());
	    if (user != null) {
		NotebookEntry entry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			SurveyConstants.TOOL_SIGNATURE, userId);
		if (entry != null) {
		    surveyDao.removeObject(NotebookEntry.class, entry.getUid());
		}

		surveyUserDao.removeObject(SurveyUser.class, user.getUid());
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
	return learnerService.completeToolSession(toolSessionId, learnerId);
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
	return new ArrayList<ToolOutput>();
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	// no actions required
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /*
     * Sample content for import: <struct> <var name='objectType'><string>content</string></var> <var
     * name='id'><number>34.0</number></var> <var name='questions'><array length='3'> <struct> <var
     * name='order'><number>1.0</number></var> <var name='isOptional'><boolean value='true'/></var> <var
     * name='question'><string>Sample Multiple choice - only one response allowed?</string></var> <var
     * name='questionType'><string>simpleChoice</string></var> <var name='isTextBoxEnabled'><boolean
     * value='false'/></var> <var name='candidates'><array length='3'> <struct> <var
     * name='order'><number>1.0</number></var> <var name='answer'><string>Option 1</string></var> </struct> <struct>
     * <var name='order'><number>2.0</number></var> <var name='answer'><string>Option 2</string></var> </struct>
     * <struct> <var name='order'><number>3.0</number></var> <var name='answer'><string>Option 3</string></var>
     * </struct> </array></var> </struct> <struct> <var name='order'><number>2.0</number></var> <var
     * name='isOptional'><boolean value='false'/></var> <var name='question'><string>Sample Multiple choice - multiple
     * response allowed?</string></var> <var name='questionType'><string>choiceMultiple</string></var> <var
     * name='isTextBoxEnabled'><boolean value='true'/></var> <var name='candidates'><array length='3'> <struct> <var
     * name='order'><number>1.0</number></var> <var name='answer'><string>Option 1</string></var> </struct><struct> <var
     * name='order'><number>2.0</number></var> <var name='answer'><string>Option 2</string></var> </struct><struct> <var
     * name='order'><number>3.0</number></var> <var name='answer'><string>Option 3</string></var> </struct>
     * </array></var> </struct> <struct> <var name='order'><number>3.0</number></var> <var name='isOptional'><boolean
     * value='true'/></var> <var name='question'><string>Sample Free text question?</string></var> <var
     * name='questionType'><string>textEntry</string></var> <var name='isTextBoxEnabled'><boolean value='false'/></var>
     * <var name='candidates'><array length='0'></array></var> </struct> </array></var> <var
     * name='contentDefineLater'><boolean value='false'/></var> <var name='body'><string>Put instructions
     * here.</string></var> <var name='contentShowUser'><boolean value='false'/></var> <var name='isHTML'><boolean
     * value='false'/></var> <var name='summary'><string>Thank you for your participation!</string></var> xxxxxxxxxx
     * <var name='title'><string>Put Title Here</string></var> <var name='description'><string>Survey
     * Questions</string></var> <var name='contentType'><string>surveycontent</string></var> <var
     * name='isReusable'><boolean value='false'/></var> </struct></array></var> <var
     * name='firstActivity'><number>31.0</number></var>
     */

    /**
     * Import the data for a 1.0.2 Noticeboard or HTMLNoticeboard
     */
    @Override
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	Survey toolContentObj = new Survey();

	try {
	    toolContentObj.setTitle((String) importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
	    toolContentObj.setContentId(toolContentId);
	    toolContentObj.setContentInUse(Boolean.FALSE);
	    toolContentObj.setCreated(now);
	    toolContentObj.setDefineLater(Boolean.FALSE);
	    toolContentObj.setInstructions(
		    WebUtil.convertNewlines((String) importValues.get(ToolContentImport102Manager.CONTENT_BODY)));
	    toolContentObj.setUpdated(now);

	    Boolean isReusable = WDDXProcessor.convertToBoolean(importValues,
		    ToolContentImport102Manager.CONTENT_REUSABLE);
	    toolContentObj.setLockWhenFinished(isReusable != null ? !isReusable.booleanValue() : true);

	    SurveyUser ruser = new SurveyUser();
	    ruser.setUserId(new Long(user.getUserID().longValue()));
	    ruser.setFirstName(user.getFirstName());
	    ruser.setLastName(user.getLastName());
	    ruser.setLoginName(user.getLogin());
	    createUser(ruser);
	    toolContentObj.setCreatedBy(ruser);

	    // survey questions
	    toolContentObj.setQuestions(new HashSet<SurveyQuestion>());

	    Vector questions = (Vector) importValues.get(ToolContentImport102Manager.CONTENT_SURVEY_QUESTIONS);
	    if (questions != null) {
		int dummySequenceNumber = questions.size(); // dummy number in case we can't convert question order
		Iterator iter = questions.iterator();
		while (iter.hasNext()) {
		    Hashtable questionMap = (Hashtable) iter.next();

		    SurveyQuestion item = new SurveyQuestion();
		    item.setCreateDate(now);
		    item.setCreateBy(ruser);

		    // try to set the type from the map. if that doesn't work then assume it is a text entry
		    String surveyType = (String) questionMap
			    .get(ToolContentImport102Manager.CONTENT_SURVEY_QUESTION_TYPE);
		    if (ToolContentImport102Manager.CONTENT_SURVEY_TYPE_SINGLE.equals(surveyType)) {
			item.setType((short) 1);
			item.setAllowMultipleAnswer(false);
		    } else if (ToolContentImport102Manager.CONTENT_SURVEY_TYPE_MULTIPLE.equals(surveyType)) {
			item.setType((short) 2);
			item.setAllowMultipleAnswer(true);
		    } else {
			item.setType((short) 3);
			item.setAllowMultipleAnswer(false);
		    }

		    Integer order = WDDXProcessor.convertToInteger(questionMap,
			    ToolContentImport102Manager.CONTENT_SURVEY_ORDER);
		    item.setSequenceId(order != null ? order.intValue() : dummySequenceNumber++);

		    item.setDescription(WebUtil.convertNewlines(
			    (String) questionMap.get(ToolContentImport102Manager.CONTENT_SURVEY_QUESTION)));

		    // completion message purposely not supported in 2.0, so value can be dropped.

		    Boolean appendText = WDDXProcessor.convertToBoolean(questionMap,
			    ToolContentImport102Manager.CONTENT_SURVEY_TEXTBOX_ENABLED);
		    item.setAppendText(appendText != null ? appendText.booleanValue() : false);

		    Boolean isOptional = WDDXProcessor.convertToBoolean(questionMap,
			    ToolContentImport102Manager.CONTENT_SURVEY_OPTIONAL);
		    item.setOptional(isOptional != null ? isOptional.booleanValue() : false);

		    Vector candidates = (Vector) questionMap.get(ToolContentImport102Manager.CONTENT_SURVEY_CANDIDATES);
		    if ((candidates != null) && (candidates.size() > 0)) {
			item.setOptions(new HashSet());
			int dummyCandidateOrder = candidates.size(); // dummy number in case we can't convert
			// question order
			Iterator candIter = candidates.iterator();
			while (candIter.hasNext()) {
			    Hashtable candidateEntry = (Hashtable) candIter.next();
			    String candidateText = (String) candidateEntry
				    .get(ToolContentImport102Manager.CONTENT_SURVEY_ANSWER);
			    Integer candidateOrder = WDDXProcessor.convertToInteger(candidateEntry,
				    ToolContentImport102Manager.CONTENT_SURVEY_ORDER);

			    SurveyOption option = new SurveyOption();
			    option.setDescription(candidateText);
			    option.setSequenceId(
				    candidateOrder != null ? candidateOrder.intValue() : dummyCandidateOrder++);
			    item.getOptions().add(option);
			}
		    }

		    toolContentObj.getQuestions().add(item);
		}
	    }

	} catch (WDDXProcessorConversionException e) {
	    SurveyServiceImpl.log.error("Unable to content for activity " + toolContentObj.getTitle()
		    + "properly due to a WDDXProcessorConversionException.", e);
	    throw new ToolException("Invalid import data format for activity " + toolContentObj.getTitle()
		    + "- WDDX caused an exception. Some data from the design will have been lost. See log for more details.");
	}

	surveyDao.saveObject(toolContentObj);

    }

    /** Set the description, throws away the title value as this is not supported in 2.0 */
    @Override
    public void setReflectiveData(Long toolContentId, String title, String description)
	    throws ToolException, DataMissingException {

	Survey toolContentObj = getSurveyByContentId(toolContentId);
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	// toolContentObj.setReflectOnActivity(Boolean.TRUE);
	// toolContentObj.setReflectInstructions(description);
    }

    // *****************************************************************************
    // set methods for Spring Bean
    // *****************************************************************************

    public void setLearnerService(ILearnerService learnerService) {
	this.learnerService = learnerService;
    }

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

    public void setSurveyToolContentHandler(SurveyToolContentHandler surveyToolContentHandler) {
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

    public ICoreNotebookService getCoreNotebookService() {
	return coreNotebookService;
    }

    public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
	this.coreNotebookService = coreNotebookService;
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

    // ****************** REST methods *************************

    /**
     * Used by the Rest calls to create content.
     *
     * Mandatory fields in toolContentJSON: title, instructions, questions. Optional fields are lockWhenFinished
     * (default true), showOnePage (default true), notifyTeachersOnAnswerSumbit (default false), showOtherUsersAnswers
     * (default false), reflectOnActivity, reflectInstructions, submissionDeadline
     *
     * Questions must contain a JSONArray of JSONObject objects, which have the following mandatory fields:
     * questionText, type (1=one answer,2=multiple answers,3=free text entry) and answers. Answers is a JSONArray of
     * strings, which are the answer text. A question may also have the optional fields: allowOtherTextEntry (default
     * false), required (default true)
     *
     * There should be at least one question object in the Questions array and at least one option in the Options array.
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, JSONObject toolContentJSON)
	    throws JSONException {

	Survey survey = new Survey();
	Date updateDate = new Date();
	survey.setCreated(updateDate);
	survey.setUpdated(updateDate);

	survey.setContentId(toolContentID);
	survey.setTitle(toolContentJSON.getString(RestTags.TITLE));
	survey.setInstructions(toolContentJSON.getString(RestTags.INSTRUCTIONS));

	survey.setContentInUse(false);
	survey.setDefineLater(false);
	survey.setLockWhenFinished(JsonUtil.opt(toolContentJSON, RestTags.LOCK_WHEN_FINISHED, Boolean.TRUE));
	survey.setReflectInstructions((String) JsonUtil.opt(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS, null));
	survey.setReflectOnActivity(JsonUtil.opt(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	survey.setNotifyTeachersOnAnswerSumbit(
		JsonUtil.opt(toolContentJSON, "notifyTeachersOnAnswerSumbit", Boolean.FALSE));
	survey.setShowOnePage(JsonUtil.opt(toolContentJSON, "showOnePage", Boolean.TRUE));
	survey.setShowOtherUsersAnswers(JsonUtil.opt(toolContentJSON, "showOtherUsersAnswers", Boolean.FALSE));

	// submissionDeadline is set in monitoring

	SurveyUser surveyUser = new SurveyUser(userID.longValue(), toolContentJSON.getString("firstName"),
		toolContentJSON.getString("lastName"), toolContentJSON.getString("loginName"), survey);
	survey.setCreatedBy(surveyUser);

	// **************************** Handle Survey Questions *********************

	JSONArray questions = toolContentJSON.getJSONArray(RestTags.QUESTIONS);
	for (int i = 0; i < questions.length(); i++) {
	    JSONObject questionData = (JSONObject) questions.get(i);
	    SurveyQuestion newQuestion = new SurveyQuestion();
	    newQuestion.setCreateBy(surveyUser);
	    newQuestion.setCreateDate(updateDate);
	    newQuestion.setDescription(questionData.getString(RestTags.QUESTION_TEXT));
	    newQuestion.setType((short) questionData.getInt("type"));
	    newQuestion.setAppendText(JsonUtil.opt(questionData, "allowOtherTextEntry", Boolean.FALSE));
	    Boolean required = JsonUtil.opt(questionData, "required", Boolean.TRUE);
	    newQuestion.setOptional(!required);
	    newQuestion.setSequenceId(i + 1); // sequence number starts at 1

	    Set<SurveyOption> newOptions = new HashSet<SurveyOption>();
	    JSONArray options = questionData.getJSONArray(RestTags.ANSWERS);
	    for (int j = 0; j < options.length(); j++) {
		SurveyOption newOption = new SurveyOption();
		newOption.setDescription(options.getString(j));
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