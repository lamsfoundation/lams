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
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
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
import org.lamsfoundation.lams.tool.wiki.web.forms.AuthoringForm;
import org.lamsfoundation.lams.tool.wiki.web.forms.LearningForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author lfoxton
 * @version
 * 
 * @struts.action path="/learning" parameter="dispatch" scope="request"
 *                name="learningForm" validate="false"
 * @struts.action-forward name="wiki" path="tiles:/learning/main"
 * @struts.action-forward name="runOffline" path="tiles:/learning/runOffline"
 * @struts.action-forward name="compareWiki" path="/pages/wiki/compare.jsp"
 * @struts.action-forward name="viewWiki" path="/pages/wiki/viewWiki.jsp"
 * @struts.action-forward name="defineLater" path="tiles:/learning/defineLater"
 */
public class LearningAction extends WikiPageAction {

    private static Logger log = Logger.getLogger(LearningAction.class);

    private static final boolean MODE_OPTIONAL = false;

    private IWikiService wikiService;

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
	    throw new WikiException("Cannot retreive session with toolSessionID" + toolSessionID);
	}

	Wiki wiki = wikiSession.getWiki();

	// check defineLater
	if (wiki.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set mode, toolSessionID and WikiDTO
	request.setAttribute("mode", mode.toString());
	learningForm.setToolSessionID(toolSessionID);

	WikiDTO wikiDTO = new WikiDTO();
	wikiDTO.title = wiki.getTitle();
	wikiDTO.instructions = wiki.getInstructions();
	wikiDTO.lockOnFinish = wiki.isLockOnFinished();
	request.setAttribute("wikiDTO", wikiDTO);

	// Set the content in use flag.
	if (!wiki.isContentInUse()) {
	    wiki.setContentInUse(new Boolean(true));
	    wikiService.saveOrUpdateWiki(wiki);
	}

	// check runOffline
	if (wiki.isRunOffline()) {
	    return mapping.findForward("runOffline");
	}

	WikiUser wikiUser;
	if (mode.equals(ToolAccessMode.TEACHER)) {
	    Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
	    wikiUser = wikiService.getUserByUserIdAndSessionId(userID, toolSessionID);
	} else {
	    wikiUser = getCurrentUser(toolSessionID);
	}

	// Get the wikipages from the session and the main page
	SortedSet<WikiPageDTO> wikiPageDTOs = new TreeSet<WikiPageDTO>();
	WikiPage mainPage = null;
	for (WikiPage wikiPage : wikiSession.getWikiPages()) {
	    WikiPageDTO pageDTO = new WikiPageDTO(wikiPage);

	    wikiPageDTOs.add(pageDTO);

	    // Set the main page
	    if (wikiPage.getTitle().equals(wiki.getMainPage().getTitle())) {
		mainPage = wikiPage;
	    }
	}
	request.setAttribute(WikiConstants.ATTR_WIKI_PAGES, wikiPageDTOs);
	request.setAttribute(WikiConstants.ATTR_MAIN_WIKI_PAGE, new WikiPageDTO(mainPage));

	// Set the current wiki page, if there is none, set to the main page
	WikiPage currentWikiPage = null;
	if (currentPageUid != null) {
	    currentWikiPage = wikiService.getWikiPageByUid(currentPageUid);
	} else {
	    currentWikiPage = mainPage;
	}
	request.setAttribute(WikiConstants.ATTR_CURRENT_WIKI, new WikiPageDTO(currentWikiPage));

	// Set the current wiki history
	SortedSet<WikiPageContentDTO> currentWikiPageHistoryDTOs = new TreeSet<WikiPageContentDTO>();
	for (WikiPageContent wikiPageContentHistoryItem : currentWikiPage.getWikiContentVersions()) {
	    currentWikiPageHistoryDTOs.add(new WikiPageContentDTO(wikiPageContentHistoryItem));
	}
	request.setAttribute(WikiConstants.ATTR_WIKI_PAGE_CONTENT_HISTORY, currentWikiPageHistoryDTOs);

	// get any existing wiki entry
	/*
	 * NotebookEntry nbEntry = null; if ( wikiUser != null ) { nbEntry =
	 * wikiService.getEntry(wikiUser.getEntryUID()); } if (nbEntry != null) {
	 * learningForm.setEntryText(nbEntry.getEntry()); }
	 */

	// set readOnly flag.
	if (mode.equals(ToolAccessMode.TEACHER) || (wiki.isLockOnFinished() && wikiUser.isFinishedActivity())) {
	    request.setAttribute("contentEditable", false);
	} else {
	    request.setAttribute("contentEditable", true);
	}
	request.setAttribute("finishedActivity", wikiUser.isFinishedActivity());

	return mapping.findForward("wiki");
    }

    protected ActionForward returnToWiki(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, Long currentWikiPageId) throws Exception {
	LearningForm learnForm = (LearningForm) form;
	learnForm.setCurrentWikiPageId(currentWikiPageId);
	return unspecified(mapping, learnForm, request, response);
    }

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

    public ActionForward finishActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");

	WikiUser wikiUser = getCurrentUser(toolSessionID);

	if (wikiUser != null) {

	    LearningForm learningForm = (LearningForm) form;

	    // TODO fix idType to use real value not 999

	    if (wikiUser.getEntryUID() == null) {
		wikiUser.setEntryUID(wikiService.createNotebookEntry(toolSessionID,
			CoreNotebookConstants.NOTEBOOK_TOOL, WikiConstants.TOOL_SIGNATURE, wikiUser.getUserId()
				.intValue(), learningForm.getEntryText()));
	    } else {
		// update existing entry.
		wikiService.updateEntry(wikiUser.getEntryUID(), learningForm.getEntryText());
	    }

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
}
