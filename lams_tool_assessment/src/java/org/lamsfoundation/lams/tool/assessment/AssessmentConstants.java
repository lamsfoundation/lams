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

package org.lamsfoundation.lams.tool.assessment;

public class AssessmentConstants {
    public static final String TOOL_SIGNATURE = "laasse10";

    public static final String ASSESSMENT_SERVICE = "laasseAssessmentService";

    public static final int COMPLETED = 1;

    public static final int INITIAL_OVERALL_FEEDBACK_NUMBER = 3;

    public static final String EXPORT_QUESTIONS_FILENAME = "questions.xml";

    // for parameters' name

    public static final String PARAM_WAITING_MESSAGE_KEY = "waitingMessageKey";

    public static final String PARAM_TOOL_CONTENT_ID = "toolContentID";

    public static final String PARAM_TOOL_SESSION_ID = "toolSessionID";

    public static final String PARAM_FILE_VERSION_ID = "fileVersionId";

    public static final String PARAM_FILE_UUID = "fileUuid";

    public static final String PARAM_QUESTION_INDEX = "questionIndex";

    public static final String PARAM_QUESTION_DISPLAY_ORDER = "questionDisplayOrder";

    public static final String PARAM_QUESTION_UID = "questionUid";

    public static final String PARAM_OPTION_INDEX = "optionIndex";

    public static final String PARAM_TITLE = "title";

    public static final String PARAM_NOT_A_NUMBER = "nan";

    public static final String PARAM_GRADE = "grade";

    public static final String PARAM_MARKER_COMMENT = "markerComment";

    public static final String PARAM_COLUMN = "column";

    public static final String PARAM_MAX_MARK = "maxMark";

    public static final String PARAM_SEQUENCE_ID = "sequenceId";

    public static final String PARAM_SESSION_ID = "sessionId";

    public static final String PARAM_QUESTION_RESULT_UID = "questionResultUid";

    // for request attribute name

    /**
     * used to signify edit in monitor when assessment has been attempted already
     */
    public static final String ATTR_IS_AUTHORING_RESTRICTED = "isAuthoringRestricted";

    public static final String ATTR_GROUP_USERS = "groupUsers";

    public static final String ATTR_IS_USER_LEADER = "isUserLeader";

    public static final String ATTR_GROUP_LEADER = "groupLeader";

    public static final String ATTR_TOOL_CONTENT_ID = "toolContentID";

    public static final String ATTR_TOOL_SESSION_ID = "toolSessionID";

    public static final String ATTR_QUESTION_PREFIX = "question";

    public static final String ATTR_CONFIDENCE_LEVEL_PREFIX = "confidenceLevel";

    public static final String ATTR_ANSWER_JUSTIFICATION_PREFIX = "answerJustification";

    public static final String ATTR_QUESTION_LIST = "questionList";

    public static final String ATTR_RANDOM_POOL_QUESTIONS = "randomPoolQuestions";

    public static final String ATTR_DELETED_RANDOM_POOL_QUESTIONS = "deletedRandomPoolQuestions";

    public static final String ATTR_QUESTION_REFERENCES = "questionReferences";

    public static final String ATTR_DELETED_QUESTION_REFERENCES = "deleteQuestionReferences";

    public static final String ATTR_UNIT_LIST = "unitList";

    public static final String ATTR_REFERENCES_MAX_MARKS = "referenceMaxMarks";

    public static final String ATTR_REFERENCES_SEQUENCE_IDS = "sequenceIds";

    public static final String ATTR_HAS_EDIT_RIGHT = "hasEditRight";

    public static final String ATTR_OVERALL_FEEDBACK_LIST = "overallFeedbackList";

    public static final String ATTR_OVERALL_FEEDBACK_COUNT = "overallFeedbackCount";

    public static final String ATTR_OVERALL_FEEDBACK_GRADE_BOUNDARY_PREFIX = "overallFeedbackGradeBoundary";

    public static final String ATTR_OVERALL_FEEDBACK_FEEDBACK_PREFIX = "overallFeedbackFeedback";

    public static final String ATTR_OVERALL_FEEDBACK_SEQUENCE_ID_PREFIX = "overallFeedbackSequenceId";

    public static final String ATTR_ASSESSMENT = "assessment";

    public static final String ATTR_QUESTION_UID = "questionUid";

    public static final String ATTR_NEXT_ACTIVITY_URL = "nextActivityUrl";

    public static final String ATTR_SUMMARY_LIST = "summaryList";

    public static final String ATTR_USER_SUMMARY = "userSummary";

    public static final String ATTR_QUESTION_SUMMARY = "questionSummary";

    public static final String ATTR_QUESTION_SUMMARY_LIST = "questionSummaryList";

