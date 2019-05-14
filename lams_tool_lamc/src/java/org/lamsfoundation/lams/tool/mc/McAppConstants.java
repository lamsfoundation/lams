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

    public static final String TOOL_SIGNATURE = "lamc11";

    public static final String LOAD_AUTHORING = "authoringTabsHolder";
    public static final String LEARNING_STARTER = "learningStarter";
    public static final String LOAD_LEARNER = "loadLearner";
    public static final String LOAD_MONITORING_CONTENT = "loadMonitoring";
    public static final String ERROR_LIST = "errorList";
    public static final String QUESTION_DTOS = "questionDtos";
    public static final String LIST_DELETED_QUESTION_DTOS = "deletedQuestionDTOs";
    public static final String MC_GENERAL_MONITORING_DTO = "mcGeneralMonitoringDTO";
    public static final String QUESTION_DTO = "questionDto";

    public static final String REFLECTION_SUBJECT = "reflectionSubject";
    public static final String REFLECTIONS_CONTAINER_DTO = "reflectionsContainerDTO";
    public static final String LEARNER_NOTEBOOK = "learnerNotebook";
    public static final String USER_MASTER_DETAIL = "userMasterDetail";
    public static final String STATISTICS = "statistic";

    public static final String TOOL_CONTENT_ID = "toolContentID";
    public static final String TOOL_SESSION_ID = "toolSessionID";

    public static final int MAX_OPTION_COUNT = 25;
    public static final String QUESTION_AND_CANDIDATE_ANSWERS_KEY = "questionAndCandidateAnswersKey";
    public static final String CONFIDENCE_LEVELS_KEY = "confidenceLevelsMap";

    /*
     * authoring mode constants
     */

    public static final String TITLE = "title";
    public static final String INSTRUCTIONS = "instructions";
    public static final String WAIT_FOR_LEADER = "waitForLeader";
    public static final String RETRIES = "retries";
    public static final String PASSMARK = "passMark";
    public static final String VIEW_ANSWERS = "viewAnswers";
    public static final String ATTR_SESSION_MAP_ID = "sessionMapId";

    /* Date time restriction */
    public static final String ATTR_SUBMISSION_DEADLINE = "submissionDeadline";
    public static final String ATTR_SUBMISSION_DEADLINE_DATESTRING = "submissionDateString";
    public static final String ATTR_IS_SUBMISSION_DEADLINE_PASSED = "isSubmissionDeadlinePassed";
    public static final String ATTR_GROUP_USERS = "groupUsers";
    public static final String ATTR_IS_USER_LEADER = "isUserLeader";
    public static final String ATTR_GROUP_LEADER = "groupLeader";

    /*
     * the learner or monitoring environment provides toolSessionId
     */
    public static final String COMPLETED = "COMPLETED";
    public static final String OPTION_OFF = "false";
    public static final String CORRECT = "Correct";

    /* learner mode contants */
    public static final String LEARNER_ANSWER_DTOS = "learnerAnswerDtos";
    public static final String MC_GENERAL_LEARNER_FLOW_DTO = "mcGeneralLearnerFlowDTO";
    public static final String NOTEBOOK = "notebook";
    public static final String ENTRY_TEXT = "entryText";

    /*
     * Monitoring Mode constants
     */
    public static final String ATTR_CONTENT = "content";
    public static final String USER_UID = "userUid";
    public static final String USER_ATTEMPTS = "userAttempts";
    public static final String PARAM_NOT_A_NUMBER = "nan";
    public static final String PARAM_GRADE = "grade";
    public static final String PARAM_USER_ATTEMPT_UID = "userAttemptUid";
    public static final String SESSION_DTOS = "sessionDtos";

    public static final String MODE = "mode";
    public static final String REFLECT = "reflect";
    
  //output definitions
    public static final String ATTR_ACTIVITY_EVALUATION = "activityEvaluation";
    public static final String ATTR_TOOL_OUTPUT_DEFINITIONS = "toolOutputDefinitions";
    public static final String OUTPUT_NAME_LEARNER_MARK = "learner.mark";
    public static final String OUTPUT_NAME_LEARNER_ALL_CORRECT = "learner.all.correct";

    public static final String SUCCESS = "success";
    public static final String CANDIDATE_ANSWER_COUNT = "candidateAnswerCount";
    public static final String CANDIDATE_ANSWER_PREFIX = "candidateAnswer";
    public static final int QUESTION_DEFAULT_MARK = 1;
    public static final int CANDIDATE_ANSWER_DEFAULT_COUNT = 3;
}
