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

package org.lamsfoundation.lams.tool.zoom.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.zoom.model.Zoom;
import org.lamsfoundation.lams.tool.zoom.service.IZoomService;
import org.lamsfoundation.lams.tool.zoom.util.ZoomConstants;
import org.lamsfoundation.lams.tool.zoom.web.forms.AuthoringForm;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/authoring")
public class AuthoringController {

    @Autowired
    private IZoomService zoomService;

    @Autowired
    @Qualifier("zoomMessageService")
    private MessageService messageService;

    @RequestMapping("/start")
    public String start(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request)
	    throws ServletException {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);

	// retrieving Zoom with given toolContentID
	Zoom zoom = zoomService.getZoomByContentId(toolContentID);
	if (zoom == null) {
	    zoom = zoomService.copyDefaultContent(toolContentID);
	    zoom.setCreateDate(new Date());
	    zoomService.saveOrUpdateZoom(zoom);
	}

	return readDatabaseData(authoringForm, zoom, request, mode);
    }

    /**
     * Set the defineLater flag so that learners cannot use content while we are editing. This flag is released when
     * updateContent is called.
     */
    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    public String definelater(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request) throws ServletException {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Zoom zoom = zoomService.getZoomByContentId(toolContentID);
	zoom.setDefineLater(true);
	zoomService.saveOrUpdateZoom(zoom);

	//audit log the teacher has started editing activity in monitor
	zoomService.auditLogStartEditingActivityInMonitor(toolContentID);

	return readDatabaseData(authoringForm, zoom, request, ToolAccessMode.TEACHER);
    }
    
    /**
     * Common method for "unspecified" and "defineLater"
     */
    private String readDatabaseData(AuthoringForm authoringForm, Zoom zoom, HttpServletRequest request,
	    ToolAccessMode mode) throws ServletException {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	
	// Set up the authForm.
	copyProperties(authoringForm, zoom);

	// Set up sessionMap
	SessionMap<String, Object> map = createSessionMap(zoom, mode, contentFolderID, toolContentID);
	authoringForm.setSessionMapID(map.getSessionID());

	// add the sessionMap to HTTPSession.
	request.getSession().setAttribute(map.getSessionID(), map);
	request.setAttribute(ZoomConstants.ATTR_SESSION_MAP, map);

	if (zoomService.getApis().isEmpty()) {
	    MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>(1);
	    errorMap.add("GLOBAL", messageService.getMessage("error.api.none.configured"));
	    request.setAttribute("errorMap", errorMap);
	}

	return "pages/authoring/authoring";
    }

    @RequestMapping(path = "/updateContent", method = RequestMethod.POST)
    public String updateContent(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request,
	    HttpServletResponse response) {

	// get authForm and session map.
	SessionMap<String, Object> map = getSessionMap(request, authoringForm);

	// get zoom content.
	Zoom zoom = zoomService.getZoomByContentId((Long) map.get(ZoomConstants.KEY_TOOL_CONTENT_ID));

	// update zoom content using form inputs
	copyProperties(zoom, authoringForm);

	// set the update date
	zoom.setUpdateDate(new Date());

	// releasing defineLater flag so that learner can start using the tool.
	zoom.setDefineLater(false);
	zoomService.saveOrUpdateZoom(zoom);

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authoringForm.setSessionMapID(map.getSessionID());

	request.setAttribute(ZoomConstants.ATTR_SESSION_MAP, map);

	return "pages/authoring/authoring";
    }

    /* ========== Private Methods */

    /**
     * Updates Zoom content using AuthoringForm inputs.
     *
     * @param authForm
     * @param mode
     * @return
     */
    private void copyProperties(Zoom zoom, AuthoringForm authForm) {
	zoom.setTitle(authForm.getTitle());
	zoom.setInstructions(authForm.getInstructions());
	zoom.setReflectOnActivity(authForm.isReflectOnActivity());
	zoom.setReflectInstructions(authForm.getReflectInstructions());
	zoom.setStartInMonitor(authForm.isStartInMonitor());
	Integer duration = authForm.getDuration();
	zoom.setDuration(duration != null && duration > 0 ? duration : null);
    }

    /**
     * Updates AuthoringForm using Zoom content.
     *
     * @param zoom
     * @param authForm
     * @return
     * @throws ServletException
     */
    private void copyProperties(AuthoringForm authForm, Zoom zoom) throws ServletException {
	try {
	    BeanUtils.copyProperties(authForm, zoom);
	} catch (IllegalAccessException e) {
	    throw new ServletException(e);
	} catch (InvocationTargetException e) {
	    throw new ServletException(e);
	}
    }

    /**
     * Updates SessionMap using Zoom content.
     *
     * @param zoom
     * @param mode
     */
    private SessionMap<String, Object> createSessionMap(Zoom zoom, ToolAccessMode mode, String contentFolderID,
	    Long toolContentID) {

	SessionMap<String, Object> map = new SessionMap<String, Object>();

	map.put(ZoomConstants.KEY_MODE, mode);
	map.put(ZoomConstants.KEY_CONTENT_FOLDER_ID, contentFolderID);
	map.put(ZoomConstants.KEY_TOOL_CONTENT_ID, toolContentID);

	return map;
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
}
