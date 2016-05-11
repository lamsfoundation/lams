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


package org.lamsfoundation.lams.learningdesign.service;

/**
 * WDDX tags used in LAMS 1.0.2
 */
public class WDDXTAGS102 {

    public static final String OBJECT_TYPE = "objectType";
    public static final String DESCRIPTION = "description";
    public static final String TITLE = "title";
    public static final String LD_ITEM_ID = "id";
    public static final String SID = "sid";
    public static final String DELETE = "delete"; // delete this object (and sub objects)

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss Z";

    // The transition following this activity or task. Applicable only to task and
    // activity.
    public static final String FOLLOWING_TRANSITION = "nextTransition";

    // learning design specific tags
    public static final String LD_MAXID = "maxId";
    public static final String LD_ID = "LDId";
    public static final String LD_ACTTRAN = "activitiesTransitions";
    public static final String LD_CONTENT = "content";
    public static final String LD_FIRSTACT = "firstActivity";

    public static final String LD_VERSION = "lamsVersion";

    public static final String LD_READONLY = "readOnly"; // read only as it has been run.
    public static final String LD_READACCESS_STATUS = "readAccessStatus"; // read only access based on privilege. boolean value
    public static final String LD_READACCESS_DESC = "readAccessDesc"; // "private", "global" or organisation name
    public static final String LD_READACCESS_ID = "readAccess"; // public, private or organisation id
    public static final String LD_WRITEACCESS_STATUS = "writeAccessStatus"; // write acess read based on privilege. boolean value
    public static final String LD_WRITEACCESS_DESC = "writeAccessDesc"; // "private", "global" or organisation name
    public static final String LD_WRITEACCESS_ID = "writeAccess"; // public, private or organisation id
    public static final String LD_HELPTEXT = "helpText";

    public static final String PRIVATE_VALUE = "Private";
    public static final String GLOBAL_VALUE = "Globally available";

    // activity specific tags
    public static final String ACT_LIBRARY_ID = "libId";
    public static final String ACT_TASKTRAN = "tasksTransitions";
    public static final String ACT_FIRSTTASK = "firstTask";
    public static final String ACT_X = "x";
    public static final String ACT_Y = "y";

    // task specific tags
    public static final String TASK_COMPLETION = "completion";
    public static final String TASK_TOOLTYPE = "toolType";
    public static final String TASK_INPUT_CONTENT = "inputContent";
    public static final String TASK_OUTPUT_CONTENT = "outputContent";
    public static final String TASK_GROUPING = "grouping";
    public static final String TASK_GROUPING_CLASS_VALUE = "c";

    // multi task specific tags
    public static final String MTASK_SUBTASKS = "subTasks";
    public static final String MTASK_INPUT_CONTENT_TASK = "inputContentTask";
    public static final String MTASK_OUTPUT_CONTENT_TASK = "outputContentTask";
    public static final String MTASK_SUBTASK_ORDER = "taskOrder";

    // transition specific tags
    public static final String TRANSITION_COMPLETIONTYPE = "completionType";
    public static final String TRANSITION_FROM_TASKS = "fromTasks";
    public static final String TRANSITION_TO_TASKS = "toTasks";

    /**
     * Transition is based on multiple learners completing a task (synchronize)
     * or a teacher controlling the transition (synchronize_teacher)
     * - this should be used for TransitionVODefn and subclasses
     */
    public static final String TRANSITION_COMPLETION_SYNCRONIZE = "synchronize";
    public static final String TRANSITION_COMPLETION_SYNCRONIZE_TEACHER = "synchronize_teacher";
    public static final String TRANSITION_COMPLETION_SYNCRONIZE_BOTH = "synchronize_both";

    // library specific tags
    public static final String LIB_PACKAGES = "packages";
    public static final String LIB_ACTIVITIES = "activities";

    // ld list specific tags
    public static final String LD_LIST = "list";

    // content specific tags
    public static final String CONTENT_BODY = "body";
    public static final String CONTENT_SHOW_USER = "contentShowUser"; // boolean

