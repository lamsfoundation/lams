/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc;

/**
 * @author Ozgur Demirtas Defines constants used throughout the application
 */
public interface McAppConstants {

    public static final String MY_SIGNATURE = "lamc11";
    public static final String DEFAULT_CONTENT_ID = "defaultContentId";
    public static final String TOOL_SERVICE = "tool_service";
    public static final String ERROR_MCAPPLICATION = "error.exception.McApplication";

    /*
     * Struts level constants
     */
    public static final String LOAD_AUTHORING = "authoringTabsHolder";
    public static final String LEARNING_STARTER = "learningStarter";
    public static final String LOAD_LEARNER = "loadLearner";
    public static final String LOAD_MONITORING_CONTENT = "loadMonitoring";
    public static final String LOAD_MONITORING = "refreshMonitoring";
    public static final String LOAD_MONITORING_CONTENT_EDITACTIVITY = "loadMonitoringEditActivity";
    public static final String LIST_ALL_GROUPS_DTO = "listAllGroupsDTO";
    public static final String EXPORT_PORTFOLIO = "exportPortfolio";
    public static final String INDIVIDUAL_REPORT = "individualReport";
    public static final String REDO_QUESTIONS = "redoQuestions";
    public static final String SINGLE_QUESTION_ANSWERS = "singleQuestionAnswers";
    public static final String ERROR_LIST = "errorList";
    public static final String PREVIEW = "preview";
    public static final String LEARNER_PROGRESS = "learnerProgress";
    public static final String LEARNER_PROGRESS_USERID = "learnerProgressUserId";
    public static final String ACTIVITY_TITLE_KEY = "activityTitleKey";
    public static final String ACTIVITY_INSTRUCTIONS_KEY = "activityInstructionsKey";
    public static final String LIST_QUESTION_DTOS = "listQuestionContentDTO";
    public static final String LIST_DELETED_QUESTION_DTOS = "deletedQuestionDTOs";
    public static final String LOAD_VIEW_ONLY = "loadViewOnly";
    public static final String MC_GENERAL_AUTHORING_DTO = "mcGeneralAuthoringDTO";
    public static final String LIST_ADD_QUESTION_CONTENT_DTO_KEY = "listAddQuestionContentDtoKey";
    public static final String NEW_QUESTION_CONTENT_DTO = "newQuestionContentDTO";
    public static final String LIST_CANDIDATE_ANSWERS_DTO = "listCandidateAnswersDTO";
    public static final String CURRENT_EDITABLE_QUESTION_INDEX = "currentEditableQuestionIndex";
    public static final String MC_GENERAL_MONITORING_DTO = "mcGeneralMonitoringDTO";
    public static final String NOTEBOOK_ENTRIES_EXIST = "notebookEntriesExist";
    public static final String NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST = "noSessionsNotebookEntriesExist";
    public static final String MONITORING = "monitoring";
    public static final String RESPONSE_ID = "responseId";
    public static final String NEW_QUESTION_BOX_DTO = "newQuestionBoxDTO";
    public static final String NEW_QUESTION_DTO = "newQuestionDTO";
    public static final String TOTAL_MARKS_POSSIBLE = "totalMarksPossible";

    public static final String AUTHORING_CANCELLED = "authoringCancelled";
    public static final String DEFINE_LATER_EDIT_ACTIVITY = "defineLaterEditActivity";
    public static final String EDIT_OPTIONS_MODE = "editOptionsMode";
    public static final String SUBMIT_SUCCESS = "sbmtSuccess";
    public static final String QUESTION_INDEX = "questionIndex";
    public static final String REFLECTION_SUBJECT = "reflectionSubject";
    public static final String REFLECTIONS_CONTAINER_DTO = "reflectionsContainerDTO";
    public static final String LEARNER_NOTEBOOK = "learnerNotebook";
    public static final String USER_MASTER_DETAIL = "userMasterDetail";

    /*
     * authoring mode controllers
     */
    public static final String IS_ADD_QUESTION = "isAddQuestion";
    public static final String IS_REMOVE_QUESTION = "isRemoveQuestion";
    public static final String IS_REMOVE_CONTENT = "isRemoveContent";
    public static final String IS_REVISITING_USER = "isRevisitingUser";

