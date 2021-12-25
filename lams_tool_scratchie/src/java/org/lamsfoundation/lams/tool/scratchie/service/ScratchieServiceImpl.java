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
import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.confidencelevel.VsaAnswerDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.outcome.Outcome;
import org.lamsfoundation.lams.outcome.OutcomeMapping;
import org.lamsfoundation.lams.outcome.service.IOutcomeService;
import org.lamsfoundation.lams.qb.QbUtils;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.model.QbToolQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
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
import org.lamsfoundation.lams.tool.scratchie.dto.OptionDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.ScratchieItemDTO;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswerVisitLog;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieBurningQuestion;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieConfigItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieItemComparator;
import org.lamsfoundation.lams.tool.scratchie.web.controller.LearningWebsocketServer;
import org.lamsfoundation.lams.tool.service.ICommonScratchieService;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.service.IQbToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.excel.ExcelRow;
import org.lamsfoundation.lams.util.excel.ExcelSheet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Andrey Balan
 */
public class ScratchieServiceImpl implements IScratchieService, ICommonScratchieService, ToolContentManager,
	ToolSessionManager, ToolRestManager, IQbToolService {
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

    private IQbService qbService;

    private ILearnerService learnerService;

    private IOutcomeService outcomeService;

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
	for (ScratchieItem item : scratchie.getScratchieItems()) {
	    scratchieItemDao.saveObject(item);
	}
	scratchieDao.saveObject(scratchie);
    }

    @Override
    public void releaseFromCache(Object object) {
	scratchieDao.releaseFromCache(object);
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
	    //find corresponding QbQuestion
	    for (ConfidenceLevelDTO confidenceLevelDto : confidenceLevelDtos) {
		if (item.getQbQuestion().getUid().equals(confidenceLevelDto.getQbQuestionUid())) {

		    //find corresponding QbOption
		    for (OptionDTO optionDTO : item.getOptionDtos()) {
			if (optionDTO.getQbOptionUid().equals(confidenceLevelDto.getQbOptionUid())) {
			    optionDTO.getConfidenceLevelDtos().add(confidenceLevelDto);
			}
		    }
		}
	    }
	}
    }

    @Override
    public Set<ToolActivity> getActivitiesProvidingConfidenceLevels(Long toolContentId) {
	return toolService.getActivitiesProvidingConfidenceLevels(toolContentId);
    }

    @Override
    public Set<ToolActivity> getActivitiesProvidingVsaAnswers(Long toolContentId) {
	return toolService.getActivitiesProvidingVsaAnswers(toolContentId);
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
	    // set leader only if the leader entered the activity
	    if (user.getUserId().equals(leaderUserId)) {
		// is it me?
		leader = user;
	    } else {
		leader = getUserByIDAndSession(leaderUserId, toolSessionId);
	    }
	    if (leader != null) {
		// set group leader
		scratchieSession.setGroupLeader(leader);
		saveOrUpdateScratchieSession(scratchieSession);
	    }
	}
	return leader;
    }

    @Override
    public boolean checkTimeLimitExceeded(long toolContentId, int userId) {
	Long secondsLeft = LearningWebsocketServer.getSecondsLeft(toolContentId, userId);
	return secondsLeft != null && secondsLeft.equals(0L);
    }

    @Override
    public LocalDateTime launchTimeLimit(long toolContentId, int userId) {
	ScratchieUser user = getUserByUserIDAndContentID(Integer.valueOf(userId).longValue(), toolContentId);
	if (user == null) {
	    return null;
	}

	ScratchieSession session = user.getSession();
	Date launchDate = session.getTimeLimitLaunchedDate();
	if (launchDate == null) {
	    if (!session.isUserGroupLeader(user.getUid())) {
		// only leader launches time limit
		return null;
	    }

	    //store timeLimitLaunchedDate into DB
	    launchDate = new Date();
	    session.setTimeLimitLaunchedDate(launchDate);
	    scratchieSessionDao.saveObject(session);
	}

	return launchDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
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
    public List<ScratchieSession> getSessionsByContentId(Long toolContentId) {
	return scratchieSessionDao.getByContentId(toolContentId);
    }

    @Override
    public void saveOrUpdateScratchieSession(ScratchieSession resSession) {
	scratchieSessionDao.saveObject(resSession);
    }

    @Override
    public ScratchieAnswerVisitLog getLog(Long sessionId, Long itemUid, boolean isCaseSensitive, String answer) {
	return scratchieAnswerVisitDao.getLog(sessionId, itemUid, isCaseSensitive, answer);
    }

    @Override
    public void recordItemScratched(Long sessionId, Long itemUid, Long optionUid) {
	QbOption option = this.getQbOptionByUid(optionUid);
	if (option == null) {
	    return;
	}

	ScratchieAnswerVisitLog log = scratchieAnswerVisitDao.getLog(optionUid, itemUid, sessionId);
	if (log == null) {
	    log = new ScratchieAnswerVisitLog();
	    log.setQbOption(option);
	    log.setSessionId(sessionId);
	    QbToolQuestion qbToolQuestion = scratchieDao.find(QbToolQuestion.class, itemUid);
	    log.setQbToolQuestion(qbToolQuestion);
	    log.setAccessDate(new Timestamp(new Date().getTime()));
	    scratchieAnswerVisitDao.saveObject(log);

	    recalculateMarkForSession(sessionId, false);
	}
    }

    @Override
    public void recordVsaAnswer(Long sessionId, Long itemUid, boolean isCaseSensitive, String answer) {
	ScratchieAnswerVisitLog log = scratchieAnswerVisitDao.getLog(sessionId, itemUid, isCaseSensitive, answer);
	if (log == null) {
	    log = new ScratchieAnswerVisitLog();
	    log.setAnswer(answer);
	    log.setSessionId(sessionId);
	    QbToolQuestion qbToolQuestion = scratchieDao.find(QbToolQuestion.class, itemUid);
	    log.setQbToolQuestion(qbToolQuestion);
	    log.setAccessDate(new Timestamp(new Date().getTime()));
	    scratchieAnswerVisitDao.saveObject(log);

	    recalculateMarkForSession(sessionId, false);
	}
    }

    @Override
    public void recalculateMarkForSession(Long sessionId, boolean isPropagateToGradebook) {
	ScratchieSession session = getScratchieSessionBySessionId(sessionId);
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
	    List<ScratchieUser> users = getUsersBySession(sessionId);
	    for (ScratchieUser user : users) {
		Double userMark = 0.0;
		if (isLearnerEligibleForMark(user.getUserId(), scratchie.getContentId())) {
		    userMark = Double.valueOf(mark);
		}
		toolService.updateActivityMark(userMark, null, user.getUserId().intValue(),
			user.getSession().getSessionId(), false);
	    }
	}
    }

    @Override
    public void recalculateUserAnswers(Scratchie scratchie, Set<ScratchieItem> oldItems, Set<ScratchieItem> newItems,
	    String oldPresetMarks) {
	// create list of modified questions
	List<ScratchieItem> modifiedItems = new ArrayList<>();
	for (ScratchieItem oldItem : oldItems) {
	    for (ScratchieItem newItem : newItems) {
		if (oldItem.getDisplayOrder() == newItem.getDisplayOrder()) {

		    // title or question is different - do nothing

		    // options are different
		    List<QbOption> oldOptions = oldItem.getQbQuestion().getQbOptions();
		    List<QbOption> newOptions = newItem.getQbQuestion().getQbOptions();
		    boolean isItemModified = oldOptions.size() != newOptions.size();

		    for (QbOption oldOption : oldOptions) {
			if (isItemModified) {
			    break;
			}

			for (QbOption newOption : newOptions) {
			    if (oldOption.getDisplayOrder() == newOption.getDisplayOrder()) {

				if (oldOption.isCorrect() != newOption.isCorrect()) {
				    isItemModified = true;
				    break;
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
	    String newPresetMarks = scratchie.getPresetMarks();
	    boolean isRecalculateMarks = oldPresetMarks == null ? newPresetMarks != null
		    : newPresetMarks == null || !oldPresetMarks.equals(newPresetMarks);

	    // remove all scratches for modified items

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
    public boolean recalculateMarksForVsaQuestion(Long qbQuestionUid, String answer) {
	List<Long> sessionIds = scratchieSessionDao.getSessionIdsByQbQuestion(qbQuestionUid, answer);
	// recalculate marks if it's required
	for (Long sessionId : sessionIds) {
	    recalculateMarkForSession(sessionId, true);
	}
	return !sessionIds.isEmpty();
    }

    @Override
    public Map<QbToolQuestion, Map<String, Integer>> getUnallocatedVSAnswers(long toolContentId) {
	Map<QbToolQuestion, Map<String, Integer>> result = new LinkedHashMap<>();

	List<ScratchieSession> sessions = scratchieSessionDao.getByContentId(toolContentId);
	if (sessions.isEmpty()) {
	    return result;
	}

	Scratchie scratchie = sessions.get(0).getScratchie();
	Map<Long, Integer> sessionToLeaderMap = sessions.stream().filter(s -> s.getGroupLeader() != null).collect(
		Collectors.toMap(ScratchieSession::getSessionId, s -> s.getGroupLeader().getUserId().intValue()));

	for (ScratchieItem item : scratchie.getScratchieItems()) {
	    QbQuestion qbQuestion = item.getQbQuestion();
	    if (qbQuestion.getType().equals(QbQuestion.TYPE_VERY_SHORT_ANSWERS)) {
		Set<String> notAllocatedAnswers = new HashSet<>();
		Map<String, Integer> notAllocatedAnswerMap = new LinkedHashMap<>();

		List<ScratchieAnswerVisitLog> visitLogs = scratchieAnswerVisitDao.getVsaLogsByItem(item.getUid());
		for (ScratchieAnswerVisitLog visitLog : visitLogs) {
		    String answer = visitLog.getAnswer();
		    boolean isAnswerAllocated = QbUtils.isVSAnswerAllocated(qbQuestion, answer, notAllocatedAnswers);
		    if (!isAnswerAllocated) {
			notAllocatedAnswerMap.put(answer.strip(), sessionToLeaderMap.get(visitLog.getSessionId()));
		    }
		}

		result.put(item, notAllocatedAnswerMap);
	    }
	}
	return result;
    }

    /**
     * Counts how many questions were answered correctly on first attempt by the given user, regardless of mark given.
     */
    @Override
    public Integer countCorrectAnswers(long toolContentId, int userId) {
	Scratchie scratchie = getScratchieByContentId(toolContentId);
	ScratchieUser user = getUserByUserIDAndContentID((long) userId, toolContentId);
	if (user == null) {
	    return null;
	}
	List<ScratchieAnswerVisitLog> logs = scratchieAnswerVisitDao.getLogsBySession(user.getSession().getSessionId());
	if (logs.isEmpty()) {
	    return 0;
	}

	int count = 0;

	for (ScratchieItem item : scratchie.getScratchieItems()) {

	    //create a list of attempts user done for the current item
	    List<ScratchieAnswerVisitLog> visitLogs = new ArrayList<>();
	    for (ScratchieAnswerVisitLog log : logs) {
		if (log.getQbToolQuestion().getUid().equals(item.getUid())) {
		    visitLogs.add(log);
		}
	    }

	    int numberOfAttempts = ScratchieServiceImpl.getNumberAttemptsForItem(item, visitLogs);
	    boolean isUnraveledOnFirstAttempt = (numberOfAttempts == 1)
		    && ScratchieServiceImpl.isItemUnraveled(item, logs);
	    if (isUnraveledOnFirstAttempt) {
		count++;
	    }
	}

	return count;
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
    public QbOption getQbOptionByUid(Long optionUid) {
	QbOption res = (QbOption) userManagementService.findById(QbOption.class, optionUid);
	releaseFromCache(res);
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
    public Collection<User> getAllGroupUsers(Long toolSessionId) {
	return toolService.getToolSession(toolSessionId).getLearners();
    }

    @Override
    /*
     * If isIncludeOnlyLeaders then include the portrait ids needed for monitoring. If false then it
     * is probably the export and that doesn't need portraits.
     */
    public List<GroupSummary> getMonitoringSummary(Long contentId) {
	List<GroupSummary> groupSummaryList = new ArrayList<>();
	List<ScratchieSession> sessions = scratchieSessionDao.getByContentId(contentId);

	for (ScratchieSession session : sessions) {

	    Long sessionId = session.getSessionId();

	    Collection<User> groupUsers = getAllGroupUsers(sessionId);

	    // one new summary for one session.
	    GroupSummary groupSummary = new GroupSummary(session);

	    groupSummary.setScratchingFinished(session.isScratchingFinished());

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

		scratchieUser.setPortraitId(user.getPortraitUuid() == null ? null : user.getPortraitUuid().toString());
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
	    QbQuestion qbQuestion = item.getQbQuestion();
	    List<ScratchieAnswerVisitLog> itemLogs = scratchieAnswerVisitDao.getLogsBySessionAndItem(sessionId,
		    item.getUid());

	    for (OptionDTO optionDto : item.getOptionDtos()) {
		if (QbQuestion.TYPE_MULTIPLE_CHOICE == qbQuestion.getType()
			|| QbQuestion.TYPE_MARK_HEDGING == qbQuestion.getType()) {
		    int attemptNumber;
		    ScratchieAnswerVisitLog log = scratchieAnswerVisitDao.getLog(optionDto.getQbOptionUid(),
			    item.getUid(), sessionId);
		    if (log == null) {
			// -1 if there is no log
			attemptNumber = -1;
		    } else {
			// adding 1 to start from 1
			attemptNumber = itemLogs.indexOf(log) + 1;
		    }
		    optionDto.setAttemptOrder(attemptNumber);

		    //process VSA questions
		} else {
		    // -1 if there is no log
		    int attemptNumber = -1;
		    for (ScratchieAnswerVisitLog itemLog : itemLogs) {
			if (itemLog.getQbToolQuestion().getUid().equals(item.getUid())
				&& isAnswersEqual(item, itemLog.getAnswer(), optionDto.getAnswer())) {
			    // adding 1 to start from 1
			    attemptNumber = itemLogs.indexOf(itemLog) + 1;
			    break;
			}
		    }
		    optionDto.setAttemptOrder(attemptNumber);
		}
	    }
	}
    }

    private boolean isAnswersEqual(ScratchieItem item, String answer1, String answer2) {
	if (answer1 == null || answer2 == null) {
	    return false;
	}

	return item.getQbQuestion().isCaseSensitive() ? answer1.equals(answer2) : answer1.equalsIgnoreCase(answer2);
    }

    @Override
    public Collection<ScratchieItem> getItemsWithIndicatedScratches(Long toolSessionId) {
	List<ScratchieAnswerVisitLog> userLogs = scratchieAnswerVisitDao.getLogsBySession(toolSessionId);

	Scratchie scratchie = getScratchieBySessionId(toolSessionId);
	Set<ScratchieItem> items = new TreeSet<>(new ScratchieItemComparator());
	items.addAll(scratchie.getScratchieItems());

	//populate Scratchie items with VSA answers
	fillItemsWithVsaAnswers(items, toolSessionId, scratchie, userLogs);

	//mark scratched options
	for (ScratchieItem item : items) {
	    for (OptionDTO optionDto : item.getOptionDtos()) {

		// find according log if it exists
		boolean isScratched = false;
		if (QbQuestion.TYPE_MULTIPLE_CHOICE == item.getQbQuestion().getType()
			|| QbQuestion.TYPE_MARK_HEDGING == item.getQbQuestion().getType()) {
		    for (ScratchieAnswerVisitLog userLog : userLogs) {
			if (userLog.getQbToolQuestion().getUid().equals(item.getUid())
				&& userLog.getQbOption().getUid().equals(optionDto.getQbOptionUid())) {
			    isScratched = true;
			    break;
			}
		    }

		    //process VSA question
		} else {
		    // find according log if it exists
		    for (ScratchieAnswerVisitLog userLog : userLogs) {
			if (userLog.getQbToolQuestion().getUid().equals(item.getUid())
				&& isAnswersEqual(item, userLog.getAnswer(), optionDto.getAnswer())) {
			    isScratched = true;
			    break;
			}
		    }
		}
		optionDto.setScratched(isScratched);
	    }

	    boolean isItemUnraveled = ScratchieServiceImpl.isItemUnraveled(item, userLogs);
	    item.setUnraveled(isItemUnraveled);
	}

	return items;
    }

    /**
     * Populate Scratchie item with VSA answers (both from Assessment tool and entered by current learner)
     */
    private void fillItemsWithVsaAnswers(Collection<ScratchieItem> items, Long toolSessionId, Scratchie scratchie,
	    List<ScratchieAnswerVisitLog> userLogs) {
	ScratchieUser leader = scratchieSessionDao.getSessionBySessionId(toolSessionId).getGroupLeader();
	Collection<VsaAnswerDTO> assessmentAnswers = scratchie.isAnswersFetchingEnabled()
		? toolService.getVsaAnswersFromAssessment(scratchie.getActivityUiidProvidingVsaAnswers(),
			leader.getUserId().intValue(), toolSessionId)
		: null;

	for (ScratchieItem item : items) {
	    Long itemQbQuestionUid = item.getQbQuestion().getUid();

	    //process only VSA items
	    if (item.getQbQuestion().getType() != QbQuestion.TYPE_VERY_SHORT_ANSWERS) {
		continue;
	    }
	    //clear optionDtos in case fillItemsWithVsaAnswers is used in a loop
//	    item.getOptionDtos().clear();
	    List<OptionDTO> optionDtosFromScratchieUsers = new LinkedList<>();
	    for (OptionDTO optionDto : item.getOptionDtos()) {
		if (optionDto.getDisplayOrder() == -200) {
		    optionDtosFromScratchieUsers.add(optionDto);
		}
	    }
	    item.getOptionDtos().clear();
	    item.getOptionDtos().addAll(optionDtosFromScratchieUsers);

	    //populate Scratchie items with VSA answers, entered by learners in Assessment tool
	    if (scratchie.isAnswersFetchingEnabled()) {
		//find corresponding QbQuestion
		for (VsaAnswerDTO assessmentAnswer : assessmentAnswers) {
		    if (itemQbQuestionUid.equals(assessmentAnswer.getQbQuestionUid())) {
			OptionDTO optionDto = new OptionDTO();
			optionDto.setAnswer(assessmentAnswer.getAnswer());
			optionDto.setCorrect(assessmentAnswer.isCorrect());
			optionDto.setUserId(assessmentAnswer.getUserId());
			optionDto.setQbQuestionUid(assessmentAnswer.getQbQuestionUid());
			optionDto.setDisplayOrder(-100);
			if (!scratchie.isConfidenceLevelsEnabled()) {
			    //don't show confidence levels
			    for (ConfidenceLevelDTO confidenceLevel : assessmentAnswer.getConfidenceLevels()) {
				confidenceLevel.setLevel(-1);
			    }
			}
			optionDto.getConfidenceLevelDtos().addAll(assessmentAnswer.getConfidenceLevels());

			item.getOptionDtos().add(optionDto);
		    }
		}
	    }

	    //add answers provided by user, which didn't come from Assessment
	    for (ScratchieAnswerVisitLog userLog : userLogs) {
		if (itemQbQuestionUid.equals(userLog.getQbToolQuestion().getQbQuestion().getUid())) {

		    //try to find already existing VsaAnswerDTO for the answer
		    OptionDTO optionDto = null;
		    boolean skipAddingUserAnswerToConfidenceLevel = false;
		    for (OptionDTO optionDtoIter : item.getOptionDtos()) {
			if (itemQbQuestionUid.equals(optionDtoIter.getQbQuestionUid())
				&& isAnswersEqual(item, optionDtoIter.getAnswer(), userLog.getAnswer())) {
			    optionDto = optionDtoIter;
			    //skip showing ConfidenceLevel, as we already show it due to this user's answer in Assessment
			    skipAddingUserAnswerToConfidenceLevel = optionDtoIter.getUserId()
				    .equals(leader.getUserId());
			    break;
			}
		    }
		    if (skipAddingUserAnswerToConfidenceLevel) {
			continue;
		    }

		    if (optionDto == null) {
			optionDto = new OptionDTO();
			optionDto.setQbQuestionUid(itemQbQuestionUid);
			String answer = userLog.getAnswer();
			optionDto.setAnswer(answer);
			optionDto.setDisplayOrder(-200);
			boolean isCorrect = ScratchieServiceImpl.isItemUnraveledByAnswers(item, List.of(answer));
			optionDto.setCorrect(isCorrect);
			optionDto.setUserId(leader.getUserId());
			item.getOptionDtos().add(optionDto);
		    }

		    ConfidenceLevelDTO confidenceLevelDto = new ConfidenceLevelDTO();
		    confidenceLevelDto.setUserId(leader.getUserId().intValue());
		    String userName = StringUtils.isBlank(leader.getFirstName())
			    && StringUtils.isBlank(leader.getLastName()) ? leader.getLoginName()
				    : leader.getFirstName() + " " + leader.getLastName();
		    confidenceLevelDto.setUserName(userName);
		    confidenceLevelDto.setPortraitUuid(leader.getPortraitId());
		    //don't show confidence level
		    confidenceLevelDto.setLevel(-1);
		    optionDto.getConfidenceLevelDtos().add(confidenceLevelDto);
		}
	    }
	}
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
    private static boolean isItemUnraveled(ScratchieItem item, List<ScratchieAnswerVisitLog> userLogs) {
	boolean isItemUnraveled = false;

	if (QbQuestion.TYPE_MULTIPLE_CHOICE == item.getQbQuestion().getType()
		|| QbQuestion.TYPE_MARK_HEDGING == item.getQbQuestion().getType()) {
	    for (QbOption option : item.getQbQuestion().getQbOptions()) {

		ScratchieAnswerVisitLog log = null;
		for (ScratchieAnswerVisitLog userLog : userLogs) {
		    if (userLog.getQbToolQuestion().getUid().equals(item.getUid())
			    && userLog.getQbOption().getUid().equals(option.getUid())) {
			log = userLog;
			break;
		    }
		}

		if (log != null) {
		    isItemUnraveled |= option.isCorrect();
		}
	    }

	    //VSA question
	} else {
	    List<String> userAnswers = new ArrayList<>();
	    for (ScratchieAnswerVisitLog userLog : userLogs) {
		if (userLog.getQbToolQuestion().getUid().equals(item.getUid())
			&& StringUtils.isNotBlank(userLog.getAnswer())) {
		    userAnswers.add(userLog.getAnswer());
		}
	    }

	    isItemUnraveled = ScratchieServiceImpl.isItemUnraveledByAnswers(item, userAnswers);
	}

	return isItemUnraveled;
    }

    public static boolean isItemUnraveledByAnswers(ScratchieItem item, List<String> userAnswers) {
	QbQuestion qbQuestion = item.getQbQuestion();

	QbOption correctAnswersGroup = qbQuestion.getQbOptions().get(0).isCorrect() ? qbQuestion.getQbOptions().get(0)
		: qbQuestion.getQbOptions().get(1);
	String name = correctAnswersGroup.getName();

	for (String userAnswer : userAnswers) {
	    if (QbUtils.isVSAnswerAllocated(name, userAnswer, qbQuestion.isCaseSensitive())) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public void populateScratchieItemsWithMarks(Scratchie scratchie, Collection<ScratchieItem> items, long sessionId) {
	List<ScratchieAnswerVisitLog> userLogs = scratchieAnswerVisitDao.getLogsBySession(sessionId);
	String[] presetMarks = getPresetMarks(scratchie);

	for (ScratchieItem item : items) {
	    // get lowest mark by default
	    int mark = Integer.parseInt(presetMarks[presetMarks.length - 1]);
	    // add mark only if an item was unravelled
	    if (ScratchieServiceImpl.isItemUnraveled(item, userLogs)) {
		int itemAttempts = ScratchieServiceImpl.getNumberAttemptsForItem(item, userLogs);
		String markStr = (itemAttempts <= presetMarks.length) ? presetMarks[itemAttempts - 1]
			: presetMarks[presetMarks.length - 1];
		mark = Integer.parseInt(markStr);
	    }
	    item.setMark(mark);
	}
    }

    /**
     * Returns number of scraches user done for the specified item.
     */
    private static int getNumberAttemptsForItem(ScratchieItem item, List<ScratchieAnswerVisitLog> userLogs) {
	int itemAttempts = 0;
	String correctVsaOption = null;
	QbQuestion qbQuestion = item.getQbQuestion();
	// if VS answer was marked as correct in Scratchie after leader chose another answer,
	// then the subsequent answers do not count
	if (qbQuestion.getType().equals(QbQuestion.TYPE_VERY_SHORT_ANSWERS)) {
	    QbOption correctAnswersGroup = qbQuestion.getQbOptions().get(0).isCorrect()
		    ? qbQuestion.getQbOptions().get(0)
		    : qbQuestion.getQbOptions().get(1);
	    correctVsaOption = correctAnswersGroup.getName();
	}
	for (ScratchieAnswerVisitLog userLog : userLogs) {
	    if (userLog.getQbToolQuestion().getUid().equals(item.getUid())) {
		itemAttempts++;
		if (correctVsaOption != null && QbUtils.isVSAnswerAllocated(correctVsaOption, userLog.getAnswer(),
			qbQuestion.isCaseSensitive())) {
		    break;
		}
	    }
	}

	return itemAttempts;
    }

    @Override
    public List<GroupSummary> getGroupSummariesByItem(Long contentId, Long itemUid) {
	List<GroupSummary> groupSummaryList = new ArrayList<>();
	ScratchieItem item = scratchieItemDao.getByUid(itemUid);
	boolean isMcqItem = item.getQbQuestion().getType() == QbQuestion.TYPE_MULTIPLE_CHOICE
		|| item.getQbQuestion().getType() == QbQuestion.TYPE_MARK_HEDGING;
	List<QbOption> options = item.getQbQuestion().getQbOptions();

	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(contentId);
	for (ScratchieSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    // one new summary for one session.
	    GroupSummary groupSummary = new GroupSummary(session);
	    List<ScratchieAnswerVisitLog> sessionAttempts = scratchieAnswerVisitDao.getLogsBySessionAndItem(sessionId,
		    itemUid);
	    int numberColumns = options.size() > sessionAttempts.size() || isMcqItem ? options.size()
		    : sessionAttempts.size();
	    groupSummary.setNumberColumns(numberColumns);

	    Map<Long, OptionDTO> optionMap = new HashMap<>();
	    long i = 0l;
	    for (QbOption dbOption : options) {
		// clone it so it doesn't interfere with values from other sessions
		OptionDTO optionDto = new OptionDTO(dbOption);
		int[] attempts = new int[numberColumns];
		optionDto.setAttempts(attempts);
		optionMap.put(dbOption.getUid(), optionDto);
	    }

	    // calculate attempts table
	    List<ScratchieUser> users = scratchieUserDao.getBySessionID(sessionId);
	    for (ScratchieUser user : users) {
		int attemptNumber = 0;

		for (ScratchieAnswerVisitLog attempt : sessionAttempts) {
		    Long optionUid;
		    if (isMcqItem) {
			optionUid = attempt.getQbOption().getUid();

		    } else {
			String answer = attempt.getAnswer();
			boolean isCorrect = ScratchieServiceImpl.isItemUnraveledByAnswers(item, List.of(answer));
			boolean isFirstAnswersGroupCorrect = options.get(0).isCorrect();

			optionUid = isCorrect && isFirstAnswersGroupCorrect || !isCorrect && !isFirstAnswersGroupCorrect
				? options.get(0).getUid()
				: options.get(1).getUid();
		    }
		    OptionDTO optionDto = optionMap.get(optionUid);
		    int[] attempts = optionDto.getAttempts();
		    // +1 for corresponding choice
		    attempts[attemptNumber++]++;
		}
	    }

	    groupSummary.setOptionDtos(optionMap.values());
	    groupSummaryList.add(groupSummary);
	}

	// show total groupSummary if there is more than 1 group available
	if (sessionList.size() > 1 && isMcqItem) {
	    GroupSummary groupSummaryTotal = new GroupSummary();
	    groupSummaryTotal.setSessionId(0L);
	    groupSummaryTotal.setSessionName("Summary");
	    groupSummaryTotal.setMark(0);
	    groupSummaryTotal.setNumberColumns(options.size());

	    Map<Long, OptionDTO> optionMapTotal = new HashMap<>();
	    for (QbOption dbOption : options) {
		// clone it so it doesn't interfere with values from other sessions
		OptionDTO optionDto = new OptionDTO(dbOption);
		int[] attempts = new int[options.size()];
		optionDto.setAttempts(attempts);
		optionMapTotal.put(dbOption.getUid(), optionDto);
	    }

	    for (GroupSummary groupSummary : groupSummaryList) {
		Collection<OptionDTO> optionDtos = groupSummary.getOptionDtos();
		for (OptionDTO optionDto : optionDtos) {
		    int[] attempts = optionDto.getAttempts();

		    OptionDTO optionTotal = optionMapTotal.get(optionDto.getQbOptionUid());
		    int[] attemptsTotal = optionTotal.getAttempts();
		    for (int i = 0; i < attempts.length; i++) {
			attemptsTotal[i] += attempts[i];
		    }
		}
	    }

	    groupSummaryTotal.setOptionDtos(optionMapTotal.values());
	    groupSummaryList.add(0, groupSummaryTotal);
	}

	return groupSummaryList;
    }

    @Override
    public List<BurningQuestionItemDTO> getBurningQuestionDtos(Scratchie scratchie, Long sessionId,
	    boolean includeEmptyItems, boolean prepareForHTML) {

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
	ScratchieItem generalDummyItem = new ScratchieItem();//generalDummyItem's uid will be set to 0 in jsp
	generalDummyItem.setQbQuestion(new QbQuestion());
	releaseFromCache(generalDummyItem);
	releaseFromCache(generalDummyItem.getQbQuestion());
	final String generalQuestionMessage = messageService.getMessage("label.general.burning.question");
	generalDummyItem.getQbQuestion().setName(generalQuestionMessage);
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

		String escapedBurningQuestion = burningQuestionDto.getBurningQuestion().getQuestion();
		if (prepareForHTML) {
		    escapedBurningQuestion = escapedBurningQuestion.replaceAll("\n", "<br>");
		}
		escapedBurningQuestion = StringEscapeUtils.escapeJavaScript(escapedBurningQuestion);
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

    /**
     * Return list of correct AnswerLetters, one per each item
     */
    public static void fillCorrectAnswerLetters(Collection<ScratchieItem> items) {
	for (ScratchieItem item : items) {
	    boolean isMcqItem = item.getQbQuestion().getType() == QbQuestion.TYPE_MULTIPLE_CHOICE
		    || item.getQbQuestion().getType() == QbQuestion.TYPE_MARK_HEDGING;

	    // find out the correct answer's sequential letter - A,B,C...
	    String correctAnswerLetter = "";
	    if (isMcqItem) {
		int answerCount = 1;
		for (OptionDTO answer : item.getOptionDtos()) {
		    if (answer.isCorrect()) {
			correctAnswerLetter = String.valueOf((char) ((answerCount + 'A') - 1));
			break;
		    }
		    answerCount++;
		}

	    } else {
		List<QbOption> options = item.getQbQuestion().getQbOptions();
		if (!options.isEmpty()) {
		    correctAnswerLetter = options.get(0).isCorrect() ? "A" : "B";
		}
	    }
	    item.setCorrectAnswerLetter(correctAnswerLetter);
	}
    }

    @Override
    public List<ExcelSheet> exportExcel(Long contentId) {
	Scratchie scratchie = scratchieDao.getByContentId(contentId);
	Collection<ScratchieItem> items = new TreeSet<>(new ScratchieItemComparator());
	items.addAll(scratchie.getScratchieItems());
	int numberOfItems = items.size();

	List<ScratchieAnswerVisitLog> logs = scratchieAnswerVisitDao.getLogsByScratchieUid(scratchie.getUid());

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
	row.addCell(getMessage("label.marks"), true);
	row.addCell(getMessage("label.marks") + " %", true);

	List<GroupSummary> summaryByTeam = getSummaryByTeam(scratchie, items);
	int maxPossibleScore = getMaxPossibleScore(scratchie);

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

	    row.addCell(summary.getMark());
	    percentage = (numberOfItems == 0) ? 0 : (double) summary.getMark() / maxPossibleScore;
	    row.addPercentageCell(percentage);
	}

	// ======================================================= For Report by Team TRA page
	// =======================================
	ExcelSheet reportByTeamSheet = new ExcelSheet(getMessage("label.report.by.team.tra"));
	sheets.add(reportByTeamSheet);

	LeaderResultsDTO overallDTO = getLeaderResultsDTOForLeaders(contentId);

	ExcelRow overallSummaryRow = reportByTeamSheet.initRow();
	overallSummaryRow.addCell(getMessage("label.export.overall.summary"), true);
	reportByTeamSheet.addEmptyRow();

	overallSummaryRow = reportByTeamSheet.initRow();
	overallSummaryRow.addCell(getMessage("label.number.groups.finished"), true);
	overallSummaryRow.addCell(overallDTO.getCount());

	overallSummaryRow = reportByTeamSheet.initRow();
	overallSummaryRow.addCell(getMessage("label.lowest.mark"), true);
	overallSummaryRow.addCell(overallDTO.getMin() == null ? 0 : overallDTO.getMin());

	overallSummaryRow = reportByTeamSheet.initRow();
	overallSummaryRow.addCell(getMessage("label.highest.mark"), true);
	overallSummaryRow.addCell(overallDTO.getMax() == null ? 0 : overallDTO.getMax());

	overallSummaryRow = reportByTeamSheet.initRow();
	overallSummaryRow.addCell(getMessage("label.average.mark") + ":", true);
	overallSummaryRow.addCell(overallDTO.getAverage() == null ? 0 : overallDTO.getAverage());

	overallSummaryRow = reportByTeamSheet.initRow();
	overallSummaryRow.addCell(getMessage("label.median.mark"), true);
	overallSummaryRow.addCell(overallDTO.getMedian() == null ? 0 : overallDTO.getMedian());

	overallSummaryRow = reportByTeamSheet.initRow();
	overallSummaryRow.addCell(getMessage("label.modes.mark"), true);
	overallSummaryRow.addCell(overallDTO.getModesString());
	reportByTeamSheet.addEmptyRow();
	reportByTeamSheet.addEmptyRow();
	reportByTeamSheet.addEmptyRow();

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
	row.addCell(getMessage("label.marks"));
	row.addCell(getMessage("label.marks") + " %");

	row = reportByTeamSheet.initRow();
	row.addCell(getMessage("label.correct.answer"));
	ScratchieServiceImpl.fillCorrectAnswerLetters(items);
	for (ScratchieItem item : items) {
	    row.addCell(item.getCorrectAnswerLetter());
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
		row.addCell(itemDto.getOptionsSequence(), color);
	    }
	    row.addCell(Integer.valueOf(numberOfFirstChoiceEvents));
	    double percentage = (numberOfItems == 0) ? 0 : (double) numberOfFirstChoiceEvents / numberOfItems;
	    row.addPercentageCell(percentage);

	    percentages[groupCount - 1] = percentage;
	    groupCount++;

	    row.addCell(summary.getMark());
	    percentage = (numberOfItems == 0) ? 0 : (double) summary.getMark() / maxPossibleScore;
	    row.addPercentageCell(percentage);
	}

	Arrays.sort(percentages);

	// avg mean
	double sum = 0;
	for (int i = 0; i < percentages.length; i++) {
	    sum += percentages[i];
	}
	int percentagesLength = percentages.length == 0 ? 1 : percentages.length;
	double avgMean = sum / percentagesLength;
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
	row.addPercentageCell(median);

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

	List<GroupSummary> summaryList = getMonitoringSummary(contentId);
	for (GroupSummary summary : summaryList) {
	    for (ScratchieUser user : summary.getUsers()) {
		if (!isLearnerEligibleForMark(user.getUserId(), contentId)) {
		    continue;
		}
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
	    List<GroupSummary> itemSummary = getGroupSummariesByItem(contentId, item.getUid());
	    boolean isMcqItem = item.getQbQuestion().getType() == QbQuestion.TYPE_MULTIPLE_CHOICE
		    || item.getQbQuestion().getType() == QbQuestion.TYPE_MARK_HEDGING;

	    row = researchAndAnalysisSheet.initRow();
	    row.addCell(getMessage("label.question.semicolon", new Object[] { item.getQbQuestion().getName() }), true);

	    row = researchAndAnalysisSheet.initRow();
	    row.addCell(removeHtmlMarkup(item.getQbQuestion().getDescription()), true);
	    researchAndAnalysisSheet.addEmptyRow();
	    researchAndAnalysisSheet.addEmptyRow();
	    // show all team summary in case there is more than 1 group
	    if (summaryList.size() > 1 && isMcqItem) {
		row = researchAndAnalysisSheet.initRow();
		row.addCell(getMessage("label.all.teams.summary"), true);

		GroupSummary allTeamSummary = itemSummary.get(0);
		Collection<OptionDTO> optionDtos = allTeamSummary.getOptionDtos();

		row = researchAndAnalysisSheet.initRow();
		row.addEmptyCell();
		for (int i = 0; i < optionDtos.size(); i++) {
		    row.addCell(Long.valueOf(i + 1), IndexedColors.YELLOW);
		}

		for (OptionDTO optionDto : optionDtos) {
		    row = researchAndAnalysisSheet.initRow();
		    String answerTitle = removeHtmlMarkup(optionDto.getAnswer());

		    IndexedColors color = null;
		    if (optionDto.isCorrect()) {
			answerTitle += "(" + getMessage("label.monitoring.item.summary.correct") + ")";
			color = IndexedColors.GREEN;
		    }
		    row.addCell(answerTitle, color);

		    for (int numberAttempts : optionDto.getAttempts()) {
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

		Collection<OptionDTO> optionDtos = groupSummary.getOptionDtos();

		row = researchAndAnalysisSheet.initRow();
		row.addCell(groupSummary.getSessionName(), true);

		int longestRowLength = 0;
		if (isMcqItem) {
		    longestRowLength = optionDtos.size();
		} else {
		    for (OptionDTO optionDto : optionDtos) {
			if (longestRowLength < optionDto.getAttempts().length) {
			    longestRowLength = optionDto.getAttempts().length;
			}
		    }
		}

		row = researchAndAnalysisSheet.initRow();
		row.addEmptyCell();
		for (int i = 0; i < longestRowLength; i++) {
		    row.addCell(Integer.valueOf(i + 1));
		}

		for (OptionDTO optionDto : optionDtos) {
		    row = researchAndAnalysisSheet.initRow();
		    String optionTitle = isMcqItem ? removeHtmlMarkup(optionDto.getAnswer())
			    : optionDto.getAnswer().strip().replace("\r\n", ", ");
		    if (optionDto.isCorrect()) {
			optionTitle += "(" + getMessage("label.monitoring.item.summary.correct") + ")";
		    }
		    row.addCell(optionTitle);

		    for (int numberAttempts : optionDto.getAttempts()) {
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
		    boolean isMcqItem = item.getQbQuestion().getType() == QbQuestion.TYPE_MULTIPLE_CHOICE
			    || item.getQbQuestion().getType() == QbQuestion.TYPE_MARK_HEDGING;

		    //build list of all logs left for this item and this session
		    List<ScratchieAnswerVisitLog> logsBySessionAndItem = new ArrayList<>();
		    for (ScratchieAnswerVisitLog log : logs) {
			if (log.getSessionId().equals(sessionId)
				&& log.getQbToolQuestion().getUid().equals(item.getUid())) {
			    logsBySessionAndItem.add(log);
			}
		    }

		    row = researchAndAnalysisSheet.initRow();
		    row.addCell(getMessage("label.question.semicolon", new Object[] { item.getQbQuestion().getName() }),
			    false);

		    int i = 1;
		    for (ScratchieAnswerVisitLog log : logsBySessionAndItem) {
			row = researchAndAnalysisSheet.initRow();
			row.addCell(Integer.valueOf(i++));
			String answerDescr = isMcqItem ? log.getQbOption().getName() : log.getAnswer();
			row.addCell(removeHtmlMarkup(answerDescr));
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

	int maxLogCount = 0;
	for (ScratchieItem item : items) {
	    //search for max value in options size
	    if (item.getOptionDtos().size() > maxLogCount) {
		maxLogCount = item.getOptionDtos().size();
	    }

	    //search for max value in logs length
	    for (GroupSummary summary : summaryByTeam) {
		Long sessionId = summary.getSessionId();

		int logsBySessionAndItem = 0;
		for (ScratchieAnswerVisitLog log : logs) {
		    if (log.getSessionId().equals(sessionId)
			    && log.getQbToolQuestion().getUid().equals(item.getUid())) {
			logsBySessionAndItem++;
		    }
		}

		if (logsBySessionAndItem > maxLogCount) {
		    maxLogCount = logsBySessionAndItem;
		}
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

	for (int i = 0; i < maxLogCount; i++) {
	    row.addCell(getMessage("label." + (i + 1) + ".answer.selected"), true);
	}
	row.addCell(getMessage("label.date"), true);
	for (int i = 0; i < maxLogCount; i++) {
	    row.addCell(getMessage("label.time.of.selection." + (i + 1)), true);
	}

	// Table content------------------------------------

	for (GroupSummary summary : summaryByTeam) {
	    Long sessionId = summary.getSessionId();
	    List<ScratchieUser> users = scratchieUserDao.getBySessionID(sessionId);

	    for (ScratchieUser user : users) {
		int questionCount = 1;
		for (ScratchieItemDTO itemDto : summary.getItemDtos()) {
		    boolean isMcqItem = itemDto.getType() == QbQuestion.TYPE_MULTIPLE_CHOICE
			    || itemDto.getType() == QbQuestion.TYPE_MARK_HEDGING;

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

		    // correct option
		    String correctOption = "";
		    List<OptionDTO> options = itemDto.getOptionDtos();
		    for (OptionDTO option : options) {
			if (option.isCorrect()) {
			    correctOption = option.getAnswer();
			    correctOption = isMcqItem ? removeHtmlMarkup(correctOption)
				    : correctOption.strip().replace("\r\n", ", ");
			}
		    }
		    row.addCell(correctOption);

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

		    //build list of all logs left for this item and this session
		    List<ScratchieAnswerVisitLog> logsBySessionAndItem = new ArrayList<>();
		    for (ScratchieAnswerVisitLog log : logs) {
			if (log.getSessionId().equals(sessionId)
				&& log.getQbToolQuestion().getUid().equals(itemDto.getUid())) {
			    logsBySessionAndItem.add(log);
			}
		    }

		    for (ScratchieAnswerVisitLog log : logsBySessionAndItem) {
			String answer = removeHtmlMarkup(isMcqItem ? log.getQbOption().getName() : log.getAnswer());
			row.addCell(answer);
		    }
		    for (int i = logsBySessionAndItem.size(); i < maxLogCount; i++) {
			row.addEmptyCell();
		    }

		    // Date
		    String dateStr = "";
		    if (logsBySessionAndItem.size() > 0) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
			Date accessDate = logsBySessionAndItem.iterator().next().getAccessDate();
			dateStr = dateFormat.format(accessDate);
		    }
		    row.addCell(dateStr);

		    // time of selection
		    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		    for (ScratchieAnswerVisitLog log : logsBySessionAndItem) {
			Date accessDate = log.getAccessDate();
			String timeStr = timeFormat.format(accessDate);
			row.addCell(timeStr);
		    }
		    for (int i = logsBySessionAndItem.size(); i < maxLogCount; i++) {
			row.addCell("");
		    }
		}
	    }
	}

	// =========================  Burning questions page ======================================
	if (scratchie.isBurningQuestionsEnabled()) {
	    ExcelSheet burningQuestionsSheet = new ExcelSheet(getMessage("label.burning.questions"));
	    sheets.add(burningQuestionsSheet);

	    row = burningQuestionsSheet.initRow();
	    row.addCell(getMessage("label.burning.questions"), true);
	    burningQuestionsSheet.addEmptyRow();

	    row = burningQuestionsSheet.initRow();
	    row.addCell("#", IndexedColors.BLUE);
	    row.addCell(getMessage("label.monitoring.summary.user.name"), IndexedColors.BLUE);
	    row.addCell(getMessage("label.burning.questions"), IndexedColors.BLUE);
	    row.addCell(getMessage("label.count"), IndexedColors.BLUE);

	    List<BurningQuestionItemDTO> burningQuestionItemDtos = getBurningQuestionDtos(scratchie, null, true, false);
	    int index = 1;
	    for (BurningQuestionItemDTO burningQuestionItemDto : burningQuestionItemDtos) {
		ScratchieItem item = burningQuestionItemDto.getScratchieItem();

		row = burningQuestionsSheet.initRow();
		if (index < burningQuestionItemDtos.size()) {
		    row.addCell(index++);
		} else {
		    row.addEmptyCell();
		}

		row.addCell(item.getQbQuestion().getName());

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
    public List<Integer> getMarksArray(Long toolContentId) {
	return scratchieSessionDao.getRawLeaderMarksByToolContentId(toolContentId);
    }

    @Override
    public LeaderResultsDTO getLeaderResultsDTOForLeaders(Long toolContentId) {

	List<Integer> marks = scratchieSessionDao.getRawLeaderMarksByToolContentId(toolContentId);
	LeaderResultsDTO newDto = new LeaderResultsDTO(toolContentId, marks);

	return newDto;
    }

    @Override
    public Map<String, Object> prepareStudentChoicesData(Scratchie scratchie) {
	Map<String, Object> model = new HashMap<>();

	Set<ScratchieItem> items = new TreeSet<>(new ScratchieItemComparator());
	items.addAll(scratchie.getScratchieItems());
	List<ScratchieItem> itemList = new LinkedList<>(items);
	model.put("items", itemList);

	Map<Long, ScratchieItem> itemToCorrectOnFirstAttemptMap = itemList.stream()
		.collect(Collectors.toMap(ScratchieItem::getUid, Function.identity()));

	//correct answers row
	ScratchieServiceImpl.fillCorrectAnswerLetters(itemList);

	Map<String, ScratchieSession> sessionsByName = scratchieSessionDao.getByContentId(scratchie.getContentId())
		.stream().filter(s -> s.getGroupLeader() != null)
		.collect(Collectors.toMap(ScratchieSession::getSessionName, Function.identity()));
	List<GroupSummary> groupSummaries = getSummaryByTeam(scratchie, items);

	for (GroupSummary summary : groupSummaries) {
	    ScratchieSession session = sessionsByName.get(summary.getSessionName());
	    if (session != null) {
		// for this view, we need user ID, not UID
		summary.setLeaderUid(session.getGroupLeader().getUserId());
	    }

	    //prepare OptionDtos to display
	    int i = 0;
	    for (ScratchieItemDTO itemDto : summary.getItemDtos()) {
		String optionSequence = itemDto.getOptionsSequence();
		String[] optionLetters = optionSequence.split(", ");

		List<OptionDTO> optionDtos = new LinkedList<>();
		for (int j = 0; j < optionLetters.length; j++) {
		    String optionLetter = optionLetters[j];
		    String correctOptionLetter = itemList.get(i).getCorrectAnswerLetter();

		    OptionDTO optionDto = new OptionDTO();
		    optionDto.setAnswer(optionLetter);
		    optionDto.setCorrect(correctOptionLetter.equals(optionLetter));
		    optionDtos.add(optionDto);
		}

		itemDto.setOptionDtos(optionDtos);
		i++;
	    }

	    //calculate what is the percentage of first choice events in each session
	    int numberOfFirstChoiceEvents = 0;
	    for (ScratchieItemDTO itemDto : summary.getItemDtos()) {
		if (itemDto.isUnraveledOnFirstAttempt()) {
		    numberOfFirstChoiceEvents++;

		    ScratchieItem item = itemToCorrectOnFirstAttemptMap.get(itemDto.getUid());
		    item.setCorrectOnFirstAttemptCount(item.getCorrectOnFirstAttemptCount() + 1);
		}
	    }
	    summary.setMark(numberOfFirstChoiceEvents);

	    // round the percentage cell
	    String totalPercentage = String
		    .valueOf((items.size() == 0) ? 0 : (double) numberOfFirstChoiceEvents * 100 / items.size());
	    summary.setTotalPercentage(Double.valueOf(totalPercentage));

	}
	model.put("sessionDtos", groupSummaries);

	boolean vsaPresent = false;
	for (ScratchieItem item : itemList) {
	    item.setCorrectOnFirstAttemptPercent(groupSummaries.isEmpty() ? 0
		    : (double) item.getCorrectOnFirstAttemptCount() * 100 / groupSummaries.size());

	    if (!vsaPresent && item.getQbQuestion().getType().equals(QbQuestion.TYPE_VERY_SHORT_ANSWERS)) {
		vsaPresent = true;
	    }
	}

	model.put("vsaPresent", vsaPresent);

	return model;
    }

    @Override
    public boolean isLearnerEligibleForMark(long learnerId, long toolContentId) {
	return learnerService.isLearnerStartedLessonByContentId(Long.valueOf(learnerId).intValue(), toolContentId);
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************

    /**
     * Currently removes only <div> tags.
     */
    private String removeHtmlMarkup(String string) {
	return string == null ? null : string.replaceAll("[<](/)?div[^>]*[>]", "");
    }

    /**
     * Serves merely for excel export purposes. Produces data for "Summary By Team" section.
     */
    @Override
    public List<GroupSummary> getSummaryByTeam(Scratchie scratchie, Collection<ScratchieItem> sortedItems) {
	List<GroupSummary> groupSummaries = new ArrayList<>();
	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(scratchie.getContentId());
	for (ScratchieSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    // one new summary for one session.
	    GroupSummary groupSummary = new GroupSummary(session);
	    ArrayList<ScratchieItemDTO> itemDtos = new ArrayList<>();

	    ScratchieUser groupLeader = session.getGroupLeader();
	    List<ScratchieAnswerVisitLog> logs = scratchieAnswerVisitDao.getLogsBySession(sessionId);

	    populateScratchieItemsWithMarks(scratchie, sortedItems, sessionId);

	    for (ScratchieItem item : sortedItems) {
		ScratchieItemDTO itemDto = new ScratchieItemDTO();
		int numberOfAttempts = 0;
		int mark = -1;
		boolean isUnraveledOnFirstAttempt = false;
		String optionsSequence = "";
		QbQuestion qbQuestion = item.getQbQuestion();
		boolean isMcqItem = qbQuestion.getType() == QbQuestion.TYPE_MULTIPLE_CHOICE
			|| qbQuestion.getType() == QbQuestion.TYPE_MARK_HEDGING;

		// if there is no group leader don't calculate numbers - there aren't any
		if (groupLeader != null) {

		    //create a list of attempts user done for the current item
		    List<ScratchieAnswerVisitLog> visitLogs = new ArrayList<>();
		    for (ScratchieAnswerVisitLog log : logs) {
			if (log.getQbToolQuestion().getUid().equals(item.getUid())) {
			    visitLogs.add(log);
			}
		    }
		    numberOfAttempts = ScratchieServiceImpl.getNumberAttemptsForItem(item, visitLogs);

		    // for displaying purposes if there is no attemps we assign -1 which will be shown as "-"
		    mark = numberOfAttempts == 0 ? -1 : item.getMark();

		    isUnraveledOnFirstAttempt = numberOfAttempts == 1
			    && ScratchieServiceImpl.isItemUnraveled(item, logs);

		    // find out options' sequential letters - A,B,C...
		    for (ScratchieAnswerVisitLog visitLog : visitLogs) {
			String sequencialLetter = "";

			if (isMcqItem) {
			    int optionCount = 1;
			    for (OptionDTO optionDto : item.getOptionDtos()) {
				boolean isOptionMet = optionDto.getQbOptionUid()
					.equals(visitLog.getQbOption().getUid());
				if (isOptionMet) {
				    sequencialLetter = String.valueOf((char) ((optionCount + 'A') - 1));
				    break;
				}
				optionCount++;
			    }

			} else {
			    String answer = visitLog.getAnswer();
			    boolean isCorrect = ScratchieServiceImpl.isItemUnraveledByAnswers(item, List.of(answer));
			    boolean isFirstAnswersGroupCorrect = qbQuestion.getQbOptions().get(0).isCorrect();

			    sequencialLetter = isCorrect && isFirstAnswersGroupCorrect
				    || !isCorrect && !isFirstAnswersGroupCorrect ? "A" : "B";
			}

			optionsSequence += optionsSequence.isEmpty() ? sequencialLetter : ", " + sequencialLetter;
		    }
		}

		itemDto.setUid(item.getUid());
		itemDto.setTitle(qbQuestion.getName());
		itemDto.setType(qbQuestion.getType());
		List<OptionDTO> optionDtos = new LinkedList<>();
		if (isMcqItem) {
		    optionDtos = item.getOptionDtos();
		} else {
		    for (QbOption qbOption : qbQuestion.getQbOptions()) {
			OptionDTO optionDTO = new OptionDTO(qbOption);
			optionDTO.setMcqType(false);
			optionDtos.add(optionDTO);
		    }
		}
		itemDto.setOptionDtos(optionDtos);
		itemDto.setUserAttempts(numberOfAttempts);
		itemDto.setUserMark(mark);
		itemDto.setUnraveledOnFirstAttempt(isUnraveledOnFirstAttempt);
		itemDto.setOptionsSequence(optionsSequence);

		itemDtos.add(itemDto);
	    }

	    groupSummary.setItemDtos(itemDtos);
	    groupSummaries.add(groupSummary);
	}

	return groupSummaries;
    }

    /**
     * Return specified option's sequential letter (e.g. A,B,C) among other possible options
     */
    private static String getSequencialLetter(ScratchieItem item, QbOption qbOption) {
	String sequencialLetter = "";
	int optionCount = 1;
	for (OptionDTO optionDto : item.getOptionDtos()) {
	    if (optionDto.getQbOptionUid() != null && optionDto.getQbOptionUid().equals(qbOption.getUid())) {
		sequencialLetter = String.valueOf((char) ((optionCount + 'A') - 1));
		break;
	    }
	    optionCount++;
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

    @Override
    public List<QbToolQuestion> replaceQuestions(long toolContentId, String newActivityName,
	    List<QbQuestion> newQuestions) {
	Scratchie scratchie = getScratchieByContentId(toolContentId);
	if (newActivityName != null) {
	    scratchie.setTitle(newActivityName);
	    scratchieDao.update(scratchie);
	}

	// remove all existing questions
	for (ScratchieItem scratchieItem : scratchie.getScratchieItems()) {
	    scratchieItemDao.delete(scratchieItem);
	}
	// this is needed, otherwise Hibernate wants to re-save the deleted Scratchie questions
	scratchie.getScratchieItems().clear();

	// populate Scratchie with new questions
	int displayOrder = 1;
	List<QbToolQuestion> result = new ArrayList<>(newQuestions.size());
	for (QbQuestion qbQuestion : newQuestions) {
	    ScratchieItem scratchieItem = new ScratchieItem();
	    scratchieItem.setDisplayOrder(displayOrder++);
	    scratchieItem.setQbQuestion(qbQuestion);
	    scratchieItem.setToolContentId(toolContentId);
	    scratchieItemDao.insert(scratchieItem);
	    scratchie.getScratchieItems().add(scratchieItem);
	}
	scratchieDao.update(scratchie);
	return result;
    }

    @Override
    public void replaceQuestion(long toolContentId, long oldQbQuestionUid, long newQbQuestionUid) {
	Scratchie scratchie = getScratchieByContentId(toolContentId);
	QbQuestion newQbQuestion = null;
	for (ScratchieItem item : scratchie.getScratchieItems()) {
	    if (item.getQbQuestion().getUid().equals(oldQbQuestionUid)) {
		if (newQbQuestion == null) {
		    newQbQuestion = qbService.getQuestionByUid(newQbQuestionUid);
		}
		item.setQbQuestion(newQbQuestion);
	    }
	}
    }

    @Override
    public void changeLeaderForGroup(long toolSessionId, long leaderUserId) {
	ScratchieSession session = getScratchieSessionBySessionId(toolSessionId);
	if (session.isScratchingFinished()) {
	    throw new InvalidParameterException("Attempting to assing a new leader with user ID " + leaderUserId
		    + " to a finished session wtih ID " + toolSessionId);
	}

	ScratchieUser existingLeader = session.getGroupLeader();
	if (existingLeader == null || existingLeader.getUserId().equals(leaderUserId)) {
	    return;
	}
	Scratchie scratchie = session.getScratchie();
	ScratchieUser newLeader = getUserByUserIDAndContentID(leaderUserId, scratchie.getContentId());
	if (newLeader == null) {
	    User user = userManagementService.getUserById(Long.valueOf(leaderUserId).intValue());
	    newLeader = new ScratchieUser(user.getUserDTO(), session);
	    createUser(newLeader);

	    if (log.isDebugEnabled()) {
		log.debug("Created user with ID " + leaderUserId + " to become a new leader for session with ID "
			+ toolSessionId);
	    }
	} else if (!newLeader.getSession().getSessionId().equals(toolSessionId)) {
	    throw new InvalidParameterException("User with ID " + leaderUserId + " belongs to session with ID "
		    + newLeader.getSession().getSessionId() + " and not to session with ID " + toolSessionId);
	}

	session.setGroupLeader(newLeader);
	scratchieSessionDao.update(session);

	if (log.isDebugEnabled()) {
	    log.debug("User with ID " + leaderUserId + " became a new leader for session with ID " + toolSessionId);
	}

	Set<Integer> userIds = getUsersBySession(toolSessionId).stream()
		.collect(Collectors.mapping(scratchieUser -> scratchieUser.getUserId().intValue(), Collectors.toSet()));

	ObjectNode jsonCommand = JsonNodeFactory.instance.objectNode();
	jsonCommand.put("hookTrigger", "scratchie-leader-change-refresh-" + toolSessionId);
	learnerService.createCommandForLearners(scratchie.getContentId(), userIds, jsonCommand.toString());
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
	for (ScratchieItem scratchieItem : toolContentObj.getScratchieItems()) {
	    qbService.prepareQuestionForExport(scratchieItem.getQbQuestion());
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

	    long userQbCollectionUid = qbService.getUserPrivateCollection(newUserUid).getUid();

	    // we need to save QB questions and options first
	    for (ScratchieItem scratchieItem : toolContentObj.getScratchieItems()) {
		QbQuestion qbQuestion = scratchieItem.getQbQuestion();
		qbQuestion.clearID();

		// try to match the question to an existing QB question in DB
		QbQuestion existingQuestion = qbService.getQuestionByUUID(qbQuestion.getUuid());
		if (existingQuestion == null) {
		    // none found, create a new QB question
		    qbService.insertQuestion(qbQuestion);
		    qbService.addQuestionToCollection(userQbCollectionUid, qbQuestion.getQuestionId(), false);
		} else {
		    // found, use the existing one
		    scratchieItem.setQbQuestion(existingQuestion);
		}

		scratchieDao.insert(scratchieItem);
		// in case an imported question had a question ID which is the highest
		qbService.updateMaxQuestionId();
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
	return scratchieOutputFactory.getToolOutputDefinitions(this, content, definitionType);
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
	saveOrUpdateScratchie(toContent);
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
		    session.setScratchingFinished(false);
		    session.setSessionEndDate(null);
		    session.setTimeLimitLaunchedDate(null);
		    scratchieSessionDao.update(session);

		    scratchieAnswerVisitDao.deleteByProperty(ScratchieAnswerVisitLog.class, "sessionId",
			    session.getSessionId());
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
	return scratchieOutputFactory.getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return scratchieOutputFactory.getToolOutput(name, this, toolSessionId, learnerId);
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

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
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

    public void setQbService(IQbService qbService) {
	this.qbService = qbService;
    }

    public void setOutcomeService(IOutcomeService outcomeService) {
	this.outcomeService = outcomeService;
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
	return scratchieOutputFactory.getSupportedDefinitionClasses(definitionType);
    }

    public void setScratchieOutputFactory(ScratchieOutputFactory scratchieOutputFactory) {
	this.scratchieOutputFactory = scratchieOutputFactory;
    }

    public void setLearnerService(ILearnerService learnerService) {
	this.learnerService = learnerService;
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
    @SuppressWarnings("unchecked")
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
	scratchie.setDiscussionSentimentEnabled(
		JsonUtil.optBoolean(toolContentJSON, RestTags.ENABLE_DISCUSSION_SENTIMENT, false));
	scratchie.setRelativeTimeLimit(JsonUtil.optInt(toolContentJSON, "timeLimit", 0));
	scratchie.setReflectOnActivity(
		JsonUtil.optBoolean(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	scratchie.setReflectInstructions(JsonUtil.optString(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS));
	scratchie.setShowScrachiesInResults(JsonUtil.optBoolean(toolContentJSON, "showScrachiesInResults", true));
	scratchie.setConfidenceLevelsActivityUiid(
		JsonUtil.optInt(toolContentJSON, RestTags.CONFIDENCE_LEVELS_ACTIVITY_UIID));
	scratchie.setActivityUiidProvidingVsaAnswers(
		JsonUtil.optInt(toolContentJSON, "activityUiidProvidingVsaAnswers"));

	// Scratchie Items
	Set<ScratchieItem> newItems = new TreeSet<>();

	QbCollection collection = null;
	Set<String> collectionUUIDs = null;
	Long privateCollectionUUID = null;

	ArrayNode questions = JsonUtil.optArray(toolContentJSON, RestTags.QUESTIONS);
	for (int i = 0; i < questions.size(); i++) {

	    ObjectNode questionData = (ObjectNode) questions.get(i);

	    ScratchieItem item = new ScratchieItem();
	    int type = JsonUtil.optInt(questionData, "type", QbQuestion.TYPE_MULTIPLE_CHOICE);
	    item.setDisplayOrder(JsonUtil.optInt(questionData, RestTags.DISPLAY_ORDER));
	    item.setAnswerRequired(JsonUtil.optBoolean(questionData, "answerRequired", Boolean.FALSE));
	    item.setToolContentId(scratchie.getContentId());

	    QbQuestion qbQuestion = null;
	    String uuid = JsonUtil.optString(questionData, RestTags.QUESTION_UUID);

	    // try to match the question to an existing QB question in DB
	    if (StringUtils.isNotBlank(uuid)) {
		qbQuestion = qbService.getQuestionByUUID(UUID.fromString(uuid));
	    }
	    if (qbQuestion != null && !qbQuestion.getType().equals(type)) {
		qbQuestion = null;
	    }

	    Long collectionUid = JsonUtil.optLong(questionData, RestTags.COLLECTION_UID);
	    if (collectionUid == null) {
		// if no collection UUID was specified, questions end up in user's private collection
		if (privateCollectionUUID == null) {
		    privateCollectionUUID = qbService.getUserPrivateCollection(userID).getUid();
		}
		collectionUid = privateCollectionUUID;
	    }

	    boolean addToCollection = true;
	    // check if it is the same collection - there is a good chance it is
	    if (collection == null || collectionUid != collection.getUid()) {
		collection = qbService.getCollection(collectionUid);
		if (collection == null) {
		    addToCollection = false;
		} else {
		    collectionUUIDs = qbService.getCollectionQuestions(collection.getUid()).stream()
			    .peek(q -> qbService.releaseFromCache(q)).filter(q -> q.getUuid() != null)
			    .collect(Collectors.mapping(q -> q.getUuid().toString(), Collectors.toSet()));
		}
	    }

	    if (qbQuestion == null) {
		qbQuestion = new QbQuestion();
		qbQuestion.setType(QbQuestion.TYPE_MULTIPLE_CHOICE);
		qbQuestion.setQuestionId(qbService.generateNextQuestionId());
		qbQuestion.setContentFolderId(FileUtil.generateUniqueContentFolderID());
		qbQuestion.setName(JsonUtil.optString(questionData, RestTags.QUESTION_TITLE));
		qbQuestion.setDescription(JsonUtil.optString(questionData, RestTags.QUESTION_TEXT));
		scratchieDao.insert(qbQuestion);

		// set options
		List<QbOption> newOptions = new LinkedList<>();

		ArrayNode answersData = JsonUtil.optArray(questionData, RestTags.ANSWERS);
		for (int j = 0; j < answersData.size(); j++) {
		    ObjectNode answerData = (ObjectNode) answersData.get(j);
		    QbOption option = new QbOption();
		    // Removes redundant new line characters from options left by CKEditor (otherwise it will break
		    // Javascript in monitor). Copied from AuthoringAction.
		    String optionDescription = JsonUtil.optString(answerData, RestTags.ANSWER_TEXT);
		    option.setName(optionDescription != null ? optionDescription.replaceAll("[\n\r\f]", "") : "");
		    option.setCorrect(JsonUtil.optBoolean(answerData, RestTags.CORRECT));
		    option.setDisplayOrder(JsonUtil.optInt(answerData, RestTags.DISPLAY_ORDER));
		    option.setQbQuestion(qbQuestion);
		    newOptions.add(option);
		}

		qbQuestion.setQbOptions(newOptions);

		// only process learning outcomes when this is not a modification, i.e. it is a new question
		ArrayNode learningOutcomesJSON = JsonUtil.optArray(questionData, RestTags.LEARNING_OUTCOMES);
		if (learningOutcomesJSON != null && learningOutcomesJSON.size() > 0) {
		    for (JsonNode learningOutcomeJSON : learningOutcomesJSON) {
			String learningOutcomeText = learningOutcomeJSON.asText();
			learningOutcomeText = learningOutcomeText.strip();
			List<Outcome> learningOutcomes = userManagementService.findByProperty(Outcome.class, "name",
				learningOutcomeText);
			Outcome learningOutcome = null;
			if (learningOutcomes.isEmpty()) {
			    learningOutcome = outcomeService.createOutcome(learningOutcomeText, userID);
			} else {
			    learningOutcome = learningOutcomes.get(0);
			}

			OutcomeMapping outcomeMapping = new OutcomeMapping();
			outcomeMapping.setOutcome(learningOutcome);
			outcomeMapping.setQbQuestionId(qbQuestion.getQuestionId());
			userManagementService.save(outcomeMapping);
		    }
		}
	    } else if (addToCollection && collectionUUIDs.contains(uuid)) {
		addToCollection = false;
	    }

	    item.setQbQuestion(qbQuestion);
	    // we need to save item now so it gets an ID and it will be recognised in a set
	    scratchieItemDao.insert(item);
	    newItems.add(item);

	    // all questions need to end up in user's private collection
	    if (addToCollection) {
		qbService.addQuestionToCollection(collection.getUid(), qbQuestion.getQuestionId(), false);
		collectionUUIDs.add(uuid);
	    }
	}

	scratchie.setScratchieItems(newItems);
	saveOrUpdateScratchie(scratchie);
    }
}