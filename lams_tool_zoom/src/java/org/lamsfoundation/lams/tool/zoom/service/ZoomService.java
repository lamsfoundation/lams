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

package org.lamsfoundation.lams.tool.zoom.service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.integration.security.RandomPasswordGenerator;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.zoom.dao.IZoomDAO;
import org.lamsfoundation.lams.tool.zoom.model.Zoom;
import org.lamsfoundation.lams.tool.zoom.model.ZoomApi;
import org.lamsfoundation.lams.tool.zoom.model.ZoomSession;
import org.lamsfoundation.lams.tool.zoom.model.ZoomUser;
import org.lamsfoundation.lams.tool.zoom.util.ZoomConstants;
import org.lamsfoundation.lams.tool.zoom.util.ZoomException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import sun.net.www.protocol.https.HttpsURLConnectionImpl;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

/**
 * An implementation of the IZoomService interface.
 *
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 */

public class ZoomService implements ToolSessionManager, ToolContentManager, IZoomService {

    private static final Logger logger = Logger.getLogger(ZoomService.class);

    private IZoomDAO zoomDAO = null;

    private ILearnerService learnerService;

    private ILamsToolService toolService;

    private IToolContentHandler zoomToolContentHandler = null;

    private IExportToolContentService exportContentService;

    private static String TOKEN_CACHE = null;
    private static long TOKEN_CACHE_EXPIRE = 0;

    /* Methods from ToolSessionManager */
    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (ZoomService.logger.isDebugEnabled()) {
	    ZoomService.logger.debug(
		    "entering method createToolSession:" + " toolSessionId = " + toolSessionId + " toolSessionName = "
			    + toolSessionName + " toolContentId = " + toolContentId);
	}

	ZoomSession session = new ZoomSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	// learner starts
	Zoom zoom = getZoomByContentId(toolContentId);
	session.setZoom(zoom);
	zoomDAO.insertOrUpdate(session);
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	return learnerService.completeToolSession(toolSessionId, learnerId);
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
	    throws DataMissingException, ToolException {
	return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ToolSessionExportOutputData exportToolSession(java.util.List toolSessionIds)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	zoomDAO.deleteByProperty(ZoomSession.class, "sessionId", toolSessionId);
    }

    @Override
    public java.util.SortedMap<String, org.lamsfoundation.lams.tool.ToolOutput> getToolOutput(
	    java.util.List<String> names, Long toolSessionId, Long learnerId) {
	return new java.util.TreeMap<>();
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return null;
    }

    @Override
    public java.util.List<org.lamsfoundation.lams.tool.ToolOutput> getToolOutputs(String name, Long toolContentId) {
	return new java.util.ArrayList<>();
    }

