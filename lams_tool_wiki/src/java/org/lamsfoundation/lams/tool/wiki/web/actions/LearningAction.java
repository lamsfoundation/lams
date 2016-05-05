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

import java.io.IOException;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.wiki.dto.WikiDTO;
import org.lamsfoundation.lams.tool.wiki.dto.WikiPageContentDTO;
import org.lamsfoundation.lams.tool.wiki.dto.WikiPageDTO;
import org.lamsfoundation.lams.tool.wiki.dto.WikiUserDTO;
import org.lamsfoundation.lams.tool.wiki.model.Wiki;
import org.lamsfoundation.lams.tool.wiki.model.WikiPage;
import org.lamsfoundation.lams.tool.wiki.model.WikiPageContent;
import org.lamsfoundation.lams.tool.wiki.model.WikiSession;
import org.lamsfoundation.lams.tool.wiki.model.WikiUser;
import org.lamsfoundation.lams.tool.wiki.service.IWikiService;
import org.lamsfoundation.lams.tool.wiki.service.WikiServiceProxy;
import org.lamsfoundation.lams.tool.wiki.util.WikiConstants;
import org.lamsfoundation.lams.tool.wiki.util.WikiException;
import org.lamsfoundation.lams.tool.wiki.web.forms.LearningForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * This action handles all the learning actions, which include opening learner,
 * relection, going to the next activity, and all the wikipage actions
 *
 * It inherits from the WikiPageAction which inherits from the
 * LamsDispatchAction so that common actions can be used in learner, monitor and
 * author
 *
 * @author lfoxton
 * @version
 *
 * @struts.action path="/learning" parameter="dispatch" scope="request"
 *                name="learningForm" validate="false"
 * @struts.action-forward name="wiki" path="tiles:/learning/main"
 * @struts.action-forward name="compareWiki" path="/pages/wiki/compare.jsp"
 * @struts.action-forward name="viewWiki" path="/pages/wiki/viewWiki.jsp"
 * @struts.action-forward name="defineLater" path="tiles:/learning/defineLater"
 * @struts.action-forward name="notebook" path="tiles:/learning/notebook"
 */
public class LearningAction extends WikiPageAction {

    private static Logger log = Logger.getLogger(LearningAction.class);

    private static final boolean MODE_OPTIONAL = false;

    private IWikiService wikiService;

    /**
     * unspecified loads the learner window with the current wiki page as well
     * as setting all the advanced options and user-specifice info
     */
    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	LearningForm learningForm = (LearningForm) form;

	// 'toolSessionID' and 'mode' parameters are expected to be present.
	// TODO need to catch exceptions and handle errors.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, MODE_OPTIONAL);

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	Long currentPageUid = learningForm.getCurrentWikiPageId();

	// set up wikiService
	if (wikiService == null) {
	    wikiService = WikiServiceProxy.getWikiService(this.getServlet().getServletContext());
	}

	// Retrieve the session and content.
	WikiSession wikiSession = wikiService.getSessionBySessionId(toolSessionID);
	if (wikiSession == null) {
	    throw new WikiException("Cannot retrieve session with toolSessionID" + toolSessionID);
	}

	Wiki wiki = wikiSession.getWiki();

	// check defineLater
	if (wiki.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set mode, toolSessionID and WikiDTO
	request.setAttribute(WikiConstants.ATTR_MODE, mode.toString());
	learningForm.setToolSessionID(toolSessionID);

	WikiDTO wikiDTO = new WikiDTO(wiki);
	request.setAttribute(WikiConstants.ATTR_WIKI_DTO, wikiDTO);

	// Set the content in use flag.
	if (!wiki.isContentInUse()) {
	    wiki.setContentInUse(new Boolean(true));
	    wikiService.saveOrUpdateWiki(wiki);
	}

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(toolSessionID, request,
		getServlet().getServletContext());

	// get the user
	WikiUser wikiUser;
	if (mode.equals(ToolAccessMode.TEACHER)) {
	    Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
	    wikiUser = wikiService.getUserByUserIdAndSessionId(userID, toolSessionID);
	} else {
	    wikiUser = getCurrentUser(toolSessionID);
	}

	// Create the userDTO
	WikiUserDTO wikiUserDTO = new WikiUserDTO(wikiUser);
	if (wikiUser.isFinishedActivity()) {
	    // get the notebook entry.
	    NotebookEntry notebookEntry = wikiService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		    WikiConstants.TOOL_SIGNATURE, wikiUser.getUserId().intValue());
	    if (notebookEntry != null) {
		wikiUserDTO.setNotebookEntry(notebookEntry.getEntry());
	    }
	}

