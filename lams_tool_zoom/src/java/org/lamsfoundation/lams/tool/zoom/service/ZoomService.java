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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
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

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import sun.net.www.protocol.https.HttpsURLConnectionImpl;

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

    private ICoreNotebookService coreNotebookService;

    /* Methods from ToolSessionManager */
    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (ZoomService.logger.isDebugEnabled()) {
	    ZoomService.logger.debug("entering method createToolSession:" + " toolSessionId = " + toolSessionId
		    + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
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
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	zoomDAO.deleteByProperty(ZoomSession.class, "sessionId", toolSessionId);
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return new TreeMap<String, ToolOutput>();
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return null;
    }

    @Override
    public List<ToolOutput> getToolOutputs(String name, Long toolContentId) {
	return new ArrayList<ToolOutput>();
    }

    @Override
    public List<ConfidenceLevelDTO> getConfidenceLevels(Long toolSessionId) {
	return null;
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	//no actions required
    }

    /* Methods from ToolContentManager */

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {

	if (ZoomService.logger.isDebugEnabled()) {
	    ZoomService.logger.debug("entering method copyToolContent:" + " fromContentId=" + fromContentId
		    + " toContentId=" + toContentId);
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
	Zoom toContent = Zoom.newInstance(fromContent, toContentId, zoomToolContentHandler);
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

	for (ZoomSession session : zoom.getZoomSessions()) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, ZoomConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	zoomDAO.delete(zoom);
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (ZoomService.logger.isDebugEnabled()) {
	    ZoomService.logger.debug("Resetting Web Conference completion flag for user ID " + userId
		    + " and toolContentId " + toolContentId);
	}

	Zoom zoom = getZoomByContentId(toolContentId);
	if (zoom == null) {
	    ZoomService.logger
		    .warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	for (ZoomSession session : zoom.getZoomSessions()) {
	    for (ZoomUser user : session.getZoomUsers()) {
		if (user.getUserId().equals(userId)) {
		    if (user.getNotebookEntryUID() != null) {
			NotebookEntry entry = coreNotebookService.getEntry(user.getNotebookEntryUID());
			zoomDAO.delete(entry);
			user.setNotebookEntryUID(null);
		    }
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
     *             if no tool content matches the toolSessionId
     * @throws ToolException
     *             if any other error occurs
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
	zoom = Zoom.newInstance(zoom, toolContentId, null);
	zoom.setToolContentHandler(null);
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
     *             if any other error occurs
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
	    zoom.setCreateBy(new Long(newUserUid.longValue()));
	    zoom.setApi(null);

	    saveOrUpdateZoom(zoom);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @SuppressWarnings("rawtypes")
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
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	return new TreeMap<String, ToolOutputDefinition>();
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
		if (user.getNotebookEntryUID() != null) {
		    // we don't remove users in removeLearnerContent()
		    // we just set their notebook entry to NULL
		    return true;
		}
	    }
	}

	return false;
    }

    public String getContributionURL(Long toolContentId) {
	return ZoomConstants.TOOL_CONTRIBUTE_URL + toolContentId;
    }

    /* IZoomService Methods */

    @Override
    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry) {
	return coreNotebookService.createNotebookEntry(id, idType, signature, userID, "", entry);
    }

    public NotebookEntry getEntry(Long id, Integer idType, String signature, Integer userID) {
	List<NotebookEntry> list = coreNotebookService.getEntry(id, idType, signature, userID);
	if ((list == null) || list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    public NotebookEntry getNotebookEntry(Long uid) {
	return coreNotebookService.getEntry(uid);
    }

    @Override
    public void updateNotebookEntry(Long uid, String entry) {
	coreNotebookService.updateEntry(uid, "", entry);
    }

    @Override
    public void updateNotebookEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
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
	newContent = Zoom.newInstance(defaultContent, newContentID, zoomToolContentHandler);
	saveOrUpdateZoom(newContent);
	return newContent;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Zoom getZoomByContentId(Long toolContentID) {
	List<Zoom> list = zoomDAO.findByProperty(Zoom.class, "toolContentId", toolContentID);
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
    @SuppressWarnings("unchecked")
    public ZoomSession getSessionBySessionId(Long toolSessionId) {
	List<ZoomSession> list = zoomDAO.findByProperty(ZoomSession.class, "sessionId", toolSessionId);
	if (list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    @SuppressWarnings("unchecked")
    public ZoomUser getUserByUserIdAndSessionId(Integer userId, Long toolSessionId) {
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("userId", userId);
	map.put("zoomSession.sessionId", toolSessionId);
	List<ZoomUser> list = zoomDAO.findByProperties(ZoomUser.class, map);
	if (list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    @SuppressWarnings("unchecked")
    public ZoomUser getUserByUID(Long uid) {
	List<ZoomUser> list = zoomDAO.findByProperty(ZoomUser.class, "uid", uid);
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
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	zoom.setReflectOnActivity(Boolean.TRUE);
	zoom.setReflectInstructions(description);
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

    public ICoreNotebookService getCoreNotebookService() {
	return coreNotebookService;
    }

    public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
	this.coreNotebookService = coreNotebookService;
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	ZoomUser learner = getUserByUserIdAndSessionId(learnerId.intValue(), toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	return new ToolCompletionStatus(learner.isFinishedActivity() ? ToolCompletionStatus.ACTIVITY_COMPLETED
		: ToolCompletionStatus.ACTIVITY_ATTEMPTED, null, null);
    }

    public Boolean chooseApi(Long zoomUid) throws IOException, JSONException {
	Zoom zoom = (Zoom) zoomDAO.find(Zoom.class, zoomUid);
	List<ZoomApi> apis = getApis();
	if (apis.isEmpty()) {
	    return null;
	}
	ZoomApi chosenApi = null;
	TreeMap<String, ZoomApi> liveApis = new TreeMap<String, ZoomApi>();
	for (ZoomApi api : apis) {
	    String meetingListURL = "users/" + api.getEmail() + "/meetings?type=live";
	    HttpURLConnection connection = ZoomService.getZoomConnection(meetingListURL, "GET", null, api);
	    JSONObject resultJSON = ZoomService.getReponse(connection);
	    boolean noLiveMeetings = resultJSON != null && resultJSON.getInt("total_records") == 0;
	    if (noLiveMeetings) {
		// found a free API
		chosenApi = api;
		break;
	    }
	    // find the oldest live meeting
	    // string comparing of dates works fine
	    String oldestMeetingStartTime = null;
	    JSONArray meetingsJSON = resultJSON.getJSONArray("meetings");
	    for (int meetingIndex = 0; meetingIndex < meetingsJSON.length(); meetingIndex++) {
		JSONObject meetingJSON = meetingsJSON.getJSONObject(meetingIndex);
		String meetingStartTime = meetingJSON.optString("start_time");
		if (meetingStartTime == null) {
		    meetingStartTime = meetingJSON.getString("created_at");
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
    public String createMeeting(Long zoomUid) throws IOException, JSONException {
	Zoom zoom = (Zoom) zoomDAO.find(Zoom.class, zoomUid);
	if (zoom.getMeetingId() != null) {
	    return zoom.getMeetingStartUrl();
	}
	if (zoom.getApi() == null) {
	    throw new ZoomException("Can not create a meeting without API keys chosen");
	}
	JSONObject bodyJSON = new JSONObject();
	Date currentTime = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
	String startTime = sdf.format(currentTime);
	bodyJSON.put("topic", zoom.getTitle()).put("type", 2).put("start_time", startTime);
	HttpURLConnection connection = ZoomService.getZoomConnection("users/" + zoom.getApi().getEmail() + "/meetings",
		"POST", bodyJSON.toString(), zoom.getApi());
	JSONObject responseJSON = ZoomService.getReponse(connection);
	String meetingId = String.valueOf(responseJSON.getLong("id"));
	zoom.setMeetingId(meetingId);

	ZoomService.configureMeeting(zoom);

	zoomDAO.update(zoom);
	if (logger.isDebugEnabled()) {
	    logger.debug("Created meeting: " + meetingId);
	}
	return zoom.getMeetingStartUrl();
    }

    @Override
    public String registerUser(Long zoomUid, Long userUid, String sessionName) throws IOException, JSONException {
	ZoomUser user = (ZoomUser) zoomDAO.find(ZoomUser.class, userUid);
	if (user.getMeetingJoinUrl() != null) {
	    return user.getMeetingJoinUrl();
	}
	Zoom zoom = (Zoom) zoomDAO.find(Zoom.class, zoomUid);

	JSONObject bodyJSON = new JSONObject();
	String lastName = user.getLastName();
	if (isGroupedActivity(zoom.getToolContentId())) {
	    lastName += " (" + sessionName + ")";
	}
	bodyJSON.put("email", user.getEmail()).put("first_name", user.getFirstName()).put("last_name", lastName);
	HttpURLConnection connection = ZoomService.getZoomConnection("meetings/" + zoom.getMeetingId() + "/registrants",
		"POST", bodyJSON.toString(), zoom.getApi());
	JSONObject responseJSON = ZoomService.getReponse(connection);
	String meetingJoinURL = responseJSON.getString("join_url");
	user.setMeetingJoinUrl(meetingJoinURL);
	zoomDAO.update(user);
	if (logger.isDebugEnabled()) {
	    logger.debug("Registerd user with UID: " + user.getUid() + " for meeting: " + zoom.getMeetingId());
	}
	return meetingJoinURL;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ZoomApi> getApis() {
	return zoomDAO.findAll(ZoomApi.class);
    }

    @Override
    public void saveApis(List<ZoomApi> apis) {
	List<ZoomApi> existingApis = getApis();
	Set<Long> delete = new HashSet<Long>();
	Set<String> saved = new HashSet<String>();
	for (ZoomApi existingApi : existingApis) {
	    boolean found = false;
	    for (ZoomApi api : apis) {
		if (existingApi.getEmail().equalsIgnoreCase(api.getEmail())) {
		    found = true;
		    saved.add(api.getEmail());
		    if (!existingApi.getKey().equals(api.getKey())
			    || !existingApi.getSecret().equals(api.getSecret())) {
			existingApi.setKey(api.getKey());
			existingApi.setSecret(api.getSecret());
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
	    if (!saved.contains(api.getEmail())) {
		zoomDAO.insert(api);
	    }
	}
    }

    public boolean pingZoomApi(Long uid) throws IOException, JSONException {
	ZoomApi api = (ZoomApi) zoomDAO.find(ZoomApi.class, uid);
	HttpURLConnection connection = ZoomService.getZoomConnection("users/email?email=" + api.getEmail(), "GET", null,
		api);
	JSONObject resultJSON = ZoomService.getReponse(connection);
	return resultJSON != null && resultJSON.getBoolean("existed_email");
    }

    private static String generateJWT(ZoomApi api) {
	Date expiration = new Date(System.currentTimeMillis() + ZoomConstants.JWT_EXPIRATION_MILISECONDS);
	return Jwts.builder().setHeaderParam("typ", "JWT").setIssuer(api.getKey()).setExpiration(expiration)
		.signWith(SignatureAlgorithm.HS256, api.getSecret().getBytes()).compact();
    }

    private static HttpURLConnection getZoomConnection(String urlSuffix, String method, String body, ZoomApi api)
	    throws IOException {
	URL url = new URL(ZoomConstants.ZOOM_API_URL + urlSuffix);
	HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
	connection.setRequestProperty("Authorization", "Bearer " + ZoomService.generateJWT(api));
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
    private static void configureMeeting(Zoom zoom) throws IOException, JSONException {
	JSONObject bodyJSON = new JSONObject();
	JSONObject settings = new JSONObject();
	// this setting can not be set during creation, thus we need another call
	settings.put("registrants_confirmation_email", false);
	// these settings could have been set during creation, but this call would have overwritten them, so we set them here
	settings.put("approval_type", 0);
	settings.put("join_before_host", !zoom.isStartInMonitor());
	bodyJSON.put("settings", settings);
	if (zoom.getDuration() != null) {
	    bodyJSON.put("duration", zoom.getDuration());
	}
	HttpURLConnection connection = ZoomService.getZoomConnection("meetings/" + zoom.getMeetingId(), "PATCH",
		bodyJSON.toString(), zoom.getApi());
	ZoomService.getReponse(connection);
	// verify changes and get the new start URL
	connection = ZoomService.getZoomConnection("meetings/" + zoom.getMeetingId(), "GET", null, zoom.getApi());
	JSONObject responseJSON = ZoomService.getReponse(connection);
	String startURL = responseJSON.getString("start_url");
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

    private static JSONObject getReponse(HttpURLConnection connection) throws IOException, JSONException {
	JSONObject responseJSON = null;
	try {
	    connection.connect();
	    int code = connection.getResponseCode();
	    String response = null;

	    if (code < 300) {
		StringWriter writer = new StringWriter();
		IOUtils.copy(connection.getInputStream(), writer);
		response = writer.toString();
		if (response != null && response.startsWith("{")) {
		    responseJSON = new JSONObject(response);
		}
	    }

	    if (logger.isDebugEnabled()) {
		logger.debug("Server response: " + code + " " + connection.getResponseMessage() + " " + response);
	    }
	} finally {
	    connection.disconnect();
	}
	return responseJSON;
    }
}