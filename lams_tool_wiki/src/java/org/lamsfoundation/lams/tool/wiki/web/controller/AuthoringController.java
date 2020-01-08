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

package org.lamsfoundation.lams.tool.wiki.web.controller;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.wiki.dto.WikiPageContentDTO;
import org.lamsfoundation.lams.tool.wiki.dto.WikiPageDTO;
import org.lamsfoundation.lams.tool.wiki.model.Wiki;
import org.lamsfoundation.lams.tool.wiki.model.WikiPage;
import org.lamsfoundation.lams.tool.wiki.model.WikiPageContent;
import org.lamsfoundation.lams.tool.wiki.model.WikiUser;
import org.lamsfoundation.lams.tool.wiki.service.IWikiService;
import org.lamsfoundation.lams.tool.wiki.util.WikiConstants;
import org.lamsfoundation.lams.tool.wiki.web.forms.AuthoringForm;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This action handles all the authoring actions, which include opening author, saving, uploading instruction files and
 * all the wikipage actions
 *
 *
 * @author lfoxton
 */
@Controller
@RequestMapping("/authoring")
public class AuthoringController extends WikiPageController {
    private static Logger logger = Logger.getLogger(AuthoringController.class);

    @Autowired
    private IWikiService wikiService;

    // Authoring SessionMap key names
    private static final String KEY_TOOL_CONTENT_ID = "toolContentID";

    private static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";

    private static final String KEY_MODE = "mode";

    @RequestMapping("/authoring")
    public String unspecified(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);

	// retrieving Wiki with given toolContentID
	Wiki wiki = wikiService.getWikiByContentId(toolContentID);
	if (wiki == null) {
	    wiki = wikiService.copyDefaultContent(toolContentID);
	    wiki.setCreateDate(new Date());
	    wikiService.saveOrUpdateWiki(wiki);
	    // TODO NOTE: this causes DB orphans when LD not saved.
	}

