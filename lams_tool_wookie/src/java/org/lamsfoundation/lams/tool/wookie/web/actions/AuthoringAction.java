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


package org.lamsfoundation.lams.tool.wookie.web.actions;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.wookie.dto.WidgetDefinition;
import org.lamsfoundation.lams.tool.wookie.model.Wookie;
import org.lamsfoundation.lams.tool.wookie.service.IWookieService;
import org.lamsfoundation.lams.tool.wookie.service.WookieServiceProxy;
import org.lamsfoundation.lams.tool.wookie.util.WookieConstants;
import org.lamsfoundation.lams.tool.wookie.util.WookieException;
import org.lamsfoundation.lams.tool.wookie.util.WookieUtil;
import org.lamsfoundation.lams.tool.wookie.web.forms.AuthoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * @author
 * @version
 *
 * @struts.action path="/authoring" name="authoringForm" parameter="dispatch"
 *                scope="request" validate="false"
 *
 * @struts.action-forward name="success" path="tiles:/authoring/main"
 * @struts.action-forward name="widgetList"
 *                        path="/pages/authoring/widgetList.jsp"
 * @struts.action-forward name="message_page" path="tiles:/generic/message"
 */
public class AuthoringAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(AuthoringAction.class);

    public IWookieService wookieService;

    // Authoring SessionMap key names
    private static final String KEY_TOOL_CONTENT_ID = "toolContentID";
    private static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";
    private static final String KEY_MODE = "mode";

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

	AuthoringForm authForm = (AuthoringForm) form;

	ToolAccessMode mode = null;
	String modeStr = WebUtil.readStrParam(request, "mode", true);
	if (modeStr != null && !modeStr.trim().equals("")) {
	    mode = WebUtil.readToolAccessModeParam(request, "mode", true);
	}

	// set up wookieService
	if (wookieService == null) {
	    wookieService = WookieServiceProxy.getWookieService(this.getServlet().getServletContext());
	}

	String wookieUrl = wookieService.getWookieURL();
	if (wookieUrl == null) {
	    throw new WookieException(wookieService.getMessage("error.wookie.not.configured"));
	}

	// Get the widget count
	try {
	    int widgetCount = WookieUtil.getWidgetCount(wookieUrl);
	    if (widgetCount > 0) {
		int pages = 1;
		float pagesFloat = new Float(widgetCount) / WookieConstants.WIDGETS_PER_PAGE;
		if (pagesFloat > 1) {

		    pages = (new Float(Math.ceil(pagesFloat))).intValue();
		}

		request.setAttribute(WookieConstants.ATTR_WIDGET_PAGES, pages);
	    }
	} catch (Exception e) {
	    logger.error("Error initiating widget on wookie server", e);
	    throw new WookieException(wookieService.getMessage("error.initiating.widget"), e);
	}

	// retrieving Wookie with given toolContentID
	Wookie wookie = wookieService.getWookieByContentId(toolContentID);
	if (wookie == null) {
	    wookie = wookieService.copyDefaultContent(toolContentID);
	    wookie.setCreateDate(new Date());
	    wookie.setCreateBy(getUser().getUserID());
	    wookieService.saveOrUpdateWookie(wookie);
	}

	if (mode != null && mode.isTeacher()) {
	    // Set the defineLater flag so that learners cannot use content
	    // while we
	    // are editing. This flag is released when updateContent is called.
	    wookie.setDefineLater(true);
	    wookieService.saveOrUpdateWookie(wookie);
	}

	// Set up the authForm.
	updateAuthForm(authForm, wookie);
	authForm.setToolContentID(toolContentID);
	if (mode != null) {
	    authForm.setMode(mode.toString());
	} else {
	    authForm.setMode("");
	}
	authForm.setContentFolderID(contentFolderID);

	// Set up sessionMap
	SessionMap<String, Object> map = createSessionMap(wookie, getAccessMode(request), contentFolderID,
		toolContentID);
	authForm.setSessionMapID(map.getSessionID());

	// add the sessionMap to HTTPSession.
	request.getSession().setAttribute(map.getSessionID(), map);
	request.setAttribute(WookieConstants.ATTR_SESSION_MAP, map);

	return mapping.findForward("success");
    }

    public ActionForward getWidgets(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws WookieException {

	// set up wookieService
	if (wookieService == null) {
	    wookieService = WookieServiceProxy.getWookieService(this.getServlet().getServletContext());
	}

	Integer pageNumber = WebUtil.readIntParam(request, WookieConstants.PARAM_PAGE_NUMBER);

	// Fetch all the available wookie widgets
	try {
	    String wookieUrl = wookieService.getWookieURL();
	    if (wookieUrl == null) {
		throw new WookieException(wookieService.getMessage("error.wookie.not.configured"));
	    }

	    List<WidgetDefinition> widgetDefinitions = WookieUtil.getWidgetDefinitions(wookieUrl);
	    List<WidgetDefinition> cutDownWidgetDefinitions = null;

	    int startIndex = WookieConstants.WIDGETS_PER_PAGE * (pageNumber - 1);
	    int endIndex = WookieConstants.WIDGETS_PER_PAGE * (pageNumber);
	    if (widgetDefinitions.size() > startIndex) {
		if (widgetDefinitions.size() > endIndex) {
		    // Use the start and end index - within range
		    cutDownWidgetDefinitions = widgetDefinitions.subList(startIndex, endIndex);
		} else {
		    // Use the start index to the end of the list
		    cutDownWidgetDefinitions = widgetDefinitions.subList(startIndex, widgetDefinitions.size());
		}

	    }
	    request.setAttribute(WookieConstants.ATTR_WIDGET_LIST, cutDownWidgetDefinitions);
	    return mapping.findForward("widgetList");

	} catch (Exception e) {
	    logger.error("Problem reading xml from wookie server.", e);
	    throw new WookieException(e);
	}
    }

    public ActionForward initiateWidget(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws WookieException {
	try {
	    // set up wookieService
	    if (wookieService == null) {
		wookieService = WookieServiceProxy.getWookieService(this.getServlet().getServletContext());
	    }

	    String wookieUrl = wookieService.getWookieURL();
	    String wookieKey = wookieService.getWookieAPIKey();
	    String wookieIdentifier = WebUtil.readStrParam(request, WookieConstants.PARAM_KEY_WIDGET_ID);
	    Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	    wookieUrl += WookieConstants.RELATIVE_URL_WIDGET_SERVICE;

	    String returnXML = WookieUtil.getWidget(wookieUrl, wookieKey, wookieIdentifier, getUser(),
		    toolContentID.toString(), true);

	    this.writeResponse(response, "text/xml;charset=utf-8", "utf-8", returnXML);

	} catch (Exception e) {
	    log.error("Problem intitiation widget" + e);
	    throw new WookieException(e);
	}
	return null;
    }

    public ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws WookieException {
	// TODO need error checking.

	// get authForm and session map.
	AuthoringForm authForm = (AuthoringForm) form;
	SessionMap<String, Object> map = getSessionMap(request, authForm);
	ToolAccessMode mode = (ToolAccessMode) map.get(AuthoringAction.KEY_MODE);

	// get wookie content.
	Wookie wookie = wookieService.getWookieByContentId((Long) map.get(AuthoringAction.KEY_TOOL_CONTENT_ID));

	// update wookie content using form inputs.
	updateWookie(wookie, authForm, mode);

	// set the update date
	wookie.setUpdateDate(new Date());

	// releasing defineLater flag so that learner can start using the tool.
	wookie.setDefineLater(false);

	wookieService.saveOrUpdateWookie(wookie);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authForm.setSessionMapID(map.getSessionID());

	request.setAttribute(WookieConstants.ATTR_SESSION_MAP, map);

	updateAuthForm(authForm, wookie);
	if (mode != null) {
	    authForm.setMode(mode.toString());
	} else {
	    authForm.setMode("");
	}

	return mapping.findForward("success");
    }

    /* ========== Private Methods ********** */

    /**
     * Updates Wookie content using AuthoringForm inputs.
     * 
     * @param authForm
     * @param mode
     * @return
     */
    private void updateWookie(Wookie wookie, AuthoringForm authForm, ToolAccessMode mode) {
	wookie.setTitle(authForm.getTitle());
	wookie.setInstructions(authForm.getInstructions());
	if (mode.isAuthor()) { // Teacher cannot modify following
	    wookie.setLockOnFinished(authForm.isLockOnFinished());
	    wookie.setReflectOnActivity(authForm.isReflectOnActivity());
	    wookie.setReflectInstructions(authForm.getReflectInstructions());
	    wookie.setWidgetAuthorUrl(authForm.getWidgetAuthorUrl());
	    wookie.setWidgetHeight(authForm.getWidgetHeight());
	    wookie.setWidgetWidth(authForm.getWidgetWidth());
	    wookie.setWidgetMaximise(authForm.getWidgetMaximise());
	    wookie.setWidgetIdentifier(authForm.getWidgetIdentifier());
	}
    }

    /**
     * Updates AuthoringForm using Wookie content.
     * 
     * @param wookie
     * @param authForm
     * @return
     */
    private void updateAuthForm(AuthoringForm authForm, Wookie wookie) {
	authForm.setTitle(wookie.getTitle());
	authForm.setInstructions(wookie.getInstructions());
	authForm.setLockOnFinished(wookie.isLockOnFinished());
	authForm.setReflectOnActivity(wookie.isReflectOnActivity());
	authForm.setReflectInstructions(wookie.getReflectInstructions());

	// Set the wookie params
	authForm.setWidgetAuthorUrl(wookie.getWidgetAuthorUrl());
	authForm.setWidgetHeight(wookie.getWidgetHeight());
	authForm.setWidgetWidth(wookie.getWidgetWidth());
	authForm.setWidgetMaximise(wookie.getWidgetMaximise());
	authForm.setWidgetIdentifier(wookie.getWidgetIdentifier());
    }

    /**
     * Updates SessionMap using Wookie content.
     * 
     * @param wookie
     * @param mode
     */
    private SessionMap<String, Object> createSessionMap(Wookie wookie, ToolAccessMode mode, String contentFolderID,
	    Long toolContentID) {

	SessionMap<String, Object> map = new SessionMap<String, Object>();

	map.put(AuthoringAction.KEY_MODE, mode);
	map.put(AuthoringAction.KEY_CONTENT_FOLDER_ID, contentFolderID);
	map.put(AuthoringAction.KEY_TOOL_CONTENT_ID, toolContentID);

	return map;
    }

    /**
     * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR
     * mode.
     * 
     * @param request
     * @return
     */
    private ToolAccessMode getAccessMode(HttpServletRequest request) {
	ToolAccessMode mode;
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equalsIgnoreCase(modeStr, ToolAccessMode.TEACHER.toString())) {
	    mode = ToolAccessMode.TEACHER;
	} else {
	    mode = ToolAccessMode.AUTHOR;
	}
	return mode;
    }

    /**
     * Retrieve the SessionMap from the HttpSession.
     * 
     * @param request
     * @param authForm
     * @return
     */
    @SuppressWarnings("unchecked")
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request, AuthoringForm authForm) {
	return (SessionMap<String, Object>) request.getSession().getAttribute(authForm.getSessionMapID());
    }

    private UserDTO getUser() {
	return (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
    }
}
