/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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


package org.lamsfoundation.lams.tool.gmap.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
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
import org.lamsfoundation.lams.tool.gmap.dao.IGmapConfigItemDAO;
import org.lamsfoundation.lams.tool.gmap.dao.IGmapDAO;
import org.lamsfoundation.lams.tool.gmap.dao.IGmapMarkerDAO;
import org.lamsfoundation.lams.tool.gmap.dao.IGmapSessionDAO;
import org.lamsfoundation.lams.tool.gmap.dao.IGmapUserDAO;
import org.lamsfoundation.lams.tool.gmap.model.Gmap;
import org.lamsfoundation.lams.tool.gmap.model.GmapConfigItem;
import org.lamsfoundation.lams.tool.gmap.model.GmapMarker;
import org.lamsfoundation.lams.tool.gmap.model.GmapSession;
import org.lamsfoundation.lams.tool.gmap.model.GmapUser;
import org.lamsfoundation.lams.tool.gmap.util.GmapConstants;
import org.lamsfoundation.lams.tool.gmap.util.GmapException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * An implementation of the IGmapService interface.
 *
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 */

public class GmapService implements ToolSessionManager, ToolContentManager, IGmapService {

    private static Logger logger = Logger.getLogger(GmapService.class.getName());

    private IGmapDAO gmapDAO = null;

    private IGmapMarkerDAO gmapMarkerDAO = null;

    private IGmapSessionDAO gmapSessionDAO = null;

    private IGmapUserDAO gmapUserDAO = null;

    private ILamsToolService toolService;

    private IUserManagementService userManagementService;

    private IToolContentHandler gmapToolContentHandler = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private IGmapConfigItemDAO gmapConfigItemDAO;

    public GmapService() {
	super();
	// TODO Auto-generated constructor stub
    }

