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

public final class ZoomConstants {

    public static final String APP_RESOURCES = "org.lamsfoundation.lams.tool.zoom.ApplicationResources";

    // Attribute names
    public static final String ATTR_CONFIG_DTO = "configDTO";
    public static final String ATTR_CONTENT_DTO = "contentDTO";
    public static final String ATTR_CONTENT_FOLDER_ID = "contentFolderID";
    public static final String ATTR_MEETING_OPEN = "meetingOpen";
    public static final String ATTR_MEETING_URL = "meetingURL";
    public static final String ATTR_MEETING_PASSWORD = "meetingPassword";
    public static final String ATTR_MESSAGE_KEY = "messageKey";
    public static final String ATTR_SESSION_MAP = "sessionMap";
    public static final String ATTR_TOOL_SESSION_ID = "toolSessionID";
    public static final String ATTR_USER_DTO = "userDTO";

    // Configuration keys

    // Authoring SessionMap key names
    public static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";
    public static final String KEY_MODE = "mode";
    public static final String KEY_TOOL_CONTENT_ID = "toolContentID";

    // Parameter names
    public static final String PARAM_USER_UID = "userUID";

    // Tool signature
    public static final String TOOL_SIGNATURE = "lazoom10";
    public static final String TOOL_CONTRIBUTE_URL = "tool/" + TOOL_SIGNATURE
	    + "/monitoring/startMeeting.do?toolContentID=";

    public static final String ZOOM_API_URL = "https://api.zoom.us/v2/";
    public static final String ZOOM_TOKEN_URL = "https://zoom.us/oauth/token";

    private ZoomConstants() {
	// prevent construction
    }
}