	return readDatabaseData(authoringForm, wiki, request, mode);
    }
    
    /**
     * Set the defineLater flag so that learners cannot use content while we are editing. This flag is released when
     * updateContent is called.
     */
    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    public String definelater(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Wiki wiki = wikiService.getWikiByContentId(toolContentID);
	wiki.setDefineLater(true);
	wikiService.saveOrUpdateWiki(wiki);

	//audit log the teacher has started editing activity in monitor
	wikiService.auditLogStartEditingActivityInMonitor(toolContentID);

	return readDatabaseData(authoringForm, wiki, request, ToolAccessMode.TEACHER);
    }
    
    /**
     * Common method for "unspecified" and "defineLater"
     */
    private String readDatabaseData(AuthoringForm authoringForm, Wiki wiki, HttpServletRequest request,
	    ToolAccessMode mode) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	Long currentPageUid = authoringForm.getCurrentWikiPageId();
	
	// update the form
	updateAuthForm(authoringForm, wiki);

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
	authoringForm.setIsEditable(currentPageDTO.getEditable());

	// Set the current wiki history
	SortedSet<WikiPageContentDTO> currentWikiPageHistoryDTOs = new TreeSet<>();
	for (WikiPageContent wikiPageContentHistoryItem : currentWikiPage.getWikiContentVersions()) {
	    currentWikiPageHistoryDTOs.add(new WikiPageContentDTO(wikiPageContentHistoryItem));
	}
	request.setAttribute(WikiConstants.ATTR_WIKI_PAGE_CONTENT_HISTORY, currentWikiPageHistoryDTOs);

	// Get the child wiki pages
	SortedSet<WikiPageDTO> wikiPageDTOs = new TreeSet<>();
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
	authoringForm.setSessionMapID(map.getSessionID());

	// add the sessionMap to HTTPSession.
	request.getSession().setAttribute(map.getSessionID(), map);
	request.setAttribute(WikiConstants.ATTR_SESSION_MAP, map);

	return "pages/authoring/authoring";
    }

    @RequestMapping(path = "/editPage", method = RequestMethod.POST)
    public String editPage(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request) throws Exception {
	super.editPage(authoringForm, request);
	Long currentPageUid = WebUtil.readLongParam(request, WikiConstants.ATTR_CURRENT_WIKI);
	return returnToWiki(authoringForm, request, currentPageUid);
    }

    @RequestMapping(path = "/revertPage", method = RequestMethod.POST)
    public String revertPage(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request) throws Exception {
	super.revertPage(authoringForm, request);
	return unspecified(authoringForm, request);
    }

    @RequestMapping(path = "/comparePage")
    public String comparePage(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request)
	    throws Exception {
	super.comparePage(authoringForm, request);
	return "pages/wiki/compare";
    }

    @RequestMapping(path = "/viewPage")
    public String viewPage(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request) throws Exception {
	super.viewPage(authoringForm, request);
	return "pages/wiki/viewWiki";
    }

    @RequestMapping(path = "/changePage", method = RequestMethod.POST)
    public String changePage(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request) throws Exception {
	Long pageUid = super.changePage(authoringForm, request);
	return this.returnToWiki(authoringForm, request, pageUid);
    }

    @RequestMapping(path = "/addPage", method = RequestMethod.POST)
    public String addPage(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request) throws Exception {
	Long currentWikiPageId = super.addPage(authoringForm, request);
	return returnToWiki(authoringForm, request, currentWikiPageId);
    }

    @RequestMapping(path = "/removePage", method = RequestMethod.POST)
    public String removePage(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request) throws Exception {

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	Long currentPageUid = WebUtil.readLongParam(request, WikiConstants.ATTR_CURRENT_WIKI);
	Wiki wiki = wikiService.getWikiByContentId(toolContentID);
	if (wiki.isDefineLater()) {
	    // Only mark as removed if editing a live version (monitor/live edit)
	    super.removePage(authoringForm, request);
	    return returnToWiki(authoringForm, request, currentPageUid);
	}
	WikiPage wikiPage = wikiService.getWikiPageByUid(currentPageUid);
	wikiService.deleteWikiPage(wikiPage);

	// return to the main page, by setting the current page to null
	return this.returnToWiki(authoringForm, request, null);
    }

    @RequestMapping(path = "/restorePage", method = RequestMethod.POST)
    public String restorePage(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request)
	    throws Exception {
	super.restorePage(authoringForm, request);
	Long currentWikiPageId = WebUtil.readLongParam(request, WikiConstants.ATTR_CURRENT_WIKI);
	return this.returnToWiki(authoringForm, request, currentWikiPageId);
    }

    @RequestMapping(path = "/toggleLearnerSubsciption", method = RequestMethod.POST)
    public String toggleLearnerSubsciption(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request)
	    throws Exception {
	super.toggleLearnerSubsciption(authoringForm, request);
	Long currentWikiPageId = WebUtil.readLongParam(request, WikiConstants.ATTR_CURRENT_WIKI);
	return returnToWiki(authoringForm, request, currentWikiPageId);
    }

    @Override
    @RequestMapping(path = "/notifyWikiChange", method = RequestMethod.POST)
    public void notifyWikiChange(Long toolSessionID, String subjectLangKey, String bodyLangKey, WikiUser wikiUser,
	    HttpServletRequest request) throws Exception {
	super.notifyWikiChange(toolSessionID, subjectLangKey, bodyLangKey, wikiUser, request);
    }

    @RequestMapping("/revertJavascriptTokenReplacement")
    public void revertJavascriptTokenReplacement(AuthoringForm authoringForm) {
	super.revertJavascriptTokenReplacement(authoringForm);
    }

    /**
     * Wrapper method to make sure that the correct wiki is returned to from the WikiPageAction class
     */
    protected String returnToWiki(AuthoringForm authoringForm, HttpServletRequest request, Long currentWikiPageId)
	    throws Exception {
	authoringForm.setCurrentWikiPageId(currentWikiPageId);
	return unspecified(authoringForm, request);
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
     */
    @RequestMapping(path = "/updateContent", method = RequestMethod.POST)
    public String updateContent(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request,
	    HttpServletResponse response) {
	// TODO need error checking.

	SessionMap<String, Object> map = getSessionMap(request, authoringForm);

	// get wiki content.
	Wiki wiki = wikiService.getWikiByContentId((Long) map.get(AuthoringController.KEY_TOOL_CONTENT_ID));

	// update wiki content using form inputs
	updateWiki(wiki, authoringForm);

	// set the update date
	wiki.setUpdateDate(new Date());

	// releasing defineLater flag so that learner can start using the tool.
	wiki.setDefineLater(false);

	wikiService.saveOrUpdateWiki(wiki);

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authoringForm.setSessionMapID(map.getSessionID());

	request.setAttribute(WikiConstants.ATTR_SESSION_MAP, map);

	return "pages/authoring/authoring";
    }

    /**
     * Updates Wiki content using AuthoringForm inputs.
     */
    private void updateWiki(Wiki wiki, AuthoringForm authForm) {
	// wiki.setTitle(authForm.getTitle());
	WikiPage mainPage = wiki.getMainPage();
	// mainPage.setTitle(authForm.getTitle());

	WikiPageContent content = mainPage.getCurrentWikiContent();
	// content.setBody(authForm.getWikiBody());
	// mainPage.setCurrentWikiContent(content);

	wiki.setMainPage(mainPage);
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

    /**
     * Updates AuthoringForm using Wiki content.
     */
    private void updateAuthForm(AuthoringForm authForm, Wiki wiki) {
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
     */
    private SessionMap<String, Object> createSessionMap(Wiki wiki, ToolAccessMode mode, String contentFolderID,
	    Long toolContentID) {

	SessionMap<String, Object> map = new SessionMap<>();

	map.put(AuthoringController.KEY_MODE, mode);
	map.put(AuthoringController.KEY_CONTENT_FOLDER_ID, contentFolderID);
	map.put(AuthoringController.KEY_TOOL_CONTENT_ID, toolContentID);

	return map;
    }

    /**
     * Retrieve the SessionMap from the HttpSession.
     */
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request, AuthoringForm authForm) {
	return (SessionMap<String, Object>) request.getSession().getAttribute(authForm.getSessionMapID());
    }

}