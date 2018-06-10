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

package org.lamsfoundation.lams.tool.zoom.web.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.zoom.model.Zoom;
import org.lamsfoundation.lams.tool.zoom.service.IZoomService;
import org.lamsfoundation.lams.tool.zoom.service.ZoomServiceProxy;
import org.lamsfoundation.lams.tool.zoom.util.ZoomConstants;
import org.lamsfoundation.lams.tool.zoom.web.forms.AuthoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

public class AuthoringAction extends DispatchAction {

    private IZoomService zoomService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// set up zoomService
	zoomService = ZoomServiceProxy.getZoomService(this.getServlet().getServletContext());

	return super.execute(mapping, form, request, response);
    }

    /**
     * Default method when no dispatch parameter is specified. It is expected that the parameter
     * <code>toolContentID</code> will be passed in. This will be used to retrieve content for this tool.
     *
     * @throws ServletException
     *
     */
    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {

	// Extract toolContentID from parameters.
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);

	// retrieving Zoom with given toolContentID
	Zoom zoom = zoomService.getZoomByContentId(toolContentID);
	if (zoom == null) {
	    zoom = zoomService.copyDefaultContent(toolContentID);
	    zoom.setCreateDate(new Date());
	    zoomService.saveOrUpdateZoom(zoom);
	}

	if (mode.isTeacher()) {
	    // Set the defineLater flag so that learners cannot use content
	    // while we are editing. This flag is released when updateContent is
	    // called.
	    zoom.setDefineLater(true);
	    zoomService.saveOrUpdateZoom(zoom);

	    //audit log the teacher has started editing activity in monitor
	    zoomService.auditLogStartEditingActivityInMonitor(toolContentID);
	}

	// Set up the authForm.
	AuthoringForm authForm = (AuthoringForm) form;
	copyProperties(authForm, zoom);

	// Set up sessionMap
	SessionMap<String, Object> map = createSessionMap(zoom, mode, contentFolderID, toolContentID);
	authForm.setSessionMapID(map.getSessionID());

	// add the sessionMap to HTTPSession.
	request.getSession().setAttribute(map.getSessionID(), map);
	request.setAttribute(ZoomConstants.ATTR_SESSION_MAP, map);

	return mapping.findForward("success");
    }

    public ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get authForm and session map.
	AuthoringForm authForm = (AuthoringForm) form;
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	// get zoom content.
	Zoom zoom = zoomService.getZoomByContentId((Long) map.get(ZoomConstants.KEY_TOOL_CONTENT_ID));

	// update zoom content using form inputs
	copyProperties(zoom, authForm);

	// set the update date
	zoom.setUpdateDate(new Date());

	// releasing defineLater flag so that learner can start using the tool.
	zoom.setDefineLater(false);
	zoomService.saveOrUpdateZoom(zoom);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authForm.setSessionMapID(map.getSessionID());

	request.setAttribute(ZoomConstants.ATTR_SESSION_MAP, map);

	return mapping.findForward("success");
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
