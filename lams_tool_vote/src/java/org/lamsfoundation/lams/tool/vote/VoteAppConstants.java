/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote;

/**
 * Defines constants used throughout the application
 *
 * @author Ozgur Demirtas
 *
 */
public interface VoteAppConstants {

    public static final String MY_SIGNATURE = "lavote11";
    public static final String TOOL_SERVICE = "tool_service";
    public static final String ERROR_VOTEAPPLICATION = "error.exception.VoteApplication";
    public static final String DEFINE_LATER = "defineLater";

    /*
     * Struts level constants
     */
    public static final String LOAD = "load";
    public static final String LOAD_QUESTIONS = "load";
    public static final String LOAD_NOMINATIONS = "loadNominations";
    public static final String LOAD_MONITORING = "refreshMonitoring";
    public static final String LOAD_STARTER = "starter";
    public static final String AUTHORING_STARTER = "starter";
    public static final String LEARNING_STARTER = "learningStarter";
    public static final String MONITORING_STARTER = "monitoringStarter";
    public static final String LOAD_LEARNER = "loadLearner";
    public static final String ALL_NOMINATIONS = "allNominations";
    public static final String LOAD_MONITORING_CONTENT = "loadMonitoring";
    public static final String MONITORING_STARTER_REDIRECT = "monitoringStarterRedirect";
    public static final String INDIVIDUAL_REPORT = "individualReport";
    public static final String EXIT_PAGE = "exitPage";
    public static final String VIEW_SUMMARY = "viewSummary";
    public static final String REDO_QUESTIONS = "redoQuestions";
    public static final String SINGLE_QUESTION_ANSWERS = "singleQuestionAnswers";
    public static final String RESULTS_SUMMARY = "resultsSummary";
    public static final String ERROR_LIST = "errorList";
    public static final String PREVIEW = "preview";
    public static final String LEARNER_PROGRESS = "learnerProgress";
    public static final String VOTE_NOMINATION_VIEWER = "voteNominationViewer";
    public static final String STATISTICS = "statistics";
    public static final String LEARNER_PROGRESS_USERID = "learnerProgressUserId";
    public static final String MIN_NOMINATION_COUNT = "minNominationCount";
    public static final String MAX_NOMINATION_COUNT = "maxNominationCount";
    public static final String ALLOW_TEXT = "allowText";
    public static final String USE_SELECT_LEADER_TOOL_OUTPUT = "useSelectLeaderToolOuput";
    public static final String ALLOW_TEXT_ENTRY = "allowTextEntry";
    public static final String SHOW_RESULTS = "showResults";
    public static final String LOCK_ON_FINISH = "lockOnFinish";
    public static final String REPORT_VIEW_ONLY = "reportViewOnly";
    public static final String USER_ENTRY = "userEntry";
    public static final String RESPONSE_ID = "responseId";
    public static final String CURRENT_UID = "currentUid";
    public static final String DEFAULT_VOTING_TITLE = "Voting";
    public static final String DEFAULT_VOTING_INSTRUCTIONS = "Instructions";
    public static final String UUID = "uuid";
    public static final String VOTE_STATS_DTO = "voteStatsDTO";
    public static final String LIST_ALL_GROUPS_DTO = "listAllGroupsDTO";
    public static final String CURRENT_SESSION_NAME = "currentSessionName";
    public static final String REVISITED_ALL_NOMINATIONS = "revisitedAllNominations";

    public static final String ACTIVITY_TITLE_KEY = "activityTitleKey";
    public static final String ACTIVITY_INSTRUCTIONS_KEY = "activityInstructionsKey";
    public static final String VOTE_GENERAL_MONITORING_DTO = "voteGeneralMonitoringDTO";
    public static final String LEARNER_NOTEBOOK = "learnerNotebook";

    public static final String AUTHORING = "authoring";
    public static final String REMOVABLE_OPTION_INDEX = "removableOptionIndex";
    public static final String AUTHORING_CANCELLED = "authoringCancelled";
    public static final String REQUEST_LEARNING_REPORT = "requestLearningReport";
    public static final String REQUEST_LEARNING_REPORT_PROGRESS = "requestLearningReportProgress";
    public static final String VOTE_GENERAL_AUTHORING_DTO = "voteGeneralAuthoringDTO";
    public static final String HTTP_SESSION_ID = "httpSessionID";
    public static final String LIST_QUESTION_DTO = "listQuestionDTO";
    public static final String LIST_DELETED_QUESTION_DTOS = "deletedQuestionDTOs";

    public static final String MAX_INPUTS = "maxInputs";

    public static final String NOTEBOOK = "notebook";
    public static final String ENTRY_TEXT = "entryText";
    public static final String REFLECTION_SUBJECT = "reflectionSubject";
    public static final String REFLECTIONS_CONTAINER_DTO = "reflectionsContainerDTO";

    /*
     * authoring mode controllers
     */
    public static final String IS_ADD_QUESTION = "isAddQuestion";
    public static final String IS_REMOVE_QUESTION = "isRemoveQuestion";
    public static final String IS_REMOVE_CONTENT = "isRemoveContent";
    public static final String IS_REVISITING_USER = "isRevisitingUser";
    public static final String REVISITING_USER = "revisitingUser";

