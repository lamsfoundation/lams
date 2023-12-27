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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellUtil;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.confidencelevel.VsaAnswerDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.model.QbToolQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.dao.IMcConfigDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcOptionsContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcQueContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcSessionDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcUserDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcUsrAttemptDAO;
import org.lamsfoundation.lams.tool.mc.dto.AnswerDTO;
import org.lamsfoundation.lams.tool.mc.dto.LeaderResultsDTO;
import org.lamsfoundation.lams.tool.mc.dto.McOptionDTO;
import org.lamsfoundation.lams.tool.mc.dto.McQuestionDTO;
import org.lamsfoundation.lams.tool.mc.dto.McSessionMarkDTO;
import org.lamsfoundation.lams.tool.mc.dto.McUserMarkDTO;
import org.lamsfoundation.lams.tool.mc.dto.SessionDTO;
import org.lamsfoundation.lams.tool.mc.dto.ToolOutputDTO;
import org.lamsfoundation.lams.tool.mc.model.McContent;
import org.lamsfoundation.lams.tool.mc.model.McOptsContent;
import org.lamsfoundation.lams.tool.mc.model.McQueContent;
import org.lamsfoundation.lams.tool.mc.model.McQueUsr;
import org.lamsfoundation.lams.tool.mc.model.McSession;
import org.lamsfoundation.lams.tool.mc.model.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.util.McSessionComparator;
import org.lamsfoundation.lams.tool.mc.util.McStringComparator;
import org.lamsfoundation.lams.tool.service.ICommonAssessmentService;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.service.IQbToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.NumberUtil;
import org.lamsfoundation.lams.util.excel.ExcelUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.dao.DataAccessException;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The POJO implementation of Mc service. All business logics of MCQ tool are implemented in this class. It translate
 * the request from presentation layer and perform appropriate database operation.
 *
 * @author Ozgur Demirtas
 */
