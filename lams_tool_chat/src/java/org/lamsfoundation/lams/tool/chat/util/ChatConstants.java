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

package org.lamsfoundation.lams.tool.chat.util;

public interface ChatConstants {
    public static final String TOOL_SIGNATURE = "lachat11";

    // Chat session status
    public static final Integer SESSION_NOT_STARTED = new Integer(0);
    public static final Integer SESSION_IN_PROGRESS = new Integer(1);
    public static final Integer SESSION_COMPLETED = new Integer(2);

    public static final String AUTHORING_DEFAULT_TAB = "1";
    public static final String AUTH_SESSION_ID_COUNTER = "authoringSessionIdCounter";
    public static final String AUTH_SESSION_ID = "authoringSessionId";

    public static final int MONITORING_SUMMARY_MAX_MESSAGES = 5;

    public static final long PRESENCE_IDLE_TIMEOUT = 10 * 1000;

    // Attribute names
    public static final String ATTR_MESSAGE = "message";
    public static final String ATTR_SESSION_MAP = "sessionMap";

    // Parameter names
    public static final String PARAM_PARENT_PAGE = "parentPage";

    static final String FILTER_REPLACE_TEXT = "***";

    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";
    public static final String ATTR_CONDITION_SET = "conditionList";

    public static final String PARAM_ORDER_ID = "orderId";
    public static final String ATTR_DELETED_CONDITION_LIST = "deletedConditionList";
    public static final String USER_MESSAGES_DEFINITION_NAME = "user.messages.output.definition.chat";
    public static final String ALL_USERS_MESSAGES_DEFINITION_NAME = "all.users.messages.output.definition.chat";
    public final static String LEARNER_NUM_POSTS_DEFINITION_NAME = "learner.number.of.posts";
    public static final String TEXT_SEARCH_DEFINITION_NAME = "text.search.output.definition.chat";
    public static final String TEXT_SEARCH_DEFAULT_CONDITION_DISPLAY_NAME_KEY = "text.search.output.definition.chat.default.condition";

    public static final String SUCCESS = "success";
    public static final String ERROR_MSG_CONDITION = "error.condition";
    public static final String ERROR_MSG_NAME_BLANK = "error.condition.name.blank";
    public static final String ERROR_MSG_NAME_DUPLICATED = "error.condition.duplicated.name";
    public static final String MESSAGE_SEPARATOR = " ";

    /* Date time restriction */
    public static final String ATTR_SUBMISSION_DEADLINE = "submissionDeadline";
    public static final String ATTR_IS_SUBMISSION_DEADLINE_PASSED = "isSubmissionDeadlinePassed";

}