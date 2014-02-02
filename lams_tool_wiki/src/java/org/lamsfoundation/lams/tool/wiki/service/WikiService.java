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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.wiki.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
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
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.wiki.dao.IWikiDAO;
import org.lamsfoundation.lams.tool.wiki.dao.IWikiPageContentDAO;
import org.lamsfoundation.lams.tool.wiki.dao.IWikiPageDAO;
import org.lamsfoundation.lams.tool.wiki.dao.IWikiSessionDAO;
import org.lamsfoundation.lams.tool.wiki.dao.IWikiUserDAO;
import org.lamsfoundation.lams.tool.wiki.model.Wiki;
import org.lamsfoundation.lams.tool.wiki.model.WikiPage;
import org.lamsfoundation.lams.tool.wiki.model.WikiPageContent;
import org.lamsfoundation.lams.tool.wiki.model.WikiSession;
import org.lamsfoundation.lams.tool.wiki.model.WikiUser;
import org.lamsfoundation.lams.tool.wiki.util.WikiConstants;
import org.lamsfoundation.lams.tool.wiki.util.WikiException;
import org.lamsfoundation.lams.tool.wiki.util.diff.Diff;
import org.lamsfoundation.lams.tool.wiki.util.diff.Difference;
import org.lamsfoundation.lams.tool.wiki.web.forms.WikiPageForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;

/**
 * An implementation of the IWikiService interface.
 * 
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 */
public class WikiService implements ToolSessionManager, ToolContentManager, IWikiService, ToolContentImport102Manager {

    static Logger logger = Logger.getLogger(WikiService.class.getName());

    private IWikiDAO wikiDAO = null;

    private IWikiPageDAO wikiPageDAO = null;

    private IWikiPageContentDAO wikiPageContentDAO = null;

    private IWikiSessionDAO wikiSessionDAO = null;

    private IWikiUserDAO wikiUserDAO = null;

    private ILearnerService learnerService;

    private ILamsToolService toolService;

    private IToolContentHandler wikiToolContentHandler = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private WikiOutputFactory wikiOutputFactory;

    private IEventNotificationService eventNotificationService;

    private MessageService messageService;

    private ILessonService lessonService;

    public WikiService() {
	super();
    }