    // contentType is based on the content class used - doesn't persist
    public static final String CONTENT_TYPE = "contentType";
    public static final String CONTENT_NUMGROUPS = "number_groups";
    public static final String CONTENT_MINNUM_GROUP = "min_number_in_group";
    public static final String CONTENT_MAXNUM_GROUP = "max_number_in_group";

    // Ranking tool tags
    public static final String CONTENT_VOTE_MAXCHOOSE = "maxChoose";
    public static final String CONTENT_VOTE_METHOD = "voteMethod"; // equal vote or preferential
    public static final String CONTENT_VOTE_NOMINATIONS = "nominations";
    public static final String CONTENT_VOTE_ALLOW_POLL_NOMINATIONS = "nominatePollTime"; // allow nomination or not (Boolean)
    public static final String CONTENT_VOTE_PROGRESSIVE_DISPLAY = "progressive_display";

    // url content
    public static final String CONTENT_URL_MIN_NUMBER_COMPLETE = "minNumberComplete";
    public static final String CONTENT_URL_RUNTIME_STAFF_SUBMIT_URL = "runtimeSubmissionStaffURL";
    public static final String CONTENT_URL_RUNTIME_LEARNER_SUBMIT_URL = "runtimeSubmissionLearnerURL";
    public static final String CONTENT_URL_RUNTIME_STAFF_SUBMIT_FILE = "runtimeSubmissionStaffFile";
    public static final String CONTENT_URL_RUNTIME_LEARNER_SUBMIT_FILE = "runtimeSubmissionLearnerFile";
    public static final String CONTENT_URL_URLS = "urls";

    public static final String TOOL_TYPE_URL_CONTENT = "urlcontent";
    public static final String CONTENT_URL_URL_SHOWBUTTONS = "showbuttons";
    public static final String CONTENT_URL_URL_VIEW_ORDER = "order";
    public static final String CONTENT_URL_URL_TITLE = "title";
    public static final String CONTENT_URL_URL_URL = "url";
    public static final String CONTENT_URL_URL_DOWNLOAD = "download"; // boolean: Author prefers that the content is downloaded. Only applicable to file content.
    public static final String CONTENT_URL_URL_TYPE = "resourcetype"; // see URLContent TYPE_* fields
    public static final String CONTENT_URL_URL_INSTRUCTION_ARRAY = "instructions";
    public static final String CONTENT_URL_INSTRUCTION = "instruction";

    // message content
    public static final String CONTENT_MB_TERMINATION_TYPE = "terminationType"; // type string
    public static final String CONTENT_MB_DURATION_DAYS = "durationInDays"; // type string
    public static final String CONTENT_MB_POSTING_NOTIFIED = "isPostingNotified"; // type boolean
    public static final String CONTENT_MB_POSTING_MODERATED = "isPostingModerated"; // type boolean
    public static final String CONTENT_MB_NEW_TOPIC_ALLOWED = "isNewTopicAllowed"; //type boolean
    public static final String CONTENT_MB_REUSABLE = "isReusable"; //type boolean
    public static final String CONTENT_MB_TOPICS = "topics"; // array

    public static final String CONTENT_MB_TOPIC_TITLE = "title"; // string
    public static final String CONTENT_MB_TOPIC_SUBJECT = "subject"; // string
    public static final String CONTENT_MB_TOPIC_MESSAGE = "message"; // string 
    public static final String CONTENT_MB_TOPIC_NUMBER = "number"; // number

    // Simple Questions content
    public static final String CONTENT_Q_SHOW_FEEDBACK = "showfeedback"; // boolean
    public static final String CONTENT_Q_ALLOW_REDO = "allowredo"; // integer
    public static final String CONTENT_Q_MIN_PASSMARK = "minpassmark"; // integer
    public static final String CONTENT_Q_SHOW_TOP_USERNAMES = "showtopusernames"; // boolean
    public static final String CONTENT_Q_ORDER = "order"; // integer
    public static final String CONTENT_Q_QUESTION_INFO = "questionanswers"; // string
    public static final String CONTENT_Q_QUESTION = "question"; // string
    public static final String CONTENT_Q_FEEDBACK = "feedback"; // string
    public static final String CONTENT_Q_CANDIDATES = "candidates"; // array of string 
    public static final String CONTENT_Q_ANSWER = "answer"; // string 

