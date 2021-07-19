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

package org.lamsfoundation.lams.tool.dokumaran.service;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.etherpad.EtherpadException;
import org.lamsfoundation.lams.etherpad.service.IEtherpadService;
import org.lamsfoundation.lams.etherpad.util.EtherpadUtil;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.model.ToolActivityRatingCriteria;
import org.lamsfoundation.lams.rating.service.IRatingService;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.dokumaran.DokumaranConstants;
import org.lamsfoundation.lams.tool.dokumaran.dao.DokumaranDAO;
import org.lamsfoundation.lams.tool.dokumaran.dao.DokumaranSessionDAO;
import org.lamsfoundation.lams.tool.dokumaran.dao.DokumaranUserDAO;
import org.lamsfoundation.lams.tool.dokumaran.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.dokumaran.dto.SessionDTO;
import org.lamsfoundation.lams.tool.dokumaran.model.Dokumaran;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranSession;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranUser;
import org.lamsfoundation.lams.tool.dokumaran.web.controller.LearningWebsocketServer;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.gjerull.etherpad.client.EPLiteClient;

/**
 * @author Dapeng.Ni
 */
public class DokumaranService implements IDokumaranService, ToolContentManager, ToolSessionManager, ToolRestManager {
    private static Logger log = Logger.getLogger(DokumaranService.class.getName());

    private DokumaranDAO dokumaranDao;

    private DokumaranUserDAO dokumaranUserDao;

    private DokumaranSessionDAO dokumaranSessionDao;

    // tool service
    private IToolContentHandler dokumaranToolContentHandler;

    private MessageService messageService;

    // system services

    private ILamsToolService toolService;

    private IUserManagementService userManagementService;

    private ILearnerService learnerService;

    private ILessonService lessonService;

    private IRatingService ratingService;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private IEtherpadService etherpadService;

    private DokumaranOutputFactory dokumaranOutputFactory;

    // *******************************************************************************
    // Service method
    // *******************************************************************************

    @Override
    public Dokumaran getDokumaranByContentId(Long contentId) {
	Dokumaran rs = dokumaranDao.getByContentId(contentId);
	return rs;
    }