    @Override
    public java.util.List<org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO> getConfidenceLevels(
	    Long toolSessionId) {
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

    /* Methods from ToolContentManager */

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {

	if (ZoomService.logger.isDebugEnabled()) {
	    ZoomService.logger.debug(
		    "entering method copyToolContent:" + " fromContentId=" + fromContentId + " toContentId="
			    + toContentId);
	}

	if (toContentId == null) {
	    String error = "Failed to copy tool content: toContentID is null";
	    throw new ToolException(error);
	}

	Zoom fromContent = null;
	if (fromContentId != null) {
	    fromContent = getZoomByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}
	Zoom toContent = Zoom.newInstance(fromContent, toContentId);
	saveOrUpdateZoom(toContent);
    }

    @Override
    public void resetDefineLater(Long toolContentId) {
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Zoom zoom = getZoomByContentId(toolContentId);
	if (zoom == null) {
	    ZoomService.logger.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	zoomDAO.delete(zoom);
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (ZoomService.logger.isDebugEnabled()) {
	    ZoomService.logger.debug(
		    "Resetting Web Conference completion flag for user ID " + userId + " and toolContentId "
			    + toolContentId);
	}

	Zoom zoom = getZoomByContentId(toolContentId);
	if (zoom == null) {
	    ZoomService.logger.warn(
		    "Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	for (ZoomSession session : zoom.getZoomSessions()) {
	    for (ZoomUser user : session.getZoomUsers()) {
		if (user.getUserId().equals(userId)) {
		    user.setFinishedActivity(false);
		    zoomDAO.update(user);
		}
	    }
	}
    }

    /**
     * Export the XML fragment for the tool's content, along with any files needed for the content.
     *
     * @throws DataMissingException
     * 	if no tool content matches the toolSessionId
     * @throws ToolException
     * 	if any other error occurs
     */

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Zoom zoom = getZoomByContentId(toolContentId);
	if (zoom == null) {
	    zoom = getDefaultContent();
	}
	if (zoom == null) {
	    throw new DataMissingException("Unable to find default content for the zoom tool");
	}

	// set ResourceToolContentHandler as null to avoid copy file node in
	// repository again.
	zoom = Zoom.newInstance(zoom, toolContentId);
	zoom.setZoomSessions(null);
	zoom.setApi(null);
	try {
	    exportContentService.exportToolContent(toolContentId, zoom, zoomToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    /**
     * Import the XML fragment for the tool's content, along with any files needed for the content.
     *
     * @throws ToolException
     * 	if any other error occurs
     */
    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(ZoomImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, zoomToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Zoom)) {
		throw new ImportToolContentException(
			"Import Zoom tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Zoom zoom = (Zoom) toolPOJO;

	    // reset it to new toolContentId
	    zoom.setToolContentId(toolContentId);
	    zoom.setCreateBy(newUserUid.longValue());
	    zoom.setApi(null);

	    saveOrUpdateZoom(zoom);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return null;
    }

    /**
     * Get the definitions for possible output for an activity, based on the toolContentId. These may be definitions
     * that are always available for the tool (e.g. number of marks for Multiple Choice) or a custom definition created
     * for a particular activity such as the answer to the third question contains the word Koala and hence the need for
     * the toolContentId
     *
     * @return SortedMap of ToolOutputDefinitions with the key being the name of each definition
     */
    @Override
    public java.util.SortedMap<String, org.lamsfoundation.lams.tool.ToolOutputDefinition> getToolOutputDefinitions(
	    Long toolContentId, int definitionType) throws ToolException {
	return new java.util.TreeMap<>();
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getZoomByContentId(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getZoomByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	Zoom content = getZoomByContentId(toolContentId);
	for (ZoomSession session : content.getZoomSessions()) {
	    for (ZoomUser user : session.getZoomUsers()) {
		if (user.isFinishedActivity()) {
		    // we don't remove users in removeLearnerContent()
		    // we just set their notebook entry to NULL
		    return true;
		}
	    }
	}

	return false;
    }

    @Override
    public String getContributionURL(Long toolContentId) {
	return ZoomConstants.TOOL_CONTRIBUTE_URL + toolContentId;
    }

    @Override
    public Long getDefaultContentIdBySignature(String toolSignature) {
	return toolService.getToolDefaultContentIdBySignature(toolSignature);
    }

    @Override
    public Zoom getDefaultContent() {
	Long defaultContentID = getDefaultContentIdBySignature(ZoomConstants.TOOL_SIGNATURE);
	Zoom defaultContent = getZoomByContentId(defaultContentID);
	if (defaultContent == null) {
	    String error = "Could not retrieve default content record for this tool";
	    ZoomService.logger.error(error);
	}
	return defaultContent;
    }

    @Override
    public Zoom copyDefaultContent(Long newContentID) {
	if (newContentID == null) {
	    String error = "Cannot copy the Zoom tools default content: + " + "newContentID is null";
	    ZoomService.logger.error(error);
	}

	Zoom defaultContent = getDefaultContent();
	// create new zoom using the newContentID
	Zoom newContent = new Zoom();
	newContent = Zoom.newInstance(defaultContent, newContentID);
	saveOrUpdateZoom(newContent);
	return newContent;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Zoom getZoomByContentId(Long toolContentID) {
	java.util.List<org.lamsfoundation.lams.tool.zoom.model.Zoom> list = zoomDAO.findByProperty(Zoom.class,
		"toolContentId", toolContentID);
	if (list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
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
    @SuppressWarnings("unchecked")
    public ZoomSession getSessionBySessionId(Long toolSessionId) {
	java.util.List<org.lamsfoundation.lams.tool.zoom.model.ZoomSession> list = zoomDAO.findByProperty(
		ZoomSession.class, "sessionId", toolSessionId);
	if (list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    @SuppressWarnings("unchecked")
    public ZoomUser getUserByUserIdAndSessionId(Integer userId, Long toolSessionId) {
	java.util.Map<String, Object> map = new java.util.HashMap<>();
	map.put("userId", userId);
	map.put("zoomSession.sessionId", toolSessionId);
	java.util.List<org.lamsfoundation.lams.tool.zoom.model.ZoomUser> list = zoomDAO.findByProperties(ZoomUser.class,
		map);
	if (list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    @SuppressWarnings("unchecked")
    public ZoomUser getUserByUID(Long uid) {
	java.util.List<org.lamsfoundation.lams.tool.zoom.model.ZoomUser> list = zoomDAO.findByProperty(ZoomUser.class,
		"uid", uid);
	if (list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    public void saveOrUpdateZoom(Zoom zoom) {
	zoomDAO.insertOrUpdate(zoom);
    }

    @Override
    public void saveOrUpdateZoomSession(ZoomSession zoomSession) {
	zoomDAO.insertOrUpdate(zoomSession);
    }

    @Override
    public void saveOrUpdateZoomUser(ZoomUser zoomUser) {
	zoomDAO.insertOrUpdate(zoomUser);
    }

    @Override
    public ZoomUser createZoomUser(UserDTO user, ZoomSession zoomSession) {
	ZoomUser zoomUser = new ZoomUser(user, zoomSession);
	saveOrUpdateZoomUser(zoomUser);
	return zoomUser;
    }

    /**
     * Set the description, throws away the title value as this is not supported in 2.0
     */
    public void setReflectiveData(Long toolContentId, String title, String description)
	    throws ToolException, DataMissingException {

	ZoomService.logger.warn(
		"Setting the reflective field on a zoom. This doesn't make sense as the zoom is for reflection and we don't reflect on reflection!");
	Zoom zoom = getZoomByContentId(toolContentId);
	if (zoom == null) {
	    throw new DataMissingException(
		    "Unable to set reflective data titled " + title + " on activity toolContentId " + toolContentId
			    + " as the tool content does not exist.");
	}
    }

    // =========================================================================================
    /* Used by Spring to "inject" the linked objects */

    public IZoomDAO getZoomDAO() {
	return zoomDAO;
    }

    public void setZoomDAO(IZoomDAO zoomDAO) {
	this.zoomDAO = zoomDAO;
    }

    public IToolContentHandler getZoomToolContentHandler() {
	return zoomToolContentHandler;
    }

    public void setZoomToolContentHandler(IToolContentHandler zoomToolContentHandler) {
	this.zoomToolContentHandler = zoomToolContentHandler;
    }

    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public ILearnerService getLearnerService() {
	return learnerService;
    }

    public void setLearnerService(ILearnerService learnerService) {
	this.learnerService = learnerService;
    }

    public IExportToolContentService getExportContentService() {
	return exportContentService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	ZoomUser learner = getUserByUserIdAndSessionId(learnerId.intValue(), toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	return new ToolCompletionStatus(learner.isFinishedActivity()
		? ToolCompletionStatus.ACTIVITY_COMPLETED
		: ToolCompletionStatus.ACTIVITY_ATTEMPTED, null, null);
    }

    @Override
    public Boolean chooseApi(Long zoomUid) throws IOException {
	Zoom zoom = (Zoom) zoomDAO.find(Zoom.class, zoomUid);
	java.util.List<org.lamsfoundation.lams.tool.zoom.model.ZoomApi> apis = getApis();
	if (apis.isEmpty()) {
	    return null;
	}
	ZoomApi chosenApi = null;
	java.util.TreeMap<String, org.lamsfoundation.lams.tool.zoom.model.ZoomApi> liveApis = new java.util.TreeMap<>();
	for (ZoomApi api : apis) {
	    String meetingListURL = "users/" + api.getEmail() + "/meetings?type=live";
	    HttpURLConnection connection = ZoomService.getZoomConnection(meetingListURL, "GET", null, api);
	    ObjectNode resultJSON = ZoomService.getReponse(connection);
	    boolean noLiveMeetings = resultJSON != null && JsonUtil.optInt(resultJSON, "total_records") == 0;
	    if (noLiveMeetings) {
		// found a free API
		chosenApi = api;
		break;
	    }
	    // find the oldest live meeting
	    // string comparing of dates works fine
	    String oldestMeetingStartTime = null;

	    ArrayNode meetingsJSON = JsonUtil.optArray(resultJSON, "meetings");
	    for (int meetingIndex = 0; meetingIndex < meetingsJSON.size(); meetingIndex++) {
		ObjectNode meetingJSON = (ObjectNode) meetingsJSON.get(meetingIndex);
		String meetingStartTime = JsonUtil.optString(meetingJSON, "start_time");
		if (meetingStartTime == null) {
		    meetingStartTime = JsonUtil.optString(meetingJSON, "created_at");
		}
		if (oldestMeetingStartTime == null || meetingStartTime.compareTo(oldestMeetingStartTime) < 0) {
		    oldestMeetingStartTime = meetingStartTime;
		}
	    }
	    liveApis.put(oldestMeetingStartTime, api);
	}

	boolean result = chosenApi != null;
	if (!result) {
	    chosenApi = liveApis.firstEntry().getValue();
	}
	zoom.setApi(chosenApi);
	zoomDAO.update(zoom);
	return result;
    }

    @Override
    public Zoom createMeeting(Long zoomUid) throws IOException {
	Zoom zoom = (Zoom) zoomDAO.find(Zoom.class, zoomUid);
	if (zoom.getMeetingId() != null) {
	    return zoom;
	}
	if (zoom.getApi() == null) {
	    throw new ZoomException("Can not create a meeting without API keys chosen");
	}
	ObjectNode bodyJSON = JsonNodeFactory.instance.objectNode();
	Date currentTime = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
	String startTime = sdf.format(currentTime);
	bodyJSON.put("topic", zoom.getTitle());
	bodyJSON.put("type", 2);
	bodyJSON.put("start_time", startTime);
	HttpURLConnection connection = ZoomService.getZoomConnection("users/" + zoom.getApi().getEmail() + "/meetings",
		"POST", bodyJSON.toString(), zoom.getApi());
	ObjectNode responseJSON = ZoomService.getReponse(connection);
	String meetingId = String.valueOf(JsonUtil.optString(responseJSON, "id"));
	zoom.setMeetingId(meetingId);

	ZoomService.configureMeeting(zoom);

	zoomDAO.update(zoom);
	if (logger.isDebugEnabled()) {
	    logger.debug("Created meeting: " + meetingId);
	}
	return zoom;
    }

    @Override
    public String registerUser(Long zoomUid, Long userUid, String sessionName) throws IOException {
	ZoomUser user = (ZoomUser) zoomDAO.find(ZoomUser.class, userUid);
	if (user.getMeetingJoinUrl() != null) {
	    return user.getMeetingJoinUrl();
	}

	Zoom zoom = (Zoom) zoomDAO.find(Zoom.class, zoomUid);
	if (zoom.getApi() == null) {
	    Boolean apiOK = chooseApi(zoomUid);
	    if (apiOK == null || !apiOK) {
		throw new ZoomException("Can not join the meeting. Problem with Zoom API.");
	    }
	}

	ObjectNode bodyJSON = JsonNodeFactory.instance.objectNode();
	String lastName = user.getLastName();
	if (isGroupedActivity(zoom.getToolContentId())) {
	    lastName += " (" + sessionName + ")";
	}
	bodyJSON.put("email", user.getEmail()).put("first_name", user.getFirstName()).put("last_name", lastName);
	HttpURLConnection connection = ZoomService.getZoomConnection("meetings/" + zoom.getMeetingId() + "/registrants",
		"POST", bodyJSON.toString(), zoom.getApi());
	ObjectNode responseJSON = ZoomService.getReponse(connection);
	String meetingJoinURL = JsonUtil.optString(responseJSON, "join_url");
	if (meetingJoinURL == null) {
	    throw new ZoomException("Could not register user " + user.getUid() + " for meeting " + zoom.getMeetingId());
	}
	// strip URL from password so users need to provide it manually
	meetingJoinURL = meetingJoinURL.replaceFirst("&pwd=[^&]+", "");

	user.setMeetingJoinUrl(meetingJoinURL);
	zoomDAO.update(user);
	if (logger.isDebugEnabled()) {
	    logger.debug("Registerd user with UID: " + user.getUid() + " for meeting: " + zoom.getMeetingId());
	}

	return meetingJoinURL;
    }

    @SuppressWarnings("unchecked")
    @Override
    public java.util.List<org.lamsfoundation.lams.tool.zoom.model.ZoomApi> getApis() {
	return zoomDAO.findAll(ZoomApi.class);
    }

    @Override
    public void saveApis(java.util.List<org.lamsfoundation.lams.tool.zoom.model.ZoomApi> apis) {
	java.util.List<org.lamsfoundation.lams.tool.zoom.model.ZoomApi> existingApis = getApis();
	java.util.Set<Long> delete = new java.util.HashSet<>();
	java.util.Set<String> saved = new java.util.HashSet<>();
	for (ZoomApi existingApi : existingApis) {
	    boolean found = false;
	    for (ZoomApi api : apis) {
		if (existingApi.getEmail().equalsIgnoreCase(api.getEmail())) {
		    found = true;
		    saved.add(api.getEmail());
		    if (!existingApi.getAccountId().equals(api.getAccountId()) || !existingApi.getClientId()
			    .equals(api.getClientId()) || !existingApi.getClientSecret()
			    .equals(api.getClientSecret())) {
			existingApi.setAccountId(api.getAccountId());
			existingApi.setClientId(api.getClientId());
			existingApi.setClientSecret(api.getClientSecret());
			zoomDAO.update(existingApi);
		    }
		    break;
		}
	    }
	    if (!found) {
		delete.add(existingApi.getUid());
	    }
	}
	for (Long uidToDelete : delete) {
	    zoomDAO.deleteById(ZoomApi.class, uidToDelete);
	}
	for (ZoomApi api : apis) {
	    if (!saved.contains(api.getClientId())) {
		zoomDAO.insert(api);
	    }
	}
    }

    @Override
    public boolean pingZoomApi(Long uid) throws IOException {
	ZoomApi api = zoomDAO.find(ZoomApi.class, uid);
	HttpURLConnection connection = ZoomService.getZoomConnection("users/email?email=" + api.getEmail(), "GET", null,
		api);

	ObjectNode resultJSON = ZoomService.getReponse(connection);
	return resultJSON != null && JsonUtil.optBoolean(resultJSON, "existed_email", false);
    }

    private static String generateToken(ZoomApi api) throws IOException {
	if (TOKEN_CACHE == null || TOKEN_CACHE_EXPIRE < System.currentTimeMillis()) {
	    TOKEN_CACHE = null;

	    String clientEncoded = Base64.getEncoder()
		    .encodeToString((api.getClientId() + ":" + api.getClientSecret()).getBytes());

	    HttpsURLConnection connection = (HttpsURLConnection) HttpUrlConnectionUtil.getConnection(
		    ZoomConstants.ZOOM_TOKEN_URL + "?grant_type=account_credentials&account_id=" + api.getAccountId());
	    connection.setRequestMethod("POST");
	    connection.setRequestProperty("Authorization", "Basic " + clientEncoded);

	    ObjectNode responseJSON = getReponse(connection);
	    TOKEN_CACHE = JsonUtil.optString(responseJSON, "access_token");
	    TOKEN_CACHE_EXPIRE =
		    System.currentTimeMillis() + JsonUtil.optInt(responseJSON, "expires_in", 0) * 1000 - 10000;
	}

	return TOKEN_CACHE;
    }

    private static HttpURLConnection getZoomConnection(String urlSuffix, String method, String body, ZoomApi api)
	    throws IOException {
	HttpsURLConnection connection = (HttpsURLConnection) HttpUrlConnectionUtil.getConnection(
		ZoomConstants.ZOOM_API_URL + urlSuffix);
	connection.setRequestProperty("Authorization", "Bearer " + ZoomService.generateToken(api));
	switch (method) {
	    case "PATCH":
		ZoomService.setRequestMethod(connection, method);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);
		break;
	    case "POST":
		connection.setRequestMethod(method);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);
		break;
	    default:
		connection.setRequestMethod(method);
		break;
	}
	if (body != null) {
	    OutputStream os = connection.getOutputStream();
	    OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
	    osw.write(body);
	    osw.flush();
	    osw.close();
	    os.close();
	}
	return connection;
    }

    /**
     * Put extra options which can not be set when creating a meeting
     */
    private static void configureMeeting(Zoom zoom) throws IOException {
	ObjectNode bodyJSON = JsonNodeFactory.instance.objectNode();
	ObjectNode settings = JsonNodeFactory.instance.objectNode();
	// this setting can not be set during creation, thus we need another call
	settings.put("registrants_confirmation_email", false);
	// these settings could have been set during creation, but this call would have overwritten them, so we set them here
	settings.put("approval_type", 0);
	settings.put("join_before_host", !zoom.isStartInMonitor());
	bodyJSON.set("settings", settings);
	if (zoom.getDuration() != null) {
	    bodyJSON.put("duration", zoom.getDuration());
	}
	String password = RandomPasswordGenerator.nextPassword(6);
	bodyJSON.put("password", password);
	zoom.setMeetingPassword(password);

	HttpURLConnection connection = ZoomService.getZoomConnection("meetings/" + zoom.getMeetingId(), "PATCH",
		bodyJSON.toString(), zoom.getApi());
	ZoomService.getReponse(connection);
	// verify changes and get the new start URL
	connection = ZoomService.getZoomConnection("meetings/" + zoom.getMeetingId(), "GET", null, zoom.getApi());
	ObjectNode responseJSON = ZoomService.getReponse(connection);
	String startURL = JsonUtil.optString(responseJSON, "start_url");
	zoom.setMeetingStartUrl(startURL);

	if (logger.isDebugEnabled()) {
	    logger.debug("Configured meeting: " + zoom.getMeetingId());
	}
    }

    private static void setRequestMethod(HttpURLConnection connection, String method) {
	try {
	    final Object target;
	    if (connection instanceof HttpsURLConnectionImpl) {
		final Field delegate = HttpsURLConnectionImpl.class.getDeclaredField("delegate");
		delegate.setAccessible(true);
		target = delegate.get(connection);
	    } else {
		target = connection;
	    }
	    final Field f = HttpURLConnection.class.getDeclaredField("method");
	    f.setAccessible(true);
	    f.set(target, method);
	} catch (IllegalAccessException | NoSuchFieldException ex) {
	    throw new AssertionError(ex);
	}
    }

    private static ObjectNode getReponse(HttpURLConnection connection) throws IOException {
	ObjectNode responseJSON = null;
	try {
	    connection.connect();
	    int code = connection.getResponseCode();
	    String responseMessage = connection.getResponseMessage();
	    String response = null;
	    InputStream responseStream = code < 300 ? connection.getInputStream() : connection.getErrorStream();

	    if (responseStream != null) {
		StringWriter writer = new StringWriter();
		IOUtils.copy(responseStream, writer, Charset.defaultCharset());
		response = writer.toString();
		if (response != null && response.startsWith("{")) {
		    responseJSON = JsonUtil.readObject(response);
		    if (code >= 300) {
			String errorCode = JsonUtil.optString(responseJSON, "code");
			if (StringUtils.isNotBlank(errorCode) && errorCode.equals("200")) {
			    throw new ZoomException("API can only be used with a paid account");
			}
		    }
		}
	    }

	    if (logger.isDebugEnabled()) {
		logger.debug("Server response: " + code + (responseMessage == null
			? ""
			: " " + connection.getResponseMessage()) + (response == null ? "" : " " + response));
	    }
	} finally {
	    connection.disconnect();
	}
	return responseJSON;
    }
}