    public static final String ATTR_USER_LIST = "userList";

    public static final String ATTR_LOCK_ON_FINISH = "lockOnFinish";

    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";

    public static final String ATTR_ASSESSMENT_FORM = "assessmentForm";

    public static final String ATTR_FILE_TYPE_FLAG = "fileTypeFlag";

    public static final String ATTR_TITLE = "title";

    public static final String ATTR_INSTRUCTIONS = "instructions";

    public static final String ATTR_PAGE_NUMBER = "pageNumber";

    public static final String ATTR_PAGED_QUESTION_DTOS = "pagedQuestions";

    public static final String ATTR_ASSESSMENT_RESULT = "assessmentResult";

    public static final String ATTR_IS_RESUBMIT_ALLOWED = "isResubmitAllowed";

    public static final String ATTR_IS_USER_FAILED = "isUserFailed";

    public static final String ATTR_IS_ANSWERS_VALIDATION_FAILED = "isAnswersValidationFailed";

    public static final String ATTR_SHOW_RESULTS = "showResults";

    public static final String ATTR_USER_FINISHED = "userFinished";

    public static final String ATTR_USER = "user";

    public static final String ATTR_QUESTION_NUMBERING_OFFSET = "questionNumberingOffset";

    public static final String ATTR_SUBMISSION_DEADLINE = "submissionDeadline";

    public static final String ATTR_SUBMISSION_DEADLINE_DATESTRING = "submissionDateString";

    public static final String ATTR_IS_SUBMISSION_DEADLINE_PASSED = "isSubmissionDeadlinePassed";

    public static final String ATTR_IS_GROUPED_ACTIVITY = "isGroupedActivity";

    public static final String ATTR_REFLECTION_ON = "reflectOn";

    public static final String ATTR_REFLECTION_INSTRUCTION = "reflectInstructions";

    public static final String ATTR_REFLECTION_ENTRY = "reflectEntry";

    public static final String ATTR_REFLECT_LIST = "reflectList";

    public static final String ATTR_ACTIVITY_EVALUATION = "activityEvaluation";

    public static final String ATTR_TOOL_OUTPUT_DEFINITIONS = "toolOutputDefinitions";

    public static final String ATTR_MARK_SUMMARY = "markSummary";

    //output definitions
    public static final String OUTPUT_NAME_LEARNER_TOTAL_SCORE = "learner.total.score";
    public static final String OUTPUT_NAME_LEARNER_TIME_TAKEN = "learner.time.taken";
    public static final String OUTPUT_NAME_LEARNER_NUMBER_ATTEMPTS = "learner.number.of.attempts";

    public static final String OUTPUT_NAME_BEST_SCORE = "best.score";
    public static final String OUTPUT_NAME_FIRST_SCORE = "first.score";
    public static final String OUTPUT_NAME_AVERAGE_SCORE = "average.score";

    public static final String OUTPUT_NAME_ORDERED_ANSWERS = "ordered.answers";
    public static final String OUTPUT_NAME_CONDITION_ORDERED_ANSWER = "ordered.answer";

    public static final String ATTR_USER_UID = "userUid";

    public static final String DEFUALT_PROTOCOL_REFIX = "http://";

    public static final String ALLOW_PROTOCOL_REFIX = new String("[http://|https://|ftp://|nntp://]");

    public static final String EVENT_NAME_NOTIFY_TEACHERS_ON_ASSIGMENT_SUBMIT = "notify_teachers_on_assigment_submit";

    // configuration keys
    public static final String CONFIG_KEY_HIDE_TITLES = "hideTitles";

    public static final String CONFIG_KEY_AUTO_EXPAND_JUSTIFICATION = "autoexpandJustification";

    public static final String ATTR_IS_QUESTION_ETHERPAD_ENABLED = "isQuestionEtherpadEnabled";

    public static final String ATTR_CODE_STYLES = "codeStyles";

    public static final String ATTR_ALL_GROUP_USERS = "allGroupUsers";

    //flux management
    public static final String COMPLETION_CHARTS_UPDATE_SINK_NAME = "assessment completion chart updated";
    public static final String ANSWERS_UPDATED_SINK_NAME = "assessment learner answers updated";
    public static final String LEARNER_TRAVERSED_SINK_NAME = "assessment learner traversed";
    public static final String COMPLETION_CHARTS_UPDATE_FLUX_NAME = "assessment completion chart updated";

    public static final String TIME_LIMIT_PANEL_UPDATE_SINK_NAME = "assessment time limit panel updated";
    public static final String TIME_LIMIT_PANEL_UPDATE_FLUX_NAME = "assessment time limit panel updated";
}