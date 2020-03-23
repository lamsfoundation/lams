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

package org.lamsfoundation.lams.tool.scratchie.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
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
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.dao.BurningQuestionLikeDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieAnswerVisitDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieBurningQuestionDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieConfigItemDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieItemDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieSessionDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieUserDAO;
import org.lamsfoundation.lams.tool.scratchie.dto.BurningQuestionDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.BurningQuestionItemDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.GroupSummary;
import org.lamsfoundation.lams.tool.scratchie.dto.LeaderResultsDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.ScratchieItemDTO;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswer;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswerVisitLog;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieBurningQuestion;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieConfigItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieAnswerComparator;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieItemComparator;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.NumberUtil;
import org.lamsfoundation.lams.util.excel.ExcelRow;
import org.lamsfoundation.lams.util.excel.ExcelSheet;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Andrey Balan
 */
public class ScratchieServiceImpl
	implements IScratchieService, ToolContentManager, ToolSessionManager, ToolRestManager {
    private static Logger log = Logger.getLogger(ScratchieServiceImpl.class.getName());

    private ScratchieDAO scratchieDao;

    private ScratchieItemDAO scratchieItemDao;

    private ScratchieUserDAO scratchieUserDao;

    private ScratchieSessionDAO scratchieSessionDao;

    private ScratchieAnswerVisitDAO scratchieAnswerVisitDao;

    private ScratchieBurningQuestionDAO scratchieBurningQuestionDao;

    private BurningQuestionLikeDAO burningQuestionLikeDao;

    private ScratchieConfigItemDAO scratchieConfigItemDao;

    // tool service
    private IToolContentHandler scratchieToolContentHandler;

    private MessageService messageService;

    // system services

    private ILamsToolService toolService;

    private IUserManagementService userManagementService;

    private IExportToolContentService exportContentService;

    private ILogEventService logEventService;

    private ICoreNotebookService coreNotebookService;

    private IEventNotificationService eventNotificationService;

    private ScratchieOutputFactory scratchieOutputFactory;

    // *******************************************************************************
    // Service method
    // *******************************************************************************

    @Override
    public Scratchie getScratchieByContentId(Long contentId) {
	Scratchie rs = scratchieDao.getByContentId(contentId);
	return rs;
    }

    @Override
    public Scratchie getDefaultContent(Long contentId) throws ScratchieApplicationException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    log.error(error);
	    throw new ScratchieApplicationException(error);
	}

	Scratchie defaultContent = getDefaultScratchie();
	// save default content by given ID.
	Scratchie content = new Scratchie();
	content = Scratchie.newInstance(defaultContent, contentId);
	return content;
    }

    @Override
    public List<ScratchieItem> getAuthoredItems(Long scratchieUid) {
	List<ScratchieItem> res = scratchieItemDao.getAuthoringItems(scratchieUid);
	return res;
    }

    @Override
    public void createUser(ScratchieUser scratchieUser) {
	ScratchieUser user = getUserByIDAndSession(scratchieUser.getUserId(),
		scratchieUser.getSession().getSessionId());
	if (user == null) {
	    user = scratchieUser;
	}
	// Save it no matter if the user already exists.
	// At checkLeaderSelectToolForSessionLeader() the user is added to session.
	// Sometimes session save is earlier that user save in another thread, leading to an exception.
	scratchieUserDao.saveObject(user);
    }

    @Override
    public ScratchieUser getUserByIDAndSession(Long userId, Long sessionId) {
	return scratchieUserDao.getUserByUserIDAndSessionID(userId, sessionId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ScratchieUser getUserByLoginAndSessionId(String login, long toolSessionId) {
	List<User> user = scratchieUserDao.findByProperty(User.class, "login", login);
	return user.isEmpty() ? null
		: scratchieUserDao.getUserByUserIDAndSessionID(user.get(0).getUserId().longValue(), toolSessionId);
    }

    @Override
    public int countUsersByContentId(Long contentId) {
	return scratchieUserDao.countUsersByContentId(contentId);
    }

    @Override
    public void saveOrUpdateScratchie(Scratchie scratchie) {
	scratchieDao.saveObject(scratchie);
    }

    @Override
    public void releaseItemsFromCache(Scratchie scratchie) {
	for (ScratchieItem item : scratchie.getScratchieItems()) {
	    scratchieItemDao.releaseItemFromCache(item);
	}
    }

    @Override
    public ScratchieConfigItem getConfigItem(String key) {
	return scratchieConfigItemDao.getConfigItemByKey(key);
    }

    @Override
    public void saveOrUpdateScratchieConfigItem(ScratchieConfigItem item) {
	scratchieConfigItemDao.saveOrUpdate(item);
    }

    @Override
    public String[] getPresetMarks(Scratchie scratchie) {
	String presetMarks = "";
	if (StringUtils.isNotEmpty(scratchie.getPresetMarks())) {
	    presetMarks = scratchie.getPresetMarks();
	} else {
	    ScratchieConfigItem defaultPresetMarks = getConfigItem(ScratchieConfigItem.KEY_PRESET_MARKS);
	    if (defaultPresetMarks != null) {
		presetMarks = defaultPresetMarks.getConfigValue();
	    }
	}

	//remove all white spaces and split the settings around matches of ","
	return presetMarks.replaceAll("\\s+", "").split(",");
    }

    @Override
    public int getMaxPossibleScore(Scratchie scratchie) {
	int itemsNumber = scratchie.getScratchieItems().size();

	// calculate totalMarksPossible
	String[] presetMarks = getPresetMarks(scratchie);
	int maxPossibleScore = (presetMarks.length > 0) ? itemsNumber * Integer.parseInt(presetMarks[0]) : 0;

	// count an extra point if such option is ON
	if (scratchie.isExtraPoint()) {
	    maxPossibleScore += itemsNumber;
	}

	return maxPossibleScore;
    }

    @Override
    public void deleteScratchieItem(Long uid) {
	scratchieItemDao.removeObject(ScratchieItem.class, uid);
    }

    @Override
    public void populateItemsWithConfidenceLevels(Long userId, Long toolSessionId, Integer confidenceLevelsActivityUiid,
	    Collection<ScratchieItem> items) {
	List<ConfidenceLevelDTO> confidenceLevelDtos = toolService
		.getConfidenceLevelsByActivity(confidenceLevelsActivityUiid, userId.intValue(), toolSessionId);

	//populate Scratchie items with confidence levels
	for (ScratchieItem item : items) {

	    //init answers' confidenceLevelDtos list
	    for (ScratchieAnswer answer : item.getAnswers()) {
		LinkedList<ConfidenceLevelDTO> confidenceLevelDtosTemp = new LinkedList<>();
		answer.setConfidenceLevelDtos(confidenceLevelDtosTemp);
	    }

	    //Assessment (similar with Scratchie) adds '\n' at the end of question, MCQ - '\r\n'
	    String question = item.getDescription() == null ? "" : item.getDescription().replaceAll("(\\r|\\n)", "");

	    //find according confidenceLevelDto
	    for (ConfidenceLevelDTO confidenceLevelDto : confidenceLevelDtos) {
		if (question.equals(confidenceLevelDto.getQuestion().replaceAll("(\\r|\\n)", ""))) {

		    //find according answer
		    for (ScratchieAnswer answer : item.getAnswers()) {
			String answerText = answer.getDescription().replaceAll("(\\r|\\n)", "");
			if (answerText.equals(confidenceLevelDto.getAnswer().replaceAll("(\\r|\\n)", ""))) {
			    answer.getConfidenceLevelDtos().add(confidenceLevelDto);
			}
		    }

		}
	    }

	}
    }

    @Override
    public Set<ToolActivity> getPrecedingConfidenceLevelsActivities(Long toolContentId) {
	return toolService.getPrecedingConfidenceLevelsActivities(toolContentId);
    }

    @Override
    public boolean isUserGroupLeader(Long userId, Long toolSessionId) {
	ScratchieSession session = getScratchieSessionBySessionId(toolSessionId);
	ScratchieUser groupLeader = session.getGroupLeader();

	return (groupLeader != null) && userId.equals(groupLeader.getUserId());
    }

    @Override
    public ScratchieUser checkLeaderSelectToolForSessionLeader(ScratchieUser user, Long toolSessionId) {
	if ((user == null) || (toolSessionId == null)) {
	    return null;
	}
	ScratchieSession scratchieSession = getScratchieSessionBySessionId(toolSessionId);
	ScratchieUser leader = scratchieSession.getGroupLeader();
	// check leader select tool for a leader only in case scratchie tool doesn't know it. As otherwise it will screw
	// up previous scratches done
	if (leader == null) {
	    Long leaderUserId = toolService.getLeaderUserId(toolSessionId, user.getUserId().intValue());
	    if (leaderUserId != null) {
		leader = getUserByIDAndSession(leaderUserId, toolSessionId);

		// create new user in a DB
		if (leader == null) {
		    log.debug("creating new user with userId: " + leaderUserId);
		    User leaderDto = (User) getUserManagementService().findById(User.class, leaderUserId.intValue());
		    leader = new ScratchieUser(leaderDto.getUserDTO(), scratchieSession);
		    this.createUser(leader);
		}

		// set group leader
		scratchieSession.setGroupLeader(leader);
		saveOrUpdateScratchieSession(scratchieSession);
	    }
	}
	return leader;
    }

    @Override
    public void launchTimeLimit(Long sessionId) {
	ScratchieSession session = getScratchieSessionBySessionId(sessionId);
	int timeLimit = session.getScratchie().getTimeLimit();
	if (timeLimit == 0) {
	    return;
	}

	//store timeLimitLaunchedDate into DB
	Date timeLimitLaunchedDate = new Date();
	session.setTimeLimitLaunchedDate(timeLimitLaunchedDate);
	scratchieSessionDao.saveObject(session);
    }

    @Override
    public boolean isWaitingForLeaderToSubmitNotebook(ScratchieSession toolSession) {
	Long toolSessionId = toolSession.getSessionId();
	Scratchie scratchie = toolSession.getScratchie();
	ScratchieUser groupLeader = toolSession.getGroupLeader();

	boolean isReflectOnActivity = scratchie.isReflectOnActivity();
	// get notebook entry
	NotebookEntry notebookEntry = null;
	if (isReflectOnActivity && (groupLeader != null)) {
	    notebookEntry = getEntry(toolSessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ScratchieConstants.TOOL_SIGNATURE, groupLeader.getUserId().intValue());
	}

	// return whether it's waiting for the leader to submit notebook
	return isReflectOnActivity && (notebookEntry == null);
    }

    @Override
    public void changeUserMark(Long userId, Long sessionId, Integer newMark) {
	if (newMark == null) {
	    return;
	}

	ScratchieSession session = this.getScratchieSessionBySessionId(sessionId);
	int oldMark = session.getMark();

	session.setMark(newMark);
	scratchieSessionDao.saveObject(session);

	// propagade new mark to Gradebook for all students in a group
	List<ScratchieUser> users = this.getUsersBySession(sessionId);
	for (ScratchieUser user : users) {

	    toolService.updateActivityMark(newMark.doubleValue(), null, user.getUserId().intValue(),
		    user.getSession().getSessionId(), false);

	    // record mark change with audit service
	    Long toolContentId = null;
	    if (session.getScratchie() != null) {
		toolContentId = session.getScratchie().getContentId();
	    }

	    logEventService.logMarkChange(user.getUserId(), user.getLoginName(), toolContentId, "" + oldMark,
		    "" + newMark);
	}

    }

    @Override
    public Scratchie getScratchieBySessionId(Long sessionId) {
	ScratchieSession session = scratchieSessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getScratchie().getContentId();
	return scratchieDao.getByContentId(contentId);
    }

    @Override
    public ScratchieSession getScratchieSessionBySessionId(Long sessionId) {
	return scratchieSessionDao.getSessionBySessionId(sessionId);
    }

    @Override
    public int countSessionsByContentId(Long toolContentId) {
	return scratchieSessionDao.getByContentId(toolContentId).size();
    }

    @Override
    public void saveOrUpdateScratchieSession(ScratchieSession resSession) {
	scratchieSessionDao.saveObject(resSession);
    }

    @Override
    public void recordItemScratched(Long sessionId, Long answerUid) {
	ScratchieAnswer answer = this.getScratchieAnswerByUid(answerUid);
	if (answer == null) {
	    return;
	}

	ScratchieAnswerVisitLog log = scratchieAnswerVisitDao.getLog(answerUid, sessionId);
	if (log == null) {
	    log = new ScratchieAnswerVisitLog();
	    log.setScratchieAnswer(answer);
	    log.setSessionId(sessionId);
	    log.setAccessDate(new Timestamp(new Date().getTime()));
	    scratchieAnswerVisitDao.saveObject(log);
	}

	this.recalculateMarkForSession(sessionId, false);
    }

    /**
     * Recalculate mark for leader and sets it to all members of a group
     *
     * @param leader
     * @param answerUid
     */
    @Override
    public void recalculateMarkForSession(Long sessionId, boolean isPropagateToGradebook) {
	ScratchieSession session = this.getScratchieSessionBySessionId(sessionId);
	Scratchie scratchie = session.getScratchie();
	Set<ScratchieItem> items = scratchie.getScratchieItems();

	populateScratchieItemsWithMarks(scratchie, scratchie.getScratchieItems(), sessionId);

	// calculate mark
	int mark = 0;
	for (ScratchieItem item : items) {
	    mark += item.getMark();
	}

	// change mark for all learners in a group
	session.setMark(mark);
	scratchieSessionDao.saveObject(session);

	// propagade changes to Gradebook
	if (isPropagateToGradebook) {
	    List<ScratchieUser> users = this.getUsersBySession(sessionId);
	    for (ScratchieUser user : users) {
		toolService.updateActivityMark(Double.valueOf(mark), null, user.getUserId().intValue(),
			user.getSession().getSessionId(), false);
	    }
	}
    }

    @Override
    public void recalculateUserAnswers(Scratchie scratchie, Set<ScratchieItem> oldItems, Set<ScratchieItem> newItems,
	    List<ScratchieItem> deletedItems) {

	// create list of modified questions
	List<ScratchieItem> modifiedItems = new ArrayList<>();
	for (ScratchieItem oldItem : oldItems) {
	    for (ScratchieItem newItem : newItems) {
		if (oldItem.getUid().equals(newItem.getUid())) {

		    boolean isItemModified = false;

		    // title or description is different
		    if (!oldItem.getTitle().equals(newItem.getTitle())
			    || !oldItem.getDescription().equals(newItem.getDescription())) {
			isItemModified = true;
		    }

		    // options are different
		    Set<ScratchieAnswer> oldAnswers = oldItem.getAnswers();
		    Set<ScratchieAnswer> newAnswers = newItem.getAnswers();
		    for (ScratchieAnswer oldAnswer : oldAnswers) {
			for (ScratchieAnswer newAnswer : newAnswers) {
			    if (oldAnswer.getUid().equals(newAnswer.getUid())) {

				if (!StringUtils.equals(oldAnswer.getDescription(), newAnswer.getDescription())
					|| (oldAnswer.isCorrect() != newAnswer.isCorrect())) {
				    isItemModified = true;
				}
			    }
			}
		    }

		    if (isItemModified) {
			modifiedItems.add(newItem);
		    }
		}
	    }
	}

	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(scratchie.getContentId());
	for (ScratchieSession session : sessionList) {
	    Long toolSessionId = session.getSessionId();
	    List<ScratchieAnswerVisitLog> visitLogsToDelete = new ArrayList<>();
	    boolean isRecalculateMarks = false;

	    // remove all scratches for modified and removed items

	    // [+] if the item was removed
	    for (ScratchieItem deletedItem : deletedItems) {
		List<ScratchieAnswerVisitLog> visitLogs = scratchieAnswerVisitDao.getLogsBySessionAndItem(toolSessionId,
			deletedItem.getUid());
		visitLogsToDelete.addAll(visitLogs);
	    }

	    // [+] if the question is modified
	    for (ScratchieItem modifiedItem : modifiedItems) {
		List<ScratchieAnswerVisitLog> visitLogs = scratchieAnswerVisitDao.getLogsBySessionAndItem(toolSessionId,
			modifiedItem.getUid());
		visitLogsToDelete.addAll(visitLogs);
	    }

	    // remove all visit logs marked for deletion
	    Iterator<ScratchieAnswerVisitLog> iter = visitLogsToDelete.iterator();
	    while (iter.hasNext()) {
		ScratchieAnswerVisitLog visitLogToDelete = iter.next();
		iter.remove();
		scratchieAnswerVisitDao.removeObject(ScratchieAnswerVisitLog.class, visitLogToDelete.getUid());
		isRecalculateMarks = true;
	    }

	    // [+] doing nothing if the new question was added

	    // recalculate marks if it's required
	    if (isRecalculateMarks) {
		recalculateMarkForSession(toolSessionId, true);
	    }
	}

    }

    @Override
    public List<ScratchieBurningQuestion> getBurningQuestionsBySession(Long sessionId) {
	return scratchieBurningQuestionDao.getBurningQuestionsBySession(sessionId);
    }

    @Override
    public void saveBurningQuestion(Long sessionId, Long itemUid, String question) {

	boolean isGeneralBurningQuestion = itemUid == null;

	ScratchieBurningQuestion burningQuestion = (isGeneralBurningQuestion)
		? scratchieBurningQuestionDao.getGeneralBurningQuestionBySession(sessionId)
		: scratchieBurningQuestionDao.getBurningQuestionBySessionAndItem(sessionId, itemUid);

	if (burningQuestion == null) {
	    burningQuestion = new ScratchieBurningQuestion();
	    if (!isGeneralBurningQuestion) {
		ScratchieItem item = scratchieItemDao.getByUid(itemUid);
		burningQuestion.setScratchieItem(item);
	    }
	    burningQuestion.setGeneralQuestion(isGeneralBurningQuestion);
	    burningQuestion.setSessionId(sessionId);
	    burningQuestion.setAccessDate(new Date());
	}
	burningQuestion.setQuestion(question);

	scratchieBurningQuestionDao.saveObject(burningQuestion);
    }

    @Override
    public ScratchieAnswer getScratchieAnswerByUid(Long answerUid) {
	ScratchieAnswer res = (ScratchieAnswer) userManagementService.findById(ScratchieAnswer.class, answerUid);
	return res;
    }

    @Override
    public void setScratchingFinished(Long toolSessionId) throws IOException {
	ScratchieSession session = this.getScratchieSessionBySessionId(toolSessionId);
	session.setScratchingFinished(true);
	scratchieSessionDao.saveObject(session);

	recalculateMarkForSession(toolSessionId, false);
    }

    @Override
    public String finishToolSession(Long toolSessionId, Long userId) throws ScratchieApplicationException {
	String nextUrl = null;
	try {
	    ScratchieUser user = scratchieUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	    user.setSessionFinished(true);
	    scratchieUserDao.saveObject(user);

	    //if this is a leader finishes, complete all non-leaders as well
	    boolean isUserGroupLeader = user.getSession().isUserGroupLeader(user.getUid());
	    if (isUserGroupLeader) {
		getUsersBySession(toolSessionId).forEach(sessionUser -> {
		    //finish non-leader
		    sessionUser.setSessionFinished(true);
		    scratchieUserDao.saveObject(user);

		    // as long as there is no individual results in Scratchie tool (but rather one for entire group) there is no
		    // need to copyAnswersFromLeader()
		});
	    }

	    nextUrl = this.leaveToolSession(toolSessionId, userId);
	} catch (DataMissingException e) {
	    throw new ScratchieApplicationException(e);
	} catch (ToolException e) {
	    throw new ScratchieApplicationException(e);
	}
	return nextUrl;
    }

    @Override
    public ScratchieItem getScratchieItemByUid(Long itemUid) {
	return scratchieItemDao.getByUid(itemUid);
    }

    @Override
    public Set<ScratchieUser> getAllLeaders(Long contentId) {
	Set<ScratchieUser> leaders = new TreeSet<>();
	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(contentId);
	for (ScratchieSession session : sessionList) {
	    ScratchieUser leader = session.getGroupLeader();
	    if (leader != null) {
		leaders.add(leader);
	    }
	}
	return leaders;
    }

    @Override
    public List<ScratchieUser> getUsersBySession(Long toolSessionId) {
	return scratchieUserDao.getBySessionID(toolSessionId);
    }

    @Override
    public ScratchieUser getUserByUserIDAndContentID(Long userId, Long contentId) {
	return scratchieUserDao.getUserByUserIDAndContentID(userId, contentId);
    }

    @Override
    public void saveUser(ScratchieUser user) {
	scratchieUserDao.saveObject(user);
    }

    @Override
    /*
     * If isIncludeOnlyLeaders then include the portrait ids needed for monitoring. If false then it
     * is probably the export and that doesn't need portraits.
     */
    public List<GroupSummary> getMonitoringSummary(Long contentId, boolean addPortraits) {
	List<GroupSummary> groupSummaryList = new ArrayList<>();
	List<ScratchieSession> sessions = scratchieSessionDao.getByContentId(contentId);

	for (ScratchieSession session : sessions) {

	    Long sessionId = session.getSessionId();

	    Collection<User> groupUsers = toolService.getToolSession(sessionId).getLearners();

	    // one new summary for one session.
	    GroupSummary groupSummary = new GroupSummary(session);

	    int totalAttempts = scratchieAnswerVisitDao.getLogCountTotal(sessionId);
	    groupSummary.setTotalAttempts(totalAttempts);

	    Map<Long, ScratchieUser> sessionUsers = getUsersBySession(sessionId).stream()
		    .collect(Collectors.toMap(ScratchieUser::getUserId, s -> s));
	    groupSummary.setUsersWhoReachedActivity(sessionUsers.keySet());

	    List<ScratchieUser> usersToShow = new LinkedList<>();
	    for (User user : groupUsers) {
		boolean isUserGroupLeader = false;
		ScratchieUser scratchieUser = sessionUsers.get(user.getUserId().longValue());
		if (scratchieUser == null) {
		    scratchieUser = new ScratchieUser();
		    scratchieUser.setFirstName(user.getFirstName());
		    scratchieUser.setLastName(user.getLastName());
		    scratchieUser.setLoginName(user.getLogin());
		    scratchieUser.setUserId(user.getUserId().longValue());
		} else {
		    isUserGroupLeader = session.isUserGroupLeader(scratchieUser.getUid());
		    if (isUserGroupLeader) {
			groupSummary.setLeaderUid(scratchieUser.getUid());
		    }
		}

		scratchieUser.setPortraitId(user.getPortraitUuid());
		usersToShow.add(scratchieUser);
	    }

	    groupSummary.setUsers(usersToShow);
	    groupSummaryList.add(groupSummary);
	}
	return groupSummaryList;
    }

    @Override
    public void getScratchesOrder(Collection<ScratchieItem> items, Long sessionId) {
	for (ScratchieItem item : items) {
	    List<ScratchieAnswerVisitLog> itemLogs = scratchieAnswerVisitDao.getLogsBySessionAndItem(sessionId,
		    item.getUid());

	    for (ScratchieAnswer answer : item.getAnswers()) {

		int attemptNumber;
		ScratchieAnswerVisitLog log = scratchieAnswerVisitDao.getLog(answer.getUid(), sessionId);
		if (log == null) {
		    // -1 if there is no log
		    attemptNumber = -1;
		} else {
		    // adding 1 to start from 1.
		    attemptNumber = itemLogs.indexOf(log) + 1;
		}

		answer.setAttemptOrder(attemptNumber);
	    }
	}
    }

    @Override
    public Collection<ScratchieItem> getItemsWithIndicatedScratches(Long toolSessionId) {

	Scratchie scratchie = this.getScratchieBySessionId(toolSessionId);
	Set<ScratchieItem> items = new TreeSet<>(new ScratchieItemComparator());
	items.addAll(scratchie.getScratchieItems());

	return getItemsWithIndicatedScratches(toolSessionId, items);
    }

    @Override
    public Collection<ScratchieItem> getItemsWithIndicatedScratches(Long toolSessionId,
	    Collection<ScratchieItem> items) {
	List<ScratchieAnswerVisitLog> userLogs = scratchieAnswerVisitDao.getLogsBySession(toolSessionId);

	for (ScratchieItem item : items) {

	    for (ScratchieAnswer answer : item.getAnswers()) {

		// find according log if it exists
		ScratchieAnswerVisitLog log = null;
		for (ScratchieAnswerVisitLog userLog : userLogs) {
		    if (userLog.getScratchieAnswer().getUid().equals(answer.getUid())) {
			log = userLog;
			break;
		    }
		}
		if (log == null) {
		    answer.setScratched(false);
		} else {
		    answer.setScratched(true);
		}
	    }

	    boolean isItemUnraveled = this.isItemUnraveled(item, userLogs);
	    item.setUnraveled(isItemUnraveled);
	}

	return items;
    }

    /**
     * Check if the specified item was unraveled by user
     *
     * @param item
     *            specified item
     * @param userLogs
     *            uses logs from it (The main reason to have this parameter is to reduce number of queries to DB)
     * @return
     */
    private boolean isItemUnraveled(ScratchieItem item, List<ScratchieAnswerVisitLog> userLogs) {
	boolean isItemUnraveled = false;

	for (ScratchieAnswer answer : item.getAnswers()) {

	    ScratchieAnswerVisitLog log = null;
	    for (ScratchieAnswerVisitLog userLog : userLogs) {
		if (userLog.getScratchieAnswer().getUid().equals(answer.getUid())) {
		    log = userLog;
		    break;
		}
	    }

	    if (log != null) {
		isItemUnraveled |= answer.isCorrect();
	    }
	}

	return isItemUnraveled;
    }

    @Override
    public void populateScratchieItemsWithMarks(Scratchie scratchie, Collection<ScratchieItem> items, long sessionId) {
	List<ScratchieAnswerVisitLog> userLogs = scratchieAnswerVisitDao.getLogsBySession(sessionId);
	String[] presetMarks = getPresetMarks(scratchie);

	for (ScratchieItem item : items) {
	    // get lowest mark by default
	    int mark = Integer.parseInt(presetMarks[presetMarks.length - 1]);
	    // add mark only if an item was unravelled
	    if (isItemUnraveled(item, userLogs)) {

		int itemAttempts = calculateItemAttempts(userLogs, item);
		String markStr = (itemAttempts <= presetMarks.length) ? presetMarks[itemAttempts - 1]
			: presetMarks[presetMarks.length - 1];
		mark = Integer.parseInt(markStr);

		// add extra point if needed
		if (scratchie.isExtraPoint() && (itemAttempts == 1)) {
		    mark++;
		}

	    }
	    item.setMark(mark);
	}
    }

    /**
     * Returns number of scraches user done for the specified item.
     */
    private int calculateItemAttempts(List<ScratchieAnswerVisitLog> userLogs, ScratchieItem item) {

	int itemAttempts = 0;
	for (ScratchieAnswerVisitLog userLog : userLogs) {
	    if (userLog.getScratchieAnswer().getScratchieItem().getUid().equals(item.getUid())) {
		itemAttempts++;
	    }
	}

	return itemAttempts;
    }

    @Override
    public List<GroupSummary> getQuestionSummary(Long contentId, Long itemUid) {
	List<GroupSummary> groupSummaryList = new ArrayList<>();

	ScratchieItem item = scratchieItemDao.getByUid(itemUid);
	Collection<ScratchieAnswer> answers = item.getAnswers();
	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(contentId);
	for (ScratchieSession session : sessionList) {
	    Long sessionId = session.getSessionId();

	    // one new summary for one session.
	    GroupSummary groupSummary = new GroupSummary(session);

	    Map<Long, ScratchieAnswer> answerMap = new HashMap<>();
	    for (ScratchieAnswer dbAnswer : (Set<ScratchieAnswer>) answers) {

		// clone it so it doesn't interfere with values from other sessions
		ScratchieAnswer answer = (ScratchieAnswer) dbAnswer.clone();
		answer.setUid(dbAnswer.getUid());
		int[] attempts = new int[answers.size()];
		answer.setAttempts(attempts);
		answerMap.put(dbAnswer.getUid(), answer);
	    }
	    List<ScratchieAnswerVisitLog> sessionAttempts = scratchieAnswerVisitDao.getLogsBySessionAndItem(sessionId,
		    itemUid);

	    List<ScratchieUser> users = scratchieUserDao.getBySessionID(sessionId);

	    // calculate attempts table
	    for (ScratchieUser user : users) {
		int attemptNumber = 0;

		for (ScratchieAnswerVisitLog attempt : sessionAttempts) {
		    ScratchieAnswer answer = answerMap.get(attempt.getScratchieAnswer().getUid());
		    int[] attempts = answer.getAttempts();
		    // +1 for corresponding choice
		    attempts[attemptNumber++]++;
		}
	    }

	    Collection<ScratchieAnswer> sortedAnswers = new TreeSet<>(new ScratchieAnswerComparator());
	    sortedAnswers.addAll(answerMap.values());
	    groupSummary.setAnswers(sortedAnswers);
	    groupSummaryList.add(groupSummary);
	}

	// show total groupSummary if there is more than 1 group available
	if (sessionList.size() > 1) {
	    GroupSummary groupSummaryTotal = new GroupSummary();
	    groupSummaryTotal.setSessionId(0l);
	    groupSummaryTotal.setSessionName("Summary");
	    groupSummaryTotal.setMark(0);

	    Map<Long, ScratchieAnswer> answerMapTotal = new HashMap<>();
	    for (ScratchieAnswer dbAnswer : (Set<ScratchieAnswer>) answers) {
		// clone it so it doesn't interfere with values from other sessions
		ScratchieAnswer answer = (ScratchieAnswer) dbAnswer.clone();
		int[] attempts = new int[answers.size()];
		answer.setAttempts(attempts);
		answerMapTotal.put(dbAnswer.getUid(), answer);
	    }

	    for (GroupSummary groupSummary : groupSummaryList) {
		Collection<ScratchieAnswer> sortedAnswers = groupSummary.getAnswers();
		for (ScratchieAnswer sortedAnswer : sortedAnswers) {
		    int[] attempts = sortedAnswer.getAttempts();

		    ScratchieAnswer answerTotal = answerMapTotal.get(sortedAnswer.getUid());
		    int[] attemptsTotal = answerTotal.getAttempts();
		    for (int i = 0; i < attempts.length; i++) {
			attemptsTotal[i] += attempts[i];
		    }
		}
	    }

	    Collection<ScratchieAnswer> sortedAnswers = new TreeSet<>(new ScratchieAnswerComparator());
	    sortedAnswers.addAll(answerMapTotal.values());
	    groupSummaryTotal.setAnswers(sortedAnswers);
	    groupSummaryList.add(0, groupSummaryTotal);
	}

	return groupSummaryList;
    }

    @Override
    public List<BurningQuestionItemDTO> getBurningQuestionDtos(Scratchie scratchie, Long sessionId,
	    boolean includeEmptyItems) {

	Set<ScratchieItem> items = new TreeSet<>(new ScratchieItemComparator());
	items.addAll(scratchie.getScratchieItems());

	List<BurningQuestionDTO> burningQuestionDtos = scratchieBurningQuestionDao
		.getBurningQuestionsByContentId(scratchie.getUid(), sessionId);

	//in order to group BurningQuestions by items, organise them as a list of BurningQuestionItemDTOs
	List<BurningQuestionItemDTO> burningQuestionItemDtos = new ArrayList<>();
	for (ScratchieItem item : items) {

	    List<BurningQuestionDTO> burningQuestionDtosOfSpecifiedItem = new ArrayList<>();

	    for (BurningQuestionDTO burningQuestionDto : burningQuestionDtos) {
		ScratchieBurningQuestion burningQuestion = burningQuestionDto.getBurningQuestion();

		//general burning question is handled further down
		if (!burningQuestion.isGeneralQuestion()
			&& item.getUid().equals(burningQuestion.getScratchieItem().getUid())) {
		    burningQuestionDtosOfSpecifiedItem.add(burningQuestionDto);
		}
	    }

	    //skip empty items if required
	    if (!burningQuestionDtosOfSpecifiedItem.isEmpty() || includeEmptyItems) {
		BurningQuestionItemDTO burningQuestionItemDto = new BurningQuestionItemDTO();
		burningQuestionItemDto.setScratchieItem(item);
		burningQuestionItemDto.setBurningQuestionDtos(burningQuestionDtosOfSpecifiedItem);
		burningQuestionItemDtos.add(burningQuestionItemDto);
	    }
	}

	// handle general burning question
	BurningQuestionItemDTO generalBurningQuestionItemDto = new BurningQuestionItemDTO();
	ScratchieItem generalDummyItem = new ScratchieItem();
	generalDummyItem.setUid(0L);
	final String generalQuestionMessage = messageService.getMessage("label.general.burning.question");
	generalDummyItem.setTitle(generalQuestionMessage);
	generalBurningQuestionItemDto.setScratchieItem(generalDummyItem);
	List<BurningQuestionDTO> burningQuestionDtosOfSpecifiedItem = new ArrayList<>();
	for (BurningQuestionDTO burningQuestionDto : burningQuestionDtos) {
	    ScratchieBurningQuestion burningQuestion = burningQuestionDto.getBurningQuestion();

	    if (burningQuestion.isGeneralQuestion()) {
		burningQuestionDtosOfSpecifiedItem.add(burningQuestionDto);
	    }
	}
	generalBurningQuestionItemDto.setBurningQuestionDtos(burningQuestionDtosOfSpecifiedItem);
	//skip empty item if required
	if (!burningQuestionDtosOfSpecifiedItem.isEmpty() || includeEmptyItems) {
	    burningQuestionItemDtos.add(generalBurningQuestionItemDto);
	}

	//escape for Javascript
	for (BurningQuestionItemDTO burningQuestionItemDto : burningQuestionItemDtos) {
	    for (BurningQuestionDTO burningQuestionDto : burningQuestionItemDto.getBurningQuestionDtos()) {
		String escapedSessionName = StringEscapeUtils.escapeJavaScript(burningQuestionDto.getSessionName());
		burningQuestionDto.setSessionName(escapedSessionName);

		String escapedBurningQuestion = StringEscapeUtils
			.escapeJavaScript(burningQuestionDto.getBurningQuestion().getQuestion());
		burningQuestionDto.setEscapedBurningQuestion(escapedBurningQuestion);
	    }
	}

	return burningQuestionItemDtos;
    }

    @Override
    public boolean addLike(Long burningQuestionUid, Long sessionId) {
	return burningQuestionLikeDao.addLike(burningQuestionUid, sessionId);
    }

    @Override
    public void removeLike(Long burningQuestionUid, Long sessionId) {
	burningQuestionLikeDao.removeLike(burningQuestionUid, sessionId);
    }

    @Override
    public List<ReflectDTO> getReflectionList(Long contentId) {
	ArrayList<ReflectDTO> reflections = new ArrayList<>();

	// get all available leaders associated with this content as only leaders have reflections
	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(contentId);
	for (ScratchieSession session : sessionList) {

	    ScratchieUser leader = session.getGroupLeader();
	    if (leader != null) {
		NotebookEntry notebookEntry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			ScratchieConstants.TOOL_SIGNATURE, leader.getUserId().intValue());
		if ((notebookEntry != null) && StringUtils.isNotBlank(notebookEntry.getEntry())) {
		    User user = new User();
		    user.setLastName(leader.getLastName());
		    user.setFirstName(leader.getFirstName());
		    ReflectDTO reflectDTO = new ReflectDTO(user);
		    reflectDTO.setGroupName(session.getSessionName());
		    String reflection = notebookEntry.getEntry();
		    reflection = StringEscapeUtils.escapeJavaScript(reflection);
		    reflectDTO.setReflection(reflection);
		    reflectDTO.setIsGroupLeader(session.isUserGroupLeader(leader.getUid()));

		    reflections.add(reflectDTO);
		}
	    }
	}

	return reflections;
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
    public ScratchieUser getUser(Long uid) {
	return (ScratchieUser) scratchieUserDao.getObject(ScratchieUser.class, uid);
    }

    @Override
    public List<ExcelSheet> exportExcel(Long contentId) {
	Scratchie scratchie = scratchieDao.getByContentId(contentId);
	Collection<ScratchieItem> items = new TreeSet<>(new ScratchieItemComparator());
	items.addAll(scratchie.getScratchieItems());
	int numberOfItems = items.size();

	List<ExcelSheet> sheets = new LinkedList<>();

	// ======================================================= For Immediate Analysis page
	// =======================================
	ExcelSheet immediateAnalysisSheet = new ExcelSheet(getMessage("label.for.immediate.analysis"));
	sheets.add(immediateAnalysisSheet);

	ExcelRow row = immediateAnalysisSheet.initRow();
	row.addCell(getMessage("label.quick.analysis"), true);

	row = immediateAnalysisSheet.initRow();
	row.addEmptyCell();
	row.addCell(getMessage("label.in.table.below.we.show"));
	immediateAnalysisSheet.addEmptyRow();

	row = immediateAnalysisSheet.initRow();
	row.addEmptyCells(2);
	row.addCell(getMessage("label.questions"));

	row = immediateAnalysisSheet.initRow();
	row.addEmptyCell();
	row.addCell(getMessage("label.teams"), true);
	for (int itemCount = 0; itemCount < numberOfItems; itemCount++) {
	    row.addCell("Q" + (itemCount + 1), true);
	}
	row.addCell(getMessage("label.total"), true);
	row.addCell(getMessage("label.total") + " %", true);

	List<GroupSummary> summaryByTeam = getSummaryByTeam(scratchie, items);
	for (GroupSummary summary : summaryByTeam) {

	    row = immediateAnalysisSheet.initRow();
	    row.addEmptyCell();
	    row.addCell(summary.getSessionName(), true);

	    int numberOfFirstChoiceEvents = 0;
	    for (ScratchieItemDTO itemDto : summary.getItemDtos()) {
		int attempts = itemDto.getUserAttempts();

		String isFirstChoice;
		IndexedColors color;
		if (itemDto.isUnraveledOnFirstAttempt()) {
		    isFirstChoice = getMessage("label.correct");
		    color = IndexedColors.GREEN;
		    numberOfFirstChoiceEvents++;
		} else if (attempts == 0) {
		    isFirstChoice = null;
		    color = null;
		} else {
		    isFirstChoice = getMessage("label.incorrect");
		    color = IndexedColors.RED;
		}
		row.addCell(isFirstChoice, color);
	    }
	    row.addCell(Integer.valueOf(numberOfFirstChoiceEvents));
	    double percentage = (numberOfItems == 0) ? 0 : (double) numberOfFirstChoiceEvents / numberOfItems;
	    row.addPercentageCell(percentage);
	}

	// ======================================================= For Report by Team TRA page
	// =======================================
	ExcelSheet reportByTeamSheet = new ExcelSheet(getMessage("label.report.by.team.tra"));
	sheets.add(reportByTeamSheet);

	row = reportByTeamSheet.initRow();
	row.addCell(getMessage("label.quick.analysis"), true);

	row = reportByTeamSheet.initRow();
	row.addEmptyCell();
	row.addCell(getMessage("label.table.below.shows.which.answer.teams.selected.first.try"));
	reportByTeamSheet.addEmptyRow();

	row = reportByTeamSheet.initRow();
	row.addEmptyCell();
	for (int itemCount = 0; itemCount < numberOfItems; itemCount++) {
	    row.addCell(getMessage("label.authoring.basic.instruction") + " " + (itemCount + 1));
	}
	row.addCell(getMessage("label.total"));
	row.addCell(getMessage("label.total") + " %");

	row = reportByTeamSheet.initRow();
	row.addCell(getMessage("label.correct.answer"));
	for (ScratchieItem item : items) {

	    // find out the correct answer's sequential letter - A,B,C...
	    String correctAnswerLetter = "";
	    int answerCount = 1;
	    for (ScratchieAnswer answer : item.getAnswers()) {
		if (answer.isCorrect()) {
		    correctAnswerLetter = String.valueOf((char) ((answerCount + 'A') - 1));
		    break;
		}
		answerCount++;
	    }
	    row.addCell(correctAnswerLetter);
	}

	row = reportByTeamSheet.initRow();
	row.addCell(getMessage("monitoring.label.group"));

	int groupCount = 1;
	double[] percentages = new double[summaryByTeam.size()];
	for (GroupSummary summary : summaryByTeam) {

	    row = reportByTeamSheet.initRow();
	    row.addCell(summary.getSessionName());

	    int numberOfFirstChoiceEvents = 0;
	    for (ScratchieItemDTO itemDto : summary.getItemDtos()) {

		IndexedColors color = null;
		if (itemDto.isUnraveledOnFirstAttempt()) {
		    color = IndexedColors.GREEN;
		    numberOfFirstChoiceEvents++;
		}
		row.addCell(itemDto.getAnswersSequence(), color);
	    }
	    row.addCell(Integer.valueOf(numberOfFirstChoiceEvents));
	    double percentage = (numberOfItems == 0) ? 0 : (double) numberOfFirstChoiceEvents / numberOfItems;
	    row.addPercentageCell(percentage);

	    percentages[groupCount - 1] = percentage;
	    groupCount++;
	}

	Arrays.sort(percentages);

	// avg mean
	int sum = 0;
	for (int i = 0; i < percentages.length; i++) {
	    sum += percentages[i];
	}
	int percentagesLength = percentages.length == 0 ? 1 : percentages.length;
	double avgMean = (double) sum / percentagesLength;
	row = reportByTeamSheet.initRow();
	row.addCell(getMessage("label.avg.mean"));
	row.addEmptyCells(numberOfItems + 1);
	row.addPercentageCell(avgMean);

	// median
	double median;
	int middle = percentages.length / 2;
	if ((percentages.length % 2) == 1) {
	    median = percentages[middle];
	} else {
	    median = (percentages[middle - 1] + percentages[middle]) / 2.0;
	}
	row = reportByTeamSheet.initRow();
	row.addCell(getMessage("label.median"));
	row.addEmptyCells(numberOfItems + 1);
	row.addCell(median);

	row = reportByTeamSheet.initRow();
	row.addCell(getMessage("label.legend"));

	row = reportByTeamSheet.initRow();
	row.addCell(getMessage("label.denotes.correct.answer"), IndexedColors.GREEN);

	// ======================================================= Research and Analysis page
	// =======================================
	ExcelSheet researchAndAnalysisSheet = new ExcelSheet(getMessage("label.research.analysis"));
	sheets.add(researchAndAnalysisSheet);

	// Caption
	row = researchAndAnalysisSheet.initRow();
	row.addCell(getMessage("label.scratchie.report"), true);
	researchAndAnalysisSheet.addEmptyRow();
	researchAndAnalysisSheet.addEmptyRow();

	// Overall Summary by Team --------------------------------------------------
	row = researchAndAnalysisSheet.initRow();
	row.addCell(getMessage("label.overall.summary.by.team"), true);

	row = researchAndAnalysisSheet.initRow();
	row.addEmptyCell();
	for (int itemCount = 0; itemCount < numberOfItems; itemCount++) {
	    row.addCell(getMessage("label.for.question", new Object[] { itemCount + 1 }));
	    row.addEmptyCells(2);
	}

	row = researchAndAnalysisSheet.initRow();
	row.addEmptyCell();
	for (int itemCount = 0; itemCount < numberOfItems; itemCount++) {
	    row.addCell(getMessage("label.first.choice"), IndexedColors.BLUE);
	    row.addCell(getMessage("label.attempts"), IndexedColors.BLUE);
	    row.addCell(getMessage("label.mark"), IndexedColors.BLUE);
	}

	for (GroupSummary summary : summaryByTeam) {
	    row = researchAndAnalysisSheet.initRow();

	    row.addCell(summary.getSessionName());

	    for (ScratchieItemDTO itemDto : summary.getItemDtos()) {
		int attempts = itemDto.getUserAttempts();

		String isFirstChoice;
		IndexedColors color;
		if (itemDto.isUnraveledOnFirstAttempt()) {
		    isFirstChoice = getMessage("label.correct");
		    color = IndexedColors.GREEN;
		} else if (attempts == 0) {
		    isFirstChoice = null;
		    color = null;
		} else {
		    isFirstChoice = getMessage("label.incorrect");
		    color = IndexedColors.RED;
		}
		row.addCell(isFirstChoice, color);
		row.addCell(Integer.valueOf(attempts), color);
		Long mark = (itemDto.getUserMark() == -1) ? null : Long.valueOf(itemDto.getUserMark());
		row.addCell(mark);
	    }
	}
	researchAndAnalysisSheet.addEmptyRow();
	researchAndAnalysisSheet.addEmptyRow();
	researchAndAnalysisSheet.addEmptyRow();

	// Overall Summary By Individual Student in each Team----------------------------------------
	row = researchAndAnalysisSheet.initRow();
	row.addCell(getMessage("label.overall.summary.by.individual.student"), true);
	researchAndAnalysisSheet.addEmptyRow();

	row = researchAndAnalysisSheet.initRow();
	row.addEmptyCell();
	row.addCell(getMessage("label.attempts"));
	row.addCell(getMessage("label.mark"));
	row.addCell(getMessage("label.group"));

	List<GroupSummary> summaryList = getMonitoringSummary(contentId, false);
	for (GroupSummary summary : summaryList) {
	    for (ScratchieUser user : summary.getUsers()) {
		row = researchAndAnalysisSheet.initRow();
		row.addCell(user.getFirstName() + " " + user.getLastName());
		row.addCell(Long.valueOf(summary.getTotalAttempts()));
		Long mark = (summary.getTotalAttempts() == 0) ? null : Long.valueOf(summary.getMark());
		row.addCell(mark);
		row.addCell(summary.getSessionName());
	    }
	}
	researchAndAnalysisSheet.addEmptyRow();
	researchAndAnalysisSheet.addEmptyRow();

	// Question Reports-----------------------------------------------------------------
	row = researchAndAnalysisSheet.initRow();
	row.addCell(getMessage("label.question.reports"), true);
	researchAndAnalysisSheet.addEmptyRow();

	SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

	for (ScratchieItem item : items) {
	    List<GroupSummary> itemSummary = getQuestionSummary(contentId, item.getUid());

	    row = researchAndAnalysisSheet.initRow();
	    row.addCell(getMessage("label.question.semicolon", new Object[] { item.getTitle() }), true);

	    row = researchAndAnalysisSheet.initRow();
	    row.addCell(removeHtmlMarkup(item.getDescription()), true);
	    researchAndAnalysisSheet.addEmptyRow();
	    researchAndAnalysisSheet.addEmptyRow();

	    // show all team summary in case there is more than 1 group
	    if (summaryList.size() > 1) {
		row = researchAndAnalysisSheet.initRow();
		row.addCell(getMessage("label.all.teams.summary"), true);

		GroupSummary allTeamSummary = itemSummary.get(0);
		Collection<ScratchieAnswer> answers = allTeamSummary.getAnswers();

		row = researchAndAnalysisSheet.initRow();
		row.addEmptyCell();
		for (int i = 0; i < answers.size(); i++) {
		    row.addCell((long) i + 1, IndexedColors.YELLOW);
		}

		for (ScratchieAnswer answer : answers) {
		    row = researchAndAnalysisSheet.initRow();
		    String answerTitle = removeHtmlMarkup(answer.getDescription());
		    IndexedColors color = null;
		    if (answer.isCorrect()) {
			answerTitle += "(" + getMessage("label.monitoring.item.summary.correct") + ")";
			color = IndexedColors.GREEN;
		    }
		    row.addCell(answerTitle, color);

		    for (int numberAttempts : answer.getAttempts()) {
			row.addCell(Integer.valueOf(numberAttempts));
		    }
		}
		researchAndAnalysisSheet.addEmptyRow();
		researchAndAnalysisSheet.addEmptyRow();
	    }

	    row = researchAndAnalysisSheet.initRow();
	    row.addCell(getMessage("label.breakdown.by.team"), true);

	    for (GroupSummary groupSummary : itemSummary) {
		if (groupSummary.getSessionId().equals(0L)) {
		    continue;
		}

		Collection<ScratchieAnswer> answers = groupSummary.getAnswers();

		row = researchAndAnalysisSheet.initRow();
		row.addCell(groupSummary.getSessionName(), true);

		row = researchAndAnalysisSheet.initRow();
		row.addEmptyCell();
		for (int i = 0; i < answers.size(); i++) {
		    row.addCell(Integer.valueOf(i + 1));
		}

		for (ScratchieAnswer answer : answers) {
		    row = researchAndAnalysisSheet.initRow();
		    String answerTitle = removeHtmlMarkup(answer.getDescription());
		    if (answer.isCorrect()) {
			answerTitle += "(" + getMessage("label.monitoring.item.summary.correct") + ")";
		    }
		    row.addCell(answerTitle);

		    for (int numberAttempts : answer.getAttempts()) {
			row.addCell(Integer.valueOf(numberAttempts));
		    }
		}

	    }
	    researchAndAnalysisSheet.addEmptyRow();
	    researchAndAnalysisSheet.addEmptyRow();
	}

	// Breakdown By Student with Timing----------------------------------------------------

	row = researchAndAnalysisSheet.initRow();
	row.addCell(getMessage("label.breakdown.by.student.with.timing"), true);
	researchAndAnalysisSheet.addEmptyRow();

	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(scratchie.getContentId());
	for (ScratchieSession session : sessionList) {

	    ScratchieUser groupLeader = session.getGroupLeader();
	    Long sessionId = session.getSessionId();

	    if (groupLeader != null) {

		row = researchAndAnalysisSheet.initRow();
		row.addCell(groupLeader.getFirstName() + " " + groupLeader.getLastName(), true);
		row.addCell(getMessage("label.attempts") + ":");
		Long attempts = (long) scratchieAnswerVisitDao.getLogCountTotal(sessionId);
		row.addCell(attempts);
		row.addCell(getMessage("label.mark") + ":");
		row.addCell(Long.valueOf(session.getMark()));

		row = researchAndAnalysisSheet.initRow();
		row.addCell(getMessage("label.team.leader") + session.getSessionName());

		for (ScratchieItem item : items) {
		    row = researchAndAnalysisSheet.initRow();
		    row.addCell(getMessage("label.question.semicolon", new Object[] { item.getTitle() }), false);

		    int i = 1;
		    List<ScratchieAnswerVisitLog> logs = scratchieAnswerVisitDao.getLogsBySessionAndItem(sessionId,
			    item.getUid());
		    for (ScratchieAnswerVisitLog log : logs) {
			row = researchAndAnalysisSheet.initRow();
			row.addCell(Integer.valueOf(i++));
			String answerDescr = removeHtmlMarkup(log.getScratchieAnswer().getDescription());
			row.addCell(answerDescr);
			row.addCell(fullDateFormat.format(log.getAccessDate()));
		    }
		    researchAndAnalysisSheet.addEmptyRow();
		}

	    }
	}

	// ======================================================= For_XLS_export(SPSS analysis) page
	// =======================================
	ExcelSheet spssAnalysisSheet = new ExcelSheet(getMessage("label.spss.analysis"));
	sheets.add(spssAnalysisSheet);

	// Table header------------------------------------

	int maxAnswers = 0;
	for (ScratchieItem item : items) {
	    if (item.getAnswers().size() > maxAnswers) {
		maxAnswers = item.getAnswers().size();
	    }
	}

	row = spssAnalysisSheet.initRow();
	row.addCell(getMessage("label.student.name"), true);
	row.addCell(getMessage("label.student.username"), true);
	row.addCell(getMessage("label.team"), true);
	row.addCell(getMessage("label.question.number"), true);
	row.addCell(getMessage("label.question"), true);
	row.addCell(getMessage("label.correct.answer"), true);
	row.addCell(getMessage("label.first.choice.accuracy"), true);
	row.addCell(getMessage("label.number.of.attempts"), true);
	row.addCell(getMessage("label.mark.awarded"), true);
	for (int i = 0; i < maxAnswers; i++) {
	    row.addCell(getMessage("label." + (i + 1) + ".answer.selected"), true);
	}
	row.addCell(getMessage("label.date"), true);
	for (int i = 0; i < maxAnswers; i++) {
	    row.addCell(getMessage("label.time.of.selection." + (i + 1)), true);
	}

	// Table content------------------------------------

	for (GroupSummary summary : summaryByTeam) {
	    Long sessionId = summary.getSessionId();
	    List<ScratchieUser> users = scratchieUserDao.getBySessionID(sessionId);

	    for (ScratchieUser user : users) {
		int questionCount = 1;
		for (ScratchieItemDTO itemDto : summary.getItemDtos()) {

		    row = spssAnalysisSheet.initRow();
		    // learner name
		    row.addCell(user.getFirstName() + " " + user.getLastName());
		    // username
		    row.addCell(user.getLoginName());
		    // group name
		    row.addCell(summary.getSessionName());
		    // question number
		    row.addCell(Integer.valueOf(questionCount++));
		    // question title
		    row.addCell(itemDto.getTitle());

		    // correct answer
		    String correctAnswer = "";
		    Set<ScratchieAnswer> answers = itemDto.getAnswers();
		    for (ScratchieAnswer answer : answers) {
			if (answer.isCorrect()) {
			    correctAnswer = removeHtmlMarkup(answer.getDescription());
			}
		    }
		    row.addCell(correctAnswer);

		    // isFirstChoice
		    int attempts = itemDto.getUserAttempts();
		    String isFirstChoice;
		    if (itemDto.isUnraveledOnFirstAttempt()) {
			isFirstChoice = getMessage("label.correct");
		    } else if (attempts == 0) {
			isFirstChoice = null;
		    } else {
			isFirstChoice = getMessage("label.incorrect");
		    }
		    row.addCell(isFirstChoice);
		    // attempts
		    row.addCell(Integer.valueOf(attempts));
		    // mark
		    Object mark = (itemDto.getUserMark() == -1) ? "" : Long.valueOf(itemDto.getUserMark());
		    row.addCell(mark);

		    // Answers selected
		    List<ScratchieAnswerVisitLog> logs = scratchieAnswerVisitDao.getLogsBySessionAndItem(sessionId,
			    itemDto.getUid());
		    if (logs == null) {
			logs = new ArrayList<>();
		    }

		    for (ScratchieAnswerVisitLog log : logs) {
			String answer = removeHtmlMarkup(log.getScratchieAnswer().getDescription());
			row.addCell(answer);
		    }
		    for (int i = logs.size(); i < itemDto.getAnswers().size(); i++) {
			row.addCell(getMessage("label.none"));
		    }
		    for (int i = answers.size(); i < maxAnswers; i++) {
			row.addCell("");
		    }

		    // Date
		    String dateStr = "";
		    if (logs.size() > 0) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
			Date accessDate = logs.iterator().next().getAccessDate();
			dateStr = dateFormat.format(accessDate);
		    }
		    row.addCell(dateStr);

		    // time of selection
		    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		    for (ScratchieAnswerVisitLog log : logs) {
			Date accessDate = log.getAccessDate();
			String timeStr = timeFormat.format(accessDate);
			row.addCell(timeStr);
		    }
		    for (int i = logs.size(); i < maxAnswers; i++) {
			row.addCell("");
		    }
		}
	    }
	}

	// ======================================================= Burning questions page
	// =======================================
	if (scratchie.isBurningQuestionsEnabled()) {
	    ExcelSheet burningQuestionsSheet = new ExcelSheet(getMessage("label.burning.questions"));
	    sheets.add(burningQuestionsSheet);

	    row = burningQuestionsSheet.initRow();
	    row.addCell(getMessage("label.burning.questions"), true);
	    burningQuestionsSheet.addEmptyRow();

	    row = burningQuestionsSheet.initRow();
	    row.addCell(getMessage("label.monitoring.summary.user.name"), IndexedColors.BLUE);
	    row.addCell(getMessage("label.burning.questions"), IndexedColors.BLUE);
	    row.addCell(getMessage("label.count"), IndexedColors.BLUE);

	    List<BurningQuestionItemDTO> burningQuestionItemDtos = getBurningQuestionDtos(scratchie, null, true);
	    for (BurningQuestionItemDTO burningQuestionItemDto : burningQuestionItemDtos) {
		ScratchieItem item = burningQuestionItemDto.getScratchieItem();
		row = burningQuestionsSheet.initRow();
		row.addCell(item.getTitle());

		List<BurningQuestionDTO> burningQuestionDtos = burningQuestionItemDto.getBurningQuestionDtos();
		for (BurningQuestionDTO burningQuestionDto : burningQuestionDtos) {
		    String burningQuestion = burningQuestionDto.getBurningQuestion().getQuestion();
		    row = burningQuestionsSheet.initRow();
		    row.addCell(burningQuestionDto.getSessionName());
		    row.addCell(burningQuestion);
		    row.addCell(burningQuestionDto.getLikeCount());
		}
		burningQuestionsSheet.addEmptyRow();
	    }
	}

	return sheets;
    }

    @Override
    public List<Number> getMarksArray(Long toolContentId) {
	return scratchieSessionDao.getRawLeaderMarksByToolContentId(toolContentId);
    }

    @Override
    public LeaderResultsDTO getLeaderResultsDTOForLeaders(Long contentId) {
	LeaderResultsDTO newDto = new LeaderResultsDTO(contentId);
	Object[] markStats = scratchieSessionDao.getStatsMarksForLeaders(contentId);
	if (markStats != null) {
	    newDto.setMinMark(
		    markStats[0] != null ? NumberUtil.formatLocalisedNumber((Float) markStats[0], (Locale) null, 2)
			    : "0.00");
	    newDto.setAvgMark(
		    markStats[1] != null ? NumberUtil.formatLocalisedNumber((Float) markStats[1], (Locale) null, 2)
			    : "0.00");
	    newDto.setMaxMark(
		    markStats[2] != null ? NumberUtil.formatLocalisedNumber((Float) markStats[2], (Locale) null, 2)
			    : "0.00");
	    newDto.setNumberGroupsLeaderFinished((Integer) markStats[3]);
	}
	return newDto;
    }

    @Override
    public Map<String, Object> prepareStudentChoicesData(Scratchie scratchie) {
	Map<String, Object> model = new HashMap<>();

	Set<ScratchieItem> items = new TreeSet<>(new ScratchieItemComparator());
	items.addAll(scratchie.getScratchieItems());
	model.put("items", items);

	//find second page in excel file
	List<ExcelSheet> sheets = exportExcel(scratchie.getContentId());
	ExcelSheet secondPageData = sheets.get(1);

	//correct answers
	ExcelRow correctAnswersRow = secondPageData.getRow(4);
	model.put("correctAnswers", correctAnswersRow);

	//prepare data for displaying user answers table
	int groupsSize = countSessionsByContentId(scratchie.getContentId());
	ArrayList<GroupSummary> sessionDtos = new ArrayList<>();
	for (int groupCount = 0; groupCount < groupsSize; groupCount++) {
	    ExcelRow groupRow = secondPageData.getRows().get(6 + groupCount);

	    GroupSummary groupSummary = new GroupSummary();
	    String sessionName = groupRow.getCell(0).toString();
	    groupSummary.setSessionName(sessionName);

	    Collection<ScratchieItemDTO> itemDtos = new ArrayList<>();
	    for (int i = 1; i <= items.size(); i++) {
		ScratchieItemDTO itemDto = new ScratchieItemDTO();
		String answersSequence = groupRow.getCell(i).toString();
		String[] answerLetters = answersSequence.split(", ");

		Set<ScratchieAnswer> answers = new LinkedHashSet<>();
		for (int j = 0; j < answerLetters.length; j++) {
		    String answerLetter = answerLetters[j];
		    String correctAnswerLetter = correctAnswersRow.getCell(i).toString();

		    ScratchieAnswer answer = new ScratchieAnswer();
		    answer.setDescription(answerLetter);
		    answer.setCorrect(correctAnswerLetter.equals(answerLetter));

		    answers.add(answer);
		}

		itemDto.setAnswers(answers);
		itemDtos.add(itemDto);
	    }
	    groupSummary.setItemDtos(itemDtos);

	    if (!itemDtos.isEmpty()) {
		int total = (Integer) groupRow.getCell(itemDtos.size() + 1);
		groupSummary.setMark(total);

		// round the percentage cell
		String totalPercentage = String
			.valueOf(Math.round(Double.valueOf(groupRow.getCell(itemDtos.size() + 2).toString())));
		groupSummary.setTotalPercentage(totalPercentage);
	    }

	    sessionDtos.add(groupSummary);
	}
	model.put("sessionDtos", sessionDtos);
	return model;
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************

    /**
     * Currently removes only <div> tags.
     */
    private String removeHtmlMarkup(String string) {
	return string.replaceAll("[<](/)?div[^>]*[>]", "");
    }

    /**
     * Serves merely for excel export purposes. Produces data for "Summary By Team" section.
     */
    private List<GroupSummary> getSummaryByTeam(Scratchie scratchie, Collection<ScratchieItem> sortedItems) {
	List<GroupSummary> groupSummaries = new ArrayList<>();

	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(scratchie.getContentId());
	for (ScratchieSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    // one new summary for one session.
	    GroupSummary groupSummary = new GroupSummary(session);
	    ArrayList<ScratchieItemDTO> itemDtos = new ArrayList<>();

	    ScratchieUser groupLeader = session.getGroupLeader();

	    List<ScratchieAnswerVisitLog> answerLogs = scratchieAnswerVisitDao.getLogsBySession(sessionId);

	    populateScratchieItemsWithMarks(scratchie, sortedItems, sessionId);

	    for (ScratchieItem item : sortedItems) {
		ScratchieItemDTO itemDto = new ScratchieItemDTO();
		int numberOfAttempts = 0;
		int mark = -1;
		boolean isUnraveledOnFirstAttempt = false;
		String answersSequence = "";

		// if there is no group leader don't calculate numbers - there aren't any
		if (groupLeader != null) {

		    //create a list of attempts user done for the current item
		    List<ScratchieAnswerVisitLog> itemAttempts = new ArrayList<>();
		    for (ScratchieAnswerVisitLog answerLog : answerLogs) {
			if (answerLog.getScratchieAnswer().getScratchieItem().getUid().equals(item.getUid())) {
			    itemAttempts.add(answerLog);
			}
		    }
		    numberOfAttempts = itemAttempts.size();

		    // for displaying purposes if there is no attemps we assign -1 which will be shown as "-"
		    mark = (numberOfAttempts == 0) ? -1 : item.getMark();

		    isUnraveledOnFirstAttempt = (numberOfAttempts == 1) && isItemUnraveled(item, answerLogs);

		    // find out answers' sequential letters - A,B,C...
		    for (ScratchieAnswerVisitLog itemAttempt : itemAttempts) {
			String sequencialLetter = ScratchieServiceImpl.getSequencialLetter(item,
				itemAttempt.getScratchieAnswer());
			answersSequence += answersSequence.isEmpty() ? sequencialLetter : ", " + sequencialLetter;
		    }

		}

		itemDto.setUid(item.getUid());
		itemDto.setTitle(item.getTitle());
		itemDto.setAnswers(item.getAnswers());
		itemDto.setUserAttempts(numberOfAttempts);
		itemDto.setUserMark(mark);
		itemDto.setUnraveledOnFirstAttempt(isUnraveledOnFirstAttempt);
		itemDto.setAnswersSequence(answersSequence);

		itemDtos.add(itemDto);
	    }

	    groupSummary.setItemDtos(itemDtos);
	    groupSummaries.add(groupSummary);
	}

	return groupSummaries;
    }

    /**
     * Return specified answer's sequential letter (e.g. A,B,C) among other possible answers
     */
    private static String getSequencialLetter(ScratchieItem item, ScratchieAnswer asnwer) {
	String sequencialLetter = "";

	int answerCount = 1;
	for (ScratchieAnswer answer : item.getAnswers()) {
	    if (answer.getUid().equals(asnwer.getUid())) {
		sequencialLetter = String.valueOf((char) ((answerCount + 'A') - 1));
		break;
	    }
	    answerCount++;
	}

	return sequencialLetter;
    }

    private Scratchie getDefaultScratchie() throws ScratchieApplicationException {
	Long defaultScratchieId = getToolDefaultContentIdBySignature(ScratchieConstants.TOOL_SIGNATURE);
	Scratchie defaultScratchie = getScratchieByContentId(defaultScratchieId);
	if (defaultScratchie == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    log.error(error);
	    throw new ScratchieApplicationException(error);
	}

	return defaultScratchie;
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws ScratchieApplicationException {
	Long contentId = toolService.getToolDefaultContentIdBySignature(toolSignature);
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    log.error(error);
	    throw new ScratchieApplicationException(error);
	}
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

    // *****************************************************************************
    // set methods for Spring Bean
    // *****************************************************************************

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public void setScratchieDao(ScratchieDAO scratchieDao) {
	this.scratchieDao = scratchieDao;
    }

    public void setScratchieItemDao(ScratchieItemDAO scratchieAnswerDao) {
	this.scratchieItemDao = scratchieAnswerDao;
    }

    public void setScratchieSessionDao(ScratchieSessionDAO scratchieSessionDao) {
	this.scratchieSessionDao = scratchieSessionDao;
    }

    public void setScratchieToolContentHandler(IToolContentHandler scratchieToolContentHandler) {
	this.scratchieToolContentHandler = scratchieToolContentHandler;
    }

    public void setScratchieUserDao(ScratchieUserDAO scratchieUserDao) {
	this.scratchieUserDao = scratchieUserDao;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public void setScratchieAnswerVisitDao(ScratchieAnswerVisitDAO scratchieItemVisitDao) {
	this.scratchieAnswerVisitDao = scratchieItemVisitDao;
    }

    public void setScratchieBurningQuestionDao(ScratchieBurningQuestionDAO scratchieBurningQuestionDao) {
	this.scratchieBurningQuestionDao = scratchieBurningQuestionDao;
    }

    public void setBurningQuestionLikeDao(BurningQuestionLikeDAO burningQuestionLikeDao) {
	this.burningQuestionLikeDao = burningQuestionLikeDao;
    }

    public void setScratchieConfigItemDao(ScratchieConfigItemDAO scratchieConfigItemDao) {
	this.scratchieConfigItemDao = scratchieConfigItemDao;
    }

    // *******************************************************************************
    // ToolContentManager, ToolSessionManager methods
    // *******************************************************************************

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Scratchie toolContentObj = scratchieDao.getByContentId(toolContentId);
	if (toolContentObj == null) {
	    try {
		toolContentObj = getDefaultScratchie();
	    } catch (ScratchieApplicationException e) {
		throw new DataMissingException(e.getMessage());
	    }
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the scratchie tool");
	}

	// set ScratchieToolContentHandler as null to avoid copy file node in repository again.
	toolContentObj = Scratchie.newInstance(toolContentObj, toolContentId);

	// wipe out the links from ScratchieAnswer back to ScratchieItem, or it will try to
	// include the hibernate object version of the ScratchieItem within the XML
	Set<ScratchieItem> items = toolContentObj.getScratchieItems();
	for (ScratchieItem item : items) {
	    Set<ScratchieAnswer> answers = item.getAnswers();
	    for (ScratchieAnswer answer : answers) {
		answer.setScratchieItem(null);
	    }
	}

	try {
	    exportContentService.exportToolContent(toolContentId, toolContentObj, scratchieToolContentHandler,
		    rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(ScratchieImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, scratchieToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Scratchie)) {
		throw new ImportToolContentException(
			"Import Share scratchie tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Scratchie toolContentObj = (Scratchie) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentId(toolContentId);
	    ScratchieUser user = scratchieUserDao.getUserByUserIDAndContentID(new Long(newUserUid.longValue()),
		    toolContentId);
	    if (user == null) {
		user = new ScratchieUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(newUserUid.longValue());
	    }

	    scratchieDao.saveObject(toolContentObj);
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
     * @throws ScratchieApplicationException
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Scratchie content = getScratchieByContentId(toolContentId);
	if (content == null) {
	    try {
		content = getDefaultContent(toolContentId);
	    } catch (ScratchieApplicationException e) {
		throw new ToolException(e);
	    }
	}
	return getScratchieOutputFactory().getToolOutputDefinitions(this, content, definitionType);
    }

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	if (toContentId == null) {
	    throw new ToolException("Failed to create the SharedScratchieFiles tool seession");
	}

	Scratchie scratchie = null;
	if (fromContentId != null) {
	    scratchie = scratchieDao.getByContentId(fromContentId);
	}
	if (scratchie == null) {
	    try {
		scratchie = getDefaultScratchie();
	    } catch (ScratchieApplicationException e) {
		throw new ToolException(e);
	    }
	}

	Scratchie toContent = Scratchie.newInstance(scratchie, toContentId);
	scratchieDao.saveObject(toContent);

	// save scratchie items as well
	Set<ScratchieItem> items = toContent.getScratchieItems();
	if (items != null) {
	    Iterator<ScratchieItem> iter = items.iterator();
	    while (iter.hasNext()) {
		ScratchieItem item = iter.next();
		// createRootTopic(toContent.getUid(),null,msg);
	    }
	}
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getScratchieByContentId(toolContentId).getTitle();
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Scratchie scratchie = scratchieDao.getByContentId(toolContentId);
	if (scratchie == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	scratchie.setDefineLater(false);
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getScratchieByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	List<ScratchieSession> sessions = scratchieSessionDao.getByContentId(toolContentId);
	for (ScratchieSession session : sessions) {
	    if (!scratchieUserDao.getBySessionID(session.getSessionId()).isEmpty()) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Scratchie scratchie = scratchieDao.getByContentId(toolContentId);
	if (scratchie == null) {
	    log.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (ScratchieSession session : scratchieSessionDao.getByContentId(toolContentId)) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, ScratchieConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	scratchieDao.delete(scratchie);
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (log.isDebugEnabled()) {
	    log.debug("Removing Scratchie content for user ID " + userId + " and toolContentId " + toolContentId);
	}

	List<ScratchieSession> sessions = scratchieSessionDao.getByContentId(toolContentId);
	for (ScratchieSession session : sessions) {
	    ScratchieUser user = scratchieUserDao.getUserByUserIDAndSessionID(userId.longValue(),
		    session.getSessionId());

	    if (user != null) {
		NotebookEntry entry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			ScratchieConstants.TOOL_SIGNATURE, userId);
		if (entry != null) {
		    scratchieDao.removeObject(NotebookEntry.class, entry.getUid());
		}

		if ((session.getGroupLeader() != null) && session.getGroupLeader().getUid().equals(user.getUid())) {
		    session.setGroupLeader(null);
		}

		scratchieUserDao.removeObject(ScratchieUser.class, user.getUid());
		toolService.removeActivityMark(userId, session.getSessionId());
	    }
	}
    }

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	ScratchieSession session = new ScratchieSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	Scratchie scratchie = scratchieDao.getByContentId(toolContentId);
	session.setScratchie(scratchie);
	scratchieSessionDao.saveObject(session);
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}
	ScratchieSession session = scratchieSessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(ScratchieConstants.COMPLETED);
	    scratchieSessionDao.saveObject(session);
	} else {
	    log.error("Fail to leave tool Session.Could not find shared scratchie " + "session by given session id: "
		    + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find shared scratchie session by given session id: " + toolSessionId);
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
	scratchieSessionDao.deleteBySessionId(toolSessionId);
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getScratchieOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getScratchieOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
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
    public void forceCompleteUser(Long toolSessionId, User user) {
	Long userId = user.getUserId().longValue();

	ScratchieSession session = getScratchieSessionBySessionId(toolSessionId);
	if ((session == null) || (session.getScratchie() == null)) {
	    return;
	}

	ScratchieUser scratchieUser = scratchieUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	// create user if he hasn't accessed this activity yet
	if (scratchieUser == null) {
	    scratchieUser = new ScratchieUser(user.getUserDTO(), session);
	    createUser(scratchieUser);
	}

	checkLeaderSelectToolForSessionLeader(scratchieUser, toolSessionId);
	//if this is a leader finishes, complete all non-leaders as well
	boolean isUserGroupLeader = session.isUserGroupLeader(scratchieUser.getUid());
	if (isUserGroupLeader) {
	    getUsersBySession(toolSessionId).forEach(sessionUser -> {
		//finish users
		sessionUser.setSessionFinished(true);
		scratchieUserDao.saveObject(user);

		// as long as there is no individual results in Scratchie tool (but rather one for entire group) there is no
		// need to copyAnswersFromLeader()
	    });

	} else {
	    //finish user
	    scratchieUser.setSessionFinished(true);
	    scratchieUserDao.saveObject(scratchieUser);
	}
    }

    /* =================================================================================== */

    public IExportToolContentService getExportContentService() {
	return exportContentService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
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

    @Override
    public IEventNotificationService getEventNotificationService() {
	return eventNotificationService;
    }

    public void setEventNotificationService(IEventNotificationService eventNotificationService) {
	this.eventNotificationService = eventNotificationService;
    }

    @Override
    public String getMessage(String key) {
	return messageService.getMessage(key);
    }

    /**
     * Returns localized message
     */
    public String getMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getScratchieOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    public ScratchieOutputFactory getScratchieOutputFactory() {
	return scratchieOutputFactory;
    }

    public void setScratchieOutputFactory(ScratchieOutputFactory scratchieOutputFactory) {
	this.scratchieOutputFactory = scratchieOutputFactory;
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	// db doesn't have a start/finish date for learner, and session start/finish is null
	ScratchieUser learner = getUserByIDAndSession(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	return new ToolCompletionStatus(learner.isSessionFinished() ? ToolCompletionStatus.ACTIVITY_COMPLETED
		: ToolCompletionStatus.ACTIVITY_ATTEMPTED, null, null);
    }
    // ****************** REST methods *************************

    /**
     * Rest call to create a new Scratchie content. Required fields in toolContentJSON: "title", "instructions",
     * "questions". The questions entry should be ArrayNode containing JSON objects, which in turn must contain
     * "questionText", "displayOrder" (Integer) and a ArrayNode "answers". The answers entry should be ArrayNode
     * containing JSON objects, which in turn must contain "answerText", "displayOrder" (Integer), "correct" (Boolean).
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, ObjectNode toolContentJSON) {

	Scratchie scratchie = new Scratchie();
	Date updateDate = new Date();

	scratchie.setCreated(updateDate);
	scratchie.setUpdated(updateDate);
	scratchie.setDefineLater(false);

	scratchie.setContentId(toolContentID);
	scratchie.setTitle(JsonUtil.optString(toolContentJSON, RestTags.TITLE));
	scratchie.setInstructions(JsonUtil.optString(toolContentJSON, RestTags.INSTRUCTIONS));

	scratchie.setBurningQuestionsEnabled(JsonUtil.optBoolean(toolContentJSON, "burningQuestionsEnabled", true));
	scratchie.setTimeLimit(JsonUtil.optInt(toolContentJSON, "timeLimit", 0));
	scratchie.setExtraPoint(JsonUtil.optBoolean(toolContentJSON, "extraPoint", false));
	scratchie.setReflectOnActivity(
		JsonUtil.optBoolean(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	scratchie.setReflectInstructions(JsonUtil.optString(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS));
	scratchie.setShowScrachiesInResults(JsonUtil.optBoolean(toolContentJSON, "showScrachiesInResults", true));
	scratchie.setConfidenceLevelsActivityUiid(
		JsonUtil.optInt(toolContentJSON, RestTags.CONFIDENCE_LEVELS_ACTIVITY_UIID));

	// Scratchie Items
	Set<ScratchieItem> newItems = new LinkedHashSet<>();

	ArrayNode questions = JsonUtil.optArray(toolContentJSON, RestTags.QUESTIONS);
	for (int i = 0; i < questions.size(); i++) {
	    ObjectNode questionData = (ObjectNode) questions.get(i);

	    ScratchieItem item = new ScratchieItem();
	    item.setCreateDate(updateDate);
	    item.setCreateByAuthor(true);
	    item.setOrderId(JsonUtil.optInt(questionData, RestTags.DISPLAY_ORDER));
	    item.setTitle(JsonUtil.optString(questionData, RestTags.QUESTION_TITLE));
	    item.setDescription(JsonUtil.optString(questionData, RestTags.QUESTION_TEXT));
	    newItems.add(item);

	    // set options
	    Set<ScratchieAnswer> newAnswers = new LinkedHashSet<>();

	    ArrayNode answersData = JsonUtil.optArray(questionData, RestTags.ANSWERS);
	    for (int j = 0; j < answersData.size(); j++) {
		ObjectNode answerData = (ObjectNode) answersData.get(j);
		ScratchieAnswer answer = new ScratchieAnswer();
		// Removes redundant new line characters from options left by CKEditor (otherwise it will break
		// Javascript in monitor). Copied from AuthoringAction.
		String answerDescription = JsonUtil.optString(answerData, RestTags.ANSWER_TEXT);
		answer.setDescription(answerDescription != null ? answerDescription.replaceAll("[\n\r\f]", "") : "");
		answer.setCorrect(JsonUtil.optBoolean(answerData, RestTags.CORRECT));
		answer.setOrderId(JsonUtil.optInt(answerData, RestTags.DISPLAY_ORDER));
		answer.setScratchieItem(item);
		newAnswers.add(answer);
	    }

	    item.setAnswers(newAnswers);

	}

	scratchie.setScratchieItems(newItems);
	saveOrUpdateScratchie(scratchie);
    }
}