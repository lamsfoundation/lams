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

package org.lamsfoundation.lams.tool.scratchie;

public class ScratchieConstants {
    public static final String TOOL_SIGNATURE = "lascrt11";

    public static final String SCRATCHIE_SERVICE = "scratchieService";

    public static final int COMPLETED = 1;

    public static final int INITIAL_MCQ_OPTIONS_NUMBER = 4;

    // for action forward name
    public static final String SUCCESS = "success";

    public static final String NOTEBOOK = "notebook";

    public static final String ERROR = "error";

    public static final String DEFINE_LATER = "definelater";

    public static final String WAIT_FOR_LEADER_TIME_LIMIT = "waitForLeaderTimeLimit";

    // for parameters' name
    public static final String PARAM_TOOL_CONTENT_ID = "toolContentID";

    public static final String PARAM_TOOL_SESSION_ID = "toolSessionID";

    public static final String PARAM_SESSION_ID = "sessionId";

    public static final String PARAM_ITEM_INDEX = "itemIndex";

    public static final String PARAM_ITEM_UID = "itemUid";

    public static final String PARAM_OPTION_UID = "optionUid";

    public static final String PARAM_OPTION_INDEX = "optionIndex";

    public static final String PARAM_NOT_A_NUMBER = "nan";

    public static final String PARAM_MARK = "mark";

    public static final String PARAM_BURNING_QUESTION_UID = "burningQuestionUid";

    public static final String PARAM_SEQUENCE_ID = "sequenceId";

    // for request attribute name
    public static final String ATTR_TOOL_CONTENT_ID = "toolContentID";

    public static final String ATTR_TOOL_SESSION_ID = "toolSessionID";

    public static final String ATTR_ITEM_LIST = "itemList";

    public static final String ATTR_BURNING_QUESTION_PREFIX = "burningQuestion";

    public static final String ATTR_BURNING_QUESTIONS = "burningQuestions";

    public static final String ATTR_GENERAL_BURNING_QUESTION = "generalBurningQuestion";

    public static final String ATTR_ITEM_ORDER_ID_PREFIX = "itemOrderId";

    public static final String ATTR_ITEM_TITLE_PREFIX = "itemTitle";

    public static final String ATTR_ITEM_DESCRIPTION_PREFIX = "itemDescription";

    public static final String ATTR_ITEM_COUNT = "itemCount";

    public static final String ATTR_GROUP_USERS = "groupUsers";

    public static final String ATTR_DELETED_ITEM_LIST = "deleteItemList";

    public static final String ATTR_DELETED_OPTION_LIST = "deleteOptionList";

    public static final String ATTR_SCRATCHIE = "scratchie";

    public static final String ATTR_IS_SCRATCHING_FINISHED = "isScratchingFinished";

    public static final String ATTR_IS_WAITING_FOR_LEADER_TO_SUBMIT_NOTEBOOK = "isWaitingForLeaderToSubmitNotebook";

    public static final String ATTR_WAITING_MESSAGE_KEY = "waitingMessageKey";

    public static final String ATTR_LEARNERS = "learners";

    public static final String ATTR_VISIT_LOGS = "visitLogs";

    public static final String ATTR_ITEM_UID = "itemUid";

    public static final String ATTR_NEXT_ACTIVITY_URL = "nextActivityUrl";

    public static final String ATTR_SUMMARY_LIST = "summaryList";

    public static final String ATTR_ITEM_SUMMARY = "itemSummary";

    public static final String ATTR_RESOURCE_INSTRUCTION = "instructions";

    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";

    public static final String ATTR_USER_SESSION_ID = "userSessionId";

    public static final String ATTR_RESOURCE_FORM = "scratchieForm";

    public static final String ATTR_TITLE = "title";

    public static final String ATTR_ITEM = "item";

    public static final String ATTR_SCORE = "score";

    public static final String ATTR_MAX_SCORE = "maxScore";

    public static final String ATTR_USER_FINISHED = "userFinished";

    public static final String ATTR_IS_GROUPED_ACTIVITY = "isGroupedActivity";

    public static final String ATTR_REFLECTION_ON = "reflectOn";

    public static final String ATTR_IS_BURNING_QUESTIONS_ENABLED = "isBurningQuestionsEnabled";

    public static final String ATTR_IS_PRESET_MARKS_OVERWRITTEN = "isPresetMarksOverwritten";

    public static final String ATTR_DEFAULT_PRESET_MARKS = "defaultPresetMarks";

    public static final String ATTR_REFLECTION_INSTRUCTION = "reflectInstructions";

    public static final String ATTR_REFLECTION_ENTRY = "reflectEntry";

    public static final String ATTR_REFLECTIONS = "reflections";

    public static final String ATTR_BURNING_QUESTION_ITEM_DTOS = "burningQuestionItemDtos";

    public static final String ATTR_USER_UID = "userUid";

    public static final String ATTR_USER_ID = "userId";

    public static final String ATTR_GROUP_LEADER = "groupLeader";

    public static final String ATTR_GROUP_LEADER_NAME = "groupLeaderName";

    public static final String ATTR_GROUP_LEADER_USER_ID = "groupLeaderUserId";

    public static final String ATTR_IS_USER_LEADER = "isUserLeader";

    public static final String ATTR_IS_TIME_LIMIT_ENABLED = "isTimeLimitEnabled";

    public static final String ATTR_IS_TIME_LIMIT_NOT_LAUNCHED = "isTimeLimitNotLaunched";

    public static final String ATTR_SECONDS_LEFT = "secondsLeft";

    public static final String ATTR_SUBMISSION_DEADLINE = "submissionDeadline";
    public static final String ATTR_SUBMISSION_DEADLINE_DATESTRING = "submissionDateString";
    public static final String ATTR_IS_SUBMISSION_DEADLINE_PASSED = "isSubmissionDeadlinePassed";

    public static final String ATTR_OPTION_UIDS = "optionUids";

    public static final String ATTR_ACTIVITIES_PROVIDING_CONFIDENCE_LEVELS = "activitiesProvidingConfidenceLevels";

    public static final String ATTR_ACTIVITIES_PROVIDING_VSA_ANSWERS = "activitiesProvidingVsaAnswers";

    public static final String LEARNER_MARK = "learner.mark";

    public static final String ERROR_MSG_REQUIRED_FIELDS_MISSING = "error.required.fields.missing";
    public static final String ERROR_MSG_ENTERED_MARKS_NOT_COMMA_SEPARATED_INTEGERS = "error.entered.marks.not.comma.separates";

    public static final String ATTR_SHOW_RESULTS = "showResults";

    public static final String ATTR_REFERENCES_SEQUENCE_IDS = "sequenceIds";
}
