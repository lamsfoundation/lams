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

package org.lamsfoundation.lams.tool.wiki.web.actions;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.wiki.dto.WikiPageContentDTO;
import org.lamsfoundation.lams.tool.wiki.dto.WikiPageDTO;
import org.lamsfoundation.lams.tool.wiki.model.Wiki;
import org.lamsfoundation.lams.tool.wiki.model.WikiAttachment;
import org.lamsfoundation.lams.tool.wiki.model.WikiPage;
import org.lamsfoundation.lams.tool.wiki.model.WikiPageContent;
import org.lamsfoundation.lams.tool.wiki.model.WikiUser;
import org.lamsfoundation.lams.tool.wiki.service.IWikiService;
import org.lamsfoundation.lams.tool.wiki.service.WikiServiceProxy;
import org.lamsfoundation.lams.tool.wiki.util.WikiConstants;
import org.lamsfoundation.lams.tool.wiki.web.forms.AuthoringForm;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * This action handles all the authoring actions, which include opening author, saving, uploading instruction files and
 * all the wikipage actions
 * 
 * It inherits from the WikiPageAction which inherits from the LamsDispatchAction so that common actions can be used in
 * learner, monitor and author
 * 
 * @author lfoxton
 * @version
 * 
 * @struts.action path="/authoring" name="authoringForm" parameter="dispatch" scope="request" validate="false"
 * @struts.action-forward name="success" path="tiles:/authoring/main"
 * @struts.action-forward name="compareWiki" path="/pages/wiki/compare.jsp"
 * @struts.action-forward name="viewWiki" path="/pages/wiki/viewWiki.jsp"
 * @struts.action-forward name="message_page" path="tiles:/generic/message"
 */
public class AuthoringAction extends WikiPageAction {

    private static Logger logger = Logger.getLogger(AuthoringAction.class);

    public IWikiService wikiService;

    // Authoring SessionMap key names
    private static final String KEY_TOOL_CONTENT_ID = "toolContentID";

    private static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";

    private static final String KEY_MODE = "mode";

    private static final String KEY_ONLINE_FILES = "onlineFiles";

    private static final String KEY_OFFLINE_FILES = "offlineFiles";

    private static final String KEY_UNSAVED_ONLINE_FILES = "unsavedOnlineFiles";

    private static final String KEY_UNSAVED_OFFLINE_FILES = "unsavedOfflineFiles";

    private static final String KEY_DELETED_FILES = "deletedFiles";

    /**
     * Default method when no dispatch parameter is specified. It is expected that the parameter
     * <code>toolContentID</code> will be passed in. This will be used to retrieve content for this tool.
     * 
     */
    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// Extract toolContentID from parameters.
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	ToolAccessMode mode = getAccessMode(request);

	// Set up the authForm.
	AuthoringForm authForm = (AuthoringForm) form;

	Long currentPageUid = authForm.getCurrentWikiPageId();

	// set up wikiService
	if (wikiService == null) {
	    wikiService = WikiServiceProxy.getWikiService(this.getServlet().getServletContext());
	}

	// retrieving Wiki with given toolContentID
	Wiki wiki = wikiService.getWikiByContentId(toolContentID);
	if (wiki == null) {
	    wiki = wikiService.copyDefaultContent(toolContentID);
	    wiki.setCreateDate(new Date());
	    wikiService.saveOrUpdateWiki(wiki);
	    // TODO NOTE: this causes DB orphans when LD not saved.
	}

	if (mode != null && mode.isTeacher()) {
	    // Set the defineLater flag so that learners cannot use content
	    // while we
	    // are editing. This flag is released when updateContent is called.
	    wiki.setDefineLater(true);
	    wikiService.saveOrUpdateWiki(wiki);
	}

	// update the form
	updateAuthForm(authForm, wiki);

	// Set the required main wiki page
	WikiPageDTO mainPageDTO = new WikiPageDTO(wiki.getMainPage());
	request.setAttribute(WikiConstants.ATTR_MAIN_WIKI_PAGE, mainPageDTO);