    public static final String USER = "user";
    public static final String TOOL_CONTENT_ID = "toolContentID";
    public static final String TOOL_CONTENT_UID = "toolContentUID";
    public static final String TOOL_SESSION_ID = "toolSessionID";
    public static final String USER_ID = "userID";
    public static final String MAX_QUESTION_INDEX = "maxQuestionIndex";
    public static final String COPY_TOOL_CONTENT = "copyToolContent";
    public static final String REMOVE_TOOL_CONTENT = "removeToolContent";

    public static final String DEFAULT_MCQ_TITLE = "MCQ";

    public static final int MAX_OPTION_COUNT = 25;
    public static final String QUESTION_AND_CANDIDATE_ANSWERS_KEY = "questionAndCandidateAnswersKey";

    /*
     * authoring mode constants
     */
    public static final String MAP_QUESTIONS_CONTENT = "mapQuestionsContent";
    public static final String MAP_OPTIONS_CONTENT = "mapOptionsContent";
    public static final String MAP_DEFAULTOPTIONS_CONTENT = "mapDefaultOptionsContent";
    public static final String MAP_DEFAULTSELECTEDOPTIONS_CONTENT = "mapDefaultSelectedOptionsContent";
    public static final String MAP_DISABLED_QUESTIONS = "mapDisabledQuestions";
    public static final String MAP_GENERAL_OPTIONS_CONTENT = "mapGeneralOptionsContent";
    public static final String MAP_GENERAL_SELECTED_OPTIONS_CONTENT = "mapGeneralSelectedOptionsContent";
    public static final String MAP_STARTUP_GENERAL_OPTIONS_CONTENT = "mapStartupGeneralOptionsContent";
    public static final String MAP_STARTUP_GENERAL_SELECTED_OPTIONS_CONTENT = "mapStartupGeneralSelectedOptionsContent";
    public static final String MAP_STARTUP_GENERAL_OPTIONS_QUEID = "mapStartupGeneralOptionsQueId";
    public static final String QUESTIONS_WITHNO_OPTIONS = "questionsWithNoOptions";

    public static final String MAP_GENERAL_CHECKED_OPTIONS_CONTENT = "mapGeneralCheckedOptionsContent";
    public static final String MAP_LEARNER_QUESTIONS_CONTENT = "mapLearnerQuestionsContent";
    public static final String MAP_LEARNER_CHECKED_OPTIONS_CONTENT = "mapLearnerCheckedOptionsContent";
    public static final String MAP_LEARNER_ASSESSMENT_RESULTS = "mapLearnerAssessmentResults";
    public static final String MAP_LEARNER_FEEDBACK_INCORRECT = "mapLeanerFeedbackIncorrect";
    public static final String MAP_LEARNER_FEEDBACK_CORRECT = "mapLeanerFeedbackCorrect";
    public static final String MAP_QUESTION_WEIGHTS = "mapQuestionWeights";
    public static final String MAP_RESPONSES = "mapResponses";

    public static final String MAP_WEIGHTS = "mapWeights";
    public static final String MAP_CHECKBOX_STATES = "mapCheckBoxStates";
    public static final String MAP_SELECTED_OPTIONS = "mapSelectedOptions";

    public static final String DEFAULT_FIRST_OPTION = "Candidate Answer 1";
    public static final String DEFAULT_SELECTED_OPTION = "Candidate Answer 2";
    public static final String MAP_FEEDBACK_INCORRECT = "mapFeedbackIncorrect";
    public static final String MAP_INCORRECT_FEEDBACK = "mapIncorrectFeedback";

    public static final String MAP_INCORRECT_FEEDBACK_LEARNER = "mapIncorrectFeedbackLearner";
    public static final String MAP_CORRECT_FEEDBACK_LEARNER = "mapCorrectFeedbackLearner";
    public static final String MAP_CORRECT_FEEDBACK = "mapCorrectFeedback";
    public static final String MAP_FEEDBACK_CORRECT = "mapFeedbackCorrect";
    public static final String IS_PORTFOLIO_EXPORT = "isPortfolioExport";
    public static final String PORTFOLIO_EXPORT_MODE = "portfolioExportMode";
    public static final String PORTFOLIO_EXPORT_DATA_FILENAME = "portfolioExportDataFileName";

    public static final String SELECTED_QUESTION = "selectedQuestion";
    public static final String SELECTED_QUESTION_INDEX = "selectedQuestionIndex";
    public static final String DEFAULT_QUESTION_UID = "defaultQuestionUid";

    public static final String DEFAULT_FEEDBACK_INCORRECT = "Your answer is incorrect";
    public static final String DEFAULT_FEEDBACK_CORRECT = "Correct";

