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

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.zoom.dto.ContentDTO;
import org.lamsfoundation.lams.tool.zoom.model.Zoom;
import org.lamsfoundation.lams.tool.zoom.service.IZoomService;
import org.lamsfoundation.lams.tool.zoom.util.ZoomConstants;
import org.lamsfoundation.lams.tool.zoom.util.ZoomUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {

    private static final Logger logger = Logger.getLogger(MonitoringController.class);

    @Autowired
    private IZoomService zoomService;

    @Autowired
    @Qualifier("zoomMessageService")
    private MessageService messageService;

    @RequestMapping("/start")
    public String start(HttpServletRequest request) {

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

	return "pages/monitoring/monitoring";
    }

    @RequestMapping("/startMeeting")
    public String startMeeting(HttpServletRequest request) throws Exception {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID, false);
	Zoom zoom = zoomService.getZoomByContentId(toolContentID);

	ContentDTO contentDTO = new ContentDTO();
	contentDTO.setTitle(zoom.getTitle());
	contentDTO.setInstructions(zoom.getInstructions());
	request.setAttribute(ZoomConstants.ATTR_CONTENT_DTO, contentDTO);

	MultiValueMap<String, String> errorMap = ZoomUtil.startMeeting(zoomService, messageService, zoom, request);
	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	}

	return "pages/learning/learning";
    }
}
