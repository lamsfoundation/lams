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

package org.lamsfoundation.lams.tool.zoom.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.tool.zoom.model.Zoom;
import org.lamsfoundation.lams.tool.zoom.service.IZoomService;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class ZoomUtil {

    /**
     * Creates and starts a Zoom meeting
     */
    public static MultiValueMap<String, String> startMeeting(IZoomService zoomService, MessageService messageService,
	    Zoom zoom, HttpServletRequest request) throws IOException {
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	String meetingURL = zoom.getMeetingStartUrl();
	if (meetingURL == null) {
	    Boolean apiOK = zoomService.chooseApi(zoom.getUid());
	    if (apiOK == null) {
		errorMap.add("GLOBAL", messageService.getMessage("error.api.none.configured"));
		request.setAttribute("skipContent", true);
	    } else {
		if (!apiOK) {
		    errorMap.add("GLOBAL", messageService.getMessage("error.api.reuse"));
		}
		zoom = zoomService.createMeeting(zoom.getUid());
		meetingURL = zoom.getMeetingStartUrl();
	    }
	}

	request.setAttribute(ZoomConstants.ATTR_MEETING_URL, meetingURL);
	if (zoom.isEnableMeetingPassword()) {
	    request.setAttribute(ZoomConstants.ATTR_MEETING_PASSWORD, zoom.getMeetingPassword());
	}
	return errorMap;
    }
}