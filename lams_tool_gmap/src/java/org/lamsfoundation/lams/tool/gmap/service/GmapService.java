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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.gmap.service;

import java.io.StringReader;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
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

public class GmapService implements ToolSessionManager, ToolContentManager, IGmapService, ToolContentImport102Manager {

    private static Logger logger = Logger.getLogger(GmapService.class.getName());

    private IGmapDAO gmapDAO = null;

    private IGmapMarkerDAO gmapMarkerDAO = null;

    private IGmapSessionDAO gmapSessionDAO = null;

    private IGmapUserDAO gmapUserDAO = null;

    private ILearnerService learnerService;

    private ILamsToolService toolService;

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
	if (markers != null && markers.size() > 0) {
	    for (GmapMarker marker : markers) {
		if (marker.isAuthored() && marker.getGmapSession() == null) {
		    GmapMarker newMarker = (GmapMarker) marker.clone();
		    newMarker.setGmapSession(session);
		    saveOrUpdateGmapMarker(newMarker);
		}
	    }
	}
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	return learnerService.completeToolSession(toolSessionId, learnerId);
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException,
	    ToolException {
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
    public void forceCompleteUser(Long toolSessionId, User user) {
	//no actions required
    }

    /* ************ Methods from ToolContentManager ************************* */

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
    
    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException {
	// TODO Auto-generated method stub
    }

    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (logger.isDebugEnabled()) {
	    logger.debug("Removing Gmap markers for user ID " + userId + " and toolContentId " + toolContentId);
	}
	Gmap gmap = gmapDAO.getByContentId(toolContentId);
	if (gmap == null) {
	    logger.warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
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
     *                 if no tool content matches the toolSessionId
     * @throws ToolException
     *                 if any other error occurs
     */

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
     *                 if any other error occurs
     */
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(GmapImportContentVersionFilter.class);
	
	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, gmapToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Gmap)) {
		throw new ImportToolContentException("Import Gmap tool content failed. Deserialized object is "
			+ toolPOJO);
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
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	return new TreeMap<String, ToolOutputDefinition>();
    }

    public String getToolContentTitle(Long toolContentId) {
	return getGmapByContentId(toolContentId).getTitle();
    }
    
    public boolean isContentEdited(Long toolContentId) {
	return getGmapByContentId(toolContentId).isDefineLater();
    }
    
    /* ********** IGmapService Methods ********************************* */

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

    public Gmap getGmapByContentId(Long toolContentID) {
	Gmap gmap = gmapDAO.getByContentId(toolContentID);
	if (gmap == null) {
	    GmapService.logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return gmap;
    }

    public GmapSession getSessionBySessionId(Long toolSessionId) {
	GmapSession gmapSession = gmapSessionDAO.getBySessionId(toolSessionId);
	if (gmapSession == null) {
	    GmapService.logger.debug("Could not find the gmap session with toolSessionID:" + toolSessionId);
	}
	return gmapSession;
    }

    public GmapUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return gmapUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    public GmapUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return gmapUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    public GmapUser getUserByUID(Long uid) {
	return gmapUserDAO.getByUID(uid);
    }

    public void saveOrUpdateGmap(Gmap gmap) {
	gmapDAO.saveOrUpdate(gmap);
    }

    public void saveOrUpdateGmapMarker(GmapMarker gmapMarker) {
	gmapMarkerDAO.saveOrUpdate(gmapMarker);
    }

    public List<GmapMarker> getGmapMarkersBySessionId(Long sessionId) {
	return gmapMarkerDAO.getByToolSessionId(sessionId);
    }

    public void saveOrUpdateGmapSession(GmapSession gmapSession) {
	gmapSessionDAO.saveOrUpdate(gmapSession);
    }

    public void saveOrUpdateGmapUser(GmapUser gmapUser) {
	gmapUserDAO.saveOrUpdate(gmapUser);
    }

    public GmapUser createGmapUser(UserDTO user, GmapSession gmapSession) {
	GmapUser gmapUser = new GmapUser(user, gmapSession);
	saveOrUpdateGmapUser(gmapUser);
	return gmapUser;
    }

    public GmapConfigItem getConfigItem(String key) {
	return gmapConfigItemDAO.getConfigItemByKey(key);
    }

    public void saveOrUpdateGmapConfigItem(GmapConfigItem item) {
	gmapConfigItemDAO.saveOrUpdate(item);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.gmap.service.IGmapService#updateMarkerListFromXML(java.lang.String,
     *      org.lamsfoundation.lams.tool.gmap.model.Gmap, org.lamsfoundation.lams.tool.gmap.model.GmapUser, boolean,
     *      org.lamsfoundation.lams.tool.gmap.model.GmapSession)
     */
    public void updateMarkerListFromXML(String markerXML, Gmap gmap, GmapUser guser, boolean isAuthored,
	    GmapSession session) {

	if (markerXML != null && !markerXML.equals("")) {
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
    
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 Gmap
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	Gmap gmap = new Gmap();
	gmap.setContentInUse(Boolean.FALSE);
	gmap.setCreateBy(new Long(user.getUserID().longValue()));
	gmap.setCreateDate(now);
	gmap.setDefineLater(Boolean.FALSE);
	gmap.setLockOnFinished(Boolean.TRUE);
	gmap.setTitle((String) importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
	gmap.setToolContentId(toolContentId);
	gmap.setUpdateDate(now);
	gmapDAO.saveOrUpdate(gmap);
    }

    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	GmapService.logger
		.warn("Setting the reflective field on a gmap. This doesn't make sense as the gmap is for reflection and we don't reflect on reflection!");
	Gmap gmap = getGmapByContentId(toolContentId);
	if (gmap == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	gmap.setInstructions(description);
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

    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry) {
	return coreNotebookService.createNotebookEntry(id, idType, signature, userID, "", entry);
    }

    public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID) {
	List<NotebookEntry> list = coreNotebookService.getEntry(sessionId, idType, signature, userID);
	if (list == null || list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    /**
     * @param notebookEntry
     */
    public void updateEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }

    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return null;
    }
}
