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

package org.lamsfoundation.lams.tool.commonCartridge;

public class CommonCartridgeConstants {
    public static final String TOOL_SIGNATURE = "laimsc11";

    public static final String RESOURCE_SERVICE = "commonCartridgeService";

    public static final int COMPLETED = 1;

    // commonCartridge type;
    public static final short RESOURCE_TYPE_BASIC_LTI = 1;

    public static final short RESOURCE_TYPE_COMMON_CARTRIDGE = 2;

    // for action forward name
    public static final String SUCCESS = "success";

    public static final String ERROR = "error";

    public static final String DEFINE_LATER = "definelater";

    // for parameters' name
    public static final String PARAM_TOOL_CONTENT_ID = "toolContentID";

    public static final String PARAM_TOOL_SESSION_ID = "toolSessionID";

    public static final String PARAM_FILE_VERSION_ID = "fileVersionId";

    public static final String PARAM_FILE_UUID = "fileUuid";

    public static final String PARAM_ITEM_INDEX = "itemIndex";

    public static final String PARAM_RESOURCE_ITEM_UID = "itemUid";

    public static final String PARAM_CURRENT_INSTRUCTION_INDEX = "insIdx";

    public static final String PARAM_OPEN_URL_POPUP = "popupUrl";

    public static final String PARAM_TITLE = "title";

    // for request attribute name
    public static final String ATTR_TOOL_CONTENT_ID = "toolContentID";

    public static final String ATTR_TOOL_SESSION_ID = "toolSessionID";

    public static final String ATTR_RESOURCE_ITEM_LIST = "commonCartridgeList";

    public static final String ATTR_DELETED_RESOURCE_ITEM_LIST = "deleteCommonCartridgeList";

    public static final String ATTR_RESOURCE_REVIEW_URL = "commonCartridgeItemReviewUrl";

    public static final String ATTR_RESOURCE = "commonCartridge";

    public static final String ATTR_RUN_AUTO = "runAuto";

    public static final String ATTR_RESOURCE_ITEM_UID = "itemUid";

    public static final String ATTR_SUMMARY_LIST = "summaryList";

    public static final String ATTR_USER_LIST = "userList";

    public static final String ATTR_RESOURCE_INSTRUCTION = "instructions";

    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";

    public static final String ATTR_RESOURCE_FORM = "commonCartridgeForm";

    public static final String ATTR_ADD_RESOURCE_TYPE = "addType";

    public static final String ATTR_FILE_TYPE_FLAG = "fileTypeFlag";

    public static final String ATTR_TITLE = "title";

    public static final String ATTR_INSTRUCTIONS = "instructions";

    public static final String ATTR_USER_FINISHED = "userFinished";

    public static final String ATTR_ITEM = "item";
    public static final String ATTR_LAUNCH_URL = "launchUrl";
    public static final String ATTR_SECURE_LAUNCH_URL = "secureLaunchUrl";
    public static final String ATTR_REMOTE_TOOL_KEY = "toolKey";
    public static final String ATTR_REMOTE_TOOL_SECRET = "toolSecret";
    public static final String ATTR_BUTTON_TEXT = "buttonText";
    public static final String ATTR_FRAME_HEIGHT = "frameHeight";
    public static final String ATTR_OPEN_URL_NEW_WINDOW = "openUrlNewWindow";

    // error message keys
    public static final String ERROR_MSG_TITLE_BLANK = "error.resource.item.title.blank";

    public static final String ERROR_MSG_URL_BLANK = "error.resource.item.url.blank";

    public static final String ERROR_MSG_DESC_BLANK = "error.resource.item.desc.blank";

    public static final String ERROR_MSG_FILE_BLANK = "error.resource.item.file.blank";

    public static final String ERROR_MSG_INVALID_URL = "error.resource.item.invalid.url";

    public static final String ERROR_MSG_UPLOAD_FAILED = "error.upload.failed";

    public static final String PAGE_EDITABLE = "isPageEditable";

    public static final String MODE_AUTHOR_SESSION = "author_session";

    public static final String ATTR_REFLECTION_ON = "reflectOn";

    public static final String ATTR_REFLECTION_INSTRUCTION = "reflectInstructions";

    public static final String ATTR_REFLECTION_ENTRY = "reflectEntry";

    public static final String ATTR_REFLECT_LIST = "reflectList";

    public static final String ATTR_USER_UID = "userUid";

    public static final String DEFUALT_PROTOCOL_REFIX = "http://";

    public static final String ALLOW_PROTOCOL_REFIX = new String("[http://|https://|ftp://|nntp://]");
}
