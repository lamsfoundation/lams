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


package org.lamsfoundation.lams.tool.wiki.web.actions;

import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.wiki.dto.WikiDTO;
import org.lamsfoundation.lams.tool.wiki.dto.WikiPageContentDTO;
import org.lamsfoundation.lams.tool.wiki.dto.WikiPageDTO;
import org.lamsfoundation.lams.tool.wiki.dto.WikiSessionDTO;
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
import org.lamsfoundation.lams.tool.wiki.web.forms.MonitoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

import java.util.Date;

import org.lamsfoundation.lams.util.DateUtil;

import java.util.TimeZone;

/**
 * This action handles all the monitoring actions, which include opening
 * monitor, and all the wikipage actions
 *
 * It inherits from the WikiPageAction which inherits from the
 * LamsDispatchAction so that common actions can be used in learner, monitor and
 * author
 *
 * @author lfoxton
 */
public class MonitoringAction extends WikiPageAction {

    private static Logger log = Logger.getLogger(MonitoringAction.class);

    public IWikiService wikiService;

    /**
     * Sets up the main authoring page which lists the tool sessions and allows
     * you to view their respective WikiPages
     */
    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (wikiService == null) {
	    wikiService = WikiServiceProxy.getWikiService(this.getServlet().getServletContext());
	}
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID, true);
	Wiki wiki;
	//toolContentID is null in case request comes from WikiPageAction.revertPage()
	if (toolContentID == null) {
	    Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	    WikiSession session = wikiService.getSessionBySessionId(toolSessionID);
	    wiki = session.getWiki();
	    toolContentID = wiki.getToolContentId();

	} else {
	    wiki = wikiService.getWikiByContentId(toolContentID);
	}

	if (wiki == null) {
	    throw new WikiException("Could not find wiki with content id: " + toolContentID);
	}

	WikiDTO wikiDT0 = new WikiDTO(wiki);

	Long currentTab = WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true);
	wikiDT0.setCurrentTab(currentTab);
	
	/* Check if submission deadline is null */

	Date submissionDeadline = wikiDT0.getSubmissionDeadline();

	if (submissionDeadline != null) {

	    HttpSession ss = SessionManager.getSession();
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    request.setAttribute("submissionDeadline", tzSubmissionDeadline.getTime());
	    // use the unconverted time, as convertToStringForJSON() does the timezone conversion if needed
	    request.setAttribute("submissionDateString", DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale()));


	}

	request.setAttribute(WikiConstants.ATTR_WIKI_DTO, wikiDT0);
	request.setAttribute(WikiConstants.ATTR_CONTENT_FOLDER_ID, contentFolderID);
	request.setAttribute(WikiConstants.ATTR_IS_GROUPED_ACTIVITY, wikiService.isGroupedActivity(toolContentID));
	return mapping.findForward("success");
    }

    /**
     * Wrapper method to make sure that the correct wiki is returned to from the
     * WikiPageAction class
     */
    @Override
    protected ActionForward returnToWiki(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, Long currentWikiPageId) throws Exception {
	MonitoringForm monitoringForm = (MonitoringForm) form;
	monitoringForm.setCurrentWikiPageId(currentWikiPageId);
	return showWiki(mapping, monitoringForm, request, response);
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
     * Set Submission Deadline
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward setSubmissionDeadline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	// set up wikiService
	if (wikiService == null) {
		wikiService = WikiServiceProxy.getWikiService(this.getServlet().getServletContext());
	}

	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Wiki wiki = wikiService.getWikiByContentId(contentID);

	Long dateParameter = WebUtil.readLongParam(request,WikiConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	String formattedDate = "";
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	    formattedDate = DateUtil.convertToStringForJSON(tzSubmissionDeadline, request.getLocale());
	}
	wiki.setSubmissionDeadline(tzSubmissionDeadline);
	wikiService.saveOrUpdateWiki(wiki);
	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().print(formattedDate);
	return null;
    }

    /**
     * Shows a specific wiki based on the session id
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward showWiki(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	if (wikiService == null) {
	    wikiService = WikiServiceProxy.getWikiService(this.getServlet().getServletContext());
	}

	WikiSession wikiSession = wikiService.getSessionBySessionId(toolSessionId);
	WikiSessionDTO sessionDTO = new WikiSessionDTO(wikiSession);
	Long toolContentId = wikiSession.getWiki().getToolContentId();

	// Add all the user notebook entries to the session dto
	for (WikiUserDTO userDTO : sessionDTO.getUserDTOs()) {
	    NotebookEntry notebookEntry = wikiService.getEntry(toolSessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    WikiConstants.TOOL_SIGNATURE, userDTO.getUserId().intValue());
	    if (notebookEntry != null) {
		userDTO.setNotebookEntry(notebookEntry.getEntry());
	    }
	    sessionDTO.getUserDTOs().add(userDTO);
	}
	request.setAttribute(WikiConstants.ATTR_SESSION_DTO, sessionDTO);

	// Set up the authForm.
	MonitoringForm monForm = (MonitoringForm) form;

	Long currentPageUid = monForm.getCurrentWikiPageId();

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

	// Reset the isEditable and newPageIdEditable field for the form
	monForm.setIsEditable(currentWikiPage.getEditable());
	monForm.setNewPageIsEditable(true);

	// Set the current wiki history
	SortedSet<WikiPageContentDTO> currentWikiPageHistoryDTOs = new TreeSet<WikiPageContentDTO>();
	for (WikiPageContent wikiPageContentHistoryItem : currentWikiPage.getWikiContentVersions()) {
	    currentWikiPageHistoryDTOs.add(new WikiPageContentDTO(wikiPageContentHistoryItem));
	}
	request.setAttribute(WikiConstants.ATTR_WIKI_PAGE_CONTENT_HISTORY, currentWikiPageHistoryDTOs);
	request.setAttribute(WikiConstants.ATTR_CONTENT_FOLDER_ID, contentFolderID);
	request.setAttribute(WikiConstants.ATTR_IS_GROUPED_ACTIVITY, wikiService.isGroupedActivity(toolContentId));

	return mapping.findForward("wiki_display");
    }
}
