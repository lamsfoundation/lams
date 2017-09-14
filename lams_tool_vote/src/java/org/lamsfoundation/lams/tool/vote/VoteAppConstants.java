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
    public static final String DEFINE_LATER = "defineLater";

    /*
     * Struts level constants
     */
    public static final String LOAD_QUESTIONS = "load";
    public static final String LOAD_NOMINATIONS = "loadNominations";
    public static final String LOAD_MONITORING = "refreshMonitoring";
    public static final String LEARNING_STARTER = "learningStarter";
    public static final String LOAD_LEARNER = "loadLearner";
    public static final String ALL_NOMINATIONS = "allNominations";
    public static final String INDIVIDUAL_REPORT = "individualReport";
    public static final String EXIT_PAGE = "exitPage";
    public static final String ERROR_LIST = "errorList";
    public static final String VOTE_NOMINATION_VIEWER = "voteNominationViewer";
    public static final String STATISTICS = "statistics";
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
    public static final String VOTE_STATS_DTO = "voteStatsDTO";
    public static final String REVISITED_ALL_NOMINATIONS = "revisitedAllNominations";

    public static final String ACTIVITY_TITLE_KEY = "activityTitleKey";
    public static final String ACTIVITY_INSTRUCTIONS_KEY = "activityInstructionsKey";
    public static final String VOTE_GENERAL_MONITORING_DTO = "voteGeneralMonitoringDTO";
    public static final String LEARNER_NOTEBOOK = "learnerNotebook";

    public static final String VOTE_GENERAL_AUTHORING_DTO = "voteGeneralAuthoringDTO";
    public static final String HTTP_SESSION_ID = "httpSessionID";
    public static final String LIST_QUESTION_DTO = "listQuestionDTO";
    public static final String LIST_DELETED_QUESTION_DTOS = "deletedQuestionDTOs";

    public static final String MAX_INPUTS = "maxInputs";

    public static final String NOTEBOOK = "notebook";
    public static final String ENTRY_TEXT = "entryText";

    /*
     * authoring mode controllers
     */
    public static final String REVISITING_USER = "revisitingUser";

    public static final String TOOL_CONTENT_ID = "toolContentID";
    public static final String TOOL_CONTENT_UID = "toolContentUID";
    public static final String TOOL_SESSION_ID = "toolSessionID";
    public static final String ATTR_CONTENT = "content";
    public static final String USER_ID = "userID";

    public static final String ATTR_SUBMISSION_DEADLINE = "submissionDeadline";
    public static final String ATTR_SUBMISSION_DEADLINE_DATESTRING = "submissionDateString";
    public static final String ATTR_GROUP_USERS = "groupUsers";
    public static final String ATTR_GROUP_LEADER_NAME = "groupLeaderName";
    public static final String ATTR_GROUP_LEADER_USER_ID  = "groupLeaderUserId";
    /*
     * refers to number of questions presented initially, we have a single record for default content
     */
    public static final long MAX_OPTION_COUNT = 10;

    /*
     * authoring mode constants
     */
    public static final String LIST_GENERAL_CHECKED_OPTIONS_CONTENT = "listGeneralCheckedOptionsContent";
    public static final String LIST_USER_ENTRIES_CONTENT = "listUserEntriesContent";

    public static final String VOTE_GENERAL_LEARNER_FLOW_DTO = "voteGeneralLearnerFlowDTO";

    public static final String MAP_GENERAL_CHECKED_OPTIONS_CONTENT = "mapGeneralCheckedOptionsContent";

    public static final String TITLE = "title";
    public static final String INSTRUCTIONS = "instructions";
    public static final String VIEW_ANSWERS = "viewAnswers";
    public static final String WAIT_FOR_LEADER = "waitForLeader";

    public static final String REFLECT = "reflect";

    /*
     * the learner or monitoring environment provides toolSessionId
     */
    public static final String COMPLETED = "COMPLETED";

    /*
     * user actions
     */

    /* learner mode contants */
    public static final String MAP_QUESTION_CONTENT_LEARNER = "mapQuestionContentLearner";

    /*
     * Monitoring Mode constants
     */
    public static final String PREVIEW_ONLY = "previewOnly";

    public static final String MODE = "mode";

    public static final String ATTR_QUESTION_UID = "questionUid";
    public static final String ATTR_SESSION_UID = "sessionUid"; // not the tool session id supplied by the core
    public static final String ATTR_USER_NAME = "userName";
    public static final String ATTR_USER_ID = "userId";
    public static final String ATTR_PORTRAIT_ID = "portraitId";
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
    public static final String SUCCESS = "success";

    public static final Integer DATA_FLOW_OBJECT_ASSIGMENT_ID = 0;

    // Leader selection tool Constants
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