    // optional activity fields
    public static final String OPTACT_MIN_NUMBER_COMPLETE = "minNumberComplete";
    public static final String OPTACT_ACTIVITIES = "activities";
    public static final String OPTACT_X_START = "x";
    public static final String OPTACT_Y_START = "y";
    public static final String OPTACT_X_END = "xEnd";
    public static final String OPTACT_Y_END = "yEnd";

    // for file upload - SingleResource, HTMLNoticeboard, Image tools
    public static final String DIRECTORY_NAME = "directoryName";

    // ImageGallery task tags (extended from Content.java)
    public static final String CONTENT_IMGG_DIRECTORY = DIRECTORY_NAME;
    public static final String CONTENT_IMGG_TITLE = TITLE;
    public static final String CONTENT_IMGG_BODY = CONTENT_BODY;
    public static final String CONTENT_IMGG_GROUPING = TASK_GROUPING;
    public static final String CONTENT_IMGG_SHOW_USER = CONTENT_SHOW_USER;
    public static final String CONTENT_IMGG_MAX_IMAGES = "maxImages";
    public static final String CONTENT_IMGG_ALLOW_SEARCH = "allowSearch"; // Boolean
    public static final String CONTENT_IMGG_SEARCH_URL = "searchURL";
    public static final String CONTENT_IMGG_ALLOW_URL = "allowURL"; // Boolean
    public static final String CONTENT_IMGG_ALLOW_UPLOAD = "allowUpload";
    public static final String CONTENT_IMGG_IMAGES = CONTENT_URL_URLS;
    // ImageGallery images array (also used by ImageRanking)
    public static final String CONTENT_IMGG_IMAGE_SID = SID;
    public static final String CONTENT_IMGG_IMAGE_TITLE = TITLE;
    public static final String CONTENT_IMGG_IMAGE_COMMENTS = "comments";
    public static final String CONTENT_IMGG_IMAGE_VIEW_ORDER = "order";
    public static final String CONTENT_IMGG_IMAGE_DATECREATED = "dateCreated";
    public static final String CONTENT_IMGG_IMAGE_DATEUPDATED = "dateUpdated";
    public static final String CONTENT_IMGG_IMAGE_TYPE = "type"; // "resourceType", values are "externalurl" or "file"
    public static final String CONTENT_IMGG_IMAGE_URL = "url";
    public static final String CONTENT_IMGG_IMAGE_FILENAME = "filename";
    public static final String CONTENT_IMGG_IMAGE_PATH = "path";
    public static final String CONTENT_IMGG_IMAGE_OWNERID = "ownerId";
    public static final String CONTENT_IMGG_IMAGE_OWNERNAME = "owerName";
    // Additional field to use the image array for ImageRanking (runtime only)
    public static final String CONTENT_IMGR_IMAGE_IS_SELECTED = "isSelected";

    // ImageRanking task tags (extended from Content.java)
    public static final String CONTENT_IMGR_DIRECTORY = DIRECTORY_NAME;
    public static final String CONTENT_IMGR_TITLE = TITLE;
    public static final String CONTENT_IMGR_BODY = CONTENT_BODY; // this is the description
    public static final String CONTENT_IMGR_GROUPING = TASK_GROUPING;
    public static final String CONTENT_IMGR_SHOW_USER = CONTENT_SHOW_USER;
    public static final String CONTENT_IMGR_MAX_VOTE = CONTENT_VOTE_MAXCHOOSE;
    public static final String CONTENT_IMGR_PROGRESSIVE_DISPLAY = CONTENT_VOTE_PROGRESSIVE_DISPLAY; // Boolean
    public static final String CONTENT_IMGR_IMAGES = CONTENT_URL_URLS;
    // ImageRanking images array - same as the ImageGallery images array

}
