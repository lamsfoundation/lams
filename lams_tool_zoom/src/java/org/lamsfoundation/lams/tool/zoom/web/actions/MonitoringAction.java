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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.zoom.dto.ContentDTO;
import org.lamsfoundation.lams.tool.zoom.dto.NotebookEntryDTO;//import org.lamsfoundation.lams.tool.zoom.dto.UserDTO;
import org.lamsfoundation.lams.tool.zoom.dto.ZoomUserDTO;
import org.lamsfoundation.lams.tool.zoom.model.Zoom;
import org.lamsfoundation.lams.tool.zoom.model.ZoomUser;
import org.lamsfoundation.lams.tool.zoom.service.IZoomService;
import org.lamsfoundation.lams.tool.zoom.service.ZoomServiceProxy;
import org.lamsfoundation.lams.tool.zoom.util.ZoomConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class MonitoringAction extends DispatchAction {

    private static final Logger logger = Logger.getLogger(MonitoringAction.class);

    private IZoomService zoomService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// set up zoomService
	zoomService = ZoomServiceProxy.getZoomService(this.getServlet().getServletContext());

	return super.execute(mapping, form, request, response);
    }

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	Zoom zoom = zoomService.getZoomByContentId(toolContentID);

	if (zoom == null) {
	    logger.error("Unable to find tool content with id :" + toolContentID);
	}

	ContentDTO contentDTO = new ContentDTO(zoom);

	Long currentTab = WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true);
	contentDTO.setCurrentTab(currentTab);
	contentDTO.setGroupedActivity(zoomService.isGroupedActivity(toolContentID));

	request.setAttribute(ZoomConstants.ATTR_CONTENT_DTO, contentDTO);
	request.setAttribute(ZoomConstants.ATTR_CONTENT_FOLDER_ID, contentFolderID);

	return mapping.findForward("success");
    }

    public ActionForward openNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Long uid = new Long(WebUtil.readLongParam(request, ZoomConstants.PARAM_USER_UID));

	ZoomUser user = zoomService.getUserByUID(uid);
	NotebookEntry entry = zoomService.getNotebookEntry(user.getNotebookEntryUID());

	ZoomUserDTO userDTO = new ZoomUserDTO(user);
	userDTO.setNotebookEntryDTO(new NotebookEntryDTO(entry));

	request.setAttribute(ZoomConstants.ATTR_USER_DTO, userDTO);

	return mapping.findForward("notebook");
    }

    public ActionForward startMeeting(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID, false);
	Zoom zoom = zoomService.getZoomByContentId(toolContentID);

	ContentDTO contentDTO = new ContentDTO();
	contentDTO.setTitle(zoom.getTitle());
	contentDTO.setInstructions(zoom.getInstructions());
	request.setAttribute(ZoomConstants.ATTR_CONTENT_DTO, contentDTO);

	String meetingURL = zoom.getMeetingStartUrl();

	if (meetingURL == null) {
	    ActionErrors errors = new ActionErrors();

	    Boolean apiOK = zoomService.chooseApi(zoom.getUid());
	    if (apiOK == null) {
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.api.none.configured"));
		request.setAttribute("skipContent", true);
	    } else {
		if (!apiOK) {
		    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.api.reuse"));
		}
		meetingURL = zoomService.createMeeting(zoom.getUid());
	    }
	    if (!errors.isEmpty()) {
		this.addErrors(request, errors);
	    }
	}

	request.setAttribute(ZoomConstants.ATTR_MEETING_URL, meetingURL);

	return mapping.findForward("learning");
    }
}