    @Override
    public Dokumaran getDefaultContent(Long contentId) throws DokumaranApplicationException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    DokumaranService.log.error(error);
	    throw new DokumaranApplicationException(error);
	}

	Dokumaran defaultContent = getDefaultDokumaran();
	// save default content by given ID.
	Dokumaran content = new Dokumaran();
	content = Dokumaran.newInstance(defaultContent, contentId);
	return content;
    }

    @Override
    public List<DokumaranUser> checkLeaderSelectToolForSessionLeader(DokumaranUser user, Long toolSessionId,
	    boolean isFirstTimeAccess) {
	if ((user == null) || (toolSessionId == null)) {
	    return null;
	}

	DokumaranSession session = getDokumaranSessionBySessionId(toolSessionId);
	Dokumaran dokumaran = session.getDokumaran();
	List<DokumaranUser> leaders = new ArrayList<>();
	if (dokumaran.isAllowMultipleLeaders() && !isGroupedActivity(dokumaran.getContentId())) {

	    List<DokumaranUser> createdLeaders = dokumaranUserDao.getLeadersBySessionId(toolSessionId);
	    leaders.addAll(createdLeaders);

	    // check leader select tool for a leader only in case Dokumaran activity is accessed by this user for the first
	    // time. We need to add this check in order to reduce amount of queries to Leader Selection tool.
	    if (isFirstTimeAccess) {

		//get all leaders from Leader Selection tool
		Set<Long> allLeaderUserIds = toolService.getAllLeaderUserIds(toolSessionId,
			user.getUserId().intValue());
		for (Long leaderUserId : allLeaderUserIds) {
		    //in case current user is leader - store his leader status
		    if (leaderUserId.equals(user.getUserId())) {
			user.setLeader(true);
			saveOrUpdate(user);
			leaders.add(user);
			continue;
		    }

		    //check if such leader is already created inside doKumaran
		    boolean isLeaderCreated = false;
		    for (DokumaranUser leader : createdLeaders) {
			if (leader.getUserId().equals(leaderUserId)) {
			    isLeaderCreated = true;
			    break;
			}
		    }

		    //if the leader is not yet created - create him
		    if (!isLeaderCreated && (getUserByIDAndSession(leaderUserId, toolSessionId) == null)) {
			log.debug("creating new user with userId: " + leaderUserId);
			User leaderDto = (User) userManagementService.findById(User.class, leaderUserId.intValue());
			DokumaranUser leader = new DokumaranUser(leaderDto.getUserDTO(), session);
			leader.setLeader(true);
			saveOrUpdate(leader);
			leaders.add(leader);
		    }
		}

	    }
	} else {
	    DokumaranUser leader = session.getGroupLeader();
	    // check leader select tool for a leader only in case Dokumaran tool doesn't know it. As otherwise it will
	    // screw up previous scratches done
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
		    session.setGroupLeader(leader);
		    dokumaranSessionDao.saveObject(session);
		}
	    }

	    if (leader != null) {
		leaders.add(leader);
	    }
	}

	return leaders;
    }

    @Override
    public boolean isUserLeader(List<DokumaranUser> leaders, Long userId) {
	for (DokumaranUser leader : leaders) {
	    if (userId.equals(leader.getUserId())) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public boolean isLeaderResponseFinalized(List<DokumaranUser> leaders) {
	for (DokumaranUser leader : leaders) {
	    if (leader.isSessionFinished()) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public boolean isLeaderResponseFinalized(Long toolSessionId) {
	DokumaranSession session = getDokumaranSessionBySessionId(toolSessionId);
	Dokumaran dokumaran = session.getDokumaran();

	boolean isLeaderResponseFinalized = false;
	if (dokumaran.isAllowMultipleLeaders() && !isGroupedActivity(dokumaran.getContentId())) {

	    List<DokumaranUser> leaders = dokumaranUserDao.getLeadersBySessionId(toolSessionId);
	    for (DokumaranUser leader : leaders) {
		if (leader.isSessionFinished()) {
		    isLeaderResponseFinalized = true;
		    break;
		}
	    }

	} else {
	    DokumaranUser leader = session.getGroupLeader();
	    isLeaderResponseFinalized = (leader != null) && leader.isSessionFinished();
	}

	return isLeaderResponseFinalized;
    }

    @Override
    public void changeLeaderForGroup(long toolSessionId, long leaderUserId) {
	DokumaranSession session = getDokumaranSessionBySessionId(toolSessionId);
	if (DokumaranConstants.COMPLETED == session.getStatus()) {
	    throw new InvalidParameterException("Attempting to assing a new leader with user ID " + leaderUserId
		    + " to a finished session wtih ID " + toolSessionId);
	}

	DokumaranUser existingLeader = session.getGroupLeader();
	if (existingLeader == null || existingLeader.getUserId().equals(leaderUserId)) {
	    return;
	}

	DokumaranUser newLeader = getUserByIDAndSession(leaderUserId, toolSessionId);
	if (newLeader == null) {
	    User user = userManagementService.getUserById(Long.valueOf(leaderUserId).intValue());
	    newLeader = new DokumaranUser(user.getUserDTO(), session);
	    saveOrUpdate(newLeader);

	    if (log.isDebugEnabled()) {
		log.debug("Created user with ID " + leaderUserId + " to become a new leader for session with ID "
			+ toolSessionId);
	    }
	}

	session.setGroupLeader(newLeader);
	dokumaranSessionDao.update(session);

	if (log.isDebugEnabled()) {
	    log.debug("User with ID " + leaderUserId + " became a new leader for session with ID " + toolSessionId);
	}

	Set<Integer> userIds = getUsersBySession(toolSessionId).stream()
		.collect(Collectors.mapping(dokumaranUser -> dokumaranUser.getUserId().intValue(), Collectors.toSet()));

	Dokumaran dokumaran = session.getDokumaran();
	ObjectNode jsonCommand = JsonNodeFactory.instance.objectNode();
	jsonCommand.put("hookTrigger", "doku-refresh-" + dokumaran.getContentId());
	learnerService.createCommandForLearners(dokumaran.getContentId(), userIds, jsonCommand.toString());
    }

    @Override
    public LocalDateTime launchTimeLimit(long toolContentId, int userId) {
	DokumaranUser user = getLearnerByIDAndContent(Long.valueOf(userId), toolContentId);
	if (user != null) {
	    DokumaranSession session = user.getSession();
	    Dokumaran dokumaran = session.getDokumaran();

	    if (user.getTimeLimitLaunchedDate() == null) {
		LocalDateTime launchedDate = LocalDateTime.now();
		user.setTimeLimitLaunchedDate(launchedDate);
		dokumaranUserDao.saveObject(user);

		// if the user is not a leader, store his launch date, but return null so the leader's launch date is in use
		if (!dokumaran.isUseSelectLeaderToolOuput() || (session.getGroupLeader() != null
			&& session.getGroupLeader().getUserId().equals(Long.valueOf(userId)))) {
		    return launchedDate;
		}
	    }
	}
	return null;
    }

    @Override
    public boolean checkTimeLimitExceeded(Dokumaran dokumaran, int userId) {
	Long secondsLeft = LearningWebsocketServer.getSecondsLeft(dokumaran.getContentId(), userId);
	return secondsLeft != null && secondsLeft.equals(0L);
    }

    @Override
    public List<User> getPossibleIndividualTimeLimitUsers(long toolContentId, String searchString) {
	Lesson lesson = lessonService.getLessonByToolContentId(toolContentId);
	return lessonService.getLessonLearners(lesson.getLessonId(), searchString, null, null, true);
    }

    @Override
    public DokumaranUser getLearnerByIDAndContent(Long userId, Long contentId) {
	return dokumaranUserDao.getLearnerByUserIDAndContentID(userId, contentId);
    }

    @Override
    public DokumaranUser getUserByIDAndContent(Long userId, Long contentId) {
	return dokumaranUserDao.getUserByUserIDAndContentID(userId, contentId);
    }

    @Override
    public DokumaranUser getUserByIDAndSession(Long userId, Long sessionId) {
	return dokumaranUserDao.getUserByUserIDAndSessionID(userId, sessionId);
    }

    @Override
    public List<DokumaranUser> getUsersBySession(Long toolSessionId) {
	return dokumaranUserDao.getBySessionID(toolSessionId);
    }

    @Override
    public void saveOrUpdate(Object entity) {
	dokumaranDao.saveObject(entity);
    }

    @Override
    public Dokumaran getDokumaranBySessionId(Long sessionId) {
	DokumaranSession session = dokumaranSessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getDokumaran().getContentId();
	Dokumaran res = dokumaranDao.getByContentId(contentId);
	return res;
    }

    @Override
    public DokumaranSession getDokumaranSessionBySessionId(Long sessionId) {
	return dokumaranSessionDao.getSessionBySessionId(sessionId);
    }

    @Override
    public String finishToolSession(Long toolSessionId, Long userId) throws DokumaranApplicationException {
	DokumaranUser user = dokumaranUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	user.setSessionFinished(true);
	dokumaranUserDao.saveObject(user);

	// DokumaranSession session = dokumaranSessionDao.getSessionBySessionId(toolSessionId);
	// session.setStatus(DokumaranConstants.COMPLETED);
	// dokumaranSessionDao.saveObject(session);

	//finish Etherpad session. Encapsulate it in try-catch block as we don't want it to affect regular LAMS workflow.
	try {
	    EPLiteClient client = etherpadService.getClient();

	    DokumaranSession session = dokumaranSessionDao.getSessionBySessionId(toolSessionId);
	    String groupId = session.getEtherpadGroupId();

	    String userName = user.getFirstName() + " " + user.getLastName();
	    Map<String, String> map = client.createAuthorIfNotExistsFor(user.getUserId().toString(), userName);
	    String authorId = map.get("authorID");

	    // search for already existing user's session at Etherpad server
	    Map sessionsMap = client.listSessionsOfAuthor(authorId);
	    for (String sessionId : (Set<String>) sessionsMap.keySet()) {
		Map<String, String> sessessionAttributes = (Map<String, String>) sessionsMap.get(sessionId);
		String groupIdIter = sessessionAttributes.get("groupID");
		if (groupIdIter.equals(groupId)) {
		    client.deleteSession(sessionId);
		    break;
		}
	    }
	} catch (Exception e1) {
	    log.debug(e1.getMessage());
	}

	String nextUrl = null;
	try {
	    nextUrl = this.leaveToolSession(toolSessionId, userId);
	} catch (DataMissingException e) {
	    throw new DokumaranApplicationException(e);
	} catch (ToolException e) {
	    throw new DokumaranApplicationException(e);
	}
	return nextUrl;
    }

    @Override
    public List<SessionDTO> getSummary(Long contentId, Long ratingUserId) {
	// get all sessions in a dokumaran and retrieve all dokumaran items under this session
	// plus initial dokumaran items by author creating (resItemList)
	List<DokumaranSession> sessionList = dokumaranSessionDao.getByContentId(contentId);
	Dokumaran dokumaran = dokumaranDao.getByContentId(contentId);

	Map<Long, ItemRatingDTO> itemRatingDtoMap = null;
	if (dokumaran.isGalleryWalkStarted()) {
	    if (!dokumaran.isGalleryWalkReadOnly()) {
		// it should have been created on lesson create,
		// but in case Live Edit added Gallery Walk, we need to add it now, but just once
		try {
		    createGalleryWalkRatingCriterion(dokumaran.getContentId());
		} catch (Exception e) {
		    log.warn("Ignoring error while processing Dokumaran Gallery Walk criteria for tool content ID "
			    + dokumaran.getContentId());
		}
	    }

	    // Item IDs are DokumaranSession session IDs, i.e. a single Etherpad
	    Set<Long> itemIds = sessionList.stream()
		    .collect(Collectors.mapping(DokumaranSession::getSessionId, Collectors.toSet()));

	    List<ItemRatingDTO> itemRatingDtos = ratingService.getRatingCriteriaDtos(contentId, null, itemIds, false,
		    ratingUserId);
	    // Mapping of Item ID -> DTO
	    itemRatingDtoMap = itemRatingDtos.stream()
		    .collect(Collectors.toMap(ItemRatingDTO::getItemId, Function.identity()));
	}

	List<SessionDTO> groupList = new ArrayList<>();
	for (DokumaranSession session : sessionList) {
	    // one new group for one session.
	    SessionDTO group = new SessionDTO();
	    group.setSessionId(session.getSessionId());
	    group.setSessionName(session.getSessionName());
	    group.setNumberOfLearners(getUsersBySession(session.getSessionId()).size());
	    group.setSessionFinished(DokumaranConstants.COMPLETED == session.getStatus());
	    group.setPadId(session.getPadId());
	    group.setReadOnlyPadId(session.getEtherpadReadOnlyId());

	    //mark all session that has had problems with pad initializations so that they could be fixed in monitoring by a teacher
	    if (StringUtils.isEmpty(session.getEtherpadReadOnlyId())
		    || StringUtils.isEmpty(session.getEtherpadGroupId())) {
		group.setSessionFaulty(true);
	    }

	    if (itemRatingDtoMap != null) {
		group.setItemRatingDto(itemRatingDtoMap.get(session.getSessionId()));
	    }

	    groupList.add(group);
	}

	return groupList;
    }

    @Override
    public List<ReflectDTO> getReflectList(Long contentId) {
	List<ReflectDTO> reflections = new LinkedList<>();

	List<DokumaranSession> sessionList = dokumaranSessionDao.getByContentId(contentId);
	for (DokumaranSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    // get all users in this session
	    List<DokumaranUser> users = dokumaranUserDao.getBySessionID(sessionId);
	    for (DokumaranUser user : users) {

		NotebookEntry entry = getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
			DokumaranConstants.TOOL_SIGNATURE, user.getUserId().intValue());
		if (entry != null) {
		    ReflectDTO ref = new ReflectDTO(user);
		    ref.setReflect(entry.getEntry());
		    Date postedDate = (entry.getLastModified() != null) ? entry.getLastModified()
			    : entry.getCreateDate();
		    ref.setDate(postedDate);
		    reflections.add(ref);
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
    public DokumaranUser getUser(Long uid) {
	return (DokumaranUser) dokumaranUserDao.getObject(DokumaranUser.class, uid);
    }

    @Override
    public Grouping getGrouping(long toolContentId) {
	ToolActivity toolActivity = (ToolActivity) userManagementService
		.findByProperty(ToolActivity.class, "toolContentId", toolContentId).get(0);
	return toolActivity.getApplyGrouping() ? toolActivity.getGrouping() : null;
    }

    private List<RatingCriteria> createGalleryWalkRatingCriterion(long toolContentId) {
	List<RatingCriteria> criteria = ratingService.getCriteriasByToolContentId(toolContentId);

	if (criteria.size() >= 2) {
	    criteria = ratingService.getCriteriasByToolContentId(toolContentId);
	    // Dokumaran currently supports only one place for ratings.
	    // It is rating other groups' pads on results page.
	    // Criterion gets automatically created and there must be only one.
	    try {
		for (int criterionIndex = 1; criterionIndex < criteria.size(); criterionIndex++) {
		    RatingCriteria criterion = criteria.get(criterionIndex);
		    Long criterionId = criterion.getRatingCriteriaId();
		    dokumaranDao.delete(criterion);
		    log.warn("Removed a duplicate criterion ID " + criterionId + " for Dokumaran tool content ID "
			    + toolContentId);
		}
	    } catch (Exception e) {
		log.warn("Ignoring error while deleting a duplicate criterion for Dokumaran tool content ID "
			+ toolContentId + ": " + e.getMessage());
	    }
	    return criteria;
	}

	if (criteria.isEmpty()) {
	    ToolActivityRatingCriteria criterion = (ToolActivityRatingCriteria) RatingCriteria
		    .getRatingCriteriaInstance(RatingCriteria.TOOL_ACTIVITY_CRITERIA_TYPE);
	    criterion.setTitle(messageService.getMessage("label.pad.rating.title"));
	    criterion.setOrderId(1);
	    criterion.setRatingStyle(RatingCriteria.RATING_STYLE_STAR);
	    criterion.setToolContentId(toolContentId);

	    dokumaranDao.insert(criterion);
	    criteria.add(criterion);
	}
	return criteria;
    }

    @Override
    public void startGalleryWalk(long toolContentId) throws IOException {
	Dokumaran dokumaran = getDokumaranByContentId(toolContentId);
	if (!dokumaran.isGalleryWalkEnabled()) {
	    throw new IllegalArgumentException(
		    "Can not start Gallery Walk as it is not enabled for Dokumaran with tool content ID "
			    + toolContentId);
	}
	if (dokumaran.isGalleryWalkFinished()) {
	    throw new IllegalArgumentException(
		    "Can not start Gallery Walk as it is already finished for Dokumaran with tool content ID "
			    + toolContentId);
	}
	dokumaran.setGalleryWalkStarted(true);
	dokumaranDao.saveObject(dokumaran);

	sendGalleryWalkRefreshRequest(dokumaran);
    }

    @Override
    public void finishGalleryWalk(long toolContentId) throws IOException {
	Dokumaran dokumaran = getDokumaranByContentId(toolContentId);
	if (!dokumaran.isGalleryWalkEnabled()) {
	    throw new IllegalArgumentException(
		    "Can not finish Gallery Walk as it is not enabled for Dokumaran with tool content ID "
			    + toolContentId);
	}
	dokumaran.setGalleryWalkFinished(true);
	dokumaranDao.saveObject(dokumaran);

	sendGalleryWalkRefreshRequest(dokumaran);
    }

    @Override
    public void learnerReedit(long toolContentId) throws IOException {
	Dokumaran dokumaran = getDokumaranByContentId(toolContentId);
	if (!dokumaran.isGalleryWalkEnabled()) {
	    throw new IllegalArgumentException(
		    "Can not allow learners to reedit activity as Gallery Walk is not enabled for Dokumaran with tool content ID "
			    + toolContentId);
	}
	dokumaran.setGalleryWalkStarted(false);
	dokumaranDao.saveObject(dokumaran);

	sendGalleryWalkRefreshRequest(dokumaran);
    }

    private void sendGalleryWalkRefreshRequest(Dokumaran dokumaran) {
	ObjectNode jsonCommand = JsonNodeFactory.instance.objectNode();
	jsonCommand.put("hookTrigger", "doku-refresh-" + dokumaran.getContentId());
	// get all learners in this doku
	Set<Integer> userIds = dokumaranSessionDao.getByContentId(dokumaran.getContentId()).stream()
		.flatMap(session -> dokumaranUserDao.getBySessionID(session.getSessionId()).stream())
		.collect(Collectors.mapping(user -> user.getUserId().intValue(), Collectors.toSet()));

	learnerService.createCommandForLearners(dokumaran.getContentId(), userIds, jsonCommand.toString());
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************
    private Dokumaran getDefaultDokumaran() throws DokumaranApplicationException {
	Long defaultDokumaranId = getToolDefaultContentIdBySignature(DokumaranConstants.TOOL_SIGNATURE);
	Dokumaran defaultDokumaran = getDokumaranByContentId(defaultDokumaranId);
	if (defaultDokumaran == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    DokumaranService.log.error(error);
	    throw new DokumaranApplicationException(error);
	}

	return defaultDokumaran;
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws DokumaranApplicationException {
	Long contentId = toolService.getToolDefaultContentIdBySignature(toolSignature);
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    DokumaranService.log.error(error);
	    throw new DokumaranApplicationException(error);
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

    // *******************************************************************************
    // ToolContentManager, ToolSessionManager methods
    // *******************************************************************************

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Dokumaran toolContentObj = dokumaranDao.getByContentId(toolContentId);
	if (toolContentObj == null) {
	    try {
		toolContentObj = getDefaultDokumaran();
	    } catch (DokumaranApplicationException e) {
		throw new DataMissingException(e.getMessage());
	    }
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the dokumaran tool");
	}

	// set DokumaranToolContentHandler as null to avoid copy file node in repository again.
	toolContentObj = Dokumaran.newInstance(toolContentObj, toolContentId);
	try {
	    exportContentService.exportToolContent(toolContentId, toolContentObj, dokumaranToolContentHandler,
		    rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

	try {

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, dokumaranToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Dokumaran)) {
		throw new ImportToolContentException(
			"Import Share dokumaran tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Dokumaran toolContentObj = (Dokumaran) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentId(toolContentId);
	    DokumaranUser user = dokumaranUserDao.getUserByUserIDAndContentID(new Long(newUserUid.longValue()),
		    toolContentId);
	    if (user == null) {
		user = new DokumaranUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(new Long(newUserUid.longValue()));
		user.setDokumaran(toolContentObj);
	    }
	    toolContentObj.setCreatedBy(user);

	    dokumaranDao.saveObject(toolContentObj);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Dokumaran content = getDokumaranByContentId(toolContentId);
	if (content == null) {
	    try {
		content = getDefaultContent(toolContentId);
	    } catch (DokumaranApplicationException e) {
		throw new ToolException(e);
	    }
	}
	return getDokumaranOutputFactory().getToolOutputDefinitions(content, definitionType);
    }

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	if (toContentId == null) {
	    throw new ToolException("Failed to create the SharedDokumaranFiles tool seession");
	}

	Dokumaran dokumaran = null;
	if (fromContentId != null) {
	    dokumaran = dokumaranDao.getByContentId(fromContentId);
	}
	if (dokumaran == null) {
	    try {
		dokumaran = getDefaultDokumaran();
	    } catch (DokumaranApplicationException e) {
		throw new ToolException(e);
	    }
	}

	Dokumaran toContent = Dokumaran.newInstance(dokumaran, toContentId);
	dokumaranDao.saveObject(toContent);

	if (toContent.isGalleryWalkEnabled() && !toContent.isGalleryWalkReadOnly()) {
	    createGalleryWalkRatingCriterion(toContentId);
	}
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getDokumaranByContentId(toolContentId).getTitle();
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Dokumaran dokumaran = dokumaranDao.getByContentId(toolContentId);
	if (dokumaran == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	dokumaran.setDefineLater(false);
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getDokumaranByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	List<DokumaranSession> sessions = dokumaranSessionDao.getByContentId(toolContentId);
	for (DokumaranSession session : sessions) {
	    if (!dokumaranUserDao.getBySessionID(session.getSessionId()).isEmpty()) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Dokumaran dokumaran = dokumaranDao.getByContentId(toolContentId);
	if (dokumaran == null) {
	    DokumaranService.log.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (DokumaranSession session : dokumaranSessionDao.getByContentId(toolContentId)) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, DokumaranConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}
	dokumaranDao.delete(dokumaran);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (DokumaranService.log.isDebugEnabled()) {
	    DokumaranService.log
		    .debug("Removing Dokumaran content for user ID " + userId + " and toolContentId " + toolContentId);
	}

	Dokumaran dokumaran = dokumaranDao.getByContentId(toolContentId);
	if (dokumaran == null) {
	    DokumaranService.log
		    .warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	List<DokumaranSession> sessions = dokumaranSessionDao.getByContentId(toolContentId);
	for (DokumaranSession session : sessions) {
	    DokumaranUser user = dokumaranUserDao.getUserByUserIDAndSessionID(userId.longValue(),
		    session.getSessionId());
	    if (user != null) {
		NotebookEntry entry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			DokumaranConstants.TOOL_SIGNATURE, userId);
		if (entry != null) {
		    dokumaranDao.removeObject(NotebookEntry.class, entry.getUid());
		}

		dokumaranUserDao.removeObject(DokumaranUser.class, user.getUid());
	    }
	}
    }

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	DokumaranSession session = new DokumaranSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	Dokumaran dokumaran = dokumaranDao.getByContentId(toolContentId);
	session.setDokumaran(dokumaran);
	dokumaranSessionDao.saveObject(session);

	//create pad in a try-catch block so it doesn't affect session creation operation
	try {
	    createPad(dokumaran, session);
	} catch (Exception e) {
	    log.warn(e.getMessage(), e);
	}

    }

    @Override
    public void createPad(Dokumaran dokumaran, DokumaranSession session) throws DokumaranApplicationException {
	Long toolSessionId = session.getSessionId();
	Long toolContentId = dokumaran.getContentId();
	String groupIdentifier = DokumaranConstants.PREFIX_REGULAR_GROUP + toolSessionId;
	String etherpadHtml = null;

	// in case sharedPadId is present - all sessions will share the same padId
	if (dokumaran.isSharedPadEnabled()) {

	    // find existing pad in any of the sessions associated with this toolContentId
	    List<DokumaranSession> sessions = dokumaranSessionDao.getByContentId(toolContentId);
	    DokumaranSession sessionWithAlreadyCreatedPad = null;
	    for (DokumaranSession sessionIter : sessions) {
		if (StringUtils.isNotBlank(session.getEtherpadGroupId())) {
		    sessionWithAlreadyCreatedPad = sessionIter;
		    break;
		}
	    }

	    if (sessionWithAlreadyCreatedPad == null) {
		ToolSession toolSession = toolService.getToolSession(toolSessionId);
		Long lessonId = toolSession.getLesson().getLessonId();
		groupIdentifier = DokumaranConstants.PREFIX_SHARED_GROUP + dokumaran.getSharedPadId() + lessonId;

		etherpadHtml = EtherpadUtil.preparePadContent(dokumaran.getInstructions());
	    } else {
		session.setEtherpadGroupId(sessionWithAlreadyCreatedPad.getEtherpadGroupId());
		session.setEtherpadReadOnlyId(sessionWithAlreadyCreatedPad.getEtherpadReadOnlyId());
		dokumaranSessionDao.saveObject(session);
		return;
	    }
	} else {
	    etherpadHtml = EtherpadUtil.preparePadContent(dokumaran.getInstructions());
	}

	Map<String, Object> result;

	try {
	    result = etherpadService.createPad(groupIdentifier, etherpadHtml);
	} catch (EtherpadException e) {
	    throw new DokumaranApplicationException("Exception while creating an etherpad pad", e);
	}

	String groupId = (String) result.get("groupId");
	String readOnlyId = (String) result.get("readOnlyId");
	session.setEtherpadGroupId(groupId);
	session.setEtherpadReadOnlyId(readOnlyId);

	dokumaranSessionDao.saveObject(session);
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	DokumaranUser learner = getUserByIDAndSession(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	return new ToolCompletionStatus(learner.isSessionFinished() ? ToolCompletionStatus.ACTIVITY_COMPLETED
		: ToolCompletionStatus.ACTIVITY_ATTEMPTED, null, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Cookie createEtherpadCookieForLearner(DokumaranUser user, DokumaranSession session)
	    throws DokumaranApplicationException, EtherpadException {
	String groupId = session.getEtherpadGroupId();

	//don't allow sessions that has had problems with pad initializations. they could be fixed in monitoring by a teacher
	if (StringUtils.isEmpty(session.getEtherpadReadOnlyId()) || StringUtils.isEmpty(groupId)) {
	    throw new DokumaranApplicationException(
		    "This session has had problems with initialization. Please seek help from your teacher.");
	}

	EPLiteClient client = etherpadService.getClient();

	String userName = user.getFirstName() + " " + user.getLastName();
	Map<String, Object> map = client.createAuthorIfNotExistsFor(user.getUserId().toString(), userName);
	String authorId = (String) map.get("authorID");

	// search for already existing user's session at Etherpad server
	Map etherpadSessions = client.listSessionsOfAuthor(authorId);
	String etherpadSessionId = etherpadService.getExistingSessionID(authorId, groupId, etherpadSessions);
	return etherpadService.createCookie(etherpadSessionId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Cookie createEtherpadCookieForMonitor(UserDTO user, Long contentId) throws EtherpadException {

	String authorId = etherpadService.createAuthor(user.getUserID(),
		user.getFirstName() + " " + user.getLastName());

	List<DokumaranSession> sessionList = dokumaranSessionDao.getByContentId(contentId);
	if (sessionList.isEmpty()) {
	    return null;
	}

	// in case sharedPadId is present - all sessions will share the same padId - and thus show only one pad
	Dokumaran dokumaran = getDokumaranByContentId(contentId);
	if (dokumaran.isSharedPadEnabled()) {
	    sessionList = sessionList.subList(0, 1);
	}

	// find according session
	String etherpadSessionIds = "";
	EPLiteClient client = etherpadService.getClient();
	Map<String, Object> etherpadSessions = client.listSessionsOfAuthor(authorId);
	for (DokumaranSession session : sessionList) {
	    String groupId = session.getEtherpadGroupId();

	    //skip sessions that has had problems with pad initializations so that they could be fixed in monitoring by a teacher
	    if (StringUtils.isEmpty(session.getEtherpadReadOnlyId()) || StringUtils.isEmpty(groupId)) {
		continue;
	    }

	    String etherpadSessionId = etherpadService.getExistingSessionID(authorId, groupId, etherpadSessions);
	    etherpadSessionIds += StringUtils.isEmpty(etherpadSessionIds) ? etherpadSessionId : "," + etherpadSessionId;
	}

	return etherpadService.createCookie(etherpadSessionIds);
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    DokumaranService.log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    DokumaranService.log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}

	DokumaranSession session = dokumaranSessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(DokumaranConstants.COMPLETED);
	    dokumaranSessionDao.saveObject(session);
	} else {
	    DokumaranService.log.error("Fail to leave tool Session.Could not find shared dokumaran "
		    + "session by given session id: " + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find shared dokumaran session by given session id: " + toolSessionId);
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
	dokumaranSessionDao.deleteBySessionId(toolSessionId);
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getDokumaranOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getDokumaranOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
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
	DokumaranSession session = getDokumaranSessionBySessionId(toolSessionId);
	DokumaranUser groupLeader = session == null ? null : session.getGroupLeader();

	return (groupLeader != null) && userId.equals(groupLeader.getUserId());
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	// no actions required
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getDokumaranOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    // *****************************************************************************
    // set methods for Spring Bean
    // *****************************************************************************
    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public void setDokumaranDao(DokumaranDAO dokumaranDao) {
	this.dokumaranDao = dokumaranDao;
    }

    public void setDokumaranSessionDao(DokumaranSessionDAO dokumaranSessionDao) {
	this.dokumaranSessionDao = dokumaranSessionDao;
    }

    public void setDokumaranToolContentHandler(IToolContentHandler dokumaranToolContentHandler) {
	this.dokumaranToolContentHandler = dokumaranToolContentHandler;
    }

    public void setDokumaranUserDao(DokumaranUserDAO dokumaranUserDao) {
	this.dokumaranUserDao = dokumaranUserDao;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public void setLearnerService(ILearnerService learnerService) {
	this.learnerService = learnerService;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    public void setRatingService(IRatingService ratingService) {
	this.ratingService = ratingService;
    }

    public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
	this.coreNotebookService = coreNotebookService;
    }

    public void setEtherpadService(IEtherpadService etherpadService) {
	this.etherpadService = etherpadService;
    }

    public DokumaranOutputFactory getDokumaranOutputFactory() {
	return dokumaranOutputFactory;
    }

    public void setDokumaranOutputFactory(DokumaranOutputFactory dokumaranOutputFactory) {
	this.dokumaranOutputFactory = dokumaranOutputFactory;
    }

    // ****************** REST methods *************************

    /**
     * Used by the Rest calls to create content. Mandatory fields in toolContentJSON: title, instructions, dokumaran,
     * user fields firstName, lastName and loginName Dokumaran must contain a ArrayNode of ObjectNode objects, which
     * have the following mandatory fields: title, description, type. If there are instructions for a dokumaran, the
     * instructions are a ArrayNode of Strings. There should be at least one dokumaran object in the dokumaran array.
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, ObjectNode toolContentJSON) {

	Date updateDate = new Date();

	Dokumaran dokumaran = new Dokumaran();
	dokumaran.setContentId(toolContentID);
	dokumaran.setTitle(JsonUtil.optString(toolContentJSON, RestTags.TITLE));
	dokumaran.setDescription(JsonUtil.optString(toolContentJSON, RestTags.INSTRUCTIONS));
	dokumaran.setInstructions(JsonUtil.optString(toolContentJSON, "etherpadInstructions"));
	dokumaran.setCreated(updateDate);

	dokumaran.setRelativeTimeLimit(JsonUtil.optInt(toolContentJSON, "timeLimit", 0));
	dokumaran.setShowChat(JsonUtil.optBoolean(toolContentJSON, "showChat", Boolean.FALSE));
	dokumaran.setShowLineNumbers(JsonUtil.optBoolean(toolContentJSON, "showLineNumbers", Boolean.FALSE));
	dokumaran.setSharedPadId(JsonUtil.optString(toolContentJSON, "sharedPadId"));
	dokumaran.setLockWhenFinished(JsonUtil.optBoolean(toolContentJSON, RestTags.LOCK_WHEN_FINISHED, Boolean.FALSE));
	dokumaran.setReflectOnActivity(
		JsonUtil.optBoolean(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	dokumaran.setReflectInstructions(JsonUtil.optString(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS));
	dokumaran.setUseSelectLeaderToolOuput(
		JsonUtil.optBoolean(toolContentJSON, "useSelectLeaderToolOuput", Boolean.FALSE));
	dokumaran.setAllowMultipleLeaders(JsonUtil.optBoolean(toolContentJSON, "allowMultipleLeaders", Boolean.FALSE));

	dokumaran.setGalleryWalkEnabled(JsonUtil.optBoolean(toolContentJSON, "galleryWalkEnabled", false));
	if (dokumaran.isGalleryWalkEnabled()) {
	    dokumaran.setGalleryWalkReadOnly(JsonUtil.optBoolean(toolContentJSON, "galleryWalkReadOnly", false));
	    dokumaran.setGalleryWalkInstructions(JsonUtil.optString(toolContentJSON, "galleryWalkInstructions"));
	}

	dokumaran.setContentInUse(false);
	dokumaran.setDefineLater(false);

	DokumaranUser dokumaranUser = getUserByIDAndContent(userID.longValue(), toolContentID);
	if (dokumaranUser == null) {
	    dokumaranUser = new DokumaranUser();
	    dokumaranUser.setFirstName(JsonUtil.optString(toolContentJSON, "firstName"));
	    dokumaranUser.setLastName(JsonUtil.optString(toolContentJSON, "lastName"));
	    dokumaranUser.setLoginName(JsonUtil.optString(toolContentJSON, "loginName"));
	    // dokumaranUser.setDokumaran(content);
	}

	dokumaran.setCreatedBy(dokumaranUser);

	saveOrUpdate(dokumaran);

    }
}
