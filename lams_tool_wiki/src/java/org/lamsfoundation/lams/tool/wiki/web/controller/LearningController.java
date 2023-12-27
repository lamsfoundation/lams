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

import java.io.IOException;
import java.util.Date;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
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
import org.lamsfoundation.lams.tool.wiki.util.WikiConstants;
import org.lamsfoundation.lams.tool.wiki.util.WikiException;
import org.lamsfoundation.lams.tool.wiki.web.forms.LearningForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This action handles all the learning actions, which include opening learner,
 * relection, going to the next activity, and all the wikipage actions
 *
 *
 * @author lfoxton
 */
@Controller
@RequestMapping("/learning")
public class LearningController extends WikiPageController {
    private static Logger log = Logger.getLogger(LearningController.class);

    private static final boolean MODE_OPTIONAL = false;

    @Autowired
    private IWikiService wikiService;

    /**
     * unspecified loads the learner window with the current wiki page as well
     * as setting all the advanced options and user-specifice info
     */
    @RequestMapping("/learning")
    public String unspecified(@ModelAttribute LearningForm learningForm, HttpServletRequest request) throws Exception {

	// 'toolSessionID' and 'mode' parameters are expected to be present.
	// TODO need to catch exceptions and handle errors.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, MODE_OPTIONAL);

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	Long currentPageUid = learningForm.getCurrentWikiPageId();

	// Retrieve the session and content.
	WikiSession wikiSession = wikiService.getSessionBySessionId(toolSessionID);
	if (wikiSession == null) {
	    throw new WikiException("Cannot retrieve session with toolSessionID" + toolSessionID);
	}

	Wiki wiki = wikiSession.getWiki();

	// check defineLater
	if (wiki.isDefineLater()) {
	    return "pages/learning/defineLater";
	}

	// set mode, toolSessionID and WikiDTO
	request.setAttribute(WikiConstants.ATTR_MODE, mode.toString());
	learningForm.setToolSessionID(toolSessionID);

	WikiDTO wikiDTO = new WikiDTO(wiki);
	request.setAttribute(WikiConstants.ATTR_WIKI_DTO, wikiDTO);

	// Set the content in use flag.
	if (!wiki.isContentInUse()) {
	    wiki.setContentInUse(true);
	    wikiService.saveOrUpdateWiki(wiki);
	}

	request.setAttribute(AttributeNames.ATTR_IS_LAST_ACTIVITY, wikiService.isLastActivity(toolSessionID));

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

	// Set whether the user has enabled notifications
	if (wikiService.getEventNotificationService().eventExists(WikiConstants.TOOL_SIGNATURE,
		WikiConstants.EVENT_NOTIFY_LEARNERS, toolSessionID)
		&& wikiService.getEventNotificationService().isSubscribed(WikiConstants.TOOL_SIGNATURE,
			WikiConstants.EVENT_NOTIFY_LEARNERS, toolSessionID, wikiUser.getUserId())) {
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
	SortedSet<WikiPageDTO> wikiPageDTOs = new TreeSet<>();
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
	SortedSet<WikiPageContentDTO> currentWikiPageHistoryDTOs = new TreeSet<>();
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

	/* Check if submission deadline is null */

	Date submissionDeadline = wikiDTO.getSubmissionDeadline();
	request.setAttribute("wikiDTO", wikiDTO);

	if (submissionDeadline != null) {

	    HttpSession ss = SessionManager.getSession();
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    Date currentLearnerDate = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, new Date());
	    request.setAttribute("submissionDeadline", submissionDeadline);

	    // calculate whether submission deadline has passed, and if so forward to "submissionDeadline"
	    if (currentLearnerDate.after(tzSubmissionDeadline)) {
		return "pages/learning/submissionDeadline";
	    }
	}

	return "pages/learning/wiki";
    }

    @RequestMapping(path = "/editPage", method = RequestMethod.POST)
    public String editPage(@ModelAttribute LearningForm learningForm, HttpServletRequest request) throws Exception {
	super.editPage(learningForm, request);
	Long currentWikiPageId = WebUtil.readLongParam(request, WikiConstants.ATTR_CURRENT_WIKI);
	return returnToWiki(learningForm, request, currentWikiPageId);
    }

