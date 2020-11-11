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



package org.lamsfoundation.lams.tool.sbmt;

public interface SbmtConstants {

    public static final String TOOL_SIGNATURE = "lasbmt11";

    public static final String DEFAULT_TITLE = "Submit Files Title";

    public static final String ATTR_CONTENT = "content";

    public static final String TOOL_CONTENT_HANDLER_NAME = "sbmtToolContentHandler";

    public static final String USER_ID = "userID";

    public static final String CONTENT_IN_USE = "isContentInUse";

    public static final String ATTR_FILE_TYPE_FLAG = "fileTypeFlag";

    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";

    public static final String ATTR_USER_UID = "userUid";

    public static final String ATTR_USER_FULLNAME = "fullName";

    public static final String ATTR_USER_REFLECTION = "reflection";

    public static final String ATTR_USER_NUM_FILE = "numFiles";

    public static final String ATTR_USER_FILE_MARKED = "marked";

    public static final String ATTR_PORTRAIT_ID = "portraitId";

    public static final String SUCCESS = "success";

    public static final String ATTR_FINISH_LOCK = "finishLock";

    public static final String ATTR_REFLECTION_ON = "reflectOn";

    public static final String ATTR_REFLECTION_INSTRUCTION = "reflectInstructions";

    public static final String ATTR_TITLE = "title";

    public static final String ATTR_INSTRUCTION = "instruction";

    public static final String ATTR_LOCK_ON_FINISH = "lockOnFinish";

    public static final String PARAM_MIN_UPLOAD = "minUpload";
    
    public static final String ATTR_GROUP_USERS = "groupUsers";
    
    public static final String ATTR_USE_SEL_LEADER = "useSelectLeaderToolOuput";
    
    public static final String ATTR_GROUP_LEADER = "groupLeader";

    public static final String ATTR_IS_MAX_LIMIT_UPLOAD_ENABLED = "isMaxLimitUploadEnabled";

    public static final String ATTR_MAX_LIMIT_UPLOAD_NUMBER = "maxLimitUploadNumber";
    
    public static final String ATTR_MIN_LIMIT_UPLOAD_NUMBER = "minLimitUploadNumber";

    public static final String ATTR_MAX_LIMIT_REACHED = "maxLimitReached";

    public static final String ATTR_USER_FINISHED = "userFinished";

    public static final String ATTR_IS_GROUPED_ACTIVITY = "isGroupedActivity";    
    
    public static final String ATTR_IS_MARKS_RELEASED = "isMarksReleased";
    
    public static final String ATTR_SUBMIT_FILES = "submittedFilesMap";
    
    public static final String ATTR_IS_USER_LEADER = "isUserLeader";
    
    public static final String ATTR_HAS_EDIT_RIGHT = "hasEditRight";
    
    public static final String PARAM_WAITING_MESSAGE_KEY = "waitingMessageKey";

    public static final String ATTR_UPLOAD_MAX_FILE_SIZE = "uploadMaxFileSize";

    public static final String EVENT_NAME_NOTIFY_LEARNERS_ON_MARK_RELEASE = "notify_learners_on_mark_release";

    public static final String EVENT_NAME_NOTIFY_TEACHERS_ON_FILE_SUBMIT = "notify_teachers_on_file_submit";

    public static final String ATTR_SUBMISSION_DEADLINE = "submissionDeadline";
    public static final String ATTR_SUBMISSION_DEADLINE_DATESTRING = "submissionDateString";
    public static final String ATTR_IS_SUBMISSION_DEADLINE_PASSED = "isSubmissionDeadlinePassed";

    public static final String EVENT_NAME_NOTIFY_LEARNERS_ON_MARKED_FILE = "notify_learners_on_marked_file";

    public static final String SUBMITTED_ITEMS_DEFINITION_NAME = "submitted.items.output.definition.sbmt";

    public static final String AUDIT_LOG_MODULE_NAME = "Submit Files";
    
    public static final int SORT_BY_NO = 1;
    public static final int SORT_BY_USERNAME_ASC = 2;
    public static final int SORT_BY_USERNAME_DESC = 3;
    public static final int SORT_BY_MARKED_ASC = 4;
    public static final int SORT_BY_MARKED_DESC = 5;
    public static final int SORT_BY_NUM_FILES_ASC = 6;
    public static final int SORT_BY_NUM_FILES_DESC = 7;

}
