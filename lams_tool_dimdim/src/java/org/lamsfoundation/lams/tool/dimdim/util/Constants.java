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
/* $Id$ */

package org.lamsfoundation.lams.tool.dimdim.util;

public interface Constants {
	public static final String TOOL_SIGNATURE = "laddim10";

	// Config keys
	public static final String CONFIG_SERVER_URL = "server_url";

	// Authoring SessionMap key names
	public static final String KEY_TOOL_CONTENT_ID = "toolContentID";
	public static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";
	public static final String KEY_MODE = "mode";
	public static final String KEY_ONLINE_FILES = "onlineFiles";
	public static final String KEY_OFFLINE_FILES = "offlineFiles";
	public static final String KEY_UNSAVED_ONLINE_FILES = "unsavedOnlineFiles";
	public static final String KEY_UNSAVED_OFFLINE_FILES = "unsavedOfflineFiles";
	public static final String KEY_DELETED_FILES = "deletedFiles";

	// Attribute names
	public static final String ATTR_CONTENT_DTO = "contentDTO";
	public static final String ATTR_CONTENT_FOLDER_ID = "contentFolderID";
	public static final String ATTR_FINISHED_ACTIVITY = "finishedActivity";
	public static final String ATTR_SESSION_MAP = "sessionMap";
	public static final String ATTR_USER_DTO = "userDTO";
	public static final String ATTR_CONFERENCE_URL = "conferenceURL";
	public static final String ATTR_SAVE_SUCCESS = "saveSuccess";

	// Parameter names
	public static final String PARAM_USER_UID = "userUID";
	public static final String ATTR_CONFERENCE_OPEN = "conferenceOpen";

}