    public static final String USER = "user";
    public static final String TOOL_CONTENT_ID = "toolContentID";
    public static final String TOOL_CONTENT_UID = "toolContentUID";
    public static final String TOOL_SESSION_ID = "toolSessionID";
    public static final String ATTR_CONTENT = "content";
    public static final String USER_ID = "userID";
    public static final String COPY_TOOL_CONTENT = "copyToolContent";
    public static final String REMOVE_TOOL_CONTENT = "removeToolContent";
    public static final String OPT_INDEX = "optIndex";

    public static final String ATTR_SUBMISSION_DEADLINE = "submissionDeadline";
    public static final String ATTR_SUBMISSION_DEADLINE_DATESTRING = "submissionDateString";
    public static final String ATTR_IS_SUBMISSION_DEADLINE_PASSED = "isSubmissionDeadlinePassed";
    public static final String ATTR_GROUP_USERS = "groupUsers";
    public static final String ATTR_IS_USER_LEADER = "isUserLeader";
    public static final String ATTR_GROUP_LEADER_NAME = "groupLeaderName";

    /*
     * refers to number of questions presented initially, we have a single record for default content
     */
    public static final Long INITIAL_QUESTION_COUNT = new Long(1);
    public static final long MAX_QUESTION_COUNT = 20;
    public static final long MAX_OPTION_COUNT = 10;
    public static final int MAX_ATTEMPT_HISTORY = 30;

    /*
     * authoring mode constants
     */
    public static final String IS_DEFINE_LATER = "isDefineLater";
    public static final String MAP_QUESTIONS_CONTENT = "mapQuestionsContent";
    public static final String MAP_QUESTION_CONTENT = "mapQuestionContent";
    public static final String MAP_VOTERATES_CONTENT = "mapVoteRatesContent";
    public static final String MAP_STANDARD_USER_COUNT = "mapStandardUserCount";
    public static final String MAP_STANDARD_NOMINATIONS_VISIBILITY = "mapStandardNominationsVisibility";
    public static final String MAP_STANDARD_NOMINATIONS_HTMLED_CONTENT = "mapStandardNominationsHTMLedContent";
    public static final String MAP_DISABLED_QUESTIONS = "mapDisabledQuestions";
    public static final String MAP_GENERAL_OPTIONS_CONTENT = "mapGeneralOptionsContent";
    public static final String MAP_GENERAL_SELECTED_OPTIONS_CONTENT = "mapGeneralSelectedOptionsContent";
    public static final String MAP_STARTUP_GENERAL_OPTIONS_CONTENT = "mapStartupGeneralOptionsContent";
    public static final String MAP_STARTUP_GENERAL_SELECTED_OPTIONS_CONTENT = "mapStartupGeneralSelectedOptionsContent";
    public static final String LIST_GENERAL_CHECKED_OPTIONS_CONTENT = "listGeneralCheckedOptionsContent";
    public static final String MAP_STUDENTS_VOTED = "mapStudentsVoted";
    public static final String LIST_USER_ENTRIES_CONTENT = "listUserEntriesContent";

    public static final String QUESTIONS_WITHNO_OPTIONS = "questionsWithNoOptions";
    public static final String VOTE_GENERAL_LEARNER_FLOW_DTO = "voteGeneralLearnerFlowDTO";

    public static final String MAP_GENERAL_CHECKED_OPTIONS_CONTENT = "mapGeneralCheckedOptionsContent";
    public static final String MAP_LEARNER_QUESTIONS_CONTENT = "mapLearnerQuestionsContent";
    public static final String MAP_LEARNER_CHECKED_OPTIONS_CONTENT = "mapLearnerCheckedOptionsContent";
    public static final String MAP_VIEWONLY_QUESTION_CONTENT_LEARNER = "mapViewOnlyQuestionContentLearner";
    public static final String MAP_QUE_ATTEMPTS = "mapQueAttempts";
    public static final String LIST_USER_ENTRIES = "listUserEntries";

    public static final String SELECTED_QUESTION = "selectedQuestion";
    public static final String SELECTED_QUESTION_INDEX = "selectedQuestionIndex";

    public static final String TITLE = "title";
    public static final String INSTRUCTIONS = "instructions";
    public static final String CREATION_DATE = "creationDate";
    public static final String VIEW_ANSWERS = "viewAnswers";
    public static final String WAIT_FOR_LEADER = "waitForLeader";

    public static final String REFLECT = "reflect";
    public static final String ON = "ON";
    public static final String OFF = "OFF";
    public static final String RICHTEXT_TITLE = "richTextTitle";
    public static final String RICHTEXT_INSTRUCTIONS = "richTextInstructions";
    public static final String RICHTEXT_BLANK = "<p>&nbsp;</p>";

    public static final String COUNT_SESSION_COMPLETE = "countSessionComplete";
    public static final String COUNT_ALL_USERS = "countAllUsers";
    public static final String COUNT_MAX_ATTEMPT = "countMaxAttempt";

