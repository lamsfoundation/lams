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


package org.lamsfoundation.lams.tool.qa;

/**
 * @author Ozgur Demirtas
 */
public interface QaAppConstants {

    public static final int SORT_BY_NO = 0;
    public static final int SORT_BY_ANSWER_ASC = 3; // learner
    public static final int SORT_BY_ANSWER_DESC = 4; // learner
    public static final int SORT_BY_USERNAME_ASC = 1; // monitoring
    public static final int SORT_BY_USERNAME_DESC = 2; // monitoring
    public static final int SORT_BY_RATING_ASC = 5; // learner & monitoring
    public static final int SORT_BY_RATING_DESC = 6; // learner & monitoring
    public static final int SORT_BY_COMMENT_COUNT = 7; // learner 

    public static final String MY_SIGNATURE = "laqa11";
    public static final String TOOL_CONTENT_ID = "toolContentID";
    public static final String LIST_QUESTIONS = "questions";
    public static final String LIST_DELETED_QUESTIONS = "deletedQuestions";
    public static final String QUESTION_DTO = "questionDTO";
    public static final String RESPONSES = "responses";
    
    public static final String TOOL_SERVICE = "toolService";
    public static final String TITLE = "title";
    public static final String INSTRUCTIONS = "instructions";
    public static final String LOAD_LEARNER = "learning/AnswersContent";
    public static final String VIEW_ALL_RESULTS = "viewAllResults";
    public static final String INDIVIDUAL_LEARNER_RESULTS = "learning/IndividualLearnerResults";
    public static final String MAP_ALL_RESULTS_KEY = "mapAllResultsKey";
    public static final String SOURCE_QA_STARTER = "sourceQaStarter";
    public static final String RESPONSE_UID = "responseUid";
    public static final String IS_HIDE_ITEM = "isHideItem";
    public static final String MAP_SEQUENTIAL_ANSWERS_KEY = "mapSequentialAnswersKey";
    public static final String TOOL_SESSION_ID = "toolSessionID";
    public static final String NOTEBOOK = "notebook";
    public static final String ENTRY_TEXT = "entryText";
    public static final String REFLECTIONS_CONTAINER_DTO = "reflectionsContainerDTO";
    public static final String LEARNER_NOTEBOOK = "learnerNotebook";
    public static final String QA_STATS_DTO = "qaStatsDTO";
    public static final String LIST_ALL_GROUPS_DTO = "listAllGroupsDTO";
    public static final String IS_TOOL_SESSION_AVAILABLE = "isToolSessionAvailable";

    public static final String REMOVABLE_QUESTION_INDEX = "removableQuestionIndex";
    public static final String DEFINE_LATER = "defineLater";
    public static final String WAIT_FOR_LEADER = "waitForLeader";

    public static final String SOURCE_MC_STARTER = "sourceMcStarter";
    public static final Integer MAX_QUESTION_COUNT = 50;

    public static final String GENERAL_LEARNER_FLOW_DTO = "generalLearnerFlowDTO";

    public static final Long INITIAL_QUESTION_COUNT = 1L;

    /*
     * Struts level constants
     */
    public static final String LOAD_MONITORING = "loadMonitoring";
    public static final String LEARNING_MODE = "learningMode";
    public static final String SUCCESS = "success";

    /*
     * authoring mode controllers
     */
    public static final String IS_ADD_QUESTION = "isAddQuestion";
    public static final String IS_REMOVE_QUESTION = "isRemoveQuestion";
    public static final String IS_REMOVE_CONTENT = "isRemoveContent";

    /*
     * authoring mode constants
     */
    public static final String MAP_QUESTION_CONTENT = "mapQuestionContent";
    public static final String END_LEARNING_MESSSAGE = "endLearningMessage";
    public static final String ON = "ON";
    public static final String OFF = "OFF";
    public static final String RICHTEXT_TITLE = "richTextTitle";
    public static final String RICHTEXT_INSTRUCTIONS = "richTextInstructions";
    public static final String RICHTEXT_BLANK = "<p>&nbsp;</p>";

    public static final String USER_ID = "userID";

    public final long ONE_DAY = 60 * 60 * 1000 * 24;

    public final static String NOT_ATTEMPTED = "NOT_ATTEMPTED";
    public final static String INCOMPLETE = "INCOMPLETE";
    public static final String COMPLETED = "COMPLETED";

    public static final String MAP_TOOL_SESSIONS = "mapToolSessions";
    public static final Integer MAX_TOOL_SESSION_COUNT = new Integer(500);
    public static final String COUNT_SESSION_COMPLETE = "countSessionComplete";

    public static final String COUNT_ALL_USERS = "countAllUsers";
    public static final String LIST_MONITORED_ANSWERS_CONTAINER_DTO = "listMonitoredAnswersContainerDto";