    public static final String TITLE = "title";
    public static final String INSTRUCTIONS = "instructions";
    public static final String CREATION_DATE = "creationDate";
    public static final String WAIT_FOR_LEADER = "waitForLeader";
    public static final String RETRIES = "retries";
    public static final String PASSMARK = "passMark";
    public static final String VIEW_ANSWERS = "viewAnswers";
    public static final String SHOW_AUTHORING_TABS = "showAuthoringTabs";

    public static final String ON = "ON";
    public static final String OFF = "OFF";
    public static final String RICHTEXT_FEEDBACK_CORRECT = "richTextFeedbackCorrect";
    public static final String RICHTEXT_INCORRECT_FEEDBACK = "richTextIncorrectFeedback";
    public static final String RICHTEXT_CORRECT_FEEDBACK = "richTextCorrectFeedback";
    public static final String RICHTEXT_FEEDBACK_INCORRECT = "richTextFeedbackInCorrect";
    public static final String RICHTEXT_REPORT_TITLE = "richTextReportTitle";
    public static final String RICHTEXT_TITLE = "richTextTitle";
    public static final String RICHTEXT_INSTRUCTIONS = "richTextInstructions";
    public static final String RICHTEXT_BLANK = "<p>&nbsp;</p>";

    public static final String COUNT_SESSION_COMPLETE = "countSessionComplete";
    public static final String COUNT_ALL_USERS = "countAllUsers";
    public static final String COUNT_MAX_ATTEMPT = "countMaxAttempt";
    public static final String TOP_MARK = "topMark";
    public static final String LOWEST_MARK = "lowestMark";
    public static final String AVERAGE_MARK = "averageMark";

    /* Date time restriction */
    public static final String ATTR_SUBMISSION_DEADLINE = "submissionDeadline";
    public static final String ATTR_IS_SUBMISSION_DEADLINE_PASSED = "isSubmissionDeadlinePassed";
    public static final String ATTR_GROUP_USERS = "groupUsers";
    public static final String ATTR_IS_USER_LEADER = "isUserLeader";
    public static final String ATTR_GROUP_LEADER = "groupLeader";

    /*
     * the learner or monitoring environment provides toolSessionId
     */
    public final static String NOT_ATTEMPTED = "NOT_ATTEMPTED";
    public final static String INCOMPLETE = "INCOMPLETE";
    public static final String COMPLETED = "COMPLETED";

    public static final String MAP_TOOL_SESSIONS = "mapToolSessions";
    public static final Integer MAX_TOOL_SESSION_COUNT = new Integer(500);
    public static final String OPTION_OFF = "false";
    public static final String ACTIVITY_TITLE = "activityTitle";
    public static final String ACTIVITY_INSTRUCTIONS = "activityInstructions";
    public static final String CORRECT = "Correct";

    /*
     * user actions
     */
    public static final String ADD_NEW_QUESTION = "addNewQuestion";
    public static final String REMOVE_QUESTION = "removeQuestion";
    public static final String REMOVE_ALL_CONTENT = "removeAllContent";
    public static final String SUBMIT_ALL_CONTENT = "submitAllContent";
    public static final String SUBMIT_TAB_DONE = "submitTabDone";

    /* learner mode contants */
    public static final String MAP_QUESTION_CONTENT_LEARNER = "mapQuestionContentLearner";
    public static final String LEARNER_ANSWERS_DTO_LIST = "learnerAnswersDTOList";
    public static final String MC_GENERAL_LEARNER_FLOW_DTO = "mcGeneralLearnerFlowDTO";
    public static final String ANSWER_DTOS = "answerDtos";
    public static final String CURRENT_QUESTION_INDEX = "currentQuestionIndex";
    public static final String TOTAL_QUESTION_COUNT = "totalQuestionCount";
    public static final String HR_COLUMN_COUNT = "hrColumnCount";
    public static final String LEARNER_MARK = "learnerMark";
    public static final String MAP_ANSWERS = "mapAnswers";
    public static final String CURRENT_ANSWER = "currentAnswer";
    public static final String USER_FEEDBACK = "userFeedback";
    public static final String REPORT_TITLE_LEARNER = "reportTitleLearner";
    public static final String TOTAL_COUNT_REACHED = "totalCountReached";
    public static final String NOTEBOOK = "notebook";
    public static final String ENTRY_TEXT = "entryText";

