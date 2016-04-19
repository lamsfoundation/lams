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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.wiki.dto.WikiPageContentDTO;
import org.lamsfoundation.lams.tool.wiki.dto.WikiPageDTO;
import org.lamsfoundation.lams.tool.wiki.model.Wiki;
import org.lamsfoundation.lams.tool.wiki.model.WikiPage;
import org.lamsfoundation.lams.tool.wiki.model.WikiPageContent;
import org.lamsfoundation.lams.tool.wiki.model.WikiSession;
import org.lamsfoundation.lams.tool.wiki.model.WikiUser;
import org.lamsfoundation.lams.tool.wiki.service.IWikiService;
import org.lamsfoundation.lams.tool.wiki.service.WikiServiceProxy;
import org.lamsfoundation.lams.tool.wiki.util.WikiConstants;
import org.lamsfoundation.lams.tool.wiki.web.forms.WikiPageForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * An abstract class used for wiki actions common to monitor, learner and author
 * 
 * @author lfoxton
 * 
 */
public abstract class WikiPageAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(AuthoringAction.class);

    public IWikiService wikiService;

    /**
     * Default method when no dispatch parameter is specified.
     */
    @Override
    protected abstract ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception;

    /**
     * This action returns to the current wiki by updating the form accordingly
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    protected abstract ActionForward returnToWiki(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, Long currentWikiPageId) throws Exception;

    protected abstract WikiUser getCurrentUser(Long toolSessionId);

    /**
     * Edit a page and make a new page content entry
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward editPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Long currentPageUid = WebUtil.readLongParam(request, WikiConstants.ATTR_CURRENT_WIKI);

	// set up wikiService
	if (wikiService == null) {
	    wikiService = WikiServiceProxy.getWikiService(this.getServlet().getServletContext());
	}

	// Set up the wiki form
	WikiPageForm wikiForm = (WikiPageForm) form;
	revertJavascriptTokenReplacement(wikiForm);

	// Get the current wiki page
	WikiPage currentPage = wikiService.getWikiPageByUid(currentPageUid);

	// Check if the content is different
	if (!currentPage.getCurrentWikiContent().getBody().equals(wikiForm.getWikiBody())
		|| !currentPage.getTitle().equals(wikiForm.getTitle())) {

	    // Set up the wiki user if this is a tool session (learner)
	    // Also set the editable flag here
	    Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID, true);
	    WikiUser user = null;
	    if (toolSessionID != null) {
		user = this.getCurrentUser(toolSessionID);
	    }

	    // Setting the editable flag based on call origin
	    ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);
	    if ((mode == null) || (mode == ToolAccessMode.TEACHER)) {

		// Author/Monitor/Live edit
		// If no editable flag came in the form (as in learner), set
		// false
		if (wikiForm.getIsEditable() == null) {
		    wikiForm.setIsEditable(false);
		}
	    } else {
		// Learner or preview If no editable flag came in the form
		// (as in learner), set true
		if (wikiForm.getIsEditable() == null) {
		    wikiForm.setIsEditable(true);
		}
	    }
	    // Updating the wikiPage, setting a null user which indicated
	    // this change was made in author
	    wikiService.updateWikiPage(wikiForm, currentPage, user);

	    // Send edit notifications
	    if ((toolSessionID != null) && (user != null)) {
		notifyWikiChange(toolSessionID, "notify.pageEdited.subject", "notify.pageEdited.body", user, request);
	    }

	}

	// Make sure the current page is set correctly then return to the wiki
	return returnToWiki(mapping, wikiForm, request, response, currentPageUid);
    }

    /**
     * Revert to a previous page content in the page history
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward revertPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Long revertPageContentVersion = new Long(
		WebUtil.readLongParam(request, WikiConstants.ATTR_HISTORY_PAGE_CONTENT_ID));
	Long currentPageUid = WebUtil.readLongParam(request, WikiConstants.ATTR_CURRENT_WIKI);

	// set up wikiService
	if (wikiService == null) {
	    wikiService = WikiServiceProxy.getWikiService(this.getServlet().getServletContext());
	}

	// Get the current wiki page
	WikiPage currentPage = wikiService.getWikiPageByUid(currentPageUid);

	// Set up the wiki user if this is a tool session (learner)
	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID, true);
	WikiUser user = null;
	if (toolSessionID != null) {
	    user = this.getCurrentUser(toolSessionID);
	}

	WikiPageContent content = wikiService.getWikiPageContent(revertPageContentVersion);

	// Set up the authoring form
	WikiPageForm wikiForm = (WikiPageForm) form;

	// Set the wiki body in the authform
	wikiForm.setWikiBody(content.getBody());
	wikiForm.setIsEditable(currentPage.getEditable());

	// Updating the wikiPage, setting a null user which indicated this
	// change was made in author
	wikiService.updateWikiPage(wikiForm, currentPage, user);

	// Send revert notifications
	if ((toolSessionID != null) && (user != null)) {
	    notifyWikiChange(toolSessionID, "notify.pageEdited.subject", "notify.pageEdited.body", user, request);
	}

	return unspecified(mapping, wikiForm, request, response);
    }

    /**
     * Compare two page content history items and return the result
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward comparePage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Long revertPageContentVersion = new Long(
		WebUtil.readLongParam(request, WikiConstants.ATTR_HISTORY_PAGE_CONTENT_ID));
	Long currentPageUid = WebUtil.readLongParam(request, WikiConstants.ATTR_CURRENT_WIKI);

	// set up wikiService
	if (wikiService == null) {
	    wikiService = WikiServiceProxy.getWikiService(this.getServlet().getServletContext());
	}

	// Get the current wiki page content
	WikiPage currentWikiPage = wikiService.getWikiPageByUid(currentPageUid);
	WikiPageContent currentContent = currentWikiPage.getCurrentWikiContent();

	// Get the old wiki content to compare
	WikiPageContent compareContent = wikiService.getWikiPageContent(revertPageContentVersion);

	// Do the compariason
	String diff = wikiService.comparePages(compareContent.getBody(), currentContent.getBody());

	request.setAttribute(WikiConstants.ATTR_COMPARE_VERSIONS,
		compareContent.getVersion().toString() + "-" + currentContent.getVersion().toString());
	request.setAttribute(WikiConstants.ATTR_COMPARE_TITLE, currentWikiPage.getTitle());
	request.setAttribute(WikiConstants.ATTR_COMPARE_STRING, diff);

	return mapping.findForward("compareWiki");
    }

    /**
     * View a page content from a wiki page's history
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward viewPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Long revertPageContentVersion = new Long(
		WebUtil.readLongParam(request, WikiConstants.ATTR_HISTORY_PAGE_CONTENT_ID));
	Long currentPageUid = WebUtil.readLongParam(request, WikiConstants.ATTR_CURRENT_WIKI);

	// set up wikiService
	if (wikiService == null) {
	    wikiService = WikiServiceProxy.getWikiService(this.getServlet().getServletContext());
	}

	// Get the current wiki page content
	WikiPage currentWikiPage = wikiService.getWikiPageByUid(currentPageUid);

	// Get the old wiki content
	WikiPageContent oldContent = wikiService.getWikiPageContent(revertPageContentVersion);

	// Set up the dto, only need to set title and content, as this is a view
	WikiPageDTO pageDTO = new WikiPageDTO();
	pageDTO.setTitle(currentWikiPage.getTitle());
	pageDTO.setCurrentWikiContentDTO(new WikiPageContentDTO(oldContent));

	request.setAttribute(WikiConstants.ATTR_CURRENT_WIKI, pageDTO);

	return mapping.findForward("viewWiki");
    }

    /**
     * Change the active page of the wiki form
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward changePage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	// set up wikiService
	if (wikiService == null) {
	    wikiService = WikiServiceProxy.getWikiService(this.getServlet().getServletContext());
	}

	Wiki wiki = null;
	WikiSession session = null;
	WikiPage wikiPage = null;

	String newPageName = WebUtil.readStrParam(request, WikiConstants.ATTR_NEW_PAGE_NAME).replaceAll("`", "'");

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID, true);

	// get the wiki by either toolContentId or tool session
	if (toolSessionID == null) {
	    Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	    wiki = wikiService.getWikiByContentId(toolContentID);

	    // Get the page to change to
	    wikiPage = wikiService.getWikiPageByWikiAndTitle(wiki, newPageName);
	} else {
	    session = wikiService.getSessionBySessionId(toolSessionID);
	    wiki = session.getWiki();

	    // Get the page to change to
	    wikiPage = wikiService.getWikiBySessionAndTitle(session, newPageName);
	}

	if (wikiPage == null) {
	    // TODO: Error handling page does not exist
	}

	// go through unspecified to display the author screen, using wrapper
	// method to set the current page
	return this.returnToWiki(mapping, form, request, response, wikiPage.getUid());
    }

    /**
     * Add a new wiki page to this wiki instance
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward addPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	// set up wikiService
	if (wikiService == null) {
	    wikiService = WikiServiceProxy.getWikiService(this.getServlet().getServletContext());
	}

	Wiki wiki = null;
	WikiSession session = null;
	WikiUser user = null;

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID, true);

	// Set up the authoring form
	WikiPageForm wikiForm = (WikiPageForm) form;
	revertJavascriptTokenReplacement(wikiForm);

	// get the wiki by either toolContentId or tool session
	if (toolSessionID == null) {
	    Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	    wiki = wikiService.getWikiByContentId(toolContentID);
	    // If no editable flag came in the form (as in learner), set true
	    if (wikiForm.getNewPageIsEditable() == null) {
		wikiForm.setNewPageIsEditable(false);
	    }
	} else {
	    session = wikiService.getSessionBySessionId(toolSessionID);
	    wiki = session.getWiki();
	    user = getCurrentUser(toolSessionID);

	}

	// Setting the editable flag based on call origin
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);
	if ((mode == null) || (mode == ToolAccessMode.TEACHER)) {
	    // Author/Monitor/Live edit
	    // If no editable flag came in the form (as in learner), set false
	    if (wikiForm.getNewPageIsEditable() == null) {
		wikiForm.setNewPageIsEditable(false);
	    }
	} else {
	    // Learner or preview
	    // If no editable flag came in the form (as in learner), set true
	    if (wikiForm.getNewPageIsEditable() == null) {
		wikiForm.setNewPageIsEditable(true);
	    }
	}

	// inserting the wiki page, null user and session indicates that this
	// page was saved in author
	Long currentPageUid = wikiService.insertWikiPage(wikiForm, wiki, user, session);

	// Send adding page notifications
	if ((toolSessionID != null) && (user != null)) {
	    notifyWikiChange(toolSessionID, "notify.pageAdded.subject", "notify.pageAdded.body", user, request);
	}

	// go to the new wiki page
	return returnToWiki(mapping, wikiForm, request, response, currentPageUid);
    }

    /**
     * Remove a wiki page from the wiki instance
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward removePage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	// The page to be removed
	Long currentPageUid = WebUtil.readLongParam(request, WikiConstants.ATTR_CURRENT_WIKI);

	// Get the session information for notifications
	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID, true);
	WikiUser user = getCurrentUser(toolSessionID);

	// set up wikiService
	if (wikiService == null) {
	    wikiService = WikiServiceProxy.getWikiService(this.getServlet().getServletContext());
	}

	WikiPage wikiPage = wikiService.getWikiPageByUid(currentPageUid);

	// Updating the wikiPage, setting a null user which indicated this
	// change was made in author
	wikiService.markWikiPageAsDeleted(wikiPage);

	// Send removed page notifications
	if ((toolSessionID != null) && (user != null)) {
	    notifyWikiChange(toolSessionID, "notify.pageRemoved.subject", "notify.pageRemoved.body", user, request);
	}

	// return to the same page with information about being removed displayed
	return this.returnToWiki(mapping, form, request, response, currentPageUid);

    }

    /**
     * Restore a page previously marked as removed.
     */
    public ActionForward restorePage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	// The page to be restored
	Long currentPageUid = WebUtil.readLongParam(request, WikiConstants.ATTR_CURRENT_WIKI);

	// Get the session information for notifications
	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID, true);
	WikiUser user = getCurrentUser(toolSessionID);

	// set up wikiService
	if (wikiService == null) {
	    wikiService = WikiServiceProxy.getWikiService(this.getServlet().getServletContext());
	}

	WikiPage wikiPage = wikiService.getWikiPageByUid(currentPageUid);

	// Updating the wikiPage
	wikiService.restoreWikiPage(wikiPage);

	// Send removed page notifications
	if ((toolSessionID != null) && (user != null)) {
	    notifyWikiChange(toolSessionID, "notify.pageRestored.subject", "notify.pageRestored.body", user, request);
	}

	// return to the same page
	return this.returnToWiki(mapping, form, request, response, currentPageUid);

    }

    /**
     * Toggles whether a learner wants to receive notifications for wiki changes
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward toggleLearnerSubsciption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID, true);
	Long currentPageUid = WebUtil.readLongParam(request, WikiConstants.ATTR_CURRENT_WIKI);

	// Get the current user
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	// set up wikiService
	if (wikiService == null) {
	    wikiService = WikiServiceProxy.getWikiService(this.getServlet().getServletContext());
	}

	IEventNotificationService notificationService = wikiService.getEventNotificationService();
	notificationService.createEvent(WikiConstants.TOOL_SIGNATURE, WikiConstants.EVENT_NOTIFY_LEARNERS,
		toolSessionID, null, null, false);

	// Get whether the user is subscribed
	boolean subscribed = notificationService.isSubscribed(WikiConstants.TOOL_SIGNATURE,
		WikiConstants.EVENT_NOTIFY_LEARNERS, toolSessionID, user.getUserID().longValue());

	if (subscribed) {
	    // unsubscribe the learner to the event
	    notificationService.unsubscribe(WikiConstants.TOOL_SIGNATURE, WikiConstants.EVENT_NOTIFY_LEARNERS,
		    toolSessionID, user.getUserID());
	} else {
	    // subscribe the learner to the event
	    notificationService.subscribe(WikiConstants.TOOL_SIGNATURE, WikiConstants.EVENT_NOTIFY_LEARNERS,
		    toolSessionID, user.getUserID(), IEventNotificationService.DELIVERY_METHOD_MAIL);
	}

	// return to the wiki page
	return returnToWiki(mapping, form, request, response, currentPageUid);

    }

    private void notifyWikiChange(Long toolSessionID, String subjectLangKey, String bodyLangKey, WikiUser wikiUser,
	    HttpServletRequest request) throws Exception {

	// set up wikiService
	if (wikiService == null) {
	    wikiService = WikiServiceProxy.getWikiService(this.getServlet().getServletContext());
	}

	WikiSession wikiSession = wikiService.getSessionBySessionId(toolSessionID);

	IEventNotificationService notificationService = wikiService.getEventNotificationService();

	String subject = wikiService.getLocalisedMessage(subjectLangKey, new Object[] { wikiSession.getSessionName() });
	String fullName = wikiUser.getFirstName() + " " + wikiUser.getLastName();

	// Notify all the monitors
	if (wikiSession.getWiki().isNotifyUpdates()) {
	    boolean isHtmlFormat = false;

	    List<User> monitors = wikiService.getMonitorsByToolSessionId(toolSessionID);
	    for (User monitor : monitors) {
		Integer monitorUserId = monitor.getUserId();
		String contentFolderId = wikiService.getLearnerContentFolder(toolSessionID, monitorUserId.longValue());

		String relativePath = "/tool/" + WikiConstants.TOOL_SIGNATURE
			+ "/monitoring.do?dispatch=showWiki&toolSessionID=" + toolSessionID.toString()
			+ "&contentFolderID=" + contentFolderId;

		String hash = relativePath + "," + toolSessionID.toString() + ",t";
		hash = new String(Base64.encodeBase64(hash.getBytes()));

		String link = Configuration.get(ConfigurationKeys.SERVER_URL) + "r.do?" + "h=" + hash;

		String body = wikiService.getLocalisedMessage(bodyLangKey,
			new Object[] { fullName, wikiSession.getSessionName(), link });

		notificationService.sendMessage(null, monitorUserId, IEventNotificationService.DELIVERY_METHOD_MAIL,
			subject, body, isHtmlFormat);
	    }

	}

	// trigger the event if exists for all the learners who are subscribed
	if (notificationService.eventExists(WikiConstants.TOOL_SIGNATURE, WikiConstants.EVENT_NOTIFY_LEARNERS,
		toolSessionID)) {

	    String relativePath = "/tool/" + WikiConstants.TOOL_SIGNATURE + "/learning.do?mode=learner&toolSessionID="
		    + toolSessionID.toString();

	    String hash = relativePath + "," + toolSessionID.toString() + ",l";
	    hash = new String(Base64.encodeBase64(hash.getBytes()));

	    String link = Configuration.get(ConfigurationKeys.SERVER_URL) + "r.do?" + "h=" + hash;

	    String body = wikiService.getLocalisedMessage(bodyLangKey,
		    new Object[] { fullName, wikiSession.getSessionName(), link });

	    notificationService.trigger(WikiConstants.TOOL_SIGNATURE, WikiConstants.EVENT_NOTIFY_LEARNERS,
		    toolSessionID, subject, body);
	}
    }

    /**
     * Replaces codeword back to "javascript", so the content works correctly after displaying.
     */
    private void revertJavascriptTokenReplacement(WikiPageForm form) {
	String encodedWikiBody = form.getNewPageWikiBody();
	if (encodedWikiBody != null) {
	    form.setNewPageWikiBody(
		    encodedWikiBody.replace(WikiConstants.JAVASCRIPT_REPLACE_TOKEN, WikiConstants.JAVASCRIPT_TOKEN));
	}

	encodedWikiBody = form.getWikiBody();
	if (encodedWikiBody != null) {
	    form.setWikiBody(
		    encodedWikiBody.replace(WikiConstants.JAVASCRIPT_REPLACE_TOKEN, WikiConstants.JAVASCRIPT_TOKEN));
	}
    }
}
