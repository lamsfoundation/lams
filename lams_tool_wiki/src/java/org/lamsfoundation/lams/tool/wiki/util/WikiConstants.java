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


package org.lamsfoundation.lams.tool.wiki.util;

public interface WikiConstants {
    public static final String TOOL_SIGNATURE = "lawiki10";

    // Wiki session status
    public static final Integer SESSION_NOT_STARTED = new Integer(0);
    public static final Integer SESSION_IN_PROGRESS = new Integer(1);
    public static final Integer SESSION_COMPLETED = new Integer(2);

    public static final String AUTHORING_DEFAULT_TAB = "1";
    public static final String AUTH_SESSION_ID_COUNTER = "authoringSessionIdCounter";
    public static final String AUTH_SESSION_ID = "authoringSessionId";

    public static final int MONITORING_SUMMARY_MAX_MESSAGES = 5;

    // Attribute names
    public static final String ATTR_WIKI_DTO = "wikiDTO";
    public static final String ATTR_USER_DTO = "userDTO";
    public static final String ATTR_SESSION_DTO = "sessionDTO";
    public static final String ATTR_CONTENT_FOLDER_ID = "contentFolderID";
    public static final String ATTR_MODE = "mode";
    public static final String ATTR_MESSAGE = "message";
    public static final String ATTR_SESSION_MAP = "sessionMap";
    public static final String ATTR_MAIN_WIKI_PAGE = "mainWikiPage";
    public static final String ATTR_WIKI_PAGES = "wikiPages";
    public static final String ATTR_WIKI_PAGE_CONTENT_HISTORY = "wikiPageContentHistory";
    public static final String ATTR_CURRENT_WIKI = "currentWikiPage";
    public static final String ATTR_NEW_PAGE_NAME = "newPageName";
    public static final String ATTR_HISTORY_PAGE_CONTENT_ID = "historyPageContentId";
    public static final String ATTR_COMPARE_STRING = "compareString";
    public static final String ATTR_COMPARE_TITLE = "compareTitle";
    public static final String ATTR_COMPARE_VERSIONS = "compareVersions";
    public static final String ATTR_MAX_EDITS_REACHED = "maxEditsReached";
    public static final String ATTR_EDITS_LEFT = "editsLeft";
    public static final String ATTR_MIN_EDITS_REACHED = "minEditsReached";
    public static final String ATTR_FINISHED_ACTIVITY = "finishedActivity";
    public static final String ATTR_CONTENT_EDITAVLE = "contentEditable";
    public static final String ATTR_MAIN_PAGE_TITLE = "mainPageTitle";
    public static final String ATTR_IS_GROUPED_ACTIVITY = "isGroupedActivity";

    // Events
    public static final String EVENT_NOTIFY_TEACHERS = "wikiNotifyTeachers";
    public static final String EVENT_NOTIFY_LEARNERS = "wikiNotifyLearners";

    // Parameter names
    public static final String PARAM_PARENT_PAGE = "parentPage";

    static final String FILTER_REPLACE_TEXT = "***";

    public static final String JAVASCRIPT_TOKEN = "javascript";
    public static final String JAVASCRIPT_REPLACE_TOKEN = "JAVASCRIPTREPLACE";
    
    public static final String MESSAGE_SEPARATOR = " ";
    public static final String TEXT_SEARCH_DEFINITION_NAME = "text.search.output.definition.chat";
    public static final String TEXT_SEARCH_DEFAULT_CONDITION_DISPLAY_NAME_KEY = "text.search.output.definition.chat.default.condition";
    
    /* Date time restriction */
    public static final String ATTR_SUBMISSION_DEADLINE = "submissionDeadline";
    public static final String ATTR_SUBMISSION_DEADLINE_DATESTRING = "submissionDateString";
    public static final String ATTR_IS_SUBMISSION_DEADLINE_PASSED = "isSubmissionDeadlinePassed";

}