    public static final String IS_USERNAME_VISIBLE = "isUsernameVisible";
    public static final String IS_CONTENT_IN_USE = "isContentInUse";
    public static final String IS_MONITORED_CONTENT_IN_USE = "isMonitoredContentInUse";
    public static final String IS_RETRIES = "isRetries";
    public static final String IS_SHOW_FEEDBACK = "isShowFeedback";
    public static final String IS_SHOW_LEARNERS_REPORT = "isShowLearnersReport";
    public static final String IS_ALL_SESSIONS_COMPLETED = "isAllSessionsCompleted";
    public static final String CHECK_ALL_SESSIONS_COMPLETED = "checkAllSessionsCompleted";
    public static final String FROM_TOOL_CONTENT_ID = "fromToolContentId";
    public static final String TO_TOOL_CONTENT_ID = "toToolContentId";
    public static final String LEARNER_REPORT = "learnerReport";
    public static final String MAP_USER_RESPONSES = "mapUserResponses";
    public static final String MAP_MAIN_REPORT = "mapMainReport";
    public static final String MAP_STATS = "mapStats";

    /*
     * Monitoring Mode constants
     */
    public static final String ATTR_CONTENT = "content";
    public static final String CURRENT_MONITORING_TAB = "currentMonitoringTab";
    public static final String REPORT_TITLE_MONITOR = "reportTitleMonitor";
    public static final String MONITOR_USER_ID = "userId";
    public static final String USER_UID = "userUid";
    public static final String USER_ATTEMPTS = "userAttempts";
    public static final String PARAM_NOT_A_NUMBER = "nan";
    public static final String PARAM_GRADE = "grade";
    public static final String PARAM_USER_ATTEMPT_UID = "userAttemptUid";
    public static final String MONITORING_REPORT = "monitoringReport";
    public static final String MONITORING_ERROR = "monitoringError";
    public static final String MAP_MONITORING_QUESTIONS = "mapMonitoringQuestions";
    public static final String SUMMARY_TOOL_SESSIONS = "summaryToolSessions";
    public static final String CURRENT_SESSION_NAME = "currentSessionName";
    public static final String SUMMARY_TOOL_SESSIONS_ID = "summaryToolSessionsId";
    public static final String MONITORED_CONTENT_ID = "monitoredContentId";
    public static final String EDITACTIVITY_EDITMODE = "editActivityEditMode";
    public static final String FORM_INDEX = "formIndex";
    public static final String LIST_MONITORED_ANSWERS_CONTAINER_DTO = "listMonitoredAnswersContainerDto";
    public static final String LIST_GROUPING_DATA = "listGroupingData";
    public static final String LEARNER_NAME = "learnerName";
    public static final String LIST_MONITORED_MARKS_CONTAINER_DTO = "listMonitoredMarksContainerDto";
    public static final String SESSION_DTOS = "sessionDtos";
    public static final String TIMEZONE = "timeZone";

    public static final String PREVIEW_ONLY = "previewOnly";

    public static final String MODE = "mode";
    public static final String LEARNING_MODE = "learningMode";
    public static final String EXPORT_USER_ID = "exportUserId";
    public static final String REFLECT = "reflect";
    
  //output definitions
    public static final String ATTR_ACTIVITY_EVALUATION = "activityEvaluation";
    public static final String ATTR_TOOL_OUTPUT_DEFINITIONS = "toolOutputDefinitions";
    public static final String OUTPUT_NAME_LEARNER_MARK = "learner.mark";
    public static final String OUTPUT_NAME_LEARNER_ALL_CORRECT = "learner.all.correct";

