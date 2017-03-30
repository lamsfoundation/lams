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


package org.lamsfoundation.lams.tool.scribe.web.actions;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.scribe.model.Scribe;
import org.lamsfoundation.lams.tool.scribe.model.ScribeHeading;
import org.lamsfoundation.lams.tool.scribe.service.IScribeService;
import org.lamsfoundation.lams.tool.scribe.service.ScribeServiceProxy;
import org.lamsfoundation.lams.tool.scribe.util.ScribeConstants;
import org.lamsfoundation.lams.tool.scribe.web.forms.AuthoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
public class AuthoringAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(AuthoringAction.class);

    public IScribeService scribeService;

    // Authoring SessionMap key names
    private static final String KEY_TOOL_CONTENT_ID = "toolContentID";

    private static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";

    private static final String KEY_MODE = "mode";

    private static final String KEY_HEADINGS = "headings";

    /**
     * Default method when no dispatch parameter is specified. It is expected
     * that the parameter <code>toolContentID</code> will be passed in. This
     * will be used to retrieve content for this tool.
     * 
     */
    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// Extract toolContentID from parameters.
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);

	// set up scribeService
	if (scribeService == null) {
	    scribeService = ScribeServiceProxy.getScribeService(this.getServlet().getServletContext());
	}

	// retrieving Scribe with given toolContentID
	Scribe scribe = scribeService.getScribeByContentId(toolContentID);
	if (scribe == null) {
	    scribe = scribeService.copyDefaultContent(toolContentID);
	    scribe.setCreateDate(new Date());
	    scribeService.saveOrUpdateScribe(scribe);
	    // TODO NOTE: this causes DB orphans when LD not saved.
	}

	// check if content in use is set
	if (scribe.isContentInUse()) {
	    // Cannot edit, send to message page.
	    request.setAttribute(ScribeConstants.ATTR_MESSAGE,
		    getResources(request).getMessage("error.content.locked"));
	    return mapping.findForward("message_page");
	}

	if (mode.isTeacher()) {
	    // Set the defineLater flag so that learners cannot use content
	    // while we
	    // are editing. This flag is released when updateContent is called.
	    scribe.setDefineLater(true);
	    scribeService.saveOrUpdateScribe(scribe);
	}

	// Set up the authForm.
	AuthoringForm authForm = (AuthoringForm) form;
	updateAuthForm(authForm, scribe);

	// Set up sessionMap
	SessionMap<String, Object> map = createSessionMap(scribe, mode, contentFolderID,
		toolContentID);
	authForm.setSessionMapID(map.getSessionID());

	// add the sessionMap to HTTPSession.
	request.getSession().setAttribute(map.getSessionID(), map);
	request.setAttribute(ScribeConstants.ATTR_SESSION_MAP, map);

	return mapping.findForward("success");
    }

    public ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// TODO need error checking.

	// get authForm and session map.
	AuthoringForm authForm = (AuthoringForm) form;
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	// get scribe content.
	Scribe scribe = scribeService.getScribeByContentId((Long) map.get(KEY_TOOL_CONTENT_ID));

	// update scribe content using form inputs.
	ToolAccessMode mode = (ToolAccessMode) map.get(KEY_MODE);
	updateScribe(scribe, authForm, mode);

	// update headings.
	List<ScribeHeading> updatedHeadings = getHeadingList(map);
	Set currentHeadings = scribe.getScribeHeadings();
	for (ScribeHeading heading : updatedHeadings) {
	    // it is update
	    if (heading.getUid() != null) {
		Iterator iter = currentHeadings.iterator();
		while (iter.hasNext()) {
		    ScribeHeading currHeading = (ScribeHeading) iter.next();
		    if (heading.getUid().equals(currHeading.getUid())) {
			// update fields
			currHeading.setHeadingText(heading.getHeadingText());
			currHeading.setDisplayOrder(heading.getDisplayOrder());
			break;
		    }
		} // go to next heading from page
	    } else {
		currentHeadings.add(heading);
	    }
	}
	// remove deleted heading.
	Iterator iter = currentHeadings.iterator();
	while (iter.hasNext()) {
	    ScribeHeading currHeading = (ScribeHeading) iter.next();
	    // skip new added heading
	    if (currHeading.getUid() == null) {
		continue;
	    }
	    boolean find = false;
	    for (ScribeHeading heading : updatedHeadings) {
		if (currHeading.getUid().equals(heading.getUid())) {
		    find = true;
		    break;
		}
	    }
	    if (!find) {
		// delete current heading's report first
		scribeService.deleteHeadingReport(currHeading.getUid());
		iter.remove();
	    }
	}

	// set the update date
	scribe.setUpdateDate(new Date());

	// releasing defineLater flag so that learner can start using the tool.
	scribe.setDefineLater(false);

	scribeService.saveOrUpdateScribe(scribe);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authForm.setSessionMapID(map.getSessionID());

	request.setAttribute(ScribeConstants.ATTR_SESSION_MAP, map);

	return mapping.findForward("success");
    }

    public ActionForward loadHeadingForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	String sessionMapID = WebUtil.readStrParam(request, "sessionMapID");
	Integer headingIndex = WebUtil.readIntParam(request, "headingIndex", true);

	AuthoringForm authForm = ((AuthoringForm) form);

	if (headingIndex == null) {
	    headingIndex = -1;
	}

	authForm.setHeadingIndex(headingIndex);
	authForm.setSessionMapID(sessionMapID);

	return mapping.findForward("heading_form");
    }

    public ActionForward addOrUpdateHeading(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	AuthoringForm authForm = (AuthoringForm) form;
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	String headingText = authForm.getHeading();
	Integer headingIndex = authForm.getHeadingIndex();
	Long toolContentID = getToolContentID(map);

	Scribe scribe = scribeService.getScribeByContentId(toolContentID);

	if (headingIndex == -1) {
	    // create a new heading
	    List<ScribeHeading> headings = getHeadingList(map);

	    ScribeHeading scribeHeading = new ScribeHeading(headings.size());
	    scribeHeading.setScribe(scribe);
	    scribeHeading.setHeadingText(headingText);

	    headings.add(scribeHeading);
	} else {
	    // update the existing heading
	    ScribeHeading heading = getHeadingList(map).get(headingIndex);
	    heading.setHeadingText(headingText);
	}

	request.setAttribute("sessionMapID", map.getSessionID());
	return mapping.findForward("heading_response");
    }

    public ActionForward moveHeading(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	AuthoringForm authForm = (AuthoringForm) form;
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	int headingIndex = WebUtil.readIntParam(request, "headingIndex");
	String direction = WebUtil.readStrParam(request, "direction");

	ListIterator<ScribeHeading> iter = getHeadingList(map).listIterator(headingIndex);

	ScribeHeading heading = iter.next();
	iter.remove();

	// move to correct location
	if (direction.equals("up")) {
	    if (iter.hasPrevious()) {
		iter.previous();
	    }
	} else if (direction.equals("down")) {
	    if (iter.hasNext()) {
		iter.next();
	    }
	} else {
	    // invalid direction, don't move anywhere.
	    log.error("moveHeading: received invalid direction : " + direction);
	}

	// adding heading back into list
	iter.add(heading);

	// update the displayOrder
	int i = 0;
	for (ScribeHeading elem : getHeadingList(map)) {
	    elem.setDisplayOrder(i);
	    i++;
	}

	request.setAttribute("sessionMapID", map.getSessionID());
	return mapping.findForward("heading_response");
    }

    public ActionForward deleteHeading(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	AuthoringForm authForm = (AuthoringForm) form;
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	Integer headingIndex = authForm.getHeadingIndex();

	getHeadingList(map).remove(headingIndex.intValue());

	request.setAttribute("sessionMapID", map.getSessionID());
	return mapping.findForward("heading_response");

    }

    /* ========== Private Methods ********** */

    /**
     * Updates Scribe content using AuthoringForm inputs.
     * 
     * @param authForm
     * @param mode
     * @return
     */
    private void updateScribe(Scribe scribe, AuthoringForm authForm, ToolAccessMode mode) {
	scribe.setTitle(authForm.getTitle());
	scribe.setInstructions(authForm.getInstructions());
	if (mode.isAuthor()) { // Teacher cannot modify following
	    scribe.setReflectOnActivity(authForm.isReflectOnActivity());
	    scribe.setReflectInstructions(authForm.getReflectInstructions());
	    scribe.setAutoSelectScribe(authForm.isAutoSelectScribe());
	    scribe.setShowAggregatedReports(authForm.isShowAggregatedReports());
	}
    }

    /**
     * Updates AuthoringForm using Scribe content.
     * 
     * @param scribe
     * @param authForm
     * @return
     */
    private void updateAuthForm(AuthoringForm authForm, Scribe scribe) {
	authForm.setTitle(scribe.getTitle());
	authForm.setInstructions(scribe.getInstructions());
	authForm.setReflectOnActivity(scribe.isReflectOnActivity());
	authForm.setReflectInstructions(scribe.getReflectInstructions());
	authForm.setAutoSelectScribe(scribe.isAutoSelectScribe());
	authForm.setShowAggregatedReports(scribe.isShowAggregatedReports());
    }

    /**
     * Updates SessionMap using Scribe content.
     * 
     * @param scribe
     * @param mode
     */
    private SessionMap<String, Object> createSessionMap(Scribe scribe, ToolAccessMode mode, String contentFolderID,
	    Long toolContentID) {

	SessionMap<String, Object> map = new SessionMap<String, Object>();

	map.put(KEY_MODE, mode);
	map.put(KEY_CONTENT_FOLDER_ID, contentFolderID);
	map.put(KEY_TOOL_CONTENT_ID, toolContentID);
	map.put(KEY_HEADINGS, new LinkedList<ScribeHeading>());

	// adding headings
	Iterator iter = scribe.getScribeHeadings().iterator();
	while (iter.hasNext()) {
	    ScribeHeading element = (ScribeHeading) iter.next();
	    getHeadingList(map).add(element);
	}

	// sorting headings according to displayOrder.
	Collections.sort(getHeadingList(map));

	return map;
    }

    private List<ScribeHeading> getHeadingList(SessionMap<String, Object> map) {
	return (List<ScribeHeading>) map.get(KEY_HEADINGS);
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

    private Long getToolContentID(SessionMap map) {
	return (Long) map.get(KEY_TOOL_CONTENT_ID);
    }
}