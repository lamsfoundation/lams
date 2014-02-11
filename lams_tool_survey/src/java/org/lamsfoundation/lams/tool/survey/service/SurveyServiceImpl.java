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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.lesson.service.ILessonService;
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
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;

/**
 * 
 * @author Dapeng.Ni
 * 
 */
public class SurveyServiceImpl implements ISurveyService, ToolContentManager, ToolSessionManager,
	ToolContentImport102Manager

{
    static Logger log = Logger.getLogger(SurveyServiceImpl.class.getName());

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

    private ILessonService lessonService;

    private SurveyOutputFactory surveyOutputFactory;

    private Random generator = new Random();

    // *******************************************************************************
    // Service method
    // *******************************************************************************

    public Survey getSurveyByContentId(Long contentId) {
	Survey rs = surveyDao.getByContentId(contentId);
	if (rs == null) {
	    SurveyServiceImpl.log.debug("Could not find the content by given ID:" + contentId);
	}
	return rs;
    }

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

    public void createUser(SurveyUser surveyUser) {
	surveyUserDao.saveObject(surveyUser);
    }

    public SurveyUser getUserByIDAndContent(Long userId, Long contentId) {

	return surveyUserDao.getUserByUserIDAndContentID(userId, contentId);

    }

    public SurveyUser getUserByIDAndSession(Long userId, Long sessionId) {

	return surveyUserDao.getUserByUserIDAndSessionID(userId, sessionId);

    }

    public void saveOrUpdateSurvey(Survey survey) {
	surveyDao.saveObject(survey);
    }

    public Survey getSurveyBySessionId(Long sessionId) {
	SurveySession session = surveySessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getSurvey().getContentId();
	Survey res = surveyDao.getByContentId(contentId);
	return res;
    }

    public SurveySession getSurveySessionBySessionId(Long sessionId) {
	return surveySessionDao.getSessionBySessionId(sessionId);
    }

    public void saveOrUpdateSurveySession(SurveySession resSession) {
	surveySessionDao.saveObject(resSession);
    }

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

    public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
	    String entryText) {
	return coreNotebookService.createNotebookEntry(sessionId, notebookToolType, toolSignature, userId, "",
		entryText);
    }

    public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID) {
	List<NotebookEntry> list = coreNotebookService.getEntry(sessionId, idType, signature, userID);
	if (list == null || list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    /**
     * @param notebookEntry
     */
    public void updateEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }

    public SurveyUser getUser(Long uid) {
	return (SurveyUser) surveyUserDao.getObject(SurveyUser.class, uid);
    }

    public List<SurveyUser> getSessionUsers(Long sessionId) {
	return surveyUserDao.getBySessionID(sessionId);
    }

    public void deleteQuestion(Long uid) {
	surveyQuestionDao.removeObject(SurveyQuestion.class, uid);

    }

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

    public void updateAnswerList(List<SurveyAnswer> answerList) {
	for (SurveyAnswer ans : answerList) {
	    surveyAnswerDao.saveObject(ans);
	}
    }

    public AnswerDTO getQuestionResponse(Long sessionId, Long questionUid) {
	SurveyQuestion question = surveyQuestionDao.getByUid(questionUid);
	AnswerDTO answerDto = new AnswerDTO(question);

	// get question all answer from this session
	List<SurveyAnswer> answsers = surveyAnswerDao.getSessionAnswer(sessionId, questionUid);

	// create a map to hold Option UID and sequenceID(start from 0);
	Map<String, Integer> optMap = new HashMap<String, Integer>();
	Set<SurveyOption> options = answerDto.getOptions();
	int idx = 0;
	for (SurveyOption option : options) {
	    optMap.put(option.getUid().toString(), idx);
	    idx++;
	}

	// initial a array to hold how many time chose has been done for a option or open text.
	int optSize = options.size();
	// for appendText and open Text Entry will be the last one of choose[] array.
	if (answerDto.isAppendText() || answerDto.getType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY) {
	    optSize++;
	}

	int[] choose = new int[optSize];
	Arrays.fill(choose, 0);

	// sum up all option and open text (if has) have been selected count list
	int answerSum = 0;
	if (answsers != null) {
	    for (SurveyAnswer answer : answsers) {
		String[] choseOpt = SurveyWebUtils.getChoiceList(answer.getAnswerChoices());
		for (String optUid : choseOpt) {
		    // if option has been chosen, the relative index of choose[] array will increase.
		    if (optMap.containsKey(optUid)) {
			choose[optMap.get(optUid)]++;
			answerSum++;
		    }
		}
		// handle appendText or Open Text Entry
		if ((answerDto.isAppendText() || answerDto.getType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY)
			&& !StringUtils.isBlank(answer.getAnswerText())) {
		    choose[optSize - 1]++;
		    answerSum++;
		}
	    }
	}
	// caculate the percentage of answer response
	idx = 0;
	if (answerSum == 0) {
	    answerSum = 1;
	}
	for (SurveyOption option : options) {
	    option.setResponse((double) choose[idx] / (double) answerSum * 100d);
	    option.setResponseFormatStr(new Long(Math.round(option.getResponse())).toString());
	    option.setResponseCount(choose[idx]);
	    idx++;
	}
	if (answerDto.isAppendText() || answerDto.getType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY) {
	    answerDto.setOpenResponse((double) choose[idx] / (double) answerSum * 100d);
	    answerDto.setOpenResponseFormatStr(new Long(Math.round(answerDto.getOpenResponse())).toString());
	    answerDto.setOpenResponseCount(choose[idx]);
	}

	return answerDto;

    }

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

    public SortedMap<SurveySession, Integer> getStatistic(Long contentId) {
	SortedMap<SurveySession, Integer> result = new TreeMap<SurveySession, Integer>(new SurveySessionComparator());
	List<SurveySession> sessionList = surveySessionDao.getByContentId(contentId);
	if (sessionList == null) {
	    return result;
	}

	for (SurveySession session : sessionList) {
	    List<SurveyUser> users = getSessionUsers(session.getSessionId());
	    result.put(session, users != null ? users.size() : 0);
	}

	return result;

    }

    public SurveyQuestion getQuestion(Long questionUid) {
	return surveyQuestionDao.getByUid(questionUid);
    }

    public SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> exportByContentId(Long toolContentID) {

	SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> summary = new TreeMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>>(
		new SurveySessionComparator());

	// get all tool sessions in this content
	List<SurveySession> sessions = surveySessionDao.getByContentId(toolContentID);
	if (sessions != null) {
	    for (SurveySession session : sessions) {
		// get all users under this session
		List<SurveyUser> users = surveyUserDao.getBySessionID(session.getSessionId());

		// container for this user's answers
		List<List<AnswerDTO>> learnerAnswers = new ArrayList<List<AnswerDTO>>();
		if (users != null) {
		    // for every user, get answers of all questions.
		    for (SurveyUser user : users) {
			List<AnswerDTO> answers = getQuestionAnswers(user.getSession().getSessionId(), user.getUid());
			learnerAnswers.add(answers);
		    }
		}
		toQuestionMap(summary, session, learnerAnswers);
	    }
	}

	return summary;
    }

    public SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> exportBySessionId(Long toolSessionID) {

	SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> summary = new TreeMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>>(
		new SurveySessionComparator());

	// get tool sessions
	SurveySession session = surveySessionDao.getSessionBySessionId(toolSessionID);
	List<SurveyUser> users = surveyUserDao.getBySessionID(session.getSessionId());

	// container for this user's answers
	List<List<AnswerDTO>> learnerAnswers = new ArrayList<List<AnswerDTO>>();
	if (users != null) {
	    // for every user, get answers of all questions.
	    for (SurveyUser user : users) {
		List<AnswerDTO> answers = getQuestionAnswers(user.getSession().getSessionId(), user.getUid());
		learnerAnswers.add(answers);
	    }
	}
	toQuestionMap(summary, session, learnerAnswers);

	return summary;
    }

    public SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> exportByLearner(SurveyUser learner) {
	SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> summary = new TreeMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>>(
		new SurveySessionComparator());

	SurveySession session = learner.getSession();
	List<AnswerDTO> answers = getQuestionAnswers(session.getSessionId(), learner.getUid());
	List<List<AnswerDTO>> learnerAnswers = new ArrayList<List<AnswerDTO>>();
	learnerAnswers.add(answers);

	toQuestionMap(summary, session, learnerAnswers);

	return summary;
    }
    
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************
    /**
     * Convert all user's answers to another map sorted by SurveyQuestion, rather than old sorted by user.
     */
    private void toQuestionMap(SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> summary,
	    SurveySession session, List<List<AnswerDTO>> learnerAnswers) {
	// after get all users' all answers, then sort them by SurveyQuestion
	SortedMap<SurveyQuestion, List<AnswerDTO>> questionMap = new TreeMap<SurveyQuestion, List<AnswerDTO>>(
		new QuestionsComparator());
	Survey survey = getSurveyBySessionId(session.getSessionId());
	Set<SurveyQuestion> questionList = survey.getQuestions();
	if (questionList != null) {
	    for (SurveyQuestion question : questionList) {
		List<AnswerDTO> queAnsList = new ArrayList<AnswerDTO>();
		questionMap.put(question, queAnsList);
		for (List<AnswerDTO> listAns : learnerAnswers) {
		    for (AnswerDTO answerDTO : listAns) {
			// get a user's answer for this question
			if (answerDTO.getUid().equals(question.getUid())) {
			    queAnsList.add(answerDTO);
			    break;
			}
		    }
		    // find another user's answer
		}
	    }// for this question, get all users answer, the continue next
	}

	summary.put(session, questionMap);
    }

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

    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(SurveyImportContentVersionFilter.class);
	
	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, surveyToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Survey)) {
		throw new ImportToolContentException("Import survey tool content failed. Deserialized object is "
			+ toolPOJO);
	    }
	    Survey toolContentObj = (Survey) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentId(toolContentId);
	    SurveyUser user = surveyUserDao
		    .getUserByUserIDAndContentID(new Long(newUserUid.longValue()), toolContentId);
	    if (user == null) {
		user = new SurveyUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(new Long(newUserUid.longValue()));
		user.setSurvey(toolContentObj);
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
    
    public String getToolContentTitle(Long toolContentId) {
	return getSurveyByContentId(toolContentId).getTitle();
    }

    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException {
	Survey survey = surveyDao.getByContentId(toolContentId);
	if (removeSessionData) {
	    List list = surveySessionDao.getByContentId(toolContentId);
	    Iterator iter = list.iterator();
	    while (iter.hasNext()) {
		SurveySession session = (SurveySession) iter.next();
		surveySessionDao.delete(session);
	    }
	}
	surveyDao.delete(survey);
    }

    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (log.isDebugEnabled()) {
	    log.debug("Removing Survey answers for user ID " + userId + " and toolContentId " + toolContentId);
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

    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	SurveySession session = new SurveySession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	Survey survey = surveyDao.getByContentId(toolContentId);
	session.setSurvey(survey);
	surveySessionDao.saveObject(session);
    }

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

    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	return null;
    }

    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException,
	    ToolException {
	return null;
    }

    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	surveySessionDao.deleteBySessionId(toolSessionId);
    }

    /**
     * Get the tool output for the given tool output names.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long,
     *      java.lang.Long)
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getSurveyOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long,
     *      java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getSurveyOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /*
     * Sample content for import: <struct> <var name='objectType'><string>content</string></var> <var name='id'><number>34.0</number></var>
     * <var name='questions'><array length='3'> <struct> <var name='order'><number>1.0</number></var> <var
     * name='isOptional'><boolean value='true'/></var> <var name='question'><string>Sample Multiple choice - only one
     * response allowed?</string></var> <var name='questionType'><string>simpleChoice</string></var> <var
     * name='isTextBoxEnabled'><boolean value='false'/></var> <var name='candidates'><array length='3'> <struct> <var
     * name='order'><number>1.0</number></var> <var name='answer'><string>Option 1</string></var> </struct>
     * <struct> <var name='order'><number>2.0</number></var> <var name='answer'><string>Option 2</string></var>
     * </struct> <struct> <var name='order'><number>3.0</number></var> <var name='answer'><string>Option 3</string></var>
     * </struct> </array></var> </struct> <struct> <var name='order'><number>2.0</number></var> <var
     * name='isOptional'><boolean value='false'/></var> <var name='question'><string>Sample Multiple choice -
     * multiple response allowed?</string></var> <var name='questionType'><string>choiceMultiple</string></var>
     * <var name='isTextBoxEnabled'><boolean value='true'/></var> <var name='candidates'><array length='3'> <struct>
     * <var name='order'><number>1.0</number></var> <var name='answer'><string>Option 1</string></var> </struct><struct>
     * <var name='order'><number>2.0</number></var> <var name='answer'><string>Option 2</string></var> </struct><struct>
     * <var name='order'><number>3.0</number></var> <var name='answer'><string>Option 3</string></var> </struct>
     * </array></var> </struct> <struct> <var name='order'><number>3.0</number></var> <var name='isOptional'><boolean
     * value='true'/></var> <var name='question'><string>Sample Free text question?</string></var> <var
     * name='questionType'><string>textEntry</string></var> <var name='isTextBoxEnabled'><boolean value='false'/></var>
     * <var name='candidates'><array length='0'></array></var> </struct> </array></var> <var
     * name='contentDefineLater'><boolean value='false'/></var> <var name='body'><string>Put instructions here.</string></var>
     * <var name='contentShowUser'><boolean value='false'/></var> <var name='isHTML'><boolean value='false'/></var>
     * <var name='summary'><string>Thank you for your participation!</string></var> xxxxxxxxxx <var name='title'><string>Put
     * Title Here</string></var> <var name='description'><string>Survey Questions</string></var> <var
     * name='contentType'><string>surveycontent</string></var> <var name='isReusable'><boolean value='false'/></var>
     * </struct></array></var> <var name='firstActivity'><number>31.0</number></var>
     */

    /**
     * Import the data for a 1.0.2 Noticeboard or HTMLNoticeboard
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	Survey toolContentObj = new Survey();

	try {
	    toolContentObj.setTitle((String) importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
	    toolContentObj.setContentId(toolContentId);
	    toolContentObj.setContentInUse(Boolean.FALSE);
	    toolContentObj.setCreated(now);
	    toolContentObj.setDefineLater(Boolean.FALSE);
	    toolContentObj.setInstructions(WebUtil.convertNewlines((String) importValues
		    .get(ToolContentImport102Manager.CONTENT_BODY)));
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

		    item.setDescription(WebUtil.convertNewlines((String) questionMap
			    .get(ToolContentImport102Manager.CONTENT_SURVEY_QUESTION)));

		    // completion message purposely not supported in 2.0, so value can be dropped.

		    Boolean appendText = WDDXProcessor.convertToBoolean(questionMap,
			    ToolContentImport102Manager.CONTENT_SURVEY_TEXTBOX_ENABLED);
		    item.setAppendText(appendText != null ? appendText.booleanValue() : false);

		    Boolean isOptional = WDDXProcessor.convertToBoolean(questionMap,
			    ToolContentImport102Manager.CONTENT_SURVEY_OPTIONAL);
		    item.setOptional(isOptional != null ? isOptional.booleanValue() : false);

		    Vector candidates = (Vector) questionMap.get(ToolContentImport102Manager.CONTENT_SURVEY_CANDIDATES);
		    if (candidates != null && candidates.size() > 0) {
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
			    option.setSequenceId(candidateOrder != null ? candidateOrder.intValue()
				    : dummyCandidateOrder++);
			    item.getOptions().add(option);
			}
		    }

		    toolContentObj.getQuestions().add(item);
		}
	    }

	} catch (WDDXProcessorConversionException e) {
	    SurveyServiceImpl.log.error("Unable to content for activity " + toolContentObj.getTitle()
		    + "properly due to a WDDXProcessorConversionException.", e);
	    throw new ToolException(
		    "Invalid import data format for activity "
			    + toolContentObj.getTitle()
			    + "- WDDX caused an exception. Some data from the design will have been lost. See log for more details.");
	}

	surveyDao.saveObject(toolContentObj);

    }

    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

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

    public ILessonService getLessonService() {
	return lessonService;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    /**
     * Finds out which lesson the given tool content belongs to and returns its monitoring users.
     * 
     * @param sessionId
     *                tool session ID
     * @return list of teachers that monitor the lesson which contains the tool with given session ID
     */
    public List<User> getMonitorsByToolSessionId(Long sessionId) {
	return getLessonService().getMonitorsByToolSessionId(sessionId);
    }

    public SurveyOutputFactory getSurveyOutputFactory() {
	return surveyOutputFactory;
    }

    public void setSurveyOutputFactory(SurveyOutputFactory surveyOutputFactory) {
	this.surveyOutputFactory = surveyOutputFactory;
    }

    /**
     * {@inheritDoc}
     */
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

    public void deleteCondition(SurveyCondition condition) {
	surveyDao.deleteCondition(condition);
    }

    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getSurveyOutputFactory().getSupportedDefinitionClasses(definitionType);
    }
}