    /* ************ Methods from ToolSessionManager ************* */
    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (GmapService.logger.isDebugEnabled()) {
	    GmapService.logger.debug("entering method createToolSession:" + " toolSessionId = " + toolSessionId
		    + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
	}

	GmapSession session = new GmapSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	// learner starts
	// TODO need to also set other fields.
	Gmap gmap = gmapDAO.getByContentId(toolContentId);
	session.setGmap(gmap);
	gmapSessionDAO.saveOrUpdate(session);

	Set<GmapMarker> markers = gmap.getGmapMarkers();
	if ((markers != null) && (markers.size() > 0)) {
	    for (GmapMarker marker : markers) {
		if (marker.isAuthored() && (marker.getGmapSession() == null)) {
		    GmapMarker newMarker = (GmapMarker) marker.clone();
		    newMarker.setGmapSession(session);
		    saveOrUpdateGmapMarker(newMarker);
		}
	    }
	}
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
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
	gmapSessionDAO.deleteBySessionID(toolSessionId);
	// TODO check if cascade worked
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

    /* ************ Methods from ToolContentManager ************************* */

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {

	if (GmapService.logger.isDebugEnabled()) {
	    GmapService.logger.debug("entering method copyToolContent:" + " fromContentId=" + fromContentId
		    + " toContentId=" + toContentId);
	}

	if (toContentId == null) {
	    String error = "Failed to copy tool content: toContentID is null";
	    throw new ToolException(error);
	}

	Gmap fromContent = null;
	if (fromContentId != null) {
	    fromContent = gmapDAO.getByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}
	Gmap toContent = Gmap.newInstance(fromContent, toContentId);
	gmapDAO.saveOrUpdate(toContent);
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Gmap gmap = gmapDAO.getByContentId(toolContentId);
	if (gmap == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	gmap.setDefineLater(false);
	gmapDAO.saveOrUpdate(gmap);
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Gmap gmap = gmapDAO.getByContentId(toolContentId);
	if (gmap == null) {
	    GmapService.logger.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (GmapSession session : gmap.getGmapSessions()) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, GmapConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	gmapDAO.delete(gmap);
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (GmapService.logger.isDebugEnabled()) {
	    GmapService.logger
		    .debug("Removing Gmap markers for user ID " + userId + " and toolContentId " + toolContentId);
	}
	Gmap gmap = gmapDAO.getByContentId(toolContentId);
	if (gmap == null) {
	    GmapService.logger
		    .warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	Iterator<GmapMarker> markerIterator = gmap.getGmapMarkers().iterator();
	while (markerIterator.hasNext()) {
	    GmapMarker marker = markerIterator.next();
	    if (!marker.isAuthored() && marker.getCreatedBy().getUserId().equals(userId.longValue())) {
		gmapMarkerDAO.delete(marker);
		markerIterator.remove();
	    }
	}

	for (GmapSession session : gmap.getGmapSessions()) {
	    GmapUser user = gmapUserDAO.getByUserIdAndSessionId(userId.longValue(), session.getSessionId());
	    if (user != null) {
		NotebookEntry entry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			GmapConstants.TOOL_SIGNATURE, userId);
		if (entry != null) {
		    gmapDAO.delete(entry);
		}

		user.setFinishedActivity(false);
		gmapUserDAO.update(user);
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
	Gmap gmap = gmapDAO.getByContentId(toolContentId);
	if (gmap == null) {
	    gmap = getDefaultContent();
	}
	if (gmap == null) {
	    throw new DataMissingException("Unable to find default content for the gmap tool");
	}

	// set ResourceToolContentHandler as null to avoid copy file node in
	// repository again.
	gmap = Gmap.newInstance(gmap, toolContentId);
	gmap.setGmapSessions(null);
	gmap.setCreateBy(null);

	Set<GmapMarker> markers = gmap.getGmapMarkers();
	Set<GmapMarker> authorItems = new HashSet<GmapMarker>();

	for (GmapMarker gmapMarker : markers) {
	    if (gmapMarker.isAuthored()) {
		gmapMarker.setCreatedBy(null);
		gmapMarker.setGmap(null);
		gmapMarker.setUpdatedBy(null);
		gmapMarker.setGmapSession(null);
		authorItems.add(gmapMarker);
	    }
	}
	gmap.setGmapMarkers(authorItems);

	try {
	    exportContentService.exportToolContent(toolContentId, gmap, gmapToolContentHandler, rootPath);
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
	    exportContentService.registerImportVersionFilterClass(GmapImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, gmapToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Gmap)) {
		throw new ImportToolContentException(
			"Import Gmap tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Gmap gmap = (Gmap) toolPOJO;

	    // reset it to new toolContentId
	    gmap.setToolContentId(toolContentId);

	    // Create a user for gmap to be created by:
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    GmapUser gmapUser = new GmapUser(user, null);
	    gmapUserDAO.saveOrUpdate(gmapUser);

	    gmap.setCreateBy(gmapUser.getUid());
	    // gmap.setCreateBy(new Long(newUserUid.longValue()));

	    // Fixing up any trailing spaces
	    if (gmap.getGmapMarkers() != null) {
		for (GmapMarker marker : gmap.getGmapMarkers()) {
		    if (marker.getInfoWindowMessage() != null) {
			marker.setInfoWindowMessage(marker.getInfoWindowMessage().trim());
		    }
		}
	    }

	    gmapDAO.saveOrUpdate(gmap);
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
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	return new TreeMap<String, ToolOutputDefinition>();
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getGmapByContentId(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getGmapByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	Gmap gmap = gmapDAO.getByContentId(toolContentId);
	for (GmapMarker marker : gmap.getGmapMarkers()) {
	    if (!marker.isAuthored()) {
		// in removeLearnerContent we only remove markers, not users
		return true;
	    }
	}

	return false;
    }

    /* ********** IGmapService Methods ********************************* */

    @Override
    public Long getDefaultContentIdBySignature(String toolSignature) {
	Long toolContentId = null;
	toolContentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (toolContentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    GmapService.logger.error(error);
	    throw new GmapException(error);
	}
	return toolContentId;
    }

    @Override
    public Gmap getDefaultContent() {
	Long defaultContentID = getDefaultContentIdBySignature(GmapConstants.TOOL_SIGNATURE);
	Gmap defaultContent = getGmapByContentId(defaultContentID);
	if (defaultContent == null) {
	    String error = "Could not retrieve default content record for this tool";
	    GmapService.logger.error(error);
	    throw new GmapException(error);
	}
	return defaultContent;
    }

    @Override
    public Gmap copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the Gmap tools default content: + " + "newContentID is null";
	    GmapService.logger.error(error);
	    throw new GmapException(error);
	}

	Gmap defaultContent = getDefaultContent();
	// create new gmap using the newContentID
	Gmap newContent = new Gmap();
	newContent = Gmap.newInstance(defaultContent, newContentID);
	gmapDAO.saveOrUpdate(newContent);
	return newContent;
    }

    @Override
    public Gmap getGmapByContentId(Long toolContentID) {
	Gmap gmap = gmapDAO.getByContentId(toolContentID);
	if (gmap == null) {
	    GmapService.logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return gmap;
    }

    @Override
    public GmapSession getSessionBySessionId(Long toolSessionId) {
	GmapSession gmapSession = gmapSessionDAO.getBySessionId(toolSessionId);
	if (gmapSession == null) {
	    GmapService.logger.debug("Could not find the gmap session with toolSessionID:" + toolSessionId);
	}
	return gmapSession;
    }

    @Override
    public GmapUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return gmapUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    public GmapUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return gmapUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    @Override
    public GmapUser getUserByUID(Long uid) {
	return gmapUserDAO.getByUID(uid);
    }

    @Override
    public void saveOrUpdateGmap(Gmap gmap) {
	gmapDAO.saveOrUpdate(gmap);
    }

    @Override
    public void saveOrUpdateGmapMarker(GmapMarker gmapMarker) {
	gmapMarkerDAO.saveOrUpdate(gmapMarker);
    }

    @Override
    public List<GmapMarker> getGmapMarkersBySessionId(Long sessionId) {
	return gmapMarkerDAO.getByToolSessionId(sessionId);
    }

    @Override
    public void saveOrUpdateGmapSession(GmapSession gmapSession) {
	gmapSessionDAO.saveOrUpdate(gmapSession);
    }

    @Override
    public void saveOrUpdateGmapUser(GmapUser gmapUser) {
	gmapUserDAO.saveOrUpdate(gmapUser);
    }

    @Override
    public GmapUser createGmapUser(UserDTO user, GmapSession gmapSession) {
	GmapUser gmapUser = new GmapUser(user, gmapSession);
	saveOrUpdateGmapUser(gmapUser);
	return gmapUser;
    }

    @Override
    public GmapConfigItem getConfigItem(String key) {
	return gmapConfigItemDAO.getConfigItemByKey(key);
    }

    @Override
    public void saveOrUpdateGmapConfigItem(GmapConfigItem item) {
	gmapConfigItemDAO.saveOrUpdate(item);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.tool.gmap.service.IGmapService#updateMarkerListFromXML(java.lang.String,
     * org.lamsfoundation.lams.tool.gmap.model.Gmap, org.lamsfoundation.lams.tool.gmap.model.GmapUser, boolean,
     * org.lamsfoundation.lams.tool.gmap.model.GmapSession)
     */
    @Override
    public void updateMarkerListFromXML(String markerXML, Gmap gmap, GmapUser guser, boolean isAuthored,
	    GmapSession session) {

	if ((markerXML != null) && !markerXML.equals("")) {
	    try {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(new InputSource(new StringReader(markerXML)));
		NodeList list = document.getElementsByTagName("marker");

		for (int i = 0; i < list.getLength(); i++) {
		    NamedNodeMap markerNode = list.item(i).getAttributes();

		    Long uid = Long.parseLong(markerNode.getNamedItem("markerUID").getNodeValue());
		    String markerTitle = markerNode.getNamedItem("title").getNodeValue();
		    String infoMessage = markerNode.getNamedItem("infoMessage").getNodeValue();
		    Double latitude = Double.parseDouble(markerNode.getNamedItem("latitude").getNodeValue());
		    Double longitude = Double.parseDouble(markerNode.getNamedItem("longitude").getNodeValue());

		    String markerState = markerNode.getNamedItem("state").getNodeValue();

		    if (markerState.equals("remove")) {
			gmap.removeMarker(uid);
			continue;
		    }

		    GmapMarker marker = null;
		    if (markerState.equals("save")) {
			marker = new GmapMarker();
			marker.setCreatedBy(guser);
			marker.setCreated(new Date());
			marker.setAuthored(isAuthored);
		    } else if (markerState.equals("update")) {
			marker = gmap.getMarkerByUid(uid);
		    }

		    marker.setGmapSession(session);
		    marker.setTitle(markerTitle);
		    marker.setInfoWindowMessage(infoMessage);
		    marker.setLatitude(latitude);
		    marker.setLongitude(longitude);
		    marker.setGmap(gmap);
		    marker.setUpdated(new Date());
		    marker.setUpdatedBy(guser);
		    saveOrUpdateGmapMarker(marker);
		}
	    } catch (Exception e) {
		// TODO: improve error handling
		GmapService.logger.error("Could not get marker xml object to update", e);
		throw new GmapException("Could not get marker xml object to update", e);
	    }
	} else {
	    GmapService.logger.debug("MarkerXML string was empty");
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
    public List<Object[]> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting,
	    String searchString, boolean getNotebookEntries) {
	return gmapUserDAO.getUsersForTablesorter(sessionId, page, size, sorting, searchString, getNotebookEntries,
		coreNotebookService, userManagementService);
    }


    @Override
    public int getCountUsersBySession(Long sessionId, String searchString) {
	return gmapUserDAO.getCountUsersBySession(sessionId, searchString);
    }

    // =========================================================================================
    /* ********** Used by Spring to "inject" the linked objects ************* */

    public IGmapDAO getGmapDAO() {
	return gmapDAO;
    }

    public void setGmapDAO(IGmapDAO gmapDAO) {
	this.gmapDAO = gmapDAO;
    }

    public IToolContentHandler getGmapToolContentHandler() {
	return gmapToolContentHandler;
    }

    public void setGmapToolContentHandler(IToolContentHandler gmapToolContentHandler) {
	this.gmapToolContentHandler = gmapToolContentHandler;
    }

    public IGmapSessionDAO getGmapSessionDAO() {
	return gmapSessionDAO;
    }

    public void setGmapSessionDAO(IGmapSessionDAO sessionDAO) {
	gmapSessionDAO = sessionDAO;
    }

    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public IGmapUserDAO getGmapUserDAO() {
	return gmapUserDAO;
    }

    public void setGmapUserDAO(IGmapUserDAO userDAO) {
	gmapUserDAO = userDAO;
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

    public IUserManagementService getUserManagementService() {
   	return userManagementService;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public IGmapMarkerDAO getGmapMarkerDAO() {
	return gmapMarkerDAO;
    }

    public void setGmapMarkerDAO(IGmapMarkerDAO gmapMarkerDAO) {
	this.gmapMarkerDAO = gmapMarkerDAO;
    }

    public IGmapConfigItemDAO getGmapConfigItemDAO() {
	return gmapConfigItemDAO;
    }

    public void setGmapConfigItemDAO(IGmapConfigItemDAO gmapConfigItemDAO) {
	this.gmapConfigItemDAO = gmapConfigItemDAO;
    }

    @Override
    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry) {
	return coreNotebookService.createNotebookEntry(id, idType, signature, userID, "", entry);
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

    /**
     * @param notebookEntry
     */
    @Override
    public void updateEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return null;
    }
    
    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	GmapUser learner = getUserByUserIdAndSessionId(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	Date startDate = null;
	Date endDate = null;
	List<GmapMarker> markers = getGmapMarkersBySessionId(toolSessionId);
	for ( GmapMarker marker : markers ) {
	    Date createdDate = marker.getCreated();
	    if ( marker.getCreatedBy().getUserId() == learnerId && createdDate != null ) {
		if ( startDate == null || createdDate.before(startDate) )
		    startDate = createdDate;
		if ( endDate == null || createdDate.after(endDate) )
		    endDate = createdDate;
	    }
	}
	
	if (learner.isFinishedActivity())
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_COMPLETED, startDate, endDate);
	else
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_ATTEMPTED, startDate, null);
    }
}