public class McService
	implements IMcService, ToolContentManager, ToolSessionManager, ToolRestManager, McAppConstants, IQbToolService,
	ICommonAssessmentService {
    private static Logger logger = Logger.getLogger(McService.class.getName());

    private IMcContentDAO mcContentDAO;
    private IMcQueContentDAO mcQueContentDAO;
    private IMcOptionsContentDAO mcOptionsContentDAO;
    private IMcSessionDAO mcSessionDAO;
    private IMcUserDAO mcUserDAO;
    private IMcUsrAttemptDAO mcUsrAttemptDAO;
    private IMcConfigDAO mcConfigDAO;
    private MCOutputFactory mcOutputFactory;

    private ILogEventService logEventService;
    private IUserManagementService userManagementService;
    private ILamsToolService toolService;
    private IToolContentHandler mcToolContentHandler = null;
    private IExportToolContentService exportContentService;
    private IQbService qbService;

    private MessageService messageService;

    public McService() {
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
	    // set leader only if the leader entered the activity
	    if (user.getQueUsrId().equals(leaderUserId)) {
		// is it me?
		leader = user;
	    } else {
		leader = getMcUserBySession(leaderUserId, mcSession.getUid());
	    }
	    if (leader != null) {
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
			leaderAttempt.getQbOption(), leaderAttempt.getMark(), leaderAttempt.isPassed(),
			leaderAttempt.isAttemptCorrect(), leaderAttempt.getConfidenceLevel());
		mcUsrAttemptDAO.saveMcUsrAttempt(userAttempt);

		// if it's been changed by the leader
	    } else if (leaderAttempt.getAttemptTime().compareTo(userAttempt.getAttemptTime()) != 0) {
		userAttempt.setQbOption(leaderAttempt.getQbOption());
		userAttempt.setConfidenceLevel(leaderAttempt.getConfidenceLevel());
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
	mcContentDAO.saveMcContent(mcContent);
    }

    @Override
    public McContent getMcContent(Long toolContentId) throws McApplicationException {
	return mcContentDAO.findMcContentById(toolContentId);
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
    public McQueContent getQuestionByDisplayOrder(final Integer displayOrder, final Long mcContentUid) {
	return mcQueContentDAO.getQuestionContentByDisplayOrder(displayOrder, mcContentUid);
    }

    @Override
    public List getAllQuestionsSorted(final long mcContentId) {
	return mcQueContentDAO.getAllQuestionEntriesSorted(mcContentId);
    }

    @Override
    public void saveOrUpdateMcQueContent(McQueContent question) {
	mcQueContentDAO.insertOrUpdate(question.getQbQuestion());

	for (QbOption option : question.getQbQuestion().getQbOptions()) {
	    mcQueContentDAO.insertOrUpdate(option);
	}

	mcQueContentDAO.saveOrUpdateMcQueContent(question);
    }

    @Override
    public McContent createQuestions(List<McQuestionDTO> questionDTOs, McContent content) {

	int displayOrder = 0;
	for (McQuestionDTO questionDTO : questionDTOs) {
	    // skip empty questions
	    if (questionDTO.getName().isEmpty()) {
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

	    // get the QB questionDescription from DB
	    QbQuestion qbQuestion = questionDTO.getQbQuestionUid() == null
		    ? null
		    : (QbQuestion) mcQueContentDAO.find(QbQuestion.class, questionDTO.getQbQuestionUid());
	    if (qbQuestion == null) {
		// if it does not exist, create a new one
		qbQuestion = new QbQuestion();
		qbQuestion.setType(QbQuestion.TYPE_MULTIPLE_CHOICE);
		qbQuestion.setQuestionId(qbService.generateNextQuestionId());
	    }
	    // make a clone to check if data changed
	    QbQuestion qbQuestionClone = qbQuestion.clone();

	    // set clone's data to current values
	    qbQuestionClone.setName(questionDTO.getName());
	    qbQuestionClone.setDescription(questionDTO.getDescription());
	    qbQuestionClone.setContentFolderId(questionDTO.getContentFolderId());
	    qbQuestionClone.setMaxMark(Integer.valueOf(currentMark));
	    qbQuestionClone.setFeedback(currentFeedback);

	    // modification status was already set in McController#saveQuestion()
	    switch (questionDTO.getQbQuestionModified()) {
		case IQbService.QUESTION_MODIFIED_NONE:
		    // if no modification was made, prevent clone from being persisted
		    releaseQbQuestionFromCache(qbQuestionClone);
		    break;
		case IQbService.QUESTION_MODIFIED_UPDATE:
		    // simply accept the modified clone as new version of the questionDescription
		    // this option is not supported yet
		    qbQuestion = qbQuestionClone;
		    break;
		case IQbService.QUESTION_MODIFIED_VERSION_BUMP:
		    // new version of the old questionDescription gets created
		    qbQuestion = qbQuestionClone;
		    qbQuestion.setVersion(qbService.getMaxQuestionVersion(qbQuestion.getQuestionId()) + 1);
		    qbQuestion.setCreateDate(new Date());
		    qbQuestion.setUuid(UUID.randomUUID());
		    break;
		case IQbService.QUESTION_MODIFIED_ID_BUMP:
		    // new questionDescription gets created
		    qbQuestion = qbQuestionClone;
		    qbQuestion.setVersion(1);
		    qbQuestion.setQuestionId(qbService.generateNextQuestionId());
		    qbQuestion.setCreateDate(new Date());
		    qbQuestion.setUuid(UUID.randomUUID());
		    break;
	    }

	    // in case questionDescription doesn't exist
	    if (question == null) {
		question = new McQueContent(qbQuestion, displayOrder, content);

		// adding a new questionDescription to content
		content.getMcQueContents().add(question);
		question.setMcContent(content);

		// in case questionDescription exists already
	    } else {
		question.setDisplayOrder(displayOrder);
		// this is needed, if we took the clone as the new questionDescription
		question.setQbQuestion(qbQuestion);
	    }

	    // persist candidate answers
	    List<QbOption> oldOptions = question.getQbQuestion().getQbOptions();
	    List<QbOption> newOptions = new ArrayList<>();
	    int displayOrderOption = 1;
	    Set<QbOption> qbOptionsToRemove = new HashSet<>(qbQuestion.getQbOptions());
	    List<McOptionDTO> optionDTOs = questionDTO.getOptionDtos();

	    for (McOptionDTO optionDTO : optionDTOs) {

		String optionText = optionDTO.getCandidateAnswer();
		boolean isCorrectOption = "Correct".equals(optionDTO.getCorrect());

		//find persisted option if it exists
		QbOption option = null;
		for (QbOption oldOption : oldOptions) {
		    if (oldOption.getUid().equals(optionDTO.getQbOptionUid())) {
			option = oldOption;
			break;
		    }
		}

		if (optionDTO.getQbOptionUid() == null) {
		    // it is a new option
		    option = new QbOption();
		    option.setQbQuestion(qbQuestion);
		} else {
		    // match existing options with DTO data
		    for (QbOption qbOption : qbQuestion.getQbOptions()) {
			if (qbOption.getUid().equals(optionDTO.getQbOptionUid())) {
			    option = qbOption;
			    // if a match was found, we do not remove the option
			    qbOptionsToRemove.remove(option);
			    break;
			}
		    }
		}

		// QB option data is set here
		option.setDisplayOrder(displayOrderOption);
		option.setCorrect(isCorrectOption);
		option.setName(optionText);

		newOptions.add(option);
		displayOrderOption++;
	    }
	    qbQuestion.getQbOptions().clear();
	    qbQuestion.getQbOptions().addAll(newOptions);

	    // make removed options orphaned, so they will be removed from DB
	    qbQuestion.getQbOptions().removeAll(qbOptionsToRemove);
	    qbOptionsToRemove.clear();
	    // if clone is the real questionDescription, clear its IDs so it is saved as a new questionDescription or version
	    // it needs to happen only now as above we used option UID matching
	    if (questionDTO.getQbQuestionModified() > IQbService.QUESTION_MODIFIED_UPDATE) {
		qbQuestion.clearID();
	    }

	    // updating the existing questionDescription content
	    saveOrUpdateMcQueContent(question);

	}
	return content;
    }

    @Override
    public void releaseQuestionsFromCache(McContent content) {
	for (McQueContent question : content.getMcQueContents()) {
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

	    McQueUsr user = new McQueUsr(userId, userName, fullName, mcSession);
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
    public McQueUsr getMcUserByUID(Long uid) {
	return mcUserDAO.getMcUserByUID(uid);
    }

    @Override
    public McQueUsr getMcUserByContentId(Long userId, Long contentId) {
	return mcUserDAO.getMcUserByContentId(userId, contentId);
    }

    @Override
    public String getPortraitId(Long userId) {
	if (userId != null) {
	    User user = (User) userManagementService.findById(User.class, userId.intValue());
	    return user == null || user.getPortraitUuid() == null ? null : user.getPortraitUuid().toString();
	}
	return null;
    }

    @Override
    public List<McUserMarkDTO> getPagedUsersBySession(Long sessionId, int page, int size, String sortBy,
	    String sortOrder, String searchString) {
	return mcUserDAO.getPagedUsersBySession(sessionId, page, size, sortBy, sortOrder, searchString,
		userManagementService);
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
			"Can't find questionDescription with specified questionDescription uid: "
				+ answerDto.getQuestionUid());
	    }

	    QbOption answerOption = answerDto.getAnswerOption();
	    if (answerOption != null) {

		Integer mark = answerDto.getMark();
		boolean passed = user.isMarkPassed(mark);
		boolean isAttemptCorrect = answerDto.isAttemptCorrect();
		int confidenceLevel = answerDto.getConfidenceLevel();

		McUsrAttempt userAttempt = this.getUserAttemptByQuestion(user.getUid(), questionUid);
		if (userAttempt != null) {
		    userAttempt.setAttemptTime(attemptTime);
		    userAttempt.setQbOption(answerOption);
		    userAttempt.setMark(mark);
		    userAttempt.setPassed(passed);
		    userAttempt.setAttemptCorrect(isAttemptCorrect);
		    userAttempt.setConfidenceLevel(confidenceLevel);

		} else {
		    // create new userAttempt
		    userAttempt = new McUsrAttempt(attemptTime, question, user, answerOption, mark, passed,
			    isAttemptCorrect, confidenceLevel);
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
    public int getAttemptsCountPerOption(Long optionUid, Long mcQueContentUid) {
	return mcUsrAttemptDAO.getAttemptsCountPerOption(optionUid, mcQueContentUid);
    }

    @Override
    public boolean isMcContentAttempted(Long mcContentUid) {
	return mcUsrAttemptDAO.isMcContentAttempted(mcContentUid);
    }

    @Override
    public List<AnswerDTO> getAnswersFromDatabase(McContent mcContent, McQueUsr user) {
	List<AnswerDTO> answerDtos = new LinkedList<>();
	List<McQueContent> questions = this.getQuestionsByContentUid(mcContent.getUid());

	for (McQueContent question : questions) {
	    AnswerDTO answerDto = new AnswerDTO();
	    List<QbOption> qbOptions = question.getQbQuestion().getQbOptions();
	    List<McOptsContent> mcOptsContentList = new ArrayList<>();

	    boolean randomize = mcContent.isRandomize();
	    if (randomize) {
		ArrayList<QbOption> shuffledList = new ArrayList<>(qbOptions);
		Collections.shuffle(shuffledList);
		qbOptions = new LinkedList<>(shuffledList);
	    }
	    for (QbOption qbOption : qbOptions) {
		McOptsContent option = new McOptsContent();
		option.setQbOption(qbOption);
		mcOptsContentList.add(option);
	    }

	    answerDto.setQuestionName(question.getName());
	    answerDto.setQuestionDescription(question.getDescription());
	    answerDto.setDisplayOrder(String.valueOf(question.getDisplayOrder()));
	    answerDto.setQuestionUid(question.getUid());
	    answerDto.setMark(question.getMark());
	    answerDto.setOptions(mcOptsContentList);

	    answerDtos.add(answerDto);
	}

	// populate answers
	if (user != null) {

	    for (AnswerDTO answerDto : answerDtos) {
		Long questionUid = answerDto.getQuestionUid();

		McUsrAttempt dbAttempt = this.getUserAttemptByQuestion(user.getUid(), questionUid);
		if (dbAttempt != null) {
		    answerDto.setConfidenceLevel(dbAttempt.getConfidenceLevel());

		    // mark selected option as selected
		    Long selectedOptionUid = dbAttempt.getQbOption().getUid();
		    for (McOptsContent option : answerDto.getOptions()) {
			if (selectedOptionUid.equals(option.getQbOption().getUid())) {
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
	List<McSessionMarkDTO> listMonitoredMarksContainerDTO = new LinkedList<>();
	Set<McSession> sessions = new TreeSet<>(new McSessionComparator());
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
		mcUserMarkDTO.setUserId(user.getQueUsrId().toString());

		if (isFullAttemptDetailsRequired) {

		    // The marks for the user must be listed in the display order of the questionDescription.
		    // Other parts of the code assume that the questions will be in consecutive display
		    // order starting 1 (e.g. 1, 2, 3, not 1, 3, 4) so we set up an array and use
		    // the ( display order - 1) as the index (arrays start at 0, rather than 1 hence -1)
		    // The user must answer all questions, so we can assume that they will have marks
		    // for all questions or no questions.
		    // At present there can only be one answer for each questionDescription but there may be more
		    // than one in the future and if so, we don't want to count the mark twice hence
		    // we need to check if we've already processed this questionDescription in the total.
		    Integer[] userMarks = new Integer[numQuestions];
		    String[] answeredOptions = new String[numQuestions];
		    Date attemptTime = null;
		    List<McUsrAttempt> finalizedUserAttempts = this.getFinalizedUserAttempts(user);
		    long totalMark = 0;
		    for (McUsrAttempt attempt : finalizedUserAttempts) {
			Integer displayOrder = attempt.getMcQueContent().getDisplayOrder();
			int arrayIndex = (displayOrder != null) && (displayOrder.intValue() > 0)
				? displayOrder.intValue() - 1
				: 1;
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
			    for (QbOption option : attempt.getMcQueContent().getQbQuestion().getQbOptions()) {
				if (attempt.getQbOption().getUid().equals(option.getUid())) {
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
	return mcUsrAttemptDAO.getFinalizedUserAttempts(user.getUid());
    }

    @Override
    public McUsrAttempt getUserAttemptByQuestion(Long queUsrUid, Long mcQueContentId) throws McApplicationException {
	return mcUsrAttemptDAO.getUserAttemptByQuestion(queUsrUid, mcQueContentId);
    }

    @Override
    public List<ToolOutputDTO> getLearnerMarksByContentId(Long toolContentId) {
	return mcUsrAttemptDAO.getLearnerMarksByContentId(toolContentId);
    }

    @Override
    public List<McQueContent> getQuestionsByContentUid(final Long contentUid) throws McApplicationException {
	return mcQueContentDAO.getQuestionsByContentUid(contentUid.longValue());
    }

    @Override
    public void removeMcQueContent(McQueContent mcQueContent) throws McApplicationException {
	try {
	    mcQueContentDAO.removeMcQueContent(mcQueContent);
	} catch (DataAccessException e) {
	    throw new McApplicationException(
		    "Exception occured when lams is removing mc questionDescription content: " + e.getMessage(), e);
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
    public Object[] getMarkStatistics(McSession mcSession) {
	return mcUserDAO.getStatsMarksBySession(mcSession.getMcSessionId());
    }

    @Override
    public List<QbOption> findOptionsByQuestionUid(Long mcQueContentId) throws McApplicationException {
	try {
	    return mcOptionsContentDAO.findOptionsByQueId(mcQueContentId);
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
    public void updateQbOption(QbOption option) throws McApplicationException {
	try {
	    mcOptionsContentDAO.update(option);
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

	McUsrAttempt userAttemptToUpdate = mcUsrAttemptDAO.getUserAttemptByUid(userAttemptUid);
	McQueUsr selectedUser = userAttemptToUpdate.getMcQueUsr();
	Long userUid = selectedUser.getUid();
	McSession mcSession = selectedUser.getMcSession();

	Integer oldMark = userAttemptToUpdate.getMark();
	int oldTotalMark = mcUsrAttemptDAO.getUserTotalMark(userUid);
	int totalMark = (oldMark == null) ? oldTotalMark + newMark : (oldTotalMark - oldMark) + newMark;

	List<McUsrAttempt> userAttempts = new ArrayList<>();
	McQueUsr groupLeader = mcSession.getGroupLeader();
	if (groupLeader != null) {
	    if (groupLeader.equals(selectedUser)) {
		// This is the group leader so update everyone in the group's mark
		userAttempts = mcUsrAttemptDAO.getUserAttemptsByQuestionSession(mcSession.getUid(),
			userAttemptToUpdate.getMcQueContent().getUid());
	    } else {
		String error = new StringBuilder(
			"Attempting to update the mark for a non-leader. Cannot update. Session ").append(
				mcSession.getSession_name()).append(":").append(mcSession.getMcSessionId()).append(" user ")
			.append(selectedUser.getUsername()).append(":").append(selectedUser.getQueUsrId()).toString();
		logger.warn(error);
	    }
	} else {
	    userAttempts = new ArrayList<>();
	    userAttempts.add(userAttemptToUpdate);
	}

	for (McUsrAttempt attempt : userAttempts) {

	    attempt.setMark(newMark);
	    mcUsrAttemptDAO.saveMcUsrAttempt(attempt);

	    // update user's total mark
	    McQueUsr user = attempt.getMcQueUsr();
	    user.setLastAttemptTotalMark(totalMark);
	    updateMcQueUsr(user);

	    // propagate changes to Gradebook
	    toolService.updateActivityMark(new Double(totalMark), null, user.getQueUsrId().intValue(),
		    mcSession.getMcSessionId(), false);

	    // record mark change with audit service
	    Long toolContentId = null;
	    if (mcSession.getMcContent() != null) {
		toolContentId = mcSession.getMcContent().getMcContentId();
	    }
	    logEventService.logMarkChange(user.getQueUsrId(), user.getUsername(), toolContentId, "" + oldMark,
		    "" + totalMark);
	}
    }

    @Override
    public void recalculateUserAnswers(McContent content, Set<McQueContent> oldQuestions,
	    List<McQuestionDTO> questionDTOs) {
	// create list of modified questions
	List<McQuestionDTO> modifiedQuestions = new ArrayList<>();
	// create list of modified questionDescription marks
	List<McQuestionDTO> modifiedQuestionsMarksOnly = new ArrayList<>();

	for (McQueContent oldQuestion : oldQuestions) {
	    for (McQuestionDTO questionDTO : questionDTOs) {
		if (oldQuestion.getUid().equals(questionDTO.getUid())) {

		    boolean isQuestionModified = false;
		    boolean isQuestionMarkModified = false;

		    // question name is different
		    if (!oldQuestion.getName().equals(questionDTO.getName())) {
			isQuestionModified = true;
		    }

		    // question description is different
		    if (!oldQuestion.getDescription().equals(questionDTO.getDescription())) {
			isQuestionModified = true;
		    }

		    // mark is different
		    if (!oldQuestion.getMark().equals(Integer.valueOf(questionDTO.getMark()))) {
			isQuestionMarkModified = true;
		    }

		    // options are different
		    List<QbOption> oldOptions = oldQuestion.getQbQuestion().getQbOptions();
		    List<McOptionDTO> optionDTOs = questionDTO.getOptionDtos();
		    for (QbOption oldOption : oldOptions) {
			for (McOptionDTO optionDTO : optionDTOs) {
			    if (oldOption.getUid().equals(optionDTO.getQbOptionUid())) {

				if (!StringUtils.equals(oldOption.getName(), optionDTO.getCandidateAnswer()) || (
					oldOption.isCorrect() != "Correct".equals(optionDTO.getCorrect()))) {
				    isQuestionModified = true;
				    break;
				}
			    }
			}
		    }

		    // a new option is added
		    if (oldOptions.size() != optionDTOs.size()) {
			isQuestionModified = true;
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
		    Integer oldUserMark = userAttempt.getMark();

		    // [+] if the questionDescription mark is modified
		    for (McQuestionDTO modifiedQuestion : modifiedQuestionsMarksOnly) {
			if (question.getUid().equals(modifiedQuestion.getUid())) {
			    int newQuestionMark = Integer.parseInt(modifiedQuestion.getMark());
			    Integer oldQuestionMark = question.getMark();
			    Integer newActualMark = (userAttempt.getMark() * newQuestionMark) / oldQuestionMark;

			    // update questionDescription answer's mark
			    userAttempt.setMark(newActualMark);
			    mcUsrAttemptDAO.saveMcUsrAttempt(userAttempt);

			    newTotalMark += newActualMark - oldUserMark;

			    break;
			}
		    }

		    // [+] if the questionDescription is modified
		    for (McQuestionDTO modifiedQuestion : modifiedQuestions) {
			if (question.getUid().equals(modifiedQuestion.getUid())) {

			    //check whether user has selected correct answer, taking into account changes done to modifiedQuestion
			    boolean isAnswerCorrect = false;
			    McOptsContent selectedOption = userAttempt.getMcOptionsContent();
			    for (McOptionDTO modifiedOption : modifiedQuestion.getOptionDtos()) {
				if (selectedOption.getQbOption().getUid().equals(modifiedOption.getQbOptionUid())) {
				    isAnswerCorrect = "Correct".equals(modifiedOption.getCorrect());
				}
			    }

			    //recalculate mark
			    Integer newActualMark = isAnswerCorrect ? Integer.valueOf(modifiedQuestion.getMark()) : 0;
			    boolean passed = user.isMarkPassed(newActualMark);
			    userAttempt.setMark(newActualMark);
			    userAttempt.setPassed(passed);
			    userAttempt.setAttemptCorrect(isAnswerCorrect);
			    mcUsrAttemptDAO.saveMcUsrAttempt(userAttempt);

			    newTotalMark += newActualMark - oldUserMark;

			    break;
			}
		    }
		}

		// if the mark is changed, update user's lastAttemptTotalMark and also propagade it to the Gradebook
		if (newTotalMark != oldTotalMark) {
		    user.setLastAttemptTotalMark(newTotalMark);
		    updateMcQueUsr(user);
		    toolService.updateActivityMark(Double.valueOf(newTotalMark), null, user.getQueUsrId().intValue(),
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
	    if (question.getQbQuestion().getQbOptions().size() > maxOptionsInQuestion) {
		maxOptionsInQuestion = question.getQbQuestion().getQbOptions().size();
	    }
	}

	int totalNumberOfUsers = 0;
	for (McSession session : mcContent.getMcSessions()) {
	    totalNumberOfUsers += session.getMcQueUsers().size();
	}

	List<McSessionMarkDTO> sessionMarkDTOs = this.buildGroupsMarkData(mcContent, true);

	// create an empty excel file
	HSSFWorkbook wb = new HSSFWorkbook();
	HSSFCellStyle greenColor = wb.createCellStyle();
	greenColor.setFillForegroundColor(IndexedColors.LIME.getIndex());
	greenColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	Font whiteFont = wb.createFont();
	whiteFont.setColor(IndexedColors.WHITE.getIndex());
	whiteFont.setFontName(ExcelUtil.DEFAULT_FONT_NAME);
	greenColor.setFont(whiteFont);

	short percentageFormat = wb.createDataFormat().getFormat("0%");

	// ======================================================= Report by questionDescription IRA page
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

	    double totalPercentage = 0d;
	    for (QbOption option : question.getQbQuestion().getQbOptions()) {
		int optionAttemptCount = getAttemptsCountPerOption(option.getUid(), question.getUid());
		cell = row.createCell(count++);
		Double percentage = (totalNumberOfUsers != 0) ? (double) optionAttemptCount / totalNumberOfUsers : 0d;
		cell.setCellValue(percentage);
		if (option.isCorrect()) {
		    cell.setCellStyle(greenColor);
		}
		CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, percentageFormat);

		totalPercentage += percentage;
	    }
	    cell = row.createCell(maxOptionsInQuestion + 1);
	    cell.setCellValue((1 - totalPercentage));
	    CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, percentageFormat);
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
	ArrayList<String> correctAnswers = new ArrayList<>();
	cell = row.createCell(count++);
	cell.setCellValue(messageService.getMessage("label.correct.answer"));
	for (McQueContent question : questions) {

	    // find out the correct answer's sequential letter - A,B,C...
	    String correctAnswerLetter = "";
	    int answerCount = 1;
	    for (QbOption option : question.getQbQuestion().getQbOptions()) {
		if (option.isCorrect()) {
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

	ArrayList<Double> totalPercentList = new ArrayList<>();
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

		Double totalPercents =
			questions.size() != 0 ? (double) numberOfCorrectlyAnsweredByUser / questions.size() : 0d;
		totalPercentList.add(totalPercents);
		cell = row.createCell(count++);
		cell.setCellValue(totalPercents);
		CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, percentageFormat);
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
	    Double average =
		    totalPercentList.size() == 0 ? 0d : (double) numberOfCorrectAnswers / totalPercentList.size();
	    cell.setCellValue(average);
	    CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, percentageFormat);
	}

	// class mean
	Double[] totalPercents = totalPercentList.toArray(new Double[0]);
	Arrays.sort(totalPercents);
	double sum = 0d;
	for (int i = 0; i < totalPercents.length; i++) {
	    sum += totalPercents[i];
	}
	row = sheet.createRow(rowCount++);
	cell = row.createCell(1);
	cell.setCellValue(messageService.getMessage("label.class.mean"));
	if (totalPercents.length != 0) {
	    Double classMean = totalPercents.length == 0 ? 0d : sum / totalPercents.length;
	    cell = row.createCell(questions.size() + 3);
	    cell.setCellValue(classMean);
	    CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, percentageFormat);
	}

	// median
	row = sheet.createRow(rowCount++);
	cell = row.createCell(1);
	cell.setCellValue(messageService.getMessage("label.median"));
	if (totalPercents.length != 0) {
	    Double median;
	    int middle = totalPercents.length / 2;
	    if ((totalPercents.length % 2) == 1) {
		median = totalPercents[middle];
	    } else {
		median = ((totalPercents[middle - 1] + totalPercents[middle]) / 2.0);
	    }
	    cell = row.createCell(questions.size() + 3);
	    cell.setCellValue(median);
	    CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, percentageFormat);
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
	    logger.warn("fromContentId is null.");
	    fromContentId = getToolDefaultContentIdBySignature(McAppConstants.TOOL_SIGNATURE);
	}

	if (toContentId == null) {
	    logger.error("throwing ToolException: toContentId is null");
	    throw new ToolException("toContentId is missing");
	}

	McContent fromContent = mcContentDAO.findMcContentById(fromContentId);

	if (fromContent == null) {
	    logger.warn("fromContent is null.");
	    long defaultContentId = getToolDefaultContentIdBySignature(McAppConstants.TOOL_SIGNATURE);
	    fromContent = mcContentDAO.findMcContentById(defaultContentId);
	}

	McContent toContent = McContent.newInstance(fromContent, toContentId);
	if (toContent == null) {
	    logger.error("throwing ToolException: WARNING!, retrieved toContent is null.");
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
	    logger.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
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
    public void removeLearnerContent(Long toolContentId, Integer userId, boolean resetActivityCompletionOnly)
	    throws ToolException {

	if (logger.isDebugEnabled()) {
	    if (resetActivityCompletionOnly) {
		logger.debug("Resetting Multiple Choice completion for user ID " + userId + " and toolContentId "
			+ toolContentId);
	    } else {
		logger.debug("Removing Multiple Choice content for user ID " + userId + " and toolContentId "
			+ toolContentId);
	    }
	}

	McContent content = mcContentDAO.findMcContentById(toolContentId);
	if (content != null) {
	    for (McSession session : content.getMcSessions()) {
		McQueUsr user = mcUserDAO.getMcUserBySession(userId.longValue(), session.getUid());
		if (user != null) {
		    if (resetActivityCompletionOnly) {
			user.setResponseFinalised(false);
			mcUserDAO.saveMcUser(user);
		    } else {
			mcUsrAttemptDAO.removeAllUserAttempts(user.getUid());

			if ((session.getGroupLeader() != null) && session.getGroupLeader().getUid()
				.equals(user.getUid())) {
			    session.setGroupLeader(null);
			}

			mcUserDAO.removeMcUser(user);
			toolService.removeActivityMark(userId, session.getMcSessionId());
		    }
		}
	    }
	}
    }

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	McContent toolContentObj = mcContentDAO.findMcContentById(toolContentId);
	if (toolContentObj == null) {
	    long defaultContentId = getToolDefaultContentIdBySignature(McAppConstants.TOOL_SIGNATURE);
	    toolContentObj = mcContentDAO.findMcContentById(defaultContentId);
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the multiple choice tool");
	}

	try {
	    // set ToolContentHandler as null to avoid copy file node in repository again.
	    toolContentObj = McContent.newInstance(toolContentObj, toolContentId);
	    toolContentObj.setMcSessions(null);
	    for (McQueContent mcQuestion : toolContentObj.getMcQueContents()) {
		qbService.prepareQuestionForExport(mcQuestion.getQbQuestion());
	    }
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

	    long userQbCollectionUid = qbService.getUserPrivateCollection(newUserUid).getUid();

	    // we need to save QB questions and options first
	    for (McQueContent mcQuestion : toolContentObj.getMcQueContents()) {
		QbQuestion qbQuestion = mcQuestion.getQbQuestion();
		qbQuestion.clearID();

		// try to match the question to an existing QB question in DB
		QbQuestion existingQuestion = qbService.getQuestionByUUID(qbQuestion.getUuid());
		if (existingQuestion == null) {
		    // none found, create a new QB question
		    qbService.insertQuestion(qbQuestion);
		    qbService.addQuestionToCollection(userQbCollectionUid, qbQuestion.getQuestionId(), false);
		} else {
		    // found, use the existing one
		    mcQuestion.setQbQuestion(existingQuestion);
		}
	    }

	    mcContentDAO.saveMcContent(toolContentObj);
	    // in case an imported question had a question ID which is the highest
	    qbService.updateMaxQuestionId();
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	McContent content = getMcContent(toolContentId);
	if (content == null) {
	    long defaultToolContentId = getToolDefaultContentIdBySignature(McAppConstants.TOOL_SIGNATURE);
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
	for (McSession session : content.getMcSessions()) {
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
	    logger.error("toolSessionId is null");
	    throw new ToolException("toolSessionId is missing");
	}

	McContent mcContent = mcContentDAO.findMcContentById(toolContentId);

	// create a new a new tool session if it does not already exist in the tool session table
	if (!existsSession(toolSessionId)) {
	    try {
		McSession mcSession = new McSession(toolSessionId, new Date(System.currentTimeMillis()),
			McSession.INCOMPLETE, toolSessionName, mcContent, new HashSet<McQueUsr>());

		mcSessionDAO.saveMcSession(mcSession);

	    } catch (Exception e) {
		logger.error("Error creating new toolsession in the db");
		throw new ToolException("Error creating new toolsession in the db: " + e);
	    }
	}
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    logger.error("toolSessionId is null");
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
	    logger.error("mcSession is null");
	    throw new DataMissingException("mcSession is missing");
	}

	try {
	    mcSessionDAO.removeMcSession(mcSession);
	    logger.debug("mcSession " + mcSession + " has been deleted successfully.");
	} catch (McApplicationException e) {
	    throw new ToolException("error deleting mcSession:" + e);
	}
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {

	if (toolService == null) {
	    return "dummyNextUrl";
	}

	if (learnerId == null) {
	    logger.error("learnerId is null");
	    throw new DataMissingException("learnerId is missing");
	}

	if (toolSessionId == null) {
	    logger.error("toolSessionId is null");
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

	String nextUrl = toolService.completeToolSession(toolSessionId, learnerId);
	if (nextUrl == null) {
	    logger.error("nextUrl is null");
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
    public List<ConfidenceLevelDTO> getConfidenceLevels(Long toolSessionId) {
	List<ConfidenceLevelDTO> confidenceLevelDtos = new ArrayList<>();
	if (toolSessionId == null) {
	    return confidenceLevelDtos;
	}

	McContent content = getMcSessionById(toolSessionId).getMcContent();

	//in case McContent is leader aware return all leaders confidences, otherwise - confidences from the users from the same group as requestor
	List<Object[]> userAttemptsAndPortraits = content.isUseSelectLeaderToolOuput()
		? mcUsrAttemptDAO.getLeadersFinalizedAttemptsByContentId(content.getMcContentId())
		: mcUsrAttemptDAO.getFinalizedAttemptsBySessionId(toolSessionId);

	for (Object[] userAttemptAndPortraitIter : userAttemptsAndPortraits) {
	    McUsrAttempt userAttempt = (McUsrAttempt) userAttemptAndPortraitIter[0];
	    UUID portraitUuid = (UUID) userAttemptAndPortraitIter[1];
	    McQueUsr user = userAttempt.getMcQueUsr();

	    //fill in question and option uids
	    ConfidenceLevelDTO confidenceLevelDto = new ConfidenceLevelDTO();
	    confidenceLevelDto.setUserId(user.getQueUsrId().intValue());
	    String userName = StringUtils.isBlank(user.getFullname()) ? user.getUsername() : user.getFullname();
	    confidenceLevelDto.setUserName(userName);
	    confidenceLevelDto.setPortraitUuid(portraitUuid == null ? null : portraitUuid.toString());
	    confidenceLevelDto.setLevel(userAttempt.getConfidenceLevel());
	    confidenceLevelDto.setType(ConfidenceLevelDTO.CONFIDENCE_LEVELS_TYPE_0_TO_100);
	    QbQuestion qbQuestion = userAttempt.getMcQueContent().getQbQuestion();
	    confidenceLevelDto.setQbQuestionUid(qbQuestion.getUid());
	    confidenceLevelDto.setQbOptionUid(userAttempt.getQbOption().getUid());

	    confidenceLevelDtos.add(confidenceLevelDto);
	}

	return confidenceLevelDtos;
    }

    @Override
    public boolean isUserGroupLeader(Long userId, Long toolSessionId) {
	McSession session = getMcSessionById(toolSessionId);
	McQueUsr mcUser = getMcUserBySession(userId, session.getUid());

	return (session != null) && (mcUser != null) && session.isUserGroupLeader(mcUser);
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	Long userId = user.getUserId().longValue();

	McSession session = getMcSessionById(toolSessionId);
	if ((session == null) || (session.getMcContent() == null)) {
	    return;
	}
	McContent content = session.getMcContent();

	McQueUsr mcUser = getMcUserBySession(userId, session.getUid());
	// create user if he hasn't accessed this activity yet
	if (mcUser == null) {
	    String userName = user.getLogin();
	    String fullName = user.getFirstName() + " " + user.getLastName();
	    mcUser = new McQueUsr(userId, userName, fullName, session);
	    mcUserDAO.saveMcUser(mcUser);
	}

	//finalize the latest result, if it's still active
	if (!mcUser.isResponseFinalised()) {

	    //calculate total learner mark
	    int learnerMark = 0;
	    for (McQueContent question : content.getMcQueContents()) {
		McUsrAttempt attempt = mcUsrAttemptDAO.getUserAttemptByQuestion(mcUser.getUid(), question.getUid());
		learnerMark += attempt == null ? 0 : attempt.getMark();
	    }

	    Integer numberOfAttempts = mcUser.getNumberOfAttempts() + 1;
	    mcUser.setNumberOfAttempts(numberOfAttempts);
	    mcUser.setLastAttemptTotalMark(learnerMark);
	    mcUser.setResponseFinalised(true);
	    updateMcQueUsr(mcUser);
	}

	//if this is a leader finishes, complete all non-leaders as well, also copy leader results to them
	McQueUsr groupLeader = checkLeaderSelectToolForSessionLeader(mcUser, toolSessionId);
	if (session.isUserGroupLeader(mcUser)) {
	    session.getMcQueUsers().forEach(sessionUser -> {
		//finish non-leader
		sessionUser.setResponseFinalised(true);
		updateMcQueUsr(sessionUser);

		//copy answers from leader to non-leaders
		copyAnswersFromLeader(sessionUser, groupLeader);
	    });
	}
    }

    @Override
    public Tool getToolBySignature(String toolSignature) throws McApplicationException {
	Tool tool = toolService.getToolBySignature(toolSignature);
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
    public void auditLogStartEditingActivityInMonitor(long toolContentID) {
	toolService.auditLogStartEditingActivityInMonitor(toolContentID);
    }

    @Override
    public boolean isLastActivity(Long toolSessionId) {
	return toolService.isLastActivity(toolSessionId);
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
     * 	The mcContentDAO to set.
     */
    public void setMcContentDAO(IMcContentDAO mcContentDAO) {
	this.mcContentDAO = mcContentDAO;
    }

    /**
     * @param mcOptionsContentDAO
     * 	The mcOptionsContentDAO to set.
     */
    public void setMcOptionsContentDAO(IMcOptionsContentDAO mcOptionsContentDAO) {
	this.mcOptionsContentDAO = mcOptionsContentDAO;
    }

    /**
     * @param mcQueContentDAO
     * 	The mcQueContentDAO to set.
     */
    public void setMcQueContentDAO(IMcQueContentDAO mcQueContentDAO) {
	this.mcQueContentDAO = mcQueContentDAO;
    }

    /**
     * @param mcSessionDAO
     * 	The mcSessionDAO to set.
     */
    public void setMcSessionDAO(IMcSessionDAO mcSessionDAO) {
	this.mcSessionDAO = mcSessionDAO;
    }

    /**
     * @param mcUserDAO
     * 	The mcUserDAO to set.
     */
    public void setMcUserDAO(IMcUserDAO mcUserDAO) {
	this.mcUserDAO = mcUserDAO;
    }

    /**
     * @param mcUsrAttemptDAO
     * 	The mcUsrAttemptDAO to set.
     */
    public void setMcUsrAttemptDAO(IMcUsrAttemptDAO mcUsrAttemptDAO) {
	this.mcUsrAttemptDAO = mcUsrAttemptDAO;
    }

    public void setMcConfigDAO(IMcConfigDAO mcConfigDAO) {
	this.mcConfigDAO = mcConfigDAO;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    /**
     * @param mcToolContentHandler
     * 	The mcToolContentHandler to set.
     */
    public void setMcToolContentHandler(IToolContentHandler mcToolContentHandler) {
	this.mcToolContentHandler = mcToolContentHandler;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    public void setMcOutputFactory(MCOutputFactory mcOutputFactory) {
	this.mcOutputFactory = mcOutputFactory;
    }

    /**
     * @return Returns the logEventService.
     */
    public ILogEventService getLogEventService() {
	return logEventService;
    }

    /**
     * @param logEventService
     * 	The logEventService to set.
     */
    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

    /**
     * @return Returns the MessageService.
     */
    public MessageService getMessageService() {
	return messageService;
    }

    /**
     * @param messageService
     * 	The MessageService to set.
     */
    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public void setQbService(IQbService qbService) {
	this.qbService = qbService;
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return mcOutputFactory.getSupportedDefinitionClasses(definitionType);
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	McSession session = getMcSessionById(toolSessionId);
	McQueUsr learner = mcUserDAO.getMcUserBySession(learnerId, session.getUid());
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	Date startDate = null;
	Date endDate = null;
	List<McUsrAttempt> attempts = getFinalizedUserAttempts(learner);
	for (McUsrAttempt item : attempts) {
	    Date newDate = item.getAttemptTime();
	    if (newDate != null) {
		if (startDate == null || newDate.before(startDate)) {
		    startDate = newDate;
		}
		if (endDate == null || newDate.after(endDate)) {
		    endDate = newDate;
		}
	    }
	}

	if (learner.isResponseFinalised()) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_COMPLETED, startDate, endDate);
	} else {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_ATTEMPTED, startDate, null);
	}
    }

    @Override
    public LeaderResultsDTO getLeaderResultsDTOForLeaders(Long contentId) {
	LeaderResultsDTO newDto = new LeaderResultsDTO(contentId);
	Object[] markStats = mcUserDAO.getStatsMarksForLeaders(contentId);
	if (markStats != null) {
	    newDto.setMinMark(markStats[0] != null
		    ? NumberUtil.formatLocalisedNumber((Float) markStats[0], (Locale) null, 2)
		    : "0.00");
	    newDto.setAvgMark(markStats[1] != null
		    ? NumberUtil.formatLocalisedNumber((Float) markStats[1], (Locale) null, 2)
		    : "0.00");
	    newDto.setMaxMark(markStats[2] != null
		    ? NumberUtil.formatLocalisedNumber((Float) markStats[2], (Locale) null, 2)
		    : "0.00");
	    newDto.setNumberGroupsLeaderFinished((Integer) markStats[3]);
	}
	return newDto;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SessionDTO> getSessionDtos(Long contentId, boolean includeStatistics) {
	List<SessionDTO> sessionDtos = new ArrayList<>();

	McContent mcContent = getMcContent(contentId);
	if (mcContent != null) {
	    Set<McSession> sessions = new TreeSet<>(new McSessionComparator());
	    sessions.addAll(mcContent.getMcSessions());
	    for (McSession session : sessions) {
		SessionDTO sessionDto = new SessionDTO();
		sessionDto.setSessionId(session.getMcSessionId());
		sessionDto.setSessionName(session.getSession_name());
		//for statistics tab
		if (includeStatistics) {
		    int countUsers = mcUserDAO.getCountPagedUsersBySession(session.getMcSessionId(), "");
		    sessionDto.setNumberLearners(countUsers);
		    Object[] markStats = mcUserDAO.getStatsMarksBySession(session.getMcSessionId());
		    if (markStats != null) {
			sessionDto.setMinMark(
				markStats[0] != null ? NumberUtil.formatLocalisedNumber((Float) markStats[0],
					(Locale) null, 2) : "0.00");
			sessionDto.setAvgMark(
				markStats[1] != null ? NumberUtil.formatLocalisedNumber((Float) markStats[1],
					(Locale) null, 2) : "0.00");
			sessionDto.setMaxMark(
				markStats[2] != null ? NumberUtil.formatLocalisedNumber((Float) markStats[2],
					(Locale) null, 2) : "0.00");
		    }
		}

		sessionDtos.add(sessionDto);
	    }
	}
	return sessionDtos;
    }

    @Override
    public List<Number> getMarksArray(Long sessionId) {
	return mcUserDAO.getRawUserMarksBySession(sessionId);
    }

    @Override
    public List<Number> getMarksArrayForLeaders(Long toolContentId) {
	return mcUserDAO.getRawLeaderMarksByToolContentId(toolContentId);
    }

    @Override
    public Collection<VsaAnswerDTO> getVSAnswers(Long toolSessionId) {
	return new ArrayList<>();
    }

    @Override
    public boolean recalculateMarksForVsaQuestion(Long questionUid, String answer) {
	// MCQ does not support VSA questions
	return false;
    }

    @Override
    public Map<QbToolQuestion, Map<String, Integer>> getUnallocatedVSAnswers(long toolContentId) {
	// MCQ does not support VSA questions
	return null;
    }

    /**
     * Counts how many questions were answered correctly by the given user, regardless of the mark given.
     */
    @Override
    public Integer countCorrectAnswers(long toolContentId, int userId) {
	McQueUsr user = getMcUserByContentId(Long.valueOf(userId), toolContentId);

	return Long.valueOf(
		mcUsrAttemptDAO.getFinalizedUserAttempts(user.getUid()).stream().filter(McUsrAttempt::isAttemptCorrect)
			.count()).intValue();

    }

    @Override
    public Map<Integer, Integer> countCorrectAnswers(long toolContentId) {
	List<McUsrAttempt> attempts = mcUsrAttemptDAO.findByProperty(McUsrAttempt.class, "qbToolQuestion.toolContentId",
		toolContentId);
	return attempts.stream().collect(Collectors.groupingBy(a -> a.getMcQueUsr().getQueUsrId().intValue(),
		Collectors.collectingAndThen(Collectors.toList(),
			l -> (int) l.stream().filter(McUsrAttempt::isAttemptCorrect).count())));
    }

    // ****************** REST methods *************************

    /**
     * Rest call to create a new Multiple Choice content. Required fields in toolContentJSON: "title", "instructions",
     * "questions". The questions entry should be ArrayNode containing JSON objects, which in turn must contain
     * "questionText", "displayOrder" (Integer) and a ArrayNode "answers". The answers entry should be ArrayNode
     * containing JSON objects, which in turn must contain "answerText", "displayOrder" (Integer), "correct" (Boolean).
     *
     * Retries are controlled by lockWhenFinished, which defaults to true (no retries).
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, ObjectNode toolContentJSON) {

	McContent mcq = new McContent();
	Date updateDate = new Date();

	mcq.setCreationDate(updateDate);
	mcq.setUpdateDate(updateDate);
	mcq.setCreatedBy(userID.longValue());
	mcq.setDefineLater(false);

	mcq.setMcContentId(toolContentID);
	mcq.setTitle(JsonUtil.optString(toolContentJSON, RestTags.TITLE));
	mcq.setInstructions(JsonUtil.optString(toolContentJSON, RestTags.INSTRUCTIONS));

	mcq.setRetries(JsonUtil.optBoolean(toolContentJSON, "allowRetries", Boolean.FALSE));
	mcq.setUseSelectLeaderToolOuput(
		JsonUtil.optBoolean(toolContentJSON, RestTags.USE_SELECT_LEADER_TOOL_OUTPUT, Boolean.FALSE));
	mcq.setQuestionsSequenced(JsonUtil.optBoolean(toolContentJSON, "questionsSequenced", Boolean.FALSE));
	mcq.setRandomize(JsonUtil.optBoolean(toolContentJSON, "randomize", Boolean.FALSE));
	mcq.setShowReport(JsonUtil.optBoolean(toolContentJSON, "showReport", Boolean.FALSE));
	mcq.setDisplayAnswers(JsonUtil.optBoolean(toolContentJSON, "displayAnswers", Boolean.FALSE));
	mcq.setDisplayFeedbackOnly(JsonUtil.optBoolean(toolContentJSON, "displayFeedbackOnly", Boolean.FALSE));
	mcq.setShowMarks(JsonUtil.optBoolean(toolContentJSON, "showMarks", Boolean.FALSE));
	mcq.setPrefixAnswersWithLetters(JsonUtil.optBoolean(toolContentJSON, "prefixAnswersWithLetters", Boolean.TRUE));
	mcq.setPassMark(JsonUtil.optInt(toolContentJSON, "passMark", 0));
	mcq.setEnableConfidenceLevels(
		JsonUtil.optBoolean(toolContentJSON, RestTags.ENABLE_CONFIDENCE_LEVELS, Boolean.FALSE));
	// submissionDeadline is set in monitoring

	createMc(mcq);

	QbCollection collection = qbService.getUserPrivateCollection(userID);
	Set<String> collectionUUIDs = collection == null
		? new HashSet<>()
		: qbService.getCollectionQuestions(collection.getUid()).stream().filter(q -> q.getUuid() != null)
			.collect(Collectors.mapping(q -> q.getUuid().toString(), Collectors.toSet()));
	// Questions
	ArrayNode questions = JsonUtil.optArray(toolContentJSON, RestTags.QUESTIONS);
	for (JsonNode questionDataJson : questions) {
	    ObjectNode questionData = (ObjectNode) questionDataJson;
	    boolean addToCollection = false;
	    McQueContent question = null;
	    QbQuestion qbQuestion = null;
	    String uuid = JsonUtil.optString(questionData, RestTags.QUESTION_UUID);

	    // try to match the question to an existing QB question in DB
	    if (StringUtils.isNotBlank(uuid)) {
		qbQuestion = qbService.getQuestionByUUID(UUID.fromString(uuid));
	    }

	    if (qbQuestion == null) {
		addToCollection = collection != null;

		qbQuestion = new QbQuestion();
		qbQuestion.setQuestionId(qbService.generateNextQuestionId());
		qbQuestion.setContentFolderId(FileUtil.generateUniqueContentFolderID());
		qbQuestion.setType(QbQuestion.TYPE_MULTIPLE_CHOICE);
		qbQuestion.setName(JsonUtil.optString(questionData, RestTags.QUESTION_TITLE));
		qbQuestion.setDescription(JsonUtil.optString(questionData, RestTags.QUESTION_TEXT));
		qbQuestion.setMaxMark(1);
		// UUID normally gets generated in the DB, but we need it immediately,
		// so we generate it programatically.
		// Re-reading the QbQuestion we just saved does not help as it is read from Hibernate cache,
		// not from DB where UUID is filed
		qbQuestion.setUuid(UUID.randomUUID());
		userManagementService.save(qbQuestion);

		// Store it back into JSON so Scratchie can read it
		// and use the same questions, not create new ones
		uuid = qbQuestion.getUuid().toString();
		questionData.put(RestTags.QUESTION_UUID, uuid);

		ArrayNode optionsData = JsonUtil.optArray(questionData, RestTags.ANSWERS);
		for (JsonNode optionData : optionsData) {
		    QbOption qbOption = new QbOption();
		    qbOption.setName(JsonUtil.optString(optionData, RestTags.ANSWER_TEXT));
		    qbOption.setCorrect(JsonUtil.optBoolean(optionData, RestTags.CORRECT));
		    qbOption.setDisplayOrder(JsonUtil.optInt(optionData, RestTags.DISPLAY_ORDER));
		    qbOption.setQbQuestion(qbQuestion);
		    qbQuestion.getQbOptions().add(qbOption);
		}
	    } else if (collection != null && !collectionUUIDs.contains(uuid)) {
		addToCollection = true;
	    }

	    question = new McQueContent(qbQuestion, JsonUtil.optInt(questionData, RestTags.DISPLAY_ORDER), mcq);
	    saveOrUpdateMcQueContent(question);

	    // all questions need to end up in user's private collection
	    if (addToCollection) {
		qbService.addQuestionToCollection(collection.getUid(), qbQuestion.getQuestionId(), false);
		collectionUUIDs.add(uuid);
	    }
	}

    }

    @Override
    public int isQbQuestionModified(McQuestionDTO questionDTO) {
	String name = questionDTO.getName();

	// skip empty questions
	if (name.isEmpty()) {
	    return IQbService.QUESTION_MODIFIED_NONE;
	}
	QbQuestion baseLine = questionDTO.getQbQuestionUid() == null
		? null
		: (QbQuestion) mcQueContentDAO.find(QbQuestion.class, questionDTO.getQbQuestionUid());
	if (baseLine == null) {
	    return IQbService.QUESTION_MODIFIED_ID_BUMP;
	}

	releaseQbQuestionFromCache(baseLine);
	// make a clone to check if data changed
	QbQuestion modifiedQuestion = baseLine.clone();
	releaseQbQuestionFromCache(modifiedQuestion);

	String currentMark = questionDTO.getMark();
	/* set the default mark in case it is not provided */
	if (currentMark == null) {
	    currentMark = "1";
	}

	// set clone's data to current values
	modifiedQuestion.setName(name);
	modifiedQuestion.setDescription(questionDTO.getDescription());
	modifiedQuestion.setMaxMark(Integer.valueOf(currentMark));
	modifiedQuestion.setFeedback(questionDTO.getFeedback());

	List<McOptionDTO> optionDTOs = questionDTO.getOptionDtos();
	boolean isModified =
		!baseLine.equals(modifiedQuestion) || optionDTOs.size() != modifiedQuestion.getQbOptions().size();
	if (isModified) {
	    return IQbService.QUESTION_MODIFIED_VERSION_BUMP;
	}
	// check if options changed
	for (int i = 0; i < optionDTOs.size(); i++) {
	    McOptionDTO optionDTO = optionDTOs.get(i);
	    String optionText = optionDTO.getCandidateAnswer();
	    boolean isCorrectOption = "Correct".equals(optionDTO.getCorrect());

	    //find persisted option if it exists
	    for (QbOption qbOption : modifiedQuestion.getQbOptions()) {
		if (optionDTO.getQbOptionUid().equals(qbOption.getUid())) {
		    qbOption.setCorrect(isCorrectOption);
		    qbOption.setName(optionText);
		    qbOption.setDisplayOrder(i + 1);
		    break;
		}
	    }
	}
	// run check again, this time with modified options
	return !baseLine.equals(modifiedQuestion)
		? IQbService.QUESTION_MODIFIED_VERSION_BUMP
		: IQbService.QUESTION_MODIFIED_NONE;
    }

    /**
     * Updates updates this iRAT activity with tRAT questions.
     */
    @Override
    public List<QbToolQuestion> replaceQuestions(long toolContentId, String newActivityName,
	    List<QbQuestion> newQuestions) {
	McContent mcContent = getMcContent(toolContentId);
	if (newActivityName != null) {
	    mcContent.setTitle(newActivityName);
	    mcContentDAO.updateMcContent(mcContent);
	}

	// remove all existing questions
	for (McQueContent mcQuestion : mcContent.getMcQueContents()) {
	    mcQueContentDAO.delete(mcQuestion);
	}
	// this is needed, otherwise Hibernate wants to re-save the deleted MC questions
	mcContent.getMcQueContents().clear();

	// populate MC with new questions
	int displayOrder = 1;
	List<QbToolQuestion> result = new ArrayList<>(newQuestions.size());
	for (QbQuestion qbQuestion : newQuestions) {
	    McQueContent mcQuestion = new McQueContent(qbQuestion, displayOrder++, mcContent);
	    mcQueContentDAO.insert(mcQuestion);
	    mcContent.getMcQueContents().add(mcQuestion);
	    result.add(mcQuestion);
	}
	return result;
    }

    @Override
    public boolean syncRatQuestions(long toolContentId, List<Long> newQuestionUids) {
	McContent mcContent = getMcContent(toolContentId);

	List<McQueContent> existingReferences = new ArrayList<>(mcContent.getMcQueContents());
	List<McQueContent> newReferences = new ArrayList<>();
	Set<McQueContent> referencesToRemove = new HashSet<>(existingReferences);

	int displayOrder = 0;
	boolean syncNeeded = false;
	for (Long newQuestionUid : newQuestionUids) {
	    QbQuestion newQuestion = qbService.getQuestionByUid(newQuestionUid);
	    displayOrder++;

	    McQueContent matchingReference = null;
	    for (McQueContent existingReference : existingReferences) {
		// try to find exactly same question
		if (newQuestion.getUid().equals(existingReference.getQbQuestion().getUid())) {
		    matchingReference = existingReference;
		    syncNeeded |= displayOrder != existingReference.getDisplayOrder();
		    break;
		}
	    }

	    if (matchingReference == null) {
		syncNeeded = true;
		for (McQueContent existingReference : existingReferences) {
		    // try to find same question with another version
		    if (newQuestion.getQuestionId().equals(existingReference.getQbQuestion().getQuestionId())) {
			existingReference.setQbQuestion(newQuestion);
			matchingReference = existingReference;
			break;
		    }
		}
	    }

	    if (matchingReference == null) {
		// build question reference from scratch
		matchingReference = new McQueContent();
		matchingReference.setDisplayOrder(displayOrder);
		matchingReference.setAnswerRequired(true);
		matchingReference.setQbQuestion(newQuestion);
		matchingReference.setToolContentId(toolContentId);
		mcQueContentDAO.saveOrUpdateMcQueContent(matchingReference);
	    } else {
		matchingReference.setDisplayOrder(displayOrder);
		referencesToRemove.remove(matchingReference);
	    }

	    newReferences.add(matchingReference);
	}

	// all this collections clearing is for Hibernate to feel safe
	existingReferences.clear();
	syncNeeded |= !referencesToRemove.isEmpty();

	if (!syncNeeded) {
	    return false;
	}

	mcContent.getMcQueContents().clear();

	for (McQueContent referenceToRemove : referencesToRemove) {
	    // remove question removed from the matching RAT activity
	    mcQueContentDAO.removeMcQueContent(referenceToRemove);
	}
	referencesToRemove.clear();

	mcContent.getMcQueContents().addAll(newReferences);
	newReferences.clear();

	mcContentDAO.saveOrUpdateMc(mcContent);

	return true;
    }

    @Override
    public void replaceQuestion(long toolContentId, long oldQbQuestionUid, long newQbQuestionUid) {
	throw new UnsupportedOperationException("MCQ tool does not support single question replacement yet");
    }

    @Override
    public void setConfigValue(String key, String value) {
	mcConfigDAO.setConfigValue(key, value);
    }

    @Override
    public String getConfigValue(String key) {
	return mcConfigDAO.getConfigValue(key);
    }

    private void releaseQbQuestionFromCache(QbQuestion qbQuestion) {
	mcQueContentDAO.releaseFromCache(qbQuestion);
	for (QbOption option : qbQuestion.getQbOptions()) {
	    mcQueContentDAO.releaseFromCache(option);
	}
    }
}