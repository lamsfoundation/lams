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

package org.lamsfoundation.lams.tool.spreadsheet;

public class SpreadsheetConstants {
    public static final String TOOL_SIGNATURE = "lasprd10";
    public static final String RESOURCE_SERVICE = "spreadsheetService";

    public static final int COMPLETED = 1;

    //for action forward name
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public static final String DEFINE_LATER = "definelater";

    //for parameters' name
    public static final String PARAM_TOOL_CONTENT_ID = "toolContentID";
    public static final String PARAM_TOOL_SESSION_ID = "toolSessionID";
    public static final String PARAM_FILE_VERSION_ID = "fileVersionId";
    public static final String PARAM_FILE_UUID = "fileUuid";
    public static final String PARAM_ITEM_INDEX = "itemIndex";
    public static final String PARAM_RESOURCE_ITEM_UID = "itemUid";
    public static final String PARAM_OPEN_URL_POPUP = "popupUrl";
    public static final String PARAM_TITLE = "title";

    //for request attribute name
    public static final String ATTR_TOOL_CONTENT_ID = "toolContentID";
    public static final String ATTR_TOOL_SESSION_ID = "toolSessionID";
    public static final String ATTR_INSTRUCTION_LIST = "instructionList";
    public static final String ATTR_RESOURCE_ITEM_LIST = "spreadsheetList";
    public static final String ATTR_DELETED_RESOURCE_ITEM_LIST = "deleteSpreadsheetList";
    public static final String ATTR_RESOURCE = "spreadsheet";
    public static final String ATTR_RESOURCE_ITEM_UID = "itemUid";
    public static final String ATTR_SUMMARY_LIST = "summaryList";
    public static final String ATTR_STATISTIC_LIST = "statisticList";
    public static final String ATTR_USER_LIST = "userList";
    public static final String ATTR_RESOURCE_INSTRUCTION = "instructions";
    public static final String ATTR_FINISH_LOCK = "finishedLock";
    public static final String ATTR_LOCK_ON_FINISH = "lockOnFinish";
    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";
    public static final String ATTR_RESOURCE_FORM = "spreadsheetForm";
    public static final String ATTR_ADD_RESOURCE_TYPE = "addType";
    public static final String ATTR_FILE_TYPE_FLAG = "fileTypeFlag";
    public static final String ATTR_TITLE = "title";
    public static final String ATTR_INSTRUCTIONS = "instructions";
    public static final String ATTR_USER_FINISHED = "userFinished";
    public static final String ATTR_IS_GROUPED_ACTIVITY = "isGroupedActivity";

    //error message keys
    public static final String ERROR_MSG_MARKS_BLANK = "error.summary.marks.blank";
    public static final String ERROR_MSG_MARKS_INVALID_NUMBER = "error.summary.marks.invalid.number";
    public static final String ERROR_MSG_COMMENTS_BLANK = "error.summary.comments.blank";
    public static final String ERROR_MSG_INVALID_URL = "error.resource.item.invalid.url";
    public static final String ERROR_MSG_UPLOAD_FAILED = "error.upload.failed";

    public static final String PAGE_EDITABLE = "isPageEditable";
    public static final String MODE_AUTHOR_SESSION = "author_session";

    public static final String ATTR_USER_UID = "userUid";
    public static final String ATTR_USER_ID = "userId";
    public static final String ATTR_USER_NAME = "userName";
    public static final String ATTR_USER_MARK = "mark";
    public static final String ATTR_USER_IS_MARKED = "userIsMarked";
    public static final String ATTR_CODE = "code";

    public static final String DEFUALT_PROTOCOL_REFIX = "http://";
    public static final String ALLOW_PROTOCOL_REFIX = new String("[http://|https://|ftp://|nntp://]");

    public static final int SORT_BY_NO = 1;
    public static final int SORT_BY_USERNAME_ASC = 2;
    public static final int SORT_BY_USERNAME_DESC = 3;
    public static final int SORT_BY_MARKED_ASC = 4;
    public static final int SORT_BY_MARKED_DESC = 5;

    public static final int MARK_NUM_DEC_PLACES = 2;
}
