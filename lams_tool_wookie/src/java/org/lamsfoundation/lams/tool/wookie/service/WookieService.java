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


package org.lamsfoundation.lams.tool.wookie.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.SortedMap;

import org.apache.commons.lang.StringUtils;
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
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.wookie.dao.IWookieConfigItemDAO;
import org.lamsfoundation.lams.tool.wookie.dao.IWookieDAO;
import org.lamsfoundation.lams.tool.wookie.dao.IWookieSessionDAO;
import org.lamsfoundation.lams.tool.wookie.dao.IWookieUserDAO;
import org.lamsfoundation.lams.tool.wookie.dto.WidgetData;
import org.lamsfoundation.lams.tool.wookie.model.Wookie;
import org.lamsfoundation.lams.tool.wookie.model.WookieConfigItem;
import org.lamsfoundation.lams.tool.wookie.model.WookieSession;
import org.lamsfoundation.lams.tool.wookie.model.WookieUser;
import org.lamsfoundation.lams.tool.wookie.util.WookieConstants;
import org.lamsfoundation.lams.tool.wookie.util.WookieException;
import org.lamsfoundation.lams.tool.wookie.util.WookieUtil;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;

/**
 * An implementation of the IWookieService interface.
 *
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 */

public class WookieService
	implements ToolSessionManager, ToolContentManager, IWookieService, ToolContentImport102Manager {

    private static Logger logger = Logger.getLogger(WookieService.class.getName());

    private IWookieDAO wookieDAO = null;

    private IWookieSessionDAO wookieSessionDAO = null;

    private IWookieUserDAO wookieUserDAO = null;

    private ILearnerService learnerService;

    private ILamsToolService toolService;

    private IToolContentHandler wookieToolContentHandler = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private WookieOutputFactory wookieOutputFactory;

    private IWookieConfigItemDAO wookieConfigItemDAO;

    private MessageService messageService;

    private IUserManagementService userManagementService;

    public WookieService() {
	super();
    }

    /* ************ Methods from ToolSessionManager ************* */

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (WookieService.logger.isDebugEnabled()) {
	    WookieService.logger.debug("entering method createToolSession:" + " toolSessionId = " + toolSessionId
		    + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
	}

	WookieSession session = new WookieSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	// learner starts
	// TODO need to also set other fields.
	Wookie wookie = wookieDAO.getByContentId(toolContentId);
	session.setWookie(wookie);

	// Create a copy of the widget for the session
	// Clone the wookie widget on the external server
	String wookieUrl = getWookieURL();
	try {
	    String newSharedDataKey = toolSessionId.toString() + "_" + toolContentId.toString();

	    if (wookieUrl != null) {

		if ((wookie.getWidgetIdentifier() != null) && (wookie.getWidgetIdentifier() != "")) {

		    wookieUrl += WookieConstants.RELATIVE_URL_WIDGET_SERVICE;

		    WookieService.logger
			    .debug("Creating a new clone for session of widget: " + toolContentId.toString());
		    boolean success = WookieUtil.cloneWidget(wookieUrl, getWookieAPIKey(), wookie.getWidgetIdentifier(),
			    toolContentId.toString(), newSharedDataKey, wookie.getCreateBy().toString());

		    if (success) {
			session.setWidgetSharedDataKey(newSharedDataKey);
			session.setWidgetHeight(wookie.getWidgetHeight());
			session.setWidgetWidth(wookie.getWidgetWidth());
			session.setWidgetMaximise(wookie.getWidgetMaximise());
			session.setWidgetIdentifier(wookie.getWidgetIdentifier());
		    } else {
			throw new WookieException("Failed to copy widget on wookie server, check log for details.");
		    }
		}

	    } else {
		throw new WookieException("Wookie url is not set");
	    }
	} catch (Exception e) {
	    WookieService.logger.error("Problem calling wookie server to clone instance", e);
	    throw new WookieException("Problem calling wookie server to clone instance", e);
	}

	wookieSessionDAO.saveOrUpdate(session);
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

    @Override
    @SuppressWarnings("unchecked")
    public ToolSessionExportOutputData exportToolSession(List ToolSessionIds)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	wookieSessionDAO.deleteBySessionID(toolSessionId);
	// TODO check if cascade worked
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getWookieOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getWookieOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
    }

    @Override
    public List<ToolOutput> getToolOutputs(String name, Long toolContentId) {
	return new ArrayList<ToolOutput>();
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	// no actions required
    }

    /* ************ Methods from ToolContentManager ************************* */

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {

	if (WookieService.logger.isDebugEnabled()) {
	    WookieService.logger.debug("entering method copyToolContent:" + " fromContentId=" + fromContentId
		    + " toContentId=" + toContentId);
	}

	if (toContentId == null) {
	    String error = "Failed to copy tool content: toContentID is null";
	    throw new ToolException(error);
	}

	Wookie fromContent = null;
	if (fromContentId != null) {
	    fromContent = wookieDAO.getByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}
	Wookie toContent = Wookie.newInstance(fromContent, toContentId);

	// Clone the wookie widget on the external server
	String wookieUrl = getWookieURL();
	try {
	    if (wookieUrl != null) {
		if ((fromContent.getWidgetIdentifier() != null) && (fromContent.getWidgetIdentifier() != "")) {
		    wookieUrl += WookieConstants.RELATIVE_URL_WIDGET_SERVICE;

		    WookieService.logger
			    .debug("Creating a new clone for copycontent for widget: " + fromContentId.toString());
		    boolean success = WookieUtil.cloneWidget(wookieUrl, getWookieAPIKey(),
			    fromContent.getWidgetIdentifier(), fromContentId.toString(), toContentId.toString(),
			    fromContent.getCreateBy().toString());

		    if (success) {
			toContent.setWidgetHeight(fromContent.getWidgetHeight());
			toContent.setWidgetWidth(fromContent.getWidgetWidth());
			toContent.setWidgetAuthorUrl(fromContent.getWidgetAuthorUrl());
			toContent.setWidgetMaximise(fromContent.getWidgetMaximise());
			toContent.setWidgetIdentifier(fromContent.getWidgetIdentifier());
			toContent.setCreateBy(fromContent.getCreateBy());

			// Need to add the author to the widget so authoring
			// widget url is different in the copy
			User user = (User) userManagementService.findById(User.class, fromContent.getCreateBy());
			String returnXML = WookieUtil.getWidget(wookieUrl, getWookieAPIKey(),
				fromContent.getWidgetIdentifier(), user.getUserDTO(), toContentId.toString(), true);

			toContent.setWidgetAuthorUrl(WookieUtil.getWidgetUrlFromXML(returnXML));

		    } else {
			throw new WookieException("Failed to copy widget on wookie server, check log for details.");
		    }
		}
	    } else {
		throw new WookieException("Wookie url is not set");
	    }
	} catch (Exception e) {
	    WookieService.logger.error("Problem calling wookie server to clone instance", e);
	    throw new WookieException("Problem calling wookie server to clone instance", e);
	}

	wookieDAO.saveOrUpdate(toContent);
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Wookie wookie = wookieDAO.getByContentId(toolContentId);
	if (wookie == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	wookie.setDefineLater(false);
	wookieDAO.saveOrUpdate(wookie);
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Wookie wookie = wookieDAO.getByContentId(toolContentId);
	if (wookie == null) {
	    WookieService.logger.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (WookieSession session : wookie.getWookieSessions()) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, WookieConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	wookieDAO.delete(wookie);
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (WookieService.logger.isDebugEnabled()) {
	    WookieService.logger.debug(
		    "Resetting Wookie completion flag for user ID " + userId + " and toolContentId " + toolContentId);
	}

	Wookie wookie = getWookieByContentId(toolContentId);
	if (wookie == null) {
	    WookieService.logger
		    .warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	for (WookieSession session : wookie.getWookieSessions()) {
	    WookieUser user = wookieUserDAO.getByUserIdAndSessionId(userId.longValue(), session.getSessionId());
	    if (user != null) {
		NotebookEntry entry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			WookieConstants.TOOL_SIGNATURE, userId);
		if (entry != null) {
		    wookieDAO.delete(entry);
		}
		user.setFinishedActivity(false);
		wookieUserDAO.update(user);
	    }

	}
    }

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Wookie wookie = wookieDAO.getByContentId(toolContentId);
	if (wookie == null) {
	    wookie = getDefaultContent();
	}
	if (wookie == null) {
	    throw new DataMissingException("Unable to find default content for the wookie tool");
	}

	// set ResourceToolContentHandler as null to avoid copy file node in
	// repository again.
	wookie = Wookie.newInstance(wookie, toolContentId);
	wookie.setWookieSessions(null);

	try {
	    exportContentService.exportToolContent(toolContentId, wookie, wookieToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(WookieContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, wookieToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Wookie)) {
		throw new ImportToolContentException(
			"Import Wookie tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Wookie wookie = (Wookie) toolPOJO;

	    // reset it to new toolContentId
	    wookie.setToolContentId(toolContentId);
	    wookie.setCreateBy(newUserUid);

	    User user = (User) userManagementService.findById(User.class, newUserUid);

	    // If the wookie has an identifier, it has been initiated. Make an
	    // new instance using the identifier
	    if (!StringUtils.isEmpty(wookie.getWidgetIdentifier()) && (user != null)) {
		String wookieUrl = getWookieURL();
		String wookieKey = getWookieAPIKey();
		wookieUrl += WookieConstants.RELATIVE_URL_WIDGET_SERVICE;

		String returnXML = WookieUtil.getWidget(wookieUrl, wookieKey, wookie.getWidgetIdentifier(),
			user.getUserDTO(), toolContentId.toString(), true);

		WidgetData widgetData = WookieUtil.getWidgetDataFromXML(returnXML);
		wookie.setWidgetAuthorUrl(widgetData.getUrl());
		wookie.setWidgetIdentifier(wookie.getWidgetIdentifier());
		wookie.setWidgetHeight(widgetData.getHeight());
		wookie.setWidgetMaximise(widgetData.getMaximize());
		wookie.setWidgetWidth(widgetData.getWidth());
	    }

	    wookieDAO.saveOrUpdate(wookie);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	} catch (Exception e) {
	    WookieService.logger.error("Error during import possibly because of file copy error", e);
	    throw new ToolException(e);
	}
    }

    @Override
    public String getFileExtension(String fileName) {
	String ext = "";
	int i = fileName.lastIndexOf('.');
	if ((i > 0) && (i < (fileName.length() - 1))) {
	    ext += "." + fileName.substring(i + 1).toLowerCase();
	}
	return ext;
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Wookie wookie = getWookieDAO().getByContentId(toolContentId);
	if (wookie == null) {
	    wookie = getDefaultContent();
	}
	return getWookieOutputFactory().getToolOutputDefinitions(wookie, definitionType);
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getWookieByContentId(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getWookieByContentId(toolContentId).isDefineLater();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getWookieOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    /* ********** IWookieService Methods ********************************* */

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
    public Long getDefaultContentIdBySignature(String toolSignature) {
	Long toolContentId = null;
	toolContentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (toolContentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    WookieService.logger.error(error);
	    throw new WookieException(error);
	}
	return toolContentId;
    }

    @Override
    public Wookie getDefaultContent() {
	Long defaultContentID = getDefaultContentIdBySignature(WookieConstants.TOOL_SIGNATURE);
	Wookie defaultContent = getWookieByContentId(defaultContentID);
	if (defaultContent == null) {
	    String error = "Could not retrieve default content record for this tool";
	    WookieService.logger.error(error);
	    throw new WookieException(error);
	}
	return defaultContent;
    }

    @Override
    public Wookie copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the Wookie tools default content: + " + "newContentID is null";
	    WookieService.logger.error(error);
	    throw new WookieException(error);
	}

	Wookie defaultContent = getDefaultContent();
	// create new wookie using the newContentID
	Wookie newContent = new Wookie();
	newContent = Wookie.newInstance(defaultContent, newContentID);
	wookieDAO.saveOrUpdate(newContent);
	return newContent;
    }

    @Override
    public Wookie getWookieByContentId(Long toolContentID) {
	Wookie wookie = wookieDAO.getByContentId(toolContentID);
	if (wookie == null) {
	    WookieService.logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return wookie;
    }

    @Override
    public WookieSession getSessionBySessionId(Long toolSessionId) {
	WookieSession wookieSession = wookieSessionDAO.getBySessionId(toolSessionId);
	if (wookieSession == null) {
	    WookieService.logger.debug("Could not find the wookie session with toolSessionID:" + toolSessionId);
	}
	return wookieSession;
    }

    @Override
    public WookieUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return wookieUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    public WookieUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return wookieUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    @Override
    public WookieUser getUserByUID(Long uid) {
	return wookieUserDAO.getByUID(uid);
    }

    @Override
    public void saveOrUpdateWookie(Wookie wookie) {
	wookieDAO.saveOrUpdate(wookie);
    }

    @Override
    public void saveOrUpdateWookieSession(WookieSession wookieSession) {
	wookieSessionDAO.saveOrUpdate(wookieSession);
    }

    @Override
    public void saveOrUpdateWookieUser(WookieUser wookieUser) {
	wookieUserDAO.saveOrUpdate(wookieUser);
    }

    @Override
    public WookieUser createWookieUser(UserDTO user, WookieSession wookieSession) {
	WookieUser wookieUser = new WookieUser(user, wookieSession);
	saveOrUpdateWookieUser(wookieUser);
	return wookieUser;
    }

    @Override
    public WookieConfigItem getConfigItem(String key) {
	return wookieConfigItemDAO.getConfigItemByKey(key);
    }

    @Override
    public void saveOrUpdateWookieConfigItem(WookieConfigItem item) {
	wookieConfigItemDAO.saveOrUpdate(item);
    }

    @Override
    public String getWookieURL() {
	String url = null;
	WookieConfigItem urlItem = wookieConfigItemDAO.getConfigItemByKey(WookieConfigItem.KEY_WOOKIE_URL);
	if (urlItem != null) {
	    url = urlItem.getConfigValue();
	}
	return url;
    }

    @Override
    public String getWookieAPIKey() {
	String url = null;
	WookieConfigItem apiItem = wookieConfigItemDAO.getConfigItemByKey(WookieConfigItem.KEY_API);
	if (apiItem != null) {
	    url = apiItem.getConfigValue();
	}
	return url;
    }

    @Override
    public String getMessage(String key) {
	return messageService.getMessage(key);
    }

    /*
     * ===============Methods implemented from ToolContentImport102Manager
     * ===============
     */

    /**
     * Import the data for a 1.0.2 Wookie
     */
    @Override
    @SuppressWarnings("unchecked")
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	Wookie wookie = new Wookie();
	wookie.setContentInUse(Boolean.FALSE);
	wookie.setCreateBy(user.getUserID());
	wookie.setCreateDate(now);
	wookie.setDefineLater(Boolean.FALSE);
	wookie.setInstructions(
		WebUtil.convertNewlines((String) importValues.get(ToolContentImport102Manager.CONTENT_BODY)));
	wookie.setLockOnFinished(Boolean.TRUE);
	wookie.setTitle((String) importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
	wookie.setToolContentId(toolContentId);
	wookie.setUpdateDate(now);
	wookie.setReflectOnActivity(Boolean.FALSE);
	wookieDAO.saveOrUpdate(wookie);
    }

    /**
     * Set the description, throws away the title value as this is not supported in 2.0
     */
    @Override
    public void setReflectiveData(Long toolContentId, String title, String description)
	    throws ToolException, DataMissingException {

	WookieService.logger.warn(
		"Setting the reflective field on a wookie. This doesn't make sense as the wookie is for reflection and we don't reflect on reflection!");
	Wookie wookie = getWookieByContentId(toolContentId);
	if (wookie == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	wookie.setInstructions(description);
    }

    // =========================================================================================
    /* ********** Used by Spring to "inject" the linked objects ************* */

    public IWookieDAO getWookieDAO() {
	return wookieDAO;
    }

    public void setWookieDAO(IWookieDAO wookieDAO) {
	this.wookieDAO = wookieDAO;
    }

    public IToolContentHandler getWookieToolContentHandler() {
	return wookieToolContentHandler;
    }

    public void setWookieToolContentHandler(IToolContentHandler wookieToolContentHandler) {
	this.wookieToolContentHandler = wookieToolContentHandler;
    }

    public IWookieSessionDAO getWookieSessionDAO() {
	return wookieSessionDAO;
    }

    public void setWookieSessionDAO(IWookieSessionDAO sessionDAO) {
	wookieSessionDAO = sessionDAO;
    }

    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public IWookieUserDAO getWookieUserDAO() {
	return wookieUserDAO;
    }

    public void setWookieUserDAO(IWookieUserDAO userDAO) {
	wookieUserDAO = userDAO;
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

    public WookieOutputFactory getWookieOutputFactory() {
	return wookieOutputFactory;
    }

    public void setWookieOutputFactory(WookieOutputFactory wookieOutputFactory) {
	this.wookieOutputFactory = wookieOutputFactory;
    }

    public IWookieConfigItemDAO getWookieConfigItemDAO() {
	return wookieConfigItemDAO;
    }

    public void setWookieConfigItemDAO(IWookieConfigItemDAO wookieConfigItemDAO) {
	this.wookieConfigItemDAO = wookieConfigItemDAO;
    }

    public MessageService getMessageService() {
	return messageService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public IUserManagementService getUserManagementService() {
	return userManagementService;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

}