	// Set whether the user has enabled notifications
	if (wikiService.getEventNotificationService().eventExists(WikiConstants.TOOL_SIGNATURE,
		WikiConstants.EVENT_NOTIFY_LEARNERS, toolSessionID)
		&& wikiService.getEventNotificationService().isSubscribed(WikiConstants.TOOL_SIGNATURE,
			WikiConstants.EVENT_NOTIFY_LEARNERS, toolSessionID, wikiUser.getUserId().longValue())) {

	    wikiUserDTO.setNotificationEnabled(true);
	}

	// add the userDTO to attributes
	request.setAttribute(WikiConstants.ATTR_USER_DTO, wikiUserDTO);

	// Set whether user has reached maximum edits
	int maxEdits = wiki.getMaximumEdits();
	Boolean maxEditsReached = (maxEdits != 0 && wikiUser.getWikiEdits() >= maxEdits);
	request.setAttribute(WikiConstants.ATTR_MAX_EDITS_REACHED, maxEditsReached);
	request.setAttribute(WikiConstants.ATTR_EDITS_LEFT, maxEdits - wikiUser.getWikiEdits());

	// Set whether user has reached minimum edits
	int minEdits = wiki.getMinimumEdits();
	Boolean minEditsReached = (wikiUser.getWikiEdits() >= minEdits);
	request.setAttribute(WikiConstants.ATTR_MIN_EDITS_REACHED, minEditsReached);

	// Get the wikipages from the session and the main page
	SortedSet<WikiPageDTO> wikiPageDTOs = new TreeSet<WikiPageDTO>();
	for (WikiPage wikiPage : wikiSession.getWikiPages()) {
	    WikiPageDTO pageDTO = new WikiPageDTO(wikiPage);

	    wikiPageDTOs.add(pageDTO);

	}
	request.setAttribute(WikiConstants.ATTR_WIKI_PAGES, wikiPageDTOs);
	request.setAttribute(WikiConstants.ATTR_MAIN_WIKI_PAGE, new WikiPageDTO(wikiSession.getMainPage()));

	// Set the current wiki page, if there is none, set to the main page
	WikiPage currentWikiPage = null;
	if (currentPageUid != null) {
	    currentWikiPage = wikiService.getWikiPageByUid(currentPageUid);
	} else {
	    currentWikiPage = wikiSession.getMainPage();
	}
	request.setAttribute(WikiConstants.ATTR_CURRENT_WIKI, new WikiPageDTO(currentWikiPage));

	// Set the current wiki history
	SortedSet<WikiPageContentDTO> currentWikiPageHistoryDTOs = new TreeSet<WikiPageContentDTO>();
	for (WikiPageContent wikiPageContentHistoryItem : currentWikiPage.getWikiContentVersions()) {
	    currentWikiPageHistoryDTOs.add(new WikiPageContentDTO(wikiPageContentHistoryItem));
	}
	request.setAttribute(WikiConstants.ATTR_WIKI_PAGE_CONTENT_HISTORY, currentWikiPageHistoryDTOs);

	// Set the content folder id
	request.setAttribute(WikiConstants.ATTR_CONTENT_FOLDER_ID,
		wikiService.getLearnerContentFolder(toolSessionID, wikiUser.getUserId()));

