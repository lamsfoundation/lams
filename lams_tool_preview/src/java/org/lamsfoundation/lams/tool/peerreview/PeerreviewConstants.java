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

package org.lamsfoundation.lams.tool.peerreview;

public class PeerreviewConstants {
    public static final String TOOL_SIGNATURE = "laprev11";

    public static final String PEERREVIEW_SERVICE = "peerreviewService";

    public static final int COMPLETED = 1;

    public static final int SORT_BY_NO = 0;
    public static final int SORT_BY_USERNAME_ASC = 1;
    public static final int SORT_BY_USERNAME_DESC = 2;
    public static final int SORT_BY_AVERAGE_RESULT_ASC = 3;
    public static final int SORT_BY_AVERAGE_RESULT_DESC = 4;
    
    public static final int GET_RESULTS_FOR_ALL_USERS_BY_THIS_USER = 0; // used for the edit screens
    public static final int GET_RESULTS_FOR_ALL_USERS_BY_ALL_USERS = 1; // used for "everyone's results" on the results screen
    public static final int GET_RESULTS_BY_ALL_USERS_FOR_THIS_USER = 2; //used for "current user's results" on the results screen
    
    // for action forward name
    public static final String SUCCESS = "success";

    public static final String ERROR = "error";

    public static final String DEFINE_LATER = "definelater";

    public static final String PARAM_PAGE = "page";
    public static final String PARAM_ROWS = "rows";
    public static final String PARAM_SIDX = "sidx";
    public static final String PARAM_SORD = "sord";
    public static final String PARAM_METHOD = "method";
    public static final String PARAM_DISPATCH = "dispatch";
    public static final String PARAM_LOGIN = "login";
    public static final String PARAM_ID = "id";
    public static final String PARAM_MARK = "mark";
    public static final String PARAM_FEEDBACK = "feedback";
    public static final String PARAM_USERID = "userID";
    public static final String PARAM_SEARCH = "_search";
    public static final String PARAM_SEARCH_FIELD = "searchField";
    public static final String PARAM_SEARCH_OPERATION = "searchOper";
    public static final String PARAM_SEARCH_STRING = "searchString";
    public static final String PARAM_SORT_USER_NAME = "userName";
    public static final String PARAM_SORT_NAME = "itemDescription";
    public static final String PARAM_SORT_RATING = "rating";
    public static final String PARAM_TIME_TAKEN = "timeTaken";
    public static final String PARAM_AVG_TIME_TAKEN = "avgTimeTaken";
    public static final String PARAM_AVG_MARK = "avgMark";
    public static final String PARAM_VIEW = "view";
    public static final String PARAM_START_DATE = "startDate";
    public static final String PARAM_GROUP_ID = "groupId";
    // Sort
    public static final String SORT_DESC = "desc";
    public static final String SORT_ASC = "asc";

    // for parameters' name
    public static final String PARAM_TOOL_CONTENT_ID = "toolContentID";

    public static final String PARAM_TOOL_SESSION_ID = "toolSessionId";

    public static final String PARAM_TITLE = "title";

    // for request attribute name
    public static final String ATTR_TOOL_CONTENT_ID = "toolContentID";

    public static final String ATTR_TOOL_SESSION_ID = "toolSessionID";

    public static final String ATTR_PEERREVIEW = "peerreview";

    public static final String ATTR_CRITERIAS = "criterias";

    public static final String ATTR_SUMMARY_LIST = "summaryList";

    public static final String ATTR_USER_LIST = "userList";

    public static final String ATTR_RESOURCE_INSTRUCTION = "instructions";

    public static final String ATTR_FINISH_LOCK = "finishedLock";

    public static final String ATTR_LOCK_ON_FINISH = "lockOnFinish";

    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";

    public static final String ATTR_PEERREVIEW_FORM = "peerreviewForm";

    public static final String ATTR_TITLE = "title";

    public static final String ATTR_INSTRUCTIONS = "instructions";

    public static final String ATTR_USER_FINISHED = "userFinished";

    public static final String ATTR_USER = "user";

    public static final String ATTR_IS_GROUPED_ACTIVITY = "isGroupedActivity";

    public static final String ATTR_IS_URL_ITEM_TYPE = "isUrlItemType";

    public static final String ATTR_CREATING_USERS = "creatingUsers";

    // error message keys

    public static final String ERROR_MSG_UPLOAD_FAILED = "error.upload.failed";

    public static final String PAGE_EDITABLE = "isPageEditable";

    public static final String MODE_AUTHOR_SESSION = "author_session";

    public static final String ATTR_USER_UID = "userUid";

    public static final String OUTPUT_NAME_LEARNER_RATING_AVERAGE = "learner.rating.average";
}