    /* ************ Methods from ToolSessionManager ************* */

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#createToolSession(java.lang.Long, java.lang.String,
     *      java.lang.Long)
     */
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (WikiService.logger.isDebugEnabled()) {
	    WikiService.logger.debug("entering method createToolSession:" + " toolSessionId = " + toolSessionId
		    + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
	}

	// create a new tool session
	WikiSession session = new WikiSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);

	// Get the wiki content to start a session
	Wiki wiki = wikiDAO.getByContentId(toolContentId);
	session.setWiki(wiki);

	// Create an empty list to copy the wiki pages into
	Set<WikiPage> sessionWikiPages = new HashSet<WikiPage>();
	
	// Here we need to clone wikipages and content for tool session versions
	//for (WikiPage childPage : wiki.getWikiPages()) {  // LDEV-2436
	for (WikiPage childPage : (List<WikiPage>) wikiDAO.findByProperty(WikiPage.class, "parentWiki", wiki)) {
	
	    // check that this page does not already have a session
	    if (childPage.getWikiSession() != null) {
		continue;
	    }

	    // Clone the wiki page
	    WikiPage newChildPage = (WikiPage) childPage.clone();
	    wikiPageDAO.saveOrUpdate(newChildPage);

	    // Clone the current content - leave the history null
	    WikiPageContent newPageContent = (WikiPageContent) childPage.getCurrentWikiContent().clone();
	    newPageContent.setWikiPage(newChildPage);
	    wikiPageContentDAO.saveOrUpdate(newPageContent);

	    // Set the current content
	    newChildPage.setCurrentWikiContent(newPageContent);
	    newChildPage.setParentWiki(wiki);
	    newChildPage.setWikiSession(session);
	    newChildPage.getWikiContentVersions().add(newPageContent);

	    // if childpage is the main page, set the session main page
	    if (newChildPage.getTitle().equals(wiki.getMainPage().getTitle())) {
		session.setMainPage(newChildPage);
	    }

	    // Add page to the list
	    sessionWikiPages.add(newChildPage);
	}
	session.setWikiPages(sessionWikiPages);
	wikiSessionDAO.saveOrUpdate(session);
    }

    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	return learnerService.completeToolSession(toolSessionId, learnerId);
    }

    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	return null;
    }

    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException,
	    ToolException {
	return null;
    }

    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	wikiSessionDAO.deleteBySessionID(toolSessionId);
    }

    /**
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long,
     *      java.lang.Long)
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {

	wikiOutputFactory = getWikiOutputFactory();
	WikiSession session = this.getSessionBySessionId(toolSessionId);
	if (session == null) {
	    return null;
	}
	return wikiOutputFactory.getToolOutput(names, this, toolSessionId, learnerId);
    }

    /**
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long,
     *      java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	wikiOutputFactory = getWikiOutputFactory();
	WikiSession session = this.getSessionBySessionId(toolSessionId);
	if (session == null) {
	    return null;
	}
	return wikiOutputFactory.getToolOutput(name, this, toolSessionId, learnerId);
    }

    /**
     * Get the definitions for possible output for an activity, based on the toolContentId.
     * 
     * @return SortedMap of ToolOutputDefinitions with the key being the name of each definition
     */
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	wikiOutputFactory = getWikiOutputFactory();
	Wiki wiki = getWikiByContentId(toolContentId);
	if (wiki == null) {
	    wiki = getDefaultContent();
	}
	return wikiOutputFactory.getToolOutputDefinitions(wiki, definitionType);
    }
    
    public String getToolContentTitle(Long toolContentId) {
	return getWikiByContentId(toolContentId).getTitle();
    }
   
    /* ************ Methods from ToolContentManager ************************* */

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.ToolContentManager#copyToolContent(java.lang.Long, java.lang.Long)
     */
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {

	if (WikiService.logger.isDebugEnabled()) {
	    WikiService.logger.debug("entering method copyToolContent:" + " fromContentId=" + fromContentId
		    + " toContentId=" + toContentId);
	}

	if (toContentId == null) {
	    String error = "Failed to copy tool content: toContentID is null";
	    throw new ToolException(error);
	}

	Wiki fromContent = null;
	if (fromContentId != null) {
	    fromContent = wikiDAO.getByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}
	Wiki toContent = Wiki.newInstance(fromContent, toContentId);

	insertUnsavedWikiContent(toContent);
    }

    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException {
	// TODO Auto-generated method stub
    }
    
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (logger.isDebugEnabled()) {
	    logger.debug("Removing Wiki contents for user ID " + userId + " and toolContentId " + toolContentId);
	}

	Wiki wiki = wikiDAO.getByContentId(toolContentId);
	if (wiki == null) {
	    logger.warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	for (WikiSession session : wiki.getWikiSessions()) {
	    for (WikiPage page : session.getWikiPages()) {
		if (page.getAddedBy() != null && page.getAddedBy().getUserId().equals(userId.longValue())) {
		    page.setDeleted(true);
		    wikiPageDAO.update(page);
		}
	    }

	    WikiUser user = wikiUserDAO.getByUserIdAndSessionId(userId.longValue(), session.getSessionId());
	    if (user != null) {
		if (user.getEntryUID() != null) {
		    NotebookEntry entry = coreNotebookService.getEntry(user.getEntryUID());
		    wikiDAO.delete(entry);
		    user.setEntryUID(null);
		}
		user.setFinishedActivity(false);
		user.setWikiEdits(0);
		wikiUserDAO.update(user);
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
	Wiki wiki = wikiDAO.getByContentId(toolContentId);
	if (wiki == null) {
	    wiki = getDefaultContent();
	}
	if (wiki == null) {
	    throw new DataMissingException("Unable to find default content for the wiki tool");
	}

	// set ResourceToolContentHandler as null to avoid copy file node in
	// repository again.
	wiki = Wiki.newInstance(wiki, toolContentId);
	wiki.setWikiSessions(null);

	// Go through each page add page content and remove parent references
	Set<WikiPage> wikiPages = wiki.getWikiPages();
	for (WikiPage page : wikiPages) {
	    page.setParentWiki(null);
	    page.setWikiContentVersions(null);
	    page.setWikiSession(null);
	    WikiPageContent content = page.getCurrentWikiContent();
	    content.setWikiPage(null);
	    content.setEditDate(null);
	    content.setEditor(null);
	    page.setCurrentWikiContent(content);
	}

	// Null the main page (apart from its title for identification)
	WikiPage mainPage = new WikiPage();
	mainPage.setTitle(wiki.getMainPage().getTitle());
	wiki.setMainPage(mainPage);

	try {
	    exportContentService.exportToolContent(toolContentId, wiki, wikiToolContentHandler, rootPath);
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
	    exportContentService.registerImportVersionFilterClass(WikiContentVersionFilter.class);
	
	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, wikiToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Wiki)) {
		throw new ImportToolContentException("Import Wiki tool content failed. Deserialized object is "
			+ toolPOJO);
	    }
	    Wiki wiki = (Wiki) toolPOJO;

	    // reset it to new toolContentId
	    wiki.setToolContentId(toolContentId);
	    wiki.setCreateBy(new Long(newUserUid.longValue()));

	    // Making sure the wiki titles do not have trailing newline characters
	    for (WikiPage wikiPage : wiki.getWikiPages()) {
		String title = wikiPage.getTitle();
		title = title.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", "");
		wikiPage.setTitle(title);
	    }

	    insertUnsavedWikiContent(wiki);

	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    /* ********** IWikiService Methods ********************************* */

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.wiki.service.IWikiService#createNotebookEntry(java.lang.Long,
     *      java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.String)
     */
    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry) {
	return coreNotebookService.createNotebookEntry(id, idType, signature, userID, "", entry);
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.wiki.service.IWikiService#getEntry(org.lamsfoundation.lams.notebook.model.NotebookEntry)
     */
    public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID) {
	List<NotebookEntry> list = coreNotebookService.getEntry(sessionId, idType, signature, userID);
	if (list == null || list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.wiki.service.IWikiService#updateEntry(org.lamsfoundation.lams.notebook.model.NotebookEntry)
     */
    public void updateEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.wiki.service.IWikiService#comparePages(String,String)
     */
    public String comparePages(String old, String current) {
	String oldArray[] = old.replaceAll("[\\t\\n\\r]", "").split("<div>");
	String currentArray[] = current.replaceAll("[\\t\\n\\r]", "").split("<div>");

	Diff diff = new Diff(oldArray, currentArray);
	List<Difference> diffOut = diff.diff();

	LinkedList<String> result = new LinkedList<String>(Arrays.asList(currentArray));

	int resultOffset = 0;
	for (Difference difference : diffOut) {
	    if (difference.getDeletedEnd() == -1) {
		// Added
		for (int i = difference.getAddedStart(); i <= difference.getAddedEnd(); i++) {
		    result.set(i + resultOffset, "<div style='background-color:#99FFCC; width: 90%;'> "
			    + currentArray[i]);
		}
	    } else if (difference.getAddedEnd() == -1) {
		// Deleted
		for (int i = difference.getDeletedStart(); i <= difference.getDeletedEnd(); i++) {
		    if (result.size() > i + resultOffset) {
			result.add(i + resultOffset, "<div style='background-color:#FF9999; width: 90%;'>"
				+ oldArray[i]);
		    } else {
			result.add("<div style='background-color:#FF9999; width: 90%;'>" + oldArray[i]);
		    }
		    resultOffset++;
		}
	    } else {

		// Replaced
		for (int i = difference.getAddedStart(); i <= difference.getAddedEnd(); i++) {
		    result.set(i + resultOffset, "<div style='background-color:#99FFCC; width: 90%;'>"
			    + currentArray[i]);
		}
		for (int i = difference.getDeletedStart(); i <= difference.getDeletedEnd(); i++) {
		    if (result.size() > i + resultOffset) {
			result.add(i + resultOffset, "<div style='background-color:#FF9999; width: 90%;'>"
				+ oldArray[i]);
		    } else {
			result.add("<div style='background-color:#FF9999; width: 90%;'>" + oldArray[i]);
		    }
		    resultOffset++;
		}
	    }
	}

	StringBuffer retBuf = new StringBuffer();
	for (String line : result) {
	    line = line.replaceAll("[//r][//n][//t]", "");

	    // fix up lines that dont have the div tag on them
	    if (!line.startsWith("<div")) {
		retBuf.append("<div>");
	    }

	    retBuf.append(line);

	    // fix up lines that dont have the div tag on them
	    if (!line.contains("</div>")) {
		retBuf.append("</div>");
	    }
	}
	WikiService.logger.debug("Result:");
	WikiService.logger.debug(retBuf);
	return retBuf.toString();
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.wiki.service.IWikiService#getDefaultContentIdBySignature(String)
     */
    public Long getDefaultContentIdBySignature(String toolSignature) {
	Long toolContentId = null;
	toolContentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (toolContentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    WikiService.logger.error(error);
	    throw new WikiException(error);
	}
	return toolContentId;
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.wiki.service.IWikiService#getDefaultContent()
     */
    public Wiki getDefaultContent() {
	Long defaultContentID = getDefaultContentIdBySignature(WikiConstants.TOOL_SIGNATURE);
	Wiki defaultContent = getWikiByContentId(defaultContentID);
	if (defaultContent == null) {
	    String error = "Could not retrieve default content record for this tool";
	    WikiService.logger.error(error);
	    throw new WikiException(error);
	}
	return defaultContent;
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.wiki.service.IWikiService#copyDefaultContent(Long)
     */
    public Wiki copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the Wiki tools default content: + " + "newContentID is null";
	    WikiService.logger.error(error);
	    throw new WikiException(error);
	}

	Wiki defaultContent = getDefaultContent();
	Wiki newContent = Wiki.newInstance(defaultContent, newContentID);

	insertUnsavedWikiContent(newContent);

	return newContent;
    }

    /**
     * Takes a transient wiki object and iterates down the tree ensureing each object is saved to the db and all
     * references are maintained
     * 
     * @param wiki
     * @return
     */
    private Wiki insertUnsavedWikiContent(Wiki wiki) {

	wikiDAO.saveOrUpdate(wiki);
	// Go through the wiki object and save all the pages and content
	for (WikiPage wikiPage : wiki.getWikiPages()) {
	    WikiPageContent currentContent = wikiPage.getCurrentWikiContent();
	    currentContent.setEditDate(new Date());
	    wikiPageContentDAO.saveOrUpdate(currentContent);

	    currentContent.setWikiPage(wikiPage);
	    wikiPage.setCurrentWikiContent(currentContent);
	    wikiPageDAO.saveOrUpdate(wikiPage);

	    if (wiki.getMainPage().getTitle() == wikiPage.getTitle()) {
		wiki.setMainPage(wikiPage);
	    }

	    wiki.getWikiPages().add(wikiPage);
	}

	// Update the wiki pages to reference their parent
	for (WikiPage wikiPage : wiki.getWikiPages()) {
	    wikiPage.setParentWiki(wiki);
	    wikiPageDAO.saveOrUpdate(wikiPage);
	}

	return wiki;
    }

    public Wiki getWikiByContentId(Long toolContentID) {
	Wiki wiki = wikiDAO.getByContentId(toolContentID);
	if (wiki == null) {
	    WikiService.logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return wiki;
    }

    public WikiSession getSessionBySessionId(Long toolSessionId) {
	WikiSession wikiSession = wikiSessionDAO.getBySessionId(toolSessionId);
	if (wikiSession == null) {
	    WikiService.logger.debug("Could not find the wiki session with toolSessionID:" + toolSessionId);
	}
	return wikiSession;
    }

    public WikiUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return wikiUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    public WikiUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return wikiUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    public WikiUser getUserByUID(Long uid) {
	return wikiUserDAO.getByUID(uid);
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.wiki.service.IWikiService#updateWikiPage(org.lamsfoundation.lams.tool.wiki.web.forms.WikiPageForm,
     *      org.lamsfoundation.lams.tool.wiki.model.WikiPage, org.lamsfoundation.lams.tool.wiki.model.WikiUser)
     */
    public void updateWikiPage(WikiPageForm wikiPageForm, WikiPage wikiPage, WikiUser user) {

	if (wikiPage == null) {
	    throw new WikiException("Could not find wiki page to update");
	}

	// Create a new wiki page content using the wiki page form
	WikiPageContent wikiPageContent = new WikiPageContent();
	wikiPageContent.setBody(wikiPageForm.getWikiBody());
	wikiPageContent.setEditDate(new Date());
	wikiPageContent.setEditor(user);
	wikiPageContent.setVersion(wikiPage.getCurrentWikiContent().getVersion() + 1);
	wikiPageContent.setWikiPage(wikiPage);
	wikiPageContentDAO.saveOrUpdate(wikiPageContent);

	// Now save the wiki Page
	wikiPage.setTitle(wikiPageForm.getTitle());
	wikiPage.setEditable(wikiPageForm.getIsEditable());
	wikiPage.setCurrentWikiContent(wikiPageContent);
	wikiPage.getWikiContentVersions().add(wikiPageContent);
	wikiPage.setEditable(wikiPageForm.getIsEditable());
	wikiPageContentDAO.saveOrUpdate(wikiPageContent);

	// Update the user's edits
	if (user != null) {
	    user.setWikiEdits(user.getWikiEdits() + 1);
	    wikiUserDAO.saveOrUpdate(user);
	}
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.wiki.service.IWikiService#insertWikiPage(org.lamsfoundation.lams.tool.wiki.web.forms.WikiPageForm,
     *      org.lamsfoundation.lams.tool.wiki.model.Wiki, org.lamsfoundation.lams.tool.wiki.model.WikiUser,
     *      org.lamsfoundation.lams.tool.wiki.model.WikiSession)
     */
    public Long insertWikiPage(WikiPageForm wikiPageForm, Wiki wiki, WikiUser user, WikiSession session) {

	// First create a new wiki page
	WikiPage wikiPage = new WikiPage();
	wikiPage.setEditable(wikiPageForm.getIsEditable());
	wikiPage.setParentWiki(wiki);
	wikiPage.setTitle(wikiPageForm.getNewPageTitle());
	wikiPage.setEditable(wikiPageForm.getNewPageIsEditable());
	wikiPage.setAddedBy(user);
	wikiPage.setWikiContentVersions(new HashSet<WikiPageContent>());
	wikiPage.setWikiSession(session);
	wikiPageDAO.saveOrUpdate(wikiPage);

	// Create a new wiki page content using the wiki page form
	WikiPageContent wikiPageContent = new WikiPageContent();
	wikiPageContent.setBody(wikiPageForm.getNewPageWikiBody());
	wikiPageContent.setEditDate(new Date());
	wikiPageContent.setEditor(user);
	wikiPageContent.setVersion(new Long(1));
	wikiPageContent.setWikiPage(wikiPage);
	wikiPageContentDAO.saveOrUpdate(wikiPageContent);

	// Apply the content to the wiki page and save
	wikiPage.setCurrentWikiContent(wikiPageContent);
	wikiPage.getWikiContentVersions().add(wikiPageContent);
	wikiPageDAO.saveOrUpdate(wikiPage);

	// Update the user's edits
	if (user != null) {
	    user.setWikiEdits(user.getWikiEdits() + 1);
	    wikiUserDAO.saveOrUpdate(user);
	}

	return wikiPage.getUid();
    }

    public void deleteWikiPage(WikiPage wikiPage) {
	wikiPageDAO.delete(wikiPage);
    }
    
    public void markWikiPageAsDeleted(WikiPage wikiPage) {
	wikiPage.setDeleted(true);
	wikiPageDAO.saveOrUpdate(wikiPage);
    }
    
    public void restoreWikiPage(WikiPage wikiPage) {
	wikiPage.setDeleted(false);
	wikiPageDAO.saveOrUpdate(wikiPage);
    }

    public void saveOrUpdateWiki(Wiki wiki) {
	wikiDAO.saveOrUpdate(wiki);
    }

    public void saveOrUpdateWikiPage(WikiPage wikiPage) {
	wikiPageDAO.saveOrUpdate(wikiPage);
    }

    public WikiPage getWikiPageByWikiAndTitle(Wiki wiki, String title) {
	return wikiPageDAO.getByWikiAndTitle(wiki, title);
    }

    public WikiPage getWikiBySessionAndTitle(WikiSession wikiSession, String title) {
	return wikiPageDAO.getBySessionAndTitle(wikiSession, title);
    }

    public WikiPage getWikiPageByUid(Long uid) {
	List list = wikiPageDAO.findByProperty(WikiPage.class, "uid", uid);
	if (list == null || list.size() == 0) {
	    return null;
	} else {
	    return (WikiPage) list.get(0);
	}
    }

    public WikiPageContent getWikiPageContent(Long uid) {
	List list = wikiPageContentDAO.findByProperty(WikiPageContent.class, "uid", uid);
	if (list == null || list.size() == 0) {
	    return null;
	} else {
	    return (WikiPageContent) list.get(0);
	}
    }

    public void saveOrUpdateWikiPageContent(WikiPageContent wikiPageContent) {
	wikiPageContentDAO.saveOrUpdate(wikiPageContent);
    }

    public void saveOrUpdateWikiSession(WikiSession wikiSession) {
	wikiSessionDAO.saveOrUpdate(wikiSession);
    }

    public void saveOrUpdateWikiUser(WikiUser wikiUser) {
	wikiUserDAO.saveOrUpdate(wikiUser);
    }

    public WikiUser createWikiUser(UserDTO user, WikiSession wikiSession) {
	WikiUser wikiUser = new WikiUser(user, wikiSession);
	saveOrUpdateWikiUser(wikiUser);
	return wikiUser;
    }
    
    @Override
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }
    
    @Override
    public String getLearnerContentFolder(Long toolSessionId, Long userId) {
	return toolService.getLearnerContentFolder(toolSessionId, userId);
    }

    /**
     * 
     * @param learnerId
     * @param toolSessionId
     * @return
     */
    public int getEditsNum(Long learnerId, Long toolSessionId) {
	WikiUser wikiUser = getUserByUserIdAndSessionId(learnerId, toolSessionId);

	WikiSession wikiSession = getSessionBySessionId(toolSessionId);

	int edits = 0;
	for (WikiPage wikiPage : wikiSession.getWikiPages()) {
	    for (WikiPageContent wikiPageContent : wikiPage.getWikiContentVersions()) {
		if (wikiPageContent.getEditor() != null
			&& wikiPageContent.getEditor().getUid().equals(wikiUser.getUid())) {
		    edits++;
		}
	    }
	}
	return edits;
    }

    /**
     * 
     * @param learnerId
     * @param toolSessionId
     * @return
     */
    public int getAddsNum(Long learnerId, Long toolSessionId) {

	WikiUser wikiUser = getUserByUserIdAndSessionId(learnerId, toolSessionId);

	WikiSession wikiSession = getSessionBySessionId(toolSessionId);

	int adds = 0;
	for (WikiPage wikiPage : wikiSession.getWikiPages()) {
	    if (wikiPage.getAddedBy() != null && wikiPage.getAddedBy().getUid().equals(wikiUser.getUid())) {
		adds++;
	    }
	}
	return adds;
    }

    /*
     * ===============Methods implemented from ToolContentImport102Manager ===============
     */

    /**
     * Import the data for a 1.0.2 Wiki
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	Wiki wiki = new Wiki();
	wiki.setContentInUse(Boolean.FALSE);
	wiki.setCreateBy(new Long(user.getUserID().longValue()));
	wiki.setCreateDate(now);
	wiki.setDefineLater(Boolean.FALSE);
	wiki.setInstructions(WebUtil.convertNewlines((String) importValues
		.get(ToolContentImport102Manager.CONTENT_BODY)));
	wiki.setLockOnFinished(Boolean.TRUE);
	wiki.setTitle((String) importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
	wiki.setToolContentId(toolContentId);
	wiki.setUpdateDate(now);
	wiki.setAllowLearnerAttachImages(Boolean.TRUE);
	wiki.setAllowLearnerCreatePages(Boolean.TRUE);
	wiki.setAllowLearnerInsertLinks(Boolean.TRUE);
	wiki.setReflectOnActivity(Boolean.FALSE);
	wiki.setReflectInstructions(null);
	wiki.setMaximumEdits(0);
	wiki.setMinimumEdits(0);

	wikiDAO.saveOrUpdate(wiki);
    }

    /**
     * Set the description, throws away the title value as this is not supported in 2.0
     */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	WikiService.logger
		.warn("Setting the reflective field on a wiki. This doesn't make sense as the wiki is for reflection and we don't reflect on reflection!");
	Wiki wiki = getWikiByContentId(toolContentId);
	if (wiki == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	wiki.setInstructions(description);
    }

    // =========================================================================================
    /* ********** Used by Spring to "inject" the linked objects ************* */

    public IWikiDAO getWikiDAO() {
	return wikiDAO;
    }

    public void setWikiDAO(IWikiDAO wikiDAO) {
	this.wikiDAO = wikiDAO;
    }

    public IWikiPageDAO getWikiPageDAO() {
	return wikiPageDAO;
    }

    public void setWikiPageDAO(IWikiPageDAO wikiPageDAO) {
	this.wikiPageDAO = wikiPageDAO;
    }

    public IWikiPageContentDAO getWikiPageContentDAO() {
	return wikiPageContentDAO;
    }

    public void setWikiPageContentDAO(IWikiPageContentDAO wikiPageContentDAO) {
	this.wikiPageContentDAO = wikiPageContentDAO;
    }

    public IToolContentHandler getWikiToolContentHandler() {
	return wikiToolContentHandler;
    }

    public void setWikiToolContentHandler(IToolContentHandler wikiToolContentHandler) {
	this.wikiToolContentHandler = wikiToolContentHandler;
    }

    public IWikiSessionDAO getWikiSessionDAO() {
	return wikiSessionDAO;
    }

    public void setWikiSessionDAO(IWikiSessionDAO sessionDAO) {
	wikiSessionDAO = sessionDAO;
    }

    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public IWikiUserDAO getWikiUserDAO() {
	return wikiUserDAO;
    }

    public void setWikiUserDAO(IWikiUserDAO userDAO) {
	wikiUserDAO = userDAO;
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

    public WikiOutputFactory getWikiOutputFactory() {

	if (wikiOutputFactory == null) {
	    wikiOutputFactory = new WikiOutputFactory();
	}
	return wikiOutputFactory;
    }

    public void setWikiOutputFactory(WikiOutputFactory wikiOutputFactory) {
	this.wikiOutputFactory = wikiOutputFactory;
    }

    public IEventNotificationService getEventNotificationService() {
	return eventNotificationService;
    }

    public void setEventNotificationService(IEventNotificationService eventNotificationService) {
	this.eventNotificationService = eventNotificationService;
    }

    public MessageService getMessageService() {
	return messageService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public String getLocalisedMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
    }

    public List<User> getMonitorsByToolSessionId(Long sessionId) {
	return getLessonService().getMonitorsByToolSessionId(sessionId);
    }

    public ILessonService getLessonService() {
	return lessonService;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getWikiOutputFactory().getSupportedDefinitionClasses(definitionType);
    }
}
