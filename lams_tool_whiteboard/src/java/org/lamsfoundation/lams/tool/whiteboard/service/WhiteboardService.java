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

package org.lamsfoundation.lams.tool.whiteboard.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
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
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.model.ToolActivityRatingCriteria;
import org.lamsfoundation.lams.rating.service.IRatingService;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.whiteboard.WhiteboardConstants;
import org.lamsfoundation.lams.tool.whiteboard.dao.WhiteboardConfigItemDAO;
import org.lamsfoundation.lams.tool.whiteboard.dao.WhiteboardDAO;
import org.lamsfoundation.lams.tool.whiteboard.dao.WhiteboardSessionDAO;
import org.lamsfoundation.lams.tool.whiteboard.dao.WhiteboardUserDAO;
import org.lamsfoundation.lams.tool.whiteboard.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.whiteboard.model.Whiteboard;
import org.lamsfoundation.lams.tool.whiteboard.model.WhiteboardConfigItem;
import org.lamsfoundation.lams.tool.whiteboard.model.WhiteboardSession;
import org.lamsfoundation.lams.tool.whiteboard.model.WhiteboardUser;
import org.lamsfoundation.lams.tool.whiteboard.web.controller.LearningWebsocketServer;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WhiteboardService implements IWhiteboardService, ToolContentManager, ToolSessionManager {
    private static Logger log = Logger.getLogger(WhiteboardService.class.getName());

    private WhiteboardDAO whiteboardDao;

    private WhiteboardUserDAO whiteboardUserDao;

    private WhiteboardSessionDAO whiteboardSessionDao;

    private WhiteboardConfigItemDAO whiteboardConfigItemDao;

    private IToolContentHandler whiteboardToolContentHandler;

    private MessageService messageService;

    private ILamsToolService toolService;

    private IUserManagementService userManagementService;

    private ILearnerService learnerService;

    private ILessonService lessonService;

    private IRatingService ratingService;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private WhiteboardOutputFactory whiteboardOutputFactory;

    // *******************************************************************************
    // Service method
    // *******************************************************************************

    @Override
    public Whiteboard getWhiteboardByContentId(Long contentId) {
	Whiteboard rs = whiteboardDao.getByContentId(contentId);
	return rs;
    }

    @Override
    public Whiteboard getDefaultContent(Long contentId) throws WhiteboardApplicationException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    WhiteboardService.log.error(error);
	    throw new WhiteboardApplicationException(error);
	}

	Whiteboard defaultContent = getDefaultWhiteboard();
	// save default content by given ID.
	Whiteboard content = new Whiteboard();
	content = Whiteboard.newInstance(defaultContent, contentId);
	return content;
    }

    @Override
    public WhiteboardUser checkLeaderSelectToolForSessionLeader(WhiteboardUser user, Long toolSessionId) {
	if (toolSessionId == null) {
	    return null;
	}

	WhiteboardSession session = getWhiteboardSessionBySessionId(toolSessionId);
	WhiteboardUser leader = session.getGroupLeader();
	// check leader select tool for a leader only in case Whiteboard tool doesn't know it
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
		whiteboardSessionDao.update(session);
	    }
	}

	return leader;
    }

    @Override
    public void changeLeaderForGroup(long toolSessionId, long leaderUserId) {
	WhiteboardSession session = getWhiteboardSessionBySessionId(toolSessionId);
	if (WhiteboardConstants.COMPLETED == session.getStatus()) {
	    throw new InvalidParameterException("Attempting to assing a new leader with user ID " + leaderUserId
		    + " to a finished session wtih ID " + toolSessionId);
	}

	WhiteboardUser existingLeader = session.getGroupLeader();
	if (existingLeader == null || existingLeader.getUserId().equals(leaderUserId)) {
	    return;
	}

	WhiteboardUser newLeader = getUserByIDAndSession(leaderUserId, toolSessionId);
	if (newLeader == null) {
	    User user = userManagementService.getUserById(Long.valueOf(leaderUserId).intValue());
	    newLeader = new WhiteboardUser(user.getUserDTO(), session);
	    saveOrUpdate(newLeader);

	    if (log.isDebugEnabled()) {
		log.debug("Created user with ID " + leaderUserId + " to become a new leader for session with ID "
			+ toolSessionId);
	    }
	}

	session.setGroupLeader(newLeader);
	whiteboardSessionDao.update(session);

	if (log.isDebugEnabled()) {
	    log.debug("User with ID " + leaderUserId + " became a new leader for session with ID " + toolSessionId);
	}

	Set<Integer> userIds = getUsersBySession(toolSessionId).stream().collect(
		Collectors.mapping(whiteboardUser -> whiteboardUser.getUserId().intValue(), Collectors.toSet()));

	Whiteboard whiteboard = session.getWhiteboard();
	ObjectNode jsonCommand = JsonNodeFactory.instance.objectNode();
	jsonCommand.put("hookTrigger", "whiteboard-refresh-" + whiteboard.getContentId());
	learnerService.createCommandForLearners(whiteboard.getContentId(), userIds, jsonCommand.toString());
    }

    @Override
    public LocalDateTime launchTimeLimit(long toolContentId, int userId) {
	WhiteboardUser user = getUserByIDAndContent(Long.valueOf(userId), toolContentId);
	if (user != null) {
	    LocalDateTime launchedDate = LocalDateTime.now();
	    user.setTimeLimitLaunchedDate(launchedDate);
	    whiteboardUserDao.update(user);
	    return launchedDate;
	}
	return null;
    }

    @Override
    public boolean checkTimeLimitExceeded(Whiteboard whiteboard, int userId) {
	Long secondsLeft = LearningWebsocketServer.getSecondsLeft(whiteboard.getContentId(), userId);
	return secondsLeft != null && secondsLeft.equals(0L);
    }

    @Override
    public List<User> getPossibleIndividualTimeLimitUsers(long toolContentId, String searchString) {
	Lesson lesson = lessonService.getLessonByToolContentId(toolContentId);
	return lessonService.getLessonLearners(lesson.getLessonId(), searchString, null, null, true);
    }

    @Override
    public WhiteboardUser getUserByIDAndContent(Long userId, Long contentId) {
	return whiteboardUserDao.getUserByUserIDAndContentID(userId, contentId);
    }

    @Override
    public WhiteboardUser getUserByLoginAndContent(String login, long contentId) {
	List<User> user = whiteboardUserDao.findByProperty(User.class, "login", login);
	return user.isEmpty() ? null : getUserByIDAndContent(user.get(0).getUserId().longValue(), contentId);
    }

    @Override
    public WhiteboardUser getUserByIDAndSession(Long userId, Long sessionId) {
	return whiteboardUserDao.getUserByUserIDAndSessionID(userId, sessionId);
    }

    @Override
    public List<WhiteboardUser> getUsersBySession(Long toolSessionId) {
	return whiteboardUserDao.getBySessionID(toolSessionId);
    }

    @Override
    public void saveOrUpdate(Object entity) {
	whiteboardDao.insertOrUpdate(entity);
    }

    @Override
    public Whiteboard getWhiteboardBySessionId(Long sessionId) {
	WhiteboardSession session = whiteboardSessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getWhiteboard().getContentId();
	Whiteboard res = whiteboardDao.getByContentId(contentId);
	return res;
    }

    @Override
    public WhiteboardSession getWhiteboardSessionBySessionId(Long sessionId) {
	return whiteboardSessionDao.getSessionBySessionId(sessionId);
    }

    @Override
    public String finishToolSession(Long toolSessionId, Long userId) throws WhiteboardApplicationException {
	WhiteboardUser user = whiteboardUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	user.setSessionFinished(true);
	whiteboardUserDao.update(user);

	String nextUrl = null;
	try {
	    nextUrl = this.leaveToolSession(toolSessionId, userId);
	} catch (DataMissingException e) {
	    throw new WhiteboardApplicationException(e);
	} catch (ToolException e) {
	    throw new WhiteboardApplicationException(e);
	}
	return nextUrl;
    }

    @Override
    public List<ReflectDTO> getReflectList(Long contentId) {
	List<ReflectDTO> reflections = new LinkedList<>();

	List<WhiteboardSession> sessionList = whiteboardSessionDao.getByContentId(contentId);
	for (WhiteboardSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    // get all users in this session
	    List<WhiteboardUser> users = whiteboardUserDao.getBySessionID(sessionId);
	    for (WhiteboardUser user : users) {

		NotebookEntry entry = getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
			WhiteboardConstants.TOOL_SIGNATURE, user.getUserId().intValue());
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
    public WhiteboardUser getUser(Long uid) {
	return whiteboardUserDao.find(WhiteboardUser.class, uid);
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
	    // Whiteboard currently supports only one place for ratings.
	    // It is rating other groups' boards on results page.
	    // Criterion gets automatically created and there must be only one.
	    try {
		for (int criterionIndex = 1; criterionIndex < criteria.size(); criterionIndex++) {
		    RatingCriteria criterion = criteria.get(criterionIndex);
		    Long criterionId = criterion.getRatingCriteriaId();
		    whiteboardDao.delete(criterion);
		    log.warn("Removed a duplicate criterion ID " + criterionId + " for Whiteboard tool content ID "
			    + toolContentId);
		}
	    } catch (Exception e) {
		log.warn("Ignoring error while deleting a duplicate criterion for Whiteboard tool content ID "
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

	    whiteboardDao.insert(criterion);
	    criteria.add(criterion);
	}
	return criteria;
    }

    @Override
    public void startGalleryWalk(long toolContentId) throws IOException {
	Whiteboard whiteboard = getWhiteboardByContentId(toolContentId);
	if (!whiteboard.isGalleryWalkEnabled()) {
	    throw new IllegalArgumentException(
		    "Can not start Gallery Walk as it is not enabled for Whiteboard with tool content ID "
			    + toolContentId);
	}
	if (whiteboard.isGalleryWalkFinished()) {
	    throw new IllegalArgumentException(
		    "Can not start Gallery Walk as it is already finished for Whiteboard with tool content ID "
			    + toolContentId);
	}
	whiteboard.setGalleryWalkStarted(true);
	whiteboardDao.update(whiteboard);

	sendGalleryWalkRefreshRequest(whiteboard);
    }

    @Override
    public void finishGalleryWalk(long toolContentId) throws IOException {
	Whiteboard whiteboard = getWhiteboardByContentId(toolContentId);
	if (!whiteboard.isGalleryWalkEnabled()) {
	    throw new IllegalArgumentException(
		    "Can not finish Gallery Walk as it is not enabled for Whiteboard with tool content ID "
			    + toolContentId);
	}
	whiteboard.setGalleryWalkFinished(true);
	whiteboardDao.update(whiteboard);

	sendGalleryWalkRefreshRequest(whiteboard);
    }

    private void sendGalleryWalkRefreshRequest(Whiteboard whiteboard) {
	ObjectNode jsonCommand = JsonNodeFactory.instance.objectNode();
	jsonCommand.put("hookTrigger", "whiteboard-refresh-" + whiteboard.getContentId());
	// get all learners in this whitebaord
	Set<Integer> userIds = whiteboardSessionDao.getByContentId(whiteboard.getContentId()).stream()
		.flatMap(session -> whiteboardUserDao.getBySessionID(session.getSessionId()).stream())
		.collect(Collectors.mapping(user -> user.getUserId().intValue(), Collectors.toSet()));

	learnerService.createCommandForLearners(whiteboard.getContentId(), userIds, jsonCommand.toString());
    }

    @Override
    public WhiteboardConfigItem getConfigItem(String key) {
	return whiteboardConfigItemDao.getConfigItemByKey(key);
    }

    @Override
    public void saveOrUpdateWhiteboardConfigItem(WhiteboardConfigItem item) {
	whiteboardConfigItemDao.saveOrUpdate(item);
    }

    @Override
    public String getWhiteboardServerUrl() throws WhiteboardApplicationException {
	WhiteboardConfigItem whiteboardServerUrlConfigItem = getConfigItem(WhiteboardConfigItem.KEY_SERVER_URL);
	if (whiteboardServerUrlConfigItem == null
		|| StringUtils.isBlank(whiteboardServerUrlConfigItem.getConfigValue())) {
	    throw new WhiteboardApplicationException(
		    "Whiteboard server URL is not configured on sysadmin tool management page");
	}
	String whiteboardServerUrl = whiteboardServerUrlConfigItem.getConfigValue();
	if (whiteboardServerUrl.contains(WhiteboardConfigItem.SERVER_URL_PLACEHOLDER)) {
	    String lamsServerUrl = WebUtil.getBaseServerURL();
	    if (lamsServerUrl.contains(":")) {
		lamsServerUrl = lamsServerUrl.substring(0, lamsServerUrl.lastIndexOf(':'));
	    }
	    whiteboardServerUrl = whiteboardServerUrl.replace(WhiteboardConfigItem.SERVER_URL_PLACEHOLDER,
		    lamsServerUrl);
	}
	return whiteboardServerUrl;
    }

    @Override
    public String getWhiteboardAccessTokenHash(String wid, String sourceWid) {
	if (StringUtils.isBlank(wid)) {
	    return null;
	}
	WhiteboardConfigItem whiteboardAccessTokenConfigItem = getConfigItem(WhiteboardConfigItem.KEY_ACCESS_TOKEN);
	if (whiteboardAccessTokenConfigItem == null
		|| StringUtils.isBlank(whiteboardAccessTokenConfigItem.getConfigValue())) {
	    return null;
	}
	// sourceWid is present when we want to copy content from other canvas
	String plainText = (StringUtils.isBlank(sourceWid) ? "" : sourceWid) + wid
		+ whiteboardAccessTokenConfigItem.getConfigValue();
	return String.valueOf(plainText.hashCode());
    }

    public static String getWhiteboardAuthorName(UserDTO user) throws UnsupportedEncodingException {
	if (user == null) {
	    return null;
	}
	StringBuilder authorName = new StringBuilder();
	if (StringUtils.isNotBlank(user.getFirstName())) {
	    authorName.append(user.getFirstName());
	}
	if (StringUtils.isNotBlank(user.getLastName())) {
	    if (authorName.length() > 0) {
		authorName.append(" ");
	    }
	    authorName.append(user.getLastName());
	}
	return URLEncoder.encode(authorName.length() == 0 ? user.getLogin() : authorName.toString(),
		FileUtil.ENCODING_UTF_8);
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************
    private Whiteboard getDefaultWhiteboard() throws WhiteboardApplicationException {
	Long defaultWhiteboardId = getToolDefaultContentIdBySignature(WhiteboardConstants.TOOL_SIGNATURE);
	Whiteboard defaultWhiteboard = getWhiteboardByContentId(defaultWhiteboardId);
	if (defaultWhiteboard == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    WhiteboardService.log.error(error);
	    throw new WhiteboardApplicationException(error);
	}

	return defaultWhiteboard;
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws WhiteboardApplicationException {
	return toolService.getToolDefaultContentIdBySignature(toolSignature);
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
	Whiteboard toolContentObj = whiteboardDao.getByContentId(toolContentId);
	if (toolContentObj == null) {
	    try {
		toolContentObj = getDefaultWhiteboard();
	    } catch (WhiteboardApplicationException e) {
		throw new DataMissingException(e.getMessage());
	    }
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the Whiteboard tool");
	}

	// set tool content handler as null to avoid copy file node in repository again.
	toolContentObj = Whiteboard.newInstance(toolContentObj, toolContentId);
	try {
	    exportContentService.exportToolContent(toolContentId, toolContentObj, whiteboardToolContentHandler,
		    rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

	try {

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, whiteboardToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Whiteboard)) {
		throw new ImportToolContentException(
			"Import Whiteboard tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Whiteboard toolContentObj = (Whiteboard) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentId(toolContentId);
	    WhiteboardUser user = whiteboardUserDao.getUserByUserIDAndContentID(newUserUid.longValue(), toolContentId);
	    if (user == null) {
		user = new WhiteboardUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(newUserUid.longValue());
	    }
	    toolContentObj.setCreatedBy(user);

	    whiteboardDao.insertOrUpdate(toolContentObj);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Whiteboard content = getWhiteboardByContentId(toolContentId);
	if (content == null) {
	    try {
		content = getDefaultContent(toolContentId);
	    } catch (WhiteboardApplicationException e) {
		throw new ToolException(e);
	    }
	}
	return getWhiteboardOutputFactory().getToolOutputDefinitions(content, definitionType);
    }

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	if (toContentId == null) {
	    throw new ToolException("Failed to create the Whiteboard tool seession");
	}

	Whiteboard whiteboard = null;
	if (fromContentId != null) {
	    whiteboard = whiteboardDao.getByContentId(fromContentId);
	}
	if (whiteboard == null) {
	    try {
		whiteboard = getDefaultWhiteboard();
	    } catch (WhiteboardApplicationException e) {
		throw new ToolException(e);
	    }
	}

	Whiteboard toContent = Whiteboard.newInstance(whiteboard, toContentId);
	// copy whiteboard canvas on next open
	toContent.setSourceWid(whiteboard.getContentId().toString());
	whiteboardDao.insert(toContent);

	if (toContent.isGalleryWalkEnabled() && !toContent.isGalleryWalkReadOnly()) {
	    createGalleryWalkRatingCriterion(toContentId);
	}
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getWhiteboardByContentId(toolContentId).getTitle();
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Whiteboard whiteboard = whiteboardDao.getByContentId(toolContentId);
	if (whiteboard == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	whiteboard.setDefineLater(false);
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getWhiteboardByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	List<WhiteboardSession> sessions = whiteboardSessionDao.getByContentId(toolContentId);
	for (WhiteboardSession session : sessions) {
	    if (!whiteboardUserDao.getBySessionID(session.getSessionId()).isEmpty()) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Whiteboard whiteboard = whiteboardDao.getByContentId(toolContentId);
	if (whiteboard == null) {
	    WhiteboardService.log.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (WhiteboardSession session : whiteboardSessionDao.getByContentId(toolContentId)) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, WhiteboardConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}
	whiteboardDao.delete(whiteboard);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (WhiteboardService.log.isDebugEnabled()) {
	    WhiteboardService.log
		    .debug("Removing Whiteboard content for user ID " + userId + " and toolContentId " + toolContentId);
	}

	Whiteboard whiteboard = whiteboardDao.getByContentId(toolContentId);
	if (whiteboard == null) {
	    WhiteboardService.log
		    .warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	List<WhiteboardSession> sessions = whiteboardSessionDao.getByContentId(toolContentId);
	for (WhiteboardSession session : sessions) {
	    WhiteboardUser user = whiteboardUserDao.getUserByUserIDAndSessionID(userId.longValue(),
		    session.getSessionId());
	    if (user != null) {
		NotebookEntry entry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			WhiteboardConstants.TOOL_SIGNATURE, userId);
		if (entry != null) {
		    whiteboardDao.deleteById(NotebookEntry.class, entry.getUid());
		}

		whiteboardUserDao.deleteById(WhiteboardUser.class, user.getUid());
	    }
	}
    }

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	WhiteboardSession session = new WhiteboardSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	Whiteboard whiteboard = whiteboardDao.getByContentId(toolContentId);
	session.setWhiteboard(whiteboard);
	whiteboardSessionDao.insert(session);

	//create pad in a try-catch block so it doesn't affect session creation operation
//	try {
//	    createPad(whiteboard, session);
//	} catch (Exception e) {
//	    log.warn(e.getMessage(), e);
//	}

    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	WhiteboardUser learner = getUserByIDAndSession(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	return new ToolCompletionStatus(learner.isSessionFinished() ? ToolCompletionStatus.ACTIVITY_COMPLETED
		: ToolCompletionStatus.ACTIVITY_ATTEMPTED, null, null);
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    WhiteboardService.log.error("Fail to leave tool session based on null tool session id.");
	    throw new ToolException("Fail to remove tool s ession based on null tool session id.");
	}
	if (learnerId == null) {
	    WhiteboardService.log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}

	WhiteboardSession session = whiteboardSessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(WhiteboardConstants.COMPLETED);
	    whiteboardSessionDao.update(session);
	} else {
	    WhiteboardService.log
		    .error("Fail to leave tool Session. Could not find Whiteboard session by given session id: "
			    + toolSessionId);
	    throw new DataMissingException(
		    "Fail to leave tool Session. Could not find Whiteboard session by given session id: "
			    + toolSessionId);
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
	whiteboardSessionDao.deleteBySessionId(toolSessionId);
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getWhiteboardOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getWhiteboardOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
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
	WhiteboardSession session = getWhiteboardSessionBySessionId(toolSessionId);
	WhiteboardUser groupLeader = session == null ? null : session.getGroupLeader();

	return (groupLeader != null) && userId.equals(groupLeader.getUserId());
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	// no actions required
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getWhiteboardOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    // *****************************************************************************
    // set methods for Spring Bean
    // *****************************************************************************
    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public void setWhiteboardDao(WhiteboardDAO whiteboardDao) {
	this.whiteboardDao = whiteboardDao;
    }

    public void setWhiteboardSessionDao(WhiteboardSessionDAO whiteboardSessionDao) {
	this.whiteboardSessionDao = whiteboardSessionDao;
    }

    public void setWhiteboardConfigItemDao(WhiteboardConfigItemDAO whiteboardConfigItemDao) {
	this.whiteboardConfigItemDao = whiteboardConfigItemDao;
    }

    public void setWhiteboardToolContentHandler(IToolContentHandler whiteboardToolContentHandler) {
	this.whiteboardToolContentHandler = whiteboardToolContentHandler;
    }

    public void setWhiteboardUserDao(WhiteboardUserDAO whiteboardUserDao) {
	this.whiteboardUserDao = whiteboardUserDao;
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

    public WhiteboardOutputFactory getWhiteboardOutputFactory() {
	return whiteboardOutputFactory;
    }

    public void setWhiteboardOutputFactory(WhiteboardOutputFactory whiteboardOutputFactory) {
	this.whiteboardOutputFactory = whiteboardOutputFactory;
    }
}