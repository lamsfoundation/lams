/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
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

package org.lamsfoundation.lams.web.util;

/**
 * <p>
 * <a href="AttributeNames.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class AttributeNames {

    public static final String ADMIN_ORGANISATION = "organisation";
    public static final String ADMIN_PARENT_ACCESS = "parentAccess";
    public static final String ADMIN_USER = "user";
    public static final String ADMIN_ERR_MSG = "errormsg";

    public static final String USER = "user";
    public static final String UID = "uniqueID";

    public static final String PARAM_MODE = "mode";
    public static final String PARAM_ROLE = "role";
    public static final String PARAM_SESSION_STATUS = "sessionStatus";
    public static final String PARAM_USER_ID = "userID";
    public static final String PARAM_TOOL_ID = "toolID";
    public static final String PARAM_TOOL_CONTENT_ID = "toolContentID";
    public static final String PARAM_TOOL_SESSION_ID = "toolSessionID";
    public static final String PARAM_LEARNING_LIBRARY_ID = "learningLibraryID";
    public static final String PARAM_CURRENT_ACTIVITY_ID = "currentActivityID";
    public static final String PARAM_DEST_ACTIVITY_ID = "destActivityID";
    public static final String PARAM_ACTIVITY_ID = "activityID";
    public static final String PARAM_CONTENT_FOLDER_ID = "contentFolderID";
    public static final String PARAM_TITLE = "title";
    public static final String PARAM_PRINT_VIEW = "printView";

    public static final String PARAM_COURSE_ID = "courseID";
    public static final String PARAM_CLASS_ID = "classID";
    public static final String PARAM_GROUP_ID = "groupID";

    public static final String PARAM_LEARNINGDESIGN_ID = "learningDesignID";
    public static final String PARAM_ORGANISATION_ID = "organisationID";
    public static final String PARAM_ORGANISATION_NAME = "orgName";

    public static final String PARAM_LEARNER_PROGRESS_ID = "progressID";
    public static final String PARAM_LESSON_ID = "lessonID";
    public static final String PARAM_DIRECTORY_NAME = "directoryName";
    public static final String PARAM_FILENAME = "filename";
    public static final String PARAM_PRESENCE_ENABLED = "presenceEnabledPatch";
    // PARAM_PRESENCE_ENABLED = "presenceEnabledPatch" : strange name used to avoid Java overwriting values in its a
    // hashmap used in learner action in HomeAction.java
    public static final String PARAM_PRESENCE_IM_ENABLED = "presenceImEnabled";
    public static final String PARAM_CURRENT_TAB = "currentTab";
    public static final String PARAM_CUSTOM_CSV = "customCSV";
    public static final String PARAM_EXT_LMS_ID = "extlmsid";
    public static final String PARAM_CREATE_DATE_TIME = "createDateTime";
    public static final String PARAM_NOTIFY_CLOSE_URL = "notifyCloseURL";

    // jqGrid parameters
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_ROWS = "rows";
    public static final String PARAM_SIDX = "sidx";
    public static final String PARAM_SORD = "sord";

    public static final String PARAM_LIB = "library";

    public static final String ATTR_MODE = "mode";
    public static final String ATTR_USERNAME = "username";
    public static final String ATTR_UPDATE_PROGRESS_BAR = "updateProgressBar";
    public static final String ATTR_SESSION_STATUS = "sessionStatus";
    public static final String ATTR_ACTIVITY_POSITION = "activityPosition";
    public static final String ATTR_IS_LAST_ACTIVITY = "isLastActivity";
    public static final String ATTR_LEARNER_CONTENT_FOLDER = "learnerContentFolder";

    public static final String ATTR_RATING_CRITERIAS = "ratingCriterias";
    public static final String ATTR_RATINGS = "ratings";
    public static final String ATTR_ITEM_RATING_DTO = "itemRatingDto";
    public static final String ATTR_ITEM_RATING_DTOS = "itemRatingDtos";
    public static final String ATTR_COUNT_RATED_ITEMS = "countRatedItems";

    public static final String PARAM_TOOL_CONTENT_HANDLER_NAME = "toolContentHandler";

    // AI stuff
    public static final String ATTR_IS_AI_ENABLED = "isAiEnabled";
}