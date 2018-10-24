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


package org.lamsfoundation.lams.tool.notebook.util;

public interface NotebookConstants {
    public static final String TOOL_SIGNATURE = "lantbk11";

    // Notebook session status
    public static final Integer SESSION_NOT_STARTED = new Integer(0);
    public static final Integer SESSION_IN_PROGRESS = new Integer(1);
    public static final Integer SESSION_COMPLETED = new Integer(2);

    public static final String AUTHORING_DEFAULT_TAB = "1";
    public static final String AUTH_SESSION_ID_COUNTER = "authoringSessionIdCounter";
    public static final String AUTH_SESSION_ID = "authoringSessionId";

    public static final int MONITORING_SUMMARY_MAX_MESSAGES = 5;

    // Attribute names
    public static final String ATTR_MESSAGE = "message";
    public static final String ATTR_SESSION_MAP = "sessionMap";

    // Parameter names
    public static final String PARAM_PARENT_PAGE = "parentPage";

    static final String FILTER_REPLACE_TEXT = "***";

    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";
    public static final String ATTR_CONDITION_SET = "conditionList";

    public static final String PARAM_ORDER_ID = "orderId";
    public static final String PARAM_USER_UID = "userUid";
    public static final String ATTR_USER_ID = "userId";
    public static final String ATTR_PORTRAIT_ID = "portraitId";
    public static final String ATTR_DELETED_CONDITION_LIST = "deletedConditionList";
    public static final String USER_ENTRY_DEFINITION_NAME = "user.entry.output.definition.notebook";
    public static final String ALL_USERS_ENTRIES_DEFINITION_NAME = "all.users.entries.definition.notebook";
    public static final String SUCCESS = "success";
    public static final String ERROR_MSG_CONDITION = "error.condition";
    public static final String ERROR_MSG_NAME_BLANK = "error.condition.name.blank";
    public static final String ERROR_MSG_NAME_DUPLICATED = "error.condition.duplicated.name";
    public static final String PARAM_ENTRY = "entry";
    public static final String PARAM_NAME = "userName";
    public static final String PARAM_MODIFIED_DATE = "lastEdited";
    public static final String PARAM_MODIFIED_DATE_TIMEAGO = "lastEditedTimeago";
    public static final String PARAM_COMMENT = "comment";
    public static final String PARAM_COMMENT_SORT = "commentsort"; // used to trigger sorting on comments

    //  for submission deadline
    public static final String ATTR_SUBMISSION_DEADLINE = "submissionDeadline";
    public static final String ATTR_SUBMISSION_DEADLINE_DATESTRING = "submissionDateString";
    public static final String ATTR_IS_SUBMISSION_DEADLINE_PASSED = "isSubmissionDeadlinePassed";

    // monitor sorting
    public static final int SORT_BY_NO = 1;
    public static final int SORT_BY_USERNAME_ASC = 2;
    public static final int SORT_BY_USERNAME_DESC = 3;
    public static final int SORT_BY_DATE_ASC = 4;
    public static final int SORT_BY_DATE_DESC = 5;
    public static final int SORT_BY_COMMENT_ASC = 6;
    public static final int SORT_BY_COMMENT_DESC = 7;
    public static final String ASC = "asc";
    public static final String DESC = "desc";
}