    /*
     * exception constants
     */
    public static final String USER_EXCEPTION_WRONG_FORMAT = "userExceptionWrongFormat";
    public static final String USER_EXCEPTION_INCOMPATIBLE_IDS = "userExceptionIncompatibleIds";
    public static final String USER_EXCEPTION_NUMBERFORMAT = "userExceptionNumberFormat";
    public static final String USER_EXCEPTION_CONTENT_DOESNOTEXIST = "userExceptionContentDoesNotExist";
    public static final String USER_EXCEPTION_TOOLSESSION_DOESNOTEXIST = "userExceptionToolSessionDoesNotExist";
    public static final String USER_EXCEPTION_TOOLCONTENT_DOESNOTEXIST = "userExceptionToolContentDoesNotExist";
    public static final String USER_EXCEPTION_LEARNER_REQUIRED = "userExceptionLearnerRequired";
    public static final String USER_EXCEPTION_CONTENTID_REQUIRED = "userExceptionContentIdRequired";
    public static final String USER_EXCEPTION_TOOLSESSIONID_REQUIRED = "userExceptionToolSessionIdRequired";
    public static final String USER_EXCEPTION_TOOLSESSIONID_INCONSISTENT = "userExceptionToolSessionIdInconsistent";
    public static final String USER_EXCEPTION_DEFAULTCONTENT_NOT_AVAILABLE = "userExceptionDefaultContentNotAvailable";
    public static final String USER_EXCEPTION_DEFAULTQUESTIONCONTENT_NOT_AVAILABLE = "userExceptionDefaultQuestionContentNotAvailable";
    public static final String USER_EXCEPTION_DEFAULTOPTIONSCONTENT_NOT_AVAILABLE = "userExceptionDefaultOptionsContentNotAvailable";
    public static final String USER_EXCEPTION_USERID_NOTAVAILABLE = "userExceptionUserIdNotAvailable";
    public static final String USER_EXCEPTION_USERID_NOTNUMERIC = "userExceptionUserIdNotNumeric";
    public static final String USER_EXCEPTION_ONLYCONTENT_ANDNOSESSIONS = "userExceptionOnlyContentAndNoSessions";
    public static final String USER_EXCEPTION_USERID_EXISTING = "userExceptionUserIdExisting";
    public static final String USER_EXCEPTION_USER_DOESNOTEXIST = "userExceptionUserDoesNotExist";
    public static final String USER_EXCEPTION_MONITORINGTAB_CONTENTID_REQUIRED = "userExceptionMonitoringTabContentIdRequired";
    public static final String USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP = "userExceptionDefaultContentNotSetup";
    public static final String USER_EXCEPTION_NO_TOOL_SESSIONS = "userExceptionNoToolSessions";
    public static final String USER_EXCEPTION_NO_STUDENT_ACTIVITY = "userExceptionNoStudentActivity";
    public static final String USER_EXCEPTION_MODE_REQUIRED = "userExceptionModeRequired";
    public static final String USER_EXCEPTION_CONTENT_IN_USE = "userExceptionContentInUse";
    public static final String USER_EXCEPTION_CONTENT_BEING_MODIFIED = "userExceptionContentBeingModified";
    public static final String USER_EXCEPTION_MODE_INVALID = "userExceptionModeInvalid";
    public static final String USER_EXCEPTION_QUESTION_EMPTY = "userExceptionQuestionEmpty";
    public static final String USER_EXCEPTION_ANSWER_EMPTY = "userExceptionAnswerEmpty";
    public static final String USER_EXCEPTION_WEIGHT_TOTAL = "userExceptionWeightTotal";
    public static final String USER_EXCEPTION_WEIGHT_EMPTY = "userExceptionWeightEmpty";
    public static final String USER_EXCEPTION_WEIGHT_NOTINTEGER = "userExceptionWeightNotInteger";
    public static final String USER_EXCEPTION_WEIGHT_ZERO = "userExceptionWeightZero";
    public static final String USER_EXCEPTION_ANSWERS_DUPLICATE = "userExceptionAnswersDuplicate";
    public static final String USER_EXCEPTION_OPTIONS_COUNT_ZERO = "userExceptionOptionsCountZero";
    public static final String USER_EXCEPTION_CHKBOXES_EMPTY = "userExceptionChkboxesEmpty";
    public static final String USER_EXCEPTION_SUBMIT_NONE = "userExceptionSubmitNone";
    public static final String USER_EXCEPTION_PASSMARK_NOTINTEGER = "userExceptionPassmarkNotInteger";
    public static final String USER_EXCEPTION_PASSMARK_EMPTY = "userExceptionPassmarkEmpty";
    public static final String USER_EXCEPTION_PASSMARK_GREATER100 = "userExceptionPassmarkGreater100";
    public static final String USER_EXCEPTION_WEIGHT_MUST_EQUAL100 = "userExceptionWeightMustEqual100";
    public static final String USER_EXCEPTION_SINGLE_OPTION = "userExceptionSingleOption";

    public static final String SUCCESS = "success";
    public static final String CANDIDATE_ANSWER_COUNT = "candidateAnswerCount";
    public static final String CANDIDATE_ANSWER_PREFIX = "candidateAnswer";
    public static final int QUESTION_DEFAULT_MARK = 1;
    public static final int CANDIDATE_ANSWER_DEFAULT_COUNT = 3;
}
