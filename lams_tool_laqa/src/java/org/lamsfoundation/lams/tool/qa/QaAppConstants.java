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
    public static final String WAIT_FOR_LEADER = "waitForLeader";
    public static final Integer MAX_QUESTION_COUNT = 50;
    public static final String GENERAL_LEARNER_FLOW_DTO = "generalLearnerFlowDTO";
    public static final Long INITIAL_QUESTION_COUNT = 1L;

    /*
     * Struts level constants
     */
    public static final String LOAD_MONITORING = "loadMonitoring";
    public static final String SUCCESS = "success";

    public final long ONE_DAY = 60 * 60 * 1000 * 24;

    public static final String COMPLETED = "COMPLETED";

    public static final String ACTIVITY_TITLE = "activityTitle";
    public static final String ACTIVITY_INSTRUCTIONS = "activityInstructions";

    public static final String MODE = "mode";

    // LEARNER mode contants
    public static final String LEARNER_REP = "individualLearnerRep";
    public static final String IS_DISABLED = "isDisabled";

    /*
     * Monitoring Mode constants
     */
    public static final String ATTR_CONTENT = "content";
    public static final String ATTR_SUBMISSION_DEADLINE = "submissionDeadline";
    public static final String ATTR_SUBMISSION_DEADLINE_DATESTRING = "submissionDateString";
    public static final String PARAM_SHOW_OTHER_ANSWERS_AFTER_DEADLINE = "showOtherAnswersAfterDeadline";
    public static final String ATTR_IS_SUBMISSION_DEADLINE_PASSED = "isSubmissionDeadlinePassed";
    public static final String ATTR_GROUP_USERS = "groupUsers";
    public static final String ATTR_IS_USER_LEADER = "isUserLeader";
    public static final String ATTR_GROUP_LEADER = "groupLeader";

    /*
     * Monitor and Learning common constants - used in jsp reporting
     */
    public static final String QUESTION_LISTING_MODE_SEQUENTIAL = "questionListingModeSequential";
    public static final String QUESTION_LISTING_MODE_PREVIEW = "questionListingModePreview";
    public static final String QUESTION_LISTING_MODE_COMBINED = "questionListingModeCombined";

    public static final String FEEDBACK_TYPE_SEQUENTIAL = "You will be presented a total of : ";
    public static final String FEEDBACK_TYPE_COMBINED = "You are being presented total of: ";
    public static final String QUESTIONS = " questions.";

    // for condition management use
    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";
    public static final String ATTR_CONDITION_SET = "conditionList";
    public static final String PARAM_ORDER_ID = "orderId";
    public static final String ATTR_DELETED_CONDITION_LIST = "deletedConditionList";
    public static final int QUESTION_CUTOFF_INDEX = 40;
    public static final String USER_ANSWERS_DEFINITION_NAME = "user.answers.output.definition.qa";
    public static final String GROUP_ANSWERS_DEFINITION_NAME = "group.answers.output.definition.qa";
    public static final String QUESTIONS_DEFINITION_NAME = "questions.output.definition.qa";
}
