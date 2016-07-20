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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
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
import org.lamsfoundation.lams.tool.scratchie.dto.ReflectDTO;
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
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieToolContentHandler;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.ExcelCell;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * @author Andrey Balan
 */
public class ScratchieServiceImpl
	implements IScratchieService, ToolContentManager, ToolSessionManager, ToolRestManager {
    private static Logger log = Logger.getLogger(ScratchieServiceImpl.class.getName());

    private static final ExcelCell[] EMPTY_ROW = new ExcelCell[4];

    private ScratchieDAO scratchieDao;

    private ScratchieItemDAO scratchieItemDao;

    private ScratchieUserDAO scratchieUserDao;

    private ScratchieSessionDAO scratchieSessionDao;

    private ScratchieAnswerVisitDAO scratchieAnswerVisitDao;

    private ScratchieBurningQuestionDAO scratchieBurningQuestionDao;

    private BurningQuestionLikeDAO burningQuestionLikeDao;

    private ScratchieConfigItemDAO scratchieConfigItemDao;

    // tool service
    private ScratchieToolContentHandler scratchieToolContentHandler;

    private MessageService messageService;

    // system services

    private ILamsToolService toolService;

    private ILearnerService learnerService;

    private IUserManagementService userManagementService;

    private IExportToolContentService exportContentService;

    private IGradebookService gradebookService;

    private IAuditService auditService;

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
	    ScratchieServiceImpl.log.error(error);
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
    public void deleteScratchieItem(Long uid) {
	scratchieItemDao.removeObject(ScratchieItem.class, uid);
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
		    ScratchieServiceImpl.log.debug("creating new user with userId: " + leaderUserId);
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

	    gradebookService.updateActivityMark(new Double(newMark), null, user.getUserId().intValue(),
		    user.getSession().getSessionId(), false);

	    // record mark change with audit service
	    auditService.logMarkChange(ScratchieConstants.TOOL_SIGNATURE, user.getUserId(), user.getLoginName(),
		    "" + oldMark, "" + newMark);
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
	List<ScratchieAnswerVisitLog> userLogs = scratchieAnswerVisitDao.getLogsBySession(sessionId);
	ScratchieSession session = this.getScratchieSessionBySessionId(sessionId);
	Scratchie scratchie = session.getScratchie();
	Set<ScratchieItem> items = scratchie.getScratchieItems();
	String presetMarks = getConfigItem(ScratchieConfigItem.KEY_PRESET_MARKS).getConfigValue();

	// calculate mark
	int mark = 0;
	if (!items.isEmpty()) {
	    for (ScratchieItem item : items) {
		mark += getUserMarkPerItem(scratchie, item, userLogs, presetMarks);
	    }
	}

	// change mark for all learners in a group
	session.setMark(mark);
	scratchieSessionDao.saveObject(session);

	// propagade changes to Gradebook
	if (isPropagateToGradebook) {
	    List<ScratchieUser> users = this.getUsersBySession(sessionId);
	    for (ScratchieUser user : users) {
		gradebookService.updateActivityMark(new Double(mark), null, user.getUserId().intValue(),
			user.getSession().getSessionId(), false);
	    }
	}
    }

    @Override
    public void recalculateUserAnswers(Scratchie scratchie, Set<ScratchieItem> oldItems, Set<ScratchieItem> newItems,
	    List<ScratchieItem> deletedItems) {

	// create list of modified questions
	List<ScratchieItem> modifiedItems = new ArrayList<ScratchieItem>();
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
	    List<ScratchieAnswerVisitLog> visitLogsToDelete = new ArrayList<ScratchieAnswerVisitLog>();
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
    public void setScratchingFinished(Long toolSessionId) {
	ScratchieSession session = this.getScratchieSessionBySessionId(toolSessionId);
	session.setScratchingFinished(true);

	scratchieSessionDao.saveObject(session);
    }

    @Override
    public String finishToolSession(Long toolSessionId, Long userId) throws ScratchieApplicationException {
	String nextUrl = null;
	try {
	    ScratchieUser user = scratchieUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	    user.setSessionFinished(true);
	    scratchieUserDao.saveObject(user);

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
	Set<ScratchieUser> leaders = new TreeSet<ScratchieUser>();
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
    public void saveUser(ScratchieUser user) {
	scratchieUserDao.saveObject(user);
    }

    @Override
    public List<GroupSummary> getMonitoringSummary(Long contentId, boolean isIncludeOnlyLeaders) {
	List<GroupSummary> groupSummaryList = new ArrayList<GroupSummary>();
	List<ScratchieSession> sessions = scratchieSessionDao.getByContentId(contentId);

	for (ScratchieSession session : sessions) {
	    Long sessionId = session.getSessionId();

	    // one new summary for one session.
	    GroupSummary groupSummary = new GroupSummary(session);

	    int totalAttempts = scratchieAnswerVisitDao.getLogCountTotal(sessionId);
	    groupSummary.setTotalAttempts(totalAttempts);

	    List<ScratchieUser> sessionUsers = scratchieUserDao.getBySessionID(sessionId);
	    List<ScratchieUser> usersToShow = new LinkedList<ScratchieUser>();
	    for (ScratchieUser user : sessionUsers) {

		boolean isUserGroupLeader = session.isUserGroupLeader(user.getUid());
		// include only leaders in case isUserGroupLeader is ON, include all otherwise
		if ((isIncludeOnlyLeaders && isUserGroupLeader) || !isIncludeOnlyLeaders) {
		    usersToShow.add(user);
		}
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

	    for (ScratchieAnswer answer : (Set<ScratchieAnswer>) item.getAnswers()) {

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
	Set<ScratchieItem> items = new TreeSet<ScratchieItem>(new ScratchieItemComparator());
	items.addAll(scratchie.getScratchieItems());

	return getItemsWithIndicatedScratches(toolSessionId, items);
    }

    @Override
    public Collection<ScratchieItem> getItemsWithIndicatedScratches(Long toolSessionId,
	    Collection<ScratchieItem> items) {
	List<ScratchieAnswerVisitLog> userLogs = scratchieAnswerVisitDao.getLogsBySession(toolSessionId);

	for (ScratchieItem item : items) {

	    for (ScratchieAnswer answer : (Set<ScratchieAnswer>) item.getAnswers()) {

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

	for (ScratchieAnswer answer : (Set<ScratchieAnswer>) item.getAnswers()) {

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

    /**
     *
     * @param scratchie
     * @param item
     * @param userLogs
     *            uses list of logs to reduce number of queries to DB
     * @param presetMarks
     *            presetMarks to reduce number of queries to DB
     * @return
     */
    private int getUserMarkPerItem(Scratchie scratchie, ScratchieItem item, List<ScratchieAnswerVisitLog> userLogs,
	    String presetMarks) {

	String[] marksArray = presetMarks.split(",");

	int mark = 0;
	// add mark only if an item was unraveled
	if (isItemUnraveled(item, userLogs)) {

	    int itemAttempts = calculateItemAttempts(userLogs, item);
	    String markStr = (itemAttempts <= marksArray.length) ? marksArray[itemAttempts - 1]
		    : marksArray[marksArray.length - 1];
	    mark = Integer.parseInt(markStr);

	    // add extra point if needed
	    if (scratchie.isExtraPoint() && (itemAttempts == 1)) {
		mark++;
	    }

	}

	return mark;
    }

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
	List<GroupSummary> groupSummaryList = new ArrayList<GroupSummary>();

	ScratchieItem item = scratchieItemDao.getByUid(itemUid);
	Collection<ScratchieAnswer> answers = item.getAnswers();
	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(contentId);
	for (ScratchieSession session : sessionList) {
	    Long sessionId = session.getSessionId();

	    // one new summary for one session.
	    GroupSummary groupSummary = new GroupSummary(session);

	    Map<Long, ScratchieAnswer> answerMap = new HashMap<Long, ScratchieAnswer>();
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

	    Collection<ScratchieAnswer> sortedAnswers = new TreeSet<ScratchieAnswer>(new ScratchieAnswerComparator());
	    sortedAnswers.addAll(answerMap.values());
	    groupSummary.setAnswers(sortedAnswers);
	    groupSummaryList.add(groupSummary);
	}

	// show total groupSummary if there is more than 1 group available
	if (sessionList.size() > 1) {
	    GroupSummary groupSummaryTotal = new GroupSummary();
	    groupSummaryTotal.setSessionId(new Long(0));
	    groupSummaryTotal.setSessionName("Summary");
	    groupSummaryTotal.setMark(0);

	    Map<Long, ScratchieAnswer> answerMapTotal = new HashMap<Long, ScratchieAnswer>();
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

	    Collection<ScratchieAnswer> sortedAnswers = new TreeSet<ScratchieAnswer>(new ScratchieAnswerComparator());
	    sortedAnswers.addAll(answerMapTotal.values());
	    groupSummaryTotal.setAnswers(sortedAnswers);
	    groupSummaryList.add(0, groupSummaryTotal);
	}

	return groupSummaryList;
    }

    @Override
    public List<BurningQuestionItemDTO> getBurningQuestionDtos(Scratchie scratchie, Long sessionId) {

	Set<ScratchieItem> items = new TreeSet<ScratchieItem>(new ScratchieItemComparator());
	items.addAll(scratchie.getScratchieItems());

	List<BurningQuestionDTO> burningQuestionDtos = scratchieBurningQuestionDao
		.getBurningQuestionsByContentId(scratchie.getUid(), sessionId);

	//in order to group BurningQuestions by items, organise them as a list of BurningQuestionItemDTOs
	List<BurningQuestionItemDTO> burningQuestionItemDtos = new ArrayList<BurningQuestionItemDTO>();
	for (ScratchieItem item : items) {

	    List<BurningQuestionDTO> burningQuestionDtosOfSpecifiedItem = new ArrayList<BurningQuestionDTO>();

	    for (BurningQuestionDTO burningQuestionDto : burningQuestionDtos) {
		ScratchieBurningQuestion burningQuestion = burningQuestionDto.getBurningQuestion();

		//general burning question is handled further down
		if (!burningQuestion.isGeneralQuestion()
			&& item.getUid().equals(burningQuestion.getScratchieItem().getUid())) {
		    burningQuestionDtosOfSpecifiedItem.add(burningQuestionDto);
		}
	    }

	    BurningQuestionItemDTO burningQuestionItemDto = new BurningQuestionItemDTO();
	    burningQuestionItemDto.setScratchieItem(item);
	    burningQuestionItemDto.setBurningQuestionDtos(burningQuestionDtosOfSpecifiedItem);
	    burningQuestionItemDtos.add(burningQuestionItemDto);
	}

	// handle general burning question
	BurningQuestionItemDTO generalBurningQuestionItemDto = new BurningQuestionItemDTO();
	ScratchieItem generalDummyItem = new ScratchieItem();
	generalDummyItem.setUid(0L);
	final String generalQuestionMessage = messageService.getMessage("label.general.burning.question");
	generalDummyItem.setTitle(generalQuestionMessage);
	generalBurningQuestionItemDto.setScratchieItem(generalDummyItem);
	List<BurningQuestionDTO> burningQuestionDtosOfSpecifiedItem = new ArrayList<BurningQuestionDTO>();
	for (BurningQuestionDTO burningQuestionDto : burningQuestionDtos) {
	    ScratchieBurningQuestion burningQuestion = burningQuestionDto.getBurningQuestion();

	    if (burningQuestion.isGeneralQuestion()) {
		burningQuestionDtosOfSpecifiedItem.add(burningQuestionDto);
	    }
	}
	generalBurningQuestionItemDto.setBurningQuestionDtos(burningQuestionDtosOfSpecifiedItem);
	burningQuestionItemDtos.add(generalBurningQuestionItemDto);

	//escape for Javascript
	for (BurningQuestionItemDTO burningQuestionItemDto : burningQuestionItemDtos) {
	    for (BurningQuestionDTO burningQuestionDto : burningQuestionItemDto.getBurningQuestionDtos()) {
		String escapedSessionName = StringEscapeUtils.escapeJavaScript(burningQuestionDto.getSessionName());
		burningQuestionDto.setSessionName(escapedSessionName);

		String escapedBurningQuestion = StringEscapeUtils.escapeJavaScript(
			burningQuestionDto.getBurningQuestion().getQuestion().replaceAll("\\n", "<br>"));
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
	ArrayList<ReflectDTO> reflections = new ArrayList<ReflectDTO>();

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
    public LinkedHashMap<String, ExcelCell[][]> exportExcel(Long contentId) {
	Scratchie scratchie = scratchieDao.getByContentId(contentId);
	Collection<ScratchieItem> items = new TreeSet<ScratchieItem>(new ScratchieItemComparator());
	items.addAll(scratchie.getScratchieItems());
	int numberOfItems = items.size();

	LinkedHashMap<String, ExcelCell[][]> dataToExport = new LinkedHashMap<String, ExcelCell[][]>();

	// ======================================================= For Immediate Analysis page
	// =======================================

	List<ExcelCell[]> rowList = new LinkedList<ExcelCell[]>();

	ExcelCell[] row = new ExcelCell[1];
	row[0] = new ExcelCell(getMessage("label.quick.analysis"), true);
	rowList.add(row);
	row = new ExcelCell[2];
	row[1] = new ExcelCell(getMessage("label.in.table.below.we.show"), false);
	rowList.add(row);
	rowList.add(ScratchieServiceImpl.EMPTY_ROW);

	row = new ExcelCell[3];
	row[2] = new ExcelCell(getMessage("label.questions"), false);
	rowList.add(row);

	row = new ExcelCell[numberOfItems + 4];
	int columnCount = 1;
	row[columnCount++] = new ExcelCell(getMessage("label.teams"), true);
	for (int itemCount = 0; itemCount < numberOfItems; itemCount++) {
	    row[columnCount++] = new ExcelCell("Q" + (itemCount + 1), true);
	}
	row[columnCount++] = new ExcelCell(getMessage("label.total"), true);
	row[columnCount++] = new ExcelCell(getMessage("label.total") + " %", true);
	rowList.add(row);

	List<GroupSummary> summaryByTeam = getSummaryByTeam(scratchie, items);
	for (GroupSummary summary : summaryByTeam) {

	    row = new ExcelCell[numberOfItems + 4];
	    columnCount = 1;
	    row[columnCount++] = new ExcelCell(summary.getSessionName(), true);

	    int numberOfFirstChoiceEvents = 0;
	    for (ScratchieItem item : summary.getItems()) {
		int attempts = item.getUserAttempts();

		String isFirstChoice;
		IndexedColors color;
		if (item.getCorrectAnswer().equals(Boolean.TRUE.toString())) {
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
		row[columnCount++] = new ExcelCell(isFirstChoice, color);
	    }
	    row[columnCount++] = new ExcelCell(new Integer(numberOfFirstChoiceEvents), false);
	    int percentage = (numberOfItems == 0) ? 0 : (100 * numberOfFirstChoiceEvents) / numberOfItems;
	    row[columnCount++] = new ExcelCell(percentage + "%", false);
	    rowList.add(row);
	}

	ExcelCell[][] firstPageData = rowList.toArray(new ExcelCell[][] {});
	dataToExport.put(getMessage("label.for.immediate.analysis"), firstPageData);

	// ======================================================= For Report by Team TRA page
	// =======================================

	rowList = new LinkedList<ExcelCell[]>();

	row = new ExcelCell[1];
	row[0] = new ExcelCell(getMessage("label.quick.analysis"), true);
	rowList.add(row);
	row = new ExcelCell[2];
	row[1] = new ExcelCell(getMessage("label.table.below.shows.which.answer.teams.selected.first.try"), false);
	rowList.add(row);
	rowList.add(ScratchieServiceImpl.EMPTY_ROW);

	row = new ExcelCell[numberOfItems + 3];
	columnCount = 1;
	for (int itemCount = 0; itemCount < numberOfItems; itemCount++) {
	    row[columnCount++] = new ExcelCell(getMessage("label.authoring.basic.instruction") + " " + (itemCount + 1),
		    false);
	}
	row[columnCount++] = new ExcelCell(getMessage("label.total"), false);
	row[columnCount++] = new ExcelCell(getMessage("label.total") + " %", false);
	rowList.add(row);

	row = new ExcelCell[numberOfItems + 1];
	columnCount = 0;
	row[columnCount++] = new ExcelCell(getMessage("label.correct.answer"), false);
	for (ScratchieItem item : items) {

	    // find out the correct answer's sequential letter - A,B,C...
	    String correctAnswerLetter = "";
	    int answerCount = 1;
	    for (ScratchieAnswer answer : (Set<ScratchieAnswer>) item.getAnswers()) {
		if (answer.isCorrect()) {
		    correctAnswerLetter = String.valueOf((char) ((answerCount + 'A') - 1));
		    break;
		}
		answerCount++;
	    }
	    row[columnCount++] = new ExcelCell(correctAnswerLetter, false);
	}
	rowList.add(row);

	row = new ExcelCell[1];
	row[0] = new ExcelCell(getMessage("monitoring.label.group"), false);
	rowList.add(row);

	int groupCount = 1;
	int[] percentages = new int[summaryByTeam.size()];
	for (GroupSummary summary : summaryByTeam) {

	    row = new ExcelCell[numberOfItems + 3];
	    columnCount = 0;
	    row[columnCount++] = new ExcelCell(summary.getSessionName(), false);

	    int numberOfFirstChoiceEvents = 0;
	    for (ScratchieItem item : summary.getItems()) {

		IndexedColors color = null;
		if (item.getCorrectAnswer().equals(Boolean.TRUE.toString())) {
		    color = IndexedColors.GREEN;
		    numberOfFirstChoiceEvents++;
		}
		row[columnCount++] = new ExcelCell(item.getFirstChoiceAnswerLetter(), color);
	    }
	    row[columnCount++] = new ExcelCell(new Integer(numberOfFirstChoiceEvents), false);
	    int percentage = (numberOfItems == 0) ? 0 : (100 * numberOfFirstChoiceEvents) / numberOfItems;
	    row[columnCount++] = new ExcelCell(percentage + "%", false);
	    rowList.add(row);
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
	int avgMean = sum / percentagesLength;
	row = new ExcelCell[numberOfItems + 3];
	row[0] = new ExcelCell(getMessage("label.avg.mean"), false);
	row[numberOfItems + 2] = new ExcelCell(avgMean + "%", false);
	rowList.add(row);

	// median
	int median;
	int middle = percentages.length / 2;
	if ((percentages.length % 2) == 1) {
	    median = percentages[middle];
	} else {
	    median = (int) ((percentages[middle - 1] + percentages[middle]) / 2.0);
	}
	row = new ExcelCell[numberOfItems + 3];
	row[0] = new ExcelCell(getMessage("label.median"), false);
	row[numberOfItems + 2] = new ExcelCell(median, false);
	rowList.add(row);

	row = new ExcelCell[1];
	row[0] = new ExcelCell(getMessage("label.legend"), false);
	rowList.add(row);

	row = new ExcelCell[1];
	row[0] = new ExcelCell(getMessage("label.denotes.correct.answer"), IndexedColors.GREEN);
	rowList.add(row);

	ExcelCell[][] secondPageData = rowList.toArray(new ExcelCell[][] {});
	dataToExport.put(getMessage("label.report.by.team.tra"), secondPageData);

	// ======================================================= Research and Analysis page
	// =======================================

	// all rows
	rowList = new LinkedList<ExcelCell[]>();

	// Caption
	row = new ExcelCell[2];
	row[0] = new ExcelCell(getMessage("label.scratchie.report"), true);
	rowList.add(row);
	rowList.add(ScratchieServiceImpl.EMPTY_ROW);
	rowList.add(ScratchieServiceImpl.EMPTY_ROW);

	// Overall Summary by Team --------------------------------------------------
	row = new ExcelCell[2];
	row[0] = new ExcelCell(getMessage("label.overall.summary.by.team"), true);
	rowList.add(row);

	row = new ExcelCell[(numberOfItems * 3) + 1];
	columnCount = 1;
	for (int itemCount = 0; itemCount < numberOfItems; itemCount++) {
	    row[columnCount] = new ExcelCell(getMessage("label.for.question", new Object[] { itemCount + 1 }), false);
	    columnCount += 3;
	}
	rowList.add(row);

	row = new ExcelCell[(numberOfItems * 3) + 1];
	columnCount = 1;
	for (int itemCount = 0; itemCount < numberOfItems; itemCount++) {
	    row[columnCount++] = new ExcelCell(getMessage("label.first.choice"), IndexedColors.BLUE);
	    row[columnCount++] = new ExcelCell(getMessage("label.attempts"), IndexedColors.BLUE);
	    row[columnCount++] = new ExcelCell(getMessage("label.mark"), IndexedColors.BLUE);
	}
	rowList.add(row);

	for (GroupSummary summary : summaryByTeam) {
	    row = new ExcelCell[(numberOfItems * 3) + 1];
	    columnCount = 0;

	    row[columnCount++] = new ExcelCell(summary.getSessionName(), false);

	    for (ScratchieItem item : summary.getItems()) {
		int attempts = item.getUserAttempts();

		String isFirstChoice;
		IndexedColors color;
		if (item.getCorrectAnswer().equals(Boolean.TRUE.toString())) {
		    isFirstChoice = getMessage("label.correct");
		    color = IndexedColors.GREEN;
		} else if (attempts == 0) {
		    isFirstChoice = null;
		    color = null;
		} else {
		    isFirstChoice = getMessage("label.incorrect");
		    color = IndexedColors.RED;
		}
		row[columnCount++] = new ExcelCell(isFirstChoice, color);
		row[columnCount++] = new ExcelCell(new Long(attempts), color);
		Long mark = (item.getUserMark() == -1) ? null : new Long(item.getUserMark());
		row[columnCount++] = new ExcelCell(mark, false);
	    }
	    rowList.add(row);
	}
	rowList.add(ScratchieServiceImpl.EMPTY_ROW);
	rowList.add(ScratchieServiceImpl.EMPTY_ROW);
	rowList.add(ScratchieServiceImpl.EMPTY_ROW);

	// Overall Summary By Individual Student in each Team----------------------------------------
	row = new ExcelCell[2];
	row[0] = new ExcelCell(getMessage("label.overall.summary.by.individual.student"), true);
	rowList.add(row);
	rowList.add(ScratchieServiceImpl.EMPTY_ROW);

	row = new ExcelCell[4];
	row[1] = new ExcelCell(getMessage("label.attempts"), false);
	row[2] = new ExcelCell(getMessage("label.mark"), false);
	row[3] = new ExcelCell(getMessage("label.group"), false);
	rowList.add(row);

	List<GroupSummary> summaryList = getMonitoringSummary(contentId, false);
	for (GroupSummary summary : summaryList) {
	    for (ScratchieUser user : summary.getUsers()) {
		row = new ExcelCell[4];
		row[0] = new ExcelCell(user.getFirstName() + " " + user.getLastName(), false);
		row[1] = new ExcelCell(new Long(summary.getTotalAttempts()), false);
		Long mark = (summary.getTotalAttempts() == 0) ? null : new Long(summary.getMark());
		row[2] = new ExcelCell(mark, false);
		row[3] = new ExcelCell(summary.getSessionName(), false);
		rowList.add(row);
	    }
	}
	rowList.add(ScratchieServiceImpl.EMPTY_ROW);
	rowList.add(ScratchieServiceImpl.EMPTY_ROW);

	// Question Reports-----------------------------------------------------------------
	row = new ExcelCell[1];
	row[0] = new ExcelCell(getMessage("label.question.reports"), true);
	rowList.add(row);
	rowList.add(ScratchieServiceImpl.EMPTY_ROW);

	SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

	for (ScratchieItem item : items) {
	    List<GroupSummary> itemSummary = getQuestionSummary(contentId, item.getUid());

	    row = new ExcelCell[1];
	    row[0] = new ExcelCell(getMessage("label.question.semicolon", new Object[] { item.getTitle() }), true);
	    rowList.add(row);

	    row = new ExcelCell[1];
	    row[0] = new ExcelCell(removeHtmlMarkup(item.getDescription()), true);
	    rowList.add(row);
	    rowList.add(ScratchieServiceImpl.EMPTY_ROW);
	    rowList.add(ScratchieServiceImpl.EMPTY_ROW);

	    // show all team summary in case there is more than 1 group
	    if (summaryList.size() > 1) {
		row = new ExcelCell[1];
		row[0] = new ExcelCell(getMessage("label.all.teams.summary"), true);
		rowList.add(row);

		GroupSummary allTeamSummary = itemSummary.get(0);
		Collection<ScratchieAnswer> answers = allTeamSummary.getAnswers();

		row = new ExcelCell[1 + answers.size()];
		for (int i = 0; i < answers.size(); i++) {
		    row[i + 1] = new ExcelCell((long) i + 1, IndexedColors.YELLOW);
		}
		rowList.add(row);

		for (ScratchieAnswer answer : answers) {
		    row = new ExcelCell[1 + answers.size()];
		    String answerTitle = removeHtmlMarkup(answer.getDescription());
		    IndexedColors color = null;
		    if (answer.isCorrect()) {
			answerTitle += "(" + getMessage("label.monitoring.item.summary.correct") + ")";
			color = IndexedColors.GREEN;
		    }
		    columnCount = 0;
		    row[columnCount++] = new ExcelCell(answerTitle, color);

		    for (int numberAttempts : answer.getAttempts()) {
			row[columnCount++] = new ExcelCell(new Long(numberAttempts), false);
		    }
		    rowList.add(row);
		}
		rowList.add(ScratchieServiceImpl.EMPTY_ROW);
		rowList.add(ScratchieServiceImpl.EMPTY_ROW);
	    }

	    row = new ExcelCell[1];
	    row[0] = new ExcelCell(getMessage("label.breakdown.by.team"), true);
	    rowList.add(row);
	    for (GroupSummary groupSummary : itemSummary) {
		if (groupSummary.getSessionId().equals(0L)) {
		    continue;
		}

		Collection<ScratchieAnswer> answers = groupSummary.getAnswers();

		row = new ExcelCell[1];
		row[0] = new ExcelCell(groupSummary.getSessionName(), true);
		rowList.add(row);

		row = new ExcelCell[1 + answers.size()];
		for (int i = 0; i < answers.size(); i++) {
		    row[i + 1] = new ExcelCell(new Long(i + 1), false);
		}
		rowList.add(row);

		for (ScratchieAnswer answer : answers) {
		    row = new ExcelCell[1 + answers.size()];
		    String answerTitle = removeHtmlMarkup(answer.getDescription());
		    if (answer.isCorrect()) {
			answerTitle += "(" + getMessage("label.monitoring.item.summary.correct") + ")";
		    }
		    columnCount = 0;
		    row[columnCount++] = new ExcelCell(answerTitle, false);

		    for (int numberAttempts : answer.getAttempts()) {
			row[columnCount++] = new ExcelCell(new Long(numberAttempts), false);
		    }
		    rowList.add(row);
		}

	    }
	    rowList.add(ScratchieServiceImpl.EMPTY_ROW);
	    rowList.add(ScratchieServiceImpl.EMPTY_ROW);
	}

	// Breakdown By Student with Timing----------------------------------------------------

	row = new ExcelCell[1];
	row[0] = new ExcelCell(getMessage("label.breakdown.by.student.with.timing"), true);
	rowList.add(row);
	rowList.add(ScratchieServiceImpl.EMPTY_ROW);

	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(scratchie.getContentId());
	for (ScratchieSession session : sessionList) {

	    ScratchieUser groupLeader = session.getGroupLeader();
	    Long sessionId = session.getSessionId();

	    if (groupLeader != null) {

		row = new ExcelCell[5];
		row[0] = new ExcelCell(groupLeader.getFirstName() + " " + groupLeader.getLastName(), true);
		row[1] = new ExcelCell(getMessage("label.attempts") + ":", false);
		Long attempts = (long) scratchieAnswerVisitDao.getLogCountTotal(sessionId);
		row[2] = new ExcelCell(attempts, false);
		row[3] = new ExcelCell(getMessage("label.mark") + ":", false);
		row[4] = new ExcelCell(new Long(session.getMark()), false);
		rowList.add(row);

		row = new ExcelCell[1];
		row[0] = new ExcelCell(getMessage("label.team.leader") + session.getSessionName(), false);
		rowList.add(row);

		for (ScratchieItem item : items) {
		    row = new ExcelCell[1];
		    row[0] = new ExcelCell(getMessage("label.question.semicolon", new Object[] { item.getTitle() }),
			    false);
		    rowList.add(row);
		    rowList.add(ScratchieServiceImpl.EMPTY_ROW);

		    int i = 1;
		    List<ScratchieAnswerVisitLog> logs = scratchieAnswerVisitDao.getLogsBySessionAndItem(sessionId,
			    item.getUid());
		    for (ScratchieAnswerVisitLog log : logs) {
			row = new ExcelCell[4];
			row[0] = new ExcelCell(new Long(i++), false);
			String answerDescr = removeHtmlMarkup(log.getScratchieAnswer().getDescription());
			row[1] = new ExcelCell(answerDescr, false);
			row[3] = new ExcelCell(fullDateFormat.format(log.getAccessDate()), false);
			rowList.add(row);
		    }
		    rowList.add(ScratchieServiceImpl.EMPTY_ROW);
		}

	    }
	}

	ExcelCell[][] thirdPageData = rowList.toArray(new ExcelCell[][] {});
	dataToExport.put(getMessage("label.research.analysis"), thirdPageData);

	// ======================================================= For_XLS_export(SPSS analysis) page
	// =======================================

	rowList = new LinkedList<ExcelCell[]>();

	// Table header------------------------------------

	int maxAnswers = 0;
	for (ScratchieItem item : items) {
	    if (item.getAnswers().size() > maxAnswers) {
		maxAnswers = item.getAnswers().size();
	    }
	}

	row = new ExcelCell[10 + (maxAnswers * 2)];
	columnCount = 0;
	row[columnCount++] = new ExcelCell(getMessage("label.student.name"), true);
	row[columnCount++] = new ExcelCell(getMessage("label.student.username"), true);
	row[columnCount++] = new ExcelCell(getMessage("label.team"), true);
	row[columnCount++] = new ExcelCell(getMessage("label.question.number"), true);
	row[columnCount++] = new ExcelCell(getMessage("label.question"), true);
	row[columnCount++] = new ExcelCell(getMessage("label.correct.answer"), true);
	row[columnCount++] = new ExcelCell(getMessage("label.first.choice.accuracy"), true);
	row[columnCount++] = new ExcelCell(getMessage("label.number.of.attempts"), true);
	row[columnCount++] = new ExcelCell(getMessage("label.mark.awarded"), true);
	for (int i = 0; i < maxAnswers; i++) {
	    row[columnCount++] = new ExcelCell(getMessage("label." + (i + 1) + ".answer.selected"), true);
	}
	row[columnCount++] = new ExcelCell(getMessage("label.date"), true);
	for (int i = 0; i < maxAnswers; i++) {
	    row[columnCount++] = new ExcelCell(getMessage("label.time.of.selection." + (i + 1)), true);
	}
	rowList.add(row);

	// Table content------------------------------------

	for (GroupSummary summary : summaryByTeam) {
	    Long sessionId = summary.getSessionId();

	    ScratchieSession session = getScratchieSessionBySessionId(sessionId);
	    ScratchieUser groupLeader = session.getGroupLeader();
	    List<ScratchieUser> users = scratchieUserDao.getBySessionID(sessionId);

	    for (ScratchieUser user : users) {

		int questionCount = 1;
		for (ScratchieItem item : summary.getItems()) {

		    row = new ExcelCell[10 + (maxAnswers * 2)];
		    columnCount = 0;
		    // learner name
		    row[columnCount++] = new ExcelCell(user.getFirstName() + " " + user.getLastName(), false);
		    // username
		    row[columnCount++] = new ExcelCell(user.getLoginName(), false);
		    // group name
		    row[columnCount++] = new ExcelCell(summary.getSessionName(), false);
		    // question number
		    row[columnCount++] = new ExcelCell(new Long(questionCount++), false);
		    // question title
		    row[columnCount++] = new ExcelCell(item.getTitle(), false);

		    // correct answer
		    String correctAnswer = "";
		    Set<ScratchieAnswer> answers = item.getAnswers();
		    for (ScratchieAnswer answer : answers) {
			if (answer.isCorrect()) {
			    correctAnswer = removeHtmlMarkup(answer.getDescription());
			}
		    }
		    row[columnCount++] = new ExcelCell(correctAnswer, false);

		    // isFirstChoice
		    int attempts = item.getUserAttempts();
		    String isFirstChoice;
		    if (item.getCorrectAnswer().equals(Boolean.TRUE.toString())) {
			isFirstChoice = getMessage("label.correct");
		    } else if (attempts == 0) {
			isFirstChoice = null;
		    } else {
			isFirstChoice = getMessage("label.incorrect");
		    }
		    row[columnCount++] = new ExcelCell(isFirstChoice, false);
		    // attempts
		    row[columnCount++] = new ExcelCell(new Long(attempts), false);
		    // mark
		    Object mark = (item.getUserMark() == -1) ? "" : new Long(item.getUserMark());
		    row[columnCount++] = new ExcelCell(mark, false);

		    // Answers selected
		    List<ScratchieAnswerVisitLog> logs = scratchieAnswerVisitDao.getLogsBySessionAndItem(sessionId,
			    item.getUid());
		    if (logs == null) {
			logs = new ArrayList<ScratchieAnswerVisitLog>();
		    }

		    for (ScratchieAnswerVisitLog log : logs) {
			String answer = removeHtmlMarkup(log.getScratchieAnswer().getDescription());
			row[columnCount++] = new ExcelCell(answer, false);
		    }
		    for (int i = logs.size(); i < item.getAnswers().size(); i++) {
			row[columnCount++] = new ExcelCell(getMessage("label.none"), false);
		    }
		    for (int i = answers.size(); i < maxAnswers; i++) {
			row[columnCount++] = new ExcelCell("", false);
		    }

		    // Date
		    String dateStr = "";
		    if (logs.size() > 0) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
			Date accessDate = logs.iterator().next().getAccessDate();
			dateStr = dateFormat.format(accessDate);
		    }
		    row[columnCount++] = new ExcelCell(dateStr, false);

		    // time of selection
		    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		    for (ScratchieAnswerVisitLog log : logs) {
			Date accessDate = log.getAccessDate();
			String timeStr = timeFormat.format(accessDate);
			row[columnCount++] = new ExcelCell(timeStr, false);
		    }
		    for (int i = logs.size(); i < maxAnswers; i++) {
			row[columnCount++] = new ExcelCell("", false);
		    }

		    rowList.add(row);
		}

	    }

	}

	ExcelCell[][] fourthPageData = rowList.toArray(new ExcelCell[][] {});
	dataToExport.put(getMessage("label.spss.analysis"), fourthPageData);

	// ======================================================= Burning questions page
	// =======================================

	if (scratchie.isBurningQuestionsEnabled()) {
	    rowList = new LinkedList<ExcelCell[]>();

	    row = new ExcelCell[1];
	    row[0] = new ExcelCell(getMessage("label.burning.questions"), true);
	    rowList.add(row);
	    rowList.add(ScratchieServiceImpl.EMPTY_ROW);

	    row = new ExcelCell[3];
	    row[0] = new ExcelCell(getMessage("label.monitoring.summary.user.name"), IndexedColors.BLUE);
	    row[1] = new ExcelCell(getMessage("label.burning.questions"), IndexedColors.BLUE);
	    row[2] = new ExcelCell(getMessage("label.count"), IndexedColors.BLUE);
	    rowList.add(row);

	    List<BurningQuestionItemDTO> burningQuestionItemDtos = getBurningQuestionDtos(scratchie, null);
	    for (BurningQuestionItemDTO burningQuestionItemDto : burningQuestionItemDtos) {
		ScratchieItem item = burningQuestionItemDto.getScratchieItem();
		row = new ExcelCell[1];
		row[0] = new ExcelCell(item.getTitle(), false);
		rowList.add(row);

		List<BurningQuestionDTO> burningQuestionDtos = burningQuestionItemDto.getBurningQuestionDtos();
		for (BurningQuestionDTO burningQuestionDto : burningQuestionDtos) {
		    String burningQuestion = burningQuestionDto.getBurningQuestion().getQuestion();
		    row = new ExcelCell[3];
		    row[0] = new ExcelCell(burningQuestionDto.getSessionName(), false);
		    row[1] = new ExcelCell(burningQuestion, false);
		    row[2] = new ExcelCell(burningQuestionDto.getLikeCount(), false);
		    rowList.add(row);
		}
		rowList.add(ScratchieServiceImpl.EMPTY_ROW);
	    }

	    ExcelCell[][] fifthPageData = rowList.toArray(new ExcelCell[][] {});
	    dataToExport.put(getMessage("label.burning.questions"), fifthPageData);
	}

	return dataToExport;
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
	List<GroupSummary> groupSummaries = new ArrayList<GroupSummary>();

	String presetMarks = getConfigItem(ScratchieConfigItem.KEY_PRESET_MARKS).getConfigValue();

	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(scratchie.getContentId());
	for (ScratchieSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    // one new summary for one session.
	    GroupSummary groupSummary = new GroupSummary(session);
	    ArrayList<ScratchieItem> items = new ArrayList<ScratchieItem>();

	    ScratchieUser groupLeader = session.getGroupLeader();

	    List<ScratchieAnswerVisitLog> answerLogs = scratchieAnswerVisitDao.getLogsBySession(sessionId);

	    for (ScratchieItem item : sortedItems) {
		ScratchieItem newItem = new ScratchieItem();
		int numberOfAttempts = 0;
		int mark = -1;
		boolean isFirstChoice = false;
		String firstChoiceAnswerLetter = "";

		// if there is no group leader don't calculate numbers - there aren't any
		if (groupLeader != null) {

		    numberOfAttempts = calculateItemAttempts(answerLogs, item);

		    // for displaying purposes if there is no attemps we assign -1 which will be shown as "-"
		    mark = (numberOfAttempts == 0) ? -1 : getUserMarkPerItem(scratchie, item, answerLogs, presetMarks);

		    isFirstChoice = (numberOfAttempts == 1) && isItemUnraveled(item, answerLogs);

		    if (numberOfAttempts > 0) {
			ScratchieAnswer firstChoiceAnswer = scratchieAnswerVisitDao
				.getFirstScratchedAnswerBySessionAndItem(sessionId, item.getUid());

			// find out the correct answer's sequential letter - A,B,C...
			int answerCount = 1;
			for (ScratchieAnswer answer : (Set<ScratchieAnswer>) item.getAnswers()) {
			    if (answer.getUid().equals(firstChoiceAnswer.getUid())) {
				firstChoiceAnswerLetter = String.valueOf((char) ((answerCount + 'A') - 1));
				break;
			    }
			    answerCount++;
			}
		    }

		}

		newItem.setUid(item.getUid());
		newItem.setTitle(item.getTitle());
		newItem.setAnswers(item.getAnswers());
		newItem.setUserAttempts(numberOfAttempts);
		newItem.setUserMark(mark);
		newItem.setCorrectAnswer("" + isFirstChoice);
		newItem.setFirstChoiceAnswerLetter(firstChoiceAnswerLetter);

		items.add(newItem);
	    }

	    groupSummary.setItems(items);
	    groupSummaries.add(groupSummary);
	}

	return groupSummaries;
    }

    private Scratchie getDefaultScratchie() throws ScratchieApplicationException {
	Long defaultScratchieId = getToolDefaultContentIdBySignature(ScratchieConstants.TOOL_SIGNATURE);
	Scratchie defaultScratchie = getScratchieByContentId(defaultScratchieId);
	if (defaultScratchie == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    ScratchieServiceImpl.log.error(error);
	    throw new ScratchieApplicationException(error);
	}

	return defaultScratchie;
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws ScratchieApplicationException {
	Long contentId = null;
	contentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    ScratchieServiceImpl.log.error(error);
	    throw new ScratchieApplicationException(error);
	}
	return contentId;
    }

    @Override
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
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

    public void setScratchieDao(ScratchieDAO scratchieDao) {
	this.scratchieDao = scratchieDao;
    }

    public void setScratchieItemDao(ScratchieItemDAO scratchieAnswerDao) {
	this.scratchieItemDao = scratchieAnswerDao;
    }

    public void setScratchieSessionDao(ScratchieSessionDAO scratchieSessionDao) {
	this.scratchieSessionDao = scratchieSessionDao;
    }

    public void setScratchieToolContentHandler(ScratchieToolContentHandler scratchieToolContentHandler) {
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
		user.setUserId(new Long(newUserUid.longValue()));
		user.setScratchie(toolContentObj);
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
	Set items = toContent.getScratchieItems();
	if (items != null) {
	    Iterator iter = items.iterator();
	    while (iter.hasNext()) {
		ScratchieItem item = (ScratchieItem) iter.next();
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
	    ScratchieServiceImpl.log.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
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
	if (ScratchieServiceImpl.log.isDebugEnabled()) {
	    ScratchieServiceImpl.log
		    .debug("Removing Scratchie content for user ID " + userId + " and toolContentId " + toolContentId);
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

		gradebookService.updateActivityMark(null, null, userId, session.getSessionId(), false);
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
	    ScratchieServiceImpl.log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    ScratchieServiceImpl.log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}
	ScratchieSession session = scratchieSessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(ScratchieConstants.COMPLETED);
	    scratchieSessionDao.saveObject(session);
	} else {
	    ScratchieServiceImpl.log.error("Fail to leave tool Session.Could not find shared scratchie "
		    + "session by given session id: " + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find shared scratchie session by given session id: " + toolSessionId);
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
	return new ArrayList<ToolOutput>();
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	Long userId = user.getUserId().longValue();

	ScratchieSession session = getScratchieSessionBySessionId(toolSessionId);
	if ((session == null) || (session.getScratchie() == null)) {
	    return;
	}
	Scratchie scratchie = session.getScratchie();

	// as long as leader aware feature is always ON - copy answers from leader to non-leader user

	ScratchieUser scratchieUser = scratchieUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	// create user if he hasn't accessed this activity yet
	if (scratchieUser == null) {
	    scratchieUser = new ScratchieUser(user.getUserDTO(), session);
	    createUser(scratchieUser);
	}

	// as long as there is no individual results in Scratchie tool (but rather one for entire group) there is no
	// need to copyAnswersFromLeader()
    }

    /* =================================================================================== */

    public IExportToolContentService getExportContentService() {
	return exportContentService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    public void setGradebookService(IGradebookService gradebookService) {
	this.gradebookService = gradebookService;
    }

    public void setAuditService(IAuditService auditService) {
	this.auditService = auditService;
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

    // ****************** REST methods *************************

    /**
     * Rest call to create a new Scratchie content. Required fields in toolContentJSON: "title", "instructions",
     * "questions". The questions entry should be JSONArray containing JSON objects, which in turn must contain
     * "questionText", "displayOrder" (Integer) and a JSONArray "answers". The answers entry should be JSONArray
     * containing JSON objects, which in turn must contain "answerText", "displayOrder" (Integer), "correct" (Boolean).
     */
    @SuppressWarnings("unchecked")
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, JSONObject toolContentJSON)
	    throws JSONException {

	Scratchie scratchie = new Scratchie();
	Date updateDate = new Date();

	scratchie.setCreated(updateDate);
	scratchie.setUpdated(updateDate);
	scratchie.setDefineLater(false);

	scratchie.setContentId(toolContentID);
	scratchie.setTitle(toolContentJSON.getString(RestTags.TITLE));
	scratchie.setInstructions(toolContentJSON.getString(RestTags.INSTRUCTIONS));

	scratchie.setBurningQuestionsEnabled(JsonUtil.opt(toolContentJSON, "burningQuestionsEnabled", false));
	scratchie.setTimeLimit(JsonUtil.opt(toolContentJSON, "timeLimit", 0));
	scratchie.setExtraPoint(JsonUtil.opt(toolContentJSON, "extraPoint", false));
	scratchie.setReflectOnActivity(JsonUtil.opt(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	scratchie.setReflectInstructions(JsonUtil.opt(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS, (String) null));

	// Scratchie Items
	Set<ScratchieItem> newItems = new LinkedHashSet<ScratchieItem>();

	JSONArray questions = toolContentJSON.getJSONArray(RestTags.QUESTIONS);
	for (int i = 0; i < questions.length(); i++) {
	    JSONObject questionData = (JSONObject) questions.get(i);

	    ScratchieItem item = new ScratchieItem();
	    item.setCreateDate(updateDate);
	    item.setCreateByAuthor(true);
	    item.setOrderId(questionData.getInt(RestTags.DISPLAY_ORDER));
	    item.setTitle(questionData.getString(RestTags.QUESTION_TITLE));
	    item.setDescription(questionData.getString(RestTags.QUESTION_TEXT));
	    newItems.add(item);

	    // set options
	    Set<ScratchieAnswer> newAnswers = new LinkedHashSet<ScratchieAnswer>();

	    JSONArray answersData = questionData.getJSONArray(RestTags.ANSWERS);
	    for (int j = 0; j < answersData.length(); j++) {
		JSONObject answerData = (JSONObject) answersData.get(j);
		ScratchieAnswer answer = new ScratchieAnswer();
		// Removes redundant new line characters from options left by CKEditor (otherwise it will break
		// Javascript in monitor). Copied from AuthoringAction.
		String answerDescription = answerData.getString(RestTags.ANSWER_TEXT);
		answer.setDescription(answerDescription != null ? answerDescription.replaceAll("[\n\r\f]", "") : "");
		answer.setCorrect(answerData.getBoolean(RestTags.CORRECT));
		answer.setOrderId(answerData.getInt(RestTags.DISPLAY_ORDER));
		answer.setScratchieItem(item);
		newAnswers.add(answer);
	    }

	    item.setAnswers(newAnswers);

	}

	scratchie.setScratchieItems(newItems);
	saveOrUpdateScratchie(scratchie);

    }

}
