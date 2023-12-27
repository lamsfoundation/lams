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


package org.lamsfoundation.lams.tool.forum;

import java.util.regex.Pattern;

/**
 * User: conradb Date: 14/06/2005 Time: 10:33:00
 */
public interface ForumConstants {
    public static final String TOOL_SIGNATURE = "lafrum11";

    public static final int SORT_BY_NO = 0;
    public static final int SORT_BY_USER_NAME_ASC = 1;
    public static final int SORT_BY_USER_NAME_DESC = 2;
    public static final int SORT_BY_LAST_POSTING_ASC = 3;
    public static final int SORT_BY_LAST_POSTING_DESC = 4;
    public static final int SORT_BY_NUMBER_OF_POSTS_ASC = 5;
    public static final int SORT_BY_NUMBER_OF_POSTS_DESC = 6;
    public static final int SORT_BY_MARKED_ASC = 7;
    public static final int SORT_BY_MARKED_DESC = 8;

    // See LDEV652
    // For old style (Fiona's description): The oldest topic is at the top, does not depends the reply date etc.
    // For new style (Ernie's early desc): Most current popular forum used style: latest topics
    // should be at the top, reply date will descide the order etc.
    public static final boolean OLD_FORUM_STYLE = true;

    public static final int STATUS_CONTENT_COPYED = 1;

    public static final int SESSION_STATUS_FINISHED = 1;

    public final static String FORUM_SERVICE = "forumService";

    public final static String CONTENT_HANDLER = "toolContentHandler";

    public static final String AUTHORING_DTO = "authoring";

    public static final String AUTHORING_TOPICS_LIST = "topicList";

    public static final String AUTHORING_TOPICS_INDEX = "topicIndex";

    public static final String AUTHORING_TOPIC_THREAD = "topicThread";

    public static final String AUTHORING_TOPIC = "topic";

    public static final String AUTHORING_FORUM = "forum";

    public static final String DEFAULT_TITLE = "Forum";

    // TODO:hard code!!! need to read from config
    public static final String TOOL_URL_BASE = "/lams/tool/lafrum11/";

    public static final String ATTR_FORUM_ID = "forum_id";

    public static final String ATTR_FORUM_UID = "forumUid";

    public static final String ATTR_ALLOW_EDIT = "allowEdit";

    public static final String ATTR_ALLOW_UPLOAD = "allowUpload";

    public static final String ATTR_ALLOW_NEW_TOPICS = "allowNewTopics";

    public static final String ATTR_ALLOW_RICH_EDITOR = "allowRichEditor";

    public static final String ATTR_ALLOW_RATE_MESSAGES = "allowRateMessages";

    public static final String ATTR_ALLOW_ANONYMOUS = "allowAnonym";

    public static final String ATTR_MINIMUM_RATE = "minimumRate";

    public static final String ATTR_MAXIMUM_RATE = "maximumRate";

    public static final String ATTR_MIN_CHARACTERS = "minCharacters";

    public static final String ATTR_MAX_CHARACTERS = "maxCharacters";

    public static final String ATTR_LOCK_WHEN_FINISHED = "lockedWhenFinished";

    public static final String TOPIC_DELETED_ATTACHMENT_LIST = "topicDeletedAttachmentList";

    public static final String DELETED_AUTHORING_TOPICS_LIST = "deletedAuthoringTopicList";

    public static final String USER_UID = "userUid";

    public static final String ATTR_FINISHED_LOCK = "finishedLock";

    public static final String ATTR_USER_FINISHED = "userFinished";

    public static final String ATTR_UPLOAD_MAX_FILE_SIZE = "uploadMaxFileSize";

    public static final String ATTR_SESSION_DTOS = "sessionDtos";

    // for submission deadline

    public static final String ATTR_SUBMISSION_DEADLINE = "submissionDeadline";
    
    public static final String ATTR_SUBMISSION_DEADLINE_DATESTRING = "submissionDateString";

    public static final String ATTR_IS_SUBMISSION_DEADLINE_PASSED = "isSubmissionDeadlinePassed";

    // used in monitoring
    public static final String TITLE = "title";

    public static final String INSTRUCTIONS = "instructions";

    public static final String PAGE_EDITABLE = "isPageEditable";

    public static final String ATTR_ROOT_TOPIC_UID = "rootUid";

    public static final String ATTR_FORUM_TITLE = "title";

    public static final String ATTR_FORUM_INSTRCUTION = "instruction";

    public static final String ATTR_TOOL_CONTENT_TOPICS = "ToolContentTopicList";

    public static final String ATTR_TOPIC = "topic";

    public static final String ATTR_USER = "user";

    public static final String ATTR_USER_UID = "userUid";

    public static final String ATTR_USER_ID = "userId";

    public static final String ATTR_PORTRAIT_ID = "portraitId";

    public static final String ATTR_MESSAGES = "messages";

    public static final String PARAM_UPDATE_MODE = "updateMode";

    public static final String ATTR_NO_MORE_POSTS = "noMorePosts";

    public static final String ATTR_NUM_OF_POSTS = "numOfPosts";

    public static final String ATTR_NO_MORE_RATINGSS = "noMoreRatings";

    public static final String ATTR_NUM_OF_RATINGS = "numOfRatings";

    public static final String ATTR_IS_MIN_RATINGS_COMPLETED = "isMinRatingsCompleted";

    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";

    public static final String ATTR_PARENT_TOPIC_ID = "parentID";

    // used when replying message LDEV-1305
    public static final String ATTR_ORIGINAL_MESSAGE = "originalMessage";

    public static final String ATTR_TOPIC_ID = "topicID";

    public static final String DELETED_ATTACHMENT_LIST = "deletedAttachmentList";

    public static final String ATTR_MINIMUM_REPLY = "minimumReply";

    public static final String ATTR_MAXIMUM_REPLY = "maximumReply";

    public static final String MARK_UPDATE_FROM_USER = "listMarks";

    public static final String MARK_UPDATE_FROM_FORUM = "viewForum";

    public static final String EVENT_NAME_NOTIFY_LEARNERS_ON_MARK_RELEASE = "notify_learners_on_mark_release";

    // for condition management
    public static final String ATTR_CONDITION_SET = "conditionList";
    public static final String PARAM_ORDER_ID = "orderId";
    public static final String ATTR_DELETED_CONDITION_LIST = "deletedConditionList";

    public static final String TOPIC_DATE_TO_ANSWERS_DEFINITION_NAME = "topic.name.to.answers.output.definition.forum";
    public static final String ALL_USERS_ANSWERS_DEFINITION_NAME = "all.users.answers.definition.forum";
    public final static String LEARNER_NUM_POSTS_DEFINITION_NAME = "learner.number.of.posts";

    public static final String ANSWERS_SEPARATOR = "<br \\><br \\>";
    // a forward key
    public static final String SUCCESS = "success";

    public static final String WORD_REGEX = "\\w+";

    public static final short SUBJECT_WORD_COUNT = 3;

    public static final int PATTERN_MATCHING_OPTIONS = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
	    | Pattern.MULTILINE;

    // for paging long topics & inlining reply
    public static final String PAGE_LAST_ID = "pageLastId";
    public static final String PAGE_SIZE = "size";
    public static final int DEFAULT_PAGE_SIZE = 50;
    public static final String ATTR_MESS_ID = "messageUid";
    public static final String ATTR_THREAD_ID = "threadUid";
    public static final String ATTR_NO_MORE_PAGES = "noMorePages";
}