    public static final String ACTIVITY_TITLE = "activityTitle";
    public static final String ACTIVITY_INSTRUCTIONS = "activityInstructions";
    public static final String IS_USERNAME_VISIBLE = "isUsernameVisible";
    public static final String CURRENT_ANSWER = "currentAnswer";

    public static final String MODE = "mode";
    public static final String LEARNER = "learner";
    public static final String TEACHER = "teacher";

    /*
     * user actions
     */
    public static final String ADD_NEW_QUESTION = "addNewQuestion";
    public static final String REMOVE_QUESTION = "removeQuestion";
    public static final String REMOVE_ALL_CONTENT = "removeAllContent";
    public static final String SUBMIT_ALL_CONTENT = "submitAllContent";
    public static final String SUBMIT_TAB_DONE = "submitTabDone";

    public static final String OPTION_OFF = "false";

    // LEARNER mode contants
    public static final String MAP_QUESTION_CONTENT_LEARNER = "mapQuestionContentLearner";
    public static final String CURRENT_QUESTION_INDEX = "currentQuestionIndex";
    public static final String TOTAL_QUESTION_COUNT = "totalQuestionCount";
    public static final String MAP_ANSWERS = "mapAnswers";
    public static final String USER_FEEDBACK = "userFeedback";
    public static final String REPORT_TITLE_LEARNER = "reportTitleLearner";
    public static final String END_LEARNING_MESSAGE = "endLearningMessage";
    public static final String CHECK_ALL_SESSIONS_COMPLETED = "checkAllSessionsCompleted";
    public static final String FROM_TOOL_CONTENT_ID = "fromToolContentId";
    public static final String TO_TOOL_CONTENT_ID = "toToolContentId";
    public static final String LEARNER_REPORT = "learnerRep";
    public static final String LEARNER_REP = "individualLearnerRep";
    public static final String REVISITED_LEARNER_REP = "revisitedLearnerRep";
    public static final String IS_DISABLED = "isDisabled";

    /*
     * Monitoring Mode constants
     */
    public static final String EDITACTIVITY_EDITMODE = "editActivityEditMode";
    public static final String RENDER_MONITORING_EDITACTIVITY = "renderMonitoringEditActivity";
    public static final String NO_AVAILABLE_SESSIONS = "noAvailableSessions";
    public static final String NO_TOOL_SESSIONS_AVAILABLE = "noToolSessionAvailable";
    public static final String ATTR_CONTENT = "content";
    public static final String ATTR_SUBMISSION_DEADLINE = "submissionDeadline";
    public static final String ATTR_SUBMISSION_DEADLINE_DATESTRING = "submissionDateString";
    public static final String PARAM_SHOW_OTHER_ANSWERS_AFTER_DEADLINE = "showOtherAnswersAfterDeadline";
    public static final String ATTR_IS_SUBMISSION_DEADLINE_PASSED = "isSubmissionDeadlinePassed";
    public static final String ATTR_GROUP_USERS = "groupUsers";
    public static final String ATTR_IS_USER_LEADER = "isUserLeader";
    public static final String ATTR_GROUP_LEADER = "groupLeader";

    public static final String TIMEZONE = "timeZone";
    public static final String TIMEZONE_ID = "timeZoneId";
    /*
     * Monitor and Learning common constants - used in jsp reporting
     */

    public static final String QUESTION_LISTING_MODE = "questionListingMode";
    public static final String QUESTION_LISTING_MODE_SEQUENTIAL = "questionListingModeSequential";
    public static final String QUESTION_LISTING_MODE_PREVIEW = "questionListingModePreview";
    public static final String QUESTION_LISTING_MODE_COMBINED = "questionListingModeCombined";

    public static final String FEEDBACK_TYPE_SEQUENTIAL = "You will be presented a total of : ";
    public static final String FEEDBACK_TYPE_COMBINED = "You are being presented total of: ";
    public static final String QUESTIONS = " questions.";

    public static final String SUBMIT_SUCCESS = "sbmtSuccess";
    // for condition management use
    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";
    public static final String ATTR_CONDITION_SET = "conditionList";
    public static final String ATTR_QA_AUTHORING_FORM = "QaAuthoringForm";
    public static final String PARAM_ORDER_ID = "orderId";
    public static final String ATTR_DELETED_CONDITION_LIST = "deletedConditionList";
    public static final int QUESTION_CUTOFF_INDEX = 40;
    public static final String USER_ANSWERS_DEFINITION_NAME = "user.answers.output.definition.qa";
    public static final String GROUP_ANSWERS_DEFINITION_NAME = "group.answers.output.definition.qa";
    public static final String QUESTIONS_DEFINITION_NAME = "questions.output.definition.qa";
}