	// Set the current wiki page, if there is none, set to the main page
	WikiPage currentWikiPage = null;
	if (currentPageUid != null) {
	    currentWikiPage = wikiService.getWikiPageByUid(currentPageUid);
	} else {
	    // get real instance instead of lazily initialized handler
	    currentWikiPage = wiki.getMainPage(); 
	}
	WikiPageDTO currentPageDTO = new WikiPageDTO(currentWikiPage);
	request.setAttribute(WikiConstants.ATTR_CURRENT_WIKI, currentPageDTO);

	// Reset the isEditable field for the form
	authForm.setIsEditable(currentPageDTO.getEditable());

	// Set the current wiki history
	SortedSet<WikiPageContentDTO> currentWikiPageHistoryDTOs = new TreeSet<WikiPageContentDTO>();
	for (WikiPageContent wikiPageContentHistoryItem : currentWikiPage.getWikiContentVersions()) {
	    currentWikiPageHistoryDTOs.add(new WikiPageContentDTO(wikiPageContentHistoryItem));
	}
	request.setAttribute(WikiConstants.ATTR_WIKI_PAGE_CONTENT_HISTORY, currentWikiPageHistoryDTOs);

	// Get the child wiki pages
	SortedSet<WikiPageDTO> wikiPageDTOs = new TreeSet<WikiPageDTO>();
	for (WikiPage wikiPage : wiki.getWikiPages()) {
	    // check if page exists in real, not only phantom proxied object
	    // (happens after removing a wiki page)
	    wikiPage = wikiService.getWikiPageByUid(wikiPage.getUid());
	    if (wikiPage != null) {
		wikiPageDTOs.add(new WikiPageDTO(wikiPage));
	    }
	}
	request.setAttribute(WikiConstants.ATTR_WIKI_PAGES, wikiPageDTOs);

	// Set up sessionMap
	SessionMap<String, Object> map = createSessionMap(wiki, mode, contentFolderID, toolContentID);
	authForm.setSessionMapID(map.getSessionID());

	// add the sessionMap to HTTPSession.
	request.getSession().setAttribute(map.getSessionID(), map);
	request.setAttribute(WikiConstants.ATTR_SESSION_MAP, map);