    @RequestMapping(path = "/revertPage", method = RequestMethod.POST)
    public String revertPage(@ModelAttribute LearningForm learningForm, HttpServletRequest request) throws Exception {
	super.revertPage(learningForm, request);
	return unspecified(learningForm, request);
    }

    @RequestMapping(path = "/comparePage")
    public String comparePage(@ModelAttribute LearningForm learningForm, HttpServletRequest request) throws Exception {
	super.comparePage(learningForm, request);
	return "pages/wiki/compare";
    }

    @RequestMapping(path = "/viewPage")
    public String viewPage(@ModelAttribute LearningForm learningForm, HttpServletRequest request) throws Exception {
	super.viewPage(learningForm, request);
	return "pages/wiki/viewWiki";
    }

    @RequestMapping(path = "/changePage", method = RequestMethod.POST)
    public String changePage(@ModelAttribute LearningForm learningForm, HttpServletRequest request) throws Exception {
	Long pageUid = super.changePage(learningForm, request);
	return this.returnToWiki(learningForm, request, pageUid);
    }

    @RequestMapping(path = "/addPage", method = RequestMethod.POST)
    public String addPage(@ModelAttribute LearningForm learningForm, HttpServletRequest request) throws Exception {
	Long currentWikiPageId = super.addPage(learningForm, request);
	return returnToWiki(learningForm, request, currentWikiPageId);
    }

    @RequestMapping(path = "/removePage", method = RequestMethod.POST)
    public String removePage(@ModelAttribute LearningForm learningForm, HttpServletRequest request) throws Exception {
	Long currentPageUid = WebUtil.readLongParam(request, WikiConstants.ATTR_CURRENT_WIKI);
	super.removePage(learningForm, request);
	return this.returnToWiki(learningForm, request, currentPageUid);
    }

    @RequestMapping(path = "/restorePage", method = RequestMethod.POST)
    public String restorePage(@ModelAttribute LearningForm learningForm, HttpServletRequest request) throws Exception {
	super.restorePage(learningForm, request);
	Long currentWikiPageId = WebUtil.readLongParam(request, WikiConstants.ATTR_CURRENT_WIKI);
	return this.returnToWiki(learningForm, request, currentWikiPageId);
    }

    @RequestMapping(path = "/toggleLearnerSubsciption", method = RequestMethod.POST)
    public String toggleLearnerSubsciption(@ModelAttribute LearningForm learningForm, HttpServletRequest request)
	    throws Exception {
	super.toggleLearnerSubsciption(learningForm, request);
	Long currentWikiPageId = WebUtil.readLongParam(request, WikiConstants.ATTR_CURRENT_WIKI);
	return returnToWiki(learningForm, request, currentWikiPageId);
    }

    @Override
    @RequestMapping(path = "/notifyWikiChange", method = RequestMethod.POST)
    public void notifyWikiChange(Long toolSessionID, String subjectLangKey, String bodyLangKey, WikiUser wikiUser,
	    HttpServletRequest request) throws Exception {
	super.notifyWikiChange(toolSessionID, subjectLangKey, bodyLangKey, wikiUser, request);
    }

    @RequestMapping("/revertJavascriptTokenReplacement")
    public void revertJavascriptTokenReplacement(LearningForm learningForm) {
	super.revertJavascriptTokenReplacement(learningForm);
    }

    /**
     * Wrapper method to make sure that the correct wiki is returned to from the
     * WikiPageAction class
     */
    protected String returnToWiki(LearningForm learnForm, HttpServletRequest request, Long currentWikiPageId)
	    throws Exception {
	learnForm.setCurrentWikiPageId(currentWikiPageId);
	// put the tool session id in the attributes so that the progress bar can pick it up.
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, learnForm.getToolSessionID());
	return unspecified(learnForm, request);
    }

    /**
     * Gets the current user by toolSessionId
     */
    @Override
    protected WikiUser getCurrentUser(Long toolSessionId) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	WikiUser wikiUser = wikiService.getUserByUserIdAndSessionId(user.getUserID().longValue(),
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
     */
    @RequestMapping("/finishActivity")
    public String finishActivity(@ModelAttribute LearningForm learningForm, HttpServletRequest request,
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

	String nextActivityUrl;
	try {
	    nextActivityUrl = wikiService.leaveToolSession(toolSessionID, wikiUser.getUserId());
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
