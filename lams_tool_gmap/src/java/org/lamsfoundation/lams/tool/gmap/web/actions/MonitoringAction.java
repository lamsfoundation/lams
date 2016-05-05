/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.tool.gmap.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.gmap.dto.GmapDTO;
import org.lamsfoundation.lams.tool.gmap.dto.GmapSessionDTO;
import org.lamsfoundation.lams.tool.gmap.dto.GmapUserDTO;
import org.lamsfoundation.lams.tool.gmap.model.Gmap;
import org.lamsfoundation.lams.tool.gmap.model.GmapConfigItem;
import org.lamsfoundation.lams.tool.gmap.model.GmapSession;
import org.lamsfoundation.lams.tool.gmap.model.GmapUser;
import org.lamsfoundation.lams.tool.gmap.service.GmapServiceProxy;
import org.lamsfoundation.lams.tool.gmap.service.IGmapService;
import org.lamsfoundation.lams.tool.gmap.util.GmapConstants;
import org.lamsfoundation.lams.tool.gmap.util.GmapException;
import org.lamsfoundation.lams.tool.gmap.web.forms.MonitoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
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
public class MonitoringAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(MonitoringAction.class);

    public IGmapService gmapService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	Gmap gmap = gmapService.getGmapByContentId(toolContentID);

	if (gmap == null) {
	    // TODO error page.
	}

	GmapDTO gmapDT0 = new GmapDTO(gmap);

	// Adding the markers lists to a map with tool sessions as the key

	if (gmapDT0.getSessionDTOs() != null) {
	    for (GmapSessionDTO sessionDTO : gmapDT0.getSessionDTOs()) {
		Long toolSessionID = sessionDTO.getSessionID();
		sessionDTO.setMarkerDTOs(gmapService.getGmapMarkersBySessionId(toolSessionID));

		for (GmapUserDTO userDTO : sessionDTO.getUserDTOs()) {
		    // get the notebook entry.
		    NotebookEntry notebookEntry = gmapService.getEntry(toolSessionID,
			    CoreNotebookConstants.NOTEBOOK_TOOL, GmapConstants.TOOL_SIGNATURE,
			    userDTO.getUserId().intValue());
		    if (notebookEntry != null) {
			userDTO.setFinishedReflection(true);
			//userDTO.setNotebookEntry(notebookEntry.getEntry());
		    } else {
			userDTO.setFinishedReflection(false);
		    }
		    sessionDTO.getUserDTOs().add(userDTO);
		}
	    }
	}

	// get the gmap API key from the config table and add it to the session
	GmapConfigItem gmapKey = gmapService.getConfigItem(GmapConfigItem.KEY_GMAP_KEY);
	if (gmapKey != null && gmapKey.getConfigValue() != null) {
	    request.setAttribute(GmapConstants.ATTR_GMAP_KEY, gmapKey.getConfigValue());
	}

	boolean isGroupedActivity = gmapService.isGroupedActivity(toolContentID);
	request.setAttribute("isGroupedActivity", isGroupedActivity);
	request.setAttribute("gmapDTO", gmapDT0);
	request.setAttribute("contentFolderID", contentFolderID);
	return mapping.findForward("success");
    }

    /**
     * set up gmapService
     */
    private void setupService() {
	if (gmapService == null) {
	    gmapService = GmapServiceProxy.getGmapService(this.getServlet().getServletContext());
	}
    }

    /**
     * Allows teachers to edit/remove existing markers
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward saveMarkers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	MonitoringForm monitoringForm = (MonitoringForm) form;
	Long toolSessionID = monitoringForm.getToolSessionID();
	//Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");

	GmapUser gmapUser = getCurrentUser(toolSessionID);

	if (gmapUser != null) {

	    //MonitoringForm monitoringForm = (MonitoringForm) form;

	    // Retrieve the session and content.
	    GmapSession gmapSession = gmapService.getSessionBySessionId(toolSessionID);
	    if (gmapSession == null) {
		throw new GmapException("Cannot retrieve session with toolSessionID" + toolSessionID);
	    }

	    // update the marker list
	    Gmap gmap = gmapSession.getGmap();
	    gmapService.updateMarkerListFromXML(monitoringForm.getMarkersXML(), gmap, gmapUser, true, gmapSession);

	} else {
	    log.error("saveMarkers(): couldn't find GmapUser with id: " + gmapUser.getUserId() + "and toolSessionID: "
		    + toolSessionID);
	}

	return unspecified(mapping, form, request, response);
    }

    /**
     * Get the current user
     *
     * @param toolSessionId
     * @return
     */
    private GmapUser getCurrentUser(Long toolSessionId) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	GmapUser gmapUser = gmapService.getUserByUserIdAndSessionId(new Long(user.getUserID().intValue()),
		toolSessionId);

	if (gmapUser == null) {
	    GmapSession gmapSession = gmapService.getSessionBySessionId(toolSessionId);
	    gmapUser = gmapService.createGmapUser(user, gmapSession);
	}

	return gmapUser;
    }

    /**
     * Opens a user's reflection
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward openNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	//MonitoringForm monitorForm = (MonitoringForm) form;
	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionID", false);
	Long userID = WebUtil.readLongParam(request, "userID", false);

	GmapUser gmapUser = gmapService.getUserByUserIdAndSessionId(userID, toolSessionId);

	NotebookEntry notebookEntry = gmapService.getEntry(toolSessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		GmapConstants.TOOL_SIGNATURE, userID.intValue());

	GmapUserDTO gmapUserDTO = new GmapUserDTO(gmapUser);
	gmapUserDTO.setNotebookEntry(notebookEntry.getEntry());

	request.setAttribute("gmapUserDTO", gmapUserDTO);

	return mapping.findForward("notebook");
    }
}