    /*
     * the learner or monitoring environment provides toolSessionId
     */
    public final long ONE_DAY = 60 * 60 * 1000 * 24;
    public final static String NOT_ATTEMPTED = "NOT_ATTEMPTED";
    public final static String INCOMPLETE = "INCOMPLETE";
    public static final String COMPLETED = "COMPLETED";

    public static final String MAP_TOOL_SESSIONS = "mapToolSessions";
    public static final String OPTION_OFF = "false";
    public static final String ACTIVITY_TITLE = "activityTitle";
    public static final String ACTIVITY_INSTRUCTIONS = "activityInstructions";
    public static final String REMOVABLE_QUESTION_INDEX = "removableQuestionIndex";

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
    public static final String CURRENT_QUESTION_INDEX = "currentQuestionIndex";
    public static final String MAP_ANSWERS = "mapAnswers";
    public static final String CURRENT_ANSWER = "currentAnswer";
    public static final String REPORT_TITLE_LEARNER = "reportTitleLearner";

    public static final String IS_CONTENT_IN_USE = "isContentInUse";
    public static final String IS_MONITORED_CONTENT_IN_USE = "isMonitoredContentInUse";
    public static final String IS_ALL_SESSIONS_COMPLETED = "isAllSessionsCompleted";
    public static final String CHECK_ALL_SESSIONS_COMPLETED = "checkAllSessionsCompleted";
    public static final String FROM_TOOL_CONTENT_ID = "fromToolContentId";
    public static final String TO_TOOL_CONTENT_ID = "toToolContentId";
    public static final String MAP_USER_RESPONSES = "mapUserResponses";
    public static final String MAP_MAIN_REPORT = "mapMainReport";
    public static final String MAP_STATS = "mapStats";

    /*
     * Monitoring Mode constants
     */
    public static final String ATTR_IS_GROUPED_ACTIVITY = "isGroupedActivity";
    public static final String REPORT_TITLE_MONITOR = "reportTitleMonitor";
    public static final String MONITOR_USER_ID = "userId";
    public static final String MONITORING_REPORT = "monitoringReport";
    public static final String MONITORING_ERROR = "monitoringError";
    public static final String MAP_MONITORING_QUESTIONS = "mapMonitoringQuestions";
    public static final String MONITORED_CONTENT_ID = "monitoredContentId";
    public static final String VALIDATION_ERROR = "validationError";
    public static final String FORM_INDEX = "formIndex";
//    public static final String LIST_MONITORED_ANSWERS_CONTAINER_DTO = "listMonitoredAnswersContainerDto";
    public static final String TIMEZONE = "timeZone";
    public static final String PREVIEW_ONLY = "previewOnly";

    public static final String MODE = "mode";
    public static final String LEARNING_MODE = "learningMode";
    public static final String SESSION_VOTES_CHART = "Session Votes Chart";

    public static final String ATTR_QUESTION_UID = "questionUid";
    public static final String ATTR_SESSION_UID = "sessionUid"; // not the tool session id supplied by the core
    public static final String ATTR_USER_NAME = "userName";
    public static final String ATTR_ATTEMPT_TIME = "attemptTime";
    public static final String ATTR_ATTEMPT_TIME_TIMEAGO = "attemptTimeTimeago";

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
    public static final String USER_EXCEPTION_ANSWERS_DUPLICATE = "userExceptionAnswersDuplicate";
    public static final String USER_EXCEPTION_OPTIONS_COUNT_ZERO = "userExceptionOptionsCountZero";
    public static final String USER_EXCEPTION_CHKBOXES_EMPTY = "userExceptionChkboxesEmpty";
    public static final String USER_EXCEPTION_SUBMIT_NONE = "userExceptionSubmitNone";
    public static final String USER_EXCEPTION_WEIGHT_MUST_EQUAL100 = "userExceptionWeightMustEqual100";
    public static final String USER_EXCEPTION_SINGLE_OPTION = "userExceptionSingleOption";
    public static final String USER_EXCEPTION_OPTIONS_DUPLICATE = "userExceptionOptionsDuplicate";
    public static final String USER_EXCEPTION_MAXNOMINATION_INVALID = "userExceptionMaxNominationInvalid";
    public static final String SUCCESS = "success";

    public static final Integer DATA_FLOW_OBJECT_ASSIGMENT_ID = 0;

    // Leader selection tool Constants
    public static final String LEADER_SELECTION_TOOL_SIGNATURE = "lalead11";
    public static final String LEADER_SELECTION_TOOL_OUTPUT_NAME_LEADER_USERID = "leader.user.id";

    public static final int SORT_BY_DEFAULT = 0;
    public static final int SORT_BY_NAME_ASC = 1;
    public static final int SORT_BY_NAME_DESC = 2;
    public static final int SORT_BY_DATE_ASC = 3;
    public static final int SORT_BY_DATE_DESC = 4;
    public static final int SORT_BY_ENTRY_ASC = 5;
    public static final int SORT_BY_ENTRY_DESC = 6;
    public static final int SORT_BY_VISIBLE_ASC = 7;
    public static final int SORT_BY_VISIBLE_DESC = 8;

}
