/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.AnswerDTO;
import org.lamsfoundation.lams.tool.mc.McOptionDTO;
import org.lamsfoundation.lams.tool.mc.McQuestionDTO;
import org.lamsfoundation.lams.tool.mc.McSessionMarkDTO;
import org.lamsfoundation.lams.tool.mc.McStringComparator;
import org.lamsfoundation.lams.tool.mc.McUserMarkDTO;
import org.lamsfoundation.lams.tool.mc.ReflectionDTO;
import org.lamsfoundation.lams.tool.mc.dao.IMcContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcOptionsContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcQueContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcSessionDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcUserDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcUsrAttemptDAO;
import org.lamsfoundation.lams.tool.mc.dto.ToolOutputDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.util.McSessionComparator;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.ExcelUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.dao.DataAccessException;

/**
 *
 * The POJO implementation of Mc service. All business logics of MCQ tool are implemented in this class. It translate
 * the request from presentation layer and perform appropriate database operation.
 *
 * @author Ozgur Demirtas
 */
public class McServicePOJO
	implements IMcService, ToolContentManager, ToolSessionManager, ToolRestManager, McAppConstants {
    private static Logger logger = Logger.getLogger(McServicePOJO.class.getName());

    private IMcContentDAO mcContentDAO;
    private IMcQueContentDAO mcQueContentDAO;
    private IMcOptionsContentDAO mcOptionsContentDAO;
    private IMcSessionDAO mcSessionDAO;
    private IMcUserDAO mcUserDAO;
    private IMcUsrAttemptDAO mcUsrAttemptDAO;
    private MCOutputFactory mcOutputFactory;

    private IAuditService auditService;
    private IUserManagementService userManagementService;
    private ILearnerService learnerService;
    private ILamsToolService toolService;
    private IToolContentHandler mcToolContentHandler = null;
    private IExportToolContentService exportContentService;
    private IGradebookService gradebookService;

    private ICoreNotebookService coreNotebookService;

    private MessageService messageService;

    public McServicePOJO() {
    }

    @Override
    public McQueUsr checkLeaderSelectToolForSessionLeader(McQueUsr user, Long toolSessionId) {
	if ((user == null) || (toolSessionId == null)) {
	    return null;
	}

	McSession mcSession = mcSessionDAO.getMcSessionById(toolSessionId);
	McQueUsr leader = mcSession.getGroupLeader();
	// check leader select tool for a leader only in case QA tool doesn't know it. As otherwise it will screw
	// up previous scratches done
	if (leader == null) {

	    Long leaderUserId = toolService.getLeaderUserId(toolSessionId, user.getQueUsrId().intValue());
	    if (leaderUserId != null) {

		leader = getMcUserBySession(leaderUserId, mcSession.getUid());

		// create new user in a DB
		if (leader == null) {
		    McServicePOJO.logger.debug("creating new user with userId: " + leaderUserId);
		    User leaderDto = (User) userManagementService.findById(User.class, leaderUserId.intValue());
		    String userName = leaderDto.getLogin();
		    String fullName = leaderDto.getFirstName() + " " + leaderDto.getLastName();
		    leader = new McQueUsr(leaderUserId, userName, fullName, mcSession, new TreeSet());
		    mcUserDAO.saveMcUser(user);
		}

		// set group leader
		mcSession.setGroupLeader(leader);
		mcSessionDAO.updateMcSession(mcSession);
	    }
	}

	return leader;
    }

    @Override
    public void copyAnswersFromLeader(McQueUsr user, McQueUsr leader) {

	if ((user == null) || (leader == null) || user.getUid().equals(leader.getUid())) {
	    return;
	}

	List<McUsrAttempt> leaderAttempts = this.getFinalizedUserAttempts(leader);
	for (McUsrAttempt leaderAttempt : leaderAttempts) {

	    McQueContent question = leaderAttempt.getMcQueContent();
	    McUsrAttempt userAttempt = mcUsrAttemptDAO.getUserAttemptByQuestion(user.getUid(), question.getUid());

	    // if response doesn't exist - created mcUsrAttempt in the db
	    if (userAttempt == null) {
		userAttempt = new McUsrAttempt(leaderAttempt.getAttemptTime(), question, user,
			leaderAttempt.getMcOptionsContent(), leaderAttempt.getMark(), leaderAttempt.isPassed(),
			leaderAttempt.isAttemptCorrect());
		mcUsrAttemptDAO.saveMcUsrAttempt(userAttempt);

		// if it's been changed by the leader
	    } else if (leaderAttempt.getAttemptTime().compareTo(userAttempt.getAttemptTime()) != 0) {
		userAttempt.setMcOptionsContent(leaderAttempt.getMcOptionsContent());
		userAttempt.setAttemptTime(leaderAttempt.getAttemptTime());
		this.updateMcUsrAttempt(userAttempt);
	    }

	    user.setNumberOfAttempts(leader.getNumberOfAttempts());
	    user.setLastAttemptTotalMark(leader.getLastAttemptTotalMark());
	    this.updateMcQueUsr(user);
	}
    }

    @Override
    public void createMc(McContent mcContent) throws McApplicationException {
	try {
	    mcContentDAO.saveMcContent(mcContent);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is creating mc content: " + e.getMessage(),
		    e);
	}
    }

    @Override
    public McContent getMcContent(Long toolContentId) throws McApplicationException {
	try {
	    return mcContentDAO.findMcContentById(toolContentId);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is loading mc content: " + e.getMessage(), e);
	}
    }

    @Override
    public void setDefineLater(String strToolContentID, boolean value) {

	McContent mcContent = getMcContent(new Long(strToolContentID));
	if (mcContent != null) {
	    mcContent.setDefineLater(value);
	    updateMc(mcContent);
	}
    }

    @Override
    public void updateQuestion(McQueContent mcQueContent) throws McApplicationException {
	try {
	    mcQueContentDAO.updateMcQueContent(mcQueContent);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is updating mc que content: " + e.getMessage(), e);
	}

    }

    @Override
    public McQueContent getQuestionByDisplayOrder(final Long displayOrder, final Long mcContentUid)
	    throws McApplicationException {
	try {
	    return mcQueContentDAO.getQuestionContentByDisplayOrder(displayOrder, mcContentUid);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is getting mc que content by display order: " + e.getMessage(), e);
	}
    }

    @Override
    public List getAllQuestionsSorted(final long mcContentId) throws McApplicationException {
	try {
	    return mcQueContentDAO.getAllQuestionEntriesSorted(mcContentId);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is getting all question entries: " + e.getMessage(), e);
	}
    }

    @Override
    public void saveOrUpdateMcQueContent(McQueContent mcQueContent) throws McApplicationException {
	try {
	    mcQueContentDAO.saveOrUpdateMcQueContent(mcQueContent);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is updating mc que content: " + e.getMessage(), e);
	}
    }

    @Override
    public McContent createQuestions(List<McQuestionDTO> questionDTOs, McContent content) {

	int displayOrder = 0;
	for (McQuestionDTO questionDTO : questionDTOs) {
	    String currentQuestionText = questionDTO.getQuestion();

	    // skip empty questions
	    if (currentQuestionText.isEmpty()) {
		continue;
	    }

	    ++displayOrder;
	    String currentFeedback = questionDTO.getFeedback();
	    String currentMark = questionDTO.getMark();
	    /* set the default mark in case it is not provided */
	    if (currentMark == null) {
		currentMark = "1";
	    }

	    McQueContent question = getQuestionByUid(questionDTO.getUid());

	    // in case question doesn't exist
	    if (question == null) {
		question = new McQueContent(currentQuestionText, new Integer(displayOrder), new Integer(currentMark),
			currentFeedback, content, null, null);

		// adding a new question to content
		content.getMcQueContents().add(question);
		question.setMcContent(content);

		// in case question exists already
	    } else {

		question.setQuestion(currentQuestionText);
		question.setFeedback(currentFeedback);
		question.setDisplayOrder(new Integer(displayOrder));
		question.setMark(new Integer(currentMark));
	    }

	    // persist candidate answers
	    List<McOptionDTO> optionDTOs = questionDTO.getListCandidateAnswersDTO();
	    Set<McOptsContent> oldOptions = question.getMcOptionsContents();
	    Set<McOptsContent> newOptions = new HashSet<McOptsContent>();
	    int displayOrderOption = 1;
	    for (McOptionDTO optionDTO : optionDTOs) {

		Long optionUid = optionDTO.getUid();
		String optionText = optionDTO.getCandidateAnswer();
		boolean isCorrectOption = "Correct".equals(optionDTO.getCorrect());

		//find persisted option if it exists
		McOptsContent option = new McOptsContent();
		for (McOptsContent oldOption : oldOptions) {
		    if (oldOption.getUid().equals(optionUid)) {
			option = oldOption;
		    }
		}

		option.setDisplayOrder(displayOrderOption);
		option.setCorrectOption(isCorrectOption);
		option.setMcQueOptionText(optionText);
		option.setMcQueContent(question);

		newOptions.add(option);
		displayOrderOption++;
	    }

	    question.setMcOptionsContents(newOptions);

	    // updating the existing question content
	    updateQuestion(question);

	}
	return content;
    }

    @Override
    public void releaseQuestionsFromCache(McContent content) {
	for (McQueContent question : (Set<McQueContent>) content.getMcQueContents()) {
	    mcQueContentDAO.releaseQuestionFromCache(question);
	}
    }

    @Override
    public McQueUsr createMcUser(Long toolSessionID) throws McApplicationException {
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userId = toolUser.getUserID().longValue();
	    String userName = toolUser.getLogin();
	    String fullName = toolUser.getFirstName() + " " + toolUser.getLastName();
	    McSession mcSession = getMcSessionById(toolSessionID.longValue());

	    McQueUsr user = new McQueUsr(userId, userName, fullName, mcSession, new TreeSet());
	    mcUserDAO.saveMcUser(user);

	    return user;
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is creating mc QueUsr: " + e.getMessage(), e);
	}
    }

    @Override
    public void updateMcQueUsr(McQueUsr mcQueUsr) throws McApplicationException {
	try {
	    mcUserDAO.updateMcUser(mcQueUsr);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is updating mc QueUsr: " + e.getMessage(), e);
	}
    }

    @Override
    public McQueUsr getMcUserBySession(final Long queUsrId, final Long mcSessionUid) throws McApplicationException {
	try {
	    return mcUserDAO.getMcUserBySession(queUsrId, mcSessionUid);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is getting mc QueUsr: " + e.getMessage(), e);
	}
    }

    @Override
    public McQueUsr getMcUserByUID(Long uid) throws McApplicationException {
	try {
	    return mcUserDAO.getMcUserByUID(uid);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is getting the mc QueUsr by uid." + e.getMessage(), e);
	}
    }

    @Override
    public List<McUserMarkDTO> getPagedUsersBySession(Long sessionId, int page, int size, String sortBy,
	    String sortOrder, String searchString) {
	return mcUserDAO.getPagedUsersBySession(sessionId, page, size, sortBy, sortOrder, searchString);
    }

    @Override
    public int getCountPagedUsersBySession(Long sessionId, String searchString) {
	return mcUserDAO.getCountPagedUsersBySession(sessionId, searchString);
    }

    @Override
    public String getLocalizedMessage(String key) {
	return messageService.getMessage(key);
    }

    @Override
    public void saveUserAttempt(McQueUsr user, List<AnswerDTO> answerDtos) {

	Date attemptTime = new Date(System.currentTimeMillis());

	for (AnswerDTO answerDto : answerDtos) {

	    Long questionUid = answerDto.getQuestionUid();
	    McQueContent question = this.getQuestionByUid(questionUid);
	    if (question == null) {
		throw new McApplicationException(
			"Can't find question with specified question uid: " + answerDto.getQuestionUid());
	    }

	    McOptsContent answerOption = answerDto.getAnswerOption();
	    if (answerOption != null) {

		Integer mark = answerDto.getMark();
		boolean passed = user.isMarkPassed(mark);
		boolean isAttemptCorrect = answerDto.isAttemptCorrect();

		McUsrAttempt userAttempt = this.getUserAttemptByQuestion(user.getUid(), questionUid);
		if (userAttempt != null) {

		    McOptsContent previosAnswer = userAttempt.getMcOptionsContent();
		    // check if answer hasn't been changed since the last time
		    if (previosAnswer.getUid().equals(answerOption.getUid())) {
			// don't save anything
			continue;

		    } else {
			// in case answer has been changed update userttempt
			userAttempt.setAttemptTime(attemptTime);
			userAttempt.setMcOptionsContent(answerOption);
			userAttempt.setMark(mark);
			userAttempt.setPassed(passed);
			userAttempt.setAttemptCorrect(isAttemptCorrect);
		    }

		} else {
		    // create new userAttempt
		    userAttempt = new McUsrAttempt(attemptTime, question, user, answerOption, mark, passed,
			    isAttemptCorrect);
		}

		mcUsrAttemptDAO.saveMcUsrAttempt(userAttempt);

	    }
	}
    }

    @Override
    public void updateMcUsrAttempt(McUsrAttempt mcUsrAttempt) throws McApplicationException {
	try {
	    mcUsrAttemptDAO.updateMcUsrAttempt(mcUsrAttempt);
	} catch (DataAccessException e) {
	    throw new McApplicationException("Exception occured when lams is updating mc UsrAttempt: " + e.getMessage(),
		    e);
	}
    }

    @Override
    public List<AnswerDTO> getAnswersFromDatabase(McContent mcContent, McQueUsr user) {
	List<AnswerDTO> answerDtos = new LinkedList<AnswerDTO>();
	List<McQueContent> questions = this.getQuestionsByContentUid(mcContent.getUid());

	for (McQueContent question : questions) {
	    AnswerDTO answerDto = new AnswerDTO();
	    Set<McOptsContent> optionSet = question.getMcOptionsContents();
	    List<McOptsContent> optionList = new LinkedList<McOptsContent>(optionSet);

	    boolean randomize = mcContent.isRandomize();
	    if (randomize) {
		ArrayList<McOptsContent> shuffledList = new ArrayList<McOptsContent>(optionList);
		Collections.shuffle(shuffledList);
		optionList = new LinkedList<McOptsContent>(shuffledList);
	    }

	    answerDto.setQuestion(question.getQuestion());
	    answerDto.setDisplayOrder(question.getDisplayOrder().toString());
	    answerDto.setQuestionUid(question.getUid());

	    answerDto.setMark(question.getMark());
	    answerDto.setOptions(optionList);

	    answerDtos.add(answerDto);
	}

	// populate answers
	if (user != null) {

	    for (AnswerDTO answerDto : answerDtos) {
		Long questionUid = answerDto.getQuestionUid();

		McUsrAttempt dbAttempt = this.getUserAttemptByQuestion(user.getUid(), questionUid);
		if (dbAttempt != null) {
		    Long selectedOptionUid = dbAttempt.getMcOptionsContent().getUid();

		    // mark selected option as selected
		    for (McOptsContent option : answerDto.getOptions()) {
			if (selectedOptionUid.equals(option.getUid())) {
			    option.setSelected(true);
			}
		    }
		}
	    }
	}

	return answerDtos;
    }

    @Override
    public List<McSessionMarkDTO> buildGroupsMarkData(McContent mcContent, boolean isFullAttemptDetailsRequired) {
	List<McSessionMarkDTO> listMonitoredMarksContainerDTO = new LinkedList<McSessionMarkDTO>();
	Set<McSession> sessions = new TreeSet<McSession>(new McSessionComparator());
	sessions.addAll(mcContent.getMcSessions());
	int numQuestions = mcContent.getMcQueContents().size();

	for (McSession session : sessions) {

	    McSessionMarkDTO mcSessionMarkDTO = new McSessionMarkDTO();
	    mcSessionMarkDTO.setSessionId(session.getMcSessionId().toString());
	    mcSessionMarkDTO.setSessionName(session.getSession_name().toString());

	    Set<McQueUsr> sessionUsers = session.getMcQueUsers();
	    Iterator<McQueUsr> usersIterator = sessionUsers.iterator();

	    Map<String, McUserMarkDTO> mapSessionUsersData = new TreeMap<String, McUserMarkDTO>(
		    new McStringComparator());
	    Long mapIndex = new Long(1);

	    while (usersIterator.hasNext()) {
		McQueUsr user = usersIterator.next();

		McUserMarkDTO mcUserMarkDTO = new McUserMarkDTO();
		mcUserMarkDTO.setSessionId(session.getMcSessionId().toString());
		mcUserMarkDTO.setSessionName(session.getSession_name().toString());
		mcUserMarkDTO.setFullName(user.getFullname());
		mcUserMarkDTO.setUserGroupLeader(session.isUserGroupLeader(user));
		mcUserMarkDTO.setUserName(user.getUsername());
		mcUserMarkDTO.setQueUsrId(user.getUid().toString());

		if (isFullAttemptDetailsRequired) {

		    // The marks for the user must be listed in the display order of the question.
		    // Other parts of the code assume that the questions will be in consecutive display
		    // order starting 1 (e.g. 1, 2, 3, not 1, 3, 4) so we set up an array and use
		    // the ( display order - 1) as the index (arrays start at 0, rather than 1 hence -1)
		    // The user must answer all questions, so we can assume that they will have marks
		    // for all questions or no questions.
		    // At present there can only be one answer for each question but there may be more
		    // than one in the future and if so, we don't want to count the mark twice hence
		    // we need to check if we've already processed this question in the total.
		    Integer[] userMarks = new Integer[numQuestions];
		    String[] answeredOptions = new String[numQuestions];
		    Date attemptTime = null;
		    List<McUsrAttempt> finalizedUserAttempts = this.getFinalizedUserAttempts(user);
		    long totalMark = 0;
		    for (McUsrAttempt attempt : finalizedUserAttempts) {
			Integer displayOrder = attempt.getMcQueContent().getDisplayOrder();
			int arrayIndex = (displayOrder != null) && (displayOrder.intValue() > 0)
				? displayOrder.intValue() - 1 : 1;
			if (userMarks[arrayIndex] == null) {

			    // We get the mark for the attempt if the answer is correct and we don't allow
			    // retries, or if the answer is correct and the learner has met the passmark if
			    // we do allow retries.
			    boolean isRetries = session.getMcContent().isRetries();
			    Integer mark = attempt.getMarkForShow(isRetries);
			    userMarks[arrayIndex] = mark;
			    totalMark += mark.intValue();

			    // find out the answered option's sequential letter - A,B,C...
			    String answeredOptionLetter = "";
			    int optionCount = 1;
			    for (McOptsContent option : (Set<McOptsContent>) attempt.getMcQueContent()
				    .getMcOptionsContents()) {
				if (attempt.getMcOptionsContent().getUid().equals(option.getUid())) {
				    answeredOptionLetter = String.valueOf((char) ((optionCount + 'A') - 1));
				    break;
				}
				optionCount++;
			    }
			    answeredOptions[arrayIndex] = answeredOptionLetter;
			}
			// get the attempt time, (NB all questions will have the same attempt time)
			// Not efficient, since we assign this value for each attempt
			attemptTime = attempt.getAttemptTime();
		    }

		    mcUserMarkDTO.setMarks(userMarks);
		    mcUserMarkDTO.setAnsweredOptions(answeredOptions);
		    mcUserMarkDTO.setAttemptTime(attemptTime);
		    mcUserMarkDTO.setTotalMark(new Long(totalMark));

		} else {
		    int totalMark = mcUsrAttemptDAO.getUserTotalMark(user.getUid());
		    mcUserMarkDTO.setTotalMark(new Long(totalMark));
		}

		mapSessionUsersData.put(mapIndex.toString(), mcUserMarkDTO);
		mapIndex = new Long(mapIndex.longValue() + 1);
	    }

	    mcSessionMarkDTO.setUserMarks(mapSessionUsersData);
	    listMonitoredMarksContainerDTO.add(mcSessionMarkDTO);
	}

	return listMonitoredMarksContainerDTO;
    }

    @Override
    public List<McUsrAttempt> getFinalizedUserAttempts(final McQueUsr user) throws McApplicationException {
	try {
	    return mcUsrAttemptDAO.getFinalizedUserAttempts(user.getUid());
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is getting the learner's attempts by user id and que content id and attempt order: "
			    + e.getMessage(),
		    e);
	}
    }

    @Override
    public McUsrAttempt getUserAttemptByQuestion(Long queUsrUid, Long mcQueContentId) throws McApplicationException {
	try {
	    return mcUsrAttemptDAO.getUserAttemptByQuestion(queUsrUid, mcQueContentId);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is getting the learner's attempts by user id and que content id and attempt order: "
			    + e.getMessage(),
		    e);
	}
    }
    
    @Override
    public List<ToolOutputDTO> getLearnerMarksByContentId(Long toolContentId) {
	return mcUsrAttemptDAO.getLearnerMarksByContentId(toolContentId);
    }

    @Override
    public List<McQueContent> getQuestionsByContentUid(final Long contentUid) throws McApplicationException {
	try {
	    return mcQueContentDAO.getQuestionsByContentUid(contentUid.longValue());
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is getting by uid  mc question content: " + e.getMessage(), e);
	}
    }

    @Override
    public List refreshQuestionContent(final Long mcContentId) throws McApplicationException {
	try {
	    return mcQueContentDAO.refreshQuestionContent(mcContentId);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is refreshing  mc question content: " + e.getMessage(), e);
	}

    }

    @Override
    public void removeMcQueContent(McQueContent mcQueContent) throws McApplicationException {
	try {
	    mcQueContentDAO.removeMcQueContent(mcQueContent);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is removing mc question content: " + e.getMessage(), e);
	}
    }

    @Override
    public List<McOptionDTO> getOptionDtos(Long mcQueContentId) throws McApplicationException {
	try {
	    return mcOptionsContentDAO.getOptionDtos(mcQueContentId);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is populating candidate answers dto" + e.getMessage(), e);
	}
    }

    @Override
    public McSession getMcSessionById(Long mcSessionId) throws McApplicationException {
	try {
	    return mcSessionDAO.getMcSessionById(mcSessionId);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is retrieving by id mc session : " + e.getMessage(), e);
	}
    }

    @Override
    public void updateMc(McContent mc) throws McApplicationException {
	try {
	    mcContentDAO.updateMcContent(mc);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is updating" + " the mc content: " + e.getMessage(), e);
	}
    }

    @Override
    public Integer[] getMarkStatistics(McSession mcSession) {
	return mcUserDAO.getMarkStatisticsForSession(mcSession.getUid());
    }

    @Override
    public List<McOptsContent> findOptionsByQuestionUid(Long mcQueContentId) throws McApplicationException {
	try {
	    return mcOptionsContentDAO.findMcOptionsContentByQueId(mcQueContentId);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is finding by que id" + " the mc options: " + e.getMessage(), e);
	}
    }

    @Override
    public McQueContent getQuestionByUid(Long uid) {
	if (uid == null) {
	    return null;
	}

	return mcQueContentDAO.findMcQuestionContentByUid(uid);
    }

    @Override
    public void updateMcOptionsContent(McOptsContent mcOptsContent) throws McApplicationException {
	try {
	    mcOptionsContentDAO.updateMcOptionsContent(mcOptsContent);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is updating" + " the mc options content: " + e.getMessage(), e);
	}
    }

    @Override
    public void changeUserAttemptMark(Long userAttemptUid, Integer newMark) {
	if (newMark == null) {
	    return;
	}

	McUsrAttempt userAttempt = mcUsrAttemptDAO.getUserAttemptByUid(userAttemptUid);
	Integer userId = userAttempt.getMcQueUsr().getQueUsrId().intValue();
	Long userUid = userAttempt.getMcQueUsr().getUid();
	Long toolSessionId = userAttempt.getMcQueUsr().getMcSession().getMcSessionId();
	Integer oldMark = userAttempt.getMark();
	int oldTotalMark = mcUsrAttemptDAO.getUserTotalMark(userUid);

	int totalMark = (oldMark == null) ? oldTotalMark + newMark : (oldTotalMark - oldMark) + newMark;

	//update mark for one particular question
	userAttempt.setMark(newMark);
	mcUsrAttemptDAO.saveMcUsrAttempt(userAttempt);

	//update user's total mark
	McQueUsr user = userAttempt.getMcQueUsr();
	user.setLastAttemptTotalMark(totalMark);
	updateMcQueUsr(user);

	// propagade changes to Gradebook
	gradebookService.updateActivityMark(new Double(totalMark), null, userId, toolSessionId, false);

	// record mark change with audit service
	auditService.logMarkChange(McAppConstants.MY_SIGNATURE, userAttempt.getMcQueUsr().getQueUsrId(),
		userAttempt.getMcQueUsr().getUsername(), "" + oldMark, "" + totalMark);

    }

    @Override
    public void recalculateUserAnswers(McContent content, Set<McQueContent> oldQuestions,
	    List<McQuestionDTO> questionDTOs, List<McQuestionDTO> deletedQuestions) {

	// create list of modified questions
	List<McQuestionDTO> modifiedQuestions = new ArrayList<McQuestionDTO>();
	// create list of modified question marks
	List<McQuestionDTO> modifiedQuestionsMarksOnly = new ArrayList<McQuestionDTO>();
	for (McQueContent oldQuestion : oldQuestions) {
	    for (McQuestionDTO questionDTO : questionDTOs) {
		if (oldQuestion.getUid().equals(questionDTO.getUid())) {

		    boolean isQuestionModified = false;
		    boolean isQuestionMarkModified = false;

		    // question is different
		    if (!oldQuestion.getQuestion().equals(questionDTO.getQuestion())) {
			isQuestionModified = true;
		    }

		    // mark is different
		    if (oldQuestion.getMark().intValue() != (new Integer(questionDTO.getMark())).intValue()) {
			isQuestionMarkModified = true;
		    }

		    // options are different
		    Set<McOptsContent> oldOptions = oldQuestion.getMcOptionsContents();
		    List<McOptionDTO> optionDTOs = questionDTO.getListCandidateAnswersDTO();
		    for (McOptsContent oldOption : oldOptions) {
			for (McOptionDTO optionDTO : optionDTOs) {
			    if (oldOption.getUid().equals(optionDTO.getUid())) {

				if (!StringUtils.equals(oldOption.getMcQueOptionText(), optionDTO.getCandidateAnswer())
					|| (oldOption.isCorrectOption() != "Correct".equals(optionDTO.getCorrect()))) {
				    isQuestionModified = true;
				}
			    }
			}
		    }

		    if (isQuestionModified) {
			modifiedQuestions.add(questionDTO);

		    } else if (isQuestionMarkModified) {
			modifiedQuestionsMarksOnly.add(questionDTO);
		    }
		}
	    }
	}

	Set<McSession> sessionList = content.getMcSessions();
	for (McSession session : sessionList) {
	    Long toolSessionId = session.getMcSessionId();
	    Set<McQueUsr> sessionUsers = session.getMcQueUsers();

	    for (McQueUsr user : sessionUsers) {

		final int oldTotalMark = mcUsrAttemptDAO.getUserTotalMark(user.getUid());
		int newTotalMark = oldTotalMark;

		// get all finished user results
		List<McUsrAttempt> userAttempts = getFinalizedUserAttempts(user);
		Iterator<McUsrAttempt> iter = userAttempts.iterator();
		while (iter.hasNext()) {
		    McUsrAttempt userAttempt = iter.next();

		    McQueContent question = userAttempt.getMcQueContent();

		    boolean isRemoveQuestionResult = false;

		    // [+] if the question mark is modified
		    for (McQuestionDTO modifiedQuestion : modifiedQuestionsMarksOnly) {
			if (question.getUid().equals(modifiedQuestion.getUid())) {
			    Integer newQuestionMark = new Integer(modifiedQuestion.getMark());
			    Integer oldQuestionMark = question.getMark();
			    Integer newActualMark = (userAttempt.getMark() * newQuestionMark) / oldQuestionMark;

			    newTotalMark += newActualMark - userAttempt.getMark();

			    // update question answer's mark
			    userAttempt.setMark(newActualMark);
			    mcUsrAttemptDAO.saveMcUsrAttempt(userAttempt);

			    break;
			}

		    }

		    // [+] if the question is modified
		    for (McQuestionDTO modifiedQuestion : modifiedQuestions) {
			if (question.getUid().equals(modifiedQuestion.getUid())) {
			    isRemoveQuestionResult = true;
			    break;
			}
		    }

		    // [+] if the question was removed
		    for (McQuestionDTO deletedQuestion : deletedQuestions) {
			if (question.getUid().equals(deletedQuestion.getUid())) {
			    isRemoveQuestionResult = true;
			    break;
			}
		    }

		    if (isRemoveQuestionResult) {

			Integer oldMark = userAttempt.getMark();
			if (oldMark != null) {
			    newTotalMark -= oldMark;
			}

			iter.remove();
			mcUsrAttemptDAO.removeAttempt(userAttempt);
		    }

		    // [+] doing nothing if the new question was added

		}

		// propagade new total mark to Gradebook if it was changed
		if (newTotalMark != oldTotalMark) {
		    gradebookService.updateActivityMark(new Double(newTotalMark), null, user.getQueUsrId().intValue(),
			    toolSessionId, false);
		}

	    }
	}

    }

    @Override
    public byte[] prepareSessionDataSpreadsheet(McContent mcContent) throws IOException {

	Set<McQueContent> questions = mcContent.getMcQueContents();
	int maxOptionsInQuestion = 0;
	for (McQueContent question : questions) {
	    if (question.getMcOptionsContents().size() > maxOptionsInQuestion) {
		maxOptionsInQuestion = question.getMcOptionsContents().size();
	    }
	}

	int totalNumberOfUsers = 0;
	for (McSession session : (Set<McSession>) mcContent.getMcSessions()) {
	    totalNumberOfUsers += session.getMcQueUsers().size();
	}

	List<McSessionMarkDTO> sessionMarkDTOs = this.buildGroupsMarkData(mcContent, true);

	// create an empty excel file
	HSSFWorkbook wb = new HSSFWorkbook();
	HSSFCellStyle greenColor = wb.createCellStyle();
	greenColor.setFillForegroundColor(IndexedColors.LIME.getIndex());
	greenColor.setFillPattern(CellStyle.SOLID_FOREGROUND);
	Font whiteFont = wb.createFont();
	whiteFont.setColor(IndexedColors.WHITE.getIndex());
	whiteFont.setFontName(ExcelUtil.DEFAULT_FONT_NAME);
	greenColor.setFont(whiteFont);

	// ======================================================= Report by question IRA page
	// =======================================

	HSSFSheet sheet = wb.createSheet(messageService.getMessage("label.report.by.question"));

	HSSFRow row;
	HSSFCell cell;
	int rowCount = 0;

	row = sheet.createRow(rowCount++);
	int count = 0;
	cell = row.createCell(count++);
	cell.setCellValue(messageService.getMessage("label.question"));
	for (int optionCount = 0; optionCount < maxOptionsInQuestion; optionCount++) {
	    cell = row.createCell(count++);
	    cell.setCellValue(String.valueOf((char) (optionCount + 'A')));
	}
	cell = row.createCell(count++);
	cell.setCellValue(messageService.getMessage("label.not.available"));

	for (McQueContent question : questions) {

	    row = sheet.createRow(rowCount);
	    count = 0;

	    cell = row.createCell(count++);
	    cell.setCellValue(rowCount);
	    rowCount++;

	    int totalPercentage = 0;
	    for (McOptsContent option : (Set<McOptsContent>) question.getMcOptionsContents()) {
		int optionAttemptCount = mcUsrAttemptDAO.getAttemptsCountPerOption(option.getUid());
		cell = row.createCell(count++);
		int percentage = (optionAttemptCount * 100) / totalNumberOfUsers;
		cell.setCellValue(percentage + "%");
		totalPercentage += percentage;
		if (option.isCorrectOption()) {
		    cell.setCellStyle(greenColor);
		}
	    }
	    cell = row.createCell(maxOptionsInQuestion + 1);
	    cell.setCellValue((100 - totalPercentage) + "%");
	}

	rowCount++;
	row = sheet.createRow(rowCount++);
	cell = row.createCell(0);
	cell.setCellValue(messageService.getMessage("label.legend"));
	row = sheet.createRow(rowCount++);
	cell = row.createCell(0);
	cell.setCellValue(messageService.getMessage("label.denotes.correct.answer"));
	cell.setCellStyle(greenColor);
	cell = row.createCell(1);
	cell.setCellStyle(greenColor);
	cell = row.createCell(2);
	cell.setCellStyle(greenColor);

	// ======================================================= Report by student IRA page
	// =======================================

	sheet = wb.createSheet(messageService.getMessage("label.report.by.student"));
	rowCount = 0;

	row = sheet.createRow(rowCount++);
	count = 2;
	for (int questionCount = 1; questionCount <= questions.size(); questionCount++) {
	    cell = row.createCell(count++);
	    cell.setCellValue(messageService.getMessage("label.question") + questionCount);
	}
	cell = row.createCell(count++);
	cell.setCellValue(messageService.getMessage("label.total"));
	cell = row.createCell(count++);
	cell.setCellValue(messageService.getMessage("label.total") + " %");

	row = sheet.createRow(rowCount++);
	count = 1;
	ArrayList<String> correctAnswers = new ArrayList<String>();
	cell = row.createCell(count++);
	cell.setCellValue(messageService.getMessage("label.correct.answer"));
	for (McQueContent question : questions) {

	    // find out the correct answer's sequential letter - A,B,C...
	    String correctAnswerLetter = "";
	    int answerCount = 1;
	    for (McOptsContent option : (Set<McOptsContent>) question.getMcOptionsContents()) {
		if (option.isCorrectOption()) {
		    correctAnswerLetter = String.valueOf((char) ((answerCount + 'A') - 1));
		    break;
		}
		answerCount++;
	    }
	    cell = row.createCell(count++);
	    cell.setCellValue(correctAnswerLetter);
	    correctAnswers.add(correctAnswerLetter);
	}

	row = sheet.createRow(rowCount++);
	count = 0;
	cell = row.createCell(count++);
	cell.setCellValue(messageService.getMessage("group.label"));
	cell = row.createCell(count++);
	cell.setCellValue(messageService.getMessage("label.learner"));

	ArrayList<Integer> totalPercentList = new ArrayList<Integer>();
	int[] numberOfCorrectAnswersPerQuestion = new int[questions.size()];
	for (McSessionMarkDTO sessionMarkDTO : sessionMarkDTOs) {
	    Map<String, McUserMarkDTO> usersMarksMap = sessionMarkDTO.getUserMarks();

	    for (McUserMarkDTO userMark : usersMarksMap.values()) {
		row = sheet.createRow(rowCount++);
		count = 0;
		cell = row.createCell(count++);
		cell.setCellValue(sessionMarkDTO.getSessionName());

		cell = row.createCell(count++);
		cell.setCellValue(userMark.getFullName());

		String[] answeredOptions = userMark.getAnsweredOptions();
		int numberOfCorrectlyAnsweredByUser = 0;
		for (int i = 0; i < answeredOptions.length; i++) {
		    String answeredOption = answeredOptions[i];
		    cell = row.createCell(count++);
		    cell.setCellValue(answeredOption);
		    if (StringUtils.equals(answeredOption, correctAnswers.get(i))) {
			cell.setCellStyle(greenColor);
			numberOfCorrectlyAnsweredByUser++;
			numberOfCorrectAnswersPerQuestion[count - 3]++;
		    }
		}

		cell = row.createCell(count++);
		cell.setCellValue(new Long(userMark.getTotalMark()));

		int totalPercents = (numberOfCorrectlyAnsweredByUser * 100) / questions.size();
		totalPercentList.add(totalPercents);
		cell = row.createCell(count++);
		cell.setCellValue(totalPercents + "%");
	    }

	    rowCount++;
	}

	// ave
	row = sheet.createRow(rowCount++);
	count = 1;
	cell = row.createCell(count++);
	cell.setCellValue(messageService.getMessage("label.ave"));
	for (int numberOfCorrectAnswers : numberOfCorrectAnswersPerQuestion) {
	    cell = row.createCell(count++);
	    cell.setCellValue(((numberOfCorrectAnswers * 100) / totalPercentList.size()) + "%");
	}

	// class mean
	Integer[] totalPercents = totalPercentList.toArray(new Integer[0]);
	Arrays.sort(totalPercents);
	int sum = 0;
	for (int i = 0; i < totalPercents.length; i++) {
	    sum += totalPercents[i];
	}
	row = sheet.createRow(rowCount++);
	cell = row.createCell(1);
	cell.setCellValue(messageService.getMessage("label.class.mean"));
	if (totalPercents.length != 0) {
	    int classMean = sum / totalPercents.length;
	    cell = row.createCell(questions.size() + 3);
	    cell.setCellValue(classMean + "%");
	}

	// median
	row = sheet.createRow(rowCount++);
	cell = row.createCell(1);
	cell.setCellValue(messageService.getMessage("label.median"));
	if (totalPercents.length != 0) {
	    int median;
	    int middle = totalPercents.length / 2;
	    if ((totalPercents.length % 2) == 1) {
		median = totalPercents[middle];
	    } else {
		median = (int) ((totalPercents[middle - 1] + totalPercents[middle]) / 2.0);
	    }
	    cell = row.createCell(questions.size() + 3);
	    cell.setCellValue(median + "%");
	}

	row = sheet.createRow(rowCount++);
	cell = row.createCell(0);
	cell.setCellValue(messageService.getMessage("label.legend"));

	row = sheet.createRow(rowCount++);
	cell = row.createCell(0);
	cell.setCellValue(messageService.getMessage("label.denotes.correct.answer"));
	cell.setCellStyle(greenColor);
	cell = row.createCell(1);
	cell.setCellStyle(greenColor);
	cell = row.createCell(2);
	cell.setCellStyle(greenColor);

	// ======================================================= Marks page
	// =======================================

	sheet = wb.createSheet("Marks");

	rowCount = 0;
	count = 0;

	row = sheet.createRow(rowCount++);
	for (McQueContent question : questions) {
	    cell = row.createCell(2 + count++);
	    cell.setCellValue(messageService.getMessage("label.monitoring.downloadMarks.question.mark",
		    new Object[] { count, question.getMark() }));
	}

	for (McSessionMarkDTO sessionMarkDTO : sessionMarkDTOs) {
	    Map<String, McUserMarkDTO> usersMarksMap = sessionMarkDTO.getUserMarks();

	    String currentSessionName = sessionMarkDTO.getSessionName();

	    row = sheet.createRow(rowCount++);

	    cell = row.createCell(0);
	    cell.setCellValue(messageService.getMessage("group.label"));

	    cell = row.createCell(1);
	    cell.setCellValue(currentSessionName);
	    cell.setCellStyle(greenColor);

	    rowCount++;
	    count = 0;

	    row = sheet.createRow(rowCount++);

	    cell = row.createCell(count++);
	    cell.setCellValue(messageService.getMessage("label.learner"));

	    cell = row.createCell(count++);
	    cell.setCellValue(messageService.getMessage("label.monitoring.downloadMarks.username"));

	    cell = row.createCell(questions.size() + 2);
	    cell.setCellValue(messageService.getMessage("label.total"));

	    for (McUserMarkDTO userMark : usersMarksMap.values()) {
		row = sheet.createRow(rowCount++);
		count = 0;

		cell = row.createCell(count++);
		cell.setCellValue(userMark.getFullName());

		cell = row.createCell(count++);
		cell.setCellValue(userMark.getUserName());

		Integer[] marks = userMark.getMarks();
		for (int i = 0; i < marks.length; i++) {
		    cell = row.createCell(count++);
		    Integer mark = (marks[i] == null) ? 0 : marks[i];
		    cell.setCellValue(mark);
		}

		cell = row.createCell(count++);
		cell.setCellValue(userMark.getTotalMark());
	    }

	    rowCount++;

	}

	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	wb.write(bos);

	byte[] data = bos.toByteArray();

	return data;
    }

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) {

	if (fromContentId == null) {
	    McServicePOJO.logger.warn("fromContentId is null.");
	    long defaultContentId = getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE);
	    fromContentId = new Long(defaultContentId);
	}

	if (toContentId == null) {
	    McServicePOJO.logger.error("throwing ToolException: toContentId is null");
	    throw new ToolException("toContentId is missing");
	}

	McContent fromContent = mcContentDAO.findMcContentById(fromContentId);

	if (fromContent == null) {
	    McServicePOJO.logger.warn("fromContent is null.");
	    long defaultContentId = getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE);
	    fromContent = mcContentDAO.findMcContentById(defaultContentId);
	}

	McContent toContent = McContent.newInstance(fromContent, toContentId);
	if (toContent == null) {
	    McServicePOJO.logger.error("throwing ToolException: WARNING!, retrieved toContent is null.");
	    throw new ToolException("WARNING! Fail to create toContent. Can't continue!");
	} else {
	    mcContentDAO.saveMcContent(toContent);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	McContent mcContent = mcContentDAO.findMcContentById(toolContentId);
	if (mcContent == null) {
	    McServicePOJO.logger.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (McSession session : (Set<McSession>) mcContent.getMcSessions()) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getMcSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, McAppConstants.MY_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	mcContentDAO.delete(mcContent);
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	McContent mcContent = getMcContent(toolContentId);
	if (mcContent == null) {
	    throw new DataMissingException("mcContent is missing");
	}
	mcContent.setDefineLater(false);
	mcContentDAO.saveMcContent(mcContent);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (McServicePOJO.logger.isDebugEnabled()) {
	    McServicePOJO.logger.debug(
		    "Removing Multiple Choice attempts for user ID " + userId + " and toolContentId " + toolContentId);
	}

	McContent content = mcContentDAO.findMcContentById(toolContentId);
	if (content != null) {
	    for (McSession session : (Set<McSession>) content.getMcSessions()) {
		McQueUsr user = mcUserDAO.getMcUserBySession(userId.longValue(), session.getUid());
		if (user != null) {
		    mcUsrAttemptDAO.removeAllUserAttempts(user.getUid());

		    NotebookEntry entry = getEntry(session.getMcSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			    McAppConstants.MY_SIGNATURE, userId);
		    if (entry != null) {
			mcContentDAO.delete(entry);
		    }

		    if ((session.getGroupLeader() != null) && session.getGroupLeader().getUid().equals(user.getUid())) {
			session.setGroupLeader(null);
		    }

		    mcUserDAO.removeMcUser(user);

		    gradebookService.updateActivityMark(null, null, userId, session.getMcSessionId(), false);
		}
	    }
	}
    }

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	McContent toolContentObj = mcContentDAO.findMcContentById(toolContentId);
	if (toolContentObj == null) {
	    long defaultContentId = getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE);
	    toolContentObj = mcContentDAO.findMcContentById(defaultContentId);
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the multiple choice tool");
	}

	try {
	    // set ToolContentHandler as null to avoid copy file node in repository again.
	    toolContentObj = McContent.newInstance(toolContentObj, toolContentId);
	    toolContentObj.setMcSessions(null);
	    exportContentService.exportToolContent(toolContentId, toolContentObj, mcToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(McImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, mcToolContentHandler, fromVersion,
		    toVersion);
	    if (!(toolPOJO instanceof McContent)) {
		throw new ImportToolContentException(
			"Import MC tool content failed. Deserialized object is " + toolPOJO);
	    }
	    McContent toolContentObj = (McContent) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setMcContentId(toolContentId);
	    toolContentObj.setCreatedBy(newUserUid);
	    mcContentDAO.saveMcContent(toolContentObj);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	McContent content = getMcContent(toolContentId);
	if (content == null) {
	    long defaultToolContentId = getToolDefaultContentIdBySignature(McAppConstants.MY_SIGNATURE);
	    content = getMcContent(defaultToolContentId);
	}
	return mcOutputFactory.getToolOutputDefinitions(content, definitionType);
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return mcContentDAO.findMcContentById(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return mcContentDAO.findMcContentById(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	McContent content = mcContentDAO.findMcContentById(toolContentId);
	for (McSession session : (Set<McSession>) content.getMcSessions()) {
	    if (!session.getMcQueUsers().isEmpty()) {
		return true;
	    }
	}

	return false;
    }

    /**
     * it is possible that the tool session id already exists in the tool sessions table as the users from the same
     * session are involved. existsSession(long toolSessionId)
     *
     * @param toolSessionId
     * @return boolean
     */
    @Override
    public boolean existsSession(Long toolSessionId) {
	McSession mcSession = getMcSessionById(toolSessionId);
	return mcSession != null;
    }

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {

	if (toolSessionId == null) {
	    McServicePOJO.logger.error("toolSessionId is null");
	    throw new ToolException("toolSessionId is missing");
	}

	McContent mcContent = mcContentDAO.findMcContentById(toolContentId);

	// create a new a new tool session if it does not already exist in the tool session table
	if (!existsSession(toolSessionId)) {
	    try {
		McSession mcSession = new McSession(toolSessionId, new Date(System.currentTimeMillis()),
			McSession.INCOMPLETE, toolSessionName, mcContent, new TreeSet());

		mcSessionDAO.saveMcSession(mcSession);

	    } catch (Exception e) {
		McServicePOJO.logger.error("Error creating new toolsession in the db");
		throw new ToolException("Error creating new toolsession in the db: " + e);
	    }
	}
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    McServicePOJO.logger.error("toolSessionId is null");
	    throw new DataMissingException("toolSessionId is missing");
	}

	McSession mcSession = null;
	try {
	    mcSession = getMcSessionById(toolSessionId);
	} catch (McApplicationException e) {
	    throw new DataMissingException("error retrieving mcSession: " + e);
	} catch (Exception e) {
	    throw new ToolException("error retrieving mcSession: " + e);
	}

	if (mcSession == null) {
	    McServicePOJO.logger.error("mcSession is null");
	    throw new DataMissingException("mcSession is missing");
	}

	try {
	    mcSessionDAO.removeMcSession(mcSession);
	    McServicePOJO.logger.debug("mcSession " + mcSession + " has been deleted successfully.");
	} catch (McApplicationException e) {
	    throw new ToolException("error deleting mcSession:" + e);
	}
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {

	if (learnerService == null) {
	    return "dummyNextUrl";
	}

	if (learnerId == null) {
	    McServicePOJO.logger.error("learnerId is null");
	    throw new DataMissingException("learnerId is missing");
	}

	if (toolSessionId == null) {
	    McServicePOJO.logger.error("toolSessionId is null");
	    throw new DataMissingException("toolSessionId is missing");
	}

	McSession mcSession = null;
	try {
	    mcSession = getMcSessionById(toolSessionId);
	} catch (McApplicationException e) {
	    throw new DataMissingException("error retrieving mcSession: " + e);
	} catch (Exception e) {
	    throw new ToolException("error retrieving mcSession: " + e);
	}
	mcSession.setSessionStatus(McAppConstants.COMPLETED);
	mcSessionDAO.updateMcSession(mcSession);

	String nextUrl = learnerService.completeToolSession(toolSessionId, learnerId);
	if (nextUrl == null) {
	    McServicePOJO.logger.error("nextUrl is null");
	    throw new ToolException("nextUrl is null");
	}
	return nextUrl;
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
	    throws DataMissingException, ToolException {
	throw new ToolException("not yet implemented");
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds)
	    throws DataMissingException, ToolException {
	throw new ToolException("not yet implemented");

    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return mcOutputFactory.getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return mcOutputFactory.getToolOutput(name, this, toolSessionId, learnerId);
    }
    
    @Override
    public List<ToolOutput> getToolOutputs(String name, Long toolContentId) {
	return mcOutputFactory.getToolOutputs(name, this, toolContentId);
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	Long userId = user.getUserId().longValue();

	McSession session = getMcSessionById(toolSessionId);
	if ((session == null) || (session.getMcContent() == null)) {
	    return;
	}
	McContent content = session.getMcContent();

	// copy answers only in case leader aware feature is ON
	if (content.isUseSelectLeaderToolOuput()) {

	    McQueUsr mcUser = getMcUserBySession(userId, session.getUid());
	    // create user if he hasn't accessed this activity yet
	    if (mcUser == null) {

		String userName = user.getLogin();
		String fullName = user.getFirstName() + " " + user.getLastName();
		mcUser = new McQueUsr(userId, userName, fullName, session, new TreeSet());
		mcUserDAO.saveMcUser(mcUser);
	    }

	    McQueUsr groupLeader = session.getGroupLeader();

	    // check if leader has submitted answers
	    if ((groupLeader != null) && groupLeader.isResponseFinalised()) {

		// we need to make sure specified user has the same scratches as a leader
		copyAnswersFromLeader(mcUser, groupLeader);
	    }

	}

    }

    @Override
    public IToolVO getToolBySignature(String toolSignature) throws McApplicationException {
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
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    @Override
    public String getActivityEvaluation(Long toolContentId) {
	return toolService.getActivityEvaluation(toolContentId);
    }

    @Override
    public void setActivityEvaluation(Long toolContentId, String toolOutputDefinition) {
	toolService.setActivityEvaluation(toolContentId, toolOutputDefinition);
    }

    /**
     * @param mcContentDAO
     *            The mcContentDAO to set.
     */
    public void setMcContentDAO(IMcContentDAO mcContentDAO) {
	this.mcContentDAO = mcContentDAO;
    }

    /**
     * @param mcOptionsContentDAO
     *            The mcOptionsContentDAO to set.
     */
    public void setMcOptionsContentDAO(IMcOptionsContentDAO mcOptionsContentDAO) {
	this.mcOptionsContentDAO = mcOptionsContentDAO;
    }

    /**
     * @param mcQueContentDAO
     *            The mcQueContentDAO to set.
     */
    public void setMcQueContentDAO(IMcQueContentDAO mcQueContentDAO) {
	this.mcQueContentDAO = mcQueContentDAO;
    }

    /**
     * @param mcSessionDAO
     *            The mcSessionDAO to set.
     */
    public void setMcSessionDAO(IMcSessionDAO mcSessionDAO) {
	this.mcSessionDAO = mcSessionDAO;
    }

    /**
     * @param mcUserDAO
     *            The mcUserDAO to set.
     */
    public void setMcUserDAO(IMcUserDAO mcUserDAO) {
	this.mcUserDAO = mcUserDAO;
    }

    /**
     * @param mcUsrAttemptDAO
     *            The mcUsrAttemptDAO to set.
     */
    public void setMcUsrAttemptDAO(IMcUsrAttemptDAO mcUsrAttemptDAO) {
	this.mcUsrAttemptDAO = mcUsrAttemptDAO;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    /**
     * @param mcToolContentHandler
     *            The mcToolContentHandler to set.
     */
    public void setMcToolContentHandler(IToolContentHandler mcToolContentHandler) {
	this.mcToolContentHandler = mcToolContentHandler;
    }

    /**
     * @param learnerService
     *            The learnerService to set.
     */
    public void setLearnerService(ILearnerService learnerService) {
	this.learnerService = learnerService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    public void setGradebookService(IGradebookService gradebookService) {
	this.gradebookService = gradebookService;
    }

    public void setMcOutputFactory(MCOutputFactory mcOutputFactory) {
	this.mcOutputFactory = mcOutputFactory;
    }

    @Override
    public List<ReflectionDTO> getReflectionList(McContent mcContent, Long userID) {
	List<ReflectionDTO> reflectionsContainerDTO = new LinkedList<ReflectionDTO>();
	if (userID == null) {
	    // all users mode
	    for (McSession mcSession : (Set<McSession>) mcContent.getMcSessions()) {

		for (McQueUsr user : (Set<McQueUsr>) mcSession.getMcQueUsers()) {

		    NotebookEntry notebookEntry = this.getEntry(mcSession.getMcSessionId(),
			    CoreNotebookConstants.NOTEBOOK_TOOL, McAppConstants.MY_SIGNATURE,
			    new Integer(user.getQueUsrId().toString()));

		    if (notebookEntry != null) {
			ReflectionDTO reflectionDTO = new ReflectionDTO();
			reflectionDTO.setUserId(user.getQueUsrId().toString());
			reflectionDTO.setSessionId(mcSession.getMcSessionId().toString());
			reflectionDTO.setUserName(user.getFullname());
			reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
			// String notebookEntryPresentable = McUtils.replaceNewLines(notebookEntry.getEntry());
			reflectionDTO.setEntry(notebookEntry.getEntry());
			reflectionsContainerDTO.add(reflectionDTO);
		    }
		}
	    }
	} else {
	    // single user mode
	    for (Iterator sessionIter = mcContent.getMcSessions().iterator(); sessionIter.hasNext();) {
		McSession mcSession = (McSession) sessionIter.next();
		for (Iterator userIter = mcSession.getMcQueUsers().iterator(); userIter.hasNext();) {
		    McQueUsr user = (McQueUsr) userIter.next();
		    if (user.getQueUsrId().equals(userID)) {
			NotebookEntry notebookEntry = this.getEntry(mcSession.getMcSessionId(),
				CoreNotebookConstants.NOTEBOOK_TOOL, McAppConstants.MY_SIGNATURE,
				new Integer(user.getQueUsrId().toString()));

			if (notebookEntry != null) {
			    ReflectionDTO reflectionDTO = new ReflectionDTO();
			    reflectionDTO.setUserId(user.getQueUsrId().toString());
			    reflectionDTO.setSessionId(mcSession.getMcSessionId().toString());
			    reflectionDTO.setUserName(user.getFullname());
			    reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
			    // String notebookEntryPresentable = McUtils.replaceNewLines(notebookEntry.getEntry());
			    reflectionDTO.setEntry(notebookEntry.getEntry());
			    reflectionsContainerDTO.add(reflectionDTO);
			}
		    }
		}
	    }
	}

	return reflectionsContainerDTO;
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
    public void updateEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }

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

    /**
     * @return Returns the auditService.
     */
    public IAuditService getAuditService() {
	return auditService;
    }

    /**
     * @param auditService
     *            The auditService to set.
     */
    public void setAuditService(IAuditService auditService) {
	this.auditService = auditService;
    }

    /**
     * @return Returns the MessageService.
     */
    public MessageService getMessageService() {
	return messageService;
    }

    /**
     * @param messageService
     *            The MessageService to set.
     */
    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return mcOutputFactory.getSupportedDefinitionClasses(definitionType);
    }

    // ****************** REST methods *************************

    /**
     * Rest call to create a new Multiple Choice content. Required fields in toolContentJSON: "title", "instructions",
     * "questions". The questions entry should be JSONArray containing JSON objects, which in turn must contain
     * "questionText", "displayOrder" (Integer) and a JSONArray "answers". The answers entry should be JSONArray
     * containing JSON objects, which in turn must contain "answerText", "displayOrder" (Integer), "correct" (Boolean).
     *
     * Retries are controlled by lockWhenFinished, which defaults to true (no retries).
     */
    @SuppressWarnings("unchecked")
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, JSONObject toolContentJSON)
	    throws JSONException {

	McContent mcq = new McContent();
	Date updateDate = new Date();

	mcq.setCreationDate(updateDate);
	mcq.setUpdateDate(updateDate);
	mcq.setCreatedBy(userID.longValue());
	mcq.setDefineLater(false);

	mcq.setMcContentId(toolContentID);
	mcq.setTitle(toolContentJSON.getString(RestTags.TITLE));
	mcq.setInstructions(toolContentJSON.getString(RestTags.INSTRUCTIONS));

	mcq.setRetries(JsonUtil.opt(toolContentJSON, "allowRetries", Boolean.FALSE));
	mcq.setUseSelectLeaderToolOuput(
		JsonUtil.opt(toolContentJSON, RestTags.USE_SELECT_LEADER_TOOL_OUTPUT, Boolean.FALSE));
	mcq.setReflect(JsonUtil.opt(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	mcq.setReflectionSubject(JsonUtil.opt(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS, ""));
	mcq.setQuestionsSequenced(JsonUtil.opt(toolContentJSON, "questionsSequenced", Boolean.FALSE));
	mcq.setRandomize(JsonUtil.opt(toolContentJSON, "randomize", Boolean.FALSE));
	mcq.setShowReport(JsonUtil.opt(toolContentJSON, "showReport", Boolean.FALSE));
	mcq.setDisplayAnswers(JsonUtil.opt(toolContentJSON, "displayAnswers", Boolean.FALSE));
	mcq.setShowMarks(JsonUtil.opt(toolContentJSON, "showMarks", Boolean.FALSE));
	mcq.setPrefixAnswersWithLetters(JsonUtil.opt(toolContentJSON, "prefixAnswersWithLetters", Boolean.TRUE));
	mcq.setPassMark(JsonUtil.opt(toolContentJSON, "passMark", 0));
	// submissionDeadline is set in monitoring

	createMc(mcq);

	// Questions
	JSONArray questions = toolContentJSON.getJSONArray(RestTags.QUESTIONS);
	for (int i = 0; i < questions.length(); i++) {
	    JSONObject questionData = (JSONObject) questions.get(i);
	    McQueContent question = new McQueContent(questionData.getString(RestTags.QUESTION_TEXT),
		    questionData.getInt(RestTags.DISPLAY_ORDER), 1, "", mcq, null, new HashSet<McOptsContent>());

	    JSONArray optionsData = questionData.getJSONArray(RestTags.ANSWERS);
	    for (int j = 0; j < optionsData.length(); j++) {
		JSONObject optionData = (JSONObject) optionsData.get(j);
		question.getMcOptionsContents().add(new McOptsContent(optionData.getInt(RestTags.DISPLAY_ORDER),
			optionData.getBoolean(RestTags.CORRECT), optionData.getString(RestTags.ANSWER_TEXT), question));
	    }
	    saveOrUpdateMcQueContent(question);
	}

	// TODO
	// mcq.setContent(content) - can't find in database
	// mcq.setConditions(conditions);

    }
}