	return mapping.findForward("success");
    }

    public ActionForward removePage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	Wiki wiki = wikiService.getWikiByContentId(toolContentID);
	if (wiki.isDefineLater()) {
	    // Only mark as removed if editing a live version (monitor/live edit)
	    return super.removePage(mapping, form, request, response);
	}
	// Completely delete the page
	Long currentPageUid = WebUtil.readLongParam(request, WikiConstants.ATTR_CURRENT_WIKI);

	// set up wikiService
	if (wikiService == null) {
	    wikiService = WikiServiceProxy.getWikiService(this.getServlet().getServletContext());
	}

	WikiPage wikiPage = wikiService.getWikiPageByUid(currentPageUid);
	wikiService.deleteWikiPage(wikiPage);

	// return to the main page, by setting the current page to null
	return this.returnToWiki(mapping, form, request, response, null);
    }

    /**
     * Wrapper method to make sure that the correct wiki is returned to from the WikiPageAction class
     */
    @Override
    protected ActionForward returnToWiki(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, Long currentWikiPageId) throws Exception {
	AuthoringForm authForm = (AuthoringForm) form;
	authForm.setCurrentWikiPageId(currentWikiPageId);
	return unspecified(mapping, authForm, request, response);
    }

    /**
     * Implements the abstract method, since this is author we return null here as there is no user
     */
    @Override
    public WikiUser getCurrentUser(Long toolSessionId) {
	// return null here to signify that this is in author
	return null;
    }

    /**
     * Saves the Wiki content including uploaded files and advance options
     * 
     * The WikiPage content is not saved here as that is done in the WikiPageAction
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// TODO need error checking.

	// get authForm and session map.
	AuthoringForm authForm = (AuthoringForm) form;
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	// get wiki content.
	Wiki wiki = wikiService.getWikiByContentId((Long) map.get(AuthoringAction.KEY_TOOL_CONTENT_ID));

	// update wiki content using form inputs.
	ToolAccessMode mode = (ToolAccessMode) map.get(AuthoringAction.KEY_MODE);
	updateWiki(wiki, authForm, mode);

	// remove attachments marked for deletion.
	Set<WikiAttachment> attachments = wiki.getWikiAttachments();
	if (attachments == null) {
	    attachments = new HashSet<WikiAttachment>();
	}

	for (WikiAttachment att : getAttList(AuthoringAction.KEY_DELETED_FILES, map)) {
	    // remove from db, leave in repository
	    attachments.remove(att);
	}

	// add unsaved attachments
	attachments.addAll(getAttList(AuthoringAction.KEY_UNSAVED_ONLINE_FILES, map));
	attachments.addAll(getAttList(AuthoringAction.KEY_UNSAVED_OFFLINE_FILES, map));

	// set attachments in case it didn't exist
	wiki.setWikiAttachments(attachments);

	// set the update date
	wiki.setUpdateDate(new Date());

	// releasing defineLater flag so that learner can start using the tool.
	wiki.setDefineLater(false);

	wikiService.saveOrUpdateWiki(wiki);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authForm.setSessionMapID(map.getSessionID());

	request.setAttribute(WikiConstants.ATTR_SESSION_MAP, map);

	return mapping.findForward("success");
    }

    public ActionForward uploadOnline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return uploadFile(mapping, (AuthoringForm) form, IToolContentHandler.TYPE_ONLINE, request);
    }

    public ActionForward uploadOffline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return uploadFile(mapping, (AuthoringForm) form, IToolContentHandler.TYPE_OFFLINE, request);
    }

    public ActionForward deleteOnline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return deleteFile(mapping, (AuthoringForm) form, IToolContentHandler.TYPE_ONLINE, request);
    }

    public ActionForward deleteOffline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return deleteFile(mapping, (AuthoringForm) form, IToolContentHandler.TYPE_OFFLINE, request);
    }

    public ActionForward removeUnsavedOnline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return removeUnsaved(mapping, (AuthoringForm) form, IToolContentHandler.TYPE_ONLINE, request);
    }

    public ActionForward removeUnsavedOffline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return removeUnsaved(mapping, (AuthoringForm) form, IToolContentHandler.TYPE_OFFLINE, request);
    }

    /* ========== Private Methods ********** */

    /**
     * Upload a file
     */
    private ActionForward uploadFile(ActionMapping mapping, AuthoringForm authForm, String type,
	    HttpServletRequest request) {
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	FormFile file;
	List<WikiAttachment> unsavedFiles;
	List<WikiAttachment> savedFiles;
	if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type)) {
	    file = authForm.getOfflineFile();
	    unsavedFiles = getAttList(AuthoringAction.KEY_UNSAVED_OFFLINE_FILES, map);

	    savedFiles = getAttList(AuthoringAction.KEY_OFFLINE_FILES, map);
	} else {
	    file = authForm.getOnlineFile();
	    unsavedFiles = getAttList(AuthoringAction.KEY_UNSAVED_ONLINE_FILES, map);

	    savedFiles = getAttList(AuthoringAction.KEY_ONLINE_FILES, map);
	}

	// validate file max size
	ActionMessages errors = new ActionMessages();
	FileValidatorUtil.validateFileSize(file, true, errors);
	if (!errors.isEmpty()) {
	    request.setAttribute(WikiConstants.ATTR_SESSION_MAP, map);
	    this.saveErrors(request, errors);
	    return mapping.findForward("success");
	}

	if (file.getFileName().length() != 0) {

	    // upload file to repository
	    WikiAttachment newAtt = wikiService.uploadFileToContent(
		    (Long) map.get(AuthoringAction.KEY_TOOL_CONTENT_ID), file, type);

	    // Add attachment to unsavedFiles
	    // check to see if file with same name exists
	    WikiAttachment currAtt;
	    Iterator iter = savedFiles.iterator();
	    while (iter.hasNext()) {
		currAtt = (WikiAttachment) iter.next();
		if (StringUtils.equals(currAtt.getFileName(), newAtt.getFileName())
			&& StringUtils.equals(currAtt.getFileType(), newAtt.getFileType())) {
		    // move from this this list to deleted list.
		    getAttList(AuthoringAction.KEY_DELETED_FILES, map).add(currAtt);
		    iter.remove();
		    break;
		}
	    }
	    unsavedFiles.add(newAtt);

	    request.setAttribute(WikiConstants.ATTR_SESSION_MAP, map);
	    request.setAttribute("unsavedChanges", new Boolean(true));
	}
	return mapping.findForward("success");
    }

    /**
     * Delete a file
     * 
     * @param mapping
     * @param authForm
     * @param type
     * @param request
     * @return
     */
    private ActionForward deleteFile(ActionMapping mapping, AuthoringForm authForm, String type,
	    HttpServletRequest request) {
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	List fileList;
	if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type)) {
	    fileList = getAttList(AuthoringAction.KEY_OFFLINE_FILES, map);
	} else {
	    fileList = getAttList(AuthoringAction.KEY_ONLINE_FILES, map);
	}

	Iterator iter = fileList.iterator();

	while (iter.hasNext()) {
	    WikiAttachment att = (WikiAttachment) iter.next();

	    if (att.getFileUuid().equals(authForm.getDeleteFileUuid())) {
		// move to delete file list, deleted at next updateContent
		getAttList(AuthoringAction.KEY_DELETED_FILES, map).add(att);

		// remove from this list
		iter.remove();
		break;
	    }
	}

	request.setAttribute(WikiConstants.ATTR_SESSION_MAP, map);
	request.setAttribute("unsavedChanges", new Boolean(true));

	return mapping.findForward("success");
    }

    private ActionForward removeUnsaved(ActionMapping mapping, AuthoringForm authForm, String type,
	    HttpServletRequest request) {
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	List unsavedFiles;

	if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type)) {
	    unsavedFiles = getAttList(AuthoringAction.KEY_UNSAVED_OFFLINE_FILES, map);
	} else {
	    unsavedFiles = getAttList(AuthoringAction.KEY_UNSAVED_ONLINE_FILES, map);
	}

	Iterator iter = unsavedFiles.iterator();
	while (iter.hasNext()) {
	    WikiAttachment att = (WikiAttachment) iter.next();

	    if (att.getFileUuid().equals(authForm.getDeleteFileUuid())) {
		// delete from repository and list
		wikiService.deleteFromRepository(att.getFileUuid(), att.getFileVersionId());
		iter.remove();
		break;
	    }
	}

	request.setAttribute(WikiConstants.ATTR_SESSION_MAP, map);
	request.setAttribute("unsavedChanges", new Boolean(true));

	return mapping.findForward("success");
    }

    /**
     * Updates Wiki content using AuthoringForm inputs.
     * 
     * @param authForm
     * @param mode
     * @return
     */
    private void updateWiki(Wiki wiki, AuthoringForm authForm, ToolAccessMode mode) {
	// wiki.setTitle(authForm.getTitle());
	WikiPage mainPage = wiki.getMainPage();
	// mainPage.setTitle(authForm.getTitle());

	WikiPageContent content = mainPage.getCurrentWikiContent();
	// content.setBody(authForm.getWikiBody());
	// mainPage.setCurrentWikiContent(content);

	wiki.setMainPage(mainPage);

	if (mode.isAuthor()) { // Teacher cannot modify following
	    wiki.setOfflineInstructions(authForm.getOfflineInstruction());
	    wiki.setOnlineInstructions(authForm.getOnlineInstruction());
	    wiki.setLockOnFinished(authForm.isLockOnFinished());
	    wiki.setAllowLearnerAttachImages(authForm.isAllowLearnerAttachImages());
	    wiki.setAllowLearnerCreatePages(authForm.isAllowLearnerCreatePages());
	    wiki.setAllowLearnerInsertLinks(authForm.isAllowLearnerInsertLinks());
	    wiki.setReflectOnActivity(authForm.isReflectOnActivity());
	    wiki.setReflectInstructions(authForm.getReflectInstructions());
	    wiki.setNotifyUpdates(authForm.isNotifyUpdates());
	    wiki.setMaximumEdits(authForm.getMaximumEdits());
	    wiki.setMinimumEdits(authForm.getMinimumEdits());
	}
    }

    /**
     * Updates AuthoringForm using Wiki content.
     * 
     * @param wiki
     * @param authForm
     * @return
     */
    private void updateAuthForm(AuthoringForm authForm, Wiki wiki) {
	authForm.setOnlineInstruction(wiki.getOnlineInstructions());
	authForm.setOfflineInstruction(wiki.getOfflineInstructions());
	authForm.setLockOnFinished(wiki.isLockOnFinished());
	authForm.setAllowLearnerAttachImages(wiki.isAllowLearnerAttachImages());
	authForm.setAllowLearnerCreatePages(wiki.isAllowLearnerCreatePages());
	authForm.setAllowLearnerInsertLinks(wiki.isAllowLearnerInsertLinks());
	authForm.setReflectOnActivity(wiki.isReflectOnActivity());
	authForm.setReflectInstructions(wiki.getReflectInstructions());
	authForm.setMaximumEdits(wiki.getMaximumEdits());
	authForm.setMinimumEdits(wiki.getMinimumEdits());
	authForm.setNewPageIsEditable(true);
	authForm.setNotifyUpdates(wiki.isNotifyUpdates());
    }

    /**
     * Updates SessionMap using Wiki content.
     * 
     * @param wiki
     * @param mode
     */
    private SessionMap<String, Object> createSessionMap(Wiki wiki, ToolAccessMode mode, String contentFolderID,
	    Long toolContentID) {

	SessionMap<String, Object> map = new SessionMap<String, Object>();

	map.put(AuthoringAction.KEY_MODE, mode);
	map.put(AuthoringAction.KEY_CONTENT_FOLDER_ID, contentFolderID);
	map.put(AuthoringAction.KEY_TOOL_CONTENT_ID, toolContentID);
	map.put(AuthoringAction.KEY_ONLINE_FILES, new LinkedList<WikiAttachment>());
	map.put(AuthoringAction.KEY_OFFLINE_FILES, new LinkedList<WikiAttachment>());
	map.put(AuthoringAction.KEY_UNSAVED_ONLINE_FILES, new LinkedList<WikiAttachment>());
	map.put(AuthoringAction.KEY_UNSAVED_OFFLINE_FILES, new LinkedList<WikiAttachment>());
	map.put(AuthoringAction.KEY_DELETED_FILES, new LinkedList<WikiAttachment>());

	Iterator iter = wiki.getWikiAttachments().iterator();
	while (iter.hasNext()) {
	    WikiAttachment attachment = (WikiAttachment) iter.next();
	    String type = attachment.getFileType();
	    if (type.equals(IToolContentHandler.TYPE_OFFLINE)) {
		getAttList(AuthoringAction.KEY_OFFLINE_FILES, map).add(attachment);
	    }
	    if (type.equals(IToolContentHandler.TYPE_ONLINE)) {
		getAttList(AuthoringAction.KEY_ONLINE_FILES, map).add(attachment);
	    }
	}

	return map;
    }

    /**
     * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR mode.
     * 
     * @param request
     * @return
     */
    private ToolAccessMode getAccessMode(HttpServletRequest request) {
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equalsIgnoreCase(modeStr, ToolAccessMode.TEACHER.toString())) {
	    return ToolAccessMode.TEACHER;
	}
	return ToolAccessMode.AUTHOR;
    }

    /**
     * Retrieves a List of attachments from the map using the key.
     * 
     * @param key
     * @param map
     * @return
     */
    private List<WikiAttachment> getAttList(String key, SessionMap<String, Object> map) {
	List<WikiAttachment> list = (List<WikiAttachment>) map.get(key);
	return list;
    }

    /**
     * Retrieve the SessionMap from the HttpSession.
     * 
     * @param request
     * @param authForm
     * @return
     */
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request, AuthoringForm authForm) {
	return (SessionMap<String, Object>) request.getSession().getAttribute(authForm.getSessionMapID());
    }
}