	// set readOnly flag.
	if (mode.equals(ToolAccessMode.TEACHER) || (wiki.isLockOnFinished() && wikiUser.isFinishedActivity())) {
	    request.setAttribute(WikiConstants.ATTR_CONTENT_EDITAVLE, false);
	} else {
	    request.setAttribute(WikiConstants.ATTR_CONTENT_EDITAVLE, true);
	}
	request.setAttribute(WikiConstants.ATTR_FINISHED_ACTIVITY, wikiUser.isFinishedActivity());

	return mapping.findForward("wiki");
    }

    /**
     * Wrapper method to make sure that the correct wiki is returned to from the
     * WikiPageAction class
     */
    @Override
    protected ActionForward returnToWiki(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, Long currentWikiPageId) throws Exception {
	LearningForm learnForm = (LearningForm) form;
	learnForm.setCurrentWikiPageId(currentWikiPageId);
	return unspecified(mapping, learnForm, request, response);
    }

    /**
     * Gets the current user by toolSessionId
     *
     * @param toolSessionId
     */
    @Override
    protected WikiUser getCurrentUser(Long toolSessionId) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	WikiUser wikiUser = wikiService.getUserByUserIdAndSessionId(new Long(user.getUserID().intValue()),
		toolSessionId);

	if (wikiUser == null) {
	    WikiSession wikiSession = wikiService.getSessionBySessionId(toolSessionId);
	    wikiUser = wikiService.createWikiUser(user, wikiSession);
	}

	return wikiUser;
    }

    /**
     * Finish the activity, we dont need to save anything here, as that is done
     * by the wikipage actions
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward finishActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");

	WikiUser wikiUser = getCurrentUser(toolSessionID);

	if (wikiUser != null) {
	    wikiUser.setFinishedActivity(true);
	    wikiService.saveOrUpdateWikiUser(wikiUser);
	} else {
	    log.error("finishActivity(): couldn't find WikiUser with id: " + wikiUser.getUserId()
		    + "and toolSessionID: " + toolSessionID);
	}

	ToolSessionManager sessionMgrService = WikiServiceProxy.getWikiSessionManager(getServlet().getServletContext());

	String nextActivityUrl;
	try {
	    nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, wikiUser.getUserId());
	    response.sendRedirect(nextActivityUrl);
	} catch (DataMissingException e) {
	    throw new WikiException(e);
	} catch (ToolException e) {
	    throw new WikiException(e);
	} catch (IOException e) {
	    throw new WikiException(e);
	}

	return null; // TODO need to return proper page.
    }

    /**
     * Opens the notebook page for reflections
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward openNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	LearningForm lrnForm = (LearningForm) form;

	// set the finished flag
	WikiUser wikiUser = this.getCurrentUser(lrnForm.getToolSessionID());
	WikiDTO wikiDTO = new WikiDTO(wikiUser.getWikiSession().getWiki());

	request.setAttribute("wikiDTO", wikiDTO);

	NotebookEntry notebookEntry = wikiService.getEntry(wikiUser.getWikiSession().getSessionId(),
		CoreNotebookConstants.NOTEBOOK_TOOL, WikiConstants.TOOL_SIGNATURE, wikiUser.getUserId().intValue());

	if (notebookEntry != null) {
	    lrnForm.setEntryText(notebookEntry.getEntry());
	}

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(lrnForm.getToolSessionID(), request,
		getServlet().getServletContext());

	return mapping.findForward("notebook");
    }

    /**
     * Submit reflections
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// save the reflection entry and call the notebook.

	LearningForm lrnForm = (LearningForm) form;

	WikiUser wikiUser = this.getCurrentUser(lrnForm.getToolSessionID());
	Long toolSessionID = wikiUser.getWikiSession().getSessionId();
	Integer userID = wikiUser.getUserId().intValue();

	// check for existing notebook entry
	NotebookEntry entry = wikiService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		WikiConstants.TOOL_SIGNATURE, userID);

	if (entry == null) {
	    // create new entry
	    wikiService.createNotebookEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		    WikiConstants.TOOL_SIGNATURE, userID, lrnForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(lrnForm.getEntryText());
	    entry.setLastModified(new Date());
	    wikiService.updateEntry(entry);
	}

	return finishActivity(mapping, form, request, response);
    }
}
