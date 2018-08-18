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

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.tomcat.util.json.JSONException;
import org.lamsfoundation.lams.tool.zoom.model.Zoom;
import org.lamsfoundation.lams.tool.zoom.service.IZoomService;

public class ZoomUtil {

    /**
     * Creates and starts a Zoom meeting
     */
    public static ActionErrors startMeeting(IZoomService zoomService, Zoom zoom, HttpServletRequest request)
	    throws IOException, JSONException {
	ActionErrors errors = new ActionErrors();
	String meetingURL = zoom.getMeetingStartUrl();
	if (meetingURL == null) {
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
	}

	request.setAttribute(ZoomConstants.ATTR_MEETING_URL, meetingURL);
	return errors